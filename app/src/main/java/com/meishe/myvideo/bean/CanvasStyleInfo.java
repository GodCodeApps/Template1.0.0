package com.meishe.myvideo.bean;

public class CanvasStyleInfo extends BaseInfo {
    private String mFilePath;

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getFilePath() {
        return this.mFilePath;
    }

    @Override // com.meishe.myvideo.bean.BaseInfo
    public void setFilePath(String str) {
        this.mFilePath = str;
    }
}
