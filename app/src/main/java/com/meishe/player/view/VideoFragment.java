package com.meishe.player.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.meicam.sdk.NvsLiveWindowExt;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamFxParam;
import com.meishe.engine.bean.MeicamTimelineVideoFxClip;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.MeicamVideoFx;
import com.meishe.engine.bean.MeicamWaterMark;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.background.MeicamStoryboardInfo;
import com.meishe.engine.util.StoryboardUtil;
import com.meishe.player.R;
import com.meishe.player.common.utils.ImageConverter;
import com.meishe.player.common.utils.ScreenUtils;
import com.meishe.player.view.DrawRect;
import com.meishe.player.view.PipTransformView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class VideoFragment extends Fragment {
    private static final long BASE_VALUE = 100000;
    private static final float DEFAULT_SCALE_VALUE = 1.0f;
    private static final int MESSAGE_RESET_PLATBACK_STATE = 100;
    private static final String PARAM_MAX_DURATION = "max_duration";
    private static final float PIP_MAX_ROTATION = 360.0f;
    private static final float PIP_MAX_SCALE = 10.0f;
    private static final float PIP_MIM_SCALE = 0.1f;
    private static final int PIP_NO_OPERATION_EVENT = 0;
    private static final int PIP_SCALE_OR_ROTATE_OPERATION_EVENT = 2;
    private static final int PIP_TRANSLATE_OPERATION_EVENT = 1;
    private static final float RATIO_16_9 = 1.7777778f;
    private static final float RATIO_1_1 = 1.0f;
    private static final float RATIO_3_4 = 0.75f;
    private static final float RATIO_4_3 = 1.3333334f;
    private static final float RATIO_9_16 = 0.5625f;
    private static final String TAG = "VideoFragment";
    private AssetEditListener mAssetEditListener;
    private VideoCaptionTextEditListener mCaptionTextEditListener;
    private OnCompoundCaptionListener mCompoundCaptionListener;
    private NvsTimelineAnimatedSticker mCurAnimateSticker;
    private NvsTimelineCaption mCurCaption;
    private NvsTimelineCompoundCaption mCurCompoundCaption;
    private DrawRect mDrawRect;
    private int mEditMode = 0;
    private OnFragmentLoadFinisedListener mFragmentLoadFinisedListener;
    private Handler mHandler = new Handler(new Handler.Callback() {
        /* class com.meishe.player.view.VideoFragment.AnonymousClass1 */

        public boolean handleMessage(Message message) {
            int i = message.what;
            return false;
        }
    });
    private NvsLiveWindowExt mLiveWindow;
    private OnLiveWindowClickListener mLiveWindowClickListener;
    private long mMaxDuration;
    private OnBackgroundChangedListener mOnBackgroundChangedListener;
    private OnSaveOperationListener mOnSaveOperationListener;
    private int mPipOperationEvent;
    private PipTransformView mPipTransformView;
    private float mPixTimeLineRatio = 1.0f;
    private RelativeLayout mPlayerLayout;
    private int mPlayviewHiewHeight = 0;
    private MeicamVideoClip mSelectedMeicamClipInfo;
    private long mStartTime;
    private int mStickerMuteIndex;
    private OnStickerMuteListener mStickerMuteListener;
    private NvsStreamingContext mStreamingContext = NvsStreamingContext.getInstance();
    private OnThemeCaptionSeekListener mThemeCaptionSeekListener;
    private NvsTimeline mTimeline;
    private MeicamVideoFx mTransformNvsVideoFx;
    private long mTransitionStart = 0;
    private VideoFragmentListener mVideoFragmentCallBack;
    private MeicamFxParam meicamFxParamSecond;
    private MeicamTimelineVideoFxClip meicamTimelineVideoFxClip;
    private NvsTimelineVideoFx nvsTimelineVideoFx;
    private int playState;
    private List<PointF> pointFListLiveWindow;
    private int waterType = 0;

    public interface AssetEditListener {
        void onAssetAlign(int i);

        void onAssetDelete(int i);

        void onAssetHorizFlip(boolean z);

        void onAssetScale(int i);

        void onAssetSelected(PointF pointF);

        void onAssetTranstion(int i);

        void onBeyondDrawRectClick();

        void onTouchUp(int i);
    }

    public interface OnBackgroundChangedListener {
        void onBackgroundChanged();
    }

    public interface OnCompoundCaptionListener {
        void onCaptionIndex(int i);
    }

    public interface OnFragmentLoadFinisedListener {
        void onLoadFinished();
    }

    public interface OnLiveWindowClickListener {
        void onLiveWindowClick();
    }

    public interface OnSaveOperationListener {
        void onSaveCurrentTimeline();
    }

    public interface OnStickerMuteListener {
        void onStickerMute();
    }

    public interface OnThemeCaptionSeekListener {
        void onThemeCaptionSeek(long j);
    }

    public interface VideoCaptionTextEditListener {
        void onCaptionTextEdit();
    }

    public interface VideoFragmentListener {
        void playBackEOF(NvsTimeline nvsTimeline);

        void playStopped(NvsTimeline nvsTimeline);

        void playbackTimelinePosition(NvsTimeline nvsTimeline, long j);

        void streamingEngineStateChanged(int i);
    }

    public static VideoFragment newInstance(long j) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_MAX_DURATION, j);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        if (getArguments() != null) {
            this.mMaxDuration = getArguments().getLong(PARAM_MAX_DURATION);
        }
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video, viewGroup, false);
        this.mLiveWindow = (NvsLiveWindowExt) inflate.findViewById(R.id.liveWindow);
        this.mLiveWindow.setFillMode(1);
        this.mPlayerLayout = (RelativeLayout) inflate.findViewById(R.id.playerLayout);
        this.mDrawRect = (DrawRect) inflate.findViewById(R.id.draw_rect);
        this.mPipTransformView = (PipTransformView) inflate.findViewById(R.id.pip_transform_view);
        initListener();
        return inflate;
    }

    private void initListener() {
        this.mLiveWindow.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.player.view.VideoFragment.AnonymousClass2 */

            public void onClick(View view) {
                if (VideoFragment.this.mLiveWindowClickListener != null) {
                    VideoFragment.this.mLiveWindowClickListener.onLiveWindowClick();
                }
            }
        });
        this.mPipTransformView.setOnPipTouchListener(new PipTransformView.OnPipTouchEventListener() {
            /* class com.meishe.player.view.VideoFragment.AnonymousClass3 */

            @Override // com.meishe.player.view.PipTransformView.OnPipTouchEventListener
            public void onTouchDown(PointF pointF) {
            }

            @Override // com.meishe.player.view.PipTransformView.OnPipTouchEventListener
            public void onScaleAndRotate(float f, float f2) {
                if ((f != 1.0f || f2 != 0.0f) && VideoFragment.this.mSelectedMeicamClipInfo != null) {
                    if (VideoFragment.this.mTransformNvsVideoFx != null) {
                        double floatVal = (double) VideoFragment.this.mTransformNvsVideoFx.getFloatVal("Scale X");
                        double d = (double) f;
                        Double.isNaN(floatVal);
                        Double.isNaN(d);
                        double d2 = floatVal * d;
                        if (d2 > 10.0d) {
                            d2 = 10.0d;
                        }
                        if (d2 < 0.10000000149011612d) {
                            d2 = 0.10000000149011612d;
                        }
                        float f3 = (float) d2;
                        VideoFragment.this.mTransformNvsVideoFx.setFloatVal("Scale X", f3);
                        VideoFragment.this.mTransformNvsVideoFx.setFloatVal("Scale Y", f3);
                        VideoFragment.this.mTransformNvsVideoFx.setFloatVal("Rotation", VideoFragment.this.getMirrorTargetVideoFxNewDegree(VideoFragment.this.mTransformNvsVideoFx.getFloatVal("Rotation"), f2));
                        VideoFragment videoFragment = VideoFragment.this;
                        videoFragment.seekTimeline(videoFragment.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 0);
                    } else {
                        Map transform = VideoFragment.this.getTransform();
                        float floatValue = ((Float) transform.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X)).floatValue() * f;
                        float f4 = VideoFragment.PIP_MAX_SCALE;
                        if (floatValue <= VideoFragment.PIP_MAX_SCALE) {
                            f4 = floatValue;
                        }
                        if (f4 < VideoFragment.PIP_MIM_SCALE) {
                            f4 = VideoFragment.PIP_MIM_SCALE;
                        }
                        transform.put(StoryboardUtil.STORYBOARD_KEY_SCALE_X, Float.valueOf(f4));
                        transform.put(StoryboardUtil.STORYBOARD_KEY_SCALE_Y, Float.valueOf(f4));
                        transform.put(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z, Float.valueOf(VideoFragment.this.getMirrorTargetVideoFxNewDegree(((Float) transform.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z)).floatValue(), f2)));
                        if (VideoFragment.this.mOnBackgroundChangedListener != null) {
                            VideoFragment.this.mOnBackgroundChangedListener.onBackgroundChanged();
                        }
                    }
                    VideoFragment.this.mPipOperationEvent = 2;
                }
            }

            @Override // com.meishe.player.view.PipTransformView.OnPipTouchEventListener
            public void onDrag(PointF pointF, PointF pointF2) {
                if (VideoFragment.this.mSelectedMeicamClipInfo != null) {
                    float f = pointF2.x - pointF.x;
                    float f2 = pointF2.y - pointF.y;
                    if (f != 0.0f || f2 != 0.0f) {
                        float f3 = VideoFragment.this.mPixTimeLineRatio * f;
                        float f4 = VideoFragment.this.mPixTimeLineRatio * f2;
                        if (VideoFragment.this.mTransformNvsVideoFx != null) {
                            float[] mirrorTargetVideoFxNewPosition = VideoFragment.this.getMirrorTargetVideoFxNewPosition(VideoFragment.this.mTransformNvsVideoFx.getFloatVal("Trans X"), VideoFragment.this.mTransformNvsVideoFx.getFloatVal("Trans Y"), f3, f4);
                            VideoFragment.this.mTransformNvsVideoFx.setFloatVal("Trans X", mirrorTargetVideoFxNewPosition[0]);
                            VideoFragment.this.mTransformNvsVideoFx.setFloatVal("Trans Y", mirrorTargetVideoFxNewPosition[1]);
                            VideoFragment videoFragment = VideoFragment.this;
                            videoFragment.seekTimeline(videoFragment.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 0);
                        } else {
                            Map transform = VideoFragment.this.getTransform();
                            if (transform != null) {
                                float[] mirrorTargetVideoFxNewPosition2 = VideoFragment.this.getMirrorTargetVideoFxNewPosition(((Float) transform.get(StoryboardUtil.STORYBOARD_KEY_TRANS_X)).floatValue(), ((Float) transform.get(StoryboardUtil.STORYBOARD_KEY_TRANS_Y)).floatValue(), f3, f4);
                                transform.put(StoryboardUtil.STORYBOARD_KEY_TRANS_X, Float.valueOf(mirrorTargetVideoFxNewPosition2[0]));
                                transform.put(StoryboardUtil.STORYBOARD_KEY_TRANS_Y, Float.valueOf(mirrorTargetVideoFxNewPosition2[1]));
                                if (VideoFragment.this.mOnBackgroundChangedListener != null) {
                                    VideoFragment.this.mOnBackgroundChangedListener.onBackgroundChanged();
                                }
                            } else {
                                return;
                            }
                        }
                        VideoFragment.this.mPipOperationEvent = 1;
                    }
                }
            }

            @Override // com.meishe.player.view.PipTransformView.OnPipTouchEventListener
            public void onTouchUp(PointF pointF) {
                if (VideoFragment.this.mPipOperationEvent == 1 || VideoFragment.this.mPipOperationEvent == 2) {
                    if (VideoFragment.this.mOnSaveOperationListener != null) {
                        VideoFragment.this.mOnSaveOperationListener.onSaveCurrentTimeline();
                    }
                    VideoFragment.this.mPipOperationEvent = 0;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private float getMirrorTargetVideoFxNewDegree(float f, float f2) {
        float f3;
        NvsVideoFx mirrorTargetVideoFx = getMirrorTargetVideoFx();
        if (mirrorTargetVideoFx != null && ((float) mirrorTargetVideoFx.getFloatVal("Scale X")) == -1.0f) {
            f3 = f - f2;
        } else {
            f3 = f + f2;
        }
        return f3 % PIP_MAX_ROTATION;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private float[] getMirrorTargetVideoFxNewPosition(float f, float f2, float f3, float f4) {
        float f5;
        float[] fArr = new float[2];
        NvsVideoFx mirrorTargetVideoFx = getMirrorTargetVideoFx();
        if (mirrorTargetVideoFx != null && ((float) mirrorTargetVideoFx.getFloatVal("Scale X")) == -1.0f) {
            f5 = f - f3;
        } else {
            f5 = f + f3;
        }
        float f6 = f2 - f4;
        fArr[0] = f5;
        fArr[1] = f6;
        return fArr;
    }

    private NvsVideoFx getMirrorTargetVideoFx() {
        NvsVideoClip nvsVideoClip = (NvsVideoClip) this.mSelectedMeicamClipInfo.getObject();
        if (nvsVideoClip == null) {
            return null;
        }
        int fxCount = nvsVideoClip.getFxCount();
        for (int i = 0; i < fxCount; i++) {
            NvsVideoFx fxByIndex = nvsVideoClip.getFxByIndex(i);
            if (fxByIndex.getAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_MIRROR) != null) {
                return fxByIndex;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Map<String, Float> getTransform() {
        MeicamStoryboardInfo backgroundInfo = this.mSelectedMeicamClipInfo.getBackgroundInfo();
        if (backgroundInfo == null) {
            return null;
        }
        return backgroundInfo.getClipTrans();
    }

    public void setVideoClipInfo(MeicamVideoClip meicamVideoClip, int i) {
        this.mSelectedMeicamClipInfo = meicamVideoClip;
        this.mTransformNvsVideoFx = null;
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null && this.mSelectedMeicamClipInfo != null) {
            this.mPixTimeLineRatio = ((float) nvsTimeline.getVideoRes().imageHeight) / ((float) this.mLiveWindow.getHeight());
            if (i > 0) {
                this.mTransformNvsVideoFx = this.mSelectedMeicamClipInfo.getVideoFx("Transform 2D");
                if (this.mTransformNvsVideoFx == null) {
                    this.mTransformNvsVideoFx = new MeicamVideoFx();
                    this.mTransformNvsVideoFx.setType("builtin");
                    this.mTransformNvsVideoFx.setSubType("Transform 2D");
                    this.mTransformNvsVideoFx.setDesc("Transform 2D");
                    this.mTransformNvsVideoFx.setFloatVal("Trans X", 0.0f);
                    this.mTransformNvsVideoFx.setFloatVal("Trans Y", 0.0f);
                    this.mTransformNvsVideoFx.setFloatVal("Scale X", 1.0f);
                    this.mTransformNvsVideoFx.setFloatVal("Scale Y", 1.0f);
                    this.mTransformNvsVideoFx.setFloatVal("Rotation", 0.0f);
                    this.mTransformNvsVideoFx.bindToTimeline((NvsVideoClip) this.mSelectedMeicamClipInfo.getObject());
                    this.mSelectedMeicamClipInfo.getVideoFxs().add(this.mTransformNvsVideoFx);
                }
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        initData();
        OnFragmentLoadFinisedListener onFragmentLoadFinisedListener = this.mFragmentLoadFinisedListener;
        if (onFragmentLoadFinisedListener != null) {
            onFragmentLoadFinisedListener.onLoadFinished();
        }
    }

    public void initData() {
        setLiveWindowRatio();
        connectTimelineWithLiveWindow();
        initDrawRectListener();
    }

    public void setEditMode(int i) {
        this.mEditMode = i;
    }

    public int getEditMode() {
        return this.mEditMode;
    }

    public void setStartTime(long j) {
        this.mStartTime = j;
    }

    public long getDuration() {
        long j = this.mMaxDuration;
        if (j > 0) {
            return j;
        }
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline == null) {
            return 0;
        }
        return nvsTimeline.getDuration();
    }

    public void setLiveWindowRatio() {
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null) {
            NvsVideoResolution videoRes = nvsTimeline.getVideoRes();
            final int aspectRatio = getAspectRatio(videoRes.imageWidth, videoRes.imageHeight);
            final float f = (((float) videoRes.imageWidth) * 1.0f) / ((float) videoRes.imageHeight);
            if (this.mPlayerLayout.getWidth() == 0 && this.mPlayerLayout.getHeight() == 0) {
                this.mPlayerLayout.post(new Runnable() {
                    /* class com.meishe.player.view.VideoFragment.AnonymousClass4 */

                    public void run() {
                        VideoFragment videoFragment = VideoFragment.this;
                        videoFragment.mPlayviewHiewHeight = videoFragment.mPlayerLayout.getHeight();
                        VideoFragment videoFragment2 = VideoFragment.this;
                        videoFragment2.setLiveWindowRatio(aspectRatio, videoFragment2.mPlayviewHiewHeight, f);
                    }
                });
            } else {
                setLiveWindowRatio(aspectRatio, this.mPlayviewHiewHeight, f);
            }
        }
    }

    private int getAspectRatio(int i, int i2) {
        float f = (((float) i) * 1.0f) / ((float) i2);
        if (isFloatEqual(f, RATIO_16_9)) {
            return 1;
        }
        if (isFloatEqual(f, RATIO_4_3)) {
            return 8;
        }
        if (isFloatEqual(f, 1.0f)) {
            return 2;
        }
        if (isFloatEqual(f, RATIO_3_4)) {
            return 16;
        }
        return isFloatEqual(f, RATIO_9_16) ? 4 : 0;
    }

    private boolean isFloatEqual(float f, float f2) {
        return ((double) Math.abs(f - f2)) <= 1.0E-6d;
    }

    public void connectTimelineWithLiveWindow() {
        NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
        if (nvsStreamingContext != null && this.mTimeline != null && this.mLiveWindow != null) {
            nvsStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
                /* class com.meishe.player.view.VideoFragment.AnonymousClass5 */

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
                public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {
                }

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
                public void onPlaybackStopped(NvsTimeline nvsTimeline) {
                    if (VideoFragment.this.mVideoFragmentCallBack != null) {
                        VideoFragment.this.mVideoFragmentCallBack.playStopped(nvsTimeline);
                    }
                }

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
                public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                    VideoFragment.this.mHandler.sendEmptyMessage(100);
                    if (VideoFragment.this.mVideoFragmentCallBack != null) {
                        VideoFragment.this.mVideoFragmentCallBack.playBackEOF(nvsTimeline);
                    }
                }
            });
            this.mStreamingContext.setPlaybackCallback2(new NvsStreamingContext.PlaybackCallback2() {
                /* class com.meishe.player.view.VideoFragment.AnonymousClass6 */

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback2
                public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long j) {
                    if (VideoFragment.this.mVideoFragmentCallBack != null) {
                        VideoFragment.this.mVideoFragmentCallBack.playbackTimelinePosition(nvsTimeline, j);
                    }
                }
            });
            this.mStreamingContext.setStreamingEngineCallback(new NvsStreamingContext.StreamingEngineCallback() {
                /* class com.meishe.player.view.VideoFragment.AnonymousClass7 */

                @Override // com.meicam.sdk.NvsStreamingContext.StreamingEngineCallback
                public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {
                }

                @Override // com.meicam.sdk.NvsStreamingContext.StreamingEngineCallback
                public void onStreamingEngineStateChanged(int i) {
                    VideoFragment.this.playState = i;
                    if (VideoFragment.this.mVideoFragmentCallBack != null) {
                        VideoFragment.this.mVideoFragmentCallBack.streamingEngineStateChanged(i);
                    }
                }
            });
            this.mStreamingContext.connectTimelineWithLiveWindowExt(this.mTimeline, this.mLiveWindow);
        }
    }

    public void setLiveWindowRatio(int i, int i2, float f) {
        ViewGroup.LayoutParams layoutParams = this.mPlayerLayout.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(getActivity());
        if (i != 0) {
            if (i == 1) {
                layoutParams.width = screenWidth;
                double d = (double) screenWidth;
                Double.isNaN(d);
                layoutParams.height = (int) ((d * 9.0d) / 16.0d);
            } else if (i == 2) {
                layoutParams.width = screenWidth;
                layoutParams.height = screenWidth;
                if (i2 < screenWidth) {
                    layoutParams.width = i2;
                    layoutParams.height = i2;
                }
            } else if (i == 4) {
                double d2 = (double) i2;
                Double.isNaN(d2);
                layoutParams.width = (int) ((d2 * 9.0d) / 16.0d);
                layoutParams.height = i2;
            } else if (i == 8) {
                layoutParams.width = screenWidth;
                double d3 = (double) screenWidth;
                Double.isNaN(d3);
                layoutParams.height = (int) ((d3 * 3.0d) / 4.0d);
            } else if (i != 16) {
                layoutParams.width = screenWidth;
                double d4 = (double) screenWidth;
                Double.isNaN(d4);
                layoutParams.height = (int) ((d4 * 9.0d) / 16.0d);
            } else {
                double d5 = (double) i2;
                Double.isNaN(d5);
                layoutParams.width = (int) ((d5 * 3.0d) / 4.0d);
                layoutParams.height = i2;
            }
        } else if (f - 1.0f > 0.0f) {
            layoutParams.width = screenWidth;
            layoutParams.height = (int) (((float) screenWidth) / f);
        } else {
            layoutParams.width = (int) (((float) i2) * f);
            layoutParams.height = i2;
        }
        this.mPlayerLayout.setLayoutParams(layoutParams);
        this.mLiveWindow.repaintVideoFrame();
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
    }

    public void setTimeLine(NvsTimeline nvsTimeline) {
        this.mTimeline = nvsTimeline;
    }

    public void playVideoButtonClick() {
        if (this.mTimeline != null) {
            playVideoButtonClick(0, getDuration());
        }
    }

    public void playVideoButtonClick(long j, long j2) {
        playVideo(j, j2);
    }

    public void playVideo(long j, long j2) {
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null) {
            this.mStreamingContext.playbackTimeline(nvsTimeline, j, j2, 1, true, 32);
        }
    }

    public void playTransition(long j, long j2) {
        this.mTransitionStart = j;
        this.mStreamingContext.playbackTimeline(this.mTimeline, j, j2, 1, true, 32);
    }

    public long getTransitionStart() {
        return this.mTransitionStart;
    }

    public void setTransitionStart(long j) {
        this.mTransitionStart = j;
    }

    public void seekTimeline(long j, int i) {
        if (i > 0) {
            this.mStreamingContext.seekTimeline(this.mTimeline, j, 1, i | 16);
        } else {
            this.mStreamingContext.seekTimeline(this.mTimeline, j, 1, 16);
        }
    }

    public int getCurrentEngineState() {
        return this.mStreamingContext.getStreamingEngineState();
    }

    public void stopEngine() {
        NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
        if (nvsStreamingContext != null) {
            nvsStreamingContext.stop();
        }
    }

    private void initDrawRectListener() {
        this.mDrawRect.setOnTouchListener(new DrawRect.OnTouchListener() {
            /* class com.meishe.player.view.VideoFragment.AnonymousClass8 */

            @Override // com.meishe.player.view.DrawRect.OnTouchListener
            public void onDrag(PointF pointF, PointF pointF2) {
                PointF mapViewToCanonical = VideoFragment.this.mLiveWindow.mapViewToCanonical(pointF);
                PointF mapViewToCanonical2 = VideoFragment.this.mLiveWindow.mapViewToCanonical(pointF2);
                PointF pointF3 = new PointF(mapViewToCanonical2.x - mapViewToCanonical.x, mapViewToCanonical2.y - mapViewToCanonical.y);
                if (VideoFragment.this.mEditMode == 0) {
                    if (VideoFragment.this.mCurCaption != null) {
                        VideoFragment.this.mCurCaption.translateCaption(pointF3);
                        VideoFragment videoFragment = VideoFragment.this;
                        videoFragment.updateCaptionCoordinate(videoFragment.mCurCaption);
                        VideoFragment videoFragment2 = VideoFragment.this;
                        videoFragment2.seekTimeline(videoFragment2.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 2);
                    }
                } else if (VideoFragment.this.mEditMode == 1) {
                    if (VideoFragment.this.mCurAnimateSticker != null) {
                        VideoFragment.this.mCurAnimateSticker.translateAnimatedSticker(pointF3);
                        VideoFragment videoFragment3 = VideoFragment.this;
                        videoFragment3.updateAnimateStickerCoordinate(videoFragment3.mCurAnimateSticker);
                        VideoFragment videoFragment4 = VideoFragment.this;
                        videoFragment4.seekTimeline(videoFragment4.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 4);
                    }
                } else if (VideoFragment.this.mEditMode == 2) {
                    VideoFragment videoFragment5 = VideoFragment.this;
                    if (videoFragment5.checkInLiveWindow(videoFragment5.mDrawRect.getDrawRect())) {
                        VideoFragment videoFragment6 = VideoFragment.this;
                        videoFragment6.updateWaterMarkPositionOnDrag(pointF3, videoFragment6.mDrawRect.getDrawRect());
                        int i = VideoFragment.this.waterType;
                        if (i == 0) {
                            VideoFragment.this.mTimeline.deleteWatermark();
                            VideoFragment.this.refreshLiveWindowFrame();
                            TimelineData.getInstance().getMeicamWaterMark().setList(VideoFragment.this.mDrawRect.getDrawRect());
                        } else if (i == 1 || i == 2) {
                            float[] fxData = VideoFragment.this.getFxData();
                            VideoFragment.this.nvsTimelineVideoFx.setRegion(fxData);
                            VideoFragment.this.refreshLiveWindowFrame();
                            VideoFragment.this.meicamTimelineVideoFxClip.setList(VideoFragment.this.mDrawRect.getDrawRect());
                            VideoFragment.this.meicamFxParamSecond.setValue(fxData);
                        }
                    } else {
                        return;
                    }
                } else if (VideoFragment.this.mEditMode == 4 && VideoFragment.this.mCurCompoundCaption != null) {
                    VideoFragment.this.mCurCompoundCaption.translateCaption(pointF3);
                    VideoFragment videoFragment7 = VideoFragment.this;
                    videoFragment7.updateCompoundCaptionCoordinate(videoFragment7.mCurCompoundCaption);
                    VideoFragment videoFragment8 = VideoFragment.this;
                    videoFragment8.seekTimeline(videoFragment8.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 2);
                }
                if (VideoFragment.this.mAssetEditListener != null) {
                    VideoFragment.this.mAssetEditListener.onAssetTranstion(VideoFragment.this.mEditMode);
                }
            }

            @Override // com.meishe.player.view.DrawRect.OnTouchListener
            public void onScaleAndRotate(float f, PointF pointF, float f2) {
                PointF mapViewToCanonical = VideoFragment.this.mLiveWindow.mapViewToCanonical(pointF);
                if (VideoFragment.this.mEditMode == 0) {
                    if (VideoFragment.this.mCurCaption != null) {
                        VideoFragment.this.mCurCaption.scaleCaption(f, mapViewToCanonical);
                        VideoFragment.this.mCurCaption.rotateCaption(f2);
                        VideoFragment videoFragment = VideoFragment.this;
                        videoFragment.updateCaptionCoordinate(videoFragment.mCurCaption);
                        VideoFragment videoFragment2 = VideoFragment.this;
                        videoFragment2.seekTimeline(videoFragment2.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 2);
                    }
                } else if (VideoFragment.this.mEditMode == 1) {
                    if (VideoFragment.this.mCurAnimateSticker != null) {
                        VideoFragment.this.mCurAnimateSticker.scaleAnimatedSticker(f, mapViewToCanonical);
                        VideoFragment.this.mCurAnimateSticker.rotateAnimatedSticker(f2);
                        VideoFragment videoFragment3 = VideoFragment.this;
                        videoFragment3.updateAnimateStickerCoordinate(videoFragment3.mCurAnimateSticker);
                        VideoFragment videoFragment4 = VideoFragment.this;
                        videoFragment4.seekTimeline(videoFragment4.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 4);
                    }
                } else if (VideoFragment.this.mEditMode == 4) {
                    if (VideoFragment.this.mCurCompoundCaption != null) {
                        VideoFragment.this.mCurCompoundCaption.scaleCaption(f, mapViewToCanonical);
                        VideoFragment.this.mCurCompoundCaption.rotateCaption(f2, mapViewToCanonical);
                        float scaleX = VideoFragment.this.mCurCompoundCaption.getScaleX();
                        float scaleY = VideoFragment.this.mCurCompoundCaption.getScaleY();
                        if (scaleX <= 1.0f && scaleY <= 1.0f) {
                            VideoFragment.this.mCurCompoundCaption.setScaleX(1.0f);
                            VideoFragment.this.mCurCompoundCaption.setScaleY(1.0f);
                        }
                        VideoFragment videoFragment5 = VideoFragment.this;
                        videoFragment5.updateCompoundCaptionCoordinate(videoFragment5.mCurCompoundCaption);
                        VideoFragment videoFragment6 = VideoFragment.this;
                        videoFragment6.seekTimeline(videoFragment6.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 2);
                    }
                } else if (VideoFragment.this.mEditMode == 2 && VideoFragment.this.waterType == 0) {
                    VideoFragment videoFragment7 = VideoFragment.this;
                    videoFragment7.updateWaterMarkPositionOnScaleAndRotate(f, pointF, f2, videoFragment7.mDrawRect.getDrawRect());
                    VideoFragment.this.mTimeline.deleteWatermark();
                    VideoFragment.this.refreshLiveWindowFrame();
                    TimelineData.getInstance().getMeicamWaterMark().setList(VideoFragment.this.mDrawRect.getDrawRect());
                }
                if (VideoFragment.this.mAssetEditListener != null) {
                    VideoFragment.this.mAssetEditListener.onAssetScale(VideoFragment.this.mEditMode);
                }
            }

            @Override // com.meishe.player.view.DrawRect.OnTouchListener
            public void onScaleXandY(float f, float f2, PointF pointF) {
                if (VideoFragment.this.mEditMode == 2) {
                    int i = VideoFragment.this.waterType;
                    if (i == 1 || i == 2) {
                        VideoFragment videoFragment = VideoFragment.this;
                        videoFragment.updateWaterMarkPositionOnXAndY(f, f2, pointF, videoFragment.mDrawRect.getDrawRect());
                        float[] fxData = VideoFragment.this.getFxData();
                        VideoFragment.this.nvsTimelineVideoFx.setRegion(fxData);
                        VideoFragment.this.refreshLiveWindowFrame();
                        VideoFragment.this.meicamTimelineVideoFxClip.setList(VideoFragment.this.mDrawRect.getDrawRect());
                        if (VideoFragment.this.meicamFxParamSecond != null) {
                            VideoFragment.this.meicamFxParamSecond.setValue(fxData);
                        }
                    }
                }
            }

            @Override // com.meishe.player.view.DrawRect.OnTouchListener
            public void onDel() {
                if (VideoFragment.this.mAssetEditListener != null) {
                    VideoFragment.this.mAssetEditListener.onAssetDelete(VideoFragment.this.mEditMode);
                }
            }

            @Override // com.meishe.player.view.DrawRect.OnTouchListener
            public void onTouchDown(PointF pointF) {
                if (VideoFragment.this.mAssetEditListener != null) {
                    VideoFragment.this.mAssetEditListener.onAssetSelected(pointF);
                }
            }

            @Override // com.meishe.player.view.DrawRect.OnTouchListener
            public void onAlignClick() {
                if (VideoFragment.this.mEditMode == 0 && VideoFragment.this.mCurCaption != null) {
                    int textAlignment = VideoFragment.this.mCurCaption.getTextAlignment();
                    if (textAlignment == 0) {
                        VideoFragment.this.mCurCaption.setTextAlignment(1);
                        VideoFragment.this.setAlignIndex(1);
                    } else if (textAlignment == 1) {
                        VideoFragment.this.mCurCaption.setTextAlignment(2);
                        VideoFragment.this.setAlignIndex(2);
                    } else if (textAlignment == 2) {
                        VideoFragment.this.mCurCaption.setTextAlignment(0);
                        VideoFragment.this.setAlignIndex(0);
                    }
                    VideoFragment videoFragment = VideoFragment.this;
                    videoFragment.seekTimeline(videoFragment.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 2);
                    if (VideoFragment.this.mAssetEditListener != null) {
                        VideoFragment.this.mAssetEditListener.onAssetAlign(VideoFragment.this.mCurCaption.getTextAlignment());
                    }
                }
            }

            @Override // com.meishe.player.view.DrawRect.OnTouchListener
            public void onHorizFlipClick() {
                if (VideoFragment.this.mEditMode == 1 && VideoFragment.this.mCurAnimateSticker != null) {
                    boolean z = !VideoFragment.this.mCurAnimateSticker.getHorizontalFlip();
                    VideoFragment.this.mCurAnimateSticker.setHorizontalFlip(z);
                    VideoFragment videoFragment = VideoFragment.this;
                    videoFragment.updateAnimateStickerCoordinate(videoFragment.mCurAnimateSticker);
                    VideoFragment videoFragment2 = VideoFragment.this;
                    videoFragment2.seekTimeline(videoFragment2.mStreamingContext.getTimelineCurrentPosition(VideoFragment.this.mTimeline), 4);
                    if (VideoFragment.this.mAssetEditListener != null) {
                        VideoFragment.this.mAssetEditListener.onAssetHorizFlip(z);
                    }
                }
            }

            @Override // com.meishe.player.view.DrawRect.OnTouchListener
            public void onBeyondDrawRectClick() {
                if (VideoFragment.this.mAssetEditListener != null) {
                    VideoFragment.this.mAssetEditListener.onBeyondDrawRectClick();
                }
            }

            @Override // com.meishe.player.view.DrawRect.OnTouchListener
            public void onTouchUp() {
                if (VideoFragment.this.mAssetEditListener != null) {
                    VideoFragment.this.mAssetEditListener.onTouchUp(VideoFragment.this.mEditMode);
                }
            }
        });
        this.mDrawRect.setDrawRectClickListener(new DrawRect.onDrawRectClickListener() {
            /* class com.meishe.player.view.VideoFragment.AnonymousClass9 */

            @Override // com.meishe.player.view.DrawRect.onDrawRectClickListener
            public void onDrawRectClick(int i) {
                if (VideoFragment.this.mEditMode == 0) {
                    if (VideoFragment.this.mCaptionTextEditListener != null) {
                        VideoFragment.this.mCaptionTextEditListener.onCaptionTextEdit();
                    }
                } else if (VideoFragment.this.mEditMode == 4 && VideoFragment.this.mCompoundCaptionListener != null) {
                    VideoFragment.this.mCompoundCaptionListener.onCaptionIndex(i);
                }
            }
        });
        this.mDrawRect.setStickerMuteListenser(new DrawRect.onStickerMuteListenser() {
            /* class com.meishe.player.view.VideoFragment.AnonymousClass10 */

            @Override // com.meishe.player.view.DrawRect.onStickerMuteListenser
            public void onStickerMute() {
                if (VideoFragment.this.mCurAnimateSticker != null) {
                    VideoFragment videoFragment = VideoFragment.this;
                    videoFragment.mStickerMuteIndex = videoFragment.mStickerMuteIndex == 0 ? 1 : 0;
                    float f = VideoFragment.this.mStickerMuteIndex == 0 ? 1.0f : 0.0f;
                    VideoFragment.this.mCurAnimateSticker.setVolumeGain(f, f);
                    VideoFragment videoFragment2 = VideoFragment.this;
                    videoFragment2.setStickerMuteIndex(videoFragment2.mStickerMuteIndex);
                    if (VideoFragment.this.mStickerMuteListener != null) {
                        VideoFragment.this.mStickerMuteListener.onStickerMute();
                    }
                }
            }
        });
    }

    private void setPointFListLiveWindow(int i, int i2) {
        float f = (float) 0;
        this.pointFListLiveWindow = setFourPointToList(f, (float) i, f, (float) i2);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateWaterMarkPositionOnDrag(PointF pointF, List<PointF> list) {
        ArrayList arrayList = new ArrayList();
        for (PointF pointF2 : list) {
            arrayList.add(new PointF(pointF2.x + pointF.x, pointF2.y - pointF.y));
        }
        if (checkInLiveWindow(arrayList)) {
            this.mDrawRect.setDrawRect(arrayList, 2);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateWaterMarkPositionOnXAndY(float f, float f2, PointF pointF, List<PointF> list) {
        float abs = Math.abs(list.get(0).x - list.get(3).x);
        float f3 = (abs / 2.0f) * f;
        float abs2 = (Math.abs(list.get(0).y - list.get(1).y) / 2.0f) * f2;
        refreshWaterMarkByFourPoint(pointF.x - f3, pointF.x + f3, pointF.y - abs2, pointF.y + abs2);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateWaterMarkPositionOnScaleAndRotate(float f, PointF pointF, float f2, List<PointF> list) {
        float abs = Math.abs(list.get(0).x - list.get(3).x);
        float f3 = (abs / 2.0f) * f;
        float abs2 = (Math.abs(list.get(0).y - list.get(1).y) / 2.0f) * f;
        refreshWaterMarkByFourPoint(pointF.x - f3, pointF.x + f3, pointF.y - abs2, pointF.y + abs2);
    }

    private void refreshWaterMarkByFourPoint(float f, float f2, float f3, float f4) {
        List<PointF> fourPointToList = setFourPointToList(f, f2, f3, f4);
        if (checkInLiveWindow(fourPointToList)) {
            this.mDrawRect.setDrawRect(fourPointToList, 2);
        }
    }

    public void updateCaptionCoordinate(NvsTimelineCaption nvsTimelineCaption) {
        List<PointF> boundingRectangleVertices;
        if (nvsTimelineCaption != null && (boundingRectangleVertices = nvsTimelineCaption.getBoundingRectangleVertices()) != null && boundingRectangleVertices.size() >= 4) {
            this.mDrawRect.setDrawRect(getAssetViewVerticesList(boundingRectangleVertices), 0);
        }
    }

    public void setCurAnimateSticker(NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker) {
        this.mCurAnimateSticker = nvsTimelineAnimatedSticker;
    }

    public void setStickerMuteIndex(int i) {
        this.mStickerMuteIndex = i;
        this.mDrawRect.setStickerMuteIndex(i);
    }

    public NvsTimelineAnimatedSticker getCurAnimateSticker() {
        return this.mCurAnimateSticker;
    }

    public void updateAnimateStickerCoordinate(NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker) {
        List<PointF> boundingRectangleVertices;
        if (nvsTimelineAnimatedSticker != null && (boundingRectangleVertices = nvsTimelineAnimatedSticker.getBoundingRectangleVertices()) != null && boundingRectangleVertices.size() >= 4) {
            if (nvsTimelineAnimatedSticker.getHorizontalFlip()) {
                Collections.swap(boundingRectangleVertices, 0, 3);
                Collections.swap(boundingRectangleVertices, 1, 2);
            }
            this.mDrawRect.setDrawRect(getAssetViewVerticesList(boundingRectangleVertices), 1);
        }
    }

    public void changeStickerRectVisible() {
        if (this.mEditMode == 1) {
            setDrawRectVisible(isSelectedAnimateSticker() ? 0 : 8);
        }
    }

    public void selectAnimateStickerByHandClick(PointF pointF) {
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null) {
            List<NvsTimelineAnimatedSticker> animatedStickersByTimelinePosition = nvsTimeline.getAnimatedStickersByTimelinePosition(this.mStreamingContext.getTimelineCurrentPosition(nvsTimeline));
            if (animatedStickersByTimelinePosition.size() > 1) {
                for (int i = 0; i < animatedStickersByTimelinePosition.size(); i++) {
                    NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker = animatedStickersByTimelinePosition.get(i);
                    List<PointF> assetViewVerticesList = getAssetViewVerticesList(nvsTimelineAnimatedSticker.getBoundingRectangleVertices());
                    if (this.mDrawRect.clickPointIsInnerDrawRect(assetViewVerticesList, (int) pointF.x, (int) pointF.y)) {
                        this.mDrawRect.setDrawRect(assetViewVerticesList, 1);
                        this.mCurAnimateSticker = nvsTimelineAnimatedSticker;
                        return;
                    }
                }
            }
        }
    }

    public DrawRect getDrawRectView() {
        return this.mDrawRect;
    }

    public void setMuteVisible(boolean z) {
        this.mDrawRect.setMuteVisible(z);
    }

    private boolean isSelectedAnimateSticker() {
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker = this.mCurAnimateSticker;
        return nvsTimelineAnimatedSticker != null && timelineCurrentPosition >= nvsTimelineAnimatedSticker.getInPoint() && timelineCurrentPosition <= this.mCurAnimateSticker.getOutPoint();
    }

    public void setAlignIndex(int i) {
        this.mDrawRect.setAlignIndex(i);
    }

    public void setCurCaption(NvsTimelineCaption nvsTimelineCaption) {
        this.mCurCaption = nvsTimelineCaption;
    }

    public NvsTimelineCaption getCurCaption() {
        return this.mCurCaption;
    }

    public void setCurCompoundCaption(NvsTimelineCompoundCaption nvsTimelineCompoundCaption) {
        this.mCurCompoundCaption = nvsTimelineCompoundCaption;
    }

    public NvsTimelineCompoundCaption getCurrCompoundCaption() {
        return this.mCurCompoundCaption;
    }

    public void updateCompoundCaptionCoordinate(NvsTimelineCompoundCaption nvsTimelineCompoundCaption) {
        List<PointF> compoundBoundingVertices;
        if (!(nvsTimelineCompoundCaption == null || (compoundBoundingVertices = nvsTimelineCompoundCaption.getCompoundBoundingVertices(2)) == null || compoundBoundingVertices.size() < 4)) {
            List<PointF> assetViewVerticesList = getAssetViewVerticesList(compoundBoundingVertices);
            ArrayList arrayList = new ArrayList();
            int captionCount = nvsTimelineCompoundCaption.getCaptionCount();
            for (int i = 0; i < captionCount; i++) {
                List<PointF> captionBoundingVertices = nvsTimelineCompoundCaption.getCaptionBoundingVertices(i, 0);
                if (captionBoundingVertices != null && captionBoundingVertices.size() >= 4) {
                    arrayList.add(getAssetViewVerticesList(captionBoundingVertices));
                }
            }
            this.mDrawRect.setCompoundDrawRect(assetViewVerticesList, arrayList, 4);
        }
    }

    public void setDrawRectList(List<PointF> list) {
        this.mDrawRect.setListPointF(list);
    }

    public void changeCompoundCaptionRectVisible() {
        if (this.mEditMode == 4) {
            setDrawRectVisible(false);
        }
    }

    public void changeCaptionRectVisible() {
        if (this.mEditMode == 0) {
            setDrawRectVisible(isSelectedCaption() ? 0 : 8);
        }
    }

    private boolean isSelectedCompoundCaption() {
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        NvsTimelineCompoundCaption nvsTimelineCompoundCaption = this.mCurCompoundCaption;
        return nvsTimelineCompoundCaption != null && timelineCurrentPosition >= nvsTimelineCompoundCaption.getInPoint() && timelineCurrentPosition <= this.mCurCompoundCaption.getOutPoint();
    }

    private boolean isSelectedCaption() {
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        NvsTimelineCaption nvsTimelineCaption = this.mCurCaption;
        return nvsTimelineCaption != null && timelineCurrentPosition >= nvsTimelineCaption.getInPoint() && timelineCurrentPosition <= this.mCurCaption.getOutPoint();
    }

    public boolean curPointIsInnerDrawRect(int i, int i2) {
        return this.mDrawRect.curPointIsInnerDrawRect(i, i2);
    }

    public boolean selectCompoundCaptionByHandClick(PointF pointF) {
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline == null) {
            return false;
        }
        List<NvsTimelineCompoundCaption> compoundCaptionsByTimelinePosition = nvsTimeline.getCompoundCaptionsByTimelinePosition(this.mStreamingContext.getTimelineCurrentPosition(nvsTimeline));
        if (compoundCaptionsByTimelinePosition.size() < 1) {
            return false;
        }
        for (int i = 0; i < compoundCaptionsByTimelinePosition.size(); i++) {
            NvsTimelineCompoundCaption nvsTimelineCompoundCaption = compoundCaptionsByTimelinePosition.get(i);
            List<PointF> assetViewVerticesList = getAssetViewVerticesList(nvsTimelineCompoundCaption.getCompoundBoundingVertices(2));
            if (this.mDrawRect.clickPointIsInnerDrawRect(assetViewVerticesList, (int) pointF.x, (int) pointF.y)) {
                this.mDrawRect.setDrawRectVisible(true);
                this.mDrawRect.setDrawRect(assetViewVerticesList, 4);
                this.mCurCompoundCaption = nvsTimelineCompoundCaption;
                return true;
            }
            this.mCurCompoundCaption = null;
        }
        return false;
    }

    public void selectCaptionByHandClick(PointF pointF) {
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null) {
            List<NvsTimelineCaption> captionsByTimelinePosition = nvsTimeline.getCaptionsByTimelinePosition(this.mStreamingContext.getTimelineCurrentPosition(nvsTimeline));
            if (captionsByTimelinePosition.size() > 1) {
                for (int i = 0; i < captionsByTimelinePosition.size(); i++) {
                    NvsTimelineCaption nvsTimelineCaption = captionsByTimelinePosition.get(i);
                    List<PointF> assetViewVerticesList = getAssetViewVerticesList(nvsTimelineCaption.getBoundingRectangleVertices());
                    if (this.mDrawRect.clickPointIsInnerDrawRect(assetViewVerticesList, (int) pointF.x, (int) pointF.y)) {
                        this.mDrawRect.setDrawRect(assetViewVerticesList, 0);
                        this.mCurCaption = nvsTimelineCaption;
                        return;
                    }
                }
            }
        }
    }

    public boolean clickPointIsInnerDrawRect(List<PointF> list, int i, int i2) {
        return this.mDrawRect.clickPointIsInnerDrawRect(getAssetViewVerticesList(list), i, i2);
    }

    public List<PointF> getDrawRect() {
        return this.mDrawRect.getDrawRect();
    }

    public void selectWaterMark(int i) {
        setPointFListLiveWindow(this.mLiveWindow.getWidth(), this.mLiveWindow.getHeight());
        setEditMode(2);
        this.waterType = i;
        TimelineData.getInstance().getMeicamWaterMark().setPointList(this.mLiveWindow, this.mTimeline);
        List<PointF> list = TimelineData.getInstance().getMeicamWaterMark().getList();
        if (list != null) {
            this.mDrawRect.setWaterMarkBitmap(TimelineData.getInstance().getMeicamWaterMark().getWatermarkPath(), (int) (list.get(2).x - list.get(1).x));
            this.mDrawRect.setDrawRectVisible(true);
            this.mDrawRect.setWaterMackType(i);
            this.mDrawRect.setDrawRect(list, 2);
        }
    }

    public void selectEffect(int i) {
        setPointFListLiveWindow(this.mLiveWindow.getWidth(), this.mLiveWindow.getHeight());
        setEditMode(2);
        if (TimelineData.getInstance().getMeicamTimelineVideoFxClipList().size() != 0) {
            setEffectByPoint(i);
            this.waterType = i;
        }
        addWaterToTimeline();
    }

    public void addWaterToTimeline() {
        this.mTimeline.deleteWatermark();
        NvsVideoResolution videoRes = this.mTimeline.getVideoRes();
        int i = videoRes.imageWidth;
        int i2 = videoRes.imageHeight;
        MeicamWaterMark meicamWaterMark = TimelineData.getInstance().getMeicamWaterMark();
        List<PointF> list = meicamWaterMark.getList();
        if (list != null) {
            PointF mapViewToCanonical = this.mLiveWindow.mapViewToCanonical(list.get(2));
            PointF mapViewToCanonical2 = this.mLiveWindow.mapViewToCanonical(list.get(0));
            int abs = (int) Math.abs(mapViewToCanonical.x - mapViewToCanonical2.x);
            int abs2 = (int) Math.abs(mapViewToCanonical.y - mapViewToCanonical2.y);
            int i3 = (int) (((float) (i / 2)) + mapViewToCanonical2.x);
            int i4 = (int) (((float) (i2 / 2)) - mapViewToCanonical2.y);
            this.mTimeline.addWatermark(meicamWaterMark.getWatermarkPath(), abs, abs2, 1.0f, 1, i3, i4);
            meicamWaterMark.setWatermarkW(abs);
            meicamWaterMark.setWatermarkH(abs2);
            meicamWaterMark.setWatermarkX(i3);
            meicamWaterMark.setWatermarkY(i4);
            refreshLiveWindowFrame();
        }
    }

    public void changeRatioAddWaterToTimeline() {
        MeicamWaterMark meicamWaterMark = TimelineData.getInstance().getMeicamWaterMark();
        if (meicamWaterMark != null && !TextUtils.isEmpty(meicamWaterMark.getWatermarkPath())) {
            this.mTimeline.deleteWatermark();
            this.mTimeline.addWatermark(meicamWaterMark.getWatermarkPath(), meicamWaterMark.getWatermarkW(), meicamWaterMark.getWatermarkH(), 1.0f, 1, meicamWaterMark.getWatermarkX(), meicamWaterMark.getWatermarkY());
            refreshLiveWindowFrame();
        }
    }

    public void removeWaterToTimeline() {
        if (this.waterType == 0) {
            this.mTimeline.deleteWatermark();
            TimelineData.getInstance().setMeicamWaterMark(null);
        } else {
            List<MeicamTimelineVideoFxClip> meicamTimelineVideoFxClipList = TimelineData.getInstance().getMeicamTimelineVideoFxClipList();
            if (meicamTimelineVideoFxClipList != null && meicamTimelineVideoFxClipList.size() > 0) {
                this.nvsTimelineVideoFx = (NvsTimelineVideoFx) meicamTimelineVideoFxClipList.get(0).getObject();
            }
            this.mTimeline.removeTimelineVideoFx(this.nvsTimelineVideoFx);
            TimelineData.getInstance().setMeicamTimelineVideoFxClipList(null);
        }
        setDrawRectVisible(false);
        refreshLiveWindowFrame();
    }

    public void setEffectByPoint(int i) {
        setPointFListLiveWindow(this.mLiveWindow.getWidth(), this.mLiveWindow.getHeight());
        this.waterType = i;
        if (this.waterType == 3) {
            removeWaterToTimeline();
            return;
        }
        List<MeicamTimelineVideoFxClip> meicamTimelineVideoFxClipList = TimelineData.getInstance().getMeicamTimelineVideoFxClipList();
        if (meicamTimelineVideoFxClipList.size() > 0) {
            this.meicamTimelineVideoFxClip = meicamTimelineVideoFxClipList.get(0);
            this.meicamTimelineVideoFxClip.setPointList(this.mLiveWindow);
            this.mDrawRect.setWaterMackType(this.waterType);
            this.mDrawRect.setDrawRect(this.meicamTimelineVideoFxClip.getList(), 2);
        } else {
            this.meicamTimelineVideoFxClip = new MeicamTimelineVideoFxClip();
            meicamTimelineVideoFxClipList.add(this.meicamTimelineVideoFxClip);
            setPoint(null, this.waterType);
        }
        this.meicamTimelineVideoFxClip = meicamTimelineVideoFxClipList.get(0);
        this.meicamTimelineVideoFxClip.setList(this.mDrawRect.getDrawRect());
        this.meicamTimelineVideoFxClip.setIntensity(1.0f);
        this.meicamTimelineVideoFxClip.setClipType(0);
        this.meicamTimelineVideoFxClip.setInPoint(0);
        this.meicamTimelineVideoFxClip.setOutPoint(this.mTimeline.getDuration());
        List<MeicamFxParam> meicamFxParamList = this.meicamTimelineVideoFxClip.getMeicamFxParamList();
        this.mTimeline.removeTimelineVideoFx((NvsTimelineVideoFx) this.meicamTimelineVideoFxClip.getObject());
        int i2 = this.waterType;
        if (i2 == 1) {
            this.meicamTimelineVideoFxClip.setDesc("Mosaic");
            this.nvsTimelineVideoFx = this.meicamTimelineVideoFxClip.bindToTimeline(this.mTimeline);
            this.nvsTimelineVideoFx.setFilterIntensity(1.0f);
            this.nvsTimelineVideoFx.setRegional(true);
            this.nvsTimelineVideoFx.setFloatVal("Unit Size", 0.05000000074505806d);
            float[] fxData = getFxData();
            this.nvsTimelineVideoFx.setRegion(fxData);
            this.nvsTimelineVideoFx.setRegionalFeatherWidth(0.0f);
            if (meicamFxParamList != null) {
                meicamFxParamList.clear();
                meicamFxParamList = new ArrayList<>();
            }
            meicamFxParamList.add(new MeicamFxParam(MeicamFxParam.TYPE_FLOAT, "Unit Size", Float.valueOf(0.05f)));
            this.meicamFxParamSecond = new MeicamFxParam("float[]", "Unit Size", fxData);
            meicamFxParamList.add(this.meicamFxParamSecond);
            this.meicamFxParamSecond = meicamFxParamList.get(1);
            this.meicamTimelineVideoFxClip.setMeicamFxParamList(meicamFxParamList);
        } else if (i2 == 2) {
            this.meicamTimelineVideoFxClip.setDesc("Gaussian Blur");
            this.nvsTimelineVideoFx = this.meicamTimelineVideoFxClip.bindToTimeline(this.mTimeline);
            this.nvsTimelineVideoFx.setFilterIntensity(1.0f);
            this.nvsTimelineVideoFx.setRegional(true);
            float[] fxData2 = getFxData();
            this.nvsTimelineVideoFx.setRegion(fxData2);
            this.nvsTimelineVideoFx.setRegionalFeatherWidth(0.0f);
            if (meicamFxParamList != null) {
                meicamFxParamList.clear();
                meicamFxParamList = new ArrayList<>();
            }
            this.meicamFxParamSecond = new MeicamFxParam("float[]", "Unit Size", fxData2);
            meicamFxParamList.add(this.meicamFxParamSecond);
            this.meicamFxParamSecond = meicamFxParamList.get(0);
            this.meicamTimelineVideoFxClip.setMeicamFxParamList(meicamFxParamList);
        }
        this.meicamTimelineVideoFxClip.loadData(this.nvsTimelineVideoFx);
        refreshLiveWindowFrame();
    }

    public void setWaterMarkDrawableByPath(String str, int i) {
        setPointFListLiveWindow(this.mLiveWindow.getWidth(), this.mLiveWindow.getHeight());
        this.waterType = i;
        this.mTimeline.deleteWatermark();
        setPoint(str, i);
        TimelineData.getInstance().getMeicamWaterMark().setList(this.mDrawRect.getDrawRect());
    }

    private void setPoint(String str, int i) {
        int width = this.mLiveWindow.getWidth();
        int height = this.mLiveWindow.getHeight();
        Point picturePoint = ImageConverter.getPicturePoint(str, getContext());
        int dimension = (int) getContext().getResources().getDimension(R.dimen.edit_waterMark_width);
        int dimension2 = (int) getContext().getResources().getDimension(R.dimen.edit_waterMark_height);
        if (picturePoint != null) {
            dimension2 = (picturePoint.y * dimension) / picturePoint.x;
        }
        setWaterMarkDrawableByPath(str, dimension, (width / 2) - (dimension / 2), (height / 2) - (dimension2 / 2), i);
    }

    public float[] getFxData() {
        List<PointF> drawRect = this.mDrawRect.getDrawRect();
        return new float[]{this.mLiveWindow.mapViewToNormalized(drawRect.get(0)).x, this.mLiveWindow.mapViewToNormalized(drawRect.get(0)).y, this.mLiveWindow.mapViewToNormalized(drawRect.get(1)).x, this.mLiveWindow.mapViewToNormalized(drawRect.get(1)).y, this.mLiveWindow.mapViewToNormalized(drawRect.get(2)).x, this.mLiveWindow.mapViewToNormalized(drawRect.get(2)).y, this.mLiveWindow.mapViewToNormalized(drawRect.get(3)).x, this.mLiveWindow.mapViewToNormalized(drawRect.get(3)).y};
    }

    public void setWaterMarkDrawableByPath(String str, int i, int i2, int i3, int i4) {
        this.mDrawRect.setWaterMarkBitmapByImgPath(str, i, i2, i3, i4);
    }

    public List<PointF> getAssetViewVerticesList(List<PointF> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(this.mLiveWindow.mapCanonicalToView(list.get(i)));
        }
        return arrayList;
    }

    public void setWaterMarkDrawRect(List<PointF> list) {
        this.mDrawRect.setDrawRect(list, 2);
    }

    private List<PointF> setFourPointToList(float f, float f2, float f3, float f4) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PointF(f, f3));
        arrayList.add(new PointF(f, f4));
        arrayList.add(new PointF(f2, f4));
        arrayList.add(new PointF(f2, f3));
        return arrayList;
    }

    public void setDrawRectVisible(int i) {
        this.mDrawRect.setVisibility(i);
    }

    public void setPipTransformViewVisible(int i) {
        this.mPipTransformView.setVisibility(i);
    }

    public void setDrawRectVisible(boolean z) {
        this.mDrawRect.bringToFront();
        this.mDrawRect.setDrawRectVisible(z);
    }

    public void stopToCheckDrawRectVisible(boolean z, String str) {
        String str2;
        boolean z2;
        int viewMode = this.mDrawRect.getViewMode();
        if (viewMode != 1 || this.mCurAnimateSticker == null) {
            z2 = false;
            str2 = "xxx";
        } else {
            str2 = CommonData.CLIP_STICKER;
            z2 = true;
        }
        if (viewMode == 0 && this.mCurCaption != null) {
            str2 = CommonData.CLIP_CAPTION;
            z2 = true;
        }
        if (viewMode == 4 && this.mCurCompoundCaption != null) {
            str2 = CommonData.CLIP_COMPOUND_CAPTION;
            z2 = true;
        }
        if (z2 && z && str2.equals(str)) {
            setDrawRectVisible(true);
        }
    }

    public void setDrawRectOutOfTime(long j) {
        NvsTimelineCompoundCaption nvsTimelineCompoundCaption;
        NvsTimelineCaption nvsTimelineCaption;
        NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker;
        int viewMode = this.mDrawRect.getViewMode();
        boolean z = true;
        if (viewMode != 1 ? viewMode != 0 ? viewMode != 4 || (nvsTimelineCompoundCaption = this.mCurCompoundCaption) == null || (j >= nvsTimelineCompoundCaption.getInPoint() && j <= this.mCurCompoundCaption.getOutPoint()) : (nvsTimelineCaption = this.mCurCaption) == null || (j >= nvsTimelineCaption.getInPoint() && j <= this.mCurCaption.getOutPoint()) : (nvsTimelineAnimatedSticker = this.mCurAnimateSticker) == null || (j >= nvsTimelineAnimatedSticker.getInPoint() && j <= this.mCurAnimateSticker.getOutPoint())) {
            z = false;
        }
        this.mDrawRect.setOutOfTime(z);
    }

    public void refreshLiveWindowFrame() {
        if (this.playState != 3) {
            seekTimeline(this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline), 0);
        }
    }

    public boolean getDrawRectVisible() {
        return this.mDrawRect.isVisible();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x0036  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean checkInLiveWindow(java.util.List<android.graphics.PointF> r8) {
        /*
            r7 = this;
            java.util.List<android.graphics.PointF> r0 = r7.pointFListLiveWindow
            if (r0 == 0) goto L_0x0055
            r1 = 0
            java.lang.Object r0 = r0.get(r1)
            android.graphics.PointF r0 = (android.graphics.PointF) r0
            float r0 = r0.x
            java.util.List<android.graphics.PointF> r2 = r7.pointFListLiveWindow
            r3 = 2
            java.lang.Object r2 = r2.get(r3)
            android.graphics.PointF r2 = (android.graphics.PointF) r2
            float r2 = r2.x
            java.util.List<android.graphics.PointF> r4 = r7.pointFListLiveWindow
            java.lang.Object r4 = r4.get(r1)
            android.graphics.PointF r4 = (android.graphics.PointF) r4
            float r4 = r4.y
            java.util.List<android.graphics.PointF> r5 = r7.pointFListLiveWindow
            java.lang.Object r3 = r5.get(r3)
            android.graphics.PointF r3 = (android.graphics.PointF) r3
            float r3 = r3.y
            java.util.Iterator r8 = r8.iterator()
        L_0x0030:
            boolean r5 = r8.hasNext()
            if (r5 == 0) goto L_0x0055
            java.lang.Object r5 = r8.next()
            android.graphics.PointF r5 = (android.graphics.PointF) r5
            float r6 = r5.x
            int r6 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r6 < 0) goto L_0x0054
            float r6 = r5.x
            int r6 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r6 > 0) goto L_0x0054
            float r6 = r5.y
            int r6 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r6 < 0) goto L_0x0054
            float r5 = r5.y
            int r5 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r5 <= 0) goto L_0x0030
        L_0x0054:
            return r1
        L_0x0055:
            r8 = 1
            return r8
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.player.view.VideoFragment.checkInLiveWindow(java.util.List):boolean");
    }

    public boolean isDrawRectVisible() {
        return this.mDrawRect.isVisible();
    }

    public NvsTimeline getTimeLine() {
        return this.mTimeline;
    }

    public boolean refreshWaterMark(String str, int i, int i2, int i3, int i4) {
        DrawRect drawRect;
        if ((this.mEditMode != 2 || TextUtils.isEmpty(str)) && (drawRect = this.mDrawRect) != null) {
            drawRect.setDrawRectVisible(false);
            return false;
        }
        setWaterMarkDrawableByPath(str, i, i3, i4, this.waterType);
        return true;
    }

    public void setThemeCaptionSeekListener(OnThemeCaptionSeekListener onThemeCaptionSeekListener) {
        this.mThemeCaptionSeekListener = onThemeCaptionSeekListener;
    }

    public void setLiveWindowClickListener(OnLiveWindowClickListener onLiveWindowClickListener) {
        this.mLiveWindowClickListener = onLiveWindowClickListener;
    }

    public void setCaptionTextEditListener(VideoCaptionTextEditListener videoCaptionTextEditListener) {
        this.mCaptionTextEditListener = videoCaptionTextEditListener;
    }

    public void setStickerMuteListener(OnStickerMuteListener onStickerMuteListener) {
        this.mStickerMuteListener = onStickerMuteListener;
    }

    public void setCompoundCaptionListener(OnCompoundCaptionListener onCompoundCaptionListener) {
        this.mCompoundCaptionListener = onCompoundCaptionListener;
    }

    public void setOnSaveOperationListener(OnSaveOperationListener onSaveOperationListener) {
        this.mOnSaveOperationListener = onSaveOperationListener;
    }

    public void setAssetEditListener(AssetEditListener assetEditListener) {
        this.mAssetEditListener = assetEditListener;
    }

    public void setFragmentLoadFinisedListener(OnFragmentLoadFinisedListener onFragmentLoadFinisedListener) {
        this.mFragmentLoadFinisedListener = onFragmentLoadFinisedListener;
    }

    public void setVideoFragmentCallBack(VideoFragmentListener videoFragmentListener) {
        this.mVideoFragmentCallBack = videoFragmentListener;
    }

    public void setOnBackgroundChangedListener(OnBackgroundChangedListener onBackgroundChangedListener) {
        this.mOnBackgroundChangedListener = onBackgroundChangedListener;
    }
}
