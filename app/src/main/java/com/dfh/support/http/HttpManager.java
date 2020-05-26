package com.dfh.support.http;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.dfh.support.R;
import com.dfh.support.SupportApplication;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.StreamUtils;
import com.dfh.support.utils.VersionUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Response;

public class HttpManager {
    private static String NETWORK_ERROR_INFO = "";

    public static String getErrorInfo(Context context){
        if(TextUtils.isEmpty(NETWORK_ERROR_INFO)){
            if(null == context){
                NETWORK_ERROR_INFO = "网络连接失败";
            } else {
                NETWORK_ERROR_INFO = context.getResources().getString(R.string.network_connection_failed);
            }
        }
        return NETWORK_ERROR_INFO;
    }

    public static String httpPut(String path, String json, Context context) {
        String result = "";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
//            conn.setConnectTimeout(5 * 1000);
            // PUT请求
            connection.setRequestMethod("PUT");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            // json格式上传的模式
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("authorization", SupportApplication.USER_TOKEN);
            connection.setRequestProperty("appVersion", VersionUtil.getVersionName(context));
            connection.setRequestProperty("osVersion", Build.VERSION.RELEASE);
            connection.setRequestProperty("token", Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID));
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            osw.write(json);
            osw.flush();
            osw.close();
            //获得结果码
            int responseCode = connection.getResponseCode();
            LogUtil.printPushLog("httpPut responseCode" + responseCode);
            if (responseCode == 200) {
                LogUtil.printPushLog("httpPut 请求成功");
                //请求成功 获得返回的流
                InputStream in = connection.getInputStream();
                byte[] databyte = StreamUtils.read(in);
                String data = new String(databyte, "UTF-8");
                return data;
            } else {
                //请求失败
                HttpJsonAnaly.lastError = getErrorInfo(context);
                LogUtil.printPushLog("httpPut 请求失败");
                InputStream in = connection.getInputStream();
                byte[] databyte = StreamUtils.read(in);
                String data = new String(databyte, "UTF-8");
                return data;
            }

        } catch (Exception e) {
            HttpJsonAnaly.lastError = getErrorInfo(context);
        }
        return null;
    }

    public static String httpGet(String path, Context context) {
        //get的方式提交就是url拼接的方式
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("authorization", SupportApplication.USER_TOKEN);
            connection.setRequestProperty("appVersion", VersionUtil.getVersionName(context));
            connection.setRequestProperty("osVersion", Build.VERSION.RELEASE);
            connection.setRequestProperty("token", Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID));
            connection.setRequestProperty("Content-type", "application/json");
            //获得结果码
            int responseCode = connection.getResponseCode();
            LogUtil.printPushLog("httpGet responseCode" + responseCode);
            if (responseCode == 200) {
                LogUtil.printPushLog("httpGet 请求成功");
                //请求成功 获得返回的流
                InputStream in = connection.getInputStream();
                byte[] databyte = StreamUtils.read(in);
                String data = new String(databyte, "UTF-8");
                return data;
            } else {
                //请求失败
                HttpJsonAnaly.lastError = getErrorInfo(context);
                LogUtil.printPushLog("httpGet 请求失败");
                InputStream in = connection.getInputStream();
                byte[] databyte = StreamUtils.read(in);
                String data = new String(databyte, "UTF-8");
                return data;
            }
        } catch (Exception e) {
            HttpJsonAnaly.lastError = getErrorInfo(context);
            e.printStackTrace();
        }
        return null;
    }


    public static void postJson(final String url, final String json, final Context context) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("authorization", SupportApplication.USER_TOKEN);
        headers.put("appVersion", VersionUtil.getVersionName(context));
        headers.put("osVersion", Build.VERSION.RELEASE);
        headers.put("token", Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID));
        //headers.put("osVersion", QHApplication.mDeviceUtil.getDeviceData().getDeviceVersion());
//        headers.put("userId", userId);
//        headers.put("sessionId", sessionId);
//        headers.put("Cookie", "JSESSIONID=" + sessionId);
//        headers.put("Accept-Encoding", "identity");
//        headers.put("Accept-language","en");//en英文,zh中文
        headers.put("Content-Type", "application/json");
        LogUtil.printPushLog("httpPost postJson url:" + url);
        LogUtil.printPushLog("httpPost postJson json:" + json);
        LogUtil.printPushLog("httpPost postJson headers:" + headers);
        OkGo.getInstance().addCommonHeaders(headers);
        OkGo.getInstance()
                .setConnectTimeout(10000)
                .setReadTimeOut(10000)
                .setWriteTimeOut(10000)
                .post(url)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String data, Call call, Response response) {
                        LogUtil.printPushLog("httpPost onSuccess data：" + data);
                        //发广播，记录推s,推url，通过url识别方法，哪个界面调用，哪个界面接收
                        Intent intent = new Intent();
                        intent.setAction(SupportApplication.ACTION_HTTP_RESULT);
                        intent.putExtra("url", url);
                        intent.putExtra("data", data);
                        intent.putExtra("is_success", true);
                        context.sendBroadcast(intent);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        LogUtil.printPushLog("httpPost onError");
                        HttpJsonAnaly.lastError = getErrorInfo(context);
                        Intent intent = new Intent();
                        intent.setAction(SupportApplication.ACTION_HTTP_RESULT);
                        intent.putExtra("url", url);
                        intent.putExtra("data", "");
                        intent.putExtra("is_success", false);
                        context.sendBroadcast(intent);
                        super.onError(call, response, e);

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

                    }
                });
    }


    public static void postJsonType(final String url, final String json, final Context context,final int type) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("authorization", SupportApplication.USER_TOKEN);
        headers.put("appVersion", VersionUtil.getVersionName(context));
        headers.put("osVersion", Build.VERSION.RELEASE);
        headers.put("token", Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID));
        //headers.put("osVersion", QHApplication.mDeviceUtil.getDeviceData().getDeviceVersion());
//        headers.put("userId", userId);
//        headers.put("sessionId", sessionId);
//        headers.put("Cookie", "JSESSIONID=" + sessionId);
//        headers.put("Accept-Encoding", "identity");
//        headers.put("Accept-language","en");//en英文,zh中文
        headers.put("Content-Type", "application/json");
        LogUtil.printPushLog("httpPost postJson url:" + url);
        LogUtil.printPushLog("httpPost postJson json:" + json);
        LogUtil.printPushLog("httpPost postJson headers:" + headers);
        OkGo.getInstance().addCommonHeaders(headers);
        OkGo.getInstance()
                .setConnectTimeout(10000)
                .setReadTimeOut(10000)
                .setWriteTimeOut(10000)
                .post(url)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String data, Call call, Response response) {
                        LogUtil.printPushLog("httpPost onSuccess data：" + data);
                        //发广播，记录推s,推url，通过url识别方法，哪个界面调用，哪个界面接收
                        Intent intent = new Intent();
                        intent.setAction(SupportApplication.ACTION_HTTP_RESULT);
                        intent.putExtra("url", url);
                        intent.putExtra("data", data);
                        intent.putExtra("type", type);
                        intent.putExtra("is_success", true);
                        context.sendBroadcast(intent);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        LogUtil.printPushLog("httpPost onError");
                        HttpJsonAnaly.lastError = getErrorInfo(context);
                        Intent intent = new Intent();
                        intent.setAction(SupportApplication.ACTION_HTTP_RESULT);
                        intent.putExtra("url", url);
                        intent.putExtra("data", "");
                        intent.putExtra("type", type);
                        intent.putExtra("is_success", false);
                        context.sendBroadcast(intent);
                        super.onError(call, response, e);

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

                    }
                });
    }
}
