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
import com.dfh.support.activity.adapter.DebuggingMenuAdapter;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.ClassifyListData;
import com.dfh.support.entity.DebugMenuData;
import com.dfh.support.entity.DebugMenuListData;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.ToastUtils;

import java.util.ArrayList;

public class DebuggingMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;

    private ListView mLvDebuggingMenu;
    private ArrayList<DebugMenuListData> mList = new ArrayList<DebugMenuListData>();
    private DebuggingMenuAdapter mDebuggingMenuAdapter;

    private static final int FIND_SUCCESS = 1;
    private static final int FIND_FALSE = 2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FIND_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    //刷新list
                    for(int i = 0;i< mDebugMenuListData.getDebugMenuData().size();i=i+2){
                        ArrayList<DebugMenuData> debugMenuDataList = new ArrayList<DebugMenuData>();
                        DebugMenuListData debugMenuListData = new DebugMenuListData();
                        if(i<mDebugMenuListData.getDebugMenuData().size())
                            debugMenuDataList.add(mDebugMenuListData.getDebugMenuData().get(i));
                        if(i+1<mDebugMenuListData.getDebugMenuData().size())
                            debugMenuDataList.add(mDebugMenuListData.getDebugMenuData().get(i+1));
                        debugMenuListData.setDebugMenuData(debugMenuDataList);
                        mList.add(debugMenuListData);
                    }
                    mDebuggingMenuAdapter.setList(mList);
                    break;
                case FIND_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    ToastUtils.shortToast(DebuggingMenuActivity.this,  HttpJsonAnaly.lastError);
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
        setContentView(R.layout.activity_debugging_menu);
        initView();
        initListener();
        LoadingProgressDialog.show(DebuggingMenuActivity.this, false, true, 30000);
        mFaultFindClassifyTask = new FaultFindClassifyTask();
        mFaultFindClassifyTask.execute("");
    }

    private void initListener() {
//        mLvDebuggingMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(DebuggingMenuActivity.this,DebuggingListActivity.class);
//                startActivity(intent);
//
//            }
//        });
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        mLvDebuggingMenu = (ListView)findViewById(R.id.lv_debugging_menu);
        mDebuggingMenuAdapter = new DebuggingMenuAdapter(this,mList);
        mLvDebuggingMenu.setAdapter(mDebuggingMenuAdapter);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(DebuggingMenuActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }
    private DebugMenuListData mDebugMenuListData;
    private FaultFindClassifyTask mFaultFindClassifyTask;

    private class FaultFindClassifyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mDebugMenuListData =  HttpJsonSend.faultFindClassify(DebuggingMenuActivity.this);
            if (mDebugMenuListData.isFlag()) {
                mHandler.sendEmptyMessage(FIND_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(FIND_FALSE);
            }
            LogUtil.printPushLog("httpGet faultFindClassify mDebugMenuListData" + mDebugMenuListData.toString());
            return null;
        }
    }
}
