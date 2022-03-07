package com.meishe.myvideo.view.menu.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class MenuFunctionViewHolder extends BaseMenuViewHolder {
    private ImageView mIcon;
    private RequestOptions mOptions = new RequestOptions();
    private View mRootView;
    private TextView menuName;
    private ImageView menuSelectBg;

    public MenuFunctionViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mRootView = view;
        this.mOptions.centerCrop();
        this.mOptions.skipMemoryCache(false);
        this.mOptions.placeholder(R.mipmap.ic_no);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.menu.holder.BaseMenuViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIcon = (ImageView) view.findViewById(R.id.menu_icon);
        this.menuName = (TextView) view.findViewById(R.id.menu_name);
        this.menuSelectBg = (ImageView) view.findViewById(R.id.menu_select_bg);
    }

    @Override // com.meishe.myvideo.view.menu.holder.BaseMenuViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, boolean z, View.OnClickListener onClickListener) {
        if (baseInfo.mEffectMode == BaseInfo.EFFECT_MODE_BUILTIN) {
            this.mIcon.setImageResource(baseInfo.mIconRcsId);
            this.mIcon.setScaleType(ImageView.ScaleType.CENTER);
        } else {
            String str = baseInfo.mIconUrl;
            if (str != null) {
                Glide.with(context).asBitmap().load(str).apply(this.mOptions).into(this.mIcon);
            }
            this.mIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (z) {
            this.menuSelectBg.setVisibility(0);
        } else {
            this.menuSelectBg.setVisibility(4);
        }
        this.mIcon.setBackgroundResource(baseInfo.mIconRcsId);
        this.menuName.setText(baseInfo.mName);
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
    }
}
