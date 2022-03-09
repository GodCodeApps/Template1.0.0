package com.meishe.draft.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.meishe.common.utils.Logger;
import com.meishe.draft.data.DraftFileData;
import com.meishe.myvideo.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class DraftFileUtil {
    private static final String IMAGE_BACKGROUND_DRAFT = "draft";
    private static final String TAG = "DraftFileUtil";

    public static void copyFile(File file, File file2) throws IOException {
        Throwable th;
        BufferedInputStream bufferedInputStream;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            try {
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(file2));
                try {
                    byte[] bArr = new byte[5120];
                    while (true) {
                        int read = bufferedInputStream.read(bArr);
                        if (read != -1) {
                            bufferedOutputStream2.write(bArr, 0, read);
                        } else {
                            bufferedOutputStream2.flush();
                            close(bufferedInputStream);
                            close(bufferedOutputStream2);
                            return;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedOutputStream = bufferedOutputStream2;
                    close(bufferedInputStream);
                    close(bufferedOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                close(bufferedInputStream);
                close(bufferedOutputStream);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            bufferedInputStream = null;
            close(bufferedInputStream);
            close(bufferedOutputStream);
        }
    }

    public static String getDirPath(Context context, long j) {
        File externalFilesDir = context.getApplicationContext().getExternalFilesDir(null);
        if (externalFilesDir == null) {
            return null;
        }
        String str = externalFilesDir.getAbsolutePath() + File.separator + IMAGE_BACKGROUND_DRAFT + File.separator + j;
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        return str;
    }

    public static String getDraftDirPath(Context context) {
        File externalFilesDir = context.getApplicationContext().getExternalFilesDir(null);
        if (externalFilesDir == null) {
            return null;
        }
        return externalFilesDir.getAbsolutePath() + File.separator + IMAGE_BACKGROUND_DRAFT;
    }

    public static String copyALL(Context context, List<DraftFileData> list, String str) {
        String[] split;
        String[] split2;
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        File file = new File(str);
        if (file.exists()) {
            clearDir(file);
        } else if (!file.mkdirs()) {
            return null;
        }
        if (list != null && !list.isEmpty()) {
            for (DraftFileData draftFileData : list) {
                String str2 = draftFileData.path;
                if (!TextUtils.isEmpty(str2) && (split = str2.split("/")) != null && split.length >= 1) {
                    File file2 = new File(file, split[split.length - 1]);
                    if (file2.exists()) {
                        file2.delete();
                    }
                    if (draftFileData.path.contains("assets")) {
                        copyAssetAndWrite(context, str2.replaceAll("assets:/", ""), file2);
                    } else {
                        File file3 = new File(str2);
                        if (file3.exists() || !file3.mkdir()) {
                            try {
                                copyFile(file3, file2);
                            } catch (IOException e) {
                                Logger.e(TAG, "copyPath error: " + e.fillInStackTrace());
                            }
                            String str3 = draftFileData.licPath;
                            if (!TextUtils.isEmpty(str3) && (split2 = str3.split("/")) != null && split2.length >= 1) {
                                File file4 = new File(file, split2[split2.length - 1]);
                                if (file4.exists()) {
                                    file4.delete();
                                }
                                File file5 = new File(str3);
                                if (file5.exists() || !file5.mkdir()) {
                                    try {
                                        copyFile(file5, file4);
                                    } catch (IOException e2) {
                                        Logger.e(TAG, "copyLicPath error: " + e2.fillInStackTrace());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return file.getAbsolutePath();
    }

    private static boolean copyAssetAndWrite(Context context, String str, File file) {
        try {
            InputStream open = context.getAssets().open(str);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.flush();
                    open.close();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "error :" + e.fillInStackTrace());
            return false;
        }
    }

    public static String getStringFromSDcardFile(String str) {
        Throwable th;
        BufferedReader bufferedReader;
        InputStreamReader inputStreamReader;
        IOException e;
        InputStreamReader inputStreamReader2;
        StringBuilder sb = new StringBuilder();
        try {
            try {
                inputStreamReader2 = new InputStreamReader(new FileInputStream(new File(str)), "utf-8");
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                inputStreamReader2 = null;
            }
            try {
                BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader2);
                try {
                    while (true) {
                        String readLine = bufferedReader2.readLine();
                        if (readLine != null) {
                            sb.append(readLine);
                            sb.append("\n");
                        } else {
                            String sb2 = sb.toString();
                            close(null);
                            close(inputStreamReader2);
                            close(bufferedReader2);
                            return sb2;
                        }
                    }
                } catch (IOException e3) {
                    inputStreamReader = inputStreamReader2;
                    e = e3;
                    bufferedReader = bufferedReader2;
                    try {
                        e.printStackTrace();
                        close(null);
                        close(inputStreamReader);
                        close(bufferedReader);
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        close(null);
                        close(inputStreamReader);
                        close(bufferedReader);
                        throw th;
                    }
                } catch (Throwable th3) {
                    inputStreamReader = inputStreamReader2;
                    th = th3;
                    bufferedReader = bufferedReader2;
                    close(null);
                    close(inputStreamReader);
                    close(bufferedReader);
                    throw th;
                }
            } catch (IOException e4) {
                bufferedReader = null;
                inputStreamReader = inputStreamReader2;
                e = e4;
                e.printStackTrace();
                close(null);
                close(inputStreamReader);
                close(bufferedReader);
                return null;
            } catch (Throwable th4) {
                bufferedReader = null;
                inputStreamReader = inputStreamReader2;
                th = th4;
                close(null);
                close(inputStreamReader);
                close(bufferedReader);
                throw th;
            }
        } catch (IOException e5) {
            e = e5;
            inputStreamReader = null;
            bufferedReader = null;
            e.printStackTrace();
            close(null);
            close(inputStreamReader);
            close(bufferedReader);
            return null;
        } catch (Throwable th5) {
            th = th5;
            inputStreamReader = null;
            bufferedReader = null;
            close(null);
            close(inputStreamReader);
            close(bufferedReader);
        }
        return sb.toString();
    }

    public static void deleteFolder(String str) {
        File[] listFiles;
        try {
            File file = new File(str);
            if (file.exists()) {
                if (file.isDirectory()) {
                    for (File file2 : file.listFiles()) {
                        deleteFolder(file2.getAbsolutePath());
                    }
                    file.delete();
                } else if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "deleteFolder error :" + e.fillInStackTrace());
        }
    }

    public static String saveConfigJson(String str, String str2) {
        return saveToSDCard("config", str, "json", str2);
    }

    public static String saveToSDCard(String paramString1, String paramString2, String paramString3, String paramString4) {
        boolean bool = Environment.getExternalStorageState().equals("mounted");
        String str1 = null;
        String str2 = null;
        if (!bool)
            return null;
        File file = new File(paramString4);
        if (!file.exists() && !file.mkdirs())
            return null;
        FileOutputStream fileOutputStream = null;
        File file1 = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(paramString1);
            stringBuilder.append(".");
            stringBuilder.append(paramString3);
            file1 = new File(paramString4,stringBuilder.toString());
            if (file1.exists()) {
                file1.delete();
            }
            file1.createNewFile();
            fileOutputStream = new FileOutputStream(file1);
            fileOutputStream.write(paramString2.getBytes());
            fileOutputStream.close();
        } catch (Exception exception) {

        } finally {
            paramString3 = str2;
            if (paramString3 != null)
                try {
                    fileOutputStream.close();
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                }
        }
        return file1.getAbsolutePath();
    }

    public static void renameFile(String str, String str2) {
        File file = new File(str);
        File file2 = new File(str2);
        if (file.exists()) {
            file.renameTo(file2);
        }
    }

    public static long getFileSize(String str) {
        Throwable th;
        Exception e;
        FileInputStream fileInputStream = null;
        try {
            File file = new File(str);
            if (!file.exists() || !file.isFile()) {
                Logger.d("file doesn't exist or is not a file");
                close(fileInputStream);
                return -1;
            }
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                long size = fileInputStream2.getChannel().size();
                close(fileInputStream2);
                return size;
            } catch (Exception e2) {
                fileInputStream = fileInputStream2;
                e = e2;
                try {
                    Logger.e("getFileSize: error: " + e.fillInStackTrace());
                    close(fileInputStream);
                    return -1;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                fileInputStream = fileInputStream2;
                th = th3;
                close(fileInputStream);
            }
        } catch (Exception e3) {
            e = e3;
            Logger.e("getFileSize: error: " + e.fillInStackTrace());
            close(fileInputStream);
            return -1;
        }
        return 0;
    }

    public static String getPrintSize(long j) {
        if (j < 1024) {
            return j + "B";
        }
        long j2 = j / 1024;
        if (j2 < 1024) {
            return j2 + "KB";
        }
        long j3 = j2 / 1024;
        if (j3 < 1024) {
            long j4 = j3 * 100;
            return (j4 / 100) + "." + (j4 % 100) + "M";
        }
        long j5 = (j3 * 100) / 1024;
        return (j5 / 100) + "." + (j5 % 100) + "G";
    }

    private static void clearDir(File file) {
        File[] listFiles;
        if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
            for (File file2 : listFiles) {
                deleteDirectory(file2);
            }
        }
    }

    private static void deleteDirectory(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                deleteDirectory(file2);
            }
            file.delete();
        }
    }

    public static <T extends Closeable> void close(T t) {
        if (t != null) {
            try {
                t.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
