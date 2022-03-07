package com.meishe.myvideo.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import androidx.fragment.app.FragmentManager;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.engine.TimelineUtil;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.MeicamVideoFx;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.background.MeicamStoryboardInfo;
import com.meishe.engine.data.CutData;
import com.meishe.engine.util.StoryboardUtil;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideo.view.CuttingMenuView;
import com.meishe.myvideo.view.MYSeekBarView;
import com.meishe.myvideoapp.R;
import com.meishe.player.view.CutVideoFragment;

public class ClipCuttingActivity extends BaseActivity implements NvsStreamingContext.SeekingCallback {
    public static final String INTENT_KEY_TIMELINE_HEIGHT = "timeline_height";
    public static final String INTENT_KEY_TIMELINE_WIDTH = "timeline_width";
    public static final String INTENT_KEY_TRACK_INDEX = "track_index";
    private static final String TAG = "ClipCuttingActivity";
    private CutData mCutData;
    private CuttingMenuView mCuttingMenuView;
    private int mOriginalTimelineHeight;
    private int mOriginalTimelineWidth;
    private NvsTimeline mTimeline;
    private int mTrackIndex;
    private CutVideoFragment mVideoFragment;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_clip_cutting;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
    }

    public void onClick(View view) {
    }

    @Override // com.meicam.sdk.NvsStreamingContext.SeekingCallback
    public void onSeekingTimelinePosition(NvsTimeline nvsTimeline, long j) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        this.mCuttingMenuView = (CuttingMenuView) findViewById(R.id.edit_cutting_menu_view);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        Intent intent = getIntent();
        this.mOriginalTimelineHeight = intent.getIntExtra(INTENT_KEY_TIMELINE_HEIGHT, 0);
        this.mOriginalTimelineWidth = intent.getIntExtra(INTENT_KEY_TIMELINE_WIDTH, 0);
        this.mTrackIndex = intent.getIntExtra(INTENT_KEY_TRACK_INDEX, 0);
        this.mStreamingContext.setSeekingCallback(this);
        this.mStreamingContext.setCompileCallback(new NvsStreamingContext.CompileCallback() {
            /* class com.meishe.myvideo.activity.ClipCuttingActivity.AnonymousClass1 */

            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback
            public void onCompileFailed(NvsTimeline nvsTimeline) {
            }

            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback
            public void onCompileFinished(NvsTimeline nvsTimeline) {
            }

            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback
            public void onCompileProgress(NvsTimeline nvsTimeline, int i) {
            }
        });
        addVideoClip();
        this.mCutData = getCutData();
        initPlayer();
        CutData cutData = this.mCutData;
        if (cutData != null) {
            this.mCuttingMenuView.setProgress(cutData.getTransformData(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z));
            this.mCuttingMenuView.setSelectRatio(this.mCutData.getRatio());
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mCuttingMenuView.setOnSeekBarListener(new MYSeekBarView.OnSeekBarListener() {
            /* class com.meishe.myvideo.activity.ClipCuttingActivity.AnonymousClass2 */

            @Override // com.meishe.myvideo.view.MYSeekBarView.OnSeekBarListener
            public void onStopTrackingTouch(int i, String str) {
            }

            @Override // com.meishe.myvideo.view.MYSeekBarView.OnSeekBarListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    ClipCuttingActivity.this.mVideoFragment.rotateVideo((float) (i - 45));
                }
            }
        });
        this.mCuttingMenuView.setOnRatioSelectListener(new CuttingMenuView.OnRatioSelectListener() {
            /* class com.meishe.myvideo.activity.ClipCuttingActivity.AnonymousClass3 */

            @Override // com.meishe.myvideo.view.CuttingMenuView.OnRatioSelectListener
            public void onItemClicked(int i) {
                ClipCuttingActivity.this.mVideoFragment.changeCutRectView(i);
            }

            @Override // com.meishe.myvideo.view.CuttingMenuView.OnRatioSelectListener
            public void onReset() {
                ClipCuttingActivity.this.mVideoFragment.reset();
                ClipCuttingActivity.this.mCuttingMenuView.setProgress(0.0f);
            }
        });
        this.mCuttingMenuView.setOnConfrimListener(new CuttingMenuView.OnConfirmListener() {
            /* class com.meishe.myvideo.activity.ClipCuttingActivity.AnonymousClass4 */

            @Override // com.meishe.myvideo.view.CuttingMenuView.OnConfirmListener
            public void onConfirm() {
                MeicamStoryboardInfo meicamStoryboardInfo;
                String str;
                MeicamVideoClip selectedMeicamClipInfo = TimelineData.getInstance().getSelectedMeicamClipInfo();
                MeicamVideoFx videoFx = selectedMeicamClipInfo.getVideoFx("Transform 2D");
                selectedMeicamClipInfo.removeVideoFx("Transform 2D");
                if (ClipCuttingActivity.this.mTrackIndex == 0) {
                    meicamStoryboardInfo = selectedMeicamClipInfo.getBackgroundInfo();
                    selectedMeicamClipInfo.removeBackground();
                } else {
                    meicamStoryboardInfo = null;
                }
                int[] size = ClipCuttingActivity.this.mVideoFragment.getSize();
                String transform2DStory = StoryboardUtil.getTransform2DStory(size[0], size[1], ClipCuttingActivity.this.mVideoFragment.getTransFromData(ClipCuttingActivity.this.mOriginalTimelineWidth, ClipCuttingActivity.this.mOriginalTimelineHeight));
                MeicamStoryboardInfo meicamStoryboardInfo2 = new MeicamStoryboardInfo();
                meicamStoryboardInfo2.setSubType(MeicamStoryboardInfo.SUB_TYPE_CROPPER_TRANSFROM);
                meicamStoryboardInfo2.setStoryDesc(transform2DStory);
                meicamStoryboardInfo2.setBooleanVal("No Background", true);
                meicamStoryboardInfo2.setStringVal("Description String", transform2DStory);
                meicamStoryboardInfo2.bindToTimelineByType((NvsVideoClip) selectedMeicamClipInfo.getObject(), meicamStoryboardInfo2.getSubType());
                selectedMeicamClipInfo.addStoryboardInfo(meicamStoryboardInfo2.getSubType(), meicamStoryboardInfo2);
                Log.e(ClipCuttingActivity.TAG, "onConfirm: transform2DStory = " + transform2DStory);
                NvsVideoResolution videoRes = ClipCuttingActivity.this.mTimeline.getVideoRes();
                float[] relativeSize = ClipCuttingActivity.this.getRelativeSize(videoRes.imageWidth, videoRes.imageHeight, ClipCuttingActivity.this.mOriginalTimelineWidth, ClipCuttingActivity.this.mOriginalTimelineHeight);
                MeicamStoryboardInfo meicamStoryboardInfo3 = new MeicamStoryboardInfo();
                meicamStoryboardInfo3.setSubType(MeicamStoryboardInfo.SUB_TYPE_CROPPER);
                if (ClipCuttingActivity.this.mTrackIndex == 0) {
                    str = StoryboardUtil.getCropperStory(size[0], size[1], ClipCuttingActivity.this.mVideoFragment.getRegionData(relativeSize));
                } else {
                    str = StoryboardUtil.getCropperStory(size[0], size[1], ClipCuttingActivity.this.mVideoFragment.getRegionData(relativeSize));
                }
                meicamStoryboardInfo3.setStoryDesc(str);
                meicamStoryboardInfo3.setBooleanVal("No Background", true);
                meicamStoryboardInfo3.setStringVal("Description String", str);
                meicamStoryboardInfo3.bindToTimelineByType((NvsVideoClip) selectedMeicamClipInfo.getObject(), meicamStoryboardInfo3.getSubType());
                selectedMeicamClipInfo.addStoryboardInfo(meicamStoryboardInfo3.getSubType(), meicamStoryboardInfo3);
                Log.e(ClipCuttingActivity.TAG, "onConfirm: cropperStory = " + str);
                if (videoFx != null) {
                    videoFx.bindToTimeline((NvsVideoClip) selectedMeicamClipInfo.getObject());
                    selectedMeicamClipInfo.getVideoFxs().add(videoFx);
                }
                if (meicamStoryboardInfo != null) {
                    meicamStoryboardInfo.bindToTimelineByType((NvsVideoClip) selectedMeicamClipInfo.getObject(), meicamStoryboardInfo.getSubType());
                    selectedMeicamClipInfo.addStoryboardInfo(meicamStoryboardInfo.getSubType(), meicamStoryboardInfo);
                }
                ClipCuttingActivity.this.setResult(-1);
                AppManager.getInstance().finishActivity();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private float[] getRelativeSize(int i, int i2, int i3, int i4) {
        float[] fArr = new float[2];
        float f = (float) i3;
        float f2 = f * 1.0f;
        float f3 = (float) i4;
        float f4 = (float) i;
        float f5 = (float) i2;
        if ((f4 * 1.0f) / f5 > f2 / f3) {
            fArr[0] = 1.0f;
            fArr[1] = (f5 * (f2 / f4)) / f3;
        } else {
            fArr[1] = 1.0f;
            fArr[0] = (f4 * ((f3 * 1.0f) / f5)) / f;
        }
        return fArr;
    }

    private void addVideoClip() {
        NvsVideoTrack appendVideoTrack;
        MeicamVideoClip selectedMeicamClipInfo = TimelineData.getInstance().getSelectedMeicamClipInfo();
        if (selectedMeicamClipInfo != null) {
            this.mTimeline = TimelineUtil.newTimeline(Util.getVideoEditResolutionByClip(selectedMeicamClipInfo.getFilePath()));
            NvsTimeline nvsTimeline = this.mTimeline;
            if (nvsTimeline != null && (appendVideoTrack = nvsTimeline.appendVideoTrack()) != null) {
                NvsVideoClip appendClip = appendVideoTrack.appendClip(selectedMeicamClipInfo.getFilePath());
                if (appendClip.getVideoType() == 1) {
                    appendClip.setImageMotionMode(0);
                } else {
                    appendClip.setPanAndScan(0.0f, 0.0f);
                }
            }
        }
    }

    private void initPlayer() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.mVideoFragment = CutVideoFragment.newInstance(0);
        this.mVideoFragment.setCutData(this.mCutData);
        this.mVideoFragment.setTimeLine(this.mTimeline);
        new Handler().post(new Runnable() {
            /* class com.meishe.myvideo.activity.ClipCuttingActivity.AnonymousClass5 */

            public void run() {
                ClipCuttingActivity.this.mVideoFragment.initData();
                ClipCuttingActivity.this.updatePlaySeekBar(0);
            }
        });
        supportFragmentManager.beginTransaction().add(R.id.edit_preview_view, this.mVideoFragment).commit();
        supportFragmentManager.beginTransaction().show(this.mVideoFragment);
        this.mVideoFragment.setVideoFragmentCallBack(new CutVideoFragment.VideoFragmentListener() {
            /* class com.meishe.myvideo.activity.ClipCuttingActivity.AnonymousClass6 */

            @Override // com.meishe.player.view.CutVideoFragment.VideoFragmentListener
            public void playBackEOF(NvsTimeline nvsTimeline) {
            }

            @Override // com.meishe.player.view.CutVideoFragment.VideoFragmentListener
            public void playStopped(NvsTimeline nvsTimeline) {
            }

            @Override // com.meishe.player.view.CutVideoFragment.VideoFragmentListener
            public void playbackTimelinePosition(NvsTimeline nvsTimeline, long j) {
            }

            @Override // com.meishe.player.view.CutVideoFragment.VideoFragmentListener
            public void streamingEngineStateChanged(int i) {
            }
        });
        this.mVideoFragment.setOnCutRectChangelisener(new CutVideoFragment.OnCutRectChangedListener() {
            /* class com.meishe.myvideo.activity.ClipCuttingActivity.AnonymousClass7 */

            @Override // com.meishe.player.view.CutVideoFragment.OnCutRectChangedListener
            public void onScaleAndRotate(float f, float f2) {
                ClipCuttingActivity.this.mCuttingMenuView.setProgress(f2);
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updatePlaySeekBar(long j) {
        this.mVideoFragment.seekTimeline(j, 1);
    }

    private CutData getCutData() {
        MeicamVideoClip selectedMeicamClipInfo = TimelineData.getInstance().getSelectedMeicamClipInfo();
        String str = null;
        if (selectedMeicamClipInfo == null) {
            return null;
        }
        MeicamStoryboardInfo meicamStoryboardInfo = selectedMeicamClipInfo.getStoryboardInfos().get(MeicamStoryboardInfo.SUB_TYPE_CROPPER_TRANSFROM);
        String stringVal = meicamStoryboardInfo != null ? meicamStoryboardInfo.getStringVal("Description String") : null;
        MeicamStoryboardInfo meicamStoryboardInfo2 = selectedMeicamClipInfo.getStoryboardInfos().get(MeicamStoryboardInfo.SUB_TYPE_CROPPER);
        if (meicamStoryboardInfo2 != null) {
            str = meicamStoryboardInfo2.getStringVal("Description String");
        }
        return StoryboardUtil.parseStoryToCatData(str, stringVal);
    }
}
