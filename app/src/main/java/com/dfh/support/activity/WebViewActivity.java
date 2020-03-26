package com.dfh.support.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dfh.support.R;
import com.dfh.support.activity.support.CallPhoneActivity;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.compose.UmentBroadcastReceiver;
import com.dfh.support.entity.AdvertisementListData;
import com.dfh.support.http.HttpConfig;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.ToastUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIvCancel, mIvContactUs;
    private WebView mWebView;
    private WebSettings mWebSettings;
    private String baseUrl = "";
    private String id = "";
    private static final int AD_BROWSE_SUCCESS = 1;
    private static final int AD_BROWSE_FALSE = 2;
    private static final int REPECT_GET = 30 * 1000;
    private static final int GET_BROWSE_UP = 3;
    private static final int AD_LIKE_SUCCESS = 4;
    private static final int AD_LIKE_FALSE = 5;

    private RelativeLayout mRlNoZan, mRlHasZan;
    private RelativeLayout mRlZanNo, mRlZanYes;

    public static boolean openMySelf(Context ctx, String my_id,  String my_link, String my_likes, String my_browser){
        Intent intent = new Intent(ctx, WebViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", HttpConfig.GetHttpPolicyAdress() +
                my_link
                + "? links=" + my_likes
                + "&browses=" + my_browser);
        intent.putExtra("id", my_id);
        LogUtil.printActivityLog("openMySelf==========id= " + my_id);
        try {
            LogUtil.printActivityLog("openMySelf==========end===========");
            ctx.startActivity(intent);
            LogUtil.printActivityLog("openMySelf==========end11111===========");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            LogUtil.printActivityLog("WebviewActivity::handleMessage::id= " + msg.what);
            super.handleMessage(msg);
            switch (msg.what) {
                case AD_BROWSE_SUCCESS:
                    break;
                case AD_BROWSE_FALSE:
                    mHandler.sendEmptyMessageDelayed(GET_BROWSE_UP, REPECT_GET);
                    break;
                case GET_BROWSE_UP:
                    mAdsBrowseInfoTask = new AdsBrowseInfoTask();
                    mAdsBrowseInfoTask.execute("");
                    break;
                case AD_LIKE_SUCCESS:
                    LoadingProgressDialog.Dissmiss();

                    Intent intent = new Intent(UmentBroadcastReceiver.INTENT_MSG_STR);
                    intent.putExtra(UmentBroadcastReceiver.UMENG_MESSAGE, "update");
                    sendBroadcast(intent);
                    mRlNoZan.setVisibility(View.GONE);
                    mRlHasZan.setVisibility(View.VISIBLE);
                    if(baseUrl.contains("browses")){
                        String topUrl = baseUrl.substring(0,baseUrl.lastIndexOf("=")+1);
                        String browses = baseUrl.substring(baseUrl.lastIndexOf("=")+1);
                        LogUtil.printPushLog("AD_LIKE_SUCCESS topUrl:" + topUrl);
                        LogUtil.printPushLog("AD_LIKE_SUCCESS browses:" + browses);
                        try {
                            int dbrowses = Integer.parseInt(browses)+1;
                            mWebView.loadUrl(topUrl+dbrowses);
                        }catch (Exception e){
                            mWebView.loadUrl(baseUrl);
                            e.printStackTrace();
                        }
                    }
                    break;
                case AD_LIKE_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    ToastUtils.shortToast(WebViewActivity.this, HttpJsonAnaly.lastError);
                    break;
            }
        }
    };

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.printActivityLog("WebviewActivity::onCreate======================");
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_web_view);
        initView();
        initListener();
        initWebView();
        mHandler.sendEmptyMessage(GET_BROWSE_UP);
    }

    private void initView() {
        LogUtil.printActivityLog("WebviewActivity::initView======================");
        id = getIntent().getStringExtra("id");
        if (getIntent().hasExtra("url")) baseUrl = getIntent().getStringExtra("url");
        LogUtil.printPushLog("initView baseUrl" + baseUrl);
        mIvCancel = (ImageView) findViewById(R.id.iv_cancel);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mWebView = (WebView) findViewById(R.id.wv_content);
        mRlNoZan = (RelativeLayout) findViewById(R.id.rl_not_zan);
        mRlHasZan = (RelativeLayout) findViewById(R.id.rl_has_zan);
        mRlZanNo = (RelativeLayout) findViewById(R.id.rl_zan_no);
        mRlZanYes = (RelativeLayout) findViewById(R.id.rl_zan_yes);
    }

    private void initListener() {
        LogUtil.printActivityLog("WebviewActivity::initListener======================");
        mIvContactUs.setOnClickListener(this);
        mIvCancel.setOnClickListener(this);
        mRlZanYes.setOnClickListener(this);
        mRlZanNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(WebViewActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_zan_yes:
                LoadingProgressDialog.show(WebViewActivity.this, false, true, 30000);
                mAdsLikeTask = new AdsLikeTask();
                mAdsLikeTask.execute("");
                break;
            case R.id.rl_zan_no:
                mRlNoZan.setVisibility(View.GONE);
                mRlHasZan.setVisibility(View.VISIBLE);
                break;
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        LogUtil.printActivityLog("WebviewActivity::initWebView======================");
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
        LogUtil.printActivityLog("WebviewActivity::onStop======================");
        // 清理缓存
//        if (mWebView != null) {
//            mWebView.loadUrl("about:blank");
//            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            mWebView.clearHistory();
//            mWebView.destroy();
//            mWebView = null;
//        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.printActivityLog("WebviewActivity::onDestroy======================");
        // 清理缓存
        if (mWebView != null) {
            mWebView.loadUrl("about:blank");
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        try {
//            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack() && !mWebView.getUrl().equals(baseUrl)) {
//                mWebView.goBack();//返回上个页面
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return super.onKeyDown(keyCode, event);//退出H5界面

    }

    private AdvertisementListData advertisementListData;
    private AdsBrowseInfoTask mAdsBrowseInfoTask;

    private class AdsBrowseInfoTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            boolean flag = HttpJsonSend.adsBrowseInfo(WebViewActivity.this, id);
            if (flag) {
                mHandler.sendEmptyMessage(AD_BROWSE_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(AD_BROWSE_FALSE);
            }
            LogUtil.printPushLog("httpGet adsBrowseInfo flag" + flag);
            return null;
        }
    }

    private AdsLikeTask mAdsLikeTask;

    private class AdsLikeTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            boolean flag = HttpJsonSend.adsLike(WebViewActivity.this, id);
            if (flag) {
                mHandler.sendEmptyMessage(AD_LIKE_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(AD_LIKE_FALSE);
            }
            LogUtil.printPushLog("httpGet adsLike flag" + flag);
            return null;
        }
    }
}
