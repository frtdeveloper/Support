package com.dfh.support.activity.support;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dfh.support.R;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.CallPhoneUtil;

public class CallPhoneActivity extends AppCompatActivity  implements View.OnClickListener {

    private ImageView mIvBack;
    private LinearLayout mLlCallPhone;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_call_phone);
        initView();
        initListener();
    }

    private void initListener() {
        mIvBack.setOnClickListener(this);
        mLlCallPhone.setOnClickListener(this);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mLlCallPhone = (LinearLayout) findViewById(R.id.ll_call_phone);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_call_phone:
                CallPhoneUtil.callPhoneView(CallPhoneActivity.this,"4001688848");
                break;
        }
    }
}
