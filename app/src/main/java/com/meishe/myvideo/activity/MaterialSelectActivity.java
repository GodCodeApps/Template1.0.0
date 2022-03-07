package com.meishe.myvideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.TimelineDataUtil;
import com.meishe.myvideo.adapter.AgendaSimpleSectionAdapter;
import com.meishe.myvideo.adapter.BaseFragmentPagerAdapter;
import com.meishe.myvideo.bean.MediaData;
import com.meishe.myvideo.fragment.MediaFragment;
import com.meishe.myvideo.interfaces.OnTotalNumChangeForActivity;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.MediaConstant;
import com.meishe.myvideo.util.MediaUtils;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideo.view.CustomTitleBar;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MaterialSelectActivity extends BaseActivity implements OnTotalNumChangeForActivity {
    public static final int FROM_ADD_MATERIAL_TYPE = 2;
    public static final int FROM_MAIN_ACTIVITY_TYPE = 1;
    public static final String FROM_TYPE = "from_type";
    public static final String INSERT_INDEX_TYPE = "insert_index_type";
    private String TAG = getClass().getName();
    private List<Fragment> fragmentLists = new ArrayList();
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private List<String> fragmentTabTitles = new ArrayList();
    private Integer[] fragmentTotalNumber = {0, 0, 0};
    private int mFromType;
    private int mInsertIndex;
    private int mLimiteMediaCount = -1;
    private List<MediaData> mMediaDataList = new ArrayList();
    private CustomTitleBar mTitleBar;
    private TextView meidaTVOfStart;
    private int nowFragmentPosition = 0;
    private TabLayout tlSelectMedia;
    private int visitMethod = 1001;
    private ViewPager vpSelectMedia;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_material_select;
    }

    public void onClick(View view) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        this.mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        this.tlSelectMedia = (TabLayout) findViewById(R.id.tl_select_media);
        this.vpSelectMedia = (ViewPager) findViewById(R.id.vp_select_media);
        this.meidaTVOfStart = (TextView) findViewById(R.id.tv_start_edit);
        this.meidaTVOfStart.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.MaterialSelectActivity.AnonymousClass1 */

            public void onClick(View view) {
                MaterialSelectActivity.this.selectCreateRatio(0);
            }
        });
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
        this.mTitleBar.setTextCenter(R.string.select_media);
    }

    public void setTitleText(int i) {
        if (i > 0) {
            this.mTitleBar.setTextCenter(String.format(getResources().getString(R.string.setSelectMedia), Integer.valueOf(i)));
            return;
        }
        this.mTitleBar.setTextCenter(R.string.select_media);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        Bundle extras;
        Intent intent = getIntent();
        if (!(intent == null || (extras = intent.getExtras()) == null)) {
            this.mFromType = extras.getInt("from_type");
            this.mInsertIndex = extras.getInt(INSERT_INDEX_TYPE);
        }
        String[] stringArray = getResources().getStringArray(R.array.select_media);
        checkDataCountAndTypeCount(stringArray, MediaConstant.MEDIATYPECOUNT);
        this.fragmentLists = getSupportFragmentManager().getFragments();
        List<Fragment> list = this.fragmentLists;
        if (list == null || list.size() == 0) {
            this.fragmentLists = new ArrayList();
            for (int i = 0; i < stringArray.length; i++) {
                MediaFragment mediaFragment = new MediaFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(MediaConstant.LIMIT_COUNT_MAX, this.mLimiteMediaCount);
                bundle.putInt(MediaConstant.KEY_CLICK_TYPE, 1);
                bundle.putInt(MediaConstant.MEDIA_TYPE, MediaConstant.MEDIATYPECOUNT[i]);
                mediaFragment.setArguments(bundle);
                this.fragmentLists.add(mediaFragment);
            }
        }
        for (String str : stringArray) {
            this.fragmentTabTitles.add(str);
        }
        this.vpSelectMedia.setOffscreenPageLimit(3);
        this.fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), this.fragmentLists, this.fragmentTabTitles);
        this.vpSelectMedia.setAdapter(this.fragmentPagerAdapter);
        this.vpSelectMedia.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /* class com.meishe.myvideo.activity.MaterialSelectActivity.AnonymousClass2 */

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                MaterialSelectActivity.this.nowFragmentPosition = i;
                for (int i2 = 0; i2 < MaterialSelectActivity.this.fragmentLists.size(); i2++) {
                    MediaFragment mediaFragment = (MediaFragment) MaterialSelectActivity.this.fragmentLists.get(i2);
                    List asList = Arrays.asList(MaterialSelectActivity.this.fragmentTotalNumber);
                    if (!asList.isEmpty()) {
                        mediaFragment.setTotalSize(((Integer) Collections.max(asList)).intValue());
                    }
                }
                MaterialSelectActivity.this.notifyFragmentDataSetChanged(i);
            }
        });
        this.tlSelectMedia.setupWithViewPager(this.vpSelectMedia);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void notifyFragmentDataSetChanged(int i) {
        MediaFragment mediaFragment = (MediaFragment) this.fragmentLists.get(i);
        mediaFragment.getAdapter().setSelectList(checkoutSelectList(mediaFragment));
        setTitleText(mediaFragment.getAdapter().getSelectList().size());
        String str = this.TAG;
        Logger.d(str, "onPageSelected: " + mediaFragment.getAdapter().getSelectList().size());
    }

    private List<MediaData> checkoutSelectList(MediaFragment mediaFragment) {
        List<MediaData> selectList = mediaFragment.getAdapter().getSelectList();
        List<MediaData> mediaDataList = getMediaDataList();
        for (MediaData mediaData : selectList) {
            for (MediaData mediaData2 : mediaDataList) {
                if (mediaData2.getPath().equals(mediaData.getPath()) && mediaData2.isState() == mediaData.isState()) {
                    mediaData.setPosition(mediaData2.getPosition());
                }
            }
        }
        return selectList;
    }

    private void checkDataCountAndTypeCount(String[] strArr, int[] iArr) {
        if (strArr.length != iArr.length) {
        }
    }

    @Override // androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback, androidx.fragment.app.FragmentActivity
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        for (int i2 = 0; i2 < this.fragmentLists.size(); i2++) {
            this.fragmentLists.get(i2).onRequestPermissionsResult(i, strArr, iArr);
        }
    }

    @Override // com.meishe.myvideo.interfaces.OnTotalNumChangeForActivity
    public void onTotalNumChangeForActivity(List list, Object obj) {
        int intValue = ((Integer) obj).intValue();
        this.fragmentTotalNumber[intValue] = Integer.valueOf(list.size());
        this.meidaTVOfStart.setVisibility(((Integer) Collections.max(Arrays.asList(this.fragmentTotalNumber))).intValue() > 0 ? 0 : 8);
        for (int i = 0; i < this.fragmentLists.size(); i++) {
            if (i != intValue) {
                ((MediaFragment) this.fragmentLists.get(i)).refreshSelect(list, intValue);
            }
        }
        if (intValue == this.nowFragmentPosition) {
            setTitleText(list.size());
        }
    }

    public List<MediaData> getMediaDataList() {
        AgendaSimpleSectionAdapter adapter;
        if (this.mMediaDataList == null) {
            return new ArrayList();
        }
        MediaFragment mediaFragment = (MediaFragment) this.fragmentLists.get(0);
        if (mediaFragment == null || (adapter = mediaFragment.getAdapter()) == null) {
            return new ArrayList();
        }
        return adapter.getSelectList();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onStop() {
        super.onStop();
        Logger.d(this.TAG, "onStop");
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onDestroy() {
        this.nowFragmentPosition = 0;
        super.onDestroy();
        Logger.e(this.TAG, "onDestroy");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void selectCreateRatio(int i) {
        ArrayList<MeicamVideoClip> clipInfoList = getClipInfoList();
        String str = this.TAG;
        Logger.d(str, "selectCreateRatio: " + this.mFromType);
        if (this.mFromType == 2) {
            TimelineDataUtil.addMainTrackData(clipInfoList, this.mInsertIndex);
            setResult(-1);
        } else {
            TimelineData.getInstance().clearData();
            TimelineData.getInstance().setVideoResolution(Util.getVideoEditResolutionByClip(clipInfoList.get(0).getFilePath()));
            TimelineDataUtil.setMainTrackData(clipInfoList);
            TimelineData.getInstance().setMakeRatio(i);
            AppManager.getInstance().jumpActivity(this, DraftEditActivity.class, null);
        }
        AppManager.getInstance().finishActivity();
    }

    private ArrayList<MeicamVideoClip> getClipInfoList() {
        ArrayList<MeicamVideoClip> arrayList = new ArrayList<>();
        List<MediaData> mediaDataList = getMediaDataList();
        for (int i = 0; i < mediaDataList.size(); i++) {
            arrayList.add(MediaUtils.mediaInfoToMeicamClip(mediaDataList.get(i)));
        }
        return arrayList;
    }

    private ArrayList<String> getPicInPicVideoList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (MediaData mediaData : getMediaDataList()) {
            arrayList.add(mediaData.getPath());
        }
        return arrayList;
    }
}
