package com.cdv.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import java.util.concurrent.atomic.AtomicReference;

public class NvAndroidPlatformImage {
    private static final String TAG = "NvAndroidPlatformImage";
    private static AtomicReference<RenderScript> m_rednerScript = new AtomicReference<>();
    private static final boolean m_verbose = false;

    public static void drawBitmap(Canvas canvas, Paint paint, int i, Bitmap bitmap, int i2, int i3, int i4, int i5, float f, float f2, float f3, float f4) {
        try {
            paint.setXfermode(getXferMode(i));
            paint.setAntiAlias(false);
            canvas.drawBitmap(bitmap, new Rect(i2, i3, i4 + i2, i5 + i3), new RectF(f, f2, f3 + f, f4 + f2), paint);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void copyTo(Canvas canvas, Paint paint, Bitmap bitmap) {
        try {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            canvas.save();
            canvas.setMatrix(null);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            canvas.restore();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void blurTo(Context context, Canvas canvas, Paint paint, Bitmap bitmap, Bitmap bitmap2, float f) {
        if (Build.VERSION.SDK_INT < 17) {
            copyTo(canvas, paint, bitmap2);
            return;
        }
        try {
            RenderScript rednerScriptContext = getRednerScriptContext(context);
            if (rednerScriptContext == null) {
                copyTo(canvas, paint, bitmap2);
                return;
            }
            ScriptIntrinsicBlur create = ScriptIntrinsicBlur.create(rednerScriptContext, Element.U8_4(rednerScriptContext));
            Allocation createFromBitmap = Allocation.createFromBitmap(rednerScriptContext, bitmap2, Allocation.MipmapControl.MIPMAP_NONE, 129);
            Allocation createFromBitmap2 = Allocation.createFromBitmap(rednerScriptContext, bitmap, Allocation.MipmapControl.MIPMAP_NONE, 129);
            create.setRadius(Math.max(Math.min(f, 25.0f), 0.0f));
            create.setInput(createFromBitmap);
            create.forEach(createFromBitmap2);
            createFromBitmap2.copyTo(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setupProjection(Canvas canvas, Bitmap bitmap, float f, float f2, float f3, float f4) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postTranslate(-f, -f2);
            matrix.postScale(((float) width) / f3, ((float) height) / f4);
            canvas.setMatrix(matrix);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Xfermode getXferMode(int i) {
        PorterDuff.Mode mode;
        PorterDuff.Mode mode2 = PorterDuff.Mode.SRC_OVER;
        switch (i) {
            case 1:
                mode = PorterDuff.Mode.DST_OVER;
                break;
            case 2:
                mode = PorterDuff.Mode.CLEAR;
                break;
            case 3:
                mode = PorterDuff.Mode.SRC;
                break;
            case 4:
                mode = PorterDuff.Mode.SRC_IN;
                break;
            case 5:
                mode = PorterDuff.Mode.DST_IN;
                break;
            case 6:
                mode = PorterDuff.Mode.SRC_OUT;
                break;
            case 7:
                mode = PorterDuff.Mode.DST_OUT;
                break;
            case 8:
                mode = PorterDuff.Mode.SRC_ATOP;
                break;
            case 9:
                mode = PorterDuff.Mode.DST_ATOP;
                break;
            case 10:
                mode = PorterDuff.Mode.XOR;
                break;
            default:
                mode = PorterDuff.Mode.SRC_OVER;
                break;
        }
        return new PorterDuffXfermode(mode);
    }

    private static RenderScript getRednerScriptContext(Context context) {
        try {
            RenderScript renderScript = m_rednerScript.get();
            if (renderScript != null) {
                return renderScript;
            }
            m_rednerScript.compareAndSet(null, RenderScript.create(context));
            return m_rednerScript.get();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
