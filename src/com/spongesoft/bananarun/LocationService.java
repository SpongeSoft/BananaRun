package com.spongesoft.bananarun;

import com.spongesoft.bananarun.R;
import com.spongesoft.bananarun.R.drawable;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service implements LocationListener,
		OnInitListener {
	LocationManager locationManager;
	Location previousLocation;
	boolean running = true;
	DBManagement manager;
	double race;
	int pickerType;
	int pickerValue;
	double[] infoArray;
	String unitsSystem;

	TextToSpeech talker;

	int lastDistanceSaid = 0;


	/* Updates the location every time a location is received
	 */
	@Override
	public void onLocationChanged(Location loc) {
		if (running) { //Is the user running? so we ignore any location received after the service should have been stopped
			if (previousLocation == null) {
				manager.setLocation((long) race, loc.getLatitude(),
						loc.getLongitude(), loc.getAltitude(), 0,
						loc.getSpeed());
			} else {
				manager.setLocation((long) race, loc.getLatitude(),
						loc.getLongitude(), loc.getAltitude(),
						loc.distanceTo(previousLocation), loc.getSpeed());
			}
			previousLocation = loc;

			FinishOnLimit();

			final Intent intent = new Intent(
					"com.spongesoft.bananarun.LOCATION_UPDATED");
			intent.putExtra("data", loc);
			sendBroadcast(intent);

		}
	}

	/* Overrides the onStartCommand method of the service so it does location listener initialization tasks
	 * when the service boots.
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {

		final IntentFilter myFilter = new IntentFilter(
				"com.spongesoft.bananarun.TO_SERVICE");
		registerReceiver(mReceiver, myFilter);

		Bundle extras = intent.getExtras();
		race = extras.getInt("race_id");

		manager = new DBManagement(this);
		createNotification();
		manager.open();

		run();

		talker = new TextToSpeech(this, this);

		return START_REDELIVER_INTENT; 
		// This is so the service doesn't get started more than once but
		// at the same time the onStart gets called with the *full* Intent
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
	public void onDestroy() {
		//Destroy TTS, receiver, and myself
		unregisterReceiver(mReceiver);
		this.stopForeground(true);

		if (talker != null) {
			talker.stop();
			talker.shutdown();
		}

		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void run() {
		//Sets up the location receival criteria
		final Criteria criteria = new Criteria();

		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setSpeedRequired(true);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		//Tries to get any last known location, either from GPS or network
		Location location = locationManager
				.getLastKnownLocation(locationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
		}

		if (location != null) {
			manager.setLocation((long) race, location.getLatitude(),
					location.getLongitude(), location.getAltitude(), 0,
					location.getSpeed());
		}
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		pickerType = prefs.getInt("pickerType", 2);
		pickerValue = prefs.getInt("pickerValue", -1);
		unitsSystem = prefs.getString("prefUnitSystem", "1");

		// Define a listener that responds to location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
	}

	private void createNotification() {
		//Creates the notification which will be used so the service keeps persistent.
		Intent notificationIntent = new Intent(this, SessionActivity.class);
		notificationIntent.putExtra("stopNotif", true);
		notificationIntent.putExtra("race_id", (int) race);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 12345,
				notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationManager nm = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Resources res = this.getResources();
		Notification.Builder builder = new Notification.Builder(this);

		builder.setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker("Now running!").setWhen(System.currentTimeMillis())
				.setAutoCancel(true).setContentTitle("Running!")
				.setContentText(getResources().getString(R.string.servText));
		Notification n = builder.getNotification();
		nm.notify(143214321, n);

		startForeground(143214321, n);
	}

	private void stopReceiving() {
		locationManager.removeUpdates(this);
		this.stopSelf();
	}

	/**
	 * This receiver is used to get "stop" messages from the SessionActivity
	 */
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		public void onReceive(Context context, Intent intent) {
			Log.d("BroadcastService",
					"Service received: " + intent.getCharSequenceExtra("data"));
			String message = intent.getStringExtra("data");
			if (message.equals("stop")) {
				// Stop the service and mark the race as over
				manager.updateRace((long) race);
				manager.close();
				running = false;
				stopReceiving();
			}
		}
	};
	/*
	 * Stops when user has finished
	 */
	private void FinishOnLimit() {

		ContentValues stats = manager.getStats((long) race);
		int distance = stats.getAsInteger(manager.KEY_S_TOTAL_DISTANCE);
		int timeSeconds = stats.getAsInteger(manager.KEY_S_TOTAL_TIME);

		if (pickerType != 2) {

			if (pickerType == 1) {
				if (unitsSystem.equals("1")) {
					distance = distance / 1000;
				} else if (unitsSystem.equals("2")) {
					distance = (int) (distance * 1.609);
				}

				if (distance >= pickerValue) {
					manager.updateRace((long) race);
					manager.close();
					running = false;
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.limitTTS),
							Toast.LENGTH_LONG).show();
					sendBroadcast(new Intent("xyz"));
					stopReceiving();

				}
			} else if (pickerType == 0) {
				int time = timeSeconds / 60;
				if (time >= pickerValue) {
					manager.updateRace((long) race);
					manager.close();
					running = false;
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.limitTTS),
							Toast.LENGTH_LONG).show();
					sendBroadcast(new Intent("xyz"));
					stopReceiving();
				}
			}
		}
		int distanceKm = distance / 1000;
		if (distanceKm > lastDistanceSaid) {
			lastDistanceSaid = distanceKm;
			talker.speak(lastDistanceSaid + " " + getResources().getString(R.string.kmLang),
					TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	@Override
	public void onInit(int arg0) {
		talker.speak("Empezando la carrera. ¡Ánimo!", TextToSpeech.QUEUE_FLUSH,
				null);

	}

}
