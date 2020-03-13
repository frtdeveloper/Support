package com.dfh.support.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.SparePartsListAdapter;
import com.dfh.support.activity.support.CallPhoneActivity;
import com.dfh.support.activity.support.SparePartsDetailActivity;
import com.dfh.support.activity.support.SparePartsListActivity;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.utils.ActionBarUtil;

public class TitleWebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private String mTitle = "";
    private TextView mTvTitle;
    private ImageView mIvBack, mIvContactUs;
    private WebView mWebView;
    private WebSettings mWebSettings;
    private String baseUrl = "http://218.28.95.84:3000";
    private String id = "";
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_title_web_view);
        initView();
        initListener();
        initWebView();
    }

    private void initListener() {
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        if(getIntent().hasExtra("url")) baseUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        mWebView = (WebView) findViewById(R.id.wv_content);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mTvTitle = (TextView)findViewById(R.id.tv_title);
        mTvTitle.setText(mTitle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(TitleWebViewActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);                    //  支持Javascript 与js交互
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//  支持通过JS打开新窗口
        mWebSettings.setAllowFileAccess(true);                      //  设置可以访问文件
        mWebSettings.setSupportZoom(true);                          //  支持缩放
        mWebSettings.setBuiltInZoomControls(true);                  //  设置内置的缩放控件
        mWebSettings.setUseWideViewPort(true);                      //  自适应屏幕
        mWebSettings.setSupportMultipleWindows(true);               //  多窗口
        mWebSettings.setDefaultTextEncodingName("utf-8");           //  设置编码格式
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);       //  缓存模式
        //设置不用系统浏览器打开,直接显示在当前WebView
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(baseUrl)) {
                    return false;
                }
                return true;
            }
        });
        //设置WebChromeClient类
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(baseUrl);
    }

    @Override
    protected void onStop() {
        // 清理缓存
        if (mWebView != null) {
            mWebView.loadUrl("about:blank");
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()&& !mWebView.getUrl().equals(baseUrl)) {
            mWebView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面

    }
}
