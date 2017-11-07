package com.karan.thenaptaker.napwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.karan.thenaptaker.R;
import com.karan.thenaptaker.napdatabase.DBHelper;

/**
 * {@link MyReceiverStart} is a {@link BroadcastReceiver} to start the nap when start
 * button is pressed in the widget
 */
public class MyReceiverStart extends BroadcastReceiver {

    public static CountDownTimer countDownTimer;

    /**
     *alarmSong is {@link MediaPlayer} object that stores song that is chosen for alarm
     */
    static MediaPlayer alarmSong;

    /**
     *napSong is {@link MediaPlayer} object that stores song that is chosen for nap
     */
    static MediaPlayer napSong;

    /**
     *rs stores all details related to selected nap
     */
    Cursor rs;

    /**
     *mContext is to provide context to all methods
     */
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext=context;
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.nap_widget);
        ComponentName me = new ComponentName(mContext,NapWidget.class);
        AppWidgetManager mgr = AppWidgetManager.getInstance(mContext);
        Intent i=new Intent(mContext, MyReceiverStop.class);
        PendingIntent pi= PendingIntent.getBroadcast(context, 0	, i, 0);
        updateViews.setViewVisibility(R.id.stopWidget, View.VISIBLE);
        updateViews.setOnClickPendingIntent(R.id.stopWidget,pi);
        updateViews.setViewVisibility(R.id.napWidgetTicker, View.VISIBLE);
        updateViews.setViewVisibility(R.id.startWidget, View.GONE);
        mgr.updateAppWidget(me, updateViews);

        String URL = "content://com.karan.thenaptaker.napwidget.MyContentProvider";
        Uri students = Uri.parse(URL);
        rs = mContext.getContentResolver().query(students, null, null, null, null);
        rs.moveToLast();
        Log.d("widget nap name",rs.getString(rs.getColumnIndex( MyContentProvider.NAME)));

        alarmSong = objectAlarm();
        napSong = objectNap();
        startMusic(napSong);

        timer();
    }

    /**
     * timer method uses anonymous object of {@link CountDownTimer} to show time left over
     * before alarm rings
     */
    public void timer() {
        long getTime = (long) (rs.getFloat(rs.getColumnIndex(DBHelper.COLUMN_TIME))*60*1000);
        countDownTimer = new CountDownTimer(getTime,1) {
            RemoteViews updateViews = new RemoteViews(mContext.getPackageName(), R.layout.nap_widget);
            ComponentName me = new ComponentName(mContext,NapWidget.class);
            AppWidgetManager mgr = AppWidgetManager.getInstance(mContext);

            public void onTick(long millisUntilFinished) {

                updateViews.setTextViewText(R.id.napWidgetTicker, ("Time left:"+millisUntilFinished/60000+":"+ (millisUntilFinished %60000)/1000));
                mgr.updateAppWidget(me, updateViews);
            }

            public void onFinish() {
                updateViews.setTextViewText(R.id.napWidgetTicker, ("Nap time over "+rs.getString(rs.getColumnIndex(DBHelper.COLUMN_NAME))+"\ncomeback to working mode"));
                mgr.updateAppWidget(me, updateViews);
                pauseMusic(napSong);
                startMusic(alarmSong);
                loopMusic(alarmSong,true);
            }
        }.start();
    }

    /**
     *objectAlarm is method to return the alarm song as {@link MediaPlayer} object
     */
    public MediaPlayer objectAlarm() {
            int alarmMusicID = rs.getInt(rs.getColumnIndex(DBHelper.COLUMN_ALARMMUSICID));
            if (alarmMusicID == 0) {
                return MediaPlayer.create(mContext, R.raw.ipl);
            } else if (alarmMusicID == 1) {
                return MediaPlayer.create(mContext,R.raw.nanajstar);
            } else if (alarmMusicID == 2) {
                return MediaPlayer.create(mContext,R.raw.nokia);
            }

        return null;

    }

    /**
     *objectNap is method to return the nap song as {@link MediaPlayer} object
     */
    public MediaPlayer objectNap() {
        int napMusicID = rs.getInt(rs.getColumnIndex(DBHelper.COLUMN_NAPMUSICID));
        if (napMusicID == 0) {
            return MediaPlayer.create(mContext,R.raw.aminorthing);
        } else if (napMusicID == 1) {
            return MediaPlayer.create(mContext,R.raw.pentatonicwaves);
        } else if (napMusicID == 2) {
            return MediaPlayer.create(mContext,R.raw.todreamsunsettled);
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
}
