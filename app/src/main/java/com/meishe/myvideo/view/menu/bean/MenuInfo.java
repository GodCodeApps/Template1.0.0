package com.meishe.myvideo.view.menu.bean;

import com.meishe.myvideo.bean.BaseInfo;

public class MenuInfo extends BaseInfo {
    public MenuInfo(String str, int i) {
        super(str, i);
    }

    public MenuInfo(String str, int i, boolean z) {
        super(str, i);
        this.checkAble = z;
    }

    public MenuInfo(String str, String str2, int i, int i2) {
        super(str, str2, i, i2);
    }
}
