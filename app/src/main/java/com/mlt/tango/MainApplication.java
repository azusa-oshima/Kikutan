package com.mlt.tango;

import android.app.Application;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabaseSingleton.init(getApplicationContext());
    }
}
