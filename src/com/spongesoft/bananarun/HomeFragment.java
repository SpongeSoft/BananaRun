package com.spongesoft.bananarun;


import com.spongesoft.dietapp.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
	 * The HOME tab fragment
	 */
	public class HomeFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		
		public static final String ARG_SECTION_NUMBER = "section_number";
		public final int MODE_PRIVATE = 0;

		
		ImageView weatherIcon;
		TextView temperatureText;
		SharedPreferences generalPrefs;
		Handler handler;
		String temp;
		int wCode;

		public HomeFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			final View HomeView = inflater.inflate(R.layout.home_tab, container, false);
			
			//RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.relayout);
			//Color textos #b45d1d 
			ImageView startButton = (ImageView) HomeView.findViewById(R.id.startbutton);
			
			ImageView prefsBackground = (ImageView) HomeView.findViewById(R.id.preferencesbackground);
			prefsBackground.setVisibility(View.VISIBLE);
			
			ImageView weatherBackground = (ImageView) HomeView.findViewById(R.id.weatherbackground);
			weatherBackground.setVisibility(View.VISIBLE);
			
			weatherIcon = (ImageView) HomeView.findViewById(R.id.weathericon);
			temperatureText = (TextView) HomeView.findViewById(R.id.temperature);
			Typeface lTemperaturetypeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/bradbunr.ttf");
			temperatureText.setTypeface(lTemperaturetypeFace);
			
        	generalPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());

			/* Button is pressed */
			startButton.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent newSession = new Intent(getActivity().getBaseContext(), SessionSettingsActivity.class);
					HomeView.getContext().startActivity(newSession);
				}
			 });
			
			/* Reference Preferences button from XML layout */
			ImageView prefsButton = (ImageView) HomeView.findViewById(R.id.preferencesbutton);
			
			/* Declare animation variable for the Preferences button rotation */
			final Animation rotate = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.rotate);
			rotate.setAnimationListener(new AnimationListener(){
				@Override
		        public void onAnimationStart(Animation animation) {
		            // Do nothing

		        }

		        @Override
		        public void onAnimationRepeat(Animation animation) {
		            // Do nothing

		        }

		        @Override
		        public void onAnimationEnd(Animation animation) {
		            /* Show preferences right after rotation end */
		        	Intent prefs = new Intent(getActivity().getBaseContext(),UserSettingActivity.class);
					HomeView.getContext().startActivity(prefs);

		        }
			});
			
			/* When the Preferences button is pressed, start rotation animation */
			prefsButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					v.startAnimation(rotate); //START
					}
	
				
			});
			
			/* Change textviews' font to bradrunr, located in assets/fonts */
			
			/* 'Last session' String */
			TextView lastSession =(TextView) HomeView.findViewById(R.id.lastsession);
			Typeface lSessiontypeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/bradbunr.ttf");
			lastSession.setTypeface(lSessiontypeFace);
			
			/* 'Distance' String */
			TextView lastDistance =(TextView) HomeView.findViewById(R.id.distance);
			Typeface lDistancetypeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/bradbunr.ttf");
			lastDistance.setTypeface(lDistancetypeFace);
			
			/* 'Time' String */
			TextView lastTime =(TextView) HomeView.findViewById(R.id.time);
			Typeface lTimetypeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/bradbunr.ttf");
			lastTime.setTypeface(lTimetypeFace);
			
//			TextView textView = new TextView(getActivity());
//			textView.setGravity(Gravity.CENTER);
//			textView.setText(Integer.toString(getArguments().getInt(
//					ARG_SECTION_NUMBER)));
			handler = new Handler();
	         Runnable runnable = new Runnable() {
	            public void run() {
	                   	    			
	    			temp = generalPrefs.getString("temperature", "??");
	    			wCode = generalPrefs.getInt("code", -1);
	            	//String temp = (String) getActivity().getIntent().getCharSequenceExtra("temperature");
		            //int wCode = getActivity().getIntent().getIntExtra("code", -1);
		            if(wCode!=-1) {
		    			weatherIcon.setImageResource(wCode);
		    			temperatureText.setText(temp+"º");
		            }
	                handler.postDelayed(this, 2000);  //for interval (10 mins)...
	            }
	        };
	        
	        handler.postDelayed(runnable, 2000); //for initial delay..
	    
			
			return HomeView;
		}
		
		@Override
	    public void onResume() {
	        super.onResume();
		}
		
		@Override
		public void onPause() {
			super.onPause();
		}
		
		
	}
