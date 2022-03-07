package com.meishe.myvideo.adapter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public abstract class SectionedRecyclerViewAdapter<H extends RecyclerView.ViewHolder, VH extends RecyclerView.ViewHolder, F extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_ITEM = -3;
    protected static final int TYPE_SECTION_FOOTER = -2;
    protected static final int TYPE_SECTION_HEADER = -1;
    private int count = 0;
    private boolean[] isFooter = null;
    private boolean[] isHeader = null;
    private int[] positionWithinSection = null;
    private int[] sectionForPosition = null;

    /* access modifiers changed from: protected */
    public abstract int getItemCountForSection(int i);

    /* access modifiers changed from: protected */
    public abstract int getSectionCount();

    /* access modifiers changed from: protected */
    public int getSectionFooterViewType(int i) {
        return -2;
    }

    /* access modifiers changed from: protected */
    public int getSectionHeaderViewType(int i) {
        return -1;
    }

    /* access modifiers changed from: protected */
    public int getSectionItemViewType(int i, int i2) {
        return -3;
    }

    /* access modifiers changed from: protected */
    public abstract boolean hasFooterInSection(int i);

    /* access modifiers changed from: protected */
    public boolean isSectionFooterViewType(int i) {
        return i == -2;
    }

    /* access modifiers changed from: protected */
    public boolean isSectionHeaderViewType(int i) {
        return i == -1;
    }

    /* access modifiers changed from: protected */
    public abstract void onBindItemViewHolder(VH vh, int i, int i2);

    /* access modifiers changed from: protected */
    public abstract void onBindSectionFooterViewHolder(F f, int i);

    /* access modifiers changed from: protected */
    public abstract void onBindSectionHeaderViewHolder(H h, int i);

    /* access modifiers changed from: protected */
    public abstract VH onCreateItemViewHolder(ViewGroup viewGroup, int i);

    /* access modifiers changed from: protected */
    public abstract F onCreateSectionFooterViewHolder(ViewGroup viewGroup, int i);

    /* access modifiers changed from: protected */
    public abstract H onCreateSectionHeaderViewHolder(ViewGroup viewGroup, int i);

    public SectionedRecyclerViewAdapter() {
        registerAdapterDataObserver(new SectionDataObserver());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupIndices();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.count;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setupIndices() {
        this.count = countItems();
        allocateAuxiliaryArrays(this.count);
        precomputeIndices();
    }

    private int countItems() {
        int sectionCount = getSectionCount();
        int i = 0;
        for (int i2 = 0; i2 < sectionCount; i2++) {
            i += getItemCountForSection(i2) + 1 + (hasFooterInSection(i2) ? 1 : 0);
        }
        return i;
    }

    private void precomputeIndices() {
        int sectionCount = getSectionCount();
        int i = 0;
        for (int i2 = 0; i2 < sectionCount; i2++) {
            setPrecomputedItem(i, true, false, i2, 0);
            int i3 = i + 1;
            for (int i4 = 0; i4 < getItemCountForSection(i2); i4++) {
                setPrecomputedItem(i3, false, false, i2, i4);
                i3++;
            }
            if (hasFooterInSection(i2)) {
                setPrecomputedItem(i3, false, true, i2, 0);
                i3++;
            }
            i = i3;
        }
    }

    private void allocateAuxiliaryArrays(int i) {
        this.sectionForPosition = new int[i];
        this.positionWithinSection = new int[i];
        this.isHeader = new boolean[i];
        this.isFooter = new boolean[i];
    }

    private void setPrecomputedItem(int i, boolean z, boolean z2, int i2, int i3) {
        this.isHeader[i] = z;
        this.isFooter[i] = z2;
        this.sectionForPosition[i] = i2;
        this.positionWithinSection[i] = i3;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (isSectionHeaderViewType(i)) {
            return onCreateSectionHeaderViewHolder(viewGroup, i);
        }
        if (isSectionFooterViewType(i)) {
            return onCreateSectionFooterViewHolder(viewGroup, i);
        }
        return onCreateItemViewHolder(viewGroup, i);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: androidx.recyclerview.widget.RecyclerView$ViewHolder */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int i2 = this.sectionForPosition[i];
        int i3 = this.positionWithinSection[i];
        if (isSectionHeaderPosition(i)) {
            onBindSectionHeaderViewHolder((H) viewHolder, i2);
        } else if (isSectionFooterPosition(i)) {
            onBindSectionFooterViewHolder((F) viewHolder, i2);
        } else {
            onBindItemViewHolder((VH) viewHolder, i2, i3);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (this.sectionForPosition == null) {
            setupIndices();
        }
        int i2 = this.sectionForPosition[i];
        int i3 = this.positionWithinSection[i];
        if (isSectionHeaderPosition(i)) {
            return getSectionHeaderViewType(i2);
        }
        if (isSectionFooterPosition(i)) {
            return getSectionFooterViewType(i2);
        }
        return getSectionItemViewType(i2, i3);
    }

    public boolean isSectionHeaderPosition(int i) {
        if (this.isHeader == null) {
            setupIndices();
        }
        return this.isHeader[i];
    }

    public boolean isSectionFooterPosition(int i) {
        if (this.isFooter == null) {
            setupIndices();
        }
        return this.isFooter[i];
    }

    class SectionDataObserver extends RecyclerView.AdapterDataObserver {
        SectionDataObserver() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            SectionedRecyclerViewAdapter.this.setupIndices();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i, int i2) {
            SectionedRecyclerViewAdapter.this.setupIndices();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int i, int i2) {
            SectionedRecyclerViewAdapter.this.setupIndices();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int i, int i2) {
            SectionedRecyclerViewAdapter.this.setupIndices();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeMoved(int i, int i2, int i3) {
            SectionedRecyclerViewAdapter.this.setupIndices();
        }
    }
}
