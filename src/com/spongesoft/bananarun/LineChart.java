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
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class LineChart extends Activity {
	public static final String TYPE = "type";
	private XYMultipleSeriesDataset mDataset = getDemoDataset();
	private XYMultipleSeriesRenderer mRenderer = getDemoRenderer();
	private GraphicalView mChartView;
	DBManagement entry;
	double[][] arr;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line);

		entry = new DBManagement(this);
		entry.open();
		Intent currentIntent = getIntent();
		double id_race = currentIntent.getDoubleExtra("statsID", -1.0);
		
		Intent nextIntent = getIntent();
		double graph_type = nextIntent.getDoubleExtra("graphID", -1.0);

		arr = entry.getRaceParam((long) id_race,(int) graph_type);
		entry.close();

		setRendererStyling();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getLineChartView(this, mDataset,
					mRenderer);
			mRenderer.setSelectableBuffer(100);
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

		CategorySeries firstSeries = new CategorySeries("Session");
        for(int i=0;i<arr.length;i++)
            firstSeries.add(arr[i][0]);
        dataset.addSeries(firstSeries.toXYSeries());
        
		return dataset;
	}

	private XYMultipleSeriesRenderer getDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setMargins(new int[] { 25, 30, 0, 25 });
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.BLUE);
		r.setPointStyle(PointStyle.CIRCLE);
		// r.setFillBelowLine(true);
		// r.setFillBelowLineColor(Color.WHITE);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);
		renderer.setAxesColor(Color.DKGRAY);
		renderer.setLabelsColor(Color.LTGRAY);
		return renderer;
	}
}
