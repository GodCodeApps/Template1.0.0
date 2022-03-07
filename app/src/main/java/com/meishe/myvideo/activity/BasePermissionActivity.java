package com.meishe.myvideo.activity;

import android.content.Intent;
import android.os.Bundle;
import com.meishe.myvideo.util.permission.PermissionsActivity;
import com.meishe.myvideo.util.permission.PermissionsChecker;
import java.util.List;

public abstract class BasePermissionActivity extends BaseActivity {
    static final int REQUEST_CODE = 110;
    private PermissionsChecker mPermissionsChecker;
    List<String> permissionList;

    /* access modifiers changed from: protected */
    public abstract void hasPermission();

    /* access modifiers changed from: protected */
    public abstract List<String> initPermissions();

    /* access modifiers changed from: protected */
    public abstract void noPromptPermission();

    /* access modifiers changed from: protected */
    public abstract void nonePermission();

    /* access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, com.meishe.myvideo.activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        this.permissionList = initPermissions();
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 0) {
            hasPermission();
        } else if (i2 == 1) {
            nonePermission();
        } else if (i2 == 2) {
            noPromptPermission();
        }
    }

    public boolean hasAllPermission() {
        if (this.mPermissionsChecker == null) {
            this.mPermissionsChecker = new PermissionsChecker(this);
        }
        return !this.mPermissionsChecker.lacksPermissions(this.permissionList);
    }

    public void checkPermissions() {
        if (this.mPermissionsChecker == null) {
            this.mPermissionsChecker = new PermissionsChecker(this);
        }
        this.permissionList = this.mPermissionsChecker.checkPermission(this.permissionList);
        String[] strArr = new String[this.permissionList.size()];
        this.permissionList.toArray(strArr);
        if (!this.permissionList.isEmpty()) {
            startPermissionsActivity(110, strArr);
        }
    }

    private void startPermissionsActivity(int i, String... strArr) {
        PermissionsActivity.startActivityForResult(this, i, strArr);
    }
}
