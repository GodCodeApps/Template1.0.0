package com.meishe.myvideo.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meishe.engine.util.ColorUtil;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideo.util.SoftKeyboardUtil;
import com.meishe.myvideo.util.TypefaceUtil;
import com.meishe.myvideo.util.engine.EditorEngine;
import com.meishe.myvideoapp.R;

public class MYCompoundCaptionEditView extends MYBaseView {
    private int mCaptionIndex = -1;
    private NvsTimelineCompoundCaption mCompoundCaption;
    private EditText mEtCaptionInput;
    private HorizontalScrollView mHvFont;
    private ImageView mIvCancel;
    private ImageView mIvConfirm;
    private MYMultiColorView mMultiColorView;
    private String mOldCaptionContent;
    private NvsColor mOldColor;
    private String mOldfontFamily;
    private RelativeLayout mRlColorAndFontContainer;
    private RelativeLayout mRlFontDefault;
    private RelativeLayout mRlFontFangJianTi;
    private RelativeLayout mRlFontShuJianTi;
    private RelativeLayout mRlFontWenYiTi;
    private RelativeLayout mRlFontZhuShiTi;
    private RelativeLayout mRlTopMenu;
    private TextView mTvFontFangJianTi;
    private TextView mTvFontShuJianTi;
    private TextView mTvFontWenYiTi;
    private TextView mTvFontZhuShiTi;

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initRecyclerView() {
    }

    public MYCompoundCaptionEditView(Context context) {
        super(context);
    }

    public MYCompoundCaptionEditView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MYCompoundCaptionEditView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void init() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.compound_caption_edit_view, this);
        this.mMultiColorView = (MYMultiColorView) inflate.findViewById(R.id.multi_color_view);
        this.mEtCaptionInput = (EditText) inflate.findViewById(R.id.et_caption_input);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mIvCancel = (ImageView) inflate.findViewById(R.id.iv_cancel);
        this.mRlFontDefault = (RelativeLayout) inflate.findViewById(R.id.rl_font_default);
        this.mRlFontFangJianTi = (RelativeLayout) inflate.findViewById(R.id.rl_font_fang_jian_ti);
        this.mTvFontFangJianTi = (TextView) inflate.findViewById(R.id.tv_font_fang_jian_ti);
        this.mRlFontShuJianTi = (RelativeLayout) inflate.findViewById(R.id.rl_font_shu_jian_ti);
        this.mTvFontShuJianTi = (TextView) inflate.findViewById(R.id.tv_font_shu_jian_ti);
        this.mRlFontZhuShiTi = (RelativeLayout) inflate.findViewById(R.id.rl_font_zhu_shi_ti);
        this.mTvFontZhuShiTi = (TextView) inflate.findViewById(R.id.tv_font_zhu_shi_ti);
        this.mRlFontWenYiTi = (RelativeLayout) inflate.findViewById(R.id.rl_font_wen_yi_ti);
        this.mTvFontWenYiTi = (TextView) inflate.findViewById(R.id.tv_font_wen_yi_ti);
        this.mRlTopMenu = (RelativeLayout) inflate.findViewById(R.id.rl_top_menu);
        this.mRlColorAndFontContainer = (RelativeLayout) inflate.findViewById(R.id.rl_font_and_color_container);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initData() {
//        TypefaceUtil instance = TypefaceUtil.getInstance(this.mContext);
//        instance.setTypeface(this.mTvFontFangJianTi, "font/FZFSJW.ttf");
//        instance.setTypeface(this.mTvFontShuJianTi, "font/FZSSJW.TTF");
//        instance.setTypeface(this.mTvFontZhuShiTi, "font/YRDZST.ttf");
//        instance.setTypeface(this.mTvFontWenYiTi, "font/ZKWYJW.TTF");
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initListener() {
        this.mRlFontDefault.setOnClickListener(this);
        this.mRlFontFangJianTi.setOnClickListener(this);
        this.mRlFontShuJianTi.setOnClickListener(this);
        this.mRlFontZhuShiTi.setOnClickListener(this);
        this.mRlFontWenYiTi.setOnClickListener(this);
        this.mIvConfirm.setOnClickListener(this);
        this.mIvCancel.setOnClickListener(this);
        this.mEtCaptionInput.addTextChangedListener(new TextWatcher() {
            /* class com.meishe.myvideo.view.MYCompoundCaptionEditView.AnonymousClass1 */

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (MYCompoundCaptionEditView.this.mCompoundCaption != null && MYCompoundCaptionEditView.this.mCaptionIndex >= 0) {
                    MYCompoundCaptionEditView.this.mCompoundCaption.setText(MYCompoundCaptionEditView.this.mCaptionIndex, editable.toString());
                    EditorEngine.getInstance().seekTimeline(2);
                }
            }
        });
        final MultiFunctionAdapter multiFunctionAdapter = this.mMultiColorView.getMultiFunctionAdapter();
        if (multiFunctionAdapter != null) {
            multiFunctionAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                /* class com.meishe.myvideo.view.MYCompoundCaptionEditView.AnonymousClass2 */

                @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
                public void onItemClicked(View view, int i) {
                    MYCompoundCaptionEditView.this.mCompoundCaption.setTextColor(MYCompoundCaptionEditView.this.mCaptionIndex, ColorUtil.colorStringtoNvsColor(multiFunctionAdapter.getItem(i).getColorValue()));
                    EditorEngine.getInstance().seekTimeline(2);
                }
            });
        }
    }

    private void resetCompoundCaption() {
        int i;
        NvsTimelineCompoundCaption nvsTimelineCompoundCaption = this.mCompoundCaption;
        if (nvsTimelineCompoundCaption != null && (i = this.mCaptionIndex) >= 0) {
            nvsTimelineCompoundCaption.setText(i, this.mOldCaptionContent);
            this.mCompoundCaption.setTextColor(this.mCaptionIndex, this.mOldColor);
            this.mCompoundCaption.setFontFamily(this.mCaptionIndex, this.mOldfontFamily);
            EditorEngine.getInstance().seekTimeline(2);
        }
    }

    public void show(NvsTimelineCompoundCaption nvsTimelineCompoundCaption, int i, int i2) {
        setKeyboardHeight(i2);
        super.show();
        if (this.mEtCaptionInput != null) {
            SoftKeyboardUtil.showInput(this.mContext, this.mEtCaptionInput);
            if (nvsTimelineCompoundCaption != null) {
                this.mCompoundCaption = nvsTimelineCompoundCaption;
                String text = nvsTimelineCompoundCaption.getText(i);
                this.mOldColor = this.mCompoundCaption.getTextColor(this.mCaptionIndex);
                this.mOldfontFamily = this.mCompoundCaption.getFontFamily(this.mCaptionIndex);
                this.mOldCaptionContent = text;
                this.mCaptionIndex = i;
                this.mEtCaptionInput.setText(text);
                EditText editText = this.mEtCaptionInput;
                editText.setSelection(editText.getText().length());
            }
            Log.e("lpf", "keyboardHeight:" + i2);
        }
    }

    public void setKeyboardHeight(int i) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mRlTopMenu.getLayoutParams();
        layoutParams.height = ScreenUtils.dp2px(this.mContext, 94.0f) + i;
        this.mRlTopMenu.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams layoutParams2 = this.mRlColorAndFontContainer.getLayoutParams();
        layoutParams2.height = i;
        this.mRlColorAndFontContainer.setLayoutParams(layoutParams2);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void hide() {
        super.hide();
        if (this.mEtCaptionInput != null) {
            SoftKeyboardUtil.hideInput(this.mContext, this.mEtCaptionInput);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_cancel) {
            resetCompoundCaption();
            EditorEngine.getInstance().getVideoFragment().setCurCompoundCaption(null);
            hide();
        } else if (id != R.id.iv_confirm) {
            if (id == R.id.rl_font_default) { /*{ENCODED_INT: 2131296689}*/
                EditorEngine.getInstance().setCompoundCaptionFont(this.mCompoundCaption, this.mCaptionIndex, "");
                return;
            } else if (id == R.id.rl_font_fang_jian_ti) { /*{ENCODED_INT: 2131296690}*/
                EditorEngine.getInstance().setCompoundCaptionFont(this.mCompoundCaption, this.mCaptionIndex, "font/FZFSJW.ttf");
                return;
            } else if (id == R.id.rl_font_shu_jian_ti) { /*{ENCODED_INT: 2131296691}*/
                EditorEngine.getInstance().setCompoundCaptionFont(this.mCompoundCaption, this.mCaptionIndex, "font/FZSSJW.TTF");
                return;
            } else if (id == R.id.rl_font_wen_yi_ti) { /*{ENCODED_INT: 2131296692}*/
                EditorEngine.getInstance().setCompoundCaptionFont(this.mCompoundCaption, this.mCaptionIndex, "font/ZKWYJW.TTF");
                return;
            } else if (id == R.id.rl_font_zhu_shi_ti) { /*{ENCODED_INT: 2131296693}*/
                EditorEngine.getInstance().setCompoundCaptionFont(this.mCompoundCaption, this.mCaptionIndex, "font/YRDZST.ttf");
                return;
            }
            return;
        } else {
            EditorEngine.getInstance().getVideoFragment().setDrawRectVisible(false);
            EditorEngine.getInstance().getVideoFragment().setCurCompoundCaption(null);
            hide();
        }
    }
}
