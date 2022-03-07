package com.meishe.myvideo.view.menu.adapter;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.view.menu.bean.TableFunInfo;
import com.meishe.myvideo.view.menu.bean.TableInfo;
import com.meishe.myvideo.view.menu.holder.BaseMenuViewHolder;
import com.meishe.myvideo.view.menu.holder.MenuTableItemViewHolder;
import com.meishe.myvideo.view.menu.holder.MenuTableViewHolder;
import com.meishe.myvideoapp.R;

public class MenuTableAdapter extends MenuBaseAdapter {
    public MenuTableAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override // com.meishe.myvideo.view.menu.adapter.MenuBaseAdapter, com.meishe.myvideo.view.menu.adapter.MenuBaseAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public BaseMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 102) {
            return new MenuTableViewHolder(this.mLayoutInflater.inflate(R.layout.view_table_menu_fun, (ViewGroup) null));
        }
        if (i != 103) {
            return null;
        }
        return new MenuTableItemViewHolder(this.mLayoutInflater.inflate(R.layout.view_table_menu_item, (ViewGroup) null));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        BaseInfo item = getItem(i);
        if (item instanceof TableInfo) {
            return 103;
        }
        if (item instanceof TableFunInfo) {
            return 102;
        }
        return super.getItemViewType(i);
    }
}
