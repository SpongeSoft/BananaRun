package com.spongesoft.bananarun;

import com.spongesoft.bananarun.MainActivity.DummySectionFragment;
import com.spongesoft.bananarun.MainActivity.SectionsPagerAdapter;
import com.spongesoft.bananarun.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class SessionActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	long race_id;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		race_id = this.getIntent().getIntExtra("race_id", -1);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		/* Initial tab is the HOME section */
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(2);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment;

			if (position == 0) {
				fragment = new SessionFragment();
			} else if (position == 1) {
				// LUCES DE COLORES
				fragment = new MapSectionFragment();
			} else {
				fragment = new DummySectionFragment();
			}
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			args.putLong("race_id", race_id);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section2);
			case 1:
				return getString(R.string.title_section3);
			}
			return null;
		}

	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}

}
