package com.meishe.myvideo.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import com.meishe.myvideo.application.MeiSheApplication;
import com.meishe.myvideo.util.SystemUtils;
import com.meishe.myvideoapp.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivacyPolicyDialog extends Dialog {
    private static final String PRIVACY_MATCH_RULE_CH = "《.*?》";
    private static final String PRIVACY_MATCH_RULE_EN = "\".*?\"";
    private static final String TAG = "PrivacyPolicyDialog";
    private TextView mAgreeButton;
    private Context mContext = MeiSheApplication.getContext();
    private TextView mNotUsedButton;
    private OnPrivacyClickListener mPrivacyListener;
    private TextView mStatementContent;

    public interface OnPrivacyClickListener {
        void onButtonClick(boolean z);

        void pageJumpToWeb(String str);
    }

    public void setOnButtonClickListener(OnPrivacyClickListener onPrivacyClickListener) {
        this.mPrivacyListener = onPrivacyClickListener;
    }

    public PrivacyPolicyDialog(Context context, int i) {
        super(context, i);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.privacy_policy_dialog_layout);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initViews();
        initData();
        initViewsListener();
    }

    private void initData() {
        String string = this.mContext.getString(R.string.statement_content);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) string);
        Matcher matcher = Pattern.compile(SystemUtils.isZh(this.mContext) ? PRIVACY_MATCH_RULE_CH : PRIVACY_MATCH_RULE_EN).matcher(string);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            final String group = matcher.group();
            spannableStringBuilder.setSpan(new ClickableSpan() {
                /* class com.meishe.myvideo.view.PrivacyPolicyDialog.AnonymousClass1 */

                public void onClick(View view) {
                    if (PrivacyPolicyDialog.this.mPrivacyListener != null) {
                        PrivacyPolicyDialog.this.mPrivacyListener.pageJumpToWeb(group);
                    }
                }

                public void updateDrawState(TextPaint textPaint) {
                    textPaint.setUnderlineText(false);
                }
            }, start, end, 33);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FC2B55")), start, end, 33);
        }
        this.mStatementContent.setMovementMethod(LinkMovementMethod.getInstance());
        this.mStatementContent.setText(spannableStringBuilder);
        this.mStatementContent.setHighlightColor(this.mContext.getResources().getColor(R.color.colorTranslucent));
    }

    private void initViews() {
        this.mStatementContent = (TextView) findViewById(R.id.statementContent);
        this.mNotUsedButton = (TextView) findViewById(R.id.notUsedButton);
        this.mAgreeButton = (TextView) findViewById(R.id.agreeButton);
    }

    private void initViewsListener() {
        this.mNotUsedButton.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.PrivacyPolicyDialog.AnonymousClass2 */

            public void onClick(View view) {
                if (PrivacyPolicyDialog.this.mPrivacyListener != null) {
                    PrivacyPolicyDialog.this.mPrivacyListener.onButtonClick(false);
                }
                PrivacyPolicyDialog.this.dismiss();
            }
        });
        this.mAgreeButton.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.PrivacyPolicyDialog.AnonymousClass3 */

            public void onClick(View view) {
                if (PrivacyPolicyDialog.this.mPrivacyListener != null) {
                    PrivacyPolicyDialog.this.mPrivacyListener.onButtonClick(true);
                }
                PrivacyPolicyDialog.this.dismiss();
            }
        });
    }
}
