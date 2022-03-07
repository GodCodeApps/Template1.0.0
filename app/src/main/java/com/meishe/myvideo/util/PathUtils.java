package com.meishe.myvideo.util;

import android.content.Context;
import android.os.Environment;
import com.meishe.common.utils.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PathUtils {
    private static String SDK_FILE_ROOT_DIRECTORY = ("MYVideo" + File.separator);
    private static String ASSET_DOWNLOAD_DIRECTORY = (SDK_FILE_ROOT_DIRECTORY + "Asset");
    private static String ASSET_DOWNLOAD_DIRECTORY_ANIMATEDSTICKER = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "AnimatedSticker");
    private static String ASSET_DOWNLOAD_DIRECTORY_ARSCENE = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "ArScene");
    private static String ASSET_DOWNLOAD_DIRECTORY_CAPTION = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "Caption");
    private static String ASSET_DOWNLOAD_DIRECTORY_CAPTURE_SCENE = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "CaptureScene");
    private static String ASSET_DOWNLOAD_DIRECTORY_COMPILE = null;
    private static String ASSET_DOWNLOAD_DIRECTORY_COMPOUND_CAPTION = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "CompoundCaption");
    private static String ASSET_DOWNLOAD_DIRECTORY_CUSTOM_ANIMATED_STICKER = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "CustomAnimatedSticker");
    private static String ASSET_DOWNLOAD_DIRECTORY_EFFECT_DREAM = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "dream");
    private static String ASSET_DOWNLOAD_DIRECTORY_EFFECT_FRAME = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "frame");
    private static String ASSET_DOWNLOAD_DIRECTORY_EFFECT_LIVELY = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "lively");
    private static String ASSET_DOWNLOAD_DIRECTORY_EFFECT_SHAKING = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "shaking");
    private static String ASSET_DOWNLOAD_DIRECTORY_FACE1_STICKER = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "Face1Sticker");
    private static String ASSET_DOWNLOAD_DIRECTORY_FACE_STICKER = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "FaceSticker");
    private static String ASSET_DOWNLOAD_DIRECTORY_FILTER = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "Filter");
    private static String ASSET_DOWNLOAD_DIRECTORY_FONT = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "Font");
    private static String ASSET_DOWNLOAD_DIRECTORY_GIFCONVERT = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "GifConvert");
    private static String ASSET_DOWNLOAD_DIRECTORY_PARTICLE = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "Particle");
    private static String ASSET_DOWNLOAD_DIRECTORY_PHOTO_ALBUM = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "PhotoAlbum");
    private static String ASSET_DOWNLOAD_DIRECTORY_SUPER_ZOOM = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "Meicam");
    private static String ASSET_DOWNLOAD_DIRECTORY_THEME = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "Theme");
    private static String ASSET_DOWNLOAD_DIRECTORY_TRANSITION = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "Transition");
    private static String ASSET_DOWNLOAD_VIDEO_TRANSITION_3D = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "Transtion3D");
    private static String ASSET_DOWNLOAD_VIDEO_TRANSITION_EFFECT = (ASSET_DOWNLOAD_DIRECTORY + File.separator + "TranstionEffect");
    private static String AUDIO_RECORD_DIRECTORY = (SDK_FILE_ROOT_DIRECTORY + "AudioRecord");
    private static String CRASH_LOG_DIRECTORY = (SDK_FILE_ROOT_DIRECTORY + "Log");
    private static final String IMAGE_BACKGROUND_FOLDER = "imageBackground";
    private static final String TAG = "com.meishe.myvideo.util.PathUtils";
    private static String VIDEO_CONVERT_DIRECTORY = (SDK_FILE_ROOT_DIRECTORY + "VideoConvert");
    private static String VIDEO_FREEZR_DIRECTORY = (SDK_FILE_ROOT_DIRECTORY + "VideoFreeze");
    private static String WATERMARK_CAF_DIRECTORY;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(SDK_FILE_ROOT_DIRECTORY);
        sb.append("Compile");
        ASSET_DOWNLOAD_DIRECTORY_COMPILE = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(SDK_FILE_ROOT_DIRECTORY);
        sb2.append("WaterMark");
        WATERMARK_CAF_DIRECTORY = sb2.toString();
    }

    public static String getAudioRecordFilePath() {
        return getFolderDirPath(AUDIO_RECORD_DIRECTORY);
    }

    public static String getFolderDirPath(String str) {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        if (file.exists() || file.mkdirs()) {
            return file.getAbsolutePath();
        }
        String str2 = TAG;
        Logger.e(str2, "Failed to create file dir path--->" + str);
        return null;
    }

    public static String getAssetDownloadPath(int i) {
        String assetDownloadDirPath = getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY);
        if (assetDownloadDirPath == null) {
            return null;
        }
        switch (i) {
            case 1:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_THEME);
            case 2:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_FILTER);
            case 3:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_CAPTION);
            case 4:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_ANIMATEDSTICKER);
            case 5:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_TRANSITION);
            case 6:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_FONT);
            case 7:
            case 14:
            case 22:
            case 23:
            case 24:
            default:
                return assetDownloadDirPath;
            case 8:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_CAPTURE_SCENE);
            case 9:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_PARTICLE);
            case 10:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_FACE_STICKER);
            case 11:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_FACE1_STICKER);
            case 12:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_CUSTOM_ANIMATED_STICKER);
            case 13:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_SUPER_ZOOM);
            case 15:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_ARSCENE);
            case 16:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_COMPOUND_CAPTION);
            case 17:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_PHOTO_ALBUM);
            case 18:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_EFFECT_FRAME);
            case 19:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_EFFECT_DREAM);
            case 20:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_EFFECT_LIVELY);
            case 21:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_DIRECTORY_EFFECT_SHAKING);
            case 25:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_VIDEO_TRANSITION_3D);
            case 26:
                return getAssetDownloadDirPath(ASSET_DOWNLOAD_VIDEO_TRANSITION_EFFECT);
        }
    }

    private static String getAssetDownloadDirPath(String str) {
        return getFolderDirPath(str);
    }

    public static long getFileModifiedTime(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return 0;
        }
        return file.lastModified();
    }

    public static boolean unZipFile(String str, String str2) {
        try {
            ZipFile zipFile = new ZipFile(str);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            byte[] bArr = new byte[1024];
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                if (zipEntry.isDirectory()) {
                    String str3 = str2 + zipEntry.getName();
                    str3.trim();
                    new File(str3).mkdir();
                } else {
                    try {
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(getRealFileName(str2, zipEntry.getName())));
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                        while (true) {
                            int read = bufferedInputStream.read(bArr, 0, 1024);
                            if (read == -1) {
                                break;
                            }
                            bufferedOutputStream.write(bArr, 0, read);
                        }
                        bufferedInputStream.close();
                        bufferedOutputStream.close();
                    } catch (FileNotFoundException unused) {
                        return false;
                    }
                }
            }
            try {
                zipFile.close();
                return true;
            } catch (IOException unused2) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static File getRealFileName(String str, String str2) {
        String replace = str2.replace("\\", "/");
        String[] split = replace.split("/");
        File file = new File(str);
        if (split.length <= 1) {
            return new File(file, replace);
        }
        int i = 0;
        while (i < split.length - 1) {
            i++;
            file = new File(file, split[i]);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(file, split[split.length - 1]);
    }

    public static int getAssetVersionWithPath(String str) {
        String[] split = str.split("/");
        if (split.length > 0) {
            String[] split2 = split[split.length - 1].split(".");
            if (split2.length == 3) {
                return Integer.parseInt(split2[1]);
            }
        }
        return 1;
    }

    public static String getFileName(String str) {
        if (str == null || str.length() <= 0) {
            return str;
        }
        int indexOf = str.indexOf(46);
        return (indexOf <= -1 || indexOf >= str.length()) ? str : str.substring(str.lastIndexOf(47) + 1);
    }

    public static String getColorPath(Context context) {
        File externalFilesDir = context.getApplicationContext().getExternalFilesDir(null);
        if (externalFilesDir == null) {
            return null;
        }
        String str = externalFilesDir.getAbsolutePath() + File.separator + IMAGE_BACKGROUND_FOLDER;
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        return str;
    }

    public static String getWatermarkCafDirectoryDir() {
        return getFolderDirPath(WATERMARK_CAF_DIRECTORY);
    }

    public static String getVideoConvertDir() {
        return getFolderDirPath(VIDEO_CONVERT_DIRECTORY);
    }

    public static String getVideoFreezeConvertDir() {
        return getFolderDirPath(VIDEO_FREEZR_DIRECTORY);
    }

    public static String getVideoSavePath(String str) {
        File file = new File(getFolderDirPath(ASSET_DOWNLOAD_DIRECTORY_COMPILE), str);
        if (file.exists()) {
            file.delete();
        }
        return file.getAbsolutePath();
    }

    public static String getVideoSaveName() {
        return "MY_" + System.currentTimeMillis() + ".mp4";
    }

    public static String getLogDir() {
        return getFolderDirPath(CRASH_LOG_DIRECTORY);
    }
}
