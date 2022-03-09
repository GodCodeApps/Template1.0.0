package com.meishe.myvideo.activity;

import android.view.View;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideoapp.R;

public class CompileActivity extends BaseActivity {
    View compileBack;
    View compileHome;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_compile;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        this.compileBack = findViewById(R.id.iv_compile_back);
        this.compileHome = findViewById(R.id.iv_compile_home);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.compileHome.setOnClickListener(this);
        this.compileBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_compile_back) { /*{ENCODED_INT: 2131296517}*/
            finish();
            return;
        } else if (id == R.id.iv_compile_home) { /*{ENCODED_INT: 2131296518}*/
            AppManager.getInstance().jumpActivity(this, DraftListActivity.class);
            finish();
            return;
        }
        return;
    }
}
