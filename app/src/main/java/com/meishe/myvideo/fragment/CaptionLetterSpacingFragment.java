package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideoapp.R;

public class CaptionLetterSpacingFragment extends BaseFragment {
    private static final float CAPTION_LARGE_SPACING = 200.0f;
    private static final float CAPTION_MORE_LARGE_SPACING = 150.0f;
    private static final float CAPTION_STANDARD_SPACING = 100.0f;
    private boolean mIsSelectedLarge = false;
    private boolean mIsSelectedMore = false;
    private boolean mIsSelectedStandard = false;
    private LinearLayout mLlApplyToAll;
    private TextView mTvLetterSpaceBig;
    private TextView mTvLetterSpaceMore;
    private TextView mTvLetterSpaceStandard;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.caption_letter_space_fragment;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        this.mTvLetterSpaceStandard = (TextView) this.mRootView.findViewById(R.id.tv_letter_space_standard);
        this.mTvLetterSpaceMore = (TextView) this.mRootView.findViewById(R.id.tv_letter_space_more);
        this.mTvLetterSpaceBig = (TextView) this.mRootView.findViewById(R.id.tv_letter_space_big);
        this.mLlApplyToAll = (LinearLayout) this.mRootView.findViewById(R.id.ll_apply_to_all);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mTvLetterSpaceStandard.setOnClickListener(this);
        this.mTvLetterSpaceMore.setOnClickListener(this);
        this.mTvLetterSpaceBig.setOnClickListener(this);
        this.mLlApplyToAll.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.ll_apply_to_all) {
            switch (id) {
                case R.id.tv_letter_space_big /*{ENCODED_INT: 2131296848}*/:
                    setSelectedLarge(true);
                    MessageEvent.sendEvent((float) CAPTION_LARGE_SPACING, 1015);
                    return;
                case R.id.tv_letter_space_more /*{ENCODED_INT: 2131296849}*/:
                    setSelectedMore(true);
                    MessageEvent.sendEvent((float) CAPTION_MORE_LARGE_SPACING, 1015);
                    return;
                case R.id.tv_letter_space_standard /*{ENCODED_INT: 2131296850}*/:
                    setSelectedStandard(true);
                    MessageEvent.sendEvent((float) CAPTION_STANDARD_SPACING, 1015);
                    return;
                default:
                    return;
            }
        } else {
            MessageEvent.sendEvent(MessageEvent.MESSAGE_APPLY_ALL_CAPTION_LETTER_SPACE);
        }
    }

    public void setSelectedStandard(boolean z) {
        this.mIsSelectedStandard = z;
        if (z) {
            this.mIsSelectedMore = false;
            this.mIsSelectedLarge = false;
        }
        updateButtons();
    }

    public void setSelectedMore(boolean z) {
        this.mIsSelectedMore = z;
        if (z) {
            this.mIsSelectedStandard = false;
            this.mIsSelectedLarge = false;
        }
        updateButtons();
    }

    public void setSelectedLarge(boolean z) {
        this.mIsSelectedLarge = z;
        if (z) {
            this.mIsSelectedStandard = false;
            this.mIsSelectedMore = false;
        }
        updateButtons();
    }

    private void updateButtons() {
        updateStandardButton(this.mIsSelectedStandard);
        updateMoreButton(this.mIsSelectedMore);
        updateLargeButton(this.mIsSelectedLarge);
    }

    public void updateStandardButton(boolean z) {
        this.mTvLetterSpaceStandard.setBackgroundResource(z ? R.drawable.caption_bold_select_bg : R.drawable.caption_bold_bg);
        this.mIsSelectedStandard = z;
    }

    public void updateMoreButton(boolean z) {
        this.mTvLetterSpaceMore.setBackgroundResource(z ? R.drawable.caption_bold_select_bg : R.drawable.caption_bold_bg);
        this.mIsSelectedMore = z;
    }

    public void updateLargeButton(boolean z) {
        this.mTvLetterSpaceBig.setBackgroundResource(z ? R.drawable.caption_bold_select_bg : R.drawable.caption_bold_bg);
        this.mIsSelectedLarge = z;
    }
}
