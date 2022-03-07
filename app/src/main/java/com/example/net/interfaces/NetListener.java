package com.example.net.interfaces;

public interface NetListener<T> {
    void onFaile(String str);

    void onSuccess(T t);
}
