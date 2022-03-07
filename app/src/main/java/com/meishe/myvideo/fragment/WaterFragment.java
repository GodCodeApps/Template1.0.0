package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.meishe.engine.bean.MeicamWaterMark;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.WaterMarkCacheData;
import com.meishe.myvideo.bean.WaterMarkInfo;
import com.meishe.myvideo.decoration.GridItemDecoration;
import com.meishe.myvideo.util.FileUtil;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaterFragment extends BaseFragment {
    private List<WaterMarkInfo> cacheList = new ArrayList();
    private ArrayList<WaterMarkInfo> mWaterMarkInfos;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.water_list_fragment;
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
        this.mAdapter.addAll(this.mWaterMarkInfos);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.fragment.WaterFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                if (i != 0) {
                    WaterFragment.this.mAdapter.setSelectPosition(i);
                    WaterFragment.this.mAdapter.notifyDataSetChanged();
                }
                WaterFragment.this.postEvent(i, 1003);
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
        this.mWaterMarkInfos = new ArrayList<>();
        WaterMarkInfo waterMarkInfo = new WaterMarkInfo();
        waterMarkInfo.mName = getString(R.string.add);
        this.mWaterMarkInfos.add(waterMarkInfo);
        WaterMarkInfo waterMarkInfo2 = new WaterMarkInfo();
        waterMarkInfo2.mPicPath = "file:///android_asset/water_mark/pic_meiying.png";
        waterMarkInfo2.mWaterMarkPath = "assets:/water_mark/water_mark_meiying.png";
        waterMarkInfo2.mPicType = 0;
        this.mWaterMarkInfos.add(waterMarkInfo2);
        String readWaterMarkCacheFile = FileUtil.readWaterMarkCacheFile();
        if (readWaterMarkCacheFile != null) {
            try {
                this.cacheList = ((WaterMarkCacheData) new Gson().fromJson(readWaterMarkCacheFile, WaterMarkCacheData.class)).getPicturePathName();
                this.mWaterMarkInfos.addAll(this.cacheList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        upDateSelected();
    }

    public void addDataAndRefresh(String str) {
        this.mWaterMarkInfos.clear();
        this.cacheList.clear();
        initData();
        if (!isAdd(str)) {
            WaterMarkInfo waterMarkInfo = new WaterMarkInfo(0, str, str);
            this.mWaterMarkInfos.add(waterMarkInfo);
            this.cacheList.add(waterMarkInfo);
            FileUtil.writeWaterMarkCacheFile(new Gson().toJson(new WaterMarkCacheData(this.cacheList)));
        }
        this.mAdapter.addAll(this.mWaterMarkInfos);
    }

    private boolean isAdd(String str) {
        ArrayList<WaterMarkInfo> arrayList = this.mWaterMarkInfos;
        if (arrayList == null) {
            return false;
        }
        Iterator<WaterMarkInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mWaterMarkPath)) {
                return true;
            }
        }
        return false;
    }

    public void cancleSelectedItem() {
        this.mAdapter.setSelectPosition(-1);
        this.mAdapter.notifyDataSetChanged();
    }

    public void upDateSelected() {
        String watermarkPath;
        MeicamWaterMark meicamWaterMark = TimelineData.getInstance().getMeicamWaterMark();
        if (!(meicamWaterMark == null || (watermarkPath = meicamWaterMark.getWatermarkPath()) == null)) {
            for (int i = 1; i < this.mWaterMarkInfos.size(); i++) {
                String str = this.mWaterMarkInfos.get(i).mPicPath;
                if (i == 1 && watermarkPath.contains(this.mWaterMarkInfos.get(i).mWaterMarkPath)) {
                    this.mAdapter.setSelectPosition(1);
                    this.mAdapter.notifyDataSetChanged();
                    return;
                } else if (watermarkPath.equals(str)) {
                    this.mAdapter.setSelectPosition(i);
                    this.mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }
}
