package com.meicam.sdk;

public class NvsVideoTransition extends NvsFx {
    public static final int VIDEO_TRANSITION_DURATION_MATCH_MODE_NONE = 0;
    public static final int VIDEO_TRANSITION_DURATION_MATCH_MODE_STRETCH = 1;
    public static final int VIDEO_TRANSITION_TYPE_BUILTIN = 0;
    public static final int VIDEO_TRANSITION_TYPE_CUSTOM = 2;
    public static final int VIDEO_TRANSITION_TYPE_PACKAGE = 1;

    private native void nativeEnableTimelineTransition(long j, boolean z);

    private native String nativeGetBuiltinVideoTransitionName(long j);

    private native long nativeGetVideoTransitionDuration(long j);

    private native int nativeGetVideoTransitionDurationMatchMode(long j);

    private native float nativeGetVideoTransitionDurationScaleFactor(long j);

    private native String nativeGetVideoTransitionPackageId(long j);

    private native int nativeGetVideoTransitionType(long j);

    private native boolean nativeIsTimelineTransitionEnabled(long j);

    private native void nativeSetVideoTransitionDuration(long j, long j2, int i);

    private native void nativeSetVideoTransitionDurationScaleFactor(long j, float f);

    public int getVideoTransitionType() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetVideoTransitionType(this.m_internalObject);
    }

    public String getBuiltinVideoTransitionName() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetBuiltinVideoTransitionName(this.m_internalObject);
    }

    public String getVideoTransitionPackageId() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetVideoTransitionPackageId(this.m_internalObject);
    }

    public void setVideoTransitionDurationScaleFactor(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetVideoTransitionDurationScaleFactor(this.m_internalObject, f);
    }

    public float getVideoTransitionDurationScaleFactor() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetVideoTransitionDurationScaleFactor(this.m_internalObject);
    }

    public void setVideoTransitionDuration(long j, int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetVideoTransitionDuration(this.m_internalObject, j, i);
    }

    public long getVideoTransitionDuration() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetVideoTransitionDuration(this.m_internalObject);
    }

    public long getVideoTransitionDurationMatchMode() {
        NvsUtils.checkFunctionInMainThread();
        return (long) nativeGetVideoTransitionDurationMatchMode(this.m_internalObject);
    }

    public void enableTimelineTransition(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeEnableTimelineTransition(this.m_internalObject, z);
    }

    public boolean isTimelineTransitionEnabled() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsTimelineTransitionEnabled(this.m_internalObject);
    }
}
