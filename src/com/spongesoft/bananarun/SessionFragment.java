package com.spongesoft.bananarun;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.spongesoft.dietapp.R;

public class SessionFragment extends Fragment {

	/* Global components: buttons, chronometer and preferences */
	Button startBtn;
	Button stopBtn;
	Chronometer chronometer;
	long chronometerCounter;
	int state = 0;
	SharedPreferences prefs;

	public SessionFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		final View SessionView = inflater.inflate(R.layout.new_session,
				container, false);
		
		/* Get references to fragment components */
		startBtn = (Button) SessionView.findViewById(R.id.btn_enter);
		stopBtn = (Button) SessionView.findViewById(R.id.btn_stop);
		chronometer = (Chronometer) SessionView.findViewById(R.id.chronometer);
		
		TextView kmCounter = (TextView) SessionView.findViewById(R.id.kmeter);
		TextView calorieMeter = (TextView) SessionView.findViewById(R.id.calories);
		TextView caloriesVal = (TextView) SessionView.findViewById(R.id.caloriesValue);
		TextView timeMeter = (TextView) SessionView.findViewById(R.id.timer);
		ImageView weatherIcon = (ImageView) SessionView.findViewById(R.id.sessionWeatherIcon);
		TextView sessionTemp = (TextView) SessionView.findViewById(R.id.temperature);

		/* Retrieve weather data for display from General Preferences */
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		int weatherCode = prefs.getInt("code", R.drawable.img3200); //Weather image
		Log.d("Chronometer", "WeatherCode = "+weatherCode); 
		String temp = prefs.getString("temperature", "??"); //Weather temperature
		
		/* Set font to TextView components */
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/bradbunr.ttf");
		
		kmCounter.setTypeface(font);		
		calorieMeter.setTypeface(font);
		caloriesVal.setTypeface(font);
		timeMeter.setTypeface(font);
		chronometer.setTypeface(font);

		/* Set weather information in corresponding elements */
		weatherIcon.setImageResource(weatherCode);
		sessionTemp.setText(temp);
		
		Log.d("Chronometer","Chronometer state is: "+state);
		
		/* Chronometer all-in-one Start/Pause/Continue button. It initializes 
		 * the chronometer's timer. When started, the button allows the user to 
		 * pause the chronometer and resume it afterwards.
		 * State 0: default state. Timer is set to 0 and stopped.
		 * State 1: Timer running.
		 * State 2: Timer paused. */
		startBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (state == 0) {
					chronometer.setBase(SystemClock.elapsedRealtime()); //Reset timer
					chronometer.start(); //Start timer
					state = 1;
					startBtn.setText("Pause");
					Log.d("Chronometer","Chronometer state is: "+state);

				} else if (state == 1) {
					chronometerCounter = SystemClock.elapsedRealtime(); //Store last timer value
					chronometer.stop(); //Stop timer
					state = 2;
					startBtn.setText("Continue");
					Log.d("Chronometer","Chronometer state is: "+state);

				} else if (state == 2) {
					chronometer.setBase(chronometer.getBase()
							+ SystemClock.elapsedRealtime()
							- chronometerCounter); //Resume timer with the last value as the initial one
					chronometer.start(); //Resume timer
					state = 1;
					startBtn.setText("Pause");
					Log.d("Chronometer","Chronometer state is: "+state);

				}
			}
		});

		/* Button used to stop and reset the chronometer's timer. */
		stopBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				chronometer.stop(); //Stop timer
				chronometer.setBase(SystemClock.elapsedRealtime()); //Restart timer value
				startBtn.setText("Start");
				state = 0; 
				Log.d("Chronometer","Chronometer state is: "+state);

			}

		});

		return SessionView;
	}

}
