package com.spongesoft.dietapp;

import android.support.v4.app.Fragment;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class MyMapFragment extends Fragment {
    private static final String KEY_STATE_BUNDLE = "localActivityManagerState";

    private LocalActivityManager mLocalActivityManager;

    protected LocalActivityManager getLocalActivityManager() {
        return mLocalActivityManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle state = null;
        if (savedInstanceState != null) {
            state = savedInstanceState.getBundle(KEY_STATE_BUNDLE);
        }

        mLocalActivityManager = new LocalActivityManager(getActivity(), true);
        mLocalActivityManager.dispatchCreate(state);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            //This is where you specify you activity class
        Intent i = new Intent(getActivity(), MyMapActivity.class); 
        Window w = mLocalActivityManager.startActivity("tag", i); 
        View currentView=w.getDecorView(); 
        currentView.setVisibility(View.VISIBLE); 
        currentView.setFocusableInTouchMode(true); 
        ((ViewGroup) currentView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        return currentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_STATE_BUNDLE,
                mLocalActivityManager.saveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(getActivity().isFinishing());
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocalActivityManager.dispatchStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalActivityManager.dispatchDestroy(getActivity().isFinishing());
    }
}
