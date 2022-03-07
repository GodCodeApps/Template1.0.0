package com.meishe.myvideo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.meishe.myvideoapp.R;
import com.meishe.player.common.utils.ScreenUtils;

public class CustomStickerDrawRect extends View {
    private boolean canMoveRect;
    private boolean canScaleRect;
    private Context mContext;
    private OnDrawRectListener mDrawRectListener;
    private int mImgHeight;
    private int mImgWidth;
    private int mMinPixelHeightValue;
    private int mMinPixelWidthValue;
    private Paint mRectPaint;
    private int mScaleImgBtnHeight;
    private int mScaleImgBtnWidth;
    private int mViewMode;
    private RectF mViewRectF;
    private float prevRawX;
    private float prevRawY;
    private Bitmap scaleImgBtn;
    private RectF scaleRectF;

    public interface OnDrawRectListener {
        void onDrawRect(RectF rectF);
    }

    public void setImgSize(int i, int i2) {
        this.mImgWidth = i;
        this.mImgHeight = i2;
    }

    public CustomStickerDrawRect(Context context) {
        this(context, null);
    }

    public CustomStickerDrawRect(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mViewRectF = new RectF();
        this.scaleRectF = new RectF();
        this.mRectPaint = new Paint();
        this.mViewMode = 0;
        this.prevRawX = 0.0f;
        this.prevRawY = 0.0f;
        this.mMinPixelWidthValue = 100;
        this.mMinPixelHeightValue = 100;
        this.canScaleRect = false;
        this.canMoveRect = false;
        this.mContext = context;
        this.scaleImgBtn = BitmapFactory.decodeResource(getResources(), R.mipmap.custom_sticker_scale);
        this.mScaleImgBtnWidth = this.scaleImgBtn.getWidth();
        this.mScaleImgBtnHeight = this.scaleImgBtn.getHeight();
        initRectPaint();
    }

    public int getScaleImgBtnWidth() {
        return this.mScaleImgBtnWidth;
    }

    public int getScaleImgBtnHeight() {
        return this.mScaleImgBtnHeight;
    }

    public void setDrawRect(RectF rectF, int i) {
        this.mViewRectF = rectF;
        this.mViewMode = i;
        invalidate();
    }

    public void setOnDrawRectListener(OnDrawRectListener onDrawRectListener) {
        this.mDrawRectListener = onDrawRectListener;
    }

    private void initRectPaint() {
        this.mRectPaint.setColor(Color.parseColor("#D0021B"));
        this.mRectPaint.setAntiAlias(true);
        this.mRectPaint.setStrokeWidth((float) ScreenUtils.dip2px(this.mContext, 3.0f));
        this.mRectPaint.setStyle(Paint.Style.STROKE);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = this.mViewMode;
        if (i == 2003 || i == 2005) {
            canvas.drawRect(this.mViewRectF, this.mRectPaint);
            canvas.drawBitmap(this.scaleImgBtn, this.mViewRectF.right - ((float) (this.mScaleImgBtnWidth / 2)), this.mViewRectF.bottom - ((float) (this.mScaleImgBtnHeight / 2)), this.mRectPaint);
            this.scaleRectF.set(this.mViewRectF.right - ((float) (this.mScaleImgBtnWidth / 2)), this.mViewRectF.bottom - ((float) (this.mScaleImgBtnHeight / 2)), this.mViewRectF.right + ((float) (this.mScaleImgBtnWidth / 2)), this.mViewRectF.bottom + ((float) (this.mScaleImgBtnHeight / 2)));
        } else if (i == 2004) {
            float f = (this.mViewRectF.right - this.mViewRectF.left) / 2.0f;
            float f2 = (this.mViewRectF.bottom - this.mViewRectF.top) / 2.0f;
            float f3 = f >= f2 ? f2 : f;
            float f4 = this.mViewRectF.left + f;
            float f5 = this.mViewRectF.top + f2;
            canvas.drawCircle(f4, f5, f3, this.mRectPaint);
            double d = (double) f3;
            double cos = Math.cos(0.7853981633974483d);
            Double.isNaN(d);
            float f6 = f4 + ((float) (cos * d));
            double sin = Math.sin(0.7853981633974483d);
            Double.isNaN(d);
            float f7 = f5 + ((float) (d * sin));
            canvas.drawBitmap(this.scaleImgBtn, f6 - ((float) (this.mScaleImgBtnWidth / 2)), f7 - ((float) (this.mScaleImgBtnHeight / 2)), this.mRectPaint);
            RectF rectF = this.scaleRectF;
            int i2 = this.mScaleImgBtnWidth;
            int i3 = this.mScaleImgBtnHeight;
            rectF.set(f6 - ((float) (i2 / 2)), f7 - ((float) (i3 / 2)), f6 + ((float) (i2 / 2)), f7 + ((float) (i3 / 2)));
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.canScaleRect = this.scaleRectF.contains(motionEvent.getX(), motionEvent.getY());
            this.canMoveRect = this.mViewRectF.contains(motionEvent.getX(), motionEvent.getY());
            this.prevRawX = motionEvent.getRawX();
            this.prevRawY = motionEvent.getRawY();
        } else if (action == 1) {
            this.canScaleRect = false;
            this.canMoveRect = false;
            OnDrawRectListener onDrawRectListener = this.mDrawRectListener;
            if (onDrawRectListener != null) {
                onDrawRectListener.onDrawRect(new RectF(this.mViewRectF));
            }
        } else if (action == 2) {
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            double d = (double) (rawX - this.prevRawX);
            Double.isNaN(d);
            int floor = (int) Math.floor(d + 0.5d);
            double d2 = (double) (rawY - this.prevRawY);
            Double.isNaN(d2);
            int floor2 = (int) Math.floor(d2 + 0.5d);
            this.prevRawX = rawX;
            this.prevRawY = rawY;
            int i = this.mViewMode;
            if (i == 2003) {
                if (this.canScaleRect) {
                    scaleRec(floor, floor2);
                } else if (this.canMoveRect) {
                    moveView(floor, floor2);
                }
            } else if (i == 2004 || i == 2005) {
                if (this.canScaleRect) {
                    if (floor > floor2) {
                        floor = floor2;
                    }
                    scaleRec(floor, floor);
                } else if (this.canMoveRect) {
                    moveView(floor, floor2);
                }
            }
        }
        invalidate();
        return true;
    }

    private void moveView(int i, int i2) {
        float f = this.mViewRectF.right - this.mViewRectF.left;
        float f2 = this.mViewRectF.bottom - this.mViewRectF.top;
        float f3 = (float) i;
        this.mViewRectF.left += f3;
        this.mViewRectF.right += f3;
        float f4 = (float) i2;
        this.mViewRectF.top += f4;
        this.mViewRectF.bottom += f4;
        if (this.mViewRectF.left <= 0.0f) {
            RectF rectF = this.mViewRectF;
            rectF.left = 0.0f;
            rectF.right = rectF.left + f;
        }
        float f5 = this.mViewRectF.right;
        int i3 = this.mImgWidth;
        if (f5 >= ((float) i3)) {
            RectF rectF2 = this.mViewRectF;
            rectF2.right = (float) i3;
            rectF2.left = ((float) i3) - f;
        }
        if (this.mViewRectF.top <= 0.0f) {
            RectF rectF3 = this.mViewRectF;
            rectF3.top = 0.0f;
            rectF3.bottom = rectF3.top + f2;
        }
        float f6 = this.mViewRectF.bottom;
        int i4 = this.mImgHeight;
        if (f6 >= ((float) i4)) {
            RectF rectF4 = this.mViewRectF;
            rectF4.bottom = (float) i4;
            rectF4.top = ((float) i4) - f2;
        }
    }

    private void scaleRec(int i, int i2) {
        float f = (float) i;
        this.mViewRectF.right += f;
        float f2 = (float) i2;
        this.mViewRectF.bottom += f2;
        float f3 = this.mViewRectF.right;
        int i3 = this.mImgWidth;
        if (f3 >= ((float) i3)) {
            RectF rectF = this.mViewRectF;
            rectF.right = (float) i3;
            if (this.mViewMode == 2005) {
                rectF.bottom -= f2;
            }
        }
        float f4 = this.mViewRectF.bottom;
        int i4 = this.mImgHeight;
        if (f4 >= ((float) i4)) {
            RectF rectF2 = this.mViewRectF;
            rectF2.bottom = (float) i4;
            if (this.mViewMode == 2005) {
                rectF2.right -= f;
            }
        }
        if (this.mViewRectF.right - this.mViewRectF.left <= ((float) this.mMinPixelWidthValue)) {
            RectF rectF3 = this.mViewRectF;
            rectF3.right = rectF3.left + ((float) this.mMinPixelWidthValue);
        }
        if (this.mViewRectF.bottom - this.mViewRectF.top <= ((float) this.mMinPixelHeightValue)) {
            RectF rectF4 = this.mViewRectF;
            rectF4.bottom = rectF4.top + ((float) this.mMinPixelHeightValue);
        }
    }
}
