package com.meishe.myvideo.view.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.meishe.common.utils.Logger;

public class MYCutView extends View {
    private static final int ANGEL_LENGTH = 30;
    private static final int ONE_FINGER = 1;
    private static final int PADDING = 10;
    private static final int RECT_L_B = 2;
    private static final int RECT_L_T = 1;
    private static final int RECT_R_B = 4;
    private static final int RECT_R_T = 3;
    private static final String TAG = "MYCutView";
    private static final int TOUCH_RECT_SIZE = 50;
    private static final int TWO_FINGER = 2;
    private int mAngelLength;
    private Paint mCornerPaint;
    private PointF mDownPointF;
    private Rect mDrawRect;
    private boolean mIsTwoFingerEvent;
    private PointF mMovePointF;
    private OnTransformListener mOnTransformListener;
    private int mPadding;
    private Paint mPaint;
    private int mStrokeWidth;
    private int mTouchRect;
    private float mTouchX;
    private float mTouchY;
    private double mTwoFingerEndLength;
    private PointF mTwoFingerOldPoint;
    private double mTwoFingerStartLength;

    public interface OnTransformListener {
        void onScaleAndRotate(float f, float f2);

        void onTrans(float f, float f2);

        void onTransEnd();
    }

    private void initView() {
    }

    public MYCutView(Context context) {
        super(context);
        this.mDrawRect = new Rect();
        this.mTouchRect = -1;
        this.mPadding = 10;
        this.mAngelLength = 30;
        this.mStrokeWidth = 4;
        this.mTouchX = 0.0f;
        this.mTouchY = 0.0f;
        this.mIsTwoFingerEvent = false;
        this.mTwoFingerOldPoint = new PointF();
        this.mDownPointF = new PointF(0.0f, 0.0f);
        this.mMovePointF = new PointF(0.0f, 0.0f);
    }

    public MYCutView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDrawRect = new Rect();
        this.mTouchRect = -1;
        this.mPadding = 10;
        this.mAngelLength = 30;
        this.mStrokeWidth = 4;
        this.mTouchX = 0.0f;
        this.mTouchY = 0.0f;
        this.mIsTwoFingerEvent = false;
        this.mTwoFingerOldPoint = new PointF();
        this.mDownPointF = new PointF(0.0f, 0.0f);
        this.mMovePointF = new PointF(0.0f, 0.0f);
        this.mPaint = new Paint();
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(2.0f);
        this.mCornerPaint = new Paint();
        this.mCornerPaint.setColor(-1);
        this.mCornerPaint.setStyle(Paint.Style.STROKE);
        this.mCornerPaint.setStrokeWidth(6.0f);
        initView();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        Rect rect = this.mDrawRect;
        rect.left = 0;
        rect.top = 0;
        rect.bottom = View.MeasureSpec.getSize(i2);
        this.mDrawRect.right = View.MeasureSpec.getSize(i);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) (this.mDrawRect.left + this.mPadding), (float) (this.mDrawRect.top + this.mPadding));
        path.lineTo((float) (this.mDrawRect.right - this.mPadding), (float) (this.mDrawRect.top + this.mPadding));
        path.lineTo((float) (this.mDrawRect.right - this.mPadding), (float) (this.mDrawRect.bottom - this.mPadding));
        path.lineTo((float) (this.mDrawRect.left + this.mPadding), (float) (this.mDrawRect.bottom - this.mPadding));
        path.lineTo((float) (this.mDrawRect.left + this.mPadding), (float) (this.mDrawRect.top + this.mPadding));
        canvas.drawPath(path, this.mPaint);
        int i = this.mDrawRect.right - this.mDrawRect.left;
        int i2 = this.mDrawRect.bottom - this.mDrawRect.top;
        path.moveTo(((float) this.mDrawRect.left) + ((((float) (i - (this.mPadding * 2))) * 1.0f) / 3.0f), (float) (this.mDrawRect.top + this.mPadding));
        path.lineTo(((float) this.mDrawRect.left) + ((((float) (i - (this.mPadding * 2))) * 1.0f) / 3.0f), (float) (this.mDrawRect.bottom - this.mPadding));
        canvas.drawPath(path, this.mPaint);
        path.moveTo(((float) this.mDrawRect.left) + (((((float) (i - (this.mPadding * 2))) * 1.0f) / 3.0f) * 2.0f), (float) (this.mDrawRect.top + this.mPadding));
        path.lineTo(((float) this.mDrawRect.left) + (((((float) (i - (this.mPadding * 2))) * 1.0f) / 3.0f) * 2.0f), (float) (this.mDrawRect.bottom - this.mPadding));
        canvas.drawPath(path, this.mPaint);
        path.moveTo((float) (this.mDrawRect.left + this.mPadding), ((float) this.mDrawRect.top) + (((((float) (i2 - (this.mPadding * 2))) * 1.0f) / 3.0f) * 2.0f));
        path.lineTo((float) (this.mDrawRect.right - this.mPadding), ((float) this.mDrawRect.top) + (((((float) (i2 - (this.mPadding * 2))) * 1.0f) / 3.0f) * 2.0f));
        canvas.drawPath(path, this.mPaint);
        path.moveTo((float) (this.mDrawRect.left + this.mPadding), ((float) this.mDrawRect.top) + ((((float) (i2 - (this.mPadding * 2))) * 1.0f) / 3.0f));
        path.lineTo((float) (this.mDrawRect.right - this.mPadding), ((float) this.mDrawRect.top) + ((((float) (i2 - (this.mPadding * 2))) * 1.0f) / 3.0f));
        canvas.drawPath(path, this.mPaint);
        path.reset();
        path.moveTo((float) (this.mDrawRect.left + this.mAngelLength + this.mPadding + (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + this.mPadding + (this.mStrokeWidth / 2)));
        path.lineTo((float) (this.mDrawRect.left + this.mPadding + (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + this.mPadding + (this.mStrokeWidth / 2)));
        path.lineTo((float) (this.mDrawRect.left + this.mPadding + (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + this.mAngelLength + this.mPadding + (this.mStrokeWidth / 2)));
        canvas.drawPath(path, this.mCornerPaint);
        path.moveTo((float) (((this.mDrawRect.right - this.mAngelLength) - this.mPadding) - (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + this.mPadding + (this.mStrokeWidth / 2)));
        path.lineTo((float) ((this.mDrawRect.right - this.mPadding) - (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + this.mPadding + (this.mStrokeWidth / 2)));
        path.lineTo((float) ((this.mDrawRect.right - this.mPadding) - (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + this.mPadding + this.mAngelLength + (this.mStrokeWidth / 2)));
        canvas.drawPath(path, this.mCornerPaint);
        path.moveTo((float) ((this.mDrawRect.right - this.mPadding) - (this.mStrokeWidth / 2)), (float) (((this.mDrawRect.bottom - this.mPadding) - (this.mStrokeWidth / 2)) - this.mAngelLength));
        path.lineTo((float) ((this.mDrawRect.right - this.mPadding) - (this.mStrokeWidth / 2)), (float) ((this.mDrawRect.bottom - this.mPadding) - (this.mStrokeWidth / 2)));
        path.lineTo((float) (((this.mDrawRect.right - this.mPadding) - (this.mStrokeWidth / 2)) - this.mAngelLength), (float) ((this.mDrawRect.bottom - this.mPadding) - (this.mStrokeWidth / 2)));
        canvas.drawPath(path, this.mCornerPaint);
        path.moveTo((float) (this.mDrawRect.left + this.mPadding + (this.mStrokeWidth / 2)), (float) (((this.mDrawRect.bottom - this.mPadding) - (this.mStrokeWidth / 2)) - this.mAngelLength));
        path.lineTo((float) (this.mDrawRect.left + this.mPadding + (this.mStrokeWidth / 2)), (float) ((this.mDrawRect.bottom - this.mPadding) - (this.mStrokeWidth / 2)));
        path.lineTo((float) (this.mDrawRect.left + this.mPadding + (this.mStrokeWidth / 2) + this.mAngelLength), (float) ((this.mDrawRect.bottom - this.mPadding) - (this.mStrokeWidth / 2)));
        canvas.drawPath(path, this.mCornerPaint);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        if (pointerCount > 2) {
            return false;
        }
        if ((motionEvent.getAction() & 255) == 0 && pointerCount == 1) {
            this.mIsTwoFingerEvent = false;
        }
        if (pointerCount != 2) {
            return oneFingerTouch(motionEvent);
        }
        this.mIsTwoFingerEvent = true;
        return twoFingerTouch(motionEvent);
    }

    private boolean oneFingerTouch(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mTouchRect = getTouchRect(motionEvent);
            if (this.mTouchRect > 0) {
                this.mTouchX = (float) ((int) motionEvent.getX());
                this.mTouchY = (float) ((int) motionEvent.getY());
            }
            return true;
        }
        if (action == 2) {
            int i = this.mTouchRect;
            if (i == 1) {
                this.mDrawRect.left = (int) motionEvent.getX();
                this.mDrawRect.top = (int) motionEvent.getY();
                this.mDrawRect.right = getWidth();
                this.mDrawRect.bottom = getHeight();
            } else if (i == 2) {
                this.mDrawRect.left = (int) motionEvent.getX();
                Rect rect = this.mDrawRect;
                rect.top = 0;
                rect.right = getWidth();
                this.mDrawRect.bottom = (int) motionEvent.getY();
            } else if (i == 3) {
                this.mDrawRect.top = (int) motionEvent.getY();
                Rect rect2 = this.mDrawRect;
                rect2.left = 0;
                rect2.right = (int) motionEvent.getX();
                this.mDrawRect.bottom = getHeight();
            } else if (i == 4) {
                Rect rect3 = this.mDrawRect;
                rect3.left = 0;
                rect3.top = 0;
                rect3.right = (int) motionEvent.getX();
                this.mDrawRect.bottom = (int) motionEvent.getY();
            }
            if (this.mTouchRect > 0) {
                invalidate();
            } else {
                OnTransformListener onTransformListener = this.mOnTransformListener;
                if (onTransformListener != null) {
                    onTransformListener.onTrans(motionEvent.getX() - this.mTouchX, motionEvent.getY() - this.mTouchY);
                }
            }
        } else if (action == 1) {
            Rect rect4 = this.mDrawRect;
            rect4.left = 0;
            rect4.top = 0;
            rect4.right = getWidth();
            this.mDrawRect.bottom = getHeight();
            invalidate();
            OnTransformListener onTransformListener2 = this.mOnTransformListener;
            if (onTransformListener2 != null) {
                onTransformListener2.onTransEnd();
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    private boolean twoFingerTouch(MotionEvent motionEvent) {
        OnTransformListener onTransformListener;
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
            OnTransformListener onTransformListener2 = this.mOnTransformListener;
            if (onTransformListener2 != null) {
                onTransformListener2.onScaleAndRotate(f, degrees2);
            }
            this.mTwoFingerStartLength = this.mTwoFingerEndLength;
            this.mTwoFingerOldPoint.set(x2, y2);
        } else if ((motionEvent.getAction() & 255) == 1 && (onTransformListener = this.mOnTransformListener) != null) {
            onTransformListener.onTransEnd();
        }
        return super.onTouchEvent(motionEvent);
    }

    private int getTouchRect(MotionEvent motionEvent) {
        if (isInLeftTop(motionEvent)) {
            Logger.d(TAG, "getTouchRect: RECT_L_T");
            return 1;
        } else if (isInLeftBottom(motionEvent)) {
            Logger.d(TAG, "getTouchRect: RECT_L_B");
            return 2;
        } else if (isInRightBottom(motionEvent)) {
            Logger.d(TAG, "getTouchRect: RECT_R_B");
            return 4;
        } else if (!isInRightTop(motionEvent)) {
            return -1;
        } else {
            Logger.d(TAG, "getTouchRect: RECT_R_T");
            return 3;
        }
    }

    private boolean isInLeftTop(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x >= 0.0f && x <= 50.0f && y >= 0.0f && y <= 50.0f;
    }

    private boolean isInRightTop(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x >= ((float) (getWidth() + -50)) && x <= ((float) getWidth()) && y >= 0.0f && y <= 50.0f;
    }

    private boolean isInLeftBottom(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x >= 0.0f && x <= 50.0f && y >= ((float) (getHeight() + -50)) && y <= ((float) getHeight());
    }

    private boolean isInRightBottom(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x >= ((float) (getWidth() + -50)) && x <= ((float) getWidth()) && y >= ((float) (getHeight() + -50)) && y <= ((float) getHeight());
    }

    public void setOnTransformListener(OnTransformListener onTransformListener) {
        this.mOnTransformListener = onTransformListener;
    }
}
