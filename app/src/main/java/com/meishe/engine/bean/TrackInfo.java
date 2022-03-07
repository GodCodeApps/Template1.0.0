package com.meishe.engine.bean;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class TrackInfo<T> extends NvsObject<T> implements Cloneable {
    private int index;
    @SerializedName("clipInfos")
    private List<ClipInfo> mClipInfoList = new ArrayList();
    private boolean show = true;
    private String type = "base";
    private float volume = 1.0f;

    public TrackInfo() {
        super(null);
    }

    public TrackInfo(String str, int i) {
        super(null);
        this.type = str;
        this.index = i;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public boolean isShow() {
        return this.show;
    }

    public void setShow(boolean z) {
        this.show = z;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float f) {
        this.volume = f;
    }

    public List<ClipInfo> getClipInfoList() {
        return this.mClipInfoList;
    }

    public void setClipInfoList(List<? extends ClipInfo> list) {
        List<ClipInfo> list2 = this.mClipInfoList;
        if (list2 != null) {
            list2.clear();
            this.mClipInfoList.addAll(list);
        }
    }

    public void addClipInfoList(List<? extends ClipInfo> list, int i) {
        List<ClipInfo> list2 = this.mClipInfoList;
        if (list2 != null) {
            list2.addAll(i, list);
        }
    }
}
