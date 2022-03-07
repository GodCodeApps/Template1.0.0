package com.cdv.text;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import java.lang.Character;
import java.util.ArrayList;
import java.util.Iterator;

public class NvAndroidTextLayout {
    public static final int AlignBottom = 64;
    public static final int AlignHCenter = 4;
    public static final int AlignJustify = 8;
    public static final int AlignLeft = 1;
    public static final int AlignRight = 2;
    public static final int AlignTop = 32;
    public static final int AlignVCenter = 128;
    private static final String TAG = "NvAndroidTextLayout";
    private static final boolean m_verbose = false;
    private boolean m_glyphInfoPrepared = false;
    private ArrayList<ArrayList<GlyphInfo>> m_glyphLines;
    private RectF[] m_glyphLinesBounding;
    private StaticLayout m_layout;
    private String m_text;
    private RectF m_textBounding;
    private boolean m_verticalText = false;
    private float m_yOffset = 0.0f;

    public static class GlyphInfo {
        public RectF bounding;
        public boolean colorGlyph = false;
        public int endCharIdx;
        public Path glyphPath;
        public PointF pos;
        public int startCharIdx;
    }

    NvAndroidTextLayout(String str, Typeface typeface, TextPaint textPaint, float f, int i, boolean z, int i2, int i3, boolean z2) {
        this.m_verticalText = z2;
        if (str != null) {
            this.m_text = str;
            try {
                initLayout(typeface, textPaint, f, i, z, i2, i3);
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean isValid() {
        return this.m_layout != null;
    }

    public Layout getLayout() {
        return this.m_layout;
    }

    public String getText() {
        return this.m_text;
    }

    public RectF getBoundingRect() {
        prepareGlyphLines();
        RectF rectF = this.m_textBounding;
        if (rectF != null) {
            return rectF;
        }
        return new RectF();
    }

    public int getLineCount() {
        StaticLayout staticLayout = this.m_layout;
        if (staticLayout != null) {
            return staticLayout.getLineCount();
        }
        return 0;
    }

    public RectF getBoundingRectAtLine(int i) {
        RectF[] rectFArr;
        prepareGlyphLines();
        if (i < 0 || (rectFArr = this.m_glyphLinesBounding) == null || i >= rectFArr.length || rectFArr[i] == null) {
            return new RectF();
        }
        return rectFArr[i];
    }

    public int getGlyphCountInLine(int i) {
        ArrayList<ArrayList<GlyphInfo>> arrayList;
        prepareGlyphLines();
        if (i < 0 || (arrayList = this.m_glyphLines) == null || i >= arrayList.size() || this.m_glyphLines.get(i) == null) {
            return 0;
        }
        return this.m_glyphLines.get(i).size();
    }

    public GlyphInfo getGlyphInfo(int i, int i2) {
        ArrayList<ArrayList<GlyphInfo>> arrayList;
        prepareGlyphLines();
        if (i >= 0 && (arrayList = this.m_glyphLines) != null && i < arrayList.size() && this.m_glyphLines.get(i) != null) {
            ArrayList<GlyphInfo> arrayList2 = this.m_glyphLines.get(i);
            if (i2 >= 0 && i2 < arrayList2.size()) {
                return arrayList2.get(i2);
            }
        }
        return null;
    }

    public boolean prepareDrawText() {
        prepareGlyphLines();
        return true;
    }

    private void initLayout(Typeface typeface, TextPaint textPaint, float f, int i, boolean z, int i2, int i3) {
        StaticLayout staticLayout;
        int i4 = (!z || this.m_verticalText) ? 16777216 : i2;
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        if ((i & 4) != 0) {
            alignment = Layout.Alignment.ALIGN_CENTER;
        } else if ((i & 2) != 0) {
            alignment = Layout.Alignment.ALIGN_OPPOSITE;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            String str = this.m_text;
            StaticLayout.Builder obtain = StaticLayout.Builder.obtain(str, 0, str.length(), textPaint, i4);
            obtain.setAlignment(alignment);
            obtain.setLineSpacing(f, 1.0f);
            staticLayout = obtain.build();
        } else {
            staticLayout = new StaticLayout(this.m_text, textPaint, i4, alignment, 1.0f, f, false);
        }
        float height = (float) staticLayout.getHeight();
        if ((i & 64) != 0) {
            this.m_yOffset = ((float) i3) - height;
        } else if ((i & 128) != 0) {
            this.m_yOffset = (((float) i3) - height) / 2.0f;
        }
        this.m_layout = staticLayout;
        if (this.m_verticalText) {
            prepareGlyphLines();
            layoutVertically(z, f, i, (float) i2, (float) i3);
        }
    }

    /* access modifiers changed from: private */
    public static class VerticalLayoutContext {
        public int currentGlyphCountInLine;
        public float currentLineHeight;
        public boolean firstLine;
        public float height;
        public float width;
        public float xCenter;
        public float xOrigin;
        public float yOrigin;

        private VerticalLayoutContext() {
        }

        public void moveToNextLine(Paint.FontMetrics fontMetrics, float f, float f2) {
            this.height = Math.max(this.height, this.currentLineHeight);
            float f3 = f + f2;
            this.xOrigin -= f3;
            this.xCenter -= f3;
            this.yOrigin = -fontMetrics.ascent;
            this.currentLineHeight = 0.0f;
            this.currentGlyphCountInLine = 0;
            this.width += f;
            if (!this.firstLine) {
                this.width += f2;
            } else {
                this.firstLine = false;
            }
        }
    }

    private void layoutVertically(boolean z, float f, int i, float f2, float f3) {
        float f4;
        float f5;
        float f6;
        TextPaint paint = this.m_layout.getPaint();
        float measureText = paint.measureText("X") * 2.0f;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float f7 = (-fontMetrics.ascent) + fontMetrics.descent;
        if (Build.VERSION.SDK_INT >= 21) {
            f7 += paint.getLetterSpacing();
        }
        VerticalLayoutContext verticalLayoutContext = new VerticalLayoutContext();
        verticalLayoutContext.firstLine = true;
        verticalLayoutContext.currentGlyphCountInLine = 0;
        verticalLayoutContext.currentLineHeight = 0.0f;
        verticalLayoutContext.width = 0.0f;
        verticalLayoutContext.height = 0.0f;
        verticalLayoutContext.xOrigin = f2 - measureText;
        verticalLayoutContext.xCenter = f2 - (measureText / 2.0f);
        verticalLayoutContext.yOrigin = -fontMetrics.ascent;
        float f8 = 1.6777216E7f;
        if (z) {
            f8 = Math.max(0.0f, f3);
        }
        ArrayList<ArrayList<GlyphInfo>> arrayList = new ArrayList<>();
        ArrayList<GlyphInfo> arrayList2 = new ArrayList<>();
        Iterator<ArrayList<GlyphInfo>> it = this.m_glyphLines.iterator();
        while (it.hasNext()) {
            Iterator<GlyphInfo> it2 = it.next().iterator();
            while (it2.hasNext()) {
                GlyphInfo next = it2.next();
                if (z && verticalLayoutContext.currentGlyphCountInLine != 0 && next.bounding.bottom + verticalLayoutContext.yOrigin > f8) {
                    verticalLayoutContext.moveToNextLine(fontMetrics, measureText, f);
                    arrayList.add(arrayList2);
                    arrayList2 = new ArrayList<>();
                }
                float f9 = verticalLayoutContext.xOrigin;
                float centerX = f9 + (verticalLayoutContext.xCenter - ((next.bounding.centerX() - next.pos.x) + f9));
                float f10 = verticalLayoutContext.yOrigin;
                float f11 = centerX - next.pos.x;
                float f12 = f10 - next.pos.y;
                next.pos.x = centerX;
                next.pos.y = f10;
                next.bounding.offset(f11, f12);
                if (next.glyphPath != null) {
                    next.glyphPath.offset(f11, f12);
                }
                verticalLayoutContext.currentLineHeight = next.bounding.bottom;
                verticalLayoutContext.yOrigin += f7;
                arrayList2.add(next);
                verticalLayoutContext.currentGlyphCountInLine++;
                f8 = f8;
                it = it;
            }
            verticalLayoutContext.moveToNextLine(fontMetrics, measureText, f);
            arrayList.add(arrayList2);
            arrayList2 = new ArrayList<>();
        }
        this.m_glyphLines = arrayList;
        if ((i & 64) != 0) {
            f4 = verticalLayoutContext.width - f2;
        } else {
            f4 = (i & 128) != 0 ? (verticalLayoutContext.width - f2) / 2.0f : 0.0f;
        }
        int i2 = i & 2;
        if (i2 != 0) {
            f5 = f3 - verticalLayoutContext.height;
        } else {
            f5 = (i & 4) != 0 ? (f3 - verticalLayoutContext.height) / 2.0f : 0.0f;
        }
        this.m_textBounding = new RectF();
        this.m_glyphLinesBounding = new RectF[this.m_glyphLines.size()];
        int size = this.m_glyphLines.size();
        for (int i3 = 0; i3 < size; i3++) {
            ArrayList<GlyphInfo> arrayList3 = this.m_glyphLines.get(i3);
            float f13 = !arrayList3.isEmpty() ? arrayList3.get(arrayList3.size() - 1).bounding.bottom : 0.0f;
            if (i2 != 0) {
                f6 = f5 + (verticalLayoutContext.height - f13);
            } else {
                f6 = (i & 4) != 0 ? ((verticalLayoutContext.height - f13) / 2.0f) + f5 : f5;
            }
            RectF rectF = new RectF();
            Iterator<GlyphInfo> it3 = arrayList3.iterator();
            while (it3.hasNext()) {
                GlyphInfo next2 = it3.next();
                next2.pos.x += f4;
                next2.pos.y += f6;
                next2.bounding.offset(f4, f6);
                if (next2.glyphPath != null) {
                    next2.glyphPath.offset(f4, f6);
                }
                if (!rectF.isEmpty()) {
                    rectF.union(next2.bounding);
                } else {
                    rectF.set(next2.bounding);
                }
            }
            this.m_glyphLinesBounding[i3] = rectF;
            if (!this.m_textBounding.isEmpty()) {
                this.m_textBounding.union(rectF);
            } else {
                this.m_textBounding.set(rectF);
            }
        }
    }

    private void prepareGlyphLines() {
        int i;
        GlyphInfo glyphInfo;
        if (!this.m_glyphInfoPrepared) {
            int i2 = 1;
            this.m_glyphInfoPrepared = true;
            if (this.m_layout != null) {
                this.m_textBounding = new RectF();
                int lineCount = this.m_layout.getLineCount();
                this.m_glyphLinesBounding = new RectF[lineCount];
                this.m_glyphLines = new ArrayList<>();
                boolean z = false;
                int i3 = 0;
                while (i3 < lineCount) {
                    this.m_glyphLinesBounding[i3] = new RectF();
                    ArrayList<GlyphInfo> arrayList = new ArrayList<>();
                    this.m_glyphLines.add(arrayList);
                    int lineBaseline = this.m_layout.getLineBaseline(i3);
                    int lineStart = this.m_layout.getLineStart(i3);
                    int lineEnd = this.m_layout.getLineEnd(i3);
                    int i4 = lineEnd - lineStart;
                    float[] fArr = new float[i4];
                    if (this.m_layout.getPaint().getTextWidths(this.m_text, lineStart, lineEnd, fArr) != i4) {
                        Log.w(TAG, "Paint.getTextWidths() return unexpected value!");
                    }
                    int i5 = lineStart;
                    while (i5 < lineEnd) {
                        int i6 = i5 + 1;
                        while (i6 < lineEnd && fArr[i6 - lineStart] == 0.0f) {
                            i6++;
                        }
                        if (i6 - i5 == i2 && this.m_text.charAt(i5) == '\n') {
                            break;
                        }
                        GlyphInfo glyphInfo2 = new GlyphInfo();
                        glyphInfo2.startCharIdx = i5;
                        glyphInfo2.endCharIdx = i6;
                        glyphInfo2.colorGlyph = z;
                        glyphInfo2.pos = new PointF(this.m_layout.getPrimaryHorizontal(i5), ((float) lineBaseline) + this.m_yOffset);
                        glyphInfo2.bounding = new RectF();
                        if (!glyphInfo2.colorGlyph) {
                            glyphInfo2.glyphPath = new Path();
                            i = lineCount;
                            glyphInfo = glyphInfo2;
                            this.m_layout.getPaint().getTextPath(this.m_text, i5, i6, glyphInfo2.pos.x, glyphInfo2.pos.y, glyphInfo2.glyphPath);
                            if (!glyphInfo.glyphPath.isEmpty()) {
                                glyphInfo.glyphPath.computeBounds(glyphInfo.bounding, false);
                            } else if (isSuspectableColorGlyph(this.m_text, i5, i6)) {
                                i2 = 1;
                                glyphInfo.colorGlyph = true;
                            }
                            i2 = 1;
                        } else {
                            i = lineCount;
                            glyphInfo = glyphInfo2;
                            i2 = 1;
                        }
                        if (glyphInfo.colorGlyph) {
                            Rect rect = new Rect();
                            this.m_layout.getPaint().getTextBounds(this.m_text, i5, i6, rect);
                            glyphInfo.bounding.set(rect);
                            glyphInfo.bounding.offset(glyphInfo.pos.x, glyphInfo.pos.y);
                        }
                        arrayList.add(glyphInfo);
                        if (this.m_glyphLinesBounding[i3].isEmpty()) {
                            this.m_glyphLinesBounding[i3].set(glyphInfo.bounding);
                        } else if (!glyphInfo.bounding.isEmpty()) {
                            this.m_glyphLinesBounding[i3].union(glyphInfo.bounding);
                        }
                        i5 = i6;
                        lineCount = i;
                        z = false;
                    }
                    if (this.m_textBounding.isEmpty()) {
                        this.m_textBounding.set(this.m_glyphLinesBounding[i3]);
                    } else if (!this.m_glyphLinesBounding[i3].isEmpty()) {
                        this.m_textBounding.union(this.m_glyphLinesBounding[i3]);
                    }
                    i3++;
                    lineCount = lineCount;
                    z = false;
                }
            }
        }
    }

    private boolean isSuspectableColorGlyph(String str, int i, int i2) {
        while (i < i2) {
            char charAt = str.charAt(i);
            if (!(charAt == 8205 || charAt == 8204)) {
                if (!Character.isHighSurrogate(charAt)) {
                    Character.UnicodeBlock of = Character.UnicodeBlock.of(charAt);
                    if (!(of == Character.UnicodeBlock.VARIATION_SELECTORS || of == Character.UnicodeBlock.ARROWS || of == Character.UnicodeBlock.BASIC_LATIN || of == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || of == Character.UnicodeBlock.COMBINING_MARKS_FOR_SYMBOLS || of == Character.UnicodeBlock.DINGBATS || of == Character.UnicodeBlock.ENCLOSED_ALPHANUMERICS || of == Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS || of == Character.UnicodeBlock.GENERAL_PUNCTUATION || of == Character.UnicodeBlock.GEOMETRIC_SHAPES || of == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || of == Character.UnicodeBlock.LATIN_1_SUPPLEMENT || of == Character.UnicodeBlock.LETTERLIKE_SYMBOLS || of == Character.UnicodeBlock.MISCELLANEOUS_TECHNICAL || of == Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS || of == Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_ARROWS || of == Character.UnicodeBlock.SUPPLEMENTAL_ARROWS_B)) {
                        return false;
                    }
                } else if (i >= i2 - 1) {
                    return false;
                } else {
                    i++;
                    char charAt2 = str.charAt(i);
                    if (!Character.isLowSurrogate(charAt2)) {
                        return false;
                    }
                    int codePoint = Character.toCodePoint(charAt, charAt2);
                    Character.UnicodeBlock of2 = Character.UnicodeBlock.of(codePoint);
                    if (!(of2 == Character.UnicodeBlock.VARIATION_SELECTORS_SUPPLEMENT || of2 == Character.UnicodeBlock.TAGS || ((codePoint >= 128512 && codePoint <= 128591) || ((codePoint >= 127744 && codePoint <= 128511) || ((codePoint >= 129280 && codePoint <= 129535) || ((codePoint >= 128640 && codePoint <= 128767) || ((codePoint >= 126976 && codePoint <= 127023) || ((codePoint >= 127136 && codePoint <= 127231) || ((codePoint >= 127232 && codePoint <= 127487) || ((codePoint >= 127488 && codePoint <= 127743) || ((codePoint >= 128896 && codePoint <= 129023) || (codePoint >= 129648 && codePoint <= 129791)))))))))))) {
                        return false;
                    }
                }
            }
            i++;
        }
        return true;
    }
}
