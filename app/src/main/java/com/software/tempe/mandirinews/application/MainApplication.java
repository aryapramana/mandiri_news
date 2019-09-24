package com.software.tempe.mandirinews.application;

import android.app.Application;

public class MainApplication extends Application {

    private static MainApplication mainApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
    }

    public static MainApplication getMainApplicationInstance()  {
        return mainApplication;
    }
}
