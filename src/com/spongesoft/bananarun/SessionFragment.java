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
import android.provider.MediaStore;
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
	ImageView musicIcon;
	Chronometer chronometer;
	long chronometerCounter;
	int state;
	SharedPreferences prefs;
	DBManagement manager;
	
	TextView kmCounter;
	TextView timeMeter;
	long race_id;
	
	@Override
	public void onDestroy() {
		//unRegister receiver
		stopReceiver();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		stopReceiver();
		super.onPause();
	}

	@Override
	public void onResume() {
		/* Register receivers */
        final IntentFilter myFilter = new IntentFilter("com.spongesoft.bananarun.LOCATION_UPDATED");
        this.getActivity().registerReceiver(mReceiver, myFilter);
        this.getActivity().registerReceiver(abcd, new IntentFilter("xyz"));
		super.onResume();
	}

	private void stopReceiver() {
		try {
			/* Unregister receivers */
			this.getActivity().unregisterReceiver(mReceiver);
			this.getActivity().unregisterReceiver(abcd);
		} catch(IllegalArgumentException e) {
			Log.d("receiver", "Unregistered receiver more than once!");
		}
	}


	AuxMethods aux;
	
	public SessionFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
        final IntentFilter myFilter = new IntentFilter("com.spongesoft.bananarun.LOCATION_UPDATED");
        
        /* Retrieve receivers */
        this.getActivity().registerReceiver(mReceiver, myFilter);
        this.getActivity().registerReceiver(abcd, new IntentFilter("xyz"));
		
		/* Get current session ID for database manipulation */
		race_id = getArguments().getLong("race_id");
		manager = new DBManagement(this.getActivity()); //Database Helper Object
		manager.open();
		
		final View SessionView = inflater.inflate(R.layout.new_session,
				container, false);
		
		/* Get references to fragment components */
		stopBtn = (ImageView) SessionView.findViewById(R.id.btnStop);
		chronometer = (Chronometer) SessionView.findViewById(R.id.chronometer);
		
		kmCounter = (TextView) SessionView.findViewById(R.id.kmeter);
		ImageView weatherIcon = (ImageView) SessionView.findViewById(R.id.sessionWeatherIcon);
		TextView sessionTemp = (TextView) SessionView.findViewById(R.id.temperature);

		/* Retrieve weather data for display from General Preferences */
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		int weatherCode = prefs.getInt("code", R.drawable.img3200); //Weather image
		Log.d("Chronometer", "WeatherCode = "+weatherCode); 
		String temp = prefs.getString("temperature", "??"); //Weather temperature
		
        
        aux = new AuxMethods(prefs);
		
		/* Set font to TextView components */
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/bradbunr.ttf");
		
		kmCounter.setTypeface(font);		
		chronometer.setTypeface(font);
		sessionTemp.setTypeface(font);

		/* Set weather information in corresponding elements */
		weatherIcon.setImageResource(weatherCode);
		sessionTemp.setText(temp + "º");
		
		/* Reference music icon and define its behaviour */
		musicIcon = (ImageView) SessionView.findViewById(R.id.musicIcon);
		
		/* Music icon behaviour: redirect the user to her favourite music player.
		 * Based on the code from: http://stackoverflow.com/questions/9324354/how-to-open-audio-player-in-android-as-an-intent*/
		musicIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
				startActivity(intent);
				
			}
		});
		
		chronometer.setBase(SystemClock.elapsedRealtime()); //Reset timer
		chronometer.start(); //Start timer
		
		Log.d("Chronometer","Chronometer state is: "+state);

		/* Button used to stop and reset the chronometer's timer and stop the Location Receiver */
		stopBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//Send broadcast to stop service.
		            final Intent intent = new Intent("com.spongesoft.bananarun.TO_SERVICE");
		            intent.putExtra("data", "stop");
		            v.getContext().sendBroadcast(intent);
		            
		            stop();
			}
		});

		return SessionView;
	}

	
	
	
	//We use this to receive the broadcast when a location has been updated
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		/* Update screen's distance information*/
	            public void onReceive(Context context, Intent intent) {
	                        updateStats();

	            }
	
	};
	
	/* BroadcastReceiver used to determine when to stop the Location Service */
	private final BroadcastReceiver abcd = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	stop();                                
        }
};

	/* Update screen information */
	private void updateStats() {
		ContentValues stats = manager.getStats(race_id);
		kmCounter.setText(aux.getDistance(Double.parseDouble(stats.getAsString(manager.KEY_S_TOTAL_DISTANCE))));
		
		Log.d("stats", "raceID: "+race_id+" total_dist: "+stats.getAsString(manager.KEY_S_TOTAL_DISTANCE)+"total_time: "+stats.getAsString(manager.KEY_S_TOTAL_DISTANCE));
	}
	
	private void stop(){
		chronometer.stop(); //Stop timer
		chronometer.setBase(SystemClock.elapsedRealtime()); //Restart timer value
		Log.d("Chronometer","Chronometer state is: "+state);
		
		/* Go back to Home Fragment */
		Intent backToHome = new Intent (getActivity().getApplicationContext(), MainActivity.class);
        startActivity(backToHome);
        this.getActivity().finish();
		
	}
	
}
