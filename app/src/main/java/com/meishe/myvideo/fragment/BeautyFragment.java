package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.MeicamVideoTrack;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.util.engine.EditorEngine;
import com.meishe.myvideo.util.ui.ToastUtil;
import com.meishe.myvideoapp.R;
import java.util.List;
import java.util.Map;

public class BeautyFragment extends BaseFragment {
    private static final String TAG = "BeautyFragment";
    private static final int TYPE_BUFFING = 1;
    private static final int TYPE_RUDDY = 3;
    private static final int TYPE_WHITENING = 2;
    private NvsVideoFx mBeautyFx = null;
    private ImageView mIvApplyAll;
    private ImageView mIvBuffing;
    private ImageView mIvRuddy;
    private ImageView mIvWhitening;
    private MeicamVideoClip mMeicamCipInfo;
    private RelativeLayout mRlBuffing;
    private RelativeLayout mRlRuddy;
    private RelativeLayout mRlWhitening;
    private SeekBar mSeekBar;
    private TextView mTvApplyAll;
    private TextView mTvBuffing;
    private TextView mTvEndText;
    private TextView mTvReset;
    private TextView mTvRuddy;
    private TextView mTvWhitening;
    private int mType;
    private NvsVideoClip mVideoClip;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.beauty_list_fragment;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        MeicamVideoClip meicamVideoClip;
        this.mIvApplyAll = (ImageView) this.mRootView.findViewById(R.id.iv_apply_all);
        this.mTvApplyAll = (TextView) this.mRootView.findViewById(R.id.tv_apply_all);
        this.mTvEndText = (TextView) this.mRootView.findViewById(R.id.tv_end_text);
        this.mSeekBar = (SeekBar) this.mRootView.findViewById(R.id.seek_bar);
        this.mRlBuffing = (RelativeLayout) this.mRootView.findViewById(R.id.rl_buffing);
        this.mIvBuffing = (ImageView) this.mRootView.findViewById(R.id.iv_buffing);
        this.mTvBuffing = (TextView) this.mRootView.findViewById(R.id.tv_buffing);
        this.mRlWhitening = (RelativeLayout) this.mRootView.findViewById(R.id.rl_whitening);
        this.mIvWhitening = (ImageView) this.mRootView.findViewById(R.id.iv_whitening);
        this.mTvWhitening = (TextView) this.mRootView.findViewById(R.id.tv_whitening);
        this.mRlRuddy = (RelativeLayout) this.mRootView.findViewById(R.id.rl_ruddy);
        this.mIvRuddy = (ImageView) this.mRootView.findViewById(R.id.iv_ruddy);
        this.mTvRuddy = (TextView) this.mRootView.findViewById(R.id.tv_ruddy);
        this.mTvReset = (TextView) this.mRootView.findViewById(R.id.tv_reset);
        this.mTvReset.setSelected(true);
        Bundle arguments = getArguments();
        if (arguments != null && (meicamVideoClip = (MeicamVideoClip) arguments.getSerializable(BeautyShapeFragment.MRICAMVIDEOCLIP)) != null) {
            initVideoClip(meicamVideoClip);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
        this.mSeekBar.setMax(100);
        selectBuffing();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mIvApplyAll.setOnClickListener(this);
        this.mTvApplyAll.setOnClickListener(this);
        this.mRlBuffing.setOnClickListener(this);
        this.mRlWhitening.setOnClickListener(this);
        this.mRlRuddy.setOnClickListener(this);
        this.mTvReset.setOnClickListener(this);
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.fragment.BeautyFragment.AnonymousClass1 */

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = BeautyFragment.this.mTvEndText;
                textView.setText((((float) Math.round((((float) seekBar.getProgress()) / 100.0f) * 10.0f)) / 10.0f) + "");
                int i2 = BeautyFragment.this.mType;
                if (i2 == 1) {
                    BeautyFragment.this.mBeautyFx.setFloatVal(CommonData.VIDEO_FX_BEAUTY_STRENGTH, (double) (((float) seekBar.getProgress()) / 100.0f));
                    BeautyFragment.this.mMeicamCipInfo.getFaceEffectParameter().put(CommonData.VIDEO_FX_BEAUTY_STRENGTH, Float.valueOf(((float) seekBar.getProgress()) / 100.0f));
                } else if (i2 == 2) {
                    BeautyFragment.this.mBeautyFx.setFloatVal(CommonData.VIDEO_FX_BEAUTY_WHITENING, (double) (((float) seekBar.getProgress()) / 100.0f));
                    BeautyFragment.this.mMeicamCipInfo.getFaceEffectParameter().put(CommonData.VIDEO_FX_BEAUTY_WHITENING, Float.valueOf(((float) seekBar.getProgress()) / 100.0f));
                } else if (i2 == 3) {
                    BeautyFragment.this.mBeautyFx.setFloatVal(CommonData.VIDEO_FX_BEAUTY_REDDENING, (double) (((float) seekBar.getProgress()) / 100.0f));
                    BeautyFragment.this.mMeicamCipInfo.getFaceEffectParameter().put(CommonData.VIDEO_FX_BEAUTY_REDDENING, Float.valueOf(((float) seekBar.getProgress()) / 100.0f));
                }
                BeautyFragment.this.setResetStatus();
                EditorEngine.getInstance().seekTimeline();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setResetStatus() {
        Map<String, Float> faceEffectParameter = this.mMeicamCipInfo.getFaceEffectParameter();
        if (!(faceEffectParameter == null || faceEffectParameter.size() == 0)) {
            for (Map.Entry<String, Float> entry : faceEffectParameter.entrySet()) {
                String key = entry.getKey();
                float floatValue = entry.getValue().floatValue();
                if (key.equals(CommonData.VIDEO_FX_BEAUTY_STRENGTH) || key.equals(CommonData.VIDEO_FX_BEAUTY_WHITENING) || key.equals(CommonData.VIDEO_FX_BEAUTY_REDDENING)) {
                    if (key.equals(CommonData.VIDEO_FX_BEAUTY_STRENGTH)) {
                        if (floatValue != 0.5f) {
                            this.mTvReset.setSelected(false);
                            return;
                        }
                    } else if (floatValue != 0.0f) {
                        this.mTvReset.setSelected(false);
                        return;
                    }
                }
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_apply_all) { /*{ENCODED_INT: 2131296511}*/
            applyAllClip();
            return;
        } else if (id == R.id.rl_buffing) { /*{ENCODED_INT: 2131296684}*/
            selectBuffing();
            return;
        } else if (id == R.id.rl_ruddy) { /*{ENCODED_INT: 2131296698}*/
            selectRuddy();
            return;
        } else if (id == R.id.rl_whitening) { /*{ENCODED_INT: 2131296701}*/
            selectWhitening();
            return;
        } else if (id == R.id.tv_apply_all) { /*{ENCODED_INT: 2131296823}*/
            applyAllClip();
            return;
        } else if (id == R.id.tv_reset) { /*{ENCODED_INT: 2131296859}*/
            arSceneReset();
            return;
        }
        return;
    }

    private void applyAllClip() {
        MeicamVideoClip meicamVideoClip = this.mMeicamCipInfo;
        if (meicamVideoClip != null) {
            Map<String, Float> faceEffectParameter = meicamVideoClip.getFaceEffectParameter();
            List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
            if (!CollectionUtils.isEmpty(meicamVideoTrackList)) {
                for (int i = 0; i < meicamVideoTrackList.size(); i++) {
                    List<ClipInfo> clipInfoList = meicamVideoTrackList.get(i).getClipInfoList();
                    if (!CollectionUtils.isEmpty(clipInfoList)) {
                        for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                            ClipInfo clipInfo = clipInfoList.get(i2);
                            if ((clipInfo instanceof MeicamVideoClip) && clipInfo != this.mMeicamCipInfo) {
                                MeicamVideoClip meicamVideoClip2 = (MeicamVideoClip) clipInfo;
                                meicamVideoClip2.setFaceBeautyEffectParameter(faceEffectParameter);
                                meicamVideoClip2.applyBeautyFaceEffect();
                            }
                        }
                    }
                }
            }
            ToastUtil.showCenterToast(getContext(), (int) R.string.has_been_apply_to_all);
        }
    }

    private void arSceneReset() {
        this.mMeicamCipInfo.resetBeauty(this.mBeautyFx);
        if (this.mType == 1) {
            this.mSeekBar.setProgress(50);
        } else {
            this.mSeekBar.setProgress(0);
        }
        EditorEngine.getInstance().seekTimeline();
        this.mTvReset.setSelected(true);
    }

    private void selectRuddy() {
        this.mType = 3;
        this.mIvBuffing.setSelected(false);
        this.mTvBuffing.setTextColor(getResources().getColor(R.color.white_8));
        this.mIvWhitening.setSelected(false);
        this.mTvWhitening.setTextColor(getResources().getColor(R.color.white_8));
        this.mIvRuddy.setSelected(true);
        this.mTvRuddy.setTextColor(getResources().getColor(R.color.red_half_trans));
        NvsVideoFx nvsVideoFx = this.mBeautyFx;
        if (nvsVideoFx != null) {
            double floatVal = nvsVideoFx.getFloatVal(CommonData.VIDEO_FX_BEAUTY_REDDENING);
            this.mSeekBar.setProgress((int) (100.0d * floatVal));
            TextView textView = this.mTvEndText;
            textView.setText((((float) Math.round(10.0d * floatVal)) / 10.0f) + "");
            this.mMeicamCipInfo.getFaceEffectParameter().put(CommonData.VIDEO_FX_BEAUTY_REDDENING, Float.valueOf((float) floatVal));
        }
    }

    private void selectWhitening() {
        this.mType = 2;
        this.mIvBuffing.setSelected(false);
        this.mTvBuffing.setTextColor(getResources().getColor(R.color.white_8));
        this.mIvWhitening.setSelected(true);
        this.mTvWhitening.setTextColor(getResources().getColor(R.color.red_half_trans));
        this.mIvRuddy.setSelected(false);
        this.mTvRuddy.setTextColor(getResources().getColor(R.color.white_8));
        NvsVideoFx nvsVideoFx = this.mBeautyFx;
        if (nvsVideoFx != null) {
            double floatVal = nvsVideoFx.getFloatVal(CommonData.VIDEO_FX_BEAUTY_WHITENING);
            this.mSeekBar.setProgress((int) (100.0d * floatVal));
            TextView textView = this.mTvEndText;
            textView.setText((((float) Math.round(10.0d * floatVal)) / 10.0f) + "");
            this.mMeicamCipInfo.getFaceEffectParameter().put(CommonData.VIDEO_FX_BEAUTY_WHITENING, Float.valueOf((float) floatVal));
        }
    }

    private void selectBuffing() {
        this.mType = 1;
        this.mIvBuffing.setSelected(true);
        this.mTvBuffing.setTextColor(getResources().getColor(R.color.red_half_trans));
        this.mIvWhitening.setSelected(false);
        this.mTvWhitening.setTextColor(getResources().getColor(R.color.white_8));
        this.mIvRuddy.setSelected(false);
        this.mTvRuddy.setTextColor(getResources().getColor(R.color.white_8));
        NvsVideoFx nvsVideoFx = this.mBeautyFx;
        if (nvsVideoFx != null) {
            double floatVal = nvsVideoFx.getFloatVal(CommonData.VIDEO_FX_BEAUTY_STRENGTH);
            this.mSeekBar.setProgress((int) (100.0d * floatVal));
            TextView textView = this.mTvEndText;
            textView.setText((((float) Math.round(10.0d * floatVal)) / 10.0f) + "");
            this.mMeicamCipInfo.getFaceEffectParameter().put(CommonData.VIDEO_FX_BEAUTY_STRENGTH, Float.valueOf((float) floatVal));
        }
    }

    public void initVideoClip(MeicamVideoClip meicamVideoClip) {
        this.mMeicamCipInfo = meicamVideoClip;
        this.mVideoClip = (NvsVideoClip) meicamVideoClip.getObject();
        int fxCount = this.mVideoClip.getFxCount();
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= fxCount) {
                break;
            }
            NvsVideoFx fxByIndex = this.mVideoClip.getFxByIndex(i);
            if (fxByIndex.getAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_AR_SCENE) != null) {
                this.mBeautyFx = fxByIndex;
                z = true;
                break;
            }
            i++;
        }
        if (!z) {
            this.mBeautyFx = this.mVideoClip.appendBuiltinFx(CommonData.VIDEO_FX_AR_SCENE);
            NvsVideoFx nvsVideoFx = this.mBeautyFx;
            if (nvsVideoFx == null) {
                Logger.e(TAG, "appendBuiltinFx mBeautyFx is null");
                return;
            }
            nvsVideoFx.setAttachment(CommonData.ATTACHMENT_KEY_VIDEO_CLIP_AR_SCENE, CommonData.VIDEO_FX_AR_SCENE);
        } else {
            setResetStatus();
        }
        meicamVideoClip.openBeautyFx(this.mBeautyFx);
    }
}
