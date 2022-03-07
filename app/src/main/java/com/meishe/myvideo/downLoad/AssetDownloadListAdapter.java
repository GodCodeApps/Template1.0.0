package com.meishe.myvideo.downLoad;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideoapp.R;
import java.io.File;
import java.util.ArrayList;

public class AssetDownloadListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int LOADING = 1;
    public static final int LOADING_COMPLETE = 2;
    public static final int LOADING_END = 4;
    public static final int LOADING_FAILED = 3;
    private final int TYPE_FOOTER = 2;
    private final int TYPE_ITEM = 1;
    private int curTimelineRatio = 0;
    private int currentLoadState = 2;
    private ArrayList<NvAssetInfo> mAssetDataList = new ArrayList<>();
    private int mAssetType = 0;
    private Context mContext;
    private OnDownloadClickListener mDownloadClickerListener = null;

    public interface OnDownloadClickListener {
        void onItemDownloadClick(RecyclerViewHolder recyclerViewHolder, int i);
    }

    public class DownloadButtonInfo {
        int buttonBackgroud;
        String buttonText;
        String buttonTextColor;

        public DownloadButtonInfo() {
        }
    }

    public AssetDownloadListAdapter(Context context) {
        this.mContext = context;
    }

    public void setAssetType(int i) {
        this.mAssetType = i;
    }

    public void setCurTimelineRatio(int i) {
        this.curTimelineRatio = i;
    }

    public void setAssetDatalist(ArrayList<NvAssetInfo> arrayList) {
        this.mAssetDataList = arrayList;
        Log.e("Datalist", "DataCount = " + this.mAssetDataList.size());
    }

    public void setDownloadClickerListener(OnDownloadClickListener onDownloadClickListener) {
        this.mDownloadClickerListener = onDownloadClickListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return i + 1 == getItemCount() ? 2 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new RecyclerViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_asset_download, viewGroup, false));
        }
        if (i == 2) {
            return new FootViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_asset_download_footer, viewGroup, false));
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) viewHolder;
            final NvAssetInfo nvAssetInfo = this.mAssetDataList.get(i);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.circleCrop();
            requestOptions.placeholder(R.mipmap.bank_thumbnail_local);
            Glide.with(this.mContext).asBitmap().load(nvAssetInfo.coverUrl).apply(requestOptions).into(recyclerViewHolder.mAssetCover);
            recyclerViewHolder.mAssetName.setText(nvAssetInfo.mName);
            if (this.mAssetType != 15) {
                recyclerViewHolder.assetCover_type_image.setVisibility(4);
            } else if (nvAssetInfo.categoryId > BaseConstants.PROP_IMAGES.length || nvAssetInfo.categoryId - 1 < 0) {
                recyclerViewHolder.assetCover_type_image.setVisibility(4);
            } else {
                recyclerViewHolder.assetCover_type_image.setVisibility(0);
                recyclerViewHolder.assetCover_type_image.setBackground(this.mContext.getResources().getDrawable(BaseConstants.PROP_IMAGES[nvAssetInfo.categoryId - 1]));
            }
            if (this.mAssetType == 4) {
                recyclerViewHolder.mAssetRatio.setText(R.string.asset_ratio);
            } else {
                recyclerViewHolder.mAssetRatio.setText(getAssetRatio(nvAssetInfo.aspectRatio));
            }
            recyclerViewHolder.mAssetSize.setText(getAssetSize(nvAssetInfo.remotePackageSize));
            DownloadButtonInfo downloadButtonInfo = getDownloadButtonInfo(nvAssetInfo);
            recyclerViewHolder.mDownloadButton.setBackgroundResource(downloadButtonInfo.buttonBackgroud);
            recyclerViewHolder.mDownloadButton.setText(downloadButtonInfo.buttonText);
            recyclerViewHolder.mDownloadButton.setTextColor(Color.parseColor(downloadButtonInfo.buttonTextColor));
            recyclerViewHolder.mDownloadProgressBar.setVisibility(8);
            recyclerViewHolder.mDownloadButton.setVisibility(0);
            if (nvAssetInfo.downloadStatus == 5) {
                recyclerViewHolder.mDownloadButton.setText(R.string.retry);
                recyclerViewHolder.mDownloadButton.setTextColor(Color.parseColor("#ffffffff"));
                recyclerViewHolder.mDownloadButton.setBackgroundResource(R.drawable.download_button_shape_corner_retry);
                recyclerViewHolder.mDownloadProgressBar.setVisibility(8);
                recyclerViewHolder.mDownloadButton.setVisibility(0);
            } else if (nvAssetInfo.downloadStatus == 4) {
                recyclerViewHolder.mDownloadProgressBar.setVisibility(8);
                recyclerViewHolder.mDownloadButton.setVisibility(0);
            } else if (nvAssetInfo.downloadStatus == 2) {
                recyclerViewHolder.mDownloadProgressBar.setVisibility(0);
                recyclerViewHolder.mDownloadProgressBar.setProgress(nvAssetInfo.downloadProgress);
                recyclerViewHolder.mDownloadButton.setVisibility(8);
            }
            recyclerViewHolder.mDownloadButton.setOnClickListener(new View.OnClickListener() {
                /* class com.meishe.myvideo.downLoad.AssetDownloadListAdapter.AnonymousClass1 */

                public void onClick(View view) {
                    if (AssetDownloadListAdapter.this.curTimelineRatio >= 1 && AssetDownloadListAdapter.this.mAssetType != 4 && (AssetDownloadListAdapter.this.curTimelineRatio & nvAssetInfo.aspectRatio) == 0) {
                        return;
                    }
                    if (!nvAssetInfo.isUsable() || nvAssetInfo.hasUpdate()) {
                        if (nvAssetInfo.isUsable() && nvAssetInfo.hasRemoteAsset() && nvAssetInfo.hasUpdate()) {
                            File file = new File(nvAssetInfo.localDirPath);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                        if (AssetDownloadListAdapter.this.mDownloadClickerListener != null) {
                            AssetDownloadListAdapter.this.mDownloadClickerListener.onItemDownloadClick(recyclerViewHolder, i);
                        }
                    }
                }
            });
        } else if (viewHolder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
            int i2 = this.currentLoadState;
            if (i2 == 1) {
                footViewHolder.mLoadLayout.setVisibility(0);
                footViewHolder.mLoadFailTips.setVisibility(8);
            } else if (i2 == 2) {
                footViewHolder.mLoadLayout.setVisibility(4);
                footViewHolder.mLoadFailTips.setVisibility(8);
            } else if (i2 == 3) {
                footViewHolder.mLoadLayout.setVisibility(8);
                footViewHolder.mLoadFailTips.setVisibility(0);
            } else if (i2 == 4) {
                footViewHolder.mLoadLayout.setVisibility(8);
                footViewHolder.mLoadFailTips.setVisibility(8);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mAssetDataList.size() + 1;
    }

    private String getAssetRatio(int i) {
        int length = BaseInfo.RatioArray.length;
        String str = "";
        for (int i2 = 0; i2 < length; i2++) {
            if ((BaseInfo.RatioArray[i2] & i) != 0) {
                if (i2 != length - 1) {
                    str = (str + BaseInfo.RatioStringArray[i2]) + " ";
                } else if (i >= BaseInfo.RatioArray[i2]) {
                    str = this.mContext.getResources().getString(R.string.asset_ratio);
                }
            }
        }
        return str;
    }

    private String getAssetSize(int i) {
        int i2 = i / 1024;
        int i3 = i2 / 1024;
        int i4 = i2 % 1024;
        double d = (double) i4;
        Double.isNaN(d);
        float f = (float) (d / 1024.0d);
        if (i3 > 0) {
            String format = String.format("%.1f", Float.valueOf(((float) i3) + f));
            return format + "M";
        }
        String format2 = String.format("%d", Integer.valueOf(i4));
        return format2 + "K";
    }

    private DownloadButtonInfo getDownloadButtonInfo(NvAssetInfo nvAssetInfo) {
        DownloadButtonInfo downloadButtonInfo = new DownloadButtonInfo();
        int i = this.curTimelineRatio;
        if (i >= 1 && this.mAssetType != 4 && (i & nvAssetInfo.aspectRatio) == 0) {
            downloadButtonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_finished;
            downloadButtonInfo.buttonText = this.mContext.getResources().getString(R.string.asset_mismatch);
            downloadButtonInfo.buttonTextColor = "#ff928c8c";
        } else if (!nvAssetInfo.isUsable() && nvAssetInfo.hasRemoteAsset()) {
            downloadButtonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_download;
            downloadButtonInfo.buttonText = this.mContext.getResources().getString(R.string.asset_download);
            downloadButtonInfo.buttonTextColor = "#ffffffff";
        } else if (nvAssetInfo.isUsable() && !nvAssetInfo.hasUpdate()) {
            downloadButtonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_finished;
            downloadButtonInfo.buttonText = this.mContext.getResources().getString(R.string.asset_downloadfinished);
            downloadButtonInfo.buttonTextColor = "#ff909293";
        } else if (nvAssetInfo.isUsable() && nvAssetInfo.hasRemoteAsset() && nvAssetInfo.hasUpdate()) {
            downloadButtonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_update;
            downloadButtonInfo.buttonText = this.mContext.getResources().getString(R.string.asset_update);
            downloadButtonInfo.buttonTextColor = "#ffffffff";
        }
        return downloadButtonInfo;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView assetCover_type_image;
        ImageView mAssetCover;
        TextView mAssetName;
        TextView mAssetRatio;
        TextView mAssetSize;
        Button mDownloadButton;
        DownloadProgressBar mDownloadProgressBar;

        RecyclerViewHolder(View view) {
            super(view);
            this.mAssetCover = (ImageView) view.findViewById(R.id.assetCover);
            this.assetCover_type_image = (ImageView) view.findViewById(R.id.assetCover_type_image);
            this.mAssetName = (TextView) view.findViewById(R.id.assetName);
            this.mAssetRatio = (TextView) view.findViewById(R.id.assetRatio);
            this.mAssetSize = (TextView) view.findViewById(R.id.assetSize);
            this.mDownloadButton = (Button) view.findViewById(R.id.download_button);
            this.mDownloadProgressBar = (DownloadProgressBar) view.findViewById(R.id.downloadProgressBar);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        FrameLayout mLoadFailTips;
        LinearLayout mLoadLayout;

        FootViewHolder(View view) {
            super(view);
            this.mLoadLayout = (LinearLayout) view.findViewById(R.id.loadLayout);
            this.mLoadFailTips = (FrameLayout) view.findViewById(R.id.loadFailTips);
        }
    }

    public void setLoadState(int i) {
        this.currentLoadState = i;
        notifyDataSetChanged();
    }

    public void updateDownloadItems() {
        notifyDataSetChanged();
    }
}
