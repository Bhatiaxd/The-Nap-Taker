package com.karan.thenaptaker.napalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.karan.thenaptaker.R;
import com.karan.thenaptaker.napdatabase.DBHelper;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;

/**
 * {@link StopActivity}
 * It basically plays nap song selected by user  and runs a count informing about how much time is
 * left. It also provide with the DND service to stop interruption in between the nap.
 */


public class StopActivity extends AppCompatActivity {

    /**
     *alarmSong is {@link MediaPlayer} object that stores song that is chosen for alarm
     */
    MediaPlayer alarmSong;
    /**
     *napSong is {@link MediaPlayer} object that stores song that is chosen for nap
     */
    MediaPlayer napSong;

    /**
     *rs stores all details related to nap
     */
    Cursor rs;

    /**
     *audioManager is used to block ringer sound according to users call
     */
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);

        audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);

        SharedPreferences idShared = getSharedPreferences("Id", MODE_PRIVATE);
        int id=idShared.getInt("id",1);

        DBHelper mydb = new DBHelper(this);
        rs = mydb.getData(id);
        rs.moveToFirst();

        alarmSong = objectAlarm();
        napSong = objectNap();
        startMusic(napSong);

        timer();
    }

    @Override
    public void onBackPressed() {
        stopNap(null);
        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        audioManager.setRingerMode(RINGER_MODE_NORMAL);
    }

    /**
     *stopNap is method to stop  the given nap
     */
    public void stopNap(View view) {
        stopMusic(alarmSong);
        stopMusic(napSong);
        finish();
    }

    /**
     * timer method uses anonymous object of {@link CountDownTimer} to show time left over
     * before alarm rings
     */
    public void timer() {

        long getTime = (long) (rs.getFloat(rs.getColumnIndex(DBHelper.COLUMN_TIME))*60*1000);

        new CountDownTimer(getTime,1) {

            TextView textView = (TextView) findViewById(R.id.textView_timer);

            public void onTick(long millisUntilFinished) {
                textView.setText("Time left:"+millisUntilFinished/60000+":"+ (millisUntilFinished %60000)/1000);
            }

            public void onFinish() {
                textView.setText("Nap time over "+rs.getString(rs.getColumnIndex(DBHelper.COLUMN_NAME))+"\ncomeback to working mode");
                pauseMusic(napSong);
                startMusic(alarmSong);
                loopMusic(alarmSong,true);
                audioManager.setRingerMode(RINGER_MODE_NORMAL);
            }
        }.start();
    }

    /**
     *objectAlarm is method to return the alarm song as {@link MediaPlayer} object
     */
    public MediaPlayer objectAlarm() {
        int alarmMusicID = rs.getInt(rs.getColumnIndex(DBHelper.COLUMN_ALARMMUSICID));
        if (alarmMusicID == 0) {
            return MediaPlayer.create(this, R.raw.ipl);
        } else if (alarmMusicID == 1) {
            return MediaPlayer.create(this,R.raw.nanajstar);
        } else if (alarmMusicID == 2) {
            return MediaPlayer.create(this,R.raw.nokia);
        }
        return null;

    }

    /**
     *objectNap is method to return the nap song as {@link MediaPlayer} object
     */
    public MediaPlayer objectNap() {
        int napMusicID = rs.getInt(rs.getColumnIndex(DBHelper.COLUMN_NAPMUSICID));
        if (napMusicID == 0) {
            return MediaPlayer.create(this,R.raw.aminorthing);
        } else if (napMusicID == 1) {
            return MediaPlayer.create(this,R.raw.pentatonicwaves);
        } else if (napMusicID == 2) {
            return MediaPlayer.create(this,R.raw.todreamsunsettled);
        }
        return null;
    }

    /**
     *startMusic is method to start the given music
     */
    public void startMusic(MediaPlayer mediaPlayer) {
        try{
            mediaPlayer.start();
        }catch (Exception e){
            Log.e("media start",e.getMessage());
        }
    }

    /**
     *stopMusic is method to stop the given music
     */
    public void stopMusic(MediaPlayer mediaPlayer) {
        try{
            mediaPlayer.stop();
        }catch (Exception e){
            Log.e("media stop",e.getMessage());
        }
    }

    /**
     *loopMusic is method to set whether the given music will loop or not
     */
    public void loopMusic(MediaPlayer mediaPlayer,boolean b) {
        try{
            mediaPlayer.setLooping(b);
        }catch (Exception e){
            Log.e("media loop",e.getMessage());
        }
    }

    /**
     *pauseMusic is method to pause the given music
     */
    public void pauseMusic(MediaPlayer mediaPlayer) {
        try{
            mediaPlayer.pause();
        }catch (Exception e){
            Log.e("media pause",e.getMessage());
        }
    }

    /**
     *dnd is method used to to stop interruption in between the nap.
     */
    public void dnd(View view)  {


            audioManager.setRingerMode(RINGER_MODE_SILENT);

        Toast.makeText(getApplicationContext(), "chl padha haaye", Toast.LENGTH_LONG).show();
    }


}
