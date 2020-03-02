package com.dfh.support;

import android.app.Application;
import android.content.Context;

import com.dfh.support.compose.UmentBroadcastReceiver;
import com.dfh.support.utils.LogUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

public class SupportApplication extends Application implements IUmengRegisterCallback {

    private PushAgent mPushAgent;
    private UmengMessageHandler mUmengMessageHandler;
    private String mUmentKey;

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, "5e5bc092570df3a9fc0000bb", "Umeng",
                UMConfigure.DEVICE_TYPE_PHONE, "26c496679dab7728c41b76620e90faf9");//nojtgbprhi2w5yoyludz2vplramuqktf
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(this);

    }

    @Override
    public void onSuccess(String s) {
        mUmentKey = s;
        LogUtil.printPushLog("SupportApplication::onSuccess= " + s + " key= " + mUmentKey);
        mUmengMessageHandler = new UmengMessageHandler(){
            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
                LogUtil.printPushLog("SupportApplication::dealWithNotificationMessage= " + uMessage.text);
                UmentBroadcastReceiver.sendBroadcast(SupportApplication.this, uMessage.text);
            }

            @Override
            public void dealWithCustomMessage(Context context, UMessage uMessage) {
                super.dealWithCustomMessage(context, uMessage);
                LogUtil.printPushLog("SupportApplication::dealWithCustomMessage= " + uMessage.text);
                UmentBroadcastReceiver.sendCBroadcast(SupportApplication.this, uMessage.text);
            }
        };
        mPushAgent.setMessageHandler(mUmengMessageHandler);
    }

    @Override
    public void onFailure(String s, String s1) {
        LogUtil.printPushLog("SupportApplication::onFailure= " + s + " cause= " + s1);
        mUmentKey = null;
    }
}
