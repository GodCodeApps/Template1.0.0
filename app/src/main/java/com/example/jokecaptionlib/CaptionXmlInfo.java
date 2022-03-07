package com.example.jokecaptionlib;

import java.util.ArrayList;

class CaptionXmlInfo {
    private String a;
    private float b;
    private float c;
    private int d;
    private String e;
    private String f;
    private String g;
    private ArrayList h;

    CaptionXmlInfo() {
    }

    /* access modifiers changed from: package-private */
    public final ArrayList a() {
        return this.h;
    }

    /* access modifiers changed from: package-private */
    public final void a(float f2) {
        this.b = f2;
    }

    /* access modifiers changed from: package-private */
    public final void a(int i) {
        this.d = i;
    }

    /* access modifiers changed from: package-private */
    public final void a(String str) {
        this.f = str;
    }

    /* access modifiers changed from: package-private */
    public final void a(ArrayList arrayList) {
        this.h = arrayList;
    }

    /* access modifiers changed from: package-private */
    public final String b() {
        return this.f;
    }

    /* access modifiers changed from: package-private */
    public final void b(float f2) {
        this.c = f2;
    }

    /* access modifiers changed from: package-private */
    public final void b(String str) {
        this.a = str;
    }

    /* access modifiers changed from: package-private */
    public final String c() {
        return this.a;
    }

    /* access modifiers changed from: package-private */
    public final void c(String str) {
        this.e = str;
    }

    /* access modifiers changed from: package-private */
    public final float d() {
        return this.b;
    }

    /* access modifiers changed from: package-private */
    public final float e() {
        return this.c;
    }

    /* access modifiers changed from: package-private */
    public final int f() {
        return this.d;
    }

    public String getFontFamily() {
        return this.g;
    }

    public void setFontFamily(String str) {
        this.g = str;
    }
}
