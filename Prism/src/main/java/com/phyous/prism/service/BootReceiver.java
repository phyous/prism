package com.phyous.prism.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getName();

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "About to start ReminderService");
            context.startService(new Intent(context, ReminderService.class));
        }
    }
}
