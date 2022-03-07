package com.meishe.engine.bean;

import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoTrack;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MeicamVideoTrack extends TrackInfo<NvsVideoTrack> implements Cloneable, Serializable {
    @SerializedName("isMute")
    private boolean mIsMute = false;
    @SerializedName("transitions")
    private List<MeicamTransition> mTransitionInfoList = new ArrayList();

    public MeicamVideoTrack(int i) {
        super(CommonData.TRACK_VIDEO, i);
    }

    public List<MeicamTransition> getTransitionInfoList() {
        return this.mTransitionInfoList;
    }

    public void setTransitionInfoList(List<MeicamTransition> list) {
        this.mTransitionInfoList = list;
    }

    public boolean isMute() {
        return this.mIsMute;
    }

    public void setIsMute(boolean z) {
        NvsVideoTrack nvsVideoTrack = (NvsVideoTrack) getObject();
        if (nvsVideoTrack != null) {
            if (z) {
                nvsVideoTrack.setVolumeGain(0.0f, 0.0f);
            } else {
                nvsVideoTrack.setVolumeGain(1.0f, 1.0f);
            }
        }
        this.mIsMute = z;
    }

    public NvsVideoTrack bindToTimeline(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            return null;
        }
        NvsVideoTrack appendVideoTrack = nvsTimeline.appendVideoTrack();
        if (isMute()) {
            appendVideoTrack.setVolumeGain(0.0f, 0.0f);
        } else {
            appendVideoTrack.setVolumeGain(1.0f, 1.0f);
        }
        setObject(appendVideoTrack);
        return appendVideoTrack;
    }
}
