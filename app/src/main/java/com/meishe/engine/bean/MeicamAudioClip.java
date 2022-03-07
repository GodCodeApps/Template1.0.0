package com.meishe.engine.bean;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsVolume;
import com.meishe.engine.util.DeepCopyUtil;
import java.io.Serializable;
import java.util.List;

public class MeicamAudioClip extends ClipInfo<NvsAudioClip> implements Cloneable, Serializable {
    public static final int AUDIO_MUSIC = 3;
    public static final int AUDIO_RECORD_FILE = 1;
    public static final int AUDIO_RECORD_ING = 2;
    public static final int VIDEO_MUSIC = 4;
    private long fadeInDuration;
    private long fadeOutDuration;
    private String filePath;
    private long id = -1;
    @SerializedName("audioType")
    private int mAudioType;
    @SerializedName("drawText")
    private String mDrawText;
    @SerializedName("audioFxs")
    private List<MeicamAudioFx> mMeicamAudioFxs;
    @SerializedName("orgDuration")
    private long mOriginalDuring;
    private double speed = 1.0d;
    private long trimIn = 0;
    private long trimOut = 0;
    private float volume = 0.5f;

    public MeicamAudioClip() {
        super(CommonData.CLIP_AUDIO);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public long getTrimIn() {
        return this.trimIn;
    }

    public void setTrimIn(long j) {
        this.trimIn = j;
    }

    public long getTrimOut() {
        return this.trimOut;
    }

    public void setTrimOut(long j) {
        this.trimOut = j;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float f) {
        this.volume = f;
        NvsAudioClip nvsAudioClip = (NvsAudioClip) getObject();
        if (nvsAudioClip != null) {
            nvsAudioClip.setVolumeGain(f, f);
        }
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double d) {
        this.speed = d;
        NvsAudioClip nvsAudioClip = (NvsAudioClip) getObject();
        if (nvsAudioClip != null) {
            nvsAudioClip.changeSpeed(d);
            setInPoint(nvsAudioClip.getInPoint());
            setOutPoint(nvsAudioClip.getOutPoint());
        }
    }

    public long getFadeInDuration() {
        return this.fadeInDuration;
    }

    public void setFadeInDuration(long j) {
        this.fadeInDuration = j;
        NvsAudioClip nvsAudioClip = (NvsAudioClip) getObject();
        if (nvsAudioClip != null) {
            nvsAudioClip.setFadeInDuration(j);
        }
    }

    public long getFadeOutDuration() {
        return this.fadeOutDuration;
    }

    public void setFadeOutDuration(long j) {
        this.fadeOutDuration = j;
        NvsAudioClip nvsAudioClip = (NvsAudioClip) getObject();
        if (nvsAudioClip != null) {
            nvsAudioClip.setFadeOutDuration(j);
        }
    }

    public int getAudioType() {
        return this.mAudioType;
    }

    public void setAudioType(int i) {
        this.mAudioType = i;
    }

    public String getDrawText() {
        return this.mDrawText;
    }

    public void setDrawText(String str) {
        this.mDrawText = str;
    }

    public long getOriginalDuring() {
        return this.mOriginalDuring;
    }

    public void setOriginalDuring(long j) {
        this.mOriginalDuring = j;
    }

    public List<MeicamAudioFx> getMeicamAudioFxes() {
        return this.mMeicamAudioFxs;
    }

    public void setMeicaAudioFxes(List<MeicamAudioFx> list) {
        this.mMeicamAudioFxs = list;
        NvsAudioClip nvsAudioClip = (NvsAudioClip) getObject();
        if (nvsAudioClip != null) {
            if (list == null) {
                nvsAudioClip.removeFx(0);
                return;
            }
            for (MeicamAudioFx meicamAudioFx : list) {
                if ("builtin".equals(meicamAudioFx.getType())) {
                    nvsAudioClip.removeFx(0);
                    nvsAudioClip.appendFx(meicamAudioFx.getDesc());
                }
            }
        }
    }

    public void loadData(NvsAudioClip nvsAudioClip) {
        setObject(nvsAudioClip);
        setFilePath(nvsAudioClip.getFilePath());
        setInPoint(nvsAudioClip.getInPoint());
        setOutPoint(nvsAudioClip.getOutPoint());
        this.trimIn = nvsAudioClip.getTrimIn();
        this.trimOut = nvsAudioClip.getTrimOut();
        this.speed = nvsAudioClip.getSpeed();
        this.fadeInDuration = nvsAudioClip.getFadeInDuration();
        this.fadeOutDuration = nvsAudioClip.getFadeOutDuration();
        int fxCount = nvsAudioClip.getFxCount();
        List<MeicamAudioFx> list = this.mMeicamAudioFxs;
        if (list != null) {
            list.clear();
            if (fxCount > 0) {
                this.mMeicamAudioFxs.add(new MeicamAudioFx(0, "builtin", nvsAudioClip.getFxByIndex(0).getBuiltinAudioFxName()));
            }
        }
        NvsVolume volumeGain = nvsAudioClip.getVolumeGain();
        if (volumeGain != null) {
            setVolume(volumeGain.leftVolume);
        }
    }

    public NvsAudioClip bindToTimeline(NvsAudioTrack nvsAudioTrack) {
        NvsAudioClip nvsAudioClip;
        if (nvsAudioTrack == null) {
            return null;
        }
        if (getTrimOut() <= 0) {
            nvsAudioClip = nvsAudioTrack.addClip(getFilePath(), getInPoint());
        } else {
            nvsAudioClip = nvsAudioTrack.addClip(getFilePath(), getInPoint(), getTrimIn(), getTrimOut());
        }
        setIndex(nvsAudioTrack.getIndex());
        setOtherAttribute(nvsAudioClip);
        return nvsAudioClip;
    }

    public void setOtherAttribute(NvsAudioClip nvsAudioClip) {
        if (nvsAudioClip != null) {
            setObject(nvsAudioClip);
            nvsAudioClip.setFadeInDuration(getFadeInDuration());
            nvsAudioClip.setFadeOutDuration(getFadeOutDuration());
            float f = this.volume;
            nvsAudioClip.setVolumeGain(f, f);
            nvsAudioClip.changeSpeed(getSpeed());
            List<MeicamAudioFx> list = this.mMeicamAudioFxs;
            if (list != null) {
                for (MeicamAudioFx meicamAudioFx : list) {
                    if ("builtin".equals(meicamAudioFx.getType())) {
                        nvsAudioClip.appendFx(meicamAudioFx.getDesc());
                    }
                }
            }
        }
    }

    @Override // java.lang.Object
    @NonNull
    public Object clone() {
        return DeepCopyUtil.deepClone(this);
    }
}
