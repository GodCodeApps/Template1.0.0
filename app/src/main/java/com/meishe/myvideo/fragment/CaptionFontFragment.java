package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.util.TypefaceUtil;
import com.meishe.myvideoapp.R;
import org.greenrobot.eventbus.EventBus;

public class CaptionFontFragment extends BaseFragment {
    private boolean mIsBold;
    private boolean mIsItalics;
    private boolean mIsShadow;
    private LinearLayout mLlApplyToAll;
    private RelativeLayout mRlFontDefault;
    private RelativeLayout mRlFontFangJianTi;
    private RelativeLayout mRlFontShuJianTi;
    private RelativeLayout mRlFontWenYiTi;
    private RelativeLayout mRlFontZhuShiTi;
    private TextView mTvFontBold;
    private TextView mTvFontFangJianTi;
    private TextView mTvFontItalics;
    private TextView mTvFontShadow;
    private TextView mTvFontShuJianTi;
    private TextView mTvFontWenYiTi;
    private TextView mTvFontZhuShiTi;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.caption_font_list_fragment;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        this.mRlFontDefault = (RelativeLayout) this.mRootView.findViewById(R.id.rl_font_default);
        this.mRlFontFangJianTi = (RelativeLayout) this.mRootView.findViewById(R.id.rl_font_fang_jian_ti);
        this.mTvFontFangJianTi = (TextView) this.mRootView.findViewById(R.id.tv_font_fang_jian_ti);
        this.mRlFontShuJianTi = (RelativeLayout) this.mRootView.findViewById(R.id.rl_font_shu_jian_ti);
        this.mTvFontShuJianTi = (TextView) this.mRootView.findViewById(R.id.tv_font_shu_jian_ti);
        this.mRlFontZhuShiTi = (RelativeLayout) this.mRootView.findViewById(R.id.rl_font_zhu_shi_ti);
        this.mTvFontZhuShiTi = (TextView) this.mRootView.findViewById(R.id.tv_font_zhu_shi_ti);
        this.mRlFontWenYiTi = (RelativeLayout) this.mRootView.findViewById(R.id.rl_font_wen_yi_ti);
        this.mTvFontWenYiTi = (TextView) this.mRootView.findViewById(R.id.tv_font_wen_yi_ti);
        this.mTvFontBold = (TextView) this.mRootView.findViewById(R.id.tv_font_bold);
        this.mTvFontItalics = (TextView) this.mRootView.findViewById(R.id.tv_font_italics);
        this.mTvFontShadow = (TextView) this.mRootView.findViewById(R.id.tv_font_shadow);
        this.mLlApplyToAll = (LinearLayout) this.mRootView.findViewById(R.id.ll_apply_to_all);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
//        TypefaceUtil instance = TypefaceUtil.getInstance(getContext());
//        instance.setTypeface(this.mTvFontFangJianTi, "font/FZFSJW.ttf");
//        instance.setTypeface(this.mTvFontShuJianTi, "font/FZSSJW.TTF");
//        instance.setTypeface(this.mTvFontZhuShiTi, "font/YRDZST.ttf");
//        instance.setTypeface(this.mTvFontWenYiTi, "font/ZKWYJW.TTF");
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mRlFontDefault.setOnClickListener(this);
        this.mRlFontFangJianTi.setOnClickListener(this);
        this.mRlFontShuJianTi.setOnClickListener(this);
        this.mRlFontZhuShiTi.setOnClickListener(this);
        this.mRlFontWenYiTi.setOnClickListener(this);
        this.mTvFontBold.setOnClickListener(this);
        this.mTvFontItalics.setOnClickListener(this);
        this.mTvFontShadow.setOnClickListener(this);
        this.mLlApplyToAll.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_apply_to_all) {
            MessageEvent.sendEvent(MessageEvent.MESSAGE_APPLY_ALL_CAPTION_FONT);
        } else if (id != R.id.tv_font_bold) {
            if (id == R.id.rl_font_default) { /*{ENCODED_INT: 2131296689}*/
                sendEvent("", 1011);
                return;
            } else if (id == R.id.rl_font_fang_jian_ti) { /*{ENCODED_INT: 2131296690}*/
                sendEvent("font/FZFSJW.ttf", 1011);
                return;
            } else if (id == R.id.rl_font_shu_jian_ti) { /*{ENCODED_INT: 2131296691}*/
                sendEvent("font/FZSSJW.TTF", 1011);
                return;
            } else if (id == R.id.rl_font_wen_yi_ti) { /*{ENCODED_INT: 2131296692}*/
                sendEvent("font/ZKWYJW.TTF", 1011);
                return;
            } else if (id == R.id.rl_font_zhu_shi_ti) { /*{ENCODED_INT: 2131296693}*/
                sendEvent("font/YRDZST.ttf", 1011);
                return;
            }
            if (id == R.id.tv_font_italics) { /*{ENCODED_INT: 2131296841}*/
                this.mIsItalics = !this.mIsItalics;
                updateItalicButton(this.mIsItalics);
                sendEvent(this.mIsItalics, 1013);
                return;
            } else if (id == R.id.tv_font_shadow) { /*{ENCODED_INT: 2131296842}*/
                this.mIsShadow = !this.mIsShadow;
                updateShadowButton(this.mIsShadow);
                sendEvent(this.mIsShadow, 1014);
                return;
            }
            return;
        } else {
            this.mIsBold = !this.mIsBold;
            updateBoldButton(this.mIsBold);
            sendEvent(this.mIsBold, 1012);
        }
    }

    private void sendEvent(String str, int i) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i);
        messageEvent.setStrValue(str);
        EventBus.getDefault().post(messageEvent);
    }

    private void sendEvent(boolean z, int i) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i);
        messageEvent.setBooleanValue(z);
        EventBus.getDefault().post(messageEvent);
    }

    public void updateBoldButton(boolean z) {
        this.mTvFontBold.setBackgroundResource(z ? R.drawable.caption_bold_select_bg : R.drawable.caption_bold_bg);
    }

    public void updateItalicButton(boolean z) {
        this.mTvFontItalics.setBackgroundResource(z ? R.drawable.caption_bold_select_bg : R.drawable.caption_bold_bg);
    }

    public void updateShadowButton(boolean z) {
        this.mTvFontShadow.setBackgroundResource(z ? R.drawable.caption_bold_select_bg : R.drawable.caption_bold_bg);
    }
}
