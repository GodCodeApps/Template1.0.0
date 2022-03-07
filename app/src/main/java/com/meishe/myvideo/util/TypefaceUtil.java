package com.meishe.myvideo.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class TypefaceUtil {
    private static TypefaceUtil mTypefaceUtil;
    private Context mContext;

    private TypefaceUtil(Context context) {
        this.mContext = context;
    }

    public static TypefaceUtil getInstance(Context context) {
        if (mTypefaceUtil == null) {
            mTypefaceUtil = new TypefaceUtil(context);
        }
        return mTypefaceUtil;
    }

    private Typeface getTypefaceFromTTF(String str) {
        if (str == null) {
            return Typeface.DEFAULT;
        }
        return Typeface.createFromAsset(this.mContext.getAssets(), str);
    }

    public void setBold(TextView textView, boolean z) {
        textView.getPaint().setFakeBoldText(z);
    }

    public void setDefaultTypeFace(TextView textView, boolean z) {
        textView.setTypeface(Typeface.DEFAULT);
        setBold(textView, z);
    }

    public void setDefaultTypeFace(TextView textView) {
        textView.setTypeface(Typeface.DEFAULT);
    }

    public void setTypeface(TextView textView, String str) {
        textView.setTypeface(getTypefaceFromTTF(str));
    }
}
