package com.meishe.myvideo.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.myvideo.application.MeiSheApplication;
import com.meishe.myvideo.bean.MediaData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MediaUtils {

    public interface LocalMediaCallback {
        void onLocalMediaCallback(List<MediaData> list);
    }

    public static Bitmap getVideoThumbnail(String str, int i, int i2, int i3) {
        Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(str, i3);
        return createVideoThumbnail != null ? ThumbnailUtils.extractThumbnail(createVideoThumbnail, i, i2, 2) : createVideoThumbnail;
    }

    public static Bitmap getVideoThumbnail(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime();
            try {
                mediaMetadataRetriever.release();
                return frameAtTime;
            } catch (RuntimeException e) {
                e.printStackTrace();
                return frameAtTime;
            }
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            mediaMetadataRetriever.release();
        } catch (RuntimeException e3) {
            e3.printStackTrace();
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException e4) {
                e4.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException e5) {
                e5.printStackTrace();
            }
            throw th;
        }
        return null;
    }

    public static void getAllPhotoInfo(final Activity activity, final LocalMediaCallback localMediaCallback) {
        ThreadPoolUtils.getExecutor().execute(new Runnable() {
            /* class com.meishe.myvideo.util.MediaUtils.AnonymousClass1 */

            public void run() {
                final ArrayList arrayList = new ArrayList();
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String[] strArr = {"_id", "_data", "date_added", "_data", "_display_name"};
                String[] strArr2 = {"image/jpeg", "image/png", "image/jpg"};
                Cursor query = activity.getContentResolver().query(uri, strArr, null, null, null);
                if (query != null) {
                    while (query.moveToNext()) {
                        int columnIndex = query.getColumnIndex("_data");
                        int columnIndex2 = query.getColumnIndex("date_added");
                        int i = query.getInt(query.getColumnIndexOrThrow("_id"));
                        String string = query.getString(columnIndex);
                        Long valueOf = Long.valueOf(query.getLong(columnIndex2) * 1000);
                        String string2 = query.getString(query.getColumnIndexOrThrow(strArr[1]));
                        String path = SdkVersionUtils.checkedAndroid_Q() ? SdkVersionUtils.getPath(i) : string2;
                        if (FileManagerUtils.fileIsExists(string2)) {
                            arrayList.add(new MediaData(i, 2, path, string, valueOf.longValue(), query.getString(query.getColumnIndexOrThrow(strArr[4])), false));
                        }
                    }
                    query.close();
                }
                activity.runOnUiThread(new Runnable() {
                    /* class com.meishe.myvideo.util.MediaUtils.AnonymousClass1.AnonymousClass1 */

                    public void run() {
                        MediaUtils.sortByTimeRepoList(arrayList);
                        localMediaCallback.onLocalMediaCallback(arrayList);
                    }
                });
            }
        });
    }

    public static void getAllVideoInfos(final Activity activity, final LocalMediaCallback localMediaCallback) {
        ThreadPoolUtils.getExecutor().execute(new Runnable() {
            /* class com.meishe.myvideo.util.MediaUtils.AnonymousClass2 */

            public void run() {
                final ArrayList arrayList = new ArrayList();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] strArr = {"_id", "_id", "_data", "duration", "_size", "date_added", "_display_name", "date_modified"};
                String[] strArr2 = {"video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv", "video/mkv", "video/mov", "video/mpg"};
                Cursor query = activity.getContentResolver().query(uri, strArr, null, null, "date_added DESC ");
                if (query != null) {
                    while (query.moveToNext()) {
                        int i = query.getInt(query.getColumnIndex("_id"));
                        String string = query.getString(query.getColumnIndexOrThrow(strArr[2]));
                        String path = SdkVersionUtils.checkedAndroid_Q() ? SdkVersionUtils.getPath(i) : string;
                        long j = query.getLong(query.getColumnIndex("duration"));
                        if (query.getLong(query.getColumnIndex("_size")) / 1024 < 0) {
                            Log.e("dml", "this video size < 0 " + path);
                            long length = new File(path).length() / 1024;
                        }
                        String string2 = query.getString(query.getColumnIndex("_display_name"));
                        Long valueOf = Long.valueOf(query.getLong(query.getColumnIndex("date_added")) * 1000);
                        if (FileManagerUtils.fileIsExists(string)) {
                            arrayList.add(new MediaData(i, 1, path, path, j, valueOf.longValue(), string2, false));
                        }
                    }
                    query.close();
                }
                activity.runOnUiThread(new Runnable() {
                    /* class com.meishe.myvideo.util.MediaUtils.AnonymousClass2.AnonymousClass1 */

                    public void run() {
                        localMediaCallback.onLocalMediaCallback(arrayList);
                    }
                });
            }
        });
    }

    public static void getAllPhotoAndVideo(final Activity activity, final LocalMediaCallback localMediaCallback) {
        getAllPhotoInfo(activity, new LocalMediaCallback() {
            /* class com.meishe.myvideo.util.MediaUtils.AnonymousClass3 */

            @Override // com.meishe.myvideo.util.MediaUtils.LocalMediaCallback
            public void onLocalMediaCallback(final List<MediaData> list) {
                MediaUtils.getAllVideoInfos(activity, new LocalMediaCallback() {
                    /* class com.meishe.myvideo.util.MediaUtils.AnonymousClass3.AnonymousClass1 */

                    @Override // com.meishe.myvideo.util.MediaUtils.LocalMediaCallback
                    public void onLocalMediaCallback(List<MediaData> list) {
                        ArrayList arrayList = new ArrayList();
                        ArrayList arrayList2 = new ArrayList();
                        List list2 = list;
                        if (list2 != null && list2.size() > 0) {
                            arrayList2.addAll(list);
                        }
                        ArrayList arrayList3 = new ArrayList();
                        if (list != null && list.size() > 0) {
                            arrayList3.addAll(list);
                        }
                        arrayList.addAll(arrayList2);
                        arrayList.addAll(arrayList3);
                        MediaUtils.sortByTimeRepoList(arrayList);
                        localMediaCallback.onLocalMediaCallback(arrayList);
                    }
                });
            }
        });
    }

    public static void getMediasByType(Activity activity, int i, LocalMediaCallback localMediaCallback) {
        if (i == 0) {
            getAllPhotoAndVideo(activity, localMediaCallback);
        } else if (i == 1) {
            getAllVideoInfos(activity, localMediaCallback);
        } else {
            getAllPhotoInfo(activity, localMediaCallback);
        }
    }

    public static void sortByTimeRepoList(List<MediaData> list) {
        Collections.sort(list, new Comparator<MediaData>() {
            /* class com.meishe.myvideo.util.MediaUtils.AnonymousClass4 */

            public int compare(MediaData mediaData, MediaData mediaData2) {
                return new Date(mediaData2.getData() * 1000).compareTo(new Date(mediaData.getData() * 1000));
            }
        });
    }

    private static void getUriColumns(Uri uri) {
        Cursor query = MeiSheApplication.getContext().getContentResolver().query(uri, null, null, null, null);
        query.moveToFirst();
        String[] columnNames = query.getColumnNames();
        for (String str : columnNames) {
            System.out.println(query.getColumnIndex(str) + " = " + str);
        }
        query.close();
    }

    public static ListOfAllMedia groupListByTime(List<MediaData> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (MediaData mediaData : list) {
            String format = new SimpleDateFormat("yyyy年MM月dd日").format(new Date(mediaData.getData()));
            List list2 = (List) linkedHashMap.get(format);
            if (list2 == null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(mediaData);
                linkedHashMap.put(format, arrayList);
            } else {
                list2.add(mediaData);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        Set<Map.Entry> entrySet = linkedHashMap.entrySet();
        for (Map.Entry entry : entrySet) {
            MediaData mediaData2 = new MediaData();
            mediaData2.setData(getIntTime((String) entry.getKey()));
            arrayList3.add(mediaData2);
            arrayList2.add(entry.getValue());
        }
        return new ListOfAllMedia(arrayList3, arrayList2);
    }

    private static long getIntTime(String str) {
        try {
            return new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA).parse(str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static class ListOfAllMedia {
        private List<List<MediaData>> listOfAll;
        private List<MediaData> listOfParent;

        public ListOfAllMedia(List<MediaData> list, List<List<MediaData>> list2) {
            this.listOfParent = list;
            this.listOfAll = list2;
        }

        public List<MediaData> getListOfParent() {
            List<MediaData> list = this.listOfParent;
            return list == null ? new ArrayList() : list;
        }

        public void setListOfParent(List<MediaData> list) {
            this.listOfParent = list;
        }

        public List<List<MediaData>> getListOfAll() {
            List<List<MediaData>> list = this.listOfAll;
            return list == null ? new ArrayList() : list;
        }

        public void setListOfAll(List<List<MediaData>> list) {
            this.listOfAll = list;
        }
    }

    public static Uri getPhotoUri(Cursor cursor) {
        return getMediaUri(cursor, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static Uri getVideoUri(Cursor cursor) {
        return getMediaUri(cursor, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

    public static Uri getMediaUri(Cursor cursor, Uri uri) {
        return Uri.withAppendedPath(uri, cursor.getString(cursor.getColumnIndex("_id")));
    }

    public static MeicamVideoClip mediaInfoToMeicamClip(MediaData mediaData) {
        int type = mediaData.getType();
        long duration = mediaData.getDuration() * 1000;
        String str = CommonData.CLIP_VIDEO;
        if (type == 1) {
            NvsAVFileInfo aVFileInfo = NvsStreamingContext.getInstance().getAVFileInfo(mediaData.getPath());
            if (aVFileInfo != null) {
                duration = aVFileInfo.getDuration();
            }
        } else {
            duration = CommonData.DEFAULT_LENGTH;
            str = CommonData.CLIP_IMAGE;
        }
        return new MeicamVideoClip(mediaData.getPath(), str, duration);
    }
}
