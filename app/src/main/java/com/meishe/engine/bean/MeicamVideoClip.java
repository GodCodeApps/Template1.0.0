package com.meishe.engine.bean;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.background.MeicamBackgroundStory;
import com.meishe.engine.bean.background.MeicamStoryboardInfo;
import com.meishe.engine.data.MeicamAdjustData;
import com.meishe.engine.util.DeepCopyUtil;
import com.meishe.engine.util.StoryboardUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MeicamVideoClip extends ClipInfo<NvsVideoClip> implements Cloneable, Serializable {
    public static final String ATTACHMENT_KEY_FX_TYPE = "videoFxType";
    private static final String[] sFxName = {CommonData.VIDEO_FX_BEAUTY_STRENGTH, CommonData.VIDEO_FX_BEAUTY_WHITENING, CommonData.VIDEO_FX_BEAUTY_REDDENING, "Face Size Warp Degree", "Eye Size Warp Degree", "Chin Length Warp Degree", "Forehead Height Warp Degree", "Nose Width Warp Degree", "Mouth Size Warp Degree"};
    private int extraRotation;
    private String filePath;
    private String id;
    private boolean isConvertSuccess = false;
    private boolean isVideoReverse = false;
    @SerializedName("faceEffectParameter")
    private Map<String, Float> mFaceEffectParameter = new HashMap();
    @SerializedName("imageDisplayMode")
    private int mImageDisplayMode = 0;
    @SerializedName("adjustData")
    private MeicamAdjustData mMeicamAdjustData = new MeicamAdjustData();
    @SerializedName("roleInTheme")
    private int mRoleInTheme;
    @SerializedName("scan")
    private float mScan = 0.0f;
    @SerializedName("span")
    private float mSpan = 0.0f;
    @SerializedName("storyboardInfo")
    private Map<String, MeicamStoryboardInfo> mStoryboardInfos = new HashMap();
    private float opacity = 1.0f;
    private long orgDuration;
    private boolean reverse;
    private String reverseFilePath;
    private double speed = 1.0d;
    private long trimIn;
    private long trimOut;
    private List<MeicamVideoFx> videoFxs = new ArrayList();
    private String videoType;
    private float volume = 1.0f;

    public MeicamVideoClip() {
        super(CommonData.CLIP_VIDEO);
    }

    public MeicamVideoClip(String str, String str2, long j) {
        super(CommonData.CLIP_VIDEO);
        this.filePath = str;
        this.videoType = str2;
        this.orgDuration = j;
    }

    @Deprecated
    public void loadData(NvsVideoClip nvsVideoClip) {
        if (nvsVideoClip != null) {
            setObject(nvsVideoClip);
            setInPoint(nvsVideoClip.getInPoint());
            setOutPoint(nvsVideoClip.getOutPoint());
            this.isVideoReverse = nvsVideoClip.getPlayInReverse();
            if (this.isVideoReverse) {
                this.reverseFilePath = nvsVideoClip.getFilePath();
            } else {
                this.filePath = nvsVideoClip.getFilePath();
            }
            this.trimIn = nvsVideoClip.getTrimIn();
            this.trimOut = nvsVideoClip.getTrimOut();
            this.speed = nvsVideoClip.getSpeed();
            this.extraRotation = nvsVideoClip.getExtraVideoRotation();
            this.mRoleInTheme = nvsVideoClip.getRoleInTheme();
            this.opacity = nvsVideoClip.getOpacity();
        }
    }

    public Map<String, Float> getFaceEffectParameter() {
        return this.mFaceEffectParameter;
    }

    public void setFaceEffectParameter(Map<String, Float> map) {
        this.mFaceEffectParameter = map;
    }

    public void setFaceBeautyEffectParameter(Map<String, Float> map) {
        if (map != null) {
            this.mFaceEffectParameter.put(CommonData.VIDEO_FX_BEAUTY_STRENGTH, map.get(CommonData.VIDEO_FX_BEAUTY_STRENGTH));
            this.mFaceEffectParameter.put(CommonData.VIDEO_FX_BEAUTY_WHITENING, map.get(CommonData.VIDEO_FX_BEAUTY_WHITENING));
            this.mFaceEffectParameter.put(CommonData.VIDEO_FX_BEAUTY_REDDENING, map.get(CommonData.VIDEO_FX_BEAUTY_REDDENING));
        }
    }

    public void setFaceBeautyShapeEffectParameter(Map<String, Float> map) {
        if (map != null) {
            for (String str : map.keySet()) {
                if (!isBeautyEffectKey(str)) {
                    this.mFaceEffectParameter.put(str, map.get(str));
                }
            }
        }
    }

    public MeicamStoryboardInfo getBackgroundInfo() {
        if (this.mStoryboardInfos.isEmpty()) {
            return null;
        }
        return this.mStoryboardInfos.get(MeicamStoryboardInfo.SUB_TYPE_BACKGROUND);
    }

    public Map<String, MeicamStoryboardInfo> getStoryboardInfos() {
        return this.mStoryboardInfos;
    }

    public void addStoryboardInfo(String str, MeicamStoryboardInfo meicamStoryboardInfo) {
        this.mStoryboardInfos.put(str, meicamStoryboardInfo);
    }

    public MeicamAdjustData getMeicamAdjustData() {
        return this.mMeicamAdjustData;
    }

    public void setMeicamAdjustData(MeicamAdjustData meicamAdjustData) {
        this.mMeicamAdjustData = meicamAdjustData;
    }

    public int getRoleInTheme() {
        return this.mRoleInTheme;
    }

    public void setRoleInTheme(int i) {
        this.mRoleInTheme = i;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public String getReverseFilePath() {
        return this.reverseFilePath;
    }

    public void setReverseFilePath(String str) {
        this.reverseFilePath = str;
    }

    public String getVideoType() {
        return this.videoType;
    }

    public void setVideoType(String str) {
        this.videoType = str;
    }

    public long getTrimIn() {
        return this.trimIn;
    }

    public void setTrimIn(long j) {
        this.trimIn = j;
    }

    public long getTrimOut() {
        return this.trimOut;
    }

    public void setTrimOut(long j) {
        this.trimOut = j;
    }

    public long getOrgDuration() {
        return this.orgDuration;
    }

    public void setOrgDuration(long j) {
        this.orgDuration = j;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float f) {
        if (getObject() != null) {
            ((NvsVideoClip) getObject()).setVolumeGain(f, f);
        }
        this.volume = f;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double d) {
        this.speed = d;
        if (getObject() != null) {
            ((NvsVideoClip) getObject()).changeSpeed(d);
        }
    }

    public int getExtraRotation() {
        return this.extraRotation;
    }

    public void setExtraRotation(int i) {
        this.extraRotation = i;
    }

    public boolean isReverse() {
        return this.reverse;
    }

    public void setReverse(boolean z) {
        this.reverse = z;
    }

    public List<MeicamVideoFx> getVideoFxs() {
        return this.videoFxs;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float f) {
        this.opacity = f;
    }

    public boolean getVideoReverse() {
        return this.isVideoReverse;
    }

    public void setVideoReverse(boolean z) {
        this.isVideoReverse = z;
    }

    public boolean isConvertSuccess() {
        return this.isConvertSuccess;
    }

    public void setConvertSuccess(boolean z) {
        this.isConvertSuccess = z;
    }

    public int getImageDisplayMode() {
        return this.mImageDisplayMode;
    }

    public void setmImageDisplayMode(int i) {
        this.mImageDisplayMode = i;
    }

    public void setScan(float f) {
        this.mScan = f;
    }

    public void setSpan(float f) {
        this.mSpan = f;
    }

    public float getScan() {
        return this.mScan;
    }

    public float getSpan() {
        return this.mSpan;
    }

    public void removeBackground() {
        MeicamStoryboardInfo meicamStoryboardInfo;
        NvsVideoFx nvsVideoFx;
        NvsVideoClip nvsVideoClip;
        if (!this.mStoryboardInfos.isEmpty() && (meicamStoryboardInfo = this.mStoryboardInfos.get(MeicamStoryboardInfo.SUB_TYPE_BACKGROUND)) != null && (nvsVideoFx = (NvsVideoFx) meicamStoryboardInfo.getObject()) != null && (nvsVideoClip = (NvsVideoClip) getObject()) != null) {
            nvsVideoClip.removeFx(nvsVideoFx.getIndex());
            this.mStoryboardInfos.remove(MeicamStoryboardInfo.SUB_TYPE_BACKGROUND);
        }
    }

    public NvsVideoClip appendToTimeline(NvsVideoTrack nvsVideoTrack) {
        String str;
        if (nvsVideoTrack == null) {
            return null;
        }
        if (this.isVideoReverse) {
            str = this.reverseFilePath;
        } else {
            str = this.filePath;
        }
        NvsVideoClip appendClip = nvsVideoTrack.appendClip(str);
        if (appendClip == null) {
            Logger.e("ContentValues", "failed to append clip");
            return null;
        }
        setObject(appendClip);
        setData(appendClip, nvsVideoTrack.getIndex());
        return appendClip;
    }

    public NvsVideoClip addToTimeline(NvsVideoTrack nvsVideoTrack) {
        String str;
        if (nvsVideoTrack == null) {
            return null;
        }
        if (this.isVideoReverse) {
            str = this.reverseFilePath;
        } else {
            str = this.filePath;
        }
        NvsVideoClip addClip = nvsVideoTrack.addClip(str, getInPoint(), getTrimIn(), getTrimOut());
        if (addClip == null) {
            Logger.e("ContentValues", "failed to append clip");
            return null;
        }
        setObject(addClip);
        setData(addClip, nvsVideoTrack.getIndex());
        return addClip;
    }

    public NvsVideoClip insertToTimeline(NvsVideoTrack nvsVideoTrack, int i) {
        String str;
        if (nvsVideoTrack == null) {
            return null;
        }
        if (this.isVideoReverse) {
            str = this.reverseFilePath;
        } else {
            str = this.filePath;
        }
        NvsVideoClip insertClip = nvsVideoTrack.insertClip(str, i);
        if (insertClip == null) {
            Logger.e("ContentValues", "failed to append clip");
            return null;
        }
        setObject(insertClip);
        setData(insertClip, nvsVideoTrack.getIndex());
        return insertClip;
    }

    public void setData(NvsVideoClip nvsVideoClip, int i) {
        applyFaceEffect(i);
        setAdjustEffects(nvsVideoClip);
        for (MeicamVideoFx meicamVideoFx : this.videoFxs) {
            removeNvsVideoFx(meicamVideoFx.getSubType());
        }
        for (int i2 = 0; i2 < this.videoFxs.size(); i2++) {
            this.videoFxs.get(i2).bindToTimeline(nvsVideoClip);
        }
        if (nvsVideoClip.getVideoType() == 1) {
            long trimIn2 = nvsVideoClip.getTrimIn();
            long trimOut2 = getTrimOut();
            if (trimOut2 > 0 && trimOut2 > trimIn2) {
                nvsVideoClip.changeTrimOutPoint(trimOut2, true);
            }
            nvsVideoClip.setImageMotionMode(getImageDisplayMode());
            nvsVideoClip.setImageMotionAnimationEnabled(false);
        } else {
            float f = this.volume;
            nvsVideoClip.setVolumeGain(f, f);
            double d = this.speed;
            if (d > 0.0d) {
                nvsVideoClip.changeSpeed(d);
            }
            nvsVideoClip.setPanAndScan(getSpan(), getScan());
        }
        nvsVideoClip.setExtraVideoRotation(this.extraRotation);
        nvsVideoClip.setOpacity(getOpacity());
        long j = this.trimIn;
        if (j > 0) {
            nvsVideoClip.changeTrimInPoint(j, true);
        }
        long j2 = this.trimOut;
        if (j2 > 0 && j2 > this.trimIn) {
            nvsVideoClip.changeTrimOutPoint(j2, true);
        }
        addStoryboards(i);
    }

    private void removeNvsVideoFx(String str) {
        MeicamVideoFx videoFx = getVideoFx(str);
        if (videoFx != null) {
            try {
                NvsVideoFx nvsVideoFx = (NvsVideoFx) videoFx.getObject();
                if (nvsVideoFx != null) {
                    ((NvsVideoClip) getObject()).removeFx(nvsVideoFx.getIndex());
                }
            } catch (Exception e) {
                Logger.e("ContentValues", "removeVideoFx:error:" + e.fillInStackTrace());
            }
        }
    }

    private void addStoryboards(int i) {
        MeicamVideoFx videoFx = getVideoFx("Transform 2D");
        removeVideoFx("Transform 2D");
        Map<String, MeicamStoryboardInfo> storyboardInfos = getStoryboardInfos();
        MeicamStoryboardInfo meicamStoryboardInfo = storyboardInfos.get(MeicamStoryboardInfo.SUB_TYPE_CROPPER_TRANSFROM);
        if (meicamStoryboardInfo != null) {
            meicamStoryboardInfo.bindToTimelineByType((NvsVideoClip) getObject(), meicamStoryboardInfo.getSubType());
        }
        MeicamStoryboardInfo meicamStoryboardInfo2 = storyboardInfos.get(MeicamStoryboardInfo.SUB_TYPE_CROPPER);
        if (meicamStoryboardInfo2 != null) {
            meicamStoryboardInfo2.bindToTimelineByType((NvsVideoClip) getObject(), meicamStoryboardInfo2.getSubType());
        }
        if (videoFx != null) {
            videoFx.bindToTimeline((NvsVideoClip) getObject());
            getVideoFxs().add(videoFx);
        }
        if (i == 0) {
            MeicamStoryboardInfo backgroundInfo = getBackgroundInfo();
            if (backgroundInfo == null) {
                NvsVideoResolution videoResolution = TimelineData.getInstance().getVideoResolution();
                setDefaultBackground(this, videoResolution.imageWidth, videoResolution.imageHeight);
                return;
            }
            backgroundInfo.bindToTimelineByType((NvsVideoClip) getObject(), backgroundInfo.getSubType());
        }
    }

    private void setAdjustEffects(NvsVideoClip nvsVideoClip) {
        MeicamAdjustData meicamAdjustData = getMeicamAdjustData();
        if (meicamAdjustData != null) {
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getAmount(), "Amount", "Sharpen");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getDegree(), "Degree", "Vignette");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getBlackPoint(), "Blackpoint", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getTint(), "Tint", "Tint");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getTemperature(), "Temperature", "Tint");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getShadow(), "Shadow", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getHighlight(), "Highlight", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getSaturation(), "Saturation", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getContrast(), "Contrast", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getBrightness(), "Brightness", "BasicImageAdjust");
        }
    }

    public void setAdjustEffects() {
        MeicamAdjustData meicamAdjustData;
        NvsVideoClip nvsVideoClip = (NvsVideoClip) getObject();
        if (nvsVideoClip != null && (meicamAdjustData = getMeicamAdjustData()) != null) {
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getAmount(), "Amount", "Sharpen");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getDegree(), "Degree", "Vignette");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getBlackPoint(), "Blackpoint", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getTint(), "Tint", "Tint");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getTemperature(), "Temperature", "Tint");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getShadow(), "Shadow", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getHighlight(), "Highlight", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getSaturation(), "Saturation", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getContrast(), "Contrast", "BasicImageAdjust");
            setAdjustEffect(nvsVideoClip, meicamAdjustData.getBrightness(), "Brightness", "BasicImageAdjust");
        }
    }

    @Override // java.lang.Object
    @NonNull
    public Object clone() {
        return DeepCopyUtil.deepClone(this);
    }

    private void setDefaultBackground(MeicamVideoClip meicamVideoClip, int i, int i2) {
        MeicamBackgroundStory meicamBackgroundStory = new MeicamBackgroundStory();
        HashMap hashMap = new HashMap();
        Float valueOf = Float.valueOf(1.0f);
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_SCALE_X, valueOf);
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_SCALE_Y, valueOf);
        Float valueOf2 = Float.valueOf(0.0f);
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z, valueOf2);
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_TRANS_X, valueOf2);
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_TRANS_Y, valueOf2);
        meicamBackgroundStory.setClipTrans(hashMap);
        meicamBackgroundStory.setSource("nobackground.png");
        meicamBackgroundStory.setSourceDir("assets:/background");
        String imageBackgroundStory = StoryboardUtil.getImageBackgroundStory(meicamBackgroundStory.getSource(), i, i2, hashMap);
        meicamBackgroundStory.setStringVal("Resource Dir", meicamBackgroundStory.getSourceDir());
        meicamBackgroundStory.setBooleanVal("No Background", true);
        meicamBackgroundStory.setStringVal("Description String", imageBackgroundStory);
        meicamBackgroundStory.setBackgroundType(0);
        meicamBackgroundStory.bindToTimelineByType((NvsVideoClip) meicamVideoClip.getObject(), meicamBackgroundStory.getSubType());
        meicamVideoClip.addStoryboardInfo(meicamBackgroundStory.getSubType(), meicamBackgroundStory);
    }

    public void setFilterIntensity(float f, String str) {
        if (!TextUtils.isEmpty(str)) {
            MeicamVideoFx meicamVideoFx = null;
            Iterator<MeicamVideoFx> it = this.videoFxs.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MeicamVideoFx next = it.next();
                if (str.equals(next.getSubType())) {
                    meicamVideoFx = next;
                    break;
                }
            }
            if (meicamVideoFx != null) {
                meicamVideoFx.setIntensity(f);
            }
        }
    }

    public float getFilterIntensity(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0.0f;
        }
        MeicamVideoFx meicamVideoFx = null;
        Iterator<MeicamVideoFx> it = this.videoFxs.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MeicamVideoFx next = it.next();
            if (str.equals(next.getSubType())) {
                meicamVideoFx = next;
                break;
            }
        }
        if (meicamVideoFx == null) {
            return 0.0f;
        }
        return meicamVideoFx.intensity;
    }

    public MeicamVideoFx getVideoFx(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (MeicamVideoFx meicamVideoFx : this.videoFxs) {
            if (str.equals(meicamVideoFx.getSubType())) {
                return meicamVideoFx;
            }
        }
        return null;
    }

    public void removeVideoFx(String str) {
        MeicamVideoFx videoFx = getVideoFx(str);
        if (videoFx != null) {
            try {
                ((NvsVideoClip) getObject()).removeFx(((NvsVideoFx) videoFx.getObject()).getIndex());
                this.videoFxs.remove(videoFx);
            } catch (Exception e) {
                Logger.e("ContentValues", "removeVideoFx:error:" + e.fillInStackTrace());
            }
        }
    }

    private void setAdjustEffect(NvsVideoClip nvsVideoClip, float f, String str, String str2) {
        NvsVideoFx nvsVideoFx;
        Object attachment;
        int fxCount = nvsVideoClip.getFxCount();
        int i = 0;
        while (true) {
            if (i < fxCount) {
                nvsVideoFx = nvsVideoClip.getFxByIndex(i);
                if (nvsVideoFx != null && (attachment = nvsVideoFx.getAttachment(str2)) != null && str2.equals(attachment)) {
                    break;
                }
                i++;
            } else {
                nvsVideoFx = null;
                break;
            }
        }
        if (nvsVideoFx != null) {
            nvsVideoFx.setFloatVal(str, (double) f);
        } else {
            nvsVideoFx = nvsVideoClip.appendBuiltinFx(str2);
            nvsVideoFx.setAttachment(str2, str2);
            nvsVideoFx.setFloatVal(str, (double) f);
        }
        setRegionData(nvsVideoFx);
    }

    public void applyFaceEffect(int i) {
        NvsVideoClip nvsVideoClip = (NvsVideoClip) getObject();
        if (nvsVideoClip != null) {
            int fxCount = nvsVideoClip.getFxCount();
            NvsVideoFx nvsVideoFx = null;
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= fxCount) {
                    break;
                }
                NvsVideoFx fxByIndex = nvsVideoClip.getFxByIndex(i2);
                if (fxByIndex.getAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_AR_SCENE) != null) {
                    z = true;
                    nvsVideoFx = fxByIndex;
                    break;
                }
                i2++;
            }
            if (!z) {
                nvsVideoFx = nvsVideoClip.appendBuiltinFx(CommonData.VIDEO_FX_AR_SCENE);
                if (nvsVideoFx == null) {
                    Logger.e("ContentValues", "appendBuiltinFx arSceneFx is null");
                    return;
                }
                nvsVideoFx.setAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_AR_SCENE, CommonData.VIDEO_FX_AR_SCENE);
            }
            Set<String> keySet = this.mFaceEffectParameter.keySet();
            openBeautyFx(nvsVideoFx);
            if (i > 0) {
                setRegionData(nvsVideoFx);
            }
            for (String str : keySet) {
                nvsVideoFx.setFloatVal(str, (double) this.mFaceEffectParameter.get(str).floatValue());
            }
        }
    }

    private void setRegionData(NvsVideoFx nvsVideoFx) {
        float f;
        if (nvsVideoFx != null) {
            NvsAVFileInfo aVFileInfo = NvsStreamingContext.getInstance().getAVFileInfo(getFilePath());
            NvsSize videoStreamDimension = aVFileInfo.getVideoStreamDimension(0);
            int videoStreamRotation = aVFileInfo.getVideoStreamRotation(0);
            int i = videoStreamDimension.width;
            int i2 = videoStreamDimension.height;
            if (videoStreamRotation == 1 || videoStreamRotation == 3) {
                i = videoStreamDimension.height;
                i2 = videoStreamDimension.width;
            }
            NvsVideoResolution videoResolution = TimelineData.getInstance().getVideoResolution();
            int i3 = videoResolution.imageHeight;
            float f2 = (float) videoResolution.imageWidth;
            float f3 = 1.0f;
            float f4 = f2 * 1.0f;
            float f5 = (float) i3;
            float f6 = (float) i;
            float f7 = (float) i2;
            if ((f6 * 1.0f) / f7 > f4 / f5) {
                f3 = ((f7 * (f4 / f6)) / f5) * 0.99f;
                f = 1.0f;
            } else {
                f = ((f6 * ((f5 * 1.0f) / f7)) / f2) * 0.99f;
            }
            float f8 = -f;
            float f9 = -f3;
            nvsVideoFx.setRegion(new float[]{f8, f3, f, f3, f, f9, f8, f9});
            nvsVideoFx.setRegional(true);
        }
    }

    public void applyBeautyFaceEffect() {
        NvsVideoClip nvsVideoClip = (NvsVideoClip) getObject();
        if (nvsVideoClip != null) {
            int fxCount = nvsVideoClip.getFxCount();
            NvsVideoFx nvsVideoFx = null;
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= fxCount) {
                    break;
                }
                NvsVideoFx fxByIndex = nvsVideoClip.getFxByIndex(i);
                if (fxByIndex.getAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_AR_SCENE) != null) {
                    z = true;
                    nvsVideoFx = fxByIndex;
                    break;
                }
                i++;
            }
            if (!z) {
                nvsVideoFx = nvsVideoClip.appendBuiltinFx(CommonData.VIDEO_FX_AR_SCENE);
                if (nvsVideoFx == null) {
                    Logger.e("ContentValues", "appendBuiltinFx arSceneFx is null");
                    return;
                }
                nvsVideoFx.setAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_AR_SCENE, CommonData.VIDEO_FX_AR_SCENE);
            }
            openBeautyFx(nvsVideoFx);
            nvsVideoFx.setFloatVal(CommonData.VIDEO_FX_BEAUTY_STRENGTH, (double) this.mFaceEffectParameter.get(CommonData.VIDEO_FX_BEAUTY_STRENGTH).floatValue());
            nvsVideoFx.setFloatVal(CommonData.VIDEO_FX_BEAUTY_WHITENING, (double) this.mFaceEffectParameter.get(CommonData.VIDEO_FX_BEAUTY_WHITENING).floatValue());
            nvsVideoFx.setFloatVal(CommonData.VIDEO_FX_BEAUTY_REDDENING, (double) this.mFaceEffectParameter.get(CommonData.VIDEO_FX_BEAUTY_REDDENING).floatValue());
        }
    }

    public void applyBeautyShapeFaceEffect() {
        NvsVideoClip nvsVideoClip = (NvsVideoClip) getObject();
        if (nvsVideoClip != null) {
            int fxCount = nvsVideoClip.getFxCount();
            NvsVideoFx nvsVideoFx = null;
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= fxCount) {
                    break;
                }
                NvsVideoFx fxByIndex = nvsVideoClip.getFxByIndex(i);
                if (fxByIndex.getAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_AR_SCENE) != null) {
                    z = true;
                    nvsVideoFx = fxByIndex;
                    break;
                }
                i++;
            }
            if (!z) {
                nvsVideoFx = nvsVideoClip.appendBuiltinFx(CommonData.VIDEO_FX_AR_SCENE);
                if (nvsVideoFx == null) {
                    Logger.e("ContentValues", "appendBuiltinFx arSceneFx is null");
                    return;
                }
                nvsVideoFx.setAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_AR_SCENE, CommonData.VIDEO_FX_AR_SCENE);
            }
            Set<String> keySet = this.mFaceEffectParameter.keySet();
            openBeautyFx(nvsVideoFx);
            for (String str : keySet) {
                if (!isBeautyEffectKey(str)) {
                    nvsVideoFx.setFloatVal(str, (double) this.mFaceEffectParameter.get(str).floatValue());
                }
            }
        }
    }

    public boolean isBeautyEffectKey(String str) {
        return CommonData.VIDEO_FX_BEAUTY_STRENGTH.equals(str) || CommonData.VIDEO_FX_BEAUTY_WHITENING.equals(str) || CommonData.VIDEO_FX_BEAUTY_REDDENING.equals(str);
    }

    public void resetBeautyShape(NvsVideoFx nvsVideoFx) {
        Set<String> keySet = this.mFaceEffectParameter.keySet();
        openBeautyFx(nvsVideoFx);
        for (String str : keySet) {
            if (!isBeautyEffectKey(str)) {
                nvsVideoFx.setFloatVal(str, 0.0d);
            }
        }
    }

    public void resetBeauty(NvsVideoFx nvsVideoFx) {
        if (nvsVideoFx != null) {
            openBeautyFx(nvsVideoFx);
            nvsVideoFx.setFloatVal(CommonData.VIDEO_FX_BEAUTY_STRENGTH, 0.5d);
            nvsVideoFx.setFloatVal(CommonData.VIDEO_FX_BEAUTY_WHITENING, 0.0d);
            nvsVideoFx.setFloatVal(CommonData.VIDEO_FX_BEAUTY_REDDENING, 0.0d);
            this.mFaceEffectParameter.put(CommonData.VIDEO_FX_BEAUTY_STRENGTH, Float.valueOf(0.5f));
            this.mFaceEffectParameter.put(CommonData.VIDEO_FX_BEAUTY_WHITENING, Float.valueOf(0.0f));
            this.mFaceEffectParameter.put(CommonData.VIDEO_FX_BEAUTY_REDDENING, Float.valueOf(0.0f));
        }
    }

    public void openBeautyFx(NvsVideoFx nvsVideoFx) {
        nvsVideoFx.setBooleanVal(CommonData.VIDEO_FX_BEAUTY_EFFECT, true);
        nvsVideoFx.setBooleanVal(CommonData.VIDEO_FX_BEAUTY_SHAPE, true);
        nvsVideoFx.setBooleanVal(CommonData.VIDEO_FX_SINGLE_BUFFER_MODE, false);
        nvsVideoFx.getARSceneManipulate().setDetectionMode(16);
    }
}
