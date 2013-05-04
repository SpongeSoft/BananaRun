package com.spongesoft.bananarun;

import com.spongesoft.dietapp.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;


public class SessionSettingsActivity extends Activity {
	
	private final int KILOMETERS = 0;
	private final int MINUTES = 1;
	private final int FREERUN = 2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.session_settings);

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		setPickerType(prefs, FREERUN);
		// Number picker parameters
		final NumberPicker np = (NumberPicker) findViewById(R.id.limit_picker);
		np.setMaxValue(300);
		np.setMinValue(1);
		np.setValue(0);

		ImageView goButton = (ImageView) findViewById(R.id.startsessionbutton);

		final RadioButton r1 = (RadioButton) findViewById(R.id.time_limit);
		final RadioButton r2 = (RadioButton) findViewById(R.id.distance_limit);
		final RadioButton r3 = (RadioButton) findViewById(R.id.free_running);

		r1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setPickerType(prefs, MINUTES);
				np.setVisibility(View.VISIBLE);
				
			}
		});

		r2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setPickerType(prefs, KILOMETERS);
				np.setVisibility(View.VISIBLE);
			}
		});

		r3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setPickerType(prefs, FREERUN);
				np.setVisibility(View.INVISIBLE);
			}
		});

		goButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int pickerVal = np.getValue();
				if(pickerVal != 0){
					updatePrefsLimit(prefs, pickerVal);
				}
				Intent startSession = new Intent(SessionSettingsActivity.this,
						SessionActivity.class);
				startActivity(startSession);
				// finishActivity(MainActivity);
			}
		});

	}// oncreate

	public void onBackPressed() {
		super.onBackPressed();

		Intent newSession = new Intent(SessionSettingsActivity.this,
				MainActivity.class);
		startActivity(newSession);
		finish();

	}
	
	public void updatePrefsLimit(SharedPreferences prefs, int value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("pickerValue", value);
		Log.d("updatePrefsLimit", "Picker value is: "+value);
		editor.commit();
	}
	
	public void setPickerType(SharedPreferences prefs, int type) {
		SharedPreferences.Editor editor = prefs.edit();
		if(type == KILOMETERS){
			editor.putInt("pickerType", KILOMETERS);
			Log.d("updatePrefsLimit", "Picker Type is: KILOMETERS");
		} else if(type == MINUTES) {
			editor.putInt("pickerType", MINUTES);
			Log.d("updatePrefsLimit", "Picker Type is: MINUTES");
		} else {
			editor.putInt("pickerType", FREERUN);
			Log.d("updatePrefsLimit", "Picker Type is: FREERUN");
		}
		editor.commit();
	}

}
