package com.phyous.prism.service;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.phyous.prism.TimelineActivity;

public class NotifyService extends Service {
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    private static final int NOTIFICATION = 123;
    public static final String INTENT_NOTIFY = "com.phyous.prism.service.INTENT_NOTIFY";
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new ServiceBinder();

    private void showNotification() {
        final PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, TimelineActivity.class), 0);
        CharSequence title = "Prism";
        int icon = R.drawable.ic_dialog_alert;
        CharSequence text = "Time to grade yourself";
        long time = System.currentTimeMillis();

        Notification notification = new Notification(icon, text, time);
        notification.setLatestEventInfo(this, title, text, contentIntent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        mNM.notify(NOTIFICATION, notification);

        stopSelf();
    }
}