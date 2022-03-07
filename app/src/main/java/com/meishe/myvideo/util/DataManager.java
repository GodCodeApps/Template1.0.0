package com.meishe.myvideo.util;

import com.meishe.engine.data.MeicamClipInfo;
import java.util.ArrayList;

public class DataManager {
    private static final DataManager ourInstance = new DataManager();
    private ArrayList<MeicamClipInfo> clipInfoList;

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }

    public ArrayList<MeicamClipInfo> getClipInfoList() {
        return this.clipInfoList;
    }

    public void setClipInfoList(ArrayList<MeicamClipInfo> arrayList) {
        this.clipInfoList = arrayList;
    }
}
