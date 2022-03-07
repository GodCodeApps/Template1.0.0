package com.meishe.myvideo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamCaptionClip;
import com.meishe.engine.bean.MeicamCompoundCaptionClip;
import com.meishe.engine.bean.MeicamStickerClip;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.TimelineDataUtil;
import com.meishe.myvideo.manager.StateManager;
import com.meishe.myvideo.ui.Utils.ClipBitmapUtil;
import com.meishe.myvideo.util.Constants;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class MYLineView extends RelativeLayout {
    private static final String TAG = "MYLineView";
    private int count = 0;
    private boolean isVisablePipClip = true;
    private boolean isVisableStickerOrCaption = true;
    private int mBitmapHeight;
    private int mBitmapMargin;
    private int mBitmapWidth;
    private float[] mCaptionArray;
    private int mCaptionColor;
    private float[] mCompoundCaptionArray;
    private int mCompoundCaptionColor;
    private Bitmap mConverBitmap;
    private int mLineHeight = 1;
    private int mLineMargin = 5;
    private Paint mLinePaint;
    private List<ClipInfo> mMeicamCaptionInfos;
    private List<ClipInfo> mMeicamClipInfos;
    private List<ClipInfo> mMeicamCompoundCaptionInfos;
    private List<ClipInfo> mMeicamStickerInfos;
    private List<PipBitmapInfo> mPipBitmapList;
    private float[] mPipClipArray;
    private int mPipColor;
    private boolean mShowBitmap = true;
    private int mStartPadding;
    private float[] mStickerArray;
    private int mStickerColor;
    private int mViewMarginBottom;
    private int totalHeight = 0;

    public MYLineView(Context context) {
        super(context);
        init(context);
    }

    public MYLineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MYLineView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mStartPadding = ScreenUtils.getScreenWidth(context) / 2;
        this.mStickerColor = getResources().getColor(R.color.track_background_color_sticker);
        this.mCaptionColor = getResources().getColor(R.color.track_background_color_caption);
        this.mCompoundCaptionColor = getResources().getColor(R.color.track_background_color_compound_caption);
        this.mPipColor = getResources().getColor(R.color.track_background_color_pip);
        this.mViewMarginBottom = getResources().getDimensionPixelOffset(R.dimen.dp2);
        this.mLineHeight = getResources().getDimensionPixelOffset(R.dimen.line_view_height);
        this.mLineMargin = getResources().getDimensionPixelOffset(R.dimen.line_view_margin);
        this.mBitmapWidth = getResources().getDimensionPixelOffset(R.dimen.line_view_bitmap_width);
        this.mBitmapHeight = getResources().getDimensionPixelOffset(R.dimen.line_view_bitmap_height);
        this.mBitmapMargin = getResources().getDimensionPixelOffset(R.dimen.line_view_bitmap_margin);
        this.mConverBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.lineview_bitmap_cover);
        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(false);
        this.mLinePaint.setStyle(Paint.Style.FILL);
        this.mLinePaint.setStrokeWidth((float) this.mLineHeight);
        this.mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        intArray();
    }

    private void intArray() {
        this.mMeicamCaptionInfos = TimelineDataUtil.getStickerOrCaptionListByType(CommonData.CLIP_CAPTION);
        this.mMeicamStickerInfos = TimelineDataUtil.getStickerOrCaptionListByType(CommonData.CLIP_STICKER);
        this.mMeicamCompoundCaptionInfos = TimelineDataUtil.getStickerOrCaptionListByType(CommonData.CLIP_COMPOUND_CAPTION);
        this.mMeicamClipInfos = TimelineDataUtil.getAllPipClipList();
        int i = this.mLineHeight + this.mLineMargin;
        this.isVisablePipClip = true;
        this.isVisableStickerOrCaption = true;
        String currentState = StateManager.getInstance().getCurrentState();
        if (!this.mShowBitmap && (Constants.STATE_STICKER.equals(currentState) || Constants.STATE_CAPTION.equals(currentState) || Constants.STATE_COMPOUND_CAPTION.equals(currentState))) {
            this.isVisableStickerOrCaption = false;
        }
        if (!this.mShowBitmap && Constants.STATE_PIC_IN_PIC.equals(currentState)) {
            this.isVisablePipClip = false;
        }
        this.totalHeight = 0;
        if (this.isVisableStickerOrCaption) {
            if (!this.mMeicamCaptionInfos.isEmpty()) {
                this.totalHeight += i;
                this.count++;
            }
            if (!this.mMeicamStickerInfos.isEmpty()) {
                this.totalHeight += i;
                this.count++;
            }
            if (!this.mMeicamCompoundCaptionInfos.isEmpty()) {
                this.totalHeight += i;
                this.count++;
            }
        }
        if (this.isVisablePipClip && !this.mMeicamClipInfos.isEmpty()) {
            if (this.mShowBitmap) {
                this.totalHeight += i + this.mBitmapHeight + this.mBitmapMargin;
            } else {
                this.totalHeight += i;
            }
            this.count++;
        }
        if (this.totalHeight == 0) {
            Logger.d(TAG, "init: 没有  别费劲了");
            this.mPipClipArray = null;
            this.mCaptionArray = null;
            this.mCompoundCaptionArray = null;
            this.mStickerArray = null;
            invalidate();
            return;
        }
        setDrawData(this.count);
        if (getLayoutParams() == null) {
            post(new Runnable() {
                /* class com.meishe.myvideo.view.MYLineView.AnonymousClass1 */

                public void run() {
                    MYLineView.this.setViewLayoutValue();
                }
            });
        } else {
            setViewLayoutValue();
        }
    }

    private void setDrawData(int i) {
        List<ClipInfo> list;
        if (this.isVisablePipClip && (list = this.mMeicamClipInfos) != null && list.size() > 0) {
            this.mPipClipArray = new float[(this.mMeicamClipInfos.size() * 4)];
            this.mPipBitmapList = new ArrayList(this.mMeicamClipInfos.size());
            int i2 = this.totalHeight;
            int i3 = this.mLineHeight;
            int i4 = this.mLineMargin;
            int i5 = (i2 - ((i3 + i4) * i)) + i4;
            int size = this.mMeicamClipInfos.size();
            for (int i6 = 0; i6 < size; i6++) {
                MeicamVideoClip meicamVideoClip = (MeicamVideoClip) this.mMeicamClipInfos.get(i6);
                Point xAndLengthFromInfo = getXAndLengthFromInfo(meicamVideoClip);
                int i7 = i6 * 4;
                this.mPipClipArray[i7] = (float) xAndLengthFromInfo.x;
                float[] fArr = this.mPipClipArray;
                float f = (float) i5;
                fArr[i7 + 1] = f;
                fArr[i7 + 2] = (float) (xAndLengthFromInfo.x + xAndLengthFromInfo.y);
                this.mPipClipArray[i7 + 3] = f;
                if (this.mShowBitmap) {
                    int i8 = xAndLengthFromInfo.x;
                    int i9 = this.mBitmapWidth;
                    int i10 = i8 - (i9 / 2);
                    this.mPipBitmapList.add(new PipBitmapInfo(ClipBitmapUtil.getBitmapFromClipInfo(getContext(), meicamVideoClip), new Rect(i10, 0, i9 + i10, this.mBitmapHeight), meicamVideoClip));
                }
            }
            i--;
        }
        if (this.isVisableStickerOrCaption) {
            List<ClipInfo> list2 = this.mMeicamStickerInfos;
            if (list2 != null && list2.size() > 0) {
                this.mStickerArray = new float[(this.mMeicamStickerInfos.size() * 4)];
                int i11 = this.totalHeight;
                int i12 = this.mLineHeight;
                int i13 = this.mLineMargin;
                int i14 = (i11 - ((i12 + i13) * i)) + i13;
                int size2 = this.mMeicamStickerInfos.size();
                for (int i15 = 0; i15 < size2; i15++) {
                    Point xAndLengthFromInfo2 = getXAndLengthFromInfo((MeicamStickerClip) this.mMeicamStickerInfos.get(i15));
                    int i16 = i15 * 4;
                    this.mStickerArray[i16] = (float) xAndLengthFromInfo2.x;
                    float[] fArr2 = this.mStickerArray;
                    float f2 = (float) i14;
                    fArr2[i16 + 1] = f2;
                    fArr2[i16 + 2] = (float) (xAndLengthFromInfo2.x + xAndLengthFromInfo2.y);
                    this.mStickerArray[i16 + 3] = f2;
                }
                i--;
            }
            List<ClipInfo> list3 = this.mMeicamCaptionInfos;
            if (list3 != null && list3.size() > 0) {
                this.mCaptionArray = new float[(this.mMeicamCaptionInfos.size() * 4)];
                int i17 = this.totalHeight;
                int i18 = this.mLineHeight;
                int i19 = this.mLineMargin;
                int i20 = (i17 - ((i18 + i19) * i)) + i19;
                int size3 = this.mMeicamCaptionInfos.size();
                for (int i21 = 0; i21 < size3; i21++) {
                    Point xAndLengthFromInfo3 = getXAndLengthFromInfo((MeicamCaptionClip) this.mMeicamCaptionInfos.get(i21));
                    int i22 = i21 * 4;
                    this.mCaptionArray[i22] = (float) xAndLengthFromInfo3.x;
                    float[] fArr3 = this.mCaptionArray;
                    float f3 = (float) i20;
                    fArr3[i22 + 1] = f3;
                    fArr3[i22 + 2] = (float) (xAndLengthFromInfo3.x + xAndLengthFromInfo3.y);
                    this.mCaptionArray[i22 + 3] = f3;
                }
                i--;
            }
            List<ClipInfo> list4 = this.mMeicamCompoundCaptionInfos;
            if (list4 != null && list4.size() > 0) {
                this.mCompoundCaptionArray = new float[(this.mMeicamCompoundCaptionInfos.size() * 4)];
                int i23 = this.totalHeight;
                int i24 = this.mLineHeight;
                int i25 = this.mLineMargin;
                int i26 = (i23 - ((i24 + i25) * i)) + i25;
                int size4 = this.mMeicamCompoundCaptionInfos.size();
                for (int i27 = 0; i27 < size4; i27++) {
                    Point xAndLengthFromInfo4 = getXAndLengthFromInfo((MeicamCompoundCaptionClip) this.mMeicamCompoundCaptionInfos.get(i27));
                    int i28 = i27 * 4;
                    this.mCompoundCaptionArray[i28] = (float) xAndLengthFromInfo4.x;
                    float[] fArr4 = this.mCompoundCaptionArray;
                    float f4 = (float) i26;
                    fArr4[i28 + 1] = f4;
                    fArr4[i28 + 2] = (float) (xAndLengthFromInfo4.x + xAndLengthFromInfo4.y);
                    this.mCompoundCaptionArray[i28 + 3] = f4;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setViewLayoutValue() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        int i = this.totalHeight;
        layoutParams.height = i;
        layoutParams.topMargin = (-i) - this.mViewMarginBottom;
        setLayoutParams(layoutParams);
    }

    public void refresh(boolean z) {
        this.mShowBitmap = z;
        this.mPipClipArray = null;
        this.mCaptionArray = null;
        this.mCompoundCaptionArray = null;
        this.mStickerArray = null;
        intArray();
    }

    public void refreshOnPixelChange() {
        setDrawData(this.count);
        requestLayout();
    }

    public Point getXAndLengthFromInfo(ClipInfo clipInfo) {
        long inPoint = clipInfo.getInPoint();
        return new Point(PixelPerMicrosecondUtil.durationToLength(inPoint) + this.mStartPadding, PixelPerMicrosecondUtil.durationToLength(clipInfo.getOutPoint() - inPoint));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float[] fArr = this.mPipClipArray;
        if (fArr != null && fArr.length > 0) {
            this.mLinePaint.setColor(this.mPipColor);
            canvas.drawLines(this.mPipClipArray, this.mLinePaint);
            for (PipBitmapInfo pipBitmapInfo : this.mPipBitmapList) {
                if (!pipBitmapInfo.mBitmap.isRecycled()) {
                    canvas.drawBitmap(pipBitmapInfo.mBitmap, (Rect) null, pipBitmapInfo.mRect, this.mLinePaint);
                }
                canvas.drawBitmap(this.mConverBitmap, (Rect) null, pipBitmapInfo.mRect, this.mLinePaint);
            }
        }
        float[] fArr2 = this.mCompoundCaptionArray;
        if (fArr2 != null && fArr2.length > 0) {
            this.mLinePaint.setColor(this.mCompoundCaptionColor);
            canvas.drawLines(this.mCompoundCaptionArray, this.mLinePaint);
        }
        float[] fArr3 = this.mStickerArray;
        if (fArr3 != null && fArr3.length > 0) {
            this.mLinePaint.setColor(this.mStickerColor);
            canvas.drawLines(this.mStickerArray, this.mLinePaint);
        }
        float[] fArr4 = this.mCaptionArray;
        if (fArr4 != null && fArr4.length > 0) {
            this.mLinePaint.setColor(this.mCaptionColor);
            canvas.drawLines(this.mCaptionArray, this.mLinePaint);
        }
    }

    public boolean hasPipBitmapList() {
        List<PipBitmapInfo> list = this.mPipBitmapList;
        return list != null && !list.isEmpty();
    }

    public MeicamVideoClip clickPip(int i, int i2) {
        for (PipBitmapInfo pipBitmapInfo : this.mPipBitmapList) {
            if (pipBitmapInfo.mRect.contains(i, i2)) {
                return pipBitmapInfo.mMeicamVideoClip;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public class PipBitmapInfo {
        Bitmap mBitmap;
        MeicamVideoClip mMeicamVideoClip;
        Rect mRect;

        public PipBitmapInfo() {
        }

        public PipBitmapInfo(Bitmap bitmap, Rect rect, MeicamVideoClip meicamVideoClip) {
            this.mBitmap = bitmap;
            this.mRect = rect;
            this.mMeicamVideoClip = meicamVideoClip;
        }
    }
}
