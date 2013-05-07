package com.spongesoft.bananarun;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*Based on the code from: 
 * http://stackoverflow.com/questions/9650265/how-do-disable-paging-by-swiping-with-finger-in-viewpager-but-still-be-able-to-s */
public class LockableViewPager extends android.support.v4.view.ViewPager {
	private boolean isLocked = false; //Flag
	
    public LockableViewPager(Context arg0) {
		super(arg0);
	}

    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // Never allow swiping to switch between pages
    	if(isLocked) {
    		return false;
    	}else{
    		return super.onInterceptTouchEvent(arg0);
    	}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
    	if(isLocked){
    		return false;
    	}else{
    		return super.onTouchEvent(event);
    	}
    }
    
    /* Retrieve ViewPager status */
    public boolean isLocked() {
    	return isLocked;
    }
    
    /* Lock ViewPager */
    public void lock() {
    	isLocked = true;
    }
    
    /* Unlock ViewPager */
    public void unlock() {
    	isLocked = false;
    }
}