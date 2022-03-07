package com.meishe.myvideo.view.menu.adapter;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int bottomSpace;
    private int leftSpace;
    private int rightSpace;
    private int topSpace;

    public GridItemDecoration(int i, int i2, int i3, int i4) {
        this.leftSpace = i;
        this.rightSpace = i2;
        this.topSpace = i3;
        this.bottomSpace = i4;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        if (recyclerView.getChildAdapterPosition(view) != 0) {
            rect.left = this.leftSpace;
        } else {
            rect.left = 0;
        }
        rect.right = this.rightSpace;
        rect.top = this.topSpace;
        rect.bottom = this.bottomSpace;
    }
}
