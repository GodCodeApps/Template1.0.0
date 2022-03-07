package com.meicam.sdk;

import android.os.Handler;
import android.os.Looper;

public class NvsARFaceContext {
    public static final int OBJECT_LAND_MARK_TYPE_ANIMAL = 1;
    public static final int OBJECT_LAND_MARK_TYPE_FACE = 0;
    public static final int OBJECT_TRACKING_TYPE_ANIMAL = 1;
    public static final int OBJECT_TRACKING_TYPE_FACE = 0;
    NvsARFaceContextCallback m_callback = null;
    NvsARFaceContextInternalCallback m_callbackinternal = null;
    private long m_contextInterface;
    NvsARFaceContextErrorCallback m_errorCallback = null;
    NvsARFaceContextLandmarkCallback m_landmarkCallback = null;
    Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface NvsARFaceContextCallback {
        void notifyFaceItemLoadingBegin(String str);

        void notifyFaceItemLoadingFinish();
    }

    public interface NvsARFaceContextErrorCallback {
        void notifyFaceItemLoadingFailed(String str, int i);
    }

    /* access modifiers changed from: private */
    public interface NvsARFaceContextInternalCallback {
        void notifyFaceItemLoadingBegin(String str);

        void notifyFaceItemLoadingFailed(String str, int i);

        void notifyFaceItemLoadingFinish();

        void notifyObjectLandmark(float[] fArr, int i, int i2, long j);
    }

    public interface NvsARFaceContextLandmarkCallback {
        void notifyObjectLandmark(float[] fArr, int i, int i2, long j);
    }

    private native void nativeCleanup(long j);

    private native boolean nativeIsObjectTracking(long j, int i);

    private native void nativeSetARFaceCallback(long j, NvsARFaceContextInternalCallback nvsARFaceContextInternalCallback);

    private native void nativeSetDualBufferInputUsed(long j, boolean z);

    private native void nativeSetReloadCurSticker(long j, boolean z);

    public boolean isFaceTracking() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsObjectTracking(this.m_contextInterface, 0);
    }

    public boolean isObjectTracking(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsObjectTracking(this.m_contextInterface, i);
    }

    public void setContextCallback(NvsARFaceContextCallback nvsARFaceContextCallback) {
        this.m_callback = nvsARFaceContextCallback;
        if (this.m_callback != null) {
            createInternalCallback();
        } else if (this.m_errorCallback == null && this.m_landmarkCallback == null) {
            this.m_callbackinternal = null;
        }
        nativeSetARFaceCallback(this.m_contextInterface, this.m_callbackinternal);
    }

    public void setContextLandmarkCallback(NvsARFaceContextLandmarkCallback nvsARFaceContextLandmarkCallback) {
        this.m_landmarkCallback = nvsARFaceContextLandmarkCallback;
        if (this.m_landmarkCallback != null) {
            createInternalCallback();
        } else if (this.m_callback == null && this.m_errorCallback == null) {
            this.m_callbackinternal = null;
        }
        nativeSetARFaceCallback(this.m_contextInterface, this.m_callbackinternal);
    }

    public void setContextErrorCallback(NvsARFaceContextErrorCallback nvsARFaceContextErrorCallback) {
        this.m_errorCallback = nvsARFaceContextErrorCallback;
        if (this.m_errorCallback != null) {
            createInternalCallback();
        } else if (this.m_callback == null && this.m_landmarkCallback == null) {
            this.m_callbackinternal = null;
        }
        nativeSetARFaceCallback(this.m_contextInterface, this.m_callbackinternal);
    }

    public void setDualBufferInputUsed(boolean z) {
        nativeSetDualBufferInputUsed(this.m_contextInterface, z);
    }

    public void setReloadCurSticker() {
        nativeSetReloadCurSticker(this.m_contextInterface, true);
    }

    public void release() {
        this.m_callbackinternal = null;
        long j = this.m_contextInterface;
        if (j != 0) {
            nativeCleanup(j);
            this.m_contextInterface = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void setContextInterface(long j) {
        this.m_contextInterface = j;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        release();
        super.finalize();
    }

    private void createInternalCallback() {
        if (this.m_callbackinternal == null) {
            this.m_callbackinternal = new NvsARFaceContextInternalCallback() {
                /* class com.meicam.sdk.NvsARFaceContext.AnonymousClass1 */

                @Override // com.meicam.sdk.NvsARFaceContext.NvsARFaceContextInternalCallback
                public void notifyFaceItemLoadingBegin(final String str) {
                    NvsARFaceContext.this.mainHandler.post(new Runnable() {
                        /* class com.meicam.sdk.NvsARFaceContext.AnonymousClass1.AnonymousClass1 */

                        public void run() {
                            if (NvsARFaceContext.this.m_callback != null) {
                                NvsARFaceContext.this.m_callback.notifyFaceItemLoadingBegin(str);
                            }
                        }
                    });
                }

                @Override // com.meicam.sdk.NvsARFaceContext.NvsARFaceContextInternalCallback
                public void notifyFaceItemLoadingFinish() {
                    NvsARFaceContext.this.mainHandler.post(new Runnable() {
                        /* class com.meicam.sdk.NvsARFaceContext.AnonymousClass1.AnonymousClass2 */

                        public void run() {
                            if (NvsARFaceContext.this.m_callback != null) {
                                NvsARFaceContext.this.m_callback.notifyFaceItemLoadingFinish();
                            }
                        }
                    });
                }

                @Override // com.meicam.sdk.NvsARFaceContext.NvsARFaceContextInternalCallback
                public void notifyFaceItemLoadingFailed(final String str, final int i) {
                    NvsARFaceContext.this.mainHandler.post(new Runnable() {
                        /* class com.meicam.sdk.NvsARFaceContext.AnonymousClass1.AnonymousClass3 */

                        public void run() {
                            if (NvsARFaceContext.this.m_errorCallback != null) {
                                NvsARFaceContext.this.m_errorCallback.notifyFaceItemLoadingFailed(str, i);
                            }
                        }
                    });
                }

                @Override // com.meicam.sdk.NvsARFaceContext.NvsARFaceContextInternalCallback
                public void notifyObjectLandmark(final float[] fArr, final int i, final int i2, final long j) {
                    NvsARFaceContext.this.mainHandler.post(new Runnable() {
                        /* class com.meicam.sdk.NvsARFaceContext.AnonymousClass1.AnonymousClass4 */

                        public void run() {
                            if (NvsARFaceContext.this.m_landmarkCallback != null) {
                                NvsARFaceContext.this.m_landmarkCallback.notifyObjectLandmark(fArr, i, i2, j);
                            }
                        }
                    });
                }
            };
        }
    }
}
