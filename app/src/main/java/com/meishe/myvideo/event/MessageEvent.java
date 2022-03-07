package com.meishe.myvideo.event;

import com.meishe.myvideo.bean.BaseInfo;
import org.greenrobot.eventbus.EventBus;

public class MessageEvent {
    public static final int MESSAGE_APPLY_ALL_CAPTION_COLOR = 1049;
    public static final int MESSAGE_APPLY_ALL_CAPTION_FONT = 1051;
    public static final int MESSAGE_APPLY_ALL_CAPTION_LETTER_SPACE = 1052;
    public static final int MESSAGE_APPLY_ALL_CAPTION_OUT_LINE = 1050;
    public static final int MESSAGE_APPLY_ALL_CAPTION_POSITION = 1053;
    public static final int MESSAGE_APPLY_ALL_FILTER = 1048;
    public static final int MESSAGE_REMOVE_CLIP_FILTER = 1046;
    public static final int MESSAGE_REMOVE_TIMELINE_FILTER = 1047;
    public static final int MESSAGE_TYPE_ADJUST = 1026;
    public static final int MESSAGE_TYPE_ADJUST_TOUCH_STOP = 1038;
    public static final int MESSAGE_TYPE_ANIMATED_STICKER = 1001;
    public static final int MESSAGE_TYPE_APPLY_ALL_BACKGROUND_BLUR = 1029;
    public static final int MESSAGE_TYPE_APPLY_ALL_BACKGROUND_COLOR = 1027;
    public static final int MESSAGE_TYPE_APPLY_ALL_BACKGROUND_IMAGE = 1028;
    public static final int MESSAGE_TYPE_AUDIO_SPEED = 1030;
    public static final int MESSAGE_TYPE_AUDIO_TRANSTION = 1033;
    public static final int MESSAGE_TYPE_AUDIO_VOICE = 1032;
    public static final int MESSAGE_TYPE_AUDIO_VOLUME = 1031;
    public static final int MESSAGE_TYPE_BASE = 1000;
    public static final int MESSAGE_TYPE_CAPTION_COLOR = 1006;
    public static final int MESSAGE_TYPE_CAPTION_COLOR_OPACITY = 1010;
    public static final int MESSAGE_TYPE_CAPTION_FONT = 1011;
    public static final int MESSAGE_TYPE_CAPTION_FONT_BOLD = 1012;
    public static final int MESSAGE_TYPE_CAPTION_FONT_ITALICS = 1013;
    public static final int MESSAGE_TYPE_CAPTION_FONT_LETTER_SPACE = 1015;
    public static final int MESSAGE_TYPE_CAPTION_FONT_POSITION = 1016;
    public static final int MESSAGE_TYPE_CAPTION_FONT_SHADOW = 1014;
    public static final int MESSAGE_TYPE_CAPTION_OUT_LINE_COLOR = 1007;
    public static final int MESSAGE_TYPE_CAPTION_OUT_LINE_OPACITY = 1008;
    public static final int MESSAGE_TYPE_CAPTION_OUT_LINE_WIDTH = 1009;
    public static final int MESSAGE_TYPE_CAPTION_STYLE = 1005;
    public static final int MESSAGE_TYPE_CHANGE_ADJUST_FINISH = 1041;
    public static final int MESSAGE_TYPE_CHANGE_BACKGROUND_BLUR = 1021;
    public static final int MESSAGE_TYPE_CHANGE_BACKGROUND_COLOR = 1019;
    public static final int MESSAGE_TYPE_CHANGE_BACKGROUND_IMAGE = 1020;
    public static final int MESSAGE_TYPE_CHANGE_BLUR_BACKGROUND_UI = 1044;
    public static final int MESSAGE_TYPE_CHANGE_CLIP_FILTER_FINISH = 1040;
    public static final int MESSAGE_TYPE_CHANGE_CLIP_FILTER_PROGRESS = 1034;
    public static final int MESSAGE_TYPE_CHANGE_COLOR_BACKGROUND_UI = 1042;
    public static final int MESSAGE_TYPE_CHANGE_IMAGE_BACKGROUND_UI = 1043;
    public static final int MESSAGE_TYPE_CHANGE_SELECT_BACKGROUND_IMAGE = 1022;
    public static final int MESSAGE_TYPE_CHANGE_TIMELINE_FILTER_FINISH = 1039;
    public static final int MESSAGE_TYPE_CHANGE_TIMELINE_FILTER_PROGRESS = 1035;
    public static final int MESSAGE_TYPE_COMPOUND_CAPTION = 1017;
    public static final int MESSAGE_TYPE_CUSTOM_ANIMATED_STICKER = 1002;
    public static final int MESSAGE_TYPE_OPACITY = 1025;
    public static final int MESSAGE_TYPE_RESOURCE_DOWNLOAD_SUCCESS = 1018;
    public static final int MESSAGE_TYPE_SAVE_OPERATION = 1045;
    public static final int MESSAGE_TYPE_UPDATE_FILTER_PROGRESS = 1036;
    public static final int MESSAGE_TYPE_UPDATE_SELECT_POSITION = 1037;
    public static final int MESSAGE_TYPE_VIDEO_SPEED = 1054;
    public static final int MESSAGE_TYPE_VIDEO_SPEED_CONFORM = 1056;
    public static final int MESSAGE_TYPE_VIDEO_VOLUME = 1055;
    public static final int MESSAGE_TYPE_WATER = 1003;
    public static final int MESSAGE_TYPE_WATER_EFFECT = 1004;
    public static final int MESSAGE_TYPE_WIDTH_CONFIRM_EFFECT = 1023;
    public static final int MESSAGE_TYPE_WIDTH_CONFIRM_EFFECT_CONFIRM = 1024;
    private BaseInfo mBaseInfo;
    private boolean mBooleanValue;
    private int mEventType;
    private float mFloatValue;
    private int mIntValue;
    private int mProgress;
    private String mStrValue;

    public BaseInfo getBaseInfo() {
        return this.mBaseInfo;
    }

    public void setBaseInfo(BaseInfo baseInfo) {
        this.mBaseInfo = baseInfo;
    }

    public int getEventType() {
        return this.mEventType;
    }

    public void setEventType(int i) {
        this.mEventType = i;
    }

    public void setProgress(int i) {
        this.mProgress = i;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public String getStrValue() {
        return this.mStrValue;
    }

    public void setStrValue(String str) {
        this.mStrValue = str;
    }

    public boolean isBooleanValue() {
        return this.mBooleanValue;
    }

    public void setBooleanValue(boolean z) {
        this.mBooleanValue = z;
    }

    public float getFloatValue() {
        return this.mFloatValue;
    }

    public void setFloatValue(float f) {
        this.mFloatValue = f;
    }

    public int getIntValue() {
        return this.mIntValue;
    }

    public void setIntValue(int i) {
        this.mIntValue = i;
    }

    public static void sendEvent(float f, int i) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i);
        messageEvent.setFloatValue(f);
        EventBus.getDefault().post(messageEvent);
    }

    public static void sendEvent(int i, int i2) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i2);
        messageEvent.setIntValue(i);
        EventBus.getDefault().post(messageEvent);
    }

    public static void sendEvent(BaseInfo baseInfo, int i) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i);
        messageEvent.setBaseInfo(baseInfo);
        EventBus.getDefault().post(messageEvent);
    }

    public static void sendEvent(String str, int i) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i);
        messageEvent.setStrValue(str);
        EventBus.getDefault().post(messageEvent);
    }

    public static void sendEvent(int i) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i);
        EventBus.getDefault().post(messageEvent);
    }
}
