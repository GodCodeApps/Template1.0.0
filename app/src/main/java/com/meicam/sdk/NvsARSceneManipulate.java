package com.meicam.sdk;

import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;

public class NvsARSceneManipulate {
    NvsARSceneManipulateCallback m_callback = null;
    private long m_contextInterface;

    public interface NvsARSceneManipulateCallback {
        void notifyFaceBoundingRect(List<NvsFaceBoundingRectInfo> list);

        void notifyFaceFeatureInfos(List<NvsFaceFeatureInfo> list);
    }

    private native void nativeCleanup(long j);

    private native void nativeResetSkinColor(long j);

    private native void nativeResetTracking(long j);

    private native void nativeSetARSceneManipulateCallback(long j, NvsARSceneManipulateCallback nvsARSceneManipulateCallback);

    private native void nativeSetDetectionMode(long j, int i);

    public void setARSceneCallback(NvsARSceneManipulateCallback nvsARSceneManipulateCallback) {
        this.m_callback = nvsARSceneManipulateCallback;
        nativeSetARSceneManipulateCallback(this.m_contextInterface, nvsARSceneManipulateCallback);
    }

    public void setDetectionMode(int i) {
        nativeSetDetectionMode(this.m_contextInterface, i);
    }

    public void resetTracking() {
        nativeResetTracking(this.m_contextInterface);
    }

    public void resetSkinColor() {
        nativeResetSkinColor(this.m_contextInterface);
    }

    /* access modifiers changed from: protected */
    public void setContextInterface(long j) {
        this.m_contextInterface = j;
    }

    public void release() {
        this.m_callback = null;
        long j = this.m_contextInterface;
        if (j != 0) {
            nativeCleanup(j);
            this.m_contextInterface = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        release();
        super.finalize();
    }

    public static class NvsFaceBoundingRectInfo {
        public RectF faceBoundingRect;
        public int faceId;

        public NvsFaceBoundingRectInfo(int i, float f, float f2, float f3, float f4) {
            this.faceId = i;
            this.faceBoundingRect = new RectF(f, f2, f3, f4);
        }
    }

    public static class NvsFaceFeatureInfo {
        public RectF boundingBox;
        public int faceId;
        public List<NvsPosition2D> landmarks;
        public float pitch;
        public float roll;
        public List<Float> visibilities;
        public float yaw;

        public void setFaceId(int i) {
            this.faceId = i;
        }

        public void setBoundingBox(float f, float f2, float f3, float f4) {
            this.boundingBox = new RectF(f, f2, f3, f4);
        }

        public void setLandmarks(ArrayList<NvsPosition2D> arrayList) {
            this.landmarks = new ArrayList();
            this.landmarks = arrayList;
        }

        public void setVisibilities(ArrayList<Float> arrayList) {
            this.visibilities = new ArrayList();
            this.visibilities = arrayList;
        }

        public void setPitch(float f) {
            this.pitch = f;
        }

        public void setYaw(float f) {
            this.yaw = f;
        }

        public void setRoll(float f) {
            this.roll = f;
        }
    }
}
