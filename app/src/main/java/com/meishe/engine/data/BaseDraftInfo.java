package com.meishe.engine.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

public class BaseDraftInfo implements Cloneable {
    public static final String ATTACHMENT_KEY_TRACK_INDEX = "attachment_key_track_index";
    @Expose
    protected int clipIndexInTrack;
    @SerializedName("attachment")
    protected Map<String, Object> mAttachment = new HashMap();
    @SerializedName("inPoint")
    protected long mInPoint;
    @SerializedName("isPanoramic")
    protected boolean mIsPanoramic;
    @SerializedName("itemIndicate")
    protected String mItemIndicate;
    @SerializedName("outPoint")
    protected long mOutPoint;
    @Expose
    protected double mSpeed = 1.0d;
    @SerializedName("trackIndex")
    protected int mTrackIndex;
    @SerializedName("zValue")
    protected float mZValue;

    public BaseDraftInfo(String str, int i, long j, long j2) {
        this.mItemIndicate = str;
        this.mTrackIndex = i;
        this.mInPoint = j;
        this.mOutPoint = j2;
    }

    public String getItemIndicate() {
        return this.mItemIndicate;
    }

    public void setItemIndicate(String str) {
        this.mItemIndicate = str;
    }

    public int getTrackIndex() {
        return this.mTrackIndex;
    }

    public void setTrackIndex(int i) {
        this.mTrackIndex = i;
    }

    public Map<String, Object> getAttachment() {
        return this.mAttachment;
    }

    public void setAttachment(Map<String, Object> map) {
        this.mAttachment = map;
    }

    public long getInPoint() {
        return this.mInPoint;
    }

    public void setInPoint(long j) {
        this.mInPoint = j;
    }

    public long getOutPoint() {
        return this.mOutPoint;
    }

    public void setOutPoint(long j) {
        this.mOutPoint = j;
    }

    public boolean isPanoramic() {
        return this.mIsPanoramic;
    }

    public void setIsPanoramic(boolean z) {
        this.mIsPanoramic = z;
    }

    public float getZValue() {
        return this.mZValue;
    }

    public void setZValue(float f) {
        this.mZValue = f;
    }

    public int getClipIndexInTrack() {
        return this.clipIndexInTrack;
    }

    public void setClipIndexInTrack(int i) {
        this.clipIndexInTrack = i;
    }

    public double getSpeed() {
        return this.mSpeed;
    }

    public void setSpeed(double d) {
        this.mSpeed = d;
    }
}
