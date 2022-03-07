package com.meishe.effect;

public enum EGLError {
    OK(0, "ok"),
    ConfigErr(101, "config not support");
    
    private int a;
    private String b;

    private EGLError(int i, String str) {
        this.a = i;
        this.b = str;
    }

    public final String toString() {
        return this.b;
    }

    public final int value() {
        return this.a;
    }
}
