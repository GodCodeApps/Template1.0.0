package com.meishe.myvideo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meicam.sdk.NvsVideoTransition;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.application.MeiSheApplication;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.TransitionInfo;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class EditorTimelineTransitionAdapter extends RecyclerView.Adapter {
    private static final long MIN_SHOWN_TRANSITON_TIME = 1000000;
    private static final String TAG = "EditorTimelineTransitionAdapter";
    private static final int TYPE_FOOT = 2;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_MID = 1;
    private List<BaseInfo> mAllTransData = new ArrayList();
    private int mButtonHeightAndWidth;
    private Context mContext;
    private TransitionData mCurrSelectTransitionData;
    private boolean mDownOnTrans = false;
    private long mDownTimeNow;
    private boolean mIconVisiable = true;
    private List<TransitionData> mItemLengthList = new ArrayList();
    private int mLeftPadding;
    private OnClickTransitionListener mOnClickTransitionListener;
    private float mOnInterceptDownX;
    private int mRightPadding;
    private int mTotalMoveXOnIntercept;

    public interface OnClickTransitionListener {
        void onClickTransition(int i, TransitionData transitionData);
    }

    public EditorTimelineTransitionAdapter(Context context, int i, int i2, List<NvsVideoClip> list, NvsVideoTrack nvsVideoTrack, List<Pair<Integer, Integer>> list2) {
        this.mContext = context;
        this.mLeftPadding = i;
        this.mRightPadding = i2;
        this.mButtonHeightAndWidth = this.mContext.getResources().getDimensionPixelOffset(R.dimen.editor_timeline_preview_cut_to_button_height);
        init(list, nvsVideoTrack, list2);
    }

    public void refreshTransition(List<Pair<Integer, Integer>> list) {
        for (int i = 1; i < this.mItemLengthList.size() - 1; i++) {
            TransitionData transitionData = this.mItemLengthList.get(i);
            int i2 = i - 1;
            int intValue = ((Integer) list.get(i2).first).intValue();
            int intValue2 = ((Integer) list.get(i2).second).intValue();
            if (i == 1) {
                transitionData.setLength((intValue2 - intValue) + (this.mButtonHeightAndWidth / 2));
            } else if (i == this.mItemLengthList.size() - 2) {
                transitionData.setLength((intValue2 - intValue) - (this.mButtonHeightAndWidth / 2));
            } else {
                transitionData.setLength(intValue2 - intValue);
            }
        }
    }

    public void init(List<NvsVideoClip> list, NvsVideoTrack nvsVideoTrack, List<Pair<Integer, Integer>> list2) {
        TransitionData transitionData;
        String str;
        this.mAllTransData.clear();
        this.mAllTransData.addAll(MenuDataManager.getTransitionData(this.mContext));
        this.mAllTransData.addAll(MenuDataManager.getTransition3DData(this.mContext));
        this.mAllTransData.addAll(MenuDataManager.getTransitionEffectData(this.mContext));
        this.mItemLengthList.clear();
        this.mItemLengthList.add(new TransitionData(this.mLeftPadding, false));
        if (list2.size() != list.size()) {
            Logger.e(TAG, "clipPointList.size() != clipList.size()");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i);
            int intValue = ((Integer) list2.get(i).first).intValue();
            int intValue2 = ((Integer) list2.get(i).second).intValue();
            NvsVideoTransition transitionBySourceClipIndex = nvsVideoTrack.getTransitionBySourceClipIndex(i);
            boolean z = transitionBySourceClipIndex != null;
            if (i == 0) {
                transitionData = new TransitionData((intValue2 - intValue) + (this.mButtonHeightAndWidth / 2), z);
            } else if (i == list.size() - 1) {
                transitionData = new TransitionData((intValue2 - intValue) - (this.mButtonHeightAndWidth / 2), z);
            } else {
                transitionData = new TransitionData(intValue2 - intValue, z);
            }
            if (z) {
                if (transitionBySourceClipIndex.getVideoTransitionType() == 0) {
                    str = transitionBySourceClipIndex.getBuiltinVideoTransitionName();
                } else {
                    str = transitionBySourceClipIndex.getVideoTransitionPackageId();
                }
                for (BaseInfo baseInfo : this.mAllTransData) {
                    if (str.equals(((TransitionInfo) baseInfo).getEffectName()) || str.equals(baseInfo.getPackageId())) {
                        transitionData.setIconRcsId(baseInfo.mIconRcsId);
                        transitionData.setIconPath(baseInfo.mIconUrl);
                        transitionData.setTransitionName(baseInfo.mName);
                        transitionData.setTransitionType(baseInfo.mEffectType);
                    }
                }
            }
            this.mItemLengthList.add(transitionData);
        }
        this.mItemLengthList.add(new TransitionData(this.mRightPadding, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = new View(this.mContext);
        if (i == 0) {
            view.setLayoutParams(new ViewGroup.LayoutParams(this.mLeftPadding, -1));
            return new HeaderHolder(view);
        } else if (i == 1) {
            return new MidHolder(LayoutInflater.from(this.mContext).inflate(R.layout.timeline_editor_trans_layout, viewGroup, false));
        } else {
            if (i != 2) {
                return null;
            }
            view.setLayoutParams(new ViewGroup.LayoutParams(this.mRightPadding, -1));
            return new FootHolder(view);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @SuppressLint({"ClickableViewAccessibility"})
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof HeaderHolder) {
            viewHolder.itemView.setLayoutParams(new ViewGroup.LayoutParams(this.mItemLengthList.get(i).getLength(), -1));
        } else if (viewHolder instanceof MidHolder) {
            MidHolder midHolder = (MidHolder) viewHolder;
            midHolder.rootLayout.setLayoutParams(new ViewGroup.LayoutParams(this.mItemLengthList.get(i).getLength(), -1));
            if (i == this.mItemLengthList.size() - 2) {
                midHolder.parentLayout.setVisibility(8);
                return;
            }
            midHolder.parentLayout.requestDisallowInterceptTouchEvent(true);
            midHolder.parentLayout.setOnTouchListener(new View.OnTouchListener() {
                /* class com.meishe.myvideo.adapter.EditorTimelineTransitionAdapter.AnonymousClass1 */

                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 0) {
                        EditorTimelineTransitionAdapter.this.mDownOnTrans = true;
                        EditorTimelineTransitionAdapter.this.mDownTimeNow = System.currentTimeMillis();
                        EditorTimelineTransitionAdapter.this.mTotalMoveXOnIntercept = 0;
                        EditorTimelineTransitionAdapter.this.mOnInterceptDownX = motionEvent.getX();
                    } else if (motionEvent.getAction() == 2) {
                        EditorTimelineTransitionAdapter editorTimelineTransitionAdapter = EditorTimelineTransitionAdapter.this;
                        editorTimelineTransitionAdapter.mTotalMoveXOnIntercept = (int) (((float) editorTimelineTransitionAdapter.mTotalMoveXOnIntercept) + Math.abs(motionEvent.getX() - EditorTimelineTransitionAdapter.this.mOnInterceptDownX));
                        EditorTimelineTransitionAdapter.this.mOnInterceptDownX = motionEvent.getX();
                        if (System.currentTimeMillis() - EditorTimelineTransitionAdapter.this.mDownTimeNow > 200) {
                            EditorTimelineTransitionAdapter.this.mDownOnTrans = false;
                            Logger.e(EditorTimelineTransitionAdapter.TAG, "onInterceptTouchEvent: 长按");
                        }
                        if (EditorTimelineTransitionAdapter.this.mTotalMoveXOnIntercept > 10) {
                            Logger.e(EditorTimelineTransitionAdapter.TAG, "onInterceptTouchEvent: 移动");
                            EditorTimelineTransitionAdapter.this.mDownOnTrans = false;
                        }
                    } else if (motionEvent.getAction() == 1) {
                        if (!EditorTimelineTransitionAdapter.this.mDownOnTrans) {
                            return false;
                        }
                        if (EditorTimelineTransitionAdapter.this.mOnClickTransitionListener != null) {
                            EditorTimelineTransitionAdapter.this.mOnClickTransitionListener.onClickTransition(i, (TransitionData) EditorTimelineTransitionAdapter.this.mItemLengthList.get(i));
                            EditorTimelineTransitionAdapter editorTimelineTransitionAdapter2 = EditorTimelineTransitionAdapter.this;
                            editorTimelineTransitionAdapter2.mCurrSelectTransitionData = (TransitionData) editorTimelineTransitionAdapter2.mItemLengthList.get(i);
                        }
                        EditorTimelineTransitionAdapter.this.mDownOnTrans = false;
                    }
                    return true;
                }
            });
            if (!this.mItemLengthList.get(i).isShowView() || !this.mIconVisiable) {
                midHolder.parentLayout.setVisibility(4);
                return;
            }
            midHolder.backgroundView.setBackgroundResource(0);
            midHolder.backgroundView.setImageDrawable(null);
            midHolder.backgroundView.setImageBitmap(null);
            midHolder.parentLayout.setVisibility(0);
            if (!this.mItemLengthList.get(i).isHasTrans()) {
                midHolder.backgroundView.setBackgroundResource(R.drawable.icon_transtion_default);
            } else if (this.mItemLengthList.get(i).getIconRcsId() != 0) {
                midHolder.backgroundView.setBackgroundResource(this.mItemLengthList.get(i).getIconRcsId());
            } else if (TextUtils.isEmpty(this.mItemLengthList.get(i).getIconPath())) {
                midHolder.backgroundView.setBackgroundResource(R.drawable.icon_transtion_default);
            } else {
                Glide.with(MeiSheApplication.getContext()).asBitmap().load(this.mItemLengthList.get(i).getIconPath()).apply(new RequestOptions().centerInside().skipMemoryCache(false)).into(midHolder.backgroundView);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<TransitionData> list = this.mItemLengthList;
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

    public TransitionData getItemData(int i) {
        if (CollectionUtils.isEmpty(this.mItemLengthList)) {
            return null;
        }
        return this.mItemLengthList.get(i);
    }

    public TransitionData getCurrSelectedTransitionData() {
        return this.mCurrSelectTransitionData;
    }

    public List<TransitionData> getCurrSelectedTransitionDatas() {
        return this.mItemLengthList;
    }

    public void refresh(String str, int i, boolean z) {
        this.mItemLengthList.get(i).setTransitionName(str);
        this.mItemLengthList.get(i).setHasTrans(z);
        notifyItemChanged(i);
    }

    public boolean isDownOnTrans() {
        return this.mDownOnTrans;
    }

    public void setDownOnTrans(boolean z) {
        this.mDownOnTrans = z;
    }

    public void refresh(int i, boolean z, int i2, String str) {
        this.mItemLengthList.get(i).setHasTrans(z);
        this.mItemLengthList.get(i).setIconRcsId(i2);
        this.mItemLengthList.get(i).setIconPath(str);
        notifyItemChanged(i);
    }

    public void refresh(BaseInfo baseInfo, int i, boolean z) {
        this.mItemLengthList.get(i).setTransitionType(baseInfo.mEffectType);
        this.mItemLengthList.get(i).setTransitionName(baseInfo.mName);
        this.mItemLengthList.get(i).setHasTrans(z);
        this.mItemLengthList.get(i).setIconRcsId(baseInfo.mIconRcsId);
        this.mItemLengthList.get(i).setIconPath(baseInfo.mIconUrl);
        notifyItemChanged(i);
    }

    public void refreshAll(BaseInfo baseInfo) {
        for (int i = 1; i < this.mItemLengthList.size() - 1; i++) {
            TransitionData transitionData = this.mItemLengthList.get(i);
            transitionData.setTransitionType(baseInfo.mEffectType);
            transitionData.setTransitionName(baseInfo.mName);
            transitionData.setHasTrans(true);
            transitionData.setIconRcsId(baseInfo.mIconRcsId);
            transitionData.setIconPath(baseInfo.mIconUrl);
        }
        notifyDataSetChanged();
    }

    public boolean isItemViewShow(int i) {
        int i2;
        if (this.mItemLengthList.isEmpty() || (i2 = i + 1) < 0 || i2 > this.mItemLengthList.size()) {
            return false;
        }
        return this.mItemLengthList.get(i2).isShowView();
    }

    public class TransitionData {
        private boolean hasTrans;
        private String iconPath;
        private int iconRcsId;
        private int length;
        private boolean showView = true;
        private String transitionName;
        private int transitionType;

        TransitionData(int i, boolean z) {
            this.length = i;
            this.hasTrans = z;
        }

        public String getIconPath() {
            return this.iconPath;
        }

        public void setIconPath(String str) {
            this.iconPath = str;
        }

        public int getLength() {
            return this.length;
        }

        public void setLength(int i) {
            this.length = i;
        }

        public boolean isHasTrans() {
            return this.hasTrans;
        }

        public void setHasTrans(boolean z) {
            this.hasTrans = z;
        }

        public boolean isShowView() {
            return this.showView;
        }

        public void setShowView(boolean z) {
            this.showView = z;
        }

        public int getIconRcsId() {
            return this.iconRcsId;
        }

        public void setIconRcsId(int i) {
            this.iconRcsId = i;
        }

        public int getTransitionType() {
            return this.transitionType;
        }

        public void setTransitionType(int i) {
            this.transitionType = i;
        }

        public String getTransitionName() {
            return this.transitionName;
        }

        public void setTransitionName(String str) {
            this.transitionName = str;
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View view) {
            super(view);
        }
    }

    private class FootHolder extends RecyclerView.ViewHolder {
        public FootHolder(View view) {
            super(view);
        }
    }

    private class MidHolder extends RecyclerView.ViewHolder {
        private ImageView backgroundView;
        private RelativeLayout parentLayout;
        private RelativeLayout rootLayout;

        public MidHolder(View view) {
            super(view);
            this.rootLayout = (RelativeLayout) view.findViewById(R.id.editor_thumbnail_trans_root_layout);
            this.parentLayout = (RelativeLayout) view.findViewById(R.id.editor_thumbnail_trans_view_parent);
            this.backgroundView = (ImageView) view.findViewById(R.id.editor_thumbnail_trans_view);
        }
    }

    public void setOnClickTransitionListener(OnClickTransitionListener onClickTransitionListener) {
        this.mOnClickTransitionListener = onClickTransitionListener;
    }

    public void setIconVisiable(boolean z) {
        this.mIconVisiable = z;
        notifyDataSetChanged();
    }
}
