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

import com.dfh.support.R;
import com.dfh.support.activity.adapter.SparePartsListAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.utils.ActionBarUtil;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_about_us);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(AboutUsActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }
}
