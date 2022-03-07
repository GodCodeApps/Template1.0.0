package com.meishe.engine.bean;

import java.io.Serializable;

public class MeicamCompoundCaptionItem implements Cloneable, Serializable {
    private String font = "";
    private int index;
    private String text;
    private float[] textColor = {1.0f, 1.0f, 1.0f, 1.0f};

    public MeicamCompoundCaptionItem(int i, String str) {
        this.index = i;
        this.text = str;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getFont() {
        return this.font;
    }

    public void setFont(String str) {
        this.font = str;
    }

    public float[] getTextColor() {
        return this.textColor;
    }

    public void setTextColor(float[] fArr) {
        this.textColor = fArr;
    }
}
