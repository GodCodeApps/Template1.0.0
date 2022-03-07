package com.meishe.effect;

import android.opengl.GLES20;
import com.meicam.effect.sdk.NvsEffect;
import com.meicam.effect.sdk.NvsEffectRenderCore;
import com.meicam.effect.sdk.NvsEffectSdkContext;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsVideoResolution;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class NvZoomEffectRenderCore {
    public static final String POSITION_COORDINATE = "position";
    public static final String TEXTURE_COORDINATE = "inputTextureCoordinate";
    public static final String TEXTURE_UNIFORM = "inputImageTexture";
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
    private int m = 0;
    private NvsVideoResolution n;
    private byte[] o;
    private Object p = new Object();

    NvZoomEffectRenderCore(NvsEffectSdkContext nvsEffectSdkContext) {
        this.j = nvsEffectSdkContext.createEffectRenderCore();
        this.n = new NvsVideoResolution();
        this.n.imagePAR = new NvsRational(1, 1);
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

    private boolean a(NvsEffect nvsEffect, int i2, int i3, int i4, int i5, long j2) {
        NvsEffectRenderCore nvsEffectRenderCore = this.j;
        if (nvsEffectRenderCore == null) {
            return false;
        }
        NvsVideoResolution nvsVideoResolution = this.n;
        nvsVideoResolution.imageWidth = i3;
        nvsVideoResolution.imageHeight = i4;
        return nvsEffectRenderCore.renderEffect(nvsEffect, i2, nvsVideoResolution, i5, j2, 0) == 0;
    }

    public void addNewRenderEffect(NvsEffect nvsEffect) {
        if (nvsEffect != null) {
            synchronized (this.i) {
                this.g.add(nvsEffect);
            }
        }
    }

    public void destoryGLResource() {
        a(this.k);
        this.k = 0;
        a(this.l);
        this.l = 0;
        a(this.m);
        this.m = 0;
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
        int i2 = this.c;
        if (i2 > 0) {
            GLES20.glDeleteProgram(i2);
        }
        this.c = -1;
    }

    public NvsEffectRenderCore getEffectRenderCore() {
        return this.j;
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

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.effect.NvZoomEffectRenderCore.removeRenderEffect(java.lang.String):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0050, code lost:
        if (r22.a != false) goto L_0x005a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0052, code lost:
        r22.a = r22.j.initialize();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0064, code lost:
        if (r24 == false) goto L_0x01a7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0068, code lost:
        if (r22.c > 0) goto L_0x00b9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x006a, code lost:
        r22.c = com.meishe.effect.EGLHelper.loadProgramForSurfaceTexture();
        r22.e = java.nio.ByteBuffer.allocateDirect(com.meishe.effect.EGLHelper.CUBE.length << 2).order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        r22.e.put(com.meishe.effect.EGLHelper.CUBE).position(0);
        r22.d = java.nio.ByteBuffer.allocateDirect(com.meishe.effect.EGLHelper.TEXTURE_NO_ROTATION.length << 2).order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        r22.d.clear();
        r22.d.put(com.meishe.effect.EGLHelper.TEXTURE_NO_ROTATION).position(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00bc, code lost:
        if (r22.c >= 0) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00be, code lost:
        r0 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00c1, code lost:
        r3 = com.meishe.effect.EGLHelper.getRotation(r29, true, r30);
        r22.d.clear();
        r22.d.put(r3).position(0);
        com.meishe.effect.EGLHelper.checkGlError("preProcess");
        android.opengl.GLES20.glUseProgram(r22.c);
        com.meishe.effect.EGLHelper.checkGlError("glUseProgram");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e8, code lost:
        if (r22.f > 0) goto L_0x00f9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ea, code lost:
        r22.f = a(r25, r26);
        com.meishe.effect.EGLHelper.bindFrameBuffer(r22.f, r22.b[0], r25, r26);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00f9, code lost:
        android.opengl.GLES20.glBindTexture(3553, r22.f);
        android.opengl.GLES20.glTexParameterf(3553, 10240, 9729.0f);
        android.opengl.GLES20.glTexParameterf(3553, 10241, 9729.0f);
        android.opengl.GLES20.glTexParameterf(3553, 10242, 33071.0f);
        android.opengl.GLES20.glTexParameterf(3553, 10243, 33071.0f);
        android.opengl.GLES20.glBindFramebuffer(36160, r22.b[0]);
        android.opengl.GLES20.glFramebufferTexture2D(36160, 36064, 3553, r22.f, 0);
        r22.e.position(0);
        r3 = android.opengl.GLES20.glGetAttribLocation(r22.c, "position");
        android.opengl.GLES20.glVertexAttribPointer(r3, 2, 5126, false, 0, (java.nio.Buffer) r22.e);
        android.opengl.GLES20.glEnableVertexAttribArray(r3);
        com.meishe.effect.EGLHelper.checkGlError("glEnableVertexAttribArray");
        r22.d.clear();
        r5 = android.opengl.GLES20.glGetAttribLocation(r22.c, "inputTextureCoordinate");
        android.opengl.GLES20.glVertexAttribPointer(r5, 2, 5126, false, 0, (java.nio.Buffer) r22.d);
        android.opengl.GLES20.glEnableVertexAttribArray(r5);
        com.meishe.effect.EGLHelper.checkGlError("glEnableVertexAttribArray");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x016e, code lost:
        if (r0 == -1) goto L_0x0186;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0170, code lost:
        android.opengl.GLES20.glBindTexture(36197, r0);
        r0 = android.opengl.GLES20.glGetUniformLocation(r22.c, "inputImageTexture");
        android.opengl.GLES20.glActiveTexture(33984);
        android.opengl.GLES20.glUniform1i(r0, 0);
        com.meishe.effect.EGLHelper.checkGlError("glBindTexture");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0186, code lost:
        android.opengl.GLES20.glActiveTexture(33984);
        android.opengl.GLES20.glViewport(0, 0, r25, r26);
        android.opengl.GLES20.glDrawArrays(5, 0, 4);
        android.opengl.GLES20.glDisableVertexAttribArray(r3);
        android.opengl.GLES20.glDisableVertexAttribArray(r5);
        android.opengl.GLES20.glBindTexture(36197, 0);
        com.meishe.effect.EGLHelper.checkGlError("glBindTexture");
        android.opengl.GLES20.glBindFramebuffer(36160, 0);
        android.opengl.GLES20.glUseProgram(0);
        r0 = r22.f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x01a7, code lost:
        com.meishe.effect.EGLHelper.checkGlError("preProcess");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x01ae, code lost:
        if (r22.k > 0) goto L_0x01b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x01b0, code lost:
        r22.k = a(r25, r26);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x01b6, code lost:
        com.meishe.effect.EGLHelper.checkGlError("createGLTexture");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x01bf, code lost:
        if (r1.size() <= 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x01c1, code lost:
        r7 = r22.k;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x01c5, code lost:
        if (r22.l > 0) goto L_0x01cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x01c7, code lost:
        r22.l = a(r25, r26);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x01cf, code lost:
        if (r22.m > 0) goto L_0x01d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x01d1, code lost:
        r22.m = a(r25, r26);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x01d7, code lost:
        r8 = new int[1];
        android.opengl.GLES20.glGetIntegerv(36006, r8, 0);
        com.meishe.effect.EGLHelper.checkGlError("glGetIntegerv");
        r2 = (com.meicam.effect.sdk.NvsVideoEffect) r1.get(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x01ed, code lost:
        if (r31 == false) goto L_0x0205;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x01ef, code lost:
        r18 = r7;
        r19 = r8;
        a(r2, r22.m, r25, r26, r7, r27 * 1000);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0205, code lost:
        r18 = r7;
        r19 = r8;
        a(r2, r0, r25, r26, r18, r27 * 1000);
        android.opengl.GLES20.glBindFramebuffer(36160, r22.b[0]);
        android.opengl.GLES20.glFramebufferTexture2D(36160, 36064, 3553, r0, 0);
        android.opengl.GLES20.glBindTexture(3553, r22.m);
        android.opengl.GLES20.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, r25, r26);
        android.opengl.GLES20.glBindFramebuffer(36160, 0);
        android.opengl.GLES20.glBindTexture(3553, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x023a, code lost:
        com.meishe.effect.EGLHelper.checkGlError("ProcessSingleFilter");
        android.opengl.GLES20.glBindFramebuffer(36160, r19[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        return r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int renderVideoEffect(int r23, boolean r24, int r25, int r26, long r27, int r29, boolean r30, boolean r31) {
        /*
        // Method dump skipped, instructions count: 591
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.effect.NvZoomEffectRenderCore.renderVideoEffect(int, boolean, int, int, long, int, boolean, boolean):int");
        return r23;
    }

    public void sendPreviewBuffer(byte[] bArr, int i2, int i3) {
        byte[] bArr2 = this.o;
        if (bArr2 == null || bArr2.length != ((i2 * i3) * 3) / 2) {
            this.o = new byte[(((i2 * i3) * 3) / 2)];
        }
        synchronized (this.p) {
            System.arraycopy(bArr, 0, this.o, 0, bArr.length);
        }
    }
}
