package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideo.decoration.GridItemDecoration;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideo.util.FileUtil;
import com.meishe.myvideo.util.PathUtils;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class StickerCustomFragment extends BaseFragment {
    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.sticker_custom_list_fragment;
    }

    public void onClick(View view) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
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

    private void initData() {
        String assetDownloadPath = PathUtils.getAssetDownloadPath(12);
        ArrayList<NvAssetInfo> customAnimateStickerDataList = MenuDataManager.getCustomAnimateStickerDataList(getContext());
        String str = (customAnimateStickerDataList == null || customAnimateStickerDataList.size() <= 0) ? "" : customAnimateStickerDataList.get(0).uuid;
        ArrayList arrayList = new ArrayList();
        List<String> filesAllName = FileUtil.getFilesAllName(assetDownloadPath);
        for (int i = 0; i < filesAllName.size(); i++) {
            NvAssetInfo nvAssetInfo = new NvAssetInfo();
            nvAssetInfo.mFilePath = filesAllName.get(i);
            nvAssetInfo.mEffectType = 22;
            nvAssetInfo.uuid = str;
            arrayList.add(nvAssetInfo);
        }
        NvAssetInfo nvAssetInfo2 = new NvAssetInfo();
        nvAssetInfo2.mName = getString(R.string.add);
        arrayList.add(0, nvAssetInfo2);
        this.mAdapter.addAll(arrayList);
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
            /* class com.meishe.myvideo.fragment.StickerCustomFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                StickerCustomFragment.this.postEvent(i, 1002);
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.getEventType() == 22) {
            initData();
        }
    }
}
