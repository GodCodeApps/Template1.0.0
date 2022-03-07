package com.meicam.sdk;

import android.graphics.Bitmap;

public class NvsVideoKeyFrameRetriever {
    long m_internalObject = 0;
    int m_videoFrameHeight;

    public static class KeyFrame {
        public Bitmap bitmap;
        public long timestamp;
    }

    private native void nativeCleanup(long j);

    private native void nativeCreateVideoRetrieverReader(String str, boolean z);

    private native KeyFrame nativeGetNextKeyFrame(long j, int i);

    private native void nativeStartGettingKeyFrame(long j, long j2);

    NvsVideoKeyFrameRetriever(String str, int i, boolean z) {
        nativeCreateVideoRetrieverReader(str, z);
        this.m_videoFrameHeight = i;
    }

    public void release() {
        long j = this.m_internalObject;
        if (j != 0) {
            nativeCleanup(j);
            this.m_internalObject = 0;
        }
    }

    public void startGettingKeyFrame(long j) {
        nativeStartGettingKeyFrame(this.m_internalObject, j);
    }

    public KeyFrame getNextKeyFrame() {
        long j = this.m_internalObject;
        if (j == 0) {
            return null;
        }
        return nativeGetNextKeyFrame(j, this.m_videoFrameHeight);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        long j = this.m_internalObject;
        if (j != 0) {
            nativeCleanup(j);
            this.m_internalObject = 0;
        }
        super.finalize();
    }
}
