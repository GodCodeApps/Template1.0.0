package com.meishe.player.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class ScreenUtils {
    public static int creenRealHeight;

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2sp(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int sp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static void setNine_Sixteenth(Context context, View view) {
        int windowWidth = getWindowWidth(context);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (windowWidth * 9) / 16;
        view.setLayoutParams(layoutParams);
    }

    public static final int getWindowWidth(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public static void setOne_Four_H(Context context, View view) {
        int windowWidth = getWindowWidth(context);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (windowWidth * 1) / 4;
        view.setLayoutParams(layoutParams);
    }

    public static void setOne_Four_V(Context context, View view) {
        int windowWidth = getWindowWidth(context);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (windowWidth * 1) / 4;
        view.setLayoutParams(layoutParams);
    }

    public static void setSixteen_TwentyThree(Context context, View view) {
        int windowWidth = getWindowWidth(context);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (windowWidth * 23) / 16;
        view.setLayoutParams(layoutParams);
    }

    public static int getBottomStatusHeight(Context context) {
        return getDpi(context) - getWindowHeight(context);
    }

    public static int getDpi(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class.forName("android.view.Display").getMethod("getRealMetrics", DisplayMetrics.class).invoke(defaultDisplay, displayMetrics);
            return displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static final int getWindowHeight(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    public static boolean hasNavigationBar(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        float f = displayMetrics.density;
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 17) {
            defaultDisplay.getRealMetrics(displayMetrics2);
        } else {
            try {
                Class.forName("android.view.Display").getMethod("getRealMetrics", DisplayMetrics.class).invoke(defaultDisplay, displayMetrics2);
            } catch (Exception e) {
                displayMetrics2.setToDefaults();
                e.printStackTrace();
            }
        }
        creenRealHeight = displayMetrics2.heightPixels;
        Math.sqrt(Math.pow((double) displayMetrics2.widthPixels, 2.0d) + Math.pow((double) creenRealHeight, 2.0d));
        Resources resources = activity.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        boolean z = identifier > 0 ? resources.getBoolean(identifier) : false;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str = (String) cls.getMethod("get", String.class).invoke(cls, "qemu.hw.mainkeys");
            if ("1".equals(str)) {
                return false;
            }
            if ("0".equals(str)) {
                return true;
            }
            return z;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean checkDeviceHasNavigationBar(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return creenRealHeight - displayMetrics.widthPixels > 0;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources;
        int identifier;
        if (!hasNavBar(context) || (identifier = (resources = context.getResources()).getIdentifier("navigation_bar_height", "dimen", "android")) <= 0) {
            return 0;
        }
        return resources.getDimensionPixelSize(identifier);
    }

    @TargetApi(14)
    public static boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (identifier != 0) {
            return resources.getBoolean(identifier);
        }
        return false;
    }

    private static String getNavBarOverride() {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                Class<?> cls = Class.forName("android.os.SystemProperties");
                return (String) cls.getMethod("get", String.class).invoke(cls, "qemu.hw.mainkeys");
            } catch (Throwable unused) {
            }
        }
        return null;
    }
}
