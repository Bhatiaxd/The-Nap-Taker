package com.karan.thenaptaker.napwidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.karan.thenaptaker.R;

import static com.karan.thenaptaker.napwidget.MyReceiverStart.alarmSong;
import static com.karan.thenaptaker.napwidget.MyReceiverStart.countDownTimer;
import static com.karan.thenaptaker.napwidget.MyReceiverStart.napSong;

/**
 * {@link MyReceiverStop} is a {@link BroadcastReceiver} to stop the nap when stop
 * button is pressed in the widget
 */
public class MyReceiverStop extends BroadcastReceiver {

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

        stopTicker();

        updateViews.setViewVisibility(R.id.startWidget, View.VISIBLE);
        updateViews.setViewVisibility(R.id.stopWidget, View.GONE);
        updateViews.setTextViewText(R.id.napWidgetTicker, " ");
        updateViews.setViewVisibility(R.id.napWidgetTicker, View.INVISIBLE);
        mgr.updateAppWidget(me, updateViews);

        stopNap();

    }

    /**
     *stopNap is method to stop  the given nap
     */
    public  void stopNap() {
        stopMusic(alarmSong);
        stopMusic(napSong);
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
     *stopTicker is method to stop countDownTimer
     */
    public void stopTicker() {
        try{
            countDownTimer.cancel();
        }catch (Exception e){
            Log.e("ticker",e.getMessage());
        }
    }

}
