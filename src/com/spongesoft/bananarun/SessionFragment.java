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
		startBtn = (Button) SessionView.findViewById(R.id.btn_enter);
		stopBtn = (Button) SessionView.findViewById(R.id.btn_stop);
		chronometer = (Chronometer) SessionView.findViewById(R.id.chronometer);

		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		int weatherCode = prefs.getInt("code", R.drawable.img3200);
		Log.d("Chronometer", "WeatherCode = "+weatherCode);
		String temp = prefs.getString("temperature", "??");
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/bradbunr.ttf");
		System.out.println(getActivity().getAssets().toString());
		
		TextView kmCounter = (TextView) SessionView.findViewById(R.id.kmeter);
		kmCounter.setTypeface(font);
		
		TextView calorieMeter = (TextView) SessionView.findViewById(R.id.calories);
		calorieMeter.setTypeface(font);
		
		TextView caloriesVal = (TextView) SessionView.findViewById(R.id.caloriesValue);
		caloriesVal.setTypeface(font);
		
		TextView timeMeter = (TextView) SessionView.findViewById(R.id.timer);
		timeMeter.setTypeface(font);

		ImageView weatherIcon = (ImageView) SessionView.findViewById(R.id.sessionWeatherIcon);
		weatherIcon.setImageResource(weatherCode);
		TextView sessionTemp = (TextView) SessionView.findViewById(R.id.temperature);
		sessionTemp.setText(temp);
		
		chronometer.setTypeface(font);

		Log.d("Chronometer","Chronometer state is: "+state);
		startBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (state == 0) {
					chronometer.setBase(SystemClock.elapsedRealtime());
					chronometer.start();
					state = 1;
					startBtn.setText("Pause");
					Log.d("Chronometer","Chronometer state is: "+state);

				} else if (state == 1) {
					chronometerCounter = SystemClock.elapsedRealtime();
					chronometer.stop();
					state = 2;
					startBtn.setText("Continue");
					Log.d("Chronometer","Chronometer state is: "+state);

				} else if (state == 2) {
					chronometer.setBase(chronometer.getBase()
							+ SystemClock.elapsedRealtime()
							- chronometerCounter);
					chronometer.start();
					state = 1;
					startBtn.setText("Pause");
					Log.d("Chronometer","Chronometer state is: "+state);

				}
			}
		});

		stopBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				chronometer.stop();
				chronometer.setBase(SystemClock.elapsedRealtime());
				startBtn.setText("Start");
				state = 0;
				Log.d("Chronometer","Chronometer state is: "+state);

			}

		});

		return SessionView;
	}

}
