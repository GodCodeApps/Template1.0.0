package com.meishe.myvideo.edit;

import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.TimelineData;

public class OperateData {
    private static final String TAG = "OperateData";
    private String mOperateData;
    private String mOperateFilePath;
    private int mTag;

    public OperateData() {
    }

    public OperateData(int i) {
        this.mTag = i;
    }

    public OperateData build() {
        this.mOperateData = TimelineData.getInstance().toJson();
        Logger.d(TAG, "build: mOperateData = " + this.mOperateData);
        return this;
    }

    public String getOperateData() {
        Logger.d(TAG, "getOperateData: mOperateData = " + this.mOperateData);
        return this.mOperateData;
    }

    public void setOperateData(String str) {
        this.mOperateData = str;
    }

    public int getTag() {
        return this.mTag;
    }

    public void setTag(int i) {
        this.mTag = i;
    }

    public String getOperateFilePath() {
        return this.mOperateFilePath;
    }

    public void setOperateFilePath(String str) {
        this.mOperateFilePath = str;
    }
}
