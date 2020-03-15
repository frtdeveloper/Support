package com.dfh.support.activity.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.TitleWebViewActivity;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.DebugDetailData;
import com.dfh.support.entity.DebugListData;
import com.dfh.support.http.HttpConfig;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.ToastUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DebuggingDetailActivity  extends AppCompatActivity implements View.OnClickListener {
    private String mTitle = "";
    private TextView mTvTitle;
    private ImageView mIvBack, mIvContactUs;
    private WebView mWebView;
    private WebSettings mWebSettings;
    private String baseUrl = "";
    private String id = "";

    private static final int FIND_SUCCESS = 1;
    private static final int FIND_FALSE = 2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FIND_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    LogUtil.printPushLog("FIND_SUCCESS url:" + mDebugDetailData.getUrl());
                    mWebView.loadUrl(mDebugDetailData.getUrl());
                    break;
                case FIND_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    ToastUtils.shortToast(DebuggingDetailActivity.this,  HttpJsonAnaly.lastError);
                    break;
            }
        }
    };

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_debugging_detail);
        initView();
        initListener();
        initWebView();
        LoadingProgressDialog.show(DebuggingDetailActivity.this, false, true, 30000);
        mFaultFindIdDetailTask = new FaultFindIdDetailTask();
        mFaultFindIdDetailTask.execute("");
    }

    private void initListener() {
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        if (getIntent().hasExtra("url")) baseUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        mWebView = (WebView) findViewById(R.id.wv_content);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText(mTitle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(DebuggingDetailActivity.this, CallPhoneActivity.class);
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
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack() && !mWebView.getUrl().equals(baseUrl)) {
                mWebView.goBack();//返回上个页面
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);//退出H5界面

    }

    private DebugDetailData mDebugDetailData;
    private FaultFindIdDetailTask mFaultFindIdDetailTask;


    private class FaultFindIdDetailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mDebugDetailData =  HttpJsonSend.faultFindIdDetail(DebuggingDetailActivity.this,id);
            if (mDebugDetailData.isFlag()) {
                mHandler.sendEmptyMessage(FIND_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(FIND_FALSE);
            }
            LogUtil.printPushLog("httpGet faultFindIdDetail mDebugDetailData" + mDebugDetailData.toString());
            return null;
        }
    }
}
