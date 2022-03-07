package com.meishe.myvideo.bean;

public interface IBaseInfo {
    void copyAsset(NvAssetInfo nvAssetInfo);

    String getBeautyShapeId();

    String getColorValue();

    String getCompoundPath();

    String getCoverUrl();

    String getFilePath();

    String getName();

    int getPackageType();

    String getPicPath();

    int getPicType();

    String getUuid();

    String getWaterMarkPath();

    boolean hasRemoteAsset();

    boolean hasUpdate();

    boolean isInstalling();

    boolean isInstallingFailed();

    boolean isInstallingFinished();

    boolean isReserved();

    boolean isSelected();

    boolean isUsable();

    void setColorValue(String str);

    void setSelected(boolean z);
}
