package com.meishe.myvideo.util.permission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionsActivity extends AppCompatActivity {
    public static final String EXTRA_PERMISSIONS = "com.meicam.sdkdemo.utils.permission.extra_permission";
    public static final int PERMISSIONS_DENIED = 1;
    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_No_PROMPT = 2;
    private static final int PERMISSION_REQUEST_CODE = 0;
    private PermissionsChecker mChecker;

    public static void startActivityForResult(Activity activity, int i, String... strArr) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, strArr);
        ActivityCompat.startActivityForResult(activity, intent, i, null);
    }

    /* access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
        }
        this.mChecker = new PermissionsChecker(this);
        String[] permissions = getPermissions();
        if (this.mChecker.lacksPermissions(permissions)) {
            requestPermissions(permissions);
        } else {
            allPermissionsGranted();
        }
    }

    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    private void requestPermissions(String... strArr) {
        ActivityCompat.requestPermissions(this, strArr, 0);
    }

    private void allPermissionsGranted() {
        setResult(0);
        finish();
    }

    @Override // androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback, androidx.fragment.app.FragmentActivity
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        int i2;
        if (i == 0) {
            int i3 = 0;
            int i4 = 0;
            int i5 = -1;
            while (true) {
                if (i4 >= iArr.length) {
                    i2 = i5;
                    break;
                }
                if (iArr[i4] != 0) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, strArr[i4])) {
                        i2 = 2;
                        break;
                    }
                    i5 = 1;
                }
                i4++;
            }
            if (i2 != -1) {
                i3 = i2;
            }
            setActivityResult(i3);
        }
    }

    private void setActivityResult(int i) {
        setResult(i);
        finish();
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onPause() {
        super.onPause();
    }
}
