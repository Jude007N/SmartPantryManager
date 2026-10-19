package com.example.smartpantry.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/** Normalises names and units so "Tomatoes" and "tomato", or "1 kg" and "1000 g", compare equal. */
public final class IngredientMatcher {

    public enum Dimension { MASS, VOLUME, COUNT }

    // Words that look plural but are not, so we must not strip the "s"
    private static final Set<String> NO_SINGULAR = new HashSet<>(
            Arrays.asList("asparagus", "couscous", "hummus", "molasses", "swiss", "citrus"));

    private IngredientMatcher() {}

    /** Lower-cases, trims, collapses spaces and singularises the last word. */
    public static String normalizeName(String raw) {
        if (raw == null) return "";
        String s = raw.toLowerCase(Locale.ROOT).replaceAll("[^a-z \\-]", " ").replaceAll("\\s+", " ").trim();
        if (s.isEmpty()) return s;
        int cut = s.lastIndexOf(' ');
        String head = cut >= 0 ? s.substring(0, cut + 1) : "";
        return head + singular(s.substring(cut + 1));
    }

    /** Very small rule based singulariser (no NLP needed for this assignment). */
    static String singular(String w) {
        if (w.length() <= 3 || NO_SINGULAR.contains(w)) return w;
        if (w.endsWith("ies")) return w.substring(0, w.length() - 3) + "y";          // berries -> berry
        if (w.endsWith("oes") || w.endsWith("ches") || w.endsWith("shes")
                || w.endsWith("xes") || w.endsWith("sses"))
            return w.substring(0, w.length() - 2);                                    // tomatoes -> tomato
        if (w.endsWith("ss") || w.endsWith("us") || w.endsWith("is")) return w;
        if (w.endsWith("s")) return w.substring(0, w.length() - 1);                   // eggs -> egg
        return w;
    }

    private static String cleanUnit(String unit) {
        return unit == null ? "" : unit.toLowerCase(Locale.ROOT).trim().replace(".", "");
    }

    /** Which kind of quantity a unit measures. Unknown units are treated as counts. */
    public static Dimension dimensionOf(String unit) {
        switch (cleanUnit(unit)) {
            case "g": case "gram": case "grams": case "kg": case "kilogram": case "kilograms":
                return Dimension.MASS;
            case "ml": case "l": case "litre": case "litres": case "liter": case "liters":
            case "tsp": case "teaspoon": case "teaspoons": case "tbsp": case "tablespoon":
            case "tablespoons": case "cup": case "cups":
                return Dimension.VOLUME;
            default:
                return Dimension.COUNT;
        }
    }

    /** Converts to the base unit: grams, millilitres, or plain count. */
    public static double toBase(double qty, String unit) {
        switch (cleanUnit(unit)) {
            case "kg": case "kilogram": case "kilograms": return qty * 1000;
            case "l": case "litre": case "litres": case "liter": case "liters": return qty * 1000;
            case "tsp": case "teaspoon": case "teaspoons": return qty * 5;
            case "tbsp": case "tablespoon": case "tablespoons": return qty * 15;
            case "cup": case "cups": return qty * 250;
            default: return qty; // g, ml, pcs
        }
    }

    /** Key used to group quantities: normalised name plus measurement dimension. */
    public static String key(String name, String unit) {
        return normalizeName(name) + "|" + dimensionOf(unit);
    }
}
