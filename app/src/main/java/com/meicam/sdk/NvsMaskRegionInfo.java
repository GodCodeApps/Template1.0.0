package com.meicam.sdk;

import java.util.ArrayList;
import java.util.List;

public class NvsMaskRegionInfo extends NvsArbitraryData {
    public static final int MASK_REGION_TYPE_CUBIC_CURVE = 1;
    public static final int MASK_REGION_TYPE_ELLIPSE2D = 2;
    public static final int MASK_REGION_TYPE_POLYGON = 0;
    private List<RegionInfo> regionInfoArray = new ArrayList();

    public List<RegionInfo> getRegionInfoArray() {
        return this.regionInfoArray;
    }

    public void addRegionInfo(RegionInfo regionInfo) {
        this.regionInfoArray.add(regionInfo);
    }

    public void removeRegionInfoByIndex(int i) {
        if (i >= 0 && i < this.regionInfoArray.size()) {
            this.regionInfoArray.remove(i);
        }
    }

    public static class Transform2D {
        private NvsPosition2D anchor;
        private float rotation;
        private NvsPosition2D scale;
        private NvsPosition2D translation;

        public Transform2D() {
            this.anchor = new NvsPosition2D(0.0f, 0.0f);
            this.scale = new NvsPosition2D(1.0f, 1.0f);
            this.rotation = 0.0f;
            this.translation = new NvsPosition2D(0.0f, 0.0f);
        }

        public Transform2D(NvsPosition2D nvsPosition2D, NvsPosition2D nvsPosition2D2, float f, NvsPosition2D nvsPosition2D3) {
            this.anchor = nvsPosition2D;
            this.scale = nvsPosition2D2;
            this.rotation = f;
            this.translation = nvsPosition2D3;
        }

        public NvsPosition2D getAnchor() {
            return this.anchor;
        }

        public NvsPosition2D getScale() {
            return this.scale;
        }

        public float getRotation() {
            return this.rotation;
        }

        public NvsPosition2D getTranslation() {
            return this.translation;
        }

        public void setAnchor(NvsPosition2D nvsPosition2D) {
            this.anchor = nvsPosition2D;
        }

        public void setRotation(float f) {
            this.rotation = f;
        }

        public void setScale(NvsPosition2D nvsPosition2D) {
            this.scale = nvsPosition2D;
        }

        public void setTranslation(NvsPosition2D nvsPosition2D) {
            this.translation = nvsPosition2D;
        }
    }

    public static class Ellipse2D {
        private float a;
        private float b;
        private NvsPosition2D center;
        private float theta;

        public Ellipse2D() {
            this.center = new NvsPosition2D(0.0f, 0.0f);
            this.theta = 0.0f;
            this.b = 0.0f;
            this.a = 0.0f;
        }

        public Ellipse2D(NvsPosition2D nvsPosition2D, float f, float f2, float f3) {
            this.center = nvsPosition2D;
            this.a = f;
            this.b = f2;
            this.theta = f3;
        }

        public NvsPosition2D getCenter() {
            return this.center;
        }

        public float getA() {
            return this.a;
        }

        public float getB() {
            return this.b;
        }

        public float getTheta() {
            return this.theta;
        }

        public void setCenter(NvsPosition2D nvsPosition2D) {
            this.center = nvsPosition2D;
        }

        public void setA(float f) {
            this.a = f;
        }

        public void setB(float f) {
            this.b = f;
        }

        public void setTheta(float f) {
            this.theta = f;
        }
    }

    public static class RegionInfo {
        private Ellipse2D ellipse2d = new Ellipse2D();
        private List<NvsPosition2D> points = new ArrayList();
        private Transform2D transform2d = new Transform2D();
        private int type;

        public RegionInfo(int i) {
            this.type = i;
        }

        public List<NvsPosition2D> getPoints() {
            return this.points;
        }

        public Ellipse2D getEllipse2D() {
            return this.ellipse2d;
        }

        public Transform2D getTransform2D() {
            return this.transform2d;
        }

        public int getType() {
            return this.type;
        }

        public void setPoints(List<NvsPosition2D> list) {
            this.points = list;
        }

        public void setEllipse2D(Ellipse2D ellipse2D) {
            this.ellipse2d = ellipse2D;
        }

        public void setTransform2D(Transform2D transform2D) {
            this.transform2d = transform2D;
        }
    }
}
