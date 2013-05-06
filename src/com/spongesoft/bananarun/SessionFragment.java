package com.spongesoft.bananarun;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.spongesoft.bananarun.R;

public class SessionFragment extends Fragment {

	/* Global components: buttons, chronometer and preferences */
	ImageView stopBtn;
	Chronometer chronometer;
	long chronometerCounter;
	int state;
	SharedPreferences prefs;
	DBManagement manager;
	
	TextView kmCounter;
	TextView calorieMeter;
	TextView timeMeter;
	long race_id;
	public SessionFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
        final IntentFilter myFilter = new IntentFilter("com.spongesoft.bananarun.LOCATION_UPDATED");
        this.getActivity().registerReceiver(mReceiver, myFilter);
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		
		race_id = getArguments().getInt("race_id");
		manager = new DBManagement(this.getActivity());
		manager.open();
		final View SessionView = inflater.inflate(R.layout.new_session,
				container, false);
		
		/* Get references to fragment components */
		stopBtn = (ImageView) SessionView.findViewById(R.id.btnStop);
		chronometer = (Chronometer) SessionView.findViewById(R.id.chronometer);
		
		kmCounter = (TextView) SessionView.findViewById(R.id.kmeter);
		calorieMeter = (TextView) SessionView.findViewById(R.id.calories);
		TextView caloriesVal = (TextView) SessionView.findViewById(R.id.caloriesValue);
		timeMeter = (TextView) SessionView.findViewById(R.id.timer);
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
		
		
		chronometer.setBase(SystemClock.elapsedRealtime()); //Reset timer
		chronometer.start(); //Start timer
		
		Log.d("Chronometer","Chronometer state is: "+state);
		
		/* Chronometer all-in-one Start/Pause/Continue button. It initializes 
		 * the chronometer's timer. When started, the button allows the user to 
		 * pause the chronometer and resume it afterwards.
		 * State 0: default state. Timer is set to 0 and stopped.
		 * State 1: Timer running.
		 * State 2: Timer paused. */
		/*startBtn.setOnClickListener(new View.OnClickListener() {

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
		});*/

		/* Button used to stop and reset the chronometer's timer. */
		stopBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				chronometer.stop(); //Stop timer
				chronometer.setBase(SystemClock.elapsedRealtime()); //Restart timer value
				Log.d("Chronometer","Chronometer state is: "+state);
				
				//Send broadcast to stop service.

		            final Intent intent = new Intent("com.spongesoft.bananarun.TO_SERVICE");

		            intent.putExtra("data", "stop");

		            v.getContext().sendBroadcast(intent);
			}

		});

		return SessionView;
	}

	
	
	
	//We use this to receive the broadcast when a location has been updated
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

	            public void onReceive(Context context, Intent intent) {		
	                       // Log.d("BroadcastService", "Service received: " + intent.getCharSequenceExtra("data"));
	                       // String message = intent.getStringExtra("data");
	                        updateStats();

	            }
	
	};
	private void updateStats() {
		ContentValues stats = manager.getStats(race_id);
		kmCounter.setText(stats.getAsString(manager.KEY_S_TOTAL_DISTANCE));
		timeMeter.setText(stats.getAsString(manager.KEY_S_TOTAL_TIME));
		
		Log.d("stats", "raceID: "+race_id+" total_dist: "+stats.getAsString(manager.KEY_S_TOTAL_DISTANCE)+"total_time: "+stats.getAsString(manager.KEY_S_TOTAL_DISTANCE));
	}
}
