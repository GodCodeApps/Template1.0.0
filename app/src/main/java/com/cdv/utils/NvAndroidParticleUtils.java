package com.cdv.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class NvAndroidParticleUtils {

    public static class SpriteGenerator {
        private Canvas m_canvas;
        private Paint m_paint = new Paint();
        private int m_spriteImageSize;

        SpriteGenerator(Bitmap bitmap, int i) {
            this.m_canvas = new Canvas(bitmap);
            this.m_canvas.drawColor(0, PorterDuff.Mode.SRC);
            this.m_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            this.m_paint.setAntiAlias(false);
            this.m_paint.setFilterBitmap(false);
            this.m_spriteImageSize = i;
        }

        /* access modifiers changed from: package-private */
        public void drawSpriteImage(int i, int i2, Bitmap bitmap) {
            if (bitmap != null) {
                if (bitmap.getWidth() == this.m_spriteImageSize && bitmap.getHeight() == this.m_spriteImageSize) {
                    this.m_canvas.drawBitmap(bitmap, (float) i, (float) i2, this.m_paint);
                    return;
                }
                Canvas canvas = this.m_canvas;
                int i3 = this.m_spriteImageSize;
                canvas.drawBitmap(bitmap, (Rect) null, new Rect(i, i2, i + i3, i3 + i2), this.m_paint);
            }
        }
    }
}
