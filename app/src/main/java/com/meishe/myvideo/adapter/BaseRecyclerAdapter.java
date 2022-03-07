package com.meishe.myvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.holder.BaseViewHolder;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> implements View.OnClickListener {
    private static final int BASE_TYPE = 100;
    protected static final int EDIT_MENU_ADJUST = 112;
    protected static final int EDIT_MENU_BEAUTY_SHAPE = 113;
    protected static final int EDIT_MENU_CANVAS_BLUR = 111;
    protected static final int EDIT_MENU_CANVAS_STYLE = 110;
    protected static final int EDIT_MENU_COMPOUND_CAPTION = 108;
    protected static final int EDIT_MENU_TRANSITION = 114;
    protected static final int EDIT_MENU_TYPE = 101;
    protected static final int EDIT_MENU_TYPE_ASSET = 105;
    protected static final int EDIT_MENU_TYPE_CAPTION_COLOR = 104;
    protected static final int EDIT_MENU_TYPE_CAPTION_STYLE = 103;
    protected static final int EDIT_MENU_WATER_MARK = 107;
    protected static final int EDIT_MENU_WATER_MARK_EFFECT = 106;
    protected static final int EFFECT_MENU_TYPE = 102;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<BaseInfo> mList;
    protected OnItemClickListener mOnItemClickListener;
    protected RecyclerView mRecyclerView;
    protected int mSelectPosition = -1;

    public interface OnItemClickListener {
        void onItemClicked(View view, int i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    public BaseRecyclerAdapter(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        this.mList = new ArrayList();
    }

    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        baseViewHolder.bindViewHolder(this.mContext, getItem(i), i, i == this.mSelectPosition, this);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<BaseInfo> list = this.mList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public BaseInfo getItem(int i) {
        List<BaseInfo> list;
        if (i < 0 || (list = this.mList) == null || i >= list.size()) {
            return new BaseInfo("");
        }
        return this.mList.get(i);
    }

    public BaseInfo getItem(View view) {
        return getItem(this.mRecyclerView.getChildAdapterPosition(view));
    }

    public int getItemPosition(BaseInfo baseInfo) {
        return this.mList.indexOf(baseInfo);
    }

    public void updateListItem(int i, BaseInfo baseInfo) {
        List<BaseInfo> list = this.mList;
        if (list != null && i < list.size()) {
            this.mList.set(i, baseInfo);
        }
    }

    public void addItem(BaseInfo baseInfo) {
        if (baseInfo != null) {
            this.mList.add(baseInfo);
            notifyItemRangeChanged(this.mList.indexOf(baseInfo) - 1, 2);
        }
    }

    public List<BaseInfo> getAllItems() {
        List<BaseInfo> list = this.mList;
        return list == null ? new ArrayList() : list;
    }

    public void addItems(int i, List<BaseInfo> list) {
        if (list != null && i < this.mList.size()) {
            this.mList.addAll(i, list);
            notifyItemRangeChanged(i - 1, (i + list.size()) - 1);
        }
    }

    public void addAll(List<? extends BaseInfo> list) {
        if (list != null && list.size() > 0) {
            this.mList.clear();
            this.mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setSelectPosition(int i) {
        this.mSelectPosition = i;
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        return this.mSelectPosition;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void onClick(View view) {
        if (this.mOnItemClickListener != null && (view.getTag() instanceof BaseInfo)) {
            BaseInfo baseInfo = (BaseInfo) view.getTag();
            int itemPosition = getItemPosition(baseInfo);
            this.mOnItemClickListener.onItemClicked(view, itemPosition);
            if (!this.mContext.getResources().getString(R.string.more).equals(baseInfo.mName)) {
                this.mSelectPosition = itemPosition;
            }
            notifyDataSetChanged();
        }
    }

    public void setPositionClick(int i) {
        OnItemClickListener onItemClickListener = this.mOnItemClickListener;
        if (onItemClickListener != null) {
            onItemClickListener.onItemClicked(null, i);
            this.mSelectPosition = i;
            notifyDataSetChanged();
        }
    }
}
