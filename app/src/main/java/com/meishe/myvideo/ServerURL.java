package com.meishe.myvideo;

public class ServerURL {
    private static String baseUrl = "https://vsapi.meishesdk.com";
    public static final String FEED_BACK = (baseUrl + "/feedback/index.php");
    public static final String MATERIAL_INFO = (baseUrl + "/materialinfo/index.php");

    public static String getBaseUrl() {
        return baseUrl;
    }
}
