package com.meishe.myvideo.util;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ParseJsonFile {
    private static final String TAG = "ParseJsonFile";

    public static <T> T fromJson(String str, Class<T> cls) {
        return (T) new Gson().fromJson(str, (Class) cls);
    }

    public static <T> T fromJson(String str, Type type) {
        return (T) new Gson().fromJson(str, type);
    }

    public static ArrayList<FxJsonFileInfo.JsonFileInfo> readBundleFxJsonFile(Context context, String str) {
        String readAssetJsonFile = readAssetJsonFile(context, str);
        if (TextUtils.isEmpty(readAssetJsonFile)) {
            return null;
        }
        return ((FxJsonFileInfo) fromJson(readAssetJsonFile, FxJsonFileInfo.class)).getFxInfoList();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0067 A[SYNTHETIC, Splitter:B:33:0x0067] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readAssetJsonFile(android.content.Context r7, java.lang.String r8) {
        /*
        // Method dump skipped, instructions count: 114
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.myvideo.util.ParseJsonFile.readAssetJsonFile(android.content.Context, java.lang.String):java.lang.String");
        return r8;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0062 A[SYNTHETIC, Splitter:B:30:0x0062] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readSDJsonFile(android.content.Context r7, java.lang.String r8) {
        /*
        // Method dump skipped, instructions count: 109
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.myvideo.util.ParseJsonFile.readSDJsonFile(android.content.Context, java.lang.String):java.lang.String");
        return r8;
    }

    public static class FxJsonFileInfo {
        private ArrayList<JsonFileInfo> fxInfoList;

        public ArrayList<JsonFileInfo> getFxInfoList() {
            return this.fxInfoList;
        }

        public static class JsonFileInfo {
            private String fitRatio;
            private String fxFileName;
            private String fxLicFileName;
            private String fxPackageId;
            private String imageName;
            private String name;
            private String name_Zh;

            public String getName_Zh() {
                return this.name_Zh;
            }

            public String getName() {
                return this.name;
            }

            public String getFxPackageId() {
                return this.fxPackageId;
            }

            public String getFxFileName() {
                return this.fxFileName;
            }

            public String getFxLicFileName() {
                return this.fxLicFileName;
            }

            public String getImageName() {
                return this.imageName;
            }

            public String getFitRatio() {
                return this.fitRatio;
            }
        }
    }
}
