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
import android.widget.ListView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.SparePartsDetailAdapter;
import com.dfh.support.activity.adapter.SparePartsMenuAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.ClassifyListData;
import com.dfh.support.entity.PartsData;
import com.dfh.support.entity.PictureVOData;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;
import com.dfh.support.utils.ToastUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SparePartsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;
    private ChildrenListView mLvPic;
    private ArrayList<PictureVOData> mPicList = new ArrayList<PictureVOData>();
    private SparePartsDetailAdapter mSparePartsDetailAdapter;

    private TextView tvName;
    private TextView tvValue;
    private TextView tvContent;
    private TextView tvText;
    private TextView tvRemark;

    private static final int PARTS_FIND_SUCCESS = 1;
    private static final int PARTS_FIND_FALSE = 2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PARTS_FIND_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    //刷新内容
                    tvName.setText(mPartsData.getName());
                    tvValue.setText(mPartsData.getPpPrice());
                    tvContent.setText(getResources().getString(R.string.common_specs)+mPartsData.getSpecs());
                    tvText.setText(getResources().getString(R.string.common_explain)+mPartsData.getIntro());
                    if(!TextUtils.isEmpty(mPartsData.getTips()))tvRemark.setText(mPartsData.getTips());
                    mPicList = mPartsData.getPictureVOData();
                    mSparePartsDetailAdapter.setList(mPicList);
                    break;
                case PARTS_FIND_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    ToastUtils.shortToast(SparePartsDetailActivity.this,  HttpJsonAnaly.lastError);
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
        setContentView(R.layout.activity_spare_parts_detail);
        initView();
        initListener();
        LoadingProgressDialog.show(SparePartsDetailActivity.this, false, true, 30000);
        mPartsIdDetailTask = new PartsIdDetailTask();
        mPartsIdDetailTask.execute("");
    }

    private void initListener() {
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvPic = (ChildrenListView)findViewById(R.id.lv_spare_parts_pic_list);
        mSparePartsDetailAdapter = new SparePartsDetailAdapter(this,mPicList);
        mLvPic.setAdapter(mSparePartsDetailAdapter);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvValue = (TextView) findViewById(R.id.tv_value);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvText = (TextView) findViewById(R.id.tv_text);
        tvRemark = (TextView) findViewById(R.id.tv_remark);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(SparePartsDetailActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }
    private PartsData mPartsData;
    private PartsIdDetailTask mPartsIdDetailTask;
    private String id = "";

    private class PartsIdDetailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mPartsData =  HttpJsonSend.partsIdDetail(SparePartsDetailActivity.this,id);
            if (mPartsData.isFlag()) {
                mHandler.sendEmptyMessage(PARTS_FIND_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(PARTS_FIND_FALSE);
            }
            LogUtil.printPushLog("httpGet partsIdDetail mPartsData" + mPartsData.toString());
            return null;
        }
    }
}
