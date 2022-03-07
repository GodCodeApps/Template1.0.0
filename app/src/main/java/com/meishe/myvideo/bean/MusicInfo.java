package com.meishe.myvideo.bean;

import android.graphics.Bitmap;
import java.io.Serializable;

public class MusicInfo implements Serializable {
    private String m_artist = null;
    private String m_assetPath = null;
    private long m_duration = 0;
    private String m_exoPlayerPath;
    private int m_extraMusic = 0;
    private long m_extraMusicLeft = 0;
    private long m_fadeDuration = 0;
    private String m_filePath = null;
    private String m_fileUrl = null;
    private Bitmap m_image = null;
    private String m_imagePath = null;
    private long m_inPoint = 0;
    private boolean m_isAsset = false;
    private boolean m_isHttpMusic = false;
    private String m_lrcPath = "";
    private int m_mimeType = 0;
    private long m_originalInPoint = 0;
    private long m_originalOutPoint = 0;
    private long m_originalTrimIn = 0;
    private long m_originalTrimOut = 0;
    private long m_outPoint = 0;
    private boolean m_play = false;
    private boolean m_prepare = false;
    private String m_title = null;
    private long m_trimIn = 0;
    private long m_trimOut = 0;
    private float m_volume = 1.0f;

    @Override // java.lang.Object
    public MusicInfo clone() {
        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setFileUrl(getFileUrl());
        musicInfo.setFilePath(getFilePath());
        musicInfo.setDuration(getDuration());
        musicInfo.setArtist(getArtist());
        musicInfo.setImage(getImage());
        musicInfo.setImagePath(getImagePath());
        musicInfo.setTitle(getTitle());
        musicInfo.setTrimIn(getTrimIn());
        musicInfo.setTrimOut(getTrimOut());
        musicInfo.setMimeType(getMimeType());
        musicInfo.setIsAsset(isAsset());
        musicInfo.setPrepare(isPrepare());
        musicInfo.setPlay(isPlay());
        musicInfo.setIsHttpMusic(isHttpMusic());
        musicInfo.setAssetPath(getAssetPath());
        musicInfo.setInPoint(getInPoint());
        musicInfo.setOutPoint(getOutPoint());
        musicInfo.setVolume(getVolume());
        musicInfo.setOriginalInPoint(getOriginalInPoint());
        musicInfo.setOriginalOutPoint(getOriginalOutPoint());
        musicInfo.setOriginalTrimIn(getOriginalTrimIn());
        musicInfo.setOriginalTrimOut(getOriginalTrimOut());
        musicInfo.setExtraMusic(getExtraMusic());
        musicInfo.setExtraMusicLeft(getExtraMusicLeft());
        musicInfo.setFadeDuration(getFadeDuration());
        musicInfo.setLrcPath(getLrcPath());
        return musicInfo;
    }

    public void setTitle(String str) {
        this.m_title = str;
    }

    public String getTitle() {
        return this.m_title;
    }

    public void setArtist(String str) {
        this.m_artist = str;
    }

    public String getArtist() {
        return this.m_artist;
    }

    public void setImagePath(String str) {
        this.m_imagePath = str;
    }

    public String getImagePath() {
        return this.m_imagePath;
    }

    public void setFilePath(String str) {
        this.m_filePath = str;
    }

    public String getFilePath() {
        return this.m_filePath;
    }

    public void setFileUrl(String str) {
        this.m_fileUrl = str;
    }

    public String getFileUrl() {
        return this.m_fileUrl;
    }

    public String getAssetPath() {
        return this.m_assetPath;
    }

    public void setAssetPath(String str) {
        this.m_assetPath = str;
    }

    public void setImage(Bitmap bitmap) {
        this.m_image = bitmap;
    }

    public Bitmap getImage() {
        return this.m_image;
    }

    public void setInPoint(long j) {
        this.m_inPoint = j;
    }

    public long getInPoint() {
        return this.m_inPoint;
    }

    public void setOutPoint(long j) {
        this.m_outPoint = j;
    }

    public long getOutPoint() {
        return this.m_outPoint;
    }

    public void setDuration(long j) {
        this.m_duration = j;
    }

    public long getDuration() {
        return this.m_duration;
    }

    public void setTrimIn(long j) {
        this.m_trimIn = j;
    }

    public long getTrimIn() {
        return this.m_trimIn;
    }

    public void setTrimOut(long j) {
        this.m_trimOut = j;
    }

    public long getTrimOut() {
        return this.m_trimOut;
    }

    public int getMimeType() {
        return this.m_mimeType;
    }

    public void setMimeType(int i) {
        this.m_mimeType = i;
    }

    public boolean isPrepare() {
        return this.m_prepare;
    }

    public void setPrepare(boolean z) {
        this.m_prepare = z;
    }

    public boolean isHttpMusic() {
        return this.m_isHttpMusic;
    }

    public void setIsHttpMusic(boolean z) {
        this.m_isHttpMusic = z;
    }

    public boolean isAsset() {
        return this.m_isAsset;
    }

    public void setIsAsset(boolean z) {
        this.m_isAsset = z;
    }

    public boolean isPlay() {
        return this.m_play;
    }

    public void setPlay(boolean z) {
        this.m_play = z;
    }

    public float getVolume() {
        return this.m_volume;
    }

    public void setVolume(float f) {
        this.m_volume = f;
    }

    public long getOriginalInPoint() {
        return this.m_originalInPoint;
    }

    public void setOriginalInPoint(long j) {
        this.m_originalInPoint = j;
    }

    public long getOriginalOutPoint() {
        return this.m_originalOutPoint;
    }

    public void setOriginalOutPoint(long j) {
        this.m_originalOutPoint = j;
    }

    public long getOriginalTrimIn() {
        return this.m_originalTrimIn;
    }

    public void setOriginalTrimIn(long j) {
        this.m_originalTrimIn = j;
    }

    public long getOriginalTrimOut() {
        return this.m_originalTrimOut;
    }

    public void setOriginalTrimOut(long j) {
        this.m_originalTrimOut = j;
    }

    public int getExtraMusic() {
        return this.m_extraMusic;
    }

    public void setExtraMusic(int i) {
        this.m_extraMusic = i;
    }

    public long getExtraMusicLeft() {
        return this.m_extraMusicLeft;
    }

    public void setExtraMusicLeft(long j) {
        this.m_extraMusicLeft = j;
    }

    public long getFadeDuration() {
        return this.m_fadeDuration;
    }

    public void setFadeDuration(long j) {
        this.m_fadeDuration = j;
    }

    public void setExoplayerPath(String str) {
        this.m_exoPlayerPath = str;
    }

    public String getExoPlayerPath() {
        return this.m_exoPlayerPath;
    }

    public String getLrcPath() {
        return this.m_lrcPath;
    }

    public void setLrcPath(String str) {
        this.m_lrcPath = str;
    }
}
