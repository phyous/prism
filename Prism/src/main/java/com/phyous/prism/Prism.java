package com.phyous.prism;

import android.app.Application;
import android.content.res.Configuration;

import com.phyous.prism.provider.PrismDbHelper;

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
