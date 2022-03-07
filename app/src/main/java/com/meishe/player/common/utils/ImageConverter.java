package com.meishe.player.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageConverter {
    private static final String TAG = "Image Converter";
    private static final String assetName = "assets:/";
    private static int m_videoEditHeight = 720;
    private static int m_videoEditWidth = 1280;
    private static final String waterMarkPath = "assets:/water_mark/";
    private static final String water_Mark = "water_mark/";

    public static Bitmap convertImage(Context context, String str) {
        Bitmap bitmap;
        if (str == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        Matrix matrix = new Matrix();
        if (str.contains(waterMarkPath)) {
            bitmap = getBitmapFromAssetFile(context, str);
        } else {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            if (options.outWidth == 0 || options.outHeight == 0) {
                return null;
            }
            Point calcConvertedImageSize = calcConvertedImageSize(options.outWidth, options.outHeight);
            options.inSampleSize = calculateInSampleSize(options, calcConvertedImageSize.x, calcConvertedImageSize.y);
            int i = 0;
            options.inJustDecodeBounds = false;
            Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
            if (decodeFile == null) {
                return null;
            }
            if (options.outMimeType.equals("image/jpeg")) {
                i = detectImageRotation(str);
            }
            if (!(calcConvertedImageSize.x == decodeFile.getWidth() && calcConvertedImageSize.y == decodeFile.getHeight())) {
                matrix.postScale(((float) calcConvertedImageSize.x) / ((float) decodeFile.getWidth()), ((float) calcConvertedImageSize.y) / ((float) decodeFile.getHeight()));
            }
            if (i != 0) {
                matrix.postRotate((float) i);
            }
            bitmap = decodeFile;
        }
        if (bitmap == null) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap convertImageScaleByWidth(Context context, String str, int i) {
        Bitmap bitmap;
        if (str == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        Matrix matrix = new Matrix();
        if (str.contains(waterMarkPath)) {
            Bitmap bitmapFromAssetFile = getBitmapFromAssetFile(context, str);
            float width = ((float) i) / ((float) bitmapFromAssetFile.getWidth());
            matrix.postScale(width, width);
            bitmap = bitmapFromAssetFile;
        } else {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            if (options.outWidth == 0 || options.outHeight == 0) {
                return null;
            }
            Point calcConvertedImageSize = calcConvertedImageSize(options.outWidth, options.outHeight);
            options.inSampleSize = calculateInSampleSize(options, calcConvertedImageSize.x, calcConvertedImageSize.y);
            int i2 = 0;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(str, options);
            if (bitmap == null) {
                return null;
            }
            if (options.outMimeType.equals("image/jpeg")) {
                i2 = detectImageRotation(str);
            }
            if (!(calcConvertedImageSize.x == bitmap.getWidth() && calcConvertedImageSize.y == bitmap.getHeight())) {
                matrix.postScale(((float) calcConvertedImageSize.x) / ((float) bitmap.getWidth()), ((float) calcConvertedImageSize.y) / ((float) bitmap.getHeight()));
            }
            if (i2 != 0) {
                matrix.postRotate((float) i2);
            }
        }
        if (bitmap == null) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Point calcConvertedImageSize(int i, int i2) {
        double d;
        double d2;
        int i3 = m_videoEditWidth;
        int i4 = m_videoEditHeight;
        if (i < i2 ? i3 > i4 : i3 < i4) {
            i4 = i3;
            i3 = i4;
        }
        if (i > i3) {
            double d3 = (double) i3;
            double d4 = (double) i;
            Double.isNaN(d3);
            Double.isNaN(d4);
            d = d3 / d4;
        } else {
            d = 1.0d;
        }
        if (i2 > i4) {
            double d5 = (double) i4;
            double d6 = (double) i2;
            Double.isNaN(d5);
            Double.isNaN(d6);
            d2 = d5 / d6;
        } else {
            d2 = 1.0d;
        }
        if (d < 1.0d || d2 < 1.0d) {
            double min = Math.min(d, d2);
            double d7 = (double) i;
            Double.isNaN(d7);
            i = (int) ((d7 * min) + 0.5d);
            double d8 = (double) i2;
            Double.isNaN(d8);
            i2 = (int) ((d8 * min) + 0.5d);
        }
        return new Point(i, i2);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 >= i2 && i7 / i5 >= i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    private static int detectImageRotation(String str) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(str);
        } catch (Exception e) {
            e.printStackTrace();
            exifInterface = null;
        }
        if (exifInterface == null) {
            return 0;
        }
        int attributeInt = exifInterface.getAttributeInt("Orientation", 0);
        if (attributeInt == 3) {
            return 180;
        }
        if (attributeInt == 6) {
            return 90;
        }
        if (attributeInt != 8) {
            return 0;
        }
        return 270;
    }

    private static boolean saveBitmap(Bitmap bitmap, String str) {
        File file = new File(str);
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Point getPicturePoint(String str, Context context) {
        if (str != null) {
            if (str.contains(assetName)) {
                Bitmap bitmapFromAssetFile = getBitmapFromAssetFile(context, str);
                int width = bitmapFromAssetFile.getWidth();
                int height = bitmapFromAssetFile.getHeight();
                bitmapFromAssetFile.recycle();
                return new Point(width, height);
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            if (!(options.outWidth == 0 || options.outHeight == 0)) {
                Point point = new Point(options.outWidth, options.outHeight);
                options.inJustDecodeBounds = false;
                return point;
            }
        }
        return null;
    }

    private static Bitmap getBitmapFromAssetFile(Context context, String str) {
        if (str == null || !str.contains(waterMarkPath)) {
            return null;
        }
        String substring = str.substring(str.lastIndexOf("/") + 1, str.length());
        try {
            return BitmapFactory.decodeStream(context.getResources().getAssets().open(water_Mark + substring));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "convertImage: !!!!!!!!!!!!!!!   " + e.getMessage());
            return null;
        }
    }
}
