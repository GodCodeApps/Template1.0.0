package com.meishe.myvideo.bean.down;

import java.util.ArrayList;

public class AssetListDownResponse extends AbstractResponse {
    public boolean hasNext;
    public ArrayList<NvAssetInfo> list;

    public class NvAssetInfo {
        public int category;
        public String coverUrl;
        public String desc;
        public String id;
        public String minAppVersion;
        public String name;
        public int packageSize;
        public String packageUrl;
        public int supportedAspectRatio;
        public String tags;
        public int version;

        public NvAssetInfo() {
        }
    }
}
