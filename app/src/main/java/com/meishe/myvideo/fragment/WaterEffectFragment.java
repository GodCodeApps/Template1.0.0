package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.engine.bean.MeicamFxParam;
import com.meishe.engine.bean.MeicamTimelineVideoFxClip;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.WaterMarkEffectInfo;
import com.meishe.myvideo.decoration.GridItemDecoration;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class WaterEffectFragment extends BaseFragment {
    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.water_effect_list_fragment;
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
        initData();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.fragment.WaterEffectFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                WaterEffectFragment.this.mAdapter.setSelectPosition(i);
                WaterEffectFragment.this.mAdapter.notifyDataSetChanged();
                WaterEffectFragment.this.postEvent(i, 1004);
            }
        });
    }

    private void initRecyclerView() {
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.mAdapter = new MultiFunctionAdapter(getContext(), this.mRecyclerView);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new GridItemDecoration(ScreenUtils.dp2px(getContext(), 3.0f), ScreenUtils.dp2px(getContext(), 12.0f), ScreenUtils.dp2px(getContext(), 3.0f), 0));
    }

    private void initData() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new WaterMarkEffectInfo(3, getContext().getResources().getString(R.string.top_menu_no), R.mipmap.ic_water_mark_no));
        arrayList.add(new WaterMarkEffectInfo(1, getContext().getResources().getString(R.string.effect_mosaic), R.mipmap.ic_mosaic));
        arrayList.add(new WaterMarkEffectInfo(2, getContext().getResources().getString(R.string.effect_blur), R.mipmap.ic_blur));
        this.mAdapter.addAll(arrayList);
        upDateSelected();
    }

    public void cancleSelectedItem() {
        this.mAdapter.setSelectPosition(-1);
        this.mAdapter.notifyDataSetChanged();
    }

    public void upDateSelected() {
        List<MeicamFxParam> meicamFxParamList;
        List<MeicamTimelineVideoFxClip> meicamTimelineVideoFxClipList = TimelineData.getInstance().getMeicamTimelineVideoFxClipList();
        if (meicamTimelineVideoFxClipList.size() > 0 && (meicamFxParamList = meicamTimelineVideoFxClipList.get(0).getMeicamFxParamList()) != null && meicamFxParamList.size() > 0) {
            if (meicamFxParamList.size() == 1) {
                this.mAdapter.setSelectPosition(2);
                this.mAdapter.notifyDataSetChanged();
            } else if (meicamFxParamList.size() == 2) {
                this.mAdapter.setSelectPosition(1);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }
}
