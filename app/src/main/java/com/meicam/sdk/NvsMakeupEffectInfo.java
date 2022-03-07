package com.meicam.sdk;

import java.util.ArrayList;
import java.util.List;

public class NvsMakeupEffectInfo extends NvsArbitraryData {
    public static final int MAKEUP_EFFECT_BLENDING_MODE_ADD = 4;
    public static final int MAKEUP_EFFECT_BLENDING_MODE_EXCLUSION = 5;
    public static final int MAKEUP_EFFECT_BLENDING_MODE_MULTIPLY = 1;
    public static final int MAKEUP_EFFECT_BLENDING_MODE_NORMAL = 0;
    public static final int MAKEUP_EFFECT_BLENDING_MODE_SCREEN = 3;
    public static final int MAKEUP_EFFECT_BLENDING_MODE_SUBTRACT = 2;
    public static final int MAKEUP_EFFECT_BLENDING_MODE_UNKNOWN = -1;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_ALL = 255;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_BLUSHER = 32;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_BRIGHTEN = 128;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_EYEBROW = 2;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_EYELASH = 8;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_EYELINER = 16;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_EYESHADOW = 4;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_LIP = 1;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_NONE = 0;
    public static final int MAKEUP_EFFECT_CUSTOM_ENABLED_FLAG_SHADOW = 64;
    public static final int MAKEUP_EFFECT_LAYER_TYPE_3D = 0;
    public static final int MAKEUP_EFFECT_LAYER_TYPE_LUT = 1;
    private List<MakeupEffect> makeupEffectArray = new ArrayList();

    public List<MakeupEffect> getMakeupEffectArray() {
        return this.makeupEffectArray;
    }

    public void addMakeupEffect(MakeupEffect makeupEffect) {
        this.makeupEffectArray.add(makeupEffect);
    }

    public void removeMakeupEffectByIndex(int i) {
        if (i >= 0 && i < this.makeupEffectArray.size()) {
            this.makeupEffectArray.remove(i);
        }
    }

    public static class MakeupEffect {
        public NvsColor color;
        public String effectId;
        public float intensity;
        private List<MakeupEffectLayer> makeupEffectLayerArray = new ArrayList();

        public String getEffectId() {
            return this.effectId;
        }

        public NvsColor getColor() {
            return this.color;
        }

        public float getIntensity() {
            return this.intensity;
        }

        public List<MakeupEffectLayer> getMakeupEffectLayerArray() {
            return this.makeupEffectLayerArray;
        }

        public void addMakeupEffectLayer(MakeupEffectLayer makeupEffectLayer) {
            this.makeupEffectLayerArray.add(makeupEffectLayer);
        }

        public void removeMakeupEffectLayerByIndex(int i) {
            if (i >= 0 && i < this.makeupEffectLayerArray.size()) {
                this.makeupEffectLayerArray.remove(i);
            }
        }
    }

    public static class MakeupEffectLayer {
        public float intensity;
        public String layerId;
        public int type;

        public MakeupEffectLayer(int i) {
            this.type = i;
        }

        public String getLayerId() {
            return this.layerId;
        }

        public float getIntensity() {
            return this.intensity;
        }

        public int getType() {
            return this.type;
        }
    }

    public static class MakeupEffectLayer3D extends MakeupEffectLayer {
        public int blendingMode;
        public NvsColor texColor;
        public String texFilePath;

        public MakeupEffectLayer3D() {
            super(0);
        }

        public String getTexFilePath() {
            return this.texFilePath;
        }

        public NvsColor getTexColor() {
            return this.texColor;
        }

        public int getBlendingMode() {
            return this.blendingMode;
        }
    }

    public static class MakeupEffectLayerLut extends MakeupEffectLayer {
        public String lutFilePath;

        public MakeupEffectLayerLut() {
            super(1);
        }

        public String getLutFilePath() {
            return this.lutFilePath;
        }
    }
}
