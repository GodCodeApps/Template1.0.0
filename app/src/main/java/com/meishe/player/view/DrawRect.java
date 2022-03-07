package com.meishe.player.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.meishe.player.R;
import com.meishe.player.common.utils.ImageConverter;
import java.util.ArrayList;
import java.util.List;

public class DrawRect extends View {
    private static final String TAG = "DrawRect";
    private Bitmap[] alignImgArray;
    private RectF alignRectF;
    private boolean canAlignClick;
    private boolean canDel;
    private boolean canHorizFlipClick;
    private boolean canMuteClick;
    private boolean canScalOrRotate;
    private Bitmap deleteImgBtn;
    private RectF deleteRectF;
    private String filePath;
    private RectF horizFlipRectF;
    private Bitmap horizontalFlipImgBtn;
    private boolean isInnerDrawRect;
    private double mClickMoveDistance;
    private onDrawRectClickListener mDrawRectClickListener;
    private boolean mHasAudio;
    private int mIndex;
    private boolean mIsOutOfTime;
    private boolean mIsVisible;
    private List<PointF> mListPointF;
    private OnTouchListener mListener;
    private boolean mMoveOutScreen;
    private long mPrevMillionSecond;
    private Paint mRectPaint;
    private int mStickerMuteIndex;
    private onStickerMuteListenser mStickerMuteListenser;
    private List<List<PointF>> mSubListPointF;
    private Paint mSubRectPaint;
    private Bitmap[] muteImgArray;
    private RectF muteRectF;
    private PointF prePointF;
    private Path rectPath;
    private Bitmap rotationImgBtn;
    private RectF rotationRectF;
    private int subCaptionIndex;
    private int viewMode;
    private int waterMackType;
    private Bitmap waterMarkBitmap;

    public interface OnTouchListener {
        void onAlignClick();

        void onBeyondDrawRectClick();

        void onDel();

        void onDrag(PointF pointF, PointF pointF2);

        void onHorizFlipClick();

        void onScaleAndRotate(float f, PointF pointF, float f2);

        void onScaleXandY(float f, float f2, PointF pointF);

        void onTouchDown(PointF pointF);

        void onTouchUp();
    }

    public interface onDrawRectClickListener {
        void onDrawRectClick(int i);
    }

    public interface onStickerMuteListenser {
        void onStickerMute();
    }

    public DrawRect(Context context) {
        this(context, null);
    }

    public DrawRect(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.prePointF = new PointF(0.0f, 0.0f);
        this.alignRectF = new RectF();
        this.horizFlipRectF = new RectF();
        this.rotationRectF = new RectF();
        this.deleteRectF = new RectF();
        this.muteRectF = new RectF();
        this.rectPath = new Path();
        this.canScalOrRotate = false;
        this.canHorizFlipClick = false;
        this.canMuteClick = false;
        this.isInnerDrawRect = false;
        this.canDel = false;
        this.canAlignClick = false;
        this.mIndex = 0;
        this.viewMode = 0;
        this.mStickerMuteIndex = 0;
        this.mHasAudio = false;
        this.rotationImgBtn = BitmapFactory.decodeResource(getResources(), R.mipmap.scale);
        this.alignImgArray = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.mipmap.left_align), BitmapFactory.decodeResource(getResources(), R.mipmap.center_align), BitmapFactory.decodeResource(getResources(), R.mipmap.right_align)};
        this.deleteImgBtn = BitmapFactory.decodeResource(getResources(), R.mipmap.delete);
        this.horizontalFlipImgBtn = BitmapFactory.decodeResource(getResources(), R.mipmap.horizontal_flip);
        this.muteImgArray = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.mipmap.stickerunmute), BitmapFactory.decodeResource(getResources(), R.mipmap.stickermute)};
        this.mPrevMillionSecond = 0;
        this.mClickMoveDistance = 0.0d;
        this.mRectPaint = new Paint();
        this.mSubRectPaint = new Paint();
        this.mMoveOutScreen = false;
        this.mIsVisible = true;
        this.mIsOutOfTime = false;
        this.subCaptionIndex = -1;
        this.waterMackType = -1;
        initRectPaint();
        initSubRectPaint();
    }

    public void cleanUp() {
        bitmapRecycle(this.rotationImgBtn);
        this.rotationImgBtn = null;
        int length = this.alignImgArray.length;
        for (int i = 0; i < length; i++) {
            bitmapRecycle(this.alignImgArray[i]);
            this.alignImgArray[i] = null;
        }
        bitmapRecycle(this.deleteImgBtn);
        this.deleteImgBtn = null;
        bitmapRecycle(this.horizontalFlipImgBtn);
        this.horizontalFlipImgBtn = null;
        int length2 = this.muteImgArray.length;
        for (int i2 = 0; i2 < length2; i2++) {
            bitmapRecycle(this.muteImgArray[i2]);
            this.muteImgArray[i2] = null;
        }
        bitmapRecycle(this.waterMarkBitmap);
        this.waterMarkBitmap = null;
    }

    private void bitmapRecycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    private void setRectPath(List<PointF> list) {
        this.rectPath.reset();
        this.rectPath.moveTo(list.get(0).x, list.get(0).y);
        this.rectPath.lineTo(list.get(1).x, list.get(1).y);
        this.rectPath.lineTo(list.get(2).x, list.get(2).y);
        this.rectPath.lineTo(list.get(3).x, list.get(3).y);
        this.rectPath.close();
    }

    private void initRectPaint() {
        this.mRectPaint.setColor(Color.parseColor("#4A90E2"));
        this.mRectPaint.setAntiAlias(true);
        this.mRectPaint.setStrokeWidth(8.0f);
        this.mRectPaint.setStyle(Paint.Style.STROKE);
    }

    private void initSubRectPaint() {
        this.mSubRectPaint.setColor(Color.parseColor("#9B9B9B"));
        this.mSubRectPaint.setAntiAlias(true);
        float f = (float) 4;
        this.mSubRectPaint.setStrokeWidth(f);
        this.mSubRectPaint.setStyle(Paint.Style.STROKE);
        this.mSubRectPaint.setPathEffect(new DashPathEffect(new float[]{f, (float) 2}, 0.0f));
    }

    private int getSubCaptionIndex(int i, int i2) {
        List<List<PointF>> list = this.mSubListPointF;
        if (list == null) {
            return -1;
        }
        int size = list.size();
        for (int i3 = 0; i3 < size; i3++) {
            if (clickPointIsInnerDrawRect(this.mSubListPointF.get(i3), i, i2)) {
                return i3;
            }
        }
        return -1;
    }

    public void setAlignIndex(int i) {
        this.mIndex = i;
        invalidate();
    }

    public void setStickerMuteIndex(int i) {
        this.mStickerMuteIndex = i;
        invalidate();
    }

    public void setMuteVisible(boolean z) {
        this.mHasAudio = z;
        invalidate();
    }

    public void setDrawRect(List<PointF> list, int i) {
        setDrawRectVisible(true);
        setVisibility(0);
        this.mListPointF = list;
        this.viewMode = i;
        invalidate();
    }

    public void setCompoundDrawRect(List<PointF> list, List<List<PointF>> list2, int i) {
        this.mListPointF = list;
        this.mSubListPointF = list2;
        this.viewMode = i;
        invalidate();
    }

    public void setWaterMackType(int i) {
        this.waterMackType = i;
    }

    public List<PointF> getDrawRect() {
        return this.mListPointF;
    }

    public void setListPointF(List<PointF> list) {
        this.mListPointF = list;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mListener = onTouchListener;
    }

    public void setDrawRectClickListener(onDrawRectClickListener ondrawrectclicklistener) {
        this.mDrawRectClickListener = ondrawrectclicklistener;
    }

    public void setStickerMuteListenser(onStickerMuteListenser onstickermutelistenser) {
        this.mStickerMuteListenser = onstickermutelistenser;
    }

    public boolean isOutOfTime() {
        return this.mIsOutOfTime;
    }

    public void setOutOfTime(boolean z) {
        if (this.mIsOutOfTime != z) {
            this.mIsOutOfTime = z;
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        List<PointF> list;
        List<List<PointF>> list2;
        super.onDraw(canvas);
        if (this.mIsVisible && !this.mIsOutOfTime && (list = this.mListPointF) != null && list.size() == 4) {
            setRectPath(this.mListPointF);
            canvas.drawPath(this.rectPath, this.mRectPaint);
            int i = this.viewMode;
            if (i == 0) {
                canvas.drawBitmap(this.alignImgArray[this.mIndex], this.mListPointF.get(0).x - ((float) (this.alignImgArray[this.mIndex].getHeight() / 2)), this.mListPointF.get(0).y - ((float) (this.alignImgArray[this.mIndex].getWidth() / 2)), this.mRectPaint);
                this.alignRectF.set(this.mListPointF.get(0).x - ((float) (this.alignImgArray[this.mIndex].getWidth() / 2)), this.mListPointF.get(0).y - ((float) (this.alignImgArray[this.mIndex].getHeight() / 2)), this.mListPointF.get(0).x + ((float) (this.alignImgArray[this.mIndex].getWidth() / 2)), this.mListPointF.get(0).y + ((float) (this.alignImgArray[this.mIndex].getWidth() / 2)));
            } else if (i == 1) {
                canvas.drawBitmap(this.horizontalFlipImgBtn, this.mListPointF.get(0).x - ((float) (this.horizontalFlipImgBtn.getHeight() / 2)), this.mListPointF.get(0).y - ((float) (this.horizontalFlipImgBtn.getWidth() / 2)), this.mRectPaint);
                this.horizFlipRectF.set(this.mListPointF.get(0).x - ((float) (this.horizontalFlipImgBtn.getWidth() / 2)), this.mListPointF.get(0).y - ((float) (this.horizontalFlipImgBtn.getHeight() / 2)), this.mListPointF.get(0).x + ((float) (this.horizontalFlipImgBtn.getWidth() / 2)), this.mListPointF.get(0).y + ((float) (this.horizontalFlipImgBtn.getHeight() / 2)));
                if (this.mHasAudio) {
                    canvas.drawBitmap(this.muteImgArray[this.mStickerMuteIndex], this.mListPointF.get(1).x - ((float) (this.muteImgArray[this.mStickerMuteIndex].getHeight() / 2)), this.mListPointF.get(1).y - ((float) (this.muteImgArray[this.mStickerMuteIndex].getWidth() / 2)), this.mRectPaint);
                    this.muteRectF.set(this.mListPointF.get(1).x - ((float) (this.muteImgArray[this.mStickerMuteIndex].getWidth() / 2)), this.mListPointF.get(1).y - ((float) (this.muteImgArray[this.mStickerMuteIndex].getHeight() / 2)), this.mListPointF.get(1).x + ((float) (this.muteImgArray[this.mStickerMuteIndex].getWidth() / 2)), this.mListPointF.get(1).y + ((float) (this.muteImgArray[this.mStickerMuteIndex].getHeight() / 2)));
                } else {
                    this.muteRectF.set(0.0f, 0.0f, 0.0f, 0.0f);
                }
            } else if (i == 2) {
                Bitmap bitmap = this.waterMarkBitmap;
                if (bitmap != null && this.waterMackType == 0) {
                    canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), this.waterMarkBitmap.getHeight()), new RectF(this.mListPointF.get(0).x, this.mListPointF.get(0).y, this.mListPointF.get(2).x, this.mListPointF.get(2).y), (Paint) null);
                }
            } else if (i == 4 && (list2 = this.mSubListPointF) != null) {
                int size = list2.size();
                for (int i2 = 0; i2 < size; i2++) {
                    List<PointF> list3 = this.mSubListPointF.get(i2);
                    if (list3 != null && list3.size() == 4) {
                        setRectPath(list3);
                        canvas.drawPath(this.rectPath, this.mSubRectPaint);
                    }
                }
            }
            if (this.viewMode != 3) {
                canvas.drawBitmap(this.deleteImgBtn, this.mListPointF.get(3).x - ((float) (this.deleteImgBtn.getWidth() / 2)), this.mListPointF.get(3).y - ((float) (this.deleteImgBtn.getHeight() / 2)), this.mRectPaint);
                this.deleteRectF.set(this.mListPointF.get(3).x - ((float) (this.deleteImgBtn.getWidth() / 2)), this.mListPointF.get(3).y - ((float) (this.deleteImgBtn.getHeight() / 2)), this.mListPointF.get(3).x + ((float) (this.deleteImgBtn.getWidth() / 2)), this.mListPointF.get(3).y + ((float) (this.deleteImgBtn.getHeight() / 2)));
                canvas.drawBitmap(this.rotationImgBtn, this.mListPointF.get(2).x - ((float) (this.rotationImgBtn.getHeight() / 2)), this.mListPointF.get(2).y - ((float) (this.rotationImgBtn.getWidth() / 2)), this.mRectPaint);
                this.rotationRectF.set(this.mListPointF.get(2).x - ((float) (this.rotationImgBtn.getWidth() / 2)), this.mListPointF.get(2).y - ((float) (this.rotationImgBtn.getHeight() / 2)), this.mListPointF.get(2).x + ((float) (this.rotationImgBtn.getWidth() / 2)), this.mListPointF.get(2).y + ((float) (this.rotationImgBtn.getHeight() / 2)));
            }
        }
    }

    public boolean curPointIsInnerDrawRect(int i, int i2) {
        return clickPointIsInnerDrawRect(this.mListPointF, i, i2);
    }

    public boolean clickPointIsInnerDrawRect(List<PointF> list, int i, int i2) {
        if (list == null || list.size() != 4) {
            return false;
        }
        RectF rectF = new RectF();
        Path path = new Path();
        path.moveTo(list.get(0).x, list.get(0).y);
        path.lineTo(list.get(1).x, list.get(1).y);
        path.lineTo(list.get(2).x, list.get(2).y);
        path.lineTo(list.get(3).x, list.get(3).y);
        path.close();
        path.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return region.contains(i, i2);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        OnTouchListener onTouchListener;
        OnTouchListener onTouchListener2;
        OnTouchListener onTouchListener3;
        OnTouchListener onTouchListener4;
        OnTouchListener onTouchListener5;
        onStickerMuteListenser onstickermutelistenser;
        OnTouchListener onTouchListener6;
        OnTouchListener onTouchListener7;
        OnTouchListener onTouchListener8;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (this.mListPointF != null) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mPrevMillionSecond = System.currentTimeMillis();
                this.canScalOrRotate = this.rotationRectF.contains(x, y);
                this.canDel = this.deleteRectF.contains(x, y);
                int i = this.viewMode;
                if (i == 0) {
                    this.canAlignClick = this.alignRectF.contains(x, y);
                } else if (i == 1) {
                    this.canHorizFlipClick = this.horizFlipRectF.contains(x, y);
                    this.canMuteClick = this.muteRectF.contains(x, y);
                }
                OnTouchListener onTouchListener9 = this.mListener;
                if (onTouchListener9 != null && !this.canDel && !this.canScalOrRotate && !this.canAlignClick && !this.canHorizFlipClick && !this.canMuteClick) {
                    onTouchListener9.onTouchDown(new PointF(x, y));
                }
                List<PointF> list = this.mListPointF;
                if (list != null && list.size() == 4) {
                    this.isInnerDrawRect = curPointIsInnerDrawRect((int) x, (int) y);
                }
                if (this.isInnerDrawRect) {
                    this.subCaptionIndex = getSubCaptionIndex((int) x, (int) y);
                }
                this.prePointF.set(x, y);
            } else if (action == 1) {
                if (!this.canScalOrRotate && this.canDel && (onTouchListener8 = this.mListener) != null) {
                    this.isInnerDrawRect = false;
                    onTouchListener8.onDel();
                }
                int i2 = this.viewMode;
                if (i2 == 0) {
                    if (this.canAlignClick && (onTouchListener7 = this.mListener) != null) {
                        this.isInnerDrawRect = false;
                        onTouchListener7.onAlignClick();
                    }
                } else if (i2 == 1) {
                    if (this.canHorizFlipClick && (onTouchListener6 = this.mListener) != null) {
                        onTouchListener6.onHorizFlipClick();
                    }
                    if (this.canMuteClick && (onstickermutelistenser = this.mStickerMuteListenser) != null) {
                        onstickermutelistenser.onStickerMute();
                    }
                }
                long currentTimeMillis = System.currentTimeMillis() - this.mPrevMillionSecond;
                if (this.mClickMoveDistance < 10.0d && currentTimeMillis <= 200) {
                    int i3 = this.viewMode;
                    if (i3 == 0) {
                        if (this.isInnerDrawRect && !this.canScalOrRotate && !this.canDel && !this.canAlignClick) {
                            onDrawRectClickListener ondrawrectclicklistener = this.mDrawRectClickListener;
                            if (ondrawrectclicklistener != null) {
                                ondrawrectclicklistener.onDrawRectClick(0);
                            }
                        } else if (!this.canScalOrRotate && !this.canDel && !this.canAlignClick && (onTouchListener5 = this.mListener) != null) {
                            onTouchListener5.onBeyondDrawRectClick();
                        }
                    } else if (i3 == 1) {
                        if (!this.isInnerDrawRect && !this.canScalOrRotate && !this.canDel && !this.canHorizFlipClick && !this.canMuteClick && (onTouchListener4 = this.mListener) != null) {
                            onTouchListener4.onBeyondDrawRectClick();
                        }
                    } else if (i3 == 3) {
                        if (!this.isInnerDrawRect && (onTouchListener3 = this.mListener) != null) {
                            onTouchListener3.onBeyondDrawRectClick();
                        }
                    } else if (i3 == 2) {
                        if (!this.isInnerDrawRect && (onTouchListener2 = this.mListener) != null) {
                            onTouchListener2.onBeyondDrawRectClick();
                        }
                    } else if (i3 == 4) {
                        if (this.isInnerDrawRect && !this.canScalOrRotate && !this.canDel) {
                            onDrawRectClickListener ondrawrectclicklistener2 = this.mDrawRectClickListener;
                            if (ondrawrectclicklistener2 != null) {
                                ondrawrectclicklistener2.onDrawRectClick(this.subCaptionIndex);
                            }
                        } else if (!this.canScalOrRotate && !this.canDel && !this.canAlignClick && (onTouchListener = this.mListener) != null) {
                            onTouchListener.onBeyondDrawRectClick();
                        }
                    }
                }
                this.canDel = false;
                this.canScalOrRotate = false;
                this.isInnerDrawRect = false;
                this.canAlignClick = false;
                this.canHorizFlipClick = false;
                this.canMuteClick = false;
                this.mClickMoveDistance = 0.0d;
                this.mListener.onTouchUp();
            } else if (action == 2) {
                this.mClickMoveDistance = Math.sqrt(Math.pow((double) (x - this.prePointF.x), 2.0d) + Math.pow((double) (y - this.prePointF.y), 2.0d));
                if (x <= 100.0f || x >= ((float) getWidth()) || y >= ((float) getHeight()) || y <= 20.0f) {
                    this.mMoveOutScreen = true;
                } else if (this.mMoveOutScreen) {
                    this.mMoveOutScreen = false;
                } else {
                    PointF pointF = new PointF();
                    List<PointF> list2 = this.mListPointF;
                    if (list2 != null && list2.size() == 4) {
                        pointF.x = (this.mListPointF.get(0).x + this.mListPointF.get(2).x) / 2.0f;
                        pointF.y = (this.mListPointF.get(0).y + this.mListPointF.get(2).y) / 2.0f;
                    }
                    if (this.mListener != null && this.canScalOrRotate) {
                        this.isInnerDrawRect = false;
                        float sqrt = (float) (Math.sqrt(Math.pow((double) (x - pointF.x), 2.0d) + Math.pow((double) (y - pointF.y), 2.0d)) / Math.sqrt(Math.pow((double) (this.prePointF.x - pointF.x), 2.0d) + Math.pow((double) (this.prePointF.y - pointF.y), 2.0d)));
                        this.mListener.onScaleXandY((x - pointF.x) / (this.prePointF.x - pointF.x), (y - pointF.y) / (this.prePointF.y - pointF.y), new PointF(pointF.x, pointF.y));
                        double atan2 = (double) (((float) (Math.atan2((double) (y - pointF.y), (double) (x - pointF.x)) - Math.atan2((double) (this.prePointF.y - pointF.y), (double) (this.prePointF.x - pointF.x)))) * 180.0f);
                        Double.isNaN(atan2);
                        this.mListener.onScaleAndRotate(sqrt, new PointF(pointF.x, pointF.y), -((float) (atan2 / 3.141592653589793d)));
                    }
                    OnTouchListener onTouchListener10 = this.mListener;
                    if (onTouchListener10 != null && this.isInnerDrawRect && this.mIsVisible && !this.mIsOutOfTime) {
                        onTouchListener10.onDrag(this.prePointF, new PointF(x, y));
                    }
                    this.prePointF.set(x, y);
                }
            }
        }
        return true;
    }

    public void setDrawRectVisible(boolean z) {
        this.mIsVisible = z;
        invalidate();
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public void setWaterMarkBitmapByImgPath(String str, int i, int i2, int i3, int i4) {
        this.waterMackType = i4;
        ArrayList arrayList = new ArrayList();
        int dimension = (int) getContext().getResources().getDimension(R.dimen.edit_waterMark_width);
        int dimension2 = (int) getContext().getResources().getDimension(R.dimen.edit_waterMark_height);
        if (i4 == 0) {
            Point picturePoint = ImageConverter.getPicturePoint(str, getContext());
            this.waterMarkBitmap = ImageConverter.convertImageScaleByWidth(getContext(), str, dimension);
            if (picturePoint != null) {
                dimension2 = (picturePoint.y * dimension) / picturePoint.x;
            }
            if (this.waterMarkBitmap != null) {
                float f = (float) i2;
                float f2 = (float) i3;
                arrayList.add(new PointF(f, f2));
                float f3 = (float) (i3 + dimension2);
                arrayList.add(new PointF(f, f3));
                float f4 = (float) (i2 + dimension);
                arrayList.add(new PointF(f4, f3));
                arrayList.add(new PointF(f4, f2));
            } else {
                return;
            }
        } else if (i4 == 1 || i4 == 2) {
            float f5 = (float) i2;
            float f6 = (float) i3;
            arrayList.add(new PointF(f5, f6));
            float f7 = (float) (i3 + dimension2);
            arrayList.add(new PointF(f5, f7));
            float f8 = (float) (i2 + dimension);
            arrayList.add(new PointF(f8, f7));
            arrayList.add(new PointF(f8, f6));
        }
        setDrawRect(arrayList, 2);
    }

    public void setWaterMarkBitmap(String str, int i) {
        this.waterMarkBitmap = ImageConverter.convertImageScaleByWidth(getContext(), str, i);
    }

    public Point getPicturePoint(String str) {
        return ImageConverter.getPicturePoint(str, getContext());
    }

    public int getViewMode() {
        return this.viewMode;
    }

    public void setViewMode(int i) {
        this.viewMode = i;
    }
}
