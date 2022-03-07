package com.meishe.myvideo.downLoad;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideo.downLoad.AssetDownloadListAdapter;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.util.asset.NvAssetManager;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AssetListFragment extends Fragment implements NvAssetManager.NvAssetManagerListener {
    private static final int DOWNLOADASSETINPROGRESS = 202;
    private static final int GETASSETLISTFAIL = 201;
    private static final int GETASSETLISTSUCCESS = 200;
    private static final String TAG = "AssetListFragment";
    private static final int mPageSize = 20;
    private ArrayList<NvAssetInfo> mAssetDataList = new ArrayList<>();
    private AssetDownloadListAdapter mAssetListAdapter;
    private NvAssetManager mAssetManager;
    private RecyclerView mAssetRrecyclerViewList;
    private int mAssetType = 0;
    private int mCurrentRequestPage = 0;
    private Handler mHandler = new Handler(new Handler.Callback() {
        /* class com.meishe.myvideo.downLoad.AssetListFragment.AnonymousClass1 */

        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 200:
                    if (AssetListFragment.this.mIsFirstRequest) {
                        AssetListFragment.this.mIsFirstRequest = false;
                        AssetListFragment.this.mPreLoadingLayout.setVisibility(8);
                        AssetListFragment.this.mLoadFailedLayout.setVisibility(8);
                    }
                    AssetListFragment.this.closeRefresh();
                    if (AssetListFragment.this.mAssetDataList != null && AssetListFragment.this.mAssetDataList.size() > 0) {
                        AssetListFragment.this.mAssetListAdapter.setAssetDatalist(AssetListFragment.this.mAssetDataList);
                        if (!AssetListFragment.this.mHasNext) {
                            AssetListFragment.this.mAssetListAdapter.setLoadState(4);
                            break;
                        } else {
                            AssetListFragment.this.mAssetListAdapter.setLoadState(2);
                            break;
                        }
                    }
                case 201:
                    if (AssetListFragment.this.mIsFirstRequest) {
                        AssetListFragment.this.mPreLoadingLayout.setVisibility(8);
                        AssetListFragment.this.mSwipeRefreshLayout.setVisibility(8);
                        AssetListFragment.this.mLoadFailedLayout.setVisibility(0);
                    }
                    AssetListFragment.this.closeRefresh();
                    AssetListFragment.this.mAssetListAdapter.setLoadState(3);
                    break;
                case 202:
                    AssetListFragment.this.mAssetListAdapter.updateDownloadItems();
                    break;
            }
            return false;
        }
    });
    private boolean mHasNext = true;
    private boolean mIsFirstRequest = true;
    private boolean mIsLoadingMoreFlag = false;
    private boolean mIsRefreshFlag = false;
    private LinearLayout mLoadFailedLayout;
    private LinearLayout mPreLoadingLayout;
    private Button mReloadAsset;
    private ScheduledExecutorService mScheduledExecutorService;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override // com.meishe.myvideo.util.asset.NvAssetManager.NvAssetManagerListener
    public void onFinishAssetPackageInstallation(String str) {
    }

    @Override // com.meishe.myvideo.util.asset.NvAssetManager.NvAssetManagerListener
    public void onFinishAssetPackageUpgrading(String str) {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Log.e(TAG, "onCreateView");
        View inflate = layoutInflater.inflate(R.layout.asset_download_list_fragment, viewGroup, false);
        this.mPreLoadingLayout = (LinearLayout) inflate.findViewById(R.id.preloadingLayout);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh_layout);
        this.mAssetRrecyclerViewList = (RecyclerView) inflate.findViewById(R.id.asset_recyclerviewList);
        this.mLoadFailedLayout = (LinearLayout) inflate.findViewById(R.id.loadFailedLayout);
        this.mReloadAsset = (Button) inflate.findViewById(R.id.reloadAsset);
        this.mAssetManager = NvAssetManager.sharedInstance();
        this.mAssetManager.setManagerlistener(this);
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Log.e(TAG, "onViewCreated");
        initData();
    }

    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    public void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }

    public void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
        new Thread(new Runnable() {
            /* class com.meishe.myvideo.downLoad.AssetListFragment.AnonymousClass2 */

            public void run() {
                NvAssetManager.sharedInstance().setAssetInfoToSharedPreferences(AssetListFragment.this.mAssetType);
            }
        }).start();
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        stopProgressTimer();
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        Log.e(TAG, "onHiddenChanged: " + z);
    }

    @Override // com.meishe.myvideo.util.asset.NvAssetManager.NvAssetManagerListener
    public void onRemoteAssetsChanged(boolean z) {
        this.mHasNext = z;
        Log.e(TAG, "mHasNext = " + this.mHasNext);
        if (!this.mIsLoadingMoreFlag && !this.mIsRefreshFlag) {
            ArrayList<NvAssetInfo> remoteAssetsWithPage = this.mAssetManager.getRemoteAssetsWithPage(this.mAssetType, 31, 0, this.mCurrentRequestPage, 20);
            if (remoteAssetsWithPage.size() > 0) {
                this.mAssetDataList = remoteAssetsWithPage;
            }
        } else if (!this.mIsLoadingMoreFlag && this.mIsRefreshFlag) {
            this.mIsRefreshFlag = false;
            ArrayList<NvAssetInfo> remoteAssetsWithPage2 = this.mAssetManager.getRemoteAssetsWithPage(this.mAssetType, 31, 0, this.mCurrentRequestPage, 20);
            if (remoteAssetsWithPage2.size() > 0) {
                this.mAssetDataList = remoteAssetsWithPage2;
            }
        } else if (!this.mIsRefreshFlag && this.mIsLoadingMoreFlag) {
            this.mIsLoadingMoreFlag = false;
            this.mAssetDataList.addAll(this.mAssetManager.getRemoteAssetsWithPage(this.mAssetType, 31, 0, this.mCurrentRequestPage, 20));
        }
        this.mCurrentRequestPage++;
        Message obtainMessage = this.mHandler.obtainMessage();
        if (obtainMessage == null) {
            obtainMessage = new Message();
        }
        obtainMessage.what = 200;
        this.mHandler.sendMessage(obtainMessage);
    }

    @Override // com.meishe.myvideo.util.asset.NvAssetManager.NvAssetManagerListener
    public void onGetRemoteAssetsFailed() {
        Message obtainMessage = this.mHandler.obtainMessage();
        if (obtainMessage == null) {
            obtainMessage = new Message();
        }
        obtainMessage.what = 201;
        this.mHandler.sendMessage(obtainMessage);
    }

    @Override // com.meishe.myvideo.util.asset.NvAssetManager.NvAssetManagerListener
    public void onDownloadAssetProgress(String str, int i) {
        for (int i2 = 0; i2 < this.mAssetDataList.size(); i2++) {
            if (this.mAssetDataList.get(i2).uuid.compareTo(str) == 0) {
                this.mAssetDataList.get(i2).downloadProgress = i;
                if (i == 100) {
                    this.mAssetDataList.get(i2).downloadStatus = 4;
                }
                this.mAssetListAdapter.updateDownloadItems();
                return;
            }
        }
    }

    @Override // com.meishe.myvideo.util.asset.NvAssetManager.NvAssetManagerListener
    public void onDonwloadAssetFailed(String str) {
        int i = 0;
        while (i < this.mAssetDataList.size() && this.mAssetDataList.get(i).uuid.compareTo(str) != 0) {
            i++;
        }
    }

    @Override // com.meishe.myvideo.util.asset.NvAssetManager.NvAssetManagerListener
    public void onDonwloadAssetSuccess(String str, int i) {
        int i2 = 0;
        while (i2 < this.mAssetDataList.size() && this.mAssetDataList.get(i2).uuid.compareTo(str) != 0) {
            i2++;
        }
        this.mAssetListAdapter.updateDownloadItems();
        MessageEvent.sendEvent(i, 1018);
    }

    private void initData() {
        int i;
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mAssetType = arguments.getInt("assetType");
            i = arguments.getInt("ratio", 1);
        } else {
            i = 1;
        }
        this.mIsFirstRequest = true;
        this.mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        this.mAssetListAdapter = new AssetDownloadListAdapter(getActivity());
        this.mAssetListAdapter.setCurTimelineRatio(i);
        this.mAssetListAdapter.setAssetType(this.mAssetType);
        this.mAssetRrecyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAssetRrecyclerViewList.addItemDecoration(new AssetListDecoration(getActivity(), 1));
        this.mAssetListAdapter.setAssetDatalist(this.mAssetDataList);
        this.mAssetRrecyclerViewList.setAdapter(this.mAssetListAdapter);
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /* class com.meishe.myvideo.downLoad.AssetListFragment.AnonymousClass3 */

            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                AssetListFragment.this.mIsRefreshFlag = true;
                AssetListFragment.this.mCurrentRequestPage = 0;
                AssetListFragment.this.assetDataRequest();
            }
        });
        this.mAssetRrecyclerViewList.addOnScrollListener(new AssetListOnScrollListener() {
            /* class com.meishe.myvideo.downLoad.AssetListFragment.AnonymousClass4 */

            @Override // com.meishe.myvideo.downLoad.AssetListOnScrollListener
            public void onLoadMore() {
                Log.e(AssetListFragment.TAG, "mHasNext = " + AssetListFragment.this.mHasNext);
                if (AssetListFragment.this.mHasNext) {
                    AssetListFragment.this.mIsLoadingMoreFlag = true;
                    AssetListFragment.this.mAssetListAdapter.setLoadState(1);
                    AssetListFragment.this.assetDataRequest();
                    return;
                }
                AssetListFragment.this.mAssetListAdapter.setLoadState(4);
            }
        });
        this.mAssetListAdapter.setDownloadClickerListener(new AssetDownloadListAdapter.OnDownloadClickListener() {
            /* class com.meishe.myvideo.downLoad.AssetListFragment.AnonymousClass5 */

            @Override // com.meishe.myvideo.downLoad.AssetDownloadListAdapter.OnDownloadClickListener
            public void onItemDownloadClick(AssetDownloadListAdapter.RecyclerViewHolder recyclerViewHolder, int i) {
                int size = AssetListFragment.this.mAssetDataList.size();
                if (i < size && size > 0) {
                    AssetListFragment.this.mAssetManager.downloadAsset(AssetListFragment.this.mAssetType, ((NvAssetInfo) AssetListFragment.this.mAssetDataList.get(i)).uuid);
                }
            }
        });
        this.mReloadAsset.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.downLoad.AssetListFragment.AnonymousClass6 */

            public void onClick(View view) {
                AssetListFragment.this.mLoadFailedLayout.setVisibility(8);
                AssetListFragment.this.mPreLoadingLayout.setVisibility(0);
                AssetListFragment.this.assetDataRequest();
            }
        });
        this.mAssetManager.searchLocalAssets(this.mAssetType);
        assetDataRequest();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void assetDataRequest() {
        this.mAssetManager.downloadRemoteAssetsInfo(this.mAssetType, 31, 0, this.mCurrentRequestPage, 20);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void closeRefresh() {
        SwipeRefreshLayout swipeRefreshLayout = this.mSwipeRefreshLayout;
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            this.mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void startProgressTimer() {
        this.mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            /* class com.meishe.myvideo.downLoad.AssetListFragment.AnonymousClass7 */

            public void run() {
                Message message = new Message();
                message.what = 202;
                AssetListFragment.this.mHandler.sendMessage(message);
            }
        }, 0, 300, TimeUnit.MILLISECONDS);
    }

    private void stopProgressTimer() {
        ScheduledExecutorService scheduledExecutorService = this.mScheduledExecutorService;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
            this.mScheduledExecutorService = null;
        }
    }
}
