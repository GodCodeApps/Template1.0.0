package com.meishe.myvideo.util;

import android.content.Context;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PixelPerMicrosecondUtil {
    private static final String TAG = "PixelPerMicrosecondUtil";
    private static double mDefaultPerMicrosecond = 0.0d;
    private static double mPixelPerMicrosecond = 0.0d;
    private static List<PixelPerMicrosecondChangeListener> mPixelPerMicrosecondChangeListener = new ArrayList();
    private static float mScale = 1.0f;

    public interface PixelPerMicrosecondChangeListener {
        void onPixelPerMicrosecondChange(double d, float f);
    }

    public static void init(Context context) {
        double screenWidth = (double) ScreenUtils.getScreenWidth(context);
        Double.isNaN(screenWidth);
        mPixelPerMicrosecond = (screenWidth / 10.0d) / 1000000.0d;
        mDefaultPerMicrosecond = mPixelPerMicrosecond;
    }

    public static double getPixelPerMicrosecond(Context context) {
        if (mPixelPerMicrosecond == 0.0d) {
            mPixelPerMicrosecond = (double) (((long) (ScreenUtils.getScreenWidth(context) / 10)) / 1000000);
        }
        return mPixelPerMicrosecond;
    }

    public static void setScale(float f) {
        mScale = f;
        double d = mDefaultPerMicrosecond;
        double d2 = (double) f;
        Double.isNaN(d2);
        mPixelPerMicrosecond = d * d2;
        for (PixelPerMicrosecondChangeListener pixelPerMicrosecondChangeListener : mPixelPerMicrosecondChangeListener) {
            pixelPerMicrosecondChangeListener.onPixelPerMicrosecondChange(mPixelPerMicrosecond, f);
        }
    }

    private static String formatDouble(double d) {
        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(20);
        instance.setGroupingUsed(false);
        return instance.format(d);
    }

    public static void addPixelPerMicrosecondChangeListener(PixelPerMicrosecondChangeListener pixelPerMicrosecondChangeListener) {
        mPixelPerMicrosecondChangeListener.add(pixelPerMicrosecondChangeListener);
    }

    public static void removeAllListener() {
        mPixelPerMicrosecondChangeListener.clear();
    }

    public static int durationToLength(long j) {
        double d = (double) j;
        double d2 = mPixelPerMicrosecond;
        Double.isNaN(d);
        return (int) Math.floor((d * d2) + 0.5d);
    }

    public static long lengthToDuration(int i) {
        double d = (double) i;
        double d2 = mPixelPerMicrosecond;
        Double.isNaN(d);
        return (long) Math.floor((d / d2) + 0.5d);
    }

    public static void resetScale() {
        mScale = 1.0f;
    }

    public static float getScale() {
        return mScale;
    }
}
