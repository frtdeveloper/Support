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
import com.dfh.support.http.HttpJsonAnaly;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
    private static final String UTIL_LOG_TAG = "slash_utils";

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

    public static void printNameLog(String tagName, String logText) {
        if (NAME_LOG_FLAG) Log.i(tagName, logText);
    }

    public static CityData getGeo(Context ctx) {
        CityData cityData = new CityData();
        String geo_info = ctx.getResources().getString(R.string.common_default_city);
        cityData.setCityName(geo_info);
        printUtilLog("getGeo::Begin::default_address= " + geo_info);

        if (PackageManager.PERMISSION_GRANTED !=
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //Toast.makeText(ctx, R.string.check_your_permission, Toast.LENGTH_SHORT).show();
            printUtilLog("getGeo::Permission[" + Manifest.permission.ACCESS_FINE_LOCATION + "] is not granted!!!!");
            cityData.setStatus(CityData.GEO_NO_PERMISSION);
        } else {
            LocationManager location_manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            boolean is_loc_open = location_manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            printUtilLog("getGeo::is_loc_open= " + is_loc_open);
            if (is_loc_open) {
                Location network_provider = location_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (null != network_provider) {
                    final double latitude = network_provider.getLatitude(); // 经度
                    final double longitude = network_provider.getLongitude(); // 纬度
                    printUtilLog("getGeo::latitude= " + latitude + " longitude= " + longitude);
                    cityData.setLatitude(latitude);
                    cityData.setLongitude(longitude);

                    Geocoder geo_coder = new Geocoder(ctx);
                    List<Address> found_address_list = null;
                    try {
                        found_address_list = geo_coder.getFromLocation(latitude, longitude, 1);
                        if (null != found_address_list && found_address_list.size() > 0) {
                            geo_info = found_address_list.get(0).getLocality();
                            printUtilLog("getGeo::latitude= " + latitude + " longitude= " + longitude + " geo_info= " + geo_info);
                            cityData = new CityData();
                            if (geo_info.contains("市")) geo_info = geo_info.replace("市", "");
                            if (geo_info.contains("县")) geo_info = geo_info.replace("县", "");
                            if (!geo_info.equals("景德镇") && !geo_info.equals("镇江")) {
                                if (geo_info.contains("镇")) geo_info = geo_info.replace("镇", "");
                            }
                            cityData.setCityName(geo_info);
                            cityData.setLatitude(latitude);
                            cityData.setLongitude(longitude);
                        } else {
                            printUtilLog("getGeo:: Can not found city by={" + latitude + ", " + longitude + "}");
                            String cityname = reverseGeocode(latitude, longitude);
                            if (!TextUtils.isEmpty(cityname)) {
                                cityData = new CityData();
                                if (cityname.contains("市")) cityname = cityname.replace("市", "");
                                if (cityname.contains("县")) cityname = cityname.replace("县", "");
                                if (!cityname.equals("景德镇") && !cityname.equals("镇江")) {
                                    if (cityname.contains("镇")) cityname = cityname.replace("镇", "");
                                }
                                cityData.setCityName(cityname);
                                cityData.setLatitude(latitude);
                                cityData.setLongitude(longitude);
                            } else {
                                cityData.setStatus(CityData.GEO_NO_CITY);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    printUtilLog("getGeo:: Can not found GEO PROVIDER!!!!");
                    cityData.setStatus(CityData.GEO_NO_PROVIDER);
                }
            } else {
                //Toast.makeText(ctx, R.string.check_your_permission, Toast.LENGTH_SHORT).show();
                printUtilLog("getGeo:: the GEO switch is not enabled!!!!");
                cityData.setStatus(CityData.GEO_SWITCH_OFF);
            }
        }
        return cityData;
    }


    /**
     * 通过Google  map api 解析出城市
     *
     * @return
     */
    public static String reverseGeocode(double latitude, double longitude) {
        LogUtil.printPushLog("reverseGeocode latitude" + latitude + "|longitude" + longitude);
        double x = longitude, y = latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        // http://maps.google.com/maps/geo?q=40.714224,-73.961452&output=json&oe=utf8&sensor=true_or_false&key=your_api_key
        String localityName = "";
        HttpURLConnection connection = null;
        URL serverAddress = null;

        try {
            // build the URL using the latitude & longitude you want to lookup
            // NOTE: I chose XML return format here but you can choose something
            // else
            serverAddress = new URL("http://api.map.baidu.com/geocoder?output=json&location="
                    + Double.toString(tempLat) + ","
                    + Double.toString(tempLon)
                    + "&ak=esNPFDwwsXWtsQfw4NMNmur1");
            // set up out communications stuff
            connection = null;

            // Set up the initial connection
            connection = (HttpURLConnection) serverAddress.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);

            connection.connect();


            int responseCode = connection.getResponseCode();
            LogUtil.printPushLog("reverseGeocode responseCode" + responseCode);
            if (responseCode == 200) {
                LogUtil.printPushLog("reverseGeocode 请求成功");
                //请求成功 获得返回的流
                InputStream in = connection.getInputStream();
                byte[] databyte = StreamUtils.read(in);
                String data = new String(databyte, "UTF-8");
                LogUtil.printPushLog("reverseGeocode data:" + data);
                localityName = HttpJsonAnaly.analyCity(data);
                LogUtil.printPushLog("reverseGeocode localityName1:" + localityName);
            } else {
                //请求失败
                LogUtil.printPushLog("reverseGeocode 请求失败");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.printPushLog("reverseGeocode Exception");
        }

        LogUtil.printPushLog("reverseGeocode localityName2:" + localityName);
        return localityName;
    }
}
