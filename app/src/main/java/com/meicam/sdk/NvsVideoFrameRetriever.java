package com.meicam.sdk;

import android.graphics.Bitmap;

public class NvsVideoFrameRetriever {
    public static final int VIDEO_FRAME_HEIGHT_GRADE_360 = 0;
    public static final int VIDEO_FRAME_HEIGHT_GRADE_480 = 1;
    public static final int VIDEO_FRAME_HEIGHT_GRADE_720 = 2;
    long m_internalReader = 0;
    long m_internalReaderFactory = 0;

    private native void nativeCleanup(long j, long j2);

    private native void nativeCreateVideoRetrieverReader(String str);

    private native Bitmap nativeGetFrameAtTime(long j, long j2, int i, int i2);

    public Bitmap getFrameAtTime(long j, int i) {
        long j2 = this.m_internalReader;
        if (j2 == 0) {
            return null;
        }
        return nativeGetFrameAtTime(j2, j, i, 0);
    }

    public void release() {
        long j = this.m_internalReader;
        if (j != 0) {
            nativeCleanup(j, this.m_internalReaderFactory);
            this.m_internalReader = 0;
            this.m_internalReaderFactory = 0;
        }
    }

    public Bitmap getFrameAtTimeWithCustomVideoFrameHeight(long j, int i) {
        long j2 = this.m_internalReader;
        if (j2 == 0 || i <= 0) {
            return null;
        }
        return nativeGetFrameAtTime(j2, j, 0, i);
    }

    NvsVideoFrameRetriever(String str) {
        nativeCreateVideoRetrieverReader(str);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        release();
        super.finalize();
    }
}
