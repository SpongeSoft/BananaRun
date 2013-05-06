package com.spongesoft.bananarun;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import com.spongesoft.bananarun.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class LineChart extends Activity {
	public static final String TYPE = "type";
	private XYMultipleSeriesDataset mDataset = getDemoDataset();
	private XYMultipleSeriesRenderer mRenderer = getDemoRenderer();
	private GraphicalView mChartView;
	DBManagement entry;
	double[][] arr;
	SharedPreferences preferences;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line);

		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		entry = new DBManagement(this);
		entry.open();
		/*Intent currentIntent = getIntent();
		double id_race = currentIntent.getDoubleExtra("statsID", -1.0);
*/		
		long id_race  = preferences.getLong("statsID", -1);
		double graph_type = (double)preferences.getInt("graphID", -1);

		Log.d("valor", id_race + " , " + graph_type);
		arr = entry.getRaceParam( id_race, (int) graph_type);
		Log.d("valor", "" + arr.length);
		entry.close();

		setRendererStyling();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getLineChartView(this, mDataset,
					mRenderer);
			//mRenderer.setSelectableBuffer(100);
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else
			mChartView.repaint();
	}

	private void setRendererStyling() {
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
		mRenderer.setAxisTitleTextSize(16);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(15);
		mRenderer.setMargins(new int[] { 40, 20, 20, 0 });
		mRenderer.setPointSize(10);
	}

	private XYMultipleSeriesDataset getDemoDataset() {

		   
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
 
        XYSeries firstSeries = new XYSeries("Sample series One");
		if (arr != null) {
			for (int i = 0; i < arr.length; i++)
			firstSeries.add(i, arr[i][0]);
	        dataset.addSeries(firstSeries);
	 
		}
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
