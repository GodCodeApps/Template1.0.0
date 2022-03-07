package com.meishe.myvideo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meishe.common.utils.Logger;
import com.meishe.draft.DraftManager;
import com.meishe.draft.util.DraftFileUtil;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.edit.manager.OperateManager;
import com.meishe.myvideo.util.engine.EditorEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler mInstance;
    private String TAG = getClass().getSimpleName();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> paramsMap = new HashMap();

    private CrashHandler() {
    }

    public static synchronized CrashHandler getInstance() {
        CrashHandler crashHandler;
        synchronized (CrashHandler.class) {
            if (mInstance == null) {
                mInstance = new CrashHandler();
            }
            crashHandler = mInstance;
        }
        return crashHandler;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable th) {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
        if (handleException(thread, th) || (uncaughtExceptionHandler = this.mDefaultHandler) == null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                String str = this.TAG;
                Log.e(str, "uncaughtException: " + e.getMessage());
            }
            Process.killProcess(Process.myPid());
            return;
        }
        uncaughtExceptionHandler.uncaughtException(thread, th);
    }

    private boolean handleException(Thread thread, Throwable th) {
        if (th == null) {
            return false;
        }
        saveDraft();
        this.paramsMap.put("thread name", thread.getName());
        collectDeviceInfo(this.mContext);
        saveCrashInfo2File(th);
        new Thread() {
            /* class com.meishe.myvideo.util.CrashHandler.AnonymousClass1 */

            public void run() {
                Looper.prepare();
                Toast.makeText(CrashHandler.this.mContext, "程序发生异常！", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        return true;
    }

    private void saveDraft() {
        try {
            EditorEngine instance = EditorEngine.getInstance();
            NvsTimeline currentTimeline = instance.getCurrentTimeline();
            Bitmap grabImageFromTimeline = NvsStreamingContext.getInstance().grabImageFromTimeline(currentTimeline, 0, new NvsRational(1, 1));
            String draftDir = TimelineData.getInstance().getDraftDir();
            long currentTimeMillis = System.currentTimeMillis();
            if (TextUtils.isEmpty(draftDir)) {
                draftDir = DraftFileUtil.getDirPath(this.mContext, currentTimeMillis);
            }
            String str = draftDir + File.separator + "cover" + currentTimeMillis + ".png";
            ImageUtils.saveBitmap(grabImageFromTimeline, str, true);
            DraftManager.getInstance().setCoverPath(str);
            DraftManager.getInstance().setTimeLineDuring(currentTimeline.getDuration());
            if (instance.getEditType() != 1) {
                DraftManager.getInstance().saveDraftData(draftDir, currentTimeMillis);
            } else if (OperateManager.getInstance().haveOperate()) {
                DraftManager.getInstance().editDraft(draftDir, currentTimeMillis);
            } else {
                DraftManager.getInstance().editDraftNoModifiedTime(draftDir);
            }
        } catch (Exception e) {
            Logger.e("saveDraft error: " + e.getMessage());
        }
    }

    public void collectDeviceInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                this.paramsMap.put("versionName", packageInfo.versionName == null ? "null" : packageInfo.versionName);
                this.paramsMap.put("versionCode", packageInfo.versionCode + "");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(this.TAG, "collectDeviceInfo: an error occured when collect package info", e);
        }
        Field[] declaredFields = Build.class.getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                field.setAccessible(true);
                this.paramsMap.put(field.getName(), field.get(null).toString());
            } catch (Exception e2) {
                Logger.e(this.TAG, "collectDeviceInfo: an error occured when collect crash info", e2);
            }
        }
    }

    private String saveCrashInfo2File(Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : this.paramsMap.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        stringBuffer.append(stringWriter.toString());
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String str = "crash-" + this.format.format(new Date()) + "-" + currentTimeMillis + ".log";
            if (Environment.getExternalStorageState().equals("mounted")) {
                String str2 = PathUtils.getLogDir() + File.separator;
                Log.d(this.TAG, "saveCrashInfo2File: " + str2);
                File file = new File(str2);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(str2 + str);
                fileOutputStream.write(stringBuffer.toString().getBytes());
                fileOutputStream.close();
            }
            Log.e(this.TAG, stringBuffer.toString());
            return str;
        } catch (Exception e) {
            Logger.e(this.TAG, "saveCrashInfo2File: an error occured while writing file...", e);
            return null;
        }
    }
}
