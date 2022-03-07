package com.meishe.myvideo.util;

import android.content.Context;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.meishe.common.utils.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static final String TAG = "FileUtil";
    private static String waterMarkCacheFileName = "/cache.txt";

    public static List<String> getFilesAllName(String str) {
        File[] listFiles = new File(str).listFiles();
        if (listFiles == null) {
            Logger.e( "empty directory");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (File file : listFiles) {
            arrayList.add(file.getAbsolutePath());
        }
        return arrayList;
    }

    public static boolean isFolderExists(String str) {
        File file = new File(str);
        if (file.exists() || file.mkdirs()) {
            return true;
        }
        return false;
    }

    public static boolean copyFileIfNeed(Context context, String str, String str2) {
        String filePath = getFilePath(context, str2 + File.separator + str);
        if (filePath == null) {
            return true;
        }
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        try {
            String str3 = null;
            File externalFilesDir = context.getApplicationContext().getExternalFilesDir(null);
            if (externalFilesDir != null) {
                str3 = externalFilesDir.getAbsolutePath() + File.separator + str2;
            }
            File file2 = new File(str3);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            InputStream open = context.getAssets().open(str2 + File.separator + str);
            if (open == null) {
                return false;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = open.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    open.close();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (IOException unused) {
            file.delete();
            return false;
        }
    }

    public static String getFilePath(Context context, String str) {
        File externalFilesDir = context.getApplicationContext().getExternalFilesDir(null);
        if (externalFilesDir == null) {
            return null;
        }
        return externalFilesDir.getAbsolutePath() + File.separator + str;
    }

    public static String readWaterMarkCacheFile() {
        String watermarkCafDirectoryDir = PathUtils.getWatermarkCafDirectoryDir();
        if (watermarkCafDirectoryDir == null) {
            Log.d(TAG, "水印 path is null!");
            return null;
        }
        File file = new File(watermarkCafDirectoryDir + waterMarkCacheFileName);
        if (!file.exists()) {
            Log.i(TAG, "水印草稿文件不存在！！！");
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            fileInputStream.close();
            return new String(bArr, Key.STRING_CHARSET_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeWaterMarkCacheFile(String str) {
        String watermarkCafDirectoryDir = PathUtils.getWatermarkCafDirectoryDir();
        if (watermarkCafDirectoryDir == null) {
            Log.d(TAG, "水印 path is null!");
        }
        File file = new File(watermarkCafDirectoryDir + waterMarkCacheFileName);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();
            Log.i(TAG, "水印草稿保存成功！！！");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "草稿文件创建失败！！！");
        }
    }
}
