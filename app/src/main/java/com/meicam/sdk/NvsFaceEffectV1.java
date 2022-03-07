package com.meicam.sdk;

public class NvsFaceEffectV1 {
    public static final int FUAITYPE_BACKGROUNDSEGMENTATION = 2;
    public static final int FUAITYPE_BACKGROUNDSEGMENTATION_GREEN = 512;
    public static final int FUAITYPE_FACELANDMARKS209 = 64;
    public static final int FUAITYPE_FACELANDMARKS239 = 128;
    public static final int FUAITYPE_FACELANDMARKS75 = 32;
    public static final int FUAITYPE_FACEPROCESSOR = 1024;
    public static final int FUAITYPE_HAIRSEGMENTATION = 4;
    public static final int FUAITYPE_HANDGESTURE = 8;
    public static final int FUAITYPE_HUMANPOSE2D = 256;
    public static final int FUAITYPE_TONGUETRACKING = 16;

    private static native void nativeDone();

    private static native int nativeIsAIModelLoaded(int i);

    private static native void nativeLoadAiModule(String str, int i);

    private static native int nativeReleaseAiModule(int i);

    private static native void nativeSetMaxFaces(int i);

    private static native void nativeSetup(String str, byte[] bArr);

    public static void setup(String str, byte[] bArr) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetup(str, bArr);
    }

    public static void setMaxFaces(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetMaxFaces(i);
    }

    public static int releaseAiModule(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeReleaseAiModule(i);
    }

    public static int isAIModelLoaded(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsAIModelLoaded(i);
    }

    public static void loadAiModule(String str, int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeLoadAiModule(str, i);
    }

    public static void done() {
        NvsUtils.checkFunctionInMainThread();
        nativeDone();
    }
}
