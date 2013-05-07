package com.spongesoft.bananarun;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.spongesoft.bananarun.LineChart;
import com.spongesoft.bananarun.MainActivity;
import com.spongesoft.bananarun.R;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.Toast;

public class ListGraphsActivity extends Activity {
	// action id
	private static final int ID_DISTANCE = 1;
	private static final int ID_SPEED = 2;
	private static final int ID_ALTITUDE = 3;
	SharedPreferences preferences;
	private GraphicalView mChartView;
	DBManagement entry;
	double[][] arr;
	double[] seriesFirstY = {20,-20,67,180,-45,24,99,-34,-8};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selectgraph);
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


		ActionItem nextItem = new ActionItem(ID_DISTANCE, "Distance",
				getResources().getDrawable(R.drawable.menu_ok));
		ActionItem prevItem = new ActionItem(ID_SPEED, "Speed", getResources()
				.getDrawable(R.drawable.menu_ok));
		ActionItem searchItem = new ActionItem(ID_ALTITUDE, "Altitude",
				getResources().getDrawable(R.drawable.menu_ok));

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

		// Set listener for action item clicked
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);

						Log.d("actionId", actionId+"");
						// here we can filter which action item was clicked with
						// pos or actionId parameter
						if (actionId == ID_DISTANCE) {
							SharedPreferences.Editor editor = preferences.edit();
							editor.putInt("graphID",actionId );
							editor.commit();
							getLineChart();

							// Toast.makeText(getApplicationContext(),
							// "Let's do some search action",
							// Toast.LENGTH_SHORT).show();
						} else if (actionId == ID_SPEED) {
							SharedPreferences.Editor editor = preferences.edit();
							editor.putInt("graphID",actionId );
							editor.commit();
							getLineChart();
							// Toast.makeText(getApplicationContext(),
							// "I have no info this time",
							// Toast.LENGTH_SHORT).show();
						} else if (actionId == ID_ALTITUDE) {
							SharedPreferences.Editor editor = preferences.edit();
							editor.putInt("graphID",actionId );
							editor.commit();
							getLineChart();
							// Toast.makeText(getApplicationContext(),
							// actionItem.getTitle() + " selected",
							// Toast.LENGTH_SHORT).show();
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
		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

	}
	

	public void getLineChart(){
    
    Intent intent = ChartFactory.getLineChartIntent(this, getDemoDataset(), getDemoRenderer());
    startActivity(intent);
    }


private void setChartSettings(XYMultipleSeriesRenderer renderer) {
renderer.setChartTitle("Chart demo");
renderer.setXTitle("x values");
renderer.setYTitle("y values");
renderer.setApplyBackgroundColor(false);
renderer.setRange(new double[] {0,6,-70,40});
renderer.setFitLegend(false);
renderer.setAxesColor(Color.BLACK);
renderer.setShowGrid(true);
renderer.setXAxisMin(0.5);
renderer.setXAxisMax(10.5);
renderer.setYAxisMin(0);
renderer.setZoomEnabled(false);
renderer.setYAxisMax(30);
	}	


private XYMultipleSeriesDataset getDemoDataset() {

	   
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

   
	
    XYSeries firstSeries = new XYSeries("Sample series One");
    for (int i = 0; i < 9; i++)
      firstSeries.add(i, seriesFirstY[i]);
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
        renderer.setMargins(new int[] {40, 20, 20, 0});
        r.setPointStyle(PointStyle.CIRCLE);
         r.setFillPoints(true);
        r.setColor(Color.BLUE);
        renderer.addSeriesRenderer(r);
        return renderer;
}

	
	
}