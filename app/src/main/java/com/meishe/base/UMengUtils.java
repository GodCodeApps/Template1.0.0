package com.meishe.base;

import android.content.Context;
import java.util.Map;

public class UMengUtils {
    public static final String APPID = "5f4484feb4b08b653e99029d";
    public static final String EVENT_ID = "event_compile";

    public static void onEvent(Context context) {
//        MobclickAgent.onEvent(context, EVENT_ID);
    }

    public static void onEvent(Context context, Map map) {
//        MobclickAgent.onEvent(context, EVENT_ID, map);
    }

    public static void onEvent(Context context, String str) {
//        MobclickAgent.onEvent(context, EVENT_ID, str);
    }
}
