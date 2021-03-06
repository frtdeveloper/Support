package com.dfh.support.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.dfh.support.R;
import com.dfh.support.SupportApplication;
import com.dfh.support.activity.adapter.MainFragmentAdapter;
import com.dfh.support.activity.fragment.*;
import com.dfh.support.activity.fragment.RecommendFragment;
import com.dfh.support.activity.fragment.SupportFragment;
import com.dfh.support.activity.widget.CustomViewPager;
import com.dfh.support.compose.UmentBroadcastReceiver;
import com.dfh.support.http.HttpConfig;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int currentItem;
    private CustomViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private ImageView mIvRecommend,mIvSupport,mIvHint;
    private static final int RECEIVER_UPDATE = 8;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case RECEIVER_UPDATE:
                    if(currentItem!=1){
                        mIvHint.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleWhiteColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        PushAgent.getInstance(this).onAppStart();
        SupportApplication.isOpenMain = true;
        initView();
        //showSupportFragment();
        showRecommendFragment();
        mHttpReceiver = new HttpReceiver();//广播接受者实例
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UmentBroadcastReceiver.INTENT_MSG_STR);
        registerReceiver(mHttpReceiver, intentFilter);
        showNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SupportApplication.isOpenMain = false;
        unregisterReceiver(mHttpReceiver);
    }
    private void initView() {
        mIvHint = (ImageView)findViewById(R.id.iv_hint);
        mPager = (CustomViewPager) findViewById(R.id.viewpager);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new SupportFragment());
        fragmentList.add(new RecommendFragment());
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
                mIvHint.setVisibility(View.GONE);
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
        currentItem = 1;
        mPager.setCurrentItem(currentItem);
        LogUtil.setUnReadCount(MainActivity.this, 0);
    }

    private void showSupportFragment(){
        mIvRecommend.setImageResource(R.mipmap.btn_recommend_normal);
        mIvSupport.setImageResource(R.mipmap.btn_support_press);
        currentItem = 0;
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

    private HttpReceiver mHttpReceiver;

    public class HttpReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.printPushLog("mHttpReceiver action :" + action);
            if(action.equals(UmentBroadcastReceiver.INTENT_MSG_STR)){
                //先判断url
                String message = intent.getStringExtra(UmentBroadcastReceiver.UMENG_MESSAGE);
                if (message != null && !TextUtils.isEmpty(message)) {
                    //推送刷新RECEIVER_UPDATE
                    LogUtil.printPushLog("SupportApplication::Main RECEIVER_UPDATE");
                    mHandler.sendEmptyMessage(RECEIVER_UPDATE);
                }
            }
        }

    }

    private void showNotification() {
        NotificationManagerCompat notification = NotificationManagerCompat.from(this);
        boolean isEnabled = notification.areNotificationsEnabled();
        if (!isEnabled) {
            //未打开通知
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setCancelable(false)
                    .setMessage("请在“通知”中打开通知权限")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("android.provider.extra.APP_PACKAGE", MainActivity.this.getPackageName());
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", MainActivity.this.getPackageName());
                                intent.putExtra("app_uid", MainActivity.this.getApplicationInfo().uid);
                                startActivity(intent);
                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                            } else if (Build.VERSION.SDK_INT >= 15) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), null));
                            }
                            startActivity(intent);
                            dialog.cancel();
                        }
                    })
                    .create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        }
    }
}
