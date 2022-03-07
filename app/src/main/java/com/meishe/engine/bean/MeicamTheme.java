package com.meishe.engine.bean;

import com.google.gson.annotations.SerializedName;

public class MeicamTheme implements Cloneable {
    @SerializedName("themePackageId")
    private String mThemePackageId;
    @SerializedName("themeTitleText")
    private String mThemeTitleText;
    @SerializedName("themeTitlesClipDuration")
    private long mThemeTitlesClipDuration;
    @SerializedName("themeTrailerText")
    private String mThemeTrailerText;

    public MeicamTheme(String str) {
        this.mThemePackageId = str;
    }

    public String getThemePackageId() {
        return this.mThemePackageId;
    }

    public void setThemePackageId(String str) {
        this.mThemePackageId = str;
    }

    public long getThemeTitlesClipDuration() {
        return this.mThemeTitlesClipDuration;
    }

    public void setThemeTitlesClipDuration(long j) {
        this.mThemeTitlesClipDuration = j;
    }

    public String getThemeTitleText() {
        return this.mThemeTitleText;
    }

    public void setThemeTitleText(String str) {
        this.mThemeTitleText = str;
    }

    public String getThemeTrailerText() {
        return this.mThemeTrailerText;
    }

    public void setThemeTrailerText(String str) {
        this.mThemeTrailerText = str;
    }
}
