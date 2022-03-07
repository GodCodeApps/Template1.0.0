package com.meishe.effect;

import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.opengl.Matrix;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.meishe.effect.EGLBase;

public final class RenderHandler implements Runnable {
    private final Object a = new Object();
    private EGLContext b;
    private boolean c;
    private Object d;
    private int e = -1;
    private float[] f = new float[32];
    private boolean g;
    private boolean h;
    private int i;
    private long j;
    private EGLBase k;
    private EGLBase.EglSurface l;
    private GLDrawer2D m;

    private final void a() {
        EGLBase.EglSurface eglSurface = this.l;
        if (eglSurface != null) {
            eglSurface.release();
            this.l = null;
        }
        GLDrawer2D gLDrawer2D = this.m;
        if (gLDrawer2D != null) {
            gLDrawer2D.release();
            this.m = null;
        }
        EGLBase eGLBase = this.k;
        if (eGLBase != null) {
            eGLBase.release();
            this.k = null;
        }
    }

    public static final RenderHandler createHandler(String str) {
        RenderHandler renderHandler = new RenderHandler();
        synchronized (renderHandler.a) {
            if (TextUtils.isEmpty(str)) {
                str = "RenderHandler";
            }
            new Thread(renderHandler, str).start();
            try {
                renderHandler.a.wait();
            } catch (InterruptedException unused) {
            }
        }
        return renderHandler;
    }

    public final void draw(int i2, long j2) {
        draw(i2, this.f, null, j2);
    }

    public final void draw(int i2, float[] fArr, long j2) {
        draw(i2, fArr, null, j2);
    }

    public final void draw(int i2, float[] fArr, float[] fArr2, long j2) {
        synchronized (this.a) {
            if (!this.h) {
                this.e = i2;
                this.j = j2;
                if (fArr == null || fArr.length < 16) {
                    Matrix.setIdentityM(this.f, 0);
                } else {
                    System.arraycopy(fArr, 0, this.f, 0, 16);
                }
                if (fArr2 == null || fArr2.length < 16) {
                    Matrix.setIdentityM(this.f, 16);
                } else {
                    System.arraycopy(fArr2, 0, this.f, 16, 16);
                }
                this.i++;
                this.a.notifyAll();
            }
        }
    }

    public final void draw(long j2) {
        draw(this.e, this.f, null, j2);
    }

    public final void draw(float[] fArr, long j2) {
        draw(this.e, fArr, null, j2);
    }

    public final void draw(float[] fArr, float[] fArr2, long j2) {
        draw(this.e, fArr, fArr2, j2);
    }

    public final boolean isValid() {
        boolean z;
        synchronized (this.a) {
            if (this.d instanceof Surface) {
                if (!((Surface) this.d).isValid()) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public final void release() {
        synchronized (this.a) {
            if (!this.h) {
                this.h = true;
                this.a.notifyAll();
                try {
                    this.a.wait();
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005c, code lost:
        if (r0 == false) goto L_0x0096;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0060, code lost:
        if (r6.k == null) goto L_0x0010;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0064, code lost:
        if (r6.e < 0) goto L_0x0010;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0066, code lost:
        r6.l.makeCurrent();
        android.opengl.GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
        android.opengl.GLES20.glClear(16384);
        r6.m.setMatrix(r6.f, 16);
        r6.m.draw(r6.e, r6.f);
        r6.l.presentTime(r6.j);
        r6.l.swap();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0096, code lost:
        r0 = r6.a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0098, code lost:
        monitor-enter(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r6.a.wait();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        monitor-exit(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a1, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00b7, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        // Method dump skipped, instructions count: 192
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.effect.RenderHandler.run():void");
    }

    public final void setEglContext(EGLContext eGLContext, int i2, Object obj, boolean z) {
        if ((obj instanceof Surface) || (obj instanceof SurfaceTexture) || (obj instanceof SurfaceHolder)) {
            synchronized (this.a) {
                if (!this.h) {
                    this.b = eGLContext;
                    this.e = i2;
                    this.d = obj;
                    this.c = z;
                    this.g = true;
                    Matrix.setIdentityM(this.f, 0);
                    Matrix.setIdentityM(this.f, 16);
                    this.a.notifyAll();
                    try {
                        this.a.wait();
                    } catch (InterruptedException unused) {
                    }
                    return;
                }
                return;
            }
        }
        throw new RuntimeException("unsupported window type:" + obj);
    }
}
