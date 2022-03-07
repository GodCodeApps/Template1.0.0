package com.meishe.myvideo.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class CaptionColorViewHolder extends BaseViewHolder {
    private ImageView mIvColor;
    private View mMask;

    public CaptionColorViewHolder(@NonNull View view) {
        super(view, new Object[0]);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIvColor = (ImageView) view.findViewById(R.id.iv_color);
        this.mMask = view.findViewById(R.id.mask);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        this.mIvColor.setBackgroundColor(Color.parseColor(baseInfo.getColorValue()));
        this.mMask.setVisibility(z ? 0 : 4);
        this.mIvColor.setTag(baseInfo);
        this.mIvColor.setOnClickListener(onClickListener);
    }
}
