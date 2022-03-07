package com.meishe.engine.bean;

import android.text.TextUtils;
import android.util.Log;
import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meicam.sdk.NvsVideoTransition;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.CommonData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TimelineDataUtil {
    private static final String TAG = "TimelineDataUtil";

    public static ClipInfo getTimelineVideoFxData(int i, long j) {
        List<MeicamTimelineVideoFxTrack> meicamTimelineVideoFxTrackList = TimelineData.getInstance().getMeicamTimelineVideoFxTrackList();
        int size = meicamTimelineVideoFxTrackList.size();
        if (size < i) {
            Log.e(TAG, "getStickerCaptionData: trackIndex is bigger than trackSize。 trackIndex = " + i + "  trackSize = " + size);
            return null;
        }
        List<ClipInfo> clipInfoList = meicamTimelineVideoFxTrackList.get(i).getClipInfoList();
        for (ClipInfo clipInfo :clipInfoList ) {
            if (clipInfo.getInPoint() == j) {
                return clipInfo;
            }
        }
        return null;
    }

    public static ClipInfo getStickerCaptionData(int i, long j) {
        List<MeicamStickerCaptionTrack> meicamStickerCaptionTrackList = TimelineData.getInstance().getMeicamStickerCaptionTrackList();
        int size = meicamStickerCaptionTrackList.size();
        if (size < i) {
            Log.e(TAG, "getStickerCaptionData: trackIndex is bigger than trackSize。 trackIndex = " + i + "  trackSize = " + size);
            return null;
        }
        List<ClipInfo> clipInfoList = meicamStickerCaptionTrackList.get(i).getClipInfoList();
        for (ClipInfo clipInfo : clipInfoList) {
            if (clipInfo.getInPoint() == j) {
                return clipInfo;
            }
        }
        return null;
    }

    public static boolean removeStickerCaption(int i, long j) {
        List<MeicamStickerCaptionTrack> meicamStickerCaptionTrackList = TimelineData.getInstance().getMeicamStickerCaptionTrackList();
        int size = meicamStickerCaptionTrackList.size();
        if (size < i) {
            Log.e(TAG, "getStickerCaptionData: trackIndex is bigger than trackSize。 trackIndex = " + i + "  trackSize = " + size);
            return false;
        }
        List<ClipInfo> clipInfoList = meicamStickerCaptionTrackList.get(i).getClipInfoList();
        for (int size2 = clipInfoList.size() - 1; size2 >= 0; size2--) {
            if (clipInfoList.get(size2).getInPoint() == j) {
                clipInfoList.remove(size2);
                return true;
            }
        }
        return false;
    }

    public static MeicamVideoTrack getMainTrack() {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (meicamVideoTrackList.size() == 0) {
            meicamVideoTrackList.add(new MeicamVideoTrack(0));
        }
        return meicamVideoTrackList.get(0);
    }

    public static void addMainTransition(MeicamTransition meicamTransition) {
        List<MeicamTransition> transitionInfoList = getMainTrack().getTransitionInfoList();
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= transitionInfoList.size()) {
                break;
            } else if (transitionInfoList.get(i).getIndex() == meicamTransition.getIndex()) {
                transitionInfoList.set(i, meicamTransition);
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (!z) {
            transitionInfoList.add(meicamTransition);
        }
    }

    public static void setMainTrackData(ArrayList<MeicamVideoClip> arrayList) {
        List<ClipInfo> clipInfoList = getMainTrack().getClipInfoList();
        clipInfoList.addAll(arrayList);
        initClipInfos(clipInfoList);
    }

    public static void addMainTrackData(ArrayList<MeicamVideoClip> arrayList, int i) {
        ClipInfo clipInfo;
        if (!CollectionUtils.isEmpty(arrayList)) {
            List<ClipInfo> clipInfoList = getMainTrack().getClipInfoList();
            int i2 = 0;
            if (i == 0) {
                initClipInfos(arrayList);
                long outPoint = arrayList.get(arrayList.size() - 1).getOutPoint();
                while (i2 < clipInfoList.size()) {
                    ClipInfo clipInfo2 = clipInfoList.get(i2);
                    clipInfo2.setInPoint(clipInfo2.getInPoint() + outPoint);
                    clipInfo2.setOutPoint(clipInfo2.getOutPoint() + outPoint);
                    i2++;
                }
            } else {
                initClipInfos(arrayList, clipInfoList.get(i - 1).getOutPoint());
                long j = 0;
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    j += arrayList.get(i3).getOrgDuration();
                }
                while (i2 < clipInfoList.size()) {
                    if (i2 >= i && (clipInfo = clipInfoList.get(i2)) != null) {
                        clipInfo.setInPoint(clipInfo.getInPoint() + j);
                        clipInfo.setOutPoint(clipInfo.getOutPoint() + j);
                    }
                    i2++;
                }
            }
            clipInfoList.addAll(i, arrayList);
        }
    }

    public static MeicamTransition getMainTrackTransitionByIndex(int i) {
        for (MeicamTransition meicamTransition : getMainTrack().getTransitionInfoList()) {
            if (meicamTransition.getIndex() == i) {
                return meicamTransition;
            }
        }
        return null;
    }

    private static void initClipInfos(List<? extends ClipInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            ClipInfo clipInfo = (ClipInfo) list.get(i);
            if (i != 0) {
                ClipInfo clipInfo2 = (ClipInfo) list.get(i - 1);
                if (clipInfo instanceof MeicamVideoClip) {
                    clipInfo.setInPoint(clipInfo2.getOutPoint());
                    clipInfo.setOutPoint(clipInfo.getInPoint() + ((MeicamVideoClip) clipInfo).getOrgDuration());
                }
            } else if (clipInfo instanceof MeicamVideoClip) {
                clipInfo.setInPoint(0);
                clipInfo.setOutPoint(((MeicamVideoClip) clipInfo).getOrgDuration());
            }
            MeicamVideoClip meicamVideoClip = (MeicamVideoClip) clipInfo;
            meicamVideoClip.setTrimIn(0);
            meicamVideoClip.setTrimOut(meicamVideoClip.getOrgDuration());
        }
    }

    private static void initClipInfos(List<? extends ClipInfo> list, long j) {
        for (int i = 0; i < list.size(); i++) {
            ClipInfo clipInfo = (ClipInfo) list.get(i);
            if (i != 0) {
                ClipInfo clipInfo2 = (ClipInfo) list.get(i - 1);
                if (clipInfo instanceof MeicamVideoClip) {
                    clipInfo.setInPoint(clipInfo2.getOutPoint());
                    clipInfo.setOutPoint(clipInfo.getInPoint() + ((MeicamVideoClip) clipInfo).getOrgDuration());
                }
            } else if (clipInfo instanceof MeicamVideoClip) {
                clipInfo.setInPoint(j);
                clipInfo.setOutPoint(((MeicamVideoClip) clipInfo).getOrgDuration() + j);
            }
            MeicamVideoClip meicamVideoClip = (MeicamVideoClip) clipInfo;
            meicamVideoClip.setTrimIn(0);
            meicamVideoClip.setTrimOut(meicamVideoClip.getOrgDuration());
        }
    }

    public static void setMainTrackTransitionAll(MeicamTransition meicamTransition) {
        MeicamVideoTrack mainTrack = getMainTrack();
        List<MeicamTransition> transitionInfoList = mainTrack.getTransitionInfoList();
        int clipCount = ((NvsVideoTrack) mainTrack.getObject()).getClipCount();
        for (int i = 0; i < clipCount - 1; i++) {
            MeicamTransition meicamTransition2 = (MeicamTransition) meicamTransition.clone();
            meicamTransition2.setIndex(i);
            if (i < transitionInfoList.size()) {
                transitionInfoList.set(i, meicamTransition2);
            } else {
                transitionInfoList.add(meicamTransition);
            }
            meicamTransition2.bindToTimeline((NvsVideoTrack) mainTrack.getObject());
        }
    }

    public static void addStickerCaption(int i, ClipInfo clipInfo) {
        MeicamStickerCaptionTrack meicamStickerCaptionTrack;
        List<MeicamStickerCaptionTrack> meicamStickerCaptionTrackList = TimelineData.getInstance().getMeicamStickerCaptionTrackList();
        int size = meicamStickerCaptionTrackList.size();
        if (i > size) {
            Log.e(TAG, "getStickerCaptionData: trackIndex is bigger than trackSize。 trackIndex = " + i + "  trackSize = " + size);
            return;
        }
        if (size == i) {
            meicamStickerCaptionTrack = new MeicamStickerCaptionTrack(i);
            meicamStickerCaptionTrackList.add(meicamStickerCaptionTrack);
        } else {
            meicamStickerCaptionTrack = meicamStickerCaptionTrackList.get(i);
        }
        meicamStickerCaptionTrack.getClipInfoList().add(clipInfo);
    }

    public static List<ClipInfo> getStickerCaption() {
        List<MeicamStickerCaptionTrack> meicamStickerCaptionTrackList = TimelineData.getInstance().getMeicamStickerCaptionTrackList();
        if (CollectionUtils.isEmpty(meicamStickerCaptionTrackList)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < meicamStickerCaptionTrackList.size(); i++) {
            arrayList.addAll(arrayList.size(), meicamStickerCaptionTrackList.get(i).getClipInfoList());
        }
        return arrayList;
    }

    public static void addVideoClip(int i, ClipInfo clipInfo) {
        MeicamVideoTrack meicamVideoTrack;
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        int size = meicamVideoTrackList.size();
        if (i <= size) {
            if (size == i) {
                meicamVideoTrack = new MeicamVideoTrack(i);
                meicamVideoTrackList.add(meicamVideoTrack);
            } else {
                meicamVideoTrack = meicamVideoTrackList.get(i);
            }
            meicamVideoTrack.getClipInfoList().add(clipInfo);
        }
    }

    public static void removeVideoClip(int i, long j) {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        int size = meicamVideoTrackList.size();
        if (i > size) {
            Log.e(TAG, "removeVideoClip: trackIndex: " + i + "  trackSize: " + size);
            return;
        }
        List<ClipInfo> clipInfoList = meicamVideoTrackList.get(i).getClipInfoList();
        for (int size2 = clipInfoList.size() - 1; size2 >= 0; size2--) {
            if (j == clipInfoList.get(size2).getInPoint()) {
                clipInfoList.remove(size2);
                return;
            }
        }
    }

    public static int getPipClipTrackIndexByInPoint(long j) {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (CollectionUtils.isEmpty(meicamVideoTrackList)) {
            return 0;
        }
        int size = meicamVideoTrackList.size();
        for (int i = size - 1; i < size; i--) {
            List<ClipInfo> clipInfoList = meicamVideoTrackList.get(i).getClipInfoList();
            for (int size2 = clipInfoList.size() - 1; size2 >= 0; size2--) {
                if (j == clipInfoList.get(size2).getInPoint()) {
                    return i;
                }
            }
        }
        return 0;
    }

    public static List<ClipInfo> getStickerOrCaptionListByType(String str) {
        List<MeicamStickerCaptionTrack> meicamStickerCaptionTrackList = TimelineData.getInstance().getMeicamStickerCaptionTrackList();
        ArrayList arrayList = new ArrayList();
        for (MeicamStickerCaptionTrack meicamStickerCaptionTrack : meicamStickerCaptionTrackList) {
            List<ClipInfo> clipInfoList = meicamStickerCaptionTrack.getClipInfoList();
            for (int size = clipInfoList.size() - 1; size >= 0; size--) {
                ClipInfo clipInfo = clipInfoList.get(size);
                if (clipInfo.getType().equals(str)) {
                    arrayList.add(clipInfo);
                }
            }
        }
        return arrayList;
    }

    public static List<ClipInfo> getAllPipClipList() {
        ArrayList arrayList = new ArrayList();
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        for (int i = 1; i < meicamVideoTrackList.size(); i++) {
            arrayList.addAll(meicamVideoTrackList.get(i).getClipInfoList());
        }
        return arrayList;
    }

    public static List<ClipInfo> getAddCaption() {
        ArrayList arrayList = new ArrayList();
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        for (int i = 1; i < meicamVideoTrackList.size(); i++) {
            arrayList.addAll(meicamVideoTrackList.get(i).getClipInfoList());
        }
        return arrayList;
    }

    public static MeicamVideoClip findVideoClipByTrackAndInPoint(int i, long j) {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (CollectionUtils.isEmpty(meicamVideoTrackList)) {
            return null;
        }
        if (!CollectionUtils.isIndexAvailable(i, meicamVideoTrackList)) {
            Logger.e(TAG, "findVideoClipByTrackAndInPoint: trackIndex is invalid");
            return null;
        }
        List<ClipInfo> clipInfoList = meicamVideoTrackList.get(i).getClipInfoList();
        Collections.sort(clipInfoList);
        Log.e(TAG, "findVideoClipByTrackAndInPoint: " + i + "  " + j);
        for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
            ClipInfo clipInfo = clipInfoList.get(i2);
            Log.e(TAG, "findVideoClipByTrackAndInPoint: " + clipInfo.getInPoint());
            if (clipInfo.getInPoint() == j) {
                return (MeicamVideoClip) clipInfo;
            }
        }
        return null;
    }

    public static long getOtherTrackMaxDuration() {
        return Math.max(Math.max(Math.max(0L, getPipTrackMaxDuration()), getAudioTrackMaxDuration()), getStickerAndCaptionTrackMaxDuration());
    }

    public static long getPipTrackMaxDuration() {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        long j = 0;
        for (int size = meicamVideoTrackList.size() - 1; size >= 1; size--) {
            NvsVideoTrack nvsVideoTrack = (NvsVideoTrack) meicamVideoTrackList.get(size).getObject();
            if (nvsVideoTrack != null) {
                j = Math.max(nvsVideoTrack.getDuration(), j);
            }
        }
        return j;
    }

    public static long getAudioTrackMaxDuration() {
        List<MeicamAudioTrack> meicamAudioTrackList = TimelineData.getInstance().getMeicamAudioTrackList();
        long j = 0;
        for (int i = 0; i < meicamAudioTrackList.size(); i++) {
            NvsAudioTrack nvsAudioTrack = (NvsAudioTrack) meicamAudioTrackList.get(i).getObject();
            if (nvsAudioTrack != null) {
                j = Math.max(nvsAudioTrack.getDuration(), j);
            }
        }
        return j;
    }

    public static long getStickerAndCaptionTrackMaxDuration() {
        long j = 0;
        for (MeicamStickerCaptionTrack meicamStickerCaptionTrack : TimelineData.getInstance().getMeicamStickerCaptionTrackList()) {
            List<ClipInfo> clipInfoList = meicamStickerCaptionTrack.getClipInfoList();
            if (!clipInfoList.isEmpty()) {
                Collections.sort(clipInfoList);
                j = Math.max(clipInfoList.get(clipInfoList.size() - 1).getOutPoint(), j);
            }
        }
        return j;
    }

    public static void changeMainTrackVideoClipSpeed(int i, long j, float f) {
        List<ClipInfo> clipInfoList = getMainTrack().getClipInfoList();
        MeicamVideoClip findVideoClipByTrackAndInPoint = findVideoClipByTrackAndInPoint(i, j);
        if (findVideoClipByTrackAndInPoint != null) {
            NvsVideoClip nvsVideoClip = (NvsVideoClip) findVideoClipByTrackAndInPoint.getObject();
            double d = (double) f;
            nvsVideoClip.changeSpeed(d);
            findVideoClipByTrackAndInPoint.setSpeed(d);
            findVideoClipByTrackAndInPoint.setOutPoint(nvsVideoClip.getOutPoint());
            for (ClipInfo clipInfo : clipInfoList) {
                if (clipInfo.getInPoint() > findVideoClipByTrackAndInPoint.getInPoint()) {
                    NvsVideoClip nvsVideoClip2 = (NvsVideoClip) clipInfo.getObject();
                    clipInfo.setInPoint(nvsVideoClip2.getInPoint());
                    clipInfo.setOutPoint(nvsVideoClip2.getOutPoint());
                }
            }
        }
    }

    public static long getMainTrackDuration() {
        List<ClipInfo> clipInfoList = getMainTrack().getClipInfoList();
        Collections.sort(clipInfoList);
        return ((MeicamVideoClip) clipInfoList.get(clipInfoList.size() - 1)).getOutPoint();
    }

    public static MeicamVideoClip getMainTrackLastClip() {
        List<ClipInfo> clipInfoList = getMainTrack().getClipInfoList();
        Collections.sort(clipInfoList);
        return (MeicamVideoClip) clipInfoList.get(clipInfoList.size() - 1);
    }

    public static void changePipTrackVideoClipSpeed(int i, long j, float f) {
        List<ClipInfo> clipInfoList = TimelineData.getInstance().getMeicamVideoTrackList().get(i).getClipInfoList();
        MeicamVideoClip findVideoClipByTrackAndInPoint = findVideoClipByTrackAndInPoint(i, j);
        if (findVideoClipByTrackAndInPoint != null) {
            NvsVideoClip nvsVideoClip = (NvsVideoClip) findVideoClipByTrackAndInPoint.getObject();
            double d = (double) f;
            nvsVideoClip.changeSpeed(d);
            findVideoClipByTrackAndInPoint.setSpeed(d);
            findVideoClipByTrackAndInPoint.setOutPoint(nvsVideoClip.getOutPoint());
            for (ClipInfo clipInfo : clipInfoList) {
                if (clipInfo.getInPoint() > findVideoClipByTrackAndInPoint.getInPoint()) {
                    NvsVideoClip nvsVideoClip2 = (NvsVideoClip) clipInfo.getObject();
                    clipInfo.setInPoint(nvsVideoClip2.getInPoint());
                    clipInfo.setOutPoint(nvsVideoClip2.getOutPoint());
                }
            }
        }
    }

    public static MeicamTimelineVideoFxClip findTimelineEffectByTrackAndInPoint(int i, long j) {
        List<MeicamTimelineVideoFxTrack> meicamTimelineVideoFxTrackList = TimelineData.getInstance().getMeicamTimelineVideoFxTrackList();
        if (CollectionUtils.isEmpty(meicamTimelineVideoFxTrackList)) {
            return null;
        }
        if (!CollectionUtils.isIndexAvailable(i, meicamTimelineVideoFxTrackList)) {
            Logger.e(TAG, "findVideoClipByTrackAndInPoint: trackIndex is invalid");
            return null;
        }
        List<ClipInfo> clipInfoList = meicamTimelineVideoFxTrackList.get(i).getClipInfoList();
        for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
            ClipInfo clipInfo = clipInfoList.get(i2);
            if (clipInfo.getInPoint() == j) {
                return (MeicamTimelineVideoFxClip) clipInfo;
            }
        }
        return null;
    }

    public static void addAudioClipInfoByTrackIndex(NvsAudioTrack nvsAudioTrack, MeicamAudioClip meicamAudioClip) {
        MeicamAudioTrack meicamAudioTrack;
        List<MeicamAudioTrack> meicamAudioTrackList = TimelineData.getInstance().getMeicamAudioTrackList();
        int index = nvsAudioTrack.getIndex();
        if (meicamAudioTrackList != null) {
            if (index >= meicamAudioTrackList.size()) {
                meicamAudioTrack = new MeicamAudioTrack(index);
                meicamAudioTrack.setObject(nvsAudioTrack);
                meicamAudioTrackList.add(meicamAudioTrack);
            } else {
                meicamAudioTrack = meicamAudioTrackList.get(index);
                meicamAudioTrack.setObject(nvsAudioTrack);
            }
            if (meicamAudioTrack != null) {
                List<ClipInfo> clipInfoList = meicamAudioTrack.getClipInfoList();
                clipInfoList.add(meicamAudioClip);
                Collections.sort(clipInfoList);
            }
        }
    }

    public static MeicamAudioClip findAudioClipInfoByTrackAndInpoint(int i, long j) {
        List<MeicamAudioTrack> meicamAudioTrackList = TimelineData.getInstance().getMeicamAudioTrackList();
        if (meicamAudioTrackList != null) {
            if (!CollectionUtils.isIndexAvailable(i, meicamAudioTrackList)) {
                Logger.e(TAG, "findAudioClipInfoByTrackAndInpoint: trackIndex is invalid");
                return null;
            }
            MeicamAudioTrack meicamAudioTrack = meicamAudioTrackList.get(i);
            if (meicamAudioTrack != null) {
                List<ClipInfo> clipInfoList = meicamAudioTrack.getClipInfoList();
                Collections.sort(clipInfoList);
                for (ClipInfo clipInfo : clipInfoList) {
                    if (clipInfo != null && clipInfo.getInPoint() == j) {
                        return (MeicamAudioClip) clipInfo;
                    }
                }
            }
        }
        return null;
    }

    public static void refreshMeicamAudioClipByTrackIndex(int i) {
        List<MeicamAudioTrack> meicamAudioTrackList = TimelineData.getInstance().getMeicamAudioTrackList();
        if (CollectionUtils.isEmpty(meicamAudioTrackList)) {
            Logger.e(TAG, "refreshMeicamAudioClipByTrackIndex: meicamAudioTracks is null");
            return;
        }
        if (!CollectionUtils.isIndexAvailable(i, meicamAudioTrackList)) {
            Logger.e(TAG, "refreshMeicamAudioClipByTrackIndex: trackIndex is invalid");
        }
        MeicamAudioTrack meicamAudioTrack = meicamAudioTrackList.get(i);
        for (int i2 = 0; i2 < meicamAudioTrack.getClipInfoList().size(); i2++) {
            MeicamAudioClip meicamAudioClip = (MeicamAudioClip) meicamAudioTrack.getClipInfoList().get(i2);
            meicamAudioClip.loadData((NvsAudioClip) meicamAudioClip.getObject());
        }
    }

    public static List<ClipInfo> getMainTrackVideoClip() {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        List<ClipInfo> list = null;
        MeicamVideoTrack meicamVideoTrack = (meicamVideoTrackList == null || meicamVideoTrackList.size() <= 0) ? null : meicamVideoTrackList.get(0);
        if (meicamVideoTrack != null) {
            list = meicamVideoTrack.getClipInfoList();
        }
        Collections.sort(list);
        return list;
    }

    public static double getMainTrackVideoClipSpeedByIndex(int i) {
        MeicamVideoClip meicamVideoClip;
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        List<ClipInfo> list = null;
        MeicamVideoTrack meicamVideoTrack = (meicamVideoTrackList == null || meicamVideoTrackList.size() <= 0) ? null : meicamVideoTrackList.get(0);
        if (meicamVideoTrack != null) {
            list = meicamVideoTrack.getClipInfoList();
        }
        Collections.sort(list);
        if (i < list.size() && (meicamVideoClip = (MeicamVideoClip) list.get(i)) != null) {
            return meicamVideoClip.getSpeed();
        }
        return 1.0d;
    }

    public static void setMeicamVideoClips(int i, List<? extends ClipInfo> list) {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        (CollectionUtils.isIndexAvailable(i, meicamVideoTrackList) ? meicamVideoTrackList.get(i) : null).setClipInfoList(list);
    }

    public static List<ClipInfo> getPipTrackVideoClip() {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (CollectionUtils.isEmpty(meicamVideoTrackList)) {
            return null;
        }
        if (meicamVideoTrackList.size() < 1) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i < meicamVideoTrackList.size(); i++) {
            arrayList.addAll(meicamVideoTrackList.get(i).getClipInfoList());
        }
        return arrayList;
    }

    public static void updateData(int i, String str) {
        TrackInfo trackInfo = getTrackInfo(i, str);
        if (trackInfo != null) {
            for (ClipInfo clipInfo : trackInfo.getClipInfoList()) {
                clipInfo.loadData(clipInfo.getObject());
            }
        }
    }

    private static TrackInfo getTrackInfo(int i, String str) {
        ArrayList arrayList = new ArrayList();
        if (CommonData.CLIP_TIMELINE_FX.equals(str)) {
            arrayList.addAll(TimelineData.getInstance().getMeicamTimelineVideoFxTrackList());
        } else if (CommonData.CLIP_STICKER.equals(str) || CommonData.CLIP_COMPOUND_CAPTION.equals(str) || CommonData.CLIP_CAPTION.equals(str)) {
            arrayList.addAll(TimelineData.getInstance().getMeicamStickerCaptionTrackList());
        } else if (CommonData.CLIP_VIDEO.equals(str) || CommonData.CLIP_IMAGE.equals(str)) {
            arrayList.addAll(TimelineData.getInstance().getMeicamVideoTrackList());
            i++;
        } else if (CommonData.CLIP_AUDIO.equals(str)) {
            arrayList.addAll(TimelineData.getInstance().getMeicamAudioTrackList());
        }
        if (CollectionUtils.isEmpty(arrayList)) {
            return null;
        }
        if (CollectionUtils.isIndexAvailable(i, arrayList)) {
            return (TrackInfo) arrayList.get(i);
        }
        Logger.e(TAG, "findVideoClipByTrackAndInPoint: trackIndex is invalid");
        return null;
    }

    public static List<ClipInfo> getAllPipVideoClips() {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (meicamVideoTrackList == null || meicamVideoTrackList.size() == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < meicamVideoTrackList.size(); i++) {
            MeicamVideoTrack meicamVideoTrack = meicamVideoTrackList.get(i);
            if (!(meicamVideoTrack == null || meicamVideoTrack.getIndex() == 0)) {
                if (arrayList.size() == 0) {
                    arrayList.addAll(meicamVideoTrack.getClipInfoList());
                } else {
                    arrayList.addAll(arrayList.size(), meicamVideoTrack.getClipInfoList());
                }
            }
        }
        return arrayList;
    }

    public static List<ClipInfo> getVideoClipsInTrackIndex(int i) {
        MeicamVideoTrack meicamVideoTrack;
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (meicamVideoTrackList == null || meicamVideoTrackList.size() == 0 || meicamVideoTrackList.size() <= i || (meicamVideoTrack = meicamVideoTrackList.get(i)) == null) {
            return null;
        }
        return meicamVideoTrack.getClipInfoList();
    }

    public static NvsVideoTrack getNvsVideoTrack(int i) {
        MeicamVideoTrack meicamVideoTrack;
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (meicamVideoTrackList == null || meicamVideoTrackList.size() == 0 || meicamVideoTrackList.size() <= i || (meicamVideoTrack = meicamVideoTrackList.get(i)) == null) {
            return null;
        }
        return (NvsVideoTrack) meicamVideoTrack.getObject();
    }

    public static void moveMainTrackClipsFromIndex(int i, long j) {
        MeicamVideoTrack mainTrack = getMainTrack();
        if (mainTrack != null) {
            List<ClipInfo> clipInfoList = mainTrack.getClipInfoList();
            if (!CollectionUtils.isEmpty(clipInfoList)) {
                while (i < clipInfoList.size()) {
                    ClipInfo clipInfo = clipInfoList.get(i);
                    if (clipInfo != null) {
                        clipInfo.setInPoint(clipInfo.getInPoint() + j);
                        clipInfo.setOutPoint(clipInfo.getOutPoint() + j);
                    }
                    i++;
                }
            }
        }
    }

    public static int getAspectRatio() {
        NvsVideoResolution videoResolution = TimelineData.getInstance().getVideoResolution();
        return CommonData.AspectRatio.getAspect((((float) videoResolution.imageWidth) * 1.0f) / ((float) videoResolution.imageHeight));
    }

    public void getAudioCount(NvsTimeline nvsTimeline) {
        nvsTimeline.audioTrackCount();
    }

    public static void setThemeQuiet(NvsTimeline nvsTimeline) {
        MeicamTheme meicamTheme = TimelineData.getInstance().getMeicamTheme();
        if (meicamTheme != null && !TextUtils.isEmpty(meicamTheme.getThemePackageId())) {
            nvsTimeline.setThemeMusicVolumeGain(0.0f, 0.0f);
        }
    }

    public static void restoreThemeVolume(NvsTimeline nvsTimeline) {
        MeicamTheme meicamTheme = TimelineData.getInstance().getMeicamTheme();
        if (meicamTheme != null && !TextUtils.isEmpty(meicamTheme.getThemePackageId())) {
            nvsTimeline.setThemeMusicVolumeGain(1.0f, 1.0f);
        }
    }

    public static ClipInfo getVideoClip(int i, long j) {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        int size = meicamVideoTrackList.size();
        if (size <= i) {
            Log.e(TAG, "getVideoClip: trackIndex is bigger than trackSize。 trackIndex = " + i + "  trackSize = " + size);
            return null;
        }
        for (ClipInfo clipInfo : meicamVideoTrackList.get(i).getClipInfoList()) {
            if (clipInfo.getInPoint() == j) {
                return clipInfo;
            }
        }
        return null;
    }

    public static void refreshTransitionsAfterSplit(int i) {
        List<MeicamTransition> transitionInfoList = getMainTrack().getTransitionInfoList();
        if (!transitionInfoList.isEmpty()) {
            for (int i2 = 0; i2 < transitionInfoList.size(); i2++) {
                MeicamTransition meicamTransition = transitionInfoList.get(i2);
                if (meicamTransition.getIndex() >= i) {
                    meicamTransition.setIndex(meicamTransition.getIndex() + 1);
                }
            }
        }
    }

    public static void verifyTransition() {
        MeicamVideoTrack mainTrack = getMainTrack();
        List<MeicamTransition> transitionInfoList = mainTrack.getTransitionInfoList();
        NvsVideoTrack nvsVideoTrack = (NvsVideoTrack) mainTrack.getObject();
        int clipCount = nvsVideoTrack.getClipCount();
        if (clipCount > 1) {
            for (int i = 0; i < clipCount; i++) {
                if (nvsVideoTrack.getTransitionBySourceClipIndex(i) != null && !transitionInfoList.contains(new MeicamTransition(i, 1, ""))) {
                    nvsVideoTrack.setBuiltinTransition(i, "");
                }
            }
        }
    }

    public static void LogTransitions() {
        MeicamVideoTrack mainTrack = getMainTrack();
        for (MeicamTransition meicamTransition : mainTrack.getTransitionInfoList()) {
            Log.e(TAG, "LogTransitions: getIndex: " + meicamTransition.getIndex() + " getType: " + meicamTransition.getType() + "  " + meicamTransition.getDesc());
        }
        Log.e(TAG, "LogTransitions: -------------------");
        NvsVideoTrack nvsVideoTrack = (NvsVideoTrack) mainTrack.getObject();
        for (int i = 0; i < nvsVideoTrack.getClipCount(); i++) {
            NvsVideoTransition transitionBySourceClipIndex = nvsVideoTrack.getTransitionBySourceClipIndex(i);
            if (transitionBySourceClipIndex != null) {
                Log.e(TAG, "LogTransitions: index: " + i + "  " + transitionBySourceClipIndex.getBuiltinVideoTransitionName() + "  " + transitionBySourceClipIndex.getVideoTransitionPackageId());
            }
        }
        Log.e(TAG, "LogTransitions: @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public static void LogMainTrack() {
        List<ClipInfo> clipInfoList = getMainTrack().getClipInfoList();
        Collections.sort(clipInfoList);
        Iterator<ClipInfo> it = clipInfoList.iterator();
        while (it.hasNext()) {
            MeicamVideoClip meicamVideoClip = (MeicamVideoClip) it.next();
            Log.e(TAG, "LogMainTrack: getInPoint: " + meicamVideoClip.getInPoint() + "  getOutPoint: " + meicamVideoClip.getOutPoint() + "  getTrimIn: " + meicamVideoClip.getTrimIn() + "  getTrimOut: " + meicamVideoClip.getTrimOut() + "  getSpeed: " + meicamVideoClip.getSpeed());
            NvsVideoClip nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject();
            Log.e(TAG, "LogMainTrack: getInPoint: " + nvsVideoClip.getInPoint() + "  getOutPoint: " + nvsVideoClip.getOutPoint() + "  getTrimIn: " + nvsVideoClip.getTrimIn() + "  getTrimOut: " + nvsVideoClip.getTrimOut() + "  getSpeed: " + nvsVideoClip.getSpeed());
            Log.e(TAG, "=========================================");
        }
    }

    public static void LogPipTrack() {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        for (int i = 1; i < meicamVideoTrackList.size(); i++) {
            List<ClipInfo> clipInfoList = meicamVideoTrackList.get(i).getClipInfoList();
            Collections.sort(clipInfoList);
            Iterator<ClipInfo> it = clipInfoList.iterator();
            while (it.hasNext()) {
                MeicamVideoClip meicamVideoClip = (MeicamVideoClip) it.next();
                Log.e(TAG, "LogPipTrack: getInPoint: " + meicamVideoClip.getInPoint() + "  getOutPoint: " + meicamVideoClip.getOutPoint() + "  getTrimIn: " + meicamVideoClip.getTrimIn() + "  getTrimOut: " + meicamVideoClip.getTrimOut() + "  getSpeed: " + meicamVideoClip.getSpeed());
                NvsVideoClip nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject();
                if (nvsVideoClip != null) {
                    Log.e(TAG, "LogPipTrack: getInPoint: " + nvsVideoClip.getInPoint() + "  getOutPoint: " + nvsVideoClip.getOutPoint() + "  getTrimIn: " + nvsVideoClip.getTrimIn() + "  getTrimOut: " + nvsVideoClip.getTrimOut() + "  getSpeed: " + nvsVideoClip.getSpeed() + " " + nvsVideoClip.toString());
                }
                Log.e(TAG, "=========================================");
            }
        }
    }

    public static void LogStickerAndCaption() {
        List<MeicamStickerCaptionTrack> meicamStickerCaptionTrackList = TimelineData.getInstance().getMeicamStickerCaptionTrackList();
        for (int i = 0; i < meicamStickerCaptionTrackList.size(); i++) {
            List<ClipInfo> clipInfoList = meicamStickerCaptionTrackList.get(i).getClipInfoList();
            for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                ClipInfo clipInfo = clipInfoList.get(i2);
                if (clipInfo instanceof MeicamStickerClip) {
                    MeicamStickerClip meicamStickerClip = (MeicamStickerClip) clipInfo;
                    Log.e(TAG, "LogStickerAndCaption:贴纸: trackIndex: " + i + " getInPoint: " + meicamStickerClip.getInPoint() + "  getOutPoint: " + meicamStickerClip.getOutPoint() + " getzValue: " + meicamStickerClip.getzValue());
                    NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker = (NvsTimelineAnimatedSticker) meicamStickerClip.getObject();
                    Log.e(TAG, "LogStickerAndCaption:贴纸: trackIndex: " + i + " getInPoint: " + nvsTimelineAnimatedSticker.getInPoint() + "  getOutPoint: " + nvsTimelineAnimatedSticker.getOutPoint() + " getzValue: " + nvsTimelineAnimatedSticker.getZValue());
                    Log.e(TAG, "=========================================");
                } else if (clipInfo instanceof MeicamCaptionClip) {
                    MeicamCaptionClip meicamCaptionClip = (MeicamCaptionClip) clipInfo;
                    Log.e(TAG, "LogStickerAndCaption:字幕: trackIndex: " + i + " getInPoint: " + meicamCaptionClip.getInPoint() + "  getOutPoint: " + meicamCaptionClip.getOutPoint() + " getzValue: " + meicamCaptionClip.getzValue());
                    NvsTimelineCaption nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject();
                    Log.e(TAG, "LogStickerAndCaption:字幕: trackIndex: " + i + " getInPoint: " + nvsTimelineCaption.getInPoint() + "  getOutPoint: " + nvsTimelineCaption.getOutPoint() + " getzValue: " + nvsTimelineCaption.getZValue());
                    Log.e(TAG, "=========================================");
                } else if (clipInfo instanceof MeicamCompoundCaptionClip) {
                    MeicamCompoundCaptionClip meicamCompoundCaptionClip = (MeicamCompoundCaptionClip) clipInfo;
                    Log.e(TAG, "LogStickerAndCaption:组合字幕: trackIndex: " + i + " getInPoint: " + meicamCompoundCaptionClip.getInPoint() + "  getOutPoint: " + meicamCompoundCaptionClip.getOutPoint() + " getzValue: " + meicamCompoundCaptionClip.getzValue());
                    NvsTimelineCompoundCaption nvsTimelineCompoundCaption = (NvsTimelineCompoundCaption) meicamCompoundCaptionClip.getObject();
                    Log.e(TAG, "LogStickerAndCaption:组合字幕: trackIndex: " + i + " getInPoint: " + nvsTimelineCompoundCaption.getInPoint() + "  getOutPoint: " + nvsTimelineCompoundCaption.getOutPoint() + " getzValue: " + nvsTimelineCompoundCaption.getZValue());
                    Log.e(TAG, "=========================================");
                }
            }
        }
    }
}
