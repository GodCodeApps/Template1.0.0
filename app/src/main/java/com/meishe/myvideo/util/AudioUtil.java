package com.meishe.myvideo.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.meishe.myvideo.bean.MusicInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AudioUtil {
    private static final int AUDIO_DURATION = 500;
    private static final String DURATION = "duration";
    private static final String ORDER_BY = "_id DESC";
    private static final String[] PROJECTION = {"_id", "_data", "mime_type", "width", "height", "duration", "_size", "title", "artist"};
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String TAG = "AudioUtil";
    private AppCompatActivity mContext;
    private long videoMaxS = 0;
    private long videoMinS = 0;

    public interface LocalMediaLoadListener {
        void loadComplete(List<MusicInfo> list);
    }

    public AudioUtil(AppCompatActivity appCompatActivity) {
        this.mContext = appCompatActivity;
    }

    public void getMedias(final int i, final LocalMediaLoadListener localMediaLoadListener) {
        this.mContext.getSupportLoaderManager().initLoader(i, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            /* class com.meishe.myvideo.util.AudioUtil.AnonymousClass1 */

            @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
            public void onLoaderReset(Loader<Cursor> loader) {
            }

            @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                if (i != 1) {
                    return null;
                }
                AudioUtil audioUtil = AudioUtil.this;
                return new CursorLoader(AudioUtil.this.mContext, AudioUtil.QUERY_URI, AudioUtil.PROJECTION, audioUtil.getSelectionArgsForSingleMediaCondition(audioUtil.getDurationCondition(0, 500)), new String[]{String.valueOf(2)}, AudioUtil.ORDER_BY);
            }

            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                ArrayList arrayList = new ArrayList();
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    do {
                        String string = cursor.getString(cursor.getColumnIndexOrThrow(AudioUtil.PROJECTION[1]));
                        if (AudioUtil.this.isSupport(string).booleanValue()) {
                            cursor.getString(cursor.getColumnIndexOrThrow(AudioUtil.PROJECTION[2]));
                            int i = cursor.getInt(cursor.getColumnIndexOrThrow(AudioUtil.PROJECTION[5]));
                            Log.e("===>", "sd: mp3 duration: " + i);
                            cursor.getInt(cursor.getColumnIndexOrThrow(AudioUtil.PROJECTION[6]));
                            String string2 = cursor.getString(cursor.getColumnIndexOrThrow(AudioUtil.PROJECTION[7]));
                            String string3 = cursor.getString(cursor.getColumnIndexOrThrow(AudioUtil.PROJECTION[8]));
                            try {
                                mediaMetadataRetriever.setDataSource(string);
                                string3 = mediaMetadataRetriever.extractMetadata(2);
                                if (string3 == null || string3.isEmpty()) {
                                    string3 = "null";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MusicInfo musicInfo = new MusicInfo();
                            musicInfo.setFilePath(string);
                            musicInfo.setExoplayerPath(string);
                            musicInfo.setDuration((long) (i * 1000));
                            musicInfo.setTitle(string2);
                            musicInfo.setArtist(string3);
                            musicInfo.setMimeType(i);
                            musicInfo.setTrimIn(0);
                            musicInfo.setTrimOut(musicInfo.getDuration());
                            arrayList.add(musicInfo);
                        }
                    } while (cursor.moveToNext());
                    localMediaLoadListener.loadComplete(arrayList);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String getDurationCondition(long j, long j2) {
        long j3 = this.videoMaxS;
        if (j3 == 0) {
            j3 = Long.MAX_VALUE;
        }
        if (j != 0) {
            j3 = Math.min(j3, j);
        }
        Locale locale = Locale.CHINA;
        Object[] objArr = new Object[3];
        objArr[0] = Long.valueOf(Math.max(j2, this.videoMinS));
        objArr[1] = Math.max(j2, this.videoMinS) == 0 ? "" : "=";
        objArr[2] = Long.valueOf(j3);
        return String.format(locale, "%d <%s duration and duration <= %d", objArr);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String getSelectionArgsForSingleMediaCondition(String str) {
        return "media_type=? AND _size>0 AND " + str;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Boolean isSupport(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if (str.toLowerCase().endsWith(".mp3") || str.toLowerCase().endsWith(".m4a") || str.toLowerCase().endsWith(".aac") || str.toLowerCase().endsWith(".flac")) {
            return true;
        }
        return false;
    }

    public List<MusicInfo> listMusicFilesFromAssets(Context context) {
        IOException e;
        ArrayList arrayList = new ArrayList();
        try {
            String[] list = context.getAssets().list("music");
            if (list != null) {
                for (String str : list) {
                    if (isSupport(str).booleanValue()) {
                        MusicInfo musicInfo = new MusicInfo();
                        musicInfo.setIsAsset(true);
                        musicInfo.setFilePath("assets:/music/" + str);
                        musicInfo.setExoplayerPath("asset:/music/" + str);
                        musicInfo.setLrcPath("assets:/music/" + str.substring(0, str.lastIndexOf(".")) + ".lrc");
                        StringBuilder sb = new StringBuilder();
                        sb.append("music/");
                        sb.append(str);
                        musicInfo.setAssetPath(sb.toString());
                        String substring = str.substring(0, str.lastIndexOf("."));
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        Log.d(TAG, "str: " + musicInfo.getFilePath());
                        try {
                            AssetFileDescriptor openFd = context.getAssets().openFd(musicInfo.getAssetPath());
                            mediaMetadataRetriever.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                            mediaMetadataRetriever.extractMetadata(7);
                            String extractMetadata = mediaMetadataRetriever.extractMetadata(2);
                            if (extractMetadata == null || extractMetadata.isEmpty()) {
                                extractMetadata = "null";
                            }
                            String extractMetadata2 = mediaMetadataRetriever.extractMetadata(9);
                            Log.e("===>", "assets: mp3 duration: " + extractMetadata2);
                            musicInfo.setTitle(substring);
                            musicInfo.setArtist(extractMetadata);
                            musicInfo.setDuration((long) (Integer.valueOf(extractMetadata2).intValue() * 1000));
                            musicInfo.setTrimOut(musicInfo.getDuration());
                            musicInfo.setTrimIn(0);
                            arrayList.add(musicInfo);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e4) {
            e = e4;
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }
}
