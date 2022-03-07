package com.meishe.myvideo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SpUtil {
    private static SharedPreferences sp;
    private static SpUtil spUtil;

    private SpUtil(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("default", 0);
        }
    }

    public static SpUtil getInstance(Context context) {
        if (spUtil == null) {
            spUtil = new SpUtil(context);
        }
        return spUtil;
    }

    public void putString(String str, String str2) {
        if (!sp.contains(str) || !sp.getString(str, "None").equals(str2)) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(str, str2);
            edit.apply();
        }
    }

    public void putBoolean(String str, boolean z) {
        if (!sp.contains(str) || sp.getBoolean(str, false) != z) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean(str, z);
            edit.apply();
        }
    }

    public void putInt(String str, int i) {
        if (!sp.contains(str) || sp.getInt(str, Integer.MIN_VALUE) != i) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt(str, i);
            edit.apply();
        }
    }

    public void putLong(String str, long j) {
        if (!sp.contains(str) || sp.getLong(str, Long.MIN_VALUE) != j) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putLong(str, j);
            edit.apply();
        }
    }

    public void putFloat(String str, float f) {
        if (!sp.contains(str) || sp.getFloat(str, Float.MIN_VALUE) != f) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putFloat(str, f);
            edit.apply();
        }
    }

    public boolean getBoolean(String str, boolean z) {
        return sp.getBoolean(str, z);
    }

    public String getString(String str) {
        return sp.getString(str, "None");
    }

    public int getInt(String str) {
        return sp.getInt(str, 1);
    }

    public void removeKey(String... strArr) {
        for (String str : strArr) {
            SharedPreferences.Editor edit = sp.edit();
            edit.remove(str);
            edit.apply();
        }
    }

    public boolean containKeys(String... strArr) {
        for (String str : strArr) {
            if (sp.contains(str)) {
                return true;
            }
        }
        return false;
    }

    public boolean setObjectToShare(Context context, Object obj, String str) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (obj == null) {
            return defaultSharedPreferences.edit().remove(str).commit();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            String str2 = new String(Base64.encode(byteArrayOutputStream.toByteArray(), 0));
            try {
                byteArrayOutputStream.close();
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor edit = defaultSharedPreferences.edit();
            edit.putString(str, str2);
            return edit.commit();
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static Object getObjectFromShare(Context context, String str) {
        try {
            String string = PreferenceManager.getDefaultSharedPreferences(context).getString(str, "");
            if (string != null) {
                if (!string.equals("")) {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(string.getBytes(), 0));
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    Object readObject = objectInputStream.readObject();
                    byteArrayInputStream.close();
                    objectInputStream.close();
                    return readObject;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
