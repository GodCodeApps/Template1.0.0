package com.meishe.myvideo.util.engine;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFrameRetriever;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meicam.sdk.NvsVideoTransition;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.engine.TimelineUtil;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamAudioClip;
import com.meishe.engine.bean.MeicamAudioFx;
import com.meishe.engine.bean.MeicamCaptionClip;
import com.meishe.engine.bean.MeicamCompoundCaptionClip;
import com.meishe.engine.bean.MeicamStickerCaptionTrack;
import com.meishe.engine.bean.MeicamStickerClip;
import com.meishe.engine.bean.MeicamTheme;
import com.meishe.engine.bean.MeicamTimelineVideoFxClip;
import com.meishe.engine.bean.MeicamTimelineVideoFxTrack;
import com.meishe.engine.bean.MeicamTransition;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.MeicamVideoFx;
import com.meishe.engine.bean.MeicamVideoTrack;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.TimelineDataUtil;
import com.meishe.engine.bean.background.MeicamBackgroundStory;
import com.meishe.engine.bean.background.MeicamStoryboardInfo;
import com.meishe.engine.data.MeicamAdjustData;
import com.meishe.engine.util.ColorUtil;
import com.meishe.engine.util.StoryboardUtil;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideo.bean.TransitionInfo;
import com.meishe.myvideo.bean.WaterMarkEffectInfo;
import com.meishe.myvideo.bean.WaterMarkInfo;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.manager.StateManager;
import com.meishe.myvideo.ui.bean.BaseUIClip;
import com.meishe.myvideo.util.Constants;
import com.meishe.myvideo.util.ImageUtils;
import com.meishe.myvideo.util.PathUtils;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideo.util.ui.ToastUtil;
import com.meishe.myvideo.view.MYEditorTimeLine;
import com.meishe.myvideoapp.R;
import com.meishe.player.view.VideoFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EditorEngine implements EditOperater {
    private static final String ATTACHMENT_KEY = "isBackground";
    private static final long MAX_IMAGE_LENGTH = 2400000000L;
    private static final String TAG = "EditorEngine";
    private static final EditorEngine mInstance = new EditorEngine();
    private int mEditType;
    private MeicamAudioClip mMeicamAudioClip = null;
    private NvsAudioClip mNvsAudioClip;
    private NvsAudioTrack mNvsAudioTrack = null;
    private OnTimelineChangeListener mOnTimelineChangeListener;
    private OnTrackChangeListener mOnTrackChangeListener;
    private long mStickerDuration = 0;
    private NvsStreamingContext mStreamingContext = NvsStreamingContext.getInstance();
    private NvsTimeline mTimeline;
    private VideoFragment mVideoFragment;
    private MeicamTimelineVideoFxTrack mVideoFxTrackClone;

    public interface OnTimelineChangeListener {
        public static final int TYPE_COPY_CLIP = 2;
        public static final int TYPE_CUT_CLIP = 3;
        public static final int TYPE_DELETE_CLIP = 1;
        public static final int TYPE_FREEZE_CLIP = 4;
        public static final int TYPE_REVERT_CLIP = 5;

        void onAddStickerCaption(ClipInfo clipInfo, int i);

        void onChangeBottomView();

        void onNeedTrackSelectChanged(int i, long j);

        void onSaveOperation();

        void onTimelineChangeShowToast(int i);

        void onTimelineChanged(NvsTimeline nvsTimeline, boolean z);

        void refreshEditorTimelineView(int i);
    }

    public interface OnTrackChangeListener {
        void audioEditCopyClip(long j, NvsAudioTrack nvsAudioTrack);

        void audioEditCutClip(NvsAudioTrack nvsAudioTrack, long j);

        void audioEditDeleteClip();
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void audioEditPoint() {
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void beauty(int i) {
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void changeTimelineSpeed(float f) {
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void changeVoice(String str) {
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void changeVolume(float f) {
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void denoise(boolean z) {
    }

    private EditorEngine() {
    }

    public static EditorEngine getInstance() {
        return mInstance;
    }

    public void setOnTimelineChangeListener(OnTimelineChangeListener onTimelineChangeListener) {
        this.mOnTimelineChangeListener = onTimelineChangeListener;
    }

    public void setOnTrackChangeListener(OnTrackChangeListener onTrackChangeListener) {
        this.mOnTrackChangeListener = onTrackChangeListener;
    }

    public void setVideoFragment(VideoFragment videoFragment) {
        this.mVideoFragment = videoFragment;
    }

    public NvsTimeline createTimeline() {
        this.mTimeline = TimelineUtil.createTimeline();
        return this.mTimeline;
    }

    public void setBaseUIClip(MeicamAudioClip meicamAudioClip) {
        this.mNvsAudioTrack = this.mTimeline.getAudioTrackByIndex(meicamAudioClip.getIndex());
        if (this.mNvsAudioTrack == null) {
            Log.e(TAG, "mNvsAudioTrack==null");
        }
        this.mMeicamAudioClip = meicamAudioClip;
        if (this.mMeicamAudioClip == null) {
            Log.e(TAG, "mMeicamAudioClip==null");
        }
        for (int i = 0; i < this.mNvsAudioTrack.getClipCount(); i++) {
            NvsAudioClip clipByIndex = this.mNvsAudioTrack.getClipByIndex(i);
            if (clipByIndex.getInPoint() == this.mMeicamAudioClip.getInPoint()) {
                this.mNvsAudioClip = clipByIndex;
                return;
            }
        }
    }

    public MeicamAudioClip getmMeicamAudioClip() {
        return this.mMeicamAudioClip;
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void setCurrentTimeline(NvsTimeline nvsTimeline) {
        this.mTimeline = nvsTimeline;
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public NvsTimeline getCurrentTimeline() {
        return this.mTimeline;
    }

    public void setEidtType(int i) {
        this.mEditType = i;
    }

    public int getEditType() {
        return this.mEditType;
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public MeicamVideoClip getEditVideoClip(long j, int i) {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (CollectionUtils.isEmpty(meicamVideoTrackList) || !CollectionUtils.isIndexAvailable(i, meicamVideoTrackList)) {
            return null;
        }
        List<ClipInfo> clipInfoList = meicamVideoTrackList.get(i).getClipInfoList();
        if (CollectionUtils.isEmpty(clipInfoList)) {
            return null;
        }
        for (ClipInfo clipInfo : clipInfoList) {
            if (clipInfo.getInPoint() <= j && clipInfo.getOutPoint() > j) {
                return (MeicamVideoClip) clipInfo;
            }
        }
        return null;
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public long getCurrentTimelinePosition() {
        return NvsStreamingContext.getInstance().getTimelineCurrentPosition(this.mTimeline);
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void changeClipSpeed(float f) {
        MeicamVideoClip editVideoClip = getEditVideoClip(getCurrentTimelinePosition(), 0);
        if (editVideoClip != null) {
            editVideoClip.setSpeed((double) f);
            seekTimeline(getCurrentTimelinePosition(), 0);
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void cutClip(MeicamVideoClip meicamVideoClip, int i, boolean z) {
        OnTimelineChangeListener onTimelineChangeListener;
        if (this.mTimeline != null && meicamVideoClip != null) {
            NvsVideoClip nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject();
            long currentTimelinePosition = getCurrentTimelinePosition();
            if ((currentTimelinePosition <= nvsVideoClip.getInPoint() + CommonData.MIN_SHOW_LENGTH_DURATION || currentTimelinePosition >= nvsVideoClip.getOutPoint() - CommonData.MIN_SHOW_LENGTH_DURATION) && (onTimelineChangeListener = this.mOnTimelineChangeListener) != null) {
                onTimelineChangeListener.onTimelineChangeShowToast(R.string.current_position_not_allow_cut);
            } else if (!splitClip(meicamVideoClip, i, currentTimelinePosition)) {
                if (!z) {
                    StateManager.getInstance().setCurrentState(Constants.STATE_EDTI_CUT);
                }
                seekTimeline(currentTimelinePosition, 0);
                OnTimelineChangeListener onTimelineChangeListener2 = this.mOnTimelineChangeListener;
                if (onTimelineChangeListener2 != null) {
                    onTimelineChangeListener2.onTimelineChanged(this.mTimeline, true);
                    this.mOnTimelineChangeListener.refreshEditorTimelineView(3);
                }
            }
        }
    }

    private boolean handleCutClip(MeicamVideoClip meicamVideoClip, int i, boolean z, long j) {
        NvsVideoClip nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject();
        if (nvsVideoClip == null) {
            return true;
        }
        if (z) {
            List<ClipInfo> videoClipsInTrackIndex = TimelineDataUtil.getVideoClipsInTrackIndex(i);
            int index = nvsVideoClip.getIndex();
            if (videoClipsInTrackIndex == null && index > videoClipsInTrackIndex.size() - 1) {
                return true;
            }
            long inPoint = nvsVideoClip.getInPoint();
            long outPoint = nvsVideoClip.getOutPoint();
            long trimIn = (j - inPoint) + meicamVideoClip.getTrimIn();
            meicamVideoClip.setTrimOut(trimIn);
            MeicamVideoClip meicamVideoClip2 = (MeicamVideoClip) meicamVideoClip.clone();
            if (meicamVideoClip2 == null) {
                return true;
            }
            meicamVideoClip2.setTrimIn(trimIn);
            meicamVideoClip2.setTrimOut((outPoint - j) + trimIn);
            meicamVideoClip2.setInPoint(j);
            videoClipsInTrackIndex.add(index + 1, meicamVideoClip2);
            return false;
        }
        List<ClipInfo> mainTrackVideoClip = TimelineDataUtil.getMainTrackVideoClip();
        int index2 = nvsVideoClip.getIndex();
        if (mainTrackVideoClip == null && index2 > mainTrackVideoClip.size() - 1) {
            return true;
        }
        long inPoint2 = nvsVideoClip.getInPoint();
        long outPoint2 = nvsVideoClip.getOutPoint();
        long trimIn2 = (j - inPoint2) + meicamVideoClip.getTrimIn();
        meicamVideoClip.setTrimOut(trimIn2);
        meicamVideoClip.setOutPoint(j);
        MeicamVideoClip meicamVideoClip3 = (MeicamVideoClip) meicamVideoClip.clone();
        if (meicamVideoClip3 == null) {
            return true;
        }
        meicamVideoClip3.setTrimIn(trimIn2);
        meicamVideoClip3.setTrimOut((outPoint2 - j) + trimIn2);
        meicamVideoClip3.setInPoint(j);
        mainTrackVideoClip.add(index2 + 1, meicamVideoClip3);
        return false;
    }

    private boolean splitClip(MeicamVideoClip meicamVideoClip, int i, long j) {
        NvsVideoClip nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject();
        if (nvsVideoClip == null) {
            Log.e(TAG, "splitClip: clipInfo.getObject is null!");
            return true;
        }
        int index = nvsVideoClip.getIndex();
        List<ClipInfo> videoClipsInTrackIndex = TimelineDataUtil.getVideoClipsInTrackIndex(i);
        if (videoClipsInTrackIndex == null || index > videoClipsInTrackIndex.size() - 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("splitClip: videoClipsInTrackIndex == null: ");
            sb.append(videoClipsInTrackIndex == null);
            sb.append("  clipIndex: ");
            sb.append(index);
            Log.e(TAG, sb.toString());
            return true;
        }
        MeicamVideoClip meicamVideoClip2 =new MeicamVideoClip();
        meicamVideoClip2=meicamVideoClip;
        NvsVideoTrack videoTrackByIndex = getCurrentTimeline().getVideoTrackByIndex(i);
        boolean splitClip = videoTrackByIndex.splitClip(index, j);
        if (splitClip) {
            NvsVideoClip clipByIndex = videoTrackByIndex.getClipByIndex(index);
            meicamVideoClip.setOutPoint(nvsVideoClip.getOutPoint());
            meicamVideoClip.setTrimOut(nvsVideoClip.getTrimOut());
            meicamVideoClip.setObject(clipByIndex);
            meicamVideoClip.setData(clipByIndex, videoTrackByIndex.getIndex());
            int i2 = index + 1;
            NvsVideoClip clipByIndex2 = videoTrackByIndex.getClipByIndex(i2);
                meicamVideoClip2.setObject(clipByIndex2);
                meicamVideoClip2.setTrimIn(clipByIndex2.getTrimIn());
                meicamVideoClip2.setInPoint(clipByIndex2.getInPoint());
                meicamVideoClip2.setData(clipByIndex2, videoTrackByIndex.getIndex());
                videoClipsInTrackIndex.add(i2, meicamVideoClip2);
            TimelineDataUtil.refreshTransitionsAfterSplit(index);
        }
        return !splitClip;
    }

    public void addPipVideoClipToTrack(MeicamVideoClip meicamVideoClip, int i) {
        MeicamVideoTrack meicamVideoTrack;
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (meicamVideoTrackList.size() > i) {
            meicamVideoTrack = meicamVideoTrackList.get(i);
        } else {
            MeicamVideoTrack meicamVideoTrack2 = new MeicamVideoTrack(meicamVideoTrackList.size());
            meicamVideoTrack2.bindToTimeline(this.mTimeline);
            meicamVideoTrackList.add(meicamVideoTrack2);
            meicamVideoTrack = meicamVideoTrack2;
        }
        meicamVideoClip.addToTimeline((NvsVideoTrack) meicamVideoTrack.getObject());
        TimelineDataUtil.addVideoClip(i, meicamVideoClip);
    }

    public void copyVideoClip(MeicamVideoClip meicamVideoClip, boolean z) {
        MeicamVideoTrack meicamVideoTrack;
        if (this.mTimeline != null && meicamVideoClip != null) {
            long currentTimelinePosition = getCurrentTimelinePosition();
            if (z) {
                MeicamVideoClip meicamVideoClip2 = (MeicamVideoClip) meicamVideoClip.clone();
                if (meicamVideoClip2 != null) {
                    long currentTimelinePosition2 = getCurrentTimelinePosition();
                    long trimOut = (meicamVideoClip2.getTrimOut() + currentTimelinePosition2) - meicamVideoClip2.getTrimIn();
                    long duration = this.mTimeline.getDuration();
                    if (trimOut > duration) {
                        trimOut = duration;
                    }
                    meicamVideoClip2.setInPoint(currentTimelinePosition2);
                    meicamVideoClip2.setOutPoint(trimOut);
                    addPipVideoClipToTrack(meicamVideoClip2, getPipVideoClipIndex(currentTimelinePosition2, trimOut));
                } else {
                    return;
                }
            } else {
                MeicamVideoClip meicamVideoClip3 = (MeicamVideoClip) meicamVideoClip.clone();
                if (meicamVideoClip3 != null && (meicamVideoTrack = TimelineData.getInstance().getMeicamVideoTrackList().get(0)) != null) {
                    meicamVideoClip3.setInPoint(((NvsVideoClip) meicamVideoClip.getObject()).getOutPoint());
                    meicamVideoClip3.setOutPoint(meicamVideoClip3.getInPoint() + (((NvsVideoClip) meicamVideoClip.getObject()).getOutPoint() - ((NvsVideoClip) meicamVideoClip.getObject()).getInPoint()));
                    List<ClipInfo> mainTrackVideoClip = TimelineDataUtil.getMainTrackVideoClip();
                    if (mainTrackVideoClip != null) {
                        int indexOf = mainTrackVideoClip.indexOf(meicamVideoClip);
                        int i = indexOf + 1;
                        mainTrackVideoClip.add(i, meicamVideoClip3);
                        meicamVideoClip3.insertToTimeline((NvsVideoTrack) meicamVideoTrack.getObject(), i);
                        TimelineDataUtil.moveMainTrackClipsFromIndex(indexOf + 2, meicamVideoClip3.getOutPoint() - meicamVideoClip3.getInPoint());
                        TimelineUtil.clearAllBuildInTransition(this.mTimeline);
                        TimelineUtil.setTransition(this.mTimeline, meicamVideoTrack.getTransitionInfoList());
                        StateManager.getInstance().setCurrentState(Constants.STATE_EDTI_COPY);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            seekTimeline(currentTimelinePosition, 0);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
                this.mOnTimelineChangeListener.refreshEditorTimelineView(2);
            }
        }
    }

    public void changeMirror(MeicamVideoClip meicamVideoClip) {
        NvsVideoClip nvsVideoClip;
        if (this.mTimeline != null && meicamVideoClip != null && (nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject()) != null) {
            long currentTimelinePosition = getCurrentTimelinePosition();
            int fxCount = nvsVideoClip.getFxCount();
            NvsVideoFx nvsVideoFx = null;
            int i = 0;
            while (true) {
                if (i >= fxCount) {
                    break;
                }
                NvsVideoFx fxByIndex = nvsVideoClip.getFxByIndex(i);
                if (fxByIndex.getAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_MIRROR) != null) {
                    nvsVideoFx = fxByIndex;
                    break;
                }
                i++;
            }
            if (nvsVideoFx == null) {
                MeicamVideoFx meicamVideoFx = new MeicamVideoFx();
                meicamVideoFx.setDesc("Transform 2D");
                meicamVideoFx.setFloatVal("Scale X", -1.0f);
                meicamVideoFx.setType("builtin");
                meicamVideoFx.bindToTimeline(nvsVideoClip);
                ((NvsVideoFx) meicamVideoFx.getObject()).setAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_MIRROR, "video_clip_mirror");
                meicamVideoClip.getVideoFxs().add(meicamVideoFx);
            } else {
                nvsVideoFx.setFloatVal("Scale X", nvsVideoFx.getFloatVal("Scale X") * -1.0d);
            }
            seekTimeline(currentTimelinePosition, 0);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onSaveOperation();
            }
        }
    }

    public void changeRotation(MeicamVideoClip meicamVideoClip) {
        if (meicamVideoClip != null && this.mTimeline != null) {
            long currentTimelinePosition = getCurrentTimelinePosition();
            NvsVideoClip nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject();
            if (nvsVideoClip != null) {
                int extraVideoRotation = nvsVideoClip.getExtraVideoRotation() - 1;
                nvsVideoClip.setExtraVideoRotation(extraVideoRotation);
                meicamVideoClip.setExtraRotation(extraVideoRotation);
                seekTimeline(currentTimelinePosition, 0);
                OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
                if (onTimelineChangeListener != null) {
                    onTimelineChangeListener.onSaveOperation();
                }
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void changeOpacity(MeicamVideoClip meicamVideoClip, float f) {
        if (this.mTimeline != null && meicamVideoClip != null) {
            long currentTimelinePosition = getCurrentTimelinePosition();
            NvsVideoClip nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject();
            if (nvsVideoClip != null) {
                float f2 = f / 100.0f;
                meicamVideoClip.setOpacity(f2);
                nvsVideoClip.setOpacity(f2);
                seekTimeline(currentTimelinePosition, 0);
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void deleteClip(MeicamVideoClip meicamVideoClip, int i, boolean z) {
        NvsVideoClip nvsVideoClip;
        NvsVideoTrack videoTrackByIndex;
        NvsVideoClip nvsVideoClip2;
        if (this.mTimeline != null && meicamVideoClip != null) {
            long currentTimelinePosition = getCurrentTimelinePosition();
            if (z) {
                List<ClipInfo> videoClipsInTrackIndex = TimelineDataUtil.getVideoClipsInTrackIndex(i);
                if (videoClipsInTrackIndex != null && videoClipsInTrackIndex.size() != 0 && (videoTrackByIndex = this.mTimeline.getVideoTrackByIndex(i)) != null && (nvsVideoClip2 = (NvsVideoClip) meicamVideoClip.getObject()) != null) {
                    videoTrackByIndex.removeClip(nvsVideoClip2.getIndex(), true);
                    videoClipsInTrackIndex.remove(meicamVideoClip);
                } else {
                    return;
                }
            } else {
                List<ClipInfo> mainTrackVideoClip = TimelineDataUtil.getMainTrackVideoClip();
                if (mainTrackVideoClip != null && mainTrackVideoClip.size() != 0) {
                    MeicamVideoClip mainTrackLastClip = TimelineDataUtil.getMainTrackLastClip();
                    if (mainTrackVideoClip.size() == 1 || (mainTrackVideoClip.size() == 2 && mainTrackLastClip.getVideoType().equals(CommonData.CLIP_HOLDER))) {
                        OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
                        if (onTimelineChangeListener != null) {
                            onTimelineChangeListener.onTimelineChangeShowToast(R.string.toast_least_one_material);
                            return;
                        }
                        return;
                    }
                    NvsVideoTrack videoTrackByIndex2 = this.mTimeline.getVideoTrackByIndex(0);
                    if (videoTrackByIndex2 != null && (nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject()) != null) {
                        long outPoint = nvsVideoClip.getOutPoint() - nvsVideoClip.getInPoint();
                        int indexOf = mainTrackVideoClip.indexOf(meicamVideoClip);
                        videoTrackByIndex2.removeClip(nvsVideoClip.getIndex(), false);
                        TimelineDataUtil.verifyTransition();
                        mainTrackVideoClip.remove(meicamVideoClip);
                        TimelineDataUtil.moveMainTrackClipsFromIndex(indexOf, -outPoint);
                        StateManager.getInstance().setCurrentState(Constants.STATE_MAIN_MENU);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            seekTimeline(currentTimelinePosition, 0);
            OnTimelineChangeListener onTimelineChangeListener2 = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener2 != null) {
                onTimelineChangeListener2.onTimelineChanged(this.mTimeline, true);
                this.mOnTimelineChangeListener.refreshEditorTimelineView(1);
                this.mOnTimelineChangeListener.onChangeBottomView();
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void changeRatio(int i) {
        int i2 = 960;
        int i3 = 720;
        if (i == 0) {
            Point originalRatio = getOriginalRatio();
            int i4 = originalRatio.x;
            i2 = originalRatio.y;
            i3 = i4;
        } else if (i == 1) {
            i2 = 720;
            i3 = 1280;
        } else if (i == 2) {
            i2 = 720;
        } else if (i == 4) {
            i2 = 1280;
        } else if (i == 8) {
            i2 = 720;
            i3 = 960;
        } else if (i != 16) {
            i2 = 0;
            i3 = 0;
        }
        this.mTimeline.changeVideoSize(i3, i2);
        NvsVideoResolution nvsVideoResolution = new NvsVideoResolution();
        nvsVideoResolution.imageHeight = i2;
        nvsVideoResolution.imageWidth = i3;
        TimelineData.getInstance().setVideoResolution(nvsVideoResolution);
        TimelineData.getInstance().setMakeRatio(i);
        this.mVideoFragment.changeRatioAddWaterToTimeline();
        this.mVideoFragment.setLiveWindowRatio();
        new Handler().postDelayed(new Runnable() {
            /* class com.meishe.myvideo.util.engine.EditorEngine.AnonymousClass1 */

            public void run() {
                EditorEngine.this.mVideoFragment.seekTimeline(EditorEngine.this.mStreamingContext.getTimelineCurrentPosition(EditorEngine.this.mTimeline), 0);
            }
        }, 100);
        OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
        if (onTimelineChangeListener != null) {
            onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
        }
    }

    private Point getOriginalRatio() {
        NvsSize videoStreamDimension = NvsStreamingContext.getInstance().getAVFileInfo(getEditVideoClip(0, 0).getFilePath()).getVideoStreamDimension(0);
        float f = (((float) videoStreamDimension.width) * 1.0f) / ((float) videoStreamDimension.height);
        Point point = new Point();
        if (f > 1.0f) {
            point.y = 720;
            point.x = (int) (((float) 720) * f);
        } else {
            point.x = 720;
            point.y = (int) ((((float) 720) * 1.0f) / f);
        }
        return point;
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void onAdjustDataChanged(Context context, MeicamVideoClip meicamVideoClip, int i, String str) {
        MeicamAdjustData meicamAdjustData;
        if (context != null) {
            NvsVideoClip nvsVideoClip = null;
            String currentState = StateManager.getInstance().getCurrentState();
            if (Constants.STATE_PIC_IN_PIC.equals(currentState)) {
                currentState = Constants.STATE_EDIT;
            }
            if (!Constants.STATE_EDIT.equals(currentState)) {
                meicamAdjustData = TimelineData.getInstance().getMeicamAdjustData();
                if (meicamAdjustData == null) {
                    meicamAdjustData = new MeicamAdjustData();
                }
            } else if (meicamVideoClip != null && (nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject()) != null) {
                meicamAdjustData = meicamVideoClip.getMeicamAdjustData();
                if (meicamAdjustData == null) {
                    meicamAdjustData = new MeicamAdjustData();
                }
            } else {
                return;
            }
            Resources resources = context.getResources();
            float f = (((float) i) / 50.0f) - 1.0f;
            Logger.d(TAG, "adjustData:" + f);
            if (str.equals(resources.getString(R.string.adjust_amount))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Amount", "Sharpen");
                } else {
                    setTimelineAdjustEffect(f, "Amount", "Sharpen");
                }
                meicamAdjustData.setAmount(f);
            } else if (str.equals(resources.getString(R.string.adjust_degree))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Degree", "Vignette");
                } else {
                    setTimelineAdjustEffect(f, "Degree", "Vignette");
                }
                meicamAdjustData.setDegree(f);
            } else if (str.equals(resources.getString(R.string.adjust_black_point))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Blackpoint", "BasicImageAdjust");
                } else {
                    setTimelineAdjustEffect(f, "Blackpoint", "BasicImageAdjust");
                }
                meicamAdjustData.setBlackPoint(f);
            } else if (str.equals(resources.getString(R.string.adjust_tint))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Tint", "Tint");
                } else {
                    setTimelineAdjustEffect(f, "Tint", "Tint");
                }
                meicamAdjustData.setTint(f);
            } else if (str.equals(resources.getString(R.string.adjust_temperature))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Temperature", "Tint");
                } else {
                    setTimelineAdjustEffect(f, "Temperature", "Tint");
                }
                meicamAdjustData.setTemperature(f);
            } else if (str.equals(resources.getString(R.string.adjust_shadow))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Shadow", "BasicImageAdjust");
                } else {
                    setTimelineAdjustEffect(f, "Shadow", "BasicImageAdjust");
                }
                meicamAdjustData.setShadow(f);
            } else if (str.equals(resources.getString(R.string.adjust_highlight))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Highlight", "BasicImageAdjust");
                } else {
                    setTimelineAdjustEffect(f, "Highlight", "BasicImageAdjust");
                }
                meicamAdjustData.setHighlight(f);
            } else if (str.equals(resources.getString(R.string.adjust_saturation))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Saturation", "BasicImageAdjust");
                } else {
                    setTimelineAdjustEffect(f, "Saturation", "BasicImageAdjust");
                }
                meicamAdjustData.setSaturation(f);
            } else if (str.equals(resources.getString(R.string.adjust_contrast))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Contrast", "BasicImageAdjust");
                } else {
                    setTimelineAdjustEffect(f, "Contrast", "BasicImageAdjust");
                }
                meicamAdjustData.setContrast(f);
            } else if (str.equals(resources.getString(R.string.adjust_brightness))) {
                if (Constants.STATE_EDIT.equals(currentState)) {
                    setAdjustEffect(nvsVideoClip, f, "Brightness", "BasicImageAdjust");
                } else {
                    setTimelineAdjustEffect(f, "Brightness", "BasicImageAdjust");
                }
                meicamAdjustData.setBrightness(f);
            }
            if (Constants.STATE_EDIT.equals(currentState)) {
                meicamVideoClip.setMeicamAdjustData(meicamAdjustData);
            } else {
                TimelineData.getInstance().setMeicamAdjustData(meicamAdjustData);
            }
            seekTimeline(getCurrentTimelinePosition(), 0);
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
            return;
        }
        NvsVideoFx appendBuiltinFx = nvsVideoClip.appendBuiltinFx(str2);
        appendBuiltinFx.setAttachment(str2, str2);
        appendBuiltinFx.setFloatVal(str, (double) f);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setTimelineAdjustEffect(float r10, java.lang.String r11, java.lang.String r12) {
        /*
            r9 = this;
            com.meicam.sdk.NvsTimeline r0 = r9.mTimeline
            com.meicam.sdk.NvsStreamingContext r1 = r9.mStreamingContext
            long r1 = r1.getTimelineCurrentPosition(r0)
            java.util.List r0 = r0.getTimelineVideoFxByTimelinePosition(r1)
            if (r0 == 0) goto L_0x002b
            r1 = 0
        L_0x000f:
            int r2 = r0.size()
            if (r1 >= r2) goto L_0x002b
            java.lang.Object r2 = r0.get(r1)
            com.meicam.sdk.NvsTimelineVideoFx r2 = (com.meicam.sdk.NvsTimelineVideoFx) r2
            java.lang.Object r3 = r2.getAttachment(r12)
            if (r3 == 0) goto L_0x0028
            boolean r3 = r12.equals(r3)
            if (r3 == 0) goto L_0x0028
            goto L_0x002c
        L_0x0028:
            int r1 = r1 + 1
            goto L_0x000f
        L_0x002b:
            r2 = 0
        L_0x002c:
            if (r2 == 0) goto L_0x0033
            double r0 = (double) r10
            r2.setFloatVal(r11, r0)
            goto L_0x0047
        L_0x0033:
            com.meicam.sdk.NvsTimeline r3 = r9.mTimeline
            r4 = 0
            long r6 = r3.getDuration()
            r8 = r12
            com.meicam.sdk.NvsTimelineVideoFx r0 = r3.addBuiltinTimelineVideoFx(r4, r6, r8)
            r0.setAttachment(r12, r12)
            double r1 = (double) r10
            r0.setFloatVal(r11, r1)
        L_0x0047:
            return
        */

//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.myvideo.util.engine.EditorEngine.setTimelineAdjustEffect(float, java.lang.String, java.lang.String):void");
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void handleEventMessage(BaseInfo baseInfo, int i) {
        if (i != 1) {
            if (i != 2) {
                switch (i) {
                    case 1001:
                    case 1002:
                        Logger.d(TAG, "添加贴纸");
                        addAnimatorSticker(baseInfo, i);
                        return;
                    case 1003:
                    case 1004:
                        int i2 = -1;
                        String str = null;
                        if (baseInfo instanceof WaterMarkInfo) {
                            WaterMarkInfo waterMarkInfo = (WaterMarkInfo) baseInfo;
                            i2 = waterMarkInfo.mPicType;
                            str = waterMarkInfo.mWaterMarkPath;
                        } else if (baseInfo instanceof WaterMarkEffectInfo) {
                            i2 = ((WaterMarkEffectInfo) baseInfo).mPicType;
                        }
                        addAnimatorWaterMark(str, i2);
                        return;
                    default:
                        return;
                }
            } else {
                List<MeicamTimelineVideoFxClip> meicamTimelineVideoFxClipList = TimelineData.getInstance().getMeicamTimelineVideoFxClipList();
                if (meicamTimelineVideoFxClipList.size() > 0) {
                    String desc = meicamTimelineVideoFxClipList.get(0).getDesc();
                    if (TextUtils.isEmpty(desc)) {
                        return;
                    }
                    if (desc.equals("Mosaic")) {
                        this.mVideoFragment.selectEffect(1);
                    } else {
                        this.mVideoFragment.selectEffect(2);
                    }
                } else {
                    this.mVideoFragment.selectEffect(1);
                }
            }
        } else if (!TextUtils.isEmpty(TimelineData.getInstance().getMeicamWaterMark().getWatermarkPath())) {
            this.mVideoFragment.selectWaterMark(0);
        }
    }

    public void addAnimatorWaterMark(String str, int i) {
        if (!TextUtils.isEmpty(str) || i != 0) {
            this.mVideoFragment.setEditMode(2);
            if (i == 0) {
                TimelineData.getInstance().getMeicamWaterMark().setWatermarkPath(str);
                this.mVideoFragment.setWaterMarkDrawableByPath(str, i);
            } else {
                this.mVideoFragment.setEffectByPoint(i);
            }
            if (i != 3) {
                this.mVideoFragment.setDrawRectVisible(true);
                this.mVideoFragment.refreshLiveWindowFrame();
            }
        }
    }

    private void addAnimatorSticker(BaseInfo baseInfo, int i) {
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        this.mStickerDuration = CommonData.DEFAULT_LENGTH;
        long duration = this.mTimeline.getDuration();
        long j = this.mStickerDuration + timelineCurrentPosition;
        if (j <= duration) {
            duration = j;
        }
        if (duration - timelineCurrentPosition < CommonData.MIN_SHOW_LENGTH_DURATION) {
            Log.e(TAG, "addAnimatorSticker: 空间太小了");
            return;
        }
        this.mVideoFragment.setEditMode(1);
        MeicamStickerClip meicamStickerClip = new MeicamStickerClip(baseInfo.getUuid());
        meicamStickerClip.setInPoint(timelineCurrentPosition);
        meicamStickerClip.setOutPoint(duration);
        meicamStickerClip.setIsCustomSticker(i == 1002);
        if (i == 1002) {
            meicamStickerClip.setCustomanimatedStickerImagePath(baseInfo.getFilePath());
        } else {
            meicamStickerClip.setCoverImagePath(baseInfo.getCoverUrl());
        }
        int trackIndex = getTrackIndex(meicamStickerClip.getInPoint(), meicamStickerClip.getOutPoint());
        Log.e(TAG, "addAnimatorSticker: " + trackIndex);
        meicamStickerClip.setzValue((float) trackIndex);
        NvsTimelineAnimatedSticker bindToTimeline = meicamStickerClip.bindToTimeline(this.mTimeline);
        if (bindToTimeline != null) {
            List<ClipInfo> stickerCaption = TimelineDataUtil.getStickerCaption();
            if (stickerCaption != null && stickerCaption.size() > 0) {
                List<PointF> boundingRectangleVertices = bindToTimeline.getBoundingRectangleVertices();
                if (boundingRectangleVertices != null && boundingRectangleVertices.size() >= 4) {
                    if (bindToTimeline.getHorizontalFlip()) {
                        Collections.swap(boundingRectangleVertices, 0, 3);
                        Collections.swap(boundingRectangleVertices, 1, 2);
                    }
                    PointF pointF = this.mVideoFragment.getAssetViewVerticesList(boundingRectangleVertices).get(0);
                    float f = pointF.x;
                    float f2 = pointF.y;
                    Random random = new Random();
                    int nextInt = random.nextInt(10);
                    int nextInt2 = random.nextInt(10);
                    boolean nextBoolean = random.nextBoolean();
                    boolean nextBoolean2 = random.nextBoolean();
                    float f3 = (((float) nextInt) / 10.0f) * f;
                    float f4 = (((float) nextInt2) / 10.0f) * f2;
                    if (!nextBoolean) {
                        f3 = -f3;
                    }
                    if (!nextBoolean2) {
                        f4 = -f4;
                    }
                    bindToTimeline.translateAnimatedSticker(new PointF(f3, f4));
                    meicamStickerClip.setTranslationX(f3);
                    meicamStickerClip.setTranslationY(f4);
                } else {
                    return;
                }
            }
            TimelineDataUtil.addStickerCaption(trackIndex, meicamStickerClip);
            this.mVideoFragment.setCurAnimateSticker(bindToTimeline);
            this.mVideoFragment.updateAnimateStickerCoordinate(bindToTimeline);
            updateStickerMuteVisible(bindToTimeline);
            this.mVideoFragment.setDrawRectVisible(true);
            seekTimeline(timelineCurrentPosition, 4);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onSaveOperation();
            }
        }
    }

    public void onHandActionDown(int i, boolean z) {
        NvsVideoClip clipByIndex;
        boolean z2 = false;
        NvsVideoTrack videoTrackByIndex = this.mTimeline.getVideoTrackByIndex(0);
        if (videoTrackByIndex != null && (clipByIndex = videoTrackByIndex.getClipByIndex(i)) != null) {
            long j = MAX_IMAGE_LENGTH;
            if (clipByIndex.getVideoType() == 0) {
                j = this.mStreamingContext.getAVFileInfo(clipByIndex.getFilePath()).getDuration();
            }
            if (clipByIndex.getVideoType() == 0 && z) {
                z2 = true;
                j = 0;
            }
            changeTrimPointOnTrimAdjust(clipByIndex, j, z2);
        }
    }

    public void onHandActionUp(int i, boolean z) {
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null) {
            boolean z2 = false;
            NvsVideoTrack videoTrackByIndex = nvsTimeline.getVideoTrackByIndex(0);
            if (videoTrackByIndex != null) {
                NvsVideoClip clipByIndex = videoTrackByIndex.getClipByIndex(i);
                long trimOut = clipByIndex.getTrimOut();
                if (clipByIndex.getVideoType() == 0 && z) {
                    trimOut = clipByIndex.getTrimIn();
                    z2 = true;
                }
                changeTrimPointOnTrimAdjust(clipByIndex, trimOut, z2);
            }
        }
    }

    public void onChangeTrimTimelineObject(int i, boolean z, long j) {
        NvsVideoClip clipByIndex;
        NvsVideoTrack videoTrackByIndex = this.mTimeline.getVideoTrackByIndex(0);
        if (!(videoTrackByIndex == null || (clipByIndex = videoTrackByIndex.getClipByIndex(i)) == null)) {
            Log.e(TAG, "onChangeTrimTimelineObject 111: " + this.mTimeline.getDuration());
            if (!z || clipByIndex.getVideoType() != 0) {
                setTrimOutPoint(clipByIndex, j, true);
            } else {
                setTrimInPoint(clipByIndex, j, true);
            }
            Log.e(TAG, "onChangeTrimTimelineObject 222: " + this.mTimeline.getDuration());
            List<ClipInfo> mainTrackVideoClip = TimelineDataUtil.getMainTrackVideoClip();
            for (int i2 = i + 1; i2 < mainTrackVideoClip.size(); i2++) {
                MeicamVideoClip meicamVideoClip = (MeicamVideoClip) mainTrackVideoClip.get(i2);
                if (meicamVideoClip != null) {
                    meicamVideoClip.setInPoint(((NvsVideoClip) meicamVideoClip.getObject()).getInPoint());
                    meicamVideoClip.setOutPoint(((NvsVideoClip) meicamVideoClip.getObject()).getOutPoint());
                }
            }
            Log.e(TAG, "onChangeTrimTimelineObject 333: " + this.mTimeline.getDuration());
        }
    }

    public void onChangeTrimTimelineData(int i, boolean z, long j) {
        double d;
        double d2;
        if (this.mTimeline != null) {
            List<ClipInfo> mainTrackVideoClip = TimelineDataUtil.getMainTrackVideoClip();
            MeicamVideoClip meicamVideoClip = null;
            if (CollectionUtils.isIndexAvailable(i, mainTrackVideoClip)) {
                meicamVideoClip = (MeicamVideoClip) mainTrackVideoClip.get(i);
            }
            meicamVideoClip.getTrimOut();
            meicamVideoClip.getTrimIn();
            meicamVideoClip.getSpeed();
            long trimIn = meicamVideoClip.getTrimIn();
            long trimOut = meicamVideoClip.getTrimOut();
            if (!z || !meicamVideoClip.getVideoType().equals(CommonData.CLIP_VIDEO)) {
                meicamVideoClip.setTrimOut(j);
            } else {
                meicamVideoClip.setTrimIn(j);
            }
            double inPoint = (double) meicamVideoClip.getInPoint();
            double trimOut2 = (double) (meicamVideoClip.getTrimOut() - meicamVideoClip.getTrimIn());
            double speed = meicamVideoClip.getSpeed();
            Double.isNaN(trimOut2);
            Double.isNaN(inPoint);
            meicamVideoClip.setOutPoint((long) (inPoint + (trimOut2 / speed)));
            if (mainTrackVideoClip.size() > i) {
                if (z) {
                    d = (double) (trimIn - meicamVideoClip.getTrimIn());
                    d2 = meicamVideoClip.getSpeed();
                    Double.isNaN(d);
                } else {
                    d = (double) (trimOut - meicamVideoClip.getTrimOut());
                    d2 = meicamVideoClip.getSpeed();
                    Double.isNaN(d);
                }
                long j2 = (long) (d / d2);
                while (true) {
                    i++;
                    if (i < mainTrackVideoClip.size()) {
                        ClipInfo clipInfo = mainTrackVideoClip.get(i);
                        if (clipInfo != null) {
                            clipInfo.setInPoint(clipInfo.getInPoint() - j2);
                            clipInfo.setOutPoint(clipInfo.getOutPoint() - j2);
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public long getAvFileDuration(int i, boolean z) {
        NvsVideoTrack videoTrackByIndex = this.mTimeline.getVideoTrackByIndex(0);
        if (videoTrackByIndex == null) {
            return 0;
        }
        NvsVideoClip clipByIndex = videoTrackByIndex.getClipByIndex(i);
        if (clipByIndex == null) {
            Log.e(TAG, "getAvFileDuration: getClipCount: " + videoTrackByIndex.getClipCount() + "  index: " + i);
            return 0;
        } else if (clipByIndex.getVideoType() == 0) {
            return this.mStreamingContext.getAVFileInfo(clipByIndex.getFilePath()).getDuration();
        } else {
            return MAX_IMAGE_LENGTH;
        }
    }

    public void setCompoundCaptionFont(NvsTimelineCompoundCaption nvsTimelineCompoundCaption, int i, String str) {
        if (!TextUtils.isEmpty(str)) {
            nvsTimelineCompoundCaption.setFontFamily(i, this.mStreamingContext.registerFontByFilePath("assets:/" + str));
        } else {
            nvsTimelineCompoundCaption.setFontFamily(i, str);
        }
        seekTimeline(2);
    }

    public void seekTimeline() {
        seekTimeline(0);
    }

    public void seekTimeline(int i) {
        seekTimeline(this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline), i);
    }

    public void seekTimeline(long j, int i) {
        this.mVideoFragment.seekTimeline(j, i);
    }

    public void closeOriginalVoice() {
        if (this.mTimeline != null) {
            List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
            if (!CollectionUtils.isEmpty(meicamVideoTrackList)) {
                meicamVideoTrackList.get(0).setIsMute(true);
            }
        }
    }

    public void openOriginalVoice() {
        NvsVideoTrack videoTrackByIndex;
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null && (videoTrackByIndex = nvsTimeline.getVideoTrackByIndex(0)) != null) {
            videoTrackByIndex.setVolumeGain(1.0f, 1.0f);
            List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
            if (!CollectionUtils.isEmpty(meicamVideoTrackList)) {
                meicamVideoTrackList.get(0).setIsMute(false);
            }
        }
    }

    public void ApplyEffect(BaseInfo baseInfo, int i, MYEditorTimeLine mYEditorTimeLine, MeicamVideoClip meicamVideoClip) {
        MeicamTransition meicamTransition;
        int i2 = baseInfo.mEffectType;
        if (i2 == 1) {
            TimelineData.getInstance().setMeicamTheme(new MeicamTheme(baseInfo.mPackageId));
            TimelineUtil.applyTheme(this.mTimeline);
            this.mVideoFragment.playVideoButtonClick();
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
            }
        } else if (i2 == 2) {
            String currentState = StateManager.getInstance().getCurrentState();
            MeicamVideoFx meicamVideoFx = new MeicamVideoFx();
            if (baseInfo.mEffectMode == BaseInfo.EFFECT_MODE_BUILTIN) {
                meicamVideoFx.setType("builtin");
                meicamVideoFx.setDesc(baseInfo.mName);
            } else {
                meicamVideoFx.setType("package");
                meicamVideoFx.setDesc(baseInfo.mPackageId);
            }
            meicamVideoFx.setIntensity(1.0f);
            if (Constants.STATE_EDIT.equals(currentState) || Constants.STATE_PIC_IN_PIC.equals(currentState)) {
                meicamVideoFx.setSubType(MeicamVideoFx.SUB_TYPE_CLIP_FILTER);
                TimelineUtil.appendFilterFx(meicamVideoClip, meicamVideoFx);
            } else if (Constants.STATE_FILTER.equals(currentState) || Constants.STATE_ADJUST.equals(currentState)) {
                meicamVideoFx.setSubType(MeicamVideoFx.SUB_TYPE_TIMELINE_FILTER);
                TimelineData.getInstance().setFilterFx(meicamVideoFx);
                TimelineUtil.buildTimelineFilter(this.mTimeline, meicamVideoFx);
            }
            MessageEvent.sendEvent(meicamVideoFx.getIntensity(), (int) MessageEvent.MESSAGE_TYPE_UPDATE_FILTER_PROGRESS);
            seekTimeline(0);
            OnTimelineChangeListener onTimelineChangeListener2 = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener2 != null) {
                onTimelineChangeListener2.onTimelineChanged(this.mTimeline, true);
            }
        } else if (i2 == 5 || i2 == 25 || i2 == 26) {
            if (baseInfo.mEffectMode == BaseInfo.EFFECT_MODE_BUILTIN) {
                meicamTransition = new MeicamTransition(i - 1, 0, ((TransitionInfo) baseInfo).getEffectName(), baseInfo.mIconRcsId);
            } else {
                meicamTransition = new MeicamTransition(i - 1, 1, baseInfo.getPackageId(), baseInfo.mIconUrl);
            }
            NvsVideoTransition bindToTimeline = meicamTransition.bindToTimeline((NvsVideoTrack) TimelineDataUtil.getMainTrack().getObject());
            if (bindToTimeline != null) {
                meicamTransition.loadData(bindToTimeline);
                TimelineDataUtil.addMainTransition(meicamTransition);
                playTransition(this.mTimeline, i - 1);
            }
            mYEditorTimeLine.refreshTimelineTransition(i, baseInfo);
        } else {
            switch (i2) {
                case 18:
                case 19:
                case 20:
                case 21:
                    MeicamTimelineVideoFxClip meicamTimelineVideoFxClip = new MeicamTimelineVideoFxClip();
                    if (baseInfo.mEffectMode == BaseInfo.EFFECT_MODE_BUILTIN) {
                        meicamTimelineVideoFxClip.setDesc(baseInfo.mName);
                        meicamTimelineVideoFxClip.setClipType(0);
                    } else {
                        meicamTimelineVideoFxClip.setDesc(baseInfo.mPackageId);
                        meicamTimelineVideoFxClip.setClipType(1);
                    }
                    meicamTimelineVideoFxClip.setDisplayName(baseInfo.mName);
                    long currentTimelinePosition = getCurrentTimelinePosition();
                    long removePreviewEffect = removePreviewEffect(currentTimelinePosition);
                    if (removePreviewEffect >= 0) {
                        currentTimelinePosition = removePreviewEffect;
                    }
                    meicamTimelineVideoFxClip.setInPoint(currentTimelinePosition);
                    meicamTimelineVideoFxClip.setOutPoint(currentTimelinePosition + getEffectDuration(currentTimelinePosition));
                    meicamTimelineVideoFxClip.setIntensity(1.0f);
                    if (meicamTimelineVideoFxClip.bindToTimeline(this.mTimeline) != null) {
                        List<MeicamTimelineVideoFxTrack> meicamTimelineVideoFxTrackList = TimelineData.getInstance().getMeicamTimelineVideoFxTrackList();
                        if (CollectionUtils.isEmpty(meicamTimelineVideoFxTrackList)) {
                            meicamTimelineVideoFxTrackList.add(new MeicamTimelineVideoFxTrack(0));
                        }
                        meicamTimelineVideoFxTrackList.get(0).getClipInfoList().add(meicamTimelineVideoFxClip);
                        seekTimeline(0);
                        OnTimelineChangeListener onTimelineChangeListener3 = this.mOnTimelineChangeListener;
                        if (onTimelineChangeListener3 != null) {
                            onTimelineChangeListener3.onTimelineChanged(this.mTimeline, true);
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void playTransition(NvsTimeline nvsTimeline, int i) {
        NvsVideoClip clipByIndex;
        if (nvsTimeline != null) {
            NvsVideoTrack videoTrackByIndex = nvsTimeline.getVideoTrackByIndex(0);
            int i2 = i + 1;
            if (videoTrackByIndex.getClipCount() >= i2 && (clipByIndex = videoTrackByIndex.getClipByIndex(i)) != null) {
                long inPoint = clipByIndex.getInPoint();
                long outPoint = clipByIndex.getOutPoint();
                NvsVideoClip clipByIndex2 = videoTrackByIndex.getClipByIndex(i2);
                if (clipByIndex2 != null) {
                    long inPoint2 = clipByIndex2.getInPoint();
                    long outPoint2 = clipByIndex2.getOutPoint();
                    long j = outPoint - 1000000;
                    long j2 = inPoint2 + 1000000;
                    if (j >= inPoint) {
                        inPoint = j;
                    }
                    if (j2 <= outPoint2) {
                        outPoint2 = j2;
                    }
                    this.mVideoFragment.playTransition(inPoint, outPoint2);
                }
            }
        }
    }

    public void changeCaptionText(String str) {
        NvsTimelineCaption curCaption = this.mVideoFragment.getCurCaption();
        if (curCaption == null) {
            addCaption(str);
        } else {
            updateCaption(curCaption, str);
        }
    }

    private void addCaption(String str) {
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        long j = CommonData.DEFAULT_LENGTH + timelineCurrentPosition;
        long duration = this.mTimeline.getDuration();
        if (j > duration) {
            j = duration;
        }
        if (j - timelineCurrentPosition < CommonData.MIN_SHOW_LENGTH_DURATION) {
            Log.e(TAG, "addCaption: 空间太小了");
            return;
        }
        MeicamCaptionClip meicamCaptionClip = new MeicamCaptionClip(str, null);
        meicamCaptionClip.setInPoint(timelineCurrentPosition);
        meicamCaptionClip.setOutPoint(j);
        int trackIndex = getTrackIndex(meicamCaptionClip.getInPoint(), meicamCaptionClip.getOutPoint());
        float f = (float) trackIndex;
        meicamCaptionClip.setzValue(f);
        NvsTimelineCaption bindToTimeline = meicamCaptionClip.bindToTimeline(this.mTimeline, false);
        if (bindToTimeline == null) {
            Logger.e(TAG, "addCaption:  添加字幕失败！");
            return;
        }
        bindToTimeline.setZValue(f);
        meicamCaptionClip.setObject(bindToTimeline);
        TimelineDataUtil.addStickerCaption(trackIndex, meicamCaptionClip);
        this.mVideoFragment.setCurCaption(bindToTimeline);
        this.mVideoFragment.updateCaptionCoordinate(bindToTimeline);
        this.mVideoFragment.setDrawRectVisible(true);
        this.mVideoFragment.setEditMode(0);
        seekTimeline(timelineCurrentPosition, 2);
        OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
        if (onTimelineChangeListener != null) {
            onTimelineChangeListener.onAddStickerCaption(meicamCaptionClip, 1);
        }
    }

    private void updateCaption(NvsTimelineCaption nvsTimelineCaption, String str) {
        if (!str.isEmpty()) {
            nvsTimelineCaption.setText(str);
            MeicamCaptionClip meicamCaptionClip = (MeicamCaptionClip) TimelineDataUtil.getStickerCaptionData((int) nvsTimelineCaption.getZValue(), nvsTimelineCaption.getInPoint());
            if (meicamCaptionClip == null) {
                Logger.e(TAG, "updateCaption clipInfo is null!");
                return;
            }
            meicamCaptionClip.loadData(nvsTimelineCaption);
            this.mVideoFragment.setCurCaption(nvsTimelineCaption);
            this.mVideoFragment.updateCaptionCoordinate(nvsTimelineCaption);
            this.mVideoFragment.setEditMode(0);
        } else if (TimelineDataUtil.removeStickerCaption((int) nvsTimelineCaption.getZValue(), nvsTimelineCaption.getInPoint())) {
            this.mTimeline.removeCaption(nvsTimelineCaption);
            this.mVideoFragment.setCurCaption(null);
            this.mVideoFragment.updateCaptionCoordinate(null);
            this.mVideoFragment.setDrawRectVisible(false);
            this.mVideoFragment.setEditMode(0);
        }
        seekTimeline(2);
    }

    public void updateEditorRectVisible(long j) {
        boolean z = this.mStreamingContext.getStreamingEngineState() == 0 || this.mStreamingContext.getStreamingEngineState() == 4;
        String currentState = StateManager.getInstance().getCurrentState();
        if (z && Constants.STATE_CAPTION.equals(currentState) && !hasCaptionInPosition(j)) {
            z = false;
        }
        this.mVideoFragment.setDrawRectVisible(z);
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void addCompoundCaption(BaseInfo baseInfo) {
        NvsTimelineCompoundCaption currCompoundCaption = this.mVideoFragment.getCurrCompoundCaption();
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        long duration = this.mTimeline.getDuration();
        long j = CommonData.DEFAULT_LENGTH + timelineCurrentPosition;
        if (j <= duration) {
            duration = j;
        }
        if (duration - timelineCurrentPosition < CommonData.MIN_SHOW_LENGTH_DURATION) {
            Log.e(TAG, "addCaption: 空间太小了");
            return;
        }
        if (currCompoundCaption != null) {
            TimelineDataUtil.removeStickerCaption((int) currCompoundCaption.getZValue(), currCompoundCaption.getInPoint());
            removeCaption(currCompoundCaption);
        }
        MeicamCompoundCaptionClip meicamCompoundCaptionClip = new MeicamCompoundCaptionClip(baseInfo.getAsset().uuid);
        meicamCompoundCaptionClip.setInPoint(timelineCurrentPosition);
        meicamCompoundCaptionClip.setOutPoint(duration);
        int trackIndex = getTrackIndex(meicamCompoundCaptionClip.getInPoint(), meicamCompoundCaptionClip.getOutPoint());
        meicamCompoundCaptionClip.setzValue((float) trackIndex);
        NvsTimelineCompoundCaption addCompoundCaptionFirst = meicamCompoundCaptionClip.addCompoundCaptionFirst(this.mTimeline, trackIndex);
        meicamCompoundCaptionClip.loadData(addCompoundCaptionFirst);
        if (addCompoundCaptionFirst == null) {
            Logger.e(TAG, "addCompoundCaption error caption is null");
            return;
        }
        TimelineDataUtil.addStickerCaption(trackIndex, meicamCompoundCaptionClip);
        this.mVideoFragment.setEditMode(4);
        this.mVideoFragment.setCurCompoundCaption(addCompoundCaptionFirst);
        this.mVideoFragment.updateCompoundCaptionCoordinate(addCompoundCaptionFirst);
        this.mVideoFragment.setDrawRectVisible(true);
        this.mVideoFragment.seekTimeline(timelineCurrentPosition, 2);
        OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
        if (onTimelineChangeListener != null) {
            onTimelineChangeListener.onAddStickerCaption(meicamCompoundCaptionClip, 2);
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void restoreTimeline(String str, NvsTimeline nvsTimeline) {
        TimelineData.getInstance().fromJson(str);
        TimelineUtil.rebuildTimeline(nvsTimeline);
        this.mTimeline = nvsTimeline;
        seekTimeline(0);
        this.mVideoFragment.setDrawRectVisible(false);
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void updateImageBackground(String str, int i) {
        String str2;
        if (!TextUtils.isEmpty(str)) {
            if (i == 0) {
                File file = new File(str);
                if (file.exists()) {
                    str = file.getName();
                    str2 = file.getParentFile().getAbsolutePath();
                } else {
                    return;
                }
            } else {
                str2 = "assets:/background/image";
            }
            addBackgroundStory(getEditVideoClip(getCurrentTimelinePosition(), 0), str, str2, i, 1.0f);
            seekTimeline(0);
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void updateBackground() {
        String str;
        MeicamVideoClip editVideoClip = getEditVideoClip(getCurrentTimelinePosition(), 0);
        if (editVideoClip != null) {
            NvsVideoResolution videoRes = this.mTimeline.getVideoRes();
            MeicamStoryboardInfo backgroundInfo = editVideoClip.getBackgroundInfo();
            if (backgroundInfo != null) {
                Map<String, Float> clipTrans = backgroundInfo.getClipTrans();
                if (((NvsVideoFx) backgroundInfo.getObject()) == null) {
                    setDefaultBackground(editVideoClip, videoRes.imageWidth, videoRes.imageHeight, clipTrans);
                    return;
                }
                if (((MeicamBackgroundStory) backgroundInfo).getBackgroundType() == 2) {
                    str = StoryboardUtil.getBlurBackgroundStory(videoRes.imageWidth, videoRes.imageHeight, editVideoClip.getFilePath(), backgroundInfo.getIntensity(), clipTrans);
                } else {
                    str = StoryboardUtil.getImageBackgroundStory(backgroundInfo.getSource(), videoRes.imageWidth, videoRes.imageHeight, clipTrans);
                }
                backgroundInfo.setStringVal("Description String", str);
                backgroundInfo.bindToTimelineByType((NvsVideoClip) editVideoClip.getObject(), backgroundInfo.getSubType());
                seekTimeline(0);
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void deleteBackground() {
        MeicamVideoClip editVideoClip = getEditVideoClip(getCurrentTimelinePosition(), 0);
        if (editVideoClip != null) {
            NvsVideoResolution videoRes = this.mTimeline.getVideoRes();
            MeicamStoryboardInfo backgroundInfo = editVideoClip.getBackgroundInfo();
            editVideoClip.removeBackground();
            setDefaultBackground(editVideoClip, videoRes.imageWidth, videoRes.imageHeight, backgroundInfo.getClipTrans());
            seekTimeline(0);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void updateBlurBackground(float f) {
        if (f == 0.0f) {
            deleteBackground();
            return;
        }
        addBackgroundStory(getEditVideoClip(getCurrentTimelinePosition(), 0), null, null, 2, f);
        seekTimeline(0);
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void applyAllBlurBackground(float f) {
        List<ClipInfo> clipInfoList = TimelineData.getInstance().getMeicamVideoTrackList().get(0).getClipInfoList();
        if (!CollectionUtils.isEmpty(clipInfoList)) {
            for (int i = 0; i < clipInfoList.size(); i++) {
                addBackgroundStory((MeicamVideoClip) clipInfoList.get(i), null, null, 2, f);
            }
            seekTimeline(0);
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void applyAllImageBackground(String str, int i) {
        String str2;
        if (!TextUtils.isEmpty(str)) {
            if (i == 0) {
                File file = new File(str);
                if (file.exists()) {
                    str = file.getName();
                    str2 = file.getParentFile().getAbsolutePath();
                } else {
                    return;
                }
            } else {
                str2 = "assets:/background/image";
            }
            List<ClipInfo> clipInfoList = TimelineData.getInstance().getMeicamVideoTrackList().get(0).getClipInfoList();
            if (!CollectionUtils.isEmpty(clipInfoList)) {
                for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                    addBackgroundStory((MeicamVideoClip) clipInfoList.get(i2), str, str2, i, 1.0f);
                }
                seekTimeline(0);
            }
        }
    }

    private void setDefaultBackground(MeicamVideoClip meicamVideoClip, int i, int i2, Map<String, Float> map) {
        MeicamBackgroundStory meicamBackgroundStory = new MeicamBackgroundStory();
        meicamBackgroundStory.setSource("nobackground.png");
        meicamBackgroundStory.setSourceDir("assets:/background");
        meicamBackgroundStory.setClipTrans(map);
        meicamBackgroundStory.setStringVal("Description String", StoryboardUtil.getImageBackgroundStory(meicamBackgroundStory.getSource(), i, i2, map));
        meicamBackgroundStory.setStringVal("Resource Dir", meicamBackgroundStory.getSourceDir());
        meicamBackgroundStory.setBooleanVal("No Background", true);
        meicamBackgroundStory.setBackgroundType(0);
        meicamBackgroundStory.bindToTimelineByType((NvsVideoClip) meicamVideoClip.getObject(), meicamBackgroundStory.getSubType());
        meicamVideoClip.addStoryboardInfo(meicamBackgroundStory.getSubType(), meicamBackgroundStory);
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void refreshData(BaseUIClip baseUIClip) {
        if (baseUIClip != null) {
            TimelineDataUtil.updateData(baseUIClip.getTrackIndex(), baseUIClip.getType());
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void audioEditCutClip(Context context) {
        NvsAudioTrack nvsAudioTrack = this.mNvsAudioTrack;
        if (nvsAudioTrack != null && this.mMeicamAudioClip != null) {
            int clipCount = nvsAudioTrack.getClipCount();
            long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
            NvsAudioClip nvsAudioClip = (NvsAudioClip) this.mMeicamAudioClip.getObject();
            if (nvsAudioClip != null) {
                this.mNvsAudioTrack.splitClip(nvsAudioClip.getIndex(), timelineCurrentPosition);
                if (this.mNvsAudioTrack.getClipCount() > clipCount) {
                    this.mMeicamAudioClip.setInPoint(nvsAudioClip.getInPoint());
                    this.mMeicamAudioClip.setOutPoint(nvsAudioClip.getOutPoint());
                    this.mMeicamAudioClip.setTrimIn(nvsAudioClip.getTrimIn());
                    this.mMeicamAudioClip.setTrimOut(nvsAudioClip.getTrimOut());
                    this.mMeicamAudioClip.loadData(nvsAudioClip);
                    NvsAudioClip clipByIndex = this.mNvsAudioTrack.getClipByIndex(nvsAudioClip.getIndex() + 1);
                    MeicamAudioClip meicamAudioClip = (MeicamAudioClip) this.mMeicamAudioClip.clone();
                    meicamAudioClip.setInPoint(clipByIndex.getInPoint());
                    meicamAudioClip.setOutPoint(clipByIndex.getOutPoint());
                    meicamAudioClip.setTrimIn(clipByIndex.getTrimIn());
                    meicamAudioClip.setTrimOut(clipByIndex.getTrimOut());
                    meicamAudioClip.loadData(clipByIndex);
                    TimelineDataUtil.addAudioClipInfoByTrackIndex(this.mNvsAudioTrack, meicamAudioClip);
                    this.mOnTrackChangeListener.audioEditCutClip(this.mNvsAudioTrack, meicamAudioClip.getInPoint());
                    return;
                }
                ToastUtil.showToast(context, context.getResources().getString(R.string.audio_unable_cut));
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void audioEditChangeClipSpeed(float f) {
        MeicamAudioClip meicamAudioClip = this.mMeicamAudioClip;
        if (meicamAudioClip != null) {
            meicamAudioClip.setSpeed((double) f);
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void audioEditChangeVolume(float f) {
        MeicamAudioClip meicamAudioClip = this.mMeicamAudioClip;
        if (meicamAudioClip != null) {
            meicamAudioClip.setVolume(f);
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void audioEditDeleteClip() {
        MeicamAudioClip meicamAudioClip;
        if (this.mNvsAudioTrack != null && (meicamAudioClip = this.mMeicamAudioClip) != null && meicamAudioClip.getObject() != null) {
            this.mNvsAudioTrack.removeClip(((NvsAudioClip) this.mMeicamAudioClip.getObject()).getIndex(), true);
            OnTrackChangeListener onTrackChangeListener = this.mOnTrackChangeListener;
            if (onTrackChangeListener != null) {
                onTrackChangeListener.audioEditDeleteClip();
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void audioEditCopyClip(Context context) {
        MeicamAudioClip meicamAudioClip;
        if (this.mNvsAudioTrack != null && (meicamAudioClip = this.mMeicamAudioClip) != null) {
            long outPoint = meicamAudioClip.getOutPoint() - this.mMeicamAudioClip.getInPoint();
            int audioTrackCount = this.mTimeline.audioTrackCount();
            if (audioTrackCount == 1) {
                NvsAudioTrack audioTrackByIndex = this.mTimeline.getAudioTrackByIndex(0);
                int index = this.mNvsAudioClip.getIndex();
                if (index < audioTrackByIndex.getClipCount()) {
                    NvsAudioClip clipByIndex = audioTrackByIndex.getClipByIndex(index);
                    NvsAudioClip clipByIndex2 = audioTrackByIndex.getClipByIndex(index + 1);
                    NvsAudioClip clipByIndex3 = audioTrackByIndex.getClipByIndex(audioTrackByIndex.getClipCount() - 1);
                    if (clipByIndex2 == null) {
                        if (clipByIndex3.getOutPoint() + outPoint > this.mTimeline.getDuration()) {
                            this.mOnTrackChangeListener.audioEditCopyClip(clipByIndex3.getInPoint(), this.mTimeline.appendAudioTrack());
                        } else {
                            this.mOnTrackChangeListener.audioEditCopyClip(clipByIndex3.getOutPoint(), audioTrackByIndex);
                        }
                    } else if (clipByIndex2.getInPoint() - audioTrackByIndex.getClipByIndex(index).getOutPoint() >= outPoint) {
                        this.mOnTrackChangeListener.audioEditCopyClip(clipByIndex.getOutPoint(), audioTrackByIndex);
                    } else {
                        this.mOnTrackChangeListener.audioEditCopyClip(this.mMeicamAudioClip.getOutPoint(), this.mTimeline.appendAudioTrack());
                    }
                }
            } else if (this.mNvsAudioClip.getIndex() == this.mNvsAudioTrack.getClipCount() - 1) {
                NvsAudioTrack audioTrackByIndex2 = this.mTimeline.getAudioTrackByIndex(selectAddTrackIndex());
                if (audioTrackByIndex2.getClipCount() != 0) {
                    NvsAudioClip clipByIndex4 = audioTrackByIndex2.getClipByIndex(audioTrackByIndex2.getClipCount() - 1);
                    if (clipByIndex4.getOutPoint() + outPoint <= this.mTimeline.getDuration()) {
                        this.mOnTrackChangeListener.audioEditCopyClip(clipByIndex4.getOutPoint(), audioTrackByIndex2);
                    } else {
                        this.mOnTrackChangeListener.audioEditCopyClip(this.mMeicamAudioClip.getInPoint(), this.mTimeline.appendAudioTrack());
                    }
                } else if (this.mNvsAudioClip.getOutPoint() + outPoint > this.mTimeline.getDuration()) {
                    this.mOnTrackChangeListener.audioEditCopyClip(this.mNvsAudioClip.getInPoint(), audioTrackByIndex2);
                } else {
                    this.mOnTrackChangeListener.audioEditCopyClip(this.mNvsAudioClip.getOutPoint(), audioTrackByIndex2);
                }
            } else if (audioTrackCount - 1 != this.mNvsAudioTrack.getIndex()) {
                int index2 = this.mNvsAudioTrack.getIndex();
                while (index2 < this.mTimeline.audioTrackCount()) {
                    if (this.mNvsAudioClip.getOutPoint() + outPoint <= this.mNvsAudioTrack.getClipByIndex(this.mNvsAudioClip.getIndex() + 1).getInPoint()) {
                        this.mOnTrackChangeListener.audioEditCopyClip(this.mNvsAudioClip.getOutPoint(), this.mNvsAudioTrack);
                        return;
                    }
                    index2++;
                    NvsAudioTrack audioTrackByIndex3 = this.mTimeline.getAudioTrackByIndex(index2);
                    if (audioTrackByIndex3 == null) {
                        this.mOnTrackChangeListener.audioEditCopyClip(this.mMeicamAudioClip.getOutPoint(), this.mTimeline.appendAudioTrack());
                        return;
                    } else if (audioTrackByIndex3.getClipCount() == 0) {
                        this.mOnTrackChangeListener.audioEditCopyClip(this.mMeicamAudioClip.getOutPoint(), audioTrackByIndex3);
                        return;
                    } else if (audioTrackByIndex3.getClipByIndex(audioTrackByIndex3.getClipCount() - 1).getOutPoint() <= this.mNvsAudioClip.getOutPoint()) {
                        this.mOnTrackChangeListener.audioEditCopyClip(this.mMeicamAudioClip.getOutPoint(), audioTrackByIndex3);
                        return;
                    } else if (audioTrackByIndex3.getClipByTimelinePosition(this.mNvsAudioClip.getOutPoint()) == null) {
                        int i = 0;
                        while (true) {
                            if (i >= audioTrackByIndex3.getClipCount()) {
                                continue;
                            }
                            NvsAudioClip clipByIndex5 = audioTrackByIndex3.getClipByIndex(i);
                            if (this.mNvsAudioClip.getOutPoint() >= clipByIndex5.getInPoint()) {
                                i++;
                            } else if (this.mNvsAudioClip.getOutPoint() + outPoint <= clipByIndex5.getInPoint()) {
                                this.mOnTrackChangeListener.audioEditCopyClip(this.mMeicamAudioClip.getOutPoint(), audioTrackByIndex3);
                                return;
                            }
                        }
                    }
                }
            } else if (this.mNvsAudioClip.getOutPoint() + outPoint <= this.mNvsAudioTrack.getClipByIndex(this.mNvsAudioClip.getIndex() + 1).getInPoint()) {
                this.mOnTrackChangeListener.audioEditCopyClip(this.mNvsAudioClip.getOutPoint(), this.mNvsAudioTrack);
            } else {
                this.mOnTrackChangeListener.audioEditCopyClip(this.mNvsAudioClip.getOutPoint(), this.mTimeline.appendAudioTrack());
            }
        }
    }

    private int selectAddTrackIndex() {
        long j = 0;
        int i = 0;
        for (int i2 = 0; i2 <= this.mNvsAudioClip.getIndex(); i2++) {
            NvsAudioTrack audioTrackByIndex = this.mTimeline.getAudioTrackByIndex(i2);
            if (audioTrackByIndex != null) {
                if (audioTrackByIndex.getClipCount() == 0) {
                    return audioTrackByIndex.getIndex();
                }
                if (audioTrackByIndex.getIndex() == 0) {
                    j = audioTrackByIndex.getClipByIndex(audioTrackByIndex.getClipCount() - 1).getOutPoint();
                }
                long outPoint = audioTrackByIndex.getClipByIndex(audioTrackByIndex.getClipCount() - 1).getOutPoint();
                if (outPoint < j) {
                    i = i2;
                }
                j = outPoint;
            }
        }
        return i;
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void audioEditChangeVoice(String str) {
        if (this.mMeicamAudioClip != null) {
            if (TextUtils.isEmpty(str)) {
                this.mMeicamAudioClip.setMeicaAudioFxes(null);
                return;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(new MeicamAudioFx(0, "builtin", str));
            this.mMeicamAudioClip.setMeicaAudioFxes(arrayList);
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void audioEditTransition(int i, int i2) {
        this.mMeicamAudioClip.setFadeInDuration(((long) i) * 1000000);
        this.mMeicamAudioClip.setFadeOutDuration(((long) i2) * 1000000);
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void deleteEffect(BaseUIClip baseUIClip) {
        if (baseUIClip == null) {
            Log.e(TAG, "deleteEffect: trackData is null!");
            return;
        }
        MeicamTimelineVideoFxTrack findEffectTrack = findEffectTrack(baseUIClip);
        MeicamTimelineVideoFxClip findEffectInTrack = findEffectInTrack(findEffectTrack, baseUIClip.getInPoint());
        if (findEffectInTrack != null) {
            Logger.d(TAG, "即将删除特效： getInPoint: " + findEffectInTrack.getInPoint() + "  getOutPoint: " + findEffectInTrack.getOutPoint() + " getDesc: " + findEffectInTrack.getDesc());
            this.mTimeline.removeTimelineVideoFx((NvsTimelineVideoFx) findEffectInTrack.getObject());
            findEffectTrack.getClipInfoList().remove(findEffectInTrack);
            seekTimeline(0);
        } else {
            Log.e(TAG, "deleteEffect: effectInTrack is null！");
        }
        OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
        if (onTimelineChangeListener != null) {
            onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
            this.mOnTimelineChangeListener.onChangeBottomView();
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void copyEffect(BaseUIClip baseUIClip, Context context) {
        MeicamTimelineVideoFxTrack findEffectTrack;
        MeicamTimelineVideoFxClip findEffectInTrack;
        MeicamTimelineVideoFxClip clone;
        if (baseUIClip != null && (findEffectInTrack = findEffectInTrack((findEffectTrack = findEffectTrack(baseUIClip)), baseUIClip.getInPoint())) != null && (clone = findEffectInTrack.clone()) != null) {
            long outPoint = findEffectInTrack.getOutPoint();
            long effectDuration = getEffectDuration(outPoint);
            if (effectDuration < CommonData.MIN_SHOW_LENGTH_DURATION) {
                OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
                if (onTimelineChangeListener != null) {
                    onTimelineChangeListener.onTimelineChangeShowToast(R.string.unusable_space);
                    return;
                }
                return;
            }
            long outPoint2 = findEffectInTrack.getOutPoint() - findEffectInTrack.getInPoint();
            if (effectDuration > outPoint2) {
                effectDuration = outPoint2;
            }
            clone.setInPoint(outPoint);
            clone.setOutPoint(outPoint + effectDuration);
            clone.bindToTimeline(this.mTimeline);
            List<ClipInfo> clipInfoList = findEffectTrack.getClipInfoList();
            clipInfoList.add(clipInfoList.indexOf(findEffectInTrack) + 1, clone);
            seekTimeline(0);
            OnTimelineChangeListener onTimelineChangeListener2 = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener2 != null) {
                onTimelineChangeListener2.onTimelineChanged(this.mTimeline, true);
                this.mOnTimelineChangeListener.onNeedTrackSelectChanged(0, clone.getInPoint());
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void changeClipFilter(MeicamVideoClip meicamVideoClip, float f) {
        if (meicamVideoClip != null) {
            meicamVideoClip.setFilterIntensity(f, MeicamVideoFx.SUB_TYPE_CLIP_FILTER);
            seekTimeline(0);
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void changeTimelineFilter(float f) {
        TimelineData.getInstance().getFilterFx().setIntensity(f);
        TimelineUtil.changeTimelineFilterIntensity(this.mTimeline, f);
        seekTimeline(0);
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void cloneFxTrack() {
        List<MeicamTimelineVideoFxTrack> meicamTimelineVideoFxTrackList = TimelineData.getInstance().getMeicamTimelineVideoFxTrackList();
        if (!CollectionUtils.isEmpty(meicamTimelineVideoFxTrackList)) {
            this.mVideoFxTrackClone = meicamTimelineVideoFxTrackList.get(0).clone();
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void removeFxTrack() {
        this.mVideoFxTrackClone = null;
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void removeClipFilter(long j, int i) {
        MeicamVideoClip editVideoClip = getEditVideoClip(j, i);
        if (editVideoClip != null) {
            editVideoClip.removeVideoFx(MeicamVideoFx.SUB_TYPE_CLIP_FILTER);
            seekTimeline(0);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void removeTimelineClipFilter() {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (!CollectionUtils.isEmpty(meicamVideoTrackList)) {
            for (MeicamVideoTrack meicamVideoTrack : meicamVideoTrackList) {
                List<ClipInfo> clipInfoList = meicamVideoTrack.getClipInfoList();
                if (!CollectionUtils.isEmpty(clipInfoList)) {
                    Iterator<ClipInfo> it = clipInfoList.iterator();
                    while (it.hasNext()) {
                        ((MeicamVideoClip) it.next()).removeVideoFx(MeicamVideoFx.SUB_TYPE_TIMELINE_FILTER);
                    }
                }
            }
            TimelineData.getInstance().setFilterFx(null);
            seekTimeline(0);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditOperater
    public void applyAllFilter(MeicamVideoClip meicamVideoClip) {
        TimelineUtil.buildTimelineFilter(this.mTimeline, meicamVideoClip.getVideoFx(MeicamVideoFx.SUB_TYPE_CLIP_FILTER));
        seekTimeline(0);
        OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
        if (onTimelineChangeListener != null) {
            onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
        }
    }

    private MeicamTimelineVideoFxClip findEffectInTrack(MeicamTimelineVideoFxTrack meicamTimelineVideoFxTrack, long j) {
        if (meicamTimelineVideoFxTrack == null) {
            return null;
        }
        List<ClipInfo> clipInfoList = meicamTimelineVideoFxTrack.getClipInfoList();
        if (CollectionUtils.isEmpty(clipInfoList)) {
            return null;
        }
        for (int i = 0; i < clipInfoList.size(); i++) {
            MeicamTimelineVideoFxClip meicamTimelineVideoFxClip = (MeicamTimelineVideoFxClip) clipInfoList.get(i);
            if (meicamTimelineVideoFxClip.getInPoint() == j) {
                return meicamTimelineVideoFxClip;
            }
        }
        return null;
    }

    private MeicamTimelineVideoFxTrack findEffectTrack(BaseUIClip baseUIClip) {
        List<MeicamTimelineVideoFxTrack> meicamTimelineVideoFxTrackList = TimelineData.getInstance().getMeicamTimelineVideoFxTrackList();
        if (CollectionUtils.isEmpty(meicamTimelineVideoFxTrackList)) {
            Log.e(TAG, "findEffectTrack: timelineVideoFxTrackList is empty!");
            return null;
        }
        int trackIndex = baseUIClip.getTrackIndex();
        if (CollectionUtils.isIndexAvailable(trackIndex, meicamTimelineVideoFxTrackList)) {
            return meicamTimelineVideoFxTrackList.get(trackIndex);
        }
        Log.e(TAG, "findEffectTrack: trackIndex is to Big. trackIndex: " + trackIndex + " timelineVideoFxTrackList.size: " + meicamTimelineVideoFxTrackList.size());
        return null;
    }

    private void addBackgroundStory(MeicamVideoClip meicamVideoClip, String str, String str2, int i, float f) {
        String str3;
        NvsVideoResolution videoRes = this.mTimeline.getVideoRes();
        if (meicamVideoClip != null) {
            MeicamStoryboardInfo backgroundInfo = meicamVideoClip.getBackgroundInfo();
            Map<String, Float> clipTrans = backgroundInfo.getClipTrans();
            if (i == 2) {
                str3 = StoryboardUtil.getBlurBackgroundStory(videoRes.imageWidth, videoRes.imageHeight, meicamVideoClip.getFilePath(), f, clipTrans);
            } else {
                str3 = StoryboardUtil.getImageBackgroundStory(str, videoRes.imageWidth, videoRes.imageHeight, clipTrans);
            }
            Logger.d(TAG, "updateImageBackground: = " + str3);
            if (!TextUtils.isEmpty(str2)) {
                backgroundInfo.setStringVal("Resource Dir", str2);
            }
            backgroundInfo.setBooleanVal("No Background", true);
            backgroundInfo.setStringVal("Description String", str3);
            backgroundInfo.setStoryDesc(str3);
            backgroundInfo.setSubType(MeicamStoryboardInfo.SUB_TYPE_BACKGROUND);
            ((MeicamBackgroundStory) backgroundInfo).setBackgroundType(i);
            backgroundInfo.setIntensity(f);
            backgroundInfo.setSource(str);
            backgroundInfo.setSourceDir(str2);
            backgroundInfo.bindToTimelineByType((NvsVideoClip) meicamVideoClip.getObject(), backgroundInfo.getSubType());
            if (meicamVideoClip != null) {
                meicamVideoClip.addStoryboardInfo(backgroundInfo.getSubType(), backgroundInfo);
            }
        }
    }

    private NvsVideoFx getBackgroundFx(NvsVideoClip nvsVideoClip) {
        int fxCount = nvsVideoClip.getFxCount();
        for (int i = 0; i < fxCount; i++) {
            NvsVideoFx fxByIndex = nvsVideoClip.getFxByIndex(i);
            if (fxByIndex.getAttachment(ATTACHMENT_KEY) != null) {
                return fxByIndex;
            }
        }
        return null;
    }

    private boolean hasCaptionInPosition(long j) {
        List<NvsTimelineCaption> captionsByTimelinePosition = this.mTimeline.getCaptionsByTimelinePosition(j);
        return captionsByTimelinePosition != null && captionsByTimelinePosition.size() > 0;
    }

    private int getTrackIndex(long j, long j2) {
        long duration = this.mTimeline.getDuration();
        List<MeicamStickerCaptionTrack> meicamStickerCaptionTrackList = TimelineData.getInstance().getMeicamStickerCaptionTrackList();
        for (MeicamStickerCaptionTrack meicamStickerCaptionTrack : meicamStickerCaptionTrackList) {
            int index = meicamStickerCaptionTrack.getIndex();
            List<ClipInfo> clipInfoList = meicamStickerCaptionTrack.getClipInfoList();
            if (clipInfoList.isEmpty()) {
                return index;
            }
            int i = 0;
            while (true) {
                if (i < clipInfoList.size()) {
                    ClipInfo clipInfo = clipInfoList.get(i);
                    long inPoint = clipInfo.getInPoint();
                    long outPoint = clipInfo.getOutPoint();
                    long j3 = 0;
                    if (i > 0) {
                        j3 = clipInfoList.get(i - 1).getOutPoint();
                    }
                    if (j > j3 && j2 < inPoint) {
                        return index;
                    }
                    if (i < clipInfoList.size() - 1) {
                        long inPoint2 = clipInfoList.get(i + 1).getInPoint();
                        if (j > outPoint && j2 < inPoint2) {
                            return index;
                        }
                    } else if (j > outPoint && j2 < duration) {
                        return index;
                    }
                    i++;
                }
            }
        }
        return meicamStickerCaptionTrackList.size();
    }

    public int getPipVideoClipIndex(long j, long j2) {
        long duration = this.mTimeline.getDuration();
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        for (int i = 0; i < meicamVideoTrackList.size(); i++) {
            if (i != 0) {
                MeicamVideoTrack meicamVideoTrack = meicamVideoTrackList.get(i);
                int index = meicamVideoTrack.getIndex();
                List<ClipInfo> clipInfoList = meicamVideoTrack.getClipInfoList();
                if (clipInfoList.isEmpty()) {
                    return index;
                }
                for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                    ClipInfo clipInfo = clipInfoList.get(i2);
                    long inPoint = clipInfo.getInPoint();
                    long outPoint = clipInfo.getOutPoint();
                    long j3 = 0;
                    if (i2 > 0) {
                        j3 = clipInfoList.get(i2 - 1).getOutPoint();
                    }
                    if (j > j3 && j2 < inPoint) {
                        return index;
                    }
                    if (i2 < clipInfoList.size() - 1) {
                        long inPoint2 = clipInfoList.get(i2 + 1).getInPoint();
                        if (j > outPoint && j2 < inPoint2) {
                            return index;
                        }
                    } else if (j > outPoint && j2 < duration) {
                        return index;
                    }
                }
                continue;
            }
        }
        return meicamVideoTrackList.size();
    }

    public void removeCaption(MeicamCaptionClip meicamCaptionClip) {
        if (meicamCaptionClip != null) {
            Logger.d(TAG, "removeCaption: " + meicamCaptionClip.getzValue() + " " + meicamCaptionClip.getInPoint());
            NvsTimelineCaption nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject();
            if (nvsTimelineCaption != null) {
                if (!TimelineDataUtil.removeStickerCaption((int) nvsTimelineCaption.getZValue(), nvsTimelineCaption.getInPoint())) {
                    Logger.e(TAG, "removeStickerCaption fail");
                    return;
                }
                this.mTimeline.removeCaption(nvsTimelineCaption);
                this.mVideoFragment.setCurCaption(null);
                this.mVideoFragment.setDrawRectVisible(false);
                seekTimeline(this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline), 2);
                OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
                if (onTimelineChangeListener != null) {
                    onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
                    this.mOnTimelineChangeListener.onChangeBottomView();
                }
            }
        }
    }

    private void removeCaption(NvsTimelineCompoundCaption nvsTimelineCompoundCaption) {
        this.mTimeline.removeCompoundCaption(nvsTimelineCompoundCaption);
    }

    private void updateStickerMuteVisible(NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker) {
        if (nvsTimelineAnimatedSticker != null) {
            boolean hasAudio = nvsTimelineAnimatedSticker.hasAudio();
            this.mVideoFragment.setMuteVisible(hasAudio);
            if (hasAudio) {
                this.mVideoFragment.setStickerMuteIndex(((float) ((int) nvsTimelineAnimatedSticker.getVolumeGain().leftVolume)) > 0.0f ? 0 : 1);
            }
        }
    }

    private float getCurAnimateStickerZVal() {
        NvsTimelineAnimatedSticker firstAnimatedSticker = this.mTimeline.getFirstAnimatedSticker();
        float f = 0.0f;
        while (firstAnimatedSticker != null) {
            float zValue = firstAnimatedSticker.getZValue();
            if (zValue > f) {
                f = zValue;
            }
            firstAnimatedSticker = this.mTimeline.getNextAnimatedSticker(firstAnimatedSticker);
        }
        double d = (double) f;
        Double.isNaN(d);
        return (float) (d + 1.0d);
    }

    public VideoFragment getVideoFragment() {
        return this.mVideoFragment;
    }

    public void handleCaptionColorOpacityAndWidth(MeicamCaptionClip meicamCaptionClip, int i, int i2) {
        NvsTimelineCaption nvsTimelineCaption;
        if (meicamCaptionClip != null && (nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject()) != null) {
            switch (i2) {
                case 1008:
                    nvsTimelineCaption.setDrawOutline(true);
                    NvsColor outlineColor = nvsTimelineCaption.getOutlineColor();
                    outlineColor.a = ((float) i) / 100.0f;
                    nvsTimelineCaption.setOutlineColor(outlineColor);
                    meicamCaptionClip.setOutlineColor(ColorUtil.getColorArray(outlineColor));
                    break;
                case 1009:
                    nvsTimelineCaption.setDrawOutline(true);
                    float f = (float) (i / 10);
                    nvsTimelineCaption.setOutlineWidth(f);
                    meicamCaptionClip.setOutlineWidth(f);
                    break;
                case 1010:
                    NvsColor textColor = nvsTimelineCaption.getTextColor();
                    textColor.a = ((float) i) / 100.0f;
                    nvsTimelineCaption.setTextColor(textColor);
                    meicamCaptionClip.setTextColor(ColorUtil.getColorArray(textColor));
                    break;
            }
            seekTimeline(2);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onSaveOperation();
            }
        }
    }

    public void handleCaptionFont(MeicamCaptionClip meicamCaptionClip, String str, int i) {
        NvsTimelineCaption nvsTimelineCaption;
        if (meicamCaptionClip != null && (nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject()) != null) {
            if (!TextUtils.isEmpty(str)) {
                String registerFontByFilePath = this.mStreamingContext.registerFontByFilePath("assets:/" + str);
                nvsTimelineCaption.setFontFamily(registerFontByFilePath);
                meicamCaptionClip.setFont(registerFontByFilePath);
            } else {
                nvsTimelineCaption.setFontFamily(str);
                meicamCaptionClip.setFont(str);
            }
            seekTimeline(2);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onSaveOperation();
            }
        }
    }

    public void handleCaptionOtherSetting(MeicamCaptionClip meicamCaptionClip, boolean z, int i) {
        NvsTimelineCaption nvsTimelineCaption;
        if (meicamCaptionClip != null && (nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject()) != null) {
            switch (i) {
                case 1012:
                    nvsTimelineCaption.setBold(z);
                    meicamCaptionClip.setBold(z);
                    break;
                case 1013:
                    nvsTimelineCaption.setItalic(z);
                    meicamCaptionClip.setItalic(z);
                    break;
                case 1014:
                    nvsTimelineCaption.setDrawShadow(z);
                    meicamCaptionClip.setShadow(z);
                    break;
            }
            seekTimeline(2);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onSaveOperation();
            }
        }
    }

    public void handleCaptionLetterSpace(MeicamCaptionClip meicamCaptionClip, float f, int i) {
        NvsTimelineCaption nvsTimelineCaption;
        if (meicamCaptionClip != null && (nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject()) != null && i == 1015) {
            nvsTimelineCaption.setLetterSpacing(f);
            this.mVideoFragment.setCurCaption(nvsTimelineCaption);
            this.mVideoFragment.updateCaptionCoordinate(nvsTimelineCaption);
            meicamCaptionClip.setLetterSpacing(f);
            seekTimeline(2);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onSaveOperation();
            }
        }
    }

    public void handleCaptionPosition(MeicamCaptionClip meicamCaptionClip, int i, int i2) {
        NvsTimelineCaption nvsTimelineCaption;
        if (meicamCaptionClip != null && (nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject()) != null && i2 == 1016) {
            if (i == 0) {
                List<PointF> boundingRectangleVertices = nvsTimelineCaption.getBoundingRectangleVertices();
                if (boundingRectangleVertices != null && boundingRectangleVertices.size() >= 4) {
                    Collections.sort(boundingRectangleVertices, new Util.PointXComparator());
                    float f = -(((float) (this.mTimeline.getVideoRes().imageWidth / 2)) + boundingRectangleVertices.get(0).x);
                    nvsTimelineCaption.translateCaption(new PointF(f, 0.0f));
                    meicamCaptionClip.setTranslationX(f);
                    meicamCaptionClip.setTranslationY(0.0f);
                } else {
                    return;
                }
            } else if (i == 1) {
                List<PointF> boundingRectangleVertices2 = nvsTimelineCaption.getBoundingRectangleVertices();
                if (boundingRectangleVertices2 != null && boundingRectangleVertices2.size() >= 4) {
                    Collections.sort(boundingRectangleVertices2, new Util.PointXComparator());
                    float f2 = -(((boundingRectangleVertices2.get(3).x - boundingRectangleVertices2.get(0).x) / 2.0f) + boundingRectangleVertices2.get(0).x);
                    nvsTimelineCaption.translateCaption(new PointF(f2, 0.0f));
                    meicamCaptionClip.setTranslationX(f2);
                    meicamCaptionClip.setTranslationY(0.0f);
                } else {
                    return;
                }
            } else if (i == 2) {
                List<PointF> boundingRectangleVertices3 = nvsTimelineCaption.getBoundingRectangleVertices();
                if (boundingRectangleVertices3 != null && boundingRectangleVertices3.size() >= 4) {
                    Collections.sort(boundingRectangleVertices3, new Util.PointXComparator());
                    float f3 = ((float) (this.mTimeline.getVideoRes().imageWidth / 2)) - boundingRectangleVertices3.get(3).x;
                    nvsTimelineCaption.translateCaption(new PointF(f3, 0.0f));
                    meicamCaptionClip.setTranslationX(f3);
                    meicamCaptionClip.setTranslationY(0.0f);
                } else {
                    return;
                }
            } else if (i == 3) {
                List<PointF> boundingRectangleVertices4 = nvsTimelineCaption.getBoundingRectangleVertices();
                if (boundingRectangleVertices4 != null && boundingRectangleVertices4.size() >= 4) {
                    Collections.sort(boundingRectangleVertices4, new Util.PointYComparator());
                    float f4 = (((float) (this.mTimeline.getVideoRes().imageHeight / 2)) - boundingRectangleVertices4.get(0).y) - (boundingRectangleVertices4.get(3).y - boundingRectangleVertices4.get(0).y);
                    nvsTimelineCaption.translateCaption(new PointF(0.0f, f4));
                    meicamCaptionClip.setTranslationX(0.0f);
                    meicamCaptionClip.setTranslationY(f4);
                } else {
                    return;
                }
            } else if (i == 4) {
                List<PointF> boundingRectangleVertices5 = nvsTimelineCaption.getBoundingRectangleVertices();
                if (boundingRectangleVertices5 != null && boundingRectangleVertices5.size() >= 4) {
                    Collections.sort(boundingRectangleVertices5, new Util.PointYComparator());
                    float f5 = -(((boundingRectangleVertices5.get(3).y - boundingRectangleVertices5.get(0).y) / 2.0f) + boundingRectangleVertices5.get(0).y);
                    nvsTimelineCaption.translateCaption(new PointF(0.0f, f5));
                    meicamCaptionClip.setTranslationX(0.0f);
                    meicamCaptionClip.setTranslationY(f5);
                } else {
                    return;
                }
            } else if (i == 5) {
                List<PointF> boundingRectangleVertices6 = nvsTimelineCaption.getBoundingRectangleVertices();
                if (boundingRectangleVertices6 != null && boundingRectangleVertices6.size() >= 4) {
                    Collections.sort(boundingRectangleVertices6, new Util.PointYComparator());
                    float f6 = -((((float) (this.mTimeline.getVideoRes().imageHeight / 2)) + boundingRectangleVertices6.get(3).y) - (boundingRectangleVertices6.get(3).y - boundingRectangleVertices6.get(0).y));
                    nvsTimelineCaption.translateCaption(new PointF(0.0f, f6));
                    meicamCaptionClip.setTranslationX(0.0f);
                    meicamCaptionClip.setTranslationY(f6);
                } else {
                    return;
                }
            }
            this.mVideoFragment.setCurCaption(nvsTimelineCaption);
            this.mVideoFragment.updateCaptionCoordinate(nvsTimelineCaption);
            seekTimeline(2);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onSaveOperation();
            }
        }
    }

    public void deleteSticker(MeicamStickerClip meicamStickerClip) {
        NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker;
        if (meicamStickerClip != null && (nvsTimelineAnimatedSticker = (NvsTimelineAnimatedSticker) meicamStickerClip.getObject()) != null) {
            this.mTimeline.removeAnimatedSticker(nvsTimelineAnimatedSticker);
            this.mVideoFragment.setCurAnimateSticker(null);
            this.mVideoFragment.setDrawRectVisible(false);
            TimelineDataUtil.removeStickerCaption((int) nvsTimelineAnimatedSticker.getZValue(), meicamStickerClip.getInPoint());
            seekTimeline(this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline), 4);
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
                this.mOnTimelineChangeListener.onChangeBottomView();
            }
        }
    }

    public void removeCompoundCaption(MeicamCompoundCaptionClip meicamCompoundCaptionClip) {
        if (meicamCompoundCaptionClip != null) {
            Log.e(TAG, "removeCompoundCaption: getzValue: " + meicamCompoundCaptionClip.getzValue() + " getInPoint: " + meicamCompoundCaptionClip.getInPoint());
            NvsTimelineCompoundCaption nvsTimelineCompoundCaption = (NvsTimelineCompoundCaption) meicamCompoundCaptionClip.getObject();
            if (nvsTimelineCompoundCaption != null) {
                TimelineDataUtil.removeStickerCaption((int) meicamCompoundCaptionClip.getzValue(), meicamCompoundCaptionClip.getInPoint());
                this.mTimeline.removeCompoundCaption(nvsTimelineCompoundCaption);
                this.mVideoFragment.setCurCompoundCaption(null);
                this.mVideoFragment.updateCompoundCaptionCoordinate(null);
                this.mVideoFragment.setDrawRectVisible(false);
                seekTimeline(2);
                OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
                if (onTimelineChangeListener != null) {
                    onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
                    this.mOnTimelineChangeListener.onChangeBottomView();
                }
            }
        }
    }

    public boolean haveEffectInThisPosition() {
        long currentTimelinePosition = getCurrentTimelinePosition();
        MeicamTimelineVideoFxTrack meicamTimelineVideoFxTrack = this.mVideoFxTrackClone;
        if (meicamTimelineVideoFxTrack == null) {
            return false;
        }
        List<ClipInfo> clipInfoList = meicamTimelineVideoFxTrack.getClipInfoList();
        if (CollectionUtils.isEmpty(clipInfoList)) {
            return false;
        }
        for (ClipInfo clipInfo : clipInfoList) {
            if (clipInfo.getInPoint() <= currentTimelinePosition && clipInfo.getOutPoint() >= currentTimelinePosition) {
                return true;
            }
        }
        return false;
    }

    public long getEffectDuration(long j) {
        NvsTimelineVideoFx firstTimelineVideoFx = this.mTimeline.getFirstTimelineVideoFx();
        long j2 = CommonData.DEFAULT_LENGTH + j;
        while (true) {
            if (firstTimelineVideoFx == null) {
                break;
            }
            long inPoint = firstTimelineVideoFx.getInPoint();
            if (j >= inPoint) {
                firstTimelineVideoFx = this.mTimeline.getNextTimelineVideoFx(firstTimelineVideoFx);
            } else if (j2 > inPoint) {
                j2 = inPoint;
            }
        }
        if (j2 > this.mTimeline.getDuration()) {
            j2 = this.mTimeline.getDuration();
        }
        return j2 - j;
    }

    public long removePreviewEffect(long j) {
        List<MeicamTimelineVideoFxTrack> meicamTimelineVideoFxTrackList = TimelineData.getInstance().getMeicamTimelineVideoFxTrackList();
        if (CollectionUtils.isEmpty(meicamTimelineVideoFxTrackList)) {
            return -1;
        }
        List<ClipInfo> clipInfoList = meicamTimelineVideoFxTrackList.get(0).getClipInfoList();
        if (CollectionUtils.isEmpty(clipInfoList)) {
            return -1;
        }
        for (ClipInfo clipInfo : clipInfoList) {
            if (clipInfo.getInPoint() <= j && clipInfo.getOutPoint() >= j) {
                this.mTimeline.removeTimelineVideoFx((NvsTimelineVideoFx) ((MeicamTimelineVideoFxClip) clipInfo).getObject());
                clipInfoList.remove(clipInfo);
                return clipInfo.getInPoint();
            }
        }
        return -1;
    }

    public MeicamCaptionClip copyCaption(MeicamCaptionClip meicamCaptionClip) {
        Logger.d(TAG, "复制字幕");
        if (meicamCaptionClip == null) {
            return null;
        }
        MeicamCaptionClip meicamCaptionClip2 = (MeicamCaptionClip) meicamCaptionClip.clone();
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        long outPoint = (meicamCaptionClip.getOutPoint() - meicamCaptionClip.getInPoint()) + timelineCurrentPosition;
        if (outPoint > this.mTimeline.getDuration()) {
            outPoint = this.mTimeline.getDuration();
        }
        meicamCaptionClip2.setInPoint(timelineCurrentPosition);
        meicamCaptionClip2.setOutPoint(outPoint);
        int trackIndex = getTrackIndex(meicamCaptionClip2.getInPoint(), meicamCaptionClip2.getOutPoint());
        meicamCaptionClip2.setzValue((float) trackIndex);
        meicamCaptionClip2.bindToTimeline(this.mTimeline);
        TimelineDataUtil.addStickerCaption(trackIndex, meicamCaptionClip2);
        seekTimeline(2);
        OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
        if (onTimelineChangeListener != null) {
            onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
        }
        return meicamCaptionClip2;
    }

    public MeicamStickerClip copySticker(MeicamStickerClip meicamStickerClip) {
        Logger.d(TAG, "copySticker");
        if (meicamStickerClip == null) {
            Logger.e(TAG, "copySticker currSelectedSticker is null !");
            return null;
        }
        MeicamStickerClip meicamStickerClip2 = (MeicamStickerClip) meicamStickerClip.clone();
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        long outPoint = (meicamStickerClip.getOutPoint() - meicamStickerClip.getInPoint()) + timelineCurrentPosition;
        if (outPoint > this.mTimeline.getDuration()) {
            outPoint = this.mTimeline.getDuration();
        }
        meicamStickerClip2.setInPoint(timelineCurrentPosition);
        meicamStickerClip2.setOutPoint(outPoint);
        int trackIndex = getTrackIndex(meicamStickerClip2.getInPoint(), meicamStickerClip2.getOutPoint());
        meicamStickerClip2.setzValue((float) trackIndex);
        meicamStickerClip2.bindToTimeline(this.mTimeline);
        TimelineDataUtil.addStickerCaption(trackIndex, meicamStickerClip2);
        seekTimeline(0);
        OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
        if (onTimelineChangeListener != null) {
            onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
        }
        return meicamStickerClip2;
    }

    public MeicamCompoundCaptionClip copyComCaption(MeicamCompoundCaptionClip meicamCompoundCaptionClip) {
        if (meicamCompoundCaptionClip == null) {
            Logger.e(TAG, "copyComCaption currCompoundCaption is null !");
            return null;
        }
        MeicamCompoundCaptionClip meicamCompoundCaptionClip2 = (MeicamCompoundCaptionClip) meicamCompoundCaptionClip.clone();
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        long outPoint = (meicamCompoundCaptionClip.getOutPoint() - meicamCompoundCaptionClip.getInPoint()) + timelineCurrentPosition;
        if (outPoint > this.mTimeline.getDuration()) {
            outPoint = this.mTimeline.getDuration();
        }
        meicamCompoundCaptionClip2.setInPoint(timelineCurrentPosition);
        meicamCompoundCaptionClip2.setOutPoint(outPoint);
        int trackIndex = getTrackIndex(meicamCompoundCaptionClip2.getInPoint(), meicamCompoundCaptionClip2.getOutPoint());
        meicamCompoundCaptionClip2.setzValue((float) trackIndex);
        meicamCompoundCaptionClip2.bindToTimeline(this.mTimeline);
        TimelineDataUtil.addStickerCaption(trackIndex, meicamCompoundCaptionClip2);
        seekTimeline(0);
        OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
        if (onTimelineChangeListener != null) {
            onTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
        }
        return meicamCompoundCaptionClip2;
    }

    public void freezeFrameClip(MeicamVideoClip meicamVideoClip, int i, boolean z) {
        NvsVideoClip nvsVideoClip;
        Bitmap frameAtTime;
        MeicamVideoTrack meicamVideoTrack;
        OnTimelineChangeListener onTimelineChangeListener;
        if (this.mTimeline != null && meicamVideoClip != null) {
            NvsVideoClip nvsVideoClip2 = (NvsVideoClip) meicamVideoClip.getObject();
            boolean videoReverse = meicamVideoClip.getVideoReverse();
            long currentTimelinePosition = getCurrentTimelinePosition();
            if ((currentTimelinePosition <= nvsVideoClip2.getInPoint() || currentTimelinePosition >= nvsVideoClip2.getOutPoint()) && (onTimelineChangeListener = this.mOnTimelineChangeListener) != null) {
                onTimelineChangeListener.onTimelineChangeShowToast(R.string.current_position_not_allow_freeze);
                return;
            }
            NvsVideoTrack videoTrackByIndex = this.mTimeline.getVideoTrackByIndex(i);
            if (videoTrackByIndex != null && (nvsVideoClip = (NvsVideoClip) meicamVideoClip.getObject()) != null) {
                int index = nvsVideoClip.getIndex();
                NvsVideoFrameRetriever createVideoFrameRetriever = this.mStreamingContext.createVideoFrameRetriever(videoReverse ? meicamVideoClip.getReverseFilePath() : meicamVideoClip.getFilePath());
                if (createVideoFrameRetriever != null && (frameAtTime = createVideoFrameRetriever.getFrameAtTime((meicamVideoClip.getTrimIn() + currentTimelinePosition) - nvsVideoClip.getInPoint(), 2)) != null) {
                    String str = PathUtils.getVideoFreezeConvertDir() + File.separator + (System.currentTimeMillis() + ".png");
                    ImageUtils.saveBitmap(frameAtTime, str);
                    if (!handleCutClip(meicamVideoClip, i, z, currentTimelinePosition)) {
                        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
                        if (CollectionUtils.isIndexAvailable(i, meicamVideoTrackList) && (meicamVideoTrack = meicamVideoTrackList.get(i)) != null) {
                            List<ClipInfo> clipInfoList = meicamVideoTrack.getClipInfoList();
                            MeicamVideoClip meicamVideoClip2 = new MeicamVideoClip();
                            meicamVideoClip2.setFilePath(str);
                            meicamVideoClip2.setInPoint(getCurrentTimelinePosition());
                            meicamVideoClip2.setOutPoint(getCurrentTimelinePosition() + CommonData.DEFAULT_LENGTH);
                            meicamVideoClip2.setTrimIn(0);
                            meicamVideoClip2.setTrimOut(CommonData.DEFAULT_LENGTH);
                            meicamVideoClip2.setVideoType(CommonData.CLIP_IMAGE);
                            meicamVideoClip2.insertToTimeline(videoTrackByIndex, index);
                            clipInfoList.add(index + 1, meicamVideoClip2);
                            TimelineDataUtil.moveMainTrackClipsFromIndex(index + 2, CommonData.DEFAULT_LENGTH);
                        }
                        TimelineUtil.rebuildTimeline(this.mTimeline);
                        seekTimeline(0);
                        OnTimelineChangeListener onTimelineChangeListener2 = this.mOnTimelineChangeListener;
                        if (onTimelineChangeListener2 == null) {
                            return;
                        }
                        if (z) {
                            onTimelineChangeListener2.onTimelineChanged(this.mTimeline, true);
                            return;
                        }
                        onTimelineChangeListener2.refreshEditorTimelineView(4);
                        this.mOnTimelineChangeListener.onSaveOperation();
                    }
                }
            }
        }
    }

    public void setVideoConvert(MeicamVideoClip meicamVideoClip, int i) {
        MeicamVideoTrack meicamVideoTrack;
        NvsVideoTrack nvsVideoTrack;
        if (this.mTimeline != null && meicamVideoClip != null && (meicamVideoTrack = TimelineData.getInstance().getMeicamVideoTrackList().get(0)) != null && (nvsVideoTrack = (NvsVideoTrack) meicamVideoTrack.getObject()) != null) {
            long duration = this.mStreamingContext.getAVFileInfo(meicamVideoClip.getFilePath()).getDuration();
            long trimOut = meicamVideoClip.getTrimOut();
            long trimOut2 = meicamVideoClip.getTrimOut() - meicamVideoClip.getTrimIn();
            meicamVideoClip.setVideoReverse(!meicamVideoClip.getVideoReverse());
            int index = ((NvsVideoClip) meicamVideoClip.getObject()).getIndex();
            nvsVideoTrack.removeClip(index, false);
            meicamVideoClip.setTrimIn(duration - trimOut);
            meicamVideoClip.setTrimOut(meicamVideoClip.getTrimIn() + trimOut2);
            meicamVideoClip.insertToTimeline(nvsVideoTrack, index);
            TimelineUtil.clearAllBuildInTransition(this.mTimeline);
            TimelineUtil.setTransition(this.mTimeline, meicamVideoTrack.getTransitionInfoList());
            seekTimeline(0);
            if (this.mOnTimelineChangeListener != null) {
                if (meicamVideoClip.getVideoReverse()) {
                    this.mOnTimelineChangeListener.onTimelineChangeShowToast(R.string.revert_finish);
                } else {
                    this.mOnTimelineChangeListener.onTimelineChangeShowToast(R.string.cancel_revert);
                }
                this.mOnTimelineChangeListener.onTimelineChanged(this.mTimeline, true);
                this.mOnTimelineChangeListener.refreshEditorTimelineView(5);
            }
        }
    }

    public void changeStickerMirror(MeicamStickerClip meicamStickerClip) {
        NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker;
        if (this.mTimeline != null && meicamStickerClip != null && (nvsTimelineAnimatedSticker = (NvsTimelineAnimatedSticker) meicamStickerClip.getObject()) != null) {
            boolean z = !nvsTimelineAnimatedSticker.getHorizontalFlip();
            nvsTimelineAnimatedSticker.setHorizontalFlip(z);
            this.mVideoFragment.updateAnimateStickerCoordinate(nvsTimelineAnimatedSticker);
            meicamStickerClip.setHorizontalFlip(z);
            seekTimeline(4);
        }
    }

    public void handleCaptionApplyAll(MeicamCaptionClip meicamCaptionClip, int i) {
        NvsTimelineCaption nvsTimelineCaption;
        if (meicamCaptionClip != null && (nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject()) != null) {
            List<ClipInfo> stickerCaption = TimelineDataUtil.getStickerCaption();
            if (!CollectionUtils.isEmpty(stickerCaption)) {
                int i2 = 0;
                switch (i) {
                    case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_COLOR:
                        NvsColor textColor = nvsTimelineCaption.getTextColor();
                        if (textColor != null) {
                            while (i2 < stickerCaption.size()) {
                                ClipInfo clipInfo = stickerCaption.get(i2);
                                if ((clipInfo instanceof MeicamCaptionClip) && clipInfo != null) {
                                    NvsTimelineCaption nvsTimelineCaption2 = (NvsTimelineCaption) clipInfo.getObject();
                                    if (nvsTimelineCaption2 != null) {
                                        nvsTimelineCaption2.setTextColor(textColor);
                                        ((MeicamCaptionClip) clipInfo).setTextColor(ColorUtil.getColorArray(textColor));
                                    } else {
                                        return;
                                    }
                                }
                                i2++;
                            }
                            return;
                        }
                        return;
                    case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_OUT_LINE:
                        NvsColor outlineColor = nvsTimelineCaption.getOutlineColor();
                        if (outlineColor != null) {
                            while (i2 < stickerCaption.size()) {
                                ClipInfo clipInfo2 = stickerCaption.get(i2);
                                if ((clipInfo2 instanceof MeicamCaptionClip) && clipInfo2 != null) {
                                    NvsTimelineCaption nvsTimelineCaption3 = (NvsTimelineCaption) clipInfo2.getObject();
                                    if (nvsTimelineCaption3 != null) {
                                        nvsTimelineCaption.setDrawOutline(true);
                                        nvsTimelineCaption3.setOutlineColor(outlineColor);
                                        ((MeicamCaptionClip) clipInfo2).setTextColor(ColorUtil.getColorArray(outlineColor));
                                    } else {
                                        return;
                                    }
                                }
                                i2++;
                            }
                            return;
                        }
                        return;
                    case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_FONT:
                        String fontFamily = nvsTimelineCaption.getFontFamily();
                        while (i2 < stickerCaption.size()) {
                            ClipInfo clipInfo3 = stickerCaption.get(i2);
                            if ((clipInfo3 instanceof MeicamCaptionClip) && clipInfo3 != null) {
                                NvsTimelineCaption nvsTimelineCaption4 = (NvsTimelineCaption) clipInfo3.getObject();
                                if (nvsTimelineCaption4 != null) {
                                    nvsTimelineCaption4.setFontFamily(fontFamily);
                                    ((MeicamCaptionClip) clipInfo3).setFont(fontFamily);
                                } else {
                                    return;
                                }
                            }
                            i2++;
                        }
                        return;
                    case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_LETTER_SPACE:
                        float letterSpacing = nvsTimelineCaption.getLetterSpacing();
                        while (i2 < stickerCaption.size()) {
                            ClipInfo clipInfo4 = stickerCaption.get(i2);
                            if ((clipInfo4 instanceof MeicamCaptionClip) && clipInfo4 != null) {
                                NvsTimelineCaption nvsTimelineCaption5 = (NvsTimelineCaption) clipInfo4.getObject();
                                if (nvsTimelineCaption5 != null) {
                                    nvsTimelineCaption5.setLetterSpacing(letterSpacing);
                                    ((MeicamCaptionClip) clipInfo4).setLetterSpacing(letterSpacing);
                                } else {
                                    return;
                                }
                            }
                            i2++;
                        }
                        return;
                    case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_POSITION:
                        PointF captionTranslation = nvsTimelineCaption.getCaptionTranslation();
                        while (i2 < stickerCaption.size()) {
                            ClipInfo clipInfo5 = stickerCaption.get(i2);
                            if ((clipInfo5 instanceof MeicamCaptionClip) && clipInfo5 != null) {
                                NvsTimelineCaption nvsTimelineCaption6 = (NvsTimelineCaption) clipInfo5.getObject();
                                if (nvsTimelineCaption6 != null) {
                                    nvsTimelineCaption6.setCaptionTranslation(captionTranslation);
                                    MeicamCaptionClip meicamCaptionClip2 = (MeicamCaptionClip) clipInfo5;
                                    meicamCaptionClip2.setTranslationX(captionTranslation.x);
                                    meicamCaptionClip2.setTranslationY(captionTranslation.y);
                                } else {
                                    return;
                                }
                            }
                            i2++;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void handleCaptionStyleAndColor(MeicamCaptionClip meicamCaptionClip, BaseInfo baseInfo, int i) {
        NvsTimelineCaption nvsTimelineCaption;
        if (meicamCaptionClip != null && (nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject()) != null) {
            switch (i) {
                case MessageEvent.MESSAGE_TYPE_CAPTION_STYLE:
                    if (nvsTimelineCaption != null) {
                        NvAssetInfo asset = baseInfo.getAsset();
                        if (asset != null) {
                            nvsTimelineCaption.applyCaptionStyle(asset.getUuid());
                            meicamCaptionClip.setStyleId(asset.getUuid());
                        } else {
                            nvsTimelineCaption.applyCaptionStyle("");
                            meicamCaptionClip.setStyleId("");
                        }
                        this.mVideoFragment.setCurCaption(nvsTimelineCaption);
                        this.mVideoFragment.updateCaptionCoordinate(nvsTimelineCaption);
                        seekTimeline(2);
                        break;
                    }
                    break;
                case 1006:
                    if (nvsTimelineCaption != null) {
                        NvsColor colorStringtoNvsColor = ColorUtil.colorStringtoNvsColor(baseInfo.getColorValue());
                        nvsTimelineCaption.setTextColor(colorStringtoNvsColor);
                        meicamCaptionClip.setTextColor(ColorUtil.getColorArray(colorStringtoNvsColor));
                        seekTimeline(2);
                        break;
                    }
                    break;
                case 1007:
                    nvsTimelineCaption.setDrawOutline(true);
                    meicamCaptionClip.setOutline(true);
                    NvsColor colorStringtoNvsColor2 = ColorUtil.colorStringtoNvsColor(baseInfo.getColorValue());
                    nvsTimelineCaption.setOutlineColor(colorStringtoNvsColor2);
                    meicamCaptionClip.setOutlineColor(ColorUtil.getColorArray(colorStringtoNvsColor2));
                    seekTimeline(2);
                    break;
            }
            OnTimelineChangeListener onTimelineChangeListener = this.mOnTimelineChangeListener;
            if (onTimelineChangeListener != null) {
                onTimelineChangeListener.onSaveOperation();
            }
        }
    }

    public void setCaptionTranslation(MeicamCaptionClip meicamCaptionClip) {
        NvsTimelineCaption nvsTimelineCaption;
        PointF captionTranslation;
        if (meicamCaptionClip != null && (nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject()) != null && (captionTranslation = nvsTimelineCaption.getCaptionTranslation()) != null) {
            meicamCaptionClip.setTranslationX(captionTranslation.x);
            meicamCaptionClip.setTranslationY(captionTranslation.y);
        }
    }

    public void setStickerTranslation(MeicamStickerClip meicamStickerClip) {
        NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker;
        PointF translation;
        if (meicamStickerClip != null && (nvsTimelineAnimatedSticker = (NvsTimelineAnimatedSticker) meicamStickerClip.getObject()) != null && (translation = nvsTimelineAnimatedSticker.getTranslation()) != null) {
            meicamStickerClip.setTranslationX(translation.x);
            meicamStickerClip.setTranslationY(translation.y);
        }
    }

    public void setCompoundCaptionTranslation(MeicamCompoundCaptionClip meicamCompoundCaptionClip) {
        NvsTimelineCompoundCaption nvsTimelineCompoundCaption;
        PointF captionTranslation;
        if (meicamCompoundCaptionClip != null && (nvsTimelineCompoundCaption = (NvsTimelineCompoundCaption) meicamCompoundCaptionClip.getObject()) != null && (captionTranslation = nvsTimelineCompoundCaption.getCaptionTranslation()) != null) {
            meicamCompoundCaptionClip.setTranslationX(captionTranslation.x);
            meicamCompoundCaptionClip.setTranslationY(captionTranslation.y);
        }
    }

    public void setCaptionScale(MeicamCaptionClip meicamCaptionClip) {
        NvsTimelineCaption nvsTimelineCaption;
        if (meicamCaptionClip != null && (nvsTimelineCaption = (NvsTimelineCaption) meicamCaptionClip.getObject()) != null) {
            float scaleX = nvsTimelineCaption.getScaleX();
            float scaleY = nvsTimelineCaption.getScaleY();
            meicamCaptionClip.setScaleX(scaleX);
            meicamCaptionClip.setScaleY(scaleY);
        }
    }

    public void setStickerScale(MeicamStickerClip meicamStickerClip) {
        NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker;
        if (meicamStickerClip != null && (nvsTimelineAnimatedSticker = (NvsTimelineAnimatedSticker) meicamStickerClip.getObject()) != null) {
            meicamStickerClip.setScale(nvsTimelineAnimatedSticker.getScale());
        }
    }

    public void setCompoundCaptionScale(MeicamCompoundCaptionClip meicamCompoundCaptionClip) {
        NvsTimelineCompoundCaption nvsTimelineCompoundCaption;
        if (meicamCompoundCaptionClip != null && (nvsTimelineCompoundCaption = (NvsTimelineCompoundCaption) meicamCompoundCaptionClip.getObject()) != null) {
            float scaleX = nvsTimelineCompoundCaption.getScaleX();
            float scaleY = nvsTimelineCompoundCaption.getScaleY();
            meicamCompoundCaptionClip.setScaleX(scaleX);
            meicamCompoundCaptionClip.setScaleY(scaleY);
        }
    }

    public NvsStreamingContext getStreamingContext() {
        return this.mStreamingContext;
    }

    private void changeTrimPointOnTrimAdjust(NvsVideoClip nvsVideoClip, long j, boolean z) {
        if (nvsVideoClip != null) {
            if (z) {
                nvsVideoClip.changeTrimInPoint(j, true);
            } else {
                nvsVideoClip.changeTrimOutPoint(j, true);
            }
        }
    }

    private long setTrimOutPoint(NvsVideoClip nvsVideoClip, long j, boolean z) {
        if (j < 0) {
            j = 0;
        }
        return nvsVideoClip.changeTrimOutPoint(j, z);
    }

    private long setTrimInPoint(NvsVideoClip nvsVideoClip, long j, boolean z) {
        if (j < 0) {
            j = 0;
        }
        return nvsVideoClip.changeTrimInPoint(j, z);
    }

    public void stop() {
        this.mStreamingContext.stop();
    }

    public boolean isPlaying() {
        return this.mStreamingContext.getStreamingEngineState() == 3;
    }
}
