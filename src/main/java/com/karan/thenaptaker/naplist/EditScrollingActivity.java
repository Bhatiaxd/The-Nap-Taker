package com.karan.thenaptaker.naplist;

import android.content.Intent;

import com.karan.thenaptaker.napdetail.UpdateNapDetail;

/**
 * {@link EditScrollingActivity} provides list of all Nap user can edit
 */
public class EditScrollingActivity extends ScrollingActivity {
    @Override
    public Intent setIntents() {
        return new Intent(getApplicationContext(),UpdateNapDetail.class);
    }
    @Override
    public void updateList(){
        array_list.remove("NASA NAP");
        array_list.remove("POWER NAP");
        array_list.remove("LIGHT NAP");
        idEU=3;
    }
}
