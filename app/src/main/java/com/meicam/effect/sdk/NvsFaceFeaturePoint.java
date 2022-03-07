package com.meicam.effect.sdk;

import com.meicam.sdk.NvsArbitraryData;
import com.meicam.sdk.NvsPosition2D;

public class NvsFaceFeaturePoint extends NvsArbitraryData {
    public int faceCount;
    public FaceInfo[] faces;

    /* access modifiers changed from: package-private */
    public FaceInfo getFaceFeatureFromIndex(int i) {
        if (i < 0 || i > this.faceCount) {
            return null;
        }
        return this.faces[i];
    }

    public static NvsFaceFeaturePoint createFaceFeaturePoint(int i) {
        NvsFaceFeaturePoint nvsFaceFeaturePoint = new NvsFaceFeaturePoint();
        nvsFaceFeaturePoint.faceCount = i;
        nvsFaceFeaturePoint.faces = new FaceInfo[nvsFaceFeaturePoint.faceCount];
        for (int i2 = 0; i2 < i; i2++) {
            nvsFaceFeaturePoint.faces[i2] = new FaceInfo();
        }
        return nvsFaceFeaturePoint;
    }

    public static class FaceInfo {
        int ID;
        NvsPosition2D[] points_array = new NvsPosition2D[106];
        float[] visibility_array = new float[106];

        public NvsPosition2D[] getPoints_array() {
            return this.points_array;
        }

        public float[] getVisibilityArray() {
            return this.visibility_array;
        }

        public void setPoints_array(NvsPosition2D[] nvsPosition2DArr) {
            this.points_array = nvsPosition2DArr;
        }

        public int getID() {
            return this.ID;
        }

        public void setID(int i) {
            this.ID = i;
        }
    }
}
