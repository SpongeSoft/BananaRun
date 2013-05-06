package com.spongesoft.bananarun;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import android.content.SharedPreferences;

public class AuxMethods {

	SharedPreferences preferences;
	public AuxMethods(SharedPreferences preferences){
		this.preferences = preferences;
	}
	
	public String getDistance(double d) {
		
		String distUnit = preferences.getString("prefUnitSystem", "1");
		String Unit = "";
		
		double dist = 0.0;
		
		if(distUnit.equals("1")){
			Unit = " Km.";
			dist = d/1000.00;
		}else{
			Unit = " Mi.";
			dist = d/1609.34;
		}
		
		DecimalFormat twoDForm = new DecimalFormat("#.##",new DecimalFormatSymbols(Locale.US));
        dist = Double.valueOf(twoDForm.format(dist));
        
		//dist = (double) Math.round((dist*100.0)/100.0);
		
        return dist + Unit;
	}
	
}
