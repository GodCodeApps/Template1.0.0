package com.meishe.myvideo.ui.bean;

import java.io.Serializable;

public abstract class BaseUIClip implements Serializable, Cloneable {
    private int clipIndexInTrack;
    private String filePath;
    private String iconFilePath;
    private long inPoint;
    private long mAudioFadeIn;
    private long mAudioFadeOut;
    private int mAudioType;
    private Object nvsObject;
    private float[] recordArray;
    private int recordLength;
    private double speed = 1.0d;
    private String text;
    private int trackIndex;
    private long trimIn;
    private long trimOut;
    private String type;

    public Object getNvsObject() {
        return this.nvsObject;
    }

    public void setNvsObject(Object obj) {
        this.nvsObject = obj;
    }

    public int getAudioType() {
        return this.mAudioType;
    }

    public void setAudioType(int i) {
        this.mAudioType = i;
    }

    public int getClipIndexInTrack() {
        return this.clipIndexInTrack;
    }

    public void setClipIndexInTrack(int i) {
        this.clipIndexInTrack = i;
    }

    public int getTrackIndex() {
        return this.trackIndex;
    }

    public void setTrackIndex(int i) {
        this.trackIndex = i;
    }

    public long getInPoint() {
        return this.inPoint;
    }

    public void setInPoint(long j) {
        this.inPoint = j;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double d) {
        this.speed = d;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getIconFilePath() {
        return this.iconFilePath;
    }

    public void setIconFilePath(String str) {
        this.iconFilePath = str;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
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

    public float[] getRecordArray() {
        return this.recordArray;
    }

    public void setRecordArray(float[] fArr) {
        this.recordArray = fArr;
    }

    public void setRecordLength(int i) {
        this.recordLength = i;
    }

    public int getRecordLength() {
        return this.recordLength;
    }

    public long getAudioFadeIn() {
        return this.mAudioFadeIn;
    }

    public void setAudioFadeIn(long j) {
        this.mAudioFadeIn = j;
    }

    public long getAudioFadeOut() {
        return this.mAudioFadeOut;
    }

    public void setAudioFadeOut(long j) {
        this.mAudioFadeOut = j;
    }

    @Override // java.lang.Object
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return "BaseUIClip{clipIndexInTrack=" + this.clipIndexInTrack + "\r\n, trackIndex=" + this.trackIndex + "\n, inPoint=" + this.inPoint + "\n, speed=" + this.speed + "\n, trimIn=" + this.trimIn + "\n, trimOut=" + this.trimOut + "\n, type='" + this.type + '\'' + "\n, iconFilePath='" + this.iconFilePath + '\'' + "\n, filePath='" + this.filePath + '\'' + "\n, text='" + this.text + '\'' + "\n, nvsObject=" + this.nvsObject + '}';
    }
}
