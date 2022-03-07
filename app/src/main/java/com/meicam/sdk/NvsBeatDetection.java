package com.meicam.sdk;

import java.util.ArrayList;

public class NvsBeatDetection {
    private static final String TAG = "Meicam";
    private static NvsBeatDetection m_instance;
    private BeatDetectionCallback m_BeatDetectionCallback;

    public interface BeatDetectionCallback {
        void onBeatDetectionFinished(ArrayList<Long> arrayList, ArrayList<Long> arrayList2, int i);

        void onBeatDetectionProgress(float f);
    }

    private static native void nativeClose();

    private static native boolean nativeInit(int i);

    private static native void nativeSetBeatDetectionCallback(BeatDetectionCallback beatDetectionCallback);

    private static native boolean nativeStartDetect(String str, int i);

    private NvsBeatDetection() {
    }

    public static NvsBeatDetection init(int i) {
        NvsBeatDetection nvsBeatDetection = m_instance;
        if (nvsBeatDetection != null) {
            return nvsBeatDetection;
        }
        if (!nativeInit(i)) {
            return null;
        }
        m_instance = new NvsBeatDetection();
        return m_instance;
    }

    public static void close() {
        NvsBeatDetection nvsBeatDetection = m_instance;
        if (nvsBeatDetection != null) {
            nvsBeatDetection.setBeatDetectionCallback(null);
            m_instance = null;
            nativeClose();
        }
    }

    public boolean startDetect(String str, int i) {
        return nativeStartDetect(str, i);
    }

    public void setBeatDetectionCallback(BeatDetectionCallback beatDetectionCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_BeatDetectionCallback = beatDetectionCallback;
        nativeSetBeatDetectionCallback(beatDetectionCallback);
    }

    public static NvsBeatDetection getInstance() {
        NvsUtils.checkFunctionInMainThread();
        return m_instance;
    }
}
