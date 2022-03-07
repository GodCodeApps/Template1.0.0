package com.meishe.engine.data;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meishe.engine.interf.IEffect;

public class MeicamVideoClipFxInfo extends BaseDraftInfo implements IEffect, IDraft<NvsTimelineVideoFx> {
    @SerializedName("duration")
    private long mDuration;
    @SerializedName("effectMode")
    private int mEffectMode;
    @SerializedName("effectName")
    private String mEffectName;
    @Expose
    private float mFxIntensity;
    @Expose
    private boolean mIsCartoon;
    @Expose
    private boolean mIsGrayScale;
    @Expose
    private boolean mIsStrokenOnly;
    private NvsTimelineVideoFx mNvsTimelineVideoFx;
    @SerializedName("packageId")
    private String mPackageId;

    @Override // com.meishe.engine.data.IDraft
    public Object fromJson(String str) {
        return null;
    }

    @Override // com.meishe.engine.data.IDraft
    public String getMapKey(String str) {
        return null;
    }

    @Override // com.meishe.engine.data.IDraft
    public String toJson() {
        return null;
    }

    public MeicamVideoClipFxInfo() {
        super(null, 0, -1, -1);
        this.mFxIntensity = 1.0f;
        this.mIsCartoon = false;
        this.mIsStrokenOnly = true;
        this.mIsGrayScale = true;
        this.mPackageId = null;
        this.mEffectMode = 0;
        this.mFxIntensity = 1.0f;
    }

    public void setEffectMode(int i) {
        if (i == 0 || i == 1) {
            this.mEffectMode = i;
        } else {
            Log.e("", "invalid mode data");
        }
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    @Override // com.meishe.engine.interf.IEffect
    public int getEffectMode() {
        return this.mEffectMode;
    }

    @Override // com.meishe.engine.interf.IEffect
    public String getPackageId() {
        return this.mPackageId;
    }

    public void setPackageId(String str) {
        this.mPackageId = str;
    }

    public String getEffectName() {
        return this.mEffectName;
    }

    public void setEffectName(String str) {
        this.mEffectName = str;
    }

    public float getFxIntensity() {
        return this.mFxIntensity;
    }

    public void setFxIntensity(float f) {
        this.mFxIntensity = f;
    }

    public boolean isCartoon() {
        return this.mIsCartoon;
    }

    public void setIsCartoon(boolean z) {
        this.mIsCartoon = z;
    }

    public boolean isStrokenOnly() {
        return this.mIsStrokenOnly;
    }

    public void setIsStrokenOnly(boolean z) {
        this.mIsStrokenOnly = z;
    }

    public boolean isGrayScale() {
        return this.mIsGrayScale;
    }

    public void setIsGrayScale(boolean z) {
        this.mIsGrayScale = z;
    }

    public NvsTimelineVideoFx bindToTimeline(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            return null;
        }
        if (getEffectMode() == 0) {
            this.mNvsTimelineVideoFx = nvsTimeline.addBuiltinTimelineVideoFx(getInPoint(), getDuration(), getPackageId());
        } else {
            this.mNvsTimelineVideoFx = nvsTimeline.addPackagedTimelineVideoFx(getInPoint(), getDuration(), getPackageId());
        }
        return this.mNvsTimelineVideoFx;
    }

    public void loadData(NvsTimelineVideoFx nvsTimelineVideoFx) {
        if (nvsTimelineVideoFx != null) {
            this.mNvsTimelineVideoFx = nvsTimelineVideoFx;
            this.mInPoint = nvsTimelineVideoFx.getInPoint();
            this.mDuration = nvsTimelineVideoFx.getOutPoint() - nvsTimelineVideoFx.getInPoint();
            this.mPackageId = nvsTimelineVideoFx.getTimelineVideoFxPackageId();
            this.mEffectMode = nvsTimelineVideoFx.getTimelineVideoFxType();
        }
    }

    public void setNvsObject(NvsTimelineVideoFx nvsTimelineVideoFx) {
        this.mNvsTimelineVideoFx = nvsTimelineVideoFx;
    }

    @Override // com.meishe.engine.data.IDraft
    public NvsTimelineVideoFx getNvsObject() {
        return this.mNvsTimelineVideoFx;
    }

    @Override // java.lang.Object
    @NonNull
    public MeicamVideoClipFxInfo clone() {
        try {
            return (MeicamVideoClipFxInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
