package com.meishe.myvideo.ui.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsVideoFrameRetriever;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.myvideoapp.R;

public class ClipBitmapUtil {
    public static Bitmap getBitmapFromClipInfo(Context context, MeicamVideoClip meicamVideoClip) {
        String filePath = meicamVideoClip.getFilePath();
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        long trimIn = meicamVideoClip.getTrimIn();
        if (trimIn < 0) {
            trimIn = 0;
        }
        NvsStreamingContext instance = NvsStreamingContext.getInstance();
        if (context == null) {
            return null;
        }
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R.dimen.line_view_bitmap_width);
        int dimensionPixelOffset2 = context.getResources().getDimensionPixelOffset(R.dimen.line_view_bitmap_height);
        NvsAVFileInfo aVFileInfo = instance.getAVFileInfo(filePath);
        if (aVFileInfo == null) {
            return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.lineview_bitmap_cover), dimensionPixelOffset, dimensionPixelOffset2, true);
        }
        if (aVFileInfo.getAVFileType() == 2) {
            return centerSquareScaleBitmap(BitmapFactory.decodeFile(filePath), dimensionPixelOffset, dimensionPixelOffset2);
        }
        NvsVideoFrameRetriever createVideoFrameRetriever = instance.createVideoFrameRetriever(filePath);
        if (createVideoFrameRetriever == null) {
            return null;
        }
        Bitmap centerSquareScaleBitmap = centerSquareScaleBitmap(createVideoFrameRetriever.getFrameAtTime(trimIn, 1), dimensionPixelOffset, dimensionPixelOffset2);
        createVideoFrameRetriever.release();
        return centerSquareScaleBitmap;
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int i, int i2) {
        float f;
        int i3;
        if (bitmap == null || i <= 0 || i2 <= 0) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= i || height <= i2) {
            return bitmap;
        }
        float f2 = (float) width;
        float f3 = f2 / ((float) i);
        float f4 = (float) height;
        float f5 = f4 / ((float) i2);
        if (f3 > f5) {
            i3 = (int) (f2 / f5);
            f = f4 / f5;
        } else {
            i3 = (int) (f2 / f3);
            f = f4 / f3;
        }
        int i4 = (int) f;
        try {
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i3, i4, true);
            Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap, (i3 - i) / 2, (i4 - i2) / 2, i, i2);
            createScaledBitmap.recycle();
            return createBitmap;
        } catch (Exception unused) {
            return null;
        }
    }
}
