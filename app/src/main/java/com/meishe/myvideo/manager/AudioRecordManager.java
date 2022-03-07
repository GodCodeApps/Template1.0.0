package com.meishe.myvideo.manager;

import android.content.Context;
import com.meishe.myvideo.audio.AudioRecorder;
import com.meishe.myvideo.audio.AudioStatusListener;
import com.meishe.myvideo.util.PathUtils;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;

public class AudioRecordManager {
    private static final AudioRecordManager ourInstance = new AudioRecordManager();
    private AudioRecorder mAudioRecorder;
    private AudioStatusListener mAudioStatusListener = new AudioStatusListener() {
        /* class com.meishe.myvideo.manager.AudioRecordManager.AnonymousClass1 */

        @Override // com.meishe.myvideo.audio.AudioStatusListener
        public void onFileSaveSuccess(String str, long j) {
        }

        @Override // com.meishe.myvideo.audio.AudioStatusListener
        public void onRecordData(short[] sArr, int i) {
        }

        @Override // com.meishe.myvideo.audio.AudioStatusListener
        public void onVoiceVolume(int i) {
        }

        @Override // com.meishe.myvideo.audio.AudioStatusListener
        public void onStartRecording(String str) {
            AudioRecordManager.this.onRecordStartListener.onRecordStart(Long.valueOf(System.currentTimeMillis()), str);
        }

        @Override // com.meishe.myvideo.audio.AudioStatusListener
        public void onWaveDate(float[] fArr, int i, String str) {
            AudioRecordManager.this.onRecordStartListener.onRecordProgress(fArr, i, str);
        }

        @Override // com.meishe.myvideo.audio.AudioStatusListener
        public void onRecordError(int i, String str) {
            AudioRecordManager.this.onRecordStartListener.onRecordFail(str);
        }

        @Override // com.meishe.myvideo.audio.AudioStatusListener
        public void onFileSaveFailed(String str) {
            AudioRecordManager.this.onRecordStartListener.onRecordFail(str);
        }

        @Override // com.meishe.myvideo.audio.AudioStatusListener
        public void onStopRecording() {
            AudioRecordManager.this.onRecordStartListener.onRecordEnd();
        }
    };
    OnRecordListener onRecordStartListener;

    public interface OnRecordListener {
        void onRecordEnd();

        void onRecordFail(String str);

        void onRecordProgress(float[] fArr, int i, String str);

        void onRecordStart(Long l, String str);
    }

    public static AudioRecordManager getInstance() {
        return ourInstance;
    }

    private AudioRecordManager() {
    }

    public AudioRecordManager init() {
        this.mAudioRecorder = AudioRecorder.getInstance().setRecordConfig(new AudioRecorder.RecordConfig(1, AudioRecorder.RecordConfig.SAMPLE_RATE_44K_HZ, 16, 2)).setAudioStatusListener(this.mAudioStatusListener).setMaxRecordTime(2147483647L).setVolumeInterval(200).setRecordFilePath(PathUtils.getAudioRecordFilePath());
        return this;
    }

    public void startRecord(Context context) {
        String str = System.currentTimeMillis() + ".wav";
        AudioRecorder audioRecorder = this.mAudioRecorder;
        if (audioRecorder != null) {
            audioRecorder.setRecordFileName(str).setPerSecondPixel((int) (PixelPerMicrosecondUtil.getPixelPerMicrosecond(context) * 1000.0d * 1000.0d));
            this.mAudioRecorder.start(context);
        }
    }

    public void stopRecord() {
        AudioRecorder audioRecorder = this.mAudioRecorder;
        if (audioRecorder != null) {
            audioRecorder.stop();
        }
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordStartListener = onRecordListener;
    }
}
