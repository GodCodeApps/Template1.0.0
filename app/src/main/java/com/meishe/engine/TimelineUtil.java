package com.meishe.engine;

import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.MeicamAudioClip;
import com.meishe.engine.bean.MeicamAudioTrack;
import com.meishe.engine.bean.MeicamCaptionClip;
import com.meishe.engine.bean.MeicamCompoundCaptionClip;
import com.meishe.engine.bean.MeicamFxParam;
import com.meishe.engine.bean.MeicamStickerCaptionTrack;
import com.meishe.engine.bean.MeicamStickerClip;
import com.meishe.engine.bean.MeicamTheme;
import com.meishe.engine.bean.MeicamTimelineVideoFxClip;
import com.meishe.engine.bean.MeicamTimelineVideoFxTrack;
import com.meishe.engine.bean.MeicamTransition;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.MeicamVideoFx;
import com.meishe.engine.bean.MeicamVideoTrack;
import com.meishe.engine.bean.MeicamWaterMark;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.background.MeicamBackgroundStory;
import com.meishe.engine.bean.background.MeicamStoryboardInfo;
import com.meishe.engine.data.MeicamAdjustData;
import com.meishe.engine.data.MeicamClipInfo;
import com.meishe.engine.data.MeicamVideoClipFxInfo;
import com.meishe.engine.util.StoryboardUtil;
import com.meishe.myvideo.audio.AudioRecorder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TimelineUtil {
    public static final String BLURNAME = "Gaussian Blur";
    public static final String MOSAICNAME = "Mosaic";
    private static String TAG = "TimelineUtil";

    public static NvsTimeline createTimeline() {
        NvsTimeline newTimeline = newTimeline(TimelineData.getInstance().getVideoResolution());
        if (newTimeline == null) {
            Logger.e(TAG, "failed to create timeline");
            return null;
        } else if (!buildVideoTrack(newTimeline)) {
            return newTimeline;
        } else {
            setTimelineData(newTimeline);
            return newTimeline;
        }
    }

    public static void setTimelineData(NvsTimeline nvsTimeline) {
        if (nvsTimeline != null) {
            applyTheme(nvsTimeline);
            removeAllTimelineEffect(nvsTimeline);
            addTimeLineEffect(nvsTimeline);
            buildTimelineMusic(nvsTimeline);
            MeicamVideoFx filterFx = TimelineData.getInstance().getFilterFx();
            if (filterFx != null) {
                buildTimelineFilter(nvsTimeline, filterFx);
            }
            setAdjustEffects(nvsTimeline);
            buildClipFilter(nvsTimeline);
            removeAllSticker(nvsTimeline);
            removeAllCaption(nvsTimeline);
            removeAllCompoundCaption(nvsTimeline);
            setSitckerCaptionObject(nvsTimeline);
            buildTimelineRecordAudio(nvsTimeline);
            buildTimelineWaterMark(nvsTimeline);
            buildTimelineMasicEffect(nvsTimeline);
        }
    }

    private static void setAdjustEffects(NvsTimeline nvsTimeline) {
        MeicamAdjustData meicamAdjustData;
        if (nvsTimeline != null && (meicamAdjustData = TimelineData.getInstance().getMeicamAdjustData()) != null) {
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getAmount(), "Amount", "Sharpen");
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getDegree(), "Degree", "Vignette");
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getBlackPoint(), "Blackpoint", "BasicImageAdjust");
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getTint(), "Tint", "Tint");
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getTemperature(), "Temperature", "Tint");
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getShadow(), "Shadow", "BasicImageAdjust");
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getHighlight(), "Highlight", "BasicImageAdjust");
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getSaturation(), "Saturation", "BasicImageAdjust");
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getContrast(), "Contrast", "BasicImageAdjust");
            setTimelineAdjustEffect(nvsTimeline, meicamAdjustData.getBrightness(), "Brightness", "BasicImageAdjust");
        }
    }

    private static void setTimelineAdjustEffect(NvsTimeline nvsTimeline, float f, String str, String str2) {
        NvsTimelineVideoFx firstTimelineVideoFx = nvsTimeline.getFirstTimelineVideoFx();
        while (true) {
            if (firstTimelineVideoFx != null) {
                Object attachment = firstTimelineVideoFx.getAttachment(str2);
                if (attachment != null && str2.equals(attachment)) {
                    break;
                }
                firstTimelineVideoFx = nvsTimeline.getNextTimelineVideoFx(firstTimelineVideoFx);
            } else {
                firstTimelineVideoFx = null;
                break;
            }
        }
        if (firstTimelineVideoFx != null) {
            firstTimelineVideoFx.setFloatVal(str, (double) f);
            return;
        }
        NvsTimelineVideoFx addBuiltinTimelineVideoFx = nvsTimeline.addBuiltinTimelineVideoFx(0, nvsTimeline.getDuration(), str2);
       if (addBuiltinTimelineVideoFx!=null){
           addBuiltinTimelineVideoFx.setAttachment(str2, str2);
           addBuiltinTimelineVideoFx.setFloatVal(str, (double) f);
       }
    }

    private static void buildTimelineMasicEffect(NvsTimeline nvsTimeline) {
        List<MeicamTimelineVideoFxClip> meicamTimelineVideoFxClipList = TimelineData.getInstance().getMeicamTimelineVideoFxClipList();
        if (meicamTimelineVideoFxClipList.size() > 0) {
            int i = 0;
            MeicamTimelineVideoFxClip meicamTimelineVideoFxClip = meicamTimelineVideoFxClipList.get(0);
            NvsTimelineVideoFx bindToTimeline = meicamTimelineVideoFxClip.bindToTimeline(nvsTimeline);
            bindToTimeline.setFilterIntensity(meicamTimelineVideoFxClip.getIntensity());
            bindToTimeline.setRegional(true);
            if (meicamTimelineVideoFxClip.getDesc().equals("Mosaic")) {
                MeicamFxParam meicamFxParam = meicamTimelineVideoFxClip.getMeicamFxParamList().get(0);
                bindToTimeline.setFloatVal(meicamFxParam.getKey(), (double) Float.parseFloat(meicamFxParam.getValue().toString()));
                float[] fArr = new float[8];
                Object value = meicamTimelineVideoFxClip.getMeicamFxParamList().get(1).getValue();
                if (value instanceof ArrayList) {
                    ArrayList arrayList = (ArrayList) value;
                    while (i < arrayList.size()) {
                        fArr[i] = Float.parseFloat(((Double) arrayList.get(i)).toString());
                        i++;
                    }
                    bindToTimeline.setRegion(fArr);
                    return;
                }
                bindToTimeline.setRegion((float[]) value);
                return;
            }
            float[] fArr2 = new float[8];
            Object value2 = meicamTimelineVideoFxClip.getMeicamFxParamList().get(0).getValue();
            if (value2 instanceof ArrayList) {
                ArrayList arrayList2 = (ArrayList) value2;
                while (i < arrayList2.size()) {
                    fArr2[i] = Float.parseFloat(((Double) arrayList2.get(i)).toString());
                    i++;
                }
                bindToTimeline.setRegion(fArr2);
                return;
            }
            bindToTimeline.setRegion((float[]) value2);
        }
    }

    private static void buildTimelineWaterMark(NvsTimeline nvsTimeline) {
        MeicamWaterMark meicamWaterMark = TimelineData.getInstance().getMeicamWaterMark();
        if (meicamWaterMark != null) {
            nvsTimeline.deleteWatermark();
            nvsTimeline.addWatermark(meicamWaterMark.getWatermarkPath(), meicamWaterMark.getWatermarkW(), meicamWaterMark.getWatermarkH(), 1.0f, 1, meicamWaterMark.getWatermarkX(), meicamWaterMark.getWatermarkY());
        }
    }

    private static void addStoryboard(NvsTimeline nvsTimeline) {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (!CollectionUtils.isEmpty(meicamVideoTrackList)) {
            for (int i = 0; i < meicamVideoTrackList.size(); i++) {
                List<ClipInfo> clipInfoList = meicamVideoTrackList.get(i).getClipInfoList();
                if (!CollectionUtils.isEmpty(clipInfoList)) {
                    for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                        MeicamVideoClip meicamVideoClip = (MeicamVideoClip) clipInfoList.get(i2);
                        if (i == 0) {
                            MeicamStoryboardInfo backgroundInfo = meicamVideoClip.getBackgroundInfo();
                            if (backgroundInfo == null) {
                                NvsVideoResolution videoRes = nvsTimeline.getVideoRes();
                                setDefaultBackground(meicamVideoClip, videoRes.imageWidth, videoRes.imageHeight);
                            } else {
                                backgroundInfo.bindToTimelineByType((NvsVideoClip) meicamVideoClip.getObject(), backgroundInfo.getSubType());
                            }
                        }
                        MeicamVideoFx videoFx = meicamVideoClip.getVideoFx("Transform 2D");
                        meicamVideoClip.removeVideoFx("Transform 2D");
                        Map<String, MeicamStoryboardInfo> storyboardInfos = meicamVideoClip.getStoryboardInfos();
                        MeicamStoryboardInfo meicamStoryboardInfo = storyboardInfos.get(MeicamStoryboardInfo.SUB_TYPE_CROPPER_TRANSFROM);
                        if (meicamStoryboardInfo != null) {
                            meicamStoryboardInfo.bindToTimelineByType((NvsVideoClip) meicamVideoClip.getObject(), meicamStoryboardInfo.getSubType());
                        }
                        MeicamStoryboardInfo meicamStoryboardInfo2 = storyboardInfos.get(MeicamStoryboardInfo.SUB_TYPE_CROPPER);
                        if (meicamStoryboardInfo2 != null) {
                            meicamStoryboardInfo2.bindToTimelineByType((NvsVideoClip) meicamVideoClip.getObject(), meicamStoryboardInfo2.getSubType());
                        }
                        if (videoFx != null) {
                            videoFx.bindToTimeline((NvsVideoClip) meicamVideoClip.getObject());
                            meicamVideoClip.getVideoFxs().add(videoFx);
                        }
                    }
                }
            }
        }
    }

    private static void setDefaultBackground(MeicamVideoClip meicamVideoClip, int i, int i2) {
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

    private static void addTimeLineEffect(NvsTimeline nvsTimeline) {
        List<MeicamTimelineVideoFxTrack> meicamTimelineVideoFxTrackList = TimelineData.getInstance().getMeicamTimelineVideoFxTrackList();
        if (!CollectionUtils.isEmpty(meicamTimelineVideoFxTrackList)) {
            List<ClipInfo> clipInfoList = meicamTimelineVideoFxTrackList.get(0).getClipInfoList();
            if (!CollectionUtils.isEmpty(clipInfoList)) {
                Iterator<ClipInfo> it = clipInfoList.iterator();
                while (it.hasNext()) {
                    ((MeicamTimelineVideoFxClip) it.next()).bindToTimeline(nvsTimeline);
                }
            }
        }
    }

    public static boolean buildTimelineMusic(NvsTimeline nvsTimeline) {
        int audioTrackCount = nvsTimeline.audioTrackCount();
        for (int i = 0; i < audioTrackCount; i++) {
            nvsTimeline.getAudioTrackByIndex(i).removeAllClips();
        }
        MeicamTheme meicamTheme = TimelineData.getInstance().getMeicamTheme();
        if (meicamTheme != null && !TextUtils.isEmpty(meicamTheme.getThemePackageId())) {
            nvsTimeline.setThemeMusicVolumeGain(1.0f, 1.0f);
        }
        List<MeicamAudioTrack> meicamAudioTrackList = TimelineData.getInstance().getMeicamAudioTrackList();
        if (CollectionUtils.isEmpty(meicamAudioTrackList)) {
            return false;
        }
        for (MeicamAudioTrack meicamAudioTrack : meicamAudioTrackList) {
            NvsAudioTrack bindToTimeline = meicamAudioTrack.bindToTimeline(nvsTimeline);
            if (bindToTimeline == null) {
                Logger.e(TAG, "buildTimelineMusic: fail to create audio track");
                return false;
            }
            List<ClipInfo> clipInfoList = meicamAudioTrack.getClipInfoList();
            for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                ((MeicamAudioClip) clipInfoList.get(i2)).bindToTimeline(bindToTimeline);
            }
        }
        return true;
    }

    public static void setSitckerCaptionObject(NvsTimeline nvsTimeline) {
        for (MeicamStickerCaptionTrack meicamStickerCaptionTrack : TimelineData.getInstance().getMeicamStickerCaptionTrackList()) {
            List<ClipInfo> clipInfoList = meicamStickerCaptionTrack.getClipInfoList();
            for (ClipInfo clipInfo :clipInfoList) {
                if (clipInfo instanceof MeicamStickerClip) {
                    ((MeicamStickerClip) clipInfo).bindToTimeline(nvsTimeline);
                } else if (clipInfo instanceof MeicamCaptionClip) {
                    ((MeicamCaptionClip) clipInfo).bindToTimeline(nvsTimeline);
                } else if (clipInfo instanceof MeicamCompoundCaptionClip) {
                    ((MeicamCompoundCaptionClip) clipInfo).bindToTimeline(nvsTimeline);
                }
            }
        }
    }

    private static void removeAllSticker(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            Log.e(TAG, "removeAllSticker: timeline is null");
            return;
        }
        NvsTimelineAnimatedSticker firstAnimatedSticker = nvsTimeline.getFirstAnimatedSticker();
        while (firstAnimatedSticker != null) {
            firstAnimatedSticker = nvsTimeline.removeAnimatedSticker(firstAnimatedSticker);
        }
    }

    private static void removeAllTimelineEffect(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            Log.e(TAG, "removeAllSticker: timeline is null");
            return;
        }
        NvsTimelineVideoFx firstTimelineVideoFx = nvsTimeline.getFirstTimelineVideoFx();
        while (firstTimelineVideoFx != null) {
            firstTimelineVideoFx = nvsTimeline.removeTimelineVideoFx(firstTimelineVideoFx);
        }
    }

    private static void removeAllCaption(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            Log.e(TAG, "removeAllSticker: timeline is null");
            return;
        }
        NvsTimelineCaption firstCaption = nvsTimeline.getFirstCaption();
        while (firstCaption != null) {
            int category = firstCaption.getCategory();
            String str = TAG;
            Logger.e(str, "capCategory = " + category);
            int roleInTheme = firstCaption.getRoleInTheme();
            if (category != 2 || roleInTheme == 0) {
                firstCaption = nvsTimeline.removeCaption(firstCaption);
            } else {
                firstCaption = nvsTimeline.getNextCaption(firstCaption);
            }
        }
    }

    private static void removeAllCompoundCaption(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            Log.e(TAG, "removeAllSticker: timeline is null");
            return;
        }
        NvsTimelineCompoundCaption firstCompoundCaption = nvsTimeline.getFirstCompoundCaption();
        while (firstCompoundCaption != null) {
            firstCompoundCaption = nvsTimeline.removeCompoundCaption(firstCompoundCaption);
        }
    }

    public static void buildTimelineRecordAudio(NvsTimeline nvsTimeline) {
        if (nvsTimeline != null) {
            List<MeicamAudioTrack> meicamAudioTrackList = TimelineData.getInstance().getMeicamAudioTrackList();
            for (int i = 0; i < meicamAudioTrackList.size(); i++) {
                MeicamAudioTrack meicamAudioTrack = meicamAudioTrackList.get(i);
                List<ClipInfo> clipInfoList = meicamAudioTrack.getClipInfoList();
                NvsAudioTrack audioTrackByIndex = nvsTimeline.getAudioTrackByIndex(meicamAudioTrack.getIndex());
                if (audioTrackByIndex == null) {
                    audioTrackByIndex = nvsTimeline.appendAudioTrack();
                }
                for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                    ((MeicamAudioClip) clipInfoList.get(i2)).bindToTimeline(audioTrackByIndex);
                }
            }
        }
    }

    private static boolean buildVideoTrack(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            return false;
        }
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (!CollectionUtils.isEmpty(meicamVideoTrackList)) {
            return fillTrack(nvsTimeline, meicamVideoTrackList);
        }
        Logger.e(TAG, "no track data!!!");
        return false;
    }

    public static void addVideoClip(NvsVideoTrack nvsVideoTrack, MeicamClipInfo meicamClipInfo, boolean z) {
        NvsVideoFx appendBuiltinFx;
        NvsVideoFx appendBuiltinFx2;
        if (nvsVideoTrack != null && meicamClipInfo != null) {
            String filePath = meicamClipInfo.getFilePath();
            NvsVideoClip addClip = meicamClipInfo.getInPoint() > 0 ? nvsVideoTrack.addClip(filePath, meicamClipInfo.getInPoint()) : nvsVideoTrack.appendClip(filePath);
            if (addClip == null) {
                Logger.e(TAG, "failed to append clip");
                return;
            }
            if (meicamClipInfo.getTrackIndex() > 0) {
                NvsVideoFx appendBuiltinFx3 = addClip.appendBuiltinFx("Transform 2D");
                if (appendBuiltinFx3 == null) {
                    Logger.e(TAG, "failed to append transform 2d");
                    return;
                }
                appendBuiltinFx3.setFloatVal("Rotation", (double) meicamClipInfo.getTransformRotation());
                appendBuiltinFx3.setFloatVal("Scale X", (double) meicamClipInfo.getTransformScale());
                appendBuiltinFx3.setFloatVal("Scale Y", (double) meicamClipInfo.getTransformScale());
                appendBuiltinFx3.setFloatVal("Trans X", (double) meicamClipInfo.getTransformX());
                appendBuiltinFx3.setFloatVal("Trans Y", (double) meicamClipInfo.getTransformY());
            }
            float brightnessVal = meicamClipInfo.getBrightnessVal();
            float contrastVal = meicamClipInfo.getContrastVal();
            float saturationVal = meicamClipInfo.getSaturationVal();
            float vignetteVal = meicamClipInfo.getVignetteVal();
            float sharpenVal = meicamClipInfo.getSharpenVal();
            if ((brightnessVal >= 0.0f || contrastVal >= 0.0f || saturationVal >= 0.0f) && (appendBuiltinFx2 = addClip.appendBuiltinFx("Color Property")) != null) {
                if (brightnessVal >= 0.0f) {
                    appendBuiltinFx2.setFloatVal("Brightness", (double) brightnessVal);
                }
                if (contrastVal >= 0.0f) {
                    appendBuiltinFx2.setFloatVal("Contrast", (double) contrastVal);
                }
                if (saturationVal >= 0.0f) {
                    appendBuiltinFx2.setFloatVal("Saturation", (double) saturationVal);
                }
            }
            if (vignetteVal >= 0.0f) {
                addClip.appendBuiltinFx("Vignette").setFloatVal("Degree", (double) vignetteVal);
            }
            if (sharpenVal >= 0.0f) {
                addClip.appendBuiltinFx("Sharpen").setFloatVal("Amount", (double) sharpenVal);
            }
            if (addClip.getVideoType() == 1) {
                long trimIn = addClip.getTrimIn();
                long trimOut = meicamClipInfo.getTrimOut();
                if (trimOut > 0 && trimOut > trimIn) {
                    addClip.changeTrimOutPoint(trimOut, true);
                }
                if (meicamClipInfo.getImgDispalyMode() == 2001) {
                    addClip.setImageMotionMode(2);
                    RectF normalStartROI = meicamClipInfo.getNormalStartROI();
                    RectF normalEndROI = meicamClipInfo.getNormalEndROI();
                    if (!(normalStartROI == null || normalEndROI == null)) {
                        addClip.setImageMotionROI(normalStartROI, normalEndROI);
                    }
                } else {
                    addClip.setImageMotionMode(0);
                }
                addClip.setImageMotionAnimationEnabled(meicamClipInfo.ismIsOpenPhotoMove());
            } else {
                float volume = meicamClipInfo.getVolume();
                addClip.setVolumeGain(volume, volume);
                addClip.setPanAndScan(meicamClipInfo.getPan(), meicamClipInfo.getScan());
                double speed = meicamClipInfo.getSpeed();
                if (speed > 0.0d) {
                    addClip.changeSpeed(speed);
                }
                addClip.setExtraVideoRotation(meicamClipInfo.getRotateAngle());
                float scaleX = meicamClipInfo.getScaleX();
                float scaleY = meicamClipInfo.getScaleY();
                if ((scaleX >= -1.0f || scaleY >= -1.0f) && (appendBuiltinFx = addClip.appendBuiltinFx("Transform 2D")) != null) {
                    if (scaleX >= -1.0f) {
                        appendBuiltinFx.setFloatVal("Scale X", (double) scaleX);
                    }
                    if (scaleY >= -1.0f) {
                        appendBuiltinFx.setFloatVal("Scale Y", (double) scaleY);
                    }
                }
            }
            if (z) {
                long trimIn2 = meicamClipInfo.getTrimIn();
                long trimOut2 = meicamClipInfo.getTrimOut();
                if (trimIn2 > 0) {
                    addClip.changeTrimInPoint(trimIn2, true);
                }
                if (trimOut2 > 0 && trimOut2 > trimIn2) {
                    addClip.changeTrimOutPoint(trimOut2, true);
                }
            }
        }
    }

    public static void insertVideoClip(NvsVideoTrack nvsVideoTrack, MeicamVideoClip meicamVideoClip, int i, boolean z) {
        if (nvsVideoTrack != null && meicamVideoClip != null && nvsVideoTrack.insertClip(meicamVideoClip.getFilePath(), i) == null) {
            Logger.e(TAG, "failed to append clip");
        }
    }

    private static void setVideoClipExtraValue(NvsVideoClip nvsVideoClip, MeicamClipInfo meicamClipInfo, boolean z) {
        NvsVideoFx appendBuiltinFx;
        NvsVideoFx appendBuiltinFx2;
        NvsVideoFx appendBuiltinFx3 = nvsVideoClip.appendBuiltinFx("Transform 2D");
        if (appendBuiltinFx3 == null) {
            Logger.e(TAG, "failed to append transform 2d");
            return;
        }
        appendBuiltinFx3.setFloatVal("Rotation", (double) meicamClipInfo.getTransformRotation());
        appendBuiltinFx3.setFloatVal("Scale X", (double) meicamClipInfo.getTransformScale());
        appendBuiltinFx3.setFloatVal("Scale Y", (double) meicamClipInfo.getTransformScale());
        appendBuiltinFx3.setFloatVal("Trans X", (double) meicamClipInfo.getTransformX());
        appendBuiltinFx3.setFloatVal("Trans Y", (double) meicamClipInfo.getTransformY());
        float brightnessVal = meicamClipInfo.getBrightnessVal();
        float contrastVal = meicamClipInfo.getContrastVal();
        float saturationVal = meicamClipInfo.getSaturationVal();
        float vignetteVal = meicamClipInfo.getVignetteVal();
        float sharpenVal = meicamClipInfo.getSharpenVal();
        if ((brightnessVal >= 0.0f || contrastVal >= 0.0f || saturationVal >= 0.0f) && (appendBuiltinFx2 = nvsVideoClip.appendBuiltinFx("Color Property")) != null) {
            if (brightnessVal >= 0.0f) {
                appendBuiltinFx2.setFloatVal("Brightness", (double) brightnessVal);
            }
            if (contrastVal >= 0.0f) {
                appendBuiltinFx2.setFloatVal("Contrast", (double) contrastVal);
            }
            if (saturationVal >= 0.0f) {
                appendBuiltinFx2.setFloatVal("Saturation", (double) saturationVal);
            }
        }
        if (vignetteVal >= 0.0f) {
            nvsVideoClip.appendBuiltinFx("Vignette").setFloatVal("Degree", (double) vignetteVal);
        }
        if (sharpenVal >= 0.0f) {
            nvsVideoClip.appendBuiltinFx("Sharpen").setFloatVal("Amount", (double) sharpenVal);
        }
        if (nvsVideoClip.getVideoType() == 1) {
            long trimIn = nvsVideoClip.getTrimIn();
            long trimOut = meicamClipInfo.getTrimOut();
            if (trimOut > 0 && trimOut > trimIn) {
                nvsVideoClip.changeTrimOutPoint(trimOut, true);
            }
            if (meicamClipInfo.getImgDispalyMode() == 2001) {
                nvsVideoClip.setImageMotionMode(2);
                RectF normalStartROI = meicamClipInfo.getNormalStartROI();
                RectF normalEndROI = meicamClipInfo.getNormalEndROI();
                if (!(normalStartROI == null || normalEndROI == null)) {
                    nvsVideoClip.setImageMotionROI(normalStartROI, normalEndROI);
                }
            } else {
                nvsVideoClip.setImageMotionMode(0);
            }
            nvsVideoClip.setImageMotionAnimationEnabled(meicamClipInfo.ismIsOpenPhotoMove());
        } else {
            float volume = meicamClipInfo.getVolume();
            nvsVideoClip.setVolumeGain(volume, volume);
            nvsVideoClip.setPanAndScan(meicamClipInfo.getPan(), meicamClipInfo.getScan());
            double speed = meicamClipInfo.getSpeed();
            if (speed > 0.0d) {
                nvsVideoClip.changeSpeed(speed);
            }
            nvsVideoClip.setExtraVideoRotation(meicamClipInfo.getRotateAngle());
            float scaleX = meicamClipInfo.getScaleX();
            float scaleY = meicamClipInfo.getScaleY();
            if ((scaleX >= -1.0f || scaleY >= -1.0f) && (appendBuiltinFx = nvsVideoClip.appendBuiltinFx("Transform 2D")) != null) {
                if (scaleX >= -1.0f) {
                    appendBuiltinFx.setFloatVal("Scale X", (double) scaleX);
                }
                if (scaleY >= -1.0f) {
                    appendBuiltinFx.setFloatVal("Scale Y", (double) scaleY);
                }
            }
        }
        if (z) {
            long trimIn2 = meicamClipInfo.getTrimIn();
            long trimOut2 = meicamClipInfo.getTrimOut();
            if (trimIn2 > 0) {
                nvsVideoClip.changeTrimInPoint(trimIn2, true);
            }
            if (trimOut2 > 0 && trimOut2 > trimIn2) {
                nvsVideoClip.changeTrimOutPoint(trimOut2, true);
            }
        }
    }

    public static NvsTimeline newTimeline(NvsVideoResolution nvsVideoResolution) {
        NvsStreamingContext instance = NvsStreamingContext.getInstance();
        if (instance == null) {
            Logger.e(TAG, "failed to get streamingContext");
            return null;
        }
        nvsVideoResolution.imagePAR = new NvsRational(1, 1);
        NvsRational nvsRational = new NvsRational(25, 1);
        NvsAudioResolution nvsAudioResolution = new NvsAudioResolution();
        nvsAudioResolution.sampleRate = AudioRecorder.RecordConfig.SAMPLE_RATE_44K_HZ;
        nvsAudioResolution.channelCount = 2;
        return instance.createTimeline(nvsVideoResolution, nvsRational, nvsAudioResolution);
    }

    public static void alignedResolution(NvsVideoResolution nvsVideoResolution) {
        nvsVideoResolution.imageWidth = alignedData(nvsVideoResolution.imageWidth, 4);
        nvsVideoResolution.imageHeight = alignedData(nvsVideoResolution.imageHeight, 2);
    }

    private static int alignedData(int i, int i2) {
        return i - (i % i2);
    }

    public static void rebuildTimeline(NvsTimeline nvsTimeline) {
        if (nvsTimeline != null) {
            NvsVideoResolution videoResolution = TimelineData.getInstance().getVideoResolution();
            if (videoResolution != null) {
                nvsTimeline.changeVideoSize(videoResolution.imageWidth, videoResolution.imageHeight);
            }
            List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
            if (CollectionUtils.isEmpty(meicamVideoTrackList)) {
                Logger.e(TAG, "no track data!!!");
                return;
            }
            for (int videoTrackCount = nvsTimeline.videoTrackCount() - 1; videoTrackCount >= 0; videoTrackCount--) {
                nvsTimeline.removeVideoTrack(videoTrackCount);
            }
            if (fillTrack(nvsTimeline, meicamVideoTrackList)) {
                setTimelineData(nvsTimeline);
            }
        }
    }

    private static boolean fillTrack(NvsTimeline nvsTimeline, List<MeicamVideoTrack> list) {
        for (MeicamVideoTrack meicamVideoTrack : list) {
            NvsVideoTrack bindToTimeline = meicamVideoTrack.bindToTimeline(nvsTimeline);
            if (bindToTimeline == null) {
                Logger.e(TAG, "failed to append video track");
                return false;
            }
            List<ClipInfo> clipInfoList = meicamVideoTrack.getClipInfoList();
            if (CollectionUtils.isEmpty(clipInfoList)) {
                Logger.e(TAG, "no clip data!!!");
            } else {
                for (int i = 0; i < clipInfoList.size(); i++) {
                    ((MeicamVideoClip) clipInfoList.get(i)).addToTimeline(bindToTimeline);
                }
                setTransition(nvsTimeline, meicamVideoTrack.getTransitionInfoList());
            }
        }
        return true;
    }

    public static boolean setTransition(NvsTimeline nvsTimeline, List<MeicamTransition> list) {
        NvsVideoTrack videoTrackByIndex;
        if (nvsTimeline == null || (videoTrackByIndex = nvsTimeline.getVideoTrackByIndex(0)) == null || list == null || videoTrackByIndex.getClipCount() <= 1) {
            return false;
        }
        for (MeicamTransition meicamTransition : list) {
            if (meicamTransition != null) {
                meicamTransition.bindToTimeline(videoTrackByIndex);
            }
        }
        return true;
    }

    public static boolean clearAllBuildInTransition(NvsTimeline nvsTimeline) {
        NvsVideoTrack videoTrackByIndex;
        int clipCount;
        if (nvsTimeline == null || (videoTrackByIndex = nvsTimeline.getVideoTrackByIndex(0)) == null || (clipCount = videoTrackByIndex.getClipCount()) <= 1) {
            return false;
        }
        for (int i = 0; i < clipCount - 1; i++) {
            videoTrackByIndex.setBuiltinTransition(i, "");
        }
        return true;
    }

    public static boolean applyTheme(NvsTimeline nvsTimeline) {
        MeicamTheme meicamTheme;
        if (nvsTimeline == null || (meicamTheme = TimelineData.getInstance().getMeicamTheme()) == null) {
            return false;
        }
        String themePackageId = meicamTheme.getThemePackageId();
        nvsTimeline.removeCurrentTheme();
        if (TextUtils.isEmpty(themePackageId)) {
            return false;
        }
        if (!nvsTimeline.applyTheme(themePackageId)) {
            Log.e(TAG, "failed to apply theme");
            return false;
        } else if (TimelineData.getInstance().getMeicamAudioTrackList().size() == 0) {
            nvsTimeline.setThemeMusicVolumeGain(1.0f, 1.0f);
            return true;
        } else {
            nvsTimeline.setThemeMusicVolumeGain(0.0f, 0.0f);
            return true;
        }
    }

    public static boolean buildTimelineFilter(NvsTimeline nvsTimeline, MeicamVideoFx meicamVideoFx) {
        MeicamVideoTrack meicamVideoTrack;
        if (nvsTimeline == null || (meicamVideoTrack = TimelineData.getInstance().getMeicamVideoTrackList().get(0)) == null) {
            return false;
        }
        List<ClipInfo> clipInfoList = meicamVideoTrack.getClipInfoList();
        for (int i = 0; i < clipInfoList.size(); i++) {
            appendFilterFx((MeicamVideoClip) clipInfoList.get(i), meicamVideoFx.clone());
        }
        return true;
    }

    public static boolean buildClipFilter(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            return false;
        }
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (CollectionUtils.isEmpty(meicamVideoTrackList)) {
            return false;
        }
        for (MeicamVideoTrack meicamVideoTrack : meicamVideoTrackList) {
            Iterator<ClipInfo> it = meicamVideoTrack.getClipInfoList().iterator();
            while (it.hasNext()) {
                MeicamVideoClip meicamVideoClip = (MeicamVideoClip) it.next();
                appendFilterFx(meicamVideoClip, meicamVideoClip.getVideoFx(MeicamVideoFx.SUB_TYPE_CLIP_FILTER));
            }
        }
        return true;
    }

    public static boolean changeTimelineFilterIntensity(NvsTimeline nvsTimeline, float f) {
        MeicamVideoTrack meicamVideoTrack;
        if (nvsTimeline == null || nvsTimeline.getVideoTrackByIndex(0) == null || (meicamVideoTrack = TimelineData.getInstance().getMeicamVideoTrackList().get(0)) == null) {
            return false;
        }
        List<ClipInfo> clipInfoList = meicamVideoTrack.getClipInfoList();
        MeicamVideoFx filterFx = TimelineData.getInstance().getFilterFx();
        for (int i = 0; i < clipInfoList.size(); i++) {
            ((MeicamVideoClip) clipInfoList.get(i)).setFilterIntensity(f, filterFx.getSubType());
        }
        return true;
    }

    public static boolean buildTimelineEffect(NvsTimeline nvsTimeline, MeicamVideoClipFxInfo meicamVideoClipFxInfo) {
        if (nvsTimeline == null || meicamVideoClipFxInfo == null) {
            return false;
        }
        meicamVideoClipFxInfo.bindToTimeline(nvsTimeline);
        return true;
    }

    public static void appendFilterFx(MeicamVideoClip meicamVideoClip, MeicamVideoFx meicamVideoFx) {
        if (meicamVideoFx != null) {
            meicamVideoClip.removeVideoFx(meicamVideoFx.getSubType());
            NvsVideoFx bindToTimeline = meicamVideoFx.bindToTimeline((NvsVideoClip) meicamVideoClip.getObject());
            if (bindToTimeline != null) {
                bindToTimeline.setFilterMask(true);
            }
            meicamVideoClip.getVideoFxs().add(meicamVideoFx);
        }
    }

    private static boolean removeAllVideoFx(NvsVideoClip nvsVideoClip) {
        int i = 0;
        if (nvsVideoClip == null) {
            return false;
        }
        int fxCount = nvsVideoClip.getFxCount();
        while (i < fxCount) {
            NvsVideoFx fxByIndex = nvsVideoClip.getFxByIndex(i);
            if (fxByIndex != null) {
                String builtinVideoFxName = fxByIndex.getBuiltinVideoFxName();
                Log.e("===>", "fx name: " + builtinVideoFxName);
                if (!builtinVideoFxName.equals("Color Property") && !builtinVideoFxName.equals("Vignette") && !builtinVideoFxName.equals("Sharpen") && !builtinVideoFxName.equals("Transform 2D")) {
                    nvsVideoClip.removeFx(i);
                    i--;
                }
            }
            i++;
        }
        return true;
    }
}
