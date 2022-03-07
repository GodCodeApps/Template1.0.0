package com.meishe.engine.bean;

import android.graphics.PointF;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;

public class MeicamStickerClip extends ClipInfo<NvsTimelineAnimatedSticker> implements Cloneable {
    private String coverImagePath;
    private String displayName;
    private String displayNamezhCN;
    private boolean hasAudio;
    private boolean horizontalFlip = false;
    private float leftVolume;
    @SerializedName("customanimatedStickerImagePath")
    private String mCustomanimatedStickerImagePath;
    @SerializedName("isCustomSticker")
    private boolean mIsCustomSticker;
    @SerializedName("id")
    private String mPackageId;
    private float rotation;
    private float scale;
    @SerializedName(MeicamVideoFx.ATTACHMENT_KEY_SUB_TYPE)
    private String stickerType = "general";
    private float translationX;
    private float translationY;
    private boolean verticalFlip = false;
    private float zValue;

    public MeicamStickerClip(String str) {
        super(CommonData.CLIP_STICKER);
        this.mPackageId = str;
    }

    public String getStickerType() {
        return this.stickerType;
    }

    public void setStickerType(String str) {
        this.stickerType = str;
    }

    public String getPackageId() {
        return this.mPackageId;
    }

    public void setPackageId(String str) {
        this.mPackageId = str;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float f) {
        this.scale = f;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float f) {
        this.rotation = f;
    }

    public float getTranslationX() {
        return this.translationX;
    }

    public void setTranslationX(float f) {
        this.translationX = f;
    }

    public float getTranslationY() {
        return this.translationY;
    }

    public void setTranslationY(float f) {
        this.translationY = f;
    }

    public boolean isHorizontalFlip() {
        return this.horizontalFlip;
    }

    public void setHorizontalFlip(boolean z) {
        this.horizontalFlip = z;
    }

    public boolean isVerticalFlip() {
        return this.verticalFlip;
    }

    public void setVerticalFlip(boolean z) {
        this.verticalFlip = z;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public String getDisplayNamezhCN() {
        return this.displayNamezhCN;
    }

    public void setDisplayNamezhCN(String str) {
        this.displayNamezhCN = str;
    }

    public void setCustomSticker(boolean z) {
        this.mIsCustomSticker = z;
    }

    public boolean isCustomSticker() {
        return this.mIsCustomSticker;
    }

    public void setCustomanimatedStickerImagePath(String str) {
        this.mCustomanimatedStickerImagePath = str;
    }

    public String getCustomanimatedStickerImagePath() {
        return this.mCustomanimatedStickerImagePath;
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
        nvsTimelineAnimatedSticker.setHorizontalFlip(isHorizontalFlip());
        nvsTimelineAnimatedSticker.setClipAffinityEnabled(false);
        PointF pointF = new PointF(getTranslationX(), getTranslationY());
        float scale2 = getScale();
        float rotation2 = getRotation();
        if (scale2 > 0.0f) {
            nvsTimelineAnimatedSticker.setScale(scale2);
        }
        nvsTimelineAnimatedSticker.setZValue(getzValue());
        nvsTimelineAnimatedSticker.setRotationZ(rotation2);
        setObject(nvsTimelineAnimatedSticker);
        nvsTimelineAnimatedSticker.setTranslation(pointF);
        float leftVolume2 = getLeftVolume();
        nvsTimelineAnimatedSticker.setVolumeGain(leftVolume2, leftVolume2);
        nvsTimelineAnimatedSticker.setHorizontalFlip(isHorizontalFlip());
        nvsTimelineAnimatedSticker.setVerticalFlip(isVerticalFlip());
        return nvsTimelineAnimatedSticker;
    }

    public void loadData(NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker) {
        if (nvsTimelineAnimatedSticker != null) {
            setObject(nvsTimelineAnimatedSticker);
            setInPoint(nvsTimelineAnimatedSticker.getInPoint());
            setOutPoint(nvsTimelineAnimatedSticker.getOutPoint());
            this.mPackageId = nvsTimelineAnimatedSticker.getAnimatedStickerPackageId();
            PointF translation = nvsTimelineAnimatedSticker.getTranslation();
            this.translationX = translation.x;
            this.translationY = translation.y;
            this.scale = nvsTimelineAnimatedSticker.getScale();
            this.rotation = nvsTimelineAnimatedSticker.getRotationZ();
            this.horizontalFlip = nvsTimelineAnimatedSticker.getHorizontalFlip();
            this.verticalFlip = nvsTimelineAnimatedSticker.getVerticalFlip();
            this.leftVolume = nvsTimelineAnimatedSticker.getVolumeGain().leftVolume;
            this.zValue = nvsTimelineAnimatedSticker.getZValue();
        }
    }

    public float getLeftVolume() {
        return this.leftVolume;
    }

    public void setLeftVolume(float f) {
        this.leftVolume = f;
    }

    public boolean isHasAudio() {
        return this.hasAudio;
    }

    public void setHasAudio(boolean z) {
        this.hasAudio = z;
    }

    public boolean ismIsCustomSticker() {
        return this.mIsCustomSticker;
    }

    public void setIsCustomSticker(boolean z) {
        this.mIsCustomSticker = z;
    }

    public String getCoverImagePath() {
        return this.coverImagePath;
    }

    public void setCoverImagePath(String str) {
        this.coverImagePath = str;
    }

    public float getzValue() {
        return this.zValue;
    }

    public void setzValue(float f) {
        this.zValue = f;
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
