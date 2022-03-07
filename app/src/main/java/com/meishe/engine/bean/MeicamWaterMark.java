package com.meishe.engine.bean;

import android.graphics.PointF;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsLiveWindowExt;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoResolution;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MeicamWaterMark implements Cloneable, Serializable {
    private transient List<PointF> list = new ArrayList();
    @SerializedName("watermarkH")
    private int mWatermarkH;
    @SerializedName("watermarkPath")
    private String mWatermarkPath;
    @SerializedName("watermarkW")
    private int mWatermarkW;
    @SerializedName("watermarkX")
    private int mWatermarkX;
    @SerializedName("watermarkY")
    private int mWatermarkY;

    public MeicamWaterMark(String str, List<PointF> list2) {
        this.mWatermarkPath = str;
        this.list = list2;
    }

    public String getWatermarkPath() {
        return this.mWatermarkPath;
    }

    public void setWatermarkPath(String str) {
        this.mWatermarkPath = str;
    }

    public int getWatermarkX() {
        return this.mWatermarkX;
    }

    public void setWatermarkX(int i) {
        this.mWatermarkX = i;
    }

    public int getWatermarkY() {
        return this.mWatermarkY;
    }

    public void setWatermarkY(int i) {
        this.mWatermarkY = i;
    }

    public int getWatermarkW() {
        return this.mWatermarkW;
    }

    public void setWatermarkW(int i) {
        this.mWatermarkW = i;
    }

    public int getWatermarkH() {
        return this.mWatermarkH;
    }

    public void setWatermarkH(int i) {
        this.mWatermarkH = i;
    }

    public List<PointF> getList() {
        return this.list;
    }

    public void setList(List<PointF> list2) {
        this.list = list2;
    }

    public void setPointList(NvsLiveWindowExt nvsLiveWindowExt, NvsTimeline nvsTimeline) {
        List<PointF> list2 = this.list;
        if (list2 == null) {
            this.list = new ArrayList();
        } else {
            list2.clear();
        }
        NvsVideoResolution videoRes = nvsTimeline.getVideoRes();
        int i = videoRes.imageWidth;
        int i2 = videoRes.imageHeight;
        int i3 = this.mWatermarkX - (i / 2);
        int i4 = (i2 / 2) - this.mWatermarkY;
        PointF pointF = new PointF((float) i3, (float) i4);
        PointF pointF2 = new PointF((float) (i3 + this.mWatermarkW), (float) (i4 - this.mWatermarkH));
        PointF pointF3 = new PointF(pointF.x, pointF2.y);
        PointF pointF4 = new PointF(pointF2.x, pointF.y);
        this.list.add(nvsLiveWindowExt.mapCanonicalToView(pointF));
        this.list.add(nvsLiveWindowExt.mapCanonicalToView(pointF3));
        this.list.add(nvsLiveWindowExt.mapCanonicalToView(pointF2));
        this.list.add(nvsLiveWindowExt.mapCanonicalToView(pointF4));
    }
}
