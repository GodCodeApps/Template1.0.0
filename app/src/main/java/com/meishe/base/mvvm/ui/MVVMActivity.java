package com.meishe.base.mvvm.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.meishe.base.mvvm.ui.MVVMViewModel;

public abstract class MVVMActivity<VM extends MVVMViewModel> extends FragmentActivity {
    protected VM mViewModel;

    public abstract VM initViewModel();

    /* access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mViewModel = initViewModel();
    }
}
