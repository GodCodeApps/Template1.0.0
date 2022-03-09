package com.meishe.myvideo.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaData {
    private long data;
    private String displayName;
    private long duration;
    private int id;
    private String path;
    private int position;
    private boolean state;
    private String thumbPath;
    private int type;

    public MediaData() {
    }

    public MediaData(int i, int i2, String str, String str2, long j, String str3, boolean z) {
        this.id = i;
        this.type = i2;
        this.path = str;
        this.thumbPath = str2;
        this.data = j;
        this.displayName = str3;
        this.state = z;
        this.duration = -1;
    }

    public MediaData(int i, int i2, String str, String str2, long j, long j2, String str3, boolean z) {
        this.id = i;
        this.type = i2;
        this.path = str;
        this.thumbPath = str2;
        this.duration = j;
        this.data = j2;
        this.displayName = str3;
        this.state = z;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(this.data));
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(boolean z) {
        this.state = z;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getPath() {
        String str = this.path;
        return str == null ? "" : str;
    }

    public void setPath(String str) {
        if (str == null) {
            str = "";
        }
        this.path = str;
    }

    public String getThumbPath() {
        String str = this.thumbPath;
        return str == null ? "" : str;
    }

    public void setThumbPath(String str) {
        if (str == null) {
            str = "";
        }
        this.thumbPath = str;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(int i) {
        this.duration = (long) i;
    }

    public long getData() {
        return this.data;
    }

    public void setData(long j) {
        this.data = j;
    }

    public String getDisplayName() {
        String str = this.displayName;
        return str == null ? "" : str;
    }

    public void setDisplayName(String str) {
        if (str == null) {
            str = "";
        }
        this.displayName = str;
    }
}
