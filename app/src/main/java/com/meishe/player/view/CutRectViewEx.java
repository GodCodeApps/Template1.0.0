package com.meishe.player.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CutRectViewEx extends View {
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
    private int mRectHeight;
    private int mRectWidth;
    private int mStrokeWidth = 4;
    private int mTouchRect = -1;
    private double mTwoFingerEndLength;
    private PointF mTwoFingerOldPoint = new PointF();
    private double mTwoFingerStartLength;
    private float mWidthHeightRatio = -1.0f;

    public interface OnTransformListener {
        void onRectMoved(float f, Point point, Point point2);

        void onScaleAndRotate(float f, float f2);

        void onTrans(float f, float f2);

        void onTransEnd(float f, float[] fArr);
    }

    private void initView() {
    }

    public CutRectViewEx(Context context) {
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

    public CutRectViewEx(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
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
            this.mOldTouchX = motionEvent.getRawX();
            this.mOldTouchY = motionEvent.getRawY();
            return true;
        }
        if (action == 2) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int i = this.mTouchRect;
            if (i == 1) {
                this.mDrawRect.right = (getWidth() - ((getWidth() - getRectWidth()) / 2)) - this.mPadding;
                this.mDrawRect.bottom = (getHeight() - ((getHeight() - getRectHeight()) / 2)) - this.mPadding;
                if (this.mWidthHeightRatio > 0.0f) {
                    Rect rect = this.mDrawRect;
                    rect.left = rect.right - ((int) ((((float) (this.mDrawRect.bottom - this.mDrawRect.top)) * 1.0f) * this.mWidthHeightRatio));
                    this.mDrawRect.top = y;
                } else {
                    this.mDrawRect.left = x;
                }
                this.mDrawRect.top = y;
                correctRect();
            } else if (i == 2) {
                this.mDrawRect.top = ((getHeight() - getRectHeight()) / 2) + this.mPadding;
                this.mDrawRect.right = (getWidth() - ((getWidth() - getRectWidth()) / 2)) - this.mPadding;
                if (this.mWidthHeightRatio > 0.0f) {
                    Rect rect2 = this.mDrawRect;
                    rect2.left = rect2.right - ((int) ((((float) (this.mDrawRect.bottom - this.mDrawRect.top)) * 1.0f) * this.mWidthHeightRatio));
                } else {
                    this.mDrawRect.left = x;
                }
                this.mDrawRect.bottom = y;
                correctRect();
            } else if (i == 3) {
                this.mDrawRect.left = ((getWidth() - getRectWidth()) / 2) + this.mPadding;
                this.mDrawRect.bottom = (getHeight() - ((getHeight() - getRectHeight()) / 2)) - this.mPadding;
                if (this.mWidthHeightRatio > 0.0f) {
                    Rect rect3 = this.mDrawRect;
                    rect3.right = rect3.left + ((int) (((float) (this.mDrawRect.bottom - this.mDrawRect.top)) * 1.0f * this.mWidthHeightRatio));
                } else {
                    this.mDrawRect.right = x;
                }
                this.mDrawRect.top = y;
                correctRect();
            } else if (i == 4) {
                this.mDrawRect.left = ((getWidth() - getRectWidth()) / 2) + this.mPadding;
                this.mDrawRect.top = ((getHeight() - getRectHeight()) / 2) + this.mPadding;
                if (this.mWidthHeightRatio > 0.0f) {
                    Rect rect4 = this.mDrawRect;
                    rect4.right = rect4.left + ((int) (((float) (this.mDrawRect.bottom - this.mDrawRect.top)) * 1.0f * this.mWidthHeightRatio));
                } else {
                    this.mDrawRect.right = x;
                }
                this.mDrawRect.bottom = y;
                correctRect();
            }
            if (this.mTouchRect > 0) {
                invalidate();
            } else {
                OnTransformListener onTransformListener = this.mOnTransformListener;
                if (onTransformListener != null) {
                    float f = this.mOldTouchX;
                    if (f != 0.0f) {
                        onTransformListener.onTrans(f - rawX, this.mOldTouchY - rawY);
                    }
                }
                this.mOldTouchX = rawX;
                this.mOldTouchY = rawY;
            }
        } else if (action == 1) {
            if (this.mWidthHeightRatio <= 0.0f) {
                scaleAndMoveRectToCenter();
            } else if (this.mTouchRect > 0) {
                float rectWidth = (((float) getRectWidth()) * 1.0f) / ((float) Math.abs(this.mDrawRect.right - this.mDrawRect.left));
                float rectHeight = (((float) getRectHeight()) * 1.0f) / ((float) Math.abs(this.mDrawRect.bottom - this.mDrawRect.top));
                if (rectWidth < rectHeight) {
                    rectWidth = rectHeight;
                }
                OnTransformListener onTransformListener2 = this.mOnTransformListener;
                if (onTransformListener2 != null) {
                    onTransformListener2.onTransEnd(rectWidth, new float[]{(float) (this.mDrawRect.right - this.mDrawRect.left), (float) (this.mDrawRect.bottom - this.mDrawRect.top)});
                }
                setDrawRectSize(this.mRectWidth, this.mRectHeight);
                invalidate();
            }
            this.mOldTouchX = 0.0f;
            this.mOldTouchY = 0.0f;
        }
        return super.onTouchEvent(motionEvent);
    }

    private void correctRect() {
        if (this.mDrawRect.top > this.mDrawRect.bottom) {
            Rect rect = this.mDrawRect;
            rect.top = rect.bottom;
        }
        if (this.mDrawRect.right < this.mDrawRect.left) {
            Rect rect2 = this.mDrawRect;
            rect2.right = rect2.left;
        }
        int i = this.mDrawRect.top;
        int i2 = this.mPadding;
        if (i < i2) {
            this.mDrawRect.top = i2;
        }
        if (this.mDrawRect.bottom > getHeight() - this.mPadding) {
            this.mDrawRect.bottom = getHeight() - this.mPadding;
        }
    }

    private void scaleAndMoveRectToCenter() {
        Point anchorOnScreen = getAnchorOnScreen();
        int i = this.mDrawRect.right - this.mDrawRect.left;
        int i2 = this.mDrawRect.bottom - this.mDrawRect.top;
        Point freeCutRectSize = getFreeCutRectSize(i, i2);
        setDrawRectSize(freeCutRectSize.x, freeCutRectSize.y);
        Point anchorOnScreen2 = getAnchorOnScreen();
        if (this.mOnTransformListener != null) {
            float rectWidth = (((float) getRectWidth()) * 1.0f) / ((float) i);
            float rectHeight = (((float) getRectHeight()) * 1.0f) / ((float) i2);
            if (rectWidth >= rectHeight) {
                rectHeight = rectWidth;
            }
            Point point = new Point();
            point.x = anchorOnScreen2.x - anchorOnScreen.x;
            point.y = anchorOnScreen2.y - anchorOnScreen.y;
            this.mOnTransformListener.onRectMoved(rectHeight, point, anchorOnScreen);
        }
    }

    private Point getFreeCutRectSize(int i, int i2) {
        float f = (((float) i) * 1.0f) / ((float) i2);
        int width = getWidth();
        int height = getHeight();
        float f2 = ((float) width) * 1.0f;
        float f3 = (float) height;
        Point point = new Point();
        if (f > f2 / f3) {
            point.x = width;
            point.y = (int) (f2 / f);
        } else {
            point.y = height;
            point.x = (int) (f3 * f);
        }
        return point;
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

    public Point getAnchorOnScreen() {
        Point point = new Point();
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        int i = this.mTouchRect;
        if (i == 1) {
            point.x = iArr[0] + this.mDrawRect.right;
            point.y = iArr[1] + this.mDrawRect.bottom;
        } else if (i == 2) {
            point.x = iArr[0] + this.mDrawRect.right;
            point.y = iArr[1] + this.mDrawRect.top;
        } else if (i == 3) {
            point.x = iArr[0] + this.mDrawRect.left;
            point.y = iArr[1] + this.mDrawRect.bottom;
        } else if (i == 4) {
            point.x = iArr[0] + this.mDrawRect.left;
            point.y = iArr[1] + this.mDrawRect.top;
        }
        return point;
    }

    public int getRectWidth() {
        return this.mRectWidth;
    }

    public int getRectHeight() {
        return this.mRectHeight;
    }

    public Rect getDrawRect() {
        return this.mDrawRect;
    }

    public void setDrawRectSize(int i, int i2) {
        this.mDrawRect.left = ((int) ((((float) (getWidth() - i)) * 1.0f) / 2.0f)) + this.mPadding;
        Rect rect = this.mDrawRect;
        rect.right = rect.left + i;
        this.mDrawRect.top = ((int) ((((float) (getHeight() - i2)) * 1.0f) / 2.0f)) + this.mPadding;
        Rect rect2 = this.mDrawRect;
        rect2.bottom = rect2.top + i2;
        this.mRectWidth = i;
        this.mRectHeight = i2;
        invalidate();
    }

    public int getPadding() {
        return this.mPadding;
    }

    private boolean isInLeftTop(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x >= ((float) this.mDrawRect.left) && x <= ((float) (this.mDrawRect.left + 100)) && y >= ((float) this.mDrawRect.top) && y <= ((float) (this.mDrawRect.top + 100));
    }

    private boolean isInRightTop(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x >= ((float) (this.mDrawRect.right + -100)) && x <= ((float) this.mDrawRect.right) && y >= ((float) this.mDrawRect.top) && y <= ((float) (this.mDrawRect.top + 100));
    }

    private boolean isInLeftBottom(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x >= ((float) this.mDrawRect.left) && x <= ((float) (this.mDrawRect.left + 100)) && y >= ((float) (this.mDrawRect.bottom + -100)) && y <= ((float) this.mDrawRect.bottom);
    }

    private boolean isInRightBottom(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x >= ((float) (this.mDrawRect.right + -100)) && x <= ((float) this.mDrawRect.right) && y >= ((float) (this.mDrawRect.bottom + -100)) && y <= ((float) this.mDrawRect.bottom);
    }

    public void setOnTransformListener(OnTransformListener onTransformListener) {
        this.mOnTransformListener = onTransformListener;
    }
}
