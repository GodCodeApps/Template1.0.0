package com.meicam.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class NvsLiveWindow extends SurfaceView implements SurfaceHolder.Callback {
    public static final int FILLMODE_PRESERVEASPECTCROP = 0;
    public static final int FILLMODE_PRESERVEASPECTFIT = 1;
    public static final int FILLMODE_PRESERVEASPECTFIT_BLUR = 3;
    public static final int FILLMODE_STRETCH = 2;
    protected int m_fillMode = 0;
    protected long m_internalObject = 0;
    private Surface m_surface;
    private VideoFrameCallback m_videoFrameCallback = null;

    public interface InternalVideoFrameCallback {
        void onVideoFrameRendered(VideoFrameInfo videoFrameInfo);
    }

    public interface VideoFrameCallback {
        void onVideoFrameRendered(VideoFrameInfo videoFrameInfo);
    }

    public static class VideoFrameInfo {
        public float captionAnchorX = 0.0f;
        public float captionAnchorY = 0.0f;
        public float captionRotationZ = 0.0f;
        public float captionScaleX = 1.0f;
        public float captionScaleY = 1.0f;
        public float captionTransX = 0.0f;
        public float captionTransY = 0.0f;
        public long frameId;
        public long streamTime;
    }

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

    private native void nativeSetVideoFrameCallback(long j, InternalVideoFrameCallback internalVideoFrameCallback);

    private native void nativeSurfaceChanged(long j, Surface surface, int i, int i2);

    private native void nativeSurfaceDestroyed(long j);

    private native Bitmap nativeTakeScreenshot(long j);

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    public NvsLiveWindow(Context context) {
        super(context);
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public NvsLiveWindow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public NvsLiveWindow(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public NvsLiveWindow(Context context, AttributeSet attributeSet, int i, int i2) {
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

    public void setVideoFrameCallback(VideoFrameCallback videoFrameCallback) {
        NvsUtils.checkFunctionInMainThread();
        if (!isInEditMode()) {
            this.m_videoFrameCallback = videoFrameCallback;
            if (videoFrameCallback != null) {
                nativeSetVideoFrameCallback(this.m_internalObject, new InternalVideoFrameCallback() {
                    /* class com.meicam.sdk.NvsLiveWindow.AnonymousClass1 */

                    @Override // com.meicam.sdk.NvsLiveWindow.InternalVideoFrameCallback
                    public void onVideoFrameRendered(VideoFrameInfo videoFrameInfo) {
                        VideoFrameCallback videoFrameCallback = NvsLiveWindow.this.m_videoFrameCallback;
                        if (videoFrameCallback != null) {
                            videoFrameCallback.onVideoFrameRendered(videoFrameInfo);
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

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.m_videoFrameCallback = null;
        nativeSetVideoFrameCallback(this.m_internalObject, null);
        if (!isInEditMode()) {
            long j = this.m_internalObject;
            if (j != 0) {
                nativeClose(j);
                this.m_internalObject = 0;
                getHolder().removeCallback(this);
            }
        }
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (!isInEditMode()) {
            nativeOnSizeChanged(this.m_internalObject, i, i2);
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (!isInEditMode() && i2 >= 1 && i3 >= 1) {
            nativeSurfaceChanged(this.m_internalObject, surfaceHolder.getSurface(), i2, i3);
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (!isInEditMode()) {
            nativeSurfaceDestroyed(this.m_internalObject);
        }
    }

    private void init() {
        if (!isInEditMode() && this.m_internalObject == 0) {
            nativeInit(false);
            getHolder().addCallback(this);
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
