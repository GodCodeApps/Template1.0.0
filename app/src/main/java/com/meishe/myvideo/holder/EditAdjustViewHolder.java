package com.meishe.myvideo.holder;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class EditAdjustViewHolder extends BaseViewHolder {
    private CheckBox mIcon;
    private TextView mName;
    private View mRootView;

    public EditAdjustViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mRootView = view;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIcon = (CheckBox) view.findViewById(R.id.icon);
        this.mName = (TextView) view.findViewById(R.id.name);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        this.mIcon.setBackgroundResource(baseInfo.mIconRcsId);
        this.mIcon.setClickable(false);
        this.mName.setText(baseInfo.mName);
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
        if (z) {
            this.mIcon.setChecked(true);
            this.mName.setTextColor(context.getResources().getColor(R.color.adjust_selected_bg));
            return;
        }
        this.mIcon.setChecked(false);
        this.mName.setTextColor(context.getResources().getColor(R.color.white_8));
    }
}
