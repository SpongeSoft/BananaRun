package com.spongesoft.bananarun;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.spongesoft.bananarun.R;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/** 
 * This is based on the example found in the following web
 * http://www.londatiga.net/it/how-to-create-quickaction-dialog-in-android/
 */

/**
 * The Activity will show a list of general values and graphs of them
 */

public class ListBarGraphs extends Activity {
	// action id
	private static final int ID_UP = 1;
	private static final int ID_DOWN = 2;
	private static final int ID_SEARCH = 3;
	private static final int ID_OK = 6;
	
	
	DBManagement entry;
	double[][] arr;

	TextView meanSpeed;
	TextView averageTime;
	TextView timePerDistance;
	TextView totalDistance;
	TextView totalKilocalories;
	TextView overview;

	SharedPreferences preferences;
	double[] sessionsInfo;

	@Override
	
	/**
	 *Display a layout with a button and general statistics
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selectgraph);

		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		entry = new DBManagement(this);

		/* Set the TextView values */
		overview = (TextView) findViewById(R.id.overview);
		meanSpeed = (TextView) findViewById(R.id.sessionStatsMeanSpeed);
		averageTime = (TextView) findViewById(R.id.sessionStatsMeanTime);
		timePerDistance = (TextView) findViewById(R.id.sessionStatsTimePerDistance);
		totalDistance = (TextView) findViewById(R.id.sessionStatsDistance);
		totalKilocalories = (TextView) findViewById(R.id.sessionStatsKilocalories);
		
		/* Defining action items */
		
		ActionItem nextItem = new ActionItem(ID_DOWN, getResources().getString(
				R.string.timeStat), getResources().getDrawable(
				R.drawable.menu_ok));
		ActionItem prevItem = new ActionItem(ID_UP, getResources().getString(
				R.string.speedStat), getResources().getDrawable(
				R.drawable.menu_ok));
		ActionItem searchItem = new ActionItem(ID_SEARCH, getResources()
				.getString(R.string.distStat), getResources().getDrawable(
				R.drawable.menu_ok));
		ActionItem item = new ActionItem(ID_OK, getResources().getString(
				R.string.calStat), getResources().getDrawable(
				R.drawable.menu_ok));

		// use setSticky(true) to disable QuickAction dialog being dismissed
		// after an item is clicked
		prevItem.setSticky(true);
		nextItem.setSticky(true);

		// create QuickAction. Use QuickAction.VERTICAL or
		// QuickAction.HORIZONTAL param to define layout
		// orientation
		final QuickAction quickAction = new QuickAction(this,
				QuickAction.VERTICAL);

		// add action items into QuickAction
		quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
		quickAction.addActionItem(searchItem);
		quickAction.addActionItem(item);

		// Set listener for action item clicked
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);
						entry.open();

						// here we can filter which action item was clicked with actionId parameter
						if (actionId == ID_SEARCH) {
							arr = entry.getSessionsParam(0);

							if (arr != null) {
								getBarChart(actionItem.getTitle());
							} else {
								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.toastStat),
										Toast.LENGTH_SHORT).show();
							}
						} else if (actionId == ID_DOWN) {
							arr = entry.getSessionsParam(1);

							if (arr != null) {
								getBarChart(actionItem.getTitle());
							} else {
								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.toastStat),
										Toast.LENGTH_SHORT).show();
							}

						} else if (actionId == ID_UP) {
							arr = entry.getSessionsParam(2);

							if (arr != null) {
								getBarChart(actionItem.getTitle());
							} else {
								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.toastStat),
										Toast.LENGTH_SHORT).show();
							}
						} else if (actionId == ID_OK) {
							arr = entry.getSessionsParam(3);

							if (arr != null) {
								getBarChart(actionItem.getTitle());
							} else {
								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.toastStat),
										Toast.LENGTH_SHORT).show();
							}
						}
						entry.close();
					}
				});

		// set listnener for on dismiss event, this listener will be called only
		// if QuickAction dialog was dismissed
		// by clicking the area outside the dialog.
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {
				/*
				 * Toast.makeText(getApplicationContext(), "Dismissed",
				 * Toast.LENGTH_SHORT).show();
				 */
			}
		});

		// show on btn1
		Button btn1 = (Button) findViewById(R.id.btn1);
		Typeface font = Typeface.createFromAsset(this.getAssets(),
				"fonts/bradbunr.ttf");
		btn1.setTypeface(font);
		overview.setTypeface(font);
		
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

		entry.open();
		
		/* Checking if there are entries to get average values */
		
		double[][] allSessions = entry.getSessionsIdsAndDistance();
		if (allSessions != null) {
			int numSession = entry.getRaceCount();
			Log.d("sesion", "" + numSession);

			sessionsInfo = new double[5];

			for (int i = 0; i < numSession; i++) {
				double[] singleSession = entry
						.getParamsForSpecificRace((long) allSessions[i][0]);
				sessionsInfo[0] += singleSession[2]; // Average speed
				sessionsInfo[1] += singleSession[3]; // Total time
				sessionsInfo[2] += singleSession[4]; // Total distance
				sessionsInfo[3] += singleSession[5]; // Average time per Km
				sessionsInfo[4] += singleSession[6]; // Calories burnt
			}

			AuxMethods aux = new AuxMethods(preferences);
			String distanceUnit = aux.getDistanceUnits();
			
			/* Printing the values in the screen */
			meanSpeed.setText((" " + aux.stripDecimals((sessionsInfo[0] / numSession)) + " m/s."));
			averageTime.setText((" " + aux.stripDecimals((sessionsInfo[1]/numSession)/60))+" min.");
			totalDistance.setText(" " + aux.getDistance(sessionsInfo[2]));
			timePerDistance.setText(" " + aux.stripDecimals((sessionsInfo[3] / numSession)/60)+" min/"+distanceUnit+".");
			totalKilocalories.setText(" " + aux.stripDecimals(sessionsInfo[4])+ " KCal.");

			entry.close();
		} else {
			entry.close();
		}
	}

	
	/*
	 * Defining the function for plotting the bar chart
	 */
	
	
	public void getBarChart(String name) {
		XYMultipleSeriesRenderer barChartRenderer = getBarChartRenderer();
		setBarChartSettings(barChartRenderer, name);
		Intent intent = ChartFactory.getBarChartIntent(this,
				getBarDemoDataset(), barChartRenderer, Type.DEFAULT);
		startActivity(intent);
	}
	
	/* Taken the values to be printed */

	private XYMultipleSeriesDataset getBarDemoDataset() {
		XYMultipleSeriesDataset barChartDataset = new XYMultipleSeriesDataset();
		CategorySeries firstSeries = new CategorySeries("Session");
		for (int i = 0; i < arr.length; i++)
			firstSeries.add(arr[i][0]);
		barChartDataset.addSeries(firstSeries.toXYSeries());

		return barChartDataset;
	}
	/* Setting some style to the graph */
	
	public XYMultipleSeriesRenderer getBarChartRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(20);
		renderer.setChartTitleTextSize(18);
		renderer.setLabelsTextSize(18);
		renderer.setLegendTextSize(18);
		renderer.setMargins(new int[] { 25, 30, 0, 25 });
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.BLUE);
		renderer.addSeriesRenderer(r);
		return renderer;
	}

	/* Setting some style to the graph */
	
	private void setBarChartSettings(XYMultipleSeriesRenderer renderer,
			String name) {
		renderer.setChartTitle(name);
		renderer.setYTitle(name);
		//renderer.setYAxisMax(setmaxvalue(arr));
		renderer.setXLabels(RESULT_OK);
		renderer.setYLabels(RESULT_OK);
	}

	public int setmaxvalue(double arr[][]) {
		double max_value = arr[0][0];
		for (int i = 1; i < arr.length; i++) {
			if (max_value < arr[i][0]) {
				max_value = arr[i][0];
			}

		}

		return (int) max_value;

	}
}
