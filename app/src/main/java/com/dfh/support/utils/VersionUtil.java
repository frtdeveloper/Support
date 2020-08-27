package com.dfh.support.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.lang.reflect.Method;

public class VersionUtil {

    public static final String SN_PRO = "gsm.scril.sn";
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            String version = packInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "99.99";
        }

    }

    public static boolean compareVersion(String minVer, String nowVer) {
        int min = Integer.parseInt(minVer.replace(".", ""));
        int now = Integer.parseInt(nowVer.replace(".", ""));
        if (now > min) {
            return false;
        }
        return true;
    }

    public static String getSnNumber(String input_property){
        String result_sn = null;
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method getter = clazz.getDeclaredMethod("get", String.class);
            result_sn = (String) getter.invoke(null, input_property);

        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.printUtilLog("VersionUtil::getSnNumber::get_sn_reult= " + result_sn + " param= " + input_property);
        return result_sn;
    }
}
