package com.meishe.myvideo.util;

import android.content.Context;
import java.util.Locale;

public class SystemUtils {
    public static boolean isZh(Context context) {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            if (locale != null && locale.getLanguage().endsWith("zh")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
