package com.meicam.sdk;

import com.meicam.sdk.NvsCustomVideoFx;
import com.meicam.sdk.NvsCustomVideoTransition;
import java.util.List;

public class NvsVideoTrack extends NvsTrack {
    private native NvsTrackVideoFx nativeAddBuiltinTrackVideoFx(long j, long j2, long j3, String str);

    private native NvsVideoClip nativeAddClip(long j, String str, long j2);

    private native NvsVideoClip nativeAddClip(long j, String str, long j2, long j3, long j4);

    private native NvsTrackVideoFx nativeAddCustomTrackVideoFx(long j, long j2, long j3, NvsCustomVideoFx.Renderer renderer);

    private native NvsTrackVideoFx nativeAddPackagedTrackVideoFx(long j, long j2, long j3, String str);

    private native NvsVideoClip nativeGetClipByIndex(long j, int i);

    private native NvsVideoClip nativeGetClipByTimelinePosition(long j, long j2);

    private native NvsTrackVideoFx nativeGetFirstTrackVideoFx(long j);

    private native NvsTrackVideoFx nativeGetLastTrackVideoFx(long j);

    private native NvsTrackVideoFx nativeGetNextTrackVideoFx(long j, NvsTrackVideoFx nvsTrackVideoFx);

    private native NvsTrackVideoFx nativeGetPrevTrackVideoFx(long j, NvsTrackVideoFx nvsTrackVideoFx);

    private native List<NvsTrackVideoFx> nativeGetTrackVideoFxByPosition(long j, long j2);

    private native NvsVideoTransition nativeGetTransitionBySourceClipIndex(long j, int i);

    private native NvsVideoClip nativeInsertClip(long j, String str, int i);

    private native NvsVideoClip nativeInsertClip(long j, String str, long j2, long j3, int i);

    private native boolean nativeIsOriginalRender(long j);

    private native NvsTrackVideoFx nativeRemoveTrackVideoFx(long j, NvsTrackVideoFx nvsTrackVideoFx);

    private native NvsVideoTransition nativeSetBuiltinTransition(long j, int i, String str);

    private native NvsVideoTransition nativeSetCustomVideoTransition(long j, int i, NvsCustomVideoTransition.Renderer renderer);

    private native void nativeSetEnableOriginalRender(long j, boolean z);

    private native NvsVideoTransition nativeSetPackagedTransition(long j, int i, String str);

    public NvsVideoClip appendClip(String str) {
        NvsUtils.checkFunctionInMainThread();
        return insertClip(str, getClipCount());
    }

    public NvsVideoClip appendClip(String str, long j, long j2) {
        NvsUtils.checkFunctionInMainThread();
        return insertClip(str, j, j2, getClipCount());
    }

    public NvsVideoClip insertClip(String str, int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertClip(this.m_internalObject, str, i);
    }

    public NvsVideoClip insertClip(String str, long j, long j2, int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertClip(this.m_internalObject, str, j, j2, i);
    }

    public NvsVideoClip addClip(String str, long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeAddClip(this.m_internalObject, str, j);
    }

    public NvsVideoClip addClip(String str, long j, long j2, long j3) {
        NvsUtils.checkFunctionInMainThread();
        return nativeAddClip(this.m_internalObject, str, j, j2, j3);
    }

    public NvsVideoClip getClipByIndex(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetClipByIndex(this.m_internalObject, i);
    }

    public NvsVideoClip getClipByTimelinePosition(long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetClipByTimelinePosition(this.m_internalObject, j);
    }

    public NvsVideoTransition setBuiltinTransition(int i, String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeSetBuiltinTransition(this.m_internalObject, i, str);
    }

    public NvsVideoTransition setPackagedTransition(int i, String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeSetPackagedTransition(this.m_internalObject, i, str);
    }

    public NvsVideoTransition setCustomVideoTransition(int i, NvsCustomVideoTransition.Renderer renderer) {
        NvsUtils.checkFunctionInMainThread();
        return nativeSetCustomVideoTransition(this.m_internalObject, i, renderer);
    }

    public NvsVideoTransition getTransitionBySourceClipIndex(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetTransitionBySourceClipIndex(this.m_internalObject, i);
    }

    public void setEnableOriginalRender(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetEnableOriginalRender(this.m_internalObject, z);
    }

    public boolean isOriginalRender() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsOriginalRender(this.m_internalObject);
    }

    public NvsTrackVideoFx getFirstTrackVideoFx() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetFirstTrackVideoFx(this.m_internalObject);
    }

    public NvsTrackVideoFx getLastTrackVideoFx() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetLastTrackVideoFx(this.m_internalObject);
    }

    public NvsTrackVideoFx getPrevTrackVideoFx(NvsTrackVideoFx nvsTrackVideoFx) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetPrevTrackVideoFx(this.m_internalObject, nvsTrackVideoFx);
    }

    public NvsTrackVideoFx getNextTrackVideoFx(NvsTrackVideoFx nvsTrackVideoFx) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetNextTrackVideoFx(this.m_internalObject, nvsTrackVideoFx);
    }

    public List<NvsTrackVideoFx> getTrackVideoFxByPosition(long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetTrackVideoFxByPosition(this.m_internalObject, j);
    }

    public NvsTrackVideoFx addBuiltinTrackVideoFx(long j, long j2, String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeAddBuiltinTrackVideoFx(this.m_internalObject, j, j2, str);
    }

    public NvsTrackVideoFx addPackagedTrackVideoFx(long j, long j2, String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeAddPackagedTrackVideoFx(this.m_internalObject, j, j2, str);
    }

    public NvsTrackVideoFx addCustomTrackVideoFx(long j, long j2, NvsCustomVideoFx.Renderer renderer) {
        NvsUtils.checkFunctionInMainThread();
        return nativeAddCustomTrackVideoFx(this.m_internalObject, j, j2, renderer);
    }

    public NvsTrackVideoFx removeTrackVideoFx(NvsTrackVideoFx nvsTrackVideoFx) {
        NvsUtils.checkFunctionInMainThread();
        return nativeRemoveTrackVideoFx(this.m_internalObject, nvsTrackVideoFx);
    }
}
