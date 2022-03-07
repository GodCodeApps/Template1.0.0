package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meishe.myvideo.bean.Record.RecordAreaInfo;
import com.meishe.myvideoapp.R;
import java.util.HashMap;
import java.util.Map;

public class MYRecordTrackView extends RelativeLayout {
    private Map<Long, RecordAreaInfo> mAreasInfo;
    private Context mContext;
    private LinearLayout mLlAllView;
    private RelativeLayout mRlRecordAreasView;

    public MYRecordTrackView(Context context) {
        super(context);
        init(context);
    }

    public MYRecordTrackView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mAreasInfo = new HashMap();
    }

    public void initRecordView(NvsMultiThumbnailSequenceView nvsMultiThumbnailSequenceView, int i) {
        this.mLlAllView = new LinearLayout(this.mContext);
        addView(this.mLlAllView, new LinearLayout.LayoutParams(-1, -1));
        this.mRlRecordAreasView = new RelativeLayout(this.mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, -1);
        View view = new View(this.mContext);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(nvsMultiThumbnailSequenceView.getStartPadding(), -1);
        View view2 = new View(this.mContext);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(nvsMultiThumbnailSequenceView.getEndPadding(), -1);
        this.mLlAllView.addView(view, layoutParams2);
        this.mLlAllView.addView(this.mRlRecordAreasView, layoutParams);
        this.mLlAllView.addView(view2, layoutParams3);
    }

    public void addRecordView(long j, long j2, String str, double d) {
        int i;
        double d2 = (double) j;
        Double.isNaN(d2);
        int i2 = (int) (d2 * d);
        View surfaceView = new SurfaceView(this.mContext);
        surfaceView.setBackgroundColor(this.mContext.getResources().getColor(R.color.white));
        if (j2 > 0) {
            double d3 = (double) (j2 - j);
            Double.isNaN(d3);
            i = (int) (d3 * d);
        } else {
            i = 0;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, -1);
        layoutParams.setMargins(i2, 0, 0, 0);
        this.mRlRecordAreasView.addView(surfaceView, layoutParams);
        RecordAreaInfo recordAreaInfo = new RecordAreaInfo();
        recordAreaInfo.setInPoint(j);
        recordAreaInfo.setOutPoint(j2);
        recordAreaInfo.setInPosition(i2);
        recordAreaInfo.setAreaView(surfaceView);
        surfaceView.setTag(recordAreaInfo);
        this.mAreasInfo.put(Long.valueOf(j), recordAreaInfo);
    }

    public void updateRecordView(long j, long j2, double d) {
        RelativeLayout.LayoutParams layoutParams;
        RecordAreaInfo recordAreaInfo = this.mAreasInfo.get(Long.valueOf(j));
        if (recordAreaInfo != null && recordAreaInfo.getAreaView() != null && (layoutParams = (RelativeLayout.LayoutParams) recordAreaInfo.getAreaView().getLayoutParams()) != null) {
            int inPosition = recordAreaInfo.getInPosition();
            double d2 = (double) (j2 - j);
            Double.isNaN(d2);
            layoutParams.setMargins(inPosition, 0, 0, 0);
            layoutParams.width = (int) (d2 * d);
            recordAreaInfo.getAreaView().setLayoutParams(layoutParams);
            recordAreaInfo.setOutPoint(j2);
            this.mRlRecordAreasView.requestLayout();
        }
    }

    public View getRecordContainerView() {
        return this.mLlAllView;
    }
}
