package com.meishe.myvideo.bean;

public class TransitionInfo extends BaseInfo {
    private String mEffectName;

    public TransitionInfo() {
    }

    public TransitionInfo(String str) {
        super(str);
    }

    public TransitionInfo(String str, String str2, int i, int i2) {
        super(str, str2, i, i2);
    }

    public TransitionInfo(String str, String str2, int i, int i2, int i3, String str3) {
        super(str, str2, i, i2, i3, str3);
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getName() {
        return super.getName();
    }

    public String getEffectName() {
        return this.mEffectName;
    }

    public void setEffectName(String str) {
        this.mEffectName = str;
    }
}
