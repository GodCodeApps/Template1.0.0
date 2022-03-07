package com.meishe.myvideo.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meicam.sdk.NvsStreamingContext;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideo.util.statusbar.StatusBarUtils;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BaseActivity";
    protected Context mContext;
    public NvsStreamingContext mStreamingContext;

    /* access modifiers changed from: protected */
    public void beforeSetContentView() {
    }

    /* access modifiers changed from: protected */
    public abstract void initData();

    /* access modifiers changed from: protected */
    public abstract void initListener();

    /* access modifiers changed from: protected */
    public abstract int initRootView();

    /* access modifiers changed from: protected */
    public abstract void initTitle();

    /* access modifiers changed from: protected */
    public abstract void initViews();

    /* access modifiers changed from: protected */
    @Override
    // androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mStreamingContext = getStreamingContext();
        this.mStreamingContext.stop();
        AppManager.getInstance().addActivity(this);
        this.mContext = this;
        beforeSetContentView();
        setContentView(initRootView());
        StatusBarUtils.setTranslucentStatus(this);
        initViews();
        initTitle();
        initData();
        initListener();
    }

    /* access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onStop() {
        super.onStop();
        if (Util.isBackground(this)) {
            this.mStreamingContext.stop();
        }
    }

    /* access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this);
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

    /* access modifiers changed from: protected */
    public void hideBottomMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
//            getWindow().getDecorView().setSystemUiVisibility(m.a.f);
        }
    }

    public NvsStreamingContext getStreamingContext() {
        if (this.mStreamingContext == null) {
            synchronized (NvsStreamingContext.class) {
                if (this.mStreamingContext == null) {
                    this.mStreamingContext = NvsStreamingContext.getInstance();
                    if (this.mStreamingContext == null) {
                        this.mStreamingContext = NvsStreamingContext.init(getApplicationContext(), "assets:/meishesdk.lic", 1);
                    }
                }
            }
        }
        return this.mStreamingContext;
    }
}
