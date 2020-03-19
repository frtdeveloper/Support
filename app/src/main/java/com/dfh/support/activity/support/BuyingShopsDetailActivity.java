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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.DialogListAdapter;
import com.dfh.support.activity.adapter.SparePartsDetailAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.activity.widget.ListConfirmDialog;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.PictureVOData;
import com.dfh.support.entity.ServeData;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.CallPhoneUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;
import com.dfh.support.utils.ToastUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BuyingShopsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;
    private TextView mTvName;
    private TextView mTvContactName;
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
                    mTvContactName.setText(getResources().getString(R.string.common_contact)+"："+mServeData.getContact());
                    mTvTime.setText(getResources().getString(R.string.service_time)+"："+mServeData.getTime());
                    mTvPhone.setText(getResources().getString(R.string.service_phone)+"："+mServeData.getTel());
                    mTvServiceContent.setText(mServeData.getScope());
                    mTvSpecialReminder.setText(mServeData.getTips());
                    mPicList = mServeData.getPictureVOData();
                    mSparePartsDetailAdapter.setList(mPicList);
                    break;
                case SERVE_DETAIL_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    ToastUtils.shortToast(BuyingShopsDetailActivity.this,  HttpJsonAnaly.lastError);
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
        setContentView(R.layout.activity_buying_shops_detail);
        initView();
        initListener();
        LoadingProgressDialog.show(BuyingShopsDetailActivity.this, false, true, 30000);
        id = getIntent().getStringExtra("id");
        distance = getIntent().getStringExtra("distance");
        mServeIdDetailTask = new ServeIdDetailTask();
        mServeIdDetailTask.execute("");
    }


    private void initListener() {
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvPhone.setOnClickListener(this);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvPic = (ChildrenListView)findViewById(R.id.lv_pic);
        mSparePartsDetailAdapter = new SparePartsDetailAdapter(this,mPicList);
        mLvPic.setAdapter(mSparePartsDetailAdapter);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvContactName = (TextView) findViewById(R.id.tv_contact_name);
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
                Intent intent = new Intent(BuyingShopsDetailActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_phone:
                if(mServeData!=null) {
                    if(!TextUtils.isEmpty(mServeData.getTel())) {
                        showListConfirmDialog(mServeData.getTel());
                    }
                }
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
            mServeData =  HttpJsonSend.serveIdDetail(BuyingShopsDetailActivity.this,id);
            if (mServeData.isFlag()) {
                mHandler.sendEmptyMessage(SERVE_DETAIL_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(SERVE_DETAIL_FALSE);
            }
            LogUtil.printPushLog("httpGet serveIdDetail mServeData" + mServeData.toString());
            return null;
        }
    }


    private ListConfirmDialog mListConfirmDialog;

    private ListView mLvSelectRoles;
    private DialogListAdapter dialogListAdapter;
    private ArrayList<String> phoneList = new ArrayList<String>();

    public void showListConfirmDialog(String phone) {
        phoneList = new ArrayList<String>();
        if(phone.contains("/")){
            String[] phones = phone.split("/");
            for(int i = 0;i<phones.length;i++){
                String p = phones[i];
                phoneList.add(p);
            }
        }else {
            phoneList.add(phone);
        }
        mListConfirmDialog = new ListConfirmDialog(BuyingShopsDetailActivity.this,
                R.style.share_dialog);
        mListConfirmDialog.show();
        Window window = mListConfirmDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1f;
        window.setAttributes(lp);
        mLvSelectRoles = (ListView) window.findViewById(R.id.lv_select_roles);
        dialogListAdapter = new DialogListAdapter(BuyingShopsDetailActivity.this, phoneList);
        mLvSelectRoles.setAdapter(dialogListAdapter);
        mLvSelectRoles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CallPhoneUtil.callPhone(BuyingShopsDetailActivity.this,phoneList.get(position));
                mListConfirmDialog.dismiss();
            }
        });
    }
}
