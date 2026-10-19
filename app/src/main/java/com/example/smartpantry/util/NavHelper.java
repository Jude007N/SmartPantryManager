package com.example.smartpantry.util;

import android.app.Activity;
import android.content.Intent;

import com.example.smartpantry.R;
import com.example.smartpantry.ui.PantryListActivity;
import com.example.smartpantry.ui.SettingsActivity;
import com.example.smartpantry.ui.SuggestedRecipesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Wires the bottom navigation bar used by the three top-level screens. */
public final class NavHelper {
    private NavHelper() {}

    public static void setup(Activity activity, int selectedId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottom_nav);
        nav.setSelectedItemId(selectedId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId) return true;
            Class<?> target;
            if (id == R.id.nav_pantry) target = PantryListActivity.class;
            else if (id == R.id.nav_recipes) target = SuggestedRecipesActivity.class;
            else target = SettingsActivity.class;
            activity.startActivity(new Intent(activity, target));
            activity.finish();
            activity.overridePendingTransition(0, 0);
            return true;
        });
    }
}
