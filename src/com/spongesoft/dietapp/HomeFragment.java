package com.spongesoft.dietapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
	 * The HOME tab fragment
	 */
	public class HomeFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

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
			
			/* Button is pressed */
			startButton.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent newSession = new Intent(getActivity().getBaseContext(), SessionActivity.class);
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
			
			return HomeView;
		}
	}