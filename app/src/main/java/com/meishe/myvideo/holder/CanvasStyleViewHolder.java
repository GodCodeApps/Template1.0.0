package com.meishe.myvideo.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;

public class CanvasStyleViewHolder extends BaseViewHolder {
    private ImageView mIcon;
    private View mMark;
    private RequestOptions mOptions = new RequestOptions();
    private View mRootView;

    public CanvasStyleViewHolder(@NonNull View view) {
        super(view, new Object[0]);
        this.mRootView = view;
        this.mOptions.skipMemoryCache(false);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void initViewHolder(View view, Object... objArr) {
        this.mIcon = (ImageView) view.findViewById(R.id.iv_pic);
        this.mMark = view.findViewById(R.id.iv_mark);
    }

    @Override // com.meishe.myvideo.holder.BaseViewHolder
    public void bindViewHolder(Context context, BaseInfo baseInfo, int i, boolean z, View.OnClickListener onClickListener) {
        Object obj;
        String filePath = baseInfo.getFilePath();
        RequestBuilder<Bitmap> asBitmap = Glide.with(context.getApplicationContext()).asBitmap();
        if (TextUtils.isEmpty(filePath)) {
            obj = Integer.valueOf(baseInfo.mIconRcsId);
        } else {
            obj = "file:///android_asset/background/image/" + filePath;
        }
        asBitmap.load(obj).apply(this.mOptions).into(this.mIcon);
        this.mRootView.setTag(baseInfo);
        this.mRootView.setOnClickListener(onClickListener);
        int i2 = 8;
        if (i == 0) {
            this.mMark.setVisibility(8);
            return;
        }
        View view = this.mMark;
        if (z) {
            i2 = 0;
        }
        view.setVisibility(i2);
    }
}
