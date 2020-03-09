package com.dfh.support.activity.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.ServiceListAdapter;
import com.dfh.support.utils.ActionBarUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ServiceListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;
    private LinearLayout mLlSearch;

    private ListView mLvService;
    private ArrayList<String> mServiceList = new ArrayList<String>();
    private ServiceListAdapter mServiceListAdapter;
    
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_service_list);
        initView();
        initListener();
    }

    private void initListener() {
        mLvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ServiceListActivity.this,ServiceDetailActivity.class);
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
        mLvService = (ListView)findViewById(R.id.lv_service_list);
        mServiceList.add("");
        mServiceList.add("");
        mServiceList.add("");
        mServiceList.add("");
        mServiceListAdapter = new ServiceListAdapter(this,mServiceList);
        mLvService.setAdapter(mServiceListAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(ServiceListActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_search:
                intent = new Intent(ServiceListActivity.this, SearchCityActivity.class);
                startActivity(intent);
                break;
        }
    }
}
