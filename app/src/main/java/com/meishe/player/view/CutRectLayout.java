package com.meishe.player.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import com.meishe.player.view.CutRectViewEx;

public class CutRectLayout extends RelativeLayout {
    private static final int ONE_FINGER = 1;
    private static final int TWO_FINGER = 2;
    private Context mContext;
    private boolean mIsTwoFingerEvent = false;
    private float mOldTouchX;
    private float mOldTouchY;
    private OnTransformListener mOnTransformListener;
    private CutRectViewEx mRectView;
    private int mTouchRect;
    private double mTwoFingerEndLength;
    private PointF mTwoFingerOldPoint = new PointF();
    private double mTwoFingerStartLength;

    public interface OnTransformListener {
        void onRectMoved(float f, Point point, Point point2);

        void onScaleAndRotate(float f, float f2);

        void onTrans(float f, float f2);

        void onTransEnd(float f, float[] fArr);
    }

    public CutRectLayout(Context context) {
        super(context);
    }

    public CutRectLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initView();
    }

    private void initView() {
        this.mRectView = new CutRectViewEx(this.mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(15);
        addView(this.mRectView, layoutParams);
        this.mRectView.setOnTransformListener(new CutRectViewEx.OnTransformListener() {
            /* class com.meishe.player.view.CutRectLayout.AnonymousClass1 */

            @Override // com.meishe.player.view.CutRectViewEx.OnTransformListener
            public void onScaleAndRotate(float f, float f2) {
            }

            @Override // com.meishe.player.view.CutRectViewEx.OnTransformListener
            public void onTrans(float f, float f2) {
            }

            @Override // com.meishe.player.view.CutRectViewEx.OnTransformListener
            public void onTransEnd(float f, float[] fArr) {
                if (CutRectLayout.this.mOnTransformListener != null) {
                    CutRectLayout.this.mOnTransformListener.onTransEnd(f, fArr);
                }
            }

            @Override // com.meishe.player.view.CutRectViewEx.OnTransformListener
            public void onRectMoved(float f, Point point, Point point2) {
                if (CutRectLayout.this.mOnTransformListener != null) {
                    CutRectLayout.this.mOnTransformListener.onRectMoved(f, point, point2);
                }
            }
        });
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() == 2) {
            return true;
        }
        if (motionEvent.getAction() == 0) {
            this.mTouchRect = getDrawRectView().getTouchRect(motionEvent);
        }
        if (this.mTouchRect < 0) {
            return true;
        }
        return false;
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

    public int getRectWidth() {
        return this.mRectView.getRectWidth();
    }

    public int getRectHeight() {
        return this.mRectView.getRectHeight();
    }

    public CutRectViewEx getDrawRectView() {
        return this.mRectView;
    }

    public int getDrawRectViewLeft() {
        return ((int) ((((float) (getWidth() - this.mRectView.getRectWidth())) * 1.0f) / 2.0f)) + this.mRectView.getPadding();
    }

    public int getDrawRectViewTop() {
        return ((int) ((((float) (getHeight() - this.mRectView.getRectHeight())) * 1.0f) / 2.0f)) + this.mRectView.getPadding();
    }

    public void setWidthHeightRatio(float f) {
        this.mRectView.setWidthHeightRatio(f);
    }

    public void setDrawRectSize(int i, int i2) {
        this.mRectView.setDrawRectSize(i, i2);
    }

    private boolean oneFingerTouch(MotionEvent motionEvent) {
        if (this.mIsTwoFingerEvent) {
            if (motionEvent.getAction() == 1) {
                this.mIsTwoFingerEvent = false;
                this.mOldTouchX = 0.0f;
                this.mOldTouchY = 0.0f;
                OnTransformListener onTransformListener = this.mOnTransformListener;
                if (onTransformListener != null) {
                    onTransformListener.onTransEnd(-1.0f, null);
                }
            }
            return false;
        }
        int action = motionEvent.getAction();
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        if (action == 2) {
            OnTransformListener onTransformListener2 = this.mOnTransformListener;
            if (onTransformListener2 != null) {
                float f = this.mOldTouchX;
                if (f != 0.0f) {
                    onTransformListener2.onTrans(f - rawX, this.mOldTouchY - rawY);
                }
            }
            this.mOldTouchX = rawX;
            this.mOldTouchY = rawY;
        } else if (action == 1) {
            this.mOldTouchX = 0.0f;
            this.mOldTouchY = 0.0f;
        }
        return true;
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
            onTransformListener.onTransEnd(-1.0f, null);
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setOnTransformListener(OnTransformListener onTransformListener) {
        this.mOnTransformListener = onTransformListener;
    }
}
