package com.meishe.engine.bean;

import android.annotation.SuppressLint;
import java.io.Serializable;

public class MeicamFxParam<T> implements Cloneable, Serializable {
    private static final String TAG = "MeicamFxParam";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_OBJECT = "Object";
    public static final String TYPE_STRING = "String";
    String key;
    String type;
    T value;

    @SuppressLint({"NewApi"})
    public MeicamFxParam(String str, String str2, T t) {
        this.key = str2;
        this.value = t;
        this.type = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T t) {
        this.value = t;
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
