package com.meishe.myvideo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meishe.common.utils.TimeFormatUtil;
import com.meishe.myvideoapp.R;

public class CutMusicView extends RelativeLayout {
    private final String TAG = "CutMusicView";
    private final int TOUCH_CENTER = 2;
    private final int TOUCH_LEFT = 1;
    private final int TOUCH_RIGHT = 3;
    private TextView leftTime;
    private int leftToRight = 0;
    private View mBaseLineView;
    private View mBaseLineViewTop;
    private boolean mCanTouchCenter = true;
    private Context mContext;
    private int mCurrentTouch = -1;
    private int mDurationWidth = 0;
    private RelativeLayout mHandleLayout;
    private int mHandleWidth = 0;
    private long mInPoint = 0;
    private View mIndicatorView;
    private int mIndicatorWidth;
    private ImageView mLeftHandle;
    private int mLeftHandleWidth;
    private OnSeekBarChanged mListener;
    private RelativeLayout mMainLayout;
    private long mMaxDuration = 0;
    private long mMinDuration = 0;
    private int mMinSpan = 0;
    private long mOutPoint = 0;
    private int mPerSecondWidth = 0;
    private ImageView mRightHandle;
    private int mRightHandleWidth;
    private int mTotalWidth = 0;
    private TextView mTvMusicTimeFllow;
    private TextView mTvSelectMusicTime;
    private int m_touchWidth = 20;
    private int originLeft = 0;
    private int originRight = 0;
    private int prevRawX = 0;
    private RelativeLayout realFollowTime;
    private TextView rightTime;
    private TextView tvFollowLeft;
    private TextView tvFollowRight;

    public interface OnSeekBarChanged {
        void onCenterTouched(long j, long j2);

        void onLeftValueChange(long j);

        void onRightValueChange(long j);

        void onUpTouched(boolean z, long j, long j2);
    }

    public CutMusicView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public CutMusicView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public CutMusicView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.cut_music_view, this);
        this.mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        this.mHandleLayout = (RelativeLayout) findViewById(R.id.handle_layout);
        this.realFollowTime = (RelativeLayout) findViewById(R.id.real_follow_time);
        this.mLeftHandle = (ImageView) findViewById(R.id.leftHandle);
        this.mRightHandle = (ImageView) findViewById(R.id.rightHandle);
        this.leftTime = (TextView) findViewById(R.id.leftTime);
        this.rightTime = (TextView) findViewById(R.id.rightTime);
        this.mIndicatorView = findViewById(R.id.indicator_view);
        this.mLeftHandleWidth = this.mLeftHandle.getLayoutParams().width;
        this.mRightHandleWidth = this.mRightHandle.getLayoutParams().width;
        this.mHandleWidth = this.mLeftHandleWidth + this.mRightHandleWidth;
        this.mIndicatorWidth = this.mIndicatorView.getLayoutParams().width;
        this.mBaseLineView = findViewById(R.id.view_base_line);
        this.mBaseLineViewTop = findViewById(R.id.view_base_line_top);
        this.mTvMusicTimeFllow = (TextView) findViewById(R.id.tv_music_time_follow);
        this.mTvSelectMusicTime = (TextView) findViewById(R.id.select_music_time);
        this.tvFollowLeft = (TextView) findViewById(R.id.tv_follow_left);
        this.tvFollowRight = (TextView) findViewById(R.id.tv_follow_right);
    }

    public void setCutLayoutWidth(int i) {
        this.mTotalWidth = i;
        this.mDurationWidth = this.mTotalWidth - this.mHandleWidth;
    }

    public void setMinDuration(long j) {
        this.mMinDuration = j;
    }

    public long getMinDuration() {
        return this.mMinDuration;
    }

    public void setMaxDuration(long j) {
        this.mMaxDuration = j;
        long j2 = this.mMaxDuration;
        this.mOutPoint = j2;
        this.mPerSecondWidth = (int) ((double) ((1000000 / j2) * ((long) this.mDurationWidth)));
    }

    public long getInPoint() {
        return this.mInPoint;
    }

    public void setInPoint(long j) {
        this.mInPoint = j;
    }

    public long getOutPoint() {
        return this.mOutPoint;
    }

    public void setOutPoint(long j) {
        this.mOutPoint = j;
    }

    public void reLayout() {
        this.tvFollowLeft.setVisibility(8);
        this.tvFollowRight.setVisibility(8);
        this.originRight = this.mTotalWidth;
        this.originLeft = 0;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mHandleLayout.getLayoutParams();
        int i = this.originRight;
        int i2 = this.originLeft;
        layoutParams.width = i - i2;
        layoutParams.setMargins(i2, 0, this.mTotalWidth - i, 0);
        this.mHandleLayout.setLayoutParams(layoutParams);
    }

    public void setIndicator(long j) {
        double d = (double) j;
        double d2 = (double) this.mMaxDuration;
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = d / d2;
        double d4 = (double) this.mDurationWidth;
        Double.isNaN(d4);
        double d5 = d3 * d4;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mIndicatorView.getLayoutParams();
        layoutParams.width = this.mIndicatorWidth;
        int i = (int) d5;
        layoutParams.setMargins(this.mLeftHandleWidth + i, 0, 0, 0);
        this.mIndicatorView.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams layoutParams2 = this.mBaseLineViewTop.getLayoutParams();
        layoutParams2.width = this.mLeftHandleWidth + i;
        this.mBaseLineViewTop.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.mTvSelectMusicTime.getLayoutParams();
        layoutParams3.setMargins(i + this.mLeftHandleWidth, 0, 0, 0);
        this.mTvSelectMusicTime.setLayoutParams(layoutParams3);
        this.mTvSelectMusicTime.setText(TimeFormatUtil.formatUsToString2(j));
    }

    public void setCanTouchCenterMove(boolean z) {
        this.mCanTouchCenter = z;
    }

    public void setOnSeekBarChangedListener(OnSeekBarChanged onSeekBarChanged) {
        this.mListener = onSeekBarChanged;
    }

    public void setRightHandleVisiable(boolean z) {
        if (z) {
            this.mRightHandle.setVisibility(0);
            this.mRightHandleWidth = this.mRightHandle.getLayoutParams().width;
        } else {
            this.mRightHandle.setVisibility(4);
            this.mRightHandleWidth = 0;
        }
        this.mHandleWidth = this.mLeftHandleWidth + this.mRightHandleWidth;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Log.e("===>", "x: " + x + "  y: " + y);
        if (!TimeFormatUtil.formatUsToString2(this.mMinDuration).equals(TimeFormatUtil.formatUsToString2(this.mMaxDuration)) && this.mMinDuration < this.mMaxDuration) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.originLeft = this.mHandleLayout.getLeft();
                this.originRight = this.mHandleLayout.getRight();
                this.prevRawX = (int) motionEvent.getRawX();
                this.mCurrentTouch = getTouchMode(x, y);
            } else if (action == 1) {
                OnSeekBarChanged onSeekBarChanged = this.mListener;
                if (onSeekBarChanged != null) {
                    if (this.mCurrentTouch == 1) {
                        onSeekBarChanged.onUpTouched(true, this.mInPoint, this.mOutPoint);
                    } else {
                        onSeekBarChanged.onUpTouched(false, this.mInPoint, this.mOutPoint);
                    }
                }
            } else if (action == 2) {
                int rawX = (int) motionEvent.getRawX();
                int i = rawX - this.prevRawX;
                this.prevRawX = rawX;
                int i2 = this.mCurrentTouch;
                if (i2 == 1) {
                    this.tvFollowLeft.setVisibility(0);
                    left(i);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mHandleLayout.getLayoutParams();
                    int i3 = this.originRight;
                    int i4 = this.originLeft;
                    layoutParams.width = i3 - i4;
                    layoutParams.setMargins(i4, 0, this.mTotalWidth - i3, 0);
                    this.mHandleLayout.setLayoutParams(layoutParams);
                    double d = (double) ((((float) this.originLeft) / ((float) this.mDurationWidth)) * ((float) this.mMaxDuration));
                    Double.isNaN(d);
                    this.mInPoint = (long) Math.floor(d + 0.5d);
                    if (this.mListener != null) {
                        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.realFollowTime.getLayoutParams();
                        int i5 = this.originRight;
                        int i6 = this.originLeft;
                        layoutParams2.width = i5 - i6;
                        layoutParams2.setMargins(i6, 0, this.mTotalWidth - i5, 0);
                        this.realFollowTime.setLayoutParams(layoutParams2);
                        this.tvFollowLeft.setText(TimeFormatUtil.formatUsToString2(this.mInPoint));
                        this.mListener.onLeftValueChange(this.mInPoint);
                    }
                } else if (i2 == 3) {
                    this.tvFollowRight.setVisibility(0);
                    right(i);
                    RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.mHandleLayout.getLayoutParams();
                    int i7 = this.originRight;
                    int i8 = this.originLeft;
                    layoutParams3.width = i7 - i8;
                    layoutParams3.setMargins(i8, 0, this.mTotalWidth - i7, 0);
                    this.mHandleLayout.setLayoutParams(layoutParams3);
                    double d2 = (double) ((((float) (this.originRight - this.mHandleWidth)) / ((float) this.mDurationWidth)) * ((float) this.mMaxDuration));
                    Double.isNaN(d2);
                    this.mOutPoint = (long) Math.floor(d2 + 0.5d);
                    if (this.mListener != null) {
                        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) this.realFollowTime.getLayoutParams();
                        int i9 = this.originRight;
                        int i10 = this.originLeft;
                        layoutParams4.width = i9 - i10;
                        layoutParams4.setMargins(i10, 0, this.mTotalWidth - i9, 0);
                        this.realFollowTime.setLayoutParams(layoutParams4);
                        this.tvFollowRight.setText(TimeFormatUtil.formatUsToString2(this.mOutPoint));
                        this.mListener.onRightValueChange(this.mOutPoint);
                    }
                } else if (i2 == 2 && this.mCanTouchCenter) {
                    this.leftToRight = this.mMainLayout.getWidth();
                    center(i);
                    RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams) this.mHandleLayout.getLayoutParams();
                    layoutParams5.setMargins(this.originLeft, 0, this.mTotalWidth - this.originRight, 0);
                    this.mHandleLayout.setLayoutParams(layoutParams5);
                    double d3 = (double) ((((float) this.originLeft) / ((float) this.mDurationWidth)) * ((float) this.mMaxDuration));
                    Double.isNaN(d3);
                    this.mInPoint = (long) Math.floor(d3 + 0.5d);
                    double d4 = (double) ((((float) (this.originRight - this.mHandleWidth)) / ((float) this.mDurationWidth)) * ((float) this.mMaxDuration));
                    Double.isNaN(d4);
                    this.mOutPoint = (long) Math.floor(d4 + 0.5d);
                    OnSeekBarChanged onSeekBarChanged2 = this.mListener;
                    if (onSeekBarChanged2 != null) {
                        onSeekBarChanged2.onCenterTouched(this.mInPoint, this.mOutPoint);
                    }
                }
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private int getTouchMode(float f, float f2) {
        int left = this.mHandleLayout.getLeft();
        int right = this.mHandleLayout.getRight();
        Log.e("===>", "left: " + left + " right: " + right + " x: " + f + " handle_width: " + this.mHandleWidth);
        float f3 = f - ((float) left);
        if (f3 < ((float) (this.mLeftHandleWidth + this.m_touchWidth)) && f3 > 0.0f) {
            return 1;
        }
        float f4 = ((float) right) - f;
        return (f4 >= ((float) (this.mRightHandleWidth + this.m_touchWidth)) || f4 <= 0.0f) ? 2 : 3;
    }

    private void right(int i) {
        double d = (double) ((((float) this.mMinDuration) / ((float) this.mMaxDuration)) * ((float) this.mDurationWidth));
        Double.isNaN(d);
        this.mMinSpan = (int) Math.floor(d + 0.5d);
        this.originRight += i;
        int i2 = this.originRight;
        int i3 = this.mTotalWidth;
        if (i2 > i3) {
            this.originRight = i3;
        }
        int i4 = this.originRight;
        int i5 = this.originLeft;
        int i6 = this.mHandleWidth;
        int i7 = (i4 - i5) - i6;
        int i8 = this.mMinSpan;
        if (i7 < i8) {
            this.originRight = i5 + i8 + i6;
        }
    }

    private void left(int i) {
        double d = (double) ((((float) this.mMinDuration) / ((float) this.mMaxDuration)) * ((float) this.mDurationWidth));
        Double.isNaN(d);
        this.mMinSpan = (int) Math.floor(d + 0.5d);
        this.originLeft += i;
        if (this.originLeft < 0) {
            this.originLeft = 0;
        }
        int i2 = this.originRight;
        int i3 = this.mHandleWidth;
        int i4 = (i2 - this.originLeft) - i3;
        int i5 = this.mMinSpan;
        if (i4 < i5) {
            this.originLeft = (i2 - i3) - i5;
        }
    }

    private void center(int i) {
        this.originLeft += i;
        this.originRight += i;
        if (this.originLeft <= 0) {
            this.originLeft = 0;
            this.originRight = this.originLeft + this.leftToRight;
        }
        int i2 = this.originRight;
        int i3 = this.mTotalWidth;
        if (i2 > i3) {
            this.originRight = i3;
            this.originLeft = this.originRight - this.leftToRight;
        }
    }
}
