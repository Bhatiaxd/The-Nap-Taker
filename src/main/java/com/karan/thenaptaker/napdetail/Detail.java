package com.karan.thenaptaker.napdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.karan.thenaptaker.R;

/**
 *{@link Detail} provides basic and common implementation to {@link NapDetail} and
 * {@link UpdateNapDetail}
 */

public abstract class Detail extends AppCompatActivity {

    EditText name,time ;
    NumberPicker nap,alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nap_detail);
        Button button = (Button) findViewById(R.id.deleteNap);
        button.setVisibility(View.GONE);
        setFields();

    }

    /**
     * setFields is a method used to initialize all UI components of the activity
     */
    public void setFields(){
        name= (EditText) findViewById(R.id.editText_name);
        time= (EditText) findViewById(R.id.editText_time);
        nap= (NumberPicker) findViewById(R.id.numberPicker_nap);
        alarm= (NumberPicker) findViewById(R.id.numberPicker_alarm);
        String[] napSong = {"Am I Nothing","Pentatonic Wave","To dream Sun Settled","None"};
        String[] alarmTune = {"Ipl Tune","Na Na Na Na by JStar","Nokia Tune","None"};
        nap.setDisplayedValues(napSong);
        alarm.setDisplayedValues(alarmTune);
        nap.setMinValue(0);
        nap.setMaxValue(napSong.length-1);
        alarm.setMinValue(0);
        alarm.setMaxValue(alarmTune.length-1);
        nap.setValue(0);
        alarm.setValue(0);
    }

    /**
     * cancel is method to return back to main screen without making any changes
     * @param view
     */
    public void cancel(View view) {
        finish();
    }

    /**
     * deleteNap is method to return back to main screen without making any changes
     * @param view
     */
    public void deleteNap(View view) {
        finish();
    }



}

