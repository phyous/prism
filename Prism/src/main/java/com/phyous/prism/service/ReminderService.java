package com.phyous.prism.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.phyous.prism.SettingsActivity;

import java.util.Calendar;

public class ReminderService extends Service {
    private final String TAG = this.getClass().getName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences settings = this.getSharedPreferences(SettingsActivity.PREFS_NAME, 0);
        int hour = settings.getInt(
                SettingsActivity.PREF_REMINDER_HOUR,
                SettingsActivity.REMINDER_HOUR_DEFAULT);
        int minute = settings.getInt(
                SettingsActivity.PREF_REMINDER_MINUTE,
                SettingsActivity.REMINDER_MINUTE_DEFAULT);
        boolean notificationsEnabled = settings.getBoolean(
                        SettingsActivity.PREF_REMINDER_CHECKBOX,
                        SettingsActivity.REMINDER_CHECKBOX_DEFAULT);

        if (notificationsEnabled) {
            Log.d(TAG, String.format("Started Alarm for %d:%d", hour, minute));
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            new AlarmTask(this, c).run();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying ReminderService");
    }

}
