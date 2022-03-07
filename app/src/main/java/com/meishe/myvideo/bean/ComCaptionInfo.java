package com.meishe.myvideo.bean;

public class ComCaptionInfo extends BaseInfo {
    public String mCompoundPath;
    public String mPicPath;

    public ComCaptionInfo() {
    }

    public ComCaptionInfo(String str) {
        super(str);
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getCompoundPath() {
        return this.mCompoundPath;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getPicPath() {
        return this.mPicPath;
    }
}
