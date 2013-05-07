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

	/* Global variables */
	ArrayAdapter<String> adapter;
	SharedPreferences preferences;
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

		/* Retrieve application's global preferences */
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		/* Reference elements from XML layout */
		Button genStats = (Button) StatsView.findViewById(R.id.generalStats);
		ListView lv = (ListView) StatsView.findViewById(R.id.statsListview);
		TextView Sessions = (TextView) StatsView.findViewById(R.id.header);

		/* Apply text font to TextViews */
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/bradbunr.ttf");
		Sessions.setTypeface(font);
		genStats.setTypeface(font);

		/*
		 * When pressed, the 'General Statistics' button redirects the user to a
		 * new screen with a summary of the global sessions progress, and a
		 * button to show different graphs related to the general statistics
		 */
		genStats.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(),
						ListBarGraphs.class);

				StatsView.getContext().startActivity(newSession);
			}
		});

		/*
		 * Obtain the number of races currently stored in the database and
		 * display the sessions in a ListView
		 */

		entry.open(); // Open Database
		int numSessions = entry.getRaceCount(); // Get number of races
		distance = entry.getSessionsIdsAndDistance(); // Get races' distances
		entry.close(); // Close database
		AuxMethods aux = new AuxMethods(preferences);

		/*
		 * Fill array adapter with the name of each session and its
		 * corresponding distance
		 */
		ArrayList<String> list = new ArrayList<String>();
		if (distance != null) {
			for (int j = 0; j < numSessions; j++) {
				list.add(getResources().getString(R.string.session) + " "
						+ (j + 1) + " - " + aux.getDistance(distance[j][1]));
			}
		}

		/* Link adapter to ListView */
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);

		/* Behaviour of the ListView when an item is pressed */
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			/*
			 * When clicked, the application redirects the user to a new screen
			 * where she can look at a summary of the selected session's
			 * statistics, and also a button which will redirect her to another
			 * screen for the graph display
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				if (distance != null) {
					Intent newSession = new Intent(getActivity()
							.getBaseContext(), ListGraphsActivity.class);

					/*
					 * Insert the selected session's ID into the preferences so
					 * that the new screen can use it
					 */
					double raceID = distance[position][0];
					SharedPreferences.Editor editor = preferences.edit();
					editor.putLong("statsID", (long) raceID);
					editor.commit();

					/* Start specific session's statistics activity */
					StatsView.getContext().startActivity(newSession);
				}
			}
		});

		/*
		 * When the user clicks during one or two seconds over one of the
		 * ListView session items, the Application pops up a dialog where the
		 * user can decide whether the session should be deleted or not. If the
		 * user presses 'Yes', the session is deleted from the Database.
		 * Otherwise, the session is still in the DB.
		 */
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {

				Log.v("long clicked", "pos" + " " + pos);

				/* Build dialog alert */
				AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
				adb.setTitle(getResources().getString(R.string.delete));

				/* ID of the race to delete */
				final int positionToRemove = (int) distance[pos][0];

				adb.setMessage(getResources().getString(R.string.sureDelete));
				adb.setNegativeButton(
						getResources().getString(R.string.cancel), null);
				final int position = pos; // Item position in ListView
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() { // YES
							public void onClick(DialogInterface dialog,
									int which) {
								entry.open();

								/* Delete race and update sessions' indexes */
								entry.deleteRace(positionToRemove);
								distance = entry.getSessionsIdsAndDistance();

								/* Close database helper and update ListView */
								entry.close();
								adapter.remove(adapter.getItem(position));
								adapter.notifyDataSetChanged();
							}
						});
				adb.show(); // Show Dialog

				return true;
			}
		});

		return StatsView;
	}

}
