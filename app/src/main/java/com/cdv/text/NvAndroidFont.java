package com.cdv.text;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.Log;

public class NvAndroidFont {
    private static final String TAG = "NvAndroidFont";
    private static final Object m_typefaceMutex = new Object();
    private static final boolean m_verbose = false;

    private static int getTypefaceStyle(int i, boolean z) {
        int i2 = i > 500 ? 1 : 0;
        return z ? i2 | 2 : i2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0017 A[Catch:{ Exception -> 0x000a }, RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0018 A[Catch:{ Exception -> 0x000a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Typeface createTypeface(java.lang.String r3, int r4, boolean r5) {
        /*
            r0 = 0
            if (r3 == 0) goto L_0x000c
            boolean r1 = r3.isEmpty()     // Catch:{ Exception -> 0x000a }
            if (r1 == 0) goto L_0x000d
            goto L_0x000c
        L_0x000a:
            r3 = move-exception
            goto L_0x0024
        L_0x000c:
            r3 = r0
        L_0x000d:
            int r1 = getTypefaceStyle(r4, r5)     // Catch:{ Exception -> 0x000a }
            android.graphics.Typeface r3 = android.graphics.Typeface.create(r3, r1)     // Catch:{ Exception -> 0x000a }
            if (r3 != 0) goto L_0x0018
            return r0
        L_0x0018:
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x000a }
            r2 = 28
            if (r1 >= r2) goto L_0x001f
            return r3
        L_0x001f:
            android.graphics.Typeface r3 = android.graphics.Typeface.create(r3, r4, r5)     // Catch:{ Exception -> 0x000a }
            return r3
        L_0x0024:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r5 = r3.getMessage()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            java.lang.String r5 = "NvAndroidFont"
            android.util.Log.e(r5, r4)
            r3.printStackTrace()
            return r0
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.text.NvAndroidFont.createTypeface(java.lang.String, int, boolean):android.graphics.Typeface");
    }

    public static Typeface createTypefaceFromFile(Context context, String str, int i, boolean z) {
        Typeface typeface;
        Typeface create;
        try {
            if (str.startsWith("assets:/")) {
                typeface = Typeface.createFromAsset(context.getAssets(), str.substring(8));
            } else {
                typeface = Typeface.createFromFile(str);
            }
            if (Build.VERSION.SDK_INT >= 28) {
                return Typeface.create(typeface, i, z);
            }
            int typefaceStyle = getTypefaceStyle(i, z);
            synchronized (m_typefaceMutex) {
                create = Typeface.create(typeface, typefaceStyle);
            }
            return create;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static TextPaint createTextPaint(Typeface typeface, float f, int i, boolean z, float f2, boolean z2) {
        float f3;
        try {
            TextPaint textPaint = new TextPaint();
            textPaint.setTypeface(typeface);
            textPaint.setTextSize(f);
            if (i > 500 && !typeface.isBold()) {
                textPaint.setFakeBoldText(true);
            }
            if (z && !typeface.isItalic()) {
                textPaint.setTextSkewX(-0.25f);
            }
            float f4 = 100.0f;
            if ((!z2 || f2 == 0.0f) && (z2 || f2 == 100.0f)) {
                f3 = 0.0f;
            } else {
                if (z2) {
                    f4 = textPaint.measureText("X", 0, 1);
                } else {
                    f2 -= 100.0f;
                }
                f3 = f2 / f4;
            }
            if (f3 != 0.0f && Build.VERSION.SDK_INT >= 21) {
                textPaint.setLetterSpacing(f3);
            }
            return textPaint;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static float measureCharWidth(TextPaint textPaint, char c) {
        try {
            return textPaint.measureText(new char[]{c}, 0, 1);
        } catch (Exception unused) {
            return 0.0f;
        }
    }
}
