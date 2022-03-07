package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.view.MYSeekBarView;
import com.meishe.myvideoapp.R;

public class CuttingMenuView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "CuttingMenuView";
    private ImageView mConfirmImageView;
    private Context mContext;
    private MYSeekBarView mMYSeekBarView;
    private OnConfirmListener mOnConfrimListener;
    private OnRatioSelectListener mOnRatioSelectListener;
    private RationAdapter mRationAdapter;
    private RecyclerView mRecyclerView;
    private TextView mResetTextView;
    private int[] ratioIcon = {R.drawable.cutting_free_bg, R.drawable.cutting_nine_sixteen, R.drawable.cutting_three_four, R.drawable.cutting_one_tone, R.drawable.cutting_four_three, R.drawable.cutting_sixteen_nine};
    private int[] ratioString = {R.string.free, R.string.nineTSixteen, R.string.threeTFour, R.string.oneTone, R.string.fourTThree, R.string.sixteenTNine};
    private int[] ratios = {0, 4, 16, 2, 8, 1};

    public interface OnConfirmListener {
        void onConfirm();
    }

    public interface OnRatioSelectListener {
        void onItemClicked(int i);

        void onReset();
    }

    public void setOnSeekBarListener(MYSeekBarView.OnSeekBarListener onSeekBarListener) {
        MYSeekBarView mYSeekBarView = this.mMYSeekBarView;
        if (mYSeekBarView != null) {
            mYSeekBarView.setListener(onSeekBarListener);
        }
    }

    public void setOnRatioSelectListener(OnRatioSelectListener onRatioSelectListener) {
        this.mOnRatioSelectListener = onRatioSelectListener;
    }

    public void setOnConfrimListener(OnConfirmListener onConfirmListener) {
        this.mOnConfrimListener = onConfirmListener;
    }

    public CuttingMenuView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public CuttingMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initView();
        initData();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.view_cutting_menu, this);
        this.mMYSeekBarView = (MYSeekBarView) inflate.findViewById(R.id.view_seek_bar);
        this.mConfirmImageView = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mResetTextView = (TextView) inflate.findViewById(R.id.tv_reset);
        this.mResetTextView.setOnClickListener(this);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.ratio_recyclerView);
        this.mConfirmImageView.setOnClickListener(this);
        this.mRationAdapter = new RationAdapter();
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mRecyclerView.setAdapter(this.mRationAdapter);
        this.mRationAdapter.setSelection(0);
    }

    private void initData() {
        this.mMYSeekBarView.setStartValueAndCurrentValue(-45.0f, 45.0f);
        this.mMYSeekBarView.setInitData(90, 45, true);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_confirm) {
            OnConfirmListener onConfirmListener = this.mOnConfrimListener;
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm();
            }
        } else if (id == R.id.tv_reset) {
            this.mRationAdapter.setSelection(0);
            OnRatioSelectListener onRatioSelectListener = this.mOnRatioSelectListener;
            if (onRatioSelectListener != null) {
                onRatioSelectListener.onReset();
            }
        }
    }

    public void setProgress(float f) {
        this.mMYSeekBarView.setSeekProgress((int) (f + 45.0f));
    }

    public void setSelectRatio(int i) {
        int i2 = 0;
        while (true) {
            int[] iArr = this.ratios;
            if (i2 >= iArr.length) {
                return;
            }
            if (i == iArr[i2]) {
                this.mRationAdapter.setSelection(i2);
                return;
            }
            i2++;
        }
    }

    /* access modifiers changed from: package-private */
    public class RationAdapter extends RecyclerView.Adapter {
        private int mSelectPosition = -1;

        RationAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @NonNull
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item_cutting_ration, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            Holder holder = (Holder) viewHolder;
            holder.mImageView.setImageResource(CuttingMenuView.this.ratioIcon[i]);
            holder.mTextView.setText(CuttingMenuView.this.ratioString[i]);
            if (this.mSelectPosition == i) {
                holder.mImageView.setSelected(true);
                holder.mTextView.setTextColor(CuttingMenuView.this.mContext.getResources().getColor(R.color.red_half_trans));
            } else {
                holder.mImageView.setSelected(false);
                holder.mTextView.setTextColor(-1);
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                /* class com.meishe.myvideo.view.CuttingMenuView.RationAdapter.AnonymousClass1 */

                public void onClick(View view) {
                    if (CuttingMenuView.this.mOnRatioSelectListener != null) {
                        CuttingMenuView.this.mOnRatioSelectListener.onItemClicked(CuttingMenuView.this.ratios[i]);
                    }
                    RationAdapter.this.mSelectPosition = i;
                    RationAdapter.this.notifyDataSetChanged();
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return CuttingMenuView.this.ratioString.length;
        }

        public void setSelection(int i) {
            this.mSelectPosition = i;
            notifyDataSetChanged();
        }

        class Holder extends RecyclerView.ViewHolder {
            private ImageView mImageView;
            private TextView mTextView;

            public Holder(@NonNull View view) {
                super(view);
                this.mImageView = (ImageView) view.findViewById(R.id.image);
                this.mTextView = (TextView) view.findViewById(R.id.text_view);
            }
        }
    }
}
