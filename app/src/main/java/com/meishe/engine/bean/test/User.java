package com.meishe.engine.bean.test;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;

public class User implements Cloneable {
    @Expose
    private Integer age;
    @Expose(deserialize = true, serialize = false)
    private Integer id;
    @Expose(deserialize = false, serialize = true)
    private String name;
    @Expose(deserialize = true, serialize = true)
    private Persion ppp;

    /* access modifiers changed from: protected */
    @Override // java.lang.Object
    @NonNull
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer num) {
        this.age = num;
    }

    public Persion getPpp() {
        return this.ppp;
    }

    public void setPpp(Persion persion) {
        this.ppp = persion;
    }

    public String toString() {
        return "User [id=" + this.id + ", name=" + this.name + ", age=" + this.age + " , ppp= " + this.ppp.toString() + "]";
    }
}
