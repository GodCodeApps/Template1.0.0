package com.meishe.myvideo.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.ui.Utils.BorderRadiusDrawableUtil;
import com.meishe.myvideoapp.R;

public class CaptionStyleViewHolder extends BaseViewHolder {
    private ImageView mIcon;
    private RequestOptions mOptions = new RequestOptions();
    private RelativeLayout mRlMore;
    private RelativeLayout mRlNo;
    private View mRootView;
    private View mViewBorder;

    public CaptionStyleViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mRootView = view;
        this.mOptions.centerCrop();
        this.mOptions.skipMemoryCache(false);
        this.mOptions.placeholder(R.mipmap.ic_no);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIcon = (ImageView) view.findViewById(R.id.icon);
        this.mRlMore = (RelativeLayout) view.findViewById(R.id.rl_more);
        this.mRlNo = (RelativeLayout) view.findViewById(R.id.rl_no);
        this.mViewBorder = view.findViewById(R.id.view_border);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        if (context.getResources().getString(R.string.more).equals(baseInfo.mName)) {
            this.mRlNo.setVisibility(8);
            this.mRlMore.setVisibility(0);
            this.mIcon.setVisibility(8);
        } else if (context.getResources().getString(R.string.top_menu_no).equals(baseInfo.mName)) {
            this.mRlNo.setVisibility(0);
            this.mRlMore.setVisibility(8);
            this.mIcon.setVisibility(8);
        } else {
            this.mRlNo.setVisibility(8);
            this.mRlMore.setVisibility(8);
            this.mIcon.setVisibility(0);
            String str = baseInfo.getAsset().coverUrl;
            if (str != null) {
                Glide.with(context).asBitmap().load(str).apply(this.mOptions).into(this.mIcon);
            }
        }
        if (z) {
            this.mViewBorder.setBackground(BorderRadiusDrawableUtil.getRadiusDrawable(context.getResources().getDimensionPixelOffset(R.dimen.dp1), SupportMenu.CATEGORY_MASK, context.getResources().getDimensionPixelOffset(R.dimen.dp4), 0));
        } else {
            this.mViewBorder.setBackground(BorderRadiusDrawableUtil.getRadiusDrawable(context.getResources().getDimensionPixelOffset(R.dimen.dp1), context.getResources().getColor(R.color.animate_sticker_bottom_bg), context.getResources().getDimensionPixelOffset(R.dimen.dp4), 0));
        }
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
    }
}
