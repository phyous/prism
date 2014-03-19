package com.phyous.prism;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.phyous.prism.provider.PrismDbHelper;
import com.phyous.prism.service.ReminderService;

public class Prism extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize database
        PrismDbHelper.getDb(this);

        // Initialize Reminder Service
        startService(new Intent(this, ReminderService.class));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
