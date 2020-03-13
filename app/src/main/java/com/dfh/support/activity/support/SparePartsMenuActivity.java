package com.dfh.support.activity.support;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.dfh.support.R;
import com.dfh.support.activity.WebViewActivity;
import com.dfh.support.activity.adapter.ServiceListAdapter;
import com.dfh.support.activity.adapter.SparePartsMenuAdapter;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.AdvertisementListData;
import com.dfh.support.entity.ClassifyData;
import com.dfh.support.entity.ClassifyListData;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.ToastUtils;

import java.util.ArrayList;

public class SparePartsMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;
    private ListView mLvDevice;
    private ArrayList<ClassifyData> mList = new ArrayList<ClassifyData>();
    private SparePartsMenuAdapter mSparePartsMenuAdapter;

    private static final int PARTS_FIND_SUCCESS = 1;
    private static final int PARTS_FIND_FALSE = 2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PARTS_FIND_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    //刷新list
                    mList = mClassifyListData.getClassifyData();
                    mSparePartsMenuAdapter.setList(mList);
                    break;
                case PARTS_FIND_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    ToastUtils.shortToast(SparePartsMenuActivity.this,  HttpJsonAnaly.lastError);
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
        setContentView(R.layout.activity_spare_parts_menu);
        initView();
        initListener();
        LoadingProgressDialog.show(SparePartsMenuActivity.this, false, true, 30000);
        mPartsFindClassifyTask = new PartsFindClassifyTask();
        mPartsFindClassifyTask.execute("");
    }

    private void initListener() {
        mLvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SparePartsMenuActivity.this,SparePartsListActivity.class);
                intent.putExtra("partsClassifyId",mList.get(i).getId());
                intent.putExtra("image",mList.get(i).getIcon());
                startActivity(intent);
            }
        });
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvDevice = (ListView)findViewById(R.id.lv_device_list);
        mSparePartsMenuAdapter = new SparePartsMenuAdapter(this,mList);
        mLvDevice.setAdapter(mSparePartsMenuAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(SparePartsMenuActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }
    private ClassifyListData mClassifyListData;
    private PartsFindClassifyTask mPartsFindClassifyTask;

    private class PartsFindClassifyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mClassifyListData =  HttpJsonSend.partsFindClassify(SparePartsMenuActivity.this);
            if (mClassifyListData.isFlag()) {
                mHandler.sendEmptyMessage(PARTS_FIND_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(PARTS_FIND_FALSE);
            }
            LogUtil.printPushLog("httpGet partsFindClassify classifyListData" + mClassifyListData.toString());
            return null;
        }
    }
}
