package com.meishe.effect;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import com.bumptech.glide.load.Key;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FileUtils {
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
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            AssetManager assets = context.getAssets();
            InputStream open = assets.open(str2 + File.separator + str);
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

    public static Map copyStickerIconFiles(Context context, String str) {
        String[] strArr;
        TreeMap treeMap = new TreeMap();
        String str2 = null;
        try {
            strArr = context.getAssets().list(str);
        } catch (IOException e) {
            e.printStackTrace();
            strArr = null;
        }
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            str2 = externalFilesDir.getAbsolutePath() + File.separator + str;
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdir();
            }
        }
        for (String str3 : strArr) {
            if (str3.indexOf(".png") != -1) {
                copyFileIfNeed(context, str3, str);
            }
        }
        File[] listFiles = new File(str2).listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (!listFiles[i].isDirectory()) {
                String absolutePath = listFiles[i].getAbsolutePath();
                listFiles[i].getPath();
                if (absolutePath.trim().toLowerCase().endsWith(".png") && absolutePath.indexOf("mode_") == -1) {
                    treeMap.put(getFileNameNoEx(listFiles[i].getName()), BitmapFactory.decodeFile(absolutePath));
                }
            }
        }
        return treeMap;
    }

    public static List copyStickerZipFiles(Context context, String str) {
        String[] strArr;
        ArrayList arrayList = new ArrayList();
        String str2 = null;
        try {
            strArr = context.getAssets().list(str);
        } catch (IOException e) {
            e.printStackTrace();
            strArr = null;
        }
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            str2 = externalFilesDir.getAbsolutePath() + File.separator + str;
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdir();
            }
        }
        for (String str3 : strArr) {
            if (str3.indexOf(".zip") != -1) {
                copyFileIfNeed(context, str3, str);
            }
        }
        File[] listFiles = new File(str2).listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (!listFiles[i].isDirectory()) {
                String absolutePath = listFiles[i].getAbsolutePath();
                listFiles[i].getPath();
                if (absolutePath.trim().toLowerCase().endsWith(".zip")) {
                    arrayList.add(absolutePath);
                }
            }
        }
        return arrayList;
    }

    public static String getFileNameNoEx(String str) {
        int lastIndexOf;
        return (str == null || str.length() <= 0 || (lastIndexOf = str.lastIndexOf(46)) < 0 || lastIndexOf >= str.length()) ? str : str.substring(0, lastIndexOf);
    }

    public static String getFilePath(Context context, String str) {
        File externalFilesDir = context.getApplicationContext().getExternalFilesDir(null);
        if (externalFilesDir == null) {
            return null;
        }
        return externalFilesDir.getAbsolutePath() + File.separator + str;
    }

    public static Map getStickerIconFilesFromSd(Context context, String str) {
        TreeMap treeMap = new TreeMap();
        String str2 = null;
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            str2 = externalFilesDir.getAbsolutePath() + File.separator + str;
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdir();
            }
        }
        File[] listFiles = new File(str2).listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (!listFiles[i].isDirectory()) {
                String absolutePath = listFiles[i].getAbsolutePath();
                listFiles[i].getPath();
                if (absolutePath.trim().toLowerCase().endsWith(".png") && absolutePath.indexOf("mode_") == -1) {
                    treeMap.put(getFileNameNoEx(listFiles[i].getName()), BitmapFactory.decodeFile(absolutePath));
                }
            }
        }
        return treeMap;
    }

    public static List getStickerNames(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        String str2 = null;
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            str2 = externalFilesDir.getAbsolutePath() + File.separator + str;
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdir();
            }
        }
        File[] listFiles = new File(str2).listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (!listFiles[i].isDirectory()) {
                String absolutePath = listFiles[i].getAbsolutePath();
                if (absolutePath.trim().toLowerCase().endsWith(".zip") && absolutePath.indexOf("filter") == -1) {
                    arrayList.add(getFileNameNoEx(listFiles[i].getName()));
                }
            }
        }
        return arrayList;
    }

    public static String getStickerZipFileFromSd(Context context, String str) {
        new ArrayList();
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir == null) {
            return null;
        }
        String str2 = externalFilesDir.getAbsolutePath() + File.separator + str;
        if (!new File(str2).exists()) {
            return null;
        }
        return str2;
    }

    public static List getStickerZipFilesFromSd(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        String str2 = null;
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            str2 = externalFilesDir.getAbsolutePath() + File.separator + str;
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdir();
            }
        }
        File[] listFiles = new File(str2).listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (!listFiles[i].isDirectory()) {
                String absolutePath = listFiles[i].getAbsolutePath();
                listFiles[i].getPath();
                if (absolutePath.trim().toLowerCase().endsWith(".zip")) {
                    arrayList.add(absolutePath);
                }
            }
        }
        return arrayList;
    }

    public static String readFile(String str, AssetManager assetManager) {
        InputStream inputStream;
        if (assetManager == null) {
            try {
                inputStream = new FileInputStream(new File(str));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        } else {
            inputStream = assetManager.open(str);
        }
        byte[] bArr = new byte[inputStream.available()];
        inputStream.read(bArr);
        inputStream.close();
        return new String(bArr, Key.STRING_CHARSET_NAME);
    }
}
