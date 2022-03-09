package com.meishe.myvideo.util;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.meishe.myvideoapp.R;

import java.util.ArrayList;
import java.util.List;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int itemWidth;
    int marginSizeMiddle;
    int marginSizeStart;
    private int screenWidth;
    private int spanCount;
    private List<Integer> spanRightList;

    public GridSpacingItemDecoration(Context context, int i) {
        this.spanCount = i;
        this.screenWidth = ScreenUtils.getScreenWidth(context) - (((int) context.getResources().getDimension(R.dimen.select_recycle_marginLeftAndRight)) * 2);
        this.spanRightList = new ArrayList();
        this.itemWidth = ((this.screenWidth - (this.marginSizeStart * 2)) - (this.marginSizeMiddle * 3)) / 4;
        marginSizeMiddle = ((int) context.getResources().getDimension(R.dimen.select_item_between));
        marginSizeStart = ((int) context.getResources().getDimension(R.dimen.select_item_start_end));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        if (this.screenWidth - (this.spanCount * this.itemWidth) > 0) {
            int dip2px = ScreenUtils.dip2px(view.getContext(), 3.0f);
            int i = this.spanCount;
            int i2 = childAdapterPosition % i;
            int i3 = this.screenWidth / i;
            int i4 = this.marginSizeMiddle;
            if (i2 == 0) {
                rect.left = dip2px;
                rect.right = (i3 - this.itemWidth) - rect.left;
                this.spanRightList.add(Integer.valueOf(rect.right));
            } else if (i2 == i - 1) {
                rect.left = i4 - this.spanRightList.get(i2 - 1).intValue();
                rect.right = (i3 - this.itemWidth) - rect.left;
                this.spanRightList.add(Integer.valueOf(rect.right));
            } else {
                rect.left = i4 - this.spanRightList.get(i2 - 1).intValue();
                rect.right = (i3 - this.itemWidth) - rect.left;
                this.spanRightList.add(Integer.valueOf(rect.right));
            }
        }
    }
}
