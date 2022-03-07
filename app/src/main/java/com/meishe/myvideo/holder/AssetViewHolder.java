package com.meishe.myvideo.holder;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;
import java.io.File;

public class AssetViewHolder extends BaseViewHolder {
    private RelativeLayout mAdd;
    private ImageView mIvEffect;
    private RelativeLayout mMore;
    private RequestOptions mOptions = new RequestOptions();
    private View mRootView;

    public AssetViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mOptions.centerCrop();
        this.mRootView = view;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIvEffect = (ImageView) view.findViewById(R.id.iv_effect);
        this.mMore = (RelativeLayout) view.findViewById(R.id.rl_more);
        this.mAdd = (RelativeLayout) view.findViewById(R.id.rl_add);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        if (context.getResources().getString(R.string.more).equals(baseInfo.mName)) {
            this.mMore.setVisibility(0);
            this.mIvEffect.setVisibility(8);
            this.mAdd.setVisibility(8);
        } else if (context.getResources().getString(R.string.add).equals(baseInfo.mName)) {
            this.mMore.setVisibility(8);
            this.mIvEffect.setVisibility(8);
            this.mAdd.setVisibility(0);
        } else {
            this.mMore.setVisibility(8);
            this.mIvEffect.setVisibility(0);
            this.mAdd.setVisibility(8);
            if (baseInfo.mEffectType == 22) {
                File file = new File(baseInfo.getFilePath());
                if (file.exists()) {
                    Glide.with(this.mRootView.getContext()).asBitmap().load(Uri.fromFile(file)).apply(this.mOptions).into(this.mIvEffect);
                } else {
                    return;
                }
            } else {
                Glide.with(this.mRootView.getContext()).asBitmap().load(baseInfo.getCoverUrl()).apply(this.mOptions).into(this.mIvEffect);
            }
        }
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
    }
}
