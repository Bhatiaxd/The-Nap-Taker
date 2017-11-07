package com.karan.thenaptaker.napwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.karan.thenaptaker.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link NapWidgetConfigureActivity }
 */
public class NapWidget extends AppWidgetProvider {

    /**
     * updateAppWidget is to provide our view and functionality to our widget
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence widgetText = NapWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.nap_widget);
        views.setTextViewText(R.id.napWidgetTextBox, widgetText);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        context.startService(new Intent(context, NapWidgetAlarm.class));
    }

    @Override
    public void onReceive(Context ctxt, Intent intent) {
        if (intent.getAction()==null) {
            ctxt.startService(new Intent(ctxt, NapWidgetAlarm.class));
        }
        else {
            super.onReceive(ctxt, intent);
        }
    }

    @Override
    public void onUpdate(Context ctxt,
                         AppWidgetManager mgr,
                         int[] appWidgetIds) {
        ctxt.startService(new Intent(ctxt, NapWidgetAlarm.class));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            NapWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
            context.stopService(new Intent(context, NapWidgetAlarm.class));
        }
    }


}



