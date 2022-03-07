package com.meishe.myvideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.net.interfaces.NetListener;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.myvideo.MSApi;
import com.meishe.myvideo.bean.down.AbstractResponse;
import com.meishe.myvideo.bean.up.FeedBackUpParams;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.ImageUtils;
import com.meishe.myvideo.util.statusbar.StatusBarUtils;
import com.meishe.myvideoapp.R;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import org.json.JSONException;

public class FeedBackActivity extends BaseActivity {
    private static final int REQUEST_CODE = 1;
    private String mBase64PicPath;
    private EditText mEditContent;
    private EditText mEditPhone;
    private ImageView mIconback;
    private ImageView mImageSelect;
    private TextView mTvSubmit;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_feed_back;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.menu_bg));
        this.mIconback = (ImageView) findViewById(R.id.icon_back);
        this.mEditContent = (EditText) findViewById(R.id.edit_input_content);
        this.mEditPhone = (EditText) findViewById(R.id.edit_input_phone);
        this.mImageSelect = (ImageView) findViewById(R.id.image_select);
        this.mTvSubmit = (TextView) findViewById(R.id.tv_submit);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mIconback.setOnClickListener(this);
        this.mTvSubmit.setOnClickListener(this);
        this.mImageSelect.setOnClickListener(this);
        this.mEditContent.addTextChangedListener(new TextWatcher() {
            /* class com.meishe.myvideo.activity.FeedBackActivity.AnonymousClass1 */

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence != null) {
                    if (TextUtils.isEmpty(charSequence.toString())) {
                        FeedBackActivity.this.mTvSubmit.setClickable(false);
                        FeedBackActivity.this.mTvSubmit.setBackgroundResource(R.drawable.feed_back_commit_bg);
                        return;
                    }
                    FeedBackActivity.this.mTvSubmit.setClickable(true);
                    FeedBackActivity.this.mTvSubmit.setBackgroundResource(R.drawable.feed_back_commit_bg_clickable);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 && intent != null) {
            String stringExtra = intent.getStringExtra(MaterialSingleSelectActivity.KEY_FILE_PATH);
            ImageUtils.setImageByPath(this.mImageSelect, stringExtra);
            this.mBase64PicPath = ImageUtils.getImageBase64Str(stringExtra);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.icon_back) {
            AppManager.getInstance().finishActivity();
        } else if (id == R.id.image_select) {
            Bundle bundle = new Bundle();
            bundle.putInt("select_media_from", 4001);
            AppManager.getInstance().jumpActivityForResult(this, MaterialSingleSelectActivity.class, bundle, 1);
        } else if (id == R.id.tv_submit) {
            FeedBackUpParams feedBackUpParams = new FeedBackUpParams();
            if (!TextUtils.isEmpty(this.mBase64PicPath)) {
                feedBackUpParams.imageContent = this.mBase64PicPath;
            }
            feedBackUpParams.content = this.mEditContent.getText().toString();
            feedBackUpParams.contact = this.mEditPhone.getText().toString();
            NvsStreamingContext.SdkVersion sdkVersion = this.mStreamingContext.getSdkVersion();
            feedBackUpParams.sdkVersion = sdkVersion.majorVersion + "." + sdkVersion.minorVersion + "." + sdkVersion.revisionNumber;
            try {
                new MSApi().feedback(feedBackUpParams, new NetListener<AbstractResponse>() {
                    /* class com.meishe.myvideo.activity.FeedBackActivity.AnonymousClass2 */

                    @Override // com.example.net.interfaces.NetListener
                    public void onFaile(String str) {
                    }

                    public void onSuccess(AbstractResponse abstractResponse) {
                        FeedBackActivity.this.finish();
                    }
                });
            } catch (RemoteException | IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | JSONException unused) {
            }
        }
    }
}
