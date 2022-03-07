package com.meishe.myvideo.view.menu.holder;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.bean.BaseInfo;

public abstract class BaseMenuViewHolder extends RecyclerView.ViewHolder {
    public abstract void bindViewHolder(Context context, BaseInfo baseInfo, boolean z, View.OnClickListener onClickListener);

    /* access modifiers changed from: protected */
    public abstract void initViewHolder(View view, Object... objArr);

    public BaseMenuViewHolder(@NonNull View view, Object... objArr) {
        super(view);
        initViewHolder(view, objArr);
    }
}
