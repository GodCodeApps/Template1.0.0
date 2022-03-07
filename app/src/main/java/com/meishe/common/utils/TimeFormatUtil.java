package com.meishe.common.utils;

import android.os.SystemClock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

public class TimeFormatUtil {
    private static final double TIMEBASE = 1000000.0d;

    public static String formatUsToString1(long j) {
        double d = (double) j;
        Double.isNaN(d);
        double d2 = d / TIMEBASE;
        int i = (int) d2;
        int i2 = i / 3600;
        int i3 = (i % 3600) / 60;
        double d3 = d2 % 60.0d;
        if (i2 > 0) {
            return String.format("%02d:%02d:%04.1f", Integer.valueOf(i2), Integer.valueOf(i3), Double.valueOf(d3));
        }
        return String.format("%02d:%04.1f", Integer.valueOf(i3), Double.valueOf(d3));
    }

    public static String formatUsToString2(long j) {
        double d = (double) j;
        Double.isNaN(d);
        int i = (int) ((d / TIMEBASE) + 0.5d);
        int i2 = i / 3600;
        int i3 = (i % 3600) / 60;
        int i4 = i % 60;
        if (j == 0) {
            return "00:00";
        }
        if (i2 > 0) {
            return String.format("%02d:%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
        }
        return String.format("%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i4));
    }

    public static String formatUsToString3(long j) {
        double d = (double) j;
        Double.isNaN(d);
        double d2 = d / TIMEBASE;
        int i = (int) d2;
        int i2 = i / 3600;
        int i3 = (i % 3600) / 60;
        double d3 = d2 % 60.0d;
        if (i2 > 0) {
            return String.format("%02d:%02d:%04.1f", Integer.valueOf(i2), Integer.valueOf(i3), Double.valueOf(d3));
        } else if (i3 > 0) {
            return String.format("%02d:%04.1f", Integer.valueOf(i3), Double.valueOf(d3));
        } else if (d3 > 9.0d) {
            return String.format("%4.1f", Double.valueOf(d3));
        } else {
            return String.format("%3.1f", Double.valueOf(d3));
        }
    }

    public static String formatMsToString(long j) {
        if (j <= 0 || j >= 86400000) {
            return "00:00";
        }
        int i = ((int) j) / 1000;
        int i2 = i % 60;
        int i3 = (i / 60) % 60;
        int i4 = i / 3600;
        Formatter formatter = new Formatter(new StringBuilder(), Locale.getDefault());
        if (i4 > 0) {
            return formatter.format("%d:%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3), Integer.valueOf(i2)).toString();
        }
        return formatter.format("%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i2)).toString();
    }

    public static long getCurrentTimeMS() {
        return SystemClock.currentThreadTimeMillis();
    }

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return simpleDateFormat.format(date);
    }

    public static String getCurrentTime(SimpleDateFormat simpleDateFormat) {
        Date date = new Date();
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return simpleDateFormat.format(date);
    }

    public static String getCurrentTime(long j, SimpleDateFormat simpleDateFormat) {
        Date date = new Date(j);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return simpleDateFormat.format(date);
    }
}
