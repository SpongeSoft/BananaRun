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

		TextView tv = (TextView) HomeView.findViewById(R.id.tvSQLinfo);
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
		
		
		
		return HomeView;
	}

}
