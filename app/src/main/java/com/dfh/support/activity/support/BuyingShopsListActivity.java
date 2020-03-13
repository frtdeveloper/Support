package com.dfh.support.activity.support;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dfh.support.R;
import com.dfh.support.SupportApplication;
import com.dfh.support.activity.adapter.BuyingShopsListAdapter;
import com.dfh.support.activity.adapter.ServiceListAdapter;
import com.dfh.support.activity.widget.LoadListView;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.ServeData;
import com.dfh.support.entity.ServeListData;
import com.dfh.support.http.HttpConfig;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BuyingShopsListActivity extends AppCompatActivity  implements View.OnClickListener, LoadListView.ILoadListener2 {

    private ImageView mIvBack, mIvContactUs;
    private LinearLayout mLlSearch;

    private LoadListView mLvBuyingShops;
    private ArrayList<ServeData> mBuyingShopsList = new ArrayList<ServeData>();
    private BuyingShopsListAdapter mBuyingShopsListAdapter;

    private static final int SERVE_PAGER_SUCCESS = 1;
    private static final int SERVE_PAGER_FALSE = 2;
    private static final int GET_SERVE_PAGER = 3;
    private static final int REPECT_GET = 30 * 1000;
    private ImageView mIvPic;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SERVE_PAGER_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    //刷新list
                    mBuyingShopsList.addAll(serveListData.getServeData());
                    mBuyingShopsListAdapter.setList(mBuyingShopsList);
                    mLvBuyingShops.loadComplete();
                    break;
                case SERVE_PAGER_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    mHandler.sendEmptyMessageDelayed(GET_SERVE_PAGER, REPECT_GET);
                    mLvBuyingShops.loadComplete();
                    break;
                case GET_SERVE_PAGER:
                    LoadingProgressDialog.show(BuyingShopsListActivity.this, false, true, 30000);
                    mServePagerTask = new ServePagerTask();
                    mServePagerTask.execute("");
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
        setContentView(R.layout.activity_buying_shops_list);
        initView();
        initListener();
        mHttpReceiver = new HttpReceiver();//广播接受者实例
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SupportApplication.ACTION_HTTP_RESULT);
        registerReceiver(mHttpReceiver, intentFilter);
        mHandler.sendEmptyMessage(GET_SERVE_PAGER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHttpReceiver);
    }

    private void initListener() {
        mLvBuyingShops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BuyingShopsListActivity.this,BuyingShopsDetailActivity.class);
                intent.putExtra("id",mBuyingShopsList.get(i).getId());
                startActivity(intent);
            }
        });
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mLlSearch.setOnClickListener(this);
    }

    private void initView() {
        mLlSearch = (LinearLayout) findViewById(R.id.ll_search);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvBuyingShops = (LoadListView)findViewById(R.id.lv_buying_shops_list);
        mBuyingShopsListAdapter = new BuyingShopsListAdapter(this,mBuyingShopsList);
        mLvBuyingShops.setAdapter(mBuyingShopsListAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(BuyingShopsListActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_search:
                intent = new Intent(BuyingShopsListActivity.this, SearchCityActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLoad() {
        pageNo = pageNo + 1;
        mHandler.sendEmptyMessage(GET_SERVE_PAGER);
    }

    private ServePagerTask mServePagerTask;
    private ServeListData serveListData;
    private String area = "";
    private String lat = "";
    private String lng = "";
    private String pageSize = "20";
    private String pageNo = "1";

    private class ServePagerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpJsonSend.servePager(BuyingShopsListActivity.this, area,
                    lat, lng, pageSize, pageNo,HttpJsonSend.SERVE_TYPE_BUYING);
            return null;
        }
    }

    private HttpReceiver mHttpReceiver;

    public class HttpReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.printPushLog("mHttpReceiver action :" + action);
            if (action.equals(SupportApplication.ACTION_HTTP_RESULT)) {
                //先判断url
                String url = intent.getStringExtra("url");
                if (url != null && !TextUtils.isEmpty(url)) {
                    if (url.contains(HttpConfig.urL_serve_pager)) {
                        boolean is_success = intent.getBooleanExtra("is_success", false);
                        LogUtil.printPushLog("mHttpReceiver urL_serve_pager :" + is_success);
                        if (is_success) {
                            //解析data再发送结果
                            String data = intent.getStringExtra("data");
                            try {
                                serveListData = HttpJsonAnaly.servePager(data, BuyingShopsListActivity.this);
                                if (serveListData.isFlag()) {
                                    mHandler.sendEmptyMessage(SERVE_PAGER_SUCCESS);
                                } else {
                                    mHandler.sendEmptyMessage(SERVE_PAGER_FALSE);
                                }
                                LogUtil.printPushLog("mHttpReceiver serveListData.toString :" + serveListData.toString());
                            } catch (Exception e) {
                                mHandler.sendEmptyMessage(SERVE_PAGER_FALSE);
                                e.printStackTrace();
                            }
                        } else {
                            mHandler.sendEmptyMessage(SERVE_PAGER_FALSE);
                        }
                    }
                }
            }
        }

    }
}
