package com.meishe.myvideo.view.menu.adapter;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.view.menu.bean.MenuInfo;
import com.meishe.myvideo.view.menu.holder.BaseMenuViewHolder;
import com.meishe.myvideo.view.menu.holder.MenuFillViewHolder;
import com.meishe.myvideo.view.menu.holder.MenuNorViewHolder;
import com.meishe.myvideoapp.R;

public class MenuFunctionAdapter extends MenuBaseAdapter {
    public MenuFunctionAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override // com.meishe.myvideo.view.menu.adapter.MenuBaseAdapter, com.meishe.myvideo.view.menu.adapter.MenuBaseAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public BaseMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 100) {
            return new MenuNorViewHolder(this.mLayoutInflater.inflate(R.layout.view_bottom_menu_normal, (ViewGroup) null));
        }
        if (i != 101) {
            return null;
        }
        return new MenuFillViewHolder(this.mLayoutInflater.inflate(R.layout.view_bottom_menu_fill, (ViewGroup) null));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return ((BaseInfo) this.mList.get(i)) instanceof MenuInfo ? 100 : 101;
    }
}
