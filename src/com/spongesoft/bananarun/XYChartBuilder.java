package com.spongesoft.bananarun;
/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileOutputStream;
import com.spongesoft.dietapp.R;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class XYChartBuilder extends Fragment {
  public static final String TYPE = "type";

  private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

  private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

  private XYSeries mCurrentSeries;

  private XYSeriesRenderer mCurrentRenderer;

  private String mDateFormat;

  private Button mNewSeries;

  private Button mAdd;

  private EditText mX;

  private EditText mY;

  private GraphicalView mChartView;
  
  private int index = 0;
  
  View HomeView;
  private FragmentActivity fa;

  protected void onRestoreInstanceState(Bundle savedState) {
    super.onSaveInstanceState(savedState);
    mDataset = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
    mRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
    mCurrentSeries = (XYSeries) savedState.getSerializable("current_series");
    mCurrentRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");
    mDateFormat = savedState.getString("date_format");
  }

  @Override
public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable("dataset", mDataset);
    outState.putSerializable("renderer", mRenderer);
    outState.putSerializable("current_series", mCurrentSeries);
    outState.putSerializable("current_renderer", mCurrentRenderer);
    outState.putString("date_format", mDateFormat);
  }


  public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
  		super.onCreate(savedInstanceState);
		HomeView = inflater.inflate(R.layout.xy_chart, container, false);

		fa = super.getActivity();
    
    
    mX = (EditText) HomeView.findViewById(R.id.xValue);
    mY = (EditText) HomeView.findViewById(R.id.yValue);
    mRenderer.setApplyBackgroundColor(true);
    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
    mRenderer.setAxisTitleTextSize(16);
    mRenderer.setChartTitleTextSize(20);
    mRenderer.setLabelsTextSize(15);
    mRenderer.setLegendTextSize(15);
    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
    mRenderer.setZoomButtonsVisible(true);
    mRenderer.setPointSize(10);

    mAdd = (Button) HomeView.findViewById(R.id.add);
    mNewSeries = (Button) HomeView.findViewById(R.id.new_series);
    mNewSeries.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
        XYSeries series = new XYSeries(seriesTitle);
        mDataset.addSeries(series);
        mCurrentSeries = series;
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        mCurrentRenderer = renderer;
        setSeriesEnabled(true);
      }
    });

    mAdd.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        double x = 0;
        double y = 0;
        try {
          x = Double.parseDouble(mX.getText().toString());
        } catch (NumberFormatException e) {
          // TODO
          mX.requestFocus();
          return;
        }
        try {
          y = Double.parseDouble(mY.getText().toString());
        } catch (NumberFormatException e) {
          // TODO
          mY.requestFocus();
          return;
        }
        mCurrentSeries.add(x, y);
        mX.setText("");
        mY.setText("");
        mX.requestFocus();
        if (mChartView != null) {
          mChartView.repaint();
        }
        Bitmap bitmap = mChartView.toBitmap();
        try {
          File file = new File(Environment.getExternalStorageDirectory(), "test" + index++ + ".png");
          FileOutputStream output = new FileOutputStream(file);
          bitmap.compress(CompressFormat.PNG, 100, output);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
	return HomeView;
  }

  @SuppressWarnings("deprecation")
@Override
public void onResume() {
    super.onResume();
    if (mChartView == null) {
      LinearLayout layout = (LinearLayout)HomeView.findViewById(R.id.chart);
      mChartView = ChartFactory.getLineChartView(fa, mDataset, mRenderer);
     
      mRenderer.setClickEnabled(true);
      mRenderer.setSelectableBuffer(100);
      mChartView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
          double[] xy = mChartView.toRealPoint(0);
          if (seriesSelection == null) {
            Toast.makeText(fa, "No chart element was clicked", Toast.LENGTH_SHORT)
                .show();
          } else {
            Toast.makeText(
                fa,
                "Chart element in series index " + seriesSelection.getSeriesIndex()
                    + " data point index " + seriesSelection.getPointIndex() + " was clicked"
                    + " closest point value X=" + seriesSelection.getXValue() + ", Y=" + seriesSelection.getValue()
                    + " clicked point value X=" + (float) xy[0] + ", Y=" + (float) xy[1], Toast.LENGTH_SHORT).show();
          }
        }
      });
      mChartView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
          if (seriesSelection == null) {
            Toast.makeText(fa, "No chart element was long pressed",
                Toast.LENGTH_SHORT);
            return false; // no chart element was long pressed, so let something
            // else handle the event
          } else {
            Toast.makeText(fa, "Chart element in series index "
                + seriesSelection.getSeriesIndex() + " data point index "
                + seriesSelection.getPointIndex() + " was long pressed", Toast.LENGTH_SHORT);
            return true; // the element was long pressed - the event has been
            // handled
          }
        }
      });
      mChartView.addZoomListener(new ZoomListener() {
        public void zoomApplied(ZoomEvent e) {
          String type = "out";
          if (e.isZoomIn()) {
            type = "in";
          }
          System.out.println("Zoom " + type + " rate " + e.getZoomRate());
        }
        
        public void zoomReset() {
          System.out.println("Reset");
        }
      }, true, true);
      mChartView.addPanListener(new PanListener() {
        public void panApplied() {
          System.out.println("New X range=[" + mRenderer.getXAxisMin() + ", " + mRenderer.getXAxisMax()
              + "], Y range=[" + mRenderer.getYAxisMax() + ", " + mRenderer.getYAxisMax() + "]");
        }
      });
      layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      boolean enabled = mDataset.getSeriesCount() > 0;
      setSeriesEnabled(enabled);
    } else {
      mChartView.repaint();
    }
  }

  private void setSeriesEnabled(boolean enabled) {
    mX.setEnabled(enabled);
    mY.setEnabled(enabled);
    mAdd.setEnabled(enabled);
  }
}
