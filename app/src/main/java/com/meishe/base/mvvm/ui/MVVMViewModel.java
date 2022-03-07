package com.meishe.base.mvvm.ui;

import androidx.lifecycle.ViewModel;
import com.meishe.base.mvvm.inter.IMvvmModel;
import com.meishe.base.mvvm.inter.IMvvmViewModel;

public class MVVMViewModel<M extends IMvvmModel> extends ViewModel implements IMvvmViewModel<M> {
    protected M model;

    @Override // com.meishe.base.mvvm.inter.IMvvmViewModel
    public void init(M m) {
        this.model = m;
    }
}
