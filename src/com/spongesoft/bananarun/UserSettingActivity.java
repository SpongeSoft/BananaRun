package com.spongesoft.bananarun;
 
import com.spongesoft.dietapp.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
 
public class UserSettingActivity extends PreferenceActivity {
 
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        /* Load preferences */
        addPreferencesFromResource(R.xml.settings);
 
    }
}