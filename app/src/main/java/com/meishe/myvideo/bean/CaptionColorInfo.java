package com.meishe.myvideo.bean;

import com.google.gson.annotations.SerializedName;

public class CaptionColorInfo extends BaseInfo {
    public float b;
    public float g;
    private String mColorValue;
    private String mFilePath;
    @SerializedName("isSelect")
    private boolean mSelected;
    public float r;

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getColorValue() {
        return this.mColorValue;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public void setColorValue(String str) {
        this.mColorValue = str;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public boolean isSelected() {
        return this.mSelected;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public void setSelected(boolean z) {
        this.mSelected = z;
    }

    @Override // com.meishe.myvideo.bean.BaseInfo
    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getFilePath() {
        return this.mFilePath;
    }
}
