package com.meishe.myvideo.adapter;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.bean.BeautyShapeInfo;
import com.meishe.myvideo.bean.CanvasBlurInfo;
import com.meishe.myvideo.bean.CanvasStyleInfo;
import com.meishe.myvideo.bean.CaptionColorInfo;
import com.meishe.myvideo.bean.CaptionStyleInfo;
import com.meishe.myvideo.bean.ComCaptionInfo;
import com.meishe.myvideo.bean.EditAdjustInfo;
import com.meishe.myvideo.bean.EffectInfo;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideo.bean.TransitionInfo;
import com.meishe.myvideo.bean.WaterMarkEffectInfo;
import com.meishe.myvideo.bean.WaterMarkInfo;
import com.meishe.myvideo.bean.menu.BeautyShapeDataItem;
import com.meishe.myvideo.bean.menu.EditMenuInfo;
import com.meishe.myvideo.holder.AssetViewHolder;
import com.meishe.myvideo.holder.BaseViewHolder;
import com.meishe.myvideo.holder.BeautyShapeViewHolder;
import com.meishe.myvideo.holder.CanvasBlurViewHolder;
import com.meishe.myvideo.holder.CanvasStyleViewHolder;
import com.meishe.myvideo.holder.CaptionColorViewHolder;
import com.meishe.myvideo.holder.CaptionStyleViewHolder;
import com.meishe.myvideo.holder.CompoundCaptionViewHolder;
import com.meishe.myvideo.holder.EditAdjustViewHolder;
import com.meishe.myvideo.holder.EditMenuViewHolder;
import com.meishe.myvideo.holder.EffectMenuViewHolder;
import com.meishe.myvideo.holder.TransitionMenuViewHolder;
import com.meishe.myvideo.holder.WaterMarkEffectViewHolder;
import com.meishe.myvideo.holder.WaterMarkViewHolder;
import com.meishe.myvideoapp.R;

public class MultiFunctionAdapter extends BaseRecyclerAdapter {
    public MultiFunctionAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter, com.meishe.myvideo.adapter.BaseRecyclerAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case 101:
                return new EditMenuViewHolder(this.mLayoutInflater.inflate(R.layout.view_menu_item, (ViewGroup) null));
            case 102:
                return new EffectMenuViewHolder(this.mLayoutInflater.inflate(R.layout.view_menu_effect_item, (ViewGroup) null));
            case 103:
                return new CaptionStyleViewHolder(this.mLayoutInflater.inflate(R.layout.view_menu_caption_style_item, (ViewGroup) null));
            case 104:
                return new CaptionColorViewHolder(this.mLayoutInflater.inflate(R.layout.view_caption_color_item, (ViewGroup) null));
            case 105:
                return new AssetViewHolder(this.mLayoutInflater.inflate(R.layout.view_asset_item, (ViewGroup) null));
            case 106:
                return new WaterMarkEffectViewHolder(this.mLayoutInflater.inflate(R.layout.water_mark_effect_item, (ViewGroup) null));
            case 107:
                return new WaterMarkViewHolder(this.mLayoutInflater.inflate(R.layout.water_mark_item, (ViewGroup) null));
            case 108:
                return new CompoundCaptionViewHolder(this.mLayoutInflater.inflate(R.layout.compound_caption_item, (ViewGroup) null));
            case 109:
            default:
                return new EditMenuViewHolder(this.mLayoutInflater.inflate(R.layout.view_menu_item, (ViewGroup) null));
            case 110:
                return new CanvasStyleViewHolder(this.mLayoutInflater.inflate(R.layout.canvas_style_item, (ViewGroup) null));
            case 111:
                return new CanvasBlurViewHolder(this.mLayoutInflater.inflate(R.layout.canvas_blur_item, (ViewGroup) null));
            case 112:
                return new EditAdjustViewHolder(this.mLayoutInflater.inflate(R.layout.view_adjust_item, (ViewGroup) null));
            case 113:
                return new BeautyShapeViewHolder(this.mLayoutInflater.inflate(R.layout.beauty_shape_item, (ViewGroup) null));
            case 114:
                return new TransitionMenuViewHolder(this.mLayoutInflater.inflate(R.layout.view_menu_transition_item, (ViewGroup) null));
        }
    }

    @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        super.onBindViewHolder(baseViewHolder, i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (this.mList.get(i) instanceof EditMenuInfo) {
            return 101;
        }
        if (this.mList.get(i) instanceof EffectInfo) {
            return 102;
        }
        if (this.mList.get(i) instanceof BeautyShapeDataItem) {
            return 101;
        }
        if (this.mList.get(i) instanceof CaptionStyleInfo) {
            return 103;
        }
        if (this.mList.get(i) instanceof CaptionColorInfo) {
            return 104;
        }
        if (this.mList.get(i) instanceof NvAssetInfo) {
            return 105;
        }
        if (this.mList.get(i) instanceof WaterMarkEffectInfo) {
            return 106;
        }
        if (this.mList.get(i) instanceof WaterMarkInfo) {
            return 107;
        }
        if (this.mList.get(i) instanceof ComCaptionInfo) {
            return 108;
        }
        if (this.mList.get(i) instanceof CanvasStyleInfo) {
            return 110;
        }
        if (this.mList.get(i) instanceof CanvasBlurInfo) {
            return 111;
        }
        if (this.mList.get(i) instanceof EditAdjustInfo) {
            return 112;
        }
        if (this.mList.get(i) instanceof BeautyShapeInfo) {
            return 113;
        }
        if (this.mList.get(i) instanceof TransitionInfo) {
            return 114;
        }
        return 101;
    }
}
