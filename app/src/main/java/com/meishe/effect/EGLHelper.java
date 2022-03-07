package com.meishe.effect;

import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

public class EGLHelper {
    public static final String CAMERA_INPUT_FRAGMENT_SHADER = "precision mediump float;\nvarying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
    public static final float[] CUBE = {-1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f};
    public static final int SURFACE_PBUFFER = 1;
    public static final int SURFACE_PIM = 2;
    public static final int SURFACE_WINDOW = 3;
    public static final float[] TEXTURE_NO_ROTATION = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    public static final float[] TEXTURE_ROTATED_180 = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
    public static final float[] TEXTURE_ROTATED_270 = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    public static final float[] TEXTURE_ROTATED_90 = {1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
    private int a = 1;
    private Object b;
    private int c = 8;
    private int d = 8;
    private int e = 8;
    private int f = 8;
    private int g = 16;
    private int h = 4;
    private int i = 12421;
    private EGLContext j = EGL10.EGL_NO_CONTEXT;
    public EGL10 mEgl;
    public EGLConfig mEglConfig;
    public EGLContext mEglContext;
    public EGLDisplay mEglDisplay;
    public EGLSurface mEglSurface;
    public GL10 mGL;

    private static float a(float f2) {
        return f2 == 0.0f ? 1.0f : 0.0f;
    }

    private static int a(String str, int i2) {
        int[] iArr = new int[1];
        int glCreateShader = GLES20.glCreateShader(i2);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        Log.e("Load Shader Failed", "Compilation\n" + GLES20.glGetShaderInfoLog(glCreateShader));
        return 0;
    }

    public static void bindFrameBuffer(int i2, int i3, int i4, int i5) {
        GLES20.glBindTexture(3553, i2);
        GLES20.glTexImage2D(3553, 0, 6408, i4, i5, 0, 6408, 5121, null);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        GLES20.glTexParameterf(3553, 10242, 33071.0f);
        GLES20.glTexParameterf(3553, 10243, 33071.0f);
        GLES20.glBindFramebuffer(36160, i3);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, i2, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindFramebuffer(36160, 0);
    }

    public static void checkGlError(String str) {
        while (true) {
            int glGetError = GLES20.glGetError();
            if (glGetError != 0) {
                Log.e("SurfaceTest", str + ": glError " + GLUtils.getEGLErrorString(glGetError));
            } else {
                return;
            }
        }
    }

    public static float[] getRotation(int i2, boolean z, boolean z2) {
        float[] fArr = i2 != 90 ? i2 != 180 ? i2 != 270 ? TEXTURE_NO_ROTATION : TEXTURE_ROTATED_270 : TEXTURE_ROTATED_180 : TEXTURE_ROTATED_90;
        if (z) {
            fArr = new float[]{a(fArr[0]), fArr[1], a(fArr[2]), fArr[3], a(fArr[4]), fArr[5], a(fArr[6]), fArr[7]};
        }
        if (!z2) {
            return fArr;
        }
        return new float[]{fArr[0], a(fArr[1]), fArr[2], a(fArr[3]), fArr[4], a(fArr[5]), fArr[6], a(fArr[7])};
    }

    public static int loadProgram(String str, String str2) {
        String str3;
        int[] iArr = new int[1];
        int a2 = a(str, 35633);
        if (a2 == 0) {
            str3 = "Vertex Shader Failed";
        } else {
            int a3 = a(str2, 35632);
            if (a3 == 0) {
                str3 = "Fragment Shader Failed";
            } else {
                int glCreateProgram = GLES20.glCreateProgram();
                GLES20.glAttachShader(glCreateProgram, a2);
                GLES20.glAttachShader(glCreateProgram, a3);
                GLES20.glLinkProgram(glCreateProgram);
                GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
                if (iArr[0] <= 0) {
                    str3 = "Linking Failed";
                } else {
                    GLES20.glDeleteShader(a2);
                    GLES20.glDeleteShader(a3);
                    return glCreateProgram;
                }
            }
        }
        Log.d("Load Program", str3);
        return 0;
    }

    public static int loadProgramForSurfaceTexture() {
        return loadProgram("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nvarying vec2 textureCoordinate;\n\nvoid main()\n{\n\ttextureCoordinate = inputTextureCoordinate.xy;\n\tgl_Position = position;\n}", "#extension GL_OES_EGL_image_external : require\n\nprecision mediump float;\nvarying vec2 textureCoordinate;\nuniform samplerExternalOES inputImageTexture;\n\nvoid main()\n{\n\tgl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
    }

    public static int loadProgramForTexture() {
        return loadProgram("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nvarying vec2 textureCoordinate;\n\nvoid main()\n{\n\ttextureCoordinate = inputTextureCoordinate.xy;\n\tgl_Position = position;\n}", CAMERA_INPUT_FRAGMENT_SHADER);
    }

    public void config(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.c = i2;
        this.d = i3;
        this.e = i4;
        this.f = i5;
        this.g = i6;
        this.h = i7;
    }

    public void destroy() {
        EGL10 egl10 = this.mEgl;
        EGLDisplay eGLDisplay = this.mEglDisplay;
        EGLSurface eGLSurface = EGL10.EGL_NO_SURFACE;
        egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL10.EGL_NO_CONTEXT);
        this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
        this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
        this.mEgl.eglTerminate(this.mEglDisplay);
    }

    public EGLError eglInit(int i2, int i3) {
        int[] iArr = {12324, this.c, 12323, this.d, 12322, this.e, 12321, this.f, 12325, this.g, 12352, this.h, 12344};
        this.mEgl = (EGL10) EGLContext.getEGL();
        this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        this.mEgl.eglInitialize(this.mEglDisplay, new int[2]);
        int[] iArr2 = new int[1];
        this.mEgl.eglChooseConfig(this.mEglDisplay, iArr, null, 0, iArr2);
        if (iArr2[0] == 0) {
            return EGLError.ConfigErr;
        }
        EGLConfig[] eGLConfigArr = new EGLConfig[iArr2[0]];
        this.mEgl.eglChooseConfig(this.mEglDisplay, iArr, eGLConfigArr, iArr2[0], iArr2);
        this.mEglConfig = eGLConfigArr[0];
        int[] iArr3 = {12375, i2, 12374, i3, 12344};
        int i4 = this.a;
        this.mEglSurface = i4 != 2 ? i4 != 3 ? this.mEgl.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig, iArr3) : this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, this.b, iArr3) : this.mEgl.eglCreatePixmapSurface(this.mEglDisplay, this.mEglConfig, this.b, iArr3);
        this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, this.mEglConfig, this.j, new int[]{12440, 2, 12344});
        makeCurrent();
        return EGLError.OK;
    }

    public void makeCurrent() {
        EGL10 egl10 = this.mEgl;
        EGLDisplay eGLDisplay = this.mEglDisplay;
        EGLSurface eGLSurface = this.mEglSurface;
        egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.mEglContext);
        this.mGL = (GL10) this.mEglContext.getGL();
    }

    public void setSurfaceType(int i2, Object... objArr) {
        this.a = i2;
        if (objArr != null) {
            this.b = objArr[0];
        }
    }
}
