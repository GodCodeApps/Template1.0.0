package com.meishe.myvideo;

public class ServerURL {
    public static final String FEED_BACK = (baseUrl + "/feedback/index.php");
    public static final String MATERIAL_INFO = (baseUrl + "/materialinfo/index.php");
    private static String baseUrl = "https://vsapi.meishesdk.com";

    public static String getBaseUrl() {
        return baseUrl;
    }
}
