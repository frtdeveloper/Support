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
import com.dfh.support.activity.adapter.SparePartsDetailAdapter;
import com.dfh.support.activity.adapter.SparePartsMenuAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.utils.ActionBarUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class SparePartsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;
    private ChildrenListView mLvPic;
    private ArrayList<String> mPicList = new ArrayList<String>();
    private SparePartsDetailAdapter mSparePartsDetailAdapter;

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
    }

    private void initListener() {
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvPic = (ChildrenListView)findViewById(R.id.lv_spare_parts_pic_list);
        mPicList.add("");
        mPicList.add("");
        mSparePartsDetailAdapter = new SparePartsDetailAdapter(this,mPicList);
        mLvPic.setAdapter(mSparePartsDetailAdapter);
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
}
