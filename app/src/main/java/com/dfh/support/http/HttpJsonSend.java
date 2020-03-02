package com.dfh.support.http;

import android.content.Context;


import com.dfh.support.utils.LogUtil;

import org.json.JSONObject;


public class HttpJsonSend {

    //登录类型(1:账户密码登录(用户名/手机号/邮箱) 2:手机号登录 3:邮箱登录)
    public static final int LOGIN_USERNAME_TYPE = 1;
    public static final int LOGIN_PHONE_TYPE = 2;
    public static final int LOGIN_EMAIL_TYPE = 3;

    /**
     * 登录
     *
     * @param context
     * @param code
     * @param type
     * @param type
     * @return
     */
    public static boolean login(Context context, String code, String account, int type,
                                String password) {
        String url = HttpConfig.url_login;
        String path = HttpConfig.GetHttpClientAdress() + url;
        try {
            LogUtil.printPushLog("httpPost login password:" + password);
            LogUtil.printPushLog("httpPost login password MD5:" + password);
            //方法四
            JSONObject formJSONObject = new JSONObject();
            formJSONObject.put("code", code);
            formJSONObject.put("type", type);
            formJSONObject.put("password", password);
            formJSONObject.put("account", account);
            String form = formJSONObject.toString();
            LogUtil.printPushLog("httpPost login form：" + form);
            HttpManager.postJson(path, form, context);
            LogUtil.printPushLog("httpPost login postJson");
        } catch (Exception e) {
            LogUtil.printPushLog("httpPost login Exception");
            e.printStackTrace();
        }
        return true;
    }

}