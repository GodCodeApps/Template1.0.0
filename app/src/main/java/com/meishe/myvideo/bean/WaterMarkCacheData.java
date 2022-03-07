package com.meishe.myvideo.bean;

import java.util.ArrayList;
import java.util.List;

public class WaterMarkCacheData {
    private List<WaterMarkInfo> picturePathName;

    public WaterMarkCacheData(List<WaterMarkInfo> list) {
        this.picturePathName = list;
    }

    public List<WaterMarkInfo> getPicturePathName() {
        List<WaterMarkInfo> list = this.picturePathName;
        return list == null ? new ArrayList() : list;
    }

    public void setPicturePathName(List<WaterMarkInfo> list) {
        this.picturePathName = list;
    }
}
