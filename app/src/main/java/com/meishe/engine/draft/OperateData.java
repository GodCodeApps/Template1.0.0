package com.meishe.engine.draft;

public class OperateData {
    private String mDraftData;
    private String mDraftFilePath;
    private String mTag;

    public OperateData(String str, String str2) {
        this.mDraftData = str;
        this.mTag = str2;
    }

    public String getDraftData() {
        return this.mDraftData;
    }

    public void setDraftData(String str) {
        this.mDraftData = str;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setTag(String str) {
        this.mTag = str;
    }

    public String getDraftFilePath() {
        return this.mDraftFilePath;
    }

    public void setDraftFilePath(String str) {
        this.mDraftFilePath = str;
    }
}
