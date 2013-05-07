package com.spongesoft.bananarun;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spongesoft.bananarun.R;

public class StatsFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	

	ArrayAdapter<String> adapter;
	SharedPreferences preferences;

	/**
	 * Buttons and whatnot
	 */
	DBManagement entry;
	double[][] arr;
	double distance[][];

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
		final View StatsView = inflater.inflate(R.layout.stats, container,
				false);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

		Button genStats = (Button) StatsView.findViewById(R.id.generalStats);
		ListView lv = (ListView) StatsView.findViewById(R.id.statsListview);
		TextView Sessions = (TextView) StatsView.findViewById(R.id.header);
		
		Typeface font = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/bradbunr.ttf");
		Sessions.setTypeface(font);
		genStats.setTypeface(font);
		
		
		genStats.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(),
						ListBarGraphs.class);
				
				StatsView.getContext().startActivity(newSession);
			}
		});

		entry.open();
		int numSessions = entry.getRaceCount();
		distance = entry.getSessionsIdsAndDistance();
		entry.close();
		AuxMethods aux = new AuxMethods(preferences);
		

		ArrayList<String> list = new ArrayList<String>();
		if (distance != null) {
			for (int j = 0; j < numSessions; j++) {
				list.add(getResources().getString(R.string.session)+" " + (j + 1) + " - " + aux.getDistance(distance[j][1]));
			}
		}

		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				if(distance!=null) {
				Intent newSession = new Intent(getActivity().getBaseContext(),
						ListGraphsActivity.class);
				
				
				double raceID = distance[position][0];
				SharedPreferences.Editor editor = preferences.edit();
				editor.putLong("statsID",(long) raceID);
				editor.commit();
				
				StatsView.getContext().startActivity(newSession);
				}
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {

				Log.v("long clicked", "pos" + " " + pos);

				AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
				adb.setTitle(getResources().getString(R.string.delete));
				adb.setMessage(getResources().getString(R.string.sureDelete) + " " + (pos+1) + "?");
				final int positionToRemove = (int) distance[pos][0];
				adb.setNegativeButton(getResources().getString(R.string.cancel), null);
				final int position = pos;
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						entry.open();
						entry.deleteRace(positionToRemove);
						distance = entry.getSessionsIdsAndDistance();
						entry.close();
						adapter.remove(adapter.getItem(position));
						adapter.notifyDataSetChanged();
					}
				});
				adb.show();

				return true;
			}
		});

		return StatsView;
	}

	

}
