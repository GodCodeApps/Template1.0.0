package com.meishe.myvideo.holder;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class CanvasBlurViewHolder extends BaseViewHolder {
    private View mMask;
    private RequestOptions mOptions = new RequestOptions();
    private RelativeLayout mRlContent;
    private View mRootView;
    private TextView mTvContent;

    public CanvasBlurViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mRootView = view;
        this.mOptions.skipMemoryCache(false);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mTvContent = (TextView) view.findViewById(R.id.tv_content);
        this.mRlContent = (RelativeLayout) view.findViewById(R.id.rl_content);
        this.mMask = view.findViewById(R.id.mask);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        int i2 = 8;
        if (context.getResources().getString(R.string.top_menu_no).equals(baseInfo.mName)) {
            this.mRlContent.setBackgroundResource(R.mipmap.ic_blur_no);
            this.mTvContent.setVisibility(8);
        } else {
            this.mRlContent.setBackgroundResource(R.mipmap.ic_blur_strength_bg);
            this.mTvContent.setVisibility(0);
            this.mTvContent.setText(baseInfo.mName);
        }
        View view = this.mMask;
        if (z) {
            i2 = 0;
        }
        view.setVisibility(i2);
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
    }
}
