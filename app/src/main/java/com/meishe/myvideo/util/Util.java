package com.meishe.myvideo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsVideoResolution;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideo.interfaces.TipsButtonClickListener;
import com.meishe.myvideo.view.CommonDialog;
import com.uc.crashsdk.export.LogType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Util {
    private static final int MIN_DELAY_TIME = 1000;
    private static final String TAG = "Util";
    private static long lastClickTime;

    public static boolean isFastClick() {
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = currentTimeMillis - lastClickTime < 1000;
        lastClickTime = currentTimeMillis;
        return z;
    }

    public static boolean isBackground(Context context) {
        String processName = getProcessName();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return true;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.processName.equals(processName) && runningAppProcessInfo.importance == 100) {
                return false;
            }
        }
        return true;
    }

    public static String getProcessName() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/proc/" + Process.myPid() + "/cmdline")));
            String trim = bufferedReader.readLine().trim();
            bufferedReader.close();
            return trim;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double convertToDouble(String str, double d) {
        if (TextUtils.isEmpty(str)) {
            return d;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception unused) {
            return d;
        }
    }

    public static void showDialog(Context context, final String str, final String str2, final String str3) {
        final CommonDialog commonDialog = new CommonDialog(context, 1);
        commonDialog.setOnCreateListener(new CommonDialog.OnCreateListener() {
            /* class com.meishe.myvideo.util.Util.AnonymousClass1 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnCreateListener
            public void OnCreated() {
                commonDialog.setTitleTxt(str);
                commonDialog.setFirstTipsTxt(str2);
                commonDialog.setSecondTipsTxt(str3);
            }
        });
        commonDialog.setOnBtnClickListener(new CommonDialog.OnBtnClickListener() {
            /* class com.meishe.myvideo.util.Util.AnonymousClass2 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnRightBtnClicked(View view) {
                commonDialog.dismiss();
            }

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnLeftBtnClicked(View view) {
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    public static void showDialog(Context context, final String str, final String str2) {
        final CommonDialog commonDialog = new CommonDialog(context, 1);
        commonDialog.setOnCreateListener(new CommonDialog.OnCreateListener() {
            /* class com.meishe.myvideo.util.Util.AnonymousClass3 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnCreateListener
            public void OnCreated() {
                commonDialog.setTitleTxt(str);
                commonDialog.setFirstTipsTxt(str2);
            }
        });
        commonDialog.setOnBtnClickListener(new CommonDialog.OnBtnClickListener() {
            /* class com.meishe.myvideo.util.Util.AnonymousClass4 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnRightBtnClicked(View view) {
                commonDialog.dismiss();
            }

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnLeftBtnClicked(View view) {
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    public static void showDialog(Context context, final String str, final String str2, final TipsButtonClickListener tipsButtonClickListener) {
        final CommonDialog commonDialog = new CommonDialog(context, 1);
        commonDialog.setOnCreateListener(new CommonDialog.OnCreateListener() {
            /* class com.meishe.myvideo.util.Util.AnonymousClass5 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnCreateListener
            public void OnCreated() {
                commonDialog.setTitleTxt(str);
                commonDialog.setFirstTipsTxt(str2);
            }
        });
        commonDialog.setOnBtnClickListener(new CommonDialog.OnBtnClickListener() {
            /* class com.meishe.myvideo.util.Util.AnonymousClass6 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnRightBtnClicked(View view) {
                commonDialog.dismiss();
                TipsButtonClickListener tipsButtonClickListener = tipsButtonClickListener;
                if (tipsButtonClickListener != null) {
                    tipsButtonClickListener.onTipsButtoClick(view);
                }
            }

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnLeftBtnClicked(View view) {
                commonDialog.dismiss();
            }
        });
        commonDialog.setCancelable(false);
        commonDialog.show();
    }

    public static class PointXComparator implements Comparator<PointF> {
        public int compare(PointF pointF, PointF pointF2) {
            return (int) (pointF.x - pointF2.x);
        }
    }

    public static List<String> getAllPermissionsList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("android.permission.CAMERA");
        arrayList.add("android.permission.RECORD_AUDIO");
        arrayList.add("android.permission.READ_EXTERNAL_STORAGE");
        arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        return arrayList;
    }

    public static NvsVideoResolution getVideoEditResolution(int i) {
        int compileVideoRes = ParameterSettingValues.instance().getCompileVideoRes();
        NvsVideoResolution nvsVideoResolution = new NvsVideoResolution();
        Point point = new Point();
        if (i == 1) {
            point.set((compileVideoRes * 16) / 9, compileVideoRes);
        } else if (i == 2) {
            point.set(compileVideoRes, compileVideoRes);
        } else if (i == 4) {
            point.set(compileVideoRes, (compileVideoRes * 16) / 9);
        } else if (i == 16) {
            point.set(compileVideoRes, (compileVideoRes * 4) / 3);
        } else if (i == 8) {
            point.set((compileVideoRes * 4) / 3, compileVideoRes);
        } else {
            point.set(LogType.UNEXP_ANR, 720);
        }
        nvsVideoResolution.imageWidth = point.x;
        nvsVideoResolution.imageHeight = point.y;
        Logger.d("getVideoEditResolution   ", nvsVideoResolution.imageWidth + "     " + nvsVideoResolution.imageHeight);
        return nvsVideoResolution;
    }

    public static NvsVideoResolution getVideoEditResolutionByClip(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int compileVideoRes = ParameterSettingValues.instance().getCompileVideoRes();
        NvsAVFileInfo aVFileInfo = NvsStreamingContext.getInstance().getAVFileInfo(str);
        NvsSize videoStreamDimension = aVFileInfo.getVideoStreamDimension(0);
        int videoStreamRotation = aVFileInfo.getVideoStreamRotation(0);
        int i = videoStreamDimension.width;
        int i2 = videoStreamDimension.height;
        if (videoStreamRotation == 1 || videoStreamRotation == 3) {
            i = videoStreamDimension.height;
            i2 = videoStreamDimension.width;
        }
        NvsVideoResolution nvsVideoResolution = new NvsVideoResolution();
        Point point = new Point();
        if (i > i2) {
            point.set(compileVideoRes, (int) (((float) i2) * ((((float) compileVideoRes) * 1.0f) / ((float) i))));
        } else {
            point.set((int) (((float) i) * ((((float) compileVideoRes) * 1.0f) / ((float) i2))), compileVideoRes);
        }
        nvsVideoResolution.imageWidth = alignedData(point.x, 4);
        nvsVideoResolution.imageHeight = alignedData(point.y, 2);
        Logger.d("getVideoEditResolution   ", nvsVideoResolution.imageWidth + "     " + nvsVideoResolution.imageHeight);
        return nvsVideoResolution;
    }

    private static int alignedData(int i, int i2) {
        return i - (i % i2);
    }

    public static boolean getBundleFilterInfo(Context context, ArrayList<NvAssetInfo> arrayList, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), "GBK"));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String[] split = readLine.split(",");
                    if (split.length >= 3) {
                        int i = 0;
                        while (true) {
                            if (i >= arrayList.size()) {
                                break;
                            }
                            NvAssetInfo nvAssetInfo = arrayList.get(i);
                            if (nvAssetInfo != null) {
                                if (nvAssetInfo.isReserved) {
                                    String str2 = nvAssetInfo.uuid;
                                    if (!TextUtils.isEmpty(str2)) {
                                        if (str2.equals(split[0])) {
                                            nvAssetInfo.mName = split[1];
                                            nvAssetInfo.aspectRatio = Integer.parseInt(split[2]);
                                            break;
                                        }
                                    }
                                }
                            }
                            i++;
                        }
                    }
                } else {
                    bufferedReader.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class PointYComparator implements Comparator<PointF> {
        public int compare(PointF pointF, PointF pointF2) {
            return (int) (pointF.y - pointF2.y);
        }
    }

    public static ArrayList<NvsTimelineCaption> getAllCaption(NvsTimeline nvsTimeline) {
        ArrayList<NvsTimelineCaption> arrayList = new ArrayList<>();
        NvsTimelineCaption firstCaption = nvsTimeline.getFirstCaption();
        while (firstCaption != null) {
            arrayList.add(firstCaption);
            firstCaption = nvsTimeline.getNextCaption(firstCaption);
        }
        return arrayList;
    }

    public static ArrayList<NvsTimelineAnimatedSticker> getAllSticker(NvsTimeline nvsTimeline) {
        ArrayList<NvsTimelineAnimatedSticker> arrayList = new ArrayList<>();
        NvsTimelineAnimatedSticker firstAnimatedSticker = nvsTimeline.getFirstAnimatedSticker();
        while (firstAnimatedSticker != null) {
            arrayList.add(firstAnimatedSticker);
            firstAnimatedSticker = nvsTimeline.getNextAnimatedSticker(firstAnimatedSticker);
        }
        return arrayList;
    }

    public static ArrayList<NvsTimelineCompoundCaption> getAllCompoundCaption(NvsTimeline nvsTimeline) {
        ArrayList<NvsTimelineCompoundCaption> arrayList = new ArrayList<>();
        NvsTimelineCompoundCaption firstCompoundCaption = nvsTimeline.getFirstCompoundCaption();
        while (firstCompoundCaption != null) {
            arrayList.add(firstCompoundCaption);
            firstCompoundCaption = nvsTimeline.getNextCaption(firstCompoundCaption);
        }
        return arrayList;
    }
}
