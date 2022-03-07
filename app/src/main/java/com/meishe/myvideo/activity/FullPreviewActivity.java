package com.meishe.myvideo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoResolution;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.ui.Utils.FormatUtils;
import com.meishe.myvideo.util.engine.EditorEngine;
import com.meishe.myvideoapp.R;
import com.meishe.player.view.VideoFragment;

public class FullPreviewActivity extends BaseActivity implements VideoFragment.VideoFragmentListener {
    private static final String TAG = "FullPreviewActivity";
    private EditorEngine mEditorEngine;
    private ImageView mFullPreviewClose;
    private TextView mFullPreviewNow;
    private ImageView mFullPreviewPlay;
    private SeekBar mFullPreviewSeekBar;
    private TextView mFullPreviewTotal;
    private NvsTimeline mTimeline;
    private boolean mTouchToChangeProgress = false;
    private VideoFragment mVideoFragment;
    private Long startTime;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_full_preview;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
    }

    @Override // com.meishe.player.view.VideoFragment.VideoFragmentListener
    public void playStopped(NvsTimeline nvsTimeline) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        this.mFullPreviewPlay = (ImageView) findViewById(R.id.full_preview_play);
        this.mFullPreviewPlay.setOnClickListener(this);
        this.mFullPreviewClose = (ImageView) findViewById(R.id.full_preview_close);
        this.mFullPreviewClose.setOnClickListener(this);
        this.mFullPreviewNow = (TextView) findViewById(R.id.full_preview_now);
        this.mFullPreviewTotal = (TextView) findViewById(R.id.full_preview_total);
        this.mFullPreviewSeekBar = (SeekBar) findViewById(R.id.full_preview_seekBar);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.startTime = Long.valueOf(extras.getLong("nowTime"));
            Logger.d(TAG, "initData: " + this.startTime);
        }
        initPlayer();
    }

    private void initPlayer() {
        this.mEditorEngine = EditorEngine.getInstance();
        this.mTimeline = this.mEditorEngine.getCurrentTimeline();
        NvsVideoResolution videoRes = this.mTimeline.getVideoRes();
        Log.e(TAG, "initPlayer: " + videoRes.imageWidth + "  " + videoRes.imageHeight);
        if (videoRes.imageWidth - videoRes.imageHeight > 0) {
            setRequestedOrientation(0);
        }
        this.mFullPreviewTotal.setText(FormatUtils.formatTimeStrWithUs(this.mTimeline.getDuration()));
        this.mFullPreviewSeekBar.setMax((int) (this.mTimeline.getDuration() / 1000));
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.mVideoFragment = VideoFragment.newInstance(0);
        supportFragmentManager.beginTransaction().add(R.id.edit_preview_view, this.mVideoFragment).commit();
        supportFragmentManager.beginTransaction().show(this.mVideoFragment);
        this.mVideoFragment.setTimeLine(this.mTimeline);
        this.mVideoFragment.setVideoFragmentCallBack(this);
        new Handler().post(new Runnable() {
            /* class com.meishe.myvideo.activity.FullPreviewActivity.AnonymousClass1 */

            public void run() {
                FullPreviewActivity.this.mVideoFragment.initData();
                FullPreviewActivity.this.mFullPreviewSeekBar.setProgress((int) (FullPreviewActivity.this.startTime.longValue() / 1000));
                FullPreviewActivity fullPreviewActivity = FullPreviewActivity.this;
                fullPreviewActivity.updatePlaySeekBar(fullPreviewActivity.startTime.longValue());
                FullPreviewActivity.this.mFullPreviewNow.setText(FormatUtils.formatTimeStrWithUs(FullPreviewActivity.this.startTime.longValue()));
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updatePlaySeekBar(long j) {
        this.mVideoFragment.seekTimeline(j, 1);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mFullPreviewSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.activity.FullPreviewActivity.AnonymousClass2 */

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    long j = (long) (i * 1000);
                    FullPreviewActivity.this.mEditorEngine.seekTimeline(j, 0);
                    FullPreviewActivity.this.mFullPreviewNow.setText(FormatUtils.formatTimeStrWithUs(j));
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                FullPreviewActivity.this.mEditorEngine.stop();
                FullPreviewActivity.this.mTouchToChangeProgress = true;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                FullPreviewActivity.this.mTouchToChangeProgress = false;
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.full_preview_close) {
            finish();
        } else if (id == R.id.full_preview_play) {
            if (NvsStreamingContext.getInstance().getStreamingEngineState() == 3) {
                this.mVideoFragment.stopEngine();
                return;
            }
            long timelineCurrentPosition = NvsStreamingContext.getInstance().getTimelineCurrentPosition(this.mTimeline);
            if (timelineCurrentPosition == this.mTimeline.getDuration()) {
                timelineCurrentPosition = 0;
            }
            this.mVideoFragment.playVideoButtonClick(timelineCurrentPosition, this.mTimeline.getDuration());
        }
    }

    @Override // com.meishe.player.view.VideoFragment.VideoFragmentListener
    public void playBackEOF(NvsTimeline nvsTimeline) {
        this.mFullPreviewSeekBar.setProgress(0);
        this.mEditorEngine.seekTimeline(0, 0);
    }

    @Override // com.meishe.player.view.VideoFragment.VideoFragmentListener
    public void playbackTimelinePosition(NvsTimeline nvsTimeline, long j) {
        this.mFullPreviewNow.setText(FormatUtils.formatTimeStrWithUs(j));
        if (!this.mTouchToChangeProgress) {
            this.mFullPreviewSeekBar.setProgress((int) (j / 1000));
        }
    }

    @Override // com.meishe.player.view.VideoFragment.VideoFragmentListener
    public void streamingEngineStateChanged(int i) {
        if (i == 3) {
            this.mFullPreviewPlay.setImageResource(R.mipmap.control_bar_ic_pause);
        } else {
            this.mFullPreviewPlay.setImageResource(R.mipmap.control_bar_ic_play);
        }
    }
}
