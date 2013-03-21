package com.spongesoft.dietapp;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.MapActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MyMapActivity extends MapActivity {
	private GoogleMap mMap; 
	private MapView mMapView;
	private Polyline mPolyline;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    try {
	            MapsInitializer.initialize(this);
	    } catch (GooglePlayServicesNotAvailableException e) {
	            // TODO handle this situation
        }
		setContentView(R.layout.activity_map);
       // mMapView = (MapView) this.findViewById(R.id.map);
       // mMapView.onCreate(savedInstanceState);
        
       // setUpMapIfNeeded();
        
	}
	private void setUpMapIfNeeded() {
	       if (mMap == null) {
	            mMap = ((MapView) findViewById(R.id.map)).getMap();
	            if (mMap != null) {
	            	  //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
	            	  	
	            	  
	            	  //PolylineOptions rectOptions = new PolylineOptions(); //.add(new LatLng(0,0));
	            	  // Get back the mutable Polyline
	            	  //mPolyline = mMap.addPolyline(rectOptions);
	            	  
	            	  //startLocation();
	            }
	        }
	        
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
