package com.cdv.utils;

public class NvAndroidInterruptionChecker {
    private static final String TAG = "Meicam";
    private boolean m_interruptionFlag = false;

    public boolean isInterrupted() {
        return this.m_interruptionFlag;
    }

    public void setInterruptionFlag(boolean z) {
        this.m_interruptionFlag = z;
    }
}
