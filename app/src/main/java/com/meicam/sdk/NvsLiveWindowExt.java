package com.meicam.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import com.meicam.sdk.NvsLiveWindow;
import java.util.ArrayList;
import java.util.Iterator;

public class NvsLiveWindowExt extends TextureView implements TextureView.SurfaceTextureListener {
    public static final int FILLMODE_PRESERVEASPECTCROP = 0;
    public static final int FILLMODE_PRESERVEASPECTFIT = 1;
    public static final int FILLMODE_PRESERVEASPECTFIT_BLUR = 3;
    public static final int FILLMODE_STRETCH = 2;
    protected int m_fillMode = 0;
    private Object m_frameInfoMutex = new Object();
    protected long m_internalObject = 0;
    private View m_overlayBuddy = null;
    private ArrayList<NvsLiveWindow.VideoFrameInfo> m_pendingVideoFrameInfoList = new ArrayList<>();
    private Surface m_surface;
    private boolean m_verbose = false;
    private NvsLiveWindow.VideoFrameCallback m_videoFrameCallback = null;

    private native void nativeClearVideoFrame(long j);

    private native void nativeClose(long j);

    private native boolean nativeGetStopRenderingBeforeNextSurfaceChange(long j);

    private native void nativeInit(boolean z);

    private native PointF nativeMapCanonicalToView(long j, PointF pointF);

    private native PointF nativeMapNormalizedToView(long j, PointF pointF);

    private native PointF nativeMapViewToCanonical(long j, PointF pointF);

    private native PointF nativeMapViewToNormalized(long j, PointF pointF);

    private native void nativeOnSizeChanged(long j, int i, int i2);

    private native void nativeRecordUpdateTime(long j);

    private native void nativeRepaintVideoFrame(long j);

    private native void nativeSetBackgroundColor(long j, float f, float f2, float f3);

    private native void nativeSetFillMode(long j, int i);

    private native void nativeSetStopRenderingBeforeNextSurfaceChange(long j, boolean z);

    private native void nativeSetVideoFrameCallback(long j, NvsLiveWindow.InternalVideoFrameCallback internalVideoFrameCallback);

    private native void nativeSurfaceChanged(long j, Surface surface, int i, int i2);

    private native void nativeSurfaceDestroyed(long j);

    private native Bitmap nativeTakeScreenshot(long j);

    public NvsLiveWindowExt(Context context) {
        super(context);
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public NvsLiveWindowExt(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public NvsLiveWindowExt(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public NvsLiveWindowExt(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public void setFillMode(int i) {
        NvsUtils.checkFunctionInMainThread();
        if (i != this.m_fillMode) {
            this.m_fillMode = i;
            nativeSetFillMode(this.m_internalObject, i);
        }
    }

    public int getFillMode() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_fillMode;
    }

    public PointF mapCanonicalToView(PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        return nativeMapCanonicalToView(this.m_internalObject, pointF);
    }

    public PointF mapViewToCanonical(PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        return nativeMapViewToCanonical(this.m_internalObject, pointF);
    }

    public PointF mapNormalizedToView(PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        return nativeMapNormalizedToView(this.m_internalObject, pointF);
    }

    public PointF mapViewToNormalized(PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        return nativeMapViewToNormalized(this.m_internalObject, pointF);
    }

    public void repaintVideoFrame() {
        NvsUtils.checkFunctionInMainThread();
        nativeRepaintVideoFrame(this.m_internalObject);
    }

    public void clearVideoFrame() {
        NvsUtils.checkFunctionInMainThread();
        nativeClearVideoFrame(this.m_internalObject);
    }

    public Bitmap takeScreenshot() {
        NvsUtils.checkFunctionInMainThread();
        return nativeTakeScreenshot(this.m_internalObject);
    }

    public void setBackgroundColor(float f, float f2, float f3) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetBackgroundColor(this.m_internalObject, f, f2, f3);
    }

    public void setOverlayBuddy(View view) {
        NvsUtils.checkFunctionInMainThread();
        this.m_overlayBuddy = view;
    }

    public void setVideoFrameCallback(NvsLiveWindow.VideoFrameCallback videoFrameCallback) {
        NvsUtils.checkFunctionInMainThread();
        if (!isInEditMode()) {
            this.m_videoFrameCallback = videoFrameCallback;
            if (videoFrameCallback != null) {
                nativeSetVideoFrameCallback(this.m_internalObject, new NvsLiveWindow.InternalVideoFrameCallback() {
                    /* class com.meicam.sdk.NvsLiveWindowExt.AnonymousClass1 */

                    @Override // com.meicam.sdk.NvsLiveWindow.InternalVideoFrameCallback
                    public void onVideoFrameRendered(NvsLiveWindow.VideoFrameInfo videoFrameInfo) {
                        synchronized (NvsLiveWindowExt.this.m_frameInfoMutex) {
                            if (videoFrameInfo.frameId < 0) {
                                NvsLiveWindowExt.this.m_pendingVideoFrameInfoList.clear();
                            }
                            NvsLiveWindowExt.this.m_pendingVideoFrameInfoList.add(videoFrameInfo);
                        }
                    }
                });
            } else {
                nativeSetVideoFrameCallback(this.m_internalObject, null);
            }
        }
    }

    public void setStopRenderingBeforeNextSurfaceChange(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetStopRenderingBeforeNextSurfaceChange(this.m_internalObject, z);
    }

    public boolean getStopRenderingBeforeNextSurfaceChange() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetStopRenderingBeforeNextSurfaceChange(this.m_internalObject);
    }

    public void invalidate() {
        super.invalidate();
        View view = this.m_overlayBuddy;
        if (view != null) {
            view.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.m_overlayBuddy = null;
        this.m_videoFrameCallback = null;
        nativeSetVideoFrameCallback(this.m_internalObject, null);
        if (!isInEditMode()) {
            destroyCurrentSurface();
            long j = this.m_internalObject;
            if (j != 0) {
                nativeClose(j);
                this.m_internalObject = 0;
            }
        }
        setSurfaceTextureListener(null);
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (!isInEditMode()) {
            nativeOnSizeChanged(this.m_internalObject, i, i2);
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        destroyCurrentSurface();
        if (!isInEditMode() && i >= 1 && i2 >= 1) {
            this.m_surface = new Surface(surfaceTexture);
            nativeSurfaceChanged(this.m_internalObject, this.m_surface, i, i2);
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        destroyCurrentSurface();
        if (!isInEditMode() && i >= 1 && i2 >= 1) {
            this.m_surface = new Surface(surfaceTexture);
            nativeSurfaceChanged(this.m_internalObject, this.m_surface, i, i2);
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (isInEditMode()) {
            return true;
        }
        destroyCurrentSurface();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        if (this.m_videoFrameCallback != null) {
            nativeRecordUpdateTime(this.m_internalObject);
            long timestamp = surfaceTexture.getTimestamp();
            if (this.m_verbose) {
                Log.d("Meishe", "surface texture updated, frame id=" + timestamp);
            }
            NvsLiveWindow.VideoFrameInfo videoFrameInfo = null;
            synchronized (this.m_frameInfoMutex) {
                Iterator<NvsLiveWindow.VideoFrameInfo> it = this.m_pendingVideoFrameInfoList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    NvsLiveWindow.VideoFrameInfo next = it.next();
                    if (next.frameId == timestamp) {
                        if (this.m_verbose) {
                            Log.d("Meishe", "Found frame info, frame id=" + timestamp);
                        }
                        videoFrameInfo = next;
                    }
                }
                if (videoFrameInfo != null) {
                    while (true) {
                        if (this.m_pendingVideoFrameInfoList.isEmpty()) {
                            break;
                        } else if (this.m_pendingVideoFrameInfoList.get(0).frameId >= timestamp) {
                            break;
                        } else {
                            this.m_pendingVideoFrameInfoList.remove(0);
                        }
                    }
                } else if (!this.m_pendingVideoFrameInfoList.isEmpty()) {
                    videoFrameInfo = this.m_pendingVideoFrameInfoList.get(this.m_pendingVideoFrameInfoList.size() - 1);
                    this.m_pendingVideoFrameInfoList.clear();
                }
            }
            if (videoFrameInfo != null) {
                this.m_videoFrameCallback.onVideoFrameRendered(videoFrameInfo);
            }
        }
    }

    private void init() {
        setSurfaceTextureListener(this);
        if (!isInEditMode() && this.m_internalObject == 0) {
            nativeInit(true);
        }
    }

    private void destroyCurrentSurface() {
        if (!isInEditMode() && this.m_surface != null) {
            nativeSurfaceDestroyed(this.m_internalObject);
            this.m_surface.release();
            this.m_surface = null;
        }
    }
}
