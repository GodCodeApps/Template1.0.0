package com.meishe.myvideo.edit.record;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import java.io.File;
import java.io.IOException;

public class RecordManager {
    public static int audioFormat = 2;
    public static int audioSource = 1;
    public static int bufferSizeInBytes = 0;
    public static int channelConfig = 12;
    private static RecordManager mInstance = null;
    public static int sampleRateInHz = 44100;
    File file;
    String mDirName;
    private MediaRecorder mRecorder;
    OnRecordStartListener onRecordStart;

    public interface OnRecordStartListener {
        void onRecordEnd();

        void onRecordStart(Long l, String str);
    }

    public void setOnRecordStart(OnRecordStartListener onRecordStartListener) {
        this.onRecordStart = onRecordStartListener;
    }

    public static RecordManager getInstance() {
        if (mInstance == null) {
            synchronized (RecordManager.class) {
                if (mInstance == null) {
                    mInstance = new RecordManager();
                }
            }
        }
        return mInstance;
    }

    public void release() {
        try {
            if (this.mRecorder != null) {
                this.mRecorder.stop();
                this.onRecordStart.onRecordEnd();
                this.mRecorder.release();
                this.mRecorder = null;
                return;
            }
            this.onRecordStart.onRecordEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isHasPermission() {
        bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
        try {
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        Log.i("录音权限", "录音权限" + audioRecord.getRecordingState());
        if (audioRecord.getRecordingState() != 3) {
            return false;
        }
        audioRecord.stop();
        audioRecord.release();
        return true;
    }

    public void prepareAudio(String str) {
        try {
            File file2 = new File(str);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            long currentTimeMillis = System.currentTimeMillis();
            this.file = new File(file2, currentTimeMillis + ".mp3");
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
            this.mDirName = this.file.getAbsolutePath();
            this.mRecorder = new MediaRecorder();
            this.mRecorder.setOutputFile(this.file.getAbsolutePath());
            this.mRecorder.setAudioSource(1);
            this.mRecorder.setOutputFormat(2);
            this.mRecorder.setAudioEncoder(3);
            this.mRecorder.prepare();
            this.mRecorder.start();
            this.onRecordStart.onRecordStart(Long.valueOf(currentTimeMillis), this.mDirName);
        } catch (IllegalStateException e) {
            MediaRecorder mediaRecorder = this.mRecorder;
            if (mediaRecorder != null) {
                mediaRecorder.release();
                this.mRecorder = null;
            }
            if (this.file.exists()) {
                this.file.delete();
            }
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
