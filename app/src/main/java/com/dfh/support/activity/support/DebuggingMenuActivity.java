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
import com.dfh.support.activity.adapter.DebuggingMenuAdapter;
import com.dfh.support.utils.ActionBarUtil;

import java.util.ArrayList;

public class DebuggingMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs;

    private ListView mLvDebuggingMenu;
    private ArrayList<String> mDebuggingMenuList = new ArrayList<String>();
    private DebuggingMenuAdapter mDebuggingMenuAdapter;
    
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_debugging_menu);
        initView();
        initListener();
    }

    private void initListener() {
        mLvDebuggingMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DebuggingMenuActivity.this,DebuggingListActivity.class);
                startActivity(intent);

            }
        });
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void initView() {
        mLvDebuggingMenu = (ListView)findViewById(R.id.lv_debugging_menu);
        mDebuggingMenuList.add("");
        mDebuggingMenuList.add("");
        mDebuggingMenuList.add("");
        mDebuggingMenuList.add("");
        mDebuggingMenuAdapter = new DebuggingMenuAdapter(this,mDebuggingMenuList);
        mLvDebuggingMenu.setAdapter(mDebuggingMenuAdapter);
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
                Intent intent = new Intent(DebuggingMenuActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }
}
