package com.meishe.player.common.utils.asset;

public class NvAsset {
    public static final int ASSET_ANIMATED_STICKER = 4;
    public static final int ASSET_ARSCENE_FACE = 15;
    public static final int ASSET_CAPTION_STYLE = 3;
    public static final int ASSET_CAPTURE_SCENE = 8;
    public static final int ASSET_COMPOUND_CAPTION = 16;
    public static final int ASSET_CUSTOM_ANIMATED_STICKER = 12;
    public static final int ASSET_FACE1_STICKER = 11;
    public static final int ASSET_FACE_BUNDLE_STICKER = 14;
    public static final int ASSET_FACE_STICKER = 10;
    public static final int ASSET_FILTER = 2;
    public static final int ASSET_FONT = 6;
    public static final int ASSET_PARTICLE = 9;
    public static final int ASSET_SUPER_ZOOM = 13;
    public static final int ASSET_THEME = 1;
    public static final int ASSET_VIDEO_TRANSITION = 5;
    public static final int ASSET_VIDEO_TRANSITION_3D = 26;
    public static final int ASSET_VIDEO_TRANSITION_COMMON = 25;
    public static final int ASSET_VIDEO_TRANSITION_EFFECT = 27;
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
    public static final int NV_CATEGORY_ID_ALL = 0;
    public static final int NV_CATEGORY_ID_CUSTOM = 20000;
    public static final int NV_CATEGORY_ID_DOUYINFILTER = 7;
    public static final int NV_CATEGORY_ID_PARTICLE_TOUCH_TYPE = 2;
    public static final int[] RatioArray = {1, 2, 4, 16, 8, 31};
    public static final String[] RatioStringArray = {"16:9", "1:1", "9:16", "3:4", "4:3", "通用"};
    public int aspectRatio = 0;
    public String assetDescription = "";
    public int assetType = 0;
    public String bundledLocalDirPath = "";
    public int categoryId = 1;
    public String coverUrl = "";
    public String desc = "";
    public int downloadProgress = 0;
    public int downloadStatus = 0;
    public String fxFileName;
    public boolean isReserved = false;
    public String localDirPath = "";
    public String minAppVersion = "";
    public String name = "";
    public int remotePackageSize = 0;
    public String remotePackageUrl = "";
    public int remoteVersion = 1;
    public String tags = "";
    public String uuid = "";
    public int version = 1;

    public boolean isReserved() {
        return this.isReserved;
    }

    public boolean isUsable() {
        return !this.localDirPath.isEmpty() || !this.bundledLocalDirPath.isEmpty();
    }

    public boolean hasRemoteAsset() {
        return !this.remotePackageUrl.isEmpty();
    }

    public boolean hasUpdate() {
        if (!isUsable() || !hasRemoteAsset() || this.remoteVersion <= this.version) {
            return false;
        }
        return true;
    }

    public boolean isInstalling() {
        return this.downloadStatus == 3;
    }

    public boolean isInstallingFailed() {
        return this.downloadStatus == 6;
    }

    public boolean isInstallingFinished() {
        return this.downloadStatus == 4;
    }

    public int getPackageType() {
        int i = this.assetType;
        if (i == 1) {
            return 4;
        }
        if (i == 2) {
            return 0;
        }
        if (i == 3) {
            return 2;
        }
        if (i == 4) {
            return 3;
        }
        if (i == 5) {
            return 1;
        }
        if (i == 8) {
            return 5;
        }
        if (i == 9) {
            return 0;
        }
        if (i == 10) {
            return 5;
        }
        if (i == 12) {
            return 3;
        }
        if (i == 15) {
            return 6;
        }
        return i == 16 ? 7 : 4;
    }

    public void copyAsset(NvAsset nvAsset) {
        this.uuid = nvAsset.uuid;
        this.categoryId = nvAsset.categoryId;
        this.version = nvAsset.version;
        this.aspectRatio = nvAsset.aspectRatio;
        this.name = nvAsset.name;
        this.coverUrl = nvAsset.coverUrl;
        this.desc = nvAsset.desc;
        this.tags = nvAsset.tags;
        this.minAppVersion = nvAsset.minAppVersion;
        this.localDirPath = nvAsset.localDirPath;
        this.bundledLocalDirPath = nvAsset.bundledLocalDirPath;
        this.isReserved = nvAsset.isReserved;
        this.remotePackageUrl = nvAsset.remotePackageUrl;
        this.remoteVersion = nvAsset.remoteVersion;
        this.downloadProgress = nvAsset.downloadProgress;
        this.remotePackageSize = nvAsset.remotePackageSize;
        this.downloadStatus = nvAsset.downloadStatus;
        this.assetType = nvAsset.assetType;
        this.assetDescription = nvAsset.assetDescription;
    }
}
