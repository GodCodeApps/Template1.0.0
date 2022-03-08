package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.myvideo.interfaces.OnMiddleOperationClickListener;
import com.meishe.myvideoapp.R;

public class MYMiddleOperationView extends ConstraintLayout implements View.OnClickListener {
    private ImageView ivCancel;
    private ImageView ivRecover;
    private ImageView mIvOperationZoom;
    private ImageView mPlayBtn;
    private TextView mTimeRatioText;
    private OnMiddleOperationClickListener onMiddleOperationClickListener;

    public MYMiddleOperationView(Context context) {
        super(context);
        initView(context);
    }

    public MYMiddleOperationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public MYMiddleOperationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_middle_operation, (ViewGroup) this, false);
        addView(inflate);
        this.mTimeRatioText = (TextView) inflate.findViewById(R.id.tv_operate_time);
        this.mPlayBtn = (ImageView) inflate.findViewById(R.id.iv_operation_play);
        this.ivRecover = (ImageView) inflate.findViewById(R.id.iv_operation_recover);
        this.ivCancel = (ImageView) inflate.findViewById(R.id.iv_operation_cancel);
        this.mIvOperationZoom = (ImageView) inflate.findViewById(R.id.iv_operation_zoom);
        this.mPlayBtn.setOnClickListener(this);
        this.ivRecover.setOnClickListener(this);
        this.ivCancel.setOnClickListener(this);
        this.mIvOperationZoom.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (this.onMiddleOperationClickListener != null) {
            int id = view.getId();
            if (id == R.id.iv_operation_cancel) { /*{ENCODED_INT: 2131296525}*/
                this.onMiddleOperationClickListener.onCancelEventCallback();
                return;
            } else if (id == R.id.iv_operation_play) { /*{ENCODED_INT: 2131296526}*/
                if (NvsStreamingContext.getInstance().getStreamingEngineState() == 3) {
                    this.onMiddleOperationClickListener.onPlayEventCallback(true);
                    return;
                } else {
                    this.onMiddleOperationClickListener.onPlayEventCallback(false);
                    return;
                }
            } else if (id == R.id.iv_operation_recover) { /*{ENCODED_INT: 2131296527}*/
                this.onMiddleOperationClickListener.onRecoverEventCallback();
                return;
            } else if (id == R.id.iv_operation_zoom) { /*{ENCODED_INT: 2131296528}*/
                this.onMiddleOperationClickListener.onZoomEventCallback();
                return;
            }
            return;
        }
    }

    public void setOnMiddleOperationClickListener(OnMiddleOperationClickListener onMiddleOperationClickListener2) {
        this.onMiddleOperationClickListener = onMiddleOperationClickListener2;
    }

    public void setDurationText(String str) {
        TextView textView = this.mTimeRatioText;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void updateViewState(boolean z) {
        if (z) {
            this.mPlayBtn.setImageResource(R.mipmap.control_bar_ic_pause);
        } else {
            this.mPlayBtn.setImageResource(R.mipmap.control_bar_ic_play);
        }
    }

    public void updateRecoverState(boolean z) {
        if (z) {
            this.ivRecover.setImageResource(R.mipmap.ic_operate_recover);
        } else {
            this.ivRecover.setImageResource(R.mipmap.ic_operate_recover_enable);
        }
    }

    public void updateCancelState(boolean z) {
        if (z) {
            this.ivCancel.setImageResource(R.mipmap.ic_operate_cancel);
        } else {
            this.ivCancel.setImageResource(R.mipmap.ic_operate_cancel_enable);
        }
    }
}
