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
import android.widget.TextView;

public class GetWeather {

	AsyncTask<Void, Void, Void> task;
	TextView weather;
	String description;
	String city;
	String region;
	String country;

	String windChill;
	String windDirection;
	String windSpeed;

	public String temperature;
	String code;
	String weatherStatus;

	String sunrise;
	String sunset;

	String conditiontext;
	String conditiondate;

	public GetWeather(String woeid) {
		String sel_woeid = woeid;
		task = new MyQueryYahooWeatherTask(sel_woeid).execute();
	}

	private class MyQueryYahooWeatherTask extends AsyncTask<Void, Void, Void> {

		String woeid;
		String weatherString;

		MyQueryYahooWeatherTask(String w) {
			woeid = w;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			weatherString = QueryYahooWeather();
			Document weatherDoc = convertStringToDocument(weatherString);

			if (weatherDoc != null) {
				parseWeather(weatherDoc);
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
					+ woeid + "&u=c";

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(queryString);

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

					qResult = stringBuilder.toString();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Log.d("QueryYahooWeather","Result: "+qResult);

			return qResult;
		}

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

		private void parseWeather(Document srcDoc) {

			// <description>Yahoo! Weather for New York, NY</description>
			NodeList descNodelist = srcDoc.getElementsByTagName("description");
			if (descNodelist != null && descNodelist.getLength() > 0) {
				description = descNodelist.item(0).getTextContent();
			} else {
				description = "EMPTY";
			}

			if (description.equals("Yahoo! Weather Error")) {
				temperature = "?";
				Log.d("parseWeather", "City not found!");
				return;
			}

			// <yweather:location city="New York" region="NY"
			// country="United States"/>
			NodeList locationNodeList = srcDoc
					.getElementsByTagName("yweather:condition");
			if (locationNodeList != null && locationNodeList.getLength() > 0) {
				Node locationNode = locationNodeList.item(0);
				NamedNodeMap locNamedNodeMap = locationNode.getAttributes();

				temperature = (String) locNamedNodeMap.getNamedItem("temp")
						.getNodeValue().toString();
				code = (String) locNamedNodeMap.getNamedItem("code")
						.getNodeValue().toString();
				// int fTemperature = Integer.parseInt(temperature);
				// Log.d("parseWeather","initial temperature= "+temperature+", fTemperature= "+fTemperature);
				// double cTemperature = (52 - 32) * (5.0/9.0);
				// Log.d("parseWeather","cTemperature="+cTemperature);
				// temperature = Double.toString(Math.floor(cTemperature));
				Log.d("parseWeather", "temperature=" + temperature);
				weatherStatus = locNamedNodeMap.getNamedItem("text")
						.getNodeValue().toString();
			} else {
				city = "EMPTY";
				region = "EMPTY";
				country = "EMPTY";
			}

			// <yweather:wind chill="60" direction="0" speed="0"/>
			NodeList windNodeList = srcDoc
					.getElementsByTagName("yweather:wind");
			if (windNodeList != null && windNodeList.getLength() > 0) {
				Node windNode = windNodeList.item(0);
				NamedNodeMap windNamedNodeMap = windNode.getAttributes();

				windChill = windNamedNodeMap.getNamedItem("chill")
						.getNodeValue().toString();
				windDirection = windNamedNodeMap.getNamedItem("direction")
						.getNodeValue().toString();
				windSpeed = windNamedNodeMap.getNamedItem("speed")
						.getNodeValue().toString();
			} else {
				windChill = "EMPTY";
				windDirection = "EMPTY";
				windSpeed = "EMPTY";
			}

			// <yweather:astronomy sunrise="6:52 am" sunset="7:10 pm"/>
			NodeList astNodeList = srcDoc
					.getElementsByTagName("yweather:astronomy");
			if (astNodeList != null && astNodeList.getLength() > 0) {
				Node astNode = astNodeList.item(0);
				NamedNodeMap astNamedNodeMap = astNode.getAttributes();

				sunrise = astNamedNodeMap.getNamedItem("sunrise")
						.getNodeValue().toString();
				sunset = astNamedNodeMap.getNamedItem("sunset").getNodeValue()
						.toString();
			} else {
				sunrise = "EMPTY";
				sunset = "EMPTY";
			}

			// <yweather:condition text="Fair" code="33" temp="60"
			// date="Fri, 23 Mar 2012 8:49 pm EDT"/>
			NodeList conditionNodeList = srcDoc
					.getElementsByTagName("yweather:condition");
			if (conditionNodeList != null && conditionNodeList.getLength() > 0) {
				Node conditionNode = conditionNodeList.item(0);
				NamedNodeMap conditionNamedNodeMap = conditionNode
						.getAttributes();

				conditiontext = conditionNamedNodeMap.getNamedItem("text")
						.getNodeValue().toString();
				conditiondate = conditionNamedNodeMap.getNamedItem("date")
						.getNodeValue().toString();
			} else {
				conditiontext = "EMPTY";
				conditiondate = "EMPTY";
			}

		}

	}

	public String getTemperature() {
		return this.temperature;
	}

	public Void waitForResult() throws InterruptedException, ExecutionException {
		if (task != null) {
			return task.get();
		}
		return null;
	}

}