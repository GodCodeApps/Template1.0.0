package com.cdv.text;

import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.TextPaint;

public class NvAndroidTextCacheGenerator {
    private static final String TAG = "NvAndroidTextCacheGenerator";
    private static final boolean m_verbose = false;

    NvAndroidTextCacheGenerator() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x003b A[Catch:{ Exception -> 0x0060 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005a A[Catch:{ Exception -> 0x0060 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean drawColorGlyph(com.cdv.text.NvAndroidTextLayout r15, int r16, int r17, float r18, float r19, android.graphics.Canvas r20, int r21, boolean r22, boolean r23) {
        /*
        // Method dump skipped, instructions count: 130
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.text.NvAndroidTextCacheGenerator.drawColorGlyph(com.cdv.text.NvAndroidTextLayout, int, int, float, float, android.graphics.Canvas, int, boolean, boolean):boolean");
        return r22;
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x004d A[Catch:{ Exception -> 0x0071 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0057 A[Catch:{ Exception -> 0x0071 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x005b A[Catch:{ Exception -> 0x0071 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0065 A[Catch:{ Exception -> 0x0071 }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x006b A[Catch:{ Exception -> 0x0071 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean drawGlyphBodyWithColor(com.cdv.text.NvAndroidTextLayout r3, int r4, int r5, int r6, android.graphics.LinearGradient r7, float r8, float r9, android.graphics.Canvas r10, int r11, boolean r12, boolean r13) {
        /*
        // Method dump skipped, instructions count: 144
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.text.NvAndroidTextCacheGenerator.drawGlyphBodyWithColor(com.cdv.text.NvAndroidTextLayout, int, int, int, android.graphics.LinearGradient, float, float, android.graphics.Canvas, int, boolean, boolean):boolean");
        return r12;
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x004d A[Catch:{ Exception -> 0x0071 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x006b A[Catch:{ Exception -> 0x0071 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean drawGlyphDoubleOutlineWithSolidColor(com.cdv.text.NvAndroidTextLayout r3, int r4, int r5, float r6, int r7, float r8, float r9, android.graphics.Canvas r10, int r11, boolean r12, boolean r13) {
        /*
        // Method dump skipped, instructions count: 144
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.text.NvAndroidTextCacheGenerator.drawGlyphDoubleOutlineWithSolidColor(com.cdv.text.NvAndroidTextLayout, int, int, float, int, float, float, android.graphics.Canvas, int, boolean, boolean):boolean");
        return r12;
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x004e A[Catch:{ Exception -> 0x00c0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0056 A[Catch:{ Exception -> 0x00c0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0080 A[Catch:{ Exception -> 0x00c0 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean drawGlyphOutlineWithColor(com.cdv.text.NvAndroidTextLayout r3, int r4, int r5, float r6, int r7, android.graphics.LinearGradient r8, boolean r9, float r10, float r11, android.graphics.Canvas r12, int r13, boolean r14, boolean r15) {
        /*
        // Method dump skipped, instructions count: 223
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.text.NvAndroidTextCacheGenerator.drawGlyphOutlineWithColor(com.cdv.text.NvAndroidTextLayout, int, int, float, int, android.graphics.LinearGradient, boolean, float, float, android.graphics.Canvas, int, boolean, boolean):boolean");
        return r9;
    }

    private Paint selectPaint(NvAndroidTextLayout nvAndroidTextLayout, boolean z) {
        Layout layout = nvAndroidTextLayout.getLayout();
        if (layout == null) {
            return null;
        }
        TextPaint paint = layout.getPaint();
        if (!z) {
            return paint;
        }
        Paint paint2 = new Paint();
        paint2.setTypeface(paint.getTypeface());
        paint2.setTextSize(paint.getTextSize());
        paint2.setFakeBoldText(paint.isFakeBoldText());
        paint2.setTextSkewX(paint.getTextSkewX());
        if (Build.VERSION.SDK_INT >= 21) {
            paint2.setLetterSpacing(paint.getLetterSpacing());
        }
        return paint2;
    }
}
