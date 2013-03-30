package com.spongesoft.dietapp;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service implements LocationListener {
	LocationManager locationManager;
	
	   @Override
	    public void onCreate() {
	        Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
	        createNotification();
	        run();
	    }
	@Override
	public void onLocationChanged(Location loc) {
		Toast.makeText(this,"Lat: " + String.valueOf(loc.getLatitude()) + " Long: " + String.valueOf(loc.getLongitude()),Toast.LENGTH_SHORT).show();

		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	 private void run(){

	        final Criteria criteria = new Criteria();

	        criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        criteria.setSpeedRequired(true);
	        criteria.setAltitudeRequired(false);
	        criteria.setBearingRequired(false);
	        criteria.setCostAllowed(true);
	        criteria.setPowerRequirement(Criteria.POWER_LOW);
	        //Acquire a reference to the system Location Manager
	        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	        // Define a listener that responds to location updates
	      	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	 }
	 
	 
	 private void createNotification() {
		 Intent notificationIntent = new Intent(this, MainActivity.class);
		 notificationIntent.putExtra("stopNotif", true);
		 PendingIntent contentIntent = PendingIntent.getActivity(this,
		         12345, notificationIntent,
		         PendingIntent.FLAG_CANCEL_CURRENT);

		 NotificationManager nm = (NotificationManager) this
		         .getSystemService(Context.NOTIFICATION_SERVICE);

		 Resources res = this.getResources();
		 Notification.Builder builder = new Notification.Builder(this);

		 builder.setContentIntent(contentIntent)
		             .setSmallIcon(R.drawable.preferences_button)
		             //.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.some_big_img))
		             .setTicker("Started running!")
		             .setWhen(System.currentTimeMillis())
		             .setAutoCancel(true)
		             .setContentTitle("Running!")
		             .setContentText("Click to stop.");
		 Notification n = builder.getNotification();

		 nm.notify(143214321, n);
		 

		 
		 startForeground(143214321,n);
	 }
	 
}
