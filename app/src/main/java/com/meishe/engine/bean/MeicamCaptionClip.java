package com.meishe.engine.bean;

import android.graphics.PointF;
import android.util.Log;
import androidx.annotation.NonNull;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineCaption;
import com.meishe.engine.util.ColorUtil;
import com.meishe.engine.util.DeepCopyUtil;
import java.io.Serializable;

public class MeicamCaptionClip extends ClipInfo<NvsTimelineCaption> implements Cloneable, Serializable {
    private static final String TAG = "MeicamCaptionClip";
    private boolean bold;
    private String font;
    private boolean italic;
    private float letterSpacing;
    private boolean outline;
    private float[] outlineColor;
    private float outlineWidth;
    private float rotation;
    private float scaleX;
    private float scaleY;
    private boolean shadow;
    private String styleId;
    private String subType;
    private String text;
    private int textAlign;
    private float[] textColor;
    private float translationX;
    private float translationY;
    private float zValue;

    public MeicamCaptionClip() {
        super(CommonData.CLIP_CAPTION);
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.rotation = 0.0f;
        this.translationX = 0.0f;
        this.translationY = 0.0f;
        this.font = "";
        this.textColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        this.bold = false;
        this.italic = false;
        this.shadow = false;
        this.outline = false;
        this.outlineColor = new float[4];
        this.outlineWidth = 5.0f;
        this.letterSpacing = 100.0f;
        this.subType = "general";
    }

    public MeicamCaptionClip(String str, String str2) {
        super(CommonData.CLIP_CAPTION);
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.rotation = 0.0f;
        this.translationX = 0.0f;
        this.translationY = 0.0f;
        this.font = "";
        this.textColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        this.bold = false;
        this.italic = false;
        this.shadow = false;
        this.outline = false;
        this.outlineColor = new float[4];
        this.outlineWidth = 5.0f;
        this.letterSpacing = 100.0f;
        this.subType = "general";
        this.text = str;
        this.styleId = str2;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getStyleId() {
        return this.styleId;
    }

    public void setStyleId(String str) {
        this.styleId = str;
    }

    @Override // com.meishe.engine.bean.ClipInfo
    public String getType() {
        return this.type;
    }

    @Override // com.meishe.engine.bean.ClipInfo
    public void setType(String str) {
        this.type = str;
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

    public String getFont() {
        return this.font;
    }

    public void setFont(String str) {
        this.font = str;
    }

    public float[] getTextColor() {
        return this.textColor;
    }

    public void setTextColor(float[] fArr) {
        this.textColor = fArr;
    }

    public boolean isBold() {
        return this.bold;
    }

    public void setBold(boolean z) {
        this.bold = z;
    }

    public boolean isItalic() {
        return this.italic;
    }

    public void setItalic(boolean z) {
        this.italic = z;
    }

    public boolean isShadow() {
        return this.shadow;
    }

    public void setShadow(boolean z) {
        this.shadow = z;
    }

    public boolean isOutline() {
        return this.outline;
    }

    public void setOutline(boolean z) {
        this.outline = z;
    }

    public float[] getOutlineColor() {
        return this.outlineColor;
    }

    public void setOutlineColor(float[] fArr) {
        this.outlineColor = fArr;
    }

    public float getOutlineWidth() {
        return this.outlineWidth;
    }

    public void setOutlineWidth(float f) {
        this.outlineWidth = f;
    }

    public float getzValue() {
        return this.zValue;
    }

    public void setzValue(float f) {
        this.zValue = f;
    }

    public float getLetterSpacing() {
        return this.letterSpacing;
    }

    public void setLetterSpacing(float f) {
        this.letterSpacing = f;
    }

    public int getTextAlign() {
        return this.textAlign;
    }

    public void setTextAlign(int i) {
        this.textAlign = i;
    }

    public NvsTimelineCaption bindToTimeline(NvsTimeline nvsTimeline) {
        return bindToTimeline(nvsTimeline, true);
    }

    public NvsTimelineCaption bindToTimeline(NvsTimeline nvsTimeline, boolean z) {
        NvsTimelineCaption addCaption = nvsTimeline.addCaption(getText(), getInPoint(), getOutPoint() - getInPoint(), null);
        if (addCaption == null) {
            return null;
        }
        addCaption.setClipAffinityEnabled(false);
        setObject(addCaption);
        Log.e(TAG, "bindToTimeline: " + addCaption.getTextColor().r + "  " + addCaption.getTextColor().g + "   " + addCaption.getTextColor().b + "   " + addCaption.getTextColor().a);
        if (z) {
            updateCaptionAttribute(addCaption, this);
        }
        return addCaption;
    }

    private static void updateCaptionAttribute(NvsTimelineCaption nvsTimelineCaption, MeicamCaptionClip meicamCaptionClip) {
        NvsColor colorFloatToNvsColor;
        if (nvsTimelineCaption != null && meicamCaptionClip != null) {
            nvsTimelineCaption.applyCaptionStyle(meicamCaptionClip.getStyleId());
            int textAlign2 = meicamCaptionClip.getTextAlign();
            if (textAlign2 >= 0) {
                nvsTimelineCaption.setTextAlignment(textAlign2);
            }
            NvsColor colorFloatToNvsColor2 = ColorUtil.colorFloatToNvsColor(meicamCaptionClip.getTextColor());
            if (colorFloatToNvsColor2 != null) {
                nvsTimelineCaption.setTextColor(colorFloatToNvsColor2);
            }
            float scaleX2 = meicamCaptionClip.getScaleX();
            float scaleY2 = meicamCaptionClip.getScaleY();
            nvsTimelineCaption.setScaleX(scaleX2);
            nvsTimelineCaption.setScaleY(scaleY2);
            nvsTimelineCaption.setRotationZ(meicamCaptionClip.getRotation());
            nvsTimelineCaption.setZValue(meicamCaptionClip.getzValue());
            boolean isOutline = meicamCaptionClip.isOutline();
            nvsTimelineCaption.setDrawOutline(isOutline);
            if (isOutline && (colorFloatToNvsColor = ColorUtil.colorFloatToNvsColor(meicamCaptionClip.getOutlineColor())) != null) {
                nvsTimelineCaption.setOutlineColor(colorFloatToNvsColor);
                nvsTimelineCaption.setOutlineWidth(meicamCaptionClip.getOutlineWidth());
            }
            String font2 = meicamCaptionClip.getFont();
            if (!font2.isEmpty()) {
                nvsTimelineCaption.setFontByFilePath(font2);
            }
            nvsTimelineCaption.setBold(meicamCaptionClip.isBold());
            nvsTimelineCaption.setItalic(meicamCaptionClip.isItalic());
            nvsTimelineCaption.setDrawShadow(meicamCaptionClip.isShadow());
            nvsTimelineCaption.setCaptionTranslation(new PointF(meicamCaptionClip.getTranslationX(), meicamCaptionClip.getTranslationY()));
            nvsTimelineCaption.setLetterSpacing(meicamCaptionClip.getLetterSpacing());
        }
    }

    public void loadData(NvsTimelineCaption nvsTimelineCaption) {
        if (nvsTimelineCaption != null) {
            setObject(nvsTimelineCaption);
            setInPoint(nvsTimelineCaption.getInPoint());
            setOutPoint(nvsTimelineCaption.getOutPoint());
            this.text = nvsTimelineCaption.getText();
            this.styleId = nvsTimelineCaption.getCaptionStylePackageId();
            NvsColor textColor2 = nvsTimelineCaption.getTextColor();
            this.textColor[0] = textColor2.r;
            this.textColor[1] = textColor2.g;
            this.textColor[2] = textColor2.b;
            this.textColor[3] = textColor2.a;
            PointF captionTranslation = nvsTimelineCaption.getCaptionTranslation();
            this.translationX = captionTranslation.x;
            this.translationY = captionTranslation.y;
            this.scaleX = nvsTimelineCaption.getScaleX();
            this.scaleY = nvsTimelineCaption.getScaleY();
            this.rotation = nvsTimelineCaption.getRotationZ();
            this.letterSpacing = nvsTimelineCaption.getLetterSpacing();
            this.font = nvsTimelineCaption.getFontFamily();
            this.bold = nvsTimelineCaption.getBold();
            this.italic = nvsTimelineCaption.getItalic();
            this.shadow = nvsTimelineCaption.getDrawShadow();
            this.outline = nvsTimelineCaption.getDrawOutline();
            this.zValue = (float) ((int) nvsTimelineCaption.getZValue());
            this.textAlign = nvsTimelineCaption.getTextAlignment();
            NvsColor outlineColor2 = nvsTimelineCaption.getOutlineColor();
            float[] fArr = this.outlineColor;
            if (fArr != null) {
                fArr[0] = outlineColor2.r;
                this.outlineColor[0] = outlineColor2.g;
                this.outlineColor[0] = outlineColor2.b;
                this.outlineColor[0] = outlineColor2.a;
            }
            this.outlineWidth = nvsTimelineCaption.getOutlineWidth();
        }
    }

    @Override // java.lang.Object
    @NonNull
    public Object clone() {
        return DeepCopyUtil.deepClone(this);
    }
}
