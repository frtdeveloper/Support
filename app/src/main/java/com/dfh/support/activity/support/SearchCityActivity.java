package com.dfh.support.activity.support;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.SearchHistoryAdapter;
import com.dfh.support.utils.ActionBarUtil;

import java.util.ArrayList;

public class SearchCityActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private EditText mEtSearch;
    private GridView mGvHistory;
    private ArrayList<String> mHistoryList = new ArrayList<String>();
    private SearchHistoryAdapter mSearchHistoryAdapter;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBar(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_search_city);
        initView();
        initListener();
    }

    private void initView() {
        mHistoryList = new ArrayList<String>();
        mHistoryList.add("北京");
        mHistoryList.add("上海");
        mHistoryList.add("深圳");
        mHistoryList.add("广州");
        mHistoryList.add("成都");
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mGvHistory = (GridView) findViewById(R.id.gv_history);
        mSearchHistoryAdapter = new SearchHistoryAdapter(this, mHistoryList);
        mGvHistory.setAdapter(mSearchHistoryAdapter);
    }

    private void initListener() {
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_clear:
                mHistoryList = new ArrayList<String>();
                mSearchHistoryAdapter.setList(mHistoryList);
                break;
        }
    }
}
