package com.meishe.myvideo.audio.function;

public interface AudioFileListener {
    void onFailure(String str);

    void onSuccess(String str, long j);
}
