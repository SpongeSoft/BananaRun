package com.spongesoft.bananarun;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.spongesoft.bananarun.R;

/**
 * A dummy fragment representing the main.xml layout
 */
public class MapSectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public final int MODE_PRIVATE = 0;
	public static final String ARG_SECTION_NUMBER = "section_number";
	private GoogleMap mMap;
	private MapView mMapView;
	private Marker mMarker;
	private Circle mCircle;
	private Polyline mPolyline;
<<<<<<< Updated upstream

=======
	
	private DBManagement manager;
>>>>>>> Stashed changes
	private int layer;

	ImageView lockBtn;
	ImageView layerBtn;
	ImageView locateBtn;

	SharedPreferences generalPrefs;
	Handler handler;
	MapController mc;

	/* Position variables */
	public double latitude;
	public double longitude;

	/* Weather values to be retrieved */
	public String temperature;
	public String weatherCode;

	/* Variables to store parseable query results */
	Document weatherDoc;
	String weatherString;

	final String yahooPlaceApisBase = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text%3D%22";
	final String yahooapisFormat = "%22%20and%20gflags%3D%22R%22&diagnostics=true";
	String yahooPlaceAPIsQuery;

	public MapSectionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
<<<<<<< Updated upstream
		final View inflatedView = inflater.inflate(R.layout.main, container,
				false);

		layer = GoogleMap.MAP_TYPE_NORMAL; // Set the initial map type
=======
		manager = new DBManagement(getActivity());
		final View inflatedView = inflater.inflate(R.layout.main, container, false);
>>>>>>> Stashed changes

		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO handle this situation
		}

		temperature = "?"; // Default temperature value

		mMapView = (MapView) inflatedView.findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);

		lockBtn = (ImageView) inflatedView.findViewById(R.id.lockButton);
		layerBtn = (ImageView) inflatedView.findViewById(R.id.layerButton);
		locateBtn = (ImageView) inflatedView.findViewById(R.id.locationButton);

		/* Buttons behaviour */

		lockBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Lock fragment movement
			}
		});

		// TODO -->
		// http://stackoverflow.com/questions/4146848/how-would-i-make-my-google-map-zoom-in-to-my-current-location

		/*
		 * Layer button: toggle map layer type when pressed. There are two layer
		 * types: Normal (streets only, no satellite) and Hybrid (streets and
		 * satellite view).
		 */
		layerBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (layer == GoogleMap.MAP_TYPE_NORMAL) {
					layer = GoogleMap.MAP_TYPE_HYBRID;
					mMap.setMapType(layer);
					Log.d("onClickMap", "Layer type is: HYBRID");
				} else if (layer == GoogleMap.MAP_TYPE_HYBRID) {
					layer = GoogleMap.MAP_TYPE_NORMAL;
					mMap.setMapType(layer);
					Log.d("onClickMap", "Layer type is: NORMAL");
				}
				update_map();
			}
		});

		locateBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				int lat = (int) (latitude * 1E6);
				int lng = (int) (longitude * 1E6);
				GeoPoint point = new GeoPoint(lat, lng);
				mc.setCenter(point);
				mMapView.invalidate();
				*/
				
				LatLng position = new LatLng(latitude, longitude);
				mMarker.setPosition(position);
				mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17.0f));
			}
		});

		/*
		 * Preferences must be initialised here. Otherwise, we get a
		 * NullPointerException error
		 */
		generalPrefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		/* Check Internet status */
		final ConnectivityManager con = (ConnectivityManager) getActivity()
				.getSystemService(
						getActivity().getBaseContext().CONNECTIVITY_SERVICE);

		/* Asking Yahoo! every 10 minutes for weather status */
		handler = new Handler();
		Runnable runnable = new Runnable() {
			public void run() {
				if ((con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
						.getState() == NetworkInfo.State.CONNECTED)
						|| (con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
								.getState() == NetworkInfo.State.CONNECTED)) {
					new MyQueryYahooPlaceTask().execute();
				}

				handler.postDelayed(this, 600000); // for interval (10 min)...
			}
		};
		handler.postDelayed(runnable, 2000); // for initial delay..

		setUpMapIfNeeded(inflatedView);

		return inflatedView;
	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (mMap == null) {
			mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
			if (mMap != null) {
				mMarker = mMap.addMarker(new MarkerOptions().position(
						new LatLng(0, 0)).title("Marker"));
				mMap.setMapType(layer); // Apply layer to map
				Log.d("mMap config", "mMap has layer type = " + layer);

				// Instantiates a new CircleOptions object and defines the
				// center and radius
				// CircleOptions circleOptions = new CircleOptions()
				// .center(new LatLng(37.4, -122.1))
				// .radius(1000); // In meters

				// Get back the mutable Circle
				// mCircle = mMap.addCircle(circleOptions);

				PolylineOptions rectOptions = new PolylineOptions();
				// //.add(new LatLng(0,0));
				// Get back the mutable Polyline
				mPolyline = mMap.addPolyline(rectOptions);

				startLocation();
			}
		}

	}

	private void update_map() {
		//Get the race_id
		long race = getArguments().getLong("race_id");
		if(race != 0L) {
			manager.open();
			//TODO: If it's indeed a race, go and get its points
			List<LatLng> points = manager.getPoints(race);
			mPolyline.setPoints(points);
			Log.d("map", "updating map, "+points.size()+" points ("+points.get(0).latitude+")");
			mMap.animateCamera(CameraUpdateFactory.newLatLng(points.get(0)));

			manager.close();
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
		// List<LatLng> points = mPolyline.getPoints();
		LatLng position = new LatLng(location.getLatitude(),
				location.getLongitude());
		mMarker.setPosition(position);
		// mCircle.setCenter(position);
		// mCircle.setRadius(accuracy);
		// points.add(position);
		// mPolyline.setPoints(points);
		mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
		// LatLngBounds.Builder builder = LatLngBounds.builder();

		// for (LatLng point : points) {
		// builder.include(point);
		// }

		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17.0f));
	}

	public void startLocation() {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {

				/* Retrieve current position */
				latitude = location.getLatitude();
				longitude = location.getLongitude();

				/*
				 * boolean updateFlag = generalPrefs.getBoolean("updateFlag",
				 * false); /* Once the current position is determined, find its
				 * corresponding WOEID. The WOEID is a unique value that Yahoo
				 * uses to identify each place. This identifier will be used to
				 * query the weather of the current location.
				 */
				/*
				 * if(updateFlag) new MyQueryYahooPlaceTask().execute();
				 */

				// Called when a new location is found by the network location
				// provider.
				addLocation(location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

	}

	/*
	 * This class defines an asynchronous task for the application to query an
	 * HTTP petition and retrieve the WOEID identifier of the current location.
	 */
	public class MyQueryYahooPlaceTask extends AsyncTask<Void, Void, Void> {

		String woeid; // WOEID identifier

		@Override
		protected Void doInBackground(Void... arg0) {

			/* Retrieve WOEID */
			woeid = QueryYahooPlaceAPIs();
			return null;
		}

		/* After retrieving WOEID value, obtain the current position's weather */
		@Override
		protected void onPostExecute(Void result) {

			if (woeid != null) {
				try {

					/* Start asynchronous task to retrieve weather */
					GetWeather weather = new GetWeather(woeid);

					/*
					 * By the end of the newly started task, the temperature and
					 * weather code variables will have a value. Wait for that
					 * thread to finish completely. Then, manipulate its
					 * results.
					 */
					weather.waitForResult();

					/*
					 * Get temperature and weather code values from GetWeather
					 * object
					 */
					temperature = weather.temperature;
					weatherCode = weather.code;
					/*
					 * This variable holds the image resource ID to be loaded as
					 * the weather icon
					 */
					int code = getWeatherStatus(weatherCode);
					Log.d("MapSectionFragment", "code: " + code);
					/*
					 * Put the final values in the Activity's intent so that
					 * other fragments can access these variables
					 */
					SharedPreferences.Editor editor = generalPrefs.edit();

					editor.putString("temperature", temperature);
					editor.putInt("code", code);
					editor.commit();
					// getActivity().getIntent().putExtra("temperature",
					// temperature);
					// getActivity().getIntent().putExtra("code", code);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity().getBaseContext(), "No WOEID!",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}

	/*
	 * Given an HTTP query string, open an HTTP connection and send the query.
	 * Retrieve and return the HTTP response.
	 */
	private String QueryYahooWeather(String queryString) {
		String qResult = "";

		/* Objects used to send and receive HTTP messages */
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(queryString);

		try {
			/* Send HTTP message */
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

			/* Read the HTTP response */
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();

				String stringReadLine = null;

				while ((stringReadLine = bufferedreader.readLine()) != null) {
					stringBuilder.append(stringReadLine + "\n");
				}

				qResult = stringBuilder.toString();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return qResult;
	}

	/*
	 * This method creates the HTTP query for the WOEID identifier. The XML
	 * response is parsed and the WOEID value is retrieved.
	 */
	private String QueryYahooPlaceAPIs() {

		String uriPlace = String.valueOf(latitude) + "%2C"
				+ String.valueOf(longitude);

		/* Build up HTTP query string */
		yahooPlaceAPIsQuery = yahooPlaceApisBase + uriPlace + yahooapisFormat;

		// Log.d("QueryYahooPlaceAPIs", "uriPlace "+uriPlace);
		// Log.d("QueryYahooWeather",yahooPlaceAPIsQuery);

		/* Query HTTP petition for current location */
		String woeidString = QueryYahooWeather(yahooPlaceAPIsQuery);

		/* Parse JSON response and return WOEID value */
		Document woeidDoc = convertStringToDocument(woeidString);
		return parseWOEID(woeidDoc);
	}

	/* JSON parser to retrieve the WOEID identifier from the HTTP response */
	private String parseWOEID(Document srcDoc) {
		String listWOEID = "";
		String error;
		NodeList queryNodelist = srcDoc.getElementsByTagName("query");
		if (queryNodelist != null && queryNodelist.getLength() > 0) {
			Node nNode = queryNodelist.item(0);
			Element eElement = (Element) nNode;
			error = eElement.getAttribute("yahoo:count");
			Log.d("YAHOO:COUNT", "error: " + error);

		} else {
			error = "0";
			Log.d("YAHOO:COUNT", "error: " + error);

		}

		/* Check Yahoo PlaceFinder consistency */
		if (error.equals("0")) {
			/* City not found! */
			listWOEID = "";
			Log.d("parseWOEID", "NO WOEID");
			return listWOEID;
		}

		// <description>Yahoo! Weather for New York, NY</description>
		NodeList descNodelist = srcDoc.getElementsByTagName("Result");
		for (int temp = 0; temp < descNodelist.getLength(); temp++) {

			Node nNode = descNodelist.item(temp);
			Element eElement = (Element) nNode;
			listWOEID = eElement.getElementsByTagName("woeid").item(0)
					.getTextContent();
		}

		return listWOEID;
	}

	private Document convertStringToDocument(String src) {
		Document dest = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
			dest.getDocumentElement().normalize();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dest;
	}

	/*
	 * Retrieve the Image Resource ID given a weather code (whose value is
	 * defined by Yahoo)
	 */
	public int getWeatherStatus(String codeStr) {
		int code;
		if (codeStr == null) {
			code = 3200;
		} else {
			code = Integer.parseInt(codeStr);
		}
		int imageCode;
		switch (code) {

		case 0:
			imageCode = R.drawable.img0;
			break;
		case 1:
			imageCode = R.drawable.img1;
			break;
		case 2:
			imageCode = R.drawable.img2;
			break;
		case 3:
			imageCode = R.drawable.img3;
			break;
		case 4:
			imageCode = R.drawable.img4;
			break;
		case 5:
			imageCode = R.drawable.img5;
			break;
		case 6:
			imageCode = R.drawable.img6;
			break;
		case 7:
			imageCode = R.drawable.img7;
			;
			break;
		case 8:
			imageCode = R.drawable.img8;
			break;
		case 9:
			imageCode = R.drawable.img9;
			break;
		case 10:
			imageCode = R.drawable.img10;
			break;
		case 11:
			imageCode = R.drawable.img11;
			break;
		case 12:
			imageCode = R.drawable.img12;
			break;
		case 13:
			imageCode = R.drawable.img13;
			break;
		case 14:
			imageCode = R.drawable.img14;
			break;
		case 15:
			imageCode = R.drawable.img15;
			break;
		case 16:
			imageCode = R.drawable.img16;
			break;
		case 17:
			imageCode = R.drawable.img17;
			break;
		case 18:
			imageCode = R.drawable.img18;
			break;
		case 19:
			imageCode = R.drawable.img19;
			break;
		case 20:
			imageCode = R.drawable.img20;
			break;
		case 21:
			imageCode = R.drawable.img21;
			break;
		case 22:
			imageCode = R.drawable.img22;
			break;
		case 23:
			imageCode = R.drawable.img23;
			break;
		case 24:
			imageCode = R.drawable.img24;
			break;
		case 25:
			imageCode = R.drawable.img25;
			break;
		case 26:
			imageCode = R.drawable.img26;
			break;
		case 27:
			imageCode = R.drawable.img27;
			break;
		case 28:
			imageCode = R.drawable.img28;
			break;
		case 29:
			imageCode = R.drawable.img29;
			break;
		case 30:
			imageCode = R.drawable.img30;
			break;
		case 31:
			imageCode = R.drawable.img31;
			break;
		case 32:
			imageCode = R.drawable.img32;
			break;
		case 33:
			imageCode = R.drawable.img33;
			break;
		case 34:
			imageCode = R.drawable.img34;
			break;
		case 35:
			imageCode = R.drawable.img35;
			break;
		case 36:
			imageCode = R.drawable.img36;
			break;
		case 37:
			imageCode = R.drawable.img37;
			break;
		case 38:
			imageCode = R.drawable.img38;
			break;
		case 39:
			imageCode = R.drawable.img39;
			break;
		case 40:
			imageCode = R.drawable.img40;
			break;
		case 41:
			imageCode = R.drawable.img41;
			break;
		case 42:
			imageCode = R.drawable.img42;
			break;
		case 43:
			imageCode = R.drawable.img43;
			break;
		case 44:
			imageCode = R.drawable.img44;
			break;
		case 45:
			imageCode = R.drawable.img45;
			break;
		case 46:
			imageCode = R.drawable.img46;
			break;
		case 47:
			imageCode = R.drawable.img47;
			break;
		case 3200:
			imageCode = R.drawable.img3200;
			break;

		default:
			imageCode = R.drawable.img3200;
			break;
		}

		return imageCode;
	}

}