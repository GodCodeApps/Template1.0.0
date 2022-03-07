package com.meishe.myvideo.adapter;

import androidx.recyclerview.widget.GridLayoutManager;

public class SectionedSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    protected SectionedRecyclerViewAdapter<?, ?, ?> adapter = null;
    protected GridLayoutManager layoutManager = null;

    public SectionedSpanSizeLookup(SectionedRecyclerViewAdapter<?, ?, ?> sectionedRecyclerViewAdapter, GridLayoutManager gridLayoutManager) {
        this.adapter = sectionedRecyclerViewAdapter;
        this.layoutManager = gridLayoutManager;
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
    public int getSpanSize(int i) {
        if (this.adapter.isSectionHeaderPosition(i) || this.adapter.isSectionFooterPosition(i)) {
            return this.layoutManager.getSpanCount();
        }
        return 1;
    }
}
