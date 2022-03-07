package com.meishe.common.utils;

import android.util.Log;

public class Logger {
    public static boolean DEBUG = (LOGLEVEL > 3);
    public static boolean ERROR = false;
    public static boolean INFO = (LOGLEVEL > 2);
    public static int LOGLEVEL = 0;
    private static final String TAG = "Logger";
    public static boolean VERBOSE = (LOGLEVEL > 4);
    public static boolean WARN = (LOGLEVEL > 1);

    static {
        boolean z = false;
        if (LOGLEVEL > 0) {
            z = true;
        }
        ERROR = z;
    }

    public static void setDebugMode(boolean z) {
        boolean z2 = false;
        LOGLEVEL = z ? 5 : 0;
        VERBOSE = LOGLEVEL > 4;
        DEBUG = LOGLEVEL > 3;
        INFO = LOGLEVEL > 2;
        WARN = LOGLEVEL > 1;
        if (LOGLEVEL > 0) {
            z2 = true;
        }
        ERROR = z2;
    }

    public static void d(String str, String str2) {
        if (DEBUG) {
            if (str2 == null) {
                str2 = "";
            }
            Log.d(str, str2);
        }
    }

    public static void d(String str, String str2, Throwable th) {
        if (DEBUG) {
            if (str2 == null) {
                str2 = "";
            }
            Log.d(str, str2, th);
        }
    }

    public static void d(String str) {
        if (DEBUG) {
            if (str == null) {
                str = "";
            }
            Log.d(TAG, str);
        }
    }

    public static void d(String str, Throwable th) {
        if (DEBUG) {
            if (str == null) {
                str = "";
            }
            Log.d(TAG, str, th);
        }
    }

    public static void e(String str, String str2) {
        if (ERROR) {
            if (str2 == null) {
                str2 = "";
            }
            Log.e(str, str2);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        if (ERROR) {
            if (str2 == null) {
                str2 = "";
            }
            Log.e(str, str2, th);
        }
    }

    public static void e(String str) {
        if (ERROR) {
            if (str == null) {
                str = "";
            }
            Log.e(TAG, str);
        }
    }

    public static void e(String str, Throwable th) {
        if (ERROR) {
            if (str == null) {
                str = "";
            }
            Log.e(TAG, str, th);
        }
    }
}
