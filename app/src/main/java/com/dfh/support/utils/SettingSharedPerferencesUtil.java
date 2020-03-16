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

    public static final String SEARCH_CITY_PATH = "filepath_search_city_";
    private static final String SEARCH_CITY_CONFIG = "config_search_city_";

    public static boolean SetSearchCityValue(Context context, String city) {
        LogUtil.printPushLog("SetSearchHistoryValue city" + city);
        return PrefsHelper.save(context, SEARCH_CITY_CONFIG, city, SEARCH_CITY_PATH);
    }

    public static String GetSearchCityValueConfig(Context context) {
        String city = "";
        try {
            city = PrefsHelper.read(context, SEARCH_CITY_CONFIG, SEARCH_CITY_PATH);
            if(city.contains("市"))city = city.replace("市","");
            if(city.contains("县"))city = city.replace("县","");
            if(!city.equals("景德镇")&&!city.equals("镇江")) {
                if (city.contains("镇")) city = city.replace("镇", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(TextUtils.isEmpty(city)) city = "北京";
        return city;
    }
}
