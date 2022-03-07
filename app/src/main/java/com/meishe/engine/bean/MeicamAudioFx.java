package com.meishe.engine.bean;

import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsAudioFx;
import java.util.ArrayList;
import java.util.List;

public class MeicamAudioFx extends NvsObject<NvsAudioFx> implements Cloneable {
    String desc;
    int index;
    @SerializedName("fxParams")
    List<MeicamFxParam> mMeicamFxParam = new ArrayList();
    String type;

    public MeicamAudioFx() {
        super(null);
    }

    public MeicamAudioFx(int i, String str, String str2) {
        super(null);
        this.index = i;
        this.type = str;
        this.desc = str2;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public List<MeicamFxParam> getMeicamFxParam() {
        return this.mMeicamFxParam;
    }

    public void setMeicamFxParam(List<MeicamFxParam> list) {
        this.mMeicamFxParam = list;
    }
}
