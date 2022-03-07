package com.meishe.engine.bean;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meishe.engine.util.DeepCopyUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MeicamVideoFx extends NvsObject<NvsVideoFx> implements Cloneable, Serializable {
    public static final String ATTACHMENT_KEY_SUB_TYPE = "subType";
    public static final String SUB_TYPE_CLIP_FILTER = "clipFilter";
    public static final String SUB_TYPE_TIMELINE_FILTER = "timelineFilter";
    protected String desc;
    protected int index;
    protected float intensity = 1.0f;
    @SerializedName("fxParams")
    protected Map<String, MeicamFxParam> mMeicamFxParam = new HashMap();
    protected String subType;
    protected String type;

    public MeicamVideoFx() {
        super(null);
    }

    public MeicamVideoFx(int i, String str, String str2) {
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

    public float getIntensity() {
        return this.intensity;
    }

    public void setSubType(String str) {
        this.subType = str;
    }

    public String getSubType() {
        return this.subType;
    }

    public void setIntensity(float f) {
        this.intensity = f;
        NvsVideoFx nvsVideoFx = (NvsVideoFx) getObject();
        if (nvsVideoFx != null) {
            nvsVideoFx.setFilterIntensity(f);
        }
    }

    public Map<String, MeicamFxParam> getMeicamFxParam() {
        return this.mMeicamFxParam;
    }

    public void setMeicamFxParam(Map<String, MeicamFxParam> map) {
        this.mMeicamFxParam = map;
    }

    public void setStringVal(String str, String str2) {
        MeicamFxParam meicamFxParam = new MeicamFxParam(MeicamFxParam.TYPE_STRING, str, str2);
        this.mMeicamFxParam.put(meicamFxParam.getKey(), meicamFxParam);
        NvsVideoFx nvsVideoFx = (NvsVideoFx) getObject();
        if (nvsVideoFx != null) {
            nvsVideoFx.setStringVal(str, str2);
        }
    }

    public String getStringVal(String str) {
        MeicamFxParam meicamFxParam = this.mMeicamFxParam.get(str);
        if (meicamFxParam != null && MeicamFxParam.TYPE_STRING.equals(meicamFxParam.getType())) {
            return (String) meicamFxParam.getValue();
        }
        return null;
    }

    public float getFloatVal(String str) {
        MeicamFxParam meicamFxParam = this.mMeicamFxParam.get(str);
        if (meicamFxParam != null && MeicamFxParam.TYPE_FLOAT.equals(meicamFxParam.getType())) {
            Object value = meicamFxParam.getValue();
            if (value instanceof Float) {
                return ((Float) value).floatValue();
            }
            if (value instanceof Double) {
                return (float) ((Double) value).doubleValue();
            }
        }
        return -1.0f;
    }

    public void setBooleanVal(String str, boolean z) {
        MeicamFxParam meicamFxParam = new MeicamFxParam(MeicamFxParam.TYPE_BOOLEAN, str, Boolean.valueOf(z));
        this.mMeicamFxParam.put(meicamFxParam.getKey(), meicamFxParam);
        NvsVideoFx nvsVideoFx = (NvsVideoFx) getObject();
        if (nvsVideoFx != null) {
            nvsVideoFx.setBooleanVal(str, z);
        }
    }

    public void setFloatVal(String str, float f) {
        MeicamFxParam meicamFxParam = new MeicamFxParam(MeicamFxParam.TYPE_FLOAT, str, Float.valueOf(f));
        this.mMeicamFxParam.put(meicamFxParam.getKey(), meicamFxParam);
        NvsVideoFx nvsVideoFx = (NvsVideoFx) getObject();
        if (nvsVideoFx != null) {
            nvsVideoFx.setFloatVal(str, (double) f);
        }
    }

    public <T> void setObjectVal(String str, T t) {
        MeicamFxParam meicamFxParam = new MeicamFxParam(MeicamFxParam.TYPE_OBJECT, str, t);
        this.mMeicamFxParam.put(meicamFxParam.getKey(), meicamFxParam);
    }

    public NvsVideoFx bindToTimeline(NvsVideoClip nvsVideoClip) {
        NvsVideoFx nvsVideoFx = null;
        if (nvsVideoClip == null) {
            return null;
        }
        if ("builtin".equals(this.type)) {
            nvsVideoFx = nvsVideoClip.appendBuiltinFx(this.desc);
        } else if ("package".equals(this.type)) {
            nvsVideoFx = nvsVideoClip.appendPackagedFx(this.desc);
        }
        if (nvsVideoFx != null) {
            setValue(nvsVideoFx);
            setObject(nvsVideoFx);
            nvsVideoFx.setFilterIntensity(getIntensity());
            nvsVideoFx.setAttachment(ATTACHMENT_KEY_SUB_TYPE, getSubType());
        }
        return nvsVideoFx;
    }

    private void setValue(NvsVideoFx nvsVideoFx) {
        for (String str : this.mMeicamFxParam.keySet()) {
            MeicamFxParam meicamFxParam = this.mMeicamFxParam.get(str);
            if (MeicamFxParam.TYPE_STRING.equals(meicamFxParam.getType())) {
                nvsVideoFx.setStringVal(str, (String) meicamFxParam.getValue());
            } else if (MeicamFxParam.TYPE_BOOLEAN.equals(meicamFxParam.getType())) {
                nvsVideoFx.setBooleanVal(str, ((Boolean) meicamFxParam.getValue()).booleanValue());
            } else if (MeicamFxParam.TYPE_FLOAT.equals(meicamFxParam.getType())) {
                Object value = meicamFxParam.getValue();
                if (value instanceof Float) {
                    nvsVideoFx.setFloatVal(str, (double) ((Float) value).floatValue());
                } else if (value instanceof Double) {
                    nvsVideoFx.setFloatVal(str, ((Double) value).doubleValue());
                }
            }
        }
    }

    @Override // java.lang.Object
    @NonNull
    public MeicamVideoFx clone() {
        return (MeicamVideoFx) DeepCopyUtil.deepClone(this);
    }
}
