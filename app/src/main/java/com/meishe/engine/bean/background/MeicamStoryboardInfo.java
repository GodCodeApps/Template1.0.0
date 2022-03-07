package com.meishe.engine.bean.background;

import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.MeicamFxParam;
import com.meishe.engine.bean.MeicamVideoFx;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MeicamStoryboardInfo extends MeicamVideoFx implements Cloneable, Serializable {
    public static final String SUB_TYPE_BACKGROUND = "background";
    public static final String SUB_TYPE_CROPPER = "cropper";
    public static final String SUB_TYPE_CROPPER_TRANSFROM = "cropper_transform";
    private static String TAG = "MeicamStoryboardInfo";
    protected String classType = "Storyboard";
    private Map<String, Float> clipTrans = new HashMap();
    private String source;
    private String sourceDir;
    private String storyDesc;

    public MeicamStoryboardInfo() {
        this.desc = "Storyboard";
        this.type = "builtin";
    }

    public String getStoryDesc() {
        return this.storyDesc;
    }

    public void setStoryDesc(String str) {
        this.storyDesc = str;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public String getSourceDir() {
        return this.sourceDir;
    }

    public void setSourceDir(String str) {
        this.sourceDir = str;
    }

    public Map<String, Float> getClipTrans() {
        return this.clipTrans;
    }

    public void setClipTrans(Map<String, Float> map) {
        this.clipTrans = map;
    }

    public NvsVideoFx bindToTimelineByType(NvsVideoClip nvsVideoClip, String str) {
        if (nvsVideoClip == null) {
            return null;
        }
        NvsVideoFx storyboardFx = getStoryboardFx(nvsVideoClip, str);
        if (storyboardFx == null) {
            storyboardFx = nvsVideoClip.appendBuiltinFx(getDesc());
        }
        if (storyboardFx == null) {
            Logger.e(TAG, "bindToTimelineByType nvsVideoFx is null!");
            return null;
        }
        for (String str2 : this.mMeicamFxParam.keySet()) {
            MeicamFxParam meicamFxParam = (MeicamFxParam) this.mMeicamFxParam.get(str2);
            if (MeicamFxParam.TYPE_STRING.equals(meicamFxParam.getType())) {
                storyboardFx.setStringVal(str2, (String) meicamFxParam.getValue());
            } else if (MeicamFxParam.TYPE_BOOLEAN.equals(meicamFxParam.getType())) {
                storyboardFx.setBooleanVal(str2, ((Boolean) meicamFxParam.getValue()).booleanValue());
            }
        }
        if (storyboardFx != null) {
            storyboardFx.setAttachment(MeicamVideoFx.ATTACHMENT_KEY_SUB_TYPE, str);
            setObject(storyboardFx);
        }
        return storyboardFx;
    }

    private NvsVideoFx getStoryboardFx(NvsVideoClip nvsVideoClip, String str) {
        int fxCount = nvsVideoClip.getFxCount();
        for (int i = 0; i < fxCount; i++) {
            NvsVideoFx fxByIndex = nvsVideoClip.getFxByIndex(i);
            Object attachment = fxByIndex.getAttachment(MeicamVideoFx.ATTACHMENT_KEY_SUB_TYPE);
            if (attachment != null && (attachment instanceof String) && ((String) attachment).equals(str)) {
                return fxByIndex;
            }
        }
        return null;
    }
}
