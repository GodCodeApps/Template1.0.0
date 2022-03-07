package com.meishe.myvideo.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.WaterMarkInfo;
import com.meishe.myvideoapp.R;

public class WaterMarkViewHolder extends BaseViewHolder {
    private RelativeLayout mAdd;
    private ImageView mImageCover;
    private ImageView mIvWaterMark;
    private RequestOptions mOptions = new RequestOptions();
    private View mRootView;
    private RelativeLayout realImage;

    public WaterMarkViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mOptions.centerCrop();
        this.mRootView = view;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIvWaterMark = (ImageView) view.findViewById(R.id.iv_water_mark);
        this.realImage = (RelativeLayout) view.findViewById(R.id.real_image);
        this.mImageCover = (ImageView) view.findViewById(R.id.image_cover);
        this.mAdd = (RelativeLayout) view.findViewById(R.id.rl_add);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        WaterMarkInfo waterMarkInfo = (WaterMarkInfo) baseInfo;
        if (!z || i == 0) {
            this.mImageCover.setVisibility(8);
        } else {
            this.mImageCover.setVisibility(0);
        }
        if (context.getResources().getString(R.string.add).equals(baseInfo.mName)) {
            this.realImage.setVisibility(8);
            this.mAdd.setVisibility(0);
        } else if (waterMarkInfo.mPicType == 0) {
            this.realImage.setVisibility(0);
            this.mAdd.setVisibility(8);
            Glide.with(this.mRootView.getContext()).asBitmap().load(waterMarkInfo.mPicPath).apply(this.mOptions).into(this.mIvWaterMark);
        }
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
    }
}
