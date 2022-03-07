package com.meishe.myvideo.holder;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.bean.BaseInfo;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public abstract void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener);

    /* access modifiers changed from: protected */
    public abstract void initViewHolder(View view, Object... objArr);

    public BaseViewHolder(@NonNull View view, Object... objArr) {
        super(view);
        initViewHolder(view, objArr);
    }
}
