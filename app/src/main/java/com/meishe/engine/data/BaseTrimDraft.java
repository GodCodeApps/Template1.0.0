package com.meishe.engine.data;

import com.google.gson.annotations.SerializedName;

public class BaseTrimDraft extends BaseDraftInfo {
    @SerializedName("duration")
    protected long mDuration;
    @SerializedName("speed")
    protected double mSpeed = 1.0d;
    @SerializedName("trimIn")
    protected long mTrimIn;
    @SerializedName("trimOut")
    protected long mTrimOut;

    public BaseTrimDraft(long j, long j2, long j3, double d) {
        super("", -1, 0, -1);
        this.mTrimIn = j;
        this.mTrimOut = j2;
        this.mDuration = j3;
        this.mSpeed = d;
    }

    public long getTrimIn() {
        return this.mTrimIn;
    }

    public void setTrimIn(long j) {
        this.mTrimIn = j;
    }

    public long getTrimOut() {
        return this.mTrimOut;
    }

    public void setTrimOut(long j) {
        this.mTrimOut = j;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    @Override // com.meishe.engine.data.BaseDraftInfo
    public double getSpeed() {
        return this.mSpeed;
    }

    @Override // com.meishe.engine.data.BaseDraftInfo
    public void setSpeed(double d) {
        this.mSpeed = d;
    }
}
