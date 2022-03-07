package com.meishe.engine.util;

import android.graphics.Color;
import com.meicam.sdk.NvsColor;

public class ColorUtil {
    public static final int BLACK_MAGIC_COLOR = Color.parseColor("#805c00fc");
    public static final int FLICKER_AND_WHITE_COLOR = Color.parseColor("#80FF0000");
    public static final int HALLUCINATION_COLOR = Color.parseColor("#80ff4d97");
    public static final int IMAGE_COLOR = Color.parseColor("#8000fce0");
    public static final int NEON_COLOR = Color.parseColor("#8032CD32");
    public static final int SHAKE_COLOR = Color.parseColor("#80fcb600");
    public static final int SOUL_COLOR = Color.parseColor("#8000abfc");
    public static final int WAVE_COLOR = Color.parseColor("#50f8fc00");
    public static final int ZOOM_COLOR = Color.parseColor("#800B1746");
    public static String[] code = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static NvsColor colorStringtoNvsColor(String str) {
        if (str == null) {
            return null;
        }
        if (str != null && str.isEmpty()) {
            return null;
        }
        NvsColor nvsColor = new NvsColor(1.0f, 1.0f, 1.0f, 1.0f);
        int parseColor = Color.parseColor(str);
        nvsColor.a = ((float) ((-16777216 & parseColor) >>> 24)) / 255.0f;
        nvsColor.r = ((float) ((16711680 & parseColor) >> 16)) / 255.0f;
        nvsColor.g = ((float) ((65280 & parseColor) >> 8)) / 255.0f;
        nvsColor.b = ((float) (parseColor & 255)) / 255.0f;
        return nvsColor;
    }

    public static NvsColor colorFloatToNvsColor(float[] fArr) {
        if (fArr == null || fArr.length != 4) {
            return null;
        }
        return new NvsColor(fArr[0], fArr[1], fArr[2], fArr[3]);
    }

    public static int[] nvsColortoRgba(NvsColor nvsColor) {
        int[] iArr = {255, 255, 255, 255};
        if (nvsColor == null) {
            return iArr;
        }
        double d = (double) (nvsColor.r * 255.0f);
        Double.isNaN(d);
        int floor = (int) Math.floor(d + 0.5d);
        double d2 = (double) (nvsColor.g * 255.0f);
        Double.isNaN(d2);
        double d3 = (double) (nvsColor.b * 255.0f);
        Double.isNaN(d3);
        int floor2 = (int) Math.floor(d3 + 0.5d);
        double d4 = (double) (nvsColor.a * 255.0f);
        Double.isNaN(d4);
        iArr[0] = (int) Math.floor(d4 + 0.5d);
        iArr[1] = floor;
        iArr[2] = (int) Math.floor(d2 + 0.5d);
        iArr[3] = floor2;
        for (int i = 0; i < iArr.length; i++) {
            if (iArr[i] < 0) {
                iArr[i] = 0;
            } else if (iArr[i] > 255) {
                iArr[i] = 255;
            }
        }
        return iArr;
    }

    public static String nvsColorToHexString(NvsColor nvsColor) {
        int[] nvsColortoRgba = nvsColortoRgba(nvsColor);
        String str = "#";
        for (int i : nvsColortoRgba) {
            str = (str + code[i / 16]) + code[i % 16];
        }
        return str;
    }

    public static String intColorToHexString(int i) {
        return "#" + toHexString(Color.alpha(i)) + toHexString(Color.red(i)) + toHexString(Color.green(i)) + toHexString(Color.blue(i));
    }

    private static String toHexString(int i) {
        return code[i / 16] + code[i % 16] + "";
    }

    public static NvsColor getNvsColor(int i) {
        return new NvsColor((float) ((16711680 & i) >> 16), (float) ((65280 & i) >> 8), (float) (i & 255), (float) (i >>> 24));
    }

    public static float[] getColorArray(NvsColor nvsColor) {
        if (nvsColor == null) {
            return null;
        }
        return new float[]{nvsColor.r, nvsColor.g, nvsColor.b, nvsColor.a};
    }
}
