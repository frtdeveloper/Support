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
import android.widget.ListView;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.SparePartsListAdapter;
import com.dfh.support.activity.adapter.SparePartsMenuAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.utils.ActionBarUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class SparePartsListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;
    private ChildrenListView mLvSpareParts;
    private ArrayList<String> mSparePartsList = new ArrayList<String>();
    private SparePartsListAdapter mSparePartsListAdapter;
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
    }

    private void initListener() {
        mLvSpareParts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SparePartsListActivity.this,SparePartsDetailActivity.class);
                startActivity(intent);
            }
        });
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvSpareParts = (ChildrenListView)findViewById(R.id.lv_spare_parts_list);
        mSparePartsList.add("");
        mSparePartsList.add("");
        mSparePartsList.add("");
        mSparePartsList.add("");
        mSparePartsListAdapter = new SparePartsListAdapter(this,mSparePartsList);
        mLvSpareParts.setAdapter(mSparePartsListAdapter);
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
}
