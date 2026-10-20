package com.example.smartpantry.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.util.Constants;
import com.example.smartpantry.util.NavHelper;
import com.google.android.material.switchmaterial.SwitchMaterial;

/** Screen 5: settings. Stores the expiring-soon toggle in SharedPreferences. */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.title_settings);

        SwitchMaterial sw = findViewById(R.id.switch_expiry);
        sw.setChecked(getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.KEY_EXPIRY_ALERTS, true));
        sw.setOnCheckedChangeListener((button, checked) ->
                getSharedPreferences(Constants.PREFS, MODE_PRIVATE).edit().putBoolean(Constants.KEY_EXPIRY_ALERTS, checked).apply());

        NavHelper.setup(this, R.id.nav_settings);
    }
}
