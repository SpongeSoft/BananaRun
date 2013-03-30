package com.spongesoft.bananarun;

import com.spongesoft.dietapp.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
	
public class SessionActivity extends Activity {
	
	
	Button startBtn;
	Button stopBtn;
	Chronometer chronometer;
	long chronometerCounter;
	int state = 0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.new_session);
        
        startBtn = (Button) findViewById(R.id.btn_enter);
        stopBtn = (Button) findViewById(R.id.btn_stop);
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/bradbunr.ttf");
        System.out.println(getAssets().toString());
        chronometer.setTypeface(font);
        chronometer.setTextColor(getResources().getColor(R.color.text_colour));
        
        startBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(state == 0) {
					chronometer.setBase(SystemClock.elapsedRealtime());
					chronometer.start();
					state = 1;
					startBtn.setText("Pause");
				}
				
				if(state == 1) {
					chronometerCounter = SystemClock.elapsedRealtime();
					chronometer.stop();
					state = 2;
					startBtn.setText("Continue");
				} else {
					chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - chronometerCounter);
					chronometer.start();
					state = 1;
					startBtn.setText("Pause");
				}
			}
		});
        
        stopBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				chronometer.stop();
				startBtn.setText("Start");
				state = 0;	
			}
        	
        });
        
        
        
        
        
	}
	
	

}
