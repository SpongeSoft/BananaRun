package com.spongesoft.bananarun;

import com.spongesoft.bananarun.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class UserSettingActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Load preferences */
		addPreferencesFromResource(R.xml.settings);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		String height = settings.getString("prefUserHeight", "No val");
		String weight = settings.getString("prefUserWeight", "No Val");
		String unit = settings.getString("prefUnitSystem", "1");

		final SharedPreferences.Editor editor = settings.edit();

		final EditTextPreference hVal = (EditTextPreference) findPreference("prefUserHeight");
		hVal.setSummary(height.toString());
		hVal.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				// TODO Auto-generated method stub
				hVal.setSummary(newValue.toString());
				editor.putString("prefUserHeight", newValue.toString());
				editor.commit();
				return true;
			}
		});

		final EditTextPreference wVal = (EditTextPreference) findPreference("prefUserWeight");
		wVal.setSummary(weight.toString());
		wVal.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				// TODO Auto-generated method stub
				wVal.setSummary(newValue.toString());
				editor.putString("prefUserWeight", newValue.toString());
				editor.commit();
				return true;
			}
		});

		final ListPreference Units = (ListPreference) findPreference("prefUnitSystem");
		if (unit.toString().equals("1")) {
			Units.setSummary("Metric system");
		} else if (unit.toString().equals("2")) {
			Units.setSummary("Imperial system");
		}
		Units.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				// TODO Auto-generated method stub
				if (newValue.toString().equals("1")) {
					Units.setSummary("Metric system");
				} else if (newValue.toString().equals("2")) {
					Units.setSummary("Imperial system");
				}

				editor.putString("prefUnitSystem", newValue.toString());
				editor.commit();
				return true;
			}
		});

		Preference gpsPref = (Preference) findPreference("prefGps");
		gpsPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			// http://stackoverflow.com/questions/623225/android-go-to-settings-screen
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				startActivityForResult(
						new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
						0);
				return true;
			}
		});

	}// onCreate

}
