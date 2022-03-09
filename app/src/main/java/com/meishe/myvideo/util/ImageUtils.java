package com.meishe.myvideo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.common.utils.Logger;
import com.meishe.myvideoapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class ImageUtils {
    private static final String TAG = "ImageUtils";

    public static void setImageByPath(ImageView imageView, String str) {
        Glide.with(imageView.getContext()).asBitmap().load(str).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.icon_feed_back_pic).dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)).into(imageView);
    }

    public static void setImageByPathAndWidth(ImageView imageView, String str, int i) {
        Glide.with(imageView.getContext()).asBitmap().load(str).apply(new RequestOptions().centerCrop().dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).override(i, i)).into(imageView);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x002c A[SYNTHETIC, Splitter:B:19:0x002c] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0042 A[SYNTHETIC, Splitter:B:27:0x0042] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getImageBase64Str(java.lang.String r3) {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0025 }
            r1.<init>(r3)     // Catch:{ IOException -> 0x0025 }
            int r3 = r1.available()     // Catch:{ IOException -> 0x001d, all -> 0x001b }
            byte[] r0 = new byte[r3]     // Catch:{ IOException -> 0x001d, all -> 0x001b }
            r1.read(r0)     // Catch:{ IOException -> 0x001d, all -> 0x001b }
            r1.close()     // Catch:{ IOException -> 0x001d, all -> 0x001b }
            r1.close()     // Catch:{ IOException -> 0x0016 }
            goto L_0x0035
        L_0x0016:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0035
        L_0x001b:
            r3 = move-exception
            goto L_0x0040
        L_0x001d:
            r3 = move-exception
            r2 = r1
            r1 = r0
            r0 = r2
            goto L_0x0027
        L_0x0022:
            r3 = move-exception
            r1 = r0
            goto L_0x0040
        L_0x0025:
            r3 = move-exception
            r1 = r0
        L_0x0027:
            r3.printStackTrace()     // Catch:{ all -> 0x0022 }
            if (r0 == 0) goto L_0x0034
            r0.close()     // Catch:{ IOException -> 0x0030 }
            goto L_0x0034
        L_0x0030:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0034:
            r0 = r1
        L_0x0035:
            r3 = 16
            java.lang.String r3 = android.util.Base64.encodeToString(r0, r3)
            java.lang.String r3 = zipBase64(r3)
            return r3
        L_0x0040:
            if (r1 == 0) goto L_0x004a
            r1.close()     // Catch:{ IOException -> 0x0046 }
            goto L_0x004a
        L_0x0046:
            r0 = move-exception
            r0.printStackTrace()
        L_0x004a:
            throw r3
        */

//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.myvideo.util.ImageUtils.getImageBase64Str(java.lang.String):java.lang.String");
        return r3;
    }

    public static String zipBase64(String str) {
        if (str == null || str.length() <= 0) {
            return str;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(str.getBytes());
            gZIPOutputStream.close();
            String byteArrayOutputStream2 = byteArrayOutputStream.toString("ISO-8859-1");
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return byteArrayOutputStream2;
        } catch (IOException e2) {
            e2.printStackTrace();
            try {
                byteArrayOutputStream.close();
                return null;
            } catch (IOException e3) {
                e3.printStackTrace();
                return null;
            }
        } catch (Throwable th) {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
    }

    public static String getColorImageByColor(Context context, String str) {
        String colorPath = PathUtils.getColorPath(context);
        File file = new File(colorPath + File.separator + str + ".png");
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public static String parseViewToBitmap(Context context, View view, String str) {
        String str2 = PathUtils.getColorPath(context) + File.separator + str + ".png";
        File file = new File(str2);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        Bitmap createBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(-1);
        view.draw(canvas);
        saveBitmap(createBitmap, str2);
        return str2;
    }

    public static void saveBitmap(Bitmap bitmap, String str) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap(Bitmap bitmap, String str, boolean z) {
        try {
            File file = new File(str);
            if (file.exists() || file.mkdirs()) {
                if (z) {
                    clearDir(file.getParentFile(), ".png");
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            Logger.e(TAG, "saveBitmap -> error: " + e.fillInStackTrace());
        }
    }

    private static void clearDir(File file, String str) {
        File[] listFiles;
        if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
            for (File file2 : listFiles) {
                if (file2.getName().contains(str)) {
                    Logger.d(TAG, "clearDir: name = " + file2.getName());
                    file2.delete();
                }
            }
        }
    }
}
