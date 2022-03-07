package com.meishe.myvideo.view.editview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.meishe.myvideo.view.editview.EditView;
import com.meishe.myvideoapp.R;

public class EditDeNoiseView extends EditView<Boolean> {
    public Switch mSwitchView;
    private TextView mTextView;

    public EditDeNoiseView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.view_edit_denoise, this);
        this.mSwitchView = (Switch) inflate.findViewById(R.id.switch_view);
        this.mTextView = (TextView) inflate.findViewById(R.id.switch_hint);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initData() {
        this.mSwitchView.setChecked(false);
        this.mTextView.setText(R.string.open_denoise);
        this.mTextView.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.editview.EditDeNoiseView.AnonymousClass1 */

            public void onClick(View view) {
            }
        });
    }

    public void updateData(Boolean bool) {
        this.mSwitchView.setChecked(bool.booleanValue());
    }

    @Override // com.meishe.myvideo.view.editview.EditView
    public void setCallBackListener(final EditView.CallBack<Boolean> callBack) {
        this.mSwitchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class com.meishe.myvideo.view.editview.EditDeNoiseView.AnonymousClass2 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    EditDeNoiseView.this.mTextView.setText(R.string.close_denoise);
                } else {
                    EditDeNoiseView.this.mTextView.setText(R.string.open_denoise);
                }
                if (callBack != null) {
                    callBack.onCallBack(Boolean.valueOf(z));
                }
            }
        });
    }
}
