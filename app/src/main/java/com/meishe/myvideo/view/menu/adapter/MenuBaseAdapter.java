package com.meishe.myvideo.view.menu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.EffectInfo;
import com.meishe.myvideo.bean.menu.EditMenuInfo;
import com.meishe.myvideo.view.menu.holder.BaseMenuViewHolder;
import java.util.ArrayList;
import java.util.List;

public class MenuBaseAdapter extends RecyclerView.Adapter<BaseMenuViewHolder> implements View.OnClickListener {
    protected static final int MENU_FILL = 101;
    protected static final int MENU_NORMAL = 100;
    protected static final int MENU_TABLE_FUNCTION = 102;
    protected static final int MENU_TABLE_ITEM = 103;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<BaseInfo> mList;
    protected RecyclerView mRecyclerView;
    protected int mSelectPosition = -1;
    protected OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(View view, int i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public BaseMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    public MenuBaseAdapter(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mList = new ArrayList();
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void onBindViewHolder(@NonNull BaseMenuViewHolder baseMenuViewHolder, int i) {
        baseMenuViewHolder.bindViewHolder(this.mContext, getItem(i), i == this.mSelectPosition, this);
    }

    public BaseInfo getItem(int i) {
        List<BaseInfo> list;
        if (i < 0 || (list = this.mList) == null || i >= list.size()) {
            return new BaseInfo("");
        }
        return this.mList.get(i);
    }

    public void setSelectPosition(int i) {
        this.mSelectPosition = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<BaseInfo> list = this.mList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void onClick(View view) {
        if (this.onItemClickListener != null && (view.getTag() instanceof BaseInfo)) {
            this.onItemClickListener.onItemClicked(view, getItemPosition((BaseInfo) view.getTag()));
        }
        if ((view.getTag() instanceof EffectInfo) || (view.getTag() instanceof EditMenuInfo)) {
            this.mSelectPosition = getItemPosition((BaseInfo) view.getTag());
            notifyDataSetChanged();
        }
    }

    public int getItemPosition(BaseInfo baseInfo) {
        if (this.mList.contains(baseInfo)) {
            return this.mList.indexOf(baseInfo);
        }
        return -1;
    }

    public void addAll(List<BaseInfo> list) {
        if (list != null) {
            this.mList.clear();
            this.mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }
}
