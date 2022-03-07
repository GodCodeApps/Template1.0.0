package com.meishe.engine.bean;

import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsVideoResolution;
import com.meishe.engine.TimelineUtil;
import com.meishe.engine.data.MeicamAdjustData;
import com.meishe.engine.util.gson.GsonContext;
import java.util.ArrayList;
import java.util.List;

public class TimelineData {
    private static TimelineData sTimelineData;
    private long duration;
    @SerializedName("coverImagePath")
    private String mCoverImagePath;
    private transient String mDraftDir;
    private transient MeicamVideoClip mEditMeicamClipInfo;
    @SerializedName("filterFx")
    private MeicamVideoFx mFilterFx;
    @SerializedName("lastModifiedTime")
    private String mLastModifiedTime;
    @SerializedName("aspectRatioMode")
    private int mMakeRatio = 0;
    @SerializedName("adjustData")
    private MeicamAdjustData mMeicamAdjustData = new MeicamAdjustData();
    @SerializedName("audioTracks")
    private List<MeicamAudioTrack> mMeicamAudioTrackList = new ArrayList();
    @SerializedName("resources")
    private List<MeicamResource> mMeicamResourceList = new ArrayList();
    @SerializedName("stickerCaptionTracks")
    private List<MeicamStickerCaptionTrack> mMeicamStickerCaptionTrackList = new ArrayList();
    @SerializedName("theme")
    private MeicamTheme mMeicamTheme;
    @SerializedName("timelineVideoFxClips")
    private List<MeicamTimelineVideoFxClip> mMeicamTimelineVideoFxClipList = new ArrayList();
    @SerializedName("timelineVideoFxTracks")
    private List<MeicamTimelineVideoFxTrack> mMeicamTimelineVideoFxTrackList = new ArrayList();
    @SerializedName("videoTracks")
    private List<MeicamVideoTrack> mMeicamVideoTrackList = new ArrayList();
    @SerializedName("waterMark")
    private MeicamWaterMark mMeicamWaterMark = new MeicamWaterMark(null, null);
    @SerializedName("projectDuring")
    private String mProjectDuring;
    @SerializedName("projectId")
    private String mProjectId;
    @SerializedName("projectName")
    private String mProjectName;
    @SerializedName("videoResolution")
    private NvsVideoResolution mVideoResolution;
    private int resHeight;
    private int resWidth;

    private TimelineData() {
    }

    public static TimelineData getInstance() {
        if (sTimelineData == null) {
            synchronized (TimelineData.class) {
                if (sTimelineData == null) {
                    sTimelineData = new TimelineData();
                }
            }
        }
        return sTimelineData;
    }

    public String toJson() {
        return GsonContext.getInstance().getGson().toJson(this);
    }

    public Object fromJson(String str) {
        sTimelineData = (TimelineData) GsonContext.getInstance().getGson().fromJson(str, TimelineData.class);
        return sTimelineData;
    }

    public void clearData() {
        sTimelineData = null;
    }

    public int getResWidth() {
        return this.resWidth;
    }

    public void setResWidth(int i) {
        this.resWidth = i;
    }

    public int getResHeight() {
        return this.resHeight;
    }

    public void setResHeight(int i) {
        this.resHeight = i;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public List<MeicamResource> getMeicamResourceList() {
        return this.mMeicamResourceList;
    }

    public void setMeicamResourceList(List<MeicamResource> list) {
        this.mMeicamResourceList = list;
    }

    public List<MeicamVideoTrack> getMeicamVideoTrackList() {
        return this.mMeicamVideoTrackList;
    }

    public void setMeicamVideoTrackList(List<MeicamVideoTrack> list) {
        this.mMeicamVideoTrackList = list;
    }

    public List<MeicamAudioTrack> getMeicamAudioTrackList() {
        return this.mMeicamAudioTrackList;
    }

    public void setMeicamAudioTrackList(List<MeicamAudioTrack> list) {
        this.mMeicamAudioTrackList = list;
    }

    public List<MeicamStickerCaptionTrack> getMeicamStickerCaptionTrackList() {
        return this.mMeicamStickerCaptionTrackList;
    }

    public void setMeicamStickerCaptionTrackList(List<MeicamStickerCaptionTrack> list) {
        this.mMeicamStickerCaptionTrackList = list;
    }

    public List<MeicamTimelineVideoFxTrack> getMeicamTimelineVideoFxTrackList() {
        return this.mMeicamTimelineVideoFxTrackList;
    }

    public void setMeicamTimelineVideoFxTrackList(List<MeicamTimelineVideoFxTrack> list) {
        this.mMeicamTimelineVideoFxTrackList = list;
    }

    public List<MeicamTimelineVideoFxClip> getMeicamTimelineVideoFxClipList() {
        if (this.mMeicamTimelineVideoFxClipList == null) {
            this.mMeicamTimelineVideoFxClipList = new ArrayList();
        }
        return this.mMeicamTimelineVideoFxClipList;
    }

    public void setMeicamTimelineVideoFxClipList(List<MeicamTimelineVideoFxClip> list) {
        this.mMeicamTimelineVideoFxClipList = list;
    }

    public MeicamWaterMark getMeicamWaterMark() {
        if (this.mMeicamWaterMark == null) {
            this.mMeicamWaterMark = new MeicamWaterMark(null, null);
        }
        return this.mMeicamWaterMark;
    }

    public void setMeicamWaterMark(MeicamWaterMark meicamWaterMark) {
        this.mMeicamWaterMark = meicamWaterMark;
    }

    public MeicamTheme getMeicamTheme() {
        return this.mMeicamTheme;
    }

    public void setMeicamTheme(MeicamTheme meicamTheme) {
        this.mMeicamTheme = meicamTheme;
    }

    public MeicamAdjustData getMeicamAdjustData() {
        return this.mMeicamAdjustData;
    }

    public void setMeicamAdjustData(MeicamAdjustData meicamAdjustData) {
        this.mMeicamAdjustData = meicamAdjustData;
    }

    public String getProjectId() {
        return this.mProjectId;
    }

    public void setProjectId(String str) {
        this.mProjectId = str;
    }

    public String getProjectDuring() {
        return this.mProjectDuring;
    }

    public void setProjectDuring(String str) {
        this.mProjectDuring = str;
    }

    public String getProjectName() {
        return this.mProjectName;
    }

    public void setProjectName(String str) {
        this.mProjectName = str;
    }

    public String getLastModifiedTime() {
        return this.mLastModifiedTime;
    }

    public void setLastModifiedTime(String str) {
        this.mLastModifiedTime = str;
    }

    public int getMakeRatio() {
        return this.mMakeRatio;
    }

    public void setMakeRatio(int i) {
        this.mMakeRatio = i;
    }

    public String getCoverImagePath() {
        return this.mCoverImagePath;
    }

    public void setCoverImagePath(String str) {
        this.mCoverImagePath = str;
    }

    public NvsVideoResolution getVideoResolution() {
        return this.mVideoResolution;
    }

    public void setVideoResolution(NvsVideoResolution nvsVideoResolution) {
        TimelineUtil.alignedResolution(nvsVideoResolution);
        this.mVideoResolution = nvsVideoResolution;
    }

    public MeicamVideoFx getFilterFx() {
        return this.mFilterFx;
    }

    public void setFilterFx(MeicamVideoFx meicamVideoFx) {
        this.mFilterFx = meicamVideoFx;
    }

    public MeicamVideoClip getSelectedMeicamClipInfo() {
        return this.mEditMeicamClipInfo;
    }

    public void setSelectedMeicamClipInfo(MeicamVideoClip meicamVideoClip) {
        this.mEditMeicamClipInfo = meicamVideoClip;
    }

    public String getDraftDir() {
        return this.mDraftDir;
    }

    public void setDraftDir(String str) {
        this.mDraftDir = str;
    }

    public int addPicClipInfoData(MeicamVideoClip meicamVideoClip) {
        MeicamVideoTrack meicamVideoTrack = new MeicamVideoTrack(this.mMeicamVideoTrackList.size());
        meicamVideoTrack.getClipInfoList().add(meicamVideoClip);
        this.mMeicamVideoTrackList.add(meicamVideoTrack);
        return meicamVideoTrack.getIndex();
    }
}
