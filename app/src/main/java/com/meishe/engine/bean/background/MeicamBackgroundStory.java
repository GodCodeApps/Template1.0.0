package com.meishe.engine.bean.background;

public class MeicamBackgroundStory extends MeicamStoryboardInfo {
    private int backgroundType;

    public MeicamBackgroundStory() {
        this.classType = "BackgroundStory";
        this.subType = MeicamStoryboardInfo.SUB_TYPE_BACKGROUND;
    }

    public int getBackgroundType() {
        return this.backgroundType;
    }

    public void setBackgroundType(int i) {
        this.backgroundType = i;
    }
}
