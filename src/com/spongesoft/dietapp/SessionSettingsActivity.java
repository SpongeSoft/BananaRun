package com.spongesoft.dietapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.NumberPicker;

public class SessionSettingsActivity extends Activity{

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.session_settings);
        
        //Number picker parameters 
        NumberPicker np = (NumberPicker) findViewById(R.id.limit_picker);
        np.setMaxValue(300);
        np.setMinValue(1);
        
    }
	
	
}
