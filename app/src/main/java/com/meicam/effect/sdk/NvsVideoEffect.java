package com.meicam.effect.sdk;

import android.graphics.PointF;
import com.meicam.sdk.NvsUtils;
import com.meicam.sdk.NvsVideoResolution;

public class NvsVideoEffect extends NvsEffect {
    public static final int VIDEOFX_TYPE_BUILTIN = 0;
    public static final int VIDEOFX_TYPE_CUSTOM = 2;
    public static final int VIDEOFX_TYPE_PACKAGE = 1;

    private native String nativeGetBuiltinVideoFxName(long j);

    private native String nativeGetVideoFxPackageId(long j);

    private native int nativeGetVideoFxType(long j);

    private native PointF nativeMapPointFromCanonicalToParticleSystem(long j, NvsVideoResolution nvsVideoResolution, PointF pointF);

    public int getVideoFxType() {
        return nativeGetVideoFxType(this.m_internalObject);
    }

    public String getBuiltinVideoFxName() {
        return nativeGetBuiltinVideoFxName(this.m_internalObject);
    }

    public String getVideoFxPackageId() {
        return nativeGetVideoFxPackageId(this.m_internalObject);
    }

    public PointF mapPointFromCanonicalToParticleSystem(NvsVideoResolution nvsVideoResolution, PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        return nativeMapPointFromCanonicalToParticleSystem(this.m_internalObject, nvsVideoResolution, pointF);
    }
}
