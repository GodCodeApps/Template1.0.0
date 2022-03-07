package com.meishe.engine.bean.test;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;

public class Persion {
    @Expose
    private boolean abcd = false;

    public Persion(boolean z) {
        this.abcd = z;
    }

    @NonNull
    public String toString() {
        return "Persion [abcd=" + this.abcd + "]";
    }
}
