package com.meishe.engine.bean;

public class MeicamResource implements Cloneable {
    private int id;
    private String path;
    private String reversePath;

    public MeicamResource(int i, String str, String str2) {
        this.id = i;
        this.path = str;
        this.reversePath = str2;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getReversePath() {
        return this.reversePath;
    }

    public void setReversePath(String str) {
        this.reversePath = str;
    }
}
