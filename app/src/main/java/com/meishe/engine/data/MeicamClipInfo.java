package com.meishe.engine.data;

import android.graphics.RectF;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.background.MeicamStoryboardInfo;
import com.meishe.engine.util.StoryboardUtil;
import java.util.HashMap;
import java.util.Map;

public class MeicamClipInfo extends BaseTrimDraft implements IDraft<NvsVideoClip> {
    private static final String ATTACHMENT_VALUE_CLIP_ROTATION = "clipTransformAttachment";
    public static final String ATTACHMENT_VALUE_CROPPER = "cropperAttachment";
    public static final String ATTACHMENT_VALUE_FILTER = "filterAttachment";
    public static final String ATTACHMENT_VALUE_ROTATION = "rotationClipTransformAttachment";
    public static final String ATTACHMENT_VALUE_TRANSFORM_2D = "cropperTransform2DAttachment";
    private static final String[] sFxName = {CommonData.VIDEO_FX_BEAUTY_STRENGTH, CommonData.VIDEO_FX_BEAUTY_WHITENING, CommonData.VIDEO_FX_BEAUTY_REDDENING, "Face Size Warp Degree", "Eye Size Warp Degree", "Chin Length Warp Degree", "Forehead Height Warp Degree", "Nose Width Warp Degree", "Mouth Size Warp Degree"};
    @SerializedName("aseetAspectRatio")
    private float mAseetAspectRatio;
    private MeicamStoryboardInfo mBackgroundInfo;
    @SerializedName("backgroundType")
    private int mBackgroundType;
    @SerializedName("backgroundXmlString")
    private String mBackgroundXmlString;
    @SerializedName("blackPoint")
    private float mBlackPoint;
    @SerializedName("brightness")
    private float mBrightnessVal;
    @SerializedName("contentRotation")
    private String mContentRotation;
    @SerializedName("contrast")
    private float mContrastVal;
    @SerializedName("cropperTransform2DString")
    private String mCropperTransform2DString;
    @SerializedName("cropperXmlString")
    private String mCropperXmlString;
    @SerializedName("faceEffectParameter")
    private Map<String, Float> mFaceEffectParameter = new HashMap();
    @SerializedName("mediaFilePath")
    private String mFilePath = null;
    @SerializedName("filterIntensity")
    private float mFilterIntensity;
    @SerializedName("filterPackageId")
    private String mFilterPackageId;
    @SerializedName("highlight")
    private float mHighlight;
    @SerializedName("imageDisplayMode")
    private int mImageDisplayMode;
    @SerializedName("mIsOpenPhotoMove")
    private boolean mIsOpenPhotoMove = true;
    @SerializedName("leftVolumeGain")
    private float mLeftVolumeGain;
    private NvsVideoClip mNvsVideoClip;
    @SerializedName("pipTransformScale")
    private float mPipTransformScale;
    @SerializedName("rightVolumeGain")
    private float mRightVolumeGain;
    @SerializedName("roleInTheme")
    private int mRoleInTheme;
    @SerializedName("rotation")
    private int mRotate;
    @SerializedName("mRotationClipTransformAttachment")
    private String mRotationClipTransformAttachment = ATTACHMENT_VALUE_ROTATION;
    @SerializedName("saturation")
    private float mSaturationVal;
    @SerializedName(StoryboardUtil.STORYBOARD_KEY_SCALE_X)
    private float mScaleX;
    @SerializedName(StoryboardUtil.STORYBOARD_KEY_SCALE_Y)
    private float mScaleY;
    @SerializedName("shadow")
    private float mShadow;
    @SerializedName("amount")
    private float mSharpenVal;
    @SerializedName("temperature")
    private float mTemperature;
    @SerializedName("tint")
    private float mTint;
    @SerializedName("transformOpacity")
    private float mTransformOpacity;
    @SerializedName("transformRotation")
    private float mTransformRotation;
    @SerializedName("transformScale")
    private float mTransformScale;
    @SerializedName("transformX")
    private float mTransformX;
    @SerializedName("transformY")
    private float mTransformY;
    private MeicamVideoClipFxInfo mVideoClipFxInfo;
    @SerializedName("degree")
    private float mVignetteVal;
    private boolean m_mute;
    private RectF m_normalEndROI;
    private RectF m_normalStartROI;
    private float m_pan;
    private float m_scan;

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

    public float getPan() {
        return this.m_pan;
    }

    public void setPan(float f) {
        this.m_pan = f;
    }

    public float getScan() {
        return this.m_scan;
    }

    public void setScan(float f) {
        this.m_scan = f;
    }

    public RectF getNormalStartROI() {
        return this.m_normalStartROI;
    }

    public void setNormalStartROI(RectF rectF) {
        this.m_normalStartROI = rectF;
    }

    public RectF getNormalEndROI() {
        return this.m_normalEndROI;
    }

    public void setNormalEndROI(RectF rectF) {
        this.m_normalEndROI = rectF;
    }

    public boolean ismIsOpenPhotoMove() {
        return this.mIsOpenPhotoMove;
    }

    public void setmIsOpenPhotoMove(boolean z) {
        this.mIsOpenPhotoMove = z;
    }

    public int getImgDispalyMode() {
        return this.mImageDisplayMode;
    }

    public void setImgDispalyMode(int i) {
        this.mImageDisplayMode = i;
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public void setScaleX(float f) {
        this.mScaleX = f;
    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public void setScaleY(float f) {
        this.mScaleY = f;
    }

    public int getRotateAngle() {
        return this.mRotate;
    }

    public void setRotateAngle(int i) {
        this.mRotate = i;
    }

    public float getVolume() {
        return this.mLeftVolumeGain;
    }

    public void setVolume(float f) {
        this.mLeftVolumeGain = f;
    }

    public float getBrightnessVal() {
        return this.mBrightnessVal;
    }

    public void setBrightnessVal(float f) {
        this.mBrightnessVal = f;
    }

    public float getContrastVal() {
        return this.mContrastVal;
    }

    public void setContrastVal(float f) {
        this.mContrastVal = f;
    }

    public float getSaturationVal() {
        return this.mSaturationVal;
    }

    public void setSaturationVal(float f) {
        this.mSaturationVal = f;
    }

    public float getVignetteVal() {
        return this.mVignetteVal;
    }

    public void setVignetteVal(float f) {
        this.mVignetteVal = f;
    }

    public float getSharpenVal() {
        return this.mSharpenVal;
    }

    public void setSharpenVal(float f) {
        this.mSharpenVal = f;
    }

    public float getTransformX() {
        return this.mTransformX;
    }

    public void setTransformX(float f) {
        this.mTransformX = f;
    }

    public float getTransformY() {
        return this.mTransformY;
    }

    public void setTransformY(float f) {
        this.mTransformY = f;
    }

    public float getTransformScale() {
        return this.mTransformScale;
    }

    public void setTransformScale(float f) {
        this.mTransformScale = f;
    }

    public float getTransformRotation() {
        return this.mTransformRotation;
    }

    public void setTransformRotation(float f) {
        this.mTransformRotation = f;
    }

    public String getBackgroundXmlString() {
        return this.mBackgroundXmlString;
    }

    public MeicamClipInfo() {
        super(-1, -1, -1, -1.0d);
        this.mSpeed = -1.0d;
        this.m_mute = false;
        this.mTrimIn = -1;
        this.mTrimOut = -1;
        this.mBrightnessVal = -1.0f;
        this.mContrastVal = -1.0f;
        this.mSaturationVal = -1.0f;
        this.mSharpenVal = 0.0f;
        this.mVignetteVal = 0.0f;
        this.mLeftVolumeGain = 0.0f;
        this.mRotate = 0;
        this.mScaleX = -2.0f;
        this.mScaleY = -2.0f;
        this.m_pan = 0.0f;
        this.m_scan = 0.0f;
        this.mTransformRotation = 0.0f;
        this.mTransformScale = 1.0f;
        this.mTransformX = 0.0f;
        this.mTransformY = 0.0f;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public void setMute(boolean z) {
        this.m_mute = z;
    }

    public boolean getMute() {
        return this.m_mute;
    }

    public void changeTrimIn(long j) {
        this.mTrimIn = j;
    }

    @Override // com.meishe.engine.data.BaseTrimDraft
    public long getTrimIn() {
        return this.mTrimIn;
    }

    public void changeTrimOut(long j) {
        this.mTrimOut = j;
    }

    @Override // com.meishe.engine.data.BaseTrimDraft
    public long getTrimOut() {
        return this.mTrimOut;
    }

    public MeicamVideoClipFxInfo getVideoClipFxInfo() {
        return this.mVideoClipFxInfo;
    }

    public void setVideoClipFxInfo(MeicamVideoClipFxInfo meicamVideoClipFxInfo) {
        this.mVideoClipFxInfo = meicamVideoClipFxInfo;
    }

    public void setCropperXmlString(String str) {
        this.mCropperXmlString = str;
    }

    public void setCropperTransform2DString(String str) {
        this.mCropperTransform2DString = str;
    }

    public String getCropperXmlString() {
        return this.mCropperXmlString;
    }

    public String getCropperTransform2DString() {
        return this.mCropperTransform2DString;
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

    public MeicamStoryboardInfo getBackgroundInfo() {
        return this.mBackgroundInfo;
    }

    public void setBackgroundInfo(MeicamStoryboardInfo meicamStoryboardInfo) {
        this.mBackgroundInfo = meicamStoryboardInfo;
    }

    public String getFilterPackageId() {
        return this.mFilterPackageId;
    }

    public void setFilterPackageId(String str) {
        this.mFilterPackageId = str;
    }

    public void loadData(NvsVideoClip nvsVideoClip) {
        if (nvsVideoClip != null) {
            this.mNvsVideoClip = nvsVideoClip;
            this.mFilePath = nvsVideoClip.getFilePath();
            this.mInPoint = nvsVideoClip.getInPoint();
            this.mOutPoint = nvsVideoClip.getOutPoint();
            this.mTrimIn = nvsVideoClip.getTrimIn();
            this.mTrimOut = nvsVideoClip.getTrimOut();
            this.mSpeed = nvsVideoClip.getSpeed();
            this.mRotate = nvsVideoClip.getExtraVideoRotation();
            this.mRoleInTheme = nvsVideoClip.getRoleInTheme();
            int fxCount = nvsVideoClip.getFxCount();
            for (int i = 0; i < fxCount; i++) {
                NvsVideoFx fxByIndex = nvsVideoClip.getFxByIndex(i);
                String builtinVideoFxName = fxByIndex.getBuiltinVideoFxName();
                if ("Tint".equals(builtinVideoFxName)) {
                    this.mTint = (float) fxByIndex.getFloatVal(builtinVideoFxName);
                    this.mTemperature = (float) fxByIndex.getFloatVal("Temperature");
                } else if ("Vignette".equals(builtinVideoFxName)) {
                    this.mVignetteVal = (float) fxByIndex.getFloatVal("Degree");
                } else if ("Sharpen".equals(builtinVideoFxName)) {
                    this.mSharpenVal = (float) fxByIndex.getFloatVal("Amount");
                } else if ("BasicImageAdjust".equals(builtinVideoFxName)) {
                    this.mBrightnessVal = (float) fxByIndex.getFloatVal("Brightness");
                    this.mContrastVal = (float) fxByIndex.getFloatVal("Contrast");
                    this.mSaturationVal = (float) fxByIndex.getFloatVal("Saturation");
                    this.mHighlight = (float) fxByIndex.getFloatVal("Highlight");
                    this.mShadow = (float) fxByIndex.getFloatVal("Shadow");
                    this.mBlackPoint = (float) fxByIndex.getFloatVal("BlackPoint");
                } else if (CommonData.VIDEO_FX_AR_SCENE.equals(builtinVideoFxName)) {
                    this.mSharpenVal = (float) fxByIndex.getFloatVal("Amount");
                    String[] strArr = sFxName;
                    for (String str : strArr) {
                        this.mFaceEffectParameter.put(str, Float.valueOf((float) fxByIndex.getFloatVal(str)));
                    }
                }
            }
        }
    }

    public void setNvsObject(NvsVideoClip nvsVideoClip) {
        this.mNvsVideoClip = nvsVideoClip;
    }

    @Override // com.meishe.engine.data.IDraft
    public NvsVideoClip getNvsObject() {
        return this.mNvsVideoClip;
    }

    public Map<String, Float> getFaceEffectParameter() {
        return this.mFaceEffectParameter;
    }

    public void setmBlackPoint(float f) {
        this.mBlackPoint = f;
    }

    public float getmBlackPoint() {
        return this.mBlackPoint;
    }

    public void setmTint(float f) {
        this.mTint = f;
    }

    public float getmTint() {
        return this.mTint;
    }

    public void setmTemperature(float f) {
        this.mTemperature = f;
    }

    public float getmTemperature() {
        return this.mTemperature;
    }
}
