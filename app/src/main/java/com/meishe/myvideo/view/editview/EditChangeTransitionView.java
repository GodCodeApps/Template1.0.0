package com.meishe.myvideo.view.editview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.view.editview.EditView;
import com.meishe.myvideoapp.R;

public class EditChangeTransitionView extends EditView<Float> {
    public static final int maxProgress = 10;
    public TextView mEndTextFadeIn;
    public TextView mEndTextFadeOut;
    private int mFristProgress;
    private ImageView mIvConfirm;
    private int mSecondProgress;
    public SeekBar mSeekBarFadeIn;
    public SeekBar mSeekBarFadeOut;
    private TextView mTvContent;

    @Override // com.meishe.myvideo.view.editview.EditView
    public void setCallBackListener(EditView.CallBack<Float> callBack) {
    }

    public void updateData(Float f) {
    }

    public EditChangeTransitionView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.view_edit_chang_transtion, this);
        this.mSeekBarFadeIn = (SeekBar) inflate.findViewById(R.id.seek_bar_first);
        this.mSeekBarFadeOut = (SeekBar) inflate.findViewById(R.id.seek_bar_second);
        this.mEndTextFadeIn = (TextView) inflate.findViewById(R.id.end_text_first);
        this.mEndTextFadeOut = (TextView) inflate.findViewById(R.id.end_text_second);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mTvContent = (TextView) inflate.findViewById(R.id.tv_content);
    }

    public void setSeekBarMax(int i) {
        this.mSeekBarFadeIn.setMax(i);
        this.mSeekBarFadeOut.setMax(i);
    }

    public void setProgress(long j, long j2) {
        int i = (int) (j / 1000000);
        int i2 = (int) (j2 / 1000000);
        this.mFristProgress = i;
        this.mSecondProgress = i2;
        this.mSeekBarFadeIn.setProgress(i);
        this.mSeekBarFadeOut.setProgress(i2);
        if (i == 0) {
            this.mEndTextFadeIn.setText("10");
        } else {
            TextView textView = this.mEndTextFadeIn;
            textView.setText(i + "");
        }
        if (i2 == 0) {
            this.mEndTextFadeOut.setText("10");
            return;
        }
        TextView textView2 = this.mEndTextFadeOut;
        textView2.setText(i2 + "");
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initData() {
        this.mTvContent.setText(getResources().getString(R.string.sub_menu_audio_transition_all));
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initListener() {
        super.initListener();
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeTransitionView.AnonymousClass1 */

            public void onClick(View view) {
                EditChangeTransitionView.this.dismiss();
            }
        });
        this.mSeekBarFadeIn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeTransitionView.AnonymousClass2 */

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                EditChangeTransitionView.this.mFristProgress = seekBar.getProgress();
                TextView textView = EditChangeTransitionView.this.mEndTextFadeIn;
                textView.setText(EditChangeTransitionView.this.mFristProgress + "");
                MessageEvent.sendEvent(EditChangeTransitionView.this.mFristProgress + "-" + EditChangeTransitionView.this.mSecondProgress, (int) MessageEvent.MESSAGE_TYPE_AUDIO_TRANSTION);
            }
        });
        this.mSeekBarFadeOut.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeTransitionView.AnonymousClass3 */

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                EditChangeTransitionView.this.mSecondProgress = seekBar.getProgress();
                TextView textView = EditChangeTransitionView.this.mEndTextFadeOut;
                textView.setText(EditChangeTransitionView.this.mSecondProgress + "");
                MessageEvent.sendEvent(EditChangeTransitionView.this.mFristProgress + "-" + EditChangeTransitionView.this.mSecondProgress, (int) MessageEvent.MESSAGE_TYPE_AUDIO_TRANSTION);
            }
        });
    }
}
