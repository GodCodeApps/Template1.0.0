package com.meicam.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.HashSet;
import java.util.Set;

public class NvsSystemVariableManager {
    public static int getSystemVariableInt(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(str, 0);
    }

    public static void setSystemVariableInt(Context context, String str, int i) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public static String getSystemVariableString(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, "");
    }

    public static void setSystemVariableString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static Set<String> getSystemVariableStringSet(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getStringSet(str, new HashSet());
    }

    public static void setSystemVariableStringSet(Context context, String str, Set<String> set) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.remove(str);
        edit.putStringSet(str, set);
        edit.commit();
    }
}
