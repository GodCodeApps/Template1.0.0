package com.meishe.myvideo.view.editview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CompileProgress extends View {
    private Point center;
    private int height;
    private Context mContext;
    Paint mPaint;
    int maxProgress;
    int minProgress;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    int progress;
    int progressBackColor;
    int progressColor;
    private RectF progressRect;
    int progressWidth;
    protected int radius;
    protected int smallRadius;
    private int width;

    public CompileProgress(Context context) {
        this(context, null);
    }

    public CompileProgress(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public void setProgress(int i) {
        this.progress = i;
        invalidate();
    }

    public CompileProgress(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.maxProgress = 100;
        this.minProgress = 0;
        this.mContext = context;
        this.progress = 0;
        this.maxProgress = 100;
        this.minProgress = 0;
        this.progressWidth = 20;
        this.progressColor = Color.parseColor("#ff4a4a4a");
        this.progressBackColor = Color.parseColor("#fffc2b55");
        init();
    }

    private void init() {
        this.center = new Point();
        this.progressRect = new RectF();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth((float) this.progressWidth);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.paddingLeft = getPaddingLeft();
        this.paddingTop = getPaddingTop();
        this.paddingRight = getPaddingRight();
        this.paddingBottom = getPaddingBottom();
        this.width = getProgressDefaultSize(80, i);
        this.height = getProgressDefaultSize(80, i2);
        setMeasuredDimension(this.width, this.height);
    }

    public static int getProgressDefaultSize(int i, int i2) {
        int size;
        return (MeasureSpec.getMode(i2) != MeasureSpec.EXACTLY || (size = MeasureSpec.getSize(i2)) <= i) ? i : size;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = (this.width - this.paddingLeft) - this.paddingRight;
        int i2 = (this.height - this.paddingTop) - this.paddingBottom;
        if (i > i2) {
            i = i2;
        }
        int i3 = i / 2;
        this.center.set(this.paddingLeft + i3, this.paddingTop + i3);
        this.radius = i3;
        this.progressRect.set((float) ((this.center.x - this.radius) + (this.progressWidth / 2)), (float) ((this.center.y - this.radius) + (this.progressWidth / 2)), (float) ((this.center.x + this.radius) - (this.progressWidth / 2)), (float) ((this.center.y + this.radius) - (this.progressWidth / 2)));
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.progressBackColor);
        canvas.drawArc(this.progressRect, 0.0f, 360.0f, false, this.mPaint);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.progressColor);
        int degress = getDegress();
        canvas.drawArc(this.progressRect, -90.0f, (float) degress, false, this.mPaint);
        this.mPaint.setStyle(Paint.Style.FILL);
        int i4 = this.center.y;
        int i5 = this.progressWidth;
        canvas.drawCircle((float) this.center.x, (float) ((i4 + (i5 / 2)) - this.radius), (float) (i5 / 2), this.mPaint);
        double d = (double) this.center.x;
        double d2 = (double) degress;
        double sin = Math.sin(Math.toRadians(d2));
        double d3 = (double) (this.radius - (this.progressWidth / 2));
        Double.isNaN(d3);
        Double.isNaN(d);
        int i6 = (int) (d + (sin * d3));
        double d4 = (double) this.center.y;
        double cos = Math.cos(Math.toRadians(d2));
        int i7 = this.radius;
        int i8 = this.progressWidth;
        double d5 = (double) (i7 - (i8 / 2));
        Double.isNaN(d5);
        Double.isNaN(d4);
        canvas.drawCircle((float) i6, (float) ((int) (d4 - (cos * d5))), (float) (i8 / 2), this.mPaint);
    }

    private Point getProgressPoint() {
        Point point = new Point();
        int i = this.center.x;
        Math.sin((double) ((this.progress / this.maxProgress) / 360));
        int i2 = this.radius;
        int i3 = this.progressWidth / 2;
        int i4 = this.center.y;
        Math.cos((double) ((this.progress / this.maxProgress) / 360));
        int i5 = this.radius;
        int i6 = this.progressWidth / 2;
        return point;
    }

    private int getDegress() {
        return (this.progress * 360) / this.maxProgress;
    }
}
