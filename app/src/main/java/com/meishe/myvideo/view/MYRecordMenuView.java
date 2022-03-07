package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meishe.myvideoapp.R;

public class MYRecordMenuView extends RelativeLayout {
    private Context mContext;
    private ImageView mImageRecord;
    private ImageView mIvRecordConfirm;
    private OnRecordListener mOnRecordListener;
    private boolean mStartRecord = false;
    private TextView mTvRecordContent;

    public interface OnRecordListener {
        void onConfirm();

        void onStartRecord();

        void onStopRecord();
    }

    public MYRecordMenuView(Context context) {
        super(context);
        init(context);
    }

    public MYRecordMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MYRecordMenuView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.record_view, this);
        this.mImageRecord = (ImageView) inflate.findViewById(R.id.cb_record);
        this.mTvRecordContent = (TextView) inflate.findViewById(R.id.tv_start_record);
        this.mIvRecordConfirm = (ImageView) inflate.findViewById(R.id.iv_record_confirm);
        initListener();
    }

    private void initListener() {
        this.mImageRecord.setOnLongClickListener(new View.OnLongClickListener() {
            /* class com.meishe.myvideo.view.MYRecordMenuView.AnonymousClass1 */

            public boolean onLongClick(View view) {
                MYRecordMenuView.this.mStartRecord = true;
                MYRecordMenuView.this.mImageRecord.setSelected(true);
                MYRecordMenuView.this.mTvRecordContent.setText(MYRecordMenuView.this.mContext.getString(R.string.voice_recording));
                if (MYRecordMenuView.this.mOnRecordListener == null) {
                    return false;
                }
                MYRecordMenuView.this.mOnRecordListener.onStartRecord();
                return false;
            }
        });
        this.mImageRecord.setOnTouchListener(new View.OnTouchListener() {
            /* class com.meishe.myvideo.view.MYRecordMenuView.AnonymousClass2 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    MYRecordMenuView.this.mImageRecord.setSelected(false);
                    MYRecordMenuView.this.mTvRecordContent.setText(MYRecordMenuView.this.mContext.getText(R.string.start_record));
                    if (MYRecordMenuView.this.mOnRecordListener != null) {
                        MYRecordMenuView.this.mOnRecordListener.onStopRecord();
                    }
                }
                return false;
            }
        });
        this.mIvRecordConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYRecordMenuView.AnonymousClass3 */

            public void onClick(View view) {
                if (MYRecordMenuView.this.mOnRecordListener != null) {
                    MYRecordMenuView.this.mOnRecordListener.onConfirm();
                }
            }
        });
    }

    public boolean isStartRecord() {
        return this.mStartRecord;
    }

    public void setStartRecord(boolean z) {
        this.mStartRecord = z;
    }

    public void show() {
        setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_enter));
        setVisibility(0);
    }

    public void hide() {
        setVisibility(8);
        ImageView imageView = this.mImageRecord;
        if (imageView != null) {
            imageView.setSelected(false);
        }
    }

    public void stopRecord() {
        OnRecordListener onRecordListener = this.mOnRecordListener;
        if (onRecordListener != null) {
            onRecordListener.onStopRecord();
        }
        ImageView imageView = this.mImageRecord;
        if (imageView != null) {
            imageView.setSelected(false);
        }
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.mOnRecordListener = onRecordListener;
    }
}
