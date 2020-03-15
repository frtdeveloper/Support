package com.dfh.support.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.dfh.support.R;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.PermissionUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class LoadingActivity extends AppCompatActivity{ //implements PermissionUtil.PermissionCallBack {

    protected PermissionUtil mPermissionUtil;
    private static final int PERMISSION_CODE = 100;
    private static final int LOADING_TIME = 3000;
    private static final int GO_LOGIN = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_LOGIN:
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBar(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_loading);
//        mPermissionUtil = PermissionUtil.getInstance();
//        if (!mPermissionUtil.requestPermissions(LoadingActivity.this, PERMISSION_CODE)) {
//            login();
//        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
            } else{
                finish();
            }
        }else{
            login();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtil.printActivityLog("onRequestPermissionsResult");
        if (PERMISSION_CODE == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                login();
            } else {
                finish();
            }
        }
    }

//    @Override
//    public void onPermissionGetSuccess() {
//        login();
//    }
//
//    @Override
//    public void onPermissionGetFail() {
//        finish();
//    }

    private void login() {
        LogUtil.printActivityLog("LoadingActivity GO_LOGIN");
        mHandler.sendEmptyMessageDelayed(GO_LOGIN, LOADING_TIME);
    }
}
