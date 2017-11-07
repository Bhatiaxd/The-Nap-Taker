package com.karan.thenaptaker.napdetail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.karan.thenaptaker.R;
import com.karan.thenaptaker.napdatabase.DBHelper;
import com.karan.thenaptaker.naplist.EditScrollingActivity;


/**
 * {@link UpdateNapDetail} is subclass of {@link Detail} to update Nap according to user entered details
 */
public class UpdateNapDetail  extends Detail {


    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = (Button) findViewById(R.id.deleteNap);
        button.setVisibility(View.VISIBLE);
        SharedPreferences idShared = getSharedPreferences("Id", MODE_PRIVATE);
        id=idShared.getInt("id",1);
        DBHelper mydb = new DBHelper(this);
        Cursor rs = mydb.getData(id);
        rs.moveToFirst();
        name.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_NAME)));
        time.setText(""+rs.getFloat(rs.getColumnIndex(DBHelper.COLUMN_TIME)));
        nap.setValue(rs.getInt(rs.getColumnIndex(DBHelper.COLUMN_NAPMUSICID)));
        alarm.setValue(rs.getInt(rs.getColumnIndex(DBHelper.COLUMN_ALARMMUSICID)));
        mydb.close();
    }

    /**
     * saveDetail is method to edit Nap and navigate to {@link EditScrollingActivity}
     * @param view
     */
    public void saveDetail(View view) {
        DBHelper mydb = new DBHelper(this);
        mydb.updateNapDetails(id,name.getText().toString(),
                alarm.getValue(),
                nap.getValue(),
                Float.parseFloat(time.getText().toString()));
        Intent saveDetail = new Intent(this,EditScrollingActivity.class);
        startActivity(saveDetail);
        mydb.close();
        finish();
    }

    /**
     * deleteNap is method to delete Nap and navigate to {@link EditScrollingActivity}
     * @param view
     */
    @Override
    public void deleteNap(View view) {
        DBHelper mydb = new DBHelper(this);
        mydb.deleteNapDetails(id);
        Intent saveDetail = new Intent(this,EditScrollingActivity.class);
        startActivity(saveDetail);
        mydb.close();
        finish();
    }
}

