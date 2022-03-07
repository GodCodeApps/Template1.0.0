package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.engine.bean.MeicamCaptionClip;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.CaptionStyleInfo;
import com.meishe.myvideo.decoration.GridItemDecoration;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class CaptionStyleFragment extends BaseFragment {
    private int mPosition = 1;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.caption_style_list_fragment;
    }

    public void onClick(View view) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        this.mRecyclerView = (RecyclerView) this.mRootView.findViewById(R.id.recycleView);
        initRecyclerView();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
        ArrayList<CaptionStyleInfo> captionStyleData = MenuDataManager.getCaptionStyleData(getContext());
        captionStyleData.get(0).mEffectType = 3;
        this.mAdapter.addAll(captionStyleData);
        this.mAdapter.setSelectPosition(this.mPosition);
    }

    private void initRecyclerView() {
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        this.mAdapter = new MultiFunctionAdapter(getActivity(), this.mRecyclerView);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new GridItemDecoration(ScreenUtils.dp2px(getContext(), 3.0f), ScreenUtils.dp2px(getContext(), 12.0f), ScreenUtils.dp2px(getContext(), 3.0f), 0));
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.fragment.CaptionStyleFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                CaptionStyleFragment.this.postEvent(i, MessageEvent.MESSAGE_TYPE_CAPTION_STYLE);
            }
        });
    }

    public void refresh() {
        onLazyLoad();
    }

    public void setSelect(MeicamCaptionClip meicamCaptionClip) {
        if (meicamCaptionClip != null) {
            String styleId = meicamCaptionClip.getStyleId();
            if (TextUtils.isEmpty(styleId)) {
                this.mPosition = 1;
            } else if (this.mAdapter != null) {
                List<BaseInfo> allItems = this.mAdapter.getAllItems();
                for (int i = 0; i < allItems.size(); i++) {
                    BaseInfo baseInfo = allItems.get(i);
                    if (baseInfo != null && baseInfo.getAsset() != null && styleId.equals(baseInfo.getAsset().getUuid())) {
                        this.mPosition = i;
                        return;
                    }
                }
            }
        }
    }

    public void setSelect(int i) {
        this.mPosition = i;
    }
}
