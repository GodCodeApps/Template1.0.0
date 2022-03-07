package com.meishe.myvideo.downLoad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.activity.BaseActivity;
import com.meishe.myvideo.interfaces.OnTitleBarClickListener;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideo.view.CustomTitleBar;
import com.meishe.myvideoapp.R;

public class AssetDownloadActivity extends BaseActivity {
    public static String SHOW_PROP = "showProp";
    AssetListFragment mAssetListFragment;
    private int mAssetType = 1;
    private CustomTitleBar mTitleBar;
    private int mTitleResId = R.string.moreTheme;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_asset_download;
    }

    public void onClick(View view) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        Bundle extras;
        this.mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        if (getIntent() != null && (extras = getIntent().getExtras()) != null) {
            this.mTitleResId = extras.getInt("titleResId", R.string.moreTheme);
            this.mAssetType = extras.getInt("assetType", 1);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, ScreenUtils.dp2px(this, 64.0f));
        layoutParams.setMargins(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
        this.mTitleBar.setLayoutParams(layoutParams);
        this.mTitleBar.setTextCenter(this.mTitleResId);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        initAssetFragment();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            /* class com.meishe.myvideo.downLoad.AssetDownloadActivity.AnonymousClass1 */

            @Override // com.meishe.myvideo.interfaces.OnTitleBarClickListener
            public void OnCenterTextClick() {
            }

            @Override // com.meishe.myvideo.interfaces.OnTitleBarClickListener
            public void OnRightTextClick() {
            }

            @Override // com.meishe.myvideo.interfaces.OnTitleBarClickListener
            public void OnBackImageClick() {
                AssetDownloadActivity.this.setResult(-1, new Intent());
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onBackPressed() {
        setResult(-1, new Intent());
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void initAssetFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("ratio", TimelineData.getInstance().getMakeRatio());
        bundle.putInt("assetType", this.mAssetType);
        this.mAssetListFragment = new AssetListFragment();
        this.mAssetListFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.spaceLayout, this.mAssetListFragment).commit();
        getFragmentManager().beginTransaction().show(this.mAssetListFragment);
    }
}
