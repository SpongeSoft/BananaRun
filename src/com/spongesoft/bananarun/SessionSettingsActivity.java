package com.spongesoft.bananarun;

import java.util.Random;

import com.spongesoft.bananarun.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class SessionSettingsActivity extends Activity {
	
	/* Session mode constants */
	private final int MINUTES = 0;
	private final int KILOMETERS = 1;
	private final int FREERUN = 2;
	
	/* Screen elements */
	ImageView goButton;
	RadioButton r1;
	RadioButton r2;
	RadioButton r3;
	//TextView messageTitle;
	TextView message;
	TextView gpsMsg;

	/* Preferences and Picker */
	SharedPreferences prefs;
	NumberPicker np;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.session_settings);

		/* Set the screen's default configuration: free run mode. */
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		setPickerType(prefs, FREERUN);
		
		/* Reference screen components from XML file */
		goButton = (ImageView) findViewById(R.id.startsessionbutton);

		r1 = (RadioButton) findViewById(R.id.time_limit);
		r2 = (RadioButton) findViewById(R.id.distance_limit);
		r3 = (RadioButton) findViewById(R.id.free_running);

		gpsMsg = (TextView) findViewById(R.id.gpsMessage);
		message = (TextView) findViewById(R.id.message);
		generateMessage(message); //Retrieve random message
		
		
		/* Set font to TextView components */
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/bradbunr.ttf");
		//messageTitle.setTypeface(font);
		message.setTypeface(font);
		r1.setTypeface(font);
		r2.setTypeface(font);
		r3.setTypeface(font);
		gpsMsg.setTypeface(font);
		
		np = (NumberPicker) findViewById(R.id.limit_picker);

		/* Setup picker parameters. This picker will be used by the user
		 * to choose a limit, either distance or time. */
		np.setMaxValue(300); //Max value
		np.setMinValue(1); //Min value

		/* First radio button. When selected, the session limits are set to
		 * a certain TIME amount (in minutes), whose value is defined by the
		 * picker's value. */
		r1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setPickerType(prefs, MINUTES); //Update preferences
				np.setVisibility(View.VISIBLE); //Show picker
				
			}
		});

		/* Second radio button. When selected, the session limits are set to
		 * a certain DISTANCE amount (in kilometers/miles), whose value is defined 
		 * by the picker's value. */
		r2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setPickerType(prefs, KILOMETERS); //Update preferences
				np.setVisibility(View.VISIBLE); //Show picker
			}
		});

		/* Second radio button. When selected, the session has no time nor distance
		 * limits set. Therefore, the picker is not shown. */
		r3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setPickerType(prefs, FREERUN); //Update preferences
				np.setVisibility(View.INVISIBLE); // Hide picker
			}
		});

		
		/* Button to start the actual session. When pressed, the application 
		 * starts a new Activity. The picker's value is saved before. */
		goButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				/* Check Internet status */
				//final ConnectivityManager con = (ConnectivityManager)getSystemService(
				//				getBaseContext().LOCATION_SERVICE);
				final LocationManager man = (LocationManager)getSystemService(
										getBaseContext().LOCATION_SERVICE);
				
				if(!man.isProviderEnabled(LocationManager.GPS_PROVIDER)){
					Toast.makeText(getBaseContext(),getResources().getString(R.string.GPSerror),Toast.LENGTH_SHORT).show();
				} else {
					
					
				int pickerVal = np.getValue(); //Store picker value
				updatePrefsLimit(prefs, pickerVal); //Into preferences
				
				/* Create the new race */
				
				DBManagement manager = new DBManagement(v.getContext());
				manager.open();
				
				int race = (int) manager.setRace();
				
				manager.close();
				
				/* Set LastID  */
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("lastRaceID", race);
				
				editor.commit();
				
				/* Create an intent and start the new Activity */
				Intent startSession = new Intent(SessionSettingsActivity.this,
						SessionActivity.class);
				startSession.putExtra("race_id", race);
				startActivity(startSession);

				Intent serviceIntent = new Intent(getBaseContext(), LocationService.class);
				serviceIntent.putExtra("race_id", race);
        		getBaseContext().startService(serviceIntent);
        		
        		finish();
				// finishActivity(MainActivity);
				}
			}
		});

	}// oncreate

	/* When returning to the previous Activity, finish this one */
	public void onBackPressed() {
		super.onBackPressed();

		Intent newSession = new Intent(SessionSettingsActivity.this,
				MainActivity.class);
		startActivity(newSession);
		finish();

	}
	
	/* This function inserts a certain value into the application's preferences.
	 * The value corresponds to the picker's number that the user selected */
	public void updatePrefsLimit(SharedPreferences prefs, int value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("pickerValue", value);
		Log.d("updatePrefsLimit", "Picker value is: "+value);
		editor.commit();
	}
	
	/* This function updates the session limits mode value in the application's 
	 * preferences, given the correct constant. */
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
	
	/*Set random motivational message for the user */
	public void generateMessage(TextView message) {
		// Based on: http://stackoverflow.com/questions/363681/generating-random-number-in-a-range-with-java
		Random rand = new Random();
		int randomNum = rand.nextInt(6);
		String[] array = getResources().getStringArray(R.array.motivationalMessages);
		message.setText(array[randomNum]);
	}

}
