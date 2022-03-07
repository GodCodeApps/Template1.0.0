package com.meishe.engine.data;

import com.meishe.engine.util.StoryboardUtil;
import java.util.HashMap;
import java.util.Map;

public class CutData {
    private int mRatio;
    private float mRatioValue;
    private Map<String, Float> mTransformData = new HashMap();

    public CutData() {
        Map<String, Float> map = this.mTransformData;
        Float valueOf = Float.valueOf(1.0f);
        map.put(StoryboardUtil.STORYBOARD_KEY_SCALE_X, valueOf);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_SCALE_Y, valueOf);
        Map<String, Float> map2 = this.mTransformData;
        Float valueOf2 = Float.valueOf(0.0f);
        map2.put(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z, valueOf2);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_X, valueOf2);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_Y, valueOf2);
    }

    public Map<String, Float> getTransformData() {
        return this.mTransformData;
    }

    public void putTransformData(String str, float f) {
        this.mTransformData.put(str, Float.valueOf(f));
    }

    public float getTransformData(String str) {
        return this.mTransformData.get(str).floatValue();
    }

    public int getRatio() {
        return this.mRatio;
    }

    public void setRatio(int i) {
        this.mRatio = i;
    }

    public float getRatioValue() {
        return this.mRatioValue;
    }

    public void setRatioValue(float f) {
        this.mRatioValue = f;
    }

    public String toString() {
        return "CutData{mTransformData=" + this.mTransformData + ", mRatio=" + this.mRatio + '}';
    }
}
