package com.meishe.myvideo.ui.Utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class BorderRadiusDrawableUtil {
    public static Drawable getRadiusDrawable(int i, int i2, int i3, int i4) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (i4 != -1) {
            gradientDrawable.setColor(i4);
        }
        if (i3 > 0) {
            gradientDrawable.setCornerRadius((float) i3);
        }
        if (i > 0) {
            gradientDrawable.setStroke(i, i2);
        }
        return gradientDrawable;
    }
}
