package com.meishe.myvideo.bean;

import com.meishe.engine.interf.IEffect;

public class BaseInfo implements IBaseInfo, IEffect {
    public static final int ASSET_ANIMATED_STICKER = 4;
    public static final int ASSET_ANIMATED_STICKER_CUSTOM = 22;
    public static final int ASSET_ARSCENE_FACE = 15;
    public static final int ASSET_BUILTIN = 3;
    public static final int ASSET_CAPTION_STYLE = 3;
    public static final int ASSET_CAPTURE_SCENE = 8;
    public static final int ASSET_COMPOUND_CAPTION = 16;
    public static final int ASSET_CUSTOM_ANIMATED_STICKER = 12;
    public static final int ASSET_EFFECT_DREAM = 19;
    public static final int ASSET_EFFECT_FRAME = 18;
    public static final int ASSET_EFFECT_LIVELY = 20;
    public static final int ASSET_EFFECT_SHAKING = 21;
    public static final int ASSET_FACE1_STICKER = 11;
    public static final int ASSET_FACE_BUNDLE_STICKER = 14;
    public static final int ASSET_FACE_STICKER = 10;
    public static final int ASSET_FILTER = 2;
    public static final int ASSET_FONT = 6;
    public static final int ASSET_LOCAL = 2;
    public static final int ASSET_NONE = 1;
    public static final int ASSET_PARTICLE = 9;
    public static final int ASSET_PHOTO_ALBUM = 17;
    public static final int ASSET_SUPER_ZOOM = 13;
    public static final int ASSET_THEME = 1;
    public static final int ASSET_VIDEO_TRANSITION = 5;
    public static final int ASSET_VIDEO_TRANSITION_3D = 25;
    public static final int ASSET_VIDEO_TRANSITION_EFFECT = 26;
    public static final int ASSET_WATER = 24;
    public static final int ASSET_WATER_EFFECT = 23;
    public static final int AspectRatio_16v9 = 1;
    public static final int AspectRatio_1v1 = 2;
    public static final int AspectRatio_3v4 = 16;
    public static final int AspectRatio_4v3 = 8;
    public static final int AspectRatio_9v16 = 4;
    public static final int AspectRatio_All = 31;
    public static final int AspectRatio_NoFitRatio = 0;
    public static final int DownloadStatusDecompressing = 3;
    public static final int DownloadStatusDecompressingFailed = 6;
    public static final int DownloadStatusFailed = 5;
    public static final int DownloadStatusFinished = 4;
    public static final int DownloadStatusInProgress = 2;
    public static final int DownloadStatusNone = 0;
    public static final int DownloadStatusPending = 1;
    public static int EFFECT_MODE_BUILTIN = 0;
    public static int EFFECT_MODE_BUNDLE = 1;
    public static int EFFECT_MODE_PACKAGE = 2;
    public static int MENU_INDEX_LEVEL_1 = 1;
    public static int MENU_INDEX_LEVEL_2 = 2;
    public static int MENU_INDEX_LEVEL_3 = 3;
    public static final int NV_CATEGORY_ID_ALL = 0;
    public static final int NV_CATEGORY_ID_CUSTOM = 20000;
    public static final int NV_CATEGORY_ID_DOUYINFILTER = 7;
    public static final int NV_CATEGORY_ID_PARTICLE_TOUCH_TYPE = 2;
    public static final int[] RatioArray = {1, 2, 4, 16, 8, 31};
    public static final String[] RatioStringArray = {"16:9", "1:1", "9:16", "3:4", "4:3", "通用"};
    public boolean checkAble;
    protected NvAssetInfo mAsset;
    public int mAssetMode;
    public int mEffectMode;
    public int mEffectType;
    public int mIconRcsId;
    public String mIconUrl;
    public int mMenuIndex;
    public String mName;
    public String mPackageId;
    public String mType;

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public void copyAsset(NvAssetInfo nvAssetInfo) {
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public String getBeautyShapeId() {
        return "";
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public String getColorValue() {
        return null;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public String getCompoundPath() {
        return null;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public String getCoverUrl() {
        return null;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public String getFilePath() {
        return null;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public int getPackageType() {
        return 0;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public String getPicPath() {
        return null;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public int getPicType() {
        return 0;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public String getUuid() {
        return null;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public String getWaterMarkPath() {
        return null;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public boolean hasRemoteAsset() {
        return false;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public boolean hasUpdate() {
        return false;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public boolean isInstalling() {
        return false;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public boolean isInstallingFailed() {
        return false;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public boolean isInstallingFinished() {
        return false;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public boolean isReserved() {
        return false;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public boolean isSelected() {
        return false;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public boolean isUsable() {
        return false;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public void setColorValue(String str) {
    }

    public void setFilePath(String str) {
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public void setSelected(boolean z) {
    }

    public BaseInfo() {
        this.checkAble = false;
        this.mMenuIndex = MENU_INDEX_LEVEL_2;
    }

    public BaseInfo(String str) {
        this.checkAble = false;
        this.mMenuIndex = MENU_INDEX_LEVEL_2;
        this.mName = str;
    }

    public BaseInfo(String str, int i) {
        this.checkAble = false;
        this.mMenuIndex = MENU_INDEX_LEVEL_2;
        this.mName = str;
        this.mIconRcsId = i;
    }

    public BaseInfo(String str, String str2, int i, int i2) {
        this(str, i);
        this.mIconUrl = str2;
        this.mEffectType = i2;
    }

    public BaseInfo(String str, String str2, int i, int i2, int i3, String str3) {
        this(str, str2, i, i2);
        this.mEffectMode = i3;
        this.mPackageId = str3;
    }

    public void setAsset(NvAssetInfo nvAssetInfo) {
        this.mAsset = nvAssetInfo;
    }

    public NvAssetInfo getAsset() {
        return this.mAsset;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo
    public String getName() {
        return this.mName;
    }

    @Override // com.meishe.engine.interf.IEffect
    public int getEffectMode() {
        return this.mEffectMode;
    }

    @Override // com.meishe.engine.interf.IEffect
    public String getPackageId() {
        return this.mPackageId;
    }
}
