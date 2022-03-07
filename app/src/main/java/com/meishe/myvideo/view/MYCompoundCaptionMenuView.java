package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.decoration.GridItemDecoration;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;

public class MYCompoundCaptionMenuView extends MYBaseView {
    private static final int SPAN_COUNT = 4;
    private MultiFunctionAdapter mAdapter;
    private ImageView mIvConfirm;
    private OnChangeListener mOnChangeListener;
    private RecyclerView mRecyclerView;

    public interface OnChangeListener {
        void onClickConfirm();
    }

    public void onClick(View view) {
    }

    public MYCompoundCaptionMenuView(Context context) {
        super(context);
    }

    public MYCompoundCaptionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MYCompoundCaptionMenuView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void init() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.compound_caption_view, this);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initRecyclerView() {
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 4));
        this.mAdapter = new MultiFunctionAdapter(this.mContext, this.mRecyclerView);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new GridItemDecoration(ScreenUtils.dp2px(this.mContext, 3.0f), ScreenUtils.dp2px(getContext(), 12.0f), ScreenUtils.dp2px(getContext(), 3.0f), 0));
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initData() {
        ArrayList<BaseInfo> compoundCaptionDataList = MenuDataManager.getCompoundCaptionDataList(getContext());
        compoundCaptionDataList.get(0).mEffectType = 16;
        this.mAdapter.addAll(compoundCaptionDataList);
        this.mAdapter.setSelectPosition(-1);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initListener() {
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYCompoundCaptionMenuView.AnonymousClass1 */

            public void onClick(View view) {
                MYCompoundCaptionMenuView.this.hide();
                if (MYCompoundCaptionMenuView.this.mOnChangeListener != null) {
                    MYCompoundCaptionMenuView.this.mOnChangeListener.onClickConfirm();
                }
            }
        });
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.MYCompoundCaptionMenuView.AnonymousClass2 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                MYCompoundCaptionMenuView mYCompoundCaptionMenuView = MYCompoundCaptionMenuView.this;
                mYCompoundCaptionMenuView.postEvent(i, 1017, mYCompoundCaptionMenuView.mAdapter);
            }
        });
    }

    public void refreshData() {
        initData();
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void show() {
        super.show();
        MultiFunctionAdapter multiFunctionAdapter = this.mAdapter;
        if (multiFunctionAdapter != null) {
            multiFunctionAdapter.setSelectPosition(-1);
        }
    }
}
