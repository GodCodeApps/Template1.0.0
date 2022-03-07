package com.meishe.myvideo.audio.function;

public interface RecorderCallback {
    void onRecorded(short[] sArr);

    void onRecordedFail(int i);

    boolean onRecorderStart();

    void onRecorderStop();
}
