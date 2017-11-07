package com.karan.thenaptaker.napwidget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import com.karan.thenaptaker.R;


/**
 * {@link NapWidgetAlarm} is a service to provide functionality to Widget
 */
public class NapWidgetAlarm extends IntentService {

    public NapWidgetAlarm() {
        super("");
    }


    @Override
    public void onHandleIntent(Intent intent) {
        ComponentName me = new ComponentName(this,
                NapWidget.class);
        AppWidgetManager mgr = AppWidgetManager.getInstance(this);
        mgr.updateAppWidget(me, buildUpdate(this));
    }


    /**
     * buildUpdate is to return a customized object of {@link RemoteViews} to provide our view and
     * functionality to our widget
     */
    private RemoteViews buildUpdate(Context context) {
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.nap_widget);
        Intent i=new Intent(this, MyReceiverStart.class);
        SharedPreferences settingNap = getSharedPreferences("WIDGET", MODE_PRIVATE);
        PendingIntent pi= PendingIntent.getBroadcast(context, 0	, i, 0);
        updateViews.setOnClickPendingIntent(R.id.startWidget,pi);
        updateViews.setViewVisibility(R.id.stopWidget, View.GONE);
        updateViews.setTextViewText(R.id.napWidgetTextBox, settingNap.getString("name",null));
        return (updateViews);
    }
}