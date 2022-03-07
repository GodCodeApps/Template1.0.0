package com.meishe.myvideo.bean;

public class EditData {
    private static EditData sEditData;
    private boolean mIsDenoise;
    private Opacity mOpacity = new Opacity();
    private float mSpeed;
    private TransitionData mTranstionData = new TransitionData();
    private VolumeData mVolumeData = new VolumeData();

    private EditData() {
    }

    public static EditData getInstance() {
        if (sEditData == null) {
            synchronized (EditData.class) {
                if (sEditData == null) {
                    sEditData = new EditData();
                }
            }
        }
        return sEditData;
    }

    public float getSpeed() {
        return this.mSpeed;
    }

    public void setSpeed(float f) {
        this.mSpeed = f;
    }

    public boolean isDenoise() {
        return this.mIsDenoise;
    }

    public void setIsDenoise(boolean z) {
        this.mIsDenoise = z;
    }

    public VolumeData getVolumeData() {
        return this.mVolumeData;
    }

    public TransitionData getTranstionData() {
        return this.mTranstionData;
    }

    public void setTranstionData(TransitionData transitionData) {
        this.mTranstionData = transitionData;
    }

    public void setVolumeData(VolumeData volumeData) {
        this.mVolumeData = volumeData;
    }

    public Opacity getOpacity() {
        return this.mOpacity;
    }

    public void setOpacity(Opacity opacity) {
        this.mOpacity = opacity;
    }

    public class VolumeData {
        public int mMaxValue = 200;
        public float mStrength;

        public VolumeData() {
        }
    }

    public class TransitionData {
        public int mMaxValue = 10;
        public float mStrength;

        public TransitionData() {
        }
    }

    public class Opacity {
        public int mMaxValue = 100;
        public float mStrength;

        public Opacity() {
        }
    }
}
