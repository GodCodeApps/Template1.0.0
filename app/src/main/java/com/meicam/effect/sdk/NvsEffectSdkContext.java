package com.meicam.effect.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsSystemVariableManager;
import com.meicam.sdk.NvsTimeUtil;
import com.meicam.sdk.NvsUtils;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NvsEffectSdkContext {
    public static final int DEBUG_LEVEL_DEBUG = 3;
    public static final int DEBUG_LEVEL_ERROR = 1;
    public static final int DEBUG_LEVEL_NONE = 0;
    public static final int DEBUG_LEVEL_WARNING = 2;
    public static final int HUMAN_DETECTION_DATA_TYPE_FAKE_FACE = 0;
    public static final int HUMAN_DETECTION_DATA_TYPE_MAKEUP = 1;
    public static final int HUMAN_DETECTION_FEATURE_AVATAR_EXPRESSION = 4;
    public static final int HUMAN_DETECTION_FEATURE_EXTRA = 128;
    public static final int HUMAN_DETECTION_FEATURE_FACE_ACTION = 2;
    public static final int HUMAN_DETECTION_FEATURE_FACE_LANDMARK = 1;
    public static final int HUMAN_DETECTION_FEATURE_IMAGE_MODE = 16;
    public static final int HUMAN_DETECTION_FEATURE_MULTI_THREAD = 32;
    public static final int HUMAN_DETECTION_FEATURE_SINGLE_THREAD = 64;
    public static final int HUMAN_DETECTION_FEATURE_VIDEO_MODE = 8;
    private static final String TAG = "Meicam";
    private static AssetManager m_assetManager = null;
    private static Thread m_checkExpirationThread = null;
    private static ClassLoader m_classLoader = null;
    private static Context m_context = null;
    private static boolean m_customNativeLibraryDirPath = false;
    private static int m_debugLevel = 3;
    private static boolean m_faceDetectionLibLoaded = false;
    private static boolean m_initializedOnce = false;
    private static NvsEffectSdkContext m_instance = null;
    private static String m_nativeLibraryDirPath = null;
    private static boolean m_saveDebugMessagesToFile = false;
    private NvsAssetPackageManager m_assetPackageManager;

    public static class SdkVersion {
        public int majorVersion;
        public int minorVersion;
        public int revisionNumber;
    }

    public static class VerifyLicenseResult {
        public boolean needCheckExpiration;
        public boolean success;
    }

    private static native void nativeClose();

    private static native void nativeCloseHumanDetection();

    private native NvsEffectRenderCore nativeCreateEffectRenderCore();

    private native NvsVideoEffect nativeCreateVideoEffect(String str, NvsRational nvsRational, boolean z);

    private native void nativeDetectPackageName(Context context);

    private static native boolean nativeFunctionalityAuthorised(String str);

    private native List<String> nativeGetAllBuiltinVideoFxNames();

    private native long nativeGetAssetPackageManager();

    private native SdkVersion nativeGetSdkVersion();

    private static native int nativeHasARModule();

    private static native boolean nativeInit(String str, int i);

    private static native boolean nativeInitHumanDetection(Context context, String str, String str2, int i);

    private static native boolean nativeInitJNI(Context context);

    private native boolean nativeIsEffectSdkAuthorised();

    private static native void nativeSetAssetManager(AssetManager assetManager);

    private static native void nativeSetDebugLevel(int i);

    private static native void nativeSetSaveDebugMessagesToFile(boolean z);

    private static native boolean nativeSetupHumanDetectionData(int i, String str);

    private static native VerifyLicenseResult nativeVerifySdkLicenseFile(Context context, String str, boolean z);

    public static void setNativeLibraryDirPath(String str) {
        NvsUtils.checkFunctionInMainThread();
        m_nativeLibraryDirPath = str;
        if (str != null) {
            m_customNativeLibraryDirPath = true;
        }
    }

    public static void setDebugLevel(int i) {
        NvsUtils.checkFunctionInMainThread();
        if (i != m_debugLevel) {
            m_debugLevel = i;
            if (m_instance != null) {
                nativeSetDebugLevel(m_debugLevel);
            }
        }
    }

    public static void setSaveDebugMessagesToFile(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        if (z != m_saveDebugMessagesToFile) {
            m_saveDebugMessagesToFile = z;
            if (m_instance != null) {
                nativeSetSaveDebugMessagesToFile(m_saveDebugMessagesToFile);
            }
        }
    }

    public static Context getContext() {
        NvsUtils.checkFunctionInMainThread();
        return m_context;
    }

    public static ClassLoader getClassLoader() {
        NvsUtils.checkFunctionInMainThread();
        return m_classLoader;
    }

    public boolean isSdkAuthorised() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsEffectSdkAuthorised();
    }

    public static NvsEffectSdkContext init(Context context, String str, int i) {
        boolean z;
        NvsUtils.checkFunctionInMainThread();
        NvsEffectSdkContext nvsEffectSdkContext = m_instance;
        if (nvsEffectSdkContext != null) {
            return nvsEffectSdkContext;
        }
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if (m_nativeLibraryDirPath == null) {
            m_nativeLibraryDirPath = applicationInfo.nativeLibraryDir + "/";
        }
        String str2 = ("HOME=" + context.getFilesDir().getAbsolutePath()) + "\tTMPDIR=" + context.getFilesDir().getAbsolutePath();
        try {
            m_assetManager = context.getAssets();
            m_context = context.getApplicationContext();
            m_classLoader = m_context.getClassLoader();
            boolean z2 = false;
            if (!m_initializedOnce) {
                if (m_customNativeLibraryDirPath) {
                    initNativeLibraryDirPath(context, m_nativeLibraryDirPath);
                }
                tryLoadFaceDetectionLibrary();
                try {
                    Class.forName("com.meicam.sdk.NvsStreamingContext");
                    z = false;
                } catch (Exception unused) {
                    z = true;
                }
                String str3 = "NvStreamingSdkCore";
                if (z) {
                    str3 = "NvEffectSdkCore";
                }
                loadNativeLibrary(str3);
            }
            if (nativeInitJNI(m_context)) {
                nativeSetAssetManager(m_assetManager);
                nativeSetDebugLevel(m_debugLevel);
                nativeSetSaveDebugMessagesToFile(m_saveDebugMessagesToFile);
                if (NvsSystemVariableManager.getSystemVariableInt(context, "isExpired") == 1) {
                    z2 = true;
                }
                VerifyLicenseResult verifyLicenseResult = new VerifyLicenseResult();
                if (!m_initializedOnce) {
                    verifyLicenseResult = nativeVerifySdkLicenseFile(context, str, z2);
                }
                if (verifyLicenseResult.needCheckExpiration) {
                    String systemVariableString = NvsSystemVariableManager.getSystemVariableString(context, "lastTime");
                    if (!TextUtils.isEmpty(systemVariableString) && NvsTimeUtil.getHourRange(systemVariableString, NvsTimeUtil.getCurrentTime()) >= 0) {
                    }
                }
                if (!nativeInit(str2, i)) {
                    return null;
                }
                m_instance = new NvsEffectSdkContext(context);
                m_initializedOnce = true;
                return m_instance;
            }
            throw new Exception("nativeInitJNI() failed!");
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            m_context = null;
            m_classLoader = null;
            m_assetManager = null;
            return null;
        }
    }

    public static NvsEffectSdkContext init(Activity activity, String str, int i) {
        NvsUtils.checkFunctionInMainThread();
        return init((Context) activity, str, i);
    }

    public static NvsEffectSdkContext init(Activity activity, String str) {
        NvsUtils.checkFunctionInMainThread();
        return init(activity, str, 0);
    }

    public static void close() {
        NvsUtils.checkFunctionInMainThread();
        NvsEffectSdkContext nvsEffectSdkContext = m_instance;
        if (nvsEffectSdkContext != null) {
            NvsAssetPackageManager assetPackageManager = nvsEffectSdkContext.getAssetPackageManager();
            if (assetPackageManager != null) {
                assetPackageManager.setCallbackInterface(null);
            }
            m_instance = null;
            m_context = null;
            nativeSetAssetManager(null);
            m_assetManager = null;
            m_classLoader = null;
            nativeClose();
        }
    }

    public static NvsEffectSdkContext getInstance() {
        NvsUtils.checkFunctionInMainThread();
        return m_instance;
    }

    public static int hasARModule() {
        NvsUtils.checkFunctionInMainThread();
        return nativeHasARModule();
    }

    public static boolean initHumanDetection(Context context, String str, String str2, int i) {
        NvsUtils.checkFunctionInMainThread();
        tryLoadFaceDetectionLibrary();
        if (!m_faceDetectionLibLoaded) {
            return false;
        }
        return nativeInitHumanDetection(context, str, str2, i);
    }

    public static void closeHumanDetection() {
        NvsUtils.checkFunctionInMainThread();
        nativeCloseHumanDetection();
    }

    public static boolean setupHumanDetectionData(int i, String str) {
        return nativeSetupHumanDetectionData(i, str);
    }

    private NvsEffectSdkContext(Context context) {
        this.m_assetPackageManager = null;
        this.m_assetPackageManager = new NvsAssetPackageManager(true);
        this.m_assetPackageManager.setInternalObject(nativeGetAssetPackageManager());
        nativeDetectPackageName(context);
    }

    public SdkVersion getSdkVersion() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetSdkVersion();
    }

    public NvsAssetPackageManager getAssetPackageManager() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_assetPackageManager;
    }

    public List<String> getAllBuiltinVideoFxNames() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetAllBuiltinVideoFxNames();
    }

    public NvsVideoEffect createVideoEffect(String str, NvsRational nvsRational) {
        NvsUtils.checkFunctionInMainThread();
        return nativeCreateVideoEffect(str, nvsRational, true);
    }

    public NvsVideoEffect createVideoEffect(String str, NvsRational nvsRational, boolean z) {
        NvsUtils.checkFunctionInMainThread();
        return nativeCreateVideoEffect(str, nvsRational, z);
    }

    public NvsEffectRenderCore createEffectRenderCore() {
        NvsUtils.checkFunctionInMainThread();
        return nativeCreateEffectRenderCore();
    }

    private boolean checkCameraPermission() {
        if (Build.VERSION.SDK_INT < 23) {
        }
        return true;
    }

    private boolean checkInternetPermission() {
        if (Build.VERSION.SDK_INT < 23 || m_context.checkSelfPermission("android.permission.INTERNET") == 0) {
            return true;
        }
        Log.e(TAG, "INTERNET permission has not been granted!");
        return false;
    }

    private static void loadNativeLibrary(String str) throws SecurityException, UnsatisfiedLinkError, NullPointerException {
        System.loadLibrary(str);
    }

    private static boolean tryLoadNativeLibrary(String str) throws SecurityException, UnsatisfiedLinkError, NullPointerException {
        try {
            System.loadLibrary(str);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static void tryLoadFaceDetectionLibrary() {
        boolean z;
        boolean z2;
        boolean z3;
        if (!m_faceDetectionLibLoaded) {
            boolean z4 = false;
            try {
                Class.forName("com.meicam.effect.sdk.NvsBEFFaceEffectDetector");
            } catch (Exception unused) {
                try {
                    Class.forName("com.meicam.sdk.NvsBEFFaceEffectDetector");
                } catch (Exception unused2) {
                    z = false;
                }
            }
            z = true;
            if (!z || tryLoadNativeLibrary("effect")) {
                try {
                    Class.forName("com.meicam.effect.sdk.NvsMGFaceEffectDetector");
                } catch (Exception unused3) {
                    try {
                        Class.forName("com.meicam.sdk.NvsMGFaceEffectDetector");
                    } catch (Exception unused4) {
                        z2 = false;
                    }
                }
                z2 = true;
                if (!z2 || (tryLoadNativeLibrary("megface-new") && tryLoadNativeLibrary("MegviiFacepp"))) {
                    try {
                        Class.forName("com.meicam.effect.sdk.NvsSTFaceEffectDetector");
                    } catch (Exception unused5) {
                        try {
                            Class.forName("com.meicam.sdk.NvsSTFaceEffectDetector");
                        } catch (Exception unused6) {
                            z3 = false;
                        }
                    }
                    z3 = true;
                    if (!z3 || tryLoadNativeLibrary("st_mobile")) {
                        try {
                            Class.forName("com.meicam.effect.sdk.NvsFUFaceEffectDetector");
                        } catch (Exception unused7) {
                            try {
                                Class.forName("com.meicam.sdk.NvsFUFaceEffectDetector");
                            } catch (Exception unused8) {
                            }
                        }
                        z4 = true;
                        if (!z4 || tryLoadNativeLibrary("nama") || tryLoadNativeLibrary("fuai")) {
                            m_faceDetectionLibLoaded = true;
                        }
                    }
                }
            }
        }
    }

    public static boolean functionalityAuthorised(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeFunctionalityAuthorised(str);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|4|5|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x000a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void initNativeLibraryDirPath(android.content.Context r1, java.lang.String r2) {
        /*
            boolean r0 = hasDexClassLoader()
            if (r0 == 0) goto L_0x000e
            createNewNativeDirAboveEqualApiLevel14(r1, r2)     // Catch:{ Exception -> 0x000a }
            goto L_0x0011
        L_0x000a:
            createNewNativeDirAboveEqualApiLevel21(r1, r2)     // Catch:{ Exception -> 0x0011 }
            goto L_0x0011
        L_0x000e:
            createNewNativeDirBelowApiLevel14(r1, r2)     // Catch:{ Exception -> 0x0011 }
        L_0x0011:
            return
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meicam.effect.sdk.NvsEffectSdkContext.initNativeLibraryDirPath(android.content.Context, java.lang.String):void");
    }

    private static void createNewNativeDirAboveEqualApiLevel14(Context context, String str) throws Exception {
        Object pathList = getPathList((PathClassLoader) context.getClassLoader());
        Field declaredField = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
        declaredField.setAccessible(true);
        File[] fileArr = (File[]) declaredField.get(pathList);
        Object newInstance = Array.newInstance(File.class, fileArr.length + 1);
        Array.set(newInstance, 0, new File(str));
        for (int i = 1; i < fileArr.length + 1; i++) {
            Array.set(newInstance, i, fileArr[i - 1]);
        }
        declaredField.set(pathList, newInstance);
    }

    private static void createNewNativeDirBelowApiLevel14(Context context, String str) throws NoSuchFieldException, IllegalAccessException {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        Field declaredField = pathClassLoader.getClass().getDeclaredField("mLibPaths");
        declaredField.setAccessible(true);
        String[] strArr = (String[]) declaredField.get(pathClassLoader);
        Object newInstance = Array.newInstance(String.class, strArr.length + 1);
        Array.set(newInstance, 0, str);
        for (int i = 1; i < strArr.length + 1; i++) {
            Array.set(newInstance, i, strArr[i - 1]);
        }
        declaredField.set(pathClassLoader, newInstance);
    }

    private static void createNewNativeDirAboveEqualApiLevel21(Context context, String str) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        if (Build.VERSION.SDK_INT >= 21) {
            Object pathList = getPathList((PathClassLoader) context.getClassLoader());
            Field declaredField = pathList.getClass().getDeclaredField("systemNativeLibraryDirectories");
            declaredField.setAccessible(true);
            List list = (List) declaredField.get(pathList);
            list.add(new File(str));
            declaredField.set(pathList, list);
            Field declaredField2 = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
            declaredField2.setAccessible(true);
            ArrayList arrayList = (ArrayList) declaredField2.get(pathList);
            arrayList.add(new File(str));
            declaredField2.set(pathList, arrayList);
            Class<?> cls = Class.forName("dalvik.system.DexPathList$Element");
            Constructor<?> constructor = cls.getConstructor(File.class, Boolean.TYPE, File.class, DexFile.class);
            Field declaredField3 = pathList.getClass().getDeclaredField("nativeLibraryPathElements");
            Field field = declaredField3;
            field.setAccessible(true);
            Object[] objArr = (Object[]) field.get(pathList);
            Object newInstance = Array.newInstance(cls, objArr.length + 1);
            if (constructor != null) {
                try {
                    Array.set(newInstance, 0, constructor.newInstance(new File(str), true, null, null));
                    for (int i = 1; i < objArr.length + 1; i++) {
                        Array.set(newInstance, i, objArr[i - 1]);
                    }
                    declaredField3.set(pathList, newInstance);
                } catch (IllegalArgumentException unused) {
                    Method declaredMethod = pathList.getClass().getDeclaredMethod("makePathElements", List.class);
                    declaredMethod.setAccessible(true);
                    Object invoke = declaredMethod.invoke(null, arrayList);
                    Field declaredField4 = pathList.getClass().getDeclaredField("nativeLibraryPathElements");
                    declaredField4.setAccessible(true);
                    declaredField4.set(pathList, invoke);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private static Object getPathList(Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getField(obj, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static Object getField(Object obj, Class cls, String str) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }

    private static boolean hasDexClassLoader() {
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}
