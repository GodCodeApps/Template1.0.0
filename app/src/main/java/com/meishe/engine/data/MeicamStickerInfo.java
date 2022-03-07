package com.meishe.engine.data;

import android.graphics.PointF;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsVolume;
import com.meishe.engine.interf.IEffect;

public class MeicamStickerInfo extends BaseDraftInfo implements IEffect, IDraft<NvsTimelineAnimatedSticker> {
    @SerializedName("coverImagePath")
    private String mCoverImagePath;
    @SerializedName("customanimatedStickerImagePath")
    private String mCustomanimatedStickerImagePath;
    @SerializedName("effectMode")
    private int mEffectMode;
    @SerializedName("hasAudio")
    private boolean mHasAudio;
    @SerializedName("isCustomSticker")
    private boolean mIsCustomSticker;
    @SerializedName("leftVolume")
    private float mLeftVolume;
    private NvsTimelineAnimatedSticker mNvsSticker;
    @SerializedName("animatedStickerPackageId")
    private String mPackageId;
    @SerializedName("rightVolume")
    private float mRightVolume;
    @SerializedName("stickerHorizontalFlip")
    private boolean mStickerHorizontalFlip;
    @SerializedName("stickerRotationZ")
    private float mStickerRotationZ;
    @SerializedName("stickerScale")
    private float mStickerScale;
    @SerializedName("stickerTranslation")
    private PointF mStickerTranslation;
    @SerializedName("stickerTranslationX")
    private float mStickerTranslationX;
    @SerializedName("stickerTranslationY")
    private float mStickerTranslationY;
    @SerializedName("verticalFlip")
    private boolean mVerticalFlip;

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

    public MeicamStickerInfo() {
        super(null, -1, -1, -1);
    }

    public MeicamStickerInfo(String str, int i, long j, long j2) {
        super(str, i, j, j2);
    }

    public void setPackageId(String str) {
        this.mPackageId = str;
    }

    public String getCustomanimatedStickerImagePath() {
        return this.mCustomanimatedStickerImagePath;
    }

    public void setCustomanimatedStickerImagePath(String str) {
        this.mCustomanimatedStickerImagePath = str;
    }

    public String getCoverImagePath() {
        return this.mCoverImagePath;
    }

    public void setCoverImagePath(String str) {
        this.mCoverImagePath = str;
    }

    public PointF getStickerTranslation() {
        return this.mStickerTranslation;
    }

    public void setStickerTranslation(PointF pointF) {
        this.mStickerTranslation = pointF;
    }

    public float getStickerTranslationX() {
        return this.mStickerTranslationX;
    }

    public void setStickerTranslationX(float f) {
        this.mStickerTranslationX = f;
    }

    public float getStickerTranslationY() {
        return this.mStickerTranslationY;
    }

    public void setStickerTranslationY(float f) {
        this.mStickerTranslationY = f;
    }

    public float getStickerScale() {
        return this.mStickerScale;
    }

    public void setStickerScale(float f) {
        this.mStickerScale = f;
    }

    public float getStickerRotationZ() {
        return this.mStickerRotationZ;
    }

    public void setStickerRotationZ(float f) {
        this.mStickerRotationZ = f;
    }

    public boolean isStickerHorizontalFlip() {
        return this.mStickerHorizontalFlip;
    }

    public void setStickerHorizontalFlip(boolean z) {
        this.mStickerHorizontalFlip = z;
    }

    public float getLeftVolume() {
        return this.mLeftVolume;
    }

    public void setLeftVolume(float f) {
        this.mLeftVolume = f;
    }

    public float getRightVolume() {
        return this.mRightVolume;
    }

    public void setRightVolume(float f) {
        this.mRightVolume = f;
    }

    public boolean isVerticalFlip() {
        return this.mVerticalFlip;
    }

    public void setVerticalFlip(boolean z) {
        this.mVerticalFlip = z;
    }

    public boolean isCustomSticker() {
        return this.mIsCustomSticker;
    }

    public void setIsCustomSticker(boolean z) {
        this.mIsCustomSticker = z;
    }

    @Override // com.meishe.engine.interf.IEffect
    public int getEffectMode() {
        return this.mEffectMode;
    }

    @Override // com.meishe.engine.interf.IEffect
    public String getPackageId() {
        return this.mPackageId;
    }

    public void loadData(NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker) {
        if (nvsTimelineAnimatedSticker != null) {
            this.mNvsSticker = nvsTimelineAnimatedSticker;
            this.mIsPanoramic = nvsTimelineAnimatedSticker.isPanoramic();
            this.mInPoint = nvsTimelineAnimatedSticker.getInPoint();
            this.mOutPoint = nvsTimelineAnimatedSticker.getOutPoint();
            this.mPackageId = nvsTimelineAnimatedSticker.getAnimatedStickerPackageId();
            PointF translation = nvsTimelineAnimatedSticker.getTranslation();
            this.mStickerTranslation = translation;
            this.mStickerTranslationX = translation.x;
            this.mStickerTranslationY = translation.y;
            this.mStickerScale = nvsTimelineAnimatedSticker.getScale();
            this.mStickerRotationZ = nvsTimelineAnimatedSticker.getRotationZ();
            this.mStickerHorizontalFlip = nvsTimelineAnimatedSticker.getHorizontalFlip();
            NvsVolume volumeGain = nvsTimelineAnimatedSticker.getVolumeGain();
            this.mLeftVolume = volumeGain.leftVolume;
            this.mRightVolume = volumeGain.rightVolume;
            this.mHasAudio = nvsTimelineAnimatedSticker.hasAudio();
            this.mZValue = nvsTimelineAnimatedSticker.getZValue();
        }
    }

    public void setNvsObject(NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker) {
        this.mNvsSticker = nvsTimelineAnimatedSticker;
    }

    @Override // com.meishe.engine.data.IDraft
    public NvsTimelineAnimatedSticker getNvsObject() {
        return this.mNvsSticker;
    }

    public NvsTimelineAnimatedSticker bindToTimeline(NvsTimeline nvsTimeline) {
        NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker;
        if (nvsTimeline == null) {
            return null;
        }
        long outPoint = getOutPoint() - getInPoint();
        if (isCustomSticker()) {
            nvsTimelineAnimatedSticker = nvsTimeline.addCustomAnimatedSticker(getInPoint(), outPoint, getPackageId(), getCustomanimatedStickerImagePath());
        } else {
            nvsTimelineAnimatedSticker = nvsTimeline.addAnimatedSticker(getInPoint(), outPoint, getPackageId());
        }
        this.mNvsSticker = nvsTimelineAnimatedSticker;
        this.mNvsSticker.setZValue(getZValue());
        this.mNvsSticker.setHorizontalFlip(isStickerHorizontalFlip());
        PointF stickerTranslation = getStickerTranslation();
        float stickerScale = getStickerScale();
        float stickerRotationZ = getStickerRotationZ();
        if (stickerScale > 0.0f) {
            this.mNvsSticker.setScale(stickerScale);
        }
        this.mNvsSticker.setRotationZ(stickerRotationZ);
        if (stickerTranslation != null) {
            this.mNvsSticker.setTranslation(stickerTranslation);
        }
        float leftVolume = getLeftVolume();
        this.mNvsSticker.setVolumeGain(leftVolume, leftVolume);
        if (getTrackIndex() >= 0) {
            this.mNvsSticker.setAttachment(BaseDraftInfo.ATTACHMENT_KEY_TRACK_INDEX, Integer.valueOf(getTrackIndex()));
        }
        return this.mNvsSticker;
    }

    @Override // java.lang.Object
    @NonNull
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
