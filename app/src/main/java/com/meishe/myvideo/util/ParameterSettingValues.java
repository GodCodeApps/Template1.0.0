package com.meishe.myvideo.util;

import java.io.Serializable;

public class ParameterSettingValues implements Serializable {
    private static ParameterSettingValues parameterValues = null;
    public static final int videoRes1080 = 1080;
    public static final int videoRes720 = 720;
    private boolean isAutoAppendVideo;
    private int m_captureResolutionGrade = 2;
    private double m_compileBitrate = 0.0d;
    private int m_compileVideoRes = videoRes1080;
    private boolean m_disableDeviceEncorder = false;
    private boolean m_isUseBackgroundBlur = false;

    public int getCompileVideoRes() {
        return this.m_compileVideoRes;
    }

    public void setCompileVideoRes(int i) {
        this.m_compileVideoRes = i;
    }

    public static ParameterSettingValues instance() {
        if (parameterValues == null) {
            parameterValues = init();
        }
        return getParameterValues();
    }

    public static ParameterSettingValues init() {
        if (parameterValues == null) {
            synchronized (ParameterSettingValues.class) {
                if (parameterValues == null) {
                    parameterValues = new ParameterSettingValues();
                }
            }
        }
        return parameterValues;
    }

    public static void setParameterValues(ParameterSettingValues parameterSettingValues) {
        parameterValues = parameterSettingValues;
    }

    public static ParameterSettingValues getParameterValues() {
        return parameterValues;
    }

    public double getCompileBitrate() {
        return this.m_compileBitrate;
    }

    public void setCompileBitrate(double d) {
        this.m_compileBitrate = d;
    }

    public boolean disableDeviceEncorder() {
        return this.m_disableDeviceEncorder;
    }

    public void setDisableDeviceEncorder(boolean z) {
        this.m_disableDeviceEncorder = z;
    }

    public boolean isUseBackgroudBlur() {
        return this.m_isUseBackgroundBlur;
    }

    public void setUseBackgroudBlur(boolean z) {
        this.m_isUseBackgroundBlur = z;
    }

    public boolean isAutoAppendVideo() {
        return this.isAutoAppendVideo;
    }

    public void setAutoAppendVideo(boolean z) {
        this.isAutoAppendVideo = z;
    }
}
