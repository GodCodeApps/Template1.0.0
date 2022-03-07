package com.meicam.sdk;

import android.graphics.PointF;
import com.meicam.sdk.NvsTimelineCaption;
import java.util.List;

public class NvsTimelineCompoundCaption extends NvsFx {
    public static final int BOUNDING_TYPE_FRAME = 2;
    public static final int BOUNDING_TYPE_TEXT = 0;
    public static final int BOUNDING_TYPE_TEXT_FRAME = 1;

    private native long nativeChangeInPoint(long j, long j2);

    private native long nativeChangeOutPoint(long j, long j2);

    private native PointF nativeGetAnchorPoint(long j);

    private native List<PointF> nativeGetCaptionBoundingVertices(long j, int i, int i2, NvsTimelineCaption.MotionParameters motionParameters);

    private native int nativeGetCaptionCount(long j);

    private native PointF nativeGetCaptionTranslation(long j);

    private native boolean nativeGetClipAffinityEnabled(long j);

    private native List<PointF> nativeGetCompoundBoundingVertices(long j, int i, NvsTimelineCaption.MotionParameters motionParameters);

    private native String nativeGetFontFamily(long j, int i);

    private native long nativeGetInPoint(long j);

    private native float nativeGetOpacity(long j);

    private native long nativeGetOutPoint(long j);

    private native float nativeGetRotationZ(long j);

    private native float nativeGetScaleX(long j);

    private native float nativeGetScaleY(long j);

    private native String nativeGetText(long j, int i);

    private native NvsColor nativeGetTextColor(long j, int i);

    private native float nativeGetZValue(long j);

    private native void nativeMovePosition(long j, long j2);

    private native void nativeRotateCaption(long j, float f, PointF pointF);

    private native void nativeScaleCaption(long j, float f, PointF pointF);

    private native void nativeSetAnchorPoint(long j, PointF pointF);

    private native void nativeSetCaptionTranslation(long j, PointF pointF);

    private native void nativeSetClipAffinityEnabled(long j, boolean z);

    private native void nativeSetFontFamily(long j, int i, String str);

    private native void nativeSetOpacity(long j, float f);

    private native void nativeSetRotationZ(long j, float f);

    private native void nativeSetScaleX(long j, float f);

    private native void nativeSetScaleY(long j, float f);

    private native void nativeSetText(long j, int i, String str);

    private native void nativeSetTextColor(long j, int i, NvsColor nvsColor);

    private native void nativeSetZValue(long j, float f);

    private native void nativeTranslateCaption(long j, PointF pointF);

    public int getCaptionCount() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCaptionCount(this.m_internalObject);
    }

    public void setClipAffinityEnabled(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetClipAffinityEnabled(this.m_internalObject, z);
    }

    public boolean getClipAffinityEnabled() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetClipAffinityEnabled(this.m_internalObject);
    }

    public long getInPoint() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetInPoint(this.m_internalObject);
    }

    public long getOutPoint() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetOutPoint(this.m_internalObject);
    }

    public long changeInPoint(long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeChangeInPoint(this.m_internalObject, j);
    }

    public long changeOutPoint(long j) {
        NvsUtils.checkFunctionInMainThread();
        return nativeChangeOutPoint(this.m_internalObject, j);
    }

    public void movePosition(long j) {
        NvsUtils.checkFunctionInMainThread();
        nativeMovePosition(this.m_internalObject, j);
    }

    public void setText(int i, String str) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetText(this.m_internalObject, i, str);
    }

    public String getText(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetText(this.m_internalObject, i);
    }

    public void setFontFamily(int i, String str) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetFontFamily(this.m_internalObject, i, str);
    }

    public String getFontFamily(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetFontFamily(this.m_internalObject, i);
    }

    public void setTextColor(int i, NvsColor nvsColor) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetTextColor(this.m_internalObject, i, nvsColor);
    }

    public NvsColor getTextColor(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetTextColor(this.m_internalObject, i);
    }

    public void setCaptionTranslation(PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetCaptionTranslation(this.m_internalObject, pointF);
    }

    public PointF getCaptionTranslation() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCaptionTranslation(this.m_internalObject);
    }

    public void translateCaption(PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        nativeTranslateCaption(this.m_internalObject, pointF);
    }

    public void setAnchorPoint(PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetAnchorPoint(this.m_internalObject, pointF);
    }

    public PointF getAnchorPoint() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetAnchorPoint(this.m_internalObject);
    }

    public void setScaleX(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetScaleX(this.m_internalObject, f);
    }

    public float getScaleX() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetScaleX(this.m_internalObject);
    }

    public void setScaleY(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetScaleY(this.m_internalObject, f);
    }

    public float getScaleY() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetScaleY(this.m_internalObject);
    }

    public void scaleCaption(float f, PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        nativeScaleCaption(this.m_internalObject, f, pointF);
    }

    public void setRotationZ(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetRotationZ(this.m_internalObject, f);
    }

    public float getRotationZ() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetRotationZ(this.m_internalObject);
    }

    public void rotateCaption(float f, PointF pointF) {
        NvsUtils.checkFunctionInMainThread();
        nativeRotateCaption(this.m_internalObject, f, pointF);
    }

    public void rotateCaptionAroundCenter(float f, int i) {
        List<PointF> compoundBoundingVertices = getCompoundBoundingVertices(i);
        if (compoundBoundingVertices != null && compoundBoundingVertices.size() == 4) {
            float f2 = 0.0f;
            float f3 = 0.0f;
            for (int i2 = 0; i2 < 4; i2++) {
                PointF pointF = compoundBoundingVertices.get(i2);
                f2 += pointF.x;
                f3 += pointF.y;
            }
            rotateCaption(f, new PointF(f2 / 4.0f, f3 / 4.0f));
        }
    }

    public List<PointF> getCaptionBoundingVertices(int i, int i2) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCaptionBoundingVertices(this.m_internalObject, i, i2, null);
    }

    public List<PointF> getCompoundBoundingVertices(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCompoundBoundingVertices(this.m_internalObject, i, null);
    }

    public List<PointF> getCaptionBoundingVertices(int i, int i2, NvsTimelineCaption.MotionParameters motionParameters) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCaptionBoundingVertices(this.m_internalObject, i, i2, motionParameters);
    }

    public List<PointF> getCompoundBoundingVertices(int i, NvsTimelineCaption.MotionParameters motionParameters) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCompoundBoundingVertices(this.m_internalObject, i, motionParameters);
    }

    public void setZValue(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetZValue(this.m_internalObject, f);
    }

    public float getZValue() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetZValue(this.m_internalObject);
    }

    public void setOpacity(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetOpacity(this.m_internalObject, f);
    }

    public float getOpacity() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetOpacity(this.m_internalObject);
    }
}
