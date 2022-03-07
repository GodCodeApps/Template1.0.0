package com.meishe.myvideo.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class BeautyShapeViewHolder extends BaseViewHolder {
    private ImageView mIcon;
    private RequestOptions mOptions = new RequestOptions();
    private RelativeLayout mRlBeautyContainer;
    private View mRootView;
    private TextView mTvName;

    public BeautyShapeViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mOptions.centerCrop();
        this.mRootView = view;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIcon = (ImageView) view.findViewById(R.id.iv_icon);
        this.mTvName = (TextView) view.findViewById(R.id.tv_name);
        this.mRlBeautyContainer = (RelativeLayout) view.findViewById(R.id.rl_beauty_container);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        this.mTvName.setText(baseInfo.mName);
        this.mIcon.setBackgroundResource(baseInfo.mIconRcsId);
        this.mRlBeautyContainer.setSelected(z);
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
    }
}
