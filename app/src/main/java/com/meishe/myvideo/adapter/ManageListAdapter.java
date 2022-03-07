package com.meishe.myvideo.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.draft.data.DraftData;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class ManageListAdapter extends RecyclerView.Adapter<ManageListAdapter.DraftViewHolder> {
    private boolean checkAble;
    private SparseArray<Boolean> checkArray = new SparseArray<>();
    private Context mContext;
    private List<DraftData> mMusicDataList;
    private OnItemClickListener onItemClickListener;
    private OnMoreClickListener onMoreClickListener;

    public interface OnDeleteListener {
        void onComplete();

        void onStart(List<DraftData> list);
    }

    public interface OnItemClickListener {
        void onItemClick(int i, DraftData draftData);
    }

    public interface OnMoreClickListener {
        void onClick(int i, DraftData draftData);
    }

    public ManageListAdapter(Context context, List<DraftData> list) {
        this.mContext = context;
        this.mMusicDataList = list;
    }

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener2) {
        this.onMoreClickListener = onMoreClickListener2;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public void addNewDraft(DraftData draftData) {
        this.mMusicDataList.add(draftData);
        this.checkArray.clear();
        notifyDataSetChanged();
    }

    public void updateDrafts(List<DraftData> list) {
        this.mMusicDataList.clear();
        if (list != null && !list.isEmpty()) {
            this.mMusicDataList.addAll(list);
        }
        this.checkArray.clear();
        notifyDataSetChanged();
    }

    public void setCheckAble(boolean z) {
        this.checkAble = z;
        if (this.checkAble) {
            this.checkArray.clear();
        }
        notifyDataSetChanged();
    }

    public void clearPlayState() {
        List<DraftData> list = this.mMusicDataList;
        if (list != null) {
            for (DraftData draftData : list) {
            }
            notifyDataSetChanged();
        }
    }

    public void deleteDrafts(int i) {
        if (i >= 0 && i < this.mMusicDataList.size()) {
            this.mMusicDataList.remove(i);
            notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public static class DraftViewHolder extends RecyclerView.ViewHolder {
        private ImageView draftCheckState;
        private ImageView draftImage;
        private ImageButton draftManager;
        private TextView draftName;
        private TextView draftSize;
        private TextView draftTime;
        private TextView draftUpdate;
        private View itemMain;

        public DraftViewHolder(@NonNull View view) {
            super(view);
            this.itemMain = view.findViewById(R.id.item_main);
            this.draftImage = (ImageView) view.findViewById(R.id.draft_image);
            this.draftName = (TextView) view.findViewById(R.id.draft_name);
            this.draftUpdate = (TextView) view.findViewById(R.id.draft_update);
            this.draftSize = (TextView) view.findViewById(R.id.draft_size);
            this.draftTime = (TextView) view.findViewById(R.id.draft_time);
            this.draftManager = (ImageButton) view.findViewById(R.id.draft_manager);
            this.draftCheckState = (ImageView) view.findViewById(R.id.draft_check_state);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public DraftViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DraftViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_draft_data, viewGroup, false));
    }

    public void onBindViewHolder(DraftViewHolder draftViewHolder, final int i) {
        DraftData draftData;
        if (i < this.mMusicDataList.size() && (draftData = this.mMusicDataList.get(i)) != null) {
            draftViewHolder.draftName.setText(draftData.getFileName());
            draftViewHolder.draftSize.setText(draftData.getFileSize());
            draftViewHolder.draftTime.setText(draftData.getDuration());
            TextView textView = draftViewHolder.draftUpdate;
            textView.setText(this.mContext.getString(R.string.draft_update_time) + draftData.getLastModifyTime());
            Glide.with(this.mContext).load(draftData.getCoverPath()).into(draftViewHolder.draftImage);
            if (this.checkAble) {
                Boolean bool = this.checkArray.get(i);
                if (bool == null || !bool.booleanValue()) {
                    draftViewHolder.draftCheckState.setBackground(ContextCompat.getDrawable(this.mContext, R.mipmap.manager_no));
                    draftViewHolder.itemMain.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorTranslucent));
                } else {
                    draftViewHolder.draftCheckState.setBackground(ContextCompat.getDrawable(this.mContext, R.mipmap.manager_select));
                    draftViewHolder.itemMain.setBackgroundColor(this.mContext.getResources().getColor(R.color.draft_clicked_bg));
                }
                draftViewHolder.draftCheckState.setVisibility(0);
                draftViewHolder.draftManager.setVisibility(8);
            } else {
                draftViewHolder.itemMain.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorTranslucent));
                draftViewHolder.draftManager.setVisibility(0);
                draftViewHolder.draftCheckState.setVisibility(8);
            }
            draftViewHolder.draftManager.setOnClickListener(new View.OnClickListener() {
                /* class com.meishe.myvideo.adapter.ManageListAdapter.AnonymousClass1 */

                public void onClick(View view) {
                    if (ManageListAdapter.this.onMoreClickListener != null) {
                        ManageListAdapter.this.onMoreClickListener.onClick(i, (DraftData) ManageListAdapter.this.mMusicDataList.get(i));
                    }
                }
            });
            draftViewHolder.itemMain.setOnClickListener(new View.OnClickListener() {
                /* class com.meishe.myvideo.adapter.ManageListAdapter.AnonymousClass2 */

                public void onClick(View view) {
                    if (ManageListAdapter.this.checkAble) {
                        Boolean bool = (Boolean) ManageListAdapter.this.checkArray.get(i);
                        if (bool == null) {
                            bool = false;
                        }
                        ManageListAdapter.this.checkArray.put(i, Boolean.valueOf(!bool.booleanValue()));
                        ManageListAdapter.this.notifyItemChanged(i);
                    } else if (ManageListAdapter.this.onItemClickListener != null) {
                        ManageListAdapter.this.onItemClickListener.onItemClick(i, (DraftData) ManageListAdapter.this.mMusicDataList.get(i));
                    }
                }
            });
        }
    }

    public void deleteCheckedDrafts(OnDeleteListener onDeleteListener) {
        List<DraftData> deleteDraft = getDeleteDraft();
        if (!CollectionUtils.isEmpty(deleteDraft)) {
            onDeleteListener.onStart(deleteDraft);
            this.mMusicDataList.removeAll(deleteDraft);
            deleteDraft.clear();
            this.checkArray.clear();
            this.checkAble = false;
            notifyDataSetChanged();
            onDeleteListener.onComplete();
        }
    }

    public List<DraftData> getDeleteDraft() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.checkArray.size(); i++) {
            int keyAt = this.checkArray.keyAt(i);
            if (this.checkArray.get(keyAt).booleanValue() && keyAt >= 0 && keyAt <= this.mMusicDataList.size()) {
                arrayList.add(this.mMusicDataList.get(keyAt));
            }
        }
        return arrayList;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<DraftData> list = this.mMusicDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }
}
