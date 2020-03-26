package com.dfh.support.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.dfh.support.R;
import com.dfh.support.entity.CityData;

import java.util.List;

public final class LogUtil {
    private static final boolean FRAGMENT_LOG_FLAG = true;
    private static final String FRAGMENT_LOG_TAG = "slash_fragment";

    private static final boolean ACTIVITY_LOG_FLAG = true;
    private static final String ACTIVITY_LOG_TAG = "slash_activity";

    private static final boolean BROADCAST_LOG_FLAG = true;
    private static final String BROADCAST_LOG_TAG = "slash_broadcast";

    private static final boolean PUSH_LOG_FLAG = true;
    private static final String PUSH_LOG_TAG = "slash_push";

    private static final boolean UTIL_LOG_FLAG = true;
    private static final String  UTIL_LOG_TAG  = "slash_utils";

    private static final boolean NAME_LOG_FLAG = true;

    public static void printFragmentLog(String msg) {
        if (FRAGMENT_LOG_FLAG)
            android.util.Log.e(FRAGMENT_LOG_TAG, msg);
    }

    public static void printActivityLog(String msg) {
        if (ACTIVITY_LOG_FLAG)
            android.util.Log.e(ACTIVITY_LOG_TAG, msg);
    }

    public static void printPushLog(String msg) {
        if (PUSH_LOG_FLAG)
            android.util.Log.e(PUSH_LOG_TAG, msg);
    }

    public static void printBroadcastLog(String msg) {
        if (BROADCAST_LOG_FLAG)
            android.util.Log.e(BROADCAST_LOG_TAG, msg);
    }

    public static void printUtilLog(String msg) {
        if (UTIL_LOG_FLAG)
            android.util.Log.e(UTIL_LOG_TAG, msg);
    }

    public static void printNameLog(String tagName, String logText){
        if(NAME_LOG_FLAG)Log.i(tagName, logText);
    }

    public static CityData getGeo(Context ctx){
        CityData cityData = new CityData();
        String geo_info = ctx.getResources().getString(R.string.common_default_city);
        cityData.setCityName(geo_info);
        printUtilLog("getGeo::Begin::default_address= " + geo_info);

        if (PackageManager.PERMISSION_GRANTED !=
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(ctx, R.string.check_your_permission, Toast.LENGTH_SHORT).show();
            printUtilLog("getGeo::Permission[" + Manifest.permission.ACCESS_FINE_LOCATION + "] is not granted!!!!");
            cityData.setStatus(CityData.GEO_NO_PERMISSION);
        } else {
            LocationManager location_manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            boolean is_loc_open = location_manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            printUtilLog("getGeo::is_loc_open= " + is_loc_open);
            if (is_loc_open) {
                Location network_provider = location_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (null != network_provider){
                    double latitude = network_provider.getLatitude(); // 经度
                    double longitude = network_provider.getLongitude(); // 纬度
                    printUtilLog("getGeo::latitude= " + latitude + " longitude= " + longitude);

                    Geocoder geo_coder = new Geocoder(ctx);
                    List<Address> found_address_list = null;
                    try {
                        found_address_list = geo_coder.getFromLocation(latitude, longitude, 1);
                        if (null != found_address_list && found_address_list.size() > 0) {
                            geo_info = found_address_list.get(0).getLocality();
                            printUtilLog("getGeo::latitude= " + latitude + " longitude= " + longitude + " geo_info= " + geo_info);
                            cityData = new CityData();
                            if(geo_info.contains("市"))geo_info = geo_info.replace("市","");
                            if(geo_info.contains("县"))geo_info = geo_info.replace("县","");
                            if(!geo_info.equals("景德镇")&&!geo_info.equals("镇江")) {
                                if (geo_info.contains("镇")) geo_info = geo_info.replace("镇", "");
                            }
                            cityData.setCityName(geo_info);
                            cityData.setLatitude(latitude);
                            cityData.setLongitude(longitude);
                        } else {
                            printUtilLog("getGeo:: Can not found city by={" + latitude + ", " + longitude + "}");
                            cityData.setStatus(CityData.GEO_NO_CITY);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    printUtilLog("getGeo:: Can not found GEO PROVIDER!!!!");
                    cityData.setStatus(CityData.GEO_NO_PROVIDER);
                }
            } else {
                Toast.makeText(ctx, R.string.check_your_permission, Toast.LENGTH_SHORT).show();
                printUtilLog("getGeo:: the GEO switch is not enabled!!!!");
                cityData.setStatus(CityData.GEO_SWITCH_OFF);
            }
        }
        return cityData;
    }
}
