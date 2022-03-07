package com.meishe.player.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.meishe.player.view.CutVideoFragment;
import java.util.ArrayList;
import java.util.List;

public class TestRectView extends View {
    private static final String TAG = "TestRectView";
    private Paint mPaint;
    private List<CutVideoFragment.FloatPoint> mRectPoint;

    public TestRectView(Context context) {
        super(context);
        this.mRectPoint = new ArrayList();
    }

    public TestRectView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRectPoint = new ArrayList();
        this.mPaint = new Paint();
        this.mPaint.setColor(-16776961);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(5.0f);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (!this.mRectPoint.isEmpty()) {
            Path path = new Path();
            path.moveTo(this.mRectPoint.get(0).x, this.mRectPoint.get(0).y);
            path.lineTo(this.mRectPoint.get(1).x, this.mRectPoint.get(1).y);
            path.lineTo(this.mRectPoint.get(2).x, this.mRectPoint.get(2).y);
            path.lineTo(this.mRectPoint.get(3).x, this.mRectPoint.get(3).y);
            path.lineTo(this.mRectPoint.get(0).x, this.mRectPoint.get(0).y);
            canvas.drawPath(path, this.mPaint);
        }
    }

    public void setRectPoint(List<CutVideoFragment.FloatPoint> list) {
        this.mRectPoint = list;
        invalidate();
    }
}
