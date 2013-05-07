package com.spongesoft.bananarun;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
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



public class ListGraphsActivity extends Activity {
	// action id
	private static final int ID_DISTANCE = 1;
	private static final int ID_SPEED = 2;
	SharedPreferences preferences;

	TextView meanSpeed;
	TextView averageTime;
	TextView timePerDistance;
	TextView totalDistance;
	TextView totalKilocalories;

	double[] sessionsInfo;

	DBManagement entry;
	double[][] arr;
	long id_race;
	double distance[][];
	double total_distance[];
	private XYMultipleSeriesRenderer mRenderer = getDemoRenderer();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selectgraph);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		entry = new DBManagement(this);

		/*
		 * Intent currentIntent = getIntent(); double id_race =
		 * currentIntent.getDoubleExtra("statsID", -1.0);
		 */
		final long id_race = preferences.getLong("statsID", -1);

		/* Set the TextView values */
		meanSpeed = (TextView) findViewById(R.id.sessionStatsMeanSpeed);
		averageTime = (TextView) findViewById(R.id.sessionStatsMeanTime);
		timePerDistance = (TextView) findViewById(R.id.sessionStatsTimePerDistance);
		totalDistance = (TextView) findViewById(R.id.sessionStatsDistance);
		totalKilocalories = (TextView) findViewById(R.id.sessionStatsKilocalories);

		entry.open();

		sessionsInfo = new double[5];

		double[] singleSession = entry.getParamsForSpecificRace(id_race);
		sessionsInfo[0] += singleSession[2]; // Average speed
		sessionsInfo[1] += singleSession[3]; // Total time
		sessionsInfo[2] += singleSession[4]; // Total distance
		sessionsInfo[3] += singleSession[5]; // Average time per Km
		sessionsInfo[4] += singleSession[6]; // Calories burnt

		AuxMethods aux = new AuxMethods(preferences);
		String distanceUnit = aux.getDistanceUnits();
		
		meanSpeed.setText((" " + aux.stripDecimals(sessionsInfo[0])) + " "+distanceUnit+"/h.");
		averageTime.setText((" " + aux.stripDecimals((sessionsInfo[1])/60))+" min.");
		totalDistance.setText(" " + aux.getDistance(sessionsInfo[2])+" "+distanceUnit);
		timePerDistance.setText(" " + aux.stripDecimals((sessionsInfo[3])/60)+" "+distanceUnit+"/h");
		totalKilocalories.setText(" " + aux.stripDecimals(sessionsInfo[4])+ " KCal.");

		entry.close();

		ActionItem nextItem = new ActionItem(ID_DISTANCE, getResources()
				.getString(R.string.distStat), getResources().getDrawable(
				R.drawable.menu_ok));
		ActionItem prevItem = new ActionItem(ID_SPEED, getResources()
				.getString(R.string.speedStat2), getResources().getDrawable(
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

		// Set listener for action item clicked
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);
						entry.open();
						Log.d("actionId", actionId + "");
						// here we can filter which action item was clicked with
						// pos or actionId parameter
						if (actionId == ID_DISTANCE) {

							arr = entry.getRaceParam(id_race, actionId);
							if (arr != null) {

								total_distance = entry
										.getParamsForSpecificRace(id_race);
								if (total_distance[4] > 10) {
									String title_serie = "";
									getLineChart2(actionItem.getTitle(),
											title_serie);
								} else {
									Toast.makeText(
											getApplicationContext(),
											getResources().getString(
													R.string.toastStat),
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.toastStat),
										Toast.LENGTH_SHORT).show();
							}

							entry.close();
						} else if (actionId == ID_SPEED) {

							arr = entry.getRaceParam(id_race, actionId);
							if (arr != null) {

								total_distance = entry
										.getParamsForSpecificRace(id_race);
								if (total_distance[4] > 10) {
									String title_serie = "";
									getLineChart1(actionItem.getTitle(),
											title_serie);
								} else {
									Toast.makeText(
											getApplicationContext(),
											getResources().getString(
													R.string.toastStat),
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.toastStat),
										Toast.LENGTH_SHORT).show();
							}

							entry.close();

						}
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
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

	}

	public void getLineChart1(String name, String title) {
		setLineSettings(mRenderer, name);
		Intent intent = ChartFactory.getLineChartIntent(this,
				getDemoDataset(title), mRenderer);
		startActivity(intent);
	}

	public void getLineChart2(String name, String title) {
		setLineSettings(mRenderer, name);
		Intent intent = ChartFactory.getLineChartIntent(this,
				getDemoDataset2(title), mRenderer);
		startActivity(intent);
	}

	private void setLineSettings(XYMultipleSeriesRenderer renderer, String name) {
		renderer.setChartTitle(name);
		renderer.setApplyBackgroundColor(false);
		renderer.setFitLegend(false);
		renderer.setAxesColor(Color.BLACK);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setZoomButtonsVisible(true);
		renderer.setXLabels(RESULT_OK);
		renderer.setYLabels(RESULT_OK);

	}

	private XYMultipleSeriesDataset getDemoDataset(String title) {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		XYSeries firstSeries = new XYSeries(title);
		for (int i = 0; i < arr.length; i++)
			firstSeries.add(i, arr[i][0]);
		dataset.addSeries(firstSeries);

		return dataset;
	}

	private XYMultipleSeriesDataset getDemoDataset2(String title) {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries firstSeries = new XYSeries(title);
		double sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i][0];
			firstSeries.add(i, sum);
		}
		dataset.addSeries(firstSeries);

		return dataset;
	}

	private XYMultipleSeriesRenderer getDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		XYSeriesRenderer r = new XYSeriesRenderer();
		renderer.setAxisTitleTextSize(20);
		renderer.setChartTitleTextSize(18);
		renderer.setLabelsTextSize(18);
		renderer.setLegendTextSize(18);
		renderer.setMargins(new int[] { 40, 20, 20, 0 });
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		r.setColor(Color.BLUE);
		renderer.addSeriesRenderer(r);
		return renderer;
	}

	public int setmaxvalueY(double arr[][]) {
		double max_value = arr[0][0];
		for (int i = 1; i < arr.length; i++) {
			if (max_value < arr[i][0]) {
				max_value = arr[i][0];
			}

		}

		return (int) max_value;

	}

	public int setmaxvalueX(double arr[][]) {
		double max_value = arr[0][0];
		for (int i = 1; i < arr.length; i++) {
			if (max_value < arr[i][1]) {
				max_value = arr[i][1];
			}

		}

		return (int) max_value;

	}

}
