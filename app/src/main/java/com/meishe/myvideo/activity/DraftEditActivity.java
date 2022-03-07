package com.meishe.myvideo.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.base.UMengUtils;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.draft.DraftManager;
import com.meishe.draft.util.DraftFileUtil;
import com.meishe.engine.TimelineUtil;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamAudioClip;
import com.meishe.engine.bean.MeicamCaptionClip;
import com.meishe.engine.bean.MeicamCompoundCaptionClip;
import com.meishe.engine.bean.MeicamStickerClip;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.MeicamVideoFx;
import com.meishe.engine.bean.MeicamVideoTrack;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.TimelineDataUtil;
import com.meishe.engine.util.StoryboardUtil;
import com.meishe.myvideo.adapter.EditorTimelineTransitionAdapter;
import com.meishe.myvideo.application.MeiSheApplication;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.MediaData;
import com.meishe.myvideo.bean.MusicInfo;
import com.meishe.myvideo.bean.menu.EditMenuInfo;
import com.meishe.myvideo.downLoad.AssetDownloadActivity;
import com.meishe.myvideo.edit.OperateData;
import com.meishe.myvideo.edit.manager.OperateManager;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.fragment.BeautyFragment;
import com.meishe.myvideo.fragment.BeautyShapeFragment;
import com.meishe.myvideo.fragment.CaptionColorFragment;
import com.meishe.myvideo.fragment.CaptionFontFragment;
import com.meishe.myvideo.fragment.CaptionLetterSpacingFragment;
import com.meishe.myvideo.fragment.CaptionOutlineFragment;
import com.meishe.myvideo.fragment.CaptionPositionFragment;
import com.meishe.myvideo.fragment.CaptionStyleFragment;
import com.meishe.myvideo.fragment.StickerAllFragment;
import com.meishe.myvideo.fragment.StickerCustomFragment;
import com.meishe.myvideo.fragment.Transition3DFragment;
import com.meishe.myvideo.fragment.TransitionEffectFragment;
import com.meishe.myvideo.fragment.TransitionFragment;
import com.meishe.myvideo.fragment.TransitionGeneralFragment;
import com.meishe.myvideo.fragment.WaterEffectFragment;
import com.meishe.myvideo.fragment.WaterFragment;
import com.meishe.myvideo.interfaces.OnMiddleOperationClickListener;
import com.meishe.myvideo.manager.AudioRecordManager;
import com.meishe.myvideo.manager.ConvertFileManager;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideo.manager.StateManager;
import com.meishe.myvideo.ui.Utils.ClipDragUtils;
import com.meishe.myvideo.ui.Utils.FormatUtils;
import com.meishe.myvideo.ui.bean.BaseUIClip;
import com.meishe.myvideo.ui.trackview.HandView;
import com.meishe.myvideo.ui.trackview.MultiThumbnailSequenceView;
import com.meishe.myvideo.ui.trackview.TrackViewLayout;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.Constants;
import com.meishe.myvideo.util.ImageUtils;
import com.meishe.myvideo.util.MediaScannerUtil;
import com.meishe.myvideo.util.ParameterSettingValues;
import com.meishe.myvideo.util.PathUtils;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideo.util.engine.EditorEngine;
import com.meishe.myvideo.util.ui.ToastUtil;
import com.meishe.myvideo.util.ui.TrackViewDataHelper;
import com.meishe.myvideo.view.MYCanvasBlur;
import com.meishe.myvideo.view.MYCanvasColor;
import com.meishe.myvideo.view.MYCanvasStyle;
import com.meishe.myvideo.view.MYCompoundCaptionEditView;
import com.meishe.myvideo.view.MYCompoundCaptionMenuView;
import com.meishe.myvideo.view.MYEditorParentLayout;
import com.meishe.myvideo.view.MYEditorTimeLine;
import com.meishe.myvideo.view.MYEditorTimelineTrackView;
import com.meishe.myvideo.view.MYMiddleOperationView;
import com.meishe.myvideo.view.MYMultiBottomView;
import com.meishe.myvideo.view.MYRecordMenuView;
import com.meishe.myvideo.view.MYWidthConfirmMenuView;
import com.meishe.myvideo.view.MenuView;
import com.meishe.myvideo.view.editview.BottomControlView;
import com.meishe.myvideo.view.editview.CompileProgress;
import com.meishe.myvideo.view.editview.EditChangeOpacityView;
import com.meishe.myvideo.view.editview.EditChangeSpeedView;
import com.meishe.myvideo.view.editview.EditChangeTransitionView;
import com.meishe.myvideo.view.editview.EditChangeVoiceView;
import com.meishe.myvideo.view.editview.EditChangeVolumeView;
import com.meishe.myvideoapp.R;
import com.meishe.player.view.VideoFragment;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DraftEditActivity extends BaseActivity implements NvsStreamingContext.SeekingCallback, MYEditorTimeLine.OnTimeLineEditorListener, TransitionFragment.TransitionRefreshListener, OnMiddleOperationClickListener, MenuView.OnMenuClickListener, MYEditorTimeLine.OnHandAction, TrackViewLayout.OnTrackViewScrollListener, TrackViewLayout.OnTrackViewDragListener, EditorEngine.OnTimelineChangeListener, HandView.OnHandChangeListener, PixelPerMicrosecondUtil.PixelPerMicrosecondChangeListener {
    private static final int FLAG_PLAY = 1;
    private static final int FLAG_SEQ = 11;
    private static final int FLAG_TRACK_VIEW = 111;
    public static final String INTENT_KEY_DRAFT_DIR = "draft_dir";
    public static final String INTENT_KEY_FROM_TYPE = "from_type";
    public static final int INTENT_VALUE_FROM_TYPE_DRAFT = 1;
    private static final int REQUEST_ASSET_LIST_CODE = 111;
    private static final int SELETE_MUSIC = 112;
    private static final int SELETE_VIDEO = 113;
    public static final int TAB_TYPE_EFFECT = 2;
    public static final int TAB_TYPE_WATER = 1;
    private static final String TAG = "DraftEditActivity";
    public static final String VIDEO_PATH = "video_path";
    private final int MAX_BLUR_STRENGTH = 50;
    private final int REQUEST_CROP_CLIP_CODE = 103;
    private final int REQUEST_PICTURE_IN_PICTURE_CODE = 101;
    private final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 100;
    private final int REQUEST_SELECT_IAMGE_BACKGEOUND_CODE = 102;
    private final int REQUEST_SELECT_IAMGE_WATER = 104;
    private AudioRecordManager mAudioRecordManager = null;
    private View mBtCompileCancel;
    private CaptionColorFragment mCaptionColorFragment = null;
    private CaptionOutlineFragment mCaptionOutlineFragment = null;
    private CaptionStyleFragment mCaptionStyleFragment;
    private View mClCompileProgress;
    private MYCompoundCaptionEditView mCompoundCaptionEditView;
    private ConvertFileManager mConvertFileManager;
    private MeicamVideoClip mCurSelectVideoClip;
    private MeicamCompoundCaptionClip mCurrCompoundCaption = null;
    private MeicamAudioClip mCurrSelectedAudioClip = null;
    private MeicamCaptionClip mCurrSelectedCaption = null;
    private MeicamStickerClip mCurrSelectedSticker = null;
    private EditorTimelineTransitionAdapter.TransitionData mCurrTransitionData;
    private long mCurrentInPoint;
    private long mCurrentPosition;
    private long mCurrentRecordInPoint;
    private int mCurrentTrackIndex = 0;
    private View mDecorView;
    private String mDraftDir;
    private View mEditBack;
    private BottomControlView mEditBottomController;
    private MYMultiBottomView mEditBottomView;
    private MYCanvasBlur mEditCanvasBlurView;
    private MYCanvasColor mEditCanvasColorView;
    private MYCanvasStyle mEditCanvasStyleView;
    private CompileProgress mEditCompileProgress;
    private MYCompoundCaptionMenuView mEditCompoundCaptionView;
    private MenuView mEditMenuView;
    private MYMiddleOperationView mEditOperationView;
    private MYRecordMenuView mEditRecordView;
    private View mEditShare;
    private MYEditorTimeLine mEditTimeline;
    private EditorEngine mEditorEngine;
    private MYEditorParentLayout mEditorParentView;
    private MYEditorTimelineTrackView mEditorTrackView;
    private MYWidthConfirmMenuView mEditorWidthConfirmView;
    private boolean mFirstGetKeyboardHeight = true;
    private int mFromType;
    private boolean mHaveRecordPermission = false;
    private boolean mIsRecording;
    private boolean mIsVisibleForLast = false;
    private int mKeyboardHeight = 0;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = null;
    private OperateManager<OperateData> mOperateManager;
    private MeicamAudioClip mRecordAudioInfo;
    private int mSeekFlag = 1;
    private BaseUIClip mSelectTrackData;
    private StickerAllFragment mStickerAllFragment;
    private StickerCustomFragment mStickerCustomFragment;
    private ArrayList<Fragment> mStickerFragmentArrayList;
    private NvsStreamingContext mStreamingContext;
    private int mTargetTransitionIndex;
    private NvsTimeline mTimeline;
    private LinearLayout mTopOperationBarView;
    private HashMap<Integer, List<BaseUIClip>> mTrackListHashMap = new HashMap<>();
    private TextView mTvCompileProgress;
    private TextView mTvProgressDesc;
    private VideoFragment mVideoFragment;
    private String mVideoSavePath;
    private WaterEffectFragment mWaterEffectFragment;
    private WaterFragment mWaterFragment;
    private int mWaterMarkTabSelectedIndex = 0;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_draft_edit;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
    }

    @Override // com.meicam.sdk.NvsStreamingContext.SeekingCallback
    public void onSeekingTimelinePosition(NvsTimeline nvsTimeline, long j) {
    }

    private void addOnSoftKeyBoardVisibleListener() {
        if (this.mKeyboardHeight <= 0) {
            this.mDecorView = getWindow().getDecorView();
            this.mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass1 */

                public void onGlobalLayout() {
                    int i;
                    if (DraftEditActivity.this.mFirstGetKeyboardHeight || DraftEditActivity.this.mKeyboardHeight <= 0) {
                        Rect rect = new Rect();
                        DraftEditActivity.this.mDecorView.getWindowVisibleDisplayFrame(rect);
                        int i2 = rect.bottom - rect.top;
                        int height = DraftEditActivity.this.mDecorView.getHeight();
                        double d = (double) i2;
                        double d2 = (double) height;
                        Double.isNaN(d);
                        Double.isNaN(d2);
                        boolean z = d / d2 < 0.8d;
                        try {
                            Class<?> cls = Class.forName("com.android.internal.R$dimen");
                            i = DraftEditActivity.this.mContext.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            i = 0;
                        }
                        if (z && z != DraftEditActivity.this.mIsVisibleForLast) {
                            DraftEditActivity draftEditActivity = DraftEditActivity.this;
                            draftEditActivity.mKeyboardHeight = ((height - i2) - i) - ScreenUtils.getNavigationBarHeight(draftEditActivity.mContext);
                            DraftEditActivity.this.mFirstGetKeyboardHeight = false;
                            if (DraftEditActivity.this.mEditBottomView.isShow() && DraftEditActivity.this.mEditBottomView.getType() == 3) {
                                DraftEditActivity.this.mEditBottomView.setKeyboardHeight(DraftEditActivity.this.mKeyboardHeight);
                            }
                            if (DraftEditActivity.this.mCompoundCaptionEditView.isShow()) {
                                DraftEditActivity.this.mCompoundCaptionEditView.setKeyboardHeight(DraftEditActivity.this.mKeyboardHeight);
                            }
                        }
                        DraftEditActivity.this.mIsVisibleForLast = z;
                    }
                }
            };
            this.mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        this.mEditBack = findViewById(R.id.edit_back);
        this.mEditShare = findViewById(R.id.edit_create_save);
        this.mEditOperationView = (MYMiddleOperationView) findViewById(R.id.edit_operation_view);
        this.mEditTimeline = (MYEditorTimeLine) findViewById(R.id.edit_timeline);
        this.mEditRecordView = (MYRecordMenuView) findViewById(R.id.edit_record);
        this.mEditMenuView = (MenuView) findViewById(R.id.edit_menu_view);
        this.mEditBottomController = (BottomControlView) findViewById(R.id.edit_bottom_controller);
        this.mEditCompileProgress = (CompileProgress) findViewById(R.id.edit_compile_progress);
        this.mTvCompileProgress = (TextView) findViewById(R.id.tv_compile_progress);
        this.mClCompileProgress = findViewById(R.id.cl_compile_progress);
        this.mTvProgressDesc = (TextView) findViewById(R.id.tv_compile_info);
        this.mBtCompileCancel = findViewById(R.id.bt_compile_cancel);
        this.mEditBottomView = (MYMultiBottomView) findViewById(R.id.edit_add_sticker);
        this.mEditCompoundCaptionView = (MYCompoundCaptionMenuView) findViewById(R.id.edit_compound_caption_menu_view);
        this.mEditCanvasColorView = (MYCanvasColor) findViewById(R.id.edit_canvas_color_view);
        this.mEditCanvasStyleView = (MYCanvasStyle) findViewById(R.id.edit_canvas_style_view);
        this.mEditCanvasBlurView = (MYCanvasBlur) findViewById(R.id.edit_canvas_blur_view);
        this.mEditorTrackView = (MYEditorTimelineTrackView) findViewById(R.id.editor_track_view);
        this.mEditorTrackView.setOnHandChangeListener(this);
        this.mEditorParentView = (MYEditorParentLayout) findViewById(R.id.editor_parent_view);
        this.mEditorWidthConfirmView = (MYWidthConfirmMenuView) findViewById(R.id.edit_width_confirm_menu);
        this.mTopOperationBarView = (LinearLayout) findViewById(R.id.top_operation_bar_layout);
        this.mEditorParentView.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass2 */

            public void onClick(View view) {
                Logger.d(DraftEditActivity.TAG, "onClick: 点击空白区域" + StateManager.getInstance().getCurrentState());
                if (!StateManager.getInstance().getCurrentState().equals(Constants.STATE_RATIO)) {
                    DraftEditActivity.this.mVideoFragment.setDrawRectVisible(false);
                    DraftEditActivity.this.hideBottomView();
                    StateManager.getInstance().changeState(StateManager.getInstance().getCurrentState(), 3);
                    if (Constants.STATE_EDTI_CUT.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_EDTI_COPY.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_EDIT.equals(StateManager.getInstance().getCurrentState())) {
                        DraftEditActivity.this.mEditTimeline.setUnSelectSpan();
                        StateManager.getInstance().changeState(Constants.STATE_MAIN_MENU, 3);
                    } else if (Constants.STATE_STICKER.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_CAPTION.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_COMPOUND_CAPTION.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_MUSIC.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_DUBBING.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_PIC_IN_PIC.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_FX.equals(StateManager.getInstance().getCurrentState())) {
                        DraftEditActivity.this.mEditorTrackView.clickOutSide();
                    }
                }
            }
        });
        this.mCompoundCaptionEditView = (MYCompoundCaptionEditView) findViewById(R.id.edit_compound_caption_edit_view);
        addVideoFragment();
    }

    private void addVideoFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.mVideoFragment = VideoFragment.newInstance(0);
        supportFragmentManager.beginTransaction().add(R.id.edit_preview_view, this.mVideoFragment).commit();
        supportFragmentManager.beginTransaction().show(this.mVideoFragment);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        Logger.d(TAG, "init data...");
        Intent intent = getIntent();
        this.mFromType = intent.getIntExtra("from_type", -1);
        if (this.mFromType == 1) {
            this.mDraftDir = intent.getStringExtra(INTENT_KEY_DRAFT_DIR);
            TimelineData.getInstance().setDraftDir(this.mDraftDir);
        }
        this.mEditorEngine = EditorEngine.getInstance();
        this.mEditorEngine.setEidtType(this.mFromType);
        this.mEditorEngine.setOnTimelineChangeListener(this);
        this.mStreamingContext = this.mEditorEngine.getStreamingContext();
        this.mStreamingContext.setSeekingCallback(this);
        this.mConvertFileManager = ConvertFileManager.getInstance();
        addOnSoftKeyBoardVisibleListener();
        this.mStreamingContext.setCompileCallback(new NvsStreamingContext.CompileCallback() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass3 */

            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback
            public void onCompileFailed(NvsTimeline nvsTimeline) {
            }

            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback
            public void onCompileProgress(NvsTimeline nvsTimeline, int i) {
                DraftEditActivity.this.setCenterProgress(i);
            }

            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback
            public void onCompileFinished(NvsTimeline nvsTimeline) {
                if (new File(DraftEditActivity.this.mVideoSavePath).exists()) {
                    MediaScannerUtil.scanFile(DraftEditActivity.this.mVideoSavePath, "video/mp4");
                }
            }
        });
        this.mStreamingContext.setCompileCallback2(new NvsStreamingContext.CompileCallback2() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass4 */

            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback2
            public void onCompileCompleted(NvsTimeline nvsTimeline, boolean z) {
                if (!z) {
                    DraftEditActivity.this.jumpToCompile();
                } else {
                    ToastUtil.showToast(DraftEditActivity.this.getApplicationContext(), (int) R.string.compile_cancel);
                }
            }
        });
        this.mTimeline = this.mEditorEngine.createTimeline();
        Logger.d(TAG, "createTimeline: timeline = " + this.mTimeline);
        this.mAudioRecordManager = AudioRecordManager.getInstance().init();
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null) {
            this.mEditTimeline.setTimeline(nvsTimeline);
            this.mEditorTrackView.setTimeline(this.mTimeline);
            this.mEditorTrackView.setStreamingContext(this.mStreamingContext);
            initEditorTimeline();
            initPlayer();
            this.mEditorEngine.setVideoFragment(this.mVideoFragment);
            this.mEditMenuView.getRatioView().upDateRatio(TimelineData.getInstance().getMakeRatio());
            int statusBarViewHeight = ScreenUtils.getStatusBarViewHeight(this.mContext);
            if (statusBarViewHeight > 0) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mTopOperationBarView.getLayoutParams();
                layoutParams.topMargin = statusBarViewHeight + 18;
                this.mTopOperationBarView.setLayoutParams(layoutParams);
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        this.mOperateManager.destroy();
        PixelPerMicrosecondUtil.resetScale();
        PixelPerMicrosecondUtil.removeAllListener();
        View view = this.mDecorView;
        if (view != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        }
        this.mEditorEngine.removeFxTrack();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void jumpToCompile() {
        hideCenterProgress();
        AppManager.getInstance().jumpActivity(this, CompileActivity.class, null);
    }

    private void initEditorTimeLineView() {
        MYEditorTimeLine mYEditorTimeLine = this.mEditTimeline;
        if (mYEditorTimeLine != null) {
            mYEditorTimeLine.initEditorTimeLine();
        }
    }

    private void initPlayer() {
        this.mVideoFragment.setTimeLine(this.mTimeline);
        this.mVideoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass5 */

            @Override // com.meishe.player.view.VideoFragment.VideoFragmentListener
            public void playBackEOF(NvsTimeline nvsTimeline) {
                Logger.d(DraftEditActivity.TAG, "---playBackEOF:timeline.duration:" + nvsTimeline.getDuration() + " currentPosition:" + DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition());
                if (DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition() >= DraftEditActivity.this.mTimeline.getDuration() - CommonData.ONE_FRAME) {
                    DraftEditActivity.this.mSeekFlag = 11;
                    DraftEditActivity.this.mEditTimeline.multiThumbnailSequenceViewScrollTo(0L);
                    return;
                }
                DraftEditActivity.this.mSeekFlag = 11;
                DraftEditActivity.this.mEditTimeline.multiThumbnailSequenceViewScrollTo(DraftEditActivity.this.mVideoFragment.getTransitionStart());
                DraftEditActivity.this.mVideoFragment.setTransitionStart(0);
            }

            @Override // com.meishe.player.view.VideoFragment.VideoFragmentListener
            public void playStopped(NvsTimeline nvsTimeline) {
                Logger.d(DraftEditActivity.TAG, "----playStopped:timeline.duration:" + nvsTimeline.getDuration() + " currentPosition:" + DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition());
                if (DraftEditActivity.this.mContext.getResources().getString(R.string.main_menu_name_background).equals(StateManager.getInstance().getCurrentState())) {
                    DraftEditActivity.this.setCurrentMainTrackClip();
                    DraftEditActivity.this.mVideoFragment.setVideoClipInfo(TimelineData.getInstance().getSelectedMeicamClipInfo(), 0);
                }
            }

            @Override // com.meishe.player.view.VideoFragment.VideoFragmentListener
            public void playbackTimelinePosition(NvsTimeline nvsTimeline, long j) {
                MYMiddleOperationView mYMiddleOperationView = DraftEditActivity.this.mEditOperationView;
                mYMiddleOperationView.setDurationText(FormatUtils.formatTimeStrWithUs(j) + "/" + FormatUtils.formatTimeStrWithUs(DraftEditActivity.this.mTimeline.getDuration()));
                if (DraftEditActivity.this.mEditTimeline != null && DraftEditActivity.this.mTimeline != null) {
                    DraftEditActivity.this.seekViewOnPlay(j);
                    DraftEditActivity.this.mVideoFragment.setDrawRectOutOfTime(DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition());
                }
            }

            @Override // com.meishe.player.view.VideoFragment.VideoFragmentListener
            public void streamingEngineStateChanged(int i) {
                Logger.d(DraftEditActivity.TAG, "----streamingEngineStateChanged state:" + i + " timeline.Duration:" + DraftEditActivity.this.mTimeline.getDuration() + " currentPosition:" + DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition());
                boolean z = i == 3;
                if (z) {
                    DraftEditActivity.this.mVideoFragment.setDrawRectVisible(false);
                } else {
                    DraftEditActivity.this.mVideoFragment.stopToCheckDrawRectVisible(DraftEditActivity.this.mEditorTrackView.hasDragView(), DraftEditActivity.this.mEditorTrackView.getDragViewType());
                }
                DraftEditActivity.this.mEditOperationView.updateViewState(z);
                if (z) {
                    DraftEditActivity.this.mSeekFlag = 1;
                }
            }
        });
        this.mVideoFragment.setOnBackgroundChangedListener(new VideoFragment.OnBackgroundChangedListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass6 */

            @Override // com.meishe.player.view.VideoFragment.OnBackgroundChangedListener
            public void onBackgroundChanged() {
                DraftEditActivity.this.mEditorEngine.updateBackground();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void seekViewOnPlay(long j) {
        int durationToLength = PixelPerMicrosecondUtil.durationToLength(j);
        this.mEditTimeline.getMultiThumbnailSequenceView().smoothScrollTo(durationToLength, 0);
        this.mEditorTrackView.smoothScrollView(durationToLength);
        MYMiddleOperationView mYMiddleOperationView = this.mEditOperationView;
        mYMiddleOperationView.setDurationText(FormatUtils.formatTimeStrWithUs(j) + "/" + FormatUtils.formatTimeStrWithUs(this.mTimeline.getDuration()));
    }

    /* access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, com.meishe.myvideo.activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(@Nullable Bundle bundle) {
        PixelPerMicrosecondUtil.init(this);
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
    }

    /* access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Logger.d(TAG, "onSaveInstanceState happen.:timeline = " + this.mTimeline);
        saveDraft();
    }

    private void initMultiSequence() {
        NvsVideoTrack videoTrackByIndex = this.mTimeline.getVideoTrackByIndex(0);
        if (videoTrackByIndex != null) {
            int clipCount = videoTrackByIndex.getClipCount();
            ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList = new ArrayList<>();
            for (int i = 0; i < clipCount; i++) {
                NvsVideoClip clipByIndex = videoTrackByIndex.getClipByIndex(i);
                if (clipByIndex != null) {
                    MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = new MultiThumbnailSequenceView.ThumbnailSequenceDesc();
                    thumbnailSequenceDesc.mediaFilePath = clipByIndex.getFilePath();
                    thumbnailSequenceDesc.trimIn = clipByIndex.getTrimIn();
                    thumbnailSequenceDesc.trimOut = clipByIndex.getTrimOut();
                    thumbnailSequenceDesc.inPoint = clipByIndex.getInPoint();
                    thumbnailSequenceDesc.outPoint = clipByIndex.getOutPoint();
                    thumbnailSequenceDesc.stillImageHint = false;
                    thumbnailSequenceDesc.onlyDecodeKeyFrame = false;
                    arrayList.add(thumbnailSequenceDesc);
                }
            }
            long duration = this.mTimeline.getDuration();
            int screenWidth = ScreenUtils.getScreenWidth(this) / 2;
            this.mEditTimeline.setSequenceLeftPadding(screenWidth);
            this.mEditTimeline.setSequenceRightPadding(screenWidth);
            this.mEditTimeline.initTimelineEditor(arrayList, duration);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mEditBack.setOnClickListener(this);
        this.mEditShare.setOnClickListener(this);
        this.mBtCompileCancel.setOnClickListener(this);
        this.mEditTimeline.setOnTimeLineEditorListener(this);
        this.mEditOperationView.setOnMiddleOperationClickListener(this);
        this.mEditMenuView.setOnMenuClickListener(this);
        this.mEditTimeline.setOnHandAction(this);
        this.mEditorTrackView.setOnTrackViewScrollListener(this);
        this.mEditorTrackView.setOnTrackViewDragListener(this);
        PixelPerMicrosecondUtil.addPixelPerMicrosecondChangeListener(this);
        this.mEditTimeline.setOnScrollListener(new MYEditorTimeLine.OnScrollListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass7 */

            @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnScrollListener
            public void onSelectClipIndexChange(int i) {
                long j;
                NvsVideoClip clipByIndex = DraftEditActivity.this.mTimeline.getVideoTrackByIndex(0).getClipByIndex(i);
                if (clipByIndex != null) {
                    long currentTimelinePosition = DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition();
                    if (currentTimelinePosition > clipByIndex.getOutPoint()) {
                        j = clipByIndex.getOutPoint();
                    } else {
                        j = currentTimelinePosition < clipByIndex.getInPoint() ? clipByIndex.getInPoint() : 0;
                    }
                    if (j > 0) {
                        DraftEditActivity.this.mEditTimeline.multiThumbnailSequenceViewScrollTo(j);
                    }
                }
                List<ClipInfo> mainTrackVideoClip = TimelineDataUtil.getMainTrackVideoClip();
                if (mainTrackVideoClip != null && mainTrackVideoClip.size() > i) {
                    DraftEditActivity.this.mCurSelectVideoClip = (MeicamVideoClip) mainTrackVideoClip.get(i);
                    DraftEditActivity draftEditActivity = DraftEditActivity.this;
                    draftEditActivity.mCurrentInPoint = draftEditActivity.mCurSelectVideoClip.getInPoint();
                    DraftEditActivity.this.mCurrentTrackIndex = 0;
                }
                TimelineData.getInstance().setSelectedMeicamClipInfo(DraftEditActivity.this.mCurSelectVideoClip);
                if (!DraftEditActivity.this.getResources().getString(R.string.main_menu_name_background).equals(StateManager.getInstance().getCurrentState())) {
                    StateManager.getInstance().changeState(Constants.STATE_EDIT, 2);
                    DraftEditActivity.this.mEditorTrackView.toOtherMenu();
                    DraftEditActivity.this.mEditorTrackView.clickOutSide();
                    DraftEditActivity.this.hideBottomView();
                    DraftEditActivity.this.mVideoFragment.setDrawRectVisible(false);
                }
            }

            @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnScrollListener
            public void onScrollChanged(long j, int i, int i2) {
                if (!(DraftEditActivity.this.mEditorTrackView.getHorizontalScrollX() == i2 || i == i2 || i == DraftEditActivity.this.mEditorTrackView.getHorizontalScrollX())) {
                    DraftEditActivity.this.mSeekFlag = 11;
                }
                if (DraftEditActivity.this.mSeekFlag == 11) {
                    if (!(DraftEditActivity.this.mStreamingContext.getStreamingEngineState() == 3 || DraftEditActivity.this.mTimeline == null)) {
                        if (j >= DraftEditActivity.this.mTimeline.getDuration()) {
                            j = DraftEditActivity.this.mTimeline.getDuration() - CommonData.ONE_FRAME;
                        }
                        DraftEditActivity.this.mVideoFragment.seekTimeline(j, 0);
                    }
                    DraftEditActivity.this.mEditorTrackView.smoothScrollView(i);
                    MYMiddleOperationView mYMiddleOperationView = DraftEditActivity.this.mEditOperationView;
                    mYMiddleOperationView.setDurationText(FormatUtils.formatTimeStrWithUs(j) + "/" + FormatUtils.formatTimeStrWithUs(DraftEditActivity.this.mTimeline.getDuration()));
                    if (DraftEditActivity.this.getResources().getString(R.string.main_menu_name_background).equals(StateManager.getInstance().getCurrentState())) {
                        DraftEditActivity.this.mEditTimeline.refreshCoverView(j);
                    }
                    DraftEditActivity.this.mVideoFragment.setDrawRectOutOfTime(DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition());
                }
            }

            @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnScrollListener
            public void onScrollStopped() {
                if (DraftEditActivity.this.mContext.getResources().getString(R.string.main_menu_name_background).equals(StateManager.getInstance().getCurrentState())) {
                    DraftEditActivity.this.setCurrentMainTrackClip();
                    DraftEditActivity.this.mVideoFragment.setVideoClipInfo(TimelineData.getInstance().getSelectedMeicamClipInfo(), 0);
                }
            }

            @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnScrollListener
            public void onSeekingTimeline(boolean z) {
                DraftEditActivity.this.mSeekFlag = 11;
                DraftEditActivity.this.mEditorEngine.seekTimeline(0);
            }
        });
        this.mEditTimeline.setOnSelectItemListener(new MYEditorTimeLine.OnSelectItemListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass8 */

            @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnSelectItemListener
            public void onSelectTransition(int i, EditorTimelineTransitionAdapter.TransitionData transitionData) {
                DraftEditActivity.this.mTargetTransitionIndex = i;
                DraftEditActivity.this.mCurrTransitionData = transitionData;
                if (transitionData != null) {
                    int transitionType = transitionData.getTransitionType();
                    int i2 = 0;
                    if (transitionType != 5) {
                        if (transitionType == 25) {
                            i2 = 1;
                        } else if (transitionType == 26) {
                            i2 = 2;
                        }
                    }
                    DraftEditActivity.this.showTransitionView(i2);
                }
            }

            @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnSelectItemListener
            public void onSelectPip(MeicamVideoClip meicamVideoClip, int i) {
                if (meicamVideoClip != null) {
                    String videoType = meicamVideoClip.getVideoType();
                    if (CommonData.CLIP_IMAGE.equals(videoType)) {
                        DraftEditActivity.this.mEditMenuView.switchToEditorMenu(CommonData.CLIP_IMAGE);
                    } else if (CommonData.CLIP_VIDEO.equals(videoType)) {
                        DraftEditActivity.this.mEditMenuView.switchToEditorMenu(CommonData.CLIP_VIDEO);
                    }
                    DraftEditActivity.this.mCurSelectVideoClip = meicamVideoClip;
                    DraftEditActivity.this.toOtherMenu();
                    DraftEditActivity.this.mEditorTrackView.showPipTrackView(DraftEditActivity.this.mTimeline);
                    DraftEditActivity.this.mEditorTrackView.setSelect(i - 1, meicamVideoClip.getInPoint());
                    DraftEditActivity.this.mVideoFragment.setVideoClipInfo(meicamVideoClip, i);
                    DraftEditActivity.this.mVideoFragment.setDrawRectVisible(8);
                    DraftEditActivity.this.mVideoFragment.setPipTransformViewVisible(0);
                    DraftEditActivity.this.mCurrentInPoint = meicamVideoClip.getInPoint();
                    DraftEditActivity.this.mCurrentTrackIndex = i;
                    StateManager.getInstance().setCurrentState(Constants.STATE_PIC_IN_PIC);
                }
            }
        });
        this.mEditRecordView.setOnRecordListener(new MYRecordMenuView.OnRecordListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass9 */

            @Override // com.meishe.myvideo.view.MYRecordMenuView.OnRecordListener
            public void onStartRecord() {
                if (Build.VERSION.SDK_INT < 23) {
                    DraftEditActivity.this.mHaveRecordPermission = true;
                } else if (ContextCompat.checkSelfPermission(DraftEditActivity.this, "android.permission.RECORD_AUDIO") == 0) {
                    DraftEditActivity.this.mHaveRecordPermission = true;
                } else {
                    DraftEditActivity.this.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 100);
                    return;
                }
                if (DraftEditActivity.this.mHaveRecordPermission) {
                    DraftEditActivity.this.mAudioRecordManager.startRecord(DraftEditActivity.this);
                }
            }

            @Override // com.meishe.myvideo.view.MYRecordMenuView.OnRecordListener
            public void onStopRecord() {
                DraftEditActivity.this.stopRecording();
            }

            @Override // com.meishe.myvideo.view.MYRecordMenuView.OnRecordListener
            public void onConfirm() {
                if (DraftEditActivity.this.mEditRecordView != null) {
                    DraftEditActivity.this.mEditRecordView.hide();
                }
            }
        });
        this.mAudioRecordManager.setOnRecordListener(new AudioRecordManager.OnRecordListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass10 */
            BaseUIClip baseUIClip = null;

            @Override // com.meishe.myvideo.manager.AudioRecordManager.OnRecordListener
            public void onRecordStart(Long l, String str) {
                DraftEditActivity.this.doOnRecordStart(l, str);
                DraftEditActivity.this.mTrackListHashMap = TrackViewDataHelper.getInstance().addAudioBaseUIClip(DraftEditActivity.this.mTrackListHashMap, DraftEditActivity.this.mRecordAudioInfo, DraftEditActivity.this.mTimeline);
                DraftEditActivity.this.mEditorTrackView.setTrackViewLayoutData(DraftEditActivity.this.mTrackListHashMap, DraftEditActivity.this.mTimeline.getDuration());
                this.baseUIClip = TrackViewDataHelper.getInstance().getBaseUIClip(DraftEditActivity.this.mTrackListHashMap, DraftEditActivity.this.mRecordAudioInfo.getIndex(), DraftEditActivity.this.mRecordAudioInfo.getInPoint());
                DraftEditActivity.this.mEditorTrackView.setSelect(this.baseUIClip);
            }

            @Override // com.meishe.myvideo.manager.AudioRecordManager.OnRecordListener
            public void onRecordProgress(float[] fArr, int i, String str) {
                if (i != 0) {
                    long inPoint = this.baseUIClip.getInPoint() + ((long) (i * 1000));
                    if (inPoint > DraftEditActivity.this.mTimeline.getDuration()) {
                        DraftEditActivity.this.mEditRecordView.stopRecord();
                        return;
                    }
                    int durationToLength = PixelPerMicrosecondUtil.durationToLength(inPoint);
                    DraftEditActivity.this.mSeekFlag = 111;
                    DraftEditActivity.this.mEditorTrackView.smoothScrollView(durationToLength);
                    this.baseUIClip.setRecordLength(i);
                    this.baseUIClip.setRecordArray(fArr);
                    this.baseUIClip.setTrimOut(DraftEditActivity.this.mStreamingContext.getTimelineCurrentPosition(DraftEditActivity.this.mTimeline) - DraftEditActivity.this.mRecordAudioInfo.getInPoint());
                    DraftEditActivity.this.mEditorTrackView.refreshAudioSelectView(this.baseUIClip);
                }
            }

            @Override // com.meishe.myvideo.manager.AudioRecordManager.OnRecordListener
            public void onRecordFail(String str) {
                Log.e(DraftEditActivity.TAG, "onRecordFail==" + str);
                DraftEditActivity.this.refreshAudioView();
            }

            @Override // com.meishe.myvideo.manager.AudioRecordManager.OnRecordListener
            public void onRecordEnd() {
                if (DraftEditActivity.this.mHaveRecordPermission && DraftEditActivity.this.mEditRecordView.isStartRecord()) {
                    DraftEditActivity.this.doOnRecordEnd();
                    if (DraftEditActivity.this.mRecordAudioInfo != null) {
                        long timelineCurrentPosition = DraftEditActivity.this.mStreamingContext.getTimelineCurrentPosition(DraftEditActivity.this.mTimeline);
                        this.baseUIClip.setAudioType(1);
                        this.baseUIClip.setTrimOut(timelineCurrentPosition - DraftEditActivity.this.mRecordAudioInfo.getInPoint());
                        DraftEditActivity.this.mRecordAudioInfo.setTrimOut(timelineCurrentPosition - DraftEditActivity.this.mRecordAudioInfo.getInPoint());
                        MeicamAudioClip meicamAudioClip = DraftEditActivity.this.mRecordAudioInfo;
                        meicamAudioClip.setDrawText(DraftEditActivity.this.getResources().getString(R.string.audio_num) + TrackViewDataHelper.getInstance().getDrawText(DraftEditActivity.this.mTrackListHashMap));
                        this.baseUIClip.setText(DraftEditActivity.this.mRecordAudioInfo.getDrawText());
                        DraftEditActivity draftEditActivity = DraftEditActivity.this;
                        boolean addAudioClip = draftEditActivity.addAudioClip(draftEditActivity.mRecordAudioInfo);
                        MeicamAudioClip findAudioClipInfoByTrackAndInpoint = TimelineDataUtil.findAudioClipInfoByTrackAndInpoint(DraftEditActivity.this.mRecordAudioInfo.getIndex(), DraftEditActivity.this.mRecordAudioInfo.getInPoint());
                        if (findAudioClipInfoByTrackAndInpoint != null) {
                            findAudioClipInfoByTrackAndInpoint.setOutPoint(timelineCurrentPosition);
                            findAudioClipInfoByTrackAndInpoint.setAudioType(1);
                            findAudioClipInfoByTrackAndInpoint.setOriginalDuring(findAudioClipInfoByTrackAndInpoint.getTrimOut() - findAudioClipInfoByTrackAndInpoint.getTrimIn());
                            this.baseUIClip.setNvsObject(findAudioClipInfoByTrackAndInpoint.getObject());
                            if (!addAudioClip) {
                                DraftEditActivity.this.mTrackListHashMap = TrackViewDataHelper.getInstance().removeAudioBaseUIClip(DraftEditActivity.this.mTrackListHashMap, DraftEditActivity.this.mRecordAudioInfo);
                            }
                            DraftEditActivity.this.mEditorTrackView.setTrackViewLayoutData(DraftEditActivity.this.mTrackListHashMap, DraftEditActivity.this.mTimeline.getDuration());
                            DraftEditActivity.this.mEditorTrackView.setSelect(this.baseUIClip);
                            DraftEditActivity.this.saveOperation();
                        }
                    }
                    DraftEditActivity.this.mEditorTrackView.cancleAnimation();
                    DraftEditActivity.this.mEditRecordView.setStartRecord(false);
                }
            }
        });
        this.mEditBottomView.setOnChangeListener(new MYMultiBottomView.OnChangeListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass11 */

            @Override // com.meishe.myvideo.view.MYMultiBottomView.OnChangeListener
            public void onCaptionTextChange(String str) {
                DraftEditActivity.this.mEditorEngine.changeCaptionText(str);
            }

            @Override // com.meishe.myvideo.view.MYMultiBottomView.OnChangeListener
            public void onClickConfirm(int i) {
                Logger.e(DraftEditActivity.TAG, "onClickConfirm: " + i);
                if (i == 1) {
                    DraftEditActivity.this.mEditorEngine.getVideoFragment().setEditMode(2);
                    DraftEditActivity.this.mEditorEngine.getVideoFragment().setDrawRectVisible(false);
                    DraftEditActivity.this.mVideoFragment.addWaterToTimeline();
                    DraftEditActivity.this.mEditBottomView.setOnWaterMarkChangeListener(null);
                    DraftEditActivity.this.switchToMainMenu();
                    DraftEditActivity.this.saveOperation();
                } else if (i == 2) {
                    DraftEditActivity.this.mVideoFragment.setCurAnimateSticker(null);
                    DraftEditActivity.this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_STICKER), DraftEditActivity.this.mEditorEngine.getCurrentTimeline().getDuration());
                } else if (i == 3) {
                    DraftEditActivity.this.mVideoFragment.setCurCaption(null);
                    DraftEditActivity.this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_CAPTION), DraftEditActivity.this.mEditorEngine.getCurrentTimeline().getDuration());
                    DraftEditActivity.this.saveOperation();
                } else if (i == 5) {
                    DraftEditActivity.this.saveOperation();
                } else if (i == 6) {
                    DraftEditActivity.this.mEditorEngine.getVideoFragment().setEditMode(2);
                    DraftEditActivity.this.mEditorEngine.getVideoFragment().setDrawRectVisible(false);
                    DraftEditActivity.this.mEditBottomView.setOnWaterMarkChangeListener(null);
                    DraftEditActivity.this.switchToMainMenu();
                    DraftEditActivity.this.saveOperation();
                }
            }
        });
        this.mVideoFragment.setLiveWindowClickListener(new VideoFragment.OnLiveWindowClickListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass12 */

            @Override // com.meishe.player.view.VideoFragment.OnLiveWindowClickListener
            public void onLiveWindowClick() {
            }
        });
        this.mVideoFragment.setCompoundCaptionListener(new VideoFragment.OnCompoundCaptionListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass13 */

            @Override // com.meishe.player.view.VideoFragment.OnCompoundCaptionListener
            public void onCaptionIndex(int i) {
                NvsTimelineCompoundCaption currCompoundCaption = DraftEditActivity.this.mVideoFragment.getCurrCompoundCaption();
                if (currCompoundCaption == null) {
                    Logger.e(DraftEditActivity.TAG, "onCaptionIndex currCompoundCaption is null");
                    return;
                }
                int captionCount = currCompoundCaption.getCaptionCount();
                if (i < 0 || i >= captionCount) {
                    Logger.e(DraftEditActivity.TAG, "onCaptionIndex captionIndex is error! captionIndex: " + i + "  captionCount: " + captionCount);
                    return;
                }
                DraftEditActivity.this.mCompoundCaptionEditView.show(currCompoundCaption, i, DraftEditActivity.this.mKeyboardHeight);
            }
        });
        this.mVideoFragment.setCaptionTextEditListener(new VideoFragment.VideoCaptionTextEditListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass14 */

            @Override // com.meishe.player.view.VideoFragment.VideoCaptionTextEditListener
            public void onCaptionTextEdit() {
                if (DraftEditActivity.this.mVideoFragment.getDrawRectVisible()) {
                    DraftEditActivity.this.showCaptionView();
                }
            }
        });
        this.mVideoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass15 */

            @Override // com.meishe.player.view.VideoFragment.AssetEditListener
            public void onAssetDelete(int i) {
                if (i == 0) {
                    DraftEditActivity.this.mEditorEngine.removeCaption(DraftEditActivity.this.mCurrSelectedCaption);
                    DraftEditActivity.this.mEditBottomView.hide();
                } else if (i == 1) {
                    DraftEditActivity.this.mEditorEngine.deleteSticker(DraftEditActivity.this.mCurrSelectedSticker);
                    DraftEditActivity.this.mEditBottomView.hide();
                } else if (i == 2) {
                    DraftEditActivity.this.mEditorEngine.getVideoFragment().removeWaterToTimeline();
                    if (DraftEditActivity.this.mWaterMarkTabSelectedIndex == 0) {
                        if (DraftEditActivity.this.mWaterFragment != null) {
                            DraftEditActivity.this.mWaterFragment.cancleSelectedItem();
                        }
                    } else if (DraftEditActivity.this.mWaterEffectFragment != null) {
                        DraftEditActivity.this.mWaterEffectFragment.cancleSelectedItem();
                    }
                } else if (i == 4) {
                    DraftEditActivity.this.mEditorEngine.removeCompoundCaption(DraftEditActivity.this.mCurrCompoundCaption);
                    DraftEditActivity.this.mCompoundCaptionEditView.hide();
                }
                DraftEditActivity.this.mVideoFragment.setDrawRectVisible(false);
            }

            @Override // com.meishe.player.view.VideoFragment.AssetEditListener
            public void onAssetSelected(PointF pointF) {
                char c;
                NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker;
                NvsTimelineCaption nvsTimelineCaption;
                NvsTimelineCompoundCaption nvsTimelineCompoundCaption;
                String currentState = StateManager.getInstance().getCurrentState();
                Logger.d(DraftEditActivity.TAG, "onAssetSelected currentState:" + currentState);
                if (Constants.STATE_STICKER.equals(currentState) || Constants.STATE_CAPTION.equals(currentState) || Constants.STATE_COMPOUND_CAPTION.equals(currentState)) {
                    Iterator<NvsTimelineAnimatedSticker> it = DraftEditActivity.this.mTimeline.getAnimatedStickersByTimelinePosition(DraftEditActivity.this.mStreamingContext.getTimelineCurrentPosition(DraftEditActivity.this.mTimeline)).iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            c = 65535;
                            nvsTimelineAnimatedSticker = null;
                            break;
                        }
                        nvsTimelineAnimatedSticker = it.next();
                        if (DraftEditActivity.this.mVideoFragment.clickPointIsInnerDrawRect(nvsTimelineAnimatedSticker.getBoundingRectangleVertices(), (int) pointF.x, (int) pointF.y)) {
                            c = 0;
                            break;
                        }
                    }
                    List<NvsTimelineCaption> captionsByTimelinePosition = DraftEditActivity.this.mTimeline.getCaptionsByTimelinePosition(DraftEditActivity.this.mStreamingContext.getTimelineCurrentPosition(DraftEditActivity.this.mTimeline));
                    int i = 0;
                    while (true) {
                        if (i >= captionsByTimelinePosition.size()) {
                            nvsTimelineCaption = null;
                            break;
                        }
                        nvsTimelineCaption = captionsByTimelinePosition.get(i);
                        if (DraftEditActivity.this.mVideoFragment.clickPointIsInnerDrawRect(nvsTimelineCaption.getBoundingRectangleVertices(), (int) pointF.x, (int) pointF.y)) {
                            if (c != 65535) {
                                int i2 = (nvsTimelineCaption.getZValue() > nvsTimelineAnimatedSticker.getZValue() ? 1 : (nvsTimelineCaption.getZValue() == nvsTimelineAnimatedSticker.getZValue() ? 0 : -1));
                            }
                            c = 2;
                        } else {
                            i++;
                        }
                    }
                    List<NvsTimelineCompoundCaption> compoundCaptionsByTimelinePosition = DraftEditActivity.this.mTimeline.getCompoundCaptionsByTimelinePosition(DraftEditActivity.this.mStreamingContext.getTimelineCurrentPosition(DraftEditActivity.this.mTimeline));
                    int i3 = 0;
                    while (true) {
                        if (i3 >= compoundCaptionsByTimelinePosition.size()) {
                            nvsTimelineCompoundCaption = null;
                            break;
                        }
                        nvsTimelineCompoundCaption = compoundCaptionsByTimelinePosition.get(i3);
                        if (DraftEditActivity.this.mVideoFragment.clickPointIsInnerDrawRect(nvsTimelineCompoundCaption.getCompoundBoundingVertices(1), (int) pointF.x, (int) pointF.y)) {
                            if ((c != 0 || nvsTimelineCompoundCaption.getZValue() <= nvsTimelineAnimatedSticker.getZValue()) && c == 2) {
                                int i4 = (nvsTimelineCompoundCaption.getZValue() > nvsTimelineCaption.getZValue() ? 1 : (nvsTimelineCompoundCaption.getZValue() == nvsTimelineCaption.getZValue() ? 0 : -1));
                            }
                            c = 4;
                        } else {
                            i3++;
                        }
                    }
                    if (c == 0) {
                        DraftEditActivity.this.mVideoFragment.setEditMode(1);
                        DraftEditActivity.this.mVideoFragment.setCurAnimateSticker(nvsTimelineAnimatedSticker);
                        DraftEditActivity.this.mVideoFragment.updateAnimateStickerCoordinate(nvsTimelineAnimatedSticker);
                        DraftEditActivity.this.mVideoFragment.setDrawRectVisible(true);
                        DraftEditActivity.this.mVideoFragment.setDrawRectOutOfTime(DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition());
                        DraftEditActivity.this.mCurrSelectedSticker = (MeicamStickerClip) TimelineDataUtil.getStickerCaptionData((int) nvsTimelineAnimatedSticker.getZValue(), nvsTimelineAnimatedSticker.getInPoint());
                        DraftEditActivity.this.mEditorTrackView.setSelect((int) nvsTimelineAnimatedSticker.getZValue(), (long) ((int) nvsTimelineAnimatedSticker.getInPoint()));
                    } else if (c == 2) {
                        DraftEditActivity.this.mVideoFragment.setEditMode(0);
                        DraftEditActivity.this.mVideoFragment.setCurCaption(nvsTimelineCaption);
                        DraftEditActivity.this.mVideoFragment.updateCaptionCoordinate(nvsTimelineCaption);
                        DraftEditActivity.this.mVideoFragment.setDrawRectVisible(true);
                        DraftEditActivity.this.mVideoFragment.setDrawRectOutOfTime(DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition());
                        DraftEditActivity.this.mCurrSelectedCaption = (MeicamCaptionClip) TimelineDataUtil.getStickerCaptionData((int) nvsTimelineCaption.getZValue(), nvsTimelineCaption.getInPoint());
                        DraftEditActivity.this.mEditorTrackView.setSelect((int) nvsTimelineCaption.getZValue(), (long) ((int) nvsTimelineCaption.getInPoint()));
                    } else if (c == 4) {
                        DraftEditActivity.this.mVideoFragment.setEditMode(4);
                        DraftEditActivity.this.mVideoFragment.setCurCompoundCaption(nvsTimelineCompoundCaption);
                        DraftEditActivity.this.mVideoFragment.selectCompoundCaptionByHandClick(pointF);
                        DraftEditActivity.this.mVideoFragment.updateCompoundCaptionCoordinate(nvsTimelineCompoundCaption);
                        DraftEditActivity.this.mVideoFragment.setDrawRectVisible(true);
                        DraftEditActivity.this.mVideoFragment.setDrawRectOutOfTime(DraftEditActivity.this.mEditorEngine.getCurrentTimelinePosition());
                        DraftEditActivity.this.mCurrCompoundCaption = (MeicamCompoundCaptionClip) TimelineDataUtil.getStickerCaptionData((int) nvsTimelineCompoundCaption.getZValue(), nvsTimelineCompoundCaption.getInPoint());
                        DraftEditActivity.this.mEditorTrackView.setSelect((int) nvsTimelineCompoundCaption.getZValue(), (long) ((int) nvsTimelineCompoundCaption.getInPoint()));
                    } else if (!DraftEditActivity.this.mEditorTrackView.hasDragView()) {
                        DraftEditActivity.this.mVideoFragment.setDrawRectList(new ArrayList());
                    }
                } else if (Constants.STATE_WATER_MARK.equals(currentState) && DraftEditActivity.this.mEditBottomView != null && DraftEditActivity.this.mEditBottomView.isShow() && DraftEditActivity.this.mVideoFragment.getDrawRectView().curPointIsInnerDrawRect((int) pointF.x, (int) pointF.y)) {
                    DraftEditActivity.this.mVideoFragment.setEditMode(2);
                    DraftEditActivity.this.mVideoFragment.setDrawRectVisible(true);
                }
            }

            @Override // com.meishe.player.view.VideoFragment.AssetEditListener
            public void onAssetTranstion(int i) {
                if (i == 0) {
                    DraftEditActivity.this.mEditorEngine.setCaptionTranslation(DraftEditActivity.this.mCurrSelectedCaption);
                } else if (i == 1) {
                    DraftEditActivity.this.mEditorEngine.setStickerTranslation(DraftEditActivity.this.mCurrSelectedSticker);
                } else if (i == 4) {
                    DraftEditActivity.this.mEditorEngine.setCompoundCaptionTranslation(DraftEditActivity.this.mCurrCompoundCaption);
                }
            }

            @Override // com.meishe.player.view.VideoFragment.AssetEditListener
            public void onAssetScale(int i) {
                if (i == 0) {
                    DraftEditActivity.this.mEditorEngine.setCaptionScale(DraftEditActivity.this.mCurrSelectedCaption);
                } else if (i == 1) {
                    DraftEditActivity.this.mEditorEngine.setStickerScale(DraftEditActivity.this.mCurrSelectedSticker);
                } else if (i == 4) {
                    DraftEditActivity.this.mEditorEngine.setCompoundCaptionScale(DraftEditActivity.this.mCurrCompoundCaption);
                }
            }

            @Override // com.meishe.player.view.VideoFragment.AssetEditListener
            public void onAssetAlign(int i) {
                Logger.d(DraftEditActivity.TAG, "onAssetAlign: " + i);
            }

            @Override // com.meishe.player.view.VideoFragment.AssetEditListener
            public void onAssetHorizFlip(boolean z) {
                Logger.d(DraftEditActivity.TAG, "onAssetHorizFlip :" + z);
            }

            @Override // com.meishe.player.view.VideoFragment.AssetEditListener
            public void onBeyondDrawRectClick() {
                String currentState = StateManager.getInstance().getCurrentState();
                Logger.d(DraftEditActivity.TAG, "onBeyondDrawRectClick :" + currentState);
                if (!currentState.equals(Constants.STATE_WATER_MARK)) {
                    DraftEditActivity.this.mVideoFragment.setDrawRectVisible(false);
                    DraftEditActivity.this.mEditorTrackView.clickOutSide();
                    DraftEditActivity.this.hideBottomView();
                    StateManager.getInstance().changeState(StateManager.getInstance().getCurrentState(), 3);
                }
            }

            @Override // com.meishe.player.view.VideoFragment.AssetEditListener
            public void onTouchUp(int i) {
                DraftEditActivity.this.saveOperation();
            }
        });
        this.mClCompileProgress.setOnTouchListener(new View.OnTouchListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass16 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        this.mOperateManager = OperateManager.getInstance();
        this.mOperateManager.setOnOperateStateListener(new OperateManager.OnOperateStateListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass17 */

            @Override // com.meishe.myvideo.edit.manager.OperateManager.OnOperateStateListener
            public void onCancelDataListener(boolean z) {
                DraftEditActivity.this.mEditOperationView.updateCancelState(!z);
            }

            @Override // com.meishe.myvideo.edit.manager.OperateManager.OnOperateStateListener
            public void onRecoverDataListener(boolean z) {
                DraftEditActivity.this.mEditOperationView.updateRecoverState(!z);
            }
        });
        this.mEditCompoundCaptionView.setOnChangeListener(new MYCompoundCaptionMenuView.OnChangeListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass18 */

            @Override // com.meishe.myvideo.view.MYCompoundCaptionMenuView.OnChangeListener
            public void onClickConfirm() {
                Logger.d(DraftEditActivity.TAG, "CompoundCaption onClickConfirm");
                DraftEditActivity.this.saveOperation();
                DraftEditActivity.this.mVideoFragment.setDrawRectVisible(false);
                DraftEditActivity.this.mVideoFragment.setCurCompoundCaption(null);
                DraftEditActivity.this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_COMPOUND_CAPTION), DraftEditActivity.this.mEditorEngine.getCurrentTimeline().getDuration());
            }
        });
        saveOperation();
        this.mVideoFragment.setOnSaveOperationListener(new VideoFragment.OnSaveOperationListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass19 */

            @Override // com.meishe.player.view.VideoFragment.OnSaveOperationListener
            public void onSaveCurrentTimeline() {
                DraftEditActivity.this.saveOperation();
            }
        });
        this.mEditorEngine.setOnTrackChangeListener(new EditorEngine.OnTrackChangeListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass20 */

            @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTrackChangeListener
            public void audioEditCutClip(NvsAudioTrack nvsAudioTrack, long j) {
                DraftEditActivity.this.refreshAudioView();
                DraftEditActivity.this.mEditorTrackView.setSelect(TrackViewDataHelper.getInstance().getBaseUIClip(DraftEditActivity.this.mTrackListHashMap, nvsAudioTrack.getIndex(), j));
                DraftEditActivity.this.saveOperation();
            }

            @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTrackChangeListener
            public void audioEditDeleteClip() {
                if (DraftEditActivity.this.mCurrSelectedAudioClip == null) {
                    Log.e(DraftEditActivity.TAG, "找不到当前的音乐片段");
                    return;
                }
                List<ClipInfo> clipInfoList = TimelineData.getInstance().getMeicamAudioTrackList().get(DraftEditActivity.this.mCurrSelectedAudioClip.getIndex()).getClipInfoList();
                if (clipInfoList != null) {
                    clipInfoList.remove(DraftEditActivity.this.mCurrSelectedAudioClip);
                    DraftEditActivity.this.refreshAudioView();
                    if (DraftEditActivity.this.mTrackListHashMap.size() == 0) {
                        TimelineDataUtil.restoreThemeVolume(DraftEditActivity.this.mTimeline);
                    }
                    DraftEditActivity.this.saveOperation();
                }
                DraftEditActivity.this.onChangeBottomView();
            }

            @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTrackChangeListener
            public void audioEditCopyClip(long j, NvsAudioTrack nvsAudioTrack) {
                DraftEditActivity.this.createMeicamAudioInfo(j, nvsAudioTrack);
            }
        });
        this.mConvertFileManager.setOnConvertListener(new ConvertFileManager.OnConvertListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass21 */

            @Override // com.meishe.myvideo.manager.ConvertFileManager.OnConvertListener
            public void onConvertProgress(float f) {
                DraftEditActivity.this.setCenterProgress((int) f);
            }

            @Override // com.meishe.myvideo.manager.ConvertFileManager.OnConvertListener
            public void onConvertFinish(boolean z) {
                DraftEditActivity.this.hideCenterProgress();
                String currentState = StateManager.getInstance().getCurrentState();
                if (!z) {
                    return;
                }
                if (Constants.STATE_EDIT.equals(currentState)) {
                    DraftEditActivity.this.mEditorEngine.setVideoConvert(DraftEditActivity.this.mCurSelectVideoClip, 0);
                } else if (Constants.STATE_PIC_IN_PIC.equals(currentState)) {
                    DraftEditActivity.this.mEditorEngine.setVideoConvert(DraftEditActivity.this.mCurSelectVideoClip, DraftEditActivity.this.mSelectTrackData.getTrackIndex() + 1);
                }
            }

            @Override // com.meishe.myvideo.manager.ConvertFileManager.OnConvertListener
            public void onConvertCancel() {
                DraftEditActivity.this.hideCenterProgress();
            }
        });
        this.mEditBottomView.setCaptionTabChangeListener(new MYMultiBottomView.OnCaptionTabChangeListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass22 */

            @Override // com.meishe.myvideo.view.MYMultiBottomView.OnCaptionTabChangeListener
            public void onTabSelected(int i) {
                float f;
                float f2;
                if (DraftEditActivity.this.mCurrSelectedCaption == null || DraftEditActivity.this.mCaptionStyleFragment == null) {
                    DraftEditActivity.this.mCaptionStyleFragment.setSelect(1);
                } else {
                    DraftEditActivity.this.mCaptionStyleFragment.setSelect(DraftEditActivity.this.mCurrSelectedCaption);
                }
                float f3 = 1.0f;
                if (DraftEditActivity.this.mCurrSelectedCaption != null) {
                    NvsTimelineCaption nvsTimelineCaption = (NvsTimelineCaption) DraftEditActivity.this.mCurrSelectedCaption.getObject();
                    if (nvsTimelineCaption != null) {
                        NvsColor outlineColor = nvsTimelineCaption.getOutlineColor();
                        NvsColor textColor = nvsTimelineCaption.getTextColor();
                        nvsTimelineCaption.getOutlineWidth();
                        if (outlineColor != null) {
                            float f4 = outlineColor.a;
                            f3 = textColor.a;
                            f2 = f4;
                            f = DraftEditActivity.this.mCurrSelectedCaption.getOutlineWidth();
                        }
                    }
                    f2 = 1.0f;
                    f = DraftEditActivity.this.mCurrSelectedCaption.getOutlineWidth();
                } else {
                    f = 5.0f;
                    f2 = 1.0f;
                }
                if (i == 1) {
                    if (DraftEditActivity.this.mCaptionColorFragment != null) {
                        DraftEditActivity.this.mCaptionColorFragment.setOpacityProgress(f3);
                    }
                } else if (i == 2) {
                    boolean z = false;
                    if (!(DraftEditActivity.this.mCaptionOutlineFragment == null || DraftEditActivity.this.mCurrSelectedCaption == null)) {
                        float[] outlineColor2 = DraftEditActivity.this.mCurrSelectedCaption.getOutlineColor();
                        if (!(outlineColor2[0] == 0.0f && outlineColor2[1] == 0.0f && outlineColor2[2] == 0.0f && outlineColor2[3] == 0.0f)) {
                            z = true;
                        }
                    }
                    DraftEditActivity.this.mCaptionOutlineFragment.setWidthAndOpacityProgress(f, f2, z);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setCurrentMainTrackClip() {
        NvsVideoClip clipByTimelinePosition;
        if (this.mTimeline != null && (clipByTimelinePosition = this.mTimeline.getVideoTrackByIndex(0).getClipByTimelinePosition(NvsStreamingContext.getInstance().getTimelineCurrentPosition(this.mTimeline))) != null) {
            int index = clipByTimelinePosition.getIndex();
            List<ClipInfo> clipInfoList = TimelineDataUtil.getMainTrack().getClipInfoList();
            if (clipInfoList != null && clipInfoList.size() > index) {
                MeicamVideoClip meicamVideoClip = (MeicamVideoClip) clipInfoList.get(index);
                TimelineData.getInstance().setSelectedMeicamClipInfo(meicamVideoClip);
                this.mCurSelectVideoClip = meicamVideoClip;
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void stopRecording() {
        if (this.mHaveRecordPermission) {
            this.mAudioRecordManager.stopRecord();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void doOnRecordEnd() {
        MYRecordMenuView mYRecordMenuView = this.mEditRecordView;
        if (mYRecordMenuView != null && mYRecordMenuView.isShown()) {
            this.mEditRecordView.setVisibility(8);
        }
        this.mVideoFragment.stopEngine();
        this.mIsRecording = false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean addAudioClip(MeicamAudioClip meicamAudioClip) {
        NvsTimeline nvsTimeline;
        boolean z;
        if (meicamAudioClip == null || (nvsTimeline = this.mTimeline) == null) {
            return false;
        }
        int audioTrackCount = nvsTimeline.audioTrackCount();
        NvsAudioTrack nvsAudioTrack = null;
        int i = 0;
        while (true) {
            if (i >= audioTrackCount) {
                z = false;
                break;
            }
            nvsAudioTrack = this.mTimeline.getAudioTrackByIndex(i);
            NvsAudioClip clipByIndex = nvsAudioTrack.getClipByIndex(nvsAudioTrack.getClipCount() - 1);
            if (clipByIndex == null || meicamAudioClip.getInPoint() >= clipByIndex.getOutPoint()) {
                meicamAudioClip.setIndex(i);
                z = true;
            } else {
                i++;
            }
        }
        meicamAudioClip.setIndex(i);
        z = true;
        if (!z) {
            if (this.mTimeline.audioTrackCount() < 16) {
                meicamAudioClip.setIndex(audioTrackCount);
                nvsAudioTrack = this.mTimeline.appendAudioTrack();
            } else {
                ToastUtil.showToast(this.mContext, String.format(this.mContext.getResources().getString(R.string.audio_max_track), 16));
                return false;
            }
        }
        if (meicamAudioClip.bindToTimeline(nvsAudioTrack) == null) {
            return false;
        }
        TimelineDataUtil.addAudioClipInfoByTrackIndex(nvsAudioTrack, meicamAudioClip);
        TimelineDataUtil.setThemeQuiet(this.mTimeline);
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void doOnRecordStart(Long l, String str) {
        this.mIsRecording = true;
        this.mCurrentRecordInPoint = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        this.mRecordAudioInfo = new MeicamAudioClip();
        this.mRecordAudioInfo.setId(l.longValue());
        this.mRecordAudioInfo.setFilePath(str);
        this.mRecordAudioInfo.setInPoint(this.mCurrentRecordInPoint);
        this.mRecordAudioInfo.setAudioType(2);
        this.mRecordAudioInfo.setTrimIn(0);
    }

    public void onMainTrackChange() {
        this.mEditTimeline.initTimeMarkingLineView();
        this.mEditorTrackView.updateTimelineDuration(this.mTimeline.getDuration());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void switchToMainMenu() {
        MYRecordMenuView mYRecordMenuView = this.mEditRecordView;
        if (mYRecordMenuView != null) {
            mYRecordMenuView.hide();
        }
        MenuView menuView = this.mEditMenuView;
        if (menuView != null) {
            menuView.switchToMainMenu();
        }
        StateManager.getInstance().setCurrentState(Constants.STATE_MAIN_MENU);
    }

    private void updatePlaySeekBar(long j) {
        this.mVideoFragment.seekTimeline(j, 1);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.bt_compile_cancel) {
            if (id == R.id.edit_back) {
                saveDraft();
                onBackPressed();
            } else if (id == R.id.edit_create_save) {
                createAndSaveVideo();
                return;
            } else {
                return;
            }
        }
        String charSequence = this.mTvProgressDesc.getText().toString();
        if (getResources().getString(R.string.video_compiling).equals(charSequence)) {
            stopCompileVideo();
        } else if (getResources().getString(R.string.reverting).equals(charSequence)) {
            this.mConvertFileManager.cancelConvert();
        }
    }

    private void stopCompileVideo() {
        hideCenterProgress();
        if (this.mStreamingContext.getStreamingEngineState() == 5) {
            this.mStreamingContext.stop();
        }
    }

    private void createAndSaveVideo() {
        UMengUtils.onEvent(MeiSheApplication.getContext());
        int streamingEngineState = this.mStreamingContext.getStreamingEngineState();
        if (streamingEngineState == 3) {
            this.mStreamingContext.stop();
        }
        if (streamingEngineState == 0 || streamingEngineState == 4) {
            this.mVideoSavePath = PathUtils.getVideoSavePath(PathUtils.getVideoSaveName());
            int i = ParameterSettingValues.instance().getCompileVideoRes() == 1080 ? 3 : 2;
            NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
            NvsTimeline nvsTimeline = this.mTimeline;
            if (nvsStreamingContext.compileTimeline(nvsTimeline, 0, nvsTimeline.getDuration(), this.mVideoSavePath, i, 2, 32)) {
                showCenterProgress(getResources().getString(R.string.video_compiling));
            }
        }
    }

    private void showCenterProgress(String str) {
        this.mClCompileProgress.setFocusable(true);
        this.mClCompileProgress.requestFocus();
        this.mClCompileProgress.setVisibility(0);
        this.mTvProgressDesc.setText(str);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void hideCenterProgress() {
        this.mClCompileProgress.setVisibility(8);
        this.mClCompileProgress.setFocusable(false);
        this.mEditCompileProgress.setProgress(0);
        this.mTvCompileProgress.setText("0%");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setCenterProgress(int i) {
        this.mEditCompileProgress.setProgress(i);
        TextView textView = this.mTvCompileProgress;
        textView.setText(i + "%");
    }

    @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnTimeLineEditorListener
    public void addMaterialSource() {
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null) {
            int i = 0;
            NvsVideoTrack videoTrackByIndex = nvsTimeline.getVideoTrackByIndex(0);
            if (videoTrackByIndex != null) {
                long currentTimelinePosition = this.mEditorEngine.getCurrentTimelinePosition();
                this.mCurrentPosition = currentTimelinePosition;
                int clipCount = videoTrackByIndex.getClipCount();
                int i2 = 0;
                while (true) {
                    if (i2 < clipCount) {
                        if (videoTrackByIndex.getClipByIndex(i2).getOutPoint() >= currentTimelinePosition) {
                            i = i2;
                            break;
                        }
                        i2++;
                    } else {
                        break;
                    }
                }
                if (this.mTimeline.getDuration() < currentTimelinePosition + 500000) {
                    i++;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("from_type", 2);
                bundle.putInt(MaterialSelectActivity.INSERT_INDEX_TYPE, i);
                AppManager.getInstance().jumpActivityForResult(this, MaterialSelectActivity.class, bundle, 110);
            }
        }
    }

    @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnTimeLineEditorListener
    public void openOriginalVoice(View view) {
        this.mEditorEngine.openOriginalVoice();
        saveOperation();
    }

    @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnTimeLineEditorListener
    public void closeOriginalVoice(View view) {
        this.mEditorEngine.closeOriginalVoice();
        saveOperation();
    }

    /* access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.fragment.app.FragmentActivity
    public void onPause() {
        NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
        if (nvsStreamingContext != null) {
            nvsStreamingContext.stop();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onStop() {
        super.onStop();
    }

    @Override // com.meishe.myvideo.interfaces.OnMiddleOperationClickListener
    public void onPlayEventCallback(boolean z) {
        Logger.d(TAG, "----onPlayEventCallbackisPlaying: " + z + "  currentPosition :" + this.mEditorEngine.getCurrentTimelinePosition() + "  mTimeline.getDuration() :" + this.mTimeline.getDuration());
        if (z) {
            this.mVideoFragment.stopEngine();
            return;
        }
        this.mVideoFragment.playVideoButtonClick(this.mEditorEngine.getCurrentTimelinePosition(), this.mTimeline.getDuration());
    }

    @Override // com.meishe.myvideo.interfaces.OnMiddleOperationClickListener
    public void onCancelEventCallback() {
        OperateData cancelOperate = this.mOperateManager.cancelOperate();
        if (cancelOperate != null) {
            this.mEditorEngine.restoreTimeline(cancelOperate.getOperateData(), this.mTimeline);
            this.mEditorTrackView.initWidth(this.mTimeline.getDuration());
            updateUI();
        }
        String currentState = StateManager.getInstance().getCurrentState();
        this.mCurSelectVideoClip = getCurrentClip();
        TimelineData.getInstance().setSelectedMeicamClipInfo(this.mCurSelectVideoClip);
        MYWidthConfirmMenuView mYWidthConfirmMenuView = this.mEditorWidthConfirmView;
        if (mYWidthConfirmMenuView != null && mYWidthConfirmMenuView.isShow()) {
            this.mEditorWidthConfirmView.setClipInfo(this.mCurSelectVideoClip);
            MessageEvent.sendEvent(0, (int) MessageEvent.MESSAGE_TYPE_UPDATE_SELECT_POSITION);
        }
        if (Constants.STATE_STICKER.equals(currentState) || Constants.STATE_CAPTION.equals(currentState) || Constants.STATE_COMPOUND_CAPTION.equals(currentState)) {
            onChangeBottomView();
        } else if (Constants.STATE_BACKGROUND.equals(currentState)) {
            this.mVideoFragment.setVideoClipInfo(this.mCurSelectVideoClip, 0);
            String str = null;
            MeicamVideoClip meicamVideoClip = this.mCurSelectVideoClip;
            if (meicamVideoClip != null) {
                str = meicamVideoClip.getBackgroundInfo().getStringVal("Description String");
            }
            if (this.mEditCanvasColorView.isShow()) {
                MessageEvent.sendEvent(StoryboardUtil.getSourcePathFromStory(str), (int) MessageEvent.MESSAGE_TYPE_CHANGE_COLOR_BACKGROUND_UI);
            }
            if (this.mEditCanvasStyleView.isShow()) {
                MessageEvent.sendEvent(StoryboardUtil.getSourcePathFromStory(str), (int) MessageEvent.MESSAGE_TYPE_CHANGE_IMAGE_BACKGROUND_UI);
            }
            if (this.mEditCanvasBlurView.isShow()) {
                MessageEvent.sendEvent(StoryboardUtil.getBlurStrengthFromStory(str), (int) MessageEvent.MESSAGE_TYPE_CHANGE_BLUR_BACKGROUND_UI);
            }
        } else if (Constants.STATE_RATIO.equals(currentState)) {
            this.mEditMenuView.getRatioView().upDateRatio(TimelineData.getInstance().getMakeRatio());
        }
    }

    @Override // com.meishe.myvideo.interfaces.OnMiddleOperationClickListener
    public void onRecoverEventCallback() {
        OperateData recoverOperate = this.mOperateManager.recoverOperate();
        if (recoverOperate != null) {
            this.mEditorEngine.restoreTimeline(recoverOperate.getOperateData(), this.mTimeline);
            this.mEditorTrackView.initWidth(this.mTimeline.getDuration());
            updateUI();
        }
        this.mCurSelectVideoClip = getCurrentClip();
        TimelineData.getInstance().setSelectedMeicamClipInfo(this.mCurSelectVideoClip);
        String currentState = StateManager.getInstance().getCurrentState();
        MYWidthConfirmMenuView mYWidthConfirmMenuView = this.mEditorWidthConfirmView;
        if (mYWidthConfirmMenuView != null && mYWidthConfirmMenuView.isShow()) {
            this.mEditorWidthConfirmView.setClipInfo(this.mCurSelectVideoClip);
            MessageEvent.sendEvent(0, (int) MessageEvent.MESSAGE_TYPE_UPDATE_SELECT_POSITION);
        } else if (Constants.STATE_STICKER.equals(currentState) || Constants.STATE_CAPTION.equals(currentState) || Constants.STATE_COMPOUND_CAPTION.equals(currentState)) {
            onChangeBottomView();
        } else if (Constants.STATE_BACKGROUND.equals(currentState)) {
            this.mVideoFragment.setVideoClipInfo(this.mCurSelectVideoClip, 0);
            String str = null;
            MeicamVideoClip meicamVideoClip = this.mCurSelectVideoClip;
            if (meicamVideoClip != null) {
                str = meicamVideoClip.getBackgroundInfo().getStringVal("Description String");
            }
            if (this.mEditCanvasColorView.isShow()) {
                MessageEvent.sendEvent(StoryboardUtil.getSourcePathFromStory(str), (int) MessageEvent.MESSAGE_TYPE_CHANGE_COLOR_BACKGROUND_UI);
            }
            if (this.mEditCanvasStyleView.isShow()) {
                MessageEvent.sendEvent(StoryboardUtil.getSourcePathFromStory(str), (int) MessageEvent.MESSAGE_TYPE_CHANGE_IMAGE_BACKGROUND_UI);
            }
            if (this.mEditCanvasBlurView.isShow()) {
                MessageEvent.sendEvent(StoryboardUtil.getBlurStrengthFromStory(str), (int) MessageEvent.MESSAGE_TYPE_CHANGE_BLUR_BACKGROUND_UI);
            }
        } else if (Constants.STATE_RATIO.equals(currentState)) {
            this.mEditMenuView.getRatioView().upDateRatio(TimelineData.getInstance().getMakeRatio());
        }
    }

    private MeicamVideoClip getCurrentClip() {
        List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
        if (CollectionUtils.isEmpty(meicamVideoTrackList) || this.mCurrentTrackIndex >= meicamVideoTrackList.size()) {
            return null;
        }
        List<ClipInfo> clipInfoList = meicamVideoTrackList.get(this.mCurrentTrackIndex).getClipInfoList();
        if (CollectionUtils.isEmpty(clipInfoList)) {
            return null;
        }
        for (ClipInfo clipInfo : clipInfoList) {
            if (clipInfo.getInPoint() <= this.mCurrentInPoint && clipInfo.getOutPoint() > this.mCurrentInPoint) {
                long inPoint = clipInfo.getInPoint();
                Log.e(TAG, "getCurrentClip: inPoint = " + inPoint);
                return (MeicamVideoClip) clipInfo;
            }
        }
        return null;
    }

    private void updateUI() {
        MYWidthConfirmMenuView mYWidthConfirmMenuView;
        String currentState = StateManager.getInstance().getCurrentState();
        if (Constants.STATE_FX.equals(currentState)) {
            this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_TIMELINE_FX), this.mEditorEngine.getCurrentTimeline().getDuration());
            MYWidthConfirmMenuView mYWidthConfirmMenuView2 = this.mEditorWidthConfirmView;
            if (mYWidthConfirmMenuView2 != null) {
                mYWidthConfirmMenuView2.hide();
            }
        } else if (Constants.STATE_CAPTION.equals(currentState)) {
            this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_CAPTION), this.mEditorEngine.getCurrentTimeline().getDuration());
            this.mEditorEngine.seekTimeline(2);
        } else if (Constants.STATE_STICKER.equals(currentState)) {
            this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_STICKER), this.mEditorEngine.getCurrentTimeline().getDuration());
        } else if (Constants.STATE_COMPOUND_CAPTION.equals(currentState)) {
            this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_COMPOUND_CAPTION), this.mEditorEngine.getCurrentTimeline().getDuration());
        } else if (Constants.STATE_PIC_IN_PIC.equals(currentState)) {
            this.mEditorTrackView.showPipTrackView(this.mTimeline);
            BaseUIClip baseUIClip = this.mSelectTrackData;
            if (baseUIClip != null) {
                int trackIndex = baseUIClip.getTrackIndex();
                long inPoint = this.mSelectTrackData.getInPoint();
                int i = trackIndex + 1;
                this.mCurSelectVideoClip = (MeicamVideoClip) TimelineDataUtil.getVideoClip(i, inPoint);
                if (this.mCurSelectVideoClip != null) {
                    this.mEditorTrackView.setSelect(trackIndex, (long) ((int) inPoint));
                    this.mCurrentInPoint = inPoint;
                    this.mCurrentTrackIndex = i;
                    this.mVideoFragment.setVideoClipInfo(this.mCurSelectVideoClip, this.mSelectTrackData.getTrackIndex() + 1);
                    MYWidthConfirmMenuView mYWidthConfirmMenuView3 = this.mEditorWidthConfirmView;
                    if (mYWidthConfirmMenuView3 != null && mYWidthConfirmMenuView3.isShow()) {
                        this.mEditorWidthConfirmView.setSeekPress(this.mEditorWidthConfirmView.getBaseInfo());
                    }
                } else {
                    MYWidthConfirmMenuView mYWidthConfirmMenuView4 = this.mEditorWidthConfirmView;
                    if (mYWidthConfirmMenuView4 != null && mYWidthConfirmMenuView4.isShow()) {
                        this.mEditorWidthConfirmView.hide();
                    }
                    onChangeBottomView();
                    if (this.mEditBottomController.getVisibility() == 0) {
                        this.mEditBottomController.dismiss();
                    }
                }
            }
        } else if (Constants.STATE_MUSIC.equals(currentState) || Constants.STATE_DUBBING.equals(currentState)) {
            refreshAudioView();
            onChangeBottomView();
        } else if (Constants.STATE_MAIN_MENU.equals(currentState)) {
            initEditorTimeline();
        } else if (Constants.STATE_EDIT.equals(currentState)) {
            initEditorTimeline();
            this.mEditTimeline.outOfSequenceView();
            MYWidthConfirmMenuView mYWidthConfirmMenuView5 = this.mEditorWidthConfirmView;
            if (mYWidthConfirmMenuView5 != null && mYWidthConfirmMenuView5.isShow()) {
                this.mEditorWidthConfirmView.setSeekPress(this.mEditorWidthConfirmView.getBaseInfo());
            }
        } else if (Constants.STATE_EDTI_CUT.equals(currentState) || Constants.STATE_EDTI_COPY.equals(currentState)) {
            initEditorTimeline();
            StateManager.getInstance().changeState(Constants.STATE_MAIN_MENU, 3);
        } else if (Constants.STATE_ADJUST.equals(currentState) && (mYWidthConfirmMenuView = this.mEditorWidthConfirmView) != null && mYWidthConfirmMenuView.isShow()) {
            this.mEditorWidthConfirmView.setClipInfo(null);
            this.mEditorWidthConfirmView.setSeekPress(this.mEditorWidthConfirmView.getBaseInfo());
        }
        this.mEditTimeline.refreshLineView(Constants.STATE_MAIN_MENU.equals(currentState));
        PixelPerMicrosecondUtil.setScale(PixelPerMicrosecondUtil.getScale());
    }

    @Override // com.meishe.myvideo.interfaces.OnMiddleOperationClickListener
    public void onZoomEventCallback() {
        Intent intent = new Intent(this, FullPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("nowTime", this.mEditorEngine.getCurrentTimelinePosition());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnHandAction
    public void getAvFileDuration(int i, boolean z) {
        this.mEditTimeline.setClipFileDuration(Long.valueOf(this.mEditorEngine.getAvFileDuration(i, z)), z);
    }

    @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnHandAction
    public void onHandDragToSeek(long j) {
        this.mEditorEngine.seekTimeline(j, 0);
    }

    @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnHandAction
    public void onChangeTimeline(int i, boolean z, long j, long j2) {
        this.mEditorEngine.onChangeTrimTimelineData(i, z, j);
        boolean onTimelineDurationChange = onTimelineDurationChange();
        this.mEditorEngine.onChangeTrimTimelineObject(i, z, j);
        if (onTimelineDurationChange) {
            refreshEditorTimelineView(-1);
        }
        onMainTrackChange();
        PixelPerMicrosecondUtil.setScale(PixelPerMicrosecondUtil.getScale());
        saveOperation();
    }

    @Override // com.meishe.myvideo.view.MYEditorTimeLine.OnHandAction
    public void onTimeLineClickOutSide() {
        Logger.d(TAG, "onTimeLineClickOutSide: " + StateManager.getInstance().getCurrentState());
        hideBottomView();
        if (Constants.STATE_EDIT.equals(StateManager.getInstance().getCurrentState())) {
            this.mEditTimeline.setUnSelectSpan();
            StateManager.getInstance().changeState(Constants.STATE_MAIN_MENU, 3);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void hideBottomView() {
        String currentState = StateManager.getInstance().getCurrentState();
        Logger.d(TAG, "hideBottomView :" + currentState);
        MYWidthConfirmMenuView mYWidthConfirmMenuView = this.mEditorWidthConfirmView;
        if (mYWidthConfirmMenuView != null && mYWidthConfirmMenuView.isShow()) {
            this.mEditorWidthConfirmView.hide();
        }
        BottomControlView bottomControlView = this.mEditBottomController;
        if (bottomControlView != null && bottomControlView.getVisibility() == 0) {
            this.mEditBottomController.dismiss();
        }
        MYMultiBottomView mYMultiBottomView = this.mEditBottomView;
        if (mYMultiBottomView != null && mYMultiBottomView.isShow()) {
            if (this.mEditBottomView.getType() == 3) {
                saveOperation();
            }
            this.mEditBottomView.hide();
        }
        MYRecordMenuView mYRecordMenuView = this.mEditRecordView;
        if (mYRecordMenuView != null && mYRecordMenuView.isShown()) {
            this.mEditRecordView.hide();
        }
        MYCompoundCaptionEditView mYCompoundCaptionEditView = this.mCompoundCaptionEditView;
        if (mYCompoundCaptionEditView != null && mYCompoundCaptionEditView.isShow()) {
            saveOperation();
            this.mCompoundCaptionEditView.hide();
        }
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onAudioMenuClicked(View view, String str, BaseInfo baseInfo) {
        if (getString(R.string.sub_menu_audio_edit_divide).equals(str)) {
            this.mEditorEngine.audioEditCutClip(this.mContext);
        } else if (getString(R.string.sub_menu_audio_edit_speed).equals(str)) {
            EditChangeSpeedView editChangeSpeedView = new EditChangeSpeedView(this.mContext, this.mCurrSelectedAudioClip.getType());
            editChangeSpeedView.setSpeed((float) this.mCurrSelectedAudioClip.getSpeed());
            this.mEditBottomController.showDialogView(editChangeSpeedView);
        } else if (getString(R.string.sub_menu_audio_edit_volume).equals(str)) {
            EditChangeVolumeView editChangeVolumeView = new EditChangeVolumeView(this.mContext, this.mCurrSelectedAudioClip.getType());
            float volume = this.mCurrSelectedAudioClip.getVolume();
            editChangeVolumeView.setSeekBarMax(200);
            editChangeVolumeView.setProgress(volume);
            this.mEditBottomController.showDialogView(editChangeVolumeView);
        } else if (getString(R.string.sub_menu_audio_edit_copy).equals(str)) {
            this.mEditorEngine.audioEditCopyClip(this.mContext);
        } else if (getString(R.string.sub_menu_audio_edit_delete).equals(str)) {
            this.mEditorEngine.audioEditDeleteClip();
        } else if (getString(R.string.sub_menu_audio_edit_change_voice).equals(str)) {
            this.mEditBottomController.showDialogView(new EditChangeVoiceView(this.mContext));
        } else if (getString(R.string.sub_menu_audio_transition).equals(str)) {
            EditChangeTransitionView editChangeTransitionView = new EditChangeTransitionView(this.mContext);
            editChangeTransitionView.setSeekBarMax(10);
            editChangeTransitionView.setProgress(this.mCurrSelectedAudioClip.getFadeInDuration(), this.mCurrSelectedAudioClip.getFadeOutDuration());
            this.mEditBottomController.showDialogView(editChangeTransitionView);
        }
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onCaptionEditMenuClicked(View view, String str, BaseInfo baseInfo) {
        if (getString(R.string.sub_menu_caption_edit_copy).equals(str)) {
            MeicamCaptionClip copyCaption = this.mEditorEngine.copyCaption(this.mCurrSelectedCaption);
            if (copyCaption != null) {
                Logger.d(TAG, "onCaptionEditMenuClicked: " + copyCaption.getInPoint() + "  " + ((int) copyCaption.getzValue()));
                this.mEditorTrackView.setSelect((int) copyCaption.getzValue(), copyCaption.getInPoint());
            }
        } else if (getString(R.string.sub_menu_caption_edit_delete).equals(str)) {
            this.mEditorEngine.removeCaption(this.mCurrSelectedCaption);
        } else if (getString(R.string.sub_menu_caption_edit_style).equals(str)) {
            showCaptionView();
        }
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onComCaptionEditMenuClicked(View view, String str, BaseInfo baseInfo) {
        if (getString(R.string.sub_menu_compound_caption_edit_copy).equals(str)) {
            MeicamCompoundCaptionClip copyComCaption = this.mEditorEngine.copyComCaption(this.mCurrCompoundCaption);
            if (copyComCaption != null) {
                Logger.d(TAG, "onComCaptionEditMenuClicked: " + copyComCaption.getInPoint() + "  " + ((int) copyComCaption.getzValue()));
                this.mEditorTrackView.setSelect((int) copyComCaption.getzValue(), copyComCaption.getInPoint());
            }
        } else if (getString(R.string.sub_menu_compound_caption_edit_delete).equals(str)) {
            this.mEditorEngine.removeCompoundCaption(this.mCurrCompoundCaption);
        }
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onStickerEditMenuClicked(View view, String str, BaseInfo baseInfo) {
        if (getString(R.string.sub_menu_sticker_edit_copy).equals(str)) {
            MeicamStickerClip copySticker = this.mEditorEngine.copySticker(this.mCurrSelectedSticker);
            if (copySticker != null) {
                Logger.d(TAG, "onStickerEditMenuClicked: " + copySticker.getInPoint() + "  " + ((int) copySticker.getzValue()));
                this.mEditorTrackView.setSelect((int) copySticker.getzValue(), copySticker.getInPoint());
            }
        } else if (getString(R.string.sub_menu_sticker_edit_delete).equals(str)) {
            this.mEditorEngine.deleteSticker(this.mCurrSelectedSticker);
        } else if (getString(R.string.sub_menu_sticker_edit_mirror).equals(str)) {
            this.mEditorEngine.changeStickerMirror(this.mCurrSelectedSticker);
        }
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onPipEditMenuClicked(View view, String str, BaseInfo baseInfo) {
        if (getString(R.string.sub_menu_name_edit_delete).equals(str)) {
            this.mEditorEngine.deleteClip(this.mCurSelectVideoClip, this.mSelectTrackData.getTrackIndex() + 1, true);
        } else if (getString(R.string.sub_menu_name_edit_divide).equals(str)) {
            this.mEditorEngine.cutClip(this.mCurSelectVideoClip, this.mSelectTrackData.getTrackIndex() + 1, true);
        } else if (getString(R.string.sub_menu_name_edit_filter).equals(str)) {
            this.mEditorWidthConfirmView.show(str, MenuDataManager.getData(getString(R.string.main_menu_name_filter), this.mContext), this.mCurSelectVideoClip);
        } else if (getString(R.string.sub_menu_name_edit_adjust).equals(str)) {
            this.mEditorWidthConfirmView.show(str, MenuDataManager.getData(getString(R.string.main_menu_name_adjust), this.mContext), this.mCurSelectVideoClip);
        } else if (getString(R.string.sub_menu_name_edit_rotation).equals(str)) {
            this.mEditorEngine.changeRotation(this.mCurSelectVideoClip);
        } else if (getString(R.string.sub_menu_name_edit_opacity).equals(str)) {
            EditChangeOpacityView editChangeOpacityView = new EditChangeOpacityView(this.mContext, this.mCurSelectVideoClip.getType());
            EditChangeOpacityView editChangeOpacityView2 = editChangeOpacityView;
            editChangeOpacityView2.setSeekBarMax(100);
            MeicamVideoClip meicamVideoClip = this.mCurSelectVideoClip;
            if (meicamVideoClip != null) {
                editChangeOpacityView2.setCurProgress(meicamVideoClip.getOpacity() * 100.0f);
            }
            this.mEditBottomController.showDialogView(editChangeOpacityView);
        } else if (getString(R.string.sub_menu_name_edit_mirror).equals(str)) {
            this.mEditorEngine.changeMirror(this.mCurSelectVideoClip);
        } else if (getString(R.string.sub_menu_name_edit_cut).equals(str)) {
            Bundle bundle = new Bundle();
            NvsVideoResolution videoRes = this.mEditorEngine.getCurrentTimeline().getVideoRes();
            bundle.putInt(ClipCuttingActivity.INTENT_KEY_TIMELINE_HEIGHT, videoRes.imageHeight);
            bundle.putInt(ClipCuttingActivity.INTENT_KEY_TIMELINE_WIDTH, videoRes.imageWidth);
            bundle.putInt(ClipCuttingActivity.INTENT_KEY_TRACK_INDEX, this.mCurrentTrackIndex);
            TimelineData.getInstance().setSelectedMeicamClipInfo(this.mCurSelectVideoClip);
            AppManager.getInstance().jumpActivityForResult(this, ClipCuttingActivity.class, bundle, 103);
        } else if (getString(R.string.sub_menu_name_edit_copy).equals(str)) {
            this.mEditorEngine.copyVideoClip(this.mCurSelectVideoClip, true);
        } else if (getString(R.string.sub_menu_name_edit_beauty).equals(str)) {
            showBeautyView(this.mCurSelectVideoClip);
        } else if (getString(R.string.sub_menu_name_edit_reverse).equals(str)) {
            MeicamVideoClip meicamVideoClip2 = this.mCurSelectVideoClip;
            if (meicamVideoClip2 == null) {
                return;
            }
            if (meicamVideoClip2.isConvertSuccess()) {
                this.mEditorEngine.setVideoConvert(this.mCurSelectVideoClip, this.mSelectTrackData.getTrackIndex() + 1);
                return;
            }
            showCenterProgress(getResources().getString(R.string.reverting));
            this.mConvertFileManager.convertFile(this.mCurSelectVideoClip);
        } else if (getString(R.string.sub_menu_name_edit_freeze_frame).equals(str)) {
            this.mEditorEngine.freezeFrameClip(this.mCurSelectVideoClip, this.mSelectTrackData.getTrackIndex() + 1, true);
        } else if (str.equals(getString(R.string.sub_menu_name_edit_speed))) {
            EditChangeSpeedView editChangeSpeedView = new EditChangeSpeedView(this.mContext, this.mCurSelectVideoClip.getType());
            editChangeSpeedView.setSpeed((float) this.mCurSelectVideoClip.getSpeed());
            this.mEditBottomController.showDialogView(editChangeSpeedView);
            this.mEditorTrackView.setPipDuringVisiableStatus(false);
        } else if (str.equals(getString(R.string.sub_menu_name_edit_volume))) {
            EditChangeVolumeView editChangeVolumeView = new EditChangeVolumeView(this.mContext, this.mCurSelectVideoClip.getType());
            float volume = this.mCurSelectVideoClip.getVolume();
            editChangeVolumeView.setSeekBarMax(200);
            editChangeVolumeView.setProgress(volume);
            this.mEditBottomController.showDialogView(editChangeVolumeView);
        }
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onEffectEditMenuClicked(View view, String str, BaseInfo baseInfo) {
        if (getResources().getString(R.string.sub_menu_name_edit_delete).equals(str)) {
            this.mEditorEngine.deleteEffect(this.mSelectTrackData);
        } else if (getResources().getString(R.string.sub_menu_name_edit_copy).equals(str)) {
            this.mEditorEngine.copyEffect(this.mSelectTrackData, getApplicationContext());
        }
        saveOperation();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshAudioView() {
        this.mTrackListHashMap = TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_AUDIO);
        this.mEditorTrackView.setTrackViewLayoutData(this.mTrackListHashMap, this.mTimeline.getDuration());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private MeicamAudioClip createMeicamAudioInfo(long j, NvsAudioTrack nvsAudioTrack) {
        MeicamAudioClip meicamAudioClip = this.mCurrSelectedAudioClip;
        if (meicamAudioClip == null) {
            Log.e(TAG, "找不到当前的音乐片段");
            return null;
        }
        MeicamAudioClip meicamAudioClip2 = (MeicamAudioClip) meicamAudioClip.clone();
        meicamAudioClip2.loadData((NvsAudioClip) this.mCurrSelectedAudioClip.getObject());
        meicamAudioClip2.setInPoint(j);
        meicamAudioClip2.setOutPoint((this.mCurrSelectedAudioClip.getOutPoint() + j) - this.mCurrSelectedAudioClip.getInPoint());
        if (meicamAudioClip2.bindToTimeline(nvsAudioTrack) != null) {
            TimelineDataUtil.addAudioClipInfoByTrackIndex(nvsAudioTrack, meicamAudioClip2);
            refreshAudioView();
            BaseUIClip baseUIClip = TrackViewDataHelper.getInstance().getBaseUIClip(this.mTrackListHashMap, nvsAudioTrack.getIndex(), j);
            this.mSeekFlag = 111;
            this.mEditorTrackView.setSelect(baseUIClip);
            saveOperation();
        }
        return meicamAudioClip2;
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onMenuClicked(View view, String str, BaseInfo baseInfo, List<BaseInfo> list) {
        Resources resources = getResources();
        if (!resources.getString(R.string.sub_menu_name_edit_divide).equals(str) && !getResources().getString(R.string.main_menu_name_water_mark).equals(str) && !getResources().getString(R.string.sub_menu_comb_name_effect).equals(str)) {
            toOtherMenu();
        }
        if (str.equals(resources.getString(R.string.ic_add_voice_music))) {
            Bundle bundle = new Bundle();
            bundle.putInt("select_music_from", 5002);
            AppManager.getInstance().jumpActivityForResult(this, SelectMusicActivity.class, bundle, 112);
            overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_record))) {
            this.mEditRecordView.show();
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_speed))) {
            EditChangeSpeedView editChangeSpeedView = new EditChangeSpeedView(this.mContext, this.mCurSelectVideoClip.getType());
            editChangeSpeedView.setSpeed((float) this.mCurSelectVideoClip.getSpeed());
            this.mEditBottomController.showDialogView(editChangeSpeedView);
            this.mEditTimeline.getTimelineSpan().changeDurationTextState(false);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_volume))) {
            EditChangeVolumeView editChangeVolumeView = new EditChangeVolumeView(this.mContext, this.mCurSelectVideoClip.getVideoType());
            float volume = this.mCurSelectVideoClip.getVolume();
            editChangeVolumeView.setSeekBarMax(200);
            editChangeVolumeView.setProgress(volume);
            this.mEditBottomController.showDialogView(editChangeVolumeView);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_analysis))) {
            Bundle bundle2 = new Bundle();
            bundle2.putInt("select_media_from", 4004);
            AppManager.getInstance().jumpActivityForResult(this, MaterialSingleSelectActivity.class, bundle2, 113);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_divide))) {
            this.mEditorEngine.cutClip(this.mCurSelectVideoClip, 0, false);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_delete))) {
            this.mEditorEngine.deleteClip(this.mCurSelectVideoClip, 0, false);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_reverse))) {
            MeicamVideoClip meicamVideoClip = this.mCurSelectVideoClip;
            if (meicamVideoClip == null) {
                return;
            }
            if (meicamVideoClip.isConvertSuccess()) {
                this.mEditorEngine.setVideoConvert(this.mCurSelectVideoClip, 0);
                return;
            }
            showCenterProgress(getResources().getString(R.string.reverting));
            this.mConvertFileManager.convertFile(this.mCurSelectVideoClip);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_freeze_frame))) {
            this.mEditorEngine.freezeFrameClip(this.mCurSelectVideoClip, 0, false);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_opacity))) {
            float opacity = this.mCurSelectVideoClip.getOpacity();
            EditChangeOpacityView editChangeOpacityView = new EditChangeOpacityView(this.mContext, this.mCurSelectVideoClip.getType());
            EditChangeOpacityView editChangeOpacityView2 = editChangeOpacityView;
            editChangeOpacityView2.setSeekBarMax(100);
            editChangeOpacityView2.setCurProgress(opacity * 100.0f);
            this.mEditBottomController.showDialogView(editChangeOpacityView);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_rotation))) {
            this.mEditorEngine.changeRotation(this.mCurSelectVideoClip);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_mirror))) {
            this.mEditorEngine.changeMirror(this.mCurSelectVideoClip);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_beauty))) {
            showBeautyView(this.mCurSelectVideoClip);
        } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_copy))) {
            this.mEditorEngine.copyVideoClip(this.mCurSelectVideoClip, false);
        } else if (str.equals(resources.getString(R.string.sub_menu_caption))) {
            showCaptionView();
        } else if (str.equals(resources.getString(R.string.sub_menu_caption_compound_caption))) {
            showCompoundCaptionView();
        } else if (str.equals(resources.getString(R.string.sub_menu_caption_sticker))) {
            showStickerView();
        } else if (str.equals(resources.getString(R.string.sub_menu_tab_watermark))) {
            showWaterView(1);
        } else if (str.equals(resources.getString(R.string.sub_menu_tab_watermark_effect))) {
            showWaterView(2);
        } else {
            String str2 = null;
            if (str.equals(resources.getString(R.string.sub_menu_canvas_color))) {
                this.mEditCanvasColorView.show();
                MeicamVideoClip meicamVideoClip2 = this.mCurSelectVideoClip;
                if (meicamVideoClip2 != null) {
                    str2 = meicamVideoClip2.getBackgroundInfo().getStringVal("Description String");
                }
                MessageEvent.sendEvent(StoryboardUtil.getSourcePathFromStory(str2), (int) MessageEvent.MESSAGE_TYPE_CHANGE_COLOR_BACKGROUND_UI);
            } else if (str.equals(resources.getString(R.string.sub_menu_canvas_style))) {
                this.mEditCanvasStyleView.show();
                MeicamVideoClip meicamVideoClip3 = this.mCurSelectVideoClip;
                if (meicamVideoClip3 != null) {
                    str2 = meicamVideoClip3.getBackgroundInfo().getStringVal("Description String");
                }
                MessageEvent.sendEvent(StoryboardUtil.getSourcePathFromStory(str2), (int) MessageEvent.MESSAGE_TYPE_CHANGE_IMAGE_BACKGROUND_UI);
            } else if (str.equals(resources.getString(R.string.sub_menu_canvas_blur))) {
                this.mEditCanvasBlurView.show();
                MeicamVideoClip meicamVideoClip4 = this.mCurSelectVideoClip;
                if (meicamVideoClip4 != null) {
                    str2 = meicamVideoClip4.getBackgroundInfo().getStringVal("Description String");
                }
                MessageEvent.sendEvent(StoryboardUtil.getBlurStrengthFromStory(str2), (int) MessageEvent.MESSAGE_TYPE_CHANGE_BLUR_BACKGROUND_UI);
            } else if (str.equals(resources.getString(R.string.main_menu_name_background))) {
                this.mVideoFragment.setDrawRectVisible(8);
                setCurrentMainTrackClip();
                this.mVideoFragment.setVideoClipInfo(TimelineData.getInstance().getSelectedMeicamClipInfo(), 0);
                this.mVideoFragment.setPipTransformViewVisible(0);
                this.mEditTimeline.changeMode(2);
                this.mEditTimeline.refreshCoverView(this.mEditorEngine.getCurrentTimelinePosition());
            } else if (str.equals(resources.getString(R.string.main_menu_name_picture_in_picture))) {
                this.mEditorTrackView.showPipTrackView(this.mTimeline);
                this.mVideoFragment.setDrawRectVisible(8);
                this.mVideoFragment.setPipTransformViewVisible(0);
            } else if (str.equals(resources.getString(R.string.sub_pic_in_menu_add_pic))) {
                Bundle bundle3 = new Bundle();
                bundle3.putInt("select_media_from", 4007);
                AppManager.getInstance().jumpActivityForResult(this, MaterialSingleSelectActivity.class, bundle3, 101);
            } else if (str.equals(resources.getString(R.string.sub_menu_name_edit_cut))) {
                Bundle bundle4 = new Bundle();
                NvsVideoResolution videoRes = this.mEditorEngine.getCurrentTimeline().getVideoRes();
                bundle4.putInt(ClipCuttingActivity.INTENT_KEY_TIMELINE_HEIGHT, videoRes.imageHeight);
                bundle4.putInt(ClipCuttingActivity.INTENT_KEY_TIMELINE_WIDTH, videoRes.imageWidth);
                bundle4.putInt(ClipCuttingActivity.INTENT_KEY_TRACK_INDEX, 0);
                if (this.mContext.getResources().getString(R.string.main_menu_name_background).equals(StateManager.getInstance().getCurrentState())) {
                    setCurrentMainTrackClip();
                    this.mVideoFragment.setVideoClipInfo(TimelineData.getInstance().getSelectedMeicamClipInfo(), 0);
                } else {
                    this.mVideoFragment.setVideoClipInfo(this.mCurSelectVideoClip, 1);
                }
                AppManager.getInstance().jumpActivityForResult(this, ClipCuttingActivity.class, bundle4, 103);
            } else if (str.equals(this.mContext.getResources().getString(R.string.main_menu_name_theme)) || str.equals(this.mContext.getResources().getString(R.string.effect_frame)) || str.equals(this.mContext.getResources().getString(R.string.effect_shaking)) || str.equals(this.mContext.getResources().getString(R.string.effect_lively)) || str.equals(this.mContext.getResources().getString(R.string.effect_dream)) || str.equals(this.mContext.getResources().getString(R.string.sub_menu_name_edit_filter)) || str.equals(this.mContext.getResources().getString(R.string.sub_menu_name_edit_adjust))) {
                this.mEditorEngine.cloneFxTrack();
                this.mEditorWidthConfirmView.show(str, list, this.mCurSelectVideoClip);
            } else if ((baseInfo instanceof EditMenuInfo) && ((EditMenuInfo) baseInfo).mIsShowSelectState) {
                NvsVideoClip currentClip = this.mEditTimeline.getCurrentClip();
                if (this.mEditMenuView.equals(resources.getString(R.string.adjust_amount))) {
                    currentClip.appendBuiltinFx("Sharpen").setFloatVal("Amount", 1.0d);
                }
                this.mEditMenuView.showAdjustExpand(baseInfo.mName);
            } else if (this.mContext.getResources().getString(R.string.main_menu_name_fx).equals(str)) {
                this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_TIMELINE_FX), this.mEditorEngine.getCurrentTimeline().getDuration());
            } else if (this.mContext.getResources().getString(R.string.main_menu_name_ratio).equals(str)) {
                this.mEditTimeline.setTimelineTransitionVisable(false);
            }
        }
    }

    private void showStickerView() {
        this.mStickerFragmentArrayList = new ArrayList<>();
        this.mStickerAllFragment = new StickerAllFragment();
        this.mStickerFragmentArrayList.add(this.mStickerAllFragment);
        this.mStickerCustomFragment = new StickerCustomFragment();
        this.mStickerFragmentArrayList.add(this.mStickerCustomFragment);
        this.mEditBottomView.setFragmentManager(getSupportFragmentManager());
        this.mEditBottomView.show(new String[]{this.mContext.getResources().getString(R.string.fragment_menu_table_all), this.mContext.getResources().getString(R.string.fragment_menu_table_custom)}, this.mStickerFragmentArrayList, 0, false, false, 2);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void toOtherMenu() {
        this.mEditTimeline.toOtherMenu();
        this.mEditorTrackView.toOtherMenu();
    }

    public void showBeautyView(MeicamVideoClip meicamVideoClip) {
        ArrayList<? extends Fragment> arrayList = new ArrayList<>();
        BeautyFragment beautyFragment = new BeautyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BeautyShapeFragment.MRICAMVIDEOCLIP, meicamVideoClip);
        beautyFragment.setArguments(bundle);
        arrayList.add(beautyFragment);
        BeautyShapeFragment beautyShapeFragment = new BeautyShapeFragment();
        beautyShapeFragment.setArguments(bundle);
        arrayList.add(beautyShapeFragment);
        this.mEditBottomView.setFragmentManager(getSupportFragmentManager());
        this.mEditBottomView.show(new String[]{this.mContext.getResources().getString(R.string.fragment_menu_beauty), this.mContext.getResources().getString(R.string.fragment_menu_beauty_type)}, arrayList, 0, false, true, 4);
    }

    private void showWaterView(int i) {
        ArrayList<? extends Fragment> arrayList = new ArrayList<>();
        this.mWaterFragment = new WaterFragment();
        arrayList.add(this.mWaterFragment);
        this.mWaterEffectFragment = new WaterEffectFragment();
        arrayList.add(this.mWaterEffectFragment);
        String[] strArr = {this.mContext.getResources().getString(R.string.fragment_menu_water), this.mContext.getResources().getString(R.string.fragment_menu_water_effect)};
        this.mEditBottomView.setFragmentManager(getSupportFragmentManager());
        if (i == 1) {
            this.mEditBottomView.show(strArr, arrayList, 0, false, false, 1);
        } else if (i == 2) {
            this.mEditBottomView.show(strArr, arrayList, 1, false, false, 6);
        }
        this.mEditorEngine.handleEventMessage(null, i);
        this.mEditBottomView.setOnWaterMarkChangeListener(new MYMultiBottomView.OnWaterMarkChangeListener() {
            /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass23 */

            @Override // com.meishe.myvideo.view.MYMultiBottomView.OnWaterMarkChangeListener
            public void onTabSelected(int i) {
                DraftEditActivity.this.mWaterMarkTabSelectedIndex = i;
                if (i == 0) {
                    DraftEditActivity.this.mEditorEngine.handleEventMessage(null, 1);
                } else {
                    DraftEditActivity.this.mEditorEngine.handleEventMessage(null, 2);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showTransitionView(int i) {
        hideBottomView();
        ArrayList<? extends Fragment> arrayList = new ArrayList<>();
        arrayList.add(new TransitionGeneralFragment(this.mTargetTransitionIndex, this));
        arrayList.add(new Transition3DFragment(this.mTargetTransitionIndex, this));
        arrayList.add(new TransitionEffectFragment(this.mTargetTransitionIndex, this));
        String[] strArr = {this.mContext.getResources().getString(R.string.tab_name_common), this.mContext.getResources().getString(R.string.tab_name_3D), this.mContext.getResources().getString(R.string.tab_name_effects)};
        this.mEditBottomView.setFragmentManager(getSupportFragmentManager());
        this.mEditBottomView.show(strArr, arrayList, i, false, true, 5);
        this.mEditBottomView.setTransitionData(this.mCurrTransitionData);
        ((TransitionFragment) arrayList.get(i)).setTransitionData(this.mCurrTransitionData);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showCaptionView() {
        CaptionStyleFragment captionStyleFragment;
        ArrayList<Fragment> arrayList = new ArrayList<>();
        this.mCaptionStyleFragment = new CaptionStyleFragment();
        arrayList.add(this.mCaptionStyleFragment);
        this.mCaptionColorFragment = new CaptionColorFragment();
        arrayList.add(this.mCaptionColorFragment);
        this.mCaptionOutlineFragment = new CaptionOutlineFragment();
        arrayList.add(this.mCaptionOutlineFragment);
        arrayList.add(new CaptionFontFragment());
        arrayList.add(new CaptionLetterSpacingFragment());
        arrayList.add(new CaptionPositionFragment());
        String[] stringArray = getResources().getStringArray(R.array.menu_tab_caption);
        this.mEditBottomView.setFragmentManager(getSupportFragmentManager());
        NvsTimelineCaption curCaption = this.mVideoFragment.getCurCaption();
        if (curCaption == null) {
            this.mEditBottomView.show(stringArray, arrayList, 0, true, false, 3, this.mKeyboardHeight);
        } else {
            this.mEditBottomView.show(stringArray, arrayList, 0, true, false, 3, curCaption.getText(), this.mKeyboardHeight);
        }
        MeicamCaptionClip meicamCaptionClip = this.mCurrSelectedCaption;
        if (meicamCaptionClip == null || (captionStyleFragment = this.mCaptionStyleFragment) == null) {
            this.mCaptionStyleFragment.setSelect(1);
        } else {
            captionStyleFragment.setSelect(meicamCaptionClip);
        }
    }

    private void showCompoundCaptionView() {
        this.mEditCompoundCaptionView.show();
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onSwitchMainMenu() {
        this.mVideoFragment.setEditMode(0);
        this.mVideoFragment.setDrawRectVisible(false);
        this.mVideoFragment.setPipTransformViewVisible(8);
        this.mEditorTrackView.clear();
        this.mEditorTrackView.toMainMenu();
        this.mEditTimeline.toMainMenu();
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onRatioClick(int i) {
        EditorEngine editorEngine = this.mEditorEngine;
        if (editorEngine != null) {
            editorEngine.changeRatio(i);
        }
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void changeBottomMenu(String str, int i) {
        if (getResources().getString(R.string.main_menu_name_caption).equals(str) || getResources().getString(R.string.main_menu_name_sticker).equals(str) || getResources().getString(R.string.main_menu_name_com_caption).equals(str)) {
            this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_CAPTION), this.mEditorEngine.getCurrentTimeline().getDuration());
            toOtherMenu();
        } else if (getResources().getString(R.string.main_menu_name_edit).equals(str)) {
            if (i != 2 && i != 3) {
                this.mEditTimeline.setSelectSpan(this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline));
                int clipIndexByTime = this.mEditTimeline.getClipIndexByTime(this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline));
                List<ClipInfo> mainTrackVideoClip = TimelineDataUtil.getMainTrackVideoClip();
                if (mainTrackVideoClip != null && mainTrackVideoClip.size() > clipIndexByTime) {
                    this.mCurSelectVideoClip = (MeicamVideoClip) mainTrackVideoClip.get(clipIndexByTime);
                    this.mCurrentInPoint = this.mCurSelectVideoClip.getInPoint();
                    this.mCurrentTrackIndex = 0;
                }
                TimelineData.getInstance().setSelectedMeicamClipInfo(this.mCurSelectVideoClip);
                this.mEditorTrackView.toOtherMenu();
            }
        } else if (str.equals(getResources().getString(R.string.main_menu_name_music)) || str.equals(getResources().getString(R.string.main_menu_name_dubbing))) {
            refreshAudioView();
            toOtherMenu();
        }
    }

    @Override // com.meishe.myvideo.view.MenuView.OnMenuClickListener
    public void onBackClick() {
        if (Constants.STATE_RATIO.equals(StateManager.getInstance().getCurrentState())) {
            this.mEditTimeline.setTimelineTransitionVisable(true);
        }
        this.mEditorTrackView.clickOutSide();
        this.mEditTimeline.changeMode(1);
        if (Constants.STATE_STICKER.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_CAPTION.equals(StateManager.getInstance().getCurrentState()) || Constants.STATE_COMPOUND_CAPTION.equals(StateManager.getInstance().getCurrentState())) {
            this.mVideoFragment.setDrawRectVisible(false);
            this.mVideoFragment.setCurCaption(null);
            this.mVideoFragment.setCurCompoundCaption(null);
            this.mVideoFragment.setCurAnimateSticker(null);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.fragment.app.FragmentActivity
    public void onResume() {
        super.onResume();
        VideoFragment videoFragment = this.mVideoFragment;
        if (videoFragment != null) {
            videoFragment.connectTimelineWithLiveWindow();
            this.mEditorEngine.seekTimeline(2);
            seekViewOnPlay(this.mEditorEngine.getCurrentTimelinePosition());
        }
        WaterFragment waterFragment = this.mWaterFragment;
        if (waterFragment != null) {
            waterFragment.upDateSelected();
        }
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        MediaData mediaData;
        MusicInfo musicInfo;
        super.onActivityResult(i, i2, intent);
        if (i == 110) {
            TimelineUtil.rebuildTimeline(this.mTimeline);
            this.mAudioRecordManager = AudioRecordManager.getInstance();
            NvsTimeline nvsTimeline = this.mTimeline;
            if (nvsTimeline != null) {
                this.mEditTimeline.setTimeline(nvsTimeline);
                this.mEditTimeline.outOfSequenceView();
                initEditorTimeline();
                onMainTrackChange();
                this.mEditorEngine.seekTimeline(this.mCurrentPosition, 0);
                saveOperation();
                PixelPerMicrosecondUtil.setScale(PixelPerMicrosecondUtil.getScale());
            }
        } else if (i == 101) {
            if (intent != null) {
                Logger.d(TAG, "onActivityResult: add pip");
                MeicamVideoClip meicamVideoClip = (MeicamVideoClip) intent.getSerializableExtra(MaterialSingleSelectActivity.KEY_FILE_PATH);
                if (!TextUtils.isEmpty(meicamVideoClip.getFilePath())) {
                    long currentTimelinePosition = this.mEditorEngine.getCurrentTimelinePosition();
                    if (this.mTimeline.getDuration() - currentTimelinePosition < CommonData.MIN_SHOW_LENGTH_DURATION) {
                        Log.e(TAG, "onActivityResult: add pip: 空间太小了！");
                        return;
                    }
                    MeicamVideoFx meicamVideoFx = new MeicamVideoFx();
                    meicamVideoFx.setDesc("Transform 2D");
                    meicamVideoFx.setFloatVal("Scale X", 0.8f);
                    meicamVideoFx.setFloatVal("Scale Y", 0.8f);
                    meicamVideoFx.setFloatVal("Rotation", 0.0f);
                    meicamVideoFx.setFloatVal("Trans X", 0.0f);
                    meicamVideoFx.setFloatVal("Trans Y", 0.0f);
                    meicamVideoFx.setType("builtin");
                    meicamVideoFx.setSubType("Transform 2D");
                    meicamVideoClip.getVideoFxs().add(meicamVideoFx);
                    meicamVideoClip.setInPoint(currentTimelinePosition);
                    long duration = this.mTimeline.getDuration();
                    meicamVideoClip.setTrimIn(0);
                    long orgDuration = meicamVideoClip.getOrgDuration();
                    if (meicamVideoClip.getVideoType().equals(CommonData.CLIP_IMAGE)) {
                        orgDuration = CommonData.DEFAULT_LENGTH;
                        meicamVideoClip.setTrimIn(CommonData.DEFAULT_TRIM_IN);
                        meicamVideoClip.setTrimOut(meicamVideoClip.getTrimIn() + CommonData.DEFAULT_LENGTH);
                    } else {
                        meicamVideoClip.setTrimOut(orgDuration);
                    }
                    if (orgDuration + currentTimelinePosition > duration) {
                        meicamVideoClip.setTrimOut(duration - currentTimelinePosition);
                        if (meicamVideoClip.getVideoType().equals(CommonData.CLIP_IMAGE)) {
                            meicamVideoClip.setTrimOut((meicamVideoClip.getTrimIn() + duration) - currentTimelinePosition);
                        }
                    }
                    meicamVideoClip.setOutPoint((currentTimelinePosition + meicamVideoClip.getTrimOut()) - meicamVideoClip.getTrimIn());
                    Log.e(TAG, "onActivityResult: " + meicamVideoClip.getTrimIn() + " " + meicamVideoClip.getTrimOut() + " " + meicamVideoClip.getInPoint() + "  " + meicamVideoClip.getOutPoint() + "  OrgDuratio: " + meicamVideoClip.getOrgDuration());
                    int pipVideoClipIndex = this.mEditorEngine.getPipVideoClipIndex(meicamVideoClip.getInPoint(), meicamVideoClip.getOutPoint());
                    this.mEditorEngine.addPipVideoClipToTrack(meicamVideoClip, pipVideoClipIndex);
                    this.mEditorTrackView.showPipTrackView(this.mTimeline);
                    this.mEditorTrackView.setSelect(pipVideoClipIndex - 1, meicamVideoClip.getInPoint());
                    if (meicamVideoClip.getVideoType().equals(CommonData.CLIP_IMAGE)) {
                        this.mEditMenuView.switchToEditorMenu(CommonData.CLIP_IMAGE);
                    } else {
                        this.mEditMenuView.switchToEditorMenu(CommonData.CLIP_VIDEO);
                    }
                    saveOperation();
                }
            }
        } else if (i == 102) {
            if (intent != null) {
                String stringExtra = intent.getStringExtra(MaterialSingleSelectActivity.KEY_FILE_PATH);
                if (!TextUtils.isEmpty(stringExtra)) {
                    this.mEditorEngine.updateImageBackground(stringExtra, 1);
                }
            }
        } else if (i == 112) {
            if (intent != null && (musicInfo = (MusicInfo) intent.getSerializableExtra("select_music")) != null) {
                setAudioTrack(musicInfo.getFilePath(), musicInfo.getTitle(), musicInfo.getTrimIn(), 3);
            }
        } else if (i == 113) {
            if (intent != null && (mediaData = (MediaData) intent.getSerializableExtra(VIDEO_PATH)) != null) {
                setAudioTrack(mediaData.getPath(), mediaData.getDisplayName(), 0, 4);
            }
        } else if (i == 104) {
            if (intent != null) {
                this.mWaterFragment.addDataAndRefresh(intent.getStringExtra(MaterialSingleSelectActivity.KEY_FILE_PATH));
            }
        } else if (i == 103) {
            this.mEditorEngine.seekTimeline(0);
            saveOperation();
        }
    }

    private void setAudioTrack(String str, String str2, long j, int i) {
        long j2;
        long j3;
        MeicamAudioClip meicamAudioClip = new MeicamAudioClip();
        long duration = this.mStreamingContext.getAVFileInfo(str).getDuration();
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        if (duration >= this.mTimeline.getDuration() - timelineCurrentPosition) {
            j3 = this.mTimeline.getDuration();
            j2 = (this.mTimeline.getDuration() + j) - timelineCurrentPosition;
        } else {
            j3 = timelineCurrentPosition + duration;
            j2 = j + duration;
        }
        meicamAudioClip.setOriginalDuring(duration);
        meicamAudioClip.setInPoint(timelineCurrentPosition);
        meicamAudioClip.setOutPoint(j3);
        meicamAudioClip.setTrimIn(j);
        meicamAudioClip.setTrimOut(j2);
        meicamAudioClip.setFilePath(str);
        meicamAudioClip.setAudioType(i);
        meicamAudioClip.setDrawText(str2);
        addAudioClip(meicamAudioClip);
        refreshAudioView();
        this.mEditorTrackView.setSelect(TrackViewDataHelper.getInstance().getBaseUIClip(this.mTrackListHashMap, meicamAudioClip.getIndex(), meicamAudioClip.getInPoint()));
        saveOperation();
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onBackPressed() {
        saveDraft();
        super.onBackPressed();
    }

    private void saveDraft() {
        boolean z;
        DraftManager.getInstance().setTimeLineDuring(this.mTimeline.getDuration());
        Bitmap grabImageFromTimeline = this.mStreamingContext.grabImageFromTimeline(this.mTimeline, 0, new NvsRational(1, 3));
        long currentTimeMillis = System.currentTimeMillis();
        if (TextUtils.isEmpty(this.mDraftDir)) {
            this.mDraftDir = DraftFileUtil.getDirPath(this.mContext, currentTimeMillis);
            z = true;
        } else {
            z = false;
        }
        String str = this.mDraftDir + File.separator + "cover" + currentTimeMillis + ".png";
        ImageUtils.saveBitmap(grabImageFromTimeline, str, true);
        DraftManager.getInstance().setCoverPath(str);
        if (this.mFromType == 1) {
            if (this.mOperateManager.haveOperate()) {
                DraftManager.getInstance().editDraft(this.mDraftDir, currentTimeMillis);
            } else {
                DraftManager.getInstance().editDraftNoModifiedTime(this.mDraftDir);
            }
        } else if (z) {
            DraftManager.getInstance().saveDraftData(this.mDraftDir, currentTimeMillis);
        } else {
            DraftManager.getInstance().editDraft(this.mDraftDir, currentTimeMillis);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        BaseInfo baseInfo = messageEvent.getBaseInfo();
        if (baseInfo == null || !this.mContext.getString(R.string.more).equals(baseInfo.mName)) {
            switch (messageEvent.getEventType()) {
                case 1001:
                    if (baseInfo == null || !this.mContext.getString(R.string.more).equals(baseInfo.mName)) {
                        this.mEditorEngine.handleEventMessage(baseInfo, messageEvent.getEventType());
                        return;
                    }
                    return;
                case 1002:
                    if (baseInfo == null || !this.mContext.getString(R.string.add).equals(baseInfo.mName)) {
                        this.mEditorEngine.handleEventMessage(baseInfo, messageEvent.getEventType());
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("select_media_from", 4003);
                    AppManager.getInstance().jumpActivity(AppManager.getInstance().currentActivity(), MaterialSingleSelectActivity.class, bundle);
                    return;
                case 1003:
                    if (getResources().getString(R.string.add).equals(baseInfo.mName)) {
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("select_media_from", 4001);
                        AppManager.getInstance().jumpActivityForResult(this, MaterialSingleSelectActivity.class, bundle2, 104);
                        return;
                    }
                    this.mEditorEngine.handleEventMessage(baseInfo, messageEvent.getEventType());
                    return;
                case 1004:
                    this.mEditorEngine.handleEventMessage(baseInfo, messageEvent.getEventType());
                    return;
                case MessageEvent.MESSAGE_TYPE_CAPTION_STYLE:
                case 1006:
                case 1007:
                    this.mEditorEngine.handleCaptionStyleAndColor(this.mCurrSelectedCaption, baseInfo, messageEvent.getEventType());
                    return;
                case 1008:
                case 1009:
                case 1010:
                    this.mEditorEngine.handleCaptionColorOpacityAndWidth(this.mCurrSelectedCaption, messageEvent.getProgress(), messageEvent.getEventType());
                    return;
                case 1011:
                    this.mEditorEngine.handleCaptionFont(this.mCurrSelectedCaption, messageEvent.getStrValue(), messageEvent.getEventType());
                    return;
                case 1012:
                case 1013:
                case 1014:
                    this.mEditorEngine.handleCaptionOtherSetting(this.mCurrSelectedCaption, messageEvent.isBooleanValue(), messageEvent.getEventType());
                    return;
                case 1015:
                    this.mEditorEngine.handleCaptionLetterSpace(this.mCurrSelectedCaption, messageEvent.getFloatValue(), messageEvent.getEventType());
                    return;
                case 1016:
                    this.mEditorEngine.handleCaptionPosition(this.mCurrSelectedCaption, messageEvent.getIntValue(), messageEvent.getEventType());
                    return;
                case 1017:
                    this.mEditorEngine.addCompoundCaption(baseInfo);
                    return;
                case 1018:
                    int intValue = messageEvent.getIntValue();
                    Logger.d(TAG, "下载成功 intValue: " + intValue);
                    if (intValue == 4) {
                        this.mStickerAllFragment.refreshData();
                        return;
                    } else if (intValue == 16) {
                        this.mEditCompoundCaptionView.refreshData();
                        return;
                    } else if (intValue == 3) {
                        this.mCaptionStyleFragment.refresh();
                        return;
                    } else {
                        this.mEditorWidthConfirmView.updateMenuView(messageEvent.getIntValue());
                        return;
                    }
                case 1019:
                    this.mEditorEngine.updateImageBackground(baseInfo.getFilePath(), 0);
                    saveOperation();
                    return;
                case 1020:
                    if (baseInfo == null) {
                        this.mEditorEngine.deleteBackground();
                    } else {
                        this.mEditorEngine.updateImageBackground(baseInfo.getFilePath(), 1);
                    }
                    saveOperation();
                    return;
                case 1021:
                    this.mEditorEngine.updateBlurBackground(messageEvent.getFloatValue() * 50.0f);
                    saveOperation();
                    return;
                case MessageEvent.MESSAGE_TYPE_CHANGE_SELECT_BACKGROUND_IMAGE:
                    Bundle bundle3 = new Bundle();
                    bundle3.putInt("select_media_from", 4008);
                    AppManager.getInstance().jumpActivityForResult(this, MaterialSingleSelectActivity.class, bundle3, 102);
                    return;
                case MessageEvent.MESSAGE_TYPE_WIDTH_CONFIRM_EFFECT:
                    switch (baseInfo.mEffectType) {
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                            if (this.mEditorEngine.haveEffectInThisPosition()) {
                                new Handler().post(new Runnable() {
                                    /* class com.meishe.myvideo.activity.DraftEditActivity.AnonymousClass24 */

                                    public void run() {
                                        DraftEditActivity.this.mEditorWidthConfirmView.setSelection(-1);
                                    }
                                });
                                ToastUtil.showShortToast(this, R.string.toast_can_not_add_effect);
                                return;
                            }
                            break;
                    }
                    this.mEditorEngine.ApplyEffect(baseInfo, this.mTargetTransitionIndex, this.mEditTimeline, this.mCurSelectVideoClip);
                    return;
                case 1024:
                case MessageEvent.MESSAGE_TYPE_UPDATE_FILTER_PROGRESS:
                case MessageEvent.MESSAGE_TYPE_UPDATE_SELECT_POSITION:
                case MessageEvent.MESSAGE_TYPE_ADJUST_TOUCH_STOP:
                case MessageEvent.MESSAGE_TYPE_CHANGE_COLOR_BACKGROUND_UI:
                case MessageEvent.MESSAGE_TYPE_CHANGE_IMAGE_BACKGROUND_UI:
                case MessageEvent.MESSAGE_TYPE_CHANGE_BLUR_BACKGROUND_UI:
                default:
                    return;
                case 1025:
                    String currentState = StateManager.getInstance().getCurrentState();
                    if (Constants.STATE_EDIT.equals(currentState)) {
                        this.mEditorEngine.changeOpacity(this.mCurSelectVideoClip, messageEvent.getFloatValue());
                        return;
                    } else if (Constants.STATE_PIC_IN_PIC.equals(currentState)) {
                        this.mEditorEngine.changeOpacity(this.mCurSelectVideoClip, messageEvent.getFloatValue());
                        return;
                    } else {
                        return;
                    }
                case MessageEvent.MESSAGE_TYPE_ADJUST:
                    if (this.mEditorEngine != null && this.mEditTimeline != null) {
                        String currentState2 = StateManager.getInstance().getCurrentState();
                        if (Constants.STATE_EDIT.equals(currentState2)) {
                            this.mEditorEngine.onAdjustDataChanged(this.mContext, this.mCurSelectVideoClip, messageEvent.getProgress(), messageEvent.getStrValue());
                            return;
                        } else if (Constants.STATE_PIC_IN_PIC.equals(currentState2)) {
                            this.mEditorEngine.onAdjustDataChanged(this.mContext, this.mCurSelectVideoClip, messageEvent.getProgress(), messageEvent.getStrValue());
                            return;
                        } else {
                            this.mEditorEngine.onAdjustDataChanged(this.mContext, null, messageEvent.getProgress(), messageEvent.getStrValue());
                            return;
                        }
                    } else {
                        return;
                    }
                case MessageEvent.MESSAGE_TYPE_APPLY_ALL_BACKGROUND_COLOR:
                    this.mEditorEngine.applyAllImageBackground(baseInfo.getFilePath(), 0);
                    saveOperation();
                    return;
                case MessageEvent.MESSAGE_TYPE_APPLY_ALL_BACKGROUND_IMAGE:
                    this.mEditorEngine.applyAllImageBackground(baseInfo.getFilePath(), 1);
                    saveOperation();
                    return;
                case MessageEvent.MESSAGE_TYPE_APPLY_ALL_BACKGROUND_BLUR:
                    this.mEditorEngine.applyAllBlurBackground(messageEvent.getFloatValue() * 50.0f);
                    saveOperation();
                    return;
                case MessageEvent.MESSAGE_TYPE_AUDIO_SPEED:
                    this.mEditorEngine.audioEditChangeClipSpeed(messageEvent.getFloatValue());
                    TimelineDataUtil.refreshMeicamAudioClipByTrackIndex(this.mCurrSelectedAudioClip.getIndex());
                    this.mEditorTrackView.updateTimelineDuration(this.mTimeline.getDuration());
                    refreshAudioView();
                    this.mEditorTrackView.setSelect(TrackViewDataHelper.getInstance().getBaseUIClip(this.mTrackListHashMap, this.mCurrSelectedAudioClip.getIndex(), this.mCurrSelectedAudioClip.getInPoint()));
                    onTimelineDurationChange(true);
                    saveOperation();
                    return;
                case MessageEvent.MESSAGE_TYPE_AUDIO_VOLUME:
                    this.mEditorEngine.audioEditChangeVolume(messageEvent.getFloatValue());
                    return;
                case MessageEvent.MESSAGE_TYPE_AUDIO_VOICE:
                    this.mEditorEngine.audioEditChangeVoice(messageEvent.getStrValue());
                    return;
                case MessageEvent.MESSAGE_TYPE_AUDIO_TRANSTION:
                    String[] split = messageEvent.getStrValue().split("-");
                    this.mEditorEngine.audioEditTransition(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                    refreshAudioView();
                    this.mEditorTrackView.setSelect(TrackViewDataHelper.getInstance().getBaseUIClip(this.mTrackListHashMap, this.mCurrSelectedAudioClip.getIndex(), this.mCurrSelectedAudioClip.getInPoint()));
                    saveOperation();
                    return;
                case MessageEvent.MESSAGE_TYPE_CHANGE_CLIP_FILTER_PROGRESS:
                    this.mEditorEngine.changeClipFilter(this.mCurSelectVideoClip, messageEvent.getFloatValue());
                    return;
                case MessageEvent.MESSAGE_TYPE_CHANGE_TIMELINE_FILTER_PROGRESS:
                    this.mEditorEngine.changeTimelineFilter(messageEvent.getFloatValue());
                    return;
                case MessageEvent.MESSAGE_TYPE_CHANGE_TIMELINE_FILTER_FINISH:
                case MessageEvent.MESSAGE_TYPE_CHANGE_CLIP_FILTER_FINISH:
                case MessageEvent.MESSAGE_TYPE_CHANGE_ADJUST_FINISH:
                    saveOperation();
                    return;
                case MessageEvent.MESSAGE_TYPE_SAVE_OPERATION:
                    saveOperation();
                    return;
                case MessageEvent.MESSAGE_REMOVE_CLIP_FILTER:
                    this.mEditorEngine.removeClipFilter(this.mCurrentInPoint, this.mCurrentTrackIndex);
                    return;
                case MessageEvent.MESSAGE_REMOVE_TIMELINE_FILTER:
                    this.mEditorEngine.removeTimelineClipFilter();
                    return;
                case MessageEvent.MESSAGE_APPLY_ALL_FILTER:
                    this.mEditorEngine.applyAllFilter(this.mCurSelectVideoClip);
                    return;
                case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_COLOR:
                case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_OUT_LINE:
                case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_FONT:
                case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_LETTER_SPACE:
                case MessageEvent.MESSAGE_APPLY_ALL_CAPTION_POSITION:
                    this.mEditorEngine.handleCaptionApplyAll(this.mCurrSelectedCaption, messageEvent.getEventType());
                    return;
                case MessageEvent.MESSAGE_TYPE_VIDEO_SPEED:
                    float floatValue = messageEvent.getFloatValue();
                    int i = this.mCurrentTrackIndex;
                    if (i == 0) {
                        TimelineDataUtil.changeMainTrackVideoClipSpeed(i, this.mCurSelectVideoClip.getInPoint(), floatValue);
                        this.mEditTimeline.refreshTimelineMarkingLineAdapter();
                        this.mEditorEngine.seekTimeline(0);
                        onTimelineDurationChange(false);
                        refreshEditorTimelineView(-1);
                        onMainTrackChange();
                        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
                        MYMiddleOperationView mYMiddleOperationView = this.mEditOperationView;
                        mYMiddleOperationView.setDurationText(FormatUtils.formatTimeStrWithUs(timelineCurrentPosition) + "/" + FormatUtils.formatTimeStrWithUs(this.mTimeline.getDuration()));
                        this.mEditTimeline.getTimelineSpan().initSpeedText();
                        this.mEditTimeline.getTimelineSpan().changeDurationTextState(false);
                    } else {
                        TimelineDataUtil.changePipTrackVideoClipSpeed(i, this.mCurSelectVideoClip.getInPoint(), floatValue);
                        this.mEditorTrackView.updateTimelineDuration(this.mTimeline.getDuration());
                        this.mEditorTrackView.showPipTrackView(this.mTimeline);
                        this.mEditorTrackView.setSelect(this.mCurrentTrackIndex - 1, this.mCurSelectVideoClip.getInPoint());
                        onTimelineDurationChange(true);
                    }
                    saveOperation();
                    return;
                case MessageEvent.MESSAGE_TYPE_VIDEO_VOLUME:
                    this.mCurSelectVideoClip.setVolume(messageEvent.getFloatValue());
                    return;
                case MessageEvent.MESSAGE_TYPE_VIDEO_SPEED_CONFORM:
                    this.mEditorTrackView.setPipDuringVisiableStatus(true);
                    this.mEditTimeline.getTimelineSpan().changeDurationTextState(true);
                    return;
            }
        } else {
            jumpMoreAssectList(baseInfo.mEffectType);
        }
    }

    public void onTimelineDurationChange(boolean z) {
        if (onTimelineDurationChange() && z) {
            refreshEditorTimelineView(-1);
            onMainTrackChange();
        }
    }

    private boolean onTimelineDurationChange() {
        long otherTrackMaxDuration = TimelineDataUtil.getOtherTrackMaxDuration();
        MeicamVideoTrack mainTrack = TimelineDataUtil.getMainTrack();
        long mainTrackDuration = TimelineDataUtil.getMainTrackDuration();
        Log.e(TAG, "主轨长度: " + mainTrackDuration + "  " + otherTrackMaxDuration);
        if (otherTrackMaxDuration > mainTrackDuration) {
            long j = otherTrackMaxDuration - mainTrackDuration;
            MeicamVideoClip mainTrackLastClip = TimelineDataUtil.getMainTrackLastClip();
            if (mainTrackLastClip.getVideoType().equals(CommonData.CLIP_HOLDER)) {
                mainTrackLastClip.setOutPoint(mainTrackLastClip.getOutPoint() + j);
                mainTrackLastClip.setTrimOut(mainTrackLastClip.getTrimOut() + j);
                ((NvsVideoClip) mainTrackLastClip.getObject()).changeTrimOutPoint(mainTrackLastClip.getTrimOut(), false);
                Log.e(TAG, "长度修改: " + mainTrackLastClip.getTrimIn() + " " + mainTrackLastClip.getTrimOut() + " " + mainTrackLastClip.getInPoint() + "  " + mainTrackLastClip.getOutPoint());
                return true;
            }
            MeicamVideoClip meicamVideoClip = new MeicamVideoClip("assets:/black.png", CommonData.CLIP_HOLDER, 7200000000L);
            meicamVideoClip.setTrimIn(CommonData.DEFAULT_TRIM_IN);
            meicamVideoClip.setTrimOut(j + CommonData.DEFAULT_TRIM_IN);
            meicamVideoClip.appendToTimeline((NvsVideoTrack) mainTrack.getObject());
            meicamVideoClip.setInPoint(((NvsVideoClip) meicamVideoClip.getObject()).getInPoint());
            meicamVideoClip.setOutPoint(otherTrackMaxDuration);
            mainTrack.getClipInfoList().add(meicamVideoClip);
            Log.e(TAG, "过长添加: " + meicamVideoClip.getTrimIn() + " " + meicamVideoClip.getTrimOut() + " " + meicamVideoClip.getInPoint() + "  " + meicamVideoClip.getOutPoint());
            return true;
        }
        MeicamVideoClip mainTrackLastClip2 = TimelineDataUtil.getMainTrackLastClip();
        if (!mainTrackLastClip2.getVideoType().equals(CommonData.CLIP_HOLDER)) {
            return false;
        }
        long j2 = mainTrackDuration - otherTrackMaxDuration;
        if (j2 >= mainTrackLastClip2.getOutPoint()) {
            MeicamVideoTrack mainTrack2 = TimelineDataUtil.getMainTrack();
            NvsVideoTrack nvsVideoTrack = (NvsVideoTrack) mainTrack2.getObject();
            nvsVideoTrack.removeClip(nvsVideoTrack.getClipCount() - 1, false);
            mainTrack2.getClipInfoList().remove(mainTrack2.getClipInfoList().size() - 1);
            Log.e(TAG, "过段删除: " + mainTrackLastClip2.getTrimIn() + " " + mainTrackLastClip2.getTrimOut() + " " + mainTrackLastClip2.getInPoint() + "  " + mainTrackLastClip2.getOutPoint());
            return true;
        }
        mainTrackLastClip2.setOutPoint(mainTrackLastClip2.getOutPoint() - j2);
        mainTrackLastClip2.setTrimOut(mainTrackLastClip2.getTrimOut() - j2);
        ((NvsVideoClip) mainTrackLastClip2.getObject()).changeTrimOutPoint(mainTrackLastClip2.getTrimOut(), false);
        Log.e(TAG, "短度修改: " + mainTrackLastClip2.getTrimIn() + " " + mainTrackLastClip2.getTrimOut() + " " + mainTrackLastClip2.getInPoint() + "  " + mainTrackLastClip2.getOutPoint());
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void saveOperation() {
        this.mOperateManager.addOperate(new OperateData().build());
    }

    private void jumpMoreAssectList(int i) {
        Bundle bundle = new Bundle();
        if (i == 1) {
            bundle.putInt("titleResId", R.string.moreTheme);
            bundle.putInt("assetType", 1);
        } else if (i == 2) {
            bundle.putInt("titleResId", R.string.moreFilter);
            bundle.putInt("assetType", 2);
        } else if (i == 3) {
            bundle.putInt("titleResId", R.string.moreCaptionStyle);
            bundle.putInt("assetType", 3);
        } else if (i == 4) {
            bundle.putInt("titleResId", R.string.moreAnimatedSticker);
            bundle.putInt("assetType", 4);
        } else if (i == 5) {
            bundle.putInt("titleResId", R.string.moreTransition);
            bundle.putInt("assetType", 5);
        } else if (i == 16) {
            bundle.putInt("titleResId", R.string.moreCompoundCaptionStyle);
            bundle.putInt("assetType", 16);
        } else if (i == 25) {
            bundle.putInt("titleResId", R.string.moreTransition3D);
            bundle.putInt("assetType", 26);
        } else if (i != 26) {
            switch (i) {
                case 18:
                    bundle.putInt("titleResId", R.string.moreFrameEffect);
                    bundle.putInt("assetType", 18);
                    break;
                case 19:
                    bundle.putInt("titleResId", R.string.moreDreamEffect);
                    bundle.putInt("assetType", 19);
                    break;
                case 20:
                    bundle.putInt("titleResId", R.string.moreLivelyEffect);
                    bundle.putInt("assetType", 20);
                    break;
                case 21:
                    bundle.putInt("titleResId", R.string.moreShakingEffect);
                    bundle.putInt("assetType", 21);
                    break;
            }
        } else {
            bundle.putInt("titleResId", R.string.moreTransitionEffect);
            bundle.putInt("assetType", 27);
        }
        AppManager.getInstance().jumpActivityForResult(this, AssetDownloadActivity.class, bundle, 111);
    }

    @Override // com.meishe.myvideo.ui.trackview.TrackViewLayout.OnTrackViewScrollListener
    public void scrollX(int i, int i2) {
        if (!(this.mEditTimeline.getNowScrollX() == i2 || i == i2 || i == this.mEditTimeline.getNowScrollX())) {
            this.mSeekFlag = 111;
        }
        if (this.mSeekFlag == 111) {
            if (this.mTimeline != null) {
                long lengthToDuration = PixelPerMicrosecondUtil.lengthToDuration(i);
                if (lengthToDuration >= this.mTimeline.getDuration()) {
                    lengthToDuration = this.mTimeline.getDuration() - CommonData.ONE_FRAME;
                }
                this.mVideoFragment.seekTimeline(lengthToDuration, 0);
            }
            this.mEditTimeline.getMultiThumbnailSequenceView().smoothScrollTo(i, 0);
            MYMiddleOperationView mYMiddleOperationView = this.mEditOperationView;
            mYMiddleOperationView.setDurationText(FormatUtils.formatTimeStrWithUs(PixelPerMicrosecondUtil.lengthToDuration(i)) + "/" + FormatUtils.formatTimeStrWithUs(this.mTimeline.getDuration()));
            this.mVideoFragment.setDrawRectOutOfTime(this.mEditorEngine.getCurrentTimelinePosition());
        }
    }

    @Override // com.meishe.myvideo.ui.trackview.TrackViewLayout.OnTrackViewScrollListener
    public void startScroll() {
        this.mSeekFlag = 111;
        this.mEditorEngine.stop();
    }

    @Override // com.meishe.myvideo.ui.trackview.TrackViewLayout.OnTrackViewScrollListener
    public void clickOutSide() {
        String currentState = StateManager.getInstance().getCurrentState();
        Logger.d(TAG, "clickOutSide: " + currentState);
        if (!currentState.equals(Constants.STATE_RATIO)) {
            this.mVideoFragment.setVideoClipInfo(null, 0);
            this.mEditTimeline.setUnSelectSpan();
            this.mVideoFragment.setDrawRectVisible(false);
            hideBottomView();
            StateManager.getInstance().changeState(StateManager.getInstance().getCurrentState(), 3);
            if (Constants.STATE_EDIT.equals(StateManager.getInstance().getCurrentState())) {
                this.mEditTimeline.setUnSelectSpan();
                StateManager.getInstance().changeState(Constants.STATE_MAIN_MENU, 3);
            }
        }
    }

    @Override // com.meishe.myvideo.ui.trackview.TrackViewLayout.OnTrackViewScrollListener
    public void clickToMusicMenu() {
        this.mEditMenuView.performMenuClick(8);
    }

    @Override // com.meishe.myvideo.ui.trackview.TrackViewLayout.OnTrackViewScrollListener
    public void onTrackViewLongClick(BaseUIClip baseUIClip) {
        if (baseUIClip != null) {
            String type = baseUIClip.getType();
            if (!TextUtils.isEmpty(type)) {
                char c = 65535;
                switch (type.hashCode()) {
                    case -1890252483:
                        if (type.equals(CommonData.CLIP_STICKER)) {
                            c = 5;
                            break;
                        }
                        break;
                    case -1465700526:
                        if (type.equals(CommonData.CLIP_COMPOUND_CAPTION)) {
                            c = 4;
                            break;
                        }
                        break;
                    case -1211707988:
                        if (type.equals(CommonData.CLIP_HOLDER)) {
                            c = 7;
                            break;
                        }
                        break;
                    case 93166550:
                        if (type.equals(CommonData.CLIP_AUDIO)) {
                            c = 6;
                            break;
                        }
                        break;
                    case 100313435:
                        if (type.equals(CommonData.CLIP_IMAGE)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 112202875:
                        if (type.equals(CommonData.CLIP_VIDEO)) {
                            c = 0;
                            break;
                        }
                        break;
                    case 552573414:
                        if (type.equals(CommonData.CLIP_CAPTION)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 609391404:
                        if (type.equals(CommonData.CLIP_TIMELINE_FX)) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                        StateManager.getInstance().setCurrentState(Constants.STATE_PIC_IN_PIC);
                        break;
                    case 2:
                        StateManager.getInstance().setCurrentState(Constants.STATE_FX);
                        break;
                    case 3:
                        StateManager.getInstance().setCurrentState(Constants.STATE_CAPTION);
                        break;
                    case 4:
                        StateManager.getInstance().setCurrentState(Constants.STATE_COMPOUND_CAPTION);
                        break;
                    case 5:
                        StateManager.getInstance().setCurrentState(Constants.STATE_STICKER);
                        break;
                    case 6:
                        StateManager.getInstance().setCurrentState(Constants.STATE_MUSIC);
                        break;
                }
                clickOutSide();
            }
        }
    }

    @Override // com.meishe.myvideo.ui.trackview.TrackViewLayout.OnTrackViewDragListener
    public Object dragEnd(BaseUIClip baseUIClip, int i, long j) {
        Log.e(TAG, "dragEnd: beforeTrackIndex: " + baseUIClip.getTrackIndex() + " beforeInPoint: " + baseUIClip.getInPoint() + " newTrackIndex: " + i + " newInPoint: " + j);
        Object onDragEnd = ClipDragUtils.onDragEnd(baseUIClip.getType(), baseUIClip, j, i, this.mTimeline);
        this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(baseUIClip.getType()), this.mEditorEngine.getCurrentTimeline().getDuration());
        this.mEditorEngine.seekTimeline(0);
        saveOperation();
        return onDragEnd;
    }

    @Override // com.meishe.myvideo.ui.trackview.TrackViewLayout.OnTrackViewDragListener
    public void onSelectClip(BaseUIClip baseUIClip) {
        if (baseUIClip != null) {
            this.mEditTimeline.setUnSelectSpan();
            this.mSelectTrackData = baseUIClip;
            this.mEditMenuView.switchToEditorMenu(baseUIClip.getType());
            String type = baseUIClip.getType();
            char c = 65535;
            switch (type.hashCode()) {
                case -1890252483:
                    if (type.equals(CommonData.CLIP_STICKER)) {
                        c = 4;
                        break;
                    }
                    break;
                case -1465700526:
                    if (type.equals(CommonData.CLIP_COMPOUND_CAPTION)) {
                        c = 5;
                        break;
                    }
                    break;
                case 93166550:
                    if (type.equals(CommonData.CLIP_AUDIO)) {
                        c = 2;
                        break;
                    }
                    break;
                case 100313435:
                    if (type.equals(CommonData.CLIP_IMAGE)) {
                        c = 1;
                        break;
                    }
                    break;
                case 112202875:
                    if (type.equals(CommonData.CLIP_VIDEO)) {
                        c = 0;
                        break;
                    }
                    break;
                case 552573414:
                    if (type.equals(CommonData.CLIP_CAPTION)) {
                        c = 3;
                        break;
                    }
                    break;
                case 609391404:
                    if (type.equals(CommonData.CLIP_TIMELINE_FX)) {
                        c = 6;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                    this.mCurSelectVideoClip = TimelineDataUtil.findVideoClipByTrackAndInPoint(baseUIClip.getTrackIndex() + 1, baseUIClip.getInPoint());
                    Log.e(TAG, "onSelectClip: " + baseUIClip.getTrackIndex() + " " + baseUIClip.getInPoint());
                    this.mCurrentPosition = this.mCurSelectVideoClip.getInPoint();
                    this.mCurrentTrackIndex = baseUIClip.getTrackIndex() + 1;
                    TimelineData.getInstance().setSelectedMeicamClipInfo(this.mCurSelectVideoClip);
                    this.mVideoFragment.setVideoClipInfo(this.mCurSelectVideoClip, baseUIClip.getTrackIndex() + 1);
                    StateManager.getInstance().setCurrentState(Constants.STATE_PIC_IN_PIC);
                    return;
                case 2:
                    this.mCurrSelectedAudioClip = TimelineDataUtil.findAudioClipInfoByTrackAndInpoint(baseUIClip.getTrackIndex(), (long) ((int) baseUIClip.getInPoint()));
                    if (this.mCurrSelectedAudioClip != null) {
                        this.mEditorTrackView.audioScrollX(baseUIClip);
                        EditorEngine.getInstance().setBaseUIClip(this.mCurrSelectedAudioClip);
                        return;
                    }
                    Log.e(TAG, "onSelectClip: mCurrSelectedAudioClip is null");
                    return;
                case 3:
                    MeicamCaptionClip meicamCaptionClip = (MeicamCaptionClip) TimelineDataUtil.getStickerCaptionData(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
                    if (meicamCaptionClip == null) {
                        Log.e(TAG, "onSelectClip: compoundCaptionClip is null");
                    } else {
                        this.mCurrSelectedCaption = meicamCaptionClip;
                        NvsTimelineCaption nvsTimelineCaption = (NvsTimelineCaption) this.mCurrSelectedCaption.getObject();
                        if (nvsTimelineCaption != null) {
                            this.mVideoFragment.setEditMode(0);
                            this.mVideoFragment.setCurCaption(nvsTimelineCaption);
                            this.mVideoFragment.updateCaptionCoordinate(nvsTimelineCaption);
                            this.mVideoFragment.setDrawRectVisible(true);
                            this.mVideoFragment.setDrawRectOutOfTime(this.mEditorEngine.getCurrentTimelinePosition());
                        }
                    }
                    StateManager.getInstance().setCurrentState(Constants.STATE_CAPTION);
                    return;
                case 4:
                    MeicamStickerClip meicamStickerClip = (MeicamStickerClip) TimelineDataUtil.getStickerCaptionData(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
                    if (meicamStickerClip == null) {
                        Logger.e(TAG, "onSelectClip: meicamStickerClip is null");
                    } else {
                        this.mCurrSelectedSticker = meicamStickerClip;
                        NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker = (NvsTimelineAnimatedSticker) this.mCurrSelectedSticker.getObject();
                        if (nvsTimelineAnimatedSticker != null) {
                            this.mVideoFragment.setEditMode(1);
                            this.mVideoFragment.setCurAnimateSticker(nvsTimelineAnimatedSticker);
                            this.mVideoFragment.updateAnimateStickerCoordinate(nvsTimelineAnimatedSticker);
                            this.mVideoFragment.setDrawRectVisible(true);
                            this.mVideoFragment.setDrawRectOutOfTime(this.mEditorEngine.getCurrentTimelinePosition());
                        }
                    }
                    StateManager.getInstance().setCurrentState(Constants.STATE_STICKER);
                    return;
                case 5:
                    MeicamCompoundCaptionClip meicamCompoundCaptionClip = (MeicamCompoundCaptionClip) TimelineDataUtil.getStickerCaptionData(baseUIClip.getTrackIndex(), baseUIClip.getInPoint());
                    if (meicamCompoundCaptionClip == null) {
                        Log.e(TAG, "onSelectClip: compoundCaptionClip is null");
                    } else {
                        this.mCurrCompoundCaption = meicamCompoundCaptionClip;
                        NvsTimelineCompoundCaption nvsTimelineCompoundCaption = (NvsTimelineCompoundCaption) this.mCurrCompoundCaption.getObject();
                        if (nvsTimelineCompoundCaption != null) {
                            this.mVideoFragment.setEditMode(4);
                            this.mVideoFragment.setCurCompoundCaption(nvsTimelineCompoundCaption);
                            this.mVideoFragment.updateCompoundCaptionCoordinate(nvsTimelineCompoundCaption);
                            this.mVideoFragment.setDrawRectVisible(true);
                            this.mVideoFragment.setDrawRectOutOfTime(this.mEditorEngine.getCurrentTimelinePosition());
                            this.mEditorEngine.seekTimeline(2);
                        }
                    }
                    StateManager.getInstance().setCurrentState(Constants.STATE_COMPOUND_CAPTION);
                    return;
                case 6:
                    StateManager.getInstance().setCurrentState(Constants.STATE_FX);
                    return;
                default:
                    return;
            }
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTimelineChangeListener
    public void onTimelineChanged(NvsTimeline nvsTimeline, boolean z) {
        if (z) {
            saveOperation();
        }
        String currentState = StateManager.getInstance().getCurrentState();
        if (Constants.STATE_FX.equals(currentState)) {
            this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_TIMELINE_FX), this.mEditorEngine.getCurrentTimeline().getDuration());
        } else if (Constants.STATE_CAPTION.equals(currentState)) {
            this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_CAPTION), this.mEditorEngine.getCurrentTimeline().getDuration());
        } else if (Constants.STATE_STICKER.equals(currentState)) {
            this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_STICKER), this.mEditorEngine.getCurrentTimeline().getDuration());
        } else if (Constants.STATE_COMPOUND_CAPTION.equals(currentState)) {
            this.mEditorTrackView.setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_COMPOUND_CAPTION), this.mEditorEngine.getCurrentTimeline().getDuration());
        } else if (Constants.STATE_PIC_IN_PIC.equals(currentState)) {
            this.mEditorTrackView.showPipTrackView(this.mTimeline);
            int trackIndex = this.mSelectTrackData.getTrackIndex();
            long inPoint = this.mSelectTrackData.getInPoint();
            this.mEditorTrackView.setSelect(trackIndex, (long) ((int) inPoint));
            int i = trackIndex + 1;
            this.mCurSelectVideoClip = (MeicamVideoClip) TimelineDataUtil.getVideoClip(i, inPoint);
            this.mCurrentPosition = inPoint;
            this.mCurrentTrackIndex = i;
            this.mVideoFragment.setVideoClipInfo(this.mCurSelectVideoClip, this.mSelectTrackData.getTrackIndex() + 1);
        } else if (Constants.STATE_MUSIC.equals(currentState) || Constants.STATE_DUBBING.equals(currentState)) {
            refreshAudioView();
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTimelineChangeListener
    public void onNeedTrackSelectChanged(int i, long j) {
        MYEditorTimelineTrackView mYEditorTimelineTrackView = this.mEditorTrackView;
        if (mYEditorTimelineTrackView != null) {
            mYEditorTimelineTrackView.setSelect(i, j);
        }
    }

    @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTimelineChangeListener
    public void refreshEditorTimelineView(int i) {
        initEditorTimeline();
        if (i == 1) {
            this.mEditorTrackView.updateTimelineDuration(this.mTimeline.getDuration());
            this.mEditTimeline.setUnSelectSpan();
        } else if (i == 3) {
            String currentState = StateManager.getInstance().getCurrentState();
            if (Constants.STATE_EDIT.equals(currentState) || Constants.STATE_EDTI_CUT.equals(currentState)) {
                this.mEditTimeline.setSelectSpan(this.mEditorEngine.getCurrentTimelinePosition());
                this.mCurSelectVideoClip = (MeicamVideoClip) TimelineDataUtil.getVideoClip(0, this.mEditTimeline.getCurrentClip().getInPoint());
                this.mCurrentInPoint = this.mCurSelectVideoClip.getInPoint();
                this.mCurrentTrackIndex = 0;
            } else if (Constants.STATE_PIC_IN_PIC.equals(currentState)) {
                int trackIndex = this.mSelectTrackData.getTrackIndex();
                long inPoint = this.mSelectTrackData.getInPoint();
                this.mEditorTrackView.setSelect(trackIndex, (long) ((int) inPoint));
                int i2 = trackIndex + 1;
                this.mCurSelectVideoClip = (MeicamVideoClip) TimelineDataUtil.getVideoClip(i2, inPoint);
                this.mCurrentInPoint = inPoint;
                this.mCurrentTrackIndex = i2;
            }
        } else if (i == 4) {
            String currentState2 = StateManager.getInstance().getCurrentState();
            if (Constants.STATE_EDIT.equals(currentState2)) {
                this.mEditTimeline.setSelectSpan(this.mEditorEngine.getCurrentTimelinePosition());
                this.mCurSelectVideoClip = (MeicamVideoClip) TimelineDataUtil.getVideoClip(0, this.mEditTimeline.getCurrentClip().getInPoint());
                this.mCurrentPosition = this.mCurSelectVideoClip.getInPoint();
                this.mCurrentTrackIndex = 0;
                TimelineData.getInstance().setSelectedMeicamClipInfo(this.mCurSelectVideoClip);
                StateManager.getInstance().changeState(Constants.STATE_EDIT, 3);
            } else if (Constants.STATE_PIC_IN_PIC.equals(currentState2)) {
                int trackIndex2 = this.mSelectTrackData.getTrackIndex();
                long inPoint2 = this.mSelectTrackData.getInPoint();
                this.mEditorTrackView.setSelect(trackIndex2, (long) ((int) inPoint2));
                int i3 = trackIndex2 + 1;
                this.mCurSelectVideoClip = (MeicamVideoClip) TimelineDataUtil.getVideoClip(i3, inPoint2);
                this.mCurrentTrackIndex = i3;
            }
        } else if (i == 5) {
            if (Constants.STATE_PIC_IN_PIC.equals(StateManager.getInstance().getCurrentState()) && this.mCurSelectVideoClip != null) {
                this.mEditorTrackView.showPipTrackView(this.mTimeline);
                this.mEditorTrackView.setSelect(this.mSelectTrackData.getTrackIndex(), (long) ((int) this.mSelectTrackData.getInPoint()));
            }
        } else if (i == 2) {
            this.mEditorTrackView.updateTimelineDuration(this.mTimeline.getDuration());
        }
        PixelPerMicrosecondUtil.setScale(PixelPerMicrosecondUtil.getScale());
    }

    private void initEditorTimeline() {
        initMultiSequence();
        initEditorTimeLineView();
        this.mEditorTrackView.initWidth(this.mTimeline.getDuration());
    }

    @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTimelineChangeListener
    public void onChangeBottomView() {
        StateManager.getInstance().changeState(StateManager.getInstance().getCurrentState(), 3);
    }

    @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTimelineChangeListener
    public void onTimelineChangeShowToast(int i) {
        ToastUtil.showToast(this.mContext, getResources().getString(i));
    }

    @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTimelineChangeListener
    public void onSaveOperation() {
        saveOperation();
    }

    @Override // com.meishe.myvideo.util.engine.EditorEngine.OnTimelineChangeListener
    public void onAddStickerCaption(ClipInfo clipInfo, int i) {
        if (i != 1) {
            if (i == 2 && (clipInfo instanceof MeicamCompoundCaptionClip)) {
                this.mCurrCompoundCaption = (MeicamCompoundCaptionClip) clipInfo;
            }
        } else if (clipInfo instanceof MeicamCaptionClip) {
            this.mCurrSelectedCaption = (MeicamCaptionClip) clipInfo;
        }
    }

    @Override // com.meishe.myvideo.ui.trackview.HandView.OnHandChangeListener
    public void leftHandChange(int i, long j, BaseUIClip baseUIClip) {
        this.mEditorTrackView.refreshSelectView(baseUIClip, true);
    }

    @Override // com.meishe.myvideo.ui.trackview.HandView.OnHandChangeListener
    public void rightHandChange(int i, long j, BaseUIClip baseUIClip) {
        this.mEditorTrackView.refreshSelectView(baseUIClip, false);
    }

    @Override // com.meishe.myvideo.ui.trackview.HandView.OnHandChangeListener
    public void handUp(BaseUIClip baseUIClip) {
        this.mEditorEngine.refreshData(baseUIClip);
        if (getResources().getString(R.string.main_menu_name_fx).equals(StateManager.getInstance().getCurrentState())) {
            this.mEditorEngine.cloneFxTrack();
        }
        onTimelineDurationChange(true);
        this.mEditorEngine.seekTimeline(0);
        saveOperation();
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

    @Override // com.meishe.myvideo.util.PixelPerMicrosecondUtil.PixelPerMicrosecondChangeListener
    public void onPixelPerMicrosecondChange(double d, float f) {
        this.mEditorEngine.seekTimeline(0);
        seekViewOnPlay(this.mEditorEngine.getCurrentTimelinePosition());
    }

    @Override // com.meishe.myvideo.fragment.TransitionFragment.TransitionRefreshListener
    public void refreshTransition(int i) {
        BaseInfo baseInfo = new BaseInfo();
        EditorTimelineTransitionAdapter.TransitionData transitionData = this.mEditTimeline.getTransitionDatas().get(i);
        baseInfo.mEffectType = transitionData.getTransitionType();
        baseInfo.mName = transitionData.getTransitionName();
        baseInfo.mIconRcsId = transitionData.getIconRcsId();
        baseInfo.mIconUrl = transitionData.getIconPath();
        this.mEditTimeline.refreshTimelineTransition(baseInfo);
    }
}
