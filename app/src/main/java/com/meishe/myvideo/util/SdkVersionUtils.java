package com.meishe.myvideo.util;

import android.net.Uri;
import android.provider.MediaStore;

public class SdkVersionUtils {
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");

    public static boolean checkedAndroid_Q() {
        return false;
    }

    public static String getPath(int i) {
        return getRealPathAndroid_Q(i);
    }

    private static String getRealPathAndroid_Q(int i) {
        Uri.Builder buildUpon = QUERY_URI.buildUpon();
        return buildUpon.appendPath(i + "").build().toString();
    }
}
