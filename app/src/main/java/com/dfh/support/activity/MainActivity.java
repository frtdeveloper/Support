package com.dfh.support.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.MainFragmentAdapter;
import com.dfh.support.activity.fragment.*;
import com.dfh.support.activity.fragment.RecommendFragment;
import com.dfh.support.activity.fragment.SupportFragment;
import com.dfh.support.activity.widget.CustomViewPager;
import com.dfh.support.utils.ActionBarUtil;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int currentItem;
    private CustomViewPager mPager;
    private ArrayList<Fragment> fragmentList;
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

        mPager = (CustomViewPager) findViewById(R.id.viewpager);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new RecommendFragment());
        fragmentList.add(new SupportFragment());
        MainFragmentAdapter adapter = new MainFragmentAdapter(
                getSupportFragmentManager(), fragmentList);
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(currentItem);
        mPager.setOffscreenPageLimit(2);
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
        currentItem = 0;
        mPager.setCurrentItem(currentItem);
    }

    private void showSupportFragment(){
        mIvRecommend.setImageResource(R.mipmap.btn_recommend_normal);
        mIvSupport.setImageResource(R.mipmap.btn_support_press);
        currentItem = 1;
        mPager.setCurrentItem(currentItem);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
        return super.onKeyDown(keyCode, event);
    }
}
