package com.meishe.myvideo.util.permission;

import android.content.Context;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class PermissionsChecker {
    private final Context mContext;

    public PermissionsChecker(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public List<String> checkPermission(String... strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            if (lacksPermission(str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public List<String> checkPermission(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            if (lacksPermission(str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public boolean lacksPermissions(String... strArr) {
        for (String str : strArr) {
            if (lacksPermission(str)) {
                return true;
            }
        }
        return false;
    }

    public boolean lacksPermissions(List<String> list) {
        for (String str : list) {
            if (lacksPermission(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String str) {
        return ContextCompat.checkSelfPermission(this.mContext, str) == -1;
    }
}
