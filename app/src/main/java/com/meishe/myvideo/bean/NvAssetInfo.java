package com.meishe.myvideo.bean;

public class NvAssetInfo extends BaseInfo {
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
    public String mFilePath;
    public String minAppVersion = "";
    public int remotePackageSize = 0;
    public String remotePackageUrl = "";
    public int remoteVersion = 1;
    public String tags = "";
    public String uuid = "";
    public int version = 1;

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getCoverUrl() {
        return this.coverUrl;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getName() {
        return this.mName;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getFilePath() {
        return this.mFilePath;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public boolean isReserved() {
        return this.isReserved;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public boolean isUsable() {
        return !this.localDirPath.isEmpty() || !this.bundledLocalDirPath.isEmpty();
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public boolean hasRemoteAsset() {
        return !this.remotePackageUrl.isEmpty();
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public boolean hasUpdate() {
        if (!isUsable() || !hasRemoteAsset() || this.remoteVersion <= this.version) {
            return false;
        }
        return true;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public boolean isInstalling() {
        return this.downloadStatus == 3;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public boolean isInstallingFailed() {
        return this.downloadStatus == 6;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public boolean isInstallingFinished() {
        return this.downloadStatus == 4;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public String getUuid() {
        return this.uuid;
    }

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public int getPackageType() {
        int i = this.assetType;
        if (i == 1) {
            return 4;
        }
        if (i == 2 || i == 18 || i == 19 || i == 20 || i == 21) {
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

    @Override // com.meishe.myvideo.bean.IBaseInfo, com.meishe.myvideo.bean.BaseInfo
    public void copyAsset(NvAssetInfo nvAssetInfo) {
        this.uuid = nvAssetInfo.uuid;
        this.categoryId = nvAssetInfo.categoryId;
        this.version = nvAssetInfo.version;
        this.aspectRatio = nvAssetInfo.aspectRatio;
        this.mName = nvAssetInfo.mName;
        this.coverUrl = nvAssetInfo.coverUrl;
        this.desc = nvAssetInfo.desc;
        this.tags = nvAssetInfo.tags;
        this.minAppVersion = nvAssetInfo.minAppVersion;
        this.localDirPath = nvAssetInfo.localDirPath;
        this.bundledLocalDirPath = nvAssetInfo.bundledLocalDirPath;
        this.isReserved = nvAssetInfo.isReserved;
        this.remotePackageUrl = nvAssetInfo.remotePackageUrl;
        this.remoteVersion = nvAssetInfo.remoteVersion;
        this.downloadProgress = nvAssetInfo.downloadProgress;
        this.remotePackageSize = nvAssetInfo.remotePackageSize;
        this.downloadStatus = nvAssetInfo.downloadStatus;
        this.assetType = nvAssetInfo.assetType;
        this.assetDescription = nvAssetInfo.assetDescription;
    }
}
