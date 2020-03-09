package com.dfh.support.activity.support;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
import com.dfh.support.utils.ActionBarUtil;

import java.util.ArrayList;

public class SparePartsMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;
    private ListView mLvDevice;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    private SparePartsMenuAdapter mSparePartsMenuAdapter;

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
    }

    private void initListener() {
        mLvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SparePartsMenuActivity.this,SparePartsListActivity.class);
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
        mDeviceList.add("");
        mDeviceList.add("");
        mDeviceList.add("");
        mDeviceList.add("");
        mSparePartsMenuAdapter = new SparePartsMenuAdapter(this,mDeviceList);
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
}
