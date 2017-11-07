package com.karan.thenaptaker.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.karan.thenaptaker.MainActivity;

import static com.karan.thenaptaker.R.drawable;

/**
 *  {@link MyAlarmService} is a {@link Service} to remind user to take a nap after every 24 hours
 */
public class MyAlarmService extends Service{

    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        super.onStart(intent, startId);
        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
        Notification notification = new Notification(
                drawable.example_picture,"This is a test message!", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Notification.Builder builder = new Notification.Builder(MyAlarmService.this);

        builder.setAutoCancel(false);
        builder.setTicker("this is ticker text");
        builder.setContentTitle("WhatsApp Notification");
        builder.setContentText("You have a new message");
        builder.setSmallIcon(drawable.example_picture);
        builder.setContentIntent(pendingNotificationIntent);
        builder.setOngoing(true);
        builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.build();

        notification = builder.getNotification();

        mManager.notify(11, notification);

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

}