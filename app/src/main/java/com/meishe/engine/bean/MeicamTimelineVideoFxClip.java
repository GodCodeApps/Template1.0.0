package com.meishe.engine.bean;

import android.graphics.PointF;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsLiveWindowExt;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meishe.engine.util.DeepCopyUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MeicamTimelineVideoFxClip extends ClipInfo<NvsTimelineVideoFx> implements Cloneable, Serializable {
    private int clipSubType;
    private int clipType;
    private String desc;
    private String displayName;
    private String displayNamezhCN;
    private float intensity;
    private boolean isIgnoreBackground = false;
    private boolean isInverseRegion = false;
    private boolean isRegional = false;
    private transient List<PointF> list;
    @SerializedName("fxParams")
    private List<MeicamFxParam> mMeicamFxParamList = new ArrayList();
    private int regionalFeatherWidth = 0;

    public MeicamTimelineVideoFxClip() {
        super(CommonData.CLIP_TIMELINE_FX);
    }

    public MeicamTimelineVideoFxClip(int i, String str) {
        super(CommonData.CLIP_TIMELINE_FX);
        this.clipType = i;
        this.desc = str;
    }

    public List<PointF> getList() {
        return this.list;
    }

    public void setList(List<PointF> list2) {
        this.list = list2;
    }

    public int getClipType() {
        return this.clipType;
    }

    public void setClipType(int i) {
        this.clipType = i;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public int getClipSubType() {
        return this.clipSubType;
    }

    public void setClipSubType(int i) {
        this.clipSubType = i;
    }

    public float getIntensity() {
        return this.intensity;
    }

    public void setIntensity(float f) {
        this.intensity = f;
    }

    public boolean isRegional() {
        return this.isRegional;
    }

    public void setRegional(boolean z) {
        this.isRegional = z;
    }

    public boolean isIgnoreBackground() {
        return this.isIgnoreBackground;
    }

    public void setIgnoreBackground(boolean z) {
        this.isIgnoreBackground = z;
    }

    public boolean isInverseRegion() {
        return this.isInverseRegion;
    }

    public void setInverseRegion(boolean z) {
        this.isInverseRegion = z;
    }

    public int getRegionalFeatherWidth() {
        return this.regionalFeatherWidth;
    }

    public void setRegionalFeatherWidth(int i) {
        this.regionalFeatherWidth = i;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public String getDisplayNamezhCN() {
        return this.displayNamezhCN;
    }

    public void setDisplayNamezhCN(String str) {
        this.displayNamezhCN = str;
    }

    public List<MeicamFxParam> getMeicamFxParamList() {
        return this.mMeicamFxParamList;
    }

    public void setMeicamFxParamList(List<MeicamFxParam> list2) {
        this.mMeicamFxParamList = list2;
    }

    public NvsTimelineVideoFx bindToTimeline(NvsTimeline nvsTimeline) {
        NvsTimelineVideoFx nvsTimelineVideoFx;
        if (nvsTimeline == null) {
            return null;
        }
        if (this.clipType == 0) {
            nvsTimelineVideoFx = nvsTimeline.addBuiltinTimelineVideoFx(getInPoint(), getOutPoint() - getInPoint(), this.desc);
        } else {
            nvsTimelineVideoFx = nvsTimeline.addPackagedTimelineVideoFx(getInPoint(), getOutPoint() - getInPoint(), this.desc);
        }
        setObject(nvsTimelineVideoFx);
        return nvsTimelineVideoFx;
    }

    public void loadData(NvsTimelineVideoFx nvsTimelineVideoFx) {
        if (nvsTimelineVideoFx != null) {
            setObject(nvsTimelineVideoFx);
            setInPoint(nvsTimelineVideoFx.getInPoint());
            setOutPoint(nvsTimelineVideoFx.getOutPoint());
            if (this.clipType == 0) {
                setDesc(nvsTimelineVideoFx.getBuiltinTimelineVideoFxName());
            } else {
                setDesc(nvsTimelineVideoFx.getTimelineVideoFxPackageId());
            }
            setClipType(nvsTimelineVideoFx.getTimelineVideoFxType());
        }
    }

    @Override // java.lang.Object
    @NonNull
    public MeicamTimelineVideoFxClip clone() {
        return (MeicamTimelineVideoFxClip) DeepCopyUtil.deepClone(this);
    }

    public void setPointList(NvsLiveWindowExt nvsLiveWindowExt) {
        float[] fArr = new float[8];
        int i = 0;
        while (true) {
            if (i >= this.mMeicamFxParamList.size()) {
                break;
            }
            MeicamFxParam meicamFxParam = this.mMeicamFxParamList.get(i);
            if (!meicamFxParam.type.equals("float[]") || !meicamFxParam.key.equals("Unit Size")) {
                i++;
            } else {
                Object value = meicamFxParam.getValue();
                if (value instanceof ArrayList) {
                    ArrayList arrayList = (ArrayList) value;
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        fArr[i2] = Float.parseFloat(((Double) arrayList.get(i2)).toString());
                    }
                } else {
                    fArr = (float[]) value;
                }
            }
        }
        List<PointF> list2 = this.list;
        if (list2 == null) {
            this.list = new ArrayList();
        } else {
            list2.clear();
        }
        PointF pointF = new PointF(fArr[0], fArr[1]);
        PointF pointF2 = new PointF(fArr[2], fArr[3]);
        PointF pointF3 = new PointF(fArr[4], fArr[5]);
        PointF pointF4 = new PointF(fArr[6], fArr[7]);
        this.list.add(nvsLiveWindowExt.mapNormalizedToView(pointF));
        this.list.add(nvsLiveWindowExt.mapNormalizedToView(pointF2));
        this.list.add(nvsLiveWindowExt.mapNormalizedToView(pointF3));
        this.list.add(nvsLiveWindowExt.mapNormalizedToView(pointF4));
    }
}
