package com.spongesoft.bananarun;

import com.spongesoft.dietapp.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;

public class SessionSettingsActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.session_settings);
        
        //Number picker parameters 
        final NumberPicker np = (NumberPicker) findViewById(R.id.limit_picker);
        np.setMaxValue(300);
        np.setMinValue(1);


        final RadioButton r1 = (RadioButton) findViewById(R.id.time_limit);
        final RadioButton r2 = (RadioButton) findViewById(R.id.distance_limit);
        final RadioButton r3 = (RadioButton) findViewById(R.id.free_running);
        
        r1.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        			np.setVisibility(View.VISIBLE);
        	}	
        });
        
        r2.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
            	   np.setVisibility(View.VISIBLE);
        	}
        });
        
        r3.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
            	   np.setVisibility(View.INVISIBLE);
        	}
        });
        
        
        
    }
	
	
}
