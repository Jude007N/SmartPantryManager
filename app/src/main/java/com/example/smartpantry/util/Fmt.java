package com.example.smartpantry.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/** Small formatting and date helpers shared by the screens. */
public final class Fmt {
    private Fmt() {}

    /** 3.0 -> "3", 2.5 -> "2.5" */
    public static String qty(double q) {
        if (q == Math.rint(q)) return String.valueOf((long) q);
        String s = String.format(Locale.US, "%.2f", q);
        return s.replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    /** Whole days from today until the yyyy-MM-dd date, or null if empty/invalid. Negative means expired. */
    public static Long daysUntil(String expiry) {
        if (expiry == null || expiry.isEmpty()) return null;
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(expiry);
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            return Math.round((d.getTime() - today.getTimeInMillis()) / 86400000.0);
        } catch (ParseException e) {
            return null;
        }
    }
}
