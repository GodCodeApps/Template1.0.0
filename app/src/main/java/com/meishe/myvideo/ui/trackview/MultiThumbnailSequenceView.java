package com.meishe.myvideo.ui.trackview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsIconGenerator;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsUtils;
import com.meishe.engine.bean.CommonData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MultiThumbnailSequenceView extends HorizontalScrollView implements NvsIconGenerator.IconCallback {
    private static final String TAG = "Meicam";
    public static final int THUMBNAIL_IMAGE_FILLMODE_ASPECTCROP = 1;
    public static final int THUMBNAIL_IMAGE_FILLMODE_STRETCH = 0;
    private static final int THUMBNAIL_SEQUENCE_FLAGS_CACHED_KEYFRAME_ONLY = 1;
    private static final int THUMBNAIL_SEQUENCE_FLAGS_CACHED_KEYFRAME_ONLY_VALID = 2;
    private Handler mHandler;
    private ContentView m_contentView;
    private int m_contentWidth = 0;
    private ArrayList<ThumbnailSequenceDesc> m_descArray;
    private int m_endPadding = 0;
    private NvsIconGenerator m_iconGenerator = null;
    private int m_maxThumbnailWidth = 0;
    private long m_maxTimelinePosToScroll = 0;
    private double m_pixelPerMicrosecond = 7.2E-5d;
    Bitmap m_placeholderBitmap;
    private OnScrollChangeListener m_scrollChangeListener;
    private boolean m_scrollEnabled = true;
    private int m_startPadding = 0;
    private float m_thumbnailAspectRatio = 0.5625f;
    private int m_thumbnailImageFillMode = 0;
    private TreeMap<ThumbnailId, Thumbnail> m_thumbnailMap = new TreeMap<>();
    private ArrayList<ThumbnailSequence> m_thumbnailSequenceArray = new ArrayList<>();
    private TreeMap<Integer, ThumbnailSequence> m_thumbnailSequenceMap = new TreeMap<>();
    private boolean m_updatingThumbnail = false;

    public interface OnScrollChangeListener {
        void onScrollChanged(MultiThumbnailSequenceView multiThumbnailSequenceView, int i, int i2);
    }

    /* access modifiers changed from: private */
    public static class Thumbnail {
        long m_iconTaskId = 0;
        ImageView m_imageView;
        boolean m_imageViewUpToDate = false;
        ThumbnailSequence m_owner;
        long m_timestamp = 0;
        boolean m_touched = false;
    }

    public static class ThumbnailSequenceDesc {
        public long inPoint = 0;
        public String mediaFilePath;
        public boolean onlyDecodeKeyFrame = false;
        public long outPoint = CommonData.DEFAULT_LENGTH;
        public boolean stillImageHint = false;
        public float thumbnailAspectRatio = 0.0f;
        public long trimIn = 0;
        public long trimOut = CommonData.DEFAULT_LENGTH;
    }

    /* access modifiers changed from: private */
    public static class ThumbnailSequence {
        int m_flags = 0;
        long m_inPoint = 0;
        int m_index = 0;
        String m_mediaFilePath;
        boolean m_onlyDecodeKeyFrame = false;
        long m_outPoint = 0;
        boolean m_stillImageHint = false;
        public float m_thumbnailAspectRatio = 0.0f;
        int m_thumbnailWidth = 0;
        long m_trimDuration = 0;
        long m_trimIn = 0;
        int m_width = 0;
        int m_x = 0;

        public long calcTimestampFromX(int i, long j) {
            long j2 = this.m_trimIn;
            double d = (double) (i - this.m_x);
            double d2 = (double) this.m_width;
            Double.isNaN(d);
            Double.isNaN(d2);
            double d3 = d / d2;
            double d4 = (double) this.m_trimDuration;
            Double.isNaN(d4);
            double d5 = (double) (j2 + ((long) ((d3 * d4) + 0.5d)));
            double d6 = (double) j;
            Double.isNaN(d5);
            Double.isNaN(d6);
            return ((long) (d5 / d6)) * j;
        }
    }

    /* access modifiers changed from: private */
    public static class ThumbnailId implements Comparable<ThumbnailId> {
        public int m_seqIndex;
        public long m_timestamp;

        public ThumbnailId(int i, long j) {
            this.m_seqIndex = i;
            this.m_timestamp = j;
        }

        public int compareTo(ThumbnailId thumbnailId) {
            int i = this.m_seqIndex;
            int i2 = thumbnailId.m_seqIndex;
            if (i < i2) {
                return -1;
            }
            if (i > i2) {
                return 1;
            }
            long j = this.m_timestamp;
            long j2 = thumbnailId.m_timestamp;
            if (j < j2) {
                return -1;
            }
            if (j > j2) {
                return 1;
            }
            return 0;
        }
    }

    /* access modifiers changed from: private */
    public class ContentView extends ViewGroup {
        public boolean shouldDelayChildPressedState() {
            return false;
        }

        public ContentView(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            int i3 = MultiThumbnailSequenceView.this.m_contentWidth;
            int mode = View.MeasureSpec.getMode(i2);
            int size = View.MeasureSpec.getSize(i2);
            if (!(mode == 1073741824 || mode == Integer.MIN_VALUE)) {
                size = MultiThumbnailSequenceView.this.getHeight();
            }
            setMeasuredDimension(resolveSizeAndState(Math.max(i3, getSuggestedMinimumWidth()), i, 0), resolveSizeAndState(Math.max(size, getSuggestedMinimumHeight()), i2, 0));
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            MultiThumbnailSequenceView.this.updateThumbnails();
        }

        /* access modifiers changed from: protected */
        public void onSizeChanged(int i, int i2, int i3, int i4) {
            if (i2 != i4) {
                MultiThumbnailSequenceView.this.requestUpdateThumbnailSequenceGeometry();
            }
            super.onSizeChanged(i, i2, i3, i4);
        }
    }

    public MultiThumbnailSequenceView(Context context) {
        super(context);
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public MultiThumbnailSequenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public MultiThumbnailSequenceView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public MultiThumbnailSequenceView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public void setThumbnailSequenceDescArray(ArrayList<ThumbnailSequenceDesc> arrayList) {
        NvsUtils.checkFunctionInMainThread();
        if (arrayList != this.m_descArray) {
            clearThumbnailSequences();
            this.m_placeholderBitmap = null;
            this.m_descArray = arrayList;
            if (arrayList != null) {
                int i = 0;
                Iterator<ThumbnailSequenceDesc> it = arrayList.iterator();
                long j = 0;
                while (it.hasNext()) {
                    ThumbnailSequenceDesc next = it.next();
                    if (next.mediaFilePath == null || next.inPoint < j || next.outPoint <= next.inPoint || next.trimIn < 0 || next.trimOut <= next.trimIn) {
                        Log.e(TAG, "Invalid ThumbnailSequenceDesc!");
                    } else {
                        ThumbnailSequence thumbnailSequence = new ThumbnailSequence();
                        thumbnailSequence.m_index = i;
                        thumbnailSequence.m_mediaFilePath = next.mediaFilePath;
                        thumbnailSequence.m_inPoint = next.inPoint;
                        thumbnailSequence.m_outPoint = next.outPoint;
                        thumbnailSequence.m_trimIn = next.trimIn;
                        thumbnailSequence.m_trimDuration = next.trimOut - next.trimIn;
                        thumbnailSequence.m_stillImageHint = next.stillImageHint;
                        thumbnailSequence.m_onlyDecodeKeyFrame = next.onlyDecodeKeyFrame;
                        thumbnailSequence.m_thumbnailAspectRatio = next.thumbnailAspectRatio;
                        this.m_thumbnailSequenceArray.add(thumbnailSequence);
                        i++;
                        j = next.outPoint;
                    }
                }
            }
            updateThumbnailSequenceGeometry();
        }
    }

    public ArrayList<ThumbnailSequenceDesc> getThumbnailSequenceDescArray() {
        return this.m_descArray;
    }

    public void setThumbnailImageFillMode(int i) {
        NvsUtils.checkFunctionInMainThread();
        int i2 = this.m_thumbnailImageFillMode;
        if (!(i2 == 1 || i2 == 0)) {
            this.m_thumbnailImageFillMode = 0;
        }
        if (this.m_thumbnailImageFillMode != i) {
            this.m_thumbnailImageFillMode = i;
            updateThumbnailSequenceGeometry();
        }
    }

    public int getThumbnailImageFillMode() {
        return this.m_thumbnailImageFillMode;
    }

    public void setThumbnailAspectRatio(float f) {
        NvsUtils.checkFunctionInMainThread();
        if (f < 0.1f) {
            f = 0.1f;
        } else if (f > 10.0f) {
            f = 10.0f;
        }
        if (Math.abs(this.m_thumbnailAspectRatio - f) >= 0.001f) {
            this.m_thumbnailAspectRatio = f;
            updateThumbnailSequenceGeometry();
        }
    }

    public float getThumbnailAspectRatio() {
        return this.m_thumbnailAspectRatio;
    }

    public void setPixelPerMicrosecond(double d) {
        NvsUtils.checkFunctionInMainThread();
        if (d > 0.0d && d != this.m_pixelPerMicrosecond) {
            this.m_pixelPerMicrosecond = d;
            updateThumbnailSequenceGeometry();
        }
    }

    public double getPixelPerMicrosecond() {
        return this.m_pixelPerMicrosecond;
    }

    public void setStartPadding(int i) {
        NvsUtils.checkFunctionInMainThread();
        if (i >= 0 && i != this.m_startPadding) {
            this.m_startPadding = i;
            updateThumbnailSequenceGeometry();
        }
    }

    public int getStartPadding() {
        return this.m_startPadding;
    }

    public void setEndPadding(int i) {
        NvsUtils.checkFunctionInMainThread();
        if (i >= 0 && i != this.m_endPadding) {
            this.m_endPadding = i;
            updateThumbnailSequenceGeometry();
        }
    }

    public int getEndPadding() {
        return this.m_endPadding;
    }

    public void setMaxTimelinePosToScroll(int i) {
        NvsUtils.checkFunctionInMainThread();
        long max = (long) Math.max(i, 0);
        if (max != this.m_maxTimelinePosToScroll) {
            this.m_maxTimelinePosToScroll = max;
            updateThumbnailSequenceGeometry();
        }
    }

    public long getMaxTimelinePosToScroll() {
        return this.m_maxTimelinePosToScroll;
    }

    public long mapTimelinePosFromX(int i) {
        NvsUtils.checkFunctionInMainThread();
        double scrollX = (double) ((i + getScrollX()) - this.m_startPadding);
        double d = this.m_pixelPerMicrosecond;
        Double.isNaN(scrollX);
        return (long) Math.floor((scrollX / d) + 0.5d);
    }

    public int mapXFromTimelinePos(long j) {
        NvsUtils.checkFunctionInMainThread();
        double d = (double) j;
        double d2 = this.m_pixelPerMicrosecond;
        Double.isNaN(d);
        return (((int) Math.floor((d * d2) + 0.5d)) + this.m_startPadding) - getScrollX();
    }

    public void scaleWithAnchor(double d, int i) {
        NvsUtils.checkFunctionInMainThread();
        if (d > 0.0d) {
            long mapTimelinePosFromX = mapTimelinePosFromX(i);
            this.m_pixelPerMicrosecond *= d;
            updateThumbnailSequenceGeometry();
            scrollTo((getScrollX() + mapXFromTimelinePos(mapTimelinePosFromX)) - i, 0);
        }
    }

    public void setOnScrollChangeListenser(OnScrollChangeListener onScrollChangeListener) {
        NvsUtils.checkFunctionInMainThread();
        this.m_scrollChangeListener = onScrollChangeListener;
    }

    public OnScrollChangeListener getOnScrollChangeListenser() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_scrollChangeListener;
    }

    public void setScrollEnabled(boolean z) {
        this.m_scrollEnabled = z;
    }

    public boolean getScrollEnabled() {
        return this.m_scrollEnabled;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.m_iconGenerator = new NvsIconGenerator();
            this.m_iconGenerator.setIconCallback(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        cancelIconTask();
        this.m_scrollChangeListener = null;
        NvsIconGenerator nvsIconGenerator = this.m_iconGenerator;
        if (nvsIconGenerator != null) {
            nvsIconGenerator.release();
            this.m_iconGenerator = null;
        }
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        OnScrollChangeListener onScrollChangeListener = this.m_scrollChangeListener;
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChanged(this, i, i3);
        }
        updateThumbnails();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.m_scrollEnabled) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.m_scrollEnabled) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }

    private void init(Context context) {
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        this.m_contentView = new ContentView(context);
        addView(this.m_contentView, new FrameLayout.LayoutParams(-2, -1));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void requestUpdateThumbnailSequenceGeometry() {
        new Handler().post(new Runnable() {
            /* class com.meishe.myvideo.ui.trackview.MultiThumbnailSequenceView.AnonymousClass1 */

            public void run() {
                MultiThumbnailSequenceView.this.updateThumbnailSequenceGeometry();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateThumbnailSequenceGeometry() {
        int max;
        cancelIconTask();
        clearThumbnails();
        int height = getHeight();
        if (height != 0) {
            this.m_thumbnailSequenceMap.clear();
            int i = this.m_startPadding;
            this.m_maxThumbnailWidth = 0;
            Iterator<ThumbnailSequence> it = this.m_thumbnailSequenceArray.iterator();
            while (it.hasNext()) {
                ThumbnailSequence next = it.next();
                next.m_flags &= -3;
                double d = (double) next.m_inPoint;
                double d2 = this.m_pixelPerMicrosecond;
                Double.isNaN(d);
                int floor = ((int) Math.floor((d * d2) + 0.5d)) + this.m_startPadding;
                double d3 = (double) next.m_outPoint;
                double d4 = this.m_pixelPerMicrosecond;
                Double.isNaN(d3);
                int floor2 = ((int) Math.floor((d3 * d4) + 0.5d)) + this.m_startPadding;
                if (floor2 > floor) {
                    next.m_x = floor;
                    next.m_width = floor2 - floor;
                    double d5 = (double) (((float) height) * (next.m_thumbnailAspectRatio > 0.0f ? next.m_thumbnailAspectRatio : this.m_thumbnailAspectRatio));
                    Double.isNaN(d5);
                    next.m_thumbnailWidth = (int) Math.floor(d5 + 0.5d);
                    next.m_thumbnailWidth = Math.max(next.m_thumbnailWidth, 1);
                    this.m_maxThumbnailWidth = Math.max(next.m_thumbnailWidth, this.m_maxThumbnailWidth);
                    this.m_thumbnailSequenceMap.put(Integer.valueOf(floor), next);
                    i = floor2;
                }
            }
            long j = this.m_maxTimelinePosToScroll;
            if (j <= 0) {
                i += this.m_endPadding;
            } else {
                double d6 = (double) this.m_startPadding;
                double d7 = (double) j;
                double d8 = this.m_pixelPerMicrosecond;
                Double.isNaN(d7);
                Double.isNaN(d6);
                int floor3 = (int) Math.floor(d6 + (d7 * d8) + 0.5d);
                if (floor3 < i) {
                    i = floor3;
                }
            }
            this.m_contentWidth = i;
            this.m_contentView.layout(0, 0, this.m_contentWidth, getHeight());
            this.m_contentView.requestLayout();
            if (getWidth() + getScrollX() > this.m_contentWidth && (max = Math.max(getScrollX() - ((getWidth() + getScrollX()) - this.m_contentWidth), 0)) != getScrollX()) {
                scrollTo(max, 0);
            }
        }
    }

    /* access modifiers changed from: private */
    public static class ClipImageView extends AppCompatImageView {
        private int m_clipWidth;

        ClipImageView(Context context, int i) {
            super(context);
            this.m_clipWidth = i;
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            canvas.clipRect(new Rect(0, 0, this.m_clipWidth, getHeight()));
            super.onDraw(canvas);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateThumbnails() {
        Drawable drawable;
        Bitmap bitmap;
        int i;
        boolean z;
        if (this.m_iconGenerator != null) {
            if (this.m_thumbnailSequenceMap.isEmpty()) {
                clearThumbnails();
                return;
            }
            int i2 = this.m_maxThumbnailWidth;
            int scrollX = getScrollX();
            int width = getWidth();
            int max = Math.max(scrollX - i2, this.m_startPadding);
            int i3 = width + max + i2;
            if (i3 <= max) {
                clearThumbnails();
                return;
            }
            Integer floorKey = this.m_thumbnailSequenceMap.floorKey(Integer.valueOf(max));
            if (floorKey == null) {
                floorKey = this.m_thumbnailSequenceMap.firstKey();
            }
            for (Map.Entry<Integer, ThumbnailSequence> entry : this.m_thumbnailSequenceMap.tailMap(floorKey).entrySet()) {
                ThumbnailSequence value = entry.getValue();
                if (value.m_x + value.m_width >= max) {
                    if (value.m_x >= i3) {
                        break;
                    }
                    if (value.m_x < max) {
                        i = value.m_x + (((max - value.m_x) / value.m_thumbnailWidth) * value.m_thumbnailWidth);
                    } else {
                        i = value.m_x;
                    }
                    int i4 = value.m_x + value.m_width;
                    double d = (double) value.m_thumbnailWidth;
                    double d2 = (double) value.m_width;
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    double d3 = d / d2;
                    double d4 = (double) value.m_trimDuration;
                    Double.isNaN(d4);
                    long max2 = Math.max((long) (d3 * d4), 1L);
                    while (true) {
                        if (i >= i4) {
                            z = false;
                            break;
                        } else if (i >= i3) {
                            z = true;
                            break;
                        } else {
                            int i5 = value.m_thumbnailWidth;
                            if (i + i5 > i4) {
                                i5 = i4 - i;
                            }
                            long calcTimestampFromX = value.calcTimestampFromX(i, max2);
                            ThumbnailId thumbnailId = new ThumbnailId(value.m_index, calcTimestampFromX);
                            Thumbnail thumbnail = this.m_thumbnailMap.get(thumbnailId);
                            if (thumbnail == null) {
                                Thumbnail thumbnail2 = new Thumbnail();
                                thumbnail2.m_owner = value;
                                thumbnail2.m_timestamp = calcTimestampFromX;
                                thumbnail2.m_imageViewUpToDate = false;
                                thumbnail2.m_touched = true;
                                this.m_thumbnailMap.put(thumbnailId, thumbnail2);
                                if (i5 == value.m_thumbnailWidth) {
                                    thumbnail2.m_imageView = new ImageView(getContext());
                                } else {
                                    thumbnail2.m_imageView = new ClipImageView(getContext(), i5);
                                }
                                int i6 = this.m_thumbnailImageFillMode;
                                if (i6 == 0) {
                                    thumbnail2.m_imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                } else if (i6 == 1) {
                                    thumbnail2.m_imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                }
                                this.m_contentView.addView(thumbnail2.m_imageView);
                                thumbnail2.m_imageView.layout(i, 0, value.m_thumbnailWidth + i, this.m_contentView.getHeight());
                            } else {
                                thumbnail.m_touched = true;
                            }
                            i += i5;
                        }
                    }
                    if (z) {
                        break;
                    }
                }
            }
            this.m_updatingThumbnail = true;
            TreeMap treeMap = new TreeMap();
            Iterator<Map.Entry<ThumbnailId, Thumbnail>> it = this.m_thumbnailMap.entrySet().iterator();
            boolean z2 = false;
            while (it.hasNext()) {
                Map.Entry<ThumbnailId, Thumbnail> next = it.next();
                Thumbnail value2 = next.getValue();
                if (!(value2.m_imageView == null || (drawable = value2.m_imageView.getDrawable()) == null || (bitmap = ((BitmapDrawable) drawable).getBitmap()) == null)) {
                    this.m_placeholderBitmap = bitmap;
                }
                if (!value2.m_touched) {
                    if (value2.m_iconTaskId != 0) {
                        this.m_iconGenerator.cancelTask(value2.m_iconTaskId);
                    }
                    this.m_contentView.removeView(value2.m_imageView);
                    it.remove();
                } else {
                    value2.m_touched = false;
                    if (value2.m_imageViewUpToDate) {
                        treeMap.put(next.getKey(), ((BitmapDrawable) value2.m_imageView.getDrawable()).getBitmap());
                    } else {
                        long j = value2.m_owner.m_stillImageHint ? 0 : value2.m_timestamp;
                        updateKeyframeOnlyModeForThumbnailSequence(value2.m_owner);
                        int i7 = (value2.m_owner.m_flags & 1) != 0 ? 1 : 0;
                        Bitmap iconFromCache = this.m_iconGenerator.getIconFromCache(value2.m_owner.m_mediaFilePath, j, i7);
                        if (iconFromCache != null) {
                            treeMap.put(next.getKey(), iconFromCache);
                            if (setBitmapToThumbnail(iconFromCache, value2)) {
                                value2.m_imageViewUpToDate = true;
                                value2.m_iconTaskId = 0;
                            }
                        } else {
                            value2.m_iconTaskId = this.m_iconGenerator.getIcon(value2.m_owner.m_mediaFilePath, j, i7);
                            z2 = true;
                        }
                    }
                }
            }
            this.m_updatingThumbnail = false;
            if (z2) {
                if (!treeMap.isEmpty()) {
                    for (Map.Entry<ThumbnailId, Thumbnail> entry2 : this.m_thumbnailMap.entrySet()) {
                        Thumbnail value3 = entry2.getValue();
                        if (!value3.m_imageViewUpToDate) {
                            Map.Entry ceilingEntry = treeMap.ceilingEntry(entry2.getKey());
                            if (ceilingEntry != null) {
                                setBitmapToThumbnail((Bitmap) ceilingEntry.getValue(), value3);
                            } else {
                                setBitmapToThumbnail((Bitmap) treeMap.lastEntry().getValue(), value3);
                            }
                        }
                    }
                } else if (this.m_placeholderBitmap != null) {
                    for (Map.Entry<ThumbnailId, Thumbnail> entry3 : this.m_thumbnailMap.entrySet()) {
                        Thumbnail value4 = entry3.getValue();
                        if (!value4.m_imageViewUpToDate) {
                            setBitmapToThumbnail(this.m_placeholderBitmap, value4);
                        }
                    }
                }
            }
        }
    }

    private void updateKeyframeOnlyModeForThumbnailSequence(ThumbnailSequence thumbnailSequence) {
        if ((thumbnailSequence.m_flags & 2) == 0) {
            if (thumbnailSequence.m_onlyDecodeKeyFrame) {
                thumbnailSequence.m_flags |= 3;
                return;
            }
            double d = (double) thumbnailSequence.m_thumbnailWidth;
            double d2 = this.m_pixelPerMicrosecond;
            Double.isNaN(d);
            if (shouldDecodecKeyFrameOnly(thumbnailSequence.m_mediaFilePath, Math.max((long) ((d / d2) + 0.5d), 1L))) {
                thumbnailSequence.m_flags |= 1;
            } else {
                thumbnailSequence.m_flags &= -2;
            }
            thumbnailSequence.m_flags |= 2;
        }
    }

    private boolean shouldDecodecKeyFrameOnly(String str, long j) {
        NvsAVFileInfo aVFileInfo;
        NvsRational videoStreamFrameRate;
        NvsStreamingContext instance = NvsStreamingContext.getInstance();
        if (instance == null || (aVFileInfo = instance.getAVFileInfo(str)) == null || aVFileInfo.getVideoStreamCount() < 1 || (videoStreamFrameRate = aVFileInfo.getVideoStreamFrameRate(0)) == null || videoStreamFrameRate.den <= 0 || videoStreamFrameRate.num <= 0 || aVFileInfo.getVideoStreamDuration(0) < j) {
            return false;
        }
        int detectVideoFileKeyframeInterval = instance.detectVideoFileKeyframeInterval(str);
        if (detectVideoFileKeyframeInterval == 0) {
            detectVideoFileKeyframeInterval = 30;
        } else if (detectVideoFileKeyframeInterval == 1) {
            return false;
        }
        double d = (double) detectVideoFileKeyframeInterval;
        double d2 = (double) videoStreamFrameRate.den;
        double d3 = (double) videoStreamFrameRate.num;
        Double.isNaN(d2);
        Double.isNaN(d3);
        Double.isNaN(d);
        int i = (int) (d * (d2 / d3) * 1000000.0d);
        if (detectVideoFileKeyframeInterval <= 30) {
            double d4 = (double) i;
            Double.isNaN(d4);
            if (((double) j) > d4 * 0.9d) {
                return true;
            }
        } else if (detectVideoFileKeyframeInterval <= 60) {
            double d5 = (double) i;
            Double.isNaN(d5);
            if (((double) j) > d5 * 0.8d) {
                return true;
            }
        } else if (detectVideoFileKeyframeInterval <= 100) {
            double d6 = (double) i;
            Double.isNaN(d6);
            if (((double) j) > d6 * 0.7d) {
                return true;
            }
        } else if (detectVideoFileKeyframeInterval <= 150) {
            double d7 = (double) i;
            Double.isNaN(d7);
            if (((double) j) > d7 * 0.5d) {
                return true;
            }
        } else if (detectVideoFileKeyframeInterval <= 250) {
            double d8 = (double) i;
            Double.isNaN(d8);
            if (((double) j) > d8 * 0.3d) {
                return true;
            }
        } else {
            double d9 = (double) i;
            Double.isNaN(d9);
            if (((double) j) > d9 * 0.2d) {
                return true;
            }
        }
        return false;
    }

    private boolean setBitmapToThumbnail(Bitmap bitmap, Thumbnail thumbnail) {
        if (bitmap == null || thumbnail.m_imageView == null) {
            return false;
        }
        thumbnail.m_imageView.setImageBitmap(bitmap);
        return true;
    }

    private void clearThumbnailSequences() {
        cancelIconTask();
        clearThumbnails();
        this.m_thumbnailSequenceArray.clear();
        this.m_thumbnailSequenceMap.clear();
        this.m_contentWidth = 0;
    }

    private void clearThumbnails() {
        for (Map.Entry<ThumbnailId, Thumbnail> entry : this.m_thumbnailMap.entrySet()) {
            this.m_contentView.removeView(entry.getValue().m_imageView);
        }
        this.m_thumbnailMap.clear();
    }

    private void cancelIconTask() {
        NvsIconGenerator nvsIconGenerator = this.m_iconGenerator;
        if (nvsIconGenerator != null) {
            nvsIconGenerator.cancelTask(0);
        }
    }

    @Override // com.meicam.sdk.NvsIconGenerator.IconCallback
    public void onIconReady(Bitmap bitmap, long j, long j2) {
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        this.mHandler.removeCallbacksAndMessages(null);
        this.mHandler.post(new Runnable() {
            /* class com.meishe.myvideo.ui.trackview.MultiThumbnailSequenceView.AnonymousClass2 */

            public void run() {
                MultiThumbnailSequenceView.this.updateThumbnails();
            }
        });
    }
}
