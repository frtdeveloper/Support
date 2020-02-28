package com.dfh.support.utils;

public final class LogUtil {
    private static final boolean FRAGMENT_LOG_FLAG = true;
    private static final String  FRAGMENT_LOG_TAG = "slash_fragment";

    private static final boolean ACTIVITY_LOG_FLAG = true;
    private static final String  ACTIVITY_LOG_TAG  = "slash_activity";

    private static final boolean PUSH_LOG_FLAG = true;
    private static final String  PUSH_LOG_TAG  = "slash_push";

    public static void printFragmentLog(String msg){
        if (FRAGMENT_LOG_FLAG)
            android.util.Log.e(FRAGMENT_LOG_TAG, msg);
    }

    public static void printActivityLog(String msg){
        if (ACTIVITY_LOG_FLAG)
            android.util.Log.e(ACTIVITY_LOG_TAG, msg);
    }

    public static void printPushLog(String msg){
        if (PUSH_LOG_FLAG)
            android.util.Log.e(PUSH_LOG_TAG, msg);
    }
}
