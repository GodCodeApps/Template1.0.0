package com.meishe.myvideo.util.asset;

import com.meishe.engine.bean.CommonData;

public class NvAssetConstant {

    public enum NvAssetPath {
        CAPTION_STYLE(3, "captionstyle"),
        COMPOUND_CAPTION(16, "compoundcaption"),
        CUSTOM_STICKER(12, "customsticker"),
        EFFECT_FRAME(18, "effect_frame"),
        EFFECT_LIVELY(20, "effect_lively"),
        EFFECT_SHAKING(21, "effect_shaking"),
        FILTER(2, "filter"),
        FONT(6, "font"),
        STICKER(12, CommonData.CLIP_STICKER),
        THEME(1, "theme"),
        WATER_MARK(24, "water_mark"),
        EFFECT_DREAM(19, "effect_dream"),
        ASSET_VIDEO_TRANSITION_3D(25, "transition_3d"),
        ASSET_VIDEO_TRANSITION_EFFECT(26, "transition_efffect");
        
        public String path;
        public int type;

        private NvAssetPath(int i, String str) {
            this.path = str;
            this.type = i;
        }

        public static String getPath(int i) {
            NvAssetPath[] values = values();
            for (NvAssetPath nvAssetPath : values) {
                if (nvAssetPath.type == i) {
                    return nvAssetPath.path;
                }
            }
            return null;
        }
    }
}
