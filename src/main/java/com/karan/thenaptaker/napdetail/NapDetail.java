package com.karan.thenaptaker.napdetail;

import android.content.Intent;
import android.view.View;

import com.karan.thenaptaker.napdatabase.DBHelper;
import com.karan.thenaptaker.naplist.StartScrollingActivity;

/**
 * {@link NapDetail} is subclass of {@link Detail} to add new Nap according to user entered details
 */
public class NapDetail extends Detail {


    /**
     * saveDetail is method to add Nap and navigate to {@link StartScrollingActivity}
     * @param view
     */
    public void saveDetail(View view) {
        DBHelper mydb = new DBHelper(this);
        Intent saveDetail = new Intent(this, StartScrollingActivity.class);
        float ft;
        try{
            ft=Float.parseFloat(time.getText().toString());
        }catch (Exception e){
            ft=3;
        }
        mydb.insertNapDetails(name.getText().toString(),
                alarm.getValue(),
                nap.getValue(),
                ft);
        startActivity(saveDetail);
        mydb.close();
        finish();
    }
}
