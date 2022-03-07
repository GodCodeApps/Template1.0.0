package com.meishe.myvideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.meishe.common.utils.TimeFormatUtil;
import com.meishe.myvideo.bean.MusicInfo;
import com.meishe.myvideo.fragment.LocalMusicFragment;
import com.meishe.myvideo.fragment.MyMusicFragment;
import com.meishe.myvideo.interfaces.OnTitleBarClickListener;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.AudioPlayer;
import com.meishe.myvideo.util.AudioUtil;
import com.meishe.myvideo.view.CustomTitleBar;
import com.meishe.myvideo.view.CustomViewPager;
import com.meishe.myvideo.view.CutMusicView;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class SelectMusicActivity extends BaseActivity {
    private AudioUtil mAudioUtil;
    private CutMusicView mCutMusicView;
    private RelativeLayout mCutViewLayout;
    private int mDouyinMinDuration = 15000000;
    private List<Fragment> mFragmentList;
    private String[] mFragmentTitle;
    private int mFromWhatPage = 5002;
    private RelativeLayout mHaveSelectLayout;
    private TextView mLocalMusicBtn;
    private LocalMusicFragment mLocalMusicFragment;
    private View mLocalMusicSelectView;
    private TextView mMyMusicBtn;
    private MyMusicFragment mMyMusicFragment;
    private View mMyMusicSelectView;
    private MusicInfo mPlayMusic;
    private boolean mRightSelected = false;
    private TextView mSelectMusicName;
    private TextView mSelectMusicTime;
    private RelativeLayout mTabLayout;
    private CustomTitleBar mTitleBar;
    private Button mUseBtn;
    private CustomViewPager mViewPager;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_select_music;
    }

    public void onClick(View view) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        this.mTabLayout = (RelativeLayout) findViewById(R.id.tab_layout);
        this.mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        this.mMyMusicBtn = (TextView) findViewById(R.id.my_music_text);
        this.mLocalMusicBtn = (TextView) findViewById(R.id.local_music_text);
        this.mMyMusicSelectView = findViewById(R.id.my_music_select_view);
        this.mLocalMusicSelectView = findViewById(R.id.local_music_select_view);
        this.mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        this.mHaveSelectLayout = (RelativeLayout) findViewById(R.id.have_select_layout);
        this.mSelectMusicName = (TextView) findViewById(R.id.select_music_name);
        this.mSelectMusicTime = (TextView) findViewById(R.id.select_music_time);
        this.mCutMusicView = (CutMusicView) findViewById(R.id.select_music_cut_view);
        this.mUseBtn = (Button) findViewById(R.id.select_music_use_btn);
        this.mCutViewLayout = (RelativeLayout) findViewById(R.id.select_music_cut_layout);
        initViewPager();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
        this.mTitleBar.setTextCenter(R.string.music_select);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        Bundle extras;
        Intent intent = getIntent();
        if (!(intent == null || (extras = intent.getExtras()) == null)) {
            this.mFromWhatPage = extras.getInt("select_music_from", 5002);
        }
        this.mFragmentTitle = getResources().getStringArray(R.array.music_fragment_title);
        this.mAudioUtil = new AudioUtil(this);
        if (this.mFromWhatPage == 5003) {
            this.mViewPager.setCurrentItem(1);
            this.mTabLayout.setVisibility(8);
            this.mTitleBar.setBackImageVisible(0);
            this.mViewPager.setScanScroll(false);
        }
        this.mAudioUtil.getMedias(1, new AudioUtil.LocalMediaLoadListener() {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass1 */

            @Override // com.meishe.myvideo.util.AudioUtil.LocalMediaLoadListener
            public void loadComplete(List<MusicInfo> list) {
                if (SelectMusicActivity.this.mLocalMusicFragment != null) {
                    SelectMusicActivity.this.mLocalMusicFragment.loadAudioData(list);
                }
            }
        });
        new Thread(new Runnable() {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass2 */

            public void run() {
                final List<MusicInfo> listMusicFilesFromAssets = SelectMusicActivity.this.mAudioUtil.listMusicFilesFromAssets(SelectMusicActivity.this);
                SelectMusicActivity.this.runOnUiThread(new Runnable() {
                    /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass2.AnonymousClass1 */

                    public void run() {
                        if (SelectMusicActivity.this.mMyMusicFragment != null) {
                            SelectMusicActivity.this.mMyMusicFragment.loadAudioData(listMusicFilesFromAssets);
                        }
                    }
                });
            }
        }).start();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mLocalMusicBtn.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass3 */

            public void onClick(View view) {
                SelectMusicActivity.this.mLocalMusicBtn.setTextColor(ContextCompat.getColor(SelectMusicActivity.this, R.color.start_edit_bg));
                SelectMusicActivity.this.mMyMusicBtn.setTextColor(ContextCompat.getColor(SelectMusicActivity.this, R.color.white));
                if (SelectMusicActivity.this.mRightSelected) {
                    SelectMusicActivity.this.mRightSelected = false;
                    SelectMusicActivity.this.rightToLeft();
                    SelectMusicActivity.this.mViewPager.setCurrentItem(0);
                }
            }
        });
        this.mMyMusicBtn.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass4 */

            public void onClick(View view) {
                SelectMusicActivity.this.mMyMusicBtn.setTextColor(ContextCompat.getColor(SelectMusicActivity.this, R.color.start_edit_bg));
                SelectMusicActivity.this.mLocalMusicBtn.setTextColor(ContextCompat.getColor(SelectMusicActivity.this, R.color.white));
                if (!SelectMusicActivity.this.mRightSelected) {
                    SelectMusicActivity.this.mRightSelected = true;
                    SelectMusicActivity.this.leftToRight();
                    SelectMusicActivity.this.mViewPager.setCurrentItem(1);
                }
            }
        });
        this.mUseBtn.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass5 */

            public void onClick(View view) {
                SelectMusicActivity.this.closeMusicActivity();
            }
        });
        this.mCutMusicView.setOnSeekBarChangedListener(new CutMusicView.OnSeekBarChanged() {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass6 */

            @Override // com.meishe.myvideo.view.CutMusicView.OnSeekBarChanged
            public void onCenterTouched(long j, long j2) {
            }

            @Override // com.meishe.myvideo.view.CutMusicView.OnSeekBarChanged
            public void onLeftValueChange(long j) {
                if (SelectMusicActivity.this.mPlayMusic != null) {
                    TextView textView = SelectMusicActivity.this.mSelectMusicTime;
                    textView.setText(TimeFormatUtil.formatUsToString2(j) + "/" + TimeFormatUtil.formatUsToString2(SelectMusicActivity.this.mPlayMusic.getTrimOut()));
                }
            }

            @Override // com.meishe.myvideo.view.CutMusicView.OnSeekBarChanged
            public void onRightValueChange(long j) {
                if (SelectMusicActivity.this.mPlayMusic != null) {
                    TextView textView = SelectMusicActivity.this.mSelectMusicTime;
                    textView.setText(TimeFormatUtil.formatUsToString2(SelectMusicActivity.this.mPlayMusic.getTrimIn()) + "/" + TimeFormatUtil.formatUsToString2(j));
                }
            }

            @Override // com.meishe.myvideo.view.CutMusicView.OnSeekBarChanged
            public void onUpTouched(boolean z, long j, long j2) {
                if (SelectMusicActivity.this.mPlayMusic != null) {
                    SelectMusicActivity.this.mPlayMusic.setTrimIn(j);
                    SelectMusicActivity.this.mPlayMusic.setTrimOut(j2);
                }
                if (z) {
                    if (SelectMusicActivity.this.mPlayMusic != null && SelectMusicActivity.this.mPlayMusic.isPlay()) {
                        AudioPlayer.getInstance(SelectMusicActivity.this).seekPosition(j);
                    }
                    SelectMusicActivity.this.mCutMusicView.setIndicator(j);
                }
            }
        });
        AudioPlayer.getInstance(this).setPlayListener(new AudioPlayer.OnPlayListener() {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass7 */

            @Override // com.meishe.myvideo.util.AudioPlayer.OnPlayListener
            public void onMusicPlay() {
            }

            @Override // com.meishe.myvideo.util.AudioPlayer.OnPlayListener
            public void onMusicStop() {
            }

            @Override // com.meishe.myvideo.util.AudioPlayer.OnPlayListener
            public void onGetCurrentPos(int i) {
                long j = (long) i;
                SelectMusicActivity.this.mCutMusicView.setIndicator(j);
                SelectMusicActivity.this.mSelectMusicTime.setText(String.format("%s/%s", TimeFormatUtil.formatUsToString2(j), TimeFormatUtil.formatUsToString2(SelectMusicActivity.this.mPlayMusic.getTrimOut())));
            }
        });
        this.mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass8 */

            @Override // com.meishe.myvideo.interfaces.OnTitleBarClickListener
            public void OnCenterTextClick() {
            }

            @Override // com.meishe.myvideo.interfaces.OnTitleBarClickListener
            public void OnRightTextClick() {
            }

            @Override // com.meishe.myvideo.interfaces.OnTitleBarClickListener
            public void OnBackImageClick() {
                AudioPlayer.getInstance(SelectMusicActivity.this.getApplicationContext()).destroyPlayer();
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void rightToLeft() {
        TranslateAnimation translateAnimation = new TranslateAnimation(this.mMyMusicSelectView.getX(), this.mLocalMusicSelectView.getX(), this.mMyMusicSelectView.getY(), this.mLocalMusicSelectView.getY());
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        this.mLocalMusicSelectView.startAnimation(translateAnimation);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void leftToRight() {
        TranslateAnimation translateAnimation = new TranslateAnimation(this.mLocalMusicSelectView.getX(), this.mMyMusicSelectView.getX(), this.mLocalMusicSelectView.getY(), this.mMyMusicSelectView.getY());
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        this.mLocalMusicSelectView.startAnimation(translateAnimation);
    }

    private void initViewPager() {
        this.mFragmentList = new ArrayList();
        this.mLocalMusicFragment = new LocalMusicFragment();
        this.mMyMusicFragment = new MyMusicFragment();
        this.mFragmentList.add(this.mLocalMusicFragment);
        this.mFragmentList.add(this.mMyMusicFragment);
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass9 */

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                if (i == 0) {
                    SelectMusicActivity.this.mLocalMusicBtn.performClick();
                } else if (i == 1) {
                    SelectMusicActivity.this.mMyMusicBtn.performClick();
                }
            }
        });
        this.mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass10 */

            @Override // androidx.fragment.app.FragmentPagerAdapter
            public Fragment getItem(int i) {
                return (Fragment) SelectMusicActivity.this.mFragmentList.get(i);
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return SelectMusicActivity.this.mFragmentList.size();
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public CharSequence getPageTitle(int i) {
                return SelectMusicActivity.this.mFragmentTitle[i];
            }
        });
        this.mViewPager.setCurrentItem(0);
    }

    public void playMusic(MusicInfo musicInfo, boolean z) {
        if (musicInfo != null) {
            if (z) {
                this.mMyMusicFragment.clearPlayState();
            } else {
                this.mLocalMusicFragment.clearPlayState();
            }
            this.mPlayMusic = musicInfo;
            if (this.mPlayMusic.isPlay()) {
                this.mHaveSelectLayout.setVisibility(8);
                AudioPlayer.getInstance(this).stopPlay();
                return;
            }
            this.mHaveSelectLayout.setVisibility(0);
            this.mCutViewLayout.postDelayed(new Runnable() {
                /* class com.meishe.myvideo.activity.SelectMusicActivity.AnonymousClass11 */

                public void run() {
                    SelectMusicActivity.this.mSelectMusicName.setText(SelectMusicActivity.this.mPlayMusic.getTitle());
                    String formatUsToString2 = TimeFormatUtil.formatUsToString2(SelectMusicActivity.this.mPlayMusic.getDuration());
                    if (SelectMusicActivity.this.mFromWhatPage == 5001) {
                        TextView textView = SelectMusicActivity.this.mSelectMusicTime;
                        textView.setText("00:00/" + formatUsToString2);
                        SelectMusicActivity.this.mCutMusicView.setRightHandleVisiable(false);
                        SelectMusicActivity.this.mCutMusicView.setMinDuration((long) SelectMusicActivity.this.mDouyinMinDuration);
                    } else {
                        SelectMusicActivity.this.mSelectMusicTime.setText(SelectMusicActivity.this.mPlayMusic.getArtist());
                        SelectMusicActivity.this.mCutMusicView.setRightHandleVisiable(true);
                        SelectMusicActivity.this.mCutMusicView.setMinDuration(1000000);
                    }
                    SelectMusicActivity.this.mCutMusicView.setCutLayoutWidth(SelectMusicActivity.this.mCutViewLayout.getWidth());
                    SelectMusicActivity.this.mCutMusicView.setCanTouchCenterMove(false);
                    SelectMusicActivity.this.mCutMusicView.setMaxDuration(SelectMusicActivity.this.mPlayMusic.getDuration());
                    SelectMusicActivity.this.mCutMusicView.setInPoint(0);
                    SelectMusicActivity.this.mCutMusicView.setOutPoint(SelectMusicActivity.this.mPlayMusic.getDuration());
                    SelectMusicActivity.this.mCutMusicView.reLayout();
                }
            }, 100);
            this.mPlayMusic.setTrimIn(0);
            MusicInfo musicInfo2 = this.mPlayMusic;
            musicInfo2.setTrimOut(musicInfo2.getDuration());
            AudioPlayer.getInstance(this).setCurrentMusic(this.mPlayMusic, true);
            AudioPlayer.getInstance(this).startPlay();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void closeMusicActivity() {
        Intent intent = new Intent();
        MusicInfo musicInfo = this.mPlayMusic;
        if (musicInfo != null) {
            if (this.mFromWhatPage == 5002) {
                if (musicInfo.getDuration() - this.mPlayMusic.getTrimIn() <= ((long) this.mDouyinMinDuration)) {
                    MusicInfo musicInfo2 = this.mPlayMusic;
                    musicInfo2.setTrimOut(musicInfo2.getDuration() - this.mPlayMusic.getTrimIn());
                } else {
                    MusicInfo musicInfo3 = this.mPlayMusic;
                    musicInfo3.setTrimOut(musicInfo3.getTrimIn() + ((long) this.mDouyinMinDuration));
                }
            }
            intent.putExtra("select_music", this.mPlayMusic);
        }
        setResult(-1, intent);
        finishActivity();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.fragment.app.FragmentActivity
    public void onResume() {
        super.onResume();
        MusicInfo musicInfo = this.mPlayMusic;
        if (musicInfo != null) {
            if (musicInfo.isAsset()) {
                this.mMyMusicFragment.setSelectedMusic(this.mPlayMusic);
            } else {
                this.mLocalMusicFragment.setSelectedMusic(this.mPlayMusic);
            }
            AudioPlayer.getInstance(getApplicationContext()).startPlay();
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onStop() {
        super.onStop();
        AudioPlayer.getInstance(getApplicationContext()).stopPlay();
    }

    private void finishActivity() {
        AudioPlayer.getInstance(getApplicationContext()).destroyPlayer();
        AppManager.getInstance().finishActivity();
    }
}
