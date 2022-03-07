package com.meishe.myvideo.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.application.MeiSheApplication;
import com.meishe.myvideo.bean.MediaData;
import com.meishe.myvideo.fragment.MediaFragment;
import com.meishe.myvideo.holder.AgendaItemViewHolder;
import com.meishe.myvideo.interfaces.OnItemClick;
import com.meishe.myvideo.interfaces.OnTotalNumChange;
import com.meishe.myvideoapp.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgendaSimpleSectionAdapter extends SimpleSectionedAdapter<AgendaItemViewHolder> implements OnItemClick {
    private final String TAG;
    private int clickType;
    private MediaFragment fragment;
    private int limitCount;
    private List<MediaData> listOfParent;
    private List<List<MediaData>> lists;
    private Activity mActivity;
    private OnTotalNumChange mOnTotalNumChange;
    private RecyclerView recyclerView;
    private List<MediaData> selectList;
    private int tag;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SimpleSectionedAdapter
    public OnItemClick getHeadItemCick() {
        return this;
    }

    public void setSelectList(List<MediaData> list) {
        this.selectList = list;
        notifyDataSetChanged();
    }

    public AgendaSimpleSectionAdapter(List<List<MediaData>> list, List<MediaData> list2, RecyclerView recyclerView2, OnTotalNumChange onTotalNumChange, int i, Activity activity, int i2) {
        this.TAG = getClass().getName();
        this.selectList = new ArrayList();
        this.limitCount = -1;
        this.lists = list;
        this.recyclerView = recyclerView2;
        this.listOfParent = list2;
        this.mOnTotalNumChange = onTotalNumChange;
        this.tag = i;
        this.mActivity = activity;
        this.clickType = i2;
        setClickType(i2);
    }

    public AgendaSimpleSectionAdapter(List<List<MediaData>> list, List<MediaData> list2, RecyclerView recyclerView2, OnTotalNumChange onTotalNumChange, int i, Activity activity, int i2, int i3, MediaFragment mediaFragment) {
        this(list, list2, recyclerView2, onTotalNumChange, i, activity, i2);
        this.limitCount = i3;
        this.fragment = mediaFragment;
        setLimitMediaCount(i3);
    }

    public List<MediaData> getSelectList() {
        List<MediaData> list = this.selectList;
        return list == null ? new ArrayList() : list;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SimpleSectionedAdapter
    public String getSectionHeaderTitle(int i) {
        return new SimpleDateFormat(MeiSheApplication.getContext().getResources().getString(R.string.yearMonthDate)).format(new Date()).equals(this.listOfParent.get(i).getDate()) ? MeiSheApplication.getContext().getResources().getString(R.string.today) : this.listOfParent.get(i).getDate();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SimpleSectionedAdapter
    public List<MediaData> getList() {
        return this.listOfParent;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SectionedRecyclerViewAdapter
    public int getSectionCount() {
        return this.lists.size();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SectionedRecyclerViewAdapter
    public int getItemCountForSection(int i) {
        return this.lists.get(i).size();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.adapter.SectionedRecyclerViewAdapter
    public AgendaItemViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_media2, viewGroup, false);
        inflate.setTag("1");
        return new AgendaItemViewHolder(inflate, this.clickType);
    }

    /* access modifiers changed from: protected */
    public void onBindItemViewHolder(AgendaItemViewHolder agendaItemViewHolder, int i, int i2) {
        agendaItemViewHolder.render(this.lists.get(i).get(i2), i, i2, this);
    }

    @Override // com.meishe.myvideo.interfaces.OnItemClick
    public void OnHeadClick(View view, int i) {
        this.listOfParent.get(i).setState(!this.listOfParent.get(i).isState());
        int childAdapterPosition = this.recyclerView.getChildAdapterPosition(view);
        this.recyclerView.getAdapter().notifyItemChanged(childAdapterPosition);
        String str = this.TAG;
        Logger.e(str, "OnHeadClick   " + childAdapterPosition);
        for (int i2 = 0; i2 < this.lists.get(i).size(); i2++) {
            int i3 = childAdapterPosition + i2 + 1;
            if (!this.listOfParent.get(i).isState()) {
                totalChange(this.lists.get(i).get(i2).isState(), i, i2, i3, true);
                onTotalChange();
            } else if (!this.lists.get(i).get(i2).isState()) {
                totalChange(this.lists.get(i).get(i2).isState(), i, i2, i3, true);
                onTotalChange();
            }
        }
    }

    @Override // com.meishe.myvideo.interfaces.OnItemClick
    public void OnItemClick(View view, int i, int i2) {
        if (this.lists.get(i).get(i2).isState() || this.limitCount == -1 || getTotal() != this.limitCount) {
            itemClick(view, i, i2, true);
        }
    }

    public void itemClick(View view, int i, int i2, boolean z) {
        String str = this.TAG;
        Logger.d(str, "OnItemClick  第" + i + "行    第" + i2 + "个");
        if (this.clickType == 0) {
            onSingleClick(i, i2, this.recyclerView.getChildAdapterPosition(view));
        } else {
            totalChange(this.lists.get(i).get(i2).isState(), i, i2, this.recyclerView.getChildAdapterPosition(view), z);
            checkAndChangeParentItemAfterItemChange(i);
        }
        onTotalChange();
    }

    private void onSingleClick(int i, int i2, int i3) {
        if (this.selectList.size() == 0) {
            this.selectList.add(this.lists.get(i).get(i2));
            refreshView(i, i2, i3);
        } else if (this.selectList.get(0).getPath().equals(this.lists.get(i).get(i2).getPath())) {
            this.selectList.remove(this.lists.get(i).get(i2));
            refreshView(i, i2, i3);
        } else {
            Point pointByData = getPointByData(this.lists, this.selectList.get(0));
            refreshView(pointByData.x, pointByData.y, getPositionByData(this.lists, this.selectList.get(0)));
            this.selectList.clear();
            this.selectList.add(this.lists.get(i).get(i2));
            refreshView(i, i2, i3);
        }
    }

    private void refreshView(int i, int i2, int i3) {
        this.lists.get(i).get(i2).setState(!this.lists.get(i).get(i2).isState());
        this.recyclerView.getAdapter().notifyItemChanged(i3);
    }

    private void checkAndChangeParentItemAfterItemChange(int i) {
        String str = this.TAG;
        Logger.d(str, "checkAndChangeParentItemAfterItemChange   " + i + "    父item状态：    " + this.listOfParent.get(i).isState());
        if (this.listOfParent.get(i).isState()) {
            this.listOfParent.get(i).setState(!this.listOfParent.get(i).isState());
            RecyclerView recyclerView2 = this.recyclerView;
            this.recyclerView.getAdapter().notifyItemChanged(recyclerView2.getChildAdapterPosition(recyclerView2.findViewWithTag("第几行的：       " + i)));
            return;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.lists.get(i).size(); i3++) {
            i2 = this.lists.get(i).get(i3).isState() ? i2 + 1 : i2 - 1;
        }
        if (Math.abs(i2) == this.lists.get(i).size() && i2 > 0) {
            this.listOfParent.get(i).setState(!this.listOfParent.get(i).isState());
            RecyclerView recyclerView3 = this.recyclerView;
            this.recyclerView.getAdapter().notifyItemChanged(recyclerView3.getChildAdapterPosition(recyclerView3.findViewWithTag("第几行的：       " + i)));
        }
    }

    private void totalChange(boolean z, int i, int i2, int i3, boolean z2) {
        String str = this.TAG;
        Logger.d(str, "totalChange   " + i3);
        this.lists.get(i).get(i2).setState(this.lists.get(i).get(i2).isState() ^ true);
        if (!z) {
            if (z2) {
                setTotal(getTotal() + 1);
            }
            this.selectList.add(this.lists.get(i).get(i2));
            String str2 = this.TAG;
            Logger.d(str2, "列表添加数据：   " + this.selectList.size());
        } else {
            if (z2) {
                setTotal(getTotal() - 1);
            }
            int position = this.lists.get(i).get(i2).getPosition();
            this.selectList.remove(this.lists.get(i).get(i2));
            for (MediaData mediaData : this.selectList) {
                if (mediaData.getPosition() > position) {
                    int positionByData = getPositionByData(this.lists, mediaData);
                    String str3 = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("刷新大于 ");
                    sb.append(position);
                    sb.append("   为   ");
                    sb.append(mediaData.getPosition() - 1);
                    sb.append("        位置为：    ");
                    sb.append(positionByData);
                    Logger.d(str3, sb.toString());
                    mediaData.setPosition(mediaData.getPosition() - 1);
                    this.recyclerView.getAdapter().notifyItemChanged(positionByData);
                }
            }
        }
        this.lists.get(i).get(i2).setPosition(getTotal());
        this.recyclerView.getAdapter().notifyItemChanged(i3);
    }

    private void onTotalChange() {
        this.mOnTotalNumChange.onTotalNumChange(this.selectList, Integer.valueOf(this.tag));
    }

    private int getTotal() {
        return this.fragment.getTotalSize();
    }

    private void setTotal(int i) {
        this.fragment.setTotalSize(i);
    }

    public int getPositionByData(List<List<MediaData>> list, MediaData mediaData) {
        for (int i = 0; i < list.size(); i++) {
            for (int i2 = 0; i2 < list.get(i).size(); i2++) {
                if (checkIsThisData(list.get(i).get(i2), mediaData)) {
                    return i2 + getCountFromListAndListByIndex(list, i);
                }
            }
        }
        return -1;
    }

    public Point getPointByData(List<List<MediaData>> list, MediaData mediaData) {
        Point point = new Point(0, 0);
        for (int i = 0; i < list.size(); i++) {
            for (int i2 = 0; i2 < list.get(i).size(); i2++) {
                if (checkIsThisData(list.get(i).get(i2), mediaData)) {
                    return new Point(i, i2);
                }
            }
        }
        return point;
    }

    public MediaData getDataByPath(List<List<MediaData>> list, String str) {
        MediaData mediaData = new MediaData();
        for (int i = 0; i < list.size(); i++) {
            for (int i2 = 0; i2 < list.get(i).size(); i2++) {
                if (checkDataByPath(list.get(i).get(i2), str)) {
                    return list.get(i).get(i2);
                }
            }
        }
        return mediaData;
    }

    private boolean checkIsThisData(MediaData mediaData, MediaData mediaData2) {
        if (mediaData2.getType() == 2) {
            return mediaData.getPath().equals(mediaData2.getPath());
        }
        return mediaData.getId() == mediaData2.getId();
    }

    private boolean checkDataByPath(MediaData mediaData, String str) {
        return mediaData.getPath().equals(str);
    }

    private int getCountFromListAndListByIndex(List<List<MediaData>> list, int i) {
        int i2 = 0;
        if (i > 0) {
            int i3 = 0;
            while (i2 < i) {
                i3 += list.get(i2).size();
                i2++;
            }
            i2 = i3;
        }
        return i2 + i + 1;
    }
}
