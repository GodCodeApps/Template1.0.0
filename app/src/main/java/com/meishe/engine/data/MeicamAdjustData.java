package com.meishe.engine.data;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsObject;
import java.io.Serializable;

public class MeicamAdjustData implements IDraft, Cloneable, Serializable {
    public static final String[] JSON_KEY = {"brightness", "contrast", "saturation", "highlight", "shadow", "blackPoint", "amount", "temperature", "tint"};
    @SerializedName("amount")
    private float mAmount;
    @SerializedName("blackPoint")
    private float mBlackPoint;
    @SerializedName("brightness")
    private float mBrightness;
    @SerializedName("contrast")
    private float mContrast;
    @SerializedName("degree")
    private float mDegree;
    @SerializedName("highlight")
    private float mHighlight;
    @SerializedName("saturation")
    private float mSaturation;
    @SerializedName("shadow")
    private float mShadow;
    @SerializedName("temperature")
    private float mTemperature;
    @SerializedName("tint")
    private float mTint;

    @Override // com.meishe.engine.data.IDraft
    public Object fromJson(String str) {
        return null;
    }

    @Override // com.meishe.engine.data.IDraft
    public String getMapKey(String str) {
        return null;
    }

    @Override // com.meishe.engine.data.IDraft
    public NvsObject getNvsObject() {
        return null;
    }

    @Override // com.meishe.engine.data.IDraft
    public void loadData(NvsObject nvsObject) {
    }

    @Override // com.meishe.engine.data.IDraft
    public void setNvsObject(NvsObject nvsObject) {
    }

    @Override // com.meishe.engine.data.IDraft
    public String toJson() {
        return new GsonBuilder().create().toJson(this);
    }

    public float getBrightness() {
        return this.mBrightness;
    }

    public void setBrightness(float f) {
        this.mBrightness = f;
    }

    public float getContrast() {
        return this.mContrast;
    }

    public void setContrast(float f) {
        this.mContrast = f;
    }

    public float getSaturation() {
        return this.mSaturation;
    }

    public void setSaturation(float f) {
        this.mSaturation = f;
    }

    public float getHighlight() {
        return this.mHighlight;
    }

    public void setHighlight(float f) {
        this.mHighlight = f;
    }

    public float getShadow() {
        return this.mShadow;
    }

    public void setShadow(float f) {
        this.mShadow = f;
    }

    public float getBlackPoint() {
        return this.mBlackPoint;
    }

    public void setBlackPoint(float f) {
        this.mBlackPoint = f;
    }

    public float getDegree() {
        return this.mDegree;
    }

    public void setDegree(float f) {
        this.mDegree = f;
    }

    public float getAmount() {
        return this.mAmount;
    }

    public void setAmount(float f) {
        this.mAmount = f;
    }

    public float getTemperature() {
        return this.mTemperature;
    }

    public void setTemperature(float f) {
        this.mTemperature = f;
    }

    public float getTint() {
        return this.mTint;
    }

    public void setTint(float f) {
        this.mTint = f;
    }

    public void reset() {
        this.mBrightness = 0.0f;
        this.mContrast = 0.0f;
        this.mSaturation = 0.0f;
        this.mHighlight = 0.0f;
        this.mShadow = 0.0f;
        this.mBlackPoint = 0.0f;
        this.mDegree = 0.0f;
        this.mAmount = 0.0f;
        this.mTemperature = 0.0f;
        this.mTint = 0.0f;
    }

    public void setAdjustData(MeicamAdjustData meicamAdjustData) {
        if (meicamAdjustData != null) {
            this.mBrightness = meicamAdjustData.getBrightness();
            this.mContrast = meicamAdjustData.getContrast();
            this.mSaturation = meicamAdjustData.getSaturation();
            this.mHighlight = meicamAdjustData.getHighlight();
            this.mShadow = meicamAdjustData.getShadow();
            this.mBlackPoint = meicamAdjustData.getBlackPoint();
            this.mDegree = meicamAdjustData.getDegree();
            this.mAmount = meicamAdjustData.getAmount();
            this.mTemperature = meicamAdjustData.getTemperature();
            this.mTint = meicamAdjustData.getTint();
        }
    }
}
