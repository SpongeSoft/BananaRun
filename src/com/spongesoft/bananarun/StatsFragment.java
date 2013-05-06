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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spongesoft.bananarun.R;

public class StatsFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	int[] firstData={23,56};
	
	
	
	
	/**
	 * Buttons and whatnot
	 */
	DBManagement entry;
	double [][] arr;

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
		final View HomeView = inflater
				.inflate(R.layout.stats, container, false);
		entry.open();
		
		arr=entry.getRaceParam((long )1,1);
		
		Log.d("valor",""+arr.length);
		
		Button genStats = (Button) HomeView.findViewById(R.id.generalStats);
		
		genStats.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				 getBarChart();
			}
		 });
		
		Button carrera1 = (Button) HomeView.findViewById(R.id.session1);
		
		carrera1.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(), ListGraphsActivity.class);
				HomeView.getContext().startActivity(newSession);
			}
		 });
		
		Button carrera2 = (Button) HomeView.findViewById(R.id.session2);
		
		carrera2.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(), ListGraphsActivity.class);
				HomeView.getContext().startActivity(newSession);
			}
		 });
		
		Button carrera3=(Button) HomeView.findViewById(R.id.session3);
		
		carrera3.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(), ListGraphsActivity.class);
				HomeView.getContext().startActivity(newSession);
			}
		 });
		
		Button carrera4 = (Button) HomeView.findViewById(R.id.session4);
		
		carrera4.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent newSession = new Intent(getActivity().getBaseContext(), ListGraphsActivity.class);
				HomeView.getContext().startActivity(newSession);
			}
		 });
		

		/*TextView tv = (TextView) HomeView.findViewById(R.id.tvSQLinfo);
		entry.open();
		entry.setRace();
		String result = entry.getRaceAvgSpeed();
		tv.setText(result);

		
		
		//
		TextView pref = (TextView) HomeView.findViewById(R.id.pref);
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
		String height = settings.getString("prefUserHeight", "No val");
		String weight = settings.getString("prefUserWeight", "0");
		pref.setText("vals: " + height + ", " + weight);
		
		*/
		
		return HomeView;
	}
	
	public void getBarChart(){
	    XYMultipleSeriesRenderer barChartRenderer = getBarChartRenderer();
	    setBarChartSettings(barChartRenderer);
	    Intent intent = ChartFactory.getBarChartIntent(getActivity().getBaseContext(), getBarDemoDataset(), barChartRenderer, Type.DEFAULT);
	    startActivity(intent);
	    }
	
	 private XYMultipleSeriesDataset getBarDemoDataset() {
	        XYMultipleSeriesDataset barChartDataset = new XYMultipleSeriesDataset();
	        CategorySeries firstSeries = new CategorySeries("Growth of Company1");
            CategorySeries secondSeries = new CategorySeries("Growth of Company2");
            for(int i=0;i<firstData.length;i++)
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
	        renderer.setMargins(new int[] {20, 30, 15, 0});
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
