package com.meishe.draft.data;

import androidx.annotation.NonNull;
import java.io.Serializable;

public class DraftData implements Serializable, Cloneable {
    private String mCoverPath;
    private String mDirPath;
    private String mDuration;
    private String mFileName;
    private String mFileSize;
    private String mJsonData;
    private String mLastModifyTime;
    private long mLastModifyTimeLong;

    public String getDirPath() {
        return this.mDirPath;
    }

    public void setDirPath(String str) {
        this.mDirPath = str;
    }

    public String getJsonData() {
        return this.mJsonData;
    }

    public void setJsonData(String str) {
        this.mJsonData = str;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public void setFileName(String str) {
        this.mFileName = str;
    }

    public String getFileSize() {
        return this.mFileSize;
    }

    public void setFileSize(String str) {
        this.mFileSize = str;
    }

    public String getDuration() {
        return this.mDuration;
    }

    public void setDuration(String str) {
        this.mDuration = str;
    }

    public String getLastModifyTime() {
        return this.mLastModifyTime;
    }

    public void setLastModifyTime(String str) {
        this.mLastModifyTime = str;
    }

    public void setCoverPath(String str) {
        this.mCoverPath = str;
    }

    public String getCoverPath() {
        return this.mCoverPath;
    }

    public long getLasetModifyTimeLong() {
        return this.mLastModifyTimeLong;
    }

    public void setLasetModifyTimeLong(long j) {
        this.mLastModifyTimeLong = j;
    }

    @Override // java.lang.Object
    @NonNull
    public DraftData clone() {
        try {
            return (DraftData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
