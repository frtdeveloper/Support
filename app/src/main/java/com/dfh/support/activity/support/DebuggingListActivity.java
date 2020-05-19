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
import android.widget.ListView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.SupportApplication;
import com.dfh.support.activity.TitleWebViewActivity;
import com.dfh.support.activity.adapter.DebuggingListAdapter;
import com.dfh.support.activity.widget.LoadListView;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.DebugDetailData;
import com.dfh.support.entity.DebugListData;
import com.dfh.support.entity.DebugMenuData;
import com.dfh.support.entity.DebugListData;
import com.dfh.support.entity.PartsListData;
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

public class DebuggingListActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mIvBack, mIvContactUs;
    private TextView mTvTitle;
    private String mTitle = "";
    private LoadListView mLvDebuggingList;
    private ArrayList<DebugDetailData> mDebuggingList = new ArrayList<DebugDetailData>();
    private DebuggingListAdapter mDebuggingListAdapter;

    private static final int FIND_SUCCESS = 1;
    private static final int FIND_FALSE = 2;
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
                    ToastUtils.shortToast(DebuggingListActivity.this,  HttpJsonAnaly.lastError);
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
        setContentView(R.layout.activity_debugging_list);
        initView();
        initListener();
        LoadingProgressDialog.show(DebuggingListActivity.this, false, true, 30000);
        mFaultFindIdListTask = new FaultFindIdListTask();
        mFaultFindIdListTask.execute("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initListener() {
        mLvDebuggingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DebuggingListActivity.this, DebuggingDetailActivity.class);
                //intent.putExtra("title",mDebuggingList.get(i).getTitle());
                    intent.putExtra("title", mTitle);
                intent.putExtra("id",mDebuggingList.get(i).getId());
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
                Intent intent = new Intent(DebuggingListActivity.this, CallPhoneActivity.class);
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
            mDebugListData =  HttpJsonSend.faultFindIdList(DebuggingListActivity.this,id);
            if (mDebugListData.isFlag()) {
                mHandler.sendEmptyMessage(FIND_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(FIND_FALSE);
            }
            LogUtil.printPushLog("httpGet faultFindIdList mDebugListData" + mDebugListData.toString());
            return null;
        }
    }
}
