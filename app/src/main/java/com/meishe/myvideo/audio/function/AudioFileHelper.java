package com.meishe.myvideo.audio.function;

import android.text.TextUtils;
import android.util.Log;
import com.meishe.myvideo.audio.AudioRecorder;
import com.meishe.myvideo.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AudioFileHelper {
    public static final String TAG = "AudioFileHelper";
    private String mAudioName;
    private AudioRecorder.RecordConfig mConfig;
    private AudioFileListener mListener;
    private RandomAccessFile mRandomAccessFile;
    private String mSavePath;
    private File mTargetFile;

    public AudioFileHelper(AudioFileListener audioFileListener) {
        this.mListener = audioFileListener;
    }

    public void setSavePath(String str) {
        this.mSavePath = str;
        FileUtil.isFolderExists(str);
    }

    public void setAudioName(String str) {
        this.mAudioName = str;
    }

    public String getAudioPath() {
        if (TextUtils.isEmpty(this.mAudioName)) {
            return null;
        }
        return this.mSavePath + File.separator + this.mAudioName;
    }

    public void setRecorderConfig(AudioRecorder.RecordConfig recordConfig) {
        this.mConfig = recordConfig;
    }

    public void start() {
        try {
            open(this.mSavePath + File.separator + this.mAudioName);
        } catch (IOException e) {
            e.printStackTrace();
            onSaveFileFailure(e.toString());
        }
    }

    public void save(byte[] bArr, int i, int i2) {
        RandomAccessFile randomAccessFile = this.mRandomAccessFile;
        if (randomAccessFile != null) {
            try {
                write(randomAccessFile, bArr, i, i2);
            } catch (IOException e) {
                e.printStackTrace();
                onSaveFileFailure(e.toString());
            }
        }
    }

    public void finish(long j) {
        try {
            close(j);
        } catch (IOException e) {
            e.printStackTrace();
            onSaveFileFailure(e.toString());
        }
    }

    private void open(String str) throws IOException {
        if (TextUtils.isEmpty(str)) {
            Log.d(TAG, "Path not set , data will not save");
        } else if (this.mConfig == null) {
            Log.d(TAG, "RecordConfig not set , data will not save");
        } else {
            this.mTargetFile = new File(str);
            if (this.mTargetFile.exists()) {
                this.mTargetFile.delete();
            } else {
                File parentFile = this.mTargetFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
            }
            short s = 2;
            short s2 = this.mConfig.getAudioFormat() == 2 ? (short) 16 : 8;
            if (this.mConfig.getChannelConfig() == 16) {
                s = 1;
            }
            int sampleRate = this.mConfig.getSampleRate();
            this.mRandomAccessFile = new RandomAccessFile(this.mTargetFile, "rw");
            this.mRandomAccessFile.setLength(0);
            this.mRandomAccessFile.writeBytes("RIFF");
            this.mRandomAccessFile.writeInt(0);
            this.mRandomAccessFile.writeBytes("WAVE");
            this.mRandomAccessFile.writeBytes("fmt ");
            this.mRandomAccessFile.writeInt(Integer.reverseBytes(16));
            this.mRandomAccessFile.writeShort(Short.reverseBytes(1));
            this.mRandomAccessFile.writeShort(Short.reverseBytes(s));
            this.mRandomAccessFile.writeInt(Integer.reverseBytes(sampleRate));
            this.mRandomAccessFile.writeInt(Integer.reverseBytes(((sampleRate * s2) * s) / 8));
            this.mRandomAccessFile.writeShort(Short.reverseBytes((short) ((s * s2) / 8)));
            this.mRandomAccessFile.writeShort(Short.reverseBytes(s2));
            this.mRandomAccessFile.writeBytes("data");
            this.mRandomAccessFile.writeInt(0);
        }
    }

    private void write(RandomAccessFile randomAccessFile, byte[] bArr, int i, int i2) throws IOException {
        randomAccessFile.write(bArr, i, i2);
    }

    private void close(long j) throws IOException {
        RandomAccessFile randomAccessFile = null;
        try {
            if (this.mRandomAccessFile == null) {
                onSaveFileFailure("File save error exception occurs");
                if (randomAccessFile == null) {
                    return;
                }
                return;
            }
            this.mRandomAccessFile.seek(4);
            this.mRandomAccessFile.writeInt(Integer.reverseBytes((int) (this.mRandomAccessFile.length() - 8)));
            this.mRandomAccessFile.seek(40);
            this.mRandomAccessFile.writeInt(Integer.reverseBytes((int) (this.mRandomAccessFile.length() - 44)));
            if (this.mListener != null) {
                AudioFileListener audioFileListener = this.mListener;
                audioFileListener.onSuccess(this.mSavePath + this.mAudioName, j);
            }
            RandomAccessFile randomAccessFile2 = this.mRandomAccessFile;
            if (randomAccessFile2 != null) {
                randomAccessFile2.close();
                this.mRandomAccessFile = null;
            }
        } finally {
            randomAccessFile = this.mRandomAccessFile;
            if (randomAccessFile != null) {
                randomAccessFile.close();
                this.mRandomAccessFile = null;
            }
        }
    }

    public void cancel() {
        File file;
        if (this.mRandomAccessFile != null && (file = this.mTargetFile) != null) {
            if (file.exists()) {
                this.mTargetFile.delete();
            }
            this.mRandomAccessFile = null;
            this.mTargetFile = null;
        }
    }

    private void onSaveFileFailure(String str) {
        if (!TextUtils.isEmpty(this.mAudioName)) {
            File file = new File(this.mSavePath + this.mAudioName);
            if (file.exists()) {
                file.delete();
            }
        }
        AudioFileListener audioFileListener = this.mListener;
        if (audioFileListener != null) {
            audioFileListener.onFailure(str);
        }
    }
}
