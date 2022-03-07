package com.meishe.myvideo.decoration;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int mBottomSpace;
    private int mLeftSpace;
    private int mRightSpace;
    private int mTopSpace;

    public GridItemDecoration(int i, int i2, int i3, int i4) {
        this.mLeftSpace = i;
        this.mRightSpace = i3;
        this.mTopSpace = i2;
        this.mBottomSpace = i4;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        rect.left = this.mLeftSpace;
        rect.top = this.mTopSpace;
        rect.right = this.mRightSpace;
        rect.bottom = this.mBottomSpace;
    }
}
