package com.karan.thenaptaker.naplist;

import android.content.Intent;

import com.karan.thenaptaker.napalarm.StopActivity;


/**
 * {@link StartScrollingActivity} provides list of all Nap which user can start
 */
public class StartScrollingActivity extends ScrollingActivity {
    @Override
    public Intent setIntents() {
        return new Intent(getApplicationContext(),StopActivity.class);
    }
}
