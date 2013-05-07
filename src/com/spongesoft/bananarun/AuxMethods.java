package com.spongesoft.bananarun;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

public class AuxMethods {

	/**
	 * Constructor
	 */

	SharedPreferences preferences;

	public AuxMethods(SharedPreferences preferences) {
		this.preferences = preferences;
	}

	/**
	 * Get Distance with unit
	 * 
	 * @param Distance
	 *            to concatenate
	 * 
	 * @return String value of distance + unit
	 */

	public String getDistance(double d) {

		String distUnit = preferences.getString("prefUnitSystem", "1");
		String Unit = "";

		double dist = 0.0;

		if (distUnit.equals("1")) {
			Unit = " Km.";
			dist = d / 1000.00;
		} else {
			Unit = " Mi.";
			dist = d / 1609.34;
		}

		DecimalFormat twoDForm = new DecimalFormat("#.##",
				new DecimalFormatSymbols(Locale.US));
		dist = Double.valueOf(twoDForm.format(dist));

		return dist + Unit;
	}

	/**
	 * Get height and weight from preferences menu
	 * 
	 * 
	 * @return Array of integers with both values
	 */

	public int[] getHeightAndWeight() {

		int[] abundle = new int[2];

		abundle[0] = Integer.valueOf(preferences.getString("prefUserHeight",
				"75"));
		abundle[1] = Integer.valueOf(preferences.getString("prefUserWeight",
				"178"));

		return abundle;
	}

	/**
	 * Get the number given with only two decimals
	 * 
	 * @param Number
	 *            to convert
	 * 
	 * @return The number with only two decimals
	 */

	public double stripDecimals(double value) {
		DecimalFormat twoDForm = new DecimalFormat("#.##",
				new DecimalFormatSymbols(Locale.US));
		return Double.valueOf(twoDForm.format(value));
	}

	/**
	 * Get type of measure
	 * 
	 * @return String with type of measure
	 */
	public String getDistanceUnits() {
		String distUnit = preferences.getString("prefUnitSystem", "1");
		if (distUnit.equals("2")) {
			return "Mi";
		}
		return "Km";
	}
}
