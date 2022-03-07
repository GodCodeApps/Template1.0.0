package com.meishe.engine.bean;

import java.io.Serializable;

public class NvsObject<T> implements Cloneable, Serializable {
    private transient T mObject;

    public void loadData(T t) {
    }

    public NvsObject() {
    }

    public NvsObject(T t) {
        this.mObject = t;
    }

    public T getObject() {
        return this.mObject;
    }

    public void setObject(T t) {
        this.mObject = t;
    }
}
