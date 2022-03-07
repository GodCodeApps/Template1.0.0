package com.meishe.myvideo.manager;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.audio.AudioRecorder;
import java.io.File;
import java.io.IOException;

public class RecordManager {
    private static final int AUDIOENCODING = 2;
    public static final int AUDIO_SOURCE = 1;
    private static final int CHANNELCONGIFIGURATION = 16;
    private static final int FREQUENCY = 16000;
    public static int audioFormat = 2;
    public static int audioSource = 1;
    public static int bufferSizeInBytes = 0;
    public static int channelConfig = 12;
    private static final RecordManager ourInstance = new RecordManager();
    public static int sampleRateInHz = AudioRecorder.RecordConfig.SAMPLE_RATE_44K_HZ;
    private File file;
    private String mDirName;
    private MediaRecorder mRecorder;
    OnRecordListener onRecordStartListener;

    public interface OnRecordListener {
        void onRecordEnd();

        void onRecordStart(Long l, String str);
    }

    public static RecordManager getInstance() {
        return ourInstance;
    }

    private RecordManager() {
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordStartListener = onRecordListener;
    }

    public void release() {
        try {
            if (this.mRecorder != null) {
                this.mRecorder.stop();
                if (this.onRecordStartListener != null) {
                    this.onRecordStartListener.onRecordEnd();
                }
                this.mRecorder.release();
                this.mRecorder = null;
            } else if (this.onRecordStartListener != null) {
                this.onRecordStartListener.onRecordEnd();
            }
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
        Logger.d("录音权限", "录音权限" + audioRecord.getRecordingState());
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
            this.onRecordStartListener.onRecordStart(Long.valueOf(currentTimeMillis), this.mDirName);
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
