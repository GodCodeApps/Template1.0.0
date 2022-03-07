package com.meishe.engine.data;

import com.meicam.sdk.NvsObject;

public interface IDraft<T extends NvsObject> {
    Object fromJson(String str);

    String getMapKey(String str);

    T getNvsObject();

    void loadData(T t);

    void setNvsObject(T t);

    String toJson();
}
