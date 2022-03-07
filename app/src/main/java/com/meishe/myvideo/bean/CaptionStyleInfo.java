package com.meishe.myvideo.bean;

public class CaptionStyleInfo extends BaseInfo {
    @Override // com.meishe.myvideo.bean.BaseInfo
    public void setAsset(NvAssetInfo nvAssetInfo) {
        this.mAsset = nvAssetInfo;
    }

    @Override // com.meishe.myvideo.bean.BaseInfo
    public NvAssetInfo getAsset() {
        return this.mAsset;
    }
}
