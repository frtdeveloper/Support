package com.dfh.support.activity.support;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.SupportApplication;
import com.dfh.support.activity.adapter.SearchHistoryAdapter;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.DebugListData;
import com.dfh.support.entity.PartsListData;
import com.dfh.support.http.HttpConfig;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.SettingSharedPerferencesUtil;
import com.dfh.support.utils.TextUtils;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private EditText mEtSearch;
    private TextView mTvClear;
    private GridView mGvHistory;
    private ArrayList<String> mHistoryList = new ArrayList<String>();
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private ImageView mIvPic;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBar(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_search);
        initView();
        initListener();
    }

    private void initView() {
        mHistoryList = new ArrayList<String>();
        String historyData = SettingSharedPerferencesUtil.GetSearchHistoryValueConfig(SearchActivity.this);
        LogUtil.printPushLog("initView historyData" + historyData);
        if(!TextUtils.isEmpty(historyData)){
            if(historyData.contains(",")){
                LogUtil.printPushLog("initView historyData" + historyData);
                String[] historyList = historyData.split(",");
                for(int i = 0 ;i<historyList.length;i++){
                    mHistoryList.add(historyList[i]);
                }
            }else{
                mHistoryList.add(historyData);
            }
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mTvClear = (TextView) findViewById(R.id.tv_clear);
        mGvHistory = (GridView) findViewById(R.id.gv_history);
        mSearchHistoryAdapter = new SearchHistoryAdapter(this, mHistoryList);
        mGvHistory.setAdapter(mSearchHistoryAdapter);
        mGvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, DebuggingSearchListActivity.class);
                intent.putExtra("keywords", mHistoryList.get(i));
                startActivity(intent);
                finish();
            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (goSearch) {
                    goSearch = false;
                    String content = mEtSearch.getText().toString();
                    if (!TextUtils.isEmpty(content)&&!content.contains(",")) {
                        String history = SettingSharedPerferencesUtil.GetSearchHistoryValueConfig(SearchActivity.this);
                        if (!TextUtils.isEmpty(history)) {
                            if(history.contains(",")){
                                String[] historyList = history.split(",");
                                boolean hasHistory = false;
                                for(int a = 0 ;a<historyList.length;a++){
                                    if(!historyList[a].equals(content))hasHistory = true;
                                }
                                if(!hasHistory){
                                    history = history + "," + content;
                                }
                            }else{
                                if(!history.equals(content)){
                                    history = history + "," + content;
                                }
                            }
                        } else {
                            history = content;
                        }
                        SettingSharedPerferencesUtil.SetSearchHistoryValue(SearchActivity.this,history);
                    }
                    Intent intent = new Intent(SearchActivity.this, DebuggingSearchListActivity.class);
                    intent.putExtra("keywords", content);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }

    private boolean goSearch = true;

    private void initListener() {
        mIvBack.setOnClickListener(this);
        mTvClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_clear:
                SettingSharedPerferencesUtil.SetSearchHistoryValue(SearchActivity.this,"");
                mHistoryList = new ArrayList<String>();
                mSearchHistoryAdapter.setList(mHistoryList);
                break;
        }
    }

}
