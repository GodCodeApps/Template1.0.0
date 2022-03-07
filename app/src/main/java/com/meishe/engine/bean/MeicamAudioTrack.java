package com.meishe.engine.bean;

import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsTimeline;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MeicamAudioTrack extends TrackInfo<NvsAudioTrack> implements Cloneable, Serializable {
    @SerializedName("transitions")
    private List<MeicamTransition> mTransitionInfoList = new ArrayList();

    public MeicamAudioTrack(int i) {
        super(CommonData.TRACK_AUDIO, i);
    }

    public List<MeicamTransition> getTransitionInfoList() {
        return this.mTransitionInfoList;
    }

    public void setTransitionInfoList(List<MeicamTransition> list) {
        this.mTransitionInfoList = list;
    }

    public NvsAudioTrack bindToTimeline(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            return null;
        }
        NvsAudioTrack appendAudioTrack = nvsTimeline.appendAudioTrack();
        setObject(appendAudioTrack);
        return appendAudioTrack;
    }
}
