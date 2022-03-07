package com.meishe.base.inter;

public interface CallBackListener<T> {
    void onComplete(T t);

    void onError(Throwable th);
}
