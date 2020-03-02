package com.dfh.support;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dfh.support.fragment.PolicyFragment;
import com.dfh.support.fragment.RecommendFragment;
import com.dfh.support.fragment.SupportFragment;
import com.umeng.message.PushAgent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushAgent.getInstance(this).onAppStart();
        showSupportFragment();
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
            case R.id.main_nav_policy:
                showPolicyFragment();
                break;
        }
    }

    private void showRecommendFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, RecommendFragment.newInstance()).commit();
    }

    private void showSupportFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, SupportFragment.newInstance()).commit();
    }

    private void showPolicyFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, PolicyFragment.newInstance()).commit();
    }
}
