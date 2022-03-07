package com.meishe.myvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meicam.sdk.NvsVideoClip;
import com.meishe.myvideoapp.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EditorTimelineRectCoverAdapter extends RecyclerView.Adapter {
    private static final String TAG = "EditorTimelineRectCoverAdapter";
    private static final int TYPE_FOOT = 2;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_MID = 1;
    private Context mContext;
    private List<Integer> mItemLengthList = new ArrayList();
    private int mLeftPadding;
    private int mMinThumbnailWidth;
    private double mPixelPerMicrosecond;
    private int mRightPadding;
    private int mSelectPosition;
    private List<NvsVideoClip> mVideoClipList = new ArrayList();
    private boolean showWhiteCover = false;

    public EditorTimelineRectCoverAdapter(Context context, int i, int i2, List<NvsVideoClip> list, double d) {
        this.mContext = context;
        this.mLeftPadding = i;
        this.mRightPadding = i2;
        this.mPixelPerMicrosecond = d;
        this.mVideoClipList = list;
        init();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = new View(this.mContext);
        if (i == 0) {
            view.setLayoutParams(new ViewGroup.LayoutParams(this.mLeftPadding, -1));
            return new HeadHolder(view);
        } else if (i == 1) {
            return new MidHolder(LayoutInflater.from(this.mContext).inflate(R.layout.timeline_editor_cover_layout, viewGroup, false));
        } else {
            if (i != 2) {
                return null;
            }
            view.setLayoutParams(new ViewGroup.LayoutParams(this.mRightPadding, -1));
            return new FootHolder(view);
        }
    }

    public void setShowWhiteCover(boolean z) {
        this.showWhiteCover = z;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeadHolder) {
            viewHolder.itemView.setLayoutParams(new ViewGroup.LayoutParams(this.mItemLengthList.get(i).intValue(), -1));
        } else if (viewHolder instanceof MidHolder) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.mItemLengthList.get(i).intValue(), -1);
            layoutParams.width = checkMinItemLength(layoutParams.width);
            viewHolder.itemView.setLayoutParams(layoutParams);
            if (this.mSelectPosition != i || !this.showWhiteCover) {
                viewHolder.itemView.setBackgroundResource(R.drawable.editor_drawable_rect_black_black);
            } else {
                viewHolder.itemView.setBackgroundResource(R.drawable.editor_drawable_corner_white);
            }
            double speed = this.mVideoClipList.get(i - 1).getSpeed();
            if (speed == 1.0d) {
                ((MidHolder) viewHolder).mTextLayout.setVisibility(8);
                return;
            }
            MidHolder midHolder = (MidHolder) viewHolder;
            midHolder.mTextLayout.setVisibility(0);
            DecimalFormat decimalFormat = new DecimalFormat("#0.0");
            TextView textView = midHolder.mTextView;
            textView.setText(decimalFormat.format(speed) + "x");
        }
    }

    private int checkMinItemLength(int i) {
        int i2 = this.mMinThumbnailWidth;
        return i < i2 ? i2 : i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<Integer> list = this.mItemLengthList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (i == 0) {
            return 0;
        }
        return i == getItemCount() - 1 ? 2 : 1;
    }

    public void setMinThumbnailWidth(int i) {
        this.mMinThumbnailWidth = i;
    }

    private void init() {
        this.mItemLengthList.clear();
        this.mItemLengthList.add(Integer.valueOf(this.mLeftPadding));
        for (NvsVideoClip nvsVideoClip : this.mVideoClipList) {
            double inPoint = (double) nvsVideoClip.getInPoint();
            double d = this.mPixelPerMicrosecond;
            Double.isNaN(inPoint);
            double outPoint = (double) (nvsVideoClip.getOutPoint() - 1);
            double d2 = this.mPixelPerMicrosecond;
            Double.isNaN(outPoint);
            this.mItemLengthList.add(Integer.valueOf(((int) Math.floor((outPoint * d2) + 0.5d)) - ((int) Math.floor((inPoint * d) + 0.5d))));
        }
        this.mItemLengthList.add(Integer.valueOf(this.mRightPadding));
    }

    public void refresh(int i, int i2) {
        int i3 = i + 1;
        int i4 = i2 + 1;
        this.mSelectPosition = i4;
        if (i3 <= this.mItemLengthList.size() - 1 && i4 <= this.mItemLengthList.size()) {
            notifyItemChanged(i3);
            notifyItemChanged(i4);
        }
    }

    public void refresh(int i) {
        this.mSelectPosition = i + 1;
        notifyDataSetChanged();
    }

    public void refreshItemLength(int i, int i2) {
        int i3 = i2 + 1;
        this.mItemLengthList.set(i3, Integer.valueOf(i));
        notifyItemChanged(i3);
    }

    public void initData(int i, int i2, List<NvsVideoClip> list, double d) {
        this.mLeftPadding = i;
        this.mRightPadding = i2;
        this.mPixelPerMicrosecond = d;
        this.mVideoClipList = list;
        init();
    }

    class HeadHolder extends RecyclerView.ViewHolder {
        public HeadHolder(@NonNull View view) {
            super(view);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        public FootHolder(@NonNull View view) {
            super(view);
        }
    }

    class MidHolder extends RecyclerView.ViewHolder {
        private LinearLayout mTextLayout;
        private TextView mTextView;

        public MidHolder(@NonNull View view) {
            super(view);
            this.mTextLayout = (LinearLayout) view.findViewById(R.id.layout_cover_timeline_speed);
            this.mTextView = (TextView) view.findViewById(R.id.tv_cover_timeline_speed_text);
        }
    }
}
