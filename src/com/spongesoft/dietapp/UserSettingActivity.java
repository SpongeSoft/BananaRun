package com.spongesoft.dietapp;
 
import android.os.Bundle;
import android.preference.PreferenceActivity;
 
public class UserSettingActivity extends PreferenceActivity {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        /* Load preferences */
        addPreferencesFromResource(R.xml.settings);
 
    }
}