package com.example.sixsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;

import com.example.sixsapp.R;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial mSwitch;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mSwitch = findViewById(R.id.clearCache_switch);

        if (!sharedPreferences.contains("auto_cache_clear")) {
            // Set the default value on the first run
            sharedPreferences.edit().putBoolean("auto_cache_clear", true).apply();
        }

        boolean isAutoCacheClearEnabled = sharedPreferences.getBoolean("auto_cache_clear", true);

        mSwitch.setChecked(isAutoCacheClearEnabled);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean("auto_cache_clear", isChecked).apply();
            }
        });
    }
}