package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.meishe.myvideo.interfaces.OnTitleBarClickListener;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideoapp.R;

public class CustomTitleBar extends LinearLayout implements View.OnClickListener {
    private RelativeLayout backLayout;
    private ImageView backLayoutImageView;
    private boolean finishActivity = true;
    private RelativeLayout forwardLayout;
    private RelativeLayout mainLayout;
    OnTitleBarClickListener onTitleBarClickListener;
    TextView textCenter;
    TextView textRight;

    public CustomTitleBar(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_titlebar, (ViewGroup) null);
        addView(inflate);
        this.mainLayout = (RelativeLayout) inflate.findViewById(R.id.main_layout);
        this.backLayout = (RelativeLayout) inflate.findViewById(R.id.backLayout);
        this.backLayout.setOnClickListener(this);
        this.backLayoutImageView = (ImageView) inflate.findViewById(R.id.back_layout_imageView);
        this.textCenter = (TextView) inflate.findViewById(R.id.text_center);
        this.textCenter.setTextSize(0, getResources().getDimension(R.dimen.title_textSize));
        this.textCenter.getPaint().setFakeBoldText(true);
        this.textCenter.setOnClickListener(this);
        this.textRight = (TextView) inflate.findViewById(R.id.text_right);
        this.textRight.setTextSize(0, getResources().getDimension(R.dimen.title_textSize));
        this.textRight.getPaint().setFakeBoldText(true);
        this.forwardLayout = (RelativeLayout) inflate.findViewById(R.id.forwardLayout);
        this.forwardLayout.setOnClickListener(this);
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public void setFinishActivity(boolean z) {
        this.finishActivity = z;
    }

    public void setTextCenterColor(int i) {
        this.textCenter.setTextColor(i);
    }

    public void setTextCenter(String str) {
        this.textCenter.setText(str);
    }

    public void setTextCenter(@StringRes int i) {
        this.textCenter.setText(getContext().getResources().getText(i));
    }

    public void setTextRight(String str) {
        this.textRight.setText(str);
    }

    public void setTextRight(@StringRes int i) {
        this.textRight.setText(getContext().getResources().getText(i));
    }

    public void setTextRightVisible(int i) {
        this.textRight.setVisibility(i);
        if (i == 0) {
            this.forwardLayout.setClickable(true);
        } else {
            this.forwardLayout.setClickable(false);
        }
    }

    public void setBackImageVisible(int i) {
        this.backLayout.setVisibility(i);
    }

    public void setBackImageIcon(int i) {
        this.backLayoutImageView.setImageResource(i);
    }

    public void setMainLayoutColor(int i) {
        this.mainLayout.setBackgroundColor(i);
    }

    public void setMainLayoutResource(int i) {
        this.mainLayout.setBackgroundResource(i);
    }

    public OnTitleBarClickListener getOnTitleBarClickListener() {
        return this.onTitleBarClickListener;
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener2) {
        this.onTitleBarClickListener = onTitleBarClickListener2;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    public void onClick(View view) {
        OnTitleBarClickListener onTitleBarClickListener2;
        int id = view.getId();
        if (id == R.id.backLayout) {
            OnTitleBarClickListener onTitleBarClickListener3 = this.onTitleBarClickListener;
            if (onTitleBarClickListener3 != null) {
                onTitleBarClickListener3.OnBackImageClick();
            }
            if (this.finishActivity) {
                AppManager.getInstance().finishActivity();
            }
        } else if (id == R.id.forwardLayout) {
            OnTitleBarClickListener onTitleBarClickListener4 = this.onTitleBarClickListener;
            if (onTitleBarClickListener4 != null) {
                onTitleBarClickListener4.OnRightTextClick();
            }
        } else if (id == R.id.text_center && (onTitleBarClickListener2 = this.onTitleBarClickListener) != null) {
            onTitleBarClickListener2.OnCenterTextClick();
        }
    }
}
