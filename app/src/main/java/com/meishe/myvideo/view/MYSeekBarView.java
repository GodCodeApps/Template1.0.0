package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.meishe.myvideoapp.R;

public class MYSeekBarView extends RelativeLayout {
    private Context mContext;
    private TextView mCurrentText;
    private boolean mIsEndTextFixed = false;
    private OnSeekBarListener mListener;
    private String mName;
    public SeekBar mSeekBar;
    private TextView mStartText;
    private float mTotalValue = 20.0f;

    public interface OnSeekBarListener {
        void onProgressChanged(SeekBar seekBar, int i, boolean z);

        void onStopTrackingTouch(int i, String str);
    }

    public float getMinProgress() {
        return 0.0f;
    }

    public MYSeekBarView(Context context) {
        super(context);
        initView(context);
    }

    public MYSeekBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public MYSeekBarView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_edit_change_apply_time, this);
        this.mSeekBar = (SeekBar) inflate.findViewById(R.id.seek_bar);
        this.mStartText = (TextView) inflate.findViewById(R.id.tv_start_text);
        this.mCurrentText = (TextView) inflate.findViewById(R.id.tv_current_text);
        initListener();
    }

    public float getMaxProgress() {
        return (float) this.mSeekBar.getMax();
    }

    public void setTotalValue(float f) {
        this.mTotalValue = f;
    }

    public void setInitData(int i, int i2, boolean z) {
        this.mIsEndTextFixed = z;
        SeekBar seekBar = this.mSeekBar;
        if (seekBar != null) {
            seekBar.setMax(i);
            this.mSeekBar.setProgress(i2);
        }
    }

    private void initListener() {
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.view.MYSeekBarView.AnonymousClass1 */

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (!MYSeekBarView.this.mIsEndTextFixed && MYSeekBarView.this.mCurrentText != null) {
                    MYSeekBarView.this.mCurrentText.setText(String.format(MYSeekBarView.this.mContext.getResources().getString(R.string.string_format_one_point), Float.valueOf(((MYSeekBarView.this.mTotalValue * ((float) seekBar.getProgress())) / 100.0f) - 10.0f)));
                }
                if (MYSeekBarView.this.mListener != null) {
                    MYSeekBarView.this.mListener.onProgressChanged(seekBar, i, z);
                }
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (MYSeekBarView.this.mListener != null) {
                    MYSeekBarView.this.mListener.onStopTrackingTouch(seekBar.getProgress(), MYSeekBarView.this.mName);
                }
                if (!MYSeekBarView.this.mIsEndTextFixed && MYSeekBarView.this.mCurrentText != null) {
                    MYSeekBarView.this.mCurrentText.setText(String.format(MYSeekBarView.this.mContext.getResources().getString(R.string.string_format_one_point), Float.valueOf(((MYSeekBarView.this.mTotalValue * ((float) seekBar.getProgress())) / 100.0f) - 10.0f)));
                }
            }
        });
    }

    public void setListener(OnSeekBarListener onSeekBarListener) {
        this.mListener = onSeekBarListener;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public void setSeekProgress(int i) {
        SeekBar seekBar = this.mSeekBar;
        if (seekBar != null) {
            seekBar.setProgress(i);
        }
    }

    public void setStartTextVisible(boolean z) {
        this.mStartText.setVisibility(z ? 0 : 8);
    }

    public void setEndTextVisible(boolean z) {
        this.mCurrentText.setVisibility(z ? 0 : 8);
    }

    public void setStartValueAndCurrentValue(float f, float f2) {
        TextView textView = this.mStartText;
        if (textView != null) {
            textView.setText(f + "");
        }
        TextView textView2 = this.mCurrentText;
        if (textView2 != null) {
            textView2.setText(f2 + "");
        }
    }
}
