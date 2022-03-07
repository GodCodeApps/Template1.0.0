package com.meishe.myvideo.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class EffectMenuViewHolder extends BaseViewHolder {
    private ImageView mIcon;
    private ImageView mIvSelectBg;
    private ImageView mIvSelectBgNo;
    private TextView mName;
    private RequestOptions mOptions = new RequestOptions();
    private RelativeLayout mRlMore;
    private RelativeLayout mRlNo;
    private View mRootView;
    private View mllContent;

    public EffectMenuViewHolder(@NonNull View view) {
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
        this.mRlNo = (RelativeLayout) view.findViewById(R.id.rl_no);
        this.mRlMore = (RelativeLayout) view.findViewById(R.id.rl_more);
        this.mName = (TextView) view.findViewById(R.id.name);
        this.mllContent = view.findViewById(R.id.rl_content);
        this.mIvSelectBgNo = (ImageView) view.findViewById(R.id.iv_select_bg_no);
        this.mIvSelectBg = (ImageView) view.findViewById(R.id.iv_select_bg);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        if (context.getResources().getString(R.string.more).equals(baseInfo.mName)) {
            this.mRlNo.setVisibility(8);
            this.mRlMore.setVisibility(0);
            this.mllContent.setVisibility(8);
        } else if (context.getResources().getString(R.string.no).equals(baseInfo.mName)) {
            this.mRlNo.setVisibility(0);
            this.mRlMore.setVisibility(8);
            this.mllContent.setVisibility(8);
        } else {
            this.mRlNo.setVisibility(8);
            this.mRlMore.setVisibility(8);
            this.mllContent.setVisibility(0);
            if (baseInfo.mEffectMode == BaseInfo.EFFECT_MODE_BUILTIN) {
                this.mIcon.setImageResource(baseInfo.mIconRcsId);
                this.mIcon.setScaleType(ImageView.ScaleType.CENTER);
            } else {
                String str = baseInfo.mIconUrl;
                if (str != null) {
                    Glide.with(context).asBitmap().load(str).apply(this.mOptions).into(this.mIcon);
                }
            }
            this.mIcon.setScaleType(ImageView.ScaleType.FIT_XY);
            this.mName.setText(baseInfo.mName);
        }
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
        if (z) {
            this.mIvSelectBg.setBackgroundResource(R.color.red_half_trans);
            this.mIvSelectBgNo.setBackgroundResource(R.color.adjust_selected_bg);
            return;
        }
        this.mIvSelectBg.setBackgroundResource(R.color.colorTranslucent);
        this.mIvSelectBgNo.setBackgroundResource(R.color.colorTranslucent);
    }
}
