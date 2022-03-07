package com.meishe.myvideo.util.engine;

import android.content.Context;
import com.meicam.sdk.NvsTimeline;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.ui.bean.BaseUIClip;

public interface EditOperater {
    void addCompoundCaption(BaseInfo baseInfo);

    void applyAllBlurBackground(float f);

    void applyAllFilter(MeicamVideoClip meicamVideoClip);

    void applyAllImageBackground(String str, int i);

    void audioEditChangeClipSpeed(float f);

    void audioEditChangeVoice(String str);

    void audioEditChangeVolume(float f);

    void audioEditCopyClip(Context context);

    void audioEditCutClip(Context context);

    void audioEditDeleteClip();

    void audioEditPoint();

    void audioEditTransition(int i, int i2);

    void beauty(int i);

    void changeClipFilter(MeicamVideoClip meicamVideoClip, float f);

    void changeClipSpeed(float f);

    void changeOpacity(MeicamVideoClip meicamVideoClip, float f);

    void changeRatio(int i);

    void changeTimelineFilter(float f);

    void changeTimelineSpeed(float f);

    void changeVoice(String str);

    void changeVolume(float f);

    void cloneFxTrack();

    void copyEffect(BaseUIClip baseUIClip, Context context);

    void cutClip(MeicamVideoClip meicamVideoClip, int i, boolean z);

    void deleteBackground();

    void deleteClip(MeicamVideoClip meicamVideoClip, int i, boolean z);

    void deleteEffect(BaseUIClip baseUIClip);

    void denoise(boolean z);

    NvsTimeline getCurrentTimeline();

    long getCurrentTimelinePosition();

    MeicamVideoClip getEditVideoClip(long j, int i);

    void handleEventMessage(BaseInfo baseInfo, int i);

    void onAdjustDataChanged(Context context, MeicamVideoClip meicamVideoClip, int i, String str);

    void refreshData(BaseUIClip baseUIClip);

    void removeClipFilter(long j, int i);

    void removeFxTrack();

    void removeTimelineClipFilter();

    void restoreTimeline(String str, NvsTimeline nvsTimeline);

    void setCurrentTimeline(NvsTimeline nvsTimeline);

    void updateBackground();

    void updateBlurBackground(float f);

    void updateImageBackground(String str, int i);
}
