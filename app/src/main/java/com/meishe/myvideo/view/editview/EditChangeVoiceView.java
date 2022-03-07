package com.meishe.myvideo.view.editview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.decoration.SpaceItemDecoration;
import com.meishe.myvideo.edit.record.RecordFxListItem;
import com.meishe.myvideo.edit.record.RecordUtil;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.view.editview.EditView;
import com.meishe.myvideoapp.R;
import com.meishe.player.common.utils.ScreenUtils;
import java.util.List;

public class EditChangeVoiceView extends EditView<String> {
    private EditView.CallBack<String> mCallBack;
    private ImageView mIvConfirm;
    private RecyclerView mRecyclerView;
    private int mSelectPosition = -1;
    private TextView mTvContent;

    public void updateData(String str) {
    }

    public EditChangeVoiceView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.view_edit_chang_voice, this);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recycleView);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mTvContent = (TextView) inflate.findViewById(R.id.tv_content);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initData() {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mRecyclerView.addItemDecoration(new SpaceItemDecoration(0, ScreenUtils.dip2px(this.mContext, 12.5f)));
        VoiceAdapter voiceAdapter = new VoiceAdapter();
        voiceAdapter.setData(RecordUtil.listRecordFxFromJson(this.mContext));
        this.mRecyclerView.setAdapter(voiceAdapter);
        this.mTvContent.setText(getResources().getString(R.string.sub_menu_audio_edit_change_voice));
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initListener() {
        super.initListener();
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeVoiceView.AnonymousClass1 */

            public void onClick(View view) {
                EditChangeVoiceView.this.dismiss();
            }
        });
    }

    @Override // com.meishe.myvideo.view.editview.EditView
    public void setCallBackListener(EditView.CallBack<String> callBack) {
        this.mCallBack = callBack;
    }

    class VoiceAdapter extends RecyclerView.Adapter {
        private List<RecordFxListItem> mFxListItems;

        VoiceAdapter() {
        }

        public void setData(List<RecordFxListItem> list) {
            this.mFxListItems = list;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @NonNull
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(EditChangeVoiceView.this.mContext).inflate(R.layout.view_change_voice_item, (ViewGroup) null));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            final RecordFxListItem recordFxListItem = this.mFxListItems.get(i);
            viewHolder2.mIcon.setImageDrawable(recordFxListItem.image_drawable);
            viewHolder2.mName.setText(recordFxListItem.fxName);
            if (EditChangeVoiceView.this.mSelectPosition == i) {
                viewHolder2.imageCover.setVisibility(0);
            } else {
                viewHolder2.imageCover.setVisibility(8);
            }
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
                /* class com.meishe.myvideo.view.editview.EditChangeVoiceView.VoiceAdapter.AnonymousClass1 */

                public void onClick(View view) {
                    MessageEvent.sendEvent(recordFxListItem.fxID, (int) MessageEvent.MESSAGE_TYPE_AUDIO_VOICE);
                    EditChangeVoiceView.this.mSelectPosition = i;
                    VoiceAdapter.this.notifyDataSetChanged();
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List<RecordFxListItem> list = this.mFxListItems;
            if (list == null) {
                return 0;
            }
            return list.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageCover;
        public ImageView mIcon;
        public TextView mName;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.mIcon = (ImageView) view.findViewById(R.id.icon);
            this.mName = (TextView) view.findViewById(R.id.name);
            this.imageCover = (ImageView) view.findViewById(R.id.image_cover);
        }
    }
}
