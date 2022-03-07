package com.meishe.engine.bean;

import android.graphics.PointF;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meishe.engine.util.ColorUtil;
import com.meishe.engine.util.DeepCopyUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MeicamCompoundCaptionClip extends ClipInfo<NvsTimelineCompoundCaption> implements Cloneable, Serializable {
    private List<MeicamCompoundCaptionItem> mCompoundCaptionItems = new ArrayList();
    private float rotation = 0.0f;
    private float scaleX;
    private float scaleY;
    @SerializedName("styleId")
    private String styleDesc;
    private float translationX = 0.0f;
    private float translationY = 0.0f;
    private float zValue;

    public MeicamCompoundCaptionClip(String str) {
        super(CommonData.CLIP_COMPOUND_CAPTION);
        this.styleDesc = str;
    }

    public float getzValue() {
        return this.zValue;
    }

    public void setzValue(float f) {
        this.zValue = f;
    }

    public List<MeicamCompoundCaptionItem> getCompoundCaptionItems() {
        return this.mCompoundCaptionItems;
    }

    public void setCompoundCaptionItems(List<MeicamCompoundCaptionItem> list) {
        this.mCompoundCaptionItems = list;
    }

    public String getStyleDesc() {
        return this.styleDesc;
    }

    public void setStyleDesc(String str) {
        this.styleDesc = str;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(float f) {
        this.scaleX = f;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(float f) {
        this.scaleY = f;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float f) {
        this.rotation = f;
    }

    public float getTranslationX() {
        return this.translationX;
    }

    public void setTranslationX(float f) {
        this.translationX = f;
    }

    public float getTranslationY() {
        return this.translationY;
    }

    public void setTranslationY(float f) {
        this.translationY = f;
    }

    public void loadData(NvsTimelineCompoundCaption nvsTimelineCompoundCaption) {
        if (nvsTimelineCompoundCaption != null) {
            setObject(nvsTimelineCompoundCaption);
            setInPoint(nvsTimelineCompoundCaption.getInPoint());
            setOutPoint(nvsTimelineCompoundCaption.getOutPoint());
            int captionCount = nvsTimelineCompoundCaption.getCaptionCount();
            this.mCompoundCaptionItems.clear();
            for (int i = 0; i < captionCount; i++) {
                MeicamCompoundCaptionItem meicamCompoundCaptionItem = new MeicamCompoundCaptionItem(i, nvsTimelineCompoundCaption.getText(i));
                NvsColor textColor = nvsTimelineCompoundCaption.getTextColor(i);
                meicamCompoundCaptionItem.setTextColor(new float[]{textColor.r, textColor.g, textColor.b, textColor.a});
                meicamCompoundCaptionItem.setFont(nvsTimelineCompoundCaption.getFontFamily(i));
                this.mCompoundCaptionItems.add(meicamCompoundCaptionItem);
            }
            PointF captionTranslation = nvsTimelineCompoundCaption.getCaptionTranslation();
            if (captionTranslation != null) {
                this.translationX = captionTranslation.x;
                this.translationY = captionTranslation.y;
            }
            this.scaleX = nvsTimelineCompoundCaption.getScaleX();
            this.scaleY = nvsTimelineCompoundCaption.getScaleY();
            this.rotation = nvsTimelineCompoundCaption.getRotationZ();
            this.zValue = nvsTimelineCompoundCaption.getZValue();
        }
    }

    public NvsTimelineCompoundCaption bindToTimeline(NvsTimeline nvsTimeline) {
        if (nvsTimeline == null) {
            return null;
        }
        NvsTimelineCompoundCaption addCompoundCaption = nvsTimeline.addCompoundCaption(getInPoint(), getOutPoint() - getInPoint(), getStyleDesc());
        addCompoundCaption.setClipAffinityEnabled(false);
        setObject(addCompoundCaption);
        updateCompoundCaptionAttribute(addCompoundCaption, this);
        return addCompoundCaption;
    }

    public NvsTimelineCompoundCaption addCompoundCaptionFirst(NvsTimeline nvsTimeline, int i) {
        NvsTimelineCompoundCaption addCompoundCaption = nvsTimeline.addCompoundCaption(getInPoint(), getOutPoint() - getInPoint(), getStyleDesc());
        addCompoundCaption.setZValue((float) i);
        return addCompoundCaption;
    }

    private static void updateCompoundCaptionAttribute(NvsTimelineCompoundCaption nvsTimelineCompoundCaption, MeicamCompoundCaptionClip meicamCompoundCaptionClip) {
        if (!(nvsTimelineCompoundCaption == null || meicamCompoundCaptionClip == null)) {
            List<MeicamCompoundCaptionItem> compoundCaptionItems = meicamCompoundCaptionClip.getCompoundCaptionItems();
            int captionCount = nvsTimelineCompoundCaption.getCaptionCount();
            for (int i = 0; i < captionCount; i++) {
                MeicamCompoundCaptionItem meicamCompoundCaptionItem = compoundCaptionItems.get(i);
                if (meicamCompoundCaptionItem != null) {
                    nvsTimelineCompoundCaption.setTextColor(i, ColorUtil.colorFloatToNvsColor(meicamCompoundCaptionItem.getTextColor()));
                    String font = meicamCompoundCaptionItem.getFont();
                    if (!TextUtils.isEmpty(font)) {
                        nvsTimelineCompoundCaption.setFontFamily(i, font);
                    }
                    String text = meicamCompoundCaptionItem.getText();
                    if (!TextUtils.isEmpty(text)) {
                        nvsTimelineCompoundCaption.setText(i, text);
                    }
                }
            }
            float scaleX2 = meicamCompoundCaptionClip.getScaleX();
            float scaleY2 = meicamCompoundCaptionClip.getScaleY();
            nvsTimelineCompoundCaption.setScaleX(scaleX2);
            nvsTimelineCompoundCaption.setScaleY(scaleY2);
            nvsTimelineCompoundCaption.setRotationZ(meicamCompoundCaptionClip.getRotation());
            nvsTimelineCompoundCaption.setZValue(meicamCompoundCaptionClip.getzValue());
            nvsTimelineCompoundCaption.setCaptionTranslation(new PointF(meicamCompoundCaptionClip.getTranslationX(), meicamCompoundCaptionClip.getTranslationY()));
        }
    }

    @Override // java.lang.Object
    @NonNull
    public Object clone() {
        return DeepCopyUtil.deepClone(this);
    }
}
