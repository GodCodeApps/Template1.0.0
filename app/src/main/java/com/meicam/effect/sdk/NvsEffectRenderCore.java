package com.meicam.effect.sdk;

import com.meicam.sdk.NvsVideoFrameInfo;
import com.meicam.sdk.NvsVideoResolution;
import java.nio.ByteBuffer;

public class NvsEffectRenderCore {
    public static final int NV_EFFECT_CORE_ERROR_UNKNOWN = -1;
    public static final int NV_EFFECT_CORE_FLAGS_CREATE_GLCONTEXT_IF_NEED = 2;
    public static final int NV_EFFECT_CORE_FLAGS_IN_SINGLE_GLTHREAD = 1;
    public static final int NV_EFFECT_CORE_FLAGS_SUPPORT_16K = 16;
    public static final int NV_EFFECT_CORE_FLAGS_SUPPORT_4K = 4;
    public static final int NV_EFFECT_CORE_FLAGS_SUPPORT_8K = 8;
    public static final int NV_EFFECT_CORE_INVALID_TEXTURE = -2;
    public static final int NV_EFFECT_CORE_NO_ERROR = 0;
    long m_internalObject = 0;

    private native void nativeCleanUp(long j);

    private native void nativeClearCacheResources(long j);

    private native void nativeClearEffectResources(long j, NvsEffect nvsEffect);

    private native void nativeDestory(long j);

    private native ByteBuffer nativeDownloadFromTexture(long j, int i, NvsVideoResolution nvsVideoResolution, int i2, int i3, boolean z);

    private native boolean nativeInitialize(long j, int i);

    private native int nativeRenderEffect(long j, NvsEffect nvsEffect, int[] iArr, int i, byte[] bArr, NvsVideoFrameInfo nvsVideoFrameInfo, int i2, NvsVideoResolution nvsVideoResolution, int i3, long j2, int i4);

    private native ByteBuffer nativeRenderEffectWithBuffer(long j, NvsEffect nvsEffect, byte[] bArr, NvsVideoFrameInfo nvsVideoFrameInfo, int i, int i2, boolean z, long j2, int i3);

    private native int nativeUploadtoTexture(long j, byte[] bArr, NvsVideoFrameInfo nvsVideoFrameInfo, int i);

    public boolean initialize() {
        return nativeInitialize(this.m_internalObject, 0);
    }

    public boolean initialize(int i) {
        return nativeInitialize(this.m_internalObject, i);
    }

    public int renderEffect(NvsEffect nvsEffect, int i, NvsVideoResolution nvsVideoResolution, int i2, long j, int i3) {
        if (nvsEffect == null || nvsVideoResolution == null) {
            return -1;
        }
        if (i <= 0 || i2 <= 0) {
            return -2;
        }
        return nativeRenderEffect(this.m_internalObject, nvsEffect, new int[]{i}, 1, null, null, 0, nvsVideoResolution, i2, j, i3);
    }

    public int renderEffect(NvsEffect nvsEffect, int i, byte[] bArr, NvsVideoFrameInfo nvsVideoFrameInfo, int i2, NvsVideoResolution nvsVideoResolution, int i3, long j, int i4) {
        if (nvsEffect == null || nvsVideoResolution == null) {
            return -1;
        }
        if (i <= 0 || i3 <= 0) {
            return -2;
        }
        return nativeRenderEffect(this.m_internalObject, nvsEffect, new int[]{i}, 1, bArr, nvsVideoFrameInfo, i2, nvsVideoResolution, i3, j, i4);
    }

    public ByteBuffer renderEffect(NvsEffect nvsEffect, byte[] bArr, NvsVideoFrameInfo nvsVideoFrameInfo, int i, int i2, boolean z, long j, int i3) {
        if (nvsEffect == null || nvsVideoFrameInfo == null) {
            return null;
        }
        return nativeRenderEffectWithBuffer(this.m_internalObject, nvsEffect, bArr, nvsVideoFrameInfo, i, i2, z, j, i3);
    }

    public int renderEffect(NvsEffect nvsEffect, int[] iArr, int i, NvsVideoResolution nvsVideoResolution, int i2, long j, int i3) {
        if (nvsEffect == null || nvsVideoResolution == null) {
            return -1;
        }
        if (iArr == null || i2 <= 0) {
            return -2;
        }
        return nativeRenderEffect(this.m_internalObject, nvsEffect, iArr, i, null, null, 0, nvsVideoResolution, i2, j, i3);
    }

    public int uploadVideoFrameToTexture(byte[] bArr, NvsVideoFrameInfo nvsVideoFrameInfo, int i) {
        if (nvsVideoFrameInfo == null || bArr == null || i <= 0) {
            return -1;
        }
        return nativeUploadtoTexture(this.m_internalObject, bArr, nvsVideoFrameInfo, i);
    }

    public ByteBuffer downloadFromTexture(int i, NvsVideoResolution nvsVideoResolution, int i2, int i3) {
        if (i <= 0 || nvsVideoResolution == null) {
            return null;
        }
        return nativeDownloadFromTexture(this.m_internalObject, i, nvsVideoResolution, i2, i3, false);
    }

    public ByteBuffer downloadFromTexture(int i, NvsVideoResolution nvsVideoResolution, int i2, int i3, boolean z) {
        if (i <= 0 || nvsVideoResolution == null) {
            return null;
        }
        return nativeDownloadFromTexture(this.m_internalObject, i, nvsVideoResolution, i2, i3, z);
    }

    public void clearEffectResources(NvsEffect nvsEffect) {
        nativeClearEffectResources(this.m_internalObject, nvsEffect);
    }

    public void clearCacheResources() {
        nativeClearCacheResources(this.m_internalObject);
    }

    public void cleanUp() {
        nativeCleanUp(this.m_internalObject);
    }

    /* access modifiers changed from: protected */
    public void setInternalObject(long j) {
        this.m_internalObject = j;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        long j = this.m_internalObject;
        if (j != 0) {
            nativeDestory(j);
            this.m_internalObject = 0;
        }
        super.finalize();
    }
}
