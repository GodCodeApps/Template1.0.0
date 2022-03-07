package com.meishe.myvideo.audio;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.meishe.myvideo.audio.function.AudioFileHelper;
import com.meishe.myvideo.audio.function.AudioFileListener;
import com.meishe.myvideo.audio.function.Recorder;
import com.meishe.myvideo.audio.function.RecorderCallback;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicBoolean;

public class AudioRecorder implements RecorderCallback, AudioFileListener {
    private final String TAG;
    private RecordConfig config;
    private volatile int count;
    private AtomicBoolean isStarted;
    private int lastRemaining;
    private AudioFileHelper mAudioFileHelper;
    private AudioStatusListener mAudioStatusListener;
    private Handler mHandler;
    private long mMaxRecordTime;
    private int mPerSecondPixel;
    private Recorder mRecorder;
    private long mVolumeInterval;
    private float maxWave;
    private float minWave;
    private short[] tempWaveData;

    public void destory() {
    }

    private AudioRecorder() {
        this.TAG = "AudioRecorder";
        this.mMaxRecordTime = 6000;
        this.mVolumeInterval = 200;
        this.isStarted = new AtomicBoolean(false);
        this.mPerSecondPixel = 100;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mRecorder = new Recorder(this.config, this);
    }

    public static AudioRecorder getInstance() {
        return RecorderHolder.instance;
    }

    public AudioRecorder setRecordConfig(RecordConfig recordConfig) {
        this.config = recordConfig;
        this.mAudioFileHelper = new AudioFileHelper(this);
        this.mAudioFileHelper.setRecorderConfig(this.config);
        this.mRecorder.setRecordConfig(recordConfig);
        return this;
    }

    public AudioRecorder setMaxRecordTime(long j) {
        this.mMaxRecordTime = j;
        return this;
    }

    public AudioRecorder setVolumeInterval(long j) {
        if (j < 100) {
            return this;
        }
        if (j % 20 != 0) {
            j = (j / 20) * 20;
        }
        this.mVolumeInterval = j;
        return this;
    }

    public AudioRecorder setRecordFilePath(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mAudioFileHelper.setSavePath(str);
        } else {
            this.mAudioFileHelper.setSavePath(null);
            this.mAudioFileHelper = null;
        }
        return this;
    }

    public AudioRecorder setRecordFileName(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mAudioFileHelper.setAudioName(str);
        }
        return this;
    }

    public AudioRecorder setPerSecondPixel(int i) {
        this.mPerSecondPixel = i;
        return this;
    }

    public AudioRecorder setAudioStatusListener(AudioStatusListener audioStatusListener) {
        this.mAudioStatusListener = audioStatusListener;
        return this;
    }

    public boolean start(Context context) {
        if (context == null) {
            Log.e("AudioRecorder", "context is null");
            return false;
        } else if (!isRecordAudioPermissionGranted(context) || !isWriteExternalStoragePermissionGranted(context)) {
            Log.e("AudioRecorder", "no permission");
            return false;
        } else if (!this.isStarted.compareAndSet(false, true)) {
            return false;
        } else {
            this.mRecorder.start();
            return true;
        }
    }

    private boolean isRecordAudioPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.RECORD_AUDIO") == 0;
    }

    private boolean isWriteExternalStoragePermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void stop() {
        if (this.isStarted.get()) {
            this.isStarted.set(false);
            this.mRecorder.immediateStop();
            return;
        }
        Recorder recorder = this.mRecorder;
        if (recorder != null) {
            recorder.immediateStop();
        }
    }

    @Override // com.meishe.myvideo.audio.function.RecorderCallback
    public boolean onRecorderStart() {
        AudioFileHelper audioFileHelper = this.mAudioFileHelper;
        if (audioFileHelper != null) {
            audioFileHelper.start();
        }
        this.count = 0;
        runOnUi(new Runnable() {
            /* class com.meishe.myvideo.audio.AudioRecorder.AnonymousClass1 */

            public void run() {
                if (AudioRecorder.this.mAudioStatusListener != null) {
                    AudioRecorder.this.mAudioStatusListener.onStartRecording(AudioRecorder.this.mAudioFileHelper == null ? "" : AudioRecorder.this.mAudioFileHelper.getAudioPath());
                }
            }
        });
        return true;
    }

    @Override // com.meishe.myvideo.audio.function.RecorderCallback
    public void onRecorded(final short[] sArr) {
        if (sArr.length <= 0) {
            Log.e("AudioRecorder", "wave.length is 0");
            return;
        }
        this.count++;
        byte[] Shorts2Bytes = Shorts2Bytes(sArr);
        AudioFileHelper audioFileHelper = this.mAudioFileHelper;
        if (audioFileHelper != null) {
            audioFileHelper.save(Shorts2Bytes, 0, Shorts2Bytes.length);
        }
        runOnUi(new Runnable() {
            /* class com.meishe.myvideo.audio.AudioRecorder.AnonymousClass2 */

            public void run() {
                if (AudioRecorder.this.mAudioStatusListener != null) {
                    AudioStatusListener audioStatusListener = AudioRecorder.this.mAudioStatusListener;
                    audioStatusListener.onRecordData(sArr, sArr.length);
                }
            }
        });
        calculateData(sArr);
    }

    private void calculateData(short[] sArr) {
        int length = (sArr.length * 50) / this.mPerSecondPixel;
        if (sArr.length < length) {
            short[] sArr2 = this.tempWaveData;
            if (sArr2 == null) {
                this.tempWaveData = new short[sArr.length];
                System.arraycopy(sArr, 0, this.tempWaveData, 0, sArr.length);
                return;
            } else if (sArr2.length < length) {
                short[] sArr3 = new short[(sArr2.length + sArr.length)];
                System.arraycopy(sArr2, 0, sArr3, 0, sArr2.length);
                System.arraycopy(sArr, 0, sArr3, sArr2.length, sArr.length);
                this.tempWaveData = sArr3;
                if (this.tempWaveData.length < length) {
                    return;
                }
            }
        } else {
            this.tempWaveData = sArr;
        }
        int length2 = this.tempWaveData.length;
        final float[] fArr = new float[(((this.lastRemaining + length2) / length) * 2)];
        long j = 0;
        int i = 0;
        int i2 = 0;
        while (i < length2) {
            short s = this.tempWaveData[i];
            float f = (float) s;
            this.maxWave = Math.max(f, this.maxWave);
            this.minWave = Math.min(f, this.minWave);
            i++;
            if ((this.lastRemaining + i) % length == 0) {
                fArr[i2] = this.maxWave / 32768.0f;
                int i3 = i2 + 1;
                fArr[i3] = this.minWave / 32768.0f;
                i2 = i3 + 1;
                this.maxWave = 0.0f;
                this.minWave = 0.0f;
            }
            j += (long) (s * s);
        }
        this.lastRemaining = (length2 + this.lastRemaining) % length;
        if (this.mAudioStatusListener != null && fArr.length > 0) {
            runOnUi(new Runnable() {
                /* class com.meishe.myvideo.audio.AudioRecorder.AnonymousClass3 */

                public void run() {
                    AudioRecorder.this.mAudioStatusListener.onWaveDate(fArr, AudioRecorder.this.count * 20, AudioRecorder.this.mAudioFileHelper == null ? "" : AudioRecorder.this.mAudioFileHelper.getAudioPath());
                }
            });
        }
        long j2 = (long) (this.count * 20);
        long j3 = this.mVolumeInterval;
        if (j2 >= j3 && j2 % j3 == 0) {
            double d = (double) j;
            double length3 = (double) this.tempWaveData.length;
            Double.isNaN(d);
            Double.isNaN(length3);
            onRecorderVolume((int) (Math.log10(d / length3) * 10.0d));
        }
        if (j2 >= this.mMaxRecordTime) {
            this.mRecorder.stop();
            this.isStarted.set(false);
        }
        this.tempWaveData = null;
    }

    private byte[] Shorts2Bytes(short[] sArr) {
        byte[] bArr = new byte[(sArr.length * 2)];
        for (int i = 0; i < sArr.length; i++) {
            byte[] bytes = getBytes(sArr[i]);
            for (int i2 = 0; i2 < 2; i2++) {
                bArr[(i * 2) + i2] = bytes[i2];
            }
        }
        return bArr;
    }

    private byte[] getBytes(short s) {
        byte[] bArr = new byte[2];
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            for (int length = bArr.length - 1; length >= 0; length--) {
                bArr[length] = (byte) (s & 255);
                s = (short) (s >> 8);
            }
        } else {
            for (int i = 0; i < bArr.length; i++) {
                bArr[i] = (byte) (s & 255);
                s = (short) (s >> 8);
            }
        }
        return bArr;
    }

    private void onRecorderVolume(final int i) {
        runOnUi(new Runnable() {
            /* class com.meishe.myvideo.audio.AudioRecorder.AnonymousClass4 */

            public void run() {
                if (AudioRecorder.this.mAudioStatusListener != null) {
                    AudioRecorder.this.mAudioStatusListener.onVoiceVolume(i);
                }
            }
        });
    }

    @Override // com.meishe.myvideo.audio.function.RecorderCallback
    public void onRecordedFail(final int i) {
        AudioFileHelper audioFileHelper = this.mAudioFileHelper;
        runOnUi(new Runnable() {
            /* class com.meishe.myvideo.audio.AudioRecorder.AnonymousClass5 */

            public void run() {
                String str = i != 0 ? i != 1 ? i != 3 ? "未知错误" : "当前应用没有录音权限或者录音功能被占用" : "Recorder.read() 过程中发生错误" : "启动或录音时抛出异常Exception";
                if (AudioRecorder.this.mAudioStatusListener != null) {
                    AudioRecorder.this.mAudioStatusListener.onRecordError(i, str);
                }
            }
        });
    }

    @Override // com.meishe.myvideo.audio.function.RecorderCallback
    public void onRecorderStop() {
        AudioFileHelper audioFileHelper = this.mAudioFileHelper;
        if (audioFileHelper != null) {
            audioFileHelper.finish((long) (this.count * 20));
        }
        this.lastRemaining = 0;
        this.count = 0;
        runOnUi(new Runnable() {
            /* class com.meishe.myvideo.audio.AudioRecorder.AnonymousClass6 */

            public void run() {
                if (AudioRecorder.this.mAudioStatusListener != null) {
                    AudioRecorder.this.mAudioStatusListener.onStopRecording();
                }
            }
        });
    }

    private int calculateVolume(short[] sArr) {
        long j = 0;
        for (int i = 0; i < sArr.length; i++) {
            j += (long) (sArr[i] * sArr[i]);
        }
        double d = (double) j;
        double length = (double) sArr.length;
        Double.isNaN(d);
        Double.isNaN(length);
        return (int) (Math.log10(d / length) * 10.0d);
    }

    private void runOnUi(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    @Override // com.meishe.myvideo.audio.function.AudioFileListener
    public void onFailure(final String str) {
        runOnUi(new Runnable() {
            /* class com.meishe.myvideo.audio.AudioRecorder.AnonymousClass7 */

            public void run() {
                if (AudioRecorder.this.mAudioStatusListener != null) {
                    AudioRecorder.this.mAudioStatusListener.onFileSaveFailed(str);
                }
            }
        });
    }

    @Override // com.meishe.myvideo.audio.function.AudioFileListener
    public void onSuccess(final String str, final long j) {
        runOnUi(new Runnable() {
            /* class com.meishe.myvideo.audio.AudioRecorder.AnonymousClass8 */

            public void run() {
                if (AudioRecorder.this.mAudioStatusListener != null) {
                    AudioRecorder.this.mAudioStatusListener.onFileSaveSuccess(str, j);
                }
            }
        });
    }

    public static class RecordConfig {
        public static final int SAMPLE_RATE_11K_HZ = 11025;
        public static final int SAMPLE_RATE_16K_HZ = 16000;
        public static final int SAMPLE_RATE_22K_HZ = 22050;
        public static final int SAMPLE_RATE_44K_HZ = 44100;
        public static final int SAMPLE_RATE_8K_HZ = 8000;
        private int audioFormat = 2;
        private int audioSource = 1;
        private int channelConfig = 16;
        private int sampleRate = SAMPLE_RATE_16K_HZ;

        public RecordConfig(int i, int i2, int i3, int i4) {
            this.audioSource = i;
            this.sampleRate = i2;
            this.channelConfig = i3;
            this.audioFormat = i4;
        }

        public RecordConfig() {
        }

        public int getAudioSource() {
            return this.audioSource;
        }

        public RecordConfig setAudioSource(int i) {
            this.audioSource = i;
            return this;
        }

        public int getSampleRate() {
            return this.sampleRate;
        }

        public RecordConfig setSampleRate(int i) {
            this.sampleRate = i;
            return this;
        }

        public int getChannelConfig() {
            return this.channelConfig;
        }

        public RecordConfig setChannelConfig(int i) {
            this.channelConfig = i;
            return this;
        }

        public int getAudioFormat() {
            return this.audioFormat;
        }

        public RecordConfig setAudioFormat(int i) {
            this.audioFormat = i;
            return this;
        }
    }

    /* access modifiers changed from: private */
    public static class RecorderHolder {
        private static final AudioRecorder instance = new AudioRecorder();

        private RecorderHolder() {
        }
    }
}
