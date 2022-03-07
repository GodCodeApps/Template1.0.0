package com.meishe.myvideo.audio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;
import com.meishe.myvideo.util.ScreenUtils;

public class AudioWaveView extends View {
    float mDTRatio;
    private long mDuration;
    float mFixedMaxGroupData;
    private float[] mGroupData;
    private int mHeight;
    float mMaxGroupData;
    private Paint mPaint;
    private int mWaveColor;
    private int mWidth;

    public AudioWaveView(Context context) {
        this(context, null);
    }

    public AudioWaveView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AudioWaveView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDTRatio = ((float) PixelPerMicrosecondUtil.getPixelPerMicrosecond(context)) * 1000.0f;
        init();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mHeight = ScreenUtils.dip2px(getContext(), 34.0f);
    }

    public void setWaveColor(int i) {
        this.mWaveColor = i;
        this.mPaint.setColor(this.mWaveColor);
    }

    public void setWidth(long j) {
        this.mDuration = j;
        this.mWidth = (int) (((float) j) * this.mDTRatio);
        requestLayout();
    }

    public void setMaxGroupData(float f) {
        this.mFixedMaxGroupData = f;
    }

    public void addWaveData(float[] fArr) {
        if (fArr != null) {
            int i = 0;
            if (this.mGroupData == null) {
                this.mMaxGroupData = 0.0f;
                this.mGroupData = new float[(fArr.length / 2)];
                while (true) {
                    float[] fArr2 = this.mGroupData;
                    if (i < fArr2.length) {
                        fArr2[i] = Math.abs(fArr[i * 2]);
                        this.mMaxGroupData = Math.max(this.mMaxGroupData, this.mGroupData[i]);
                        i++;
                    } else {
                        return;
                    }
                }
            } else {
                float[] fArr3 = new float[(fArr.length / 2)];
                for (int i2 = 0; i2 < fArr3.length; i2++) {
                    fArr3[i2] = Math.abs(fArr[i2 * 2]);
                    this.mMaxGroupData = Math.max(this.mMaxGroupData, fArr3[i2]);
                }
                float[] fArr4 = this.mGroupData;
                float[] fArr5 = new float[(fArr4.length + fArr3.length)];
                System.arraycopy(fArr4, 0, fArr5, 0, fArr4.length);
                System.arraycopy(fArr3, 0, fArr5, fArr4.length, fArr3.length);
                this.mGroupData = fArr5;
                invalidate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int i3 = this.mWidth;
        int i4 = this.mHeight;
        if (i4 == 0) {
            i4 = getDefaultSize(getSuggestedMinimumHeight(), i2);
        }
        setMeasuredDimension(i3, i4);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode()) {
            drawPath(canvas);
        }
    }

    private void drawPath(Canvas canvas) {
        if (this.mGroupData != null) {
            float f = this.mFixedMaxGroupData;
            if (f <= 0.0f) {
                f = this.mMaxGroupData;
            }
            for (int i = 0; i < this.mGroupData.length; i++) {
                int height = (int) ((((float) getHeight()) / 2.0f) + (((this.mGroupData[i] / f) * ((float) getHeight())) / 2.0f));
                float length = (((float) i) / ((float) this.mGroupData.length)) * ((float) this.mDuration) * this.mDTRatio;
                canvas.drawLine(length, (float) ((int) ((((float) getHeight()) / 2.0f) - (((this.mGroupData[i] / f) * ((float) getHeight())) / 2.0f))), length, (float) height, this.mPaint);
            }
        }
    }
}
