package com.meishe.myvideo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.activity.MaterialSelectActivity;
import com.meishe.myvideo.adapter.AgendaSimpleSectionAdapter;
import com.meishe.myvideo.adapter.SectionedSpanSizeLookup;
import com.meishe.myvideo.bean.MediaData;
import com.meishe.myvideo.interfaces.OnTotalNumChange;
import com.meishe.myvideo.interfaces.OnTotalNumChangeForActivity;
import com.meishe.myvideo.util.GridSpacingItemDecoration;
import com.meishe.myvideo.util.MediaConstant;
import com.meishe.myvideo.util.MediaUtils;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaFragment extends BaseFragment implements OnTotalNumChange {
    public static final int GRIDITEMCOUNT = 4;
    private final String TAG = getClass().getName();
    private AgendaSimpleSectionAdapter adapter;
    private int clickType;
    private int index;
    GridLayoutManager layoutManager;
    List<MediaData> listOfOut = new ArrayList();
    List<List<MediaData>> lists = new ArrayList();
    private int mLimitMediaCount = -1;
    private OnTotalNumChangeForActivity mOnTotalNumChangeForActivity;
    RecyclerView mediaRecycler;
    private int totalSize;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    @SuppressLint({"ClickableViewAccessibility"})
    public void initListener() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.fragment_media;
    }

    public void onClick(View view) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
    }

    public int getIndex() {
        return this.index;
    }

    public void setTotalSize(int i) {
        this.totalSize = i;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    @Override // androidx.fragment.app.Fragment, com.meishe.myvideo.fragment.BaseFragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTotalNumChangeForActivity) {
            this.mOnTotalNumChangeForActivity = (OnTotalNumChangeForActivity) context;
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
        if (bundle != null) {
            this.index = bundle.getInt(MediaConstant.MEDIA_TYPE);
            this.mLimitMediaCount = bundle.getInt(MediaConstant.LIMIT_COUNT_MAX, -1);
            this.clickType = bundle.getInt(MediaConstant.KEY_CLICK_TYPE, 0);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        this.mediaRecycler = (RecyclerView) this.mRootView.findViewById(R.id.media_recycleView);
        showLocalMediaByMediaType();
    }

    private void showLocalMediaByMediaType() {
        MediaUtils.getMediasByType(this.mActivity, this.index, new MediaUtils.LocalMediaCallback() {
            /* class com.meishe.myvideo.fragment.MediaFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.util.MediaUtils.LocalMediaCallback
            public void onLocalMediaCallback(List<MediaData> list) {
                MediaUtils.ListOfAllMedia groupListByTime = MediaUtils.groupListByTime(list);
                MediaFragment.this.lists = groupListByTime.getListOfAll();
                MediaFragment.this.listOfOut = groupListByTime.getListOfParent();
                MediaFragment mediaFragment = MediaFragment.this;
                List<List<MediaData>> list2 = mediaFragment.lists;
                List<MediaData> list3 = MediaFragment.this.listOfOut;
                RecyclerView recyclerView = MediaFragment.this.mediaRecycler;
                MediaFragment mediaFragment2 = MediaFragment.this;
                mediaFragment.adapter = new AgendaSimpleSectionAdapter(list2, list3, recyclerView, mediaFragment2, mediaFragment2.index, MediaFragment.this.mActivity, MediaFragment.this.clickType, MediaFragment.this.mLimitMediaCount, MediaFragment.this);
                MediaFragment.this.mediaRecycler.setAdapter(MediaFragment.this.adapter);
                MediaFragment mediaFragment3 = MediaFragment.this;
                mediaFragment3.layoutManager = new GridLayoutManager(mediaFragment3.getContext(), 4);
                MediaFragment.this.layoutManager.setSpanSizeLookup(new SectionedSpanSizeLookup(MediaFragment.this.adapter, MediaFragment.this.layoutManager));
                MediaFragment.this.mediaRecycler.setLayoutManager(MediaFragment.this.layoutManager);
                MediaFragment.this.mediaRecycler.addItemDecoration(new GridSpacingItemDecoration(MediaFragment.this.getContext(), 4));
            }
        });
    }

    @Override // com.meishe.myvideo.interfaces.OnTotalNumChange
    public void onTotalNumChange(@NonNull List list, Object obj) {
        int size = list.size();
        String str = this.TAG;
        Logger.d(str, "当前碎片的标签：   " + this.TAG + "     总数据：    " + size);
        OnTotalNumChangeForActivity onTotalNumChangeForActivity = this.mOnTotalNumChangeForActivity;
        if (onTotalNumChangeForActivity != null) {
            onTotalNumChangeForActivity.onTotalNumChangeForActivity(list, obj);
        }
    }

    public AgendaSimpleSectionAdapter getAdapter() {
        return this.adapter;
    }

    public void refreshSelect(List<MediaData> list, int i) {
        AgendaSimpleSectionAdapter agendaSimpleSectionAdapter;
        if (this.index != 0 && (this.mActivity instanceof MaterialSelectActivity)) {
            list = ((MaterialSelectActivity) this.mActivity).getMediaDataList();
        }
        if (!(!isAdded() || (agendaSimpleSectionAdapter = this.adapter) == null || agendaSimpleSectionAdapter.getSelectList() == null)) {
            List<MediaData> needRefreshList = getNeedRefreshList(getSameTypeData(this.adapter.getSelectList(), i), getSameTypeData(list, this.index), i);
            String str = this.TAG;
            Logger.d(str, "不同个数：    " + needRefreshList.size());
            for (int i2 = 0; i2 < needRefreshList.size(); i2++) {
                int type = needRefreshList.get(i2).getType();
                int i3 = this.index;
                if (type == i3 || i3 == 0) {
                    String str2 = this.TAG;
                    Logger.d(str2, "更新数据：    " + needRefreshList.get(i2).getPath());
                    Point pointByData = this.adapter.getPointByData(this.lists, needRefreshList.get(i2));
                    AgendaSimpleSectionAdapter agendaSimpleSectionAdapter2 = this.adapter;
                    agendaSimpleSectionAdapter2.itemClick(this.mediaRecycler.getChildAt(agendaSimpleSectionAdapter2.getPositionByData(this.lists, needRefreshList.get(i2))), pointByData.x, pointByData.y, true);
                }
            }
        }
    }

    private List<MediaData> getSameTypeData(List<MediaData> list, int i) {
        ArrayList arrayList = new ArrayList();
        for (MediaData mediaData : list) {
            if (mediaData.getType() == i || i == 0) {
                arrayList.add(mediaData);
            }
        }
        return arrayList;
    }

    private List<MediaData> getNeedRefreshList(List<MediaData> list, List<MediaData> list2, int i) {
        ArrayList arrayList = new ArrayList();
        if (!(i == 0 || this.index == 0)) {
            return arrayList;
        }
        HashMap hashMap = new HashMap(list.size());
        for (MediaData mediaData : list) {
            hashMap.put(mediaData.getPath(), 1);
        }
        for (MediaData mediaData2 : list2) {
            if (hashMap.get(mediaData2.getPath()) != null) {
                hashMap.put(mediaData2.getPath(), 2);
                if (mediaData2.isState() != getStateByPathInList(list, mediaData2.getPath())) {
                    arrayList.add(mediaData2);
                }
            } else {
                arrayList.add(mediaData2);
            }
        }
        if (list.size() > list2.size()) {
            for (Map.Entry entry : hashMap.entrySet()) {
                if (((Integer) entry.getValue()).intValue() == 1 && !arrayList.contains(getDataByPath((String) entry.getKey()))) {
                    arrayList.add(getDataByPath((String) entry.getKey()));
                }
            }
        }
        return arrayList;
    }

    private boolean getStateByPathInList(List<MediaData> list, String str) {
        for (MediaData mediaData : list) {
            if (mediaData.getPath().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private MediaData getDataByPath(String str) {
        return this.adapter.getDataByPath(this.lists, str);
    }
}
