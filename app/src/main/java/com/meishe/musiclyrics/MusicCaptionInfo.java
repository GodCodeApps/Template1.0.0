package com.meishe.musiclyrics;

public class MusicCaptionInfo {
    private String a;
    private long b;
    private long c;

    public long getCaptionDurtion() {
        return this.c;
    }

    public long getCaptionStartTime() {
        return this.b;
    }

    public String getCaptionText() {
        return this.a;
    }

    public void setCaptionDurtion(long j) {
        this.c = j;
    }

    public void setCaptionStartTime(long j) {
        this.b = j;
    }

    public void setCaptionText(String str) {
        this.a = str;
    }
}
