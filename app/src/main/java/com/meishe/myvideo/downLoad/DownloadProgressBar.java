package com.meishe.myvideo.downLoad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.meishe.myvideo.util.ScreenUtils;

public class DownloadProgressBar extends ProgressBar {
    private Context mContext;
    private Paint mPaint;
    private String mProgress;

    public DownloadProgressBar(Context context) {
        super(context);
        this.mContext = context;
        initText();
    }

    public DownloadProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        initText();
    }

    public DownloadProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initText();
    }

    public synchronized void setProgress(int i) {
        super.setProgress(i);
        setText(i);
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.mProgress, 0, this.mProgress.length(), rect);
        this.mPaint.setTextSize((float) ScreenUtils.sp2px(this.mContext, 12.0f));
        canvas.drawText(this.mProgress, (float) ((getWidth() / 2) - rect.centerX()), (float) ((getHeight() / 2) - rect.centerY()), this.mPaint);
    }

    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setColor(-1);
        this.mProgress = "";
    }

    private void setText(int i) {
        int max = (i * 100) / getMax();
        this.mProgress = String.valueOf(max) + "%";
    }
}
