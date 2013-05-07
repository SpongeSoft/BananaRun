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

/* This Activity shows the user a series of options that can be used to set limits
 * for a new race or session. When configured appropiately, the user can start the
 * new sesion. */
public class UserSettingActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Load preferences from XML layout */
		addPreferencesFromResource(R.xml.settings);

		/* Retrieve the application's global preferences */
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		/*
		 * Retrieve the previous preferences to show the updated values to the
		 * user
		 */
		String height = settings.getString("prefUserHeight", "No val");
		String weight = settings.getString("prefUserWeight", "No Val");
		String unit = settings.getString("prefUnitSystem", "1");

		/* Create Preferences editor to update preference values */
		final SharedPreferences.Editor editor = settings.edit();

		/* Update preference value related to the user's height */
		final EditTextPreference hVal = (EditTextPreference) findPreference("prefUserHeight");
		hVal.setSummary(height.toString());
		hVal.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {

				hVal.setSummary(newValue.toString());
				editor.putString("prefUserHeight", newValue.toString());
				editor.commit();
				return true;
			}
		});

		/* Update preference value related to the user's weight */
		final EditTextPreference wVal = (EditTextPreference) findPreference("prefUserWeight");
		wVal.setSummary(weight.toString());
		wVal.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				wVal.setSummary(newValue.toString());
				editor.putString("prefUserWeight", newValue.toString());
				editor.commit();
				return true;
			}
		});

		/* Update preference value related to the unit system */
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

		/*
		 * Update the preference value related to the GPS configuration. Based
		 * on the code from:
		 * http://stackoverflow.com/questions/623225/android-go
		 * -to-settings-screen
		 */
		Preference gpsPref = (Preference) findPreference("prefGps");
		gpsPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				startActivityForResult(
						new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
						0);
				return true;
			}
		});

	}// onCreate

}
