package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.view.MYMultiColorView;
import com.meishe.myvideoapp.R;

public class CaptionColorFragment extends BaseFragment {
    private LinearLayout mLlApplyToAll;
    private MYMultiColorView mMultiColorView;
    private SeekBar mSbOpacity;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.caption_color_list_fragment;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        this.mMultiColorView = (MYMultiColorView) this.mRootView.findViewById(R.id.multi_color_view);
        this.mSbOpacity = (SeekBar) this.mRootView.findViewById(R.id.sb_opacity);
        this.mLlApplyToAll = (LinearLayout) this.mRootView.findViewById(R.id.ll_apply_to_all);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mAdapter = this.mMultiColorView.getMultiFunctionAdapter();
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.fragment.CaptionColorFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                CaptionColorFragment.this.postEvent(i, 1006);
            }
        });
        this.mLlApplyToAll.setOnClickListener(this);
        this.mSbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.fragment.CaptionColorFragment.AnonymousClass2 */

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                CaptionColorFragment.this.sendEvent(seekBar.getProgress(), 1010);
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.ll_apply_to_all) {
            MessageEvent.sendEvent(MessageEvent.MESSAGE_APPLY_ALL_CAPTION_COLOR);
        }
    }

    public void setOpacityProgress(float f) {
        SeekBar seekBar = this.mSbOpacity;
        if (seekBar != null) {
            seekBar.setProgress((int) (f * 100.0f));
        }
    }
}
