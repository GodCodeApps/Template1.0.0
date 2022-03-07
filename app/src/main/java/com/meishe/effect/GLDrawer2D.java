package com.meishe.effect;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLDrawer2D {
    private static final float[] a = {1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f};
    private static final float[] b = {1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};
    private final FloatBuffer c = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private final FloatBuffer d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private final float[] j = new float[16];

    public GLDrawer2D() {
        this.c.put(a);
        this.c.flip();
        this.d = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.d.put(b);
        this.d.flip();
        this.e = loadShader("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\n\nvoid main() {\n\tgl_Position = uMVPMatrix * aPosition;\n\tvTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "precision mediump float;\nuniform sampler2D sTexture;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}");
        GLES20.glUseProgram(this.e);
        this.f = GLES20.glGetAttribLocation(this.e, "aPosition");
        this.g = GLES20.glGetAttribLocation(this.e, "aTextureCoord");
        this.h = GLES20.glGetUniformLocation(this.e, "uMVPMatrix");
        this.i = GLES20.glGetUniformLocation(this.e, "uTexMatrix");
        Matrix.setIdentityM(this.j, 0);
        GLES20.glUniformMatrix4fv(this.h, 1, false, this.j, 0);
        GLES20.glUniformMatrix4fv(this.i, 1, false, this.j, 0);
        GLES20.glVertexAttribPointer(this.f, 2, 5126, false, 8, (Buffer) this.c);
        GLES20.glVertexAttribPointer(this.g, 2, 5126, false, 8, (Buffer) this.d);
        GLES20.glEnableVertexAttribArray(this.f);
        GLES20.glEnableVertexAttribArray(this.g);
    }

    public static void deleteTex(int i2) {
        GLES20.glDeleteTextures(1, new int[]{i2}, 0);
    }

    public static int initTex() {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(36197, iArr[0]);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        GLES20.glTexParameteri(36197, 10241, 9728);
        GLES20.glTexParameteri(36197, 10240, 9728);
        return iArr[0];
    }

    public static int loadShader(String str, String str2) {
        int glCreateShader = GLES20.glCreateShader(35633);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        int i2 = 0;
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] == 0) {
            GLES20.glDeleteShader(glCreateShader);
            glCreateShader = 0;
        }
        int glCreateShader2 = GLES20.glCreateShader(35632);
        GLES20.glShaderSource(glCreateShader2, str2);
        GLES20.glCompileShader(glCreateShader2);
        GLES20.glGetShaderiv(glCreateShader2, 35713, iArr, 0);
        if (iArr[0] == 0) {
            GLES20.glDeleteShader(glCreateShader2);
        } else {
            i2 = glCreateShader2;
        }
        int glCreateProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(glCreateProgram, glCreateShader);
        GLES20.glAttachShader(glCreateProgram, i2);
        GLES20.glLinkProgram(glCreateProgram);
        return glCreateProgram;
    }

    public void draw(int i2, float[] fArr) {
        GLES20.glUseProgram(this.e);
        if (fArr != null) {
            GLES20.glUniformMatrix4fv(this.i, 1, false, fArr, 0);
        }
        GLES20.glUniformMatrix4fv(this.h, 1, false, this.j, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i2);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glBindTexture(3553, 0);
        GLES20.glUseProgram(0);
    }

    public void release() {
        int i2 = this.e;
        if (i2 >= 0) {
            GLES20.glDeleteProgram(i2);
        }
        this.e = -1;
    }

    public void setMatrix(float[] fArr, int i2) {
        if (fArr == null || fArr.length < i2 + 16) {
            Matrix.setIdentityM(this.j, 0);
        } else {
            System.arraycopy(fArr, i2, this.j, 0, 16);
        }
    }
}
