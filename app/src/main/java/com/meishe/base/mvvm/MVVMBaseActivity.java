package com.meishe.base.mvvm;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.meishe.base.inter.MYCallback;
import com.meishe.base.mvvm.ui.MVVMActivity;
import com.meishe.base.mvvm.ui.MVVMViewModel;
import com.uc.crashsdk.export.LogType;

public abstract class MVVMBaseActivity<VM extends MVVMViewModel> extends MVVMActivity<VM> implements MYCallback {
    protected FragmentActivity mActivity;

    /* access modifiers changed from: protected */
    public boolean keyboardSetAJust() {
        return true;
    }

    /* access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, androidx.fragment.app.FragmentActivity, com.meishe.base.mvvm.ui.MVVMActivity
    public void onCreate(@Nullable Bundle bundle) {
        setContentView(bindLayout());
        super.onCreate(bundle);
        this.mActivity = this;
        if (keyboardSetAJust()) {
            getWindow().setSoftInputMode(32);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(LogType.UNEXP_ANR);
        }
        setContentView(bindLayout());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getBundleExtras(extras);
        }
        initView(null);
        initData();
    }

    public <T extends View> T getViewById(@IdRes int i) {
        return (T) findViewById(i);
    }
}
