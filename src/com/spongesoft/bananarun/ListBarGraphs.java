package com.spongesoft.bananarun;


import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.spongesoft.bananarun.LineChart;
import com.spongesoft.bananarun.MainActivity;
import com.spongesoft.bananarun.R;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.Toast;


public class ListBarGraphs extends Activity {
	//action id
	private static final int ID_UP     = 1;
	private static final int ID_DOWN   = 2;
	private static final int ID_SEARCH = 3;
	private static final int ID_INFO   = 4;
	private static final int ID_ERASE  = 5;	
	private static final int ID_OK     = 6;
	int[] firstData={23,36,18,18};
	
	DBManagement entry;
	double[][] arr;
	
	 
  		
  		
  	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selectgraph);
		entry = new DBManagement(this);
		
		Intent currentIntent=getIntent();
		double id_race=currentIntent.getDoubleExtra("statsID", -1.0);
		
		

		ActionItem nextItem 	= new ActionItem(ID_DOWN, "Time", getResources().getDrawable(R.drawable.menu_ok));
		ActionItem prevItem 	= new ActionItem(ID_UP, "Average Speed", getResources().getDrawable(R.drawable.menu_ok));
        ActionItem searchItem 	= new ActionItem(ID_SEARCH, "Distance", getResources().getDrawable(R.drawable.menu_ok));
        ActionItem item	= new ActionItem(ID_OK, "Kcal", getResources().getDrawable(R.drawable.menu_ok));
        
        //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
        prevItem.setSticky(true);
        nextItem.setSticky(true);
		
		//create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout 
        //orientation
		final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
		
		//add action items into QuickAction
        quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
        quickAction.addActionItem(searchItem);
        quickAction.addActionItem(item);
        
      
        
    	
        //Set listener for action item clicked
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				ActionItem actionItem = quickAction.getActionItem(pos);
				entry.open();
                 
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_SEARCH) {
					arr = entry.getSessionsParam(0);
					
					if(arr!=null){
						getBarChart(actionItem.getTitle());
						}
						else{
							Toast.makeText(getApplicationContext(), "Registro vacio", Toast.LENGTH_SHORT).show();
						}
				} else if (actionId == ID_DOWN) {
					arr = entry.getSessionsParam(1);
					
					if(arr!=null){
					getBarChart(actionItem.getTitle());
					}
					else{
						Toast.makeText(getApplicationContext(), "Registro vacio", Toast.LENGTH_SHORT).show();
					}
					
				} else if (actionId == ID_UP){
					arr = entry.getSessionsParam(2);
					
					if(arr!=null){
						getBarChart(actionItem.getTitle());
						}
						else{
							Toast.makeText(getApplicationContext(), "Registro vacio", Toast.LENGTH_SHORT).show();
						}
				}
				else if (actionId == ID_OK){
					arr = entry.getSessionsParam(3);
					
					if(arr!=null){
						getBarChart(actionItem.getTitle());
						}
						else{
							Toast.makeText(getApplicationContext(), "Registro vacio", Toast.LENGTH_SHORT).show();
						}
			}
				entry.close();
			}
		});
		
		//set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
		//by clicking the area outside the dialog.
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
			@Override
			public void onDismiss() {
				/*Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();*/
			}
		});
		
		//show on btn1
		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

		
	}
	
	public void getBarChart(String name){
	    XYMultipleSeriesRenderer barChartRenderer = getBarChartRenderer();
	    setBarChartSettings(barChartRenderer,name);
	    Intent intent = ChartFactory.getBarChartIntent(this, getBarDemoDataset(), barChartRenderer, Type.DEFAULT);
	    startActivity(intent);
	    }
	 
	 private XYMultipleSeriesDataset getBarDemoDataset() {
	        XYMultipleSeriesDataset barChartDataset = new XYMultipleSeriesDataset();
	                CategorySeries firstSeries = new CategorySeries("Session");
	                for(int i=0;i<arr.length;i++)
	                    firstSeries.add(arr[i][0]);
	                barChartDataset.addSeries(firstSeries.toXYSeries());
	         
	            
	        return barChartDataset;
	      }
	  
	 public XYMultipleSeriesRenderer getBarChartRenderer() {
	        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	        renderer.setAxisTitleTextSize(20);
	        renderer.setChartTitleTextSize(18);
	        renderer.setLabelsTextSize(18);
	        renderer.setLegendTextSize(18);
	        renderer.setMargins(new int[] {25, 30, 0, 25});
	        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	       
	        r.setColor(Color.BLUE);
	        renderer.addSeriesRenderer(r);
	        return renderer;
	      }
	 private void setBarChartSettings(XYMultipleSeriesRenderer renderer,String name) {
		 	renderer.setChartTitle(name);
	        //renderer.setXTitle("Session");
	        renderer.setYTitle(name);
	        //renderer.setShowLegend(false);
	        //renderer.setXAxisMin(0.5);
	        renderer.setXAxisMax(10);
	        renderer.setYAxisMin(0);
	        renderer.setYAxisMax(50);
	      }
}