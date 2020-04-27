package com.dfh.support;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

import com.dfh.support.activity.WebViewActivity;
import com.dfh.support.activity.support.ServiceListActivity;
import com.dfh.support.compose.UmentBroadcastReceiver;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.ScreenUtils;
import com.dfh.support.utils.ToastUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.IOException;
import java.util.List;

import androidx.core.app.ActivityCompat;

public class SupportApplication extends Application implements IUmengRegisterCallback {

    private PushAgent mPushAgent;
    private UmengMessageHandler mUmengMessageHandler;
    private String mUmentKey;
    private static Context context;
    public static Application mInstance;
    public static String USER_TOKEN = "";
    public static final String ACTION_HTTP_RESULT = "com.dfh.action.http.result";
    public static final String ACTION_UPDATE_DATA_RESULT = "com.dfh.action.update.data";
    public static final String ACTION_FINISH_ACTIVITY = "com.dfh.action.finish.activity";

    private LocationManager locationManager;
    private double latitude = 0;
    private double longitude = 0;
    private String gpsCity = "";

    private UmengNotificationClickHandler mUmengNotificationClickHandler = new UmengNotificationClickHandler() {
        @Override
        public void openActivity(Context context, UMessage uMessage) {
            //super.openActivity(context, uMessage);
            LogUtil.printPushLog("openActivity::json_a = " + uMessage.extra);
            String my_url = uMessage.extra.get("url");
            String my_likes = uMessage.extra.get("link");
            String my_browser = uMessage.extra.get("browses");
            String my_id = uMessage.extra.get("id");
            WebViewActivity.openMySelf(SupportApplication.this, my_id, my_url, my_likes, my_browser);
        }

        @Override
        public void openUrl(Context context, UMessage uMessage) {
            //super.openUrl(context, uMessage);
            LogUtil.printPushLog("openUrl::json_a = " + uMessage.extra);
        }

        @Override
        public void launchApp(Context context, UMessage uMessage) {
            //super.launchApp(context, uMessage);
            LogUtil.printPushLog("launchApp::json_a = " + uMessage.extra);
            String my_url = uMessage.extra.get("url");
            String my_likes = uMessage.extra.get("link");
            String my_browser = uMessage.extra.get("browses");
            String my_id = uMessage.extra.get("id");
            WebViewActivity.openMySelf(SupportApplication.this, my_id, my_url, my_likes, my_browser);
        }

        @Override
        public void dealWithCustomAction(Context context, UMessage uMessage) {
            //super.dealWithCustomAction(context, uMessage);
            LogUtil.printPushLog("launchApp::json_a = " + uMessage.extra);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, "5e5bc092570df3a9fc0000bb", "Umeng",
                UMConfigure.DEVICE_TYPE_PHONE, "26c496679dab7728c41b76620e90faf9");//nojtgbprhi2w5yoyludz2vplramuqktf
        mInstance = this;
        context = getApplicationContext();
        initImageLoader(this);
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(this);

        mPushAgent.setNotificationClickHandler(mUmengNotificationClickHandler);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ScreenUtils.init(this);

    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onSuccess(String s) {
        mUmentKey = s;
        LogUtil.printPushLog("SupportApplication::onSuccess= " + s + " key= " + mUmentKey);
        mUmengMessageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
                LogUtil.printPushLog("SupportApplication::dealWithNotificationMessage= " + uMessage.text);
                LogUtil.printPushLog("SupportApplication::dealWithCustomMessage extra= " + uMessage.extra);
                UmentBroadcastReceiver.sendBroadcast(SupportApplication.this, uMessage.text);
                int current_count = LogUtil.getUnReadCount(context);
                current_count = current_count + 1;
                LogUtil.setUnReadCount(context, current_count);
            }

            @Override
            public void dealWithCustomMessage(Context context, UMessage uMessage) {
                super.dealWithCustomMessage(context, uMessage);
                LogUtil.printPushLog("SupportApplication::dealWithCustomMessage= " + uMessage.text);
                LogUtil.printPushLog("SupportApplication::dealWithCustomMessage extra= " + uMessage.extra);
                UmentBroadcastReceiver.sendCBroadcast(SupportApplication.this, uMessage.text);
                int current_count = LogUtil.getUnReadCount(context);
                current_count = current_count + 1;
                LogUtil.setUnReadCount(context, current_count);
            }
        };
        mPushAgent.setMessageHandler(mUmengMessageHandler);
    }

    @Override
    public void onFailure(String s, String s1) {
        LogUtil.printPushLog("SupportApplication::onFailure= " + s + " cause= " + s1);
        mUmentKey = null;
    }

    private void initLocation() {

        if (ActivityCompat.checkSelfPermission(SupportApplication.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SupportApplication.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogUtil.printPushLog("LocationTask checkSelfPermission :" + false);
            ToastUtils.shortToast(SupportApplication.this, getResources().getString(R.string.check_your_permission));
        } else {
            LogUtil.printPushLog("LocationTask checkSelfPermission :" + true);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                LogUtil.printPushLog("LocationTask location != null ");
                latitude = location.getLatitude(); // 经度
                longitude = location.getLongitude(); // 纬度
                LogUtil.printPushLog("LocationTask latitude :" + latitude);
                LogUtil.printPushLog("LocationTask longitude :" + longitude);
                double[] data = {latitude, longitude};
                List<Address> addList = null;
                Geocoder ge = new Geocoder(getApplicationContext());
                try {
                    addList = ge.getFromLocation(data[0], data[1], 1);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (addList != null && addList.size() > 0) {
                    for (int i = 0; i < addList.size(); i++) {
                        Address ad = addList.get(i);
                        gpsCity = ad.getLocality();
                        LogUtil.printPushLog("LocationTask gpsCity :" + gpsCity);
                    }
                }
            } else {
                LogUtil.printPushLog("LocationTask location == null ");
            }
        }
    }
}
