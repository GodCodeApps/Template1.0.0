package com.meishe.myvideo.application;

import android.app.Application;
import android.content.Context;
import com.meicam.sdk.NvsStreamingContext;

public class MeiSheApplication extends Application {
    private static MeiSheApplication mApplication;
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mContext = getApplicationContext();
//        NvsStreamingContext.init(mContext, "assets:/meishesdk.lic", 1);
//        CrashHandler.getInstance().init(this);
//        UMConfigure.setLogEnabled(false);
//        UMConfigure.init(mContext, UMengUtils.APPID, "android", 1, null);
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
    }

    public static MeiSheApplication getInstance() {
        return mApplication;
    }
}
