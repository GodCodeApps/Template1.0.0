package com.meishe.myvideo.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class WaterMarkEffectViewHolder extends BaseViewHolder {
    private ImageView mIcon;
    private ImageView mImageCover;
    private ImageView mIvSelectBg;
    private TextView mName;
    private View mRootView;
    private View mllSelect;

    public WaterMarkEffectViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mRootView = view;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIcon = (ImageView) view.findViewById(R.id.iv_effect);
        this.mName = (TextView) view.findViewById(R.id.tv_name);
        this.mImageCover = (ImageView) view.findViewById(R.id.image_cover);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        if (z) {
            this.mImageCover.setVisibility(0);
        } else {
            this.mImageCover.setVisibility(8);
        }
        this.mIcon.setImageResource(baseInfo.mIconRcsId);
        this.mIcon.setScaleType(ImageView.ScaleType.CENTER);
        this.mName.setText(baseInfo.mName);
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
    }
}
