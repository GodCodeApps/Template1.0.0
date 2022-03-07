package com.meishe.myvideo.ui.trackview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class MYHorizontalScrollView extends HorizontalScrollView {
    private OnScrollViewListener mOnScrollViewListener;

    public MYHorizontalScrollView(Context context) {
        super(context);
    }

    public MYHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MYHorizontalScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        OnScrollViewListener onScrollViewListener = this.mOnScrollViewListener;
        if (onScrollViewListener != null) {
            onScrollViewListener.onScrollChanged(i, i2, i3, i4);
        }
    }

    public OnScrollViewListener getOnScrollViewListener() {
        return this.mOnScrollViewListener;
    }

    public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener) {
        this.mOnScrollViewListener = onScrollViewListener;
    }
}
