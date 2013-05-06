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

	int[] firstData = { 23, 56 };

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
	
		Log.d("valor", "" + arr.length);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

		Button genStats = (Button) StatsView.findViewById(R.id.generalStats);
		ListView lv = (ListView) StatsView.findViewById(R.id.statsListview);

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
		//int numSessions = distance[0].length;
		entry.close();
		AuxMethods aux = new AuxMethods(preferences);
		

		ArrayList<String> list = new ArrayList<String>();
		if (distance != null) {
			for (int j = 0; j < numSessions; j++) {
				list.add("Session " + (j + 1) + " - " + aux.getDistance(distance[j][1]));
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
				newSession.putExtra("statsID", raceID);
				StatsView.getContext().startActivity(newSession);
				}
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				// TODO Auto-generated method stub

				Log.v("long clicked", "pos" + " " + pos);

				AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
				adb.setTitle("Delete?");
				adb.setMessage("Are you sure you want to delete " + pos + "?");
				final int positionToRemove = pos;
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						entry.open();
						entry.deleteRace(positionToRemove);
						entry.close();
						adapter.remove(adapter.getItem(positionToRemove));
						adapter.notifyDataSetChanged();
					}
				});
				adb.show();

				return true;
			}
		});

		/*
		 * TextView tv = (TextView) HomeView.findViewById(R.id.tvSQLinfo);
		 * entry.open(); entry.setRace(); String result =
		 * entry.getRaceAvgSpeed(); tv.setText(result);
		 * 
		 * 
		 * 
		 * // TextView pref = (TextView) HomeView.findViewById(R.id.pref);
		 * 
		 * SharedPreferences settings =
		 * PreferenceManager.getDefaultSharedPreferences
		 * (getActivity().getBaseContext()); String height =
		 * settings.getString("prefUserHeight", "No val"); String weight =
		 * settings.getString("prefUserWeight", "0"); pref.setText("vals: " +
		 * height + ", " + weight);
		 */

		return StatsView;
	}

	private XYMultipleSeriesDataset getBarDemoDataset() {
		XYMultipleSeriesDataset barChartDataset = new XYMultipleSeriesDataset();
		CategorySeries firstSeries = new CategorySeries("Growth of Company1");
		CategorySeries secondSeries = new CategorySeries("Growth of Company2");
		for (int i = 0; i < firstData.length; i++)
			firstSeries.add(firstData[i]);
		barChartDataset.addSeries(firstSeries.toXYSeries());

		return barChartDataset;
	}

	public XYMultipleSeriesRenderer getBarChartRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(20);
		renderer.setChartTitleTextSize(18);
		renderer.setLabelsTextSize(18);
		renderer.setLegendTextSize(18);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.BLUE);
		renderer.addSeriesRenderer(r);
		return renderer;
	}

	private void setBarChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("run1 vs run2");
		renderer.setXTitle("Time");
		renderer.setYTitle("Kilometers");
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(560);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(50);
	}

}
