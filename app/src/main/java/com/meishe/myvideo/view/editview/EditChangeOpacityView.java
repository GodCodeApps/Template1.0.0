package com.meishe.myvideo.view.editview;

import android.content.Context;
import android.widget.SeekBar;
import android.widget.TextView;
import com.meishe.myvideo.bean.EditData;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideoapp.R;

public class EditChangeOpacityView extends EditChangeVolumeView {
    public EditChangeOpacityView(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView, com.meishe.myvideo.view.editview.EditChangeVolumeView
    public void initData() {
        EditData.Opacity opacity = EditData.getInstance().getOpacity();
        this.mSeekBar.setProgress(opacity.mMaxValue);
        this.mSeekBar.setMax(opacity.mMaxValue);
        this.mSeekBarHint.setVisibility(0);
        this.mSeekBarHint.setText(getResources().getText(R.string.title_edit_not_opacity));
        TextView textView = this.mEndText;
        textView.setText(opacity.mMaxValue + "");
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView, com.meishe.myvideo.view.editview.EditChangeVolumeView
    public void initListener() {
        super.initListener();
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeOpacityView.AnonymousClass1 */

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = EditChangeOpacityView.this.mEndText;
                textView.setText(seekBar.getProgress() + "");
                MessageEvent.sendEvent(((float) seekBar.getProgress()) * 1.0f, 1025);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                MessageEvent.sendEvent(MessageEvent.MESSAGE_TYPE_SAVE_OPERATION);
            }
        });
    }

    public void setCurProgress(float f) {
        this.mSeekBar.setProgress((int) f);
    }
}
