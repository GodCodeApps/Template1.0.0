package com.meishe.engine.bean;

import androidx.annotation.Nullable;
import java.io.Serializable;

public class ClipInfo<T> extends NvsObject<T> implements Cloneable, Comparable<ClipInfo>, Serializable {
    private long inPoint;
    private int index;
    private long outPoint;
    protected String type = "base";

    public ClipInfo() {
        super(null);
    }

    public ClipInfo(String str) {
        super(null);
        this.type = str;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
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

    public int compareTo(ClipInfo clipInfo) {
        if (this.inPoint < clipInfo.getInPoint()) {
            return -1;
        }
        return this.inPoint > clipInfo.getInPoint() ? 1 : 0;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass() && ((ClipInfo) obj).getInPoint() == getInPoint()) {
            return true;
        }
        return false;
    }
}
