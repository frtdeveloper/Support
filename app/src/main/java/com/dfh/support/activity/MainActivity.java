package com.dfh.support.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dfh.support.R;
import com.dfh.support.activity.fragment.*;
import com.dfh.support.activity.fragment.RecommendFragment;
import com.dfh.support.activity.fragment.SupportFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        showRecommendFragment();
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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, RecommendFragment.newInstance()).commit();
    }

    private void showSupportFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, SupportFragment.newInstance()).commit();
    }

}
