package com.meishe.player.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class PipTransformView extends View {
    private static final int ONE_FINGER = 1;
    private static final String TAG = "PipTransformView";
    private static final int TWO_FINGER = 2;
    private double mClickMoveDistance = 0.0d;
    private PointF mDownPointF = new PointF(0.0f, 0.0f);
    private long mDownTime;
    private boolean mIsTwoFingerEvent = false;
    private PointF mMovePointF = new PointF(0.0f, 0.0f);
    private OnPipTouchEventListener mOnPipTouchEventListener;
    private float mOneFingerTargetX;
    private float mOneFingerTargetY;
    private double mTwoFingerEndLength;
    private PointF mTwoFingerOldPoint = new PointF();
    private double mTwoFingerStartLength;

    public interface OnPipTouchEventListener {
        void onDrag(PointF pointF, PointF pointF2);

        void onScaleAndRotate(float f, float f2);

        void onTouchDown(PointF pointF);

        void onTouchUp(PointF pointF);
    }

    public PipTransformView(Context context) {
        super(context);
    }

    public PipTransformView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PipTransformView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setOnPipTouchListener(OnPipTouchEventListener onPipTouchEventListener) {
        this.mOnPipTouchEventListener = onPipTouchEventListener;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int pointerCount;
        if (this.mOnPipTouchEventListener == null || (pointerCount = motionEvent.getPointerCount()) > 2) {
            return false;
        }
        if ((motionEvent.getAction() & 255) == 0 && pointerCount == 1) {
            this.mIsTwoFingerEvent = false;
        }
        if (pointerCount == 2) {
            this.mIsTwoFingerEvent = true;
            twoFingerTouch(motionEvent);
        } else {
            oneFingerTouch(motionEvent);
        }
        return true;
    }

    private void oneFingerTouch(MotionEvent motionEvent) {
        if (!this.mIsTwoFingerEvent) {
            this.mOneFingerTargetX = motionEvent.getX();
            this.mOneFingerTargetY = motionEvent.getY();
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mDownTime = System.currentTimeMillis();
                this.mDownPointF.set(this.mOneFingerTargetX, this.mOneFingerTargetY);
                this.mOnPipTouchEventListener.onTouchDown(new PointF(this.mOneFingerTargetX, this.mOneFingerTargetY));
            } else if (action == 1) {
                this.mOnPipTouchEventListener.onTouchUp(new PointF(this.mOneFingerTargetX, this.mOneFingerTargetY));
                this.mClickMoveDistance = 0.0d;
            } else if (action == 2) {
                this.mClickMoveDistance += Math.sqrt(Math.pow((double) (this.mOneFingerTargetX - this.mDownPointF.x), 2.0d) + Math.pow((double) (this.mOneFingerTargetY - this.mDownPointF.y), 2.0d));
                this.mMovePointF.set(this.mOneFingerTargetX, this.mOneFingerTargetY);
                this.mOnPipTouchEventListener.onDrag(this.mDownPointF, this.mMovePointF);
                this.mDownPointF.set(this.mOneFingerTargetX, this.mOneFingerTargetY);
            }
        } else if (motionEvent.getAction() == 1) {
            this.mIsTwoFingerEvent = false;
            this.mOnPipTouchEventListener.onTouchUp(new PointF(motionEvent.getX(), motionEvent.getY()));
        }
    }

    private void twoFingerTouch(MotionEvent motionEvent) {
        if ((motionEvent.getAction() & 255) == 5) {
            float x = motionEvent.getX(0) - motionEvent.getX(1);
            float y = motionEvent.getY(0) - motionEvent.getY(1);
            this.mTwoFingerStartLength = Math.sqrt((double) ((x * x) + (y * y)));
            this.mTwoFingerOldPoint.set(x, y);
        } else if ((motionEvent.getAction() & 255) == 2) {
            float x2 = motionEvent.getX(0) - motionEvent.getX(1);
            float y2 = motionEvent.getY(0) - motionEvent.getY(1);
            float degrees = (float) Math.toDegrees(Math.atan2((double) (motionEvent.getX(0) - motionEvent.getX(1)), (double) (motionEvent.getY(0) - motionEvent.getY(1))));
            this.mTwoFingerEndLength = Math.sqrt((double) ((x2 * x2) + (y2 * y2)));
            float f = (float) (this.mTwoFingerEndLength / this.mTwoFingerStartLength);
            float degrees2 = degrees - ((float) Math.toDegrees(Math.atan2((double) this.mTwoFingerOldPoint.x, (double) this.mTwoFingerOldPoint.y)));
            OnPipTouchEventListener onPipTouchEventListener = this.mOnPipTouchEventListener;
            if (onPipTouchEventListener != null) {
                onPipTouchEventListener.onScaleAndRotate(f, degrees2);
            }
            this.mTwoFingerStartLength = this.mTwoFingerEndLength;
            this.mTwoFingerOldPoint.set(x2, y2);
        }
    }
}
