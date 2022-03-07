package com.meishe.myvideo.view.menu.holder;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class MenuNorViewHolder extends BaseMenuViewHolder {
    private CheckBox mIcon;
    private View mRootView;
    private TextView menuName;

    public MenuNorViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mRootView = view;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.menu.holder.BaseMenuViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIcon = (CheckBox) view.findViewById(R.id.menu_icon);
        this.menuName = (TextView) view.findViewById(R.id.menu_name);
    }

    @Override // com.meishe.myvideo.view.menu.holder.BaseMenuViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, boolean z, View.OnClickListener onClickListener) {
        this.mIcon.setClickable(baseInfo.checkAble);
        this.mIcon.setBackgroundResource(baseInfo.mIconRcsId);
        this.menuName.setText(baseInfo.mName);
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
    }
}
