package com.meishe.myvideo.view.editview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.meishe.myvideoapp.R;

public abstract class EditView<T> extends RelativeLayout {
    protected Context mContext;

    public interface CallBack<T> {
        void onCallBack(T t);

        void onCallBack(T t, T t2);
    }

    /* access modifiers changed from: protected */
    public abstract void initData();

    /* access modifiers changed from: protected */
    public void initListener() {
    }

    /* access modifiers changed from: protected */
    public abstract void initView();

    public abstract void setCallBackListener(CallBack<T> callBack);

    public abstract void updateData(T t);

    public EditView(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public EditView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initView();
        initData();
        initListener();
    }

    public void dismiss() {
        setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_exit));
        setVisibility(8);
    }
}
