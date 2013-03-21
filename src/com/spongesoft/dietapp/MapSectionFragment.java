package com.spongesoft.dietapp;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * A dummy fragment representing the main.xml layout
 */
public class MapSectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private GoogleMap mMap; 
	private MapView mMapView;
	private Marker mMarker;
	private Circle mCircle;
	private Polyline mPolyline;
	public MapSectionFragment() {
	}

	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.main, container, false);

        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO handle this situation
        }

        mMapView = (MapView) inflatedView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        
        setUpMapIfNeeded(inflatedView);

        return inflatedView;
    }

	
	
	private void setUpMapIfNeeded(View inflatedView) {
        if (mMap == null) {
            mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
            if (mMap != null) {
            	  mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
            	  
            	// Instantiates a new CircleOptions object and defines the center and radius
            	  CircleOptions circleOptions = new CircleOptions()
            	      .center(new LatLng(37.4, -122.1))
            	      .radius(1000); // In meters

            	  // Get back the mutable Circle
            	  mCircle = mMap.addCircle(circleOptions);
            	  
            	  PolylineOptions rectOptions = new PolylineOptions(); //.add(new LatLng(0,0));
            	  // Get back the mutable Polyline
            	  mPolyline = mMap.addPolyline(rectOptions);
            	  
            	  startLocation();
            }
        }
    }
	
	@Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
    
    public void addLocation(Location location) {
      Log.d("DietApp", "Moving!");
      float accuracy = location.getAccuracy();
      List<LatLng> points = mPolyline.getPoints();
  	  LatLng position = new LatLng(location.getLatitude(),location.getLongitude());
  	  mMarker.setPosition(position);
  	  mCircle.setCenter(position);
  	  mCircle.setRadius(accuracy);
  	  points.add(position);
  	  mPolyline.setPoints(points);  	 
  	  mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
	  	LatLngBounds.Builder builder = LatLngBounds.builder();
	  	
	  	for (LatLng point : points) {
	  		builder.include(point);
	  	}

  	  mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
    }
    public void startLocation() {
    	// Acquire a reference to the system Location Manager
    	LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

    	// Define a listener that responds to location updates
    	LocationListener locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	      // Called when a new location is found by the network location provider.
    	      addLocation(location);
    	    }

    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };

    	// Register the listener with the Location Manager to receive location updates
      	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

}