package com.meishe.myvideo.util.ui;

import android.text.TextUtils;
import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamAudioClip;
import com.meishe.engine.bean.MeicamAudioTrack;
import com.meishe.engine.bean.MeicamCaptionClip;
import com.meishe.engine.bean.MeicamCompoundCaptionClip;
import com.meishe.engine.bean.MeicamStickerCaptionTrack;
import com.meishe.engine.bean.MeicamStickerClip;
import com.meishe.engine.bean.MeicamTimelineVideoFxClip;
import com.meishe.engine.bean.MeicamTimelineVideoFxTrack;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.MeicamVideoTrack;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.ui.bean.BaseUIClip;
import com.meishe.myvideo.ui.bean.BaseUIVideoClip;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackViewDataHelper {
    private static TrackViewDataHelper sTrackViewDataHelper;

    private TrackViewDataHelper() {
    }

    public static TrackViewDataHelper getInstance() {
        if (sTrackViewDataHelper == null) {
            synchronized (TrackViewDataHelper.class) {
                if (sTrackViewDataHelper == null) {
                    sTrackViewDataHelper = new TrackViewDataHelper();
                }
            }
        }
        return sTrackViewDataHelper;
    }

    public HashMap<Integer, List<BaseUIClip>> getTrackData(String str) {
        HashMap<Integer, List<BaseUIClip>> hashMap = new HashMap<>();
        if (CommonData.CLIP_STICKER.equals(str) || CommonData.CLIP_CAPTION.equals(str) || CommonData.CLIP_COMPOUND_CAPTION.equals(str)) {
            return getCaptionAndStickerTrackData();
        }
        if (CommonData.CLIP_TIMELINE_FX.equals(str)) {
            return getFilterTrackData();
        }
        if (CommonData.CLIP_AUDIO.equals(str)) {
            return getAudioTrackData();
        }
        if (CommonData.CLIP_IMAGE.equals(str) || CommonData.CLIP_VIDEO.equals(str)) {
            return getPipTrackData();
        }
        return hashMap;
    }

    private HashMap<Integer, List<BaseUIClip>> getAudioTrackData() {
        List<MeicamAudioTrack> meicamAudioTrackList = TimelineData.getInstance().getMeicamAudioTrackList();
        HashMap<Integer, List<BaseUIClip>> hashMap = new HashMap<>();
        if (meicamAudioTrackList != null && meicamAudioTrackList.size() > 0) {
            for (int i = 0; i < meicamAudioTrackList.size(); i++) {
                List<ClipInfo> clipInfoList = meicamAudioTrackList.get(i).getClipInfoList();
                for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                    setTrackViewData(hashMap, (MeicamAudioClip) clipInfoList.get(i2));
                }
            }
        }
        return hashMap;
    }

    public HashMap<Integer, List<BaseUIClip>> addAudioBaseUIClip(HashMap<Integer, List<BaseUIClip>> hashMap, MeicamAudioClip meicamAudioClip, NvsTimeline nvsTimeline) {
        if (meicamAudioClip == null || nvsTimeline == null) {
            return hashMap;
        }
        int audioTrackCount = nvsTimeline.audioTrackCount();
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= audioTrackCount) {
                break;
            }
            NvsAudioTrack audioTrackByIndex = nvsTimeline.getAudioTrackByIndex(i);
            NvsAudioClip clipByIndex = audioTrackByIndex.getClipByIndex(audioTrackByIndex.getClipCount() - 1);
            if (clipByIndex == null || meicamAudioClip.getInPoint() >= clipByIndex.getOutPoint()) {
                meicamAudioClip.setIndex(i);
                z = true;
            } else {
                i++;
            }
        }
        if (!z) {
            meicamAudioClip.setIndex(audioTrackCount);
        }
        return setTrackViewData(hashMap, meicamAudioClip);
    }

    public HashMap<Integer, List<BaseUIClip>> removeAudioBaseUIClip(HashMap<Integer, List<BaseUIClip>> hashMap, MeicamAudioClip meicamAudioClip) {
        for (int i = 0; i < hashMap.size(); i++) {
            List<BaseUIClip> list = hashMap.get(Integer.valueOf(i));
            for (int i2 = 0; i2 < list.size(); i2++) {
                BaseUIClip baseUIClip = list.get(i2);
                if (baseUIClip.getTrackIndex() == meicamAudioClip.getIndex() && baseUIClip.getInPoint() == meicamAudioClip.getInPoint()) {
                    list.remove(baseUIClip);
                    return hashMap;
                }
            }
        }
        return hashMap;
    }

    public HashMap<Integer, List<BaseUIClip>> setTrackViewData(HashMap<Integer, List<BaseUIClip>> hashMap, MeicamAudioClip meicamAudioClip) {
        List<BaseUIClip> list;
        int index = meicamAudioClip.getIndex();
        if (hashMap.get(Integer.valueOf(index)) == null) {
            list = new ArrayList<>();
        } else {
            list = hashMap.get(Integer.valueOf(index));
        }
        BaseUIVideoClip baseUIVideoClip = new BaseUIVideoClip();
        baseUIVideoClip.setAudioFadeIn(meicamAudioClip.getFadeInDuration());
        baseUIVideoClip.setAudioFadeOut(meicamAudioClip.getFadeOutDuration());
        baseUIVideoClip.setTrimOut(meicamAudioClip.getTrimOut());
        baseUIVideoClip.setTrimIn(meicamAudioClip.getTrimIn());
        baseUIVideoClip.setInPoint(meicamAudioClip.getInPoint());
        baseUIVideoClip.setTrackIndex(index);
        baseUIVideoClip.setType(CommonData.CLIP_AUDIO);
        baseUIVideoClip.setText(meicamAudioClip.getDrawText());
        baseUIVideoClip.setSpeed(meicamAudioClip.getSpeed());
        baseUIVideoClip.setAudioType(meicamAudioClip.getAudioType());
        baseUIVideoClip.setFilePath(meicamAudioClip.getFilePath());
        baseUIVideoClip.setNvsObject(meicamAudioClip.getObject());
        list.add(baseUIVideoClip);
        hashMap.put(Integer.valueOf(index), list);
        return hashMap;
    }

    public int getDrawText(HashMap<Integer, List<BaseUIClip>> hashMap) {
        int parseInt;
        int i = 0;
        for (int i2 = 0; i2 < hashMap.size(); i2++) {
            List<BaseUIClip> list = hashMap.get(Integer.valueOf(i2));
            if (list != null) {
                int i3 = i;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    BaseUIClip baseUIClip = list.get(i4);
                    if (baseUIClip.getAudioType() == 1) {
                        String text = baseUIClip.getText();
                        if (!TextUtils.isEmpty(text)) {
                            String substring = text.substring(2);
                            if (!TextUtils.isEmpty(substring) && (parseInt = Integer.parseInt(substring)) > i3) {
                                i3 = parseInt;
                            }
                        }
                    }
                }
                i = i3;
            }
        }
        return i + 1;
    }

    public BaseUIClip getBaseUIClip(HashMap<Integer, List<BaseUIClip>> hashMap, int i, long j) {
        List<BaseUIClip> list;
        BaseUIClip baseUIClip = null;
        if (hashMap == null || (list = hashMap.get(Integer.valueOf(i))) == null) {
            return null;
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            baseUIClip = list.get(i2);
            if (j == baseUIClip.getInPoint()) {
                break;
            }
        }
        return baseUIClip;
    }

    public HashMap<Integer, List<BaseUIClip>> getPipTrackData() {
        List<ClipInfo> clipInfoList;
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        HashMap<Integer, List<BaseUIClip>> hashMap = new HashMap<>();
        if (meicamVideoTrackList != null && meicamVideoTrackList.size() >= 1) {
            for (int i = 1; i < meicamVideoTrackList.size(); i++) {
                MeicamVideoTrack meicamVideoTrack = meicamVideoTrackList.get(i);
                if (!(meicamVideoTrack.getIndex() == 0 || (clipInfoList = meicamVideoTrack.getClipInfoList()) == null || clipInfoList.isEmpty())) {
                    int index = meicamVideoTrack.getIndex() - 1;
                    for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                        MeicamVideoClip meicamVideoClip = (MeicamVideoClip) clipInfoList.get(i2);
                        NvsVideoClip nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject();
                        BaseUIVideoClip baseUIVideoClip = new BaseUIVideoClip();
                        baseUIVideoClip.setSpeed(meicamVideoClip.getSpeed());
                        baseUIVideoClip.setInPoint(meicamVideoClip.getInPoint());
                        baseUIVideoClip.setTrimOut(meicamVideoClip.getTrimOut());
                        baseUIVideoClip.setTrimIn(meicamVideoClip.getTrimIn());
                        if (nvsVideoClip == null) {
                            Logger.e("TAG", "ERROR: showPipTrackView -> nvsVideoClip is null!");
                        } else {
                            if (nvsVideoClip.getVideoType() == 0) {
                                baseUIVideoClip.setType(CommonData.CLIP_VIDEO);
                            } else {
                                baseUIVideoClip.setType(CommonData.CLIP_IMAGE);
                            }
                            baseUIVideoClip.setTrackIndex(index);
                            baseUIVideoClip.setFilePath(meicamVideoClip.getVideoReverse() ? meicamVideoClip.getReverseFilePath() : meicamVideoClip.getFilePath());
                            baseUIVideoClip.setNvsObject(nvsVideoClip);
                            if (hashMap.containsKey(Integer.valueOf(index))) {
                                hashMap.get(Integer.valueOf(index)).add(baseUIVideoClip);
                            } else {
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(baseUIVideoClip);
                                hashMap.put(Integer.valueOf(index), arrayList);
                            }
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    public HashMap<Integer, List<BaseUIClip>> getFilterTrackData() {
        List<MeicamTimelineVideoFxTrack> meicamTimelineVideoFxTrackList = TimelineData.getInstance().getMeicamTimelineVideoFxTrackList();
        if (CollectionUtils.isEmpty(meicamTimelineVideoFxTrackList)) {
            return null;
        }
        List<ClipInfo> clipInfoList = meicamTimelineVideoFxTrackList.get(0).getClipInfoList();
        if (CollectionUtils.isEmpty(clipInfoList)) {
            return null;
        }
        HashMap<Integer, List<BaseUIClip>> hashMap = new HashMap<>();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < clipInfoList.size(); i++) {
            MeicamTimelineVideoFxClip meicamTimelineVideoFxClip = (MeicamTimelineVideoFxClip) clipInfoList.get(i);
            BaseUIVideoClip baseUIVideoClip = new BaseUIVideoClip();
            baseUIVideoClip.setInPoint(meicamTimelineVideoFxClip.getInPoint());
            baseUIVideoClip.setTrimIn(CommonData.DEFAULT_TRIM_IN);
            baseUIVideoClip.setTrimOut((meicamTimelineVideoFxClip.getOutPoint() + CommonData.DEFAULT_TRIM_IN) - meicamTimelineVideoFxClip.getInPoint());
            baseUIVideoClip.setTrackIndex(0);
            baseUIVideoClip.setClipIndexInTrack(1);
            baseUIVideoClip.setType(CommonData.CLIP_TIMELINE_FX);
            baseUIVideoClip.setNvsObject(meicamTimelineVideoFxClip.getObject());
            baseUIVideoClip.setText(meicamTimelineVideoFxClip.getDisplayName());
            arrayList.add(baseUIVideoClip);
        }
        hashMap.put(0, arrayList);
        return hashMap;
    }

    public HashMap<Integer, List<BaseUIClip>> getCaptionAndStickerTrackData() {
        List<MeicamStickerCaptionTrack> meicamStickerCaptionTrackList = TimelineData.getInstance().getMeicamStickerCaptionTrackList();
        HashMap<Integer, List<BaseUIClip>> hashMap = new HashMap<>();
        for (MeicamStickerCaptionTrack meicamStickerCaptionTrack : meicamStickerCaptionTrackList) {
            List<ClipInfo> clipInfoList = meicamStickerCaptionTrack.getClipInfoList();
            int index = meicamStickerCaptionTrack.getIndex();
            for (ClipInfo clipInfo : clipInfoList) {
                BaseUIVideoClip baseUIVideoClip = new BaseUIVideoClip();
                baseUIVideoClip.setInPoint(clipInfo.getInPoint());
                baseUIVideoClip.setTrimIn(CommonData.DEFAULT_TRIM_IN);
                baseUIVideoClip.setTrimOut((clipInfo.getOutPoint() + CommonData.DEFAULT_TRIM_IN) - clipInfo.getInPoint());
                baseUIVideoClip.setTrackIndex(index);
                baseUIVideoClip.setNvsObject(clipInfo.getObject());
                if (clipInfo instanceof MeicamStickerClip) {
                    baseUIVideoClip.setType(CommonData.CLIP_STICKER);
                    baseUIVideoClip.setIconFilePath(((MeicamStickerClip) clipInfo).getCoverImagePath());
                } else if (clipInfo instanceof MeicamCaptionClip) {
                    baseUIVideoClip.setType(CommonData.CLIP_CAPTION);
                    baseUIVideoClip.setText(((MeicamCaptionClip) clipInfo).getText());
                } else if (clipInfo instanceof MeicamCompoundCaptionClip) {
                    baseUIVideoClip.setType(CommonData.CLIP_COMPOUND_CAPTION);
                    baseUIVideoClip.setText(((MeicamCompoundCaptionClip) clipInfo).getCompoundCaptionItems().get(0).getText());
                }
                if (hashMap.containsKey(Integer.valueOf(index))) {
                    hashMap.get(Integer.valueOf(index)).add(baseUIVideoClip);
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(baseUIVideoClip);
                    hashMap.put(Integer.valueOf(index), arrayList);
                }
            }
        }
        return hashMap;
    }

    public BaseUIClip getBaseUIClip(int i, ClipInfo clipInfo) {
        if (clipInfo == null) {
            return null;
        }
        BaseUIVideoClip baseUIVideoClip = new BaseUIVideoClip();
        baseUIVideoClip.setInPoint(clipInfo.getInPoint());
        baseUIVideoClip.setTrimIn(CommonData.DEFAULT_TRIM_IN);
        baseUIVideoClip.setTrimOut((clipInfo.getOutPoint() + CommonData.DEFAULT_TRIM_IN) - clipInfo.getInPoint());
        baseUIVideoClip.setTrackIndex(i);
        baseUIVideoClip.setNvsObject(clipInfo.getObject());
        if (clipInfo instanceof MeicamStickerClip) {
            baseUIVideoClip.setType(CommonData.CLIP_STICKER);
        } else if (clipInfo instanceof MeicamCaptionClip) {
            baseUIVideoClip.setType(CommonData.CLIP_CAPTION);
            baseUIVideoClip.setText(((MeicamCaptionClip) clipInfo).getText());
        } else if (clipInfo instanceof MeicamCompoundCaptionClip) {
            baseUIVideoClip.setType(CommonData.CLIP_COMPOUND_CAPTION);
            baseUIVideoClip.setText(((MeicamCompoundCaptionClip) clipInfo).getCompoundCaptionItems().get(0).getText());
        }
        return baseUIVideoClip;
    }
}
