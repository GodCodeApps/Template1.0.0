package com.meishe.myvideo.view.editview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import androidx.core.internal.view.SupportMenu;
import com.meishe.myvideo.view.editview.EditView;
import com.meishe.myvideoapp.R;

public class BottomControlView extends EditControlView {
    private static final String TAG = "BottomControlView";

    @Override // com.meishe.myvideo.view.editview.EditControlView
    public void execute(String str, String str2) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditControlView
    public <T> View getView(String str, EditView.CallBack<T> callBack) {
        return null;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.view.editview.EditControlView
    public void initView() {
    }

    @Override // com.meishe.myvideo.view.editview.EditControlView
    public <T> void show(String str, EditView.CallBack<T> callBack) {
    }

    public BottomControlView(Context context) {
        super(context);
        setBackgroundColor(SupportMenu.CATEGORY_MASK);
    }

    public BottomControlView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.meishe.myvideo.view.editview.EditControlView
    public void dismiss() {
        setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_exit));
        setVisibility(8);
    }

    private Class<?> parseClass(Object obj) {
        if (obj instanceof Float) {
            return Float.TYPE;
        }
        if (obj instanceof Integer) {
            return Integer.TYPE;
        }
        if (obj instanceof Boolean) {
            return Boolean.TYPE;
        }
        if (obj instanceof Long) {
            return Long.TYPE;
        }
        return obj.getClass();
    }

    public void showDialogView(View view) {
        removeAllViews();
        addView(view);
        setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_enter));
        setVisibility(0);
    }
}
