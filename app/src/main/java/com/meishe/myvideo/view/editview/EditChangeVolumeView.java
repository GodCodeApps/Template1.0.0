package com.meishe.myvideo.view.editview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.meishe.engine.bean.CommonData;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.view.editview.EditView;
import com.meishe.myvideoapp.R;

public class EditChangeVolumeView extends EditView<Float> {
    public static final int maxProgress = 200;
    public TextView mEndText;
    private ImageView mIvConfirm;
    private RelativeLayout mRlConfirmRoot;
    private RelativeLayout mRlSeekContainer;
    public SeekBar mSeekBar;
    public TextView mSeekBarHint;
    private TextView mTvContent;
    private TextView mTvStartText;
    private String mVideoType;
    private int progress = 0;
    private float range = 8.0f;

    @Override // com.meishe.myvideo.view.editview.EditView
    public void setCallBackListener(EditView.CallBack<Float> callBack) {
    }

    public void updateData(Float f) {
    }

    public EditChangeVolumeView(Context context, String str) {
        super(context);
        this.mVideoType = str;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.view_edit_chang_volume, this);
        this.mSeekBar = (SeekBar) inflate.findViewById(R.id.seek_bar);
        this.mSeekBarHint = (TextView) inflate.findViewById(R.id.tv_content);
        this.mEndText = (TextView) inflate.findViewById(R.id.end_text);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mRlConfirmRoot = (RelativeLayout) inflate.findViewById(R.id.rl_confirm_root);
        this.mRlSeekContainer = (RelativeLayout) inflate.findViewById(R.id.rl_seek_container);
        this.mTvContent = (TextView) inflate.findViewById(R.id.tv_content);
        this.mTvStartText = (TextView) inflate.findViewById(R.id.start_text);
    }

    public void setSeekBarMax(int i) {
        this.mSeekBar.setMax(i);
    }

    public void setProgress(float f) {
        this.progress = (int) ((f * 200.0f) / this.range);
        this.mSeekBar.setProgress(this.progress);
        this.mEndText.setText("200");
        TextView textView = this.mTvStartText;
        textView.setText(this.progress + "");
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initData() {
        this.mTvContent.setText(getResources().getString(R.string.sub_menu_audio_edit_volume));
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditView
    public void initListener() {
        super.initListener();
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeVolumeView.AnonymousClass1 */

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = EditChangeVolumeView.this.mTvStartText;
                textView.setText(seekBar.getProgress() + "");
                float progress = (((float) seekBar.getProgress()) / 200.0f) * EditChangeVolumeView.this.range;
                if (EditChangeVolumeView.this.mVideoType.equals(CommonData.CLIP_VIDEO)) {
                    MessageEvent.sendEvent(progress, (int) MessageEvent.MESSAGE_TYPE_VIDEO_VOLUME);
                } else if (EditChangeVolumeView.this.mVideoType.equals(CommonData.CLIP_AUDIO)) {
                    MessageEvent.sendEvent(progress, (int) MessageEvent.MESSAGE_TYPE_AUDIO_VOLUME);
                }
            }
        });
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeVolumeView.AnonymousClass2 */

            public void onClick(View view) {
                EditChangeVolumeView.this.dismiss();
            }
        });
        this.mRlConfirmRoot.setOnTouchListener(new View.OnTouchListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeVolumeView.AnonymousClass3 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        this.mRlSeekContainer.setOnTouchListener(new View.OnTouchListener() {
            /* class com.meishe.myvideo.view.editview.EditChangeVolumeView.AnonymousClass4 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }
}
