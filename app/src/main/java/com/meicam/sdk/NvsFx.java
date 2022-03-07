package com.meicam.sdk;

public class NvsFx extends NvsObject {
    public static final int KEY_FRAME_FIND_MODE_INPUT_TIME_AFTER = 2;
    public static final int KEY_FRAME_FIND_MODE_INPUT_TIME_BEFORE = 1;

    private native long nativeFindKeyframeTime(long j, String str, long j2, int i);

    private native NvsARFaceContext nativeGetARFaceContext(long j);

    private native NvsARSceneManipulate nativeGetARSceneManipulate(long j);

    private native boolean nativeGetBooleanVal(long j, String str, long j2);

    private native NvsColor nativeGetColorVal(long j, String str, long j2);

    private native NvsFxDescription nativeGetDescription(long j);

    private native float nativeGetFilterIntensity(long j);

    private native boolean nativeGetFilterMask(long j);

    private native double nativeGetFloatVal(long j, String str, long j2);

    private native boolean nativeGetIgnoreBackground(long j);

    private native int nativeGetIntVal(long j, String str, long j2);

    private native boolean nativeGetInverseRegion(long j);

    private native String nativeGetMenuVal(long j, String str, long j2);

    private native NvsPaintingEffectContext nativeGetPaintingEffectContext(long j);

    private native NvsParticleSystemContext nativeGetParticleSystemContext(long j);

    private native NvsPosition2D nativeGetPosition2DVal(long j, String str, long j2);

    private native NvsPosition3D nativeGetPosition3DVal(long j, String str, long j2);

    private native boolean nativeGetRegional(long j);

    private native float nativeGetRegionalFeatherWidth(long j);

    private native String nativeGetStringVal(long j, String str, long j2);

    private native boolean nativeHasKeyframeList(long j, String str);

    private native boolean nativeRemoveAllKeyframe(long j, String str);

    private native boolean nativeRemoveKeyframeAtTime(long j, String str, long j2);

    private native void nativeSetArbDataVal(long j, String str, NvsArbitraryData nvsArbitraryData, long j2);

    private native void nativeSetBooleanVal(long j, String str, boolean z, long j2);

    private native void nativeSetColorVal(long j, String str, NvsColor nvsColor, long j2);

    private native void nativeSetFilterIntensity(long j, float f);

    private native void nativeSetFilterMask(long j, boolean z);

    private native void nativeSetFloatVal(long j, String str, double d, long j2);

    private native void nativeSetIgnoreBackground(long j, boolean z);

    private native void nativeSetIntVal(long j, String str, int i, long j2);

    private native void nativeSetInverseRegion(long j, boolean z);

    private native void nativeSetMenuVal(long j, String str, String str2, long j2);

    private native void nativeSetPosition2DVal(long j, String str, NvsPosition2D nvsPosition2D, long j2);

    private native void nativeSetPosition3DVal(long j, String str, NvsPosition3D nvsPosition3D, long j2);

    private native void nativeSetRegion(long j, float[] fArr);

    private native void nativeSetRegionInfo(long j, NvsMaskRegionInfo nvsMaskRegionInfo, long j2);

    private native void nativeSetRegional(long j, boolean z);

    private native void nativeSetRegionalFeatherWidth(long j, float f);

    private native void nativeSetStringVal(long j, String str, String str2, long j2);

    public NvsFxDescription getDescription() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetDescription(this.m_internalObject);
    }

    public void setIntVal(String str, int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetIntVal(getInternalObject(), str, i, -1);
    }

    public int getIntVal(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetIntVal(getInternalObject(), str, -1);
    }

    public void setIntValAtTime(String str, int i, long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetIntVal(getInternalObject(), str, i, j);
    }

    public int getIntValAtTime(String str, long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetIntVal(getInternalObject(), str, j);
    }

    public void setFloatVal(String str, double d) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetFloatVal(getInternalObject(), str, d, -1);
    }

    public double getFloatVal(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetFloatVal(getInternalObject(), str, -1);
    }

    public void setFloatValAtTime(String str, double d, long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetFloatVal(getInternalObject(), str, d, j);
    }

    public double getFloatValAtTime(String str, long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetFloatVal(getInternalObject(), str, j);
    }

    public void setBooleanVal(String str, boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetBooleanVal(getInternalObject(), str, z, -1);
    }

    public boolean getBooleanVal(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetBooleanVal(getInternalObject(), str, -1);
    }

    public void setBooleanValAtTime(String str, boolean z, long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetBooleanVal(getInternalObject(), str, z, j);
    }

    public boolean getBooleanValAtTime(String str, long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetBooleanVal(getInternalObject(), str, j);
    }

    public void setStringVal(String str, String str2) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetStringVal(getInternalObject(), str, str2, -1);
    }

    public String getStringVal(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetStringVal(getInternalObject(), str, -1);
    }

    public void setStringValAtTime(String str, String str2, long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetStringVal(getInternalObject(), str, str2, j);
    }

    public String getStringValAtTime(String str, long j, int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetStringVal(getInternalObject(), str, j);
    }

    public void setColorVal(String str, NvsColor nvsColor) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetColorVal(getInternalObject(), str, nvsColor, -1);
    }

    public NvsColor getColorVal(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetColorVal(getInternalObject(), str, -1);
    }

    public void setColorValAtTime(String str, NvsColor nvsColor, long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetColorVal(getInternalObject(), str, nvsColor, j);
    }

    public NvsColor getColorValAtTime(String str, long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetColorVal(getInternalObject(), str, j);
    }

    public void setPosition2DVal(String str, NvsPosition2D nvsPosition2D) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetPosition2DVal(getInternalObject(), str, nvsPosition2D, -1);
    }

    public NvsPosition2D getPosition2DVal(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetPosition2DVal(getInternalObject(), str, -1);
    }

    public void setPosition2DValAtTime(String str, NvsPosition2D nvsPosition2D, long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetPosition2DVal(getInternalObject(), str, nvsPosition2D, j);
    }

    public NvsPosition2D getPosition2DValAtTime(String str, long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetPosition2DVal(getInternalObject(), str, j);
    }

    public void setPosition3DVal(String str, NvsPosition3D nvsPosition3D) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetPosition3DVal(getInternalObject(), str, nvsPosition3D, -1);
    }

    public NvsPosition3D getPosition3DVal(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetPosition3DVal(getInternalObject(), str, -1);
    }

    public void setPosition3DValAtTime(String str, NvsPosition3D nvsPosition3D, long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetPosition3DVal(getInternalObject(), str, nvsPosition3D, j);
    }

    public NvsPosition3D getPosition3DValAtTime(String str, long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetPosition3DVal(getInternalObject(), str, j);
    }

    public void setMenuVal(String str, String str2) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetMenuVal(getInternalObject(), str, str2, -1);
    }

    public String getMenuVal(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetMenuVal(getInternalObject(), str, -1);
    }

    public void setMenuValAtTime(String str, String str2, long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetMenuVal(getInternalObject(), str, str2, j);
    }

    public String getMenuValAtTime(String str, long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetMenuVal(getInternalObject(), str, j);
    }

    public void setArbDataVal(String str, NvsArbitraryData nvsArbitraryData) {
        nativeSetArbDataVal(getInternalObject(), str, nvsArbitraryData, -1);
    }

    public void setArbDataValAtTime(String str, NvsArbitraryData nvsArbitraryData, long j) {
        nativeSetArbDataVal(getInternalObject(), str, nvsArbitraryData, j);
    }

    public boolean removeKeyframeAtTime(String str, long j) {
        return nativeRemoveKeyframeAtTime(getInternalObject(), str, j);
    }

    public boolean removeAllKeyframe(String str) {
        return nativeRemoveAllKeyframe(getInternalObject(), str);
    }

    public boolean hasKeyframeList(String str) {
        return nativeHasKeyframeList(getInternalObject(), str);
    }

    public long findKeyframeTime(String str, long j, int i) {
        return nativeFindKeyframeTime(getInternalObject(), str, j, i);
    }

    public void setFilterIntensity(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetFilterIntensity(getInternalObject(), f);
    }

    public float getFilterIntensity() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetFilterIntensity(getInternalObject());
    }

    public void setFilterMask(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetFilterMask(getInternalObject(), z);
    }

    public boolean getFilterMask() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetFilterMask(getInternalObject());
    }

    public void setRegional(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetRegional(getInternalObject(), z);
    }

    public boolean getRegional() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetRegional(getInternalObject());
    }

    public void setIgnoreBackground(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetIgnoreBackground(getInternalObject(), z);
    }

    public boolean getIgnoreBackground() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetIgnoreBackground(getInternalObject());
    }

    public void setInverseRegion(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetInverseRegion(getInternalObject(), z);
    }

    public boolean getInverseRegion() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetInverseRegion(getInternalObject());
    }

    public void setRegion(float[] fArr) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetRegion(getInternalObject(), fArr);
    }

    public void setRegionInfo(NvsMaskRegionInfo nvsMaskRegionInfo) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetRegionInfo(getInternalObject(), nvsMaskRegionInfo, -1);
    }

    public void setRegionInfoAtTime(NvsMaskRegionInfo nvsMaskRegionInfo, long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetRegionInfo(getInternalObject(), nvsMaskRegionInfo, j);
    }

    public void setRegionalFeatherWidth(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetRegionalFeatherWidth(getInternalObject(), f);
    }

    public float getRegionalFeatherWidth() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetRegionalFeatherWidth(getInternalObject());
    }

    public NvsParticleSystemContext getParticleSystemContext() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetParticleSystemContext(this.m_internalObject);
    }

    public NvsARFaceContext getARFaceContext() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetARFaceContext(this.m_internalObject);
    }

    public NvsPaintingEffectContext getPaintingEffectContext() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetPaintingEffectContext(this.m_internalObject);
    }

    public NvsARSceneManipulate getARSceneManipulate() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetARSceneManipulate(this.m_internalObject);
    }
}
