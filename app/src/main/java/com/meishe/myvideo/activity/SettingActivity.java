package com.meishe.myvideo.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.ParameterSettingValues;
import com.meishe.myvideo.util.SpUtil;
import com.meishe.myvideo.util.SystemUtils;
import com.meishe.myvideo.util.statusbar.StatusBarUtils;
import com.meishe.myvideoapp.R;
import com.meishe.player.common.Constants;

public class SettingActivity extends BaseActivity {
    private static final String TAG = "SettingActivity";
    private int mCompileVideoRes;
    private ImageView mImageSettingBack;
    private boolean mIsAutoAppend = false;
    private RadioButton mRb1080;
    private RadioButton mRb720;
    private RelativeLayout mRealFeedBack;
    private RelativeLayout mRealPrivacyPolicy;
    private RelativeLayout mRealUserAgreements;
    private RelativeLayout mRealVersionCode;
    private Switch mSettingAppend;
    private SpUtil mSp;
    private TextView mTvVersionCode;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_setting;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.menu_bg));
        this.mImageSettingBack = (ImageView) findViewById(R.id.image_setting_back);
        this.mTvVersionCode = (TextView) findViewById(R.id.tv_versin_code);
        this.mRb1080 = (RadioButton) findViewById(R.id.rb_ratio_1080);
        this.mRb720 = (RadioButton) findViewById(R.id.rb_ratio_720);
        this.mSettingAppend = (Switch) findViewById(R.id.setting_append);
        this.mRealFeedBack = (RelativeLayout) findViewById(R.id.real_feed_back);
        this.mRealUserAgreements = (RelativeLayout) findViewById(R.id.real_user_agreements);
        this.mRealPrivacyPolicy = (RelativeLayout) findViewById(R.id.real_privacy_policy);
        this.mRealVersionCode = (RelativeLayout) findViewById(R.id.real_versin_code);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        ParameterSettingValues instance = ParameterSettingValues.instance();
        this.mCompileVideoRes = instance.getCompileVideoRes();
        this.mIsAutoAppend = instance.isAutoAppendVideo();
        if (this.mCompileVideoRes != 720) {
            this.mRb1080.setChecked(true);
            this.mRb720.setChecked(false);
        } else {
            this.mRb1080.setChecked(false);
            this.mRb720.setChecked(true);
        }
        if (this.mIsAutoAppend) {
            this.mSettingAppend.setChecked(true);
        } else {
            this.mSettingAppend.setChecked(false);
        }
        this.mTvVersionCode.setText(getAppVersionName(this));
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mRb1080.setOnClickListener(this);
        this.mRb720.setOnClickListener(this);
        this.mImageSettingBack.setOnClickListener(this);
        this.mRealFeedBack.setOnClickListener(this);
        this.mRealUserAgreements.setOnClickListener(this);
        this.mRealPrivacyPolicy.setOnClickListener(this);
        this.mRealVersionCode.setOnClickListener(this);
        this.mSettingAppend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class com.meishe.myvideo.activity.SettingActivity.AnonymousClass1 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SettingActivity.this.mIsAutoAppend = z;
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.image_setting_back) { /*{ENCODED_INT: 2131296502}*/
            setParameterSettingValues();
            AppManager.getInstance().finishActivity();
            return;
        } else if (id == R.id.rb_ratio_1080) { /*{ENCODED_INT: 2131296654}*/
            this.mRb1080.setChecked(true);
            this.mRb720.setChecked(false);
            this.mCompileVideoRes = ParameterSettingValues.videoRes1080;
            return;
        } else if (id == R.id.rb_ratio_720) { /*{ENCODED_INT: 2131296655}*/
            this.mRb1080.setChecked(false);
            this.mRb720.setChecked(true);
            this.mCompileVideoRes = 720;
            return;
        } else if (id == R.id.real_feed_back) { /*{ENCODED_INT: 2131296657}*/
            AppManager.getInstance().jumpActivity(this, FeedBackActivity.class);
            return;
        } else if (id == R.id.real_privacy_policy) { /*{ENCODED_INT: 2131296660}*/
            Bundle bundle = new Bundle();
            if (SystemUtils.isZh(this)) {
                bundle.putString("URL", Constants.PRIVACY_POLICY_URL);
            } else {
                bundle.putString("URL", Constants.PRIVACY_POLICY_URL_EN);
            }
            AppManager.getInstance().jumpActivity(this, MainWebViewActivity.class, bundle);
            return;
        } else if (id == R.id.real_user_agreements) { /*{ENCODED_INT: 2131296662}*/
            Bundle bundle2 = new Bundle();
            if (SystemUtils.isZh(this)) {
                bundle2.putString("URL", Constants.USER_AGREEMENTS);
            } else {
                bundle2.putString("URL", Constants.USER_AGREEMENTS_EN);
            }
            AppManager.getInstance().jumpActivity(this, MainWebViewActivity.class, bundle2);
            return;
        }
        return;
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onBackPressed() {
        setParameterSettingValues();
        super.onBackPressed();
    }

    private void setParameterSettingValues() {
        ParameterSettingValues instance = ParameterSettingValues.instance();
        this.mSp = SpUtil.getInstance(getApplicationContext());
        instance.setCompileVideoRes(this.mCompileVideoRes);
        instance.setAutoAppendVideo(this.mIsAutoAppend);
        this.mSp.setObjectToShare(getApplicationContext(), instance, "paramter");
    }

    private long getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= 28) {
                return packageInfo.getLongVersionCode();
            }
            return (long) packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
            return 0;
        }
    }

    private String getAppVersionName(Context context) {
        try {
            return context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
            return "";
        }
    }
}
