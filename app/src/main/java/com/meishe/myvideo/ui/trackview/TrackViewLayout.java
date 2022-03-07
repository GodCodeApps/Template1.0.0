package com.meishe.myvideo.ui.trackview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.MeicamAudioTrack;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.ui.bean.BaseUIClip;
import com.meishe.myvideo.ui.trackview.HandView;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TrackViewLayout extends RelativeLayout implements HandView.OnDownToGetNextClip, PixelPerMicrosecondUtil.PixelPerMicrosecondChangeListener {
    private static final String TAG = "TrackViewLayout";
    TranslateAnimation animation = null;
    private ImageView imageAudio;
    private LinearLayout linearScroll;
    private RelativeLayout mAddVoice;
    private Context mContext;
    private State mCurrentState;
    private long mDownTime = 0;
    private float mDragTotalY;
    private BaseItemView mDragView;
    private float mDragViewOrigX;
    private float mDragViewOrigY;
    private MYHorizontalScrollView mHorizontalScrollView;
    private HashMap<Integer, List<BaseUIClip>> mIntegerListHashMap = new HashMap<>();
    private boolean mIsDragSuccess = true;
    private float mLastPointX;
    private float mLastPointY;
    private boolean mLongPressEnd = false;
    private Handler mLongPressHandler = new Handler();
    private BaseItemView mNewDragView;
    private HandView.OnHandChangeListener mOnHandChangeListener;
    private OnTrackViewDragListener mOnTrackViewDragListener;
    private OnTrackViewScrollListener mOnTrackViewScrollListener;
    private int mSlop;
    private int mStartPadding;
    private long mTimelineDuration;
    private int mTrackHeight = 0;
    private int mTrackViewMarginTop = 0;
    private FrameLayout mTrackViewVerticalParent;
    private MYScrollView mTrackViewVerticalScroll;
    private TextView mTvAdd;
    private TextView mTvMusicDesc;
    private RelativeLayout track_view_horizontal_layout;

    public interface OnTrackViewDragListener {
        Object dragEnd(BaseUIClip baseUIClip, int i, long j);

        void onSelectClip(BaseUIClip baseUIClip);
    }

    public interface OnTrackViewScrollListener {
        void clickOutSide();

        void clickToMusicMenu();

        void onTrackViewLongClick(BaseUIClip baseUIClip);

        void scrollX(int i, int i2);

        void startScroll();
    }

    private enum State {
        IDLE,
        DRAGGING
    }

    @Override // com.meishe.myvideo.util.PixelPerMicrosecondUtil.PixelPerMicrosecondChangeListener
    public void onPixelPerMicrosecondChange(double d, float f) {
        initWidth(this.mTimelineDuration);
        for (int childCount = this.mTrackViewVerticalParent.getChildCount() - 1; childCount >= 0; childCount--) {
            BaseItemView baseItemView = (BaseItemView) this.mTrackViewVerticalParent.getChildAt(childCount);
            BaseUIClip baseUIClip = baseItemView.getBaseUIClip();
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = getChildTopMarginFromDuration(baseUIClip.getInPoint());
            layoutParams.topMargin = (this.mTrackHeight * baseUIClip.getTrackIndex()) + this.mTrackViewMarginTop;
            baseItemView.setLayoutParams(layoutParams);
        }
        BaseItemView baseItemView2 = this.mDragView;
        if (baseItemView2 != null) {
            HandView handView = baseItemView2.getHandView();
            BaseUIClip baseUIClip2 = this.mDragView.getBaseUIClip();
            if (!(handView == null || baseUIClip2 == null)) {
                handView.setBaseUIClip(baseUIClip2);
            }
        }
    }

    public TrackViewLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TrackViewLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public TrackViewLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        PixelPerMicrosecondUtil.addPixelPerMicrosecondChangeListener(this);
        this.mContext = context;
        this.mStartPadding = ScreenUtils.getScreenWidth(context) / 2;
        this.mTrackHeight = this.mContext.getResources().getDimensionPixelOffset(R.dimen.track_view_height);
        this.mTrackViewMarginTop = this.mContext.getResources().getDimensionPixelOffset(R.dimen.track_view_real_margin_top);
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.track_view_layout, this);
        this.mHorizontalScrollView = (MYHorizontalScrollView) inflate.findViewById(R.id.track_view_horizontal_scroll);
        this.mTrackViewVerticalScroll = (MYScrollView) inflate.findViewById(R.id.track_view_vertical_scroll);
        this.mTrackViewVerticalParent = (FrameLayout) inflate.findViewById(R.id.track_view_vertical_parent);
        this.linearScroll = (LinearLayout) inflate.findViewById(R.id.linear_scroll);
        this.track_view_horizontal_layout = (RelativeLayout) inflate.findViewById(R.id.track_view_horizontal_layout);
        this.mTvAdd = (TextView) inflate.findViewById(R.id.tv_add);
        this.mTvMusicDesc = (TextView) inflate.findViewById(R.id.tv_music_desc);
        this.mAddVoice = (RelativeLayout) inflate.findViewById(R.id.ll_add_voice);
        this.imageAudio = (ImageView) inflate.findViewById(R.id.image_audio);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.imageAudio.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(this.mContext) / 2;
        this.imageAudio.setLayoutParams(layoutParams);
        setAudioView();
        this.mHorizontalScrollView.setOnScrollViewListener(new OnScrollViewListener() {
            /* class com.meishe.myvideo.ui.trackview.TrackViewLayout.AnonymousClass1 */

            @Override // com.meishe.myvideo.ui.trackview.OnScrollViewListener
            public void onScrollChanged(int i, int i2, int i3, int i4) {
                if (TrackViewLayout.this.mOnTrackViewScrollListener != null) {
                    TrackViewLayout.this.mOnTrackViewScrollListener.scrollX(i, i3);
                }
            }
        });
        this.mAddVoice.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.ui.trackview.TrackViewLayout.AnonymousClass2 */

            public void onClick(View view) {
                if (TrackViewLayout.this.mOnTrackViewScrollListener != null) {
                    TrackViewLayout.this.mOnTrackViewScrollListener.clickToMusicMenu();
                }
            }
        });
    }

    public void initWidth(long j) {
        this.mTimelineDuration = j;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mTrackViewVerticalParent.getLayoutParams();
        layoutParams.width = PixelPerMicrosecondUtil.durationToLength(j) + ScreenUtils.getScreenWidth(this.mContext);
        this.mTrackViewVerticalParent.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mAddVoice.getLayoutParams();
        layoutParams2.leftMargin = this.mStartPadding;
        int durationToLength = PixelPerMicrosecondUtil.durationToLength(j);
        if (((float) durationToLength) < getResources().getDimension(R.dimen.add_voice_width)) {
            layoutParams2.width = (int) getResources().getDimension(R.dimen.add_voice_width);
        } else {
            layoutParams2.width = durationToLength;
        }
        this.mAddVoice.setLayoutParams(layoutParams2);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x000e, code lost:
        if (r0 != 3) goto L_0x0124;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r8) {
        /*
        // Method dump skipped, instructions count: 293
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.myvideo.ui.trackview.TrackViewLayout.onTouchEvent(android.view.MotionEvent):boolean");
        return false;
    }

    private void logAllChildView() {
        int childCount = this.mTrackViewVerticalParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mTrackViewVerticalParent.getChildAt(i);
            Log.e(TAG, "logAllChildView: " + childAt.getLeft() + " " + childAt.getTop() + "  " + childAt.getWidth() + "  " + childAt.getHeight() + "  " + childAt);
        }
    }

    private boolean checkCanDragToHere(int i, long j) {
        BaseUIClip baseUIClip = this.mDragView.getBaseUIClip();
        double d = (double) j;
        double trimOut = (double) (baseUIClip.getTrimOut() - baseUIClip.getTrimIn());
        double speed = baseUIClip.getSpeed();
        Double.isNaN(trimOut);
        Double.isNaN(d);
        long j2 = (long) (d + (trimOut / speed));
        if (j2 > this.mTimelineDuration) {
            return false;
        }
        List<BaseUIClip> list = this.mIntegerListHashMap.get(Integer.valueOf(i));
        if (list != null && !list.isEmpty()) {
            for (BaseUIClip baseUIClip2 : list) {
                long inPoint = baseUIClip2.getInPoint();
                double d2 = (double) inPoint;
                double trimOut2 = (double) (baseUIClip2.getTrimOut() - baseUIClip2.getTrimIn());
                double speed2 = baseUIClip2.getSpeed();
                Double.isNaN(trimOut2);
                Double.isNaN(d2);
                long j3 = (long) (d2 + (trimOut2 / speed2));
                if (!(i == baseUIClip.getTrackIndex() && inPoint == baseUIClip.getInPoint()) && ((j < inPoint && j2 > inPoint) || ((j2 > j3 && j < j3) || (j > inPoint && j2 < j3)))) {
                    return false;
                }
            }
        }
        return true;
    }

    private long getClipDuration(BaseUIClip baseUIClip) {
        double trimOut = (double) (baseUIClip.getTrimOut() - baseUIClip.getTrimIn());
        double speed = baseUIClip.getSpeed();
        Double.isNaN(trimOut);
        return (long) (trimOut / speed);
    }

    public void smoothScrollView(int i) {
        this.mHorizontalScrollView.smoothScrollTo(i, 0);
    }

    public int getHorizontalScrollX() {
        return this.mHorizontalScrollView.getScrollX();
    }

    public void smoothScrollY(int i) {
        this.mTrackViewVerticalScroll.scrollTo(0, i);
    }

    public void smoothScrollByY(int i) {
        this.mTrackViewVerticalScroll.scrollBy(0, i);
    }

    public void scrollAnimation(int i) {
        this.linearScroll.clearAnimation();
        this.animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-i));
        this.animation.setFillAfter(true);
        this.animation.setDuration(100);
        this.linearScroll.setAnimation(this.animation);
        this.animation.startNow();
    }

    public void cancleAnimation() {
        this.linearScroll.clearAnimation();
    }

    private BaseItemView isPointOnViews(MotionEvent motionEvent) {
        Rect rect = new Rect();
        for (int childCount = this.mTrackViewVerticalParent.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.mTrackViewVerticalParent.getChildAt(childCount);
            rect.set((int) childAt.getX(), (int) childAt.getY(), ((int) childAt.getX()) + childAt.getWidth(), ((int) childAt.getY()) + childAt.getHeight());
            if (rect.contains((int) (motionEvent.getX() + ((float) this.mHorizontalScrollView.getScrollX())), (int) (motionEvent.getY() + ((float) this.mTrackViewVerticalScroll.getScrollY())))) {
                BaseItemView baseItemView = (BaseItemView) childAt;
                this.mDragViewOrigX = baseItemView.getX();
                this.mDragViewOrigY = baseItemView.getY();
                return baseItemView;
            }
        }
        return null;
    }

    public void setSelectDragView(BaseUIClip baseUIClip) {
        for (int childCount = this.mTrackViewVerticalParent.getChildCount() - 1; childCount >= 0; childCount--) {
            BaseItemView baseItemView = (BaseItemView) this.mTrackViewVerticalParent.getChildAt(childCount);
            BaseUIClip baseUIClip2 = baseItemView.getBaseUIClip();
            int trackIndex = baseUIClip2.getTrackIndex();
            long inPoint = baseUIClip2.getInPoint();
            Log.e(TAG, "setSelectDragView: " + trackIndex + "  " + baseUIClip.getTrackIndex() + "  " + baseUIClip.getInPoint() + "  " + inPoint);
            if (trackIndex == baseUIClip.getTrackIndex() && baseUIClip.getInPoint() == inPoint) {
                setPipDuringVisiableStatus(false);
                this.mDragView = baseItemView;
                addSelectHandView(this.mDragView);
                return;
            }
        }
    }

    public int getTopHeightByTrackIndex(int i) {
        return this.mTrackHeight * i;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mDownTime = System.currentTimeMillis();
            HandView handView = getHandView();
            if (handView != null ? handView.isHandDownOnHandView(motionEvent.getRawX(), motionEvent.getRawY()) : false) {
                return false;
            }
            this.mNewDragView = isPointOnViews(motionEvent);
            if (this.mNewDragView != null) {
                this.mCurrentState = State.DRAGGING;
                this.mLastPointX = motionEvent.getX();
                this.mLastPointY = motionEvent.getY();
                this.mLongPressHandler.postDelayed(new Runnable() {
                    /* class com.meishe.myvideo.ui.trackview.TrackViewLayout.AnonymousClass3 */

                    public void run() {
                        if (TrackViewLayout.this.mOnTrackViewScrollListener != null) {
                            TrackViewLayout.this.clickOutSide();
                            if (TrackViewLayout.this.mNewDragView != null) {
                                TrackViewLayout.this.mOnTrackViewScrollListener.onTrackViewLongClick(TrackViewLayout.this.mNewDragView.getBaseUIClip());
                            }
                        }
                        TrackViewLayout.this.mLongPressEnd = true;
                        TrackViewLayout trackViewLayout = TrackViewLayout.this;
                        trackViewLayout.mDragView = trackViewLayout.mNewDragView;
                        TrackViewLayout.this.mDragView.bringToFront();
                        ((Vibrator) TrackViewLayout.this.mContext.getSystemService("vibrator")).vibrate(200);
                    }
                }, 500);
            }
            OnTrackViewScrollListener onTrackViewScrollListener = this.mOnTrackViewScrollListener;
            if (onTrackViewScrollListener != null) {
                onTrackViewScrollListener.startScroll();
            }
        } else if (motionEvent.getAction() == 2 && Math.abs(motionEvent.getX() - this.mLastPointX) > 1.0f) {
            this.mLongPressHandler.removeCallbacksAndMessages(null);
        }
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.mLongPressHandler.removeCallbacksAndMessages(null);
            this.mLongPressEnd = false;
            if (System.currentTimeMillis() - this.mDownTime < 200) {
                setPipDuringVisiableStatus(false);
                this.mDragView = this.mNewDragView;
                BaseItemView baseItemView = this.mDragView;
                if (baseItemView != null) {
                    addSelectHandView(baseItemView);
                } else if (this.mOnTrackViewScrollListener != null) {
                    clickOutSide();
                    this.mOnTrackViewScrollListener.clickOutSide();
                    this.mDragView = null;
                }
            } else {
                clickOutSide();
                this.mDragView = null;
            }
        }
        if (this.mDragView == null || !this.mLongPressEnd) {
            return false;
        }
        return true;
    }

    private void addSelectHandView(BaseItemView baseItemView) {
        removeHandView();
        HandView handView = new HandView(getContext());
        baseItemView.setHandView(handView);
        baseItemView.setPipDuringVisiableStatus(true);
        handView.setBaseUIClip(baseItemView.getBaseUIClip());
        handView.setTimeDuration(this.mTimelineDuration);
        handView.setOnHandChangeListener(this.mOnHandChangeListener);
        handView.setOnDownToGetNextClipListener(this);
        this.track_view_horizontal_layout.addView(handView);
        OnTrackViewDragListener onTrackViewDragListener = this.mOnTrackViewDragListener;
        if (onTrackViewDragListener != null) {
            onTrackViewDragListener.onSelectClip(baseItemView.getBaseUIClip());
        }
    }

    public void clickOutSide() {
        cancleAnimation();
        removeHandView();
        this.mDragView = null;
    }

    private void removeHandView() {
        setPipDuringVisiableStatus(false);
        int childCount = this.track_view_horizontal_layout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.track_view_horizontal_layout.getChildAt(i);
            if (childAt instanceof HandView) {
                this.track_view_horizontal_layout.removeView(childAt);
            }
        }
    }

    private HandView getHandView() {
        int childCount = this.track_view_horizontal_layout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.track_view_horizontal_layout.getChildAt(i);
            if (childAt instanceof HandView) {
                return (HandView) childAt;
            }
        }
        return null;
    }

    public void setData(HashMap<Integer, List<BaseUIClip>> hashMap, long j) {
        Set<Map.Entry<Integer, List<BaseUIClip>>> entrySet;
        clear();
        this.mTimelineDuration = j;
        this.mIntegerListHashMap = hashMap;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mTrackViewVerticalParent.getLayoutParams();
        layoutParams.width = PixelPerMicrosecondUtil.durationToLength(j) + ScreenUtils.getScreenWidth(this.mContext);
        this.mTrackViewVerticalParent.setLayoutParams(layoutParams);
        if (!(hashMap == null || (entrySet = hashMap.entrySet()) == null || entrySet.size() == 0)) {
            for (Map.Entry<Integer, List<BaseUIClip>> entry : entrySet) {
                List<BaseUIClip> value = entry.getValue();
                if (value != null && !value.isEmpty()) {
                    for (BaseUIClip baseUIClip : value) {
                        Logger.d(TAG, "setData: = " + baseUIClip);
                        BaseItemView baseItemView = new BaseItemView(this.mContext);
                        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, -2);
                        layoutParams2.leftMargin = getChildTopMarginFromDuration(baseUIClip.getInPoint());
                        layoutParams2.topMargin = (this.mTrackHeight * entry.getKey().intValue()) + this.mTrackViewMarginTop;
                        baseItemView.setLayoutParams(layoutParams2);
                        this.mTrackViewVerticalParent.addView(baseItemView);
                        baseItemView.setData(baseUIClip);
                    }
                }
            }
        }
    }

    public void refreshAudioSelectView(BaseUIClip baseUIClip) {
        HandView handView;
        BaseItemView baseItemView = this.mDragView;
        if (baseItemView != null && (handView = baseItemView.getHandView()) != null) {
            handView.setBaseUIClip(baseUIClip);
            refreshSelectView(baseUIClip, true);
        }
    }

    public void refreshSelectView(BaseUIClip baseUIClip, boolean z) {
        if (this.mDragView != null) {
            setPipDuringVisiableStatus(false);
            this.mDragView.refresh(baseUIClip, z);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mDragView.getLayoutParams();
            layoutParams.leftMargin = getChildTopMarginFromDuration(baseUIClip.getInPoint());
            layoutParams.topMargin = (this.mTrackHeight * baseUIClip.getTrackIndex()) + this.mTrackViewMarginTop;
            this.mDragView.setLayoutParams(layoutParams);
        }
    }

    public void clear() {
        this.mTrackViewVerticalParent.removeAllViews();
        removeHandView();
    }

    private int getChildTopMarginFromDuration(long j) {
        return this.mStartPadding + PixelPerMicrosecondUtil.durationToLength(j);
    }

    public void updateTimelineDuration(long j) {
        initWidth(j);
    }

    @Override // com.meishe.myvideo.ui.trackview.HandView.OnDownToGetNextClip
    public BaseUIClip getNextClip(BaseUIClip baseUIClip) {
        int trackIndex = baseUIClip.getTrackIndex();
        List<BaseUIClip> list = this.mIntegerListHashMap.get(Integer.valueOf(trackIndex));
        if (list != null) {
            Collections.sort(list, new Comparator<BaseUIClip>() {
                /* class com.meishe.myvideo.ui.trackview.TrackViewLayout.AnonymousClass4 */

                public int compare(BaseUIClip baseUIClip, BaseUIClip baseUIClip2) {
                    return (int) (baseUIClip.getInPoint() - baseUIClip2.getInPoint());
                }
            });
            for (int i = 0; i < list.size(); i++) {
                BaseUIClip baseUIClip2 = list.get(i);
                if (baseUIClip2.getInPoint() > baseUIClip.getInPoint()) {
                    return baseUIClip2;
                }
            }
            return null;
        }
        Log.e(TAG, "getNextClip: list is null! key is " + trackIndex);
        return null;
    }

    @Override // com.meishe.myvideo.ui.trackview.HandView.OnDownToGetNextClip
    public BaseUIClip getBeforeClip(BaseUIClip baseUIClip) {
        int trackIndex = baseUIClip.getTrackIndex();
        List<BaseUIClip> list = this.mIntegerListHashMap.get(Integer.valueOf(trackIndex));
        if (list != null) {
            Collections.sort(list, new Comparator<BaseUIClip>() {
                /* class com.meishe.myvideo.ui.trackview.TrackViewLayout.AnonymousClass5 */

                public int compare(BaseUIClip baseUIClip, BaseUIClip baseUIClip2) {
                    return (int) (baseUIClip.getInPoint() - baseUIClip2.getInPoint());
                }
            });
            for (int size = list.size() - 1; size >= 0; size--) {
                BaseUIClip baseUIClip2 = list.get(size);
                if (baseUIClip2.getInPoint() < baseUIClip.getInPoint()) {
                    return baseUIClip2;
                }
            }
            return null;
        }
        Log.e(TAG, "getNextClip: list is null! key is " + trackIndex);
        return null;
    }

    public void setOnTrackViewScrollListener(OnTrackViewScrollListener onTrackViewScrollListener) {
        this.mOnTrackViewScrollListener = onTrackViewScrollListener;
    }

    public void setOnTrackViewDragListener(OnTrackViewDragListener onTrackViewDragListener) {
        this.mOnTrackViewDragListener = onTrackViewDragListener;
    }

    public void setOnHandChangeListener(HandView.OnHandChangeListener onHandChangeListener) {
        this.mOnHandChangeListener = onHandChangeListener;
    }

    public void toMainMenu() {
        this.mAddVoice.setVisibility(0);
        setAudioView();
        clear();
    }

    private void setAudioView() {
        if (hasAudio()) {
            this.mTvAdd.setText("");
            this.imageAudio.setVisibility(0);
            this.mTvAdd.setBackgroundResource(R.mipmap.main_menu_ic_music);
            this.mTvMusicDesc.setText("音乐合集");
            return;
        }
        this.mTvAdd.setBackgroundResource(R.color.add_voice_bg_color);
        this.imageAudio.setVisibility(8);
        this.mTvAdd.setText("+");
        this.mTvMusicDesc.setText(getResources().getString(R.string.add_voice));
    }

    private boolean hasAudio() {
        List<MeicamAudioTrack> meicamAudioTrackList = TimelineData.getInstance().getMeicamAudioTrackList();
        if (meicamAudioTrackList.size() == 0) {
            return false;
        }
        for (int i = 0; i < meicamAudioTrackList.size(); i++) {
            if (meicamAudioTrackList.get(i).getClipInfoList().size() != 0) {
                return true;
            }
        }
        return false;
    }

    public void toOtherMenu() {
        this.mAddVoice.setVisibility(8);
    }

    public boolean hasDragView() {
        return this.mDragView != null;
    }

    public String getDragViewType() {
        BaseItemView baseItemView = this.mDragView;
        return (baseItemView == null || baseItemView.getBaseUIClip() == null) ? "" : this.mDragView.getBaseUIClip().getType();
    }

    public void setPipDuringVisiableStatus(boolean z) {
        BaseItemView baseItemView = this.mDragView;
        if (baseItemView != null) {
            baseItemView.setPipDuringVisiableStatus(z);
        }
    }
}
