package com.meishe.effect;

import android.opengl.GLES20;
import com.meicam.effect.sdk.NvsEffect;
import com.meicam.effect.sdk.NvsEffectRenderCore;
import com.meicam.effect.sdk.NvsEffectSdkContext;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsVideoResolution;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class EffectRenderCore {
    private boolean a = false;
    private int[] b = null;
    private int c = -1;
    private FloatBuffer d;
    private FloatBuffer e;
    private int f = -1;
    private ArrayList g;
    private ArrayList h;
    private Object i = new Object();
    private NvsEffectRenderCore j;
    private int k = 0;
    private int l = 0;
    private NvsVideoResolution m;
    private byte[] n;
    private Object o = new Object();
    private int p = -1;
    private FloatBuffer q;
    private FloatBuffer r;
    private long s = -1;
    private long t = 0;

    EffectRenderCore(NvsEffectSdkContext nvsEffectSdkContext) {
        this.j = nvsEffectSdkContext.createEffectRenderCore();
        this.m = new NvsVideoResolution();
        this.m.imagePAR = new NvsRational(1, 1);
        this.g = new ArrayList();
        this.h = new ArrayList();
    }

    private int a(int i2, int i3) {
        GLES20.glActiveTexture(33984);
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        EGLHelper.checkGlError("Texture generate");
        GLES20.glBindTexture(3553, iArr[0]);
        if (this.b == null) {
            this.b = new int[1];
            GLES20.glGenFramebuffers(1, this.b, 0);
        }
        GLES20.glTexImage2D(3553, 0, 6408, i2, i3, 0, 6408, 5121, null);
        EGLHelper.bindFrameBuffer(iArr[0], this.b[0], i2, i3);
        return iArr[0];
    }

    private static void a(int i2) {
        if (i2 > 0) {
            GLES20.glDeleteTextures(1, new int[]{i2}, 0);
        }
    }

    public void addNewRenderEffect(NvsEffect nvsEffect) {
        if (nvsEffect != null) {
            synchronized (this.i) {
                this.s = -1;
                this.g.add(nvsEffect);
            }
        }
    }

    public void destoryGLResource() {
        a(this.k);
        this.k = 0;
        a(this.l);
        this.l = 0;
        a(this.f);
        this.f = 0;
        int[] iArr = this.b;
        if (iArr != null) {
            GLES20.glDeleteFramebuffers(1, iArr, 0);
            this.b = null;
        }
        synchronized (this.i) {
            if (this.j != null) {
                if (this.h != null) {
                    Iterator it = this.h.iterator();
                    while (it.hasNext()) {
                        this.j.clearEffectResources((NvsEffect) it.next());
                    }
                    this.h.clear();
                }
                if (this.g != null) {
                    Iterator it2 = this.g.iterator();
                    while (it2.hasNext()) {
                        this.j.clearEffectResources((NvsEffect) it2.next());
                    }
                }
            }
        }
        this.j.clearCacheResources();
        this.j.cleanUp();
        this.j = null;
        int i2 = this.p;
        if (i2 > 0) {
            GLES20.glDeleteProgram(i2);
        }
        this.p = -1;
        int i3 = this.c;
        if (i3 > 0) {
            GLES20.glDeleteProgram(i3);
        }
        this.c = -1;
    }

    public void drawTexture(int i2, int i3, int i4, int i5, int i6) {
        float f2;
        if (this.p < 0) {
            this.p = EGLHelper.loadProgramForTexture();
            this.r = ByteBuffer.allocateDirect(EGLHelper.CUBE.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.r.put(EGLHelper.CUBE).position(0);
            this.q = ByteBuffer.allocateDirect(EGLHelper.TEXTURE_NO_ROTATION.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.q.clear();
            this.q.put(EGLHelper.TEXTURE_NO_ROTATION).position(0);
        }
        float f3 = ((float) i3) / ((float) i4);
        float f4 = ((float) i5) / ((float) i6);
        float f5 = 1.0f;
        if (f3 > f4) {
            f2 = f3 / f4;
        } else {
            f5 = f4 / f3;
            f2 = 1.0f;
        }
        float f6 = -f2;
        this.r.put(0, f6);
        this.r.put(1, f5);
        this.r.put(2, f2);
        this.r.put(3, f5);
        this.r.put(4, f6);
        float f7 = -f5;
        this.r.put(5, f7);
        this.r.put(6, f2);
        this.r.put(7, f7);
        this.r.position(0);
        GLES20.glUseProgram(this.p);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i2);
        this.r.position(0);
        int glGetAttribLocation = GLES20.glGetAttribLocation(this.p, "position");
        GLES20.glVertexAttribPointer(glGetAttribLocation, 2, 5126, false, 0, (Buffer) this.r);
        GLES20.glEnableVertexAttribArray(glGetAttribLocation);
        this.q.position(0);
        int glGetAttribLocation2 = GLES20.glGetAttribLocation(this.p, "inputTextureCoordinate");
        GLES20.glVertexAttribPointer(glGetAttribLocation2, 2, 5126, false, 0, (Buffer) this.q);
        GLES20.glEnableVertexAttribArray(glGetAttribLocation2);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.p, "inputImageTexture"), 0);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisableVertexAttribArray(glGetAttribLocation);
        GLES20.glDisableVertexAttribArray(glGetAttribLocation2);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, 0);
        GLES20.glUseProgram(0);
    }

    public long getExcutedTime() {
        return this.t;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0036 A[EDGE_INSN: B:18:0x0036->B:11:0x0036 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x000f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeRenderEffect(java.lang.String r6) {
        /*
            r5 = this;
            java.lang.Object r0 = r5.i
            monitor-enter(r0)
            java.util.ArrayList r1 = r5.g     // Catch:{ all -> 0x0038 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0038 }
        L_0x0009:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0038 }
            if (r2 == 0) goto L_0x0036
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0038 }
            com.meicam.effect.sdk.NvsEffect r2 = (com.meicam.effect.sdk.NvsEffect) r2     // Catch:{ all -> 0x0038 }
            r3 = r2
            com.meicam.effect.sdk.NvsVideoEffect r3 = (com.meicam.effect.sdk.NvsVideoEffect) r3     // Catch:{ all -> 0x0038 }
            java.lang.String r4 = r3.getVideoFxPackageId()     // Catch:{ all -> 0x0038 }
            java.lang.String r3 = r3.getBuiltinVideoFxName()     // Catch:{ all -> 0x0038 }
            boolean r4 = r4.equalsIgnoreCase(r6)     // Catch:{ all -> 0x0038 }
            if (r4 != 0) goto L_0x002c
            boolean r3 = r3.equalsIgnoreCase(r6)     // Catch:{ all -> 0x0038 }
            if (r3 == 0) goto L_0x0009
        L_0x002c:
            java.util.ArrayList r6 = r5.h     // Catch:{ all -> 0x0038 }
            r6.add(r2)     // Catch:{ all -> 0x0038 }
            java.util.ArrayList r6 = r5.g     // Catch:{ all -> 0x0038 }
            r6.remove(r2)     // Catch:{ all -> 0x0038 }
        L_0x0036:
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            return
        L_0x0038:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            goto L_0x003c
        L_0x003b:
            throw r6
        L_0x003c:
            goto L_0x003b
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.effect.EffectRenderCore.removeRenderEffect(java.lang.String):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0052, code lost:
        if (r22.a != false) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0054, code lost:
        r22.a = r22.j.initialize();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0061, code lost:
        if (r24 == false) goto L_0x01aa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0065, code lost:
        if (r22.c > 0) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0067, code lost:
        r22.c = com.meishe.effect.EGLHelper.loadProgramForSurfaceTexture();
        r22.e = java.nio.ByteBuffer.allocateDirect(com.meishe.effect.EGLHelper.CUBE.length << 2).order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        r22.e.put(com.meishe.effect.EGLHelper.CUBE).position(0);
        r22.d = java.nio.ByteBuffer.allocateDirect(com.meishe.effect.EGLHelper.TEXTURE_NO_ROTATION.length << 2).order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        r22.d.clear();
        r22.d.put(com.meishe.effect.EGLHelper.TEXTURE_NO_ROTATION).position(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00b6, code lost:
        r11 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00b9, code lost:
        if (r22.c >= 0) goto L_0x00bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00bd, code lost:
        r10 = com.meishe.effect.EGLHelper.getRotation(r29, true, r30);
        r22.d.clear();
        r22.d.put(r10).position(0);
        com.meishe.effect.EGLHelper.checkGlError("preProcess");
        android.opengl.GLES20.glUseProgram(r22.c);
        com.meishe.effect.EGLHelper.checkGlError("glUseProgram");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e4, code lost:
        if (r22.f > 0) goto L_0x00f5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e6, code lost:
        r22.f = a(r25, r26);
        com.meishe.effect.EGLHelper.bindFrameBuffer(r22.f, r22.b[0], r25, r26);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00f5, code lost:
        android.opengl.GLES20.glBindTexture(3553, r22.f);
        android.opengl.GLES20.glTexParameterf(3553, 10240, 9729.0f);
        android.opengl.GLES20.glTexParameterf(3553, 10241, 9729.0f);
        android.opengl.GLES20.glTexParameterf(3553, 10242, 33071.0f);
        android.opengl.GLES20.glTexParameterf(3553, 10243, 33071.0f);
        android.opengl.GLES20.glBindFramebuffer(36160, r22.b[0]);
        android.opengl.GLES20.glFramebufferTexture2D(36160, 36064, 3553, r22.f, 0);
        r22.e.position(0);
        r10 = android.opengl.GLES20.glGetAttribLocation(r22.c, "position");
        android.opengl.GLES20.glVertexAttribPointer(r10, 2, 5126, false, 0, (java.nio.Buffer) r22.e);
        android.opengl.GLES20.glEnableVertexAttribArray(r10);
        com.meishe.effect.EGLHelper.checkGlError("glEnableVertexAttribArray");
        r22.d.clear();
        r12 = android.opengl.GLES20.glGetAttribLocation(r22.c, "inputTextureCoordinate");
        android.opengl.GLES20.glVertexAttribPointer(r12, 2, 5126, false, 0, (java.nio.Buffer) r22.d);
        android.opengl.GLES20.glEnableVertexAttribArray(r12);
        com.meishe.effect.EGLHelper.checkGlError("glEnableVertexAttribArray");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0171, code lost:
        if (r0 == -1) goto L_0x0189;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0173, code lost:
        android.opengl.GLES20.glBindTexture(36197, r0);
        r0 = android.opengl.GLES20.glGetUniformLocation(r22.c, "inputImageTexture");
        android.opengl.GLES20.glActiveTexture(33984);
        android.opengl.GLES20.glUniform1i(r0, 0);
        com.meishe.effect.EGLHelper.checkGlError("glBindTexture");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0189, code lost:
        android.opengl.GLES20.glActiveTexture(33984);
        android.opengl.GLES20.glViewport(0, 0, r25, r26);
        android.opengl.GLES20.glDrawArrays(5, 0, 4);
        android.opengl.GLES20.glDisableVertexAttribArray(r10);
        android.opengl.GLES20.glDisableVertexAttribArray(r12);
        android.opengl.GLES20.glBindTexture(36197, 0);
        com.meishe.effect.EGLHelper.checkGlError("glBindTexture");
        android.opengl.GLES20.glBindFramebuffer(36160, 0);
        android.opengl.GLES20.glUseProgram(0);
        r0 = r22.f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x01aa, code lost:
        r11 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x01ab, code lost:
        com.meishe.effect.EGLHelper.checkGlError("preProcess");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x01b2, code lost:
        if (r22.k > 0) goto L_0x01ba;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x01b4, code lost:
        r22.k = a(r25, r26);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x01ba, code lost:
        com.meishe.effect.EGLHelper.checkGlError("createGLTexture");
        r0 = r22.k;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x01c5, code lost:
        if (r6.size() <= 1) goto L_0x01d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x01c9, code lost:
        if (r22.l > 0) goto L_0x01d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x01cb, code lost:
        r22.l = a(r25, r26);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x01d1, code lost:
        r10 = new int[1];
        android.opengl.GLES20.glGetIntegerv(36006, r10, 0);
        com.meishe.effect.EGLHelper.checkGlError("glGetIntegerv");
        r12 = r6.iterator();
        r21 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x01e8, code lost:
        if (r12.hasNext() == false) goto L_0x024e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x01ea, code lost:
        r14 = (com.meicam.effect.sdk.NvsEffect) r12.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x01fe, code lost:
        if (((com.meicam.effect.sdk.NvsVideoEffect) r14).getBuiltinVideoFxName().equals("Storyboard") == false) goto L_0x020d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0200, code lost:
        r23 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0208, code lost:
        if (r22.s >= 0) goto L_0x020f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x020a, code lost:
        r22.s = r27;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x020d, code lost:
        r23 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x020f, code lost:
        r9 = r27 - r22.s;
        r22.t = r9;
        r18 = (r9 + r31) * 1000;
        r13 = r22.j;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x021d, code lost:
        if (r13 != null) goto L_0x0221;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x021f, code lost:
        r9 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0221, code lost:
        r9 = r22.m;
        r9.imageWidth = r25;
        r9.imageHeight = r26;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0232, code lost:
        if (r13.renderEffect(r14, r11, r9, r0, r18, 0) == 0) goto L_0x0235;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0235, code lost:
        r9 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0236, code lost:
        r10 = r21 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0238, code lost:
        if (r9 == false) goto L_0x0248;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x023e, code lost:
        if (r10 == r6.size()) goto L_0x0250;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0240, code lost:
        r9 = r22.k;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0242, code lost:
        if (r0 != r9) goto L_0x0246;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0244, code lost:
        r9 = r22.l;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0246, code lost:
        r11 = r0;
        r0 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0248, code lost:
        r21 = r10;
        r10 = r23;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x024e, code lost:
        r23 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0250, code lost:
        com.meishe.effect.EGLHelper.checkGlError("ProcessSingleFilter");
        android.opengl.GLES20.glBindFramebuffer(36160, r23[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x025f, code lost:
        if (r6.size() != 0) goto L_0x0262;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
        return r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int renderVideoEffect(int r23, boolean r24, int r25, int r26, long r27, int r29, boolean r30, long r31) {
        /*
        // Method dump skipped, instructions count: 620
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.effect.EffectRenderCore.renderVideoEffect(int, boolean, int, int, long, int, boolean, long):int");
        return r23;
    }

    public void resetStartTime() {
        this.s = -1;
    }

    public void sendPreviewBuffer(byte[] bArr, int i2, int i3) {
        byte[] bArr2 = this.n;
        if (bArr2 == null || bArr2.length != ((i2 * i3) * 3) / 2) {
            this.n = new byte[(((i2 * i3) * 3) / 2)];
        }
        synchronized (this.o) {
            System.arraycopy(bArr, 0, this.n, 0, bArr.length);
        }
    }
}
