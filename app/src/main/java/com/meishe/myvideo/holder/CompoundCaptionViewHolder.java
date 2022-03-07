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

public class CompoundCaptionViewHolder extends BaseViewHolder {
    private RelativeLayout mCardView;
    private ImageView mIvCompoundCaption;
    private RelativeLayout mMore;
    private RequestOptions mOptions = new RequestOptions();
    private View mRootView;
    private View mViewBorder;

    public CompoundCaptionViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mOptions.centerCrop();
        this.mRootView = view;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIvCompoundCaption = (ImageView) view.findViewById(R.id.iv_compound_caption);
        this.mMore = (RelativeLayout) view.findViewById(R.id.rl_more);
        this.mCardView = (RelativeLayout) view.findViewById(R.id.cardView);
        this.mViewBorder = view.findViewById(R.id.view_border);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        if (context.getResources().getString(R.string.more).equals(baseInfo.mName)) {
            this.mCardView.setVisibility(8);
            this.mMore.setVisibility(0);
        } else {
            this.mCardView.setVisibility(0);
            this.mMore.setVisibility(8);
            Glide.with(this.mRootView.getContext()).asBitmap().load(baseInfo.getPicPath()).apply(this.mOptions).into(this.mIvCompoundCaption);
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
