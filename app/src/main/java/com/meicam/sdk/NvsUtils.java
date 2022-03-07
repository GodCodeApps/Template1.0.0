package com.meicam.sdk;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

public class NvsUtils {
    private static final String TAG = "Meicam";
    private static boolean sNeedCheck = true;

    public static void setCheckEnable(boolean z) {
        sNeedCheck = z;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static boolean checkFunctionInMainThread() {
        if (!sNeedCheck || isMainThread()) {
            return true;
        }
        String methodName = getMethodName(4);
        if (TextUtils.isEmpty(methodName)) {
            return false;
        }
        String methodName2 = getMethodName(5);
        Log.w(TAG, "Main Thread Checker:\"" + methodName + "\" API called on a background thread.");
        if (!TextUtils.isEmpty(methodName2)) {
            Log.w(TAG, "Invoking method: \"" + methodName2 + "\".");
        }
        return false;
    }

    public static String getMethodName(int i) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length <= i || i < 0) {
            return null;
        }
        return stackTrace[i].getMethodName();
    }
}
