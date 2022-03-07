package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideo.decoration.GridItemDecoration;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;

public class StickerAllFragment extends BaseFragment {
    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.sticker_all_list_fragment;
    }

    public void onClick(View view) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        this.mRecyclerView = (RecyclerView) this.mRootView.findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
        ArrayList<NvAssetInfo> animateStickerDataList = MenuDataManager.getAnimateStickerDataList(getContext());
        animateStickerDataList.get(0).mEffectType = 4;
        this.mAdapter.addAll(animateStickerDataList);
    }

    private void initRecyclerView() {
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.mAdapter = new MultiFunctionAdapter(getContext(), this.mRecyclerView);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new GridItemDecoration(ScreenUtils.dp2px(getContext(), 3.0f), ScreenUtils.dp2px(getContext(), 12.0f), ScreenUtils.dp2px(getContext(), 3.0f), 0));
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.fragment.StickerAllFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                StickerAllFragment.this.postEvent(i, 1001);
            }
        });
    }

    public void refreshData() {
        onLazyLoad();
    }
}
