package com.dfh.support.activity.support;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.SparePartsDetailAdapter;
import com.dfh.support.activity.adapter.SparePartsListAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.PartsData;
import com.dfh.support.entity.PictureVOData;
import com.dfh.support.entity.ServeData;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;
import com.dfh.support.utils.ToastUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ServiceDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;
    private TextView mTvName;
    private TextView mTvDistance;
    private TextView mTvAddress;
    private TextView mTvTime;
    private TextView mTvPhone;
    private TextView mTvServiceContent;
    private TextView mTvSpecialReminder;
    private ChildrenListView mLvPic;
    private ArrayList<PictureVOData> mPicList = new ArrayList<PictureVOData>();
    private SparePartsDetailAdapter mSparePartsDetailAdapter;


    private static final int SERVE_DETAIL_SUCCESS = 1;
    private static final int SERVE_DETAIL_FALSE = 2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SERVE_DETAIL_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    //刷新内容
                    mTvName.setText(mServeData.getName());
                    if(!TextUtils.isEmpty(distance)) mTvDistance.setText(distance);
                    mTvAddress.setText(mServeData.getAddress());
                    mTvTime.setText(mServeData.getTime());
                    mTvPhone.setText(mServeData.getTel());
                    mTvServiceContent.setText(mServeData.getScope());
                    mTvSpecialReminder.setText(mServeData.getTips());
                    mPicList = mServeData.getPictureVOData();
                    mSparePartsDetailAdapter.setList(mPicList);
                    break;
                case SERVE_DETAIL_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    ToastUtils.shortToast(ServiceDetailActivity.this,  HttpJsonAnaly.lastError);
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
        setContentView(R.layout.activity_service_detail);
        initView();
        initListener();
        LoadingProgressDialog.show(ServiceDetailActivity.this, false, true, 30000);
        id = getIntent().getStringExtra("id");
        distance = getIntent().getStringExtra("distance");
        mServeIdDetailTask = new ServeIdDetailTask();
        mServeIdDetailTask.execute("");
    }

    private void initListener() {
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvPic = (ChildrenListView)findViewById(R.id.lv_pic);
        mSparePartsDetailAdapter = new SparePartsDetailAdapter(this,mPicList);
        mLvPic.setAdapter(mSparePartsDetailAdapter);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvDistance = (TextView) findViewById(R.id.tv_distance);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvServiceContent = (TextView) findViewById(R.id.tv_service_content);
        mTvSpecialReminder = (TextView) findViewById(R.id.tv_special_reminder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(ServiceDetailActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }
    private ServeData mServeData;
    private ServeIdDetailTask mServeIdDetailTask;
    private String id = "";
    private String distance = "";

    private class ServeIdDetailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mServeData =  HttpJsonSend.serveIdDetail(ServiceDetailActivity.this,id);
            if (mServeData.isFlag()) {
                mHandler.sendEmptyMessage(SERVE_DETAIL_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(SERVE_DETAIL_FALSE);
            }
            LogUtil.printPushLog("httpGet serveIdDetail mServeData" + mServeData.toString());
            return null;
        }
    }
}
