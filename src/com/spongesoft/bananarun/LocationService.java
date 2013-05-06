package com.spongesoft.bananarun;

import com.spongesoft.bananarun.R;
import com.spongesoft.bananarun.R.drawable;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
	Location previousLocation;
	boolean running = true;
	DBManagement manager;
	double race;
	   @Override
	    public void onCreate() {
           final IntentFilter myFilter = new IntentFilter("com.spongesoft.bananarun.TO_SERVICE");
           registerReceiver(mReceiver, myFilter);
	    }
	@Override
	public void onLocationChanged(Location loc) {
		if(running) {
		Toast.makeText(this,"Lat: " + String.valueOf(loc.getLatitude()) + " Long: " + String.valueOf(loc.getLongitude()),Toast.LENGTH_SHORT).show();
		if(previousLocation == null) {
			manager.setLocation((long) race, loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), 0, loc.getSpeed());
		}else{
			manager.setLocation((long) race, loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), loc.distanceTo(previousLocation), loc.getSpeed());
		}
		previousLocation = loc;
		

        final Intent intent = new Intent("com.spongesoft.bananarun.LOCATION_UPDATED");
        intent.putExtra("data", loc);
        sendBroadcast(intent);
		}
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
	    Bundle extras = intent.getExtras();
	    race = extras.getInt("race_id");
	    
        Toast.makeText(this, "Service started, race id: "+race, Toast.LENGTH_LONG).show();
        manager = new DBManagement(this);
        createNotification();
        manager.open();
        
        run();
       
		return START_STICKY;
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
	      	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	 }
	 
	 
	 private void createNotification() {
		 Intent notificationIntent = new Intent(this, SessionActivity.class);
		 notificationIntent.putExtra("stopNotif", true);
		 notificationIntent.putExtra("race_id", (int) race);
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
	 
	 private void stopReceiving() {
		 locationManager.removeUpdates(this);
	 }
	 
	 

		private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		            public void onReceive(Context context, Intent intent) {		
		                        Log.d("BroadcastService", "Service received: " + intent.getCharSequenceExtra("data"));
		                        String message = intent.getStringExtra("data");
		                        if(message.equals("stop")) {
		                        	//Stop the service and mark the race as over
		                        	manager.updateRace((long) race);
		                        	manager.close();
		                        	running = false;
		                        	stopReceiving();
		                        }

		            }
		
		};
		
		public void onDestroy() {

            unregisterReceiver(mReceiver);

            super.onDestroy();

		}
	 
}
