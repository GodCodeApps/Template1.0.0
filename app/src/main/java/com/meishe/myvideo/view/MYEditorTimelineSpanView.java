package com.meishe.myvideo.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meishe.engine.bean.CommonData;
import com.meishe.myvideo.ui.Utils.BorderRadiusDrawableUtil;
import com.meishe.myvideo.ui.Utils.FormatUtils;
import com.meishe.myvideo.ui.bean.BaseUIClip;
import com.meishe.myvideo.ui.bean.BaseUIVideoClip;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.text.DecimalFormat;

public class MYEditorTimelineSpanView extends RelativeLayout {
    public static final int CENTER = 23;
    private static final int HAND_LEFT = 544;
    private static final int HAND_LIMIT_BORDER = 10;
    private static final int HAND_RIGHT = 545;
    private static final int HAND_SCREEN_BORDER = 10;
    public static final int LEFT = 22;
    public static final int RIGHT = 24;
    private static final String TAG = "MYEditorTimelineSpanView";
    private static final int TIME_INTERVAL = 50;
    private BaseUIClip mBaseUIClip;
    private boolean mCanMoveHandle;
    private Context mContexgt;
    private int mDragDirection;
    private TextView mDurationText;
    private int mHandWidth;
    Handler mHandler = new Handler() {
        /* class com.meishe.myvideo.view.MYEditorTimelineSpanView.AnonymousClass1 */

        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == MYEditorTimelineSpanView.HAND_LEFT) {
                MYEditorTimelineSpanView.this.onLeftHandToScreenBorder(-10, true);
                sendEmptyMessageDelayed(MYEditorTimelineSpanView.HAND_LEFT, 50);
            } else if (i == MYEditorTimelineSpanView.HAND_RIGHT) {
                MYEditorTimelineSpanView.this.onRightHandToScreenBorder(10, true);
                sendEmptyMessageDelayed(MYEditorTimelineSpanView.HAND_RIGHT, 50);
            }
        }
    };
    private LinearLayout mLayoutEditorTimelineSpeed;
    private ImageView mLeftHandView;
    private int mLimitLengthOfHand;
    private int mMaxLeftToLeft = 0;
    private int mMaxLeftToRight = 0;
    private int mMaxRightToLeft = 0;
    private int mMaxRightToRight = 0;
    private OnHandChangeListener mOnHandChangeListener;
    private boolean mOnMiddleState;
    private float mPreviewRawX;
    private ImageView mRightHandView;
    private TextView mTvEditorTimelineSpeedText;

    public interface OnHandChangeListener {
        void onLeftHandChange(boolean z, boolean z2, int i, boolean z3, boolean z4, int i2, boolean z5);

        void onLeftHandDown(int i);

        void onLeftHandUp();

        void onNeedScrollParentLinearLayout(int i);

        void onRightHandChange(boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5);

        void onRightHandDown();

        void onRightHandUp();
    }

    public MYEditorTimelineSpanView(Context context, BaseUIClip baseUIClip) {
        super(context);
        this.mBaseUIClip = baseUIClip;
        init(context);
    }

    public MYEditorTimelineSpanView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        this.mContexgt = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.editor_timeline_span_view_layout, this);
        this.mLimitLengthOfHand = ScreenUtils.getWindowWidth(context) / 2;
        this.mLeftHandView = (ImageView) inflate.findViewById(R.id.editor_timeline_view_left_handle);
        this.mRightHandView = (ImageView) inflate.findViewById(R.id.editor_timeline_view_right_handle);
        this.mDurationText = (TextView) inflate.findViewById(R.id.tv_editor_timeline_time_text);
        this.mTvEditorTimelineSpeedText = (TextView) inflate.findViewById(R.id.tv_editor_timeline_speed_text);
        this.mDurationText.setBackground(BorderRadiusDrawableUtil.getRadiusDrawable(-1, -1, getResources().getDimensionPixelOffset(R.dimen.dp2), getResources().getColor(R.color.black_half)));
        this.mLayoutEditorTimelineSpeed = (LinearLayout) inflate.findViewById(R.id.layout_editor_timeline_speed);
        this.mLayoutEditorTimelineSpeed.setBackground(BorderRadiusDrawableUtil.getRadiusDrawable(-1, -1, getResources().getDimensionPixelOffset(R.dimen.dp2), getResources().getColor(R.color.black_half)));
        changeDurationText(getClipDurationText());
        initSpeedText();
        BaseUIClip baseUIClip = this.mBaseUIClip;
        if (baseUIClip != null && CommonData.CLIP_IMAGE.equals(baseUIClip.getType())) {
            this.mLayoutEditorTimelineSpeed.setVisibility(8);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mCanMoveHandle = false;
            this.mPreviewRawX = (float) ((int) motionEvent.getRawX());
            this.mDragDirection = getDirection((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
            if (this.mDragDirection != 23) {
                this.mCanMoveHandle = true;
                requestDisallowIntecept(true);
            }
            if (this.mOnHandChangeListener != null && this.mDragDirection == 22) {
                int[] iArr = new int[2];
                this.mLeftHandView.getLocationOnScreen(iArr);
                if (iArr[0] < 10) {
                    this.mOnHandChangeListener.onLeftHandDown(10 - iArr[0]);
                } else {
                    this.mOnHandChangeListener.onLeftHandDown(0);
                }
            }
            OnHandChangeListener onHandChangeListener = this.mOnHandChangeListener;
            if (onHandChangeListener != null && this.mDragDirection == 24) {
                onHandChangeListener.onRightHandDown();
            }
        } else if (action == 1) {
            onTouchUp();
        } else if (action == 2) {
            float rawX = motionEvent.getRawX();
            double d = (double) (rawX - this.mPreviewRawX);
            Double.isNaN(d);
            int floor = (int) Math.floor(d + 0.5d);
            this.mPreviewRawX = rawX;
            int i = this.mDragDirection;
            if (i == 22) {
                if (checkLeftHandCanMove(floor)) {
                    onLeftHandMove(floor);
                }
            } else if (i == 24 && checkRightHandCanMove(floor)) {
                onRightHandMove(floor);
            }
        }
        return this.mCanMoveHandle;
    }

    private void onTouchUp() {
        this.mHandler.removeCallbacksAndMessages(null);
        requestDisallowIntecept(false);
        OnHandChangeListener onHandChangeListener = this.mOnHandChangeListener;
        if (onHandChangeListener != null) {
            int i = this.mDragDirection;
            if (i == 22) {
                onHandChangeListener.onLeftHandUp();
            } else if (i == 24) {
                onHandChangeListener.onRightHandUp();
            }
        }
        this.mDragDirection = 23;
    }

    private void onRightHandMove(int i) {
        int realDxForRight = getRealDxForRight(i);
        int[] iArr = new int[2];
        this.mOnMiddleState = false;
        this.mRightHandView.getLocationOnScreen(iArr);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        if (realDxForRight < 0) {
            this.mHandler.removeCallbacksAndMessages(null);
        } else if (realDxForRight > 0 && this.mHandler.hasMessages(HAND_RIGHT)) {
            return;
        }
        if (iArr[0] + realDxForRight <= this.mLimitLengthOfHand) {
            this.mOnMiddleState = true;
        }
        if (iArr[0] + 10 + this.mHandWidth + realDxForRight < ScreenUtils.getScreenWidth(this.mContexgt)) {
            layoutParams.width += realDxForRight;
            setLayoutParams(layoutParams);
            OnHandChangeListener onHandChangeListener = this.mOnHandChangeListener;
            if (onHandChangeListener != null) {
                onHandChangeListener.onRightHandChange(false, false, realDxForRight, this.mOnMiddleState, false, false);
            }
        } else if (realDxForRight < 0) {
            layoutParams.width += realDxForRight;
            setLayoutParams(layoutParams);
            OnHandChangeListener onHandChangeListener2 = this.mOnHandChangeListener;
            if (onHandChangeListener2 != null) {
                onHandChangeListener2.onRightHandChange(false, false, realDxForRight, this.mOnMiddleState, false, false);
            }
        } else {
            ScreenUtils.getScreenWidth(this.mContexgt);
            int i2 = iArr[0];
            int i3 = this.mHandWidth;
            onRightHandToScreenBorder(realDxForRight, false);
            if (!this.mHandler.hasMessages(HAND_RIGHT)) {
                this.mHandler.sendEmptyMessage(HAND_RIGHT);
            }
        }
    }

    private void onLeftHandMove(int i) {
        int realDxForLeft = getRealDxForLeft(i);
        this.mOnMiddleState = false;
        if (realDxForLeft > 0) {
            this.mHandler.removeCallbacksAndMessages(null);
        } else if (realDxForLeft < 0 && this.mHandler.hasMessages(HAND_LEFT)) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        int[] iArr = new int[2];
        this.mLeftHandView.getLocationOnScreen(iArr);
        if (iArr[0] + realDxForLeft + this.mHandWidth >= this.mLimitLengthOfHand) {
            this.mOnMiddleState = true;
        }
        if (realDxForLeft > 0) {
            layoutParams.width -= realDxForLeft;
            setLayoutParams(layoutParams);
            OnHandChangeListener onHandChangeListener = this.mOnHandChangeListener;
            if (onHandChangeListener != null) {
                onHandChangeListener.onLeftHandChange(false, false, realDxForLeft, false, this.mOnMiddleState, 0, false);
            }
        } else if (realDxForLeft >= 0) {
        } else {
            if ((iArr[0] - 10) + realDxForLeft >= 0) {
                if (this.mOnMiddleState) {
                    layoutParams.width -= realDxForLeft;
                    setLayoutParams(layoutParams);
                }
                OnHandChangeListener onHandChangeListener2 = this.mOnHandChangeListener;
                if (onHandChangeListener2 != null) {
                    onHandChangeListener2.onLeftHandChange(false, false, realDxForLeft, false, this.mOnMiddleState, 0, false);
                }
            } else if (iArr[0] >= 10) {
                int i2 = (-iArr[0]) + 10;
                OnHandChangeListener onHandChangeListener3 = this.mOnHandChangeListener;
                if (onHandChangeListener3 != null) {
                    onHandChangeListener3.onLeftHandChange(false, false, i2, false, this.mOnMiddleState, 0, false);
                }
                if (!this.mHandler.hasMessages(HAND_LEFT)) {
                    this.mHandler.sendEmptyMessage(HAND_LEFT);
                }
            } else if (!this.mHandler.hasMessages(HAND_LEFT)) {
                this.mHandler.sendEmptyMessage(HAND_LEFT);
            }
        }
    }

    public void setClipPosition(int i, int i2) {
        int i3 = (i2 - i) + (this.mHandWidth * 2);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, -1);
        layoutParams.width = i3;
        layoutParams.setMargins(i, 0, 0, 0);
        setLayoutParams(layoutParams);
        double trimOut = (double) (this.mBaseUIClip.getTrimOut() - this.mBaseUIClip.getTrimIn());
        double speed = this.mBaseUIClip.getSpeed();
        Double.isNaN(trimOut);
        changeDurationText((long) (trimOut / speed));
    }

    public void changeDurationTextState(boolean z) {
        this.mDurationText.setVisibility(z ? 0 : 8);
    }

    public void changeDurationText(String str) {
        this.mDurationText.setText(str);
    }

    public void changeDurationText(long j) {
        this.mDurationText.setText(FormatUtils.durationToText(j));
    }

    public void initSpeedText() {
        BaseUIClip baseUIClip = this.mBaseUIClip;
        if (baseUIClip == null || baseUIClip.getSpeed() == 1.0d) {
            this.mLayoutEditorTimelineSpeed.setVisibility(8);
            return;
        }
        this.mLayoutEditorTimelineSpeed.setVisibility(0);
        this.mTvEditorTimelineSpeedText.setText(getClipSpeedText());
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    private void requestDisallowIntecept(boolean z) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z);
        }
    }

    public void setHandWidth(int i) {
        this.mHandWidth = i;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onLeftHandToScreenBorder(int i, boolean z) {
        if (!z || checkLeftHandCanMove(i)) {
            int realDxForLeft = getRealDxForLeft(i);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.width -= realDxForLeft;
            setLayoutParams(layoutParams);
            OnHandChangeListener onHandChangeListener = this.mOnHandChangeListener;
            if (onHandChangeListener != null) {
                onHandChangeListener.onLeftHandChange(false, false, realDxForLeft, true, false, 0, false);
                return;
            }
            return;
        }
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private int getDirection(int i, int i2) {
        int[] iArr = new int[2];
        this.mLeftHandView.getLocationOnScreen(iArr);
        int[] iArr2 = new int[2];
        this.mRightHandView.getLocationOnScreen(iArr2);
        int i3 = i - iArr[0];
        int i4 = this.mHandWidth;
        if (i3 < i4) {
            return 22;
        }
        return iArr2[0] - i < i4 ? 24 : 23;
    }

    private int getRealDxForLeft(int i) {
        int i2;
        int i3;
        if (i <= 0 || i <= (i3 = this.mMaxLeftToRight)) {
            return (i >= 0 || (-i) <= (i2 = this.mMaxLeftToLeft)) ? i : -i2;
        }
        return i3;
    }

    private boolean checkLeftHandCanMove(int i) {
        if (i > 0) {
            return this.mMaxLeftToRight > 0;
        }
        if (i >= 0 || this.mMaxLeftToLeft <= 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onRightHandToScreenBorder(int i, boolean z) {
        if (!z || checkRightHandCanMove(i)) {
            int realDxForRight = getRealDxForRight(i);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.width += realDxForRight;
            OnHandChangeListener onHandChangeListener = this.mOnHandChangeListener;
            if (onHandChangeListener != null) {
                onHandChangeListener.onNeedScrollParentLinearLayout(realDxForRight);
            }
            setLayoutParams(layoutParams);
            OnHandChangeListener onHandChangeListener2 = this.mOnHandChangeListener;
            if (onHandChangeListener2 != null) {
                onHandChangeListener2.onRightHandChange(false, false, realDxForRight, this.mOnMiddleState, true, false);
                return;
            }
            return;
        }
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private int getRealDxForRight(int i) {
        int i2;
        int i3;
        if (i <= 0 || i <= (i3 = this.mMaxRightToRight)) {
            return (i >= 0 || (-i) <= (i2 = this.mMaxRightToLeft)) ? i : -i2;
        }
        return i3;
    }

    private boolean checkRightHandCanMove(int i) {
        if (i > 0) {
            return this.mMaxRightToRight > 0;
        }
        if (i >= 0 || this.mMaxRightToLeft <= 0) {
            return false;
        }
        return true;
    }

    public void setOnHandChangeListener(OnHandChangeListener onHandChangeListener) {
        this.mOnHandChangeListener = onHandChangeListener;
    }

    public void setMaxLeftToLeft(int i) {
        this.mMaxLeftToLeft = i;
    }

    public void setMaxLeftToRight(int i) {
        this.mMaxLeftToRight = i;
    }

    public void setMaxRightToLeft(int i) {
        this.mMaxRightToLeft = i;
    }

    public void setMaxRightToRight(int i) {
        this.mMaxRightToRight = i;
    }

    public void leftOnDxChange(int i, boolean z) {
        OnHandChangeListener onHandChangeListener;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        if (z && (onHandChangeListener = this.mOnHandChangeListener) != null) {
            onHandChangeListener.onNeedScrollParentLinearLayout(-i);
        }
        layoutParams.width -= i;
        setLayoutParams(layoutParams);
    }

    public int getRightHandX() {
        int[] iArr = new int[2];
        this.mRightHandView.getLocationOnScreen(iArr);
        return iArr[0];
    }

    public int getDragDirection() {
        return this.mDragDirection;
    }

    public boolean isOnMiddleState() {
        return this.mOnMiddleState;
    }

    private String getClipDurationText() {
        BaseUIClip baseUIClip = this.mBaseUIClip;
        if (baseUIClip == null) {
            Log.e(TAG, "getClipDurationText: mBaseUIClip is null!");
            return "";
        }
        long trimOut = baseUIClip.getTrimOut() - this.mBaseUIClip.getTrimIn();
        BaseUIClip baseUIClip2 = this.mBaseUIClip;
        if (baseUIClip2 instanceof BaseUIVideoClip) {
            double d = (double) trimOut;
            double speed = ((BaseUIVideoClip) baseUIClip2).getSpeed();
            Double.isNaN(d);
            trimOut = (long) (d / speed);
        }
        return FormatUtils.durationToText(trimOut);
    }

    private String getClipSpeedText() {
        if (this.mBaseUIClip == null) {
            Log.e(TAG, "getClipDurationText: mBaseUIClip is null!");
            return "";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        return decimalFormat.format(this.mBaseUIClip.getSpeed()) + "x";
    }

    public void setBaseUIClip(BaseUIClip baseUIClip) {
        this.mBaseUIClip = baseUIClip;
    }

    public BaseUIClip getBaseUIClip() {
        return this.mBaseUIClip;
    }
}
