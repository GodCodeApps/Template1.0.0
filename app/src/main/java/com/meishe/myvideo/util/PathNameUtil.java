package com.meishe.myvideo.util;

public class PathNameUtil {
    public static String getPathSuffix(String str) {
        return !str.isEmpty() ? str.substring(str.lastIndexOf(".") + 1) : "";
    }

    public static String getOutOfPathSuffix(String str) {
        return !str.isEmpty() ? str.substring(0, str.lastIndexOf(".") + 1) : "";
    }

    public static String getPathNameNoSuffix(String str) {
        return !str.isEmpty() ? str.substring(str.lastIndexOf("/") + 1, str.lastIndexOf(".")) : "";
    }

    public static String getPathNameWithSuffix(String str) {
        return !str.isEmpty() ? str.substring(str.lastIndexOf("/") + 1) : "";
    }
}
