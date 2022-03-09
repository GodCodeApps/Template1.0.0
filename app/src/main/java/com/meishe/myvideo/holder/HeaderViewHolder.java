package com.meishe.myvideo.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import com.meishe.myvideo.interfaces.OnItemClick;
import com.meishe.myvideoapp.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    private int clickType;
    private int mLimitMediaCount;
    private TextView select_all;
    private TextView titleText = null;

    public HeaderViewHolder(View view, @IdRes int i, int i2, int i3) {
        super(view);
        this.titleText = (TextView) view.findViewById(i);
        this.select_all = (TextView) view.findViewById(R.id.meida_head_selectAll);
        this.clickType = i2;
        this.mLimitMediaCount = i3;
    }

    public void render(String str, boolean z) {
        this.titleText.setText(str);
        int i = this.clickType;
        if (i == 1) {
            if (this.mLimitMediaCount >= 0) {
                this.select_all.setText("");
                return;
            }
            String string = itemView.getContext().getResources().getString(R.string.checkAll);
            String string2 = titleText.getContext().getResources().getString(R.string.cancelCheckAll);
            TextView textView = this.select_all;
            if (z) {
                string = string2;
            }
            textView.setText(string);
        } else if (i == 0) {
            this.select_all.setText("");
        }
    }

    public void onClick(final int i, final OnItemClick onItemClick) {
        this.select_all.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.holder.HeaderViewHolder.AnonymousClass1 */

            public void onClick(View view) {
                onItemClick.OnHeadClick(HeaderViewHolder.this.itemView, i);
            }
        });
    }
}
