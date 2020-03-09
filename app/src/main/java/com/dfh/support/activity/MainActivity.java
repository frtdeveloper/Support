package com.dfh.support.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dfh.support.R;
import com.dfh.support.activity.fragment.*;
import com.dfh.support.activity.fragment.RecommendFragment;
import com.dfh.support.activity.fragment.SupportFragment;
import com.dfh.support.utils.ActionBarUtil;
import com.umeng.message.PushAgent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvRecommend,mIvSupport;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBar(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        PushAgent.getInstance(this).onAppStart();
        initView();
        showRecommendFragment();
    }

    private void initView() {
        mIvRecommend = (ImageView)findViewById(R.id.iv_main_nav_recommend);
        mIvSupport = (ImageView)findViewById(R.id.iv_main_nav_support);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_nav_recommend:
                showRecommendFragment();
                break;
            case R.id.main_nav_support:
                showSupportFragment();
                break;
        }
    }

    private void showRecommendFragment(){
        mIvRecommend.setImageResource(R.mipmap.btn_recommend_press);
        mIvSupport.setImageResource(R.mipmap.btn_support_normal);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, RecommendFragment.newInstance()).commit();
    }

    private void showSupportFragment(){
        mIvRecommend.setImageResource(R.mipmap.btn_recommend_normal);
        mIvSupport.setImageResource(R.mipmap.btn_support_press);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, SupportFragment.newInstance()).commit();
    }

}
