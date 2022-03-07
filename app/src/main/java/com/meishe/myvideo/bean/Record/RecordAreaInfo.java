package com.meishe.myvideo.bean.Record;

import android.view.View;

public class RecordAreaInfo {
    private View areaView;
    private long inPoint;
    private int inPosition;
    private View leftHandle;
    private long outPoint;
    private View rightHandle;

    public RecordAreaInfo clone() {
        RecordAreaInfo recordAreaInfo = new RecordAreaInfo();
        recordAreaInfo.setInPoint(getInPoint());
        recordAreaInfo.setOutPoint(getOutPoint());
        recordAreaInfo.setInPosition(getInPosition());
        recordAreaInfo.setAreaView(getAreaView());
        recordAreaInfo.setLeftHandle(getLeftHandle());
        recordAreaInfo.setRightHandle(getRightHandle());
        return recordAreaInfo;
    }

    public long getInPoint() {
        return this.inPoint;
    }

    public void setInPoint(long j) {
        this.inPoint = j;
    }

    public long getOutPoint() {
        return this.outPoint;
    }

    public void setOutPoint(long j) {
        this.outPoint = j;
    }

    public int getInPosition() {
        return this.inPosition;
    }

    public void setInPosition(int i) {
        this.inPosition = i;
    }

    public View getAreaView() {
        return this.areaView;
    }

    public void setAreaView(View view) {
        this.areaView = view;
    }

    public View getLeftHandle() {
        return this.leftHandle;
    }

    public void setLeftHandle(View view) {
        this.leftHandle = view;
    }

    public View getRightHandle() {
        return this.rightHandle;
    }

    public void setRightHandle(View view) {
        this.rightHandle = view;
    }
}
