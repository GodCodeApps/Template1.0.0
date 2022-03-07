package com.meicam.sdk;

public class NvsFontInfo {
    public String famlily;
    public boolean isItalic;
    public int weight;

    public NvsFontInfo(String str, int i, boolean z) {
        this.famlily = str;
        this.weight = i;
        this.isItalic = z;
    }
}
