package com.meishe.myvideo.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.application.MeiSheApplication;
import com.meishe.myvideo.bean.MediaData;
import com.meishe.myvideo.interfaces.OnItemClick;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideo.util.TimeUtil;
import com.meishe.myvideoapp.R;

public class AgendaItemViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout item_media_hideLayout;
    ImageView iv_item_image;
    private int mClickType;
    TextView textView;
    TextView tv_selected_num;

    public AgendaItemViewHolder(View view, int i) {
        super(view);
        this.mClickType = i;
        this.textView = (TextView) view.findViewById(R.id.tv_media_type);
        this.tv_selected_num = (TextView) view.findViewById(R.id.tv_selected_num);
        this.iv_item_image = (ImageView) view.findViewById(R.id.iv_item_image);
        this.item_media_hideLayout = (RelativeLayout) view.findViewById(R.id.item_media_hideLayout);
    }

    public void render(MediaData mediaData, final int i, final int i2, final OnItemClick onItemClick) {
        int windowWidth = (((ScreenUtils.getWindowWidth(MeiSheApplication.getContext()) - (((int) MeiSheApplication.getContext().getResources().getDimension(R.dimen.select_recycle_marginLeftAndRight)) * 2)) - (((int) MeiSheApplication.getContext().getResources().getDimension(R.dimen.select_item_start_end)) * 2)) - (((int) MeiSheApplication.getContext().getResources().getDimension(R.dimen.select_item_between)) * 3)) / 4;
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(windowWidth, windowWidth);
        int dip2px = ScreenUtils.dip2px(MeiSheApplication.getContext(), 4.0f) / 2;
        int i3 = 0;
        if (i2 < 4) {
            dip2px = 0;
        }
        layoutParams.setMargins(0, dip2px, 0, dip2px);
        this.itemView.setLayoutParams(layoutParams);
        int i4 = 1;
        if (mediaData.getType() == 1) {
            this.textView.setVisibility(0);
            this.textView.getPaint().setFlags(8);
            this.textView.getPaint().setAntiAlias(true);
            TextView textView2 = this.textView;
            if (((int) (mediaData.getDuration() / 1000)) >= 1) {
                i4 = (int) (mediaData.getDuration() / 1000);
            }
            textView2.setText(TimeUtil.secToTime(i4));
        } else {
            this.textView.setVisibility(8);
        }
        RelativeLayout relativeLayout = this.item_media_hideLayout;
        if (!mediaData.isState()) {
            i3 = 8;
        }
        relativeLayout.setVisibility(i3);
        if (this.mClickType == 0) {
            this.tv_selected_num.setVisibility(8);
        } else {
            TextView textView3 = this.tv_selected_num;
            textView3.setText(mediaData.getPosition() + "");
        }
        setImageByFile(mediaData.getPath(), windowWidth);
        this.itemView.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.holder.AgendaItemViewHolder.AnonymousClass1 */

            public void onClick(View view) {
                onItemClick.OnItemClick(AgendaItemViewHolder.this.itemView, i, i2);
            }
        });
    }

    private void setImageByFile(String str, int i) {
        Glide.with(MeiSheApplication.getContext()).asBitmap().load(str).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.bank_thumbnail_local).dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).override(i, i)).into(this.iv_item_image);
    }
}
