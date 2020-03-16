 package com.dfh.support.compose;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dfh.support.utils.LogUtil;

public class UmentBroadcastReceiver extends BroadcastReceiver {

    public static String UMENG_MESSAGE = "umeng_msg_key";
    public static String INTENT_MSG_STR = "com.dfh.support.umeng_message";
    public static String INTENT_C_MSG_STR = "com.dfh.support.umeng_c_message";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(null != intent){
            String intent_action = intent.getAction();
            String msg = intent.getStringExtra(UMENG_MESSAGE);
            LogUtil.printBroadcastLog("UmentBroadcastReceiver:: action= " + intent_action + " msg= " + msg);
            if (INTENT_MSG_STR.equals(intent_action)){
                //Do message
                LogUtil.printBroadcastLog("UmentBroadcastReceiver:: Null intent============");
            } else if(INTENT_C_MSG_STR.equals(intent_action)){
                //Do c message
                LogUtil.printBroadcastLog("UmentBroadcastReceiver:: Null intent============");
            } else {

            }
        } else {
            LogUtil.printBroadcastLog("UmentBroadcastReceiver:: Null intent============");
        }
    }

    public static void sendBroadcast(Context ctx, String message){
        Intent intent = new Intent(INTENT_MSG_STR);
        intent.putExtra(UMENG_MESSAGE, message);
        ctx.sendBroadcast(intent);
    }

    public static void sendCBroadcast(Context ctx, String message){
        Intent intent = new Intent(INTENT_C_MSG_STR);
        intent.putExtra(UMENG_MESSAGE, message);
        ctx.sendBroadcast(intent);
    }
}
