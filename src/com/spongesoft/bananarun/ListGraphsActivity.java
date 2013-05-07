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
	DBManagement entry;
	double[][] arr;
	long id_race ; 
	private XYMultipleSeriesRenderer mRenderer = getDemoRenderer();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selectgraph);
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		entry = new DBManagement(this);
	
		/*Intent currentIntent = getIntent();
		double id_race = currentIntent.getDoubleExtra("statsID", -1.0);
*/		
		final long id_race  = preferences.getLong("statsID", -1);
		

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
						entry.open();
						Log.d("actionId", actionId+"");
						// here we can filter which action item was clicked with
						// pos or actionId parameter
						if (actionId == ID_DISTANCE) {
												
							arr = entry.getRaceParam(id_race, actionId);
							Log.d("arr",arr+"");
							if(arr!=null && arr[1][0]!=0.0){
							String title_serie="Distance in ";
							getLineChart(actionItem.getTitle(),title_serie);
							}
							else{
								Toast.makeText(getApplicationContext(), "Registro vacio", Toast.LENGTH_SHORT).show();
							}

							entry.close();
						} else if (actionId == ID_SPEED) {
											
							arr = entry.getRaceParam(id_race, actionId);
							Log.d("actionId", arr[1][0]+"");
							
							if(arr!=null && arr[1][0]!=0.0){
							String title_serie="Speed in ";
							getLineChart(actionItem.getTitle(),title_serie);
							}
							else{
								Toast.makeText(getApplicationContext(), "Registro vacio", Toast.LENGTH_SHORT).show();
							}
							entry.close();
							
						} else if (actionId == ID_ALTITUDE) {
												
							arr = entry.getRaceParam(id_race, actionId);
							
							if(arr!=null && arr[1][0]!=0.0){
							String title_serie="Altitude in ";
							getLineChart(actionItem.getTitle(),title_serie);
							}
							else{
								Toast.makeText(getApplicationContext(), "Registro vacio", Toast.LENGTH_SHORT).show();
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
		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

	}
	

	public void getLineChart(String name,String title){
	setLineSettings(mRenderer,name); 
    Intent intent = ChartFactory.getLineChartIntent(this, getDemoDataset(title), mRenderer);
    startActivity(intent);
    }


private void setLineSettings(XYMultipleSeriesRenderer renderer,String name) {
renderer.setChartTitle(name);
renderer.setApplyBackgroundColor(false);
//renderer.setRange(new double[] {0,6,-70,40});
renderer.setFitLegend(false);
renderer.setAxesColor(Color.BLACK);
renderer.setShowGrid(true);
renderer.setXAxisMin(0.5);
//renderer.setXAxisMax(setmaxvalueX(arr));
renderer.setYAxisMin(0);
renderer.setZoomEnabled(false);
renderer.setZoomButtonsVisible(true);
renderer.setYAxisMax(setmaxvalueY(arr));
	}	


private XYMultipleSeriesDataset getDemoDataset(String title) {

	   
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

   
	
    XYSeries firstSeries = new XYSeries(title);
    for (int i = 0; i < arr.length; i++)
      firstSeries.add(i, arr[i][0]);
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
public int setmaxvalueY( double arr[][]){
	 double max_value=arr[0][0];
	 for (int i = 1; i < arr.length; i++){
		 if(max_value<arr[i][0]){
			 max_value=arr[i][0];
		 }
		 
		 
	 }
	
	return (int)max_value;
	
}
public int setmaxvalueX( double arr[][]){
	 double max_value=arr[0][0];
	 for (int i = 1; i < arr.length; i++){
		 if(max_value<arr[i][1]){
			 max_value=arr[i][1];
		 }
		 
		 
	 }
	
	return (int)max_value;
	
}
	
	
}