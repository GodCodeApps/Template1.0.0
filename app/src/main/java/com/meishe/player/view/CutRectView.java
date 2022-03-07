package com.meishe.player.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class CutRectView extends View {
    private static final int ANGEL_LENGTH = 30;
    private static final int ONE_FINGER = 1;
    private static final int PADDING = 0;
    private static final int RECT_L_B = 2;
    private static final int RECT_L_T = 1;
    private static final int RECT_R_B = 4;
    private static final int RECT_R_T = 3;
    private static final String TAG = "MYCutView";
    private static final int TOUCH_RECT_SIZE = 100;
    private static final int TWO_FINGER = 2;
    private int mAngelLength = 30;
    private Paint mCornerPaint;
    private Rect mDrawRect = new Rect();
    private boolean mIsTwoFingerEvent = false;
    private float mOldTouchX = 0.0f;
    private float mOldTouchY = 0.0f;
    private OnTransformListener mOnTransformListener;
    private int mPadding = 0;
    private Paint mPaint = new Paint();
    private int mStrokeWidth = 4;
    private int mTouchRect = -1;
    private double mTwoFingerEndLength;
    private PointF mTwoFingerOldPoint = new PointF();
    private double mTwoFingerStartLength;
    private float mWidthHeightRatio = -1.0f;

    public interface OnTransformListener {
        void onScaleAndRotate(float f, float f2);

        void onTrans(float f, float f2);

        void onTransEnd(float f, float[] fArr);
    }

    private void initView() {
    }

    public CutRectView(Context context) {
        super(context);
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
        int i3 = this.mPadding;
        rect.left = i3;
        rect.top = i3;
        rect.bottom = View.MeasureSpec.getSize(i2) - this.mPadding;
        this.mDrawRect.right = View.MeasureSpec.getSize(i) - this.mPadding;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) this.mDrawRect.left, (float) this.mDrawRect.top);
        path.lineTo((float) this.mDrawRect.right, (float) this.mDrawRect.top);
        path.lineTo((float) this.mDrawRect.right, (float) this.mDrawRect.bottom);
        path.lineTo((float) this.mDrawRect.left, (float) this.mDrawRect.bottom);
        path.lineTo((float) this.mDrawRect.left, (float) this.mDrawRect.top);
        canvas.drawPath(path, this.mPaint);
        int i = this.mDrawRect.right - this.mDrawRect.left;
        int i2 = this.mDrawRect.bottom - this.mDrawRect.top;
        float f = (((float) i) * 1.0f) / 3.0f;
        path.moveTo(((float) this.mDrawRect.left) + f, (float) this.mDrawRect.top);
        path.lineTo(((float) this.mDrawRect.left) + f, (float) this.mDrawRect.bottom);
        canvas.drawPath(path, this.mPaint);
        float f2 = f * 2.0f;
        path.moveTo(((float) this.mDrawRect.left) + f2, (float) this.mDrawRect.top);
        path.lineTo(((float) this.mDrawRect.left) + f2, (float) this.mDrawRect.bottom);
        canvas.drawPath(path, this.mPaint);
        float f3 = (((float) i2) * 1.0f) / 3.0f;
        float f4 = 2.0f * f3;
        path.moveTo((float) this.mDrawRect.left, ((float) this.mDrawRect.top) + f4);
        path.lineTo((float) this.mDrawRect.right, ((float) this.mDrawRect.top) + f4);
        canvas.drawPath(path, this.mPaint);
        path.moveTo((float) this.mDrawRect.left, ((float) this.mDrawRect.top) + f3);
        path.lineTo((float) this.mDrawRect.right, ((float) this.mDrawRect.top) + f3);
        canvas.drawPath(path, this.mPaint);
        this.mAngelLength = 30;
        if (this.mAngelLength > i) {
            this.mAngelLength = i;
        }
        if (this.mAngelLength > i2) {
            this.mAngelLength = i2;
        }
        path.reset();
        path.moveTo((float) (this.mDrawRect.left + this.mAngelLength + (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + (this.mStrokeWidth / 2)));
        path.lineTo((float) (this.mDrawRect.left + (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + (this.mStrokeWidth / 2)));
        path.lineTo((float) (this.mDrawRect.left + (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + this.mAngelLength + (this.mStrokeWidth / 2)));
        canvas.drawPath(path, this.mCornerPaint);
        path.moveTo((float) ((this.mDrawRect.right - this.mAngelLength) - (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + (this.mStrokeWidth / 2)));
        path.lineTo((float) (this.mDrawRect.right - (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + (this.mStrokeWidth / 2)));
        path.lineTo((float) (this.mDrawRect.right - (this.mStrokeWidth / 2)), (float) (this.mDrawRect.top + this.mAngelLength + (this.mStrokeWidth / 2)));
        canvas.drawPath(path, this.mCornerPaint);
        path.moveTo((float) (this.mDrawRect.right - (this.mStrokeWidth / 2)), (float) ((this.mDrawRect.bottom - (this.mStrokeWidth / 2)) - this.mAngelLength));
        path.lineTo((float) (this.mDrawRect.right - (this.mStrokeWidth / 2)), (float) (this.mDrawRect.bottom - (this.mStrokeWidth / 2)));
        path.lineTo((float) ((this.mDrawRect.right - (this.mStrokeWidth / 2)) - this.mAngelLength), (float) (this.mDrawRect.bottom - (this.mStrokeWidth / 2)));
        canvas.drawPath(path, this.mCornerPaint);
        path.moveTo((float) (this.mDrawRect.left + (this.mStrokeWidth / 2)), (float) ((this.mDrawRect.bottom - (this.mStrokeWidth / 2)) - this.mAngelLength));
        path.lineTo((float) (this.mDrawRect.left + (this.mStrokeWidth / 2)), (float) (this.mDrawRect.bottom - (this.mStrokeWidth / 2)));
        path.lineTo((float) (this.mDrawRect.left + (this.mStrokeWidth / 2) + this.mAngelLength), (float) (this.mDrawRect.bottom - (this.mStrokeWidth / 2)));
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

    public void setWidthHeightRatio(float f) {
        this.mWidthHeightRatio = f;
    }

    public int getPadding() {
        return this.mPadding;
    }

    private boolean oneFingerTouch(MotionEvent motionEvent) {
        if (this.mIsTwoFingerEvent) {
            if (motionEvent.getAction() == 1) {
                this.mIsTwoFingerEvent = false;
                this.mOldTouchX = 0.0f;
                this.mOldTouchY = 0.0f;
            }
            return false;
        }
        int action = motionEvent.getAction();
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        if (action == 0) {
            this.mTouchRect = getTouchRect(motionEvent);
            return true;
        }
        if (action == 2) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int i = this.mTouchRect;
            if (i == 1) {
                this.mDrawRect.right = getWidth() - this.mPadding;
                this.mDrawRect.bottom = getHeight() - this.mPadding;
                if (this.mWidthHeightRatio > 0.0f) {
                    Rect rect = this.mDrawRect;
                    rect.left = rect.right - ((int) ((((float) (this.mDrawRect.bottom - this.mDrawRect.top)) * 1.0f) * this.mWidthHeightRatio));
                    this.mDrawRect.top = y;
                } else {
                    this.mDrawRect.left = x;
                }
                this.mDrawRect.top = y;
            } else if (i == 2) {
                Rect rect2 = this.mDrawRect;
                rect2.top = this.mPadding;
                rect2.right = getWidth() - this.mPadding;
                if (this.mWidthHeightRatio > 0.0f) {
                    Rect rect3 = this.mDrawRect;
                    rect3.left = rect3.right - ((int) ((((float) (this.mDrawRect.bottom - this.mDrawRect.top)) * 1.0f) * this.mWidthHeightRatio));
                } else {
                    this.mDrawRect.left = x;
                }
                this.mDrawRect.bottom = y;
            } else if (i == 3) {
                Rect rect4 = this.mDrawRect;
                rect4.left = this.mPadding;
                rect4.bottom = getHeight() - this.mPadding;
                if (this.mWidthHeightRatio > 0.0f) {
                    Rect rect5 = this.mDrawRect;
                    rect5.right = rect5.left + ((int) (((float) (this.mDrawRect.bottom - this.mDrawRect.top)) * 1.0f * this.mWidthHeightRatio));
                } else {
                    this.mDrawRect.right = x;
                }
                this.mDrawRect.top = y;
            } else if (i == 4) {
                Rect rect6 = this.mDrawRect;
                int i2 = this.mPadding;
                rect6.left = i2;
                rect6.top = i2;
                if (this.mWidthHeightRatio > 0.0f) {
                    rect6.right = rect6.left + ((int) (((float) (this.mDrawRect.bottom - this.mDrawRect.top)) * 1.0f * this.mWidthHeightRatio));
                } else {
                    rect6.right = x;
                }
                this.mDrawRect.bottom = y;
            }
            invalidate();
            this.mOldTouchX = rawX;
            this.mOldTouchY = rawY;
        } else if (action == 1) {
            float width = (((float) getWidth()) * 1.0f) / ((float) Math.abs(this.mDrawRect.right - this.mDrawRect.left));
            OnTransformListener onTransformListener = this.mOnTransformListener;
            if (onTransformListener != null) {
                onTransformListener.onTransEnd(width, new float[]{(float) (this.mDrawRect.right - this.mDrawRect.left), (float) (this.mDrawRect.bottom - this.mDrawRect.top)});
            }
            Rect rect7 = this.mDrawRect;
            int i3 = this.mPadding;
            rect7.left = i3;
            rect7.top = i3;
            rect7.right = getWidth() - this.mPadding;
            this.mDrawRect.bottom = getHeight() - this.mPadding;
            this.mOldTouchX = 0.0f;
            this.mOldTouchY = 0.0f;
            invalidate();
        }
        return super.onTouchEvent(motionEvent);
    }

    private boolean twoFingerTouch(MotionEvent motionEvent) {
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
            OnTransformListener onTransformListener = this.mOnTransformListener;
            if (onTransformListener != null) {
                onTransformListener.onScaleAndRotate(f, degrees2);
            }
            this.mTwoFingerStartLength = this.mTwoFingerEndLength;
            this.mTwoFingerOldPoint.set(x2, y2);
        } else if ((motionEvent.getAction() & 255) == 1 && this.mOnTransformListener != null) {
            float width = ((((float) getWidth()) * 1.0f) / ((float) this.mDrawRect.right)) - ((float) this.mDrawRect.left);
            this.mOnTransformListener.onTransEnd(width, new float[]{(float) (this.mDrawRect.right - this.mDrawRect.left), (float) (this.mDrawRect.bottom - this.mDrawRect.top)});
        }
        return super.onTouchEvent(motionEvent);
    }

    public int getTouchRect(MotionEvent motionEvent) {
        if (isInLeftTop(motionEvent)) {
            return 1;
        }
        if (isInLeftBottom(motionEvent)) {
            return 2;
        }
        if (isInRightBottom(motionEvent)) {
            return 4;
        }
        return isInRightTop(motionEvent) ? 3 : -1;
    }

    private boolean isInLeftTop(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        return rawX >= ((float) getLeft()) && rawX <= ((float) (getLeft() + 100)) && rawY >= ((float) getTop()) && rawY <= ((float) (getTop() + 100));
    }

    private boolean isInRightTop(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        return rawX >= ((float) ((getLeft() + getWidth()) + -100)) && rawX <= ((float) (getLeft() + getWidth())) && rawY >= ((float) getTop()) && rawY <= ((float) (getTop() + 100));
    }

    private boolean isInLeftBottom(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        return rawX >= ((float) getLeft()) && rawX <= ((float) (getLeft() + 100)) && rawY >= ((float) ((getTop() + getHeight()) + -100)) && rawY <= ((float) (getTop() + getHeight()));
    }

    private boolean isInRightBottom(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        return rawX >= ((float) ((getLeft() + getWidth()) + -100)) && rawX <= ((float) (getLeft() + getWidth())) && rawY >= ((float) ((getTop() + getHeight()) + -100)) && rawY <= ((float) (getTop() + getHeight()));
    }

    public void setOnTransformListener(OnTransformListener onTransformListener) {
        this.mOnTransformListener = onTransformListener;
    }
}
