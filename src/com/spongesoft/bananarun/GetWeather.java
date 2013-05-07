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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;

public class GetWeather {

	/* Global variables */
	AsyncTask<Void, Void, Void> task;
	String description;
	String city;
	String region;
	String country;

	public String temperature;
	String code;
	String weatherStatus;

	/* Class constructor. When created, the class executes a new thread */
	public GetWeather(String woeid) {
		String sel_woeid = woeid;
		task = new MyQueryYahooWeatherTask(sel_woeid).execute();
	}

	/*
	 * Thread class, that will retrieve the current weather using the WOEID as
	 * the argument
	 */
	private class MyQueryYahooWeatherTask extends AsyncTask<Void, Void, Void> {

		String woeid;
		String weatherString;

		MyQueryYahooWeatherTask(String w) {
			woeid = w;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Build HTTP query and get HTTP result
			weatherString = QueryYahooWeather();

			// Convert result to parseable format
			Document weatherDoc = convertStringToDocument(weatherString);

			if (weatherDoc != null) {
				parseWeather(weatherDoc); // Parse result
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.d("onPostExecute", "temperature" + temperature);
			Log.d("onPostExecute", "weather code: " + code);

			super.onPostExecute(result);
		}

		private String QueryYahooWeather() {
			String qResult = "";
			String queryString = "http://weather.yahooapis.com/forecastrss?w="
					+ woeid + "&u=c"; // HTTP query to Yahoo! Weather

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(queryString);

			/* Send query and get its response */
			try {
				HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

				if (httpEntity != null) {
					InputStream inputStream = httpEntity.getContent();
					Reader in = new InputStreamReader(inputStream);
					BufferedReader bufferedreader = new BufferedReader(in);
					StringBuilder stringBuilder = new StringBuilder();

					String stringReadLine = null;

					while ((stringReadLine = bufferedreader.readLine()) != null) {
						stringBuilder.append(stringReadLine + "\n");
					}

					qResult = stringBuilder.toString(); // HTTP Result
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Log.d("QueryYahooWeather","Result: "+qResult);

			return qResult;
		}

		/*
		 * Given a String variable, convert it to a parseable object. Based on
		 * the code provided in the following link:
		 * http://www.yousaytoo.com/get-
		 * description-from-yahoo-weather-rss-feed-and
		 * -display-in-webview/2083194
		 */
		private Document convertStringToDocument(String src) {
			Document dest = null;

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder parser;

			try {
				parser = dbFactory.newDocumentBuilder();
				dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
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
		 * Method used to parse a Document variable and manipulate its data.
		 * Here, the Document is checked to have the keys and values related to
		 * a Yahoo! Weather valid HTTP result. Based on the code from the
		 * following weppage:
		 * http://android-er.blogspot.com.es/2012/10/search-woeid
		 * -and-query-yahoo-weather.html
		 */
		private void parseWeather(Document srcDoc) {

			// Description tag contains child elements related to errors and
			// number of results
			NodeList descNodelist = srcDoc.getElementsByTagName("description");
			if (descNodelist != null && descNodelist.getLength() > 0) {
				description = descNodelist.item(0).getTextContent();
			} else {
				description = "EMPTY";
			}

			// WOEID not related to a valid location?
			if (description.equals("Yahoo! Weather Error")) {
				temperature = "?";
				Log.d("parseWeather", "City not found!");
				return;
			}

			/*
			 * <yweather:location> tag contains the required information about
			 * the location's current weather (temperature and weather code).
			 */
			NodeList locationNodeList = srcDoc
					.getElementsByTagName("yweather:condition");
			if (locationNodeList != null && locationNodeList.getLength() > 0) {
				Node locationNode = locationNodeList.item(0);
				NamedNodeMap locNamedNodeMap = locationNode.getAttributes();

				temperature = (String) locNamedNodeMap.getNamedItem("temp")
						.getNodeValue().toString();
				code = (String) locNamedNodeMap.getNamedItem("code")
						.getNodeValue().toString();

				Log.d("parseWeather", "temperature=" + temperature);
				weatherStatus = locNamedNodeMap.getNamedItem("text")
						.getNodeValue().toString();
			} else {
				city = "EMPTY";
				region = "EMPTY";
				country = "EMPTY";
			}

		}

	}

	/* Retrieve the temperature from this class object */
	public String getTemperature() {
		return this.temperature;
	}

	/*
	 * Function that will wait for the thread defined in this class to finish
	 * completely
	 */
	public Void waitForResult() throws InterruptedException, ExecutionException {
		if (task != null) {
			return task.get();
		}
		return null;
	}

}