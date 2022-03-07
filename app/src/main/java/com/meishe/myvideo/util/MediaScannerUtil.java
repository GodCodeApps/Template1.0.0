package com.meishe.myvideo.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.meishe.myvideo.application.MeiSheApplication;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MediaScannerUtil {
    private static final String TAG = "MediaScannerUtil";
    private static List<MediaScannerCallBack> callBackList = new ArrayList();
    private static final MediaScannerClient client = new MediaScannerClient();
    private static MediaScannerConnection mediaScanConn = new MediaScannerConnection(MeiSheApplication.getContext().getApplicationContext(), client);
    private static final Queue<Entity> sPendingScanList = new ConcurrentLinkedQueue();

    public static abstract class MediaScannerCallBack {
        public abstract void onScanCompleted();
    }

    private static class MediaScannerClient implements MediaScannerConnection.MediaScannerConnectionClient {
        private MediaScannerClient() {
        }

        public void onMediaScannerConnected() {
            MediaScannerUtil.scanOrDisconnect();
        }

        public void onScanCompleted(String str, Uri uri) {
            MediaScannerUtil.scanOrDisconnect();
        }
    }

    /* access modifiers changed from: private */
    public static void scanOrDisconnect() {
        Entity poll = sPendingScanList.poll();
        if (poll != null) {
            Log.e(TAG, String.format("scanFile, path =-> %s", poll.path));
            mediaScanConn.scanFile(poll.path, poll.type);
            return;
        }
        mediaScanConn.disconnect();
        Log.e(TAG, String.format("onScanCompleted and disconnect", new Object[0]));
    }

    public static void scanFile(String str, String str2) {
        if (str != null && !str.isEmpty()) {
            scan(new Entity(str, str2));
        }
    }

    public static void scan(Entity entity) {
        sPendingScanList.add(entity);
        mediaScanConn.connect();
    }

    public static void scan(String str) {
        if (!TextUtils.isEmpty(str)) {
            scan(new File(str));
        }
    }

    public static void scan(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (File file2 : listFiles) {
                        scan(file2);
                    }
                    return;
                }
                return;
            }
            scan(new Entity(file.getAbsolutePath()));
        }
    }

    public static void unScanFile() {
        mediaScanConn.disconnect();
    }

    public static void addCallBack(MediaScannerCallBack mediaScannerCallBack) {
        if (mediaScannerCallBack != null) {
            callBackList.add(mediaScannerCallBack);
        }
    }

    public static void removeCallBack(MediaScannerCallBack mediaScannerCallBack) {
        if (mediaScannerCallBack != null) {
            callBackList.remove(mediaScannerCallBack);
        }
    }

    public static void scanFileAsync(Context context, String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        context.sendBroadcast(intent);
    }

    public static class Entity {
        String path;
        String type;

        public Entity(String str, String str2) {
            this.path = str;
            this.type = str2;
        }

        public Entity(String str) {
            this.path = str;
        }
    }
}
