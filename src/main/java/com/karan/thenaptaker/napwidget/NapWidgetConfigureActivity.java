package com.karan.thenaptaker.napwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import com.karan.thenaptaker.R;
import com.karan.thenaptaker.napdatabase.DBHelper;

import static com.karan.thenaptaker.napwidget.MyContentProvider.COLUMN_ALARMMUSICID;
import static com.karan.thenaptaker.napwidget.MyContentProvider.COLUMN_NAME;
import static com.karan.thenaptaker.napwidget.MyContentProvider.COLUMN_NAPMUSICID;
import static com.karan.thenaptaker.napwidget.MyContentProvider.COLUMN_TIME;

/**
 * The configuration screen for the {@link NapWidget NapWidget} AppWidget.
 */
public class NapWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.karan.thenaptaker.napwidget.NapWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String MyOnClick = "myOnClickTag";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    NumberPicker nap;
    String[] napSong;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = NapWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = napSong[nap.getValue()];
            saveTitlePref(context, mAppWidgetId, widgetText);
            SharedPreferences settingNap = getSharedPreferences("WIDGET", MODE_PRIVATE);
            SharedPreferences.Editor editorNap = settingNap.edit();
            setNapDetail(nap.getValue()+1);
            editorNap.putString("name",napSong[nap.getValue()]);
            editorNap.apply();
            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            NapWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public NapWidgetConfigureActivity() {
        super();
    }

    public void setNapDetail(int id){

        DBHelper mydb = new DBHelper(this);
        Cursor rs = mydb.getData(id);
        rs.moveToFirst();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, rs.getString(rs.getColumnIndex(DBHelper.COLUMN_NAME)));
        contentValues.put(COLUMN_ALARMMUSICID, rs.getInt(rs.getColumnIndex(DBHelper.COLUMN_ALARMMUSICID)));
        contentValues.put(COLUMN_NAPMUSICID, rs.getInt(rs.getColumnIndex(DBHelper.COLUMN_NAPMUSICID)));
        contentValues.put(COLUMN_TIME, rs.getFloat(rs.getColumnIndex(DBHelper.COLUMN_TIME)));

        Uri uri = getContentResolver().insert(
                MyContentProvider.CONTENT_URI, contentValues);

        Log.d("database uri",uri.toString());
        mydb.close();

    }

    /**
     * Write the prefix to the SharedPreferences object for this widget
     */
     static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    /**
     * Read the prefix from the SharedPreferences object for this widget.
     * If there is no preference saved, get the default from a resource
     */
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    /**
     * Write the prefix to the SharedPreferences object for this widget
     */
    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.nap_widget_configure);
        //mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);

        DBHelper mydb = new DBHelper(this);
        napSong = mydb.getAllNapDetailsInStringArray();
        mydb.close();
        nap= (NumberPicker) findViewById(R.id.npwidget);
        nap.setDisplayedValues(napSong);
        nap.setMinValue(0);
        nap.setMaxValue(napSong.length-1);
        nap.setValue(0);


        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }


}

