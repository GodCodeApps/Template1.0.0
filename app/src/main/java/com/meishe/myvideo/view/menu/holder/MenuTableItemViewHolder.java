package com.meishe.myvideo.view.menu.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class MenuTableItemViewHolder extends BaseMenuViewHolder {
    private ImageView mIcon;
    private View mRootView;
    private ImageView selectIcon;

    public MenuTableItemViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mRootView = view;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.menu.holder.BaseMenuViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIcon = (ImageView) view.findViewById(R.id.menu_icon);
        this.selectIcon = (ImageView) view.findViewById(R.id.menu_select_bg);
    }

    @Override // com.meishe.myvideo.view.menu.holder.BaseMenuViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, boolean z, View.OnClickListener onClickListener) {
        this.mIcon.setImageResource(baseInfo.mIconRcsId);
        this.mIcon.setScaleType(ImageView.ScaleType.CENTER);
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
    }
}
