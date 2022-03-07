package com.meishe.myvideo.bean.menu;

import com.meishe.myvideo.bean.BaseInfo;

public class EditMenuInfo extends BaseInfo {
    public boolean mIsShowSelectState = false;

    public EditMenuInfo(int i, String str) {
        super(str);
        this.mIconRcsId = i;
    }

    public EditMenuInfo(int i, String str, boolean z) {
        super(str);
        this.mIsShowSelectState = z;
        this.mIconRcsId = i;
    }
}
