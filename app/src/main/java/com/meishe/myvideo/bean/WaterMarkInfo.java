package com.meishe.myvideo.bean;

public class WaterMarkInfo extends BaseInfo {
    public String mPicPath;
    public int mPicType;
    public String mWaterMarkPath;

    public WaterMarkInfo() {
    }

    public WaterMarkInfo(int i, String str, String str2) {
        this.mWaterMarkPath = str;
        this.mPicPath = str2;
        this.mPicType = i;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public int getPicType() {
        return this.mPicType;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getWaterMarkPath() {
        return this.mWaterMarkPath;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getPicPath() {
        return this.mPicPath;
    }
}
