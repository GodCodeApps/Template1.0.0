package com.meicam.sdk;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NvsStatisticsInfo {
    private static int NV_OS_TYPE_ANDROID = 1;
    private Context context = null;

    public String getPhoneNumber() {
        return "";
    }

    public NvsStatisticsInfo(Context context2) {
        this.context = context2;
    }

    public String getAppId() {
        return this.context.getApplicationInfo().packageName;
    }

    public String getStartTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }

    public String getDeviceId() {
        String str = Settings.Secure.getString(this.context.getContentResolver(), "android_id") + Build.SERIAL;
        try {
            return toMD5(str).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        }
    }

    public String getModel() {
        return Build.MODEL;
    }

    public int getOsType() {
        return NV_OS_TYPE_ANDROID;
    }

    public String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public ArrayList getLngAndLat() {
        ArrayList arrayList = new ArrayList();
        Double valueOf = Double.valueOf(0.0d);
        arrayList.add(valueOf);
        arrayList.add(valueOf);
        return arrayList;
    }

    private static String toMD5(String str) throws NoSuchAlgorithmException {
        byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public void release() {
        this.context = null;
    }
}
