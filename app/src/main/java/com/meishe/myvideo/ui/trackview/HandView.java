package com.meishe.myvideo.ui.trackview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meishe.engine.bean.CommonData;
import com.meishe.myvideo.ui.bean.BaseUIClip;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;

public class HandView extends RelativeLayout {
    private static final String TAG = "HandView";
    private BaseUIClip mBaseUIClip;
    private BaseUIClip mBeforeClip = null;
    private Context mContext;
    private int mDownX;
    private int mHandHeight;
    private int mHandWidth;
    private BaseUIClip mNextClip = null;
    private OnDownToGetNextClip mOnDownToGetNextClipListener;
    private OnHandChangeListener mOnHandChangeListener;
    private int mStartPadding;
    private long mTimeDuration;
    private ImageView mTrackDragLeftHand;
    private ImageView mTrackDragRightHand;

    public interface OnDownToGetNextClip {
        BaseUIClip getBeforeClip(BaseUIClip baseUIClip);

        BaseUIClip getNextClip(BaseUIClip baseUIClip);
    }

    public interface OnHandChangeListener {
        void handUp(BaseUIClip baseUIClip);

        void leftHandChange(int i, long j, BaseUIClip baseUIClip);

        void rightHandChange(int i, long j, BaseUIClip baseUIClip);
    }

    public HandView(Context context) {
        super(context);
        init(context);
    }

    public HandView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public HandView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void init(Context context) {
        this.mContext = context;
        this.mStartPadding = ScreenUtils.getScreenWidth(context) / 2;
        this.mHandWidth = getResources().getDimensionPixelOffset(R.dimen.editor_timeline_view_hand_width);
        this.mHandHeight = getResources().getDimensionPixelOffset(R.dimen.editor_timeline_view_hand_width);
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.track_hand_view, this);
        this.mTrackDragLeftHand = (ImageView) inflate.findViewById(R.id.track_drag_left_hand);
        this.mTrackDragRightHand = (ImageView) inflate.findViewById(R.id.track_drag_right_hand);
        this.mTrackDragLeftHand.setOnTouchListener(new View.OnTouchListener() {
            /* class com.meishe.myvideo.ui.trackview.HandView.AnonymousClass1 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    HandView.this.handDown(motionEvent);
                } else if (action == 1) {
                    HandView.this.handUp();
                } else if (action == 2) {
                    int rawX = (int) motionEvent.getRawX();
                    double d = (double) (rawX - HandView.this.mDownX);
                    Double.isNaN(d);
                    int floor = (int) Math.floor(d + 0.5d);
                    HandView.this.mDownX = rawX;
                    HandView.this.leftHandleMove(floor, PixelPerMicrosecondUtil.lengthToDuration(floor));
                }
                return true;
            }
        });
        this.mTrackDragRightHand.setOnTouchListener(new View.OnTouchListener() {
            /* class com.meishe.myvideo.ui.trackview.HandView.AnonymousClass2 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    HandView.this.handDown(motionEvent);
                } else if (action == 1) {
                    HandView.this.handUp();
                } else if (action == 2) {
                    int rawX = (int) motionEvent.getRawX();
                    double d = (double) (rawX - HandView.this.mDownX);
                    Double.isNaN(d);
                    int floor = (int) Math.floor(d + 0.5d);
                    HandView.this.mDownX = rawX;
                    HandView.this.rightHandleMove(floor, PixelPerMicrosecondUtil.lengthToDuration(floor));
                }
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void handDown(MotionEvent motionEvent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        this.mDownX = (int) motionEvent.getRawX();
        OnDownToGetNextClip onDownToGetNextClip = this.mOnDownToGetNextClipListener;
        if (onDownToGetNextClip != null) {
            this.mNextClip = onDownToGetNextClip.getNextClip(this.mBaseUIClip);
            this.mBeforeClip = this.mOnDownToGetNextClipListener.getBeforeClip(this.mBaseUIClip);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void handUp() {
        getParent().requestDisallowInterceptTouchEvent(false);
        this.mNextClip = null;
        this.mOnHandChangeListener.handUp(this.mBaseUIClip);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void leftHandleMove(int i, long j) {
        double d;
        double speed;
        double d2;
        double trimIn = (double) this.mBaseUIClip.getTrimIn();
        double d3 = (double) j;
        double speed2 = this.mBaseUIClip.getSpeed();
        Double.isNaN(d3);
        Double.isNaN(trimIn);
        long j2 = (long) (trimIn + (speed2 * d3));
        double inPoint = (double) this.mBaseUIClip.getInPoint();
        double speed3 = this.mBaseUIClip.getSpeed();
        Double.isNaN(d3);
        Double.isNaN(inPoint);
        long j3 = (long) (inPoint + (d3 * speed3));
        long trimOut = this.mBaseUIClip.getTrimOut();
        if (j2 <= 0) {
            d = (double) (0 - this.mBaseUIClip.getTrimIn());
            speed = this.mBaseUIClip.getSpeed();
            Double.isNaN(d);
        } else {
            long j4 = trimOut - CommonData.MIN_SHOW_LENGTH_DURATION;
            if (j2 > j4) {
                d = (double) (j4 - this.mBaseUIClip.getTrimIn());
                speed = this.mBaseUIClip.getSpeed();
                Double.isNaN(d);
            } else {
                BaseUIClip baseUIClip = this.mBeforeClip;
                if (baseUIClip == null || j3 >= getClipOutPoint(baseUIClip)) {
                    if (j3 < 0) {
                        d = (double) (-this.mBaseUIClip.getInPoint());
                        speed = this.mBaseUIClip.getSpeed();
                        Double.isNaN(d);
                    }
                    int durationToLength = PixelPerMicrosecondUtil.durationToLength(j);
                    double trimIn2 = (double) this.mBaseUIClip.getTrimIn();
                    double d4 = (double) j;
                    double speed4 = this.mBaseUIClip.getSpeed();
                    Double.isNaN(d4);
                    Double.isNaN(trimIn2);
                    this.mBaseUIClip.setTrimIn((long) (trimIn2 + (speed4 * d4)));
                    BaseUIClip baseUIClip2 = this.mBaseUIClip;
                    double inPoint2 = (double) baseUIClip2.getInPoint();
                    double speed5 = this.mBaseUIClip.getSpeed();
                    Double.isNaN(d4);
                    Double.isNaN(inPoint2);
                    baseUIClip2.setInPoint((long) (inPoint2 + (d4 * speed5)));
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                    layoutParams.leftMargin += durationToLength;
                    layoutParams.width -= durationToLength;
                    setLayoutParams(layoutParams);
                    handChangeNvsValue(this.mBaseUIClip, true);
                    this.mOnHandChangeListener.leftHandChange(durationToLength, j, this.mBaseUIClip);
                }
                double clipOutPoint = (double) getClipOutPoint(this.mBeforeClip);
                double inPoint3 = (double) this.mBaseUIClip.getInPoint();
                double speed6 = this.mBaseUIClip.getSpeed();
                Double.isNaN(inPoint3);
                Double.isNaN(clipOutPoint);
                d2 = clipOutPoint - (inPoint3 / speed6);
                j = (long) d2;
                int durationToLength2 = PixelPerMicrosecondUtil.durationToLength(j);
                double trimIn22 = (double) this.mBaseUIClip.getTrimIn();
                double d42 = (double) j;
                double speed42 = this.mBaseUIClip.getSpeed();
                Double.isNaN(d42);
                Double.isNaN(trimIn22);
                this.mBaseUIClip.setTrimIn((long) (trimIn22 + (speed42 * d42)));
                BaseUIClip baseUIClip22 = this.mBaseUIClip;
                double inPoint22 = (double) baseUIClip22.getInPoint();
                double speed52 = this.mBaseUIClip.getSpeed();
                Double.isNaN(d42);
                Double.isNaN(inPoint22);
                baseUIClip22.setInPoint((long) (inPoint22 + (d42 * speed52)));
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) getLayoutParams();
                layoutParams2.leftMargin += durationToLength2;
                layoutParams2.width -= durationToLength2;
                setLayoutParams(layoutParams2);
                handChangeNvsValue(this.mBaseUIClip, true);
                this.mOnHandChangeListener.leftHandChange(durationToLength2, j, this.mBaseUIClip);
            }
        }
        d2 = d / speed;
        j = (long) d2;
        int durationToLength22 = PixelPerMicrosecondUtil.durationToLength(j);
        double trimIn222 = (double) this.mBaseUIClip.getTrimIn();
        double d422 = (double) j;
        double speed422 = this.mBaseUIClip.getSpeed();
        Double.isNaN(d422);
        Double.isNaN(trimIn222);
        this.mBaseUIClip.setTrimIn((long) (trimIn222 + (speed422 * d422)));
        BaseUIClip baseUIClip222 = this.mBaseUIClip;
        double inPoint222 = (double) baseUIClip222.getInPoint();
        double speed522 = this.mBaseUIClip.getSpeed();
        Double.isNaN(d422);
        Double.isNaN(inPoint222);
        baseUIClip222.setInPoint((long) (inPoint222 + (d422 * speed522)));
        RelativeLayout.LayoutParams layoutParams22 = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams22.leftMargin += durationToLength22;
        layoutParams22.width -= durationToLength22;
        setLayoutParams(layoutParams22);
        handChangeNvsValue(this.mBaseUIClip, true);
        this.mOnHandChangeListener.leftHandChange(durationToLength22, j, this.mBaseUIClip);
    }

    private long getClipOutPoint(BaseUIClip baseUIClip) {
        double inPoint = (double) baseUIClip.getInPoint();
        double trimOut = (double) (baseUIClip.getTrimOut() - baseUIClip.getTrimIn());
        double speed = baseUIClip.getSpeed();
        Double.isNaN(trimOut);
        Double.isNaN(inPoint);
        return (long) (inPoint + (trimOut / speed));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void rightHandleMove(int i, long j) {
        long j2;
        double inPoint;
        double speed;
        double d;
        double trimOut = (double) this.mBaseUIClip.getTrimOut();
        double d2 = (double) j;
        double speed2 = this.mBaseUIClip.getSpeed();
        Double.isNaN(d2);
        Double.isNaN(trimOut);
        long j3 = (long) (trimOut + (d2 * speed2));
        long trimIn = this.mBaseUIClip.getTrimIn();
        double trimOut2 = (double) this.mBaseUIClip.getTrimOut();
        double trimIn2 = (double) this.mBaseUIClip.getTrimIn();
        double speed3 = this.mBaseUIClip.getSpeed();
        Double.isNaN(trimIn2);
        Double.isNaN(trimOut2);
        long j4 = (long) (trimOut2 - (trimIn2 / speed3));
        double d3 = (double) j3;
        double trimIn3 = (double) this.mBaseUIClip.getTrimIn();
        double speed4 = this.mBaseUIClip.getSpeed();
        Double.isNaN(trimIn3);
        Double.isNaN(d3);
        long j5 = (long) (d3 - (trimIn3 / speed4));
        if (this.mBaseUIClip.getType().equals(CommonData.CLIP_VIDEO) || this.mBaseUIClip.getType().equals(CommonData.CLIP_AUDIO)) {
            j2 = NvsStreamingContext.getInstance().getAVFileInfo(this.mBaseUIClip.getFilePath()).getDuration();
        } else {
            j2 = 7200000000L;
        }
        if (j3 > j2) {
            inPoint = (double) (j2 - this.mBaseUIClip.getTrimOut());
            speed = this.mBaseUIClip.getSpeed();
            Double.isNaN(inPoint);
        } else if (j3 - trimIn < CommonData.MIN_SHOW_LENGTH_DURATION) {
            double trimOut3 = (double) ((this.mBaseUIClip.getTrimOut() - trimIn) - CommonData.MIN_SHOW_LENGTH_DURATION);
            double speed5 = this.mBaseUIClip.getSpeed();
            Double.isNaN(trimOut3);
            d = -(trimOut3 / speed5);
            j = (long) d;
            int durationToLength = PixelPerMicrosecondUtil.durationToLength(j);
            double trimOut4 = (double) this.mBaseUIClip.getTrimOut();
            double d4 = (double) j;
            double speed6 = this.mBaseUIClip.getSpeed();
            Double.isNaN(d4);
            Double.isNaN(trimOut4);
            this.mBaseUIClip.setTrimOut((long) (trimOut4 + (d4 * speed6)));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.width += durationToLength;
            setLayoutParams(layoutParams);
            handChangeNvsValue(this.mBaseUIClip, false);
            this.mOnHandChangeListener.rightHandChange(durationToLength, j, this.mBaseUIClip);
        } else if (this.mNextClip == null || this.mBaseUIClip.getInPoint() + j5 <= this.mNextClip.getInPoint()) {
            if (this.mTimeDuration < j5 + this.mBaseUIClip.getInPoint()) {
                inPoint = (double) ((this.mTimeDuration - this.mBaseUIClip.getInPoint()) - j4);
                speed = this.mBaseUIClip.getSpeed();
                Double.isNaN(inPoint);
            }
            int durationToLength2 = PixelPerMicrosecondUtil.durationToLength(j);
            double trimOut42 = (double) this.mBaseUIClip.getTrimOut();
            double d42 = (double) j;
            double speed62 = this.mBaseUIClip.getSpeed();
            Double.isNaN(d42);
            Double.isNaN(trimOut42);
            this.mBaseUIClip.setTrimOut((long) (trimOut42 + (d42 * speed62)));
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams2.width += durationToLength2;
            setLayoutParams(layoutParams2);
            handChangeNvsValue(this.mBaseUIClip, false);
            this.mOnHandChangeListener.rightHandChange(durationToLength2, j, this.mBaseUIClip);
        } else {
            inPoint = (double) ((this.mNextClip.getInPoint() - this.mBaseUIClip.getInPoint()) - j4);
            speed = this.mBaseUIClip.getSpeed();
            Double.isNaN(inPoint);
        }
        d = inPoint / speed;
        j = (long) d;
        int durationToLength22 = PixelPerMicrosecondUtil.durationToLength(j);
        double trimOut422 = (double) this.mBaseUIClip.getTrimOut();
        double d422 = (double) j;
        double speed622 = this.mBaseUIClip.getSpeed();
        Double.isNaN(d422);
        Double.isNaN(trimOut422);
        this.mBaseUIClip.setTrimOut((long) (trimOut422 + (d422 * speed622)));
        RelativeLayout.LayoutParams layoutParams22 = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams22.width += durationToLength22;
        setLayoutParams(layoutParams22);
        handChangeNvsValue(this.mBaseUIClip, false);
        this.mOnHandChangeListener.rightHandChange(durationToLength22, j, this.mBaseUIClip);
    }

    private void handChangeNvsValue(BaseUIClip baseUIClip, boolean z) {
        long j;
        if (z) {
            j = baseUIClip.getInPoint();
        } else {
            double inPoint = (double) baseUIClip.getInPoint();
            double trimOut = (double) (baseUIClip.getTrimOut() - baseUIClip.getTrimIn());
            double speed = baseUIClip.getSpeed();
            Double.isNaN(trimOut);
            Double.isNaN(inPoint);
            j = (long) (inPoint + (trimOut / speed));
        }
        if (CommonData.CLIP_CAPTION.equals(baseUIClip.getType())) {
            NvsTimelineCaption nvsTimelineCaption = (NvsTimelineCaption) baseUIClip.getNvsObject();
            if (z) {
                nvsTimelineCaption.changeInPoint(j);
            } else {
                nvsTimelineCaption.changeOutPoint(j);
            }
        } else if (CommonData.CLIP_COMPOUND_CAPTION.equals(baseUIClip.getType())) {
            NvsTimelineCompoundCaption nvsTimelineCompoundCaption = (NvsTimelineCompoundCaption) baseUIClip.getNvsObject();
            if (z) {
                nvsTimelineCompoundCaption.changeInPoint(j);
            } else {
                nvsTimelineCompoundCaption.changeOutPoint(j);
            }
        } else if (CommonData.CLIP_STICKER.equals(baseUIClip.getType())) {
            NvsTimelineAnimatedSticker nvsTimelineAnimatedSticker = (NvsTimelineAnimatedSticker) baseUIClip.getNvsObject();
            if (z) {
                nvsTimelineAnimatedSticker.changeInPoint(j);
            } else {
                nvsTimelineAnimatedSticker.changeOutPoint(j);
            }
        } else if (CommonData.CLIP_TIMELINE_FX.equals(baseUIClip.getType())) {
            NvsTimelineVideoFx nvsTimelineVideoFx = (NvsTimelineVideoFx) baseUIClip.getNvsObject();
            if (z) {
                nvsTimelineVideoFx.changeInPoint(j);
            } else {
                nvsTimelineVideoFx.changeOutPoint(j);
            }
        } else if (CommonData.CLIP_VIDEO.equals(baseUIClip.getType())) {
            NvsVideoClip nvsVideoClip = (NvsVideoClip) baseUIClip.getNvsObject();
            if (z) {
                nvsVideoClip.changeTrimInPoint(baseUIClip.getTrimIn(), false);
            } else {
                nvsVideoClip.changeTrimOutPoint(baseUIClip.getTrimOut(), false);
            }
        } else if (CommonData.CLIP_IMAGE.equals(baseUIClip.getType())) {
            NvsVideoClip nvsVideoClip2 = (NvsVideoClip) baseUIClip.getNvsObject();
            if (z) {
                nvsVideoClip2.changeTrimInPoint(baseUIClip.getTrimIn(), false);
            } else {
                nvsVideoClip2.changeTrimOutPoint(baseUIClip.getTrimOut(), false);
            }
        } else if (CommonData.CLIP_AUDIO.equals(baseUIClip.getType())) {
            NvsAudioClip nvsAudioClip = (NvsAudioClip) baseUIClip.getNvsObject();
            if (z) {
                nvsAudioClip.changeTrimInPoint(baseUIClip.getTrimIn(), false);
            } else {
                nvsAudioClip.changeTrimOutPoint(baseUIClip.getTrimOut(), false);
            }
        }
    }

    public BaseUIClip getBaseUIClip() {
        return this.mBaseUIClip;
    }

    public void setBaseUIClip(BaseUIClip baseUIClip) {
        this.mBaseUIClip = baseUIClip;
        refreshViewPosition();
    }

    public void setTimeDuration(long j) {
        this.mTimeDuration = j;
    }

    private void refreshViewPosition() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, getResources().getDimensionPixelOffset(R.dimen.track_view_height));
        layoutParams.leftMargin = getChildTopMarginFromDuration(this.mBaseUIClip.getInPoint());
        double trimOut = (double) (this.mBaseUIClip.getTrimOut() - this.mBaseUIClip.getTrimIn());
        double speed = this.mBaseUIClip.getSpeed();
        Double.isNaN(trimOut);
        layoutParams.width = PixelPerMicrosecondUtil.durationToLength((long) (trimOut / speed)) + (this.mHandWidth * 2);
        layoutParams.topMargin = (getResources().getDimensionPixelOffset(R.dimen.track_view_height) * this.mBaseUIClip.getTrackIndex()) + getResources().getDimensionPixelOffset(R.dimen.track_view_real_margin_top);
        setLayoutParams(layoutParams);
    }

    private int getChildTopMarginFromDuration(long j) {
        return (this.mStartPadding + PixelPerMicrosecondUtil.durationToLength(j)) - getResources().getDimensionPixelOffset(R.dimen.editor_timeline_view_hand_width);
    }

    public void setOnHandChangeListener(OnHandChangeListener onHandChangeListener) {
        this.mOnHandChangeListener = onHandChangeListener;
    }

    public boolean isHandDownOnHandView(float f, float f2) {
        return isTouchPointInView(this.mTrackDragLeftHand, f, f2) || isTouchPointInView(this.mTrackDragRightHand, f, f2);
    }

    public boolean isTouchPointInView(View view, float f, float f2) {
        if (view == null) {
            return false;
        }
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        int measuredWidth = view.getMeasuredWidth() + i;
        int measuredHeight = view.getMeasuredHeight() + i2;
        if (f2 < ((float) i2) || f2 > ((float) measuredHeight) || f < ((float) i) || f > ((float) measuredWidth)) {
            return false;
        }
        return true;
    }

    public int getHandWidth() {
        return this.mHandWidth;
    }

    public int getHandHeight() {
        return this.mHandHeight;
    }

    public void setOnDownToGetNextClipListener(OnDownToGetNextClip onDownToGetNextClip) {
        this.mOnDownToGetNextClipListener = onDownToGetNextClip;
    }
}
