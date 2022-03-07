package com.meishe.myvideo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;

public class VideoPreviewFragment extends Fragment {
    private final String TAG = VideoPreviewFragment.class.getSimpleName();
    private NvsLiveWindow mLiveWindow;
    private boolean mPlayBarVisibleState;
    private RelativeLayout mPlayerLayout;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_preview_video, viewGroup, false);
        this.mLiveWindow = (NvsLiveWindow) inflate.findViewById(R.id.liveWindow);
        this.mPlayerLayout = (RelativeLayout) inflate.findViewById(R.id.player_layout);
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mStreamingContext = NvsStreamingContext.getInstance();
        initData();
    }

    private void initData() {
        initLiveWindow();
    }

    private void initLiveWindow() {
        int i;
        Bundle arguments = getArguments();
        int i2 = 0;
        if (arguments != null) {
            int i3 = arguments.getInt("ratio", 1);
            i = arguments.getInt("middleOperationHeight");
            this.mPlayBarVisibleState = arguments.getBoolean("playBarVisible", true);
            i2 = i3;
        } else {
            i = 0;
        }
        if (this.mTimeline == null) {
            Logger.e(this.TAG, "timeline is null");
            return;
        }
        setLiveWindowRatio(i2, i);
        connectTimelineWithLiveWindow();
    }

    private void connectTimelineWithLiveWindow() {
        NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
        if (nvsStreamingContext != null && this.mTimeline != null && this.mLiveWindow != null) {
            nvsStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
                /* class com.meishe.myvideo.fragment.VideoPreviewFragment.AnonymousClass1 */

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
                public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                }

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
                public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {
                }

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
                public void onPlaybackStopped(NvsTimeline nvsTimeline) {
                }
            });
            this.mStreamingContext.connectTimelineWithLiveWindow(this.mTimeline, this.mLiveWindow);
        }
    }

    private void setLiveWindowRatio(int i, int i2) {
        ViewGroup.LayoutParams layoutParams = this.mPlayerLayout.getLayoutParams();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(getActivity());
        int screenWidth = ScreenUtils.getScreenWidth(getActivity());
        int screenHeight = (ScreenUtils.getScreenHeight(getActivity()) - i2) - statusBarHeight;
        if (i == 1) {
            layoutParams.width = screenWidth;
            double d = (double) screenWidth;
            Double.isNaN(d);
            layoutParams.height = (int) ((d * 9.0d) / 16.0d);
        } else if (i == 2) {
            layoutParams.width = screenWidth;
            layoutParams.height = screenWidth;
            if (screenHeight < screenWidth) {
                layoutParams.width = screenHeight;
                layoutParams.height = screenHeight;
            }
        } else if (i == 4) {
            double d2 = (double) screenHeight;
            Double.isNaN(d2);
            layoutParams.width = (int) ((d2 * 9.0d) / 16.0d);
            layoutParams.height = screenHeight;
        } else if (i == 8) {
            layoutParams.width = screenWidth;
            double d3 = (double) screenWidth;
            Double.isNaN(d3);
            layoutParams.height = (int) ((d3 * 3.0d) / 4.0d);
        } else if (i != 16) {
            layoutParams.width = screenWidth;
            double d4 = (double) screenWidth;
            Double.isNaN(d4);
            layoutParams.height = (int) ((d4 * 9.0d) / 16.0d);
        } else {
            double d5 = (double) screenHeight;
            Double.isNaN(d5);
            layoutParams.width = (int) ((d5 * 3.0d) / 4.0d);
            layoutParams.height = screenHeight;
        }
        this.mPlayerLayout.setLayoutParams(layoutParams);
        this.mLiveWindow.setFillMode(1);
    }

    public NvsTimeline getTimeline() {
        return this.mTimeline;
    }

    public void setTimeline(NvsTimeline nvsTimeline) {
        this.mTimeline = nvsTimeline;
        connectTimelineWithLiveWindow();
    }
}
