package com.dfh.support;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

public class SupportApplication extends Application implements IUmengRegisterCallback {

    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, "", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(this);
    }

    @Override
    public void onSuccess(String s) {

    }

    @Override
    public void onFailure(String s, String s1) {

    }
}
