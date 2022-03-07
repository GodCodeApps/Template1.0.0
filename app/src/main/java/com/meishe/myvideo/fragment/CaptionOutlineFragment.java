package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.view.MYMultiColorView;
import com.meishe.myvideoapp.R;

public class CaptionOutlineFragment extends BaseFragment {
    private boolean isSend = true;
    private LinearLayout mLlApplyToAll;
    private MYMultiColorView mMultiColorView;
    private SeekBar mSbOpacity;
    private SeekBar mSbWidth;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.caption_outline_list_fragment;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        this.mMultiColorView = (MYMultiColorView) this.mRootView.findViewById(R.id.multi_color_view);
        this.mSbWidth = (SeekBar) this.mRootView.findViewById(R.id.sb_width);
        this.mSbOpacity = (SeekBar) this.mRootView.findViewById(R.id.sb_opacity);
        this.mLlApplyToAll = (LinearLayout) this.mRootView.findViewById(R.id.ll_apply_to_all);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mAdapter = this.mMultiColorView.getMultiFunctionAdapter();
        if (this.mAdapter != null) {
            this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                /* class com.meishe.myvideo.fragment.CaptionOutlineFragment.AnonymousClass1 */

                @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
                public void onItemClicked(View view, int i) {
                    if (CaptionOutlineFragment.this.isSend) {
                        CaptionOutlineFragment.this.postEvent(i, 1007);
                        return;
                    }
                    CaptionOutlineFragment.this.postEvent(i, 1007);
                    CaptionOutlineFragment captionOutlineFragment = CaptionOutlineFragment.this;
                    captionOutlineFragment.sendEvent(captionOutlineFragment.mSbWidth.getProgress(), 1009);
                    CaptionOutlineFragment captionOutlineFragment2 = CaptionOutlineFragment.this;
                    captionOutlineFragment2.sendEvent(captionOutlineFragment2.mSbOpacity.getProgress(), 1008);
                    CaptionOutlineFragment.this.isSend = true;
                }
            });
        }
        this.mLlApplyToAll.setOnClickListener(this);
        this.mSbWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.fragment.CaptionOutlineFragment.AnonymousClass2 */

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (CaptionOutlineFragment.this.isSend) {
                    CaptionOutlineFragment.this.sendEvent(seekBar.getProgress(), 1009);
                }
            }
        });
        this.mSbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.fragment.CaptionOutlineFragment.AnonymousClass3 */

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (CaptionOutlineFragment.this.isSend) {
                    CaptionOutlineFragment.this.sendEvent(seekBar.getProgress(), 1008);
                }
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.ll_apply_to_all) {
            MessageEvent.sendEvent(MessageEvent.MESSAGE_APPLY_ALL_CAPTION_OUT_LINE);
        }
    }

    public void setWidthAndOpacityProgress(float f, float f2, boolean z) {
        this.isSend = z;
        this.mSbWidth.setProgress((int) (f * 10.0f));
        this.mSbOpacity.setProgress((int) (f2 * 100.0f));
    }
}
