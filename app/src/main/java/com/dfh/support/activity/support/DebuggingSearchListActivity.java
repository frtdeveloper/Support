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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.SupportApplication;
import com.dfh.support.activity.TitleWebViewActivity;
import com.dfh.support.activity.adapter.DebuggingListAdapter;
import com.dfh.support.activity.widget.LoadListView;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.DebugDetailData;
import com.dfh.support.entity.DebugListData;
import com.dfh.support.http.HttpConfig;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;
import com.dfh.support.utils.ToastUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DebuggingSearchListActivity extends AppCompatActivity implements View.OnClickListener,LoadListView.ILoadListener2{

    private ImageView mIvBack, mIvContactUs;
    private TextView mTvTitle;
    private String mTitle = "";
    private LoadListView mLvDebuggingList;
    private ArrayList<DebugDetailData> mDebuggingList = new ArrayList<DebugDetailData>();
    private DebuggingListAdapter mDebuggingListAdapter;

    private static final int FIND_SUCCESS = 1;
    private static final int FIND_FALSE = 2;
    private static final int FIND_LIST_SUCCESS = 3;
    private static final int FIND_LIST_FALSE = 4;
    private static final int GET_LIST_PD = 5;
    private static final int REPECT_GET = 30*1000;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FIND_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    mDebuggingList = mDebugListData.getDebugDetailData();
                    mDebuggingListAdapter.setList(mDebuggingList);
                    break;
                case FIND_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    ToastUtils.shortToast(DebuggingSearchListActivity.this,  HttpJsonAnaly.lastError);
                    break;

                case FIND_LIST_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    //刷新list
                    //应该是个新的界面去获取这个内容
                    mDebuggingList = mDebugListData.getDebugDetailData();
                    mDebuggingListAdapter.setList(mDebuggingList);
                    break;
                case FIND_LIST_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    mHandler.sendEmptyMessageDelayed(GET_LIST_PD,REPECT_GET);
                    break;
                case GET_LIST_PD:
                    LoadingProgressDialog.show(DebuggingSearchListActivity.this, false, true, 30000);
                    mFaultSearchTask = new FaultSearchTask();
                    mFaultSearchTask.execute("");
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
        setContentView(R.layout.activity_debugging_search_list);
        initView();
        initListener();
//        LoadingProgressDialog.show(DebuggingSearchListActivity.this, false, true, 30000);
//        mFaultFindIdListTask = new FaultFindIdListTask();
//        mFaultFindIdListTask.execute("");
        mHttpReceiver = new HttpReceiver();//广播接受者实例
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SupportApplication.ACTION_HTTP_RESULT);
        registerReceiver(mHttpReceiver, intentFilter);
        keywords = getIntent().getStringExtra("keywords");
        mHandler.sendEmptyMessage(GET_LIST_PD);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHttpReceiver);
    }


    private void initListener() {
        mLvDebuggingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DebuggingSearchListActivity.this, TitleWebViewActivity.class);
                intent.putExtra("title",mDebuggingList.get(i).getTitle());
                //intent.putExtra("url",mDebuggingList.get(i).get());
                startActivity(intent);
            }
        });
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        mTitle = getIntent().getStringExtra("title");
        mTvTitle = (TextView)findViewById(R.id.tv_title);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvDebuggingList = (LoadListView)findViewById(R.id.lv_debugging_list);
        mDebuggingListAdapter = new DebuggingListAdapter(this,mDebuggingList);
        mLvDebuggingList.setAdapter(mDebuggingListAdapter);
        if(!TextUtils.isEmpty(mTitle))mTvTitle.setText(mTitle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(DebuggingSearchListActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }
    private DebugListData mDebugListData;
    private FaultFindIdListTask mFaultFindIdListTask;
    private String id = "";


    private class FaultFindIdListTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mDebugListData =  HttpJsonSend.faultFindIdList(DebuggingSearchListActivity.this,id);
            if (mDebugListData.isFlag()) {
                mHandler.sendEmptyMessage(FIND_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(FIND_FALSE);
            }
            LogUtil.printPushLog("httpGet faultFindIdList mDebugListData" + mDebugListData.toString());
            return null;
        }
    }

    @Override
    public void onLoad() {
        pageNo = pageNo+1;
        mHandler.sendEmptyMessage(GET_LIST_PD);
    }
    private FaultSearchTask mFaultSearchTask;
    private String keywords = "";
    private String pageSize = "20";
    private String pageNo = "1";

    private class FaultSearchTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpJsonSend.faultSearch(DebuggingSearchListActivity.this,keywords, pageSize,pageNo);
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
                    if (url.contains(HttpConfig.url_fault_search)) {
                        boolean is_success = intent.getBooleanExtra("is_success", false);
                        LogUtil.printPushLog("mHttpReceiver url_fault_search :" + is_success);
                        if (is_success) {
                            //解析data再发送结果
                            String data = intent.getStringExtra("data");
                            try {
                                mDebugListData = HttpJsonAnaly.faultSearch(data, DebuggingSearchListActivity.this);
                                if (mDebugListData.isFlag()) {
                                    mHandler.sendEmptyMessage(FIND_LIST_SUCCESS);
                                } else {
                                    mHandler.sendEmptyMessage(FIND_LIST_FALSE);
                                }
                                LogUtil.printPushLog("mHttpReceiver mDebugListData.toString :" + mDebugListData.toString());
                            } catch (Exception e) {
                                mHandler.sendEmptyMessage(FIND_LIST_FALSE);
                                e.printStackTrace();
                            }
                        } else {
                            mHandler.sendEmptyMessage(FIND_LIST_FALSE);
                        }
                    }
                }
            }
        }

    }
}
