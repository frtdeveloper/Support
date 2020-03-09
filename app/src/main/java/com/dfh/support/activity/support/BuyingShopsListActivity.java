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
import com.dfh.support.activity.adapter.BuyingShopsListAdapter;
import com.dfh.support.activity.adapter.ServiceListAdapter;
import com.dfh.support.utils.ActionBarUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class BuyingShopsListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;
    private LinearLayout mLlSearch;

    private ListView mLvBuyingShops;
    private ArrayList<String> mBuyingShopsList = new ArrayList<String>();
    private BuyingShopsListAdapter mBuyingShopsListAdapter;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_buying_shops_list);
        initView();
        initListener();
    }

    private void initListener() {
        mLvBuyingShops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BuyingShopsListActivity.this,BuyingShopsDetailActivity.class);
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
        mLvBuyingShops = (ListView)findViewById(R.id.lv_buying_shops_list);
        mBuyingShopsList.add("");
        mBuyingShopsList.add("");
        mBuyingShopsList.add("");
        mBuyingShopsList.add("");
        mBuyingShopsListAdapter = new BuyingShopsListAdapter(this,mBuyingShopsList);
        mLvBuyingShops.setAdapter(mBuyingShopsListAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(BuyingShopsListActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_search:
                intent = new Intent(BuyingShopsListActivity.this, SearchCityActivity.class);
                startActivity(intent);
                break;
        }
    }
}
