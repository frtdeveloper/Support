package com.dfh.support.activity.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dfh.support.R;
import com.dfh.support.activity.TitleWebViewActivity;
import com.dfh.support.activity.adapter.DebuggingListAdapter;
import com.dfh.support.utils.ActionBarUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class DebuggingListActivity extends AppCompatActivity {

    private ListView mLvDebuggingList;
    private ArrayList<String> mDebuggingList = new ArrayList<String>();
    private DebuggingListAdapter mDebuggingListAdapter;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_debugging_list);
        initView();
        initListener();
    }

    private void initListener() {
        mLvDebuggingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DebuggingListActivity.this, TitleWebViewActivity.class);
                intent.putExtra("title","屏幕问题");
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mLvDebuggingList = (ListView)findViewById(R.id.lv_debugging_list);
        mDebuggingList.add("");
        mDebuggingList.add("");
        mDebuggingList.add("");
        mDebuggingList.add("");
        mDebuggingListAdapter = new DebuggingListAdapter(this,mDebuggingList);
        mLvDebuggingList.setAdapter(mDebuggingListAdapter);
    }
}
