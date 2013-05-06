package com.spongesoft.bananarun;

import java.text.DecimalFormat;

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
		
		DecimalFormat twoDForm = new DecimalFormat("#.##");
        dist = Double.valueOf(twoDForm.format(dist));
             
        return dist + Unit;
	}
	
}
