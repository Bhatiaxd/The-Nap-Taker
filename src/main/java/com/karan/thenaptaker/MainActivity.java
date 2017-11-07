package com.karan.thenaptaker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.karan.thenaptaker.about.About;
import com.karan.thenaptaker.naplist.EditScrollingActivity;
import com.karan.thenaptaker.naplist.StartScrollingActivity;
import com.karan.thenaptaker.napwebservice.WebScrollingActivity;
import com.karan.thenaptaker.reminder.MyReceiver;

import java.util.Calendar;

/**
 * {@link MainActivity} or launching activity
 * It provides navigation to all features of the app
 */
public class MainActivity extends AppCompatActivity {

/**
 * menuSP is used to store information about whether
 * reminder service is running or not.
 */
    SharedPreferences menuSP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuSP = getSharedPreferences("Menu",MODE_PRIVATE);/* initialization of menuSP*/
    }
    /**
     *startStopNotification is a method to start or stop reminder
     * service that runs on default time of 6:30PM
     */
    void startStopNotification(String s){

        Calendar calendar = Calendar.getInstance();
        // we can set time by open date and time picker dialog
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        Intent intent1 = new Intent(MainActivity.this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        /*
         * am is used to setup and cancel reminder
         */
        AlarmManager am = (AlarmManager) MainActivity.this
                .getSystemService(MainActivity.this.ALARM_SERVICE);
        /*
         *condition to check either to start or stop service
         */
        if(s=="Start Reminder")
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        else am.cancel(pendingIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scrolling, menu);
        menu.getItem(0).setTitle(menuSP.getString("menu_name","Start Reminder"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences.Editor editorNap = menuSP.edit();
        if(item.getTitle()=="Start Reminder"){
            startStopNotification("Start Reminder");
            editorNap.putString("menu_name","Stop Reminder");
            editorNap.commit();
            item.setTitle("Stop Reminder");
            Toast.makeText(this, "Reminder Service Started", Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            startStopNotification("Stop Reminder");
            editorNap.putString("menu_name","Start Reminder");
            editorNap.commit();
            item.setTitle("Start Reminder");
            Toast.makeText(this, "Reminder Service Stoped", Toast.LENGTH_SHORT)
                    .show();
        }
        return true;
    }

    /**
     *startNap is method to start the selection window for
     * for starting nap
     */
    public void startNap(View view) {
        Intent startNap = new Intent(this,StartScrollingActivity.class);
        startActivity(startNap);
    }

    /**
     *benefits is method to start the selection window for
     * telling benefits of different types of nap
     */
    public void benefits(View view) {
        Intent startNap = new Intent(this,WebScrollingActivity.class);
        startActivity(startNap);
    }
    /**
     *about is method to tell about developer and app
     */
    public void about(View view) {
        Intent startNap = new Intent(this,About.class);
        startActivity(startNap);
    }

    /**
     *editor is method to start the selection window for
     * editing nap details
     */
    public void editor(View view) {
        Intent setting = new Intent(this,EditScrollingActivity.class);
        startActivity(setting);
    }
}
