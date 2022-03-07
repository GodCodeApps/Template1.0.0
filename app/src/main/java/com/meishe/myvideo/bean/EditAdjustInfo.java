package com.meishe.myvideo.bean;

public class EditAdjustInfo extends BaseInfo {
    public boolean mIsShowSelectState = false;

    public EditAdjustInfo(int i, String str) {
        super(str);
        this.mIconRcsId = i;
    }

    public EditAdjustInfo(int i, String str, boolean z) {
        super(str);
        this.mIsShowSelectState = z;
        this.mIconRcsId = i;
    }
}
