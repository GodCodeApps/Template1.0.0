package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.meishe.myvideoapp.R;

public class RatioView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private OnRatioListener mOnRatioListener;
    private RelativeLayout rl_16_9;
    private RelativeLayout rl_1_1;
    private RelativeLayout rl_3_4;
    private RelativeLayout rl_4_3;
    private RelativeLayout rl_9_16;
    private RelativeLayout rl_original;

    public interface OnRatioListener {
        void onRatioClick(int i);
    }

    public RatioView(Context context) {
        super(context);
        init(context);
    }

    public RatioView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public RatioView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_ratio, this);
        this.rl_original = (RelativeLayout) inflate.findViewById(R.id.rl_original);
        this.rl_1_1 = (RelativeLayout) inflate.findViewById(R.id.rl_1_1);
        this.rl_3_4 = (RelativeLayout) inflate.findViewById(R.id.rl_3_4);
        this.rl_4_3 = (RelativeLayout) inflate.findViewById(R.id.rl_4_3);
        this.rl_9_16 = (RelativeLayout) inflate.findViewById(R.id.rl_9_16);
        this.rl_16_9 = (RelativeLayout) inflate.findViewById(R.id.rl_16_9);
        this.rl_original.setOnClickListener(this);
        this.rl_1_1.setOnClickListener(this);
        this.rl_3_4.setOnClickListener(this);
        this.rl_4_3.setOnClickListener(this);
        this.rl_9_16.setOnClickListener(this);
        this.rl_16_9.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.rl_original) {
            if (id == R.id.rl_16_9) { /*{ENCODED_INT: 2131296676}*/
                if (this.mOnRatioListener != null) {
                    this.rl_original.setSelected(false);
                    this.rl_1_1.setSelected(false);
                    this.rl_3_4.setSelected(false);
                    this.rl_4_3.setSelected(false);
                    this.rl_9_16.setSelected(false);
                    this.rl_16_9.setSelected(true);
                    this.mOnRatioListener.onRatioClick(1);
                    return;
                }
                return;
            } else if (id == R.id.rl_1_1) { /*{ENCODED_INT: 2131296677}*/
                if (this.mOnRatioListener != null) {
                    this.rl_original.setSelected(false);
                    this.rl_1_1.setSelected(true);
                    this.rl_3_4.setSelected(false);
                    this.rl_4_3.setSelected(false);
                    this.rl_9_16.setSelected(false);
                    this.rl_16_9.setSelected(false);
                    this.mOnRatioListener.onRatioClick(2);
                    return;
                }
                return;
            } else if (id == R.id.rl_3_4) { /*{ENCODED_INT: 2131296678}*/
                if (this.mOnRatioListener != null) {
                    this.rl_original.setSelected(false);
                    this.rl_1_1.setSelected(false);
                    this.rl_3_4.setSelected(true);
                    this.rl_4_3.setSelected(false);
                    this.rl_9_16.setSelected(false);
                    this.rl_16_9.setSelected(false);
                    this.mOnRatioListener.onRatioClick(16);
                    return;
                }
                return;
            } else if (id == R.id.rl_4_3) { /*{ENCODED_INT: 2131296679}*/
                if (this.mOnRatioListener != null) {
                    this.rl_original.setSelected(false);
                    this.rl_1_1.setSelected(false);
                    this.rl_3_4.setSelected(false);
                    this.rl_4_3.setSelected(true);
                    this.rl_9_16.setSelected(false);
                    this.rl_16_9.setSelected(false);
                    this.mOnRatioListener.onRatioClick(8);
                    return;
                }
                return;
            } else if (id == R.id.rl_9_16) { /*{ENCODED_INT: 2131296680}*/
                if (this.mOnRatioListener != null) {
                    this.rl_original.setSelected(false);
                    this.rl_1_1.setSelected(false);
                    this.rl_3_4.setSelected(false);
                    this.rl_4_3.setSelected(false);
                    this.rl_9_16.setSelected(true);
                    this.rl_16_9.setSelected(false);
                    this.mOnRatioListener.onRatioClick(4);
                    return;
                }
                return;
            }
            return;
        } else if (this.mOnRatioListener != null) {
            this.rl_original.setSelected(true);
            this.rl_1_1.setSelected(false);
            this.rl_3_4.setSelected(false);
            this.rl_4_3.setSelected(false);
            this.rl_9_16.setSelected(false);
            this.rl_16_9.setSelected(false);
            this.mOnRatioListener.onRatioClick(0);
        }
    }

    public void upDateRatio(int i) {
        if (i == 0) {
            this.rl_original.setSelected(true);
            this.rl_1_1.setSelected(false);
            this.rl_3_4.setSelected(false);
            this.rl_4_3.setSelected(false);
            this.rl_9_16.setSelected(false);
            this.rl_16_9.setSelected(false);
        } else if (i == 1) {
            this.rl_original.setSelected(false);
            this.rl_1_1.setSelected(false);
            this.rl_3_4.setSelected(false);
            this.rl_4_3.setSelected(false);
            this.rl_9_16.setSelected(false);
            this.rl_16_9.setSelected(true);
        } else if (i == 2) {
            this.rl_original.setSelected(false);
            this.rl_1_1.setSelected(true);
            this.rl_3_4.setSelected(false);
            this.rl_4_3.setSelected(false);
            this.rl_9_16.setSelected(false);
            this.rl_16_9.setSelected(false);
        } else if (i == 4) {
            this.rl_original.setSelected(false);
            this.rl_1_1.setSelected(false);
            this.rl_3_4.setSelected(false);
            this.rl_4_3.setSelected(false);
            this.rl_9_16.setSelected(true);
            this.rl_16_9.setSelected(false);
        } else if (i == 8) {
            this.rl_original.setSelected(false);
            this.rl_1_1.setSelected(false);
            this.rl_3_4.setSelected(false);
            this.rl_4_3.setSelected(true);
            this.rl_9_16.setSelected(false);
            this.rl_16_9.setSelected(false);
        } else if (i == 16) {
            this.rl_original.setSelected(false);
            this.rl_1_1.setSelected(false);
            this.rl_3_4.setSelected(true);
            this.rl_4_3.setSelected(false);
            this.rl_9_16.setSelected(false);
            this.rl_16_9.setSelected(false);
        }
    }

    public void setOnRatioListener(OnRatioListener onRatioListener) {
        this.mOnRatioListener = onRatioListener;
    }
}
