package com.meishe.myvideo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meishe.myvideoapp.R;

public class CommonDialog extends Dialog {
    private int mBtnCount = 1;
    private OnBtnClickListener mClickListener;
    private Context mContext;
    private OnCreateListener mCreateListener;
    private TextView mFirstTipsTxt;
    private Button mLeftBtn;
    private RelativeLayout mLeftBtnLayout;
    private Button mRightBtn;
    private RelativeLayout mRightBtnLayout;
    private TextView mSecondTipsTxt;
    private View mTitleLine;
    private TextView mTitleTxt;

    public interface OnBtnClickListener {
        void OnLeftBtnClicked(View view);

        void OnRightBtnClicked(View view);
    }

    public interface OnCreateListener {
        void OnCreated();
    }

    public interface TipsButtonClickListener {
        void onTipsLeftButtonClick(View view);

        void onTipsRightButtonClick(View view);
    }

    public CommonDialog(Context context, int i) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.mBtnCount = i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.common_dialog);
        setCanceledOnTouchOutside(false);
        initView();
        OnCreateListener onCreateListener = this.mCreateListener;
        if (onCreateListener != null) {
            onCreateListener.OnCreated();
        }
    }

    private void initView() {
        this.mTitleTxt = (TextView) findViewById(R.id.dialog_title);
        this.mFirstTipsTxt = (TextView) findViewById(R.id.dialog_first_tip);
        this.mSecondTipsTxt = (TextView) findViewById(R.id.dialog_second_tip);
        this.mRightBtn = (Button) findViewById(R.id.right_btn);
        this.mLeftBtn = (Button) findViewById(R.id.left_btn);
        this.mRightBtnLayout = (RelativeLayout) findViewById(R.id.left_btn_layout);
        this.mLeftBtnLayout = (RelativeLayout) findViewById(R.id.right_btn_layout);
        this.mTitleLine = findViewById(R.id.title_line);
        if (this.mBtnCount == 1) {
            this.mLeftBtnLayout.setVisibility(0);
            this.mRightBtnLayout.setVisibility(8);
        } else {
            this.mRightBtnLayout.setVisibility(0);
            this.mLeftBtnLayout.setVisibility(0);
        }
        this.mRightBtn.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.CommonDialog.AnonymousClass1 */

            public void onClick(View view) {
                if (CommonDialog.this.mClickListener != null) {
                    CommonDialog.this.mClickListener.OnRightBtnClicked(view);
                }
            }
        });
        this.mLeftBtn.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.CommonDialog.AnonymousClass2 */

            public void onClick(View view) {
                if (CommonDialog.this.mClickListener != null) {
                    CommonDialog.this.mClickListener.OnLeftBtnClicked(view);
                }
            }
        });
    }

    public void setTitleTxt(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mTitleTxt.setVisibility(8);
            this.mTitleLine.setVisibility(8);
            return;
        }
        this.mTitleLine.setVisibility(0);
        this.mTitleTxt.setVisibility(0);
        this.mTitleTxt.setText(str);
    }

    public void setFirstTipsTxt(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mFirstTipsTxt.setVisibility(8);
            return;
        }
        this.mFirstTipsTxt.setText(str);
        this.mFirstTipsTxt.setVisibility(0);
    }

    public void setSecondTipsTxt(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mSecondTipsTxt.setVisibility(0);
            return;
        }
        this.mSecondTipsTxt.setVisibility(0);
        this.mSecondTipsTxt.setText(str);
    }

    public void setBtnText(String str, String str2) {
        this.mLeftBtn.setText(str);
        this.mRightBtn.setText(str2);
    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.mClickListener = onBtnClickListener;
    }

    public void setOnCreateListener(OnCreateListener onCreateListener) {
        this.mCreateListener = onCreateListener;
    }
}
