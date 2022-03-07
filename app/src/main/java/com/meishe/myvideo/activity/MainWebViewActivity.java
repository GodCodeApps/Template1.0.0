package com.meishe.myvideo.activity;

import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.example.net.parser.HttpContentType;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideoapp.R;

public class MainWebViewActivity extends BaseActivity {
    private Bundle mBundle;
    private TextView mContentTitle;
    private Toolbar mToolbar;
    private WebView mWebView;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_main_web_view;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setTitle("");
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToolbar.setNavigationIcon(R.drawable.main_webview_back);
        this.mWebView = (WebView) findViewById(R.id.main_activity_web_view);
        WebSettings settings = this.mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(0);
        }
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setCacheMode(1);
        settings.setDomStorageEnabled(true);
        settings.setSaveFormData(false);
        this.mWebView.setWebViewClient(new WebViewClient() {
            /* class com.meishe.myvideo.activity.MainWebViewActivity.AnonymousClass1 */

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Log.d("mWebView", "url: " + str);
                webView.loadUrl(str);
                return true;
            }

            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                builder.setMessage(R.string.ssl_error_prompt);
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    /* class com.meishe.myvideo.activity.MainWebViewActivity.AnonymousClass1.AnonymousClass1 */

                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    /* class com.meishe.myvideo.activity.MainWebViewActivity.AnonymousClass1.AnonymousClass2 */

                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                    }
                });
                builder.create().show();
            }
        });
        this.mContentTitle = (TextView) findViewById(R.id.webview_content_title);
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            /* class com.meishe.myvideo.activity.MainWebViewActivity.AnonymousClass2 */

            public void onReceivedTitle(WebView webView, String str) {
                super.onReceivedTitle(webView, str);
                if (MainWebViewActivity.this.mContentTitle != null) {
                    MainWebViewActivity.this.mContentTitle.setText(str);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.fragment.app.FragmentActivity
    public void onPause() {
        WebView webView = this.mWebView;
        if (webView != null) {
            webView.onPause();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.fragment.app.FragmentActivity
    public void onResume() {
        WebView webView = this.mWebView;
        if (webView != null) {
            webView.onResume();
        }
        super.onResume();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        this.mBundle = getIntent().getExtras();
        Bundle bundle = this.mBundle;
        this.mWebView.loadUrl(bundle != null ? bundle.getString("URL") : "");
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.MainWebViewActivity.AnonymousClass3 */

            public void onClick(View view) {
                MainWebViewActivity.this.finish();
            }
        });
    }

    public void onClick(View view) {
        view.getId();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_refresh, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        WebView webView;
        if (menuItem.getItemId() == R.id.webView_refresh && !Util.isFastClick() && (webView = this.mWebView) != null) {
            webView.reload();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onDestroy() {
        WebView webView = this.mWebView;
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", HttpContentType.CONTENT_TYPE_APPLICATION_BYTES, "utf-8", null);
            this.mWebView.setTag(null);
            this.mWebView.clearHistory();
            ((ViewGroup) this.mWebView.getParent()).removeView(this.mWebView);
            this.mWebView.destroy();
            this.mWebView = null;
        }
        super.onDestroy();
    }
}
