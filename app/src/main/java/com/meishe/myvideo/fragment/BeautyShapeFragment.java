package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.MeicamVideoTrack;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.decoration.SpaceItemDecoration;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideo.util.engine.EditorEngine;
import com.meishe.myvideo.util.ui.ToastUtil;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeautyShapeFragment extends BaseFragment {
    public static final String MRICAMVIDEOCLIP = "meicamVideoClip";
    private static final String TAG = "BeautyFragment";
    private NvsVideoFx mBeautyFx;
    private String mBeautyKey;
    private ImageView mIvApplyAll;
    private LinearLayout mLlTopContainer;
    private MeicamVideoClip mMeicamCipInfo;
    private RelativeLayout mRlSeekContainer;
    private SeekBar mSeekBar;
    private TextView mTvApplyAll;
    private TextView mTvEndText;
    private TextView mTvReset;
    private NvsVideoClip mVideoClip;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.beauty_type_list_fragment;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        MeicamVideoClip meicamVideoClip;
        this.mIvApplyAll = (ImageView) this.mRootView.findViewById(R.id.iv_apply_all);
        this.mTvApplyAll = (TextView) this.mRootView.findViewById(R.id.tv_apply_all);
        this.mRecyclerView = (RecyclerView) this.mRootView.findViewById(R.id.recyclerView);
        this.mRlSeekContainer = (RelativeLayout) this.mRootView.findViewById(R.id.rl_seek_container);
        this.mLlTopContainer = (LinearLayout) this.mRootView.findViewById(R.id.ll_top_container);
        this.mSeekBar = (SeekBar) this.mRootView.findViewById(R.id.seek_bar);
        this.mTvEndText = (TextView) this.mRootView.findViewById(R.id.end_text);
        this.mTvReset = (TextView) this.mRootView.findViewById(R.id.tv_reset);
        this.mTvReset.setSelected(true);
        initRecyclerView();
        Bundle arguments = getArguments();
        if (arguments != null && (meicamVideoClip = (MeicamVideoClip) arguments.getSerializable(MRICAMVIDEOCLIP)) != null) {
            initVideoClip(meicamVideoClip);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
        this.mSeekBar.setMax(100);
        ArrayList<BaseInfo> beautyShapeDataList = MenuDataManager.getBeautyShapeDataList(this.mContext);
        this.mAdapter.addAll(beautyShapeDataList);
        this.mAdapter.setSelectPosition(0);
        this.mBeautyKey = beautyShapeDataList.get(0).getBeautyShapeId();
        NvsVideoFx nvsVideoFx = this.mBeautyFx;
        if (nvsVideoFx != null) {
            double floatVal = nvsVideoFx.getFloatVal(this.mBeautyKey);
            this.mSeekBar.setProgress((int) ((1.0d + floatVal) * 50.0d));
            TextView textView = this.mTvEndText;
            textView.setText((Math.round(10.0d * floatVal) / 10) + "");
            this.mMeicamCipInfo.getFaceEffectParameter().put(this.mBeautyKey, Float.valueOf((float) floatVal));
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mTvReset.setOnClickListener(this);
        this.mIvApplyAll.setOnClickListener(this);
        this.mTvApplyAll.setOnClickListener(this);
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.fragment.BeautyShapeFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                BeautyShapeFragment beautyShapeFragment = BeautyShapeFragment.this;
                beautyShapeFragment.mBeautyKey = beautyShapeFragment.mAdapter.getItem(i).getBeautyShapeId();
                if (BeautyShapeFragment.this.mBeautyFx != null) {
                    double floatVal = BeautyShapeFragment.this.mBeautyFx.getFloatVal(BeautyShapeFragment.this.mBeautyKey);
                    BeautyShapeFragment.this.mSeekBar.setProgress((int) ((1.0d + floatVal) * 50.0d));
                    TextView textView = BeautyShapeFragment.this.mTvEndText;
                    textView.setText((((float) Math.round(10.0d * floatVal)) / 10.0f) + "");
                    BeautyShapeFragment.this.mMeicamCipInfo.getFaceEffectParameter().put(BeautyShapeFragment.this.mBeautyKey, Float.valueOf((float) floatVal));
                }
            }
        });
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.fragment.BeautyShapeFragment.AnonymousClass2 */

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                BeautyShapeFragment.this.mBeautyFx.setFloatVal(BeautyShapeFragment.this.mBeautyKey, (double) ((((float) seekBar.getProgress()) / 50.0f) - 1.0f));
                BeautyShapeFragment.this.mMeicamCipInfo.getFaceEffectParameter().put(BeautyShapeFragment.this.mBeautyKey, Float.valueOf((((float) seekBar.getProgress()) / 50.0f) - 1.0f));
                TextView textView = BeautyShapeFragment.this.mTvEndText;
                textView.setText((((float) Math.round(((((float) seekBar.getProgress()) / 50.0f) - 1.0f) * 10.0f)) / 10.0f) + "");
                EditorEngine.getInstance().seekTimeline();
                BeautyShapeFragment.this.setResetStatus();
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
                if (!(key.equals(CommonData.VIDEO_FX_BEAUTY_STRENGTH) || key.equals(CommonData.VIDEO_FX_BEAUTY_WHITENING) || key.equals(CommonData.VIDEO_FX_BEAUTY_REDDENING) || entry.getValue().floatValue() == 0.0f)) {
                    this.mTvReset.setSelected(false);
                    return;
                }
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_apply_all) {
            applyAllClip();
        } else if (id == R.id.tv_apply_all) {
            applyAllClip();
        } else if (id == R.id.tv_reset) {
            arSceneReset();
        }
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
                                meicamVideoClip2.setFaceBeautyShapeEffectParameter(faceEffectParameter);
                                meicamVideoClip2.applyBeautyShapeFaceEffect();
                            }
                        }
                    }
                }
            }
            ToastUtil.showCenterToast(getContext(), (int) R.string.has_been_apply_to_all);
        }
    }

    private void arSceneReset() {
        this.mMeicamCipInfo.resetBeautyShape(this.mBeautyFx);
        this.mSeekBar.setProgress(50);
        EditorEngine.getInstance().seekTimeline();
        this.mTvReset.setSelected(true);
    }

    private void initRecyclerView() {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mAdapter = new MultiFunctionAdapter(this.mContext, this.mRecyclerView);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dp2px(this.mContext, 3.0f), ScreenUtils.dp2px(this.mContext, 3.0f)));
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
