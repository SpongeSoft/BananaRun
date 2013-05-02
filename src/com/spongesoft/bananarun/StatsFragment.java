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
		long id = entry.setRace();
		
		long loc_id = entry.setLocation(id, 40.331632, -3.764019, 100.0, 0, 0);
		loc_id = entry.setLocation(id, 40.331642, -3.765069, 104.8, 2, 3);
		loc_id = entry.setLocation(id, 40.331023, -3.764649, 106.3, 6, 6);
		loc_id = entry.setLocation(id, 40.331523, -3.764649, 105.3, 15, 9);
		loc_id = entry.setLocation(id, 40.331723, -3.764349, 106.5, 19, 12);
		loc_id = entry.setLocation(id, 40.331823, -3.764659, 106.3, 24, 15);
		loc_id = entry.setLocation(id, 40.331923, -3.764669, 105.4, 28, 18);
		loc_id = entry.setLocation(id, 40.331523, -3.764749, 105.2, 30, 21);
		loc_id = entry.setLocation(id, 40.331423, -3.764549, 106.2, 42.15, 24);
		
		
		
		
		entry.updateRace(id);
		
		String speed = entry.getRaceParam(id,0);
		String time = entry.getRaceParam(id,1);
		String distance = entry.getRaceParam(id,2);
		String distperm = entry.getRaceParam(id,3);
		String kcal = entry.getRaceParam(id,4);
		tv.setText("Last inserted id --> " + id
				+ "\nSpeed --> " + speed + " meters per second"
				+ "\nTime --> " + time + " seconds "
				+ "\nDistance --> " + distance + " meters"
				+ "\nSeconds per meter --> " + distperm
				+ "\nkcals burnt --> " + kcal);

		//
		TextView pref = (TextView) HomeView.findViewById(R.id.pref);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getActivity().getBaseContext());
		String height = settings.getString("prefUserHeight", "170");
		String weight = settings.getString("prefUserWeight", "60");
		pref.setText("vals: " + height + ", " + weight);

		return HomeView;
	}

}
