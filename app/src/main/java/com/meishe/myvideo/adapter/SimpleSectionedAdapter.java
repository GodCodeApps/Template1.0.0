package com.meishe.myvideo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.meishe.myvideo.bean.MediaData;
import com.meishe.myvideo.holder.HeaderViewHolder;
import com.meishe.myvideo.interfaces.OnItemClick;
import com.meishe.myvideoapp.R;
import java.util.List;

public abstract class SimpleSectionedAdapter<VH extends RecyclerView.ViewHolder> extends SectionedRecyclerViewAdapter<HeaderViewHolder, VH, RecyclerView.ViewHolder> {
    private int clickType;
    private int mLimitMediaCount = -1;

    /* access modifiers changed from: protected */
    public abstract OnItemClick getHeadItemCick();

    /* access modifiers changed from: protected */
    @LayoutRes
    public int getLayoutResource() {
        return R.layout.item_head_media;
    }

    /* access modifiers changed from: protected */
    public abstract List<MediaData> getList();

    /* access modifiers changed from: protected */
    public abstract String getSectionHeaderTitle(int i);

    /* access modifiers changed from: protected */
    @IdRes
    public int getTitleTextID() {
        return R.id.meida_head_time;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SectionedRecyclerViewAdapter
    public boolean hasFooterInSection(int i) {
        return false;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SectionedRecyclerViewAdapter
    public void onBindSectionFooterViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SectionedRecyclerViewAdapter
    public RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    public void setLimitMediaCount(int i) {
        this.mLimitMediaCount = i;
    }

    public void setClickType(int i) {
        this.clickType = i;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SectionedRecyclerViewAdapter
    public HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup viewGroup, int i) {
        return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResource(), viewGroup, false), getTitleTextID(), this.clickType, this.mLimitMediaCount);
    }

    /* access modifiers changed from: protected */
    public void onBindSectionHeaderViewHolder(HeaderViewHolder headerViewHolder, int i) {
        View view = headerViewHolder.itemView;
        view.setTag("第几行的：       " + i);
        headerViewHolder.render(getSectionHeaderTitle(i), getList().get(i).isState());
        headerViewHolder.onClick(i, getHeadItemCick());
    }
}
