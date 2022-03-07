package com.cdv.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.media.MediaCodecInfo;
import android.net.Uri;
import android.os.Build;

public class NvAndroidUtils {
    private static final String TAG = "Meicam";

    public static long getSystemMemorySizeInBytes(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0078  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.media.MediaExtractor createMediaExtractorFromMediaFilePath(android.content.Context r8, java.lang.String r9) {
        /*
        // Method dump skipped, instructions count: 124
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.utils.NvAndroidUtils.createMediaExtractorFromMediaFilePath(android.content.Context, java.lang.String):android.media.MediaExtractor");
        return null;
    }

    public static int openFdForContentUrl(Context context, String str, String str2) {
        try {
            return context.getContentResolver().openFileDescriptor(Uri.parse(str), str2).detachFd();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getMimeTypeFromContentUrl(Context context, String str) {
        try {
            return context.getContentResolver().getType(Uri.parse(str));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int GetLevelSupportedFromProfile(String str, MediaCodecInfo mediaCodecInfo, int i) {
        if (mediaCodecInfo == null) {
            return -1;
        }
        try {
            MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
            if (capabilitiesForType == null) {
                return -1;
            }
            MediaCodecInfo.CodecProfileLevel[] codecProfileLevelArr = capabilitiesForType.profileLevels;
            int i2 = 0;
            for (MediaCodecInfo.CodecProfileLevel codecProfileLevel : codecProfileLevelArr) {
                if (codecProfileLevel.profile == i && codecProfileLevel.level > i2) {
                    i2 = codecProfileLevel.level;
                }
            }
            MediaCodecInfo.CodecCapabilities codecCapabilities = null;
            if (Build.VERSION.SDK_INT >= 21) {
                codecCapabilities = MediaCodecInfo.CodecCapabilities.createFromProfileLevel(str, i, i2);
            }
            if (codecCapabilities != null && codecCapabilities.profileLevels.length > 0) {
                return codecCapabilities.profileLevels[0].level;
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
