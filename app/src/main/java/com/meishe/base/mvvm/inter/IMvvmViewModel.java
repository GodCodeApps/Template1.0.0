package com.meishe.base.mvvm.inter;

import com.meishe.base.mvvm.inter.IMvvmModel;

public interface IMvvmViewModel<M extends IMvvmModel> {
    void init(M m);
}
