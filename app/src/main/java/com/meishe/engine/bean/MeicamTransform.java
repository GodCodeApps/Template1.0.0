package com.meishe.engine.bean;

import java.io.Serializable;

public class MeicamTransform implements Serializable, Cloneable {
    private float rotationZ;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float transformX;
    private float transformY;

    public float getTransformX() {
        return this.transformX;
    }

    public void setTransformX(float f) {
        this.transformX = f;
    }

    public float getTransformY() {
        return this.transformY;
    }

    public void setTransformY(float f) {
        this.transformY = f;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(float f) {
        this.scaleX = f;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(float f) {
        this.scaleY = f;
    }

    public float getRotationZ() {
        return this.rotationZ;
    }

    public void setRotationZ(float f) {
        this.rotationZ = f;
    }
}
