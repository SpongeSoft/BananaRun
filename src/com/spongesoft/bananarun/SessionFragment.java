package com.spongesoft.bananarun;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.spongesoft.dietapp.R;

public class SessionFragment extends Fragment {

	Button startBtn;
	Button stopBtn;
	Chronometer chronometer;
	long chronometerCounter;
	int state = 0;

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

		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/bradbunr.ttf");
		System.out.println(getActivity().getAssets().toString());
		chronometer.setTypeface(font);
		chronometer.setTextColor(getResources().getColor(R.color.text_colour));

		startBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (state == 0) {
					chronometer.setBase(SystemClock.elapsedRealtime());
					chronometer.start();
					state = 1;
					startBtn.setText("Pause");
				}

				if (state == 1) {
					chronometerCounter = SystemClock.elapsedRealtime();
					chronometer.stop();
					state = 2;
					startBtn.setText("Continue");
				} else {
					chronometer.setBase(chronometer.getBase()
							+ SystemClock.elapsedRealtime()
							- chronometerCounter);
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

		return SessionView;
	}

}
