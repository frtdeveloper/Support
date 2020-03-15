package com.dfh.support.activity.support;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.SearchHistoryAdapter;
import com.dfh.support.entity.CityData;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.SettingSharedPerferencesUtil;

import java.util.ArrayList;

public class SearchCityActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private EditText mEtSearch;
    private TextView mTvCity;
    private GridView mGvHistory;
    private ArrayList<String> mHistoryList = new ArrayList<String>();
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private CityData mCityData;

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
        mHistoryList.add("北京市");
        mHistoryList.add("上海市");
        mHistoryList.add("深圳市");
        mHistoryList.add("广州市");
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        mGvHistory = (GridView) findViewById(R.id.gv_history);
        mSearchHistoryAdapter = new SearchHistoryAdapter(this, mHistoryList);
        mGvHistory.setAdapter(mSearchHistoryAdapter);
        mCityData = LogUtil.getGeo(SearchCityActivity.this);
    }

    private void initListener() {
        mIvBack.setOnClickListener(this);
        mGvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SettingSharedPerferencesUtil.SetSearchCityValue(SearchCityActivity.this,mHistoryList.get(i));
                finish();
            }
        });
        if(mCityData!=null){
            mTvCity.setText(mCityData.getCityName());
            mTvCity.setOnClickListener(this);
        }else{
            mTvCity.setText(getResources().getString(R.string.positioning_failure));
        }
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
            case R.id.tv_city:
                SettingSharedPerferencesUtil.SetSearchCityValue(SearchCityActivity.this,mTvCity.getText().toString());
                finish();
                break;
        }
    }
}
