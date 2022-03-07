package com.meishe.myvideo.view.editview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.engine.bean.CommonData;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.view.editview.EditView;
import com.meishe.myvideoapp.R;

public class EditChangeSpeedView extends EditView<Float> {
    private IconAdapter iconAdapter;
    private String mClipType;
    public RecyclerView mIconRecycleView;
    private ImageView mIvConfirm;
    private LinearLayout mLlSpeedContainerView;
    private int mSelectPosition = 1;
    private float[] mSpeedData;
    public RecyclerView mTextRecycleView;
    private TextView mTvContent;

    @Override // com.meishe.myvideo.view.editview.EditView
    public void setCallBackListener(EditView.CallBack<Float> callBack) {
    }

    public void updateData(Float f) {
    }

    public EditChangeSpeedView(Context context, String str) {
        super(context);
        this.mClipType = str;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.view_edit_chang_speed, this);
        this.mLlSpeedContainerView = (LinearLayout) inflate.findViewById(R.id.ll_speed_container);
        this.mIconRecycleView = (RecyclerView) inflate.findViewById(R.id.icon_recycleView);
        this.mIconRecycleView.setNestedScrollingEnabled(false);
        this.mTextRecycleView = (RecyclerView) inflate.findViewById(R.id.text_recycleView);
        this.mTextRecycleView.setNestedScrollingEnabled(false);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mTvContent = (TextView) inflate.findViewById(R.id.tv_content);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initData() {
        this.mSpeedData = new float[]{0.2f, 1.0f, 2.0f, 3.0f, 4.0f};
        this.mIconRecycleView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.iconAdapter = new IconAdapter();
        this.mIconRecycleView.setAdapter(this.iconAdapter);
        this.mTextRecycleView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mTextRecycleView.setAdapter(new TextAdapter());
        this.mTvContent.setText(getResources().getString(R.string.sub_menu_audio_edit_speed));
    }

    public void setSpeed(float f) {
        int i = 0;
        while (true) {
            float[] fArr = this.mSpeedData;
            if (i >= fArr.length) {
                return;
            }
            if (f == fArr[i]) {
                this.mSelectPosition = i;
                this.iconAdapter.notifyDataSetChanged();
                return;
            }
            i++;
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initListener() {
        super.initListener();
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeSpeedView.AnonymousClass1 */

            public void onClick(View view) {
                EditChangeSpeedView.this.dismiss();
                MessageEvent.sendEvent(MessageEvent.MESSAGE_TYPE_VIDEO_SPEED_CONFORM);
            }
        });
        this.mLlSpeedContainerView.setOnTouchListener(new View.OnTouchListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeSpeedView.AnonymousClass2 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    /* access modifiers changed from: package-private */
    public class IconAdapter extends RecyclerView.Adapter {
        IconAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @NonNull
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new IconViewHolder(LayoutInflater.from(EditChangeSpeedView.this.mContext).inflate(R.layout.item_edit_chang_speed_icon, (ViewGroup) null));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            IconViewHolder iconViewHolder = (IconViewHolder) viewHolder;
            if (EditChangeSpeedView.this.mSelectPosition == i) {
                iconViewHolder.mPoint.setImageResource(R.drawable.item_change_speed_ball_select);
            } else {
                iconViewHolder.mPoint.setImageResource(R.drawable.item_change_speed_ball_unselect);
            }
            if (i == getItemCount() - 1) {
                iconViewHolder.mLine.setVisibility(8);
            } else {
                iconViewHolder.mLine.setVisibility(0);
            }
            final float f = EditChangeSpeedView.this.mSpeedData[i];
            iconViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                /* class com.meishe.myvideo.view.editview.EditChangeSpeedView.IconAdapter.AnonymousClass1 */

                public void onClick(View view) {
                    if (EditChangeSpeedView.this.mClipType.equals(CommonData.CLIP_VIDEO) || EditChangeSpeedView.this.mClipType.equals(CommonData.CLIP_IMAGE)) {
                        MessageEvent.sendEvent(f, (int) MessageEvent.MESSAGE_TYPE_VIDEO_SPEED);
                    } else if (EditChangeSpeedView.this.mClipType.equals(CommonData.CLIP_AUDIO)) {
                        MessageEvent.sendEvent(f, (int) MessageEvent.MESSAGE_TYPE_AUDIO_SPEED);
                    }
                    EditChangeSpeedView.this.mSelectPosition = i;
                    IconAdapter.this.notifyDataSetChanged();
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return EditChangeSpeedView.this.mSpeedData.length;
        }
    }

    class TextAdapter extends RecyclerView.Adapter {
        TextAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @NonNull
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new TextViewHolder(LayoutInflater.from(EditChangeSpeedView.this.mContext).inflate(R.layout.item_edit_chang_speed_text, (ViewGroup) null));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            TextView textView = ((TextViewHolder) viewHolder).mTextView;
            textView.setText(EditChangeSpeedView.this.mSpeedData[i] + "X");
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return EditChangeSpeedView.this.mSpeedData.length;
        }
    }

    class IconViewHolder extends RecyclerView.ViewHolder {
        public View mLine;
        public ImageView mPoint;

        public IconViewHolder(@NonNull View view) {
            super(view);
            this.mPoint = (ImageView) view.findViewById(R.id.point);
            this.mLine = view.findViewById(R.id.line);
        }
    }

    class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public TextViewHolder(@NonNull View view) {
            super(view);
            this.mTextView = (TextView) view.findViewById(R.id.text);
        }
    }
}
