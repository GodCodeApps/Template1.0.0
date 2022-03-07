package com.meishe.myvideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.FragmentTransaction;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.myvideo.bean.MediaData;
import com.meishe.myvideo.fragment.MediaFragment;
import com.meishe.myvideo.interfaces.OnTotalNumChangeForActivity;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.MediaConstant;
import com.meishe.myvideo.util.MediaUtils;
import com.meishe.myvideo.view.CustomTitleBar;
import com.meishe.myvideoapp.R;
import java.util.List;

public class MaterialSingleSelectActivity extends BaseActivity implements OnTotalNumChangeForActivity {
    public static final String KEY_FILE_PATH = "filePath";
    private int mFrom = 4001;
    private List<MediaData> mMediaDataList;
    private CustomTitleBar mTitleBar;
    private TextView mTvSingleStart;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_material_single_select;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        this.mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        this.mTvSingleStart = (TextView) findViewById(R.id.tv_single_start);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        Bundle extras;
        Intent intent = getIntent();
        if (!(intent == null || (extras = intent.getExtras()) == null)) {
            this.mFrom = extras.getInt("select_media_from", 4001);
            int i = this.mFrom;
            if (i == 4003 || i == 4008 || i == 4001) {
                this.mTitleBar.setTextCenter(R.string.single_select_picture);
            }
        }
        initVideoFragment();
    }

    private void initVideoFragment() {
        MediaFragment mediaFragment = new MediaFragment();
        Bundle bundle = new Bundle();
        int i = this.mFrom;
        if (i == 4003 || i == 4001 || i == 4008) {
            bundle.putInt(MediaConstant.MEDIA_TYPE, 2);
        } else if (i == 4004) {
            bundle.putInt(MediaConstant.MEDIA_TYPE, 1);
        }
        bundle.putInt(MediaConstant.KEY_CLICK_TYPE, 0);
        mediaFragment.setArguments(bundle);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.add(R.id.single_contain, mediaFragment).commit();
        beginTransaction.show(mediaFragment);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mTvSingleStart.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
        this.mTitleBar.setTextCenter(R.string.select_media);
    }

    public void onClick(View view) {
        this.mTvSingleStart.setClickable(false);
        int i = this.mFrom;
        if (i == 4003) {
            String path = this.mMediaDataList.get(0).getPath();
            Bundle bundle = new Bundle();
            bundle.putString(CustomAnimateStickerActivity.KEY_IMAGE_FILE_PATH, path);
            AppManager.getInstance().jumpActivity(AppManager.getInstance().currentActivity(), CustomAnimateStickerActivity.class, bundle);
            AppManager.getInstance().finishActivity();
        } else if (i == 4004) {
            Intent intent = new Intent();
            intent.putExtra(DraftEditActivity.VIDEO_PATH, this.mMediaDataList.get(0).getPath());
            setResult(100, intent);
            AppManager.getInstance().finishActivity();
        } else if (i == 4007) {
            MeicamVideoClip mediaInfoToMeicamClip = MediaUtils.mediaInfoToMeicamClip(this.mMediaDataList.get(0));
            Intent intent2 = new Intent();
            intent2.putExtra(KEY_FILE_PATH, mediaInfoToMeicamClip);
            setResult(-1, intent2);
            AppManager.getInstance().finishActivity();
        } else if (i == 4008) {
            String path2 = this.mMediaDataList.get(0).getPath();
            Intent intent3 = new Intent();
            intent3.putExtra(KEY_FILE_PATH, path2);
            setResult(-1, intent3);
            AppManager.getInstance().finishActivity();
        } else if (i == 4001) {
            String path3 = this.mMediaDataList.get(0).getPath();
            Intent intent4 = new Intent();
            intent4.putExtra(KEY_FILE_PATH, path3);
            setResult(-1, intent4);
            AppManager.getInstance().finishActivity();
        }
    }

    @Override // com.meishe.myvideo.interfaces.OnTotalNumChangeForActivity
    public void onTotalNumChangeForActivity(List list, Object obj) {
        this.mMediaDataList = list;
        this.mTvSingleStart.setVisibility(list.size() > 0 ? 0 : 8);
    }
}
