package com.dfh.support.http;


import android.content.Context;

import com.dfh.support.R;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class HttpJsonAnaly {
    public static String lastError;

    public static void lastErrorDefaultValue(Context context, String error) {
        HttpJsonAnaly.lastError = error;
    }

    public static boolean onResult(String json, Context context) throws JSONException {
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("httpGet onResult code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if(TextUtils.isEmpty(lastError))lastError = context.getResources().getString(R.string.network_connection_failed);
            return false;
        } else {
            return true;
        }
        //result{"code":500,"msg":"未知异常，请联系管理员","data":null}
        //result{"code"));"msg":"success","data":"0595"}
    }
}