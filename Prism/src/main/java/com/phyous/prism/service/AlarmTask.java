package com.phyous.prism.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmTask implements Runnable {

    private final Calendar date;
    private final AlarmManager am;
    private final Context context;

    public AlarmTask(Context context, Calendar date) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        // When setting an alarm to past time, the alarm immediately pops up. Simply check if the
        // current time is bigger than the alarm time. If so, add 24 hours to the alarm time and set
        // the alarm.
        long timeToAlarm = date.getTimeInMillis();
        if (date.getTimeInMillis() < System.currentTimeMillis())
        {
            timeToAlarm += (24*60*60*1000);
        }

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC, timeToAlarm, pendingIntent);
    }
}