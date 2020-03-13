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

import com.dfh.support.R;
import com.dfh.support.SupportApplication;
import com.dfh.support.activity.adapter.SparePartsListAdapter;
import com.dfh.support.activity.adapter.SparePartsMenuAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.activity.widget.LoadListView;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.AdvertisementListData;
import com.dfh.support.entity.PartsData;
import com.dfh.support.entity.PartsListData;
import com.dfh.support.http.HttpConfig;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;
import com.dfh.support.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SparePartsListActivity extends AppCompatActivity implements View.OnClickListener,LoadListView.ILoadListener2{

    private ImageView mIvBack, mIvContactUs;
    private LoadListView mLvSpareParts;
    private ArrayList<PartsData> mSparePartsList = new ArrayList<PartsData>();
    private SparePartsListAdapter mSparePartsListAdapter;
    private static final int PD_LIST_SUCCESS = 1;
    private static final int PD_LIST_FALSE = 2;
    private static final int GET_LIST_PD = 3;
    private static final int REPECT_GET = 30*1000;
    private ImageView mIvPic;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PD_LIST_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    //刷新list
                    mSparePartsList.addAll(partsListData.getPartsData());
                    mSparePartsListAdapter.setList(mSparePartsList);
                    mLvSpareParts.loadComplete();
                    break;
                case PD_LIST_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    mHandler.sendEmptyMessageDelayed(GET_LIST_PD,REPECT_GET);
                    mLvSpareParts.loadComplete();
                    break;
                case GET_LIST_PD:
                    LoadingProgressDialog.show(SparePartsListActivity.this, false, true, 30000);
                    mPartsFindClassifyIdPagerTask = new PartsFindClassifyIdPagerTask();
                    mPartsFindClassifyIdPagerTask.execute("");
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
        setContentView(R.layout.activity_spare_parts_list);
        initView();
        initListener();
        mHttpReceiver = new HttpReceiver();//广播接受者实例
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SupportApplication.ACTION_HTTP_RESULT);
        registerReceiver(mHttpReceiver, intentFilter);
        mHandler.sendEmptyMessage(GET_LIST_PD);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHttpReceiver);
    }

    private void initListener() {
        mLvSpareParts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SparePartsListActivity.this,SparePartsDetailActivity.class);
                intent.putExtra("id",mSparePartsList.get(i).getId());
                startActivity(intent);
            }
        });
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private String image = "";
    private void initView() {
        image = getIntent().getStringExtra("image");
        partsClassifyId = getIntent().getStringExtra("partsClassifyId");
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvSpareParts = (LoadListView)findViewById(R.id.lv_spare_parts_list);
        mSparePartsListAdapter = new SparePartsListAdapter(this,mSparePartsList);
        mLvSpareParts.setAdapter(mSparePartsListAdapter);
        mIvPic = (ImageView) findViewById(R.id.iv_pic);
        if(!TextUtils.isEmpty(image)) ImageLoader.getInstance().displayImage(image,mIvPic);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(SparePartsListActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLoad() {
        pageNo = pageNo+1;
        mHandler.sendEmptyMessage(GET_LIST_PD);
    }
    private PartsFindClassifyIdPagerTask mPartsFindClassifyIdPagerTask;
    private PartsListData partsListData;
    private String partsClassifyId = "";
    private String pageSize = "20";
    private String pageNo = "1";

    private class PartsFindClassifyIdPagerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpJsonSend.partsFindClassifyIdPager(SparePartsListActivity.this,partsClassifyId, pageSize,pageNo);
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
                    if (url.contains(HttpConfig.url_parts_find_classify_id_pager)) {
                        boolean is_success = intent.getBooleanExtra("is_success", false);
                        LogUtil.printPushLog("mHttpReceiver url_parts_find_classify_id_pager :" + is_success);
                        if (is_success) {
                            //解析data再发送结果
                            String data = intent.getStringExtra("data");
                            LogUtil.printPushLog("mHttpReceiver url_parts_find_classify_id_pager :" + data);
                            try {
                                partsListData = HttpJsonAnaly.partsFindClassifyIdPager(data, SparePartsListActivity.this);
                                if (partsListData.isFlag()) {
                                    mHandler.sendEmptyMessage(PD_LIST_SUCCESS);
                                } else {
                                    mHandler.sendEmptyMessage(PD_LIST_FALSE);
                                }
                                LogUtil.printPushLog("mHttpReceiver partsListData.toString :" + partsListData.toString());
                            } catch (Exception e) {
                                mHandler.sendEmptyMessage(PD_LIST_FALSE);
                                e.printStackTrace();
                            }
                        } else {
                            mHandler.sendEmptyMessage(PD_LIST_FALSE);
                        }
                    }
                }
            }
        }

    }
}
