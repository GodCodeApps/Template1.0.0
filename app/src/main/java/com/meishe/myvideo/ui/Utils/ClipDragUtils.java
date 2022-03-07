package com.meishe.myvideo.ui.Utils;

import android.util.Log;
import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamAudioClip;
import com.meishe.engine.bean.MeicamAudioTrack;
import com.meishe.engine.bean.MeicamCaptionClip;
import com.meishe.engine.bean.MeicamCompoundCaptionClip;
import com.meishe.engine.bean.MeicamStickerClip;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.TimelineDataUtil;
import com.meishe.myvideo.ui.bean.BaseUIClip;
import java.util.List;

public class ClipDragUtils {
    private static final String TAG = "ClipDragUtils";

    public static Object onDragEnd(String str, BaseUIClip baseUIClip, long j, int i, NvsTimeline nvsTimeline) {
        List<MeicamAudioTrack> list;
        MeicamAudioClip meicamAudioClip;
        List<MeicamAudioTrack> list2;
        if (baseUIClip == null) {
            Log.e(TAG, "onDragEnd: oldUiClip is null!");
            return null;
        }
        long inPoint = baseUIClip.getInPoint();
        long trimOut = (baseUIClip.getTrimOut() + j) - baseUIClip.getTrimIn();
        if (CommonData.CLIP_CAPTION.equals(str)) {
            NvsTimelineCaption nvsTimelineCaption = (NvsTimelineCaption) baseUIClip.getNvsObject();
            if (inPoint < j) {
                nvsTimelineCaption.changeOutPoint(trimOut);
                nvsTimelineCaption.changeInPoint(j);
            } else {
                nvsTimelineCaption.changeInPoint(j);
                nvsTimelineCaption.changeOutPoint(trimOut);
            }
            float f = (float) i;
            nvsTimelineCaption.setZValue(f);
            MeicamCaptionClip meicamCaptionClip = (MeicamCaptionClip) TimelineDataUtil.getStickerCaptionData(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
            if (meicamCaptionClip != null) {
                MeicamCaptionClip meicamCaptionClip2 = (MeicamCaptionClip) meicamCaptionClip.clone();
                TimelineDataUtil.removeStickerCaption(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
                meicamCaptionClip2.setInPoint(j);
                meicamCaptionClip2.setOutPoint(trimOut);
                meicamCaptionClip2.setzValue(f);
                meicamCaptionClip2.setObject(nvsTimelineCaption);
                TimelineDataUtil.addStickerCaption((int) meicamCaptionClip2.getzValue(), meicamCaptionClip2);
            }
            return nvsTimelineCaption;
        } else if (CommonData.CLIP_COMPOUND_CAPTION.equals(str)) {
            NvsTimelineCompoundCaption nvsTimelineCompoundCaption = (NvsTimelineCompoundCaption) baseUIClip.getNvsObject();
            if (inPoint < j) {
                nvsTimelineCompoundCaption.changeOutPoint(trimOut);
                nvsTimelineCompoundCaption.changeInPoint(j);
            } else {
                nvsTimelineCompoundCaption.changeInPoint(j);
                nvsTimelineCompoundCaption.changeOutPoint(trimOut);
            }
            float f2 = (float) i;
            nvsTimelineCompoundCaption.setZValue(f2);
            MeicamCompoundCaptionClip meicamCompoundCaptionClip = (MeicamCompoundCaptionClip) TimelineDataUtil.getStickerCaptionData(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
            if (meicamCompoundCaptionClip != null) {
                MeicamCompoundCaptionClip meicamCompoundCaptionClip2 = (MeicamCompoundCaptionClip) meicamCompoundCaptionClip.clone();
                TimelineDataUtil.removeStickerCaption(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
                meicamCompoundCaptionClip2.setInPoint(j);
                meicamCompoundCaptionClip2.setOutPoint(trimOut);
                meicamCompoundCaptionClip2.setzValue(f2);
                meicamCompoundCaptionClip2.setObject(nvsTimelineCompoundCaption);
                TimelineDataUtil.addStickerCaption((int) meicamCompoundCaptionClip2.getzValue(), meicamCompoundCaptionClip2);
            }
            return nvsTimelineCompoundCaption;
        } else if (CommonData.CLIP_STICKER.equals(str)) {
            NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker = (NvsTimelineAnimatedSticker) baseUIClip.getNvsObject();
            Log.e(TAG, "onDragEnd: " + nvsTimelineAnimatedSticker.getInPoint() + " " + nvsTimelineAnimatedSticker.getOutPoint() + " " + j + " " + trimOut);
            if (inPoint < j) {
                nvsTimelineAnimatedSticker.changeOutPoint(trimOut);
                nvsTimelineAnimatedSticker.changeInPoint(j);
            } else {
                nvsTimelineAnimatedSticker.changeInPoint(j);
                nvsTimelineAnimatedSticker.changeOutPoint(trimOut);
            }
            float f3 = (float) i;
            nvsTimelineAnimatedSticker.setZValue(f3);
            MeicamStickerClip meicamStickerClip = (MeicamStickerClip) TimelineDataUtil.getStickerCaptionData(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
            if (meicamStickerClip != null) {
                MeicamStickerClip meicamStickerClip2 = (MeicamStickerClip) meicamStickerClip.clone();
                TimelineDataUtil.removeStickerCaption(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
                meicamStickerClip2.setInPoint(j);
                meicamStickerClip2.setOutPoint(trimOut);
                meicamStickerClip2.setzValue(f3);
                meicamStickerClip2.setObject(nvsTimelineAnimatedSticker);
                TimelineDataUtil.addStickerCaption((int) meicamStickerClip2.getzValue(), meicamStickerClip2);
            }
            return nvsTimelineAnimatedSticker;
        } else if (CommonData.CLIP_TIMELINE_FX.equals(str)) {
            NvsTimelineVideoFx nvsTimelineVideoFx = (NvsTimelineVideoFx) baseUIClip.getNvsObject();
            if (inPoint < j) {
                nvsTimelineVideoFx.changeOutPoint(trimOut);
                nvsTimelineVideoFx.changeInPoint(j);
            } else {
                nvsTimelineVideoFx.changeInPoint(j);
                nvsTimelineVideoFx.changeOutPoint(trimOut);
            }
            ClipInfo timelineVideoFxData = TimelineDataUtil.getTimelineVideoFxData(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
            if (timelineVideoFxData != null) {
                timelineVideoFxData.setInPoint(j);
                timelineVideoFxData.setOutPoint(trimOut);
            }
            return nvsTimelineVideoFx;
        } else if (CommonData.CLIP_VIDEO.equals(str) || CommonData.CLIP_IMAGE.equals(str)) {
            NvsVideoClip nvsVideoClip = (NvsVideoClip) baseUIClip.getNvsObject();
            int trackIndex = baseUIClip.getTrackIndex();
            int i2 = trackIndex + 1;
            NvsVideoTrack videoTrackByIndex = nvsTimeline.getVideoTrackByIndex(i2);
            if (videoTrackByIndex == null) {
                Log.e(TAG, "onDragEnd: video -> videoTrack is null! trackindex = " + (trackIndex - 1) + "  trackCount= " + nvsTimeline.videoTrackCount());
                return null;
            }
            new MeicamVideoClip();
            MeicamVideoClip meicamVideoClip = (MeicamVideoClip) ((MeicamVideoClip) TimelineDataUtil.getVideoClip(i2, inPoint)).clone();
            TimelineDataUtil.removeVideoClip(i2, inPoint);
            videoTrackByIndex.removeClip(nvsVideoClip.getIndex(), true);
            int i3 = i + 1;
            NvsVideoTrack videoTrackByIndex2 = nvsTimeline.getVideoTrackByIndex(i3);
            if (videoTrackByIndex2 == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("onDragEnd: video -> newVideoTrack is null! trackindex = ");
                sb.append(i - 1);
                sb.append("  trackCount= ");
                sb.append(nvsTimeline.videoTrackCount());
                Log.e(TAG, sb.toString());
                return null;
            }
            meicamVideoClip.setInPoint(j);
            NvsVideoClip addToTimeline = meicamVideoClip.addToTimeline(videoTrackByIndex2);
            meicamVideoClip.setOutPoint(addToTimeline.getOutPoint());
            TimelineDataUtil.addVideoClip(i3, meicamVideoClip);
            return addToTimeline;
        } else if (!CommonData.CLIP_AUDIO.equals(str)) {
            return null;
        } else {
            NvsAudioClip nvsAudioClip = (NvsAudioClip) baseUIClip.getNvsObject();
            int trackIndex2 = baseUIClip.getTrackIndex();
            NvsAudioTrack audioTrackByIndex = nvsTimeline.getAudioTrackByIndex(trackIndex2);
            if (audioTrackByIndex == null) {
                Log.e(TAG, "onDragEnd: video -> videoTrack is null! trackindex = " + trackIndex2 + "  trackCount= " + nvsTimeline.videoTrackCount());
            } else {
                List<MeicamAudioTrack> meicamAudioTrackList = TimelineData.getInstance().getMeicamAudioTrackList();
                if (meicamAudioTrackList.size() > 0) {
                    MeicamAudioClip meicamAudioClip2 = null;
                    int i4 = 0;
                    while (i4 < meicamAudioTrackList.size()) {
                        List<ClipInfo> clipInfoList = meicamAudioTrackList.get(i4).getClipInfoList();
                        int i5 = 0;
                        while (true) {
                            if (i5 >= clipInfoList.size()) {
                                list2 = meicamAudioTrackList;
                                break;
                            }
                            MeicamAudioClip meicamAudioClip3 = (MeicamAudioClip) clipInfoList.get(i5);
                            list2 = meicamAudioTrackList;
                            if (meicamAudioClip3.getIndex() == trackIndex2 && meicamAudioClip3.getInPoint() == inPoint) {
                                clipInfoList.remove(meicamAudioClip3);
                                meicamAudioClip2 = meicamAudioClip3;
                                break;
                            }
                            i5++;
                            meicamAudioTrackList = list2;
                        }
                        i4++;
                        meicamAudioTrackList = list2;
                    }
                    list = meicamAudioTrackList;
                    meicamAudioClip = meicamAudioClip2;
                } else {
                    list = meicamAudioTrackList;
                    meicamAudioClip = null;
                }
                audioTrackByIndex.removeClip(nvsAudioClip.getIndex(), true);
                NvsAudioTrack audioTrackByIndex2 = nvsTimeline.getAudioTrackByIndex(i);
                if (audioTrackByIndex2 == null) {
                    Log.e(TAG, "onDragEnd: video -> newAudioClip is null! trackindex = " + i + "  trackCount= " + nvsTimeline.videoTrackCount());
                } else if (meicamAudioClip != null) {
                    meicamAudioClip.setInPoint(j);
                    meicamAudioClip.setIndex(i);
                    NvsAudioClip bindToTimeline = meicamAudioClip.bindToTimeline(audioTrackByIndex2);
                    meicamAudioClip.setObject(bindToTimeline);
                    if (list.size() <= i) {
                        return bindToTimeline;
                    }
                    list.get(i).getClipInfoList().add(meicamAudioClip);
                    return bindToTimeline;
                }
            }
            return null;
        }
    }
}
