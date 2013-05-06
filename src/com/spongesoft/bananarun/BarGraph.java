package com.spongesoft.bananarun;

import android.os.Bundle;
import android.app.Activity;
import com.spongesoft.bananarun.R;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

public class BarGraph  extends Activity {




	int[] firstData={23,145,67,78,86,190,46,78,167,164};
	int[] secondData={83,45,168,138,67,150,64,87,144,188};
	@Override
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*Button genBarChart=(Button)findViewById(R.id.generateBarChart);
		genBarChart.setOnClickListener(new OnClickListener(){
		            public void onClick(View v) {
		                getBarChart();
		            }
		        });*/
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void getBarChart(){
	    XYMultipleSeriesRenderer barChartRenderer = getBarChartRenderer();
	    setBarChartSettings(barChartRenderer);
	    Intent intent = ChartFactory.getBarChartIntent(this, getBarDemoDataset(), barChartRenderer, Type.DEFAULT);
	    startActivity(intent);
	    }
	 
	 private XYMultipleSeriesDataset getBarDemoDataset() {
	        XYMultipleSeriesDataset barChartDataset = new XYMultipleSeriesDataset();
	                CategorySeries firstSeries = new CategorySeries("Growth of Company1");
	                for(int i=0;i<firstData.length;i++)
	                    firstSeries.add(firstData[i]);
	                barChartDataset.addSeries(firstSeries.toXYSeries());
	         
	             CategorySeries secondSeries = new CategorySeries("Growth of Company2");
	                for(int j=0;j<secondData.length;j++)
	                    secondSeries.add(secondData[j]);
	                barChartDataset.addSeries(secondSeries.toXYSeries());
	        return barChartDataset;
	      }
	  
	 public XYMultipleSeriesRenderer getBarChartRenderer() {
	        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	        renderer.setAxisTitleTextSize(20);
	        renderer.setChartTitleTextSize(18);
	        renderer.setLabelsTextSize(18);
	        renderer.setLegendTextSize(18);
	        renderer.setMargins(new int[] {20, 30, 15, 0});
	        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	        r.setColor(Color.BLUE);
	        renderer.addSeriesRenderer(r);
	        r = new SimpleSeriesRenderer();
	        r.setColor(Color.GREEN);
	        renderer.addSeriesRenderer(r);
	        return renderer;
	      }
	 private void setBarChartSettings(XYMultipleSeriesRenderer renderer) {
	        renderer.setChartTitle("Growth comparison company1 vs company2");
	        renderer.setXTitle("No of Years in industry");
	        renderer.setYTitle("Profit in millions");
	        renderer.setXAxisMin(0.5);
	        renderer.setXAxisMax(10.5);
	        renderer.setYAxisMin(0);
	        renderer.setYAxisMax(210);
	      }

}
