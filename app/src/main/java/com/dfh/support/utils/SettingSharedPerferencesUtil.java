package com.dfh.support.utils;

import android.content.Context;

public class SettingSharedPerferencesUtil {
    public static final String SEARCH_HISTORY_PATH = "filepath_search_history_";
    private static final String SEARCH_HISTORY_CONFIG = "config_search_history_";

    public static boolean SetSearchHistoryValue(Context context, String history) {
        LogUtil.printPushLog("SetSearchHistoryValue history" + history);
        return PrefsHelper.save(context, SEARCH_HISTORY_CONFIG, history, SEARCH_HISTORY_PATH);
    }

    public static String GetSearchHistoryValueConfig(Context context) {
        String history = "";
        try {
            history = PrefsHelper.read(context, SEARCH_HISTORY_CONFIG, SEARCH_HISTORY_PATH);
            LogUtil.printPushLog("GetSearchHistoryValueConfig history" + history);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

}
