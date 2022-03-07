package com.meishe.myvideo.bean;

public class BeautyShapeInfo extends BaseInfo {
    public String mBeautyShapeId;
    public double mDefaultValue = 0.0d;
    public boolean mNeedDefaultStrength = false;
    public double mStrength = 0.0d;

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getBeautyShapeId() {
        return this.mBeautyShapeId;
    }

    public double getStrength() {
        return this.mStrength;
    }

    public boolean isNeedDefaultStrength() {
        return this.mNeedDefaultStrength;
    }

    public double getDefaultValue() {
        return this.mDefaultValue;
    }
}
