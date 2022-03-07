package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamVideoTrack;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.ui.bean.BaseUIClip;
import com.meishe.myvideo.ui.bean.BaseUIVideoClip;
import com.meishe.myvideo.ui.trackview.TrackViewLayout;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;
import com.meishe.myvideo.util.ui.TrackViewDataHelper;
import com.meishe.myvideoapp.R;
import java.util.HashMap;
import java.util.List;

public class MYEditorTimelineTrackView extends TrackViewLayout {
    private static final String TAG = "MYEditorTimelineTrackVi";
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;

    public MYEditorTimelineTrackView(Context context) {
        super(context);
    }

    public MYEditorTimelineTrackView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MYEditorTimelineTrackView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.meishe.myvideo.ui.trackview.TrackViewLayout
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Logger.d(TAG, "onTouchEvent: " + motionEvent.getAction());
        return super.onTouchEvent(motionEvent);
    }

    public void setTimeline(NvsTimeline nvsTimeline) {
        this.mTimeline = nvsTimeline;
        initWidth(nvsTimeline.getDuration());
    }

    public void setStreamingContext(NvsStreamingContext nvsStreamingContext) {
        this.mStreamingContext = nvsStreamingContext;
    }

    public void setTrackViewLayoutData(HashMap<Integer, List<BaseUIClip>> hashMap, long j) {
        setData(hashMap, j);
    }

    public void showPipTrackView(NvsTimeline nvsTimeline) {
        List<MeicamVideoTrack> meicamVideoTrackList;
        if (nvsTimeline != null && (meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList()) != null && meicamVideoTrackList.size() >= 1) {
            setData(TrackViewDataHelper.getInstance().getTrackData(CommonData.CLIP_VIDEO), nvsTimeline.getDuration());
        }
    }

    public void setSelect(int i, long j) {
        BaseUIVideoClip baseUIVideoClip = new BaseUIVideoClip();
        baseUIVideoClip.setTrackIndex(i);
        baseUIVideoClip.setInPoint(j);
        setSelect(baseUIVideoClip);
    }

    public void setSelect(BaseUIClip baseUIClip) {
        setSelectDragView(baseUIClip);
        audioScroll(baseUIClip);
    }

    public void audioScroll(BaseUIClip baseUIClip) {
        if (baseUIClip.getType() == CommonData.CLIP_AUDIO) {
            scrollAnimation(baseUIClip.getTrackIndex() * (getResources().getDimensionPixelOffset(R.dimen.track_view_real_height) + getResources().getDimensionPixelOffset(R.dimen.track_view_real_margin_top)));
        }
    }

    public void audioScrollX(BaseUIClip baseUIClip) {
        long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
        long inPoint = (baseUIClip.getInPoint() + baseUIClip.getTrimOut()) - baseUIClip.getTrimIn();
        if (timelineCurrentPosition < baseUIClip.getInPoint()) {
            smoothScrollView(PixelPerMicrosecondUtil.durationToLength(baseUIClip.getInPoint()));
        } else if (timelineCurrentPosition > inPoint) {
            smoothScrollView(PixelPerMicrosecondUtil.durationToLength(inPoint));
        }
    }
}
