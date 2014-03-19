package com.phyous.prism.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;

public class ReminderService extends Service {
    public static String HOUR_EXTRA = "hour_extra";
    public static String MINUTE_EXTRA = "minute_extra";
    public static String SECOND_EXTRA = "second_extra";

    private static final int DEFAULT_HOUR = 20;
    private static final int DEFAULT_MINUTE = 30;
    private static final int DEFAULT_SECOND = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "ReminderService created", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int hour = intent.getIntExtra(HOUR_EXTRA, DEFAULT_HOUR);
        int minute = intent.getIntExtra(MINUTE_EXTRA, DEFAULT_MINUTE);
        int second = intent.getIntExtra(SECOND_EXTRA, DEFAULT_SECOND);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        new AlarmTask(this, c).run();

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
}
