package com.meishe.myvideo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.TimelineDataUtil;
import com.meishe.myvideo.adapter.EditorTimelineMarkingLineAdapter;
import com.meishe.myvideo.adapter.EditorTimelineRectCoverAdapter;
import com.meishe.myvideo.adapter.EditorTimelineTransitionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.ui.bean.BaseUIVideoClip;
import com.meishe.myvideo.ui.trackview.MultiThumbnailSequenceView;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideo.view.MYEditorTimelineSpanView;
import com.meishe.myvideoapp.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MYEditorTimeLine extends RelativeLayout implements PixelPerMicrosecondUtil.PixelPerMicrosecondChangeListener {
    private static final int CHECK_COUNT = 20;
    private static final int ERROR_CORRECTION = 1;
    private static final int HAND_TOTAL = 2;
    private static final long LIMIT_FRAME_ERROR = 10;
    private static final int LIMIT_INTERCEPT_MOVE = 10;
    private static final long MIN_DURATION = 200000;
    public static final int MODE_COVER = 2;
    public static final int MODE_SPAN = 1;
    private static final int SEEK_OUT_POINT_ERROR = 10;
    private static final String TAG = "MYEditorTimeLine";
    private static final long TIME_DELAY = 40;
    private static final int TOUCH_ID = -1000;
    private int MIDDLE_LIMIT;
    private ImageView mAddClipImg;
    private int mAddParentLengthOnLeftHandDown;
    private Long[] mAvFileInfos;
    private Button mBtnCloseOriginalVoice;
    private List<Pair<Integer, Integer>> mClipPositionList = new ArrayList();
    private Context mContext;
    private MYTimelineEditorRecyclerView mCoverRecyclerView;
    private int mDownLeftSelectClipViewLeft;
    private long mDownTimeNow;
    private boolean mDragEnd;
    private RelativeLayout mEditorMainTrackParent;
    private EditorTimelineRectCoverAdapter mEditorTimelineCoverAdapter;
    private EditorTimelineMarkingLineAdapter mEditorTimelineMarkingLineAdapter;
    private EditorTimelineTransitionAdapter mEditorTimelineTransitionAdapter;
    private int mHandWidth;
    private boolean mIsAligningMiddle = false;
    private boolean mIsScrollPressed;
    private boolean mIsTrim;
    private LinearLayout mLLCloseVoice;
    private LinearLayout mLinearContainer;
    private MYLineView mMYLineView;
    private MYTimelineEditorRecyclerView mMarkingLineRecyclerView;
    private int mMode = 1;
    private int mNewDx = 0;
    private MultiThumbnailSequenceView mNvsMultiThumbnailSequenceView;
    private int mOldDx = 0;
    private int mOldScrollX;
    private OnHandAction mOnHandAction;
    private float mOnInterceptDownX;
    private OnSelectItemListener mOnSelectItemListener;
    private OnTimeLineEditorListener mOnTimeLineEditorListener;
    private float mOnTouchDownX = 0.0f;
    private double mPixelPerMicrosecond = 0.0d;
    private boolean mReceiveMoveEvent;
    private RelativeLayout mRlCloseVoiceRoot;
    private OnScrollListener mScrollListener;
    private boolean mScrollToClickClip;
    private int mSelectClipIndex = -1;
    private int mSequenceLeftPadding = 0;
    private int mSequenceRightPadding = 0;
    private ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> mThumbnailSequenceDescArrayListOnHandDown;
    private NvsTimeline mTimeline;
    private long mTimelineDuration = 0;
    private RelativeLayout mTimelineSpanRelativeLayout;
    private MYEditorTimelineSpanView mTimelineSpanView;
    private int mTotalMoveXOnIntercept;
    private int mTotalMoveXOnTouch = 0;
    private int mTransIconWidth;
    private MYTimelineEditorRecyclerView mTransRecyclerView;
    private TextView mTvCloseVoiceDesc;

    public interface OnHandAction {
        void getAvFileDuration(int i, boolean z);

        void onChangeTimeline(int i, boolean z, long j, long j2);

        void onHandDragToSeek(long j);

        void onTimeLineClickOutSide();
    }

    public interface OnScrollListener {
        void onScrollChanged(long j, int i, int i2);

        void onScrollStopped();

        void onSeekingTimeline(boolean z);

        void onSelectClipIndexChange(int i);
    }

    public interface OnSelectItemListener {
        void onSelectPip(MeicamVideoClip meicamVideoClip, int i);

        void onSelectTransition(int i, EditorTimelineTransitionAdapter.TransitionData transitionData);
    }

    public interface OnTimeLineEditorListener {
        void addMaterialSource();

        void closeOriginalVoice(View view);

        void openOriginalVoice(View view);
    }

    private void removeAllLayout() {
    }

    public void changeMode(int i) {
        this.mMode = i;
        if (this.mMode == 2) {
            this.mTransRecyclerView.setVisibility(8);
            this.mCoverRecyclerView.setVisibility(0);
            refreshEditorTimelineCoverAdapter();
            setCoverAdapterShowWhiteCover(true);
            this.mLinearContainer.setVisibility(8);
            return;
        }
        this.mTransRecyclerView.setVisibility(0);
        this.mCoverRecyclerView.setVisibility(0);
        setCoverAdapterShowWhiteCover(false);
        this.mLinearContainer.setVisibility(0);
    }

    private void setCoverAdapterShowWhiteCover(boolean z) {
        EditorTimelineRectCoverAdapter editorTimelineRectCoverAdapter = this.mEditorTimelineCoverAdapter;
        if (editorTimelineRectCoverAdapter != null) {
            editorTimelineRectCoverAdapter.setShowWhiteCover(z);
        }
    }

    public MYEditorTimeLine(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MYEditorTimeLine(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public MYEditorTimeLine(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        this.mContext = context;
        this.MIDDLE_LIMIT = ScreenUtils.getScreenWidth(this.mContext);
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.timeline_editor_view_layout, this);
        this.mNvsMultiThumbnailSequenceView = (MultiThumbnailSequenceView) inflate.findViewById(R.id.editor_multi_thumbnail_sequence_view);
        this.mCoverRecyclerView = (MYTimelineEditorRecyclerView) inflate.findViewById(R.id.editor_timeline_view_cover_recycler);
        this.mTransRecyclerView = (MYTimelineEditorRecyclerView) inflate.findViewById(R.id.editor_timeline_view_trans_recycler);
        this.mMarkingLineRecyclerView = (MYTimelineEditorRecyclerView) inflate.findViewById(R.id.editor_timeline_view_time_making_line_recycler);
        this.mRlCloseVoiceRoot = (RelativeLayout) inflate.findViewById(R.id.rl_close_voice_root);
        this.mLLCloseVoice = (LinearLayout) inflate.findViewById(R.id.ll_close_voice);
        this.mTvCloseVoiceDesc = (TextView) inflate.findViewById(R.id.tv_close_voice_desc);
        this.mBtnCloseOriginalVoice = (Button) inflate.findViewById(R.id.btn_close_original_voice);
        this.mAddClipImg = (ImageView) inflate.findViewById(R.id.editor_add_clip_img);
        this.mTimelineSpanRelativeLayout = (RelativeLayout) inflate.findViewById(R.id.editor_timeline_view_container_relative);
        this.mLinearContainer = (LinearLayout) inflate.findViewById(R.id.editor_timeline_view_linear_layout);
        this.mMYLineView = (MYLineView) inflate.findViewById(R.id.editor_line_view);
        this.mEditorMainTrackParent = (RelativeLayout) inflate.findViewById(R.id.editor_main_track_parent);
        this.mHandWidth = getResources().getDimensionPixelSize(R.dimen.editor_timeline_view_hand_width);
        this.mTransIconWidth = getResources().getDimensionPixelSize(R.dimen.editor_timeline_view_hand_width);
        this.mPixelPerMicrosecond = PixelPerMicrosecondUtil.getPixelPerMicrosecond(this.mContext);
        PixelPerMicrosecondUtil.addPixelPerMicrosecondChangeListener(this);
    }

    private void initListener() {
        setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass1 */

            public void onClick(View view) {
                Logger.d(MYEditorTimeLine.TAG, "onClick: outOfSequenceView");
                MYEditorTimeLine.this.outOfSequenceView();
            }
        });
        initEditorTimelineTransitionAdapterListener();
        this.mLLCloseVoice.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass2 */

            public void onClick(View view) {
                MYEditorTimeLine.this.initCloseVoiceView(view);
            }
        });
        this.mBtnCloseOriginalVoice.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass3 */

            public void onClick(View view) {
                MYEditorTimeLine.this.initCloseVoiceView(view);
            }
        });
    }

    private void initEditorTimelineTransitionAdapterListener() {
        EditorTimelineTransitionAdapter editorTimelineTransitionAdapter = this.mEditorTimelineTransitionAdapter;
        if (editorTimelineTransitionAdapter != null) {
            editorTimelineTransitionAdapter.setOnClickTransitionListener(new EditorTimelineTransitionAdapter.OnClickTransitionListener() {
                /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass4 */

                @Override
                // com.meishe.myvideo.adapter.EditorTimelineTransitionAdapter.OnClickTransitionListener
                public void onClickTransition(int i, EditorTimelineTransitionAdapter.TransitionData transitionData) {
                    if (MYEditorTimeLine.this.mOnSelectItemListener != null) {
                        MYEditorTimeLine.this.mOnSelectItemListener.onSelectTransition(i, transitionData);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initCloseVoiceView(View view) {
        Button button = this.mBtnCloseOriginalVoice;
        button.setSelected(!button.isSelected());
        if (this.mBtnCloseOriginalVoice.isSelected()) {
            this.mTvCloseVoiceDesc.setText(R.string.open_original_voice);
            OnTimeLineEditorListener onTimeLineEditorListener = this.mOnTimeLineEditorListener;
            if (onTimeLineEditorListener != null) {
                onTimeLineEditorListener.closeOriginalVoice(view);
                return;
            }
            return;
        }
        this.mTvCloseVoiceDesc.setText(R.string.close_original_voice);
        this.mOnTimeLineEditorListener.openOriginalVoice(view);
    }

    public void initEditorTimeLine() {
        refreshClipPositions();
        initCloseVoice();
        initAddMaterialView();
        initTransView();
        initTimeMarkingLineView();
        initListener();
        refreshEditorTimelineCoverAdapter();
        MYEditorTimelineSpanView mYEditorTimelineSpanView = this.mTimelineSpanView;
        if (mYEditorTimelineSpanView != null) {
            addEditorPreviewTimeSpan(((NvsVideoClip) mYEditorTimelineSpanView.getBaseUIClip().getNvsObject()).getIndex());
        }
    }

    private void initCloseVoice() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mLLCloseVoice.getLayoutParams();
        layoutParams.leftMargin = (ScreenUtils.getScreenWidth(this.mContext) / 2) - ScreenUtils.dp2px(this.mContext, 50.0f);
        this.mLLCloseVoice.setLayoutParams(layoutParams);
        NvsVideoTrack videoTrackByIndex = this.mTimeline.getVideoTrackByIndex(0);
        if (videoTrackByIndex == null) {
            return;
        }
        if (videoTrackByIndex.getVolumeGain().leftVolume > 0.0f) {
            this.mBtnCloseOriginalVoice.setSelected(false);
            this.mTvCloseVoiceDesc.setText(R.string.close_original_voice);
            return;
        }
        this.mBtnCloseOriginalVoice.setSelected(true);
        this.mTvCloseVoiceDesc.setText(R.string.open_original_voice);
    }

    private void initAddMaterialView() {
        this.mAddClipImg.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass5 */

            public void onClick(View view) {
                if (MYEditorTimeLine.this.mOnTimeLineEditorListener != null) {
                    MYEditorTimeLine.this.mOnTimeLineEditorListener.addMaterialSource();
                }
            }
        });
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void initTimelineEditor(ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList, long j) {
        if (arrayList.size() != 0) {
            this.mTimelineDuration = j;
            removeAllLayout();
            NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
            if (currentVideoTrack != null) {
                this.mAvFileInfos = new Long[currentVideoTrack.getClipCount()];
                this.mNvsMultiThumbnailSequenceView.setThumbnailSequenceDescArray(arrayList);
                this.mNvsMultiThumbnailSequenceView.setPixelPerMicrosecond(this.mPixelPerMicrosecond);
                this.mNvsMultiThumbnailSequenceView.setStartPadding(this.mSequenceLeftPadding);
                this.mNvsMultiThumbnailSequenceView.setEndPadding(this.mSequenceRightPadding);
                this.mNvsMultiThumbnailSequenceView.setClickable(true);
                this.mNvsMultiThumbnailSequenceView.setOnScrollChangeListenser(new MultiThumbnailSequenceView.OnScrollChangeListener() {
                    /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass6 */

                    @Override
                    // com.meishe.myvideo.ui.trackview.MultiThumbnailSequenceView.OnScrollChangeListener
                    public void onScrollChanged(MultiThumbnailSequenceView multiThumbnailSequenceView, int i, int i2) {
                        if (i2 != MYEditorTimeLine.this.mOldDx || i != MYEditorTimeLine.this.mNewDx) {
                            MYEditorTimeLine.this.mOldDx = i2;
                            MYEditorTimeLine.this.mNewDx = i;
                            if (MYEditorTimeLine.this.mOldScrollX >= 0 && MYEditorTimeLine.this.mOldScrollX != i) {
                                int i3 = i - i2;
                                MYEditorTimeLine.this.mTransRecyclerView.scrollBy(i3, 0);
                                MYEditorTimeLine.this.mMarkingLineRecyclerView.scrollBy(i3, 0);
                                if (!MYEditorTimeLine.this.mIsAligningMiddle) {
                                    MYEditorTimeLine.this.mLinearContainer.scrollTo(i, 0);
                                }
                                MYEditorTimeLine.this.mMYLineView.scrollTo(i, 0);
                                MYEditorTimeLine.this.mCoverRecyclerView.scrollBy(i3, 0);
                                MYEditorTimeLine.this.mRlCloseVoiceRoot.scrollTo(i, 0);
                            }
                            MYEditorTimeLine.this.mOldScrollX = i;
                            double d = (double) i;
                            double d2 = MYEditorTimeLine.this.mPixelPerMicrosecond;
                            Double.isNaN(d);
                            long floor = (long) Math.floor((d / d2) + 0.5d);
                            if (MYEditorTimeLine.this.mScrollListener != null) {
                                MYEditorTimeLine.this.mScrollListener.onScrollChanged(floor, i, i2);
                            }
                        }
                    }
                });
                final int[] iArr = new int[1];
                final int[] iArr2 = new int[1];
                this.mNvsMultiThumbnailSequenceView.setOnTouchListener(new View.OnTouchListener() {
                    /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass7 */
                    Handler handler = new Handler() {
                        /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass7.AnonymousClass1 */

                        public void handleMessage(Message message) {
                            super.handleMessage(message);
                            View view = (View) message.obj;
                            if (message.what != -1000) {
                                return;
                            }
                            if (MYEditorTimeLine.this.isFinishScroll()) {
                                MYEditorTimeLine.this.handleStop();
                                handler.removeMessages(-1000);
                                return;
                            }
                            handler.sendMessageDelayed(handler.obtainMessage(-1000, view), MYEditorTimeLine.TIME_DELAY);
                        }
                    };

                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        int action = motionEvent.getAction();
                        if (action == 0) {
                            MYEditorTimeLine.this.mNvsMultiThumbnailSequenceView.setScrollEnabled(true);
                            MYEditorTimeLine.this.mIsScrollPressed = true;
                            iArr[0] = x;
                            iArr2[0] = y;
                            this.handler.removeMessages(-1000);
                            if (MYEditorTimeLine.this.mScrollListener != null) {
                                MYEditorTimeLine.this.mScrollListener.onSeekingTimeline(true);
                            }
                        } else if (action == 1) {
                            Handler handler2 = this.handler;
                            handler2.sendMessageDelayed(handler2.obtainMessage(-1000, view), 0);
                        } else if (action == 2) {
                            MYEditorTimeLine.this.mIsScrollPressed = true;
                            this.handler.removeMessages(-1000);
                        }
                        return false;
                    }
                });
                initTimelineSpanParentLayout();
                initTimelineCover();
            }
        }
    }

    private void hideTransitionAndShowTrim() {
        RelativeLayout relativeLayout = this.mTimelineSpanRelativeLayout;
        if (relativeLayout != null) {
            relativeLayout.removeAllViews();
            this.mTimelineSpanView = null;
        }
        this.mTransRecyclerView.setVisibility(8);
        this.mCoverRecyclerView.setVisibility(8);
        addEditorPreviewTimeSpan(this.mSelectClipIndex);
    }

    private void hideTransitionAndShowCover() {
        this.mTransRecyclerView.setVisibility(8);
        this.mCoverRecyclerView.setVisibility(0);
        this.mEditorTimelineCoverAdapter.setShowWhiteCover(true);
        this.mEditorTimelineCoverAdapter.refresh(this.mSelectClipIndex);
        RelativeLayout relativeLayout = this.mTimelineSpanRelativeLayout;
        if (relativeLayout != null) {
            relativeLayout.removeAllViews();
            this.mTimelineSpanView = null;
        }
    }

    public void outOfSequenceView() {
        OnHandAction onHandAction = this.mOnHandAction;
        if (onHandAction != null) {
            onHandAction.onTimeLineClickOutSide();
        }
        showTransitionAndHideTrimView();
    }

    private void showTransitionAndHideTrimView() {
        setUnSelectSpan();
        this.mTransRecyclerView.setVisibility(0);
        this.mCoverRecyclerView.setVisibility(0);
        this.mEditorTimelineCoverAdapter.setShowWhiteCover(false);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void handleStop() {
        this.mIsScrollPressed = false;
        OnScrollListener onScrollListener = this.mScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScrollStopped();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean isFinishScroll() {
        try {
            Field declaredField = MultiThumbnailSequenceView.class.getSuperclass().getDeclaredField("mScroller");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(this.mNvsMultiThumbnailSequenceView);
            Method method = declaredField.getType().getMethod("isFinished", new Class[0]);
            method.setAccessible(true);
            return ((Boolean) method.invoke(obj, new Object[0])).booleanValue();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return false;
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
            return false;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return false;
        }
    }

    private void initTimelineSpanParentLayout() {
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null) {
            int i = this.mSequenceLeftPadding - this.mHandWidth;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSpanParentLayoutWidth(nvsTimeline.getDuration()), -1);
            layoutParams.leftMargin = i;
            this.mTimelineSpanRelativeLayout.setLayoutParams(layoutParams);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void restoreViewMarginAndPadding() {
        int i = this.mSequenceLeftPadding;
        ((LinearLayout.LayoutParams) this.mTimelineSpanRelativeLayout.getLayoutParams()).leftMargin = i - this.mHandWidth;
        this.mNvsMultiThumbnailSequenceView.setStartPadding(i);
        this.mNvsMultiThumbnailSequenceView.setEndPadding(this.mSequenceLeftPadding);
    }

    private long getNowTime() {
        return lengthToTimelinePosition(this.mNvsMultiThumbnailSequenceView.getScrollX());
    }

    public int getNowScrollX() {
        return this.mNvsMultiThumbnailSequenceView.getScrollX();
    }

    public long getCurrentPosition() {
        return getNowTime();
    }

    private long lengthToTimelinePosition(int i) {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (this.mTimeline == null || currentVideoTrack == null) {
            return 0;
        }
        int i2 = 0;
        long j = 0;
        int i3 = 0;
        for (int clipCount = currentVideoTrack.getClipCount(); i2 < clipCount; clipCount = clipCount) {
            NvsVideoClip clipByIndex = currentVideoTrack.getClipByIndex(i2);
            long trimOut = clipByIndex.getTrimOut() - clipByIndex.getTrimIn();
            long durationToShownLength = (long) durationToShownLength(trimOut);
            long j2 = ((long) i3) + durationToShownLength;
            if (j2 > ((long) i)) {
                return j + ((((long) (i - i3)) * trimOut) / durationToShownLength);
            }
            i3 = (int) j2;
            j += trimOut;
            i2++;
            currentVideoTrack = currentVideoTrack;
        }
        return j;
    }

    private int getSpanParentLayoutWidth(long j) {
        return durationToLength(j) + (this.mHandWidth * 2);
    }

    private int timelinePositionToLength(long j) {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null) {
            Logger.e(TAG, "videoTrack is null");
            return 0;
        } else if (j <= 0) {
            return 0;
        } else {
            int clipCount = currentVideoTrack.getClipCount();
            int i = 0;
            for (int i2 = 0; i2 < clipCount; i2++) {
                NvsVideoClip clipByIndex = currentVideoTrack.getClipByIndex(i2);
                long trimOut = clipByIndex == null ? 0 : clipByIndex.getTrimOut() - clipByIndex.getTrimIn();
                long durationToShownLength = (long) durationToShownLength(trimOut);
                if (j <= trimOut) {
                    return (int) (((long) i) + ((durationToShownLength * j) / trimOut));
                }
                j -= trimOut;
                i = (int) (((long) i) + durationToShownLength);
            }
            return i;
        }
    }

    private void initTransView() {
        if (((EditorTimelineTransitionAdapter) this.mTransRecyclerView.getAdapter()) == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mContext);
            linearLayoutManager.setOrientation(0);
            this.mTransRecyclerView.setLayoutManager(linearLayoutManager);
            this.mTransRecyclerView.setItemAnimator(null);
            refreshTimelineTransitionAdapter();
            return;
        }
        refreshTimelineTransitionAdapter();
        this.mTransRecyclerView.scrollBy(this.mNvsMultiThumbnailSequenceView.getScrollX(), 0);
    }

    public void initTimeMarkingLineView() {
        if (((EditorTimelineMarkingLineAdapter) this.mMarkingLineRecyclerView.getAdapter()) == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mContext);
            linearLayoutManager.setOrientation(0);
            this.mMarkingLineRecyclerView.setLayoutManager(linearLayoutManager);
            this.mMarkingLineRecyclerView.setItemAnimator(null);
            refreshTimelineMarkingLineAdapter();
            return;
        }
        refreshTimelineMarkingLineAdapter();
        this.mMarkingLineRecyclerView.scrollBy(this.mNvsMultiThumbnailSequenceView.getScrollX(), 0);
    }

    private void addEditorPreviewTimeSpan(int i) {
        Logger.d(TAG, "addEditorPreviewTimeSpan: 添加view");
        MYEditorTimelineSpanView addEditorTimelineSpanView = addEditorTimelineSpanView(i);
        if (addEditorTimelineSpanView != null) {
            deleteSpanViews();
            this.mTimelineSpanRelativeLayout.addView(addEditorTimelineSpanView);
            this.mTimelineSpanView = addEditorTimelineSpanView;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        MeicamVideoClip clickPip;
        OnSelectItemListener onSelectItemListener;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mTotalMoveXOnTouch = 0;
            this.mOnTouchDownX = motionEvent.getX();
        } else if (action != 1) {
            if (action == 2) {
                this.mTotalMoveXOnTouch = (int) (((float) this.mTotalMoveXOnTouch) + Math.abs(motionEvent.getX() - this.mOnTouchDownX));
            }
        } else if (this.mTotalMoveXOnTouch > 10) {
            Logger.e(TAG, "onTouchEvent: 移动返回");
            return false;
        } else if (!(!this.mMYLineView.hasPipBitmapList() || (clickPip = this.mMYLineView.clickPip(((int) motionEvent.getX()) + this.mNvsMultiThumbnailSequenceView.getScrollX(), ((int) motionEvent.getY()) - this.mContext.getResources().getDimensionPixelOffset(R.dimen.my_editor_timeline_residue_top))) == null || (onSelectItemListener = this.mOnSelectItemListener) == null)) {
            onSelectItemListener.onSelectPip(clickPip, TimelineDataUtil.getPipClipTrackIndexByInPoint(clickPip.getInPoint()));
        }
        return super.onTouchEvent(motionEvent);
    }

    private BaseUIVideoClip initBaseUIVideoClip(NvsVideoClip nvsVideoClip) {
        BaseUIVideoClip baseUIVideoClip = new BaseUIVideoClip();
        baseUIVideoClip.setNvsObject(nvsVideoClip);
        baseUIVideoClip.setSpeed(nvsVideoClip.getSpeed());
        baseUIVideoClip.setInPoint(nvsVideoClip.getInPoint());
        baseUIVideoClip.setTrimIn(nvsVideoClip.getTrimIn());
        baseUIVideoClip.setTrimOut(nvsVideoClip.getTrimOut());
        baseUIVideoClip.setTrackIndex(0);
        baseUIVideoClip.setClipIndexInTrack(nvsVideoClip.getIndex());
        baseUIVideoClip.setType(nvsVideoClip.getVideoType() == 0 ? CommonData.CLIP_VIDEO : CommonData.CLIP_IMAGE);
        return baseUIVideoClip;
    }

    private MYEditorTimelineSpanView addEditorTimelineSpanView(int i) {
        NvsVideoClip clipByIndex;
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null || (clipByIndex = currentVideoTrack.getClipByIndex(i)) == null) {
            return null;
        }
        MYEditorTimelineSpanView mYEditorTimelineSpanView = new MYEditorTimelineSpanView(this.mContext, initBaseUIVideoClip(clipByIndex));
        mYEditorTimelineSpanView.setHandWidth(this.mHandWidth);
        Pair<Integer, Integer> clipPosition = getClipPosition(i);
        if (clipPosition == null) {
            Logger.e(TAG, "clip is null index:" + i);
            return mYEditorTimelineSpanView;
        }
        mYEditorTimelineSpanView.setClipPosition(((Integer) clipPosition.first).intValue(), ((Integer) clipPosition.second).intValue());
        mYEditorTimelineSpanView.setOnHandChangeListener(new MYEditorTimelineSpanView.OnHandChangeListener() {
            /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass8 */

            @Override // com.meishe.myvideo.view.MYEditorTimelineSpanView.OnHandChangeListener
            public void onLeftHandDown(int i) {
                MYEditorTimeLine.this.mIsTrim = true;
                MYEditorTimeLine.this.mAddParentLengthOnLeftHandDown = i;
                MYEditorTimeLine mYEditorTimeLine = MYEditorTimeLine.this;
                mYEditorTimeLine.mDownLeftSelectClipViewLeft = mYEditorTimeLine.getTimelineSpan().getLeft();
                MYEditorTimeLine mYEditorTimeLine2 = MYEditorTimeLine.this;
                mYEditorTimeLine2.mThumbnailSequenceDescArrayListOnHandDown = mYEditorTimeLine2.mNvsMultiThumbnailSequenceView.getThumbnailSequenceDescArray();
                NvsVideoClip currentClip = MYEditorTimeLine.this.getCurrentClip();
                if (currentClip != null) {
                    if (MYEditorTimeLine.this.mAvFileInfos[MYEditorTimeLine.this.mSelectClipIndex] != null) {
                        MYEditorTimeLine mYEditorTimeLine3 = MYEditorTimeLine.this;
                        mYEditorTimeLine3.setClipFileDuration(mYEditorTimeLine3.mAvFileInfos[MYEditorTimeLine.this.mSelectClipIndex], true);
                        MYEditorTimeLine mYEditorTimeLine4 = MYEditorTimeLine.this;
                        mYEditorTimeLine4.setMaxLeftValue(mYEditorTimeLine4.getTimelineSpan(), currentClip, (MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex));
                    } else if (MYEditorTimeLine.this.mOnHandAction != null) {
                        MYEditorTimeLine.this.mOnHandAction.getAvFileDuration(MYEditorTimeLine.this.mSelectClipIndex, true);
                    }
                }
            }

            @Override // com.meishe.myvideo.view.MYEditorTimelineSpanView.OnHandChangeListener
            public void onLeftHandUp() {
                if (MYEditorTimeLine.this.mAddParentLengthOnLeftHandDown > 0) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) MYEditorTimeLine.this.mTimelineSpanRelativeLayout.getLayoutParams();
                    layoutParams.width -= MYEditorTimeLine.this.mAddParentLengthOnLeftHandDown;
                    MYEditorTimeLine.this.mTimelineSpanRelativeLayout.setLayoutParams(layoutParams);
                }
                MYEditorTimeLine.this.restoreViewMarginAndPadding();
                MYEditorTimeLine.this.mDragEnd = true;
                if (MYEditorTimeLine.this.mOnHandAction != null) {
                    long j = 0;
                    if (MYEditorTimeLine.this.mSelectClipIndex - 1 >= 0 && MYEditorTimeLine.this.mSelectClipIndex - 1 < MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.size()) {
                        j = ((MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex)).outPoint;
                    }
                    if (MYEditorTimeLine.this.getCurrentClip().getVideoType() == 1) {
                        MYEditorTimeLine.this.mOnHandAction.onChangeTimeline(MYEditorTimeLine.this.mSelectClipIndex, true, ((MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex)).trimOut, j);
                    } else {
                        MYEditorTimeLine.this.mOnHandAction.onChangeTimeline(MYEditorTimeLine.this.mSelectClipIndex, true, ((MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex)).trimIn, j);
                    }
                    MYEditorTimeLine.this.seekMultiThumbnailSequenceViewLeft();
                    MYEditorTimeLine.this.mDragEnd = false;
                }
                MYEditorTimeLine.this.mIsTrim = false;
            }

            @Override // com.meishe.myvideo.view.MYEditorTimelineSpanView.OnHandChangeListener
            public void onLeftHandChange(boolean z, boolean z2, int i, boolean z3, boolean z4, int i2, boolean z5) {
                MYEditorTimeLine.this.onLeftHandMove(i, z4, z3);
            }

            @Override // com.meishe.myvideo.view.MYEditorTimelineSpanView.OnHandChangeListener
            public void onRightHandDown() {
                MYEditorTimeLine.this.mIsTrim = true;
                MYEditorTimeLine mYEditorTimeLine = MYEditorTimeLine.this;
                mYEditorTimeLine.mThumbnailSequenceDescArrayListOnHandDown = mYEditorTimeLine.mNvsMultiThumbnailSequenceView.getThumbnailSequenceDescArray();
                NvsVideoClip currentClip = MYEditorTimeLine.this.getCurrentClip();
                if (MYEditorTimeLine.this.mAvFileInfos[MYEditorTimeLine.this.mSelectClipIndex] != null) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) MYEditorTimeLine.this.mTimelineSpanRelativeLayout.getLayoutParams();
                    int i = layoutParams.width;
                    MYEditorTimeLine mYEditorTimeLine2 = MYEditorTimeLine.this;
                    layoutParams.width = i + mYEditorTimeLine2.durationToLength(mYEditorTimeLine2.mAvFileInfos[MYEditorTimeLine.this.mSelectClipIndex].longValue());
                    MYEditorTimeLine.this.mTimelineSpanRelativeLayout.setLayoutParams(layoutParams);
                    MYEditorTimeLine mYEditorTimeLine3 = MYEditorTimeLine.this;
                    mYEditorTimeLine3.setMaxRightValue(mYEditorTimeLine3.getTimelineSpan(), currentClip, (MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex));
                } else if (MYEditorTimeLine.this.mOnHandAction != null) {
                    MYEditorTimeLine.this.mOnHandAction.getAvFileDuration(MYEditorTimeLine.this.mSelectClipIndex, false);
                }
            }

            @Override // com.meishe.myvideo.view.MYEditorTimelineSpanView.OnHandChangeListener
            public void onRightHandUp() {
                long j;
                MYEditorTimeLine.this.mDragEnd = true;
                if (MYEditorTimeLine.this.mOnHandAction != null) {
                    if (MYEditorTimeLine.this.mSelectClipIndex < 0 || MYEditorTimeLine.this.mSelectClipIndex + 1 >= MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.size()) {
                        j = ((MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex)).outPoint;
                    } else {
                        j = ((MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex + 1)).inPoint;
                    }
                    long j2 = j - MYEditorTimeLine.LIMIT_FRAME_ERROR;
                    if (((MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex)).trimOut > MYEditorTimeLine.this.mAvFileInfos[MYEditorTimeLine.this.mSelectClipIndex].longValue()) {
                        ((MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex)).trimOut = MYEditorTimeLine.this.mAvFileInfos[MYEditorTimeLine.this.mSelectClipIndex].longValue() - 1;
                    }
                    MYEditorTimeLine.this.mOnHandAction.onChangeTimeline(MYEditorTimeLine.this.mSelectClipIndex, false, ((MultiThumbnailSequenceView.ThumbnailSequenceDesc) MYEditorTimeLine.this.mThumbnailSequenceDescArrayListOnHandDown.get(MYEditorTimeLine.this.mSelectClipIndex)).trimOut, j2);
                }
                MYEditorTimeLine.this.restoreViewMarginAndPadding();
                MYEditorTimeLine.this.seekMultiThumbnailSequenceViewRight();
                MYEditorTimeLine.this.mDragEnd = false;
                MYEditorTimeLine.this.mIsTrim = false;
            }

            @Override // com.meishe.myvideo.view.MYEditorTimelineSpanView.OnHandChangeListener
            public void onRightHandChange(boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5) {
                MYEditorTimeLine.this.onRightHandMove(i, z3, z4, z5);
            }

            @Override // com.meishe.myvideo.view.MYEditorTimelineSpanView.OnHandChangeListener
            public void onNeedScrollParentLinearLayout(int i) {
                MYEditorTimeLine.this.mLinearContainer.scrollBy(i, 0);
                MYEditorTimeLine.this.mCoverRecyclerView.scrollBy(i, 0);
            }
        });
        return mYEditorTimelineSpanView;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setMaxRightValue(MYEditorTimelineSpanView mYEditorTimelineSpanView, NvsVideoClip nvsVideoClip, MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc) {
        Long[] lArr = this.mAvFileInfos;
        int i = this.mSelectClipIndex;
        if (lArr[i] == null) {
            double longValue = (double) lArr[i].longValue();
            double speed = nvsVideoClip.getSpeed();
            Double.isNaN(longValue);
            double d = longValue / speed;
            double d2 = (double) (thumbnailSequenceDesc.trimOut - thumbnailSequenceDesc.trimIn);
            Double.isNaN(d2);
            mYEditorTimelineSpanView.setMaxRightToRight(durationToLength((long) (d - d2)));
        } else if (nvsVideoClip.getVideoType() == 1) {
            mYEditorTimelineSpanView.setMaxRightToRight(durationToLength((this.mAvFileInfos[this.mSelectClipIndex].longValue() - (thumbnailSequenceDesc.trimOut - thumbnailSequenceDesc.trimIn)) / 2));
        } else {
            double longValue2 = (double) this.mAvFileInfos[this.mSelectClipIndex].longValue();
            double speed2 = nvsVideoClip.getSpeed();
            Double.isNaN(longValue2);
            double d3 = longValue2 / speed2;
            double d4 = (double) thumbnailSequenceDesc.trimOut;
            double speed3 = nvsVideoClip.getSpeed();
            Double.isNaN(d4);
            mYEditorTimelineSpanView.setMaxRightToRight(durationToLength((long) (d3 - (d4 / speed3))));
        }
        mYEditorTimelineSpanView.setMaxRightToLeft(durationToLength((thumbnailSequenceDesc.outPoint - thumbnailSequenceDesc.inPoint) - CommonData.MIN_SHOW_LENGTH_DURATION));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onLeftHandMove(int i, boolean z, boolean z2) {
        if (z) {
            this.mNvsMultiThumbnailSequenceView.setStartPadding(this.mNvsMultiThumbnailSequenceView.getStartPadding() + i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mTimelineSpanRelativeLayout.getLayoutParams();
            layoutParams.leftMargin += i;
            this.mTimelineSpanRelativeLayout.setLayoutParams(layoutParams);
        }
        long j = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex).outPoint - this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex).inPoint;
        NvsVideoClip currentClip = getCurrentClip();
        int checkLeftHandDx = checkLeftHandDx(i, currentClip);
        boolean z3 = false;
        boolean z4 = currentClip.getVideoType() == 1;
        lengthToDuration(checkLeftHandDx);
        this.mThumbnailSequenceDescArrayListOnHandDown = getNewThumbnailArray(currentClip, this.mThumbnailSequenceDescArrayListOnHandDown, lengthToDuration(checkLeftHandDx), this.mSelectClipIndex, true, z4);
        refreshMultiThumbnailSequenceView(this.mThumbnailSequenceDescArrayListOnHandDown);
        if (z4) {
            trimOutPointForClipOnThumbnailSequenceView(lengthToDuration(checkLeftHandDx), true);
        } else {
            trimInPointForClipOnThumbnailSequenceView(lengthToDuration(checkLeftHandDx));
        }
        if (this.mOnHandAction != null) {
            int i2 = this.mSelectClipIndex;
            if (i2 - 1 >= 0 && i2 - 1 < this.mThumbnailSequenceDescArrayListOnHandDown.size()) {
                long j2 = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex - 1).outPoint;
            }
        }
        if (getTimelineSpan().getLeft() <= this.mDownLeftSelectClipViewLeft + 10) {
            z3 = true;
        }
        long j3 = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex).outPoint - this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex).inPoint;
        getTimelineSpan().changeDurationText(j3);
        int computeScrollDx = computeScrollDx(j3, j);
        if (!z2 && !z) {
            if (computeScrollDx < 0) {
                multiThumbnailSequenceViewScrollTo(computeScrollDx);
                getTimelineSpan().leftOnDxChange(computeScrollDx, z3);
            } else if (computeScrollDx > 0 && getTimelineSpan().getRightHandX() != this.MIDDLE_LIMIT) {
                multiThumbnailSequenceViewScrollTo(computeScrollDx);
            }
        }
    }

    private int computeScrollDx(long j, long j2) {
        if (j < CommonData.MIN_SHOW_LENGTH_DURATION) {
            j = 800000;
        }
        if (j2 < CommonData.MIN_SHOW_LENGTH_DURATION) {
            j2 = 800000;
        }
        return -durationToLength(j - j2);
    }

    private void multiThumbnailSequenceViewScrollTo(int i) {
        if (getTimelineSpan() != null) {
            if (!getTimelineSpan().isOnMiddleState()) {
                int scrollX = this.mNvsMultiThumbnailSequenceView.getScrollX() - i;
                if (scrollX < 0) {
                    scrollX = -this.mNvsMultiThumbnailSequenceView.getScrollX();
                }
                this.mNvsMultiThumbnailSequenceView.scrollTo(scrollX, 0);
            } else if (getCurrentClip() != null) {
                this.mNvsMultiThumbnailSequenceView.scrollTo(timelinePositionToLength(getCurrentClip().getInPoint()), 0);
            }
        }
    }

    public void multiThumbnailSequenceViewScrollTo(long j) {
        this.mNvsMultiThumbnailSequenceView.scrollTo(timelinePositionToLength(j), 0);
    }

    private void trimInPointForClipOnThumbnailSequenceView(long j) {
        NvsVideoClip currentClip;
        if (getCurrentVideoTrack() != null && (currentClip = getCurrentClip()) != null) {
            setMaxLeftValue(getTimelineSpan(), currentClip, this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex));
        }
    }

    private void refreshCoverView() {
        ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList;
        int i;
        if (this.mEditorTimelineCoverAdapter != null && (arrayList = this.mThumbnailSequenceDescArrayListOnHandDown) != null && arrayList.size() > (i = this.mSelectClipIndex)) {
            this.mEditorTimelineCoverAdapter.refreshItemLength(durationToLength(this.mThumbnailSequenceDescArrayListOnHandDown.get(i).outPoint - this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex).inPoint), this.mSelectClipIndex);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onRightHandMove(int i, boolean z, boolean z2, boolean z3) {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null) {
            Logger.e(TAG, "videoTrack is null");
            return;
        }
        NvsVideoClip currentClip = getCurrentClip();
        int checkRightHandDx = checkRightHandDx(i, currentClip);
        this.mThumbnailSequenceDescArrayListOnHandDown = getNewThumbnailArray(currentClip, this.mThumbnailSequenceDescArrayListOnHandDown, lengthToDuration(checkRightHandDx), this.mSelectClipIndex, false, currentClip.getVideoType() == 1);
        if (z) {
            this.mNvsMultiThumbnailSequenceView.setEndPadding(this.mNvsMultiThumbnailSequenceView.getEndPadding() - checkRightHandDx);
        }
        refreshMultiThumbnailSequenceView(this.mThumbnailSequenceDescArrayListOnHandDown);
        trimOutPointForClipOnThumbnailSequenceView(lengthToDuration(checkRightHandDx), false);
        if (z && currentVideoTrack.getClipCount() > 1) {
            int i2 = this.mSelectClipIndex;
            currentVideoTrack.getClipCount();
        }
        if (z2) {
            MultiThumbnailSequenceView multiThumbnailSequenceView = this.mNvsMultiThumbnailSequenceView;
            multiThumbnailSequenceView.scrollTo(multiThumbnailSequenceView.getScrollX() + checkRightHandDx, 0);
        }
        getTimelineSpan().changeDurationText(this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex).outPoint - this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex).inPoint);
        if (this.mOnHandAction != null) {
            int i3 = this.mSelectClipIndex;
            if (i3 - 1 >= 0 && i3 - 1 < this.mThumbnailSequenceDescArrayListOnHandDown.size()) {
                long j = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex - 1).outPoint;
            }
            long j2 = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex).trimOut;
            long j3 = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex).trimIn;
            currentClip.getSpeed();
        }
    }

    private void trimOutPointForClipOnThumbnailSequenceView(long j, boolean z) {
        NvsVideoClip currentClip;
        if (getCurrentVideoTrack() != null && (currentClip = getCurrentClip()) != null) {
            MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex);
            if (z) {
                setMaxLeftValue(getTimelineSpan(), currentClip, thumbnailSequenceDesc);
            } else {
                setMaxRightValue(getTimelineSpan(), currentClip, thumbnailSequenceDesc);
            }
        }
    }

    private void refreshMultiThumbnailSequenceView(ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList) {
        int i = this.mSelectClipIndex;
        if (i >= 0 && i < arrayList.size() && needRefreshThumbnail(arrayList.get(this.mSelectClipIndex))) {
            this.mNvsMultiThumbnailSequenceView.setThumbnailSequenceDescArray(arrayList);
        }
    }

    private boolean needRefreshThumbnail(MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc) {
        return thumbnailSequenceDesc == null || thumbnailSequenceDesc.outPoint - thumbnailSequenceDesc.inPoint >= CommonData.MIN_SHOW_LENGTH_DURATION;
    }

    private ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> getNewThumbnailArray(NvsVideoClip nvsVideoClip, ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList, long j, int i, boolean z, boolean z2) {
        ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList2 = new ArrayList<>();
        Iterator<MultiThumbnailSequenceView.ThumbnailSequenceDesc> it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(it.next());
        }
        MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = arrayList2.get(i);
        if (!z) {
            return getNewThumbnailArrayOnOutChange(nvsVideoClip, arrayList2, thumbnailSequenceDesc, j, i);
        }
        if (z2) {
            return getNewThumbnailArrayOnOutChange(nvsVideoClip, arrayList2, thumbnailSequenceDesc, -j, i);
        }
        return getNewThumbnailArrayOnInChange(nvsVideoClip, arrayList2, thumbnailSequenceDesc, j, i);
    }

    private ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> getNewThumbnailArrayOnInChange(NvsVideoClip nvsVideoClip, ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList, MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc, long j, int i) {
        long j2 = ((thumbnailSequenceDesc.outPoint - j) - thumbnailSequenceDesc.inPoint) - MIN_DURATION < 0 ? (thumbnailSequenceDesc.outPoint - thumbnailSequenceDesc.inPoint) - MIN_DURATION : j;
        double d = (double) thumbnailSequenceDesc.trimIn;
        double d2 = (double) j2;
        double speed = nvsVideoClip.getSpeed();
        Double.isNaN(d2);
        Double.isNaN(d);
        thumbnailSequenceDesc.trimIn = (long) (d + (d2 * speed));
        thumbnailSequenceDesc.trimIn = Math.max(thumbnailSequenceDesc.trimIn, 0L);
        thumbnailSequenceDesc.outPoint -= j2;
        arrayList.set(i, thumbnailSequenceDesc);
        return changeInAndOutWhenAHeadClipChange(arrayList, j2, i, true);
    }

    private ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> changeInAndOutWhenAHeadClipChange(ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList, long j, int i, boolean z) {
        if (z) {
            j = -j;
        }
        while (true) {
            i++;
            if (i >= arrayList.size()) {
                return arrayList;
            }
            MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = arrayList.get(i);
            thumbnailSequenceDesc.inPoint += j;
            thumbnailSequenceDesc.outPoint += j;
            arrayList.set(i, thumbnailSequenceDesc);
        }
    }

    private ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> getNewThumbnailArrayOnOutChange(NvsVideoClip nvsVideoClip, ArrayList<MultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList, MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc, long j, int i) {
        if (((thumbnailSequenceDesc.outPoint + j) - thumbnailSequenceDesc.inPoint) - MIN_DURATION < 0) {
            j = (thumbnailSequenceDesc.inPoint + MIN_DURATION) - thumbnailSequenceDesc.outPoint;
        }
        double d = (double) thumbnailSequenceDesc.trimOut;
        double d2 = (double) j;
        double speed = nvsVideoClip.getSpeed();
        Double.isNaN(d2);
        Double.isNaN(d);
        thumbnailSequenceDesc.trimOut = (long) (d + (d2 * speed));
        thumbnailSequenceDesc.outPoint += j;
        arrayList.set(i, thumbnailSequenceDesc);
        return changeInAndOutWhenAHeadClipChange(arrayList, j, i, false);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void seekMultiThumbnailSequenceViewLeft() {
        refreshAllTimelineSpan();
    }

    private void setAlignMiddle(boolean z) {
        this.mIsAligningMiddle = z;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void seekMultiThumbnailSequenceViewRight() {
        refreshAllTimelineSpan();
    }

    public void setClipFileDuration(Long l, boolean z) {
        this.mAvFileInfos[this.mSelectClipIndex] = l;
        refreshMaxLengthForTwoHand(getCurrentClip());
        int width = this.mTimelineSpanRelativeLayout.getWidth() + durationToLength(l.longValue());
        if (z) {
            width += this.mAddParentLengthOnLeftHandDown;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mTimelineSpanRelativeLayout.getLayoutParams();
        layoutParams.width = width;
        this.mTimelineSpanRelativeLayout.setLayoutParams(layoutParams);
    }

    private void refreshMaxLengthForTwoHand(NvsVideoClip nvsVideoClip) {
        MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex);
        setMaxRightValue(getTimelineSpan(), nvsVideoClip, thumbnailSequenceDesc);
        setMaxLeftValue(getTimelineSpan(), nvsVideoClip, thumbnailSequenceDesc);
    }

    public void refreshAllTimelineSpan() {
        refreshClipPositions();
        deleteSpanViews();
        addEditorPreviewTimeSpan(this.mSelectClipIndex);
        refreshEditorTimelineCoverAdapter();
        initTransView();
    }

    public void refreshLineView(boolean z) {
        this.mMYLineView.refresh(z);
    }

    private int checkLeftHandDx(int i, NvsVideoClip nvsVideoClip) {
        if (nvsVideoClip.getVideoType() == 1) {
            return checkRightHandDx(i, nvsVideoClip);
        }
        long lengthToDuration = lengthToDuration(i);
        MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex);
        double d = (double) thumbnailSequenceDesc.trimIn;
        double d2 = (double) lengthToDuration;
        double speed = nvsVideoClip.getSpeed();
        Double.isNaN(d2);
        Double.isNaN(d);
        if (((long) (d + (d2 * speed))) < 0) {
            double d3 = (double) (0 - thumbnailSequenceDesc.trimIn);
            double speed2 = nvsVideoClip.getSpeed();
            Double.isNaN(d3);
            return durationToLength((long) (d3 * speed2));
        }
        return thumbnailSequenceDesc.outPoint - (lengthToDuration + thumbnailSequenceDesc.inPoint) < MIN_DURATION ? durationToLength((thumbnailSequenceDesc.outPoint - thumbnailSequenceDesc.inPoint) - MIN_DURATION) : i;
    }

    private int checkRightHandDx(int i, NvsVideoClip nvsVideoClip) {
        MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = this.mThumbnailSequenceDescArrayListOnHandDown.get(this.mSelectClipIndex);
        long lengthToDuration = lengthToDuration(i);
        if ((thumbnailSequenceDesc.outPoint + lengthToDuration) - thumbnailSequenceDesc.inPoint < MIN_DURATION) {
            return durationToLength(MIN_DURATION - (thumbnailSequenceDesc.outPoint - thumbnailSequenceDesc.inPoint));
        }
        if (nvsVideoClip.getVideoType() == 1) {
            return i;
        }
        double d = (double) thumbnailSequenceDesc.trimOut;
        double d2 = (double) lengthToDuration;
        double speed = nvsVideoClip.getSpeed();
        Double.isNaN(d2);
        Double.isNaN(d);
        if (((long) (d + (d2 * speed))) <= this.mAvFileInfos[this.mSelectClipIndex].longValue()) {
            return i;
        }
        double longValue = (double) (this.mAvFileInfos[this.mSelectClipIndex].longValue() - thumbnailSequenceDesc.trimOut);
        double speed2 = nvsVideoClip.getSpeed();
        Double.isNaN(longValue);
        return durationToLength((long) (longValue / speed2));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setMaxLeftValue(MYEditorTimelineSpanView mYEditorTimelineSpanView, NvsVideoClip nvsVideoClip, MultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc) {
        if (this.mAvFileInfos[this.mSelectClipIndex] == null || nvsVideoClip.getVideoType() != 1) {
            double d = (double) thumbnailSequenceDesc.trimIn;
            double speed = nvsVideoClip.getSpeed();
            Double.isNaN(d);
            mYEditorTimelineSpanView.setMaxLeftToLeft(durationToLength((long) (d / speed)));
        } else {
            mYEditorTimelineSpanView.setMaxLeftToLeft(durationToLength((this.mAvFileInfos[this.mSelectClipIndex].longValue() - (thumbnailSequenceDesc.trimOut - thumbnailSequenceDesc.trimIn)) / 2));
        }
        mYEditorTimelineSpanView.setMaxLeftToRight(durationToLength((thumbnailSequenceDesc.outPoint - thumbnailSequenceDesc.inPoint) - CommonData.MIN_SHOW_LENGTH_DURATION));
    }

    public NvsVideoClip getCurrentClip() {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null) {
            return null;
        }
        int i = this.mSelectClipIndex;
        if (i < 0) {
            this.mSelectClipIndex = 0;
        } else if (i >= currentVideoTrack.getClipCount()) {
            this.mSelectClipIndex = currentVideoTrack.getClipCount() - 1;
        }
        return currentVideoTrack.getClipByIndex(this.mSelectClipIndex);
    }

    private void refreshTimelineTransitionAdapter() {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null) {
            Logger.e(TAG, "videoTrack is null");
            return;
        }
        this.mEditorTimelineTransitionAdapter = new EditorTimelineTransitionAdapter(this.mContext, this.mNvsMultiThumbnailSequenceView.getStartPadding(), this.mNvsMultiThumbnailSequenceView.getEndPadding(), getVideoClipArray(currentVideoTrack), currentVideoTrack, this.mClipPositionList);
        initEditorTimelineTransitionAdapterListener();
        this.mTransRecyclerView.setAdapter(this.mEditorTimelineTransitionAdapter);
    }

    public void refreshTimelineMarkingLineAdapter() {
        if (getCurrentVideoTrack() == null) {
            Logger.e(TAG, "videoTrack is null");
            return;
        }
        this.mEditorTimelineMarkingLineAdapter = new EditorTimelineMarkingLineAdapter(this.mContext, this.mNvsMultiThumbnailSequenceView.getStartPadding(), this.mNvsMultiThumbnailSequenceView.getEndPadding(), this.mTimeline.getDuration() / 1000000);
        this.mMarkingLineRecyclerView.setAdapter(this.mEditorTimelineMarkingLineAdapter);
    }

    public void refreshTimelineTransition(int i, BaseInfo baseInfo) {
        EditorTimelineTransitionAdapter editorTimelineTransitionAdapter = this.mEditorTimelineTransitionAdapter;
        if (editorTimelineTransitionAdapter != null) {
            editorTimelineTransitionAdapter.refresh(baseInfo, i, !baseInfo.mName.equals(getResources().getString(R.string.top_menu_no)));
        }
    }

    public void refreshTimelineTransition(BaseInfo baseInfo) {
        this.mEditorTimelineTransitionAdapter.refreshAll(baseInfo);
    }

    public void setTimelineTransitionVisable(boolean z) {
        this.mEditorTimelineTransitionAdapter.setIconVisiable(z);
    }

    public List<EditorTimelineTransitionAdapter.TransitionData> getTransitionDatas() {
        return this.mEditorTimelineTransitionAdapter.getCurrSelectedTransitionDatas();
    }

    private List<NvsVideoClip> getVideoClipArray(NvsVideoTrack nvsVideoTrack) {
        ArrayList arrayList = new ArrayList();
        int clipCount = nvsVideoTrack.getClipCount();
        for (int i = 0; i < clipCount; i++) {
            arrayList.add(nvsVideoTrack.getClipByIndex(i));
        }
        return arrayList;
    }

    private Pair<Integer, Integer> getClipPosition(int i) {
        List<Pair<Integer, Integer>> list = this.mClipPositionList;
        if (list == null || i >= list.size() || i < 0) {
            return null;
        }
        return this.mClipPositionList.get(i);
    }

    private NvsVideoTrack getCurrentVideoTrack() {
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline != null) {
            return nvsTimeline.getVideoTrackByIndex(0);
        }
        Logger.e(TAG, "timeline is null");
        return null;
    }

    private void refreshClipPositions() {
        long j;
        Logger.d(TAG, "refreshClipPositions");
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null) {
            Logger.e(TAG, "videoTrack is null");
            return;
        }
        this.mClipPositionList.clear();
        int clipCount = currentVideoTrack.getClipCount();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < clipCount) {
            NvsVideoClip clipByIndex = currentVideoTrack.getClipByIndex(i);
            if (clipByIndex == null) {
                j = 0;
            } else {
                j = clipByIndex.getOutPoint() - clipByIndex.getInPoint();
            }
            i2 = durationToShownLength(j) + i3;
            this.mClipPositionList.add(new Pair<>(Integer.valueOf(i3), Integer.valueOf(i2)));
            i++;
            i3 = i2;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mTimelineSpanRelativeLayout.getLayoutParams();
        layoutParams.width = i2 + (this.mHandWidth * 2);
        this.mTimelineSpanRelativeLayout.setLayoutParams(layoutParams);
    }

    private int durationToShownLength(long j) {
        return durationToLength(j);
    }

    public void initTimelineCover() {
        if (((EditorTimelineRectCoverAdapter) this.mCoverRecyclerView.getAdapter()) == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mContext);
            linearLayoutManager.setOrientation(0);
            this.mCoverRecyclerView.setLayoutManager(linearLayoutManager);
            this.mCoverRecyclerView.setAnimation(null);
            refreshEditorTimelineCoverAdapter();
            return;
        }
        refreshEditorTimelineCoverAdapter();
    }

    public void refreshEditorTimelineCoverAdapter() {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack != null) {
            EditorTimelineRectCoverAdapter editorTimelineRectCoverAdapter = this.mEditorTimelineCoverAdapter;
            if (editorTimelineRectCoverAdapter == null) {
                this.mEditorTimelineCoverAdapter = new EditorTimelineRectCoverAdapter(this.mContext, this.mNvsMultiThumbnailSequenceView.getStartPadding(), this.mNvsMultiThumbnailSequenceView.getEndPadding(), getVideoClipArray(currentVideoTrack), this.mPixelPerMicrosecond);
            } else {
                editorTimelineRectCoverAdapter.initData(this.mNvsMultiThumbnailSequenceView.getStartPadding(), this.mNvsMultiThumbnailSequenceView.getEndPadding(), getVideoClipArray(currentVideoTrack), this.mPixelPerMicrosecond);
            }
            this.mEditorTimelineCoverAdapter.setMinThumbnailWidth(getMinClipShownWidth());
            this.mCoverRecyclerView.setAdapter(this.mEditorTimelineCoverAdapter);
            this.mCoverRecyclerView.scrollBy(this.mNvsMultiThumbnailSequenceView.getScrollX(), 0);
        }
    }

    private int getMinClipShownWidth() {
        return durationToLength(CommonData.MIN_SHOW_LENGTH_DURATION);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        NvsVideoClip clipByTimelinePosition;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mTotalMoveXOnIntercept = 0;
            this.mDownTimeNow = System.currentTimeMillis();
            this.mOnInterceptDownX = motionEvent.getX();
            this.mReceiveMoveEvent = false;
            OnScrollListener onScrollListener = this.mScrollListener;
            if (onScrollListener != null) {
                onScrollListener.onSeekingTimeline(true);
            }
        } else if (action == 1) {
            handleStop();
            if (System.currentTimeMillis() - this.mDownTimeNow > 200) {
                Logger.e(TAG, "onInterceptTouchEvent: 长按返回");
                return false;
            } else if (this.mTotalMoveXOnIntercept > 10) {
                Logger.e(TAG, "onInterceptTouchEvent: 移动返回");
                return false;
            } else {
                EditorTimelineTransitionAdapter editorTimelineTransitionAdapter = this.mEditorTimelineTransitionAdapter;
                if (editorTimelineTransitionAdapter == null || !editorTimelineTransitionAdapter.isDownOnTrans()) {
                    int[] iArr = new int[2];
                    this.mAddClipImg.getLocationInWindow(iArr);
                    if (this.mAddClipImg == null || motionEvent.getRawX() <= ((float) iArr[0]) || ((float) (iArr[0] + this.mAddClipImg.getWidth())) <= motionEvent.getRawX() || motionEvent.getRawY() <= ((float) iArr[1]) || ((float) (iArr[1] + this.mAddClipImg.getHeight())) <= motionEvent.getRawY()) {
                        int[] iArr2 = new int[2];
                        this.mTimelineSpanRelativeLayout.getLocationInWindow(iArr2);
                        int rawX = (int) (motionEvent.getRawX() - ((float) iArr2[0]));
                        motionEvent.getRawY();
                        int i = iArr2[1];
                        if (rawX < 0) {
                            Log.e(TAG, "点击超出 ");
                            outOfSequenceView();
                            return false;
                        }
                        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
                        if (currentVideoTrack == null) {
                            Logger.e(TAG, "onInterceptTouchEvent: videoTrack is null!");
                            return false;
                        } else if (lengthBigToTimelineDuration(rawX)) {
                            Logger.e(TAG, "onInterceptTouchEvent: 超出时间线位置!");
                            outOfSequenceView();
                            return false;
                        } else {
                            long lengthToTimelinePosition = lengthToTimelinePosition(rawX);
                            NvsTimeline nvsTimeline = this.mTimeline;
                            if ((nvsTimeline != null && lengthToTimelinePosition > nvsTimeline.getDuration()) || (clipByTimelinePosition = currentVideoTrack.getClipByTimelinePosition(lengthToTimelinePosition)) == null || ((MeicamVideoClip) TimelineDataUtil.getMainTrackVideoClip().get(clipByTimelinePosition.getIndex())).getVideoType().equals(CommonData.CLIP_HOLDER)) {
                                return false;
                            }
                            int clipIndexByClip = getClipIndexByClip(clipByTimelinePosition);
                            Logger.d(TAG, "clipIndex: " + clipIndexByClip + " mSelectClipIndex: " + this.mSelectClipIndex);
                            int i2 = this.mSelectClipIndex;
                            if (clipIndexByClip == i2) {
                                return false;
                            }
                            this.mScrollToClickClip = true;
                            refreshTimelineSpanChange(i2, clipIndexByClip);
                            this.mSelectClipIndex = clipIndexByClip;
                            OnScrollListener onScrollListener2 = this.mScrollListener;
                            if (onScrollListener2 != null) {
                                onScrollListener2.onSelectClipIndexChange(this.mSelectClipIndex);
                            }
                            if (this.mSelectClipIndex < 0) {
                                return false;
                            }
                            if (this.mMode == 2) {
                                hideTransitionAndShowCover();
                            } else {
                                hideTransitionAndShowTrim();
                            }
                            this.mScrollToClickClip = false;
                            return false;
                        }
                    } else {
                        Logger.d(TAG, "按在添加按钮上！");
                        return false;
                    }
                } else {
                    Log.e(TAG, "按在转场上 ");
                    return false;
                }
            }
        } else if (action == 2) {
            this.mReceiveMoveEvent = true;
            this.mTotalMoveXOnIntercept = (int) (((float) this.mTotalMoveXOnIntercept) + Math.abs(motionEvent.getX() - this.mOnInterceptDownX));
        }
        return false;
    }

    private int getTransitionIndex(long j, int i, NvsVideoTrack nvsVideoTrack) {
        NvsVideoClip clipByIndex;
        if (nvsVideoTrack == null || i < 0 || i > nvsVideoTrack.getClipCount() || (clipByIndex = getClipByIndex(i)) == null) {
            return -1;
        }
        long lengthToDuration = lengthToDuration(this.mTransIconWidth) / 2;
        if (j < clipByIndex.getOutPoint() - lengthToDuration) {
            i = j <= clipByIndex.getInPoint() + lengthToDuration ? i - 1 : -1;
        }
        if (i < 0 || i >= nvsVideoTrack.getClipCount() - 1) {
            return -1;
        }
        return i;
    }

    private boolean lengthBigToTimelineDuration(int i) {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null) {
            return true;
        }
        int clipCount = currentVideoTrack.getClipCount();
        long j = 0;
        for (int i2 = 0; i2 < clipCount; i2++) {
            NvsVideoClip clipByIndex = currentVideoTrack.getClipByIndex(i2);
            j += (long) durationToLength(clipByIndex.getOutPoint() - clipByIndex.getInPoint());
        }
        if (((long) i) > j) {
            return true;
        }
        return false;
    }

    private void deleteSpanViews() {
        RelativeLayout relativeLayout = this.mTimelineSpanRelativeLayout;
        if (relativeLayout != null) {
            relativeLayout.removeAllViews();
        }
        this.mTimelineSpanView = null;
    }

    public void setUnSelectSpan() {
        deleteSpanViews();
        this.mSelectClipIndex = -1;
    }

    public void setSelectSpan(long j) {
        this.mSelectClipIndex = getClipIndexByTime(j);
        addEditorPreviewTimeSpan(this.mSelectClipIndex);
    }

    private void refreshTimelineSpanChange(int i, int i2) {
        if (i < 0 || i2 < 0) {
            Logger.e(TAG, "before or after is invalid");
            return;
        }
        NvsVideoClip clipByIndex = getClipByIndex(i2);
        EditorTimelineRectCoverAdapter editorTimelineRectCoverAdapter = this.mEditorTimelineCoverAdapter;
        if (editorTimelineRectCoverAdapter != null) {
            editorTimelineRectCoverAdapter.refresh(i, i2);
            if (clipByIndex != null && this.mTimelineSpanView != null) {
                Pair<Integer, Integer> clipPosition = getClipPosition(i2);
                this.mTimelineSpanView.setClipPosition(((Integer) clipPosition.first).intValue(), ((Integer) clipPosition.second).intValue());
            }
        }
    }

    private NvsVideoClip getClipByIndex(int i) {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null) {
            return null;
        }
        return currentVideoTrack.getClipByIndex(i);
    }

    private int getClipIndexByClip(NvsVideoClip nvsVideoClip) {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null) {
            return -1;
        }
        int clipCount = currentVideoTrack.getClipCount();
        if (nvsVideoClip != null) {
            for (int i = 0; i < clipCount; i++) {
                if (currentVideoTrack.getClipByIndex(i).equals(nvsVideoClip)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getClipIndexByTime(long j) {
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack == null) {
            return -1;
        }
        int clipCount = currentVideoTrack.getClipCount();
        NvsVideoClip clipByTimelinePosition = currentVideoTrack.getClipByTimelinePosition(j);
        if (clipByTimelinePosition != null) {
            for (int i = 0; i < clipCount; i++) {
                if (currentVideoTrack.getClipByIndex(i).equals(clipByTimelinePosition)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean checkInterceptTouchEvent() {
        if (getTimelineSpan() == null || this.mTimeline == null) {
            return false;
        }
        return true;
    }

    public MYEditorTimelineSpanView getTimelineSpan() {
        return this.mTimelineSpanView;
    }

    public long lengthToDuration(int i) {
        double d = (double) i;
        double d2 = this.mPixelPerMicrosecond;
        Double.isNaN(d);
        return (long) Math.floor((d / d2) + 0.5d);
    }

    public int durationToLength(long j) {
        double d = (double) j;
        double d2 = this.mPixelPerMicrosecond;
        Double.isNaN(d);
        return (int) Math.floor((d * d2) + 0.5d);
    }

    public void setSequenceLeftPadding(int i) {
        this.mSequenceLeftPadding = i;
    }

    public void setSequenceRightPadding(int i) {
        this.mSequenceRightPadding = i;
    }

    public MultiThumbnailSequenceView getMultiThumbnailSequenceView() {
        return this.mNvsMultiThumbnailSequenceView;
    }

    public int getSequenceWidth() {
        double d = (double) this.mTimelineDuration;
        double d2 = this.mPixelPerMicrosecond;
        Double.isNaN(d);
        return (int) Math.floor((d * d2) + 0.5d);
    }

    public void toOtherMenu() {
        refreshLineView(false);
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.multi_thumbnail_sequence_other_margin);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mEditorMainTrackParent.getLayoutParams();
        layoutParams.topMargin = dimensionPixelOffset;
        this.mEditorMainTrackParent.setLayoutParams(layoutParams);
    }

    public void toMainMenu() {
        refreshLineView(true);
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.multi_thumbnail_sequence_main_margin);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mEditorMainTrackParent.getLayoutParams();
        layoutParams.topMargin = dimensionPixelOffset;
        this.mEditorMainTrackParent.setLayoutParams(layoutParams);
        showTransitionAndHideTrimView();
    }

    @Override // com.meishe.myvideo.util.PixelPerMicrosecondUtil.PixelPerMicrosecondChangeListener
    public void onPixelPerMicrosecondChange(double d, float f) {
        this.mPixelPerMicrosecond = d;
        this.mNvsMultiThumbnailSequenceView.setPixelPerMicrosecond(d);
        refreshEditorTimelineCoverAdapter();
        refreshClipPositions();
        initTimelineSpanParentLayout();
        int i = this.mSelectClipIndex;
        if (!(i == -1 || this.mTimelineSpanView == null)) {
            addEditorPreviewTimeSpan(i);
        }
        this.mEditorTimelineTransitionAdapter.refreshTransition(this.mClipPositionList);
        this.mEditorTimelineTransitionAdapter.notifyDataSetChanged();
        this.mEditorTimelineTransitionAdapter.setDownOnTrans(false);
        MYLineView mYLineView = this.mMYLineView;
        if (mYLineView != null) {
            mYLineView.refreshOnPixelChange();
        }
        this.mEditorTimelineMarkingLineAdapter.setUnitDistance(durationToLength(1000000), f, this.mTimeline.getDuration() / 1000000);
        new Handler().postDelayed(new Runnable() {
            /* class com.meishe.myvideo.view.MYEditorTimeLine.AnonymousClass9 */

            public void run() {
                MYEditorTimeLine.this.mMarkingLineRecyclerView.scrollBy(-(MYEditorTimeLine.this.getMarkingLineDistance() - MYEditorTimeLine.this.mNvsMultiThumbnailSequenceView.getScrollX()), 0);
                MYEditorTimeLine.this.mTransRecyclerView.scrollBy(-(MYEditorTimeLine.this.getTransDistance() - MYEditorTimeLine.this.mNvsMultiThumbnailSequenceView.getScrollX()), 0);
            }
        }, 0);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int getMarkingLineDistance() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) this.mMarkingLineRecyclerView.getLayoutManager();
        int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        View findViewByPosition = linearLayoutManager.findViewByPosition(findFirstVisibleItemPosition);
        int width = findViewByPosition.getWidth();
        if (findFirstVisibleItemPosition > 0) {
            return (this.mEditorTimelineMarkingLineAdapter.getFirstWidth() + ((findFirstVisibleItemPosition - 1) * width)) - findViewByPosition.getLeft();
        }
        return (findFirstVisibleItemPosition * width) - findViewByPosition.getLeft();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int getTransDistance() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) this.mTransRecyclerView.getLayoutManager();
        int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        View findViewByPosition = linearLayoutManager.findViewByPosition(findFirstVisibleItemPosition);
        int i = 0;
        for (int i2 = 0; i2 < findFirstVisibleItemPosition; i2++) {
            i += this.mEditorTimelineTransitionAdapter.getItemData(i2).getLength();
        }
        return i - findViewByPosition.getLeft();
    }

    public void refreshCoverView(long j) {
        NvsVideoClip clipByTimelinePosition;
        NvsVideoTrack currentVideoTrack = getCurrentVideoTrack();
        if (currentVideoTrack != null && (clipByTimelinePosition = currentVideoTrack.getClipByTimelinePosition(j)) != null) {
            this.mEditorTimelineCoverAdapter.refresh(clipByTimelinePosition.getIndex());
        }
    }

    public void setOnHandAction(OnHandAction onHandAction) {
        this.mOnHandAction = onHandAction;
    }

    public void setOnTimeLineEditorListener(OnTimeLineEditorListener onTimeLineEditorListener) {
        this.mOnTimeLineEditorListener = onTimeLineEditorListener;
    }

    public void setTimeline(NvsTimeline nvsTimeline) {
        this.mTimeline = nvsTimeline;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mScrollListener = onScrollListener;
    }

    public void setOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        this.mOnSelectItemListener = onSelectItemListener;
    }
}
