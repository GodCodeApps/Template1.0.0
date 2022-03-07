package com.meishe.myvideo.bean;

import com.meishe.engine.data.MeicamClipInfo;
import java.io.Serializable;
import java.util.List;

public class DraftBean implements Serializable {
    private List<MeicamClipInfo> clipList;
    private String draftName;
    private String draftSize;
    private String draftTime;
    private String lastModifyTime;
    private int mMakeRatio = 1;
}
