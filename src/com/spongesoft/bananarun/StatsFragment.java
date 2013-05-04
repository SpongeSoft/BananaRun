package com.spongesoft.bananarun;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spongesoft.dietapp.R;

public class StatsFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Buttons and whatnot
	 */
	DBManagement entry;

	/**
	 * Methods
	 */
	public StatsFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		entry = new DBManagement(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		final View HomeView = inflater
				.inflate(R.layout.stats, container, false);
		
		Button genStats = (Button) HomeView.findViewById(R.id.general);
		
		genStats.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//Intent newSession = new Intent(getActivity().getBaseContext(), ExampleActivity.class);
				//HomeView.getContext().startActivity(newSession);
			}
		 });
		
		Button carrera1 = (Button) HomeView.findViewById(R.id.button1);
		
		carrera1.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(), ExampleActivity.class);
				HomeView.getContext().startActivity(newSession);
			}
		 });
		
		Button carrera2 = (Button) HomeView.findViewById(R.id.button2);
		
		carrera2.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(), ExampleActivity.class);
				HomeView.getContext().startActivity(newSession);
			}
		 });
		
		Button carrera3=(Button) HomeView.findViewById(R.id.button3);
		
		carrera3.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(), ExampleActivity.class);
				HomeView.getContext().startActivity(newSession);
			}
		 });
		
		Button carrera4 = (Button) HomeView.findViewById(R.id.button4);
		
		carrera4.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(), ExampleActivity.class);
				HomeView.getContext().startActivity(newSession);
			}
		 });
		

		/*TextView tv = (TextView) HomeView.findViewById(R.id.tvSQLinfo);
		entry.open();
		entry.setRace();
		String result = entry.getRaceAvgSpeed();
		tv.setText(result);

		
		
		//
		TextView pref = (TextView) HomeView.findViewById(R.id.pref);
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
		String height = settings.getString("prefUserHeight", "No val");
		String weight = settings.getString("prefUserWeight", "0");
		pref.setText("vals: " + height + ", " + weight);
		
		*/
		
		return HomeView;
	}

}
