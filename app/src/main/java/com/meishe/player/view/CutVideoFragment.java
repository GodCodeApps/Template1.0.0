package com.meishe.player.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.meicam.sdk.NvsLiveWindowExt;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoResolution;
import com.meishe.common.utils.Logger;
import com.meishe.common.utils.TimeFormatUtil;
import com.meishe.engine.data.CutData;
import com.meishe.engine.util.StoryboardUtil;
import com.meishe.myvideoapp.R;

import java.util.HashMap;
import java.util.Map;

public class CutVideoFragment extends Fragment {
    private static final int MESSAGE_RESET_PLATBACK_STATE = 100;
    private static final String PARAM_MAX_DURATION = "max_duration";
    private static final String TAG = "CutVideoFragment";
    FloatPoint mCenterPoint;
    private TextView mCurrentPlaytimeView;
    private CutRectLayout mCutView;
    private OnFragmentLoadFinisedListener mFragmentLoadFinisedListener;
    private Handler mHandler = new Handler(new Handler.Callback() {
        /* class com.meishe.player.view.CutVideoFragment.AnonymousClass1 */

        public boolean handleMessage(Message message) {
            if (message.what != 100) {
                return false;
            }
            CutVideoFragment cutVideoFragment = CutVideoFragment.this;
            cutVideoFragment.playVideo(cutVideoFragment.mStartTime, CutVideoFragment.this.mStartTime + CutVideoFragment.this.getDuration());
            return false;
        }
    });
    private NvsLiveWindowExt mLiveWindow;
    private long mMaxDuration;
    private float mMinLiveWindowScale;
    private OnCutRectChangedListener mOnCutRectChangedListener;
    private OnPlayProgressChangeListener mOnPlayProgessChangeListener;
    private Point mOriginalSize;
    private View mPlayButton;
    private ImageView mPlayButtonImage;
    private RelativeLayout mPlayerLayout;
    private int mRatio;
    private float mRatioValue;
    private RectF mRegionData;
    private SeekBar mSeekBar;
    private float[] mSize;
    private long mStartTime;
    private NvsStreamingContext mStreamingContext = NvsStreamingContext.getInstance();
    private NvsTimeline mTimeline;
    private TextView mTotalDurationView;
    private Map<String, Float> mTransformData;
    private VideoFragmentListener mVideoFragmentCallBack;

    public interface OnCutRectChangedListener {
        void onScaleAndRotate(float f, float f2);
    }

    public interface OnFragmentLoadFinisedListener {
        void onLoadFinished();
    }

    public interface OnLiveWindowClickListener {
        void onLiveWindowClick();
    }

    public interface OnPlayProgressChangeListener {
        void onPlayProgressChanged(long j);

        void onPlayStateChanged(boolean z);
    }

    public interface OnSaveOperationListener {
        void onSaveCurrentTimeline();
    }

    public interface VideoFragmentListener {
        void playBackEOF(NvsTimeline nvsTimeline);

        void playStopped(NvsTimeline nvsTimeline);

        void playbackTimelinePosition(NvsTimeline nvsTimeline, long j);

        void streamingEngineStateChanged(int i);
    }

    public void setOnCutRectChangelisener(OnCutRectChangedListener onCutRectChangedListener) {
        this.mOnCutRectChangedListener = onCutRectChangedListener;
    }

    public void setOnPlayProgressChangeListener(OnPlayProgressChangeListener onPlayProgressChangeListener) {
        this.mOnPlayProgessChangeListener = onPlayProgressChangeListener;
    }

    public CutVideoFragment() {
        Float valueOf = Float.valueOf(1.0f);
        this.mMinLiveWindowScale = 1.0f;
        this.mSize = new float[2];
        this.mCenterPoint = new FloatPoint();
        this.mRatio = 0;
        this.mRatioValue = -1.0f;
        this.mTransformData = new HashMap();
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_SCALE_X, valueOf);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_SCALE_Y, valueOf);
        Map<String, Float> map = this.mTransformData;
        Float valueOf2 = Float.valueOf(0.0f);
        map.put(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z, valueOf2);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_X, valueOf2);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_Y, valueOf2);
    }

    public static CutVideoFragment newInstance(long j) {
        CutVideoFragment cutVideoFragment = new CutVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_MAX_DURATION, j);
        cutVideoFragment.setArguments(bundle);
        return cutVideoFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        if (getArguments() != null) {
            this.mMaxDuration = getArguments().getLong(PARAM_MAX_DURATION);
        }
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_cut_video, viewGroup, false);
        this.mLiveWindow = (NvsLiveWindowExt) inflate.findViewById(R.id.liveWindow);
        this.mLiveWindow.setFillMode(0);
        this.mPlayerLayout = (RelativeLayout) inflate.findViewById(R.id.playerLayout);
        this.mCutView = (CutRectLayout) inflate.findViewById(R.id.cut_view);
        this.mPlayButton = inflate.findViewById(R.id.playLayout);
        this.mCurrentPlaytimeView = (TextView) inflate.findViewById(R.id.currentPlaytime);
        this.mTotalDurationView = (TextView) inflate.findViewById(R.id.totalDuration);
        this.mSeekBar = (SeekBar) inflate.findViewById(R.id.playSeekBar);
        this.mPlayButtonImage = (ImageView) inflate.findViewById(R.id.playImage);
        initListener();
        return inflate;
    }

    private void initListener() {
        this.mPlayButton.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.player.view.CutVideoFragment.AnonymousClass2 */

            public void onClick(View view) {
                if (CutVideoFragment.this.getCurrentEngineState() == 3) {
                    CutVideoFragment.this.stopEngine();
                } else if (CutVideoFragment.this.mTimeline != null) {
                    long timelineCurrentPosition = CutVideoFragment.this.mStreamingContext.getTimelineCurrentPosition(CutVideoFragment.this.mTimeline);
                    long duration = CutVideoFragment.this.getDuration() + timelineCurrentPosition;
                    CutVideoFragment.this.playVideo(timelineCurrentPosition, duration - (timelineCurrentPosition - CutVideoFragment.this.mStartTime));
                    if (CutVideoFragment.this.mOnPlayProgessChangeListener != null) {
                        CutVideoFragment.this.mOnPlayProgessChangeListener.onPlayStateChanged(true);
                    }
                }
            }
        });
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.player.view.CutVideoFragment.AnonymousClass3 */
            private long currentTime = 0;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    this.currentTime = ((CutVideoFragment.this.getDuration() * ((long) i)) / 100) + CutVideoFragment.this.mStartTime;
                    CutVideoFragment.this.seekTimeline(this.currentTime, 0);
                    CutVideoFragment.this.updateCurPlayTime(this.currentTime);
                }
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                CutVideoFragment cutVideoFragment = CutVideoFragment.this;
                long j = this.currentTime;
                cutVideoFragment.playVideo(j, cutVideoFragment.getDuration() + j);
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        OnFragmentLoadFinisedListener onFragmentLoadFinisedListener = this.mFragmentLoadFinisedListener;
        if (onFragmentLoadFinisedListener != null) {
            onFragmentLoadFinisedListener.onLoadFinished();
        }
    }

    public void initData() {
        final boolean[] zArr = {false};
        this.mCutView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /* class com.meishe.player.view.CutVideoFragment.AnonymousClass4 */

            public void onGlobalLayout() {
                if (!zArr[0]) {
                    Point originalLiveWindowLayoutParam = CutVideoFragment.this.getOriginalLiveWindowLayoutParam();
                    CutVideoFragment.this.mSize[0] = (float) originalLiveWindowLayoutParam.x;
                    CutVideoFragment.this.mSize[1] = (float) originalLiveWindowLayoutParam.y;
                    CutVideoFragment.this.setCutRectViewSize(originalLiveWindowLayoutParam);
                    CutVideoFragment.this.setLiveWindowSize(originalLiveWindowLayoutParam);
                    new Handler().post(new Runnable() {
                        /* class com.meishe.player.view.CutVideoFragment.AnonymousClass4.AnonymousClass1 */

                        public void run() {
                            CutVideoFragment.this.initLiveWindowCenterPoint();
                            Point changeCutRectViewNoScale = CutVideoFragment.this.changeCutRectViewNoScale(CutVideoFragment.this.mRatio);
                            CutVideoFragment.this.mMinLiveWindowScale = CutVideoFragment.this.getSuitLiveWindowScale(changeCutRectViewNoScale);
                            CutVideoFragment.this.mTransformData = CutVideoFragment.this.parseToViewTransData(CutVideoFragment.this.mTransformData);
                            CutVideoFragment.this.mLiveWindow.setRotation(((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z)).floatValue());
                            CutVideoFragment.this.scaleLiveWindow(((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X)).floatValue());
                            CutVideoFragment.this.mLiveWindow.setTranslationX(((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_X)).floatValue());
                            CutVideoFragment.this.mLiveWindow.setTranslationY(((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_Y)).floatValue());
                            FloatPoint floatPoint = new FloatPoint();
                            FloatPoint floatPoint2 = new FloatPoint();
                            float width = (((float) CutVideoFragment.this.mLiveWindow.getWidth()) * 1.0f) / 2.0f;
                            float height = (((float) CutVideoFragment.this.mLiveWindow.getHeight()) * 1.0f) / 2.0f;
                            floatPoint.x = CutVideoFragment.this.mCenterPoint.x - width;
                            floatPoint.y = CutVideoFragment.this.mCenterPoint.y - height;
                            floatPoint2.x = CutVideoFragment.this.mCenterPoint.x + width;
                            floatPoint2.y = CutVideoFragment.this.mCenterPoint.y + height;
                            float floatValue = ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X)).floatValue();
                            float floatValue2 = ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z)).floatValue();
                            FloatPoint transformData = CutVideoFragment.this.transformData(floatPoint, CutVideoFragment.this.mCenterPoint, floatValue, floatValue2, ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_X)).floatValue(), ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_Y)).floatValue());
                            FloatPoint transformData2 = CutVideoFragment.this.transformData(floatPoint2, CutVideoFragment.this.mCenterPoint, floatValue, floatValue2, ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_X)).floatValue(), ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_Y)).floatValue());
                            CutVideoFragment.this.mCenterPoint.x = (transformData.x + transformData2.x) / 2.0f;
                            CutVideoFragment.this.mCenterPoint.y = (transformData.y + transformData2.y) / 2.0f;
                            CutVideoFragment.this.mRegionData = CutVideoFragment.this.updateRegionData();
                        }
                    });
                    zArr[0] = true;
                }
            }
        });
        connectTimelineWithLiveWindow();
        this.mCutView.setOnTransformListener(new CutRectLayout.OnTransformListener() {
            /* class com.meishe.player.view.CutVideoFragment.AnonymousClass5 */

            @Override // com.meishe.player.view.CutRectLayout.OnTransformListener
            public void onTrans(float f, float f2) {
                if (f != 0.0f && f2 != 0.0f) {
                    float floatValue = ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_X)).floatValue();
                    float floatValue2 = ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_Y)).floatValue();
                    if (CutVideoFragment.this.canTrans(f, 0.0f, 0.0f)) {
                        floatValue -= f;
                        CutVideoFragment.this.mCenterPoint.x -= f;
                    }
                    if (CutVideoFragment.this.canTrans(0.0f, f2, 0.0f)) {
                        floatValue2 -= f2;
                        CutVideoFragment.this.mCenterPoint.y -= f2;
                    }
                    CutVideoFragment.this.mLiveWindow.setTranslationX(floatValue);
                    CutVideoFragment.this.mLiveWindow.setTranslationY(floatValue2);
                    CutVideoFragment.this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_X, Float.valueOf(floatValue));
                    CutVideoFragment.this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_Y, Float.valueOf(floatValue2));
                }
            }

            @Override // com.meishe.player.view.CutRectLayout.OnTransformListener
            public void onScaleAndRotate(float f, float f2) {
                if (f >= 1.0f || CutVideoFragment.this.canTrans(0.0f, 0.0f, -f2)) {
                    float floatValue = ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z)).floatValue() - f2;
                    if (floatValue > 45.0f && f2 < 0.0f) {
                        return;
                    }
                    if (floatValue >= -45.0f || f2 <= 0.0f) {
                        float f3 = (float) ((int) floatValue);
                        CutVideoFragment.this.mLiveWindow.setRotation(f3);
                        CutVideoFragment.this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z, Float.valueOf(f3));
                        double computeScale = CutVideoFragment.this.computeScale(f3, f);
                        double floatValue2 = (double) (((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X)).floatValue() * f);
                        Logger.d(CutVideoFragment.TAG, "onScaleAndRotate: newScale = " + floatValue2 + ",scaleValue =  " + computeScale + ", scale = " + f);
                        if (floatValue2 < computeScale && computeScale > 1.0d) {
                            floatValue2 = (double) ((float) computeScale);
                        }
                        if (computeScale == 1.0d || f >= 1.0f) {
                            Logger.d(CutVideoFragment.TAG, "onScaleAndRotate: scaleResult  = " + floatValue2);
                            if (floatValue2 < ((double) CutVideoFragment.this.mMinLiveWindowScale)) {
                                floatValue2 = (double) CutVideoFragment.this.mMinLiveWindowScale;
                            }
                            float f4 = (float) floatValue2;
                            CutVideoFragment.this.scaleLiveWindow(f4);
                            CutVideoFragment.this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_SCALE_X, Float.valueOf(f4));
                            CutVideoFragment.this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_SCALE_Y, Float.valueOf(f4));
                            if (CutVideoFragment.this.mOnCutRectChangedListener != null) {
                                CutVideoFragment.this.mOnCutRectChangedListener.onScaleAndRotate(f4, f3);
                            }
                        }
                    }
                }
            }

            @Override // com.meishe.player.view.CutRectLayout.OnTransformListener
            public void onTransEnd(float f, float[] fArr) {
                if (f < 0.0f) {
                    CutVideoFragment cutVideoFragment = CutVideoFragment.this;
                    cutVideoFragment.rotateVideo(((Float) cutVideoFragment.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z)).floatValue());
                } else {
                    float floatValue = ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X)).floatValue() * f;
                    CutVideoFragment.this.scaleLiveWindow(floatValue);
                    CutVideoFragment.this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_SCALE_X, Float.valueOf(floatValue));
                    CutVideoFragment.this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_SCALE_Y, Float.valueOf(floatValue));
                }
                if (fArr != null) {
                    CutVideoFragment.this.mSize[0] = fArr[0];
                    CutVideoFragment.this.mSize[1] = fArr[1];
                }
            }

            @Override // com.meishe.player.view.CutRectLayout.OnTransformListener
            public void onRectMoved(float f, Point point, Point point2) {
                if (CutVideoFragment.this.mOriginalSize == null) {
                    CutVideoFragment.this.mOriginalSize = new Point();
                }
                CutVideoFragment.this.mOriginalSize.x = CutVideoFragment.this.mCutView.getRectWidth();
                CutVideoFragment.this.mOriginalSize.y = CutVideoFragment.this.mCutView.getRectHeight();
                CutVideoFragment cutVideoFragment = CutVideoFragment.this;
                cutVideoFragment.mMinLiveWindowScale = cutVideoFragment.getSuitLiveWindowScale(cutVideoFragment.mOriginalSize);
                float floatValue = ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X)).floatValue() * f;
                CutVideoFragment cutVideoFragment2 = CutVideoFragment.this;
                double computeScale = cutVideoFragment2.computeScale(((Float) cutVideoFragment2.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z)).floatValue(), 1.0f);
                if (((double) floatValue) < computeScale) {
                    floatValue = (float) computeScale;
                    f = floatValue / ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X)).floatValue();
                }
                CutVideoFragment.this.scaleLiveWindow(floatValue);
                FloatPoint floatPoint = new FloatPoint();
                floatPoint.x = (float) point2.x;
                floatPoint.y = (float) point2.y;
                CutVideoFragment cutVideoFragment3 = CutVideoFragment.this;
                cutVideoFragment3.transformData(floatPoint, cutVideoFragment3.mCenterPoint, f, 0.0f);
                float f2 = (((float) point2.y) - floatPoint.y) + ((float) point.y);
                CutVideoFragment.this.translateLiveWindow((((float) point2.x) - floatPoint.x) + ((float) point.x), f2);
                CutVideoFragment cutVideoFragment4 = CutVideoFragment.this;
                cutVideoFragment4.mRegionData = cutVideoFragment4.updateRegionData();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateCurPlayTime(long j) {
        this.mCurrentPlaytimeView.setText(TimeFormatUtil.formatUsToString2(j - this.mStartTime));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updatePlayProgress(long j) {
        this.mSeekBar.setProgress((int) ((((float) (j - this.mStartTime)) / ((float) getDuration())) * 100.0f));
        updateCurPlayTime(j);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void translateLiveWindow(float f, float f2) {
        float floatValue = this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_X).floatValue() + f;
        float floatValue2 = this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_Y).floatValue() + f2;
        this.mLiveWindow.setTranslationX(floatValue);
        this.mLiveWindow.setTranslationY(floatValue2);
        this.mCenterPoint.x += f;
        this.mCenterPoint.y += f2;
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_X, Float.valueOf(floatValue));
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_Y, Float.valueOf(floatValue2));
    }

    private Point getFreeCutRectSize(float[] fArr) {
        float f = fArr[0] / fArr[1];
        int width = this.mCutView.getWidth();
        int height = this.mCutView.getHeight();
        float f2 = ((float) width) * 1.0f;
        float f3 = (float) height;
        Point point = new Point();
        if (f > f2 / f3) {
            point.x = width;
            point.y = (int) (f2 / f);
        } else {
            point.y = height;
            point.x = (int) (f3 * f);
        }
        return point;
    }

    public void setCutData(CutData cutData) {
        if (cutData != null) {
            this.mRatio = cutData.getRatio();
            this.mRatioValue = cutData.getRatioValue();
            Map<String, Float> transformData = cutData.getTransformData();
            for (String str : transformData.keySet()) {
                Float f = transformData.get(str);
                if (f != null) {
                    this.mTransformData.put(str, f);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean canTrans(float f, float f2, float f3) {
        FloatPoint floatPoint = new FloatPoint();
        FloatPoint floatPoint2 = new FloatPoint();
        FloatPoint floatPoint3 = new FloatPoint();
        FloatPoint floatPoint4 = new FloatPoint();
        float width = (((float) this.mLiveWindow.getWidth()) * 1.0f) / 2.0f;
        float height = (((float) this.mLiveWindow.getHeight()) * 1.0f) / 2.0f;
        FloatPoint floatPoint5 = new FloatPoint();
        floatPoint5.x = this.mCenterPoint.x - f;
        floatPoint5.y = this.mCenterPoint.y - f2;
        floatPoint.x = floatPoint5.x - width;
        floatPoint.y = floatPoint5.y - height;
        floatPoint2.x = floatPoint5.x - width;
        floatPoint2.y = floatPoint5.y + height;
        floatPoint3.x = floatPoint5.x + width;
        floatPoint3.y = floatPoint5.y - height;
        floatPoint4.x = floatPoint5.x + width;
        floatPoint4.y = floatPoint5.y + height;
        float floatValue = this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z).floatValue() + f3;
        float floatValue2 = this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X).floatValue();
        FloatPoint transformData = transformData(floatPoint, floatPoint5, floatValue2, floatValue);
        FloatPoint transformData2 = transformData(floatPoint2, floatPoint5, floatValue2, floatValue);
        FloatPoint transformData3 = transformData(floatPoint3, floatPoint5, floatValue2, floatValue);
        FloatPoint transformData4 = transformData(floatPoint4, floatPoint5, floatValue2, floatValue);
        int[] iArr = new int[2];
        this.mCutView.getLocationOnScreen(iArr);
        int drawRectViewLeft = iArr[0] + this.mCutView.getDrawRectViewLeft();
        int drawRectViewTop = iArr[1] + this.mCutView.getDrawRectViewTop();
        int rectWidth = drawRectViewLeft + this.mCutView.getRectWidth();
        FloatPoint floatPoint6 = new FloatPoint();
        float f4 = (float) drawRectViewLeft;
        floatPoint6.x = f4;
        float f5 = (float) drawRectViewTop;
        floatPoint6.y = f5;
        boolean isInRect = isInRect(transformData, transformData3, transformData4, transformData2, floatPoint6);
        FloatPoint floatPoint7 = new FloatPoint();
        float f6 = (float) rectWidth;
        floatPoint7.x = f6;
        floatPoint7.y = f5;
        boolean isInRect2 = isInRect(transformData, transformData3, transformData4, transformData2, floatPoint7);
        FloatPoint floatPoint8 = new FloatPoint();
        floatPoint8.x = f6;
        float rectHeight = (float) (drawRectViewTop + this.mCutView.getRectHeight());
        floatPoint8.y = rectHeight;
        boolean isInRect3 = isInRect(transformData, transformData3, transformData4, transformData2, floatPoint8);
        FloatPoint floatPoint9 = new FloatPoint();
        floatPoint9.x = f4;
        floatPoint9.y = rectHeight;
        boolean isInRect4 = isInRect(transformData, transformData3, transformData4, transformData2, floatPoint9);
        if (!isInRect || !isInRect4 || !isInRect2 || !isInRect3) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0152  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x018d  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x01a8  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x01f9  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0230  */
    /* JADX WARNING: Removed duplicated region for block: B:61:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public double computeScale(float r24, float r25) {
        /*
        // Method dump skipped, instructions count: 563
        */

//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.player.view.CutVideoFragment.computeScale(float, float):double");
        return 0;
    }

    public boolean isInRect(FloatPoint floatPoint, FloatPoint floatPoint2, FloatPoint floatPoint3, FloatPoint floatPoint4, FloatPoint floatPoint5) {
        return getCross(floatPoint, floatPoint2, floatPoint5) * getCross(floatPoint3, floatPoint4, floatPoint5) >= 0.0f && getCross(floatPoint2, floatPoint3, floatPoint5) * getCross(floatPoint4, floatPoint, floatPoint5) >= 0.0f;
    }

    private float getCross(FloatPoint floatPoint, FloatPoint floatPoint2, FloatPoint floatPoint3) {
        return ((floatPoint2.x - floatPoint.x) * (floatPoint3.y - floatPoint.y)) - ((floatPoint3.x - floatPoint.x) * (floatPoint2.y - floatPoint.y));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private FloatPoint transformData(FloatPoint floatPoint, FloatPoint floatPoint2, float f, float f2) {
        float[] fArr = {floatPoint.x, floatPoint.y};
        Matrix matrix = new Matrix();
        matrix.setRotate(f2, floatPoint2.x, floatPoint2.y);
        matrix.mapPoints(fArr);
        matrix.setScale(f, f, floatPoint2.x, floatPoint2.y);
        matrix.mapPoints(fArr);
        floatPoint.x = (float) Math.round(fArr[0]);
        floatPoint.y = (float) Math.round(fArr[1]);
        return floatPoint;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private FloatPoint transformData(FloatPoint floatPoint, FloatPoint floatPoint2, float f, float f2, float f3, float f4) {
        float[] fArr = {floatPoint.x, floatPoint.y};
        Matrix matrix = new Matrix();
        matrix.setRotate(f2, floatPoint2.x, floatPoint2.y);
        matrix.mapPoints(fArr);
        matrix.setScale(f, f, floatPoint2.x, floatPoint2.y);
        matrix.mapPoints(fArr);
        matrix.setTranslate(f3, f4);
        matrix.mapPoints(fArr);
        floatPoint.x = fArr[0];
        floatPoint.y = fArr[1];
        return floatPoint;
    }

    private double pointToLine(float f, float f2, float f3, float f4, float f5, float f6) {
        float lineSpace = lineSpace(f, f2, f3, f4);
        float lineSpace2 = lineSpace(f, f2, f5, f6);
        float lineSpace3 = lineSpace(f3, f4, f5, f6);
        double d = (double) lineSpace3;
        if (d <= 1.0E-6d) {
            return 0.0d;
        }
        double d2 = (double) lineSpace2;
        if (d2 <= 1.0E-6d) {
            return 0.0d;
        }
        double d3 = (double) lineSpace;
        if (d3 <= 1.0E-6d) {
            return d2;
        }
        double d4 = (double) (((lineSpace2 + lineSpace) + lineSpace3) / 2.0f);
        Double.isNaN(d4);
        Double.isNaN(d3);
        Double.isNaN(d4);
        Double.isNaN(d4);
        Double.isNaN(d2);
        Double.isNaN(d4);
        Double.isNaN(d);
        double sqrt = Math.sqrt((d4 - d3) * d4 * (d4 - d2) * (d4 - d));
        if (lineSpace <= 0.0f) {
            return 0.0d;
        }
        Double.isNaN(d3);
        return (sqrt * 2.0d) / d3;
    }

    private float lineSpace(float f, float f2, float f3, float f4) {
        float f5 = f - f3;
        float f6 = f2 - f4;
        return (float) Math.sqrt((double) ((f5 * f5) + (f6 * f6)));
    }

    private double getPointToLine(FloatPoint floatPoint, FloatPoint floatPoint2, FloatPoint floatPoint3) {
        return pointToLine(floatPoint2.x, floatPoint2.y, floatPoint3.x, floatPoint3.y, floatPoint.x, floatPoint.y);
    }

    private float getPointToLineX(FloatPoint floatPoint, FloatPoint floatPoint2, FloatPoint floatPoint3) {
        if (((double) (floatPoint2.y - floatPoint3.y)) <= 1.0E-6d) {
            return 0.0f;
        }
        return Math.abs(((floatPoint2.x - floatPoint3.x) / (floatPoint2.y - floatPoint3.y)) * (floatPoint2.y - floatPoint.y));
    }

    private double getPointToLineY(FloatPoint floatPoint, FloatPoint floatPoint2, FloatPoint floatPoint3) {
        if (((double) (floatPoint2.x - floatPoint3.x)) <= 1.0E-6d) {
            return 0.0d;
        }
        return (double) Math.abs(((floatPoint2.x - floatPoint.x) * (floatPoint2.y - floatPoint3.y)) / (floatPoint2.x - floatPoint3.x));
    }

    public void setStartTime(long j) {
        this.mStartTime = j;
    }

    public long getDuration() {
        long j = this.mMaxDuration;
        if (j > 0) {
            return j;
        }
        NvsTimeline nvsTimeline = this.mTimeline;
        if (nvsTimeline == null) {
            return 0;
        }
        return nvsTimeline.getDuration();
    }

    private void setLiveWindowRatio(int i) {
        Point point;
        if (this.mTimeline != null) {
            if (i == 0) {
                point = getOriginalLiveWindowLayoutParam();
            } else {
                point = getLiveWindowSizeByRatio(i);
            }
            setLiveWindowSize(point);
            setCutRectViewSize(point);
            if (i == 0) {
                this.mCutView.setWidthHeightRatio(-1.0f);
            } else {
                this.mCutView.setWidthHeightRatio((((float) point.x) * 1.0f) / ((float) point.y));
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setLiveWindowSize(Point point) {
        ViewGroup.LayoutParams layoutParams = this.mLiveWindow.getLayoutParams();
        layoutParams.width = point.x;
        layoutParams.height = point.y;
        this.mLiveWindow.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setCutRectViewSize(Point point) {
        this.mCutView.setDrawRectSize(point.x, point.y);
    }

    private void connectTimelineWithLiveWindow() {
        NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
        if (nvsStreamingContext != null && this.mTimeline != null && this.mLiveWindow != null) {
            nvsStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
                /* class com.meishe.player.view.CutVideoFragment.AnonymousClass6 */

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
                public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {
                }

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
                public void onPlaybackStopped(NvsTimeline nvsTimeline) {
                    if (CutVideoFragment.this.mVideoFragmentCallBack != null) {
                        CutVideoFragment.this.mVideoFragmentCallBack.playStopped(nvsTimeline);
                    }
                }

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
                public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                    CutVideoFragment.this.mHandler.sendEmptyMessage(100);
                    if (CutVideoFragment.this.mVideoFragmentCallBack != null) {
                        CutVideoFragment.this.mVideoFragmentCallBack.playBackEOF(nvsTimeline);
                    }
                }
            });
            this.mStreamingContext.setPlaybackCallback2(new NvsStreamingContext.PlaybackCallback2() {
                /* class com.meishe.player.view.CutVideoFragment.AnonymousClass7 */

                @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback2
                public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long j) {
                    CutVideoFragment.this.updatePlayProgress(j);
                }
            });
            this.mStreamingContext.setSeekingCallback(new NvsStreamingContext.SeekingCallback() {
                /* class com.meishe.player.view.CutVideoFragment.AnonymousClass8 */

                @Override // com.meicam.sdk.NvsStreamingContext.SeekingCallback
                public void onSeekingTimelinePosition(NvsTimeline nvsTimeline, long j) {
                    CutVideoFragment.this.updatePlayProgress(j);
                }
            });
            this.mStreamingContext.setStreamingEngineCallback(new NvsStreamingContext.StreamingEngineCallback() {
                /* class com.meishe.player.view.CutVideoFragment.AnonymousClass9 */

                @Override // com.meicam.sdk.NvsStreamingContext.StreamingEngineCallback
                public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {
                }

                @Override // com.meicam.sdk.NvsStreamingContext.StreamingEngineCallback
                public void onStreamingEngineStateChanged(int i) {
                    if (i == 3) {
                        CutVideoFragment.this.mPlayButtonImage.setBackgroundResource(R.mipmap.icon_pause);
                    } else {
                        CutVideoFragment.this.mPlayButtonImage.setBackgroundResource(R.mipmap.icon_play);
                    }
                }
            });
            this.mStreamingContext.connectTimelineWithLiveWindowExt(this.mTimeline, this.mLiveWindow);
            this.mCutView.setDrawRectSize(this.mLiveWindow.getWidth(), this.mLiveWindow.getHeight());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Point getOriginalLiveWindowLayoutParam() {
        NvsVideoResolution videoRes = this.mTimeline.getVideoRes();
        Point point = new Point();
        int width = this.mPlayerLayout.getWidth();
        int height = this.mPlayerLayout.getHeight();
        float f = ((float) width) * 1.0f;
        float f2 = (float) height;
        float f3 = (float) videoRes.imageWidth;
        float f4 = (float) videoRes.imageHeight;
        if ((f3 * 1.0f) / f4 > f / f2) {
            point.x = width;
            point.y = (int) ((f / f3) * f4);
        } else {
            point.y = height;
            point.x = (int) (((f2 * 1.0f) / f4) * f3);
        }
        return point;
    }

    private Point getLiveWindowSizeByRatio(int i) {
        int width = this.mPlayerLayout.getWidth();
        int height = this.mPlayerLayout.getHeight();
        Point point = new Point();
        if (i == 1) {
            point.x = width;
            double d = (double) width;
            Double.isNaN(d);
            point.y = (int) ((d * 9.0d) / 16.0d);
        } else if (i == 2) {
            point.x = width;
            point.y = width;
            if (height < width) {
                point.x = height;
                point.y = height;
            }
        } else if (i == 4) {
            double d2 = (double) height;
            Double.isNaN(d2);
            point.x = (int) ((d2 * 9.0d) / 16.0d);
            point.y = height;
        } else if (i == 8) {
            point.x = width;
            double d3 = (double) width;
            Double.isNaN(d3);
            point.y = (int) ((d3 * 3.0d) / 4.0d);
        } else if (i != 16) {
            point.x = width;
            double d4 = (double) width;
            Double.isNaN(d4);
            point.y = (int) ((d4 * 9.0d) / 16.0d);
        } else {
            point.x = width;
            double d5 = (double) width;
            Double.isNaN(d5);
            point.y = (int) ((d5 * 4.0d) / 3.0d);
        }
        return point;
    }

    private Point getLiveWindowSizeByRatio(float f) {
        int width = this.mPlayerLayout.getWidth();
        int height = this.mPlayerLayout.getHeight();
        Point point = new Point();
        if (f >= 1.0f) {
            point.x = width;
            point.y = (int) (((float) width) / f);
        } else {
            point.x = (int) (((float) height) * f);
            point.y = height;
        }
        return point;
    }

    public void rotateVideo(float f) {
        this.mLiveWindow.setRotation(f * 1.0f);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z, Float.valueOf(f));
        float computeScale = (float) computeScale(f, 1.0f);
        if (computeScale > this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X).floatValue()) {
            scaleLiveWindow(computeScale);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void scaleLiveWindow(float f) {
        this.mLiveWindow.setScaleX(f);
        this.mLiveWindow.setScaleY(f);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_SCALE_X, Float.valueOf(f));
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_SCALE_Y, Float.valueOf(f));
    }

    public void reset() {
        NvsLiveWindowExt nvsLiveWindowExt = this.mLiveWindow;
        Float valueOf = Float.valueOf(0.0f);
        nvsLiveWindowExt.setTranslationX(0.0f);
        this.mLiveWindow.setTranslationY(0.0f);
        this.mLiveWindow.setRotation(0.0f);
        scaleLiveWindow(1.0f);
        setLiveWindowRatio(0);
        this.mOriginalSize = null;
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z, valueOf);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_X, valueOf);
        this.mTransformData.put(StoryboardUtil.STORYBOARD_KEY_TRANS_Y, valueOf);
        initLiveWindowCenterPoint();
        this.mRatio = 0;
        this.mMinLiveWindowScale = 1.0f;
        this.mRegionData = updateRegionData();
    }

    public void changeCutRectView(int i) {
        Point point;
        if (i == 0) {
            point = this.mOriginalSize;
            if (point == null) {
                point = getOriginalLiveWindowLayoutParam();
                this.mOriginalSize = point;
            }
        } else {
            point = getLiveWindowSizeByRatio(i);
        }
        setCutRectViewSize(point);
        if (i == 0) {
            this.mCutView.setWidthHeightRatio(-1.0f);
        } else {
            this.mCutView.setWidthHeightRatio((((float) point.x) * 1.0f) / ((float) point.y));
        }
        this.mMinLiveWindowScale = getSuitLiveWindowScale(point);
        this.mHandler.post(new Runnable() {
            /* class com.meishe.player.view.CutVideoFragment.AnonymousClass10 */

            public void run() {
                CutVideoFragment cutVideoFragment = CutVideoFragment.this;
                cutVideoFragment.mRegionData = cutVideoFragment.updateRegionData();
                float floatValue = ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z)).floatValue();
                if (floatValue == 0.0f) {
                    if (CutVideoFragment.this.mMinLiveWindowScale > ((Float) CutVideoFragment.this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X)).floatValue()) {
                        CutVideoFragment cutVideoFragment2 = CutVideoFragment.this;
                        cutVideoFragment2.scaleLiveWindow(cutVideoFragment2.mMinLiveWindowScale);
                        return;
                    }
                    CutVideoFragment.this.rotateVideo(floatValue);
                    return;
                }
                CutVideoFragment.this.rotateVideo(floatValue);
            }
        });
        this.mRatio = i;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private RectF updateRegionData() {
        return getRectEx(this.mCutView.getRectWidth(), this.mCutView.getRectHeight(), this.mLiveWindow.getWidth(), this.mLiveWindow.getHeight());
    }

    public Point changeCutRectViewNoScale(int i) {
        Point point;
        float f = this.mRatioValue;
        if (f > 0.0f) {
            point = getLiveWindowSizeByRatio(f);
        } else if (i == 0) {
            point = getOriginalLiveWindowLayoutParam();
        } else {
            point = getLiveWindowSizeByRatio(i);
        }
        setCutRectViewSize(point);
        if (i == 0) {
            this.mCutView.setWidthHeightRatio(-1.0f);
        } else {
            this.mCutView.setWidthHeightRatio((((float) point.x) * 1.0f) / ((float) point.y));
        }
        this.mRatio = i;
        return point;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initLiveWindowCenterPoint() {
        int[] iArr = new int[2];
        this.mLiveWindow.getLocationOnScreen(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        this.mCenterPoint.x = ((float) i) + ((((float) this.mLiveWindow.getWidth()) * 1.0f) / 2.0f);
        this.mCenterPoint.y = ((float) i2) + ((((float) this.mLiveWindow.getHeight()) * 1.0f) / 2.0f);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private float getSuitLiveWindowScale(Point point) {
        int width = this.mLiveWindow.getWidth();
        int height = this.mLiveWindow.getHeight();
        float f = (((float) point.x) * 1.0f) / ((float) width);
        float f2 = (((float) point.y) * 1.0f) / ((float) height);
        if (f >= f2) {
            f2 = f;
        }
        if (f2 < 1.0f) {
            return -1.0f;
        }
        return f2;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
    }

    public void setTimeLine(NvsTimeline nvsTimeline) {
        this.mTimeline = nvsTimeline;
    }

    public void playVideoButtonClick() {
        if (this.mTimeline != null) {
            playVideoButtonClick(0, getDuration());
        }
    }

    public void playVideoButtonClick(long j, long j2) {
        playVideo(j, j2);
    }

    public void playVideo(long j, long j2) {
        this.mStreamingContext.playbackTimeline(this.mTimeline, j, j2, 1, true, 0);
    }

    public void playVideoFromStartPosition() {
        NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
        NvsTimeline nvsTimeline = this.mTimeline;
        long j = this.mStartTime;
        nvsStreamingContext.playbackTimeline(nvsTimeline, j, getDuration() + j, 1, true, 0);
    }

    public void seekTimeline(long j, int i) {
        this.mStreamingContext.seekTimeline(this.mTimeline, j, 1, i);
    }

    public int getCurrentEngineState() {
        return this.mStreamingContext.getStreamingEngineState();
    }

    public void stopEngine() {
        NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
        if (nvsStreamingContext != null) {
            nvsStreamingContext.stop();
        }
    }

    public NvsTimeline getTimeLine() {
        return this.mTimeline;
    }

    public float[] getRegionData(float[] fArr) {
        if (this.mRegionData == null) {
            return null;
        }
        RectF rectF = new RectF();
        rectF.top = this.mRegionData.top * fArr[1];
        rectF.bottom = this.mRegionData.bottom * fArr[1];
        rectF.left = this.mRegionData.left * fArr[0];
        rectF.right = this.mRegionData.right * fArr[0];
        if (this.mRatio == 0) {
            return new float[]{rectF.left, rectF.top, rectF.right, rectF.top, rectF.right, rectF.bottom, rectF.left, rectF.bottom, rectF.left, 0.0f};
        }
        return new float[]{rectF.left, rectF.top, rectF.right, rectF.top, rectF.right, rectF.bottom, rectF.left, rectF.bottom};
    }

    private RectF getRect(int i, int i2, int i3, int i4, float[] fArr) {
        if (fArr == null) {
            fArr = new float[]{1.0f, 1.0f};
        }
        RectF rectF = new RectF();
        float f = (float) i3;
        float f2 = (float) i4;
        float f3 = (float) i;
        float f4 = f3 * 1.0f;
        float f5 = (float) i2;
        if ((f * 1.0f) / f2 > f4 / f5) {
            rectF.top = ((f2 * (f4 / f)) / f5) * fArr[1];
            rectF.bottom = -rectF.top;
            rectF.right = fArr[0] * 1.0f;
            rectF.left = -rectF.right;
        } else {
            rectF.top = fArr[1] * 1.0f;
            rectF.bottom = -rectF.top;
            rectF.right = ((f * ((f5 * 1.0f) / f2)) / f3) * fArr[0];
            rectF.left = -rectF.right;
        }
        return rectF;
    }

    private RectF getRectEx(int i, int i2, int i3, int i4) {
        float f = this.mMinLiveWindowScale;
        RectF rectF = new RectF();
        float f2 = (float) ((int) (((float) i3) * f));
        float f3 = (float) ((int) (((float) i4) * f));
        float f4 = ((float) i) * 1.0f;
        float f5 = (float) i2;
        if (f4 / f5 > (f2 * 1.0f) / f3) {
            float f6 = f4 / f2;
            rectF.right = f6;
            rectF.left = -rectF.right;
            rectF.top = (f5 * 1.0f) / (f3 * f6);
            rectF.bottom = -rectF.top;
        } else {
            float f7 = (f5 * 1.0f) / f3;
            rectF.top = f7;
            rectF.bottom = -rectF.top;
            rectF.right = f4 / (f2 * f7);
            rectF.left = -rectF.right;
        }
        return rectF;
    }

    public Map<String, Float> getTransFromData(int i, int i2) {
        return parseToTimelineTransData(i, i2);
    }

    public int[] getSize() {
        return new int[]{(int) (((float) this.mLiveWindow.getWidth()) * this.mMinLiveWindowScale), (int) (((float) this.mLiveWindow.getHeight()) * this.mMinLiveWindowScale)};
    }

    private Map<String, Float> parseToTimelineTransData(int i, int i2) {
        HashMap hashMap = new HashMap();
        float floatValue = this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X).floatValue() / this.mMinLiveWindowScale;
        float floatValue2 = this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_X).floatValue();
        float floatValue3 = this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_TRANS_Y).floatValue();
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_SCALE_X, Float.valueOf(floatValue));
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_SCALE_Y, Float.valueOf(floatValue));
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_TRANS_X, Float.valueOf(floatValue2));
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_TRANS_Y, Float.valueOf(floatValue3));
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z, this.mTransformData.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z));
        return hashMap;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Map<String, Float> parseToViewTransData(Map<String, Float> map) {
        HashMap hashMap = new HashMap();
        float floatValue = map.get(StoryboardUtil.STORYBOARD_KEY_SCALE_X).floatValue() * this.mMinLiveWindowScale;
        float floatValue2 = map.get(StoryboardUtil.STORYBOARD_KEY_TRANS_X).floatValue();
        float floatValue3 = map.get(StoryboardUtil.STORYBOARD_KEY_TRANS_Y).floatValue();
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_SCALE_X, Float.valueOf(floatValue));
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_SCALE_Y, Float.valueOf(floatValue));
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_TRANS_X, Float.valueOf(floatValue2));
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_TRANS_Y, Float.valueOf(floatValue3));
        hashMap.put(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z, map.get(StoryboardUtil.STORYBOARD_KEY_ROTATION_Z));
        return hashMap;
    }

    public void setFragmentLoadFinisedListener(OnFragmentLoadFinisedListener onFragmentLoadFinisedListener) {
        this.mFragmentLoadFinisedListener = onFragmentLoadFinisedListener;
    }

    public void setVideoFragmentCallBack(VideoFragmentListener videoFragmentListener) {
        this.mVideoFragmentCallBack = videoFragmentListener;
    }

    /* access modifiers changed from: package-private */
    public class FloatPoint {
        public float x;
        public float y;

        FloatPoint() {
        }

        public String toString() {
            return "FloatPoint{x=" + this.x + ", y=" + this.y + '}';
        }
    }
}
