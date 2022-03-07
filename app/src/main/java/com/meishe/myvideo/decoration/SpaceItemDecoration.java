package com.meishe.myvideo.decoration;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int leftSpace;
    private int rightSpace;

    public SpaceItemDecoration(int i, int i2) {
        this.leftSpace = i;
        this.rightSpace = i2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        rect.left = this.leftSpace;
        rect.right = this.rightSpace;
    }
}
