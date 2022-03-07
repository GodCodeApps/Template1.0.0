package com.meishe.engine.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.meicam.sdk.NvsVideoTrack;
import com.meicam.sdk.NvsVideoTransition;
import java.io.Serializable;

public class MeicamTransition extends NvsObject<NvsVideoTransition> implements Cloneable, Serializable {
    private String desc;
    private String displayName;
    private String displayNamezhCN;
    private long duration = 1000000;
    private String iconPath;
    private int iconResourceId;
    private int index;
    private int type;

    public MeicamTransition() {
        super(null);
    }

    public MeicamTransition(int i, int i2, String str) {
        super(null);
        this.index = i;
        this.type = i2;
        this.desc = str;
    }

    public MeicamTransition(int i, int i2, String str, String str2) {
        super(null);
        this.index = i;
        this.type = i2;
        this.desc = str;
        this.iconPath = str2;
    }

    public MeicamTransition(int i, int i2, String str, int i3) {
        super(null);
        this.index = i;
        this.type = i2;
        this.desc = str;
        this.iconResourceId = i3;
    }

    public NvsVideoTransition bindToTimeline(NvsVideoTrack nvsVideoTrack) {
        NvsVideoTransition nvsVideoTransition;
        if (this.type == 0) {
            nvsVideoTransition = nvsVideoTrack.setBuiltinTransition(this.index, this.desc);
        } else {
            nvsVideoTransition = nvsVideoTrack.setPackagedTransition(this.index, this.desc);
        }
        setObject(nvsVideoTransition);
        if (nvsVideoTransition != null) {
            nvsVideoTransition.setVideoTransitionDuration(getDuration(), 0);
        }
        return nvsVideoTransition;
    }

    public void loadData(NvsVideoTransition nvsVideoTransition) {
        setObject(nvsVideoTransition);
        if (nvsVideoTransition.getVideoTransitionType() == 0) {
            this.type = 0;
            this.desc = nvsVideoTransition.getBuiltinVideoTransitionName();
        } else {
            this.type = 1;
            this.desc = nvsVideoTransition.getVideoTransitionPackageId();
        }
        this.duration = nvsVideoTransition.getVideoTransitionDuration();
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
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

    public String getIconPath() {
        return this.iconPath;
    }

    public void setIconPath(String str) {
        this.iconPath = str;
    }

    public int getIconResourceId() {
        return this.iconResourceId;
    }

    public void setIconResourceId(int i) {
        this.iconResourceId = i;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MeicamTransition)) {
            return super.equals(obj);
        }
        if (getIndex() == ((MeicamTransition) obj).getIndex()) {
            return true;
        }
        return false;
    }

    @Override // java.lang.Object
    @NonNull
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return "";
        }
    }
}
