package com.meishe.base.app;

import android.app.Application;

public class BaseApp extends Application {
    private static BaseApp mInstance;

    public static BaseApp getInstance() {
        return mInstance;
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
