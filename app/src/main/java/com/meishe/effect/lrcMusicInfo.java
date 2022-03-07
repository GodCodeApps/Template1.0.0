package com.meishe.effect;

public class lrcMusicInfo {
    private int a;
    private String b;

    public String getMusicText() {
        return this.b;
    }

    public long getMusicTime() {
        return (long) this.a;
    }

    public void setMusicText(String str) {
        this.b = str;
    }

    public void setMusicTime(int i) {
        this.a = i;
    }
}
