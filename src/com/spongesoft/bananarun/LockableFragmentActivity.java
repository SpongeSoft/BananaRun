package com.spongesoft.bananarun;

import android.support.v4.app.FragmentActivity;


public class LockableFragmentActivity extends FragmentActivity{
	//LockableFragmentActivity is used to use lock/unlock on both SessionActivity and MainActivity
	
	LockableViewPager viewpager;
	public void unlock() {
		viewpager.unlock();
	}
	
	public void lock() {
		viewpager.lock();
	}
	
	public boolean isLocked() {
		return viewpager.isLocked();
	}
}
