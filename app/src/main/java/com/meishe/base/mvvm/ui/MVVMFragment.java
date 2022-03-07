package com.meishe.base.mvvm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.meishe.base.inter.MYCallback;
import com.meishe.base.mvvm.ui.MVVMViewModel;

public abstract class MVVMFragment<VM extends MVVMViewModel> extends Fragment implements MYCallback {
    private VM chatViewModel;
    private Activity mActivity;
    private View mRootView;

    /* access modifiers changed from: protected */
    public abstract VM initViewModel();

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.mActivity = getActivity();
        this.mRootView = layoutInflater.inflate(bindLayout(), viewGroup, false);
        this.chatViewModel = initViewModel();
        initView(this.mRootView);
        initData();
        getBundleExtras(getArguments());
        return this.mRootView;
    }

    public <V extends View> V getViewById(@IdRes int i) {
        return (V) this.mRootView.findViewById(i);
    }
}
