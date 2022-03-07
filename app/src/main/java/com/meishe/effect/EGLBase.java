package com.meishe.effect;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@TargetApi(18)
public class EGLBase {
    private EGLConfig a = null;
    private EGLContext b = EGL14.EGL_NO_CONTEXT;
    private EGLDisplay c = EGL14.EGL_NO_DISPLAY;

    public class EglSurface {
        private final EGLBase a;
        private EGLSurface b = EGL14.EGL_NO_SURFACE;
        private final int c;
        private final int d;

        EglSurface(EGLBase eGLBase, int i, int i2) {
            this.a = eGLBase;
            this.b = this.a.a((EGLBase) i, i2);
            this.c = i;
            this.d = i2;
        }

        EglSurface(EGLBase eGLBase, Object obj) {
            if ((obj instanceof SurfaceView) || (obj instanceof Surface) || (obj instanceof SurfaceHolder) || (obj instanceof SurfaceTexture)) {
                this.a = eGLBase;
                this.b = this.a.a((EGLBase) obj);
                this.c = this.a.querySurface(this.b, 12375);
                this.d = this.a.querySurface(this.b, 12374);
                return;
            }
            throw new IllegalArgumentException("unsupported surface");
        }

        public EGLContext getContext() {
            return this.a.getContext();
        }

        public int getHeight() {
            return this.d;
        }

        public int getWidth() {
            return this.c;
        }

        public void makeCurrent() {
            EGLBase.a(this.a, this.b);
        }

        public void presentTime(long j) {
            EGLBase.a(this.a, this.b, j);
        }

        public void release() {
            this.a.a();
            EGLBase.c(this.a, this.b);
            this.b = EGL14.EGL_NO_SURFACE;
        }

        public void swap() {
            EGLBase.b(this.a, this.b);
        }
    }

    public EGLBase(EGLContext eGLContext, boolean z, boolean z2) {
        if (this.c == EGL14.EGL_NO_DISPLAY) {
            this.c = EGL14.eglGetDisplay(0);
            if (this.c != EGL14.EGL_NO_DISPLAY) {
                int[] iArr = new int[2];
                if (EGL14.eglInitialize(this.c, iArr, 0, iArr, 1)) {
                    if (this.b == EGL14.EGL_NO_CONTEXT) {
                        this.a = a(z, z2);
                        if (this.a != null) {
                            this.b = eGLContext;
                        } else {
                            throw new RuntimeException("chooseConfig failed");
                        }
                    }
                    EGL14.eglQueryContext(this.c, this.b, 12440, new int[1], 0);
                    a();
                    return;
                }
                this.c = null;
                throw new RuntimeException("eglInitialize failed");
            }
            throw new RuntimeException("eglGetDisplay failed");
        }
        throw new RuntimeException("EGL already set up");
    }

    private EGLConfig a(boolean z, boolean z2) {
        int[] iArr = {12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344};
        int i = 10;
        if (z) {
            iArr[10] = 12325;
            i = 12;
            iArr[11] = 16;
        }
        if (z2 && Build.VERSION.SDK_INT >= 18) {
            int i2 = i + 1;
            iArr[i] = 12610;
            i = i2 + 1;
            iArr[i2] = 1;
        }
        for (int i3 = 16; i3 >= i; i3--) {
            iArr[i3] = 12344;
        }
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        if (EGL14.eglChooseConfig(this.c, iArr, 0, eGLConfigArr, 0, 1, new int[1], 0)) {
            return eGLConfigArr[0];
        }
        Log.w("EGLBase", "unable to find RGBA8888 /  EGLConfig");
        return null;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private EGLSurface a(int i, int i2) {
        int[] iArr = {12375, i, 12374, i2, 12344};
        EGLSurface eGLSurface = null;
        try {
            eGLSurface = EGL14.eglCreatePbufferSurface(this.c, this.a, iArr, 0);
            int eglGetError = EGL14.eglGetError();
            if (eglGetError != 12288) {
                throw new RuntimeException("eglCreatePbufferSurface" + ": EGL error: 0x" + Integer.toHexString(eglGetError));
            } else if (eGLSurface != null) {
                return eGLSurface;
            } else {
                throw new RuntimeException("surface was null");
            }
        } catch (IllegalArgumentException | RuntimeException e) {
            Log.e("EGLBase", "createOffscreenSurface", e);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private EGLSurface a(Object obj) {
        try {
            return EGL14.eglCreateWindowSurface(this.c, this.a, obj, new int[]{12344}, 0);
        } catch (IllegalArgumentException e) {
            Log.e("EGLBase", "eglCreateWindowSurface", e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void a() {
        EGLDisplay eGLDisplay = this.c;
        EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
        if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT)) {
            Log.w("TAG", "makeDefault" + EGL14.eglGetError());
        }
    }

    static /* synthetic */ void a(EGLBase eGLBase, EGLSurface eGLSurface, long j) {
        if (!EGLExt.eglPresentationTimeANDROID(eGLBase.c, eGLSurface, j)) {
            Log.e("EGLBase", "eglPresentationTimeANDROID() failed, errno=0x" + EGL14.eglGetError());
        }
    }

    static /* synthetic */ boolean a(EGLBase eGLBase, EGLSurface eGLSurface) {
        if (eGLSurface == null || eGLSurface == EGL14.EGL_NO_SURFACE) {
            if (EGL14.eglGetError() == 12299) {
                Log.e("EGLBase", "makeCurrent:returned EGL_BAD_NATIVE_WINDOW.");
            }
            return false;
        } else if (EGL14.eglMakeCurrent(eGLBase.c, eGLSurface, eGLSurface, eGLBase.b)) {
            return true;
        } else {
            Log.w("EGLBase", "eglMakeCurrent:" + EGL14.eglGetError());
            return false;
        }
    }

    static /* synthetic */ int b(EGLBase eGLBase, EGLSurface eGLSurface) {
        if (!EGL14.eglSwapBuffers(eGLBase.c, eGLSurface)) {
            return EGL14.eglGetError();
        }
        return 12288;
    }

    static /* synthetic */ void c(EGLBase eGLBase, EGLSurface eGLSurface) {
        if (eGLSurface != EGL14.EGL_NO_SURFACE) {
            EGLDisplay eGLDisplay = eGLBase.c;
            EGLSurface eGLSurface2 = EGL14.EGL_NO_SURFACE;
            EGL14.eglMakeCurrent(eGLDisplay, eGLSurface2, eGLSurface2, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(eGLBase.c, eGLSurface);
        }
        EGLSurface eGLSurface3 = EGL14.EGL_NO_SURFACE;
    }

    public EglSurface createFromSurface(Object obj) {
        EglSurface eglSurface = new EglSurface(this, obj);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    public EglSurface createOffscreen(int i, int i2) {
        EglSurface eglSurface = new EglSurface(this, i, i2);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    public EGLContext getContext() {
        return this.b;
    }

    public int querySurface(EGLSurface eGLSurface, int i) {
        int[] iArr = new int[1];
        EGL14.eglQuerySurface(this.c, eGLSurface, i, iArr, 0);
        return iArr[0];
    }

    public void release() {
        if (this.c != EGL14.EGL_NO_DISPLAY) {
            this.b = EGL14.EGL_NO_CONTEXT;
            EGL14.eglTerminate(this.c);
            EGL14.eglReleaseThread();
        }
        this.c = EGL14.EGL_NO_DISPLAY;
        this.b = EGL14.EGL_NO_CONTEXT;
    }
}
