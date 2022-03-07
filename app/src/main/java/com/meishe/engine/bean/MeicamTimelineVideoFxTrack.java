package com.meishe.engine.bean;

import androidx.annotation.NonNull;
import com.meishe.engine.util.DeepCopyUtil;
import java.io.Serializable;

public class MeicamTimelineVideoFxTrack extends TrackInfo implements Cloneable, Serializable {
    public MeicamTimelineVideoFxTrack(int i) {
        super(CommonData.TRACK_TIMELINE_FX, i);
    }

    @Override // java.lang.Object
    @NonNull
    public MeicamTimelineVideoFxTrack clone() {
        return (MeicamTimelineVideoFxTrack) DeepCopyUtil.deepClone(this);
    }
}
