package com.meishe.myvideo.audio.function;

import android.media.AudioRecord;
import android.util.Log;
import com.meishe.myvideo.audio.AudioRecorder;

public class Recorder {
    public static final int RECORDER_EXCEPTION_OCCUR = 0;
    public static final int RECORDER_PERMISSION_ERROR = 3;
    public static final int RECORDER_READ_ERROR = 1;
    private static final String TAG = "Recorder";
    public static final int TIMER_INTERVAL = 20;
    private Runnable RecordRun = new Runnable() {
        /* class com.meishe.myvideo.audio.function.Recorder.AnonymousClass1 */

        public void run() {
            int i;
            if (Recorder.this.mCallback != null) {
                Recorder.this.mCallback.onRecorderStart();
            }
            if (Recorder.this.mAudioRecorder != null && Recorder.this.mAudioRecorder.getState() == 1) {
                try {
                    Recorder.this.mAudioRecorder.stop();
                    Recorder.this.mAudioRecorder.startRecording();
                } catch (Exception e) {
                    e.printStackTrace();
                    Recorder.this.recordFailed(0);
                    Recorder.this.mAudioRecorder = null;
                }
            }
            if (Recorder.this.mAudioRecorder != null && Recorder.this.mAudioRecorder.getState() == 1 && Recorder.this.mAudioRecorder.getRecordingState() == 1) {
                Recorder.this.recordFailed(3);
                Recorder.this.mAudioRecorder = null;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= 2) {
                    break;
                } else if (Recorder.this.mAudioRecorder == null) {
                    Recorder.this.isRecord = false;
                    break;
                } else {
                    Recorder.this.mAudioRecorder.read(Recorder.this.wave, 0, Recorder.this.wave.length);
                    i2++;
                }
            }
            while (Recorder.this.isRecord) {
                try {
                    i = Recorder.this.mAudioRecorder.read(Recorder.this.wave, 0, Recorder.this.wave.length);
                } catch (Exception unused) {
                    Recorder.this.isRecord = false;
                    Recorder.this.recordFailed(0);
                    i = 0;
                }
                if (i == Recorder.this.wave.length) {
                    Recorder.this.mCallback.onRecorded(Recorder.this.wave);
                } else {
                    Recorder.this.recordFailed(1);
                    Recorder.this.isRecord = false;
                }
            }
            Recorder.this.releaseRecord();
            Recorder.this.doRecordStop();
        }
    };
    private int bufferSize;
    private boolean isRecord = false;
    private AudioRecord mAudioRecorder = null;
    private RecorderCallback mCallback;
    private AudioRecorder.RecordConfig mRecordConfig;
    private Thread mThread = null;
    private short[] wave;

    public Recorder(AudioRecorder.RecordConfig recordConfig, RecorderCallback recorderCallback) {
        this.mCallback = recorderCallback;
        this.mRecordConfig = recordConfig;
    }

    public void setRecordConfig(AudioRecorder.RecordConfig recordConfig) {
        this.mRecordConfig = recordConfig;
    }

    public boolean start() {
        this.isRecord = true;
        synchronized (this) {
            if (initializeRecord()) {
                this.mThread = new Thread(this.RecordRun);
                this.mThread.start();
                return true;
            }
            this.isRecord = false;
            return false;
        }
    }

    public void stop() {
        synchronized (this) {
            this.mThread = null;
            this.isRecord = false;
        }
    }

    public void immediateStop() {
        this.isRecord = false;
        Thread thread = this.mThread;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.mThread = null;
    }

    public boolean isStarted() {
        return this.isRecord;
    }

    private boolean initializeRecord() {
        synchronized (this) {
            try {
                if (this.mCallback == null) {
                    Log.e(TAG, "Error VoiceRecorderCallback is  null");
                    return false;
                } else if (this.mRecordConfig == null) {
                    Log.e(TAG, "Error recordConfig is null");
                    return false;
                } else {
                    int i = this.mRecordConfig.getAudioFormat() == 2 ? 16 : 8;
                    int channelConfig = this.mRecordConfig.getChannelConfig();
                    int i2 = channelConfig == 16 ? 1 : 2;
                    int audioSource = this.mRecordConfig.getAudioSource();
                    int sampleRate = this.mRecordConfig.getSampleRate();
                    int audioFormat = this.mRecordConfig.getAudioFormat();
                    int i3 = (sampleRate * 20) / 1000;
                    this.bufferSize = (((i3 * 2) * i) * i2) / 8;
                    this.wave = new short[((((i3 * i) / 8) * i2) / 2)];
                    int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
                    if (this.bufferSize < minBufferSize) {
                        this.bufferSize = minBufferSize;
                    }
                    if (this.mAudioRecorder != null) {
                        releaseRecord();
                    }
                    this.mAudioRecorder = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, this.bufferSize);
                    if (this.mAudioRecorder.getState() == 1) {
                        return true;
                    }
                    this.mAudioRecorder = null;
                    recordFailed(3);
                    throw new Exception("AudioRecord initialization failed");
                }
            } catch (Throwable th) {
                Log.e(TAG, "Exception=" + th);
                return false;
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void releaseRecord() {
        synchronized (this) {
            if (this.mAudioRecorder != null) {
                try {
                    this.mAudioRecorder.stop();
                    this.mAudioRecorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "mAudioRecorder release error!");
                }
                this.mAudioRecorder = null;
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void doRecordStop() {
        RecorderCallback recorderCallback = this.mCallback;
        if (recorderCallback != null) {
            recorderCallback.onRecorderStop();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void recordFailed(int i) {
        RecorderCallback recorderCallback = this.mCallback;
        if (recorderCallback != null) {
            recorderCallback.onRecordedFail(i);
        }
    }
}
