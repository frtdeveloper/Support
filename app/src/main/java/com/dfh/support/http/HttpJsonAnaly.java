package com.dfh.support.http;


import android.content.Context;

import com.dfh.support.R;
import com.dfh.support.entity.AdvertisementData;
import com.dfh.support.entity.AdvertisementListData;
import com.dfh.support.entity.ClassifyData;
import com.dfh.support.entity.ClassifyListData;
import com.dfh.support.entity.DebugDetailData;
import com.dfh.support.entity.DebugListData;
import com.dfh.support.entity.DebugMenuData;
import com.dfh.support.entity.DebugMenuListData;
import com.dfh.support.entity.PartsData;
import com.dfh.support.entity.PartsListData;
import com.dfh.support.entity.PictureVOData;
import com.dfh.support.entity.PolicyData;
import com.dfh.support.entity.ServeData;
import com.dfh.support.entity.ServeListData;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return false;
        } else {
            return true;
        }
        //result{"code":500,"msg":"未知异常，请联系管理员","data":null}
        //result{"code"));"msg":"success","data":"0595"}
    }

    public static AdvertisementListData adsFindCarouselAll(String json, Context context) throws JSONException {
        AdvertisementListData advertisementListData = new AdvertisementListData();
        advertisementListData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("adsFindCarouselAll code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return advertisementListData;
        } else {
            JSONArray dataArrayJson = resultJson.getJSONArray("data");
            ArrayList<AdvertisementData> advertisementDataList = new ArrayList<AdvertisementData>();
            for (int i = 0; i < dataArrayJson.length(); i++) {
                AdvertisementData advertisementData = new AdvertisementData();

                JSONObject dataJson = (JSONObject) dataArrayJson.get(i);
                if (dataJson.has("adClssifyId"))
                    advertisementData.setAdClssifyId(dataJson.getString("adClssifyId"));
                if (dataJson.has("adClssifyName"))
                    advertisementData.setAdClssifyName(dataJson.getString("adClssifyName"));
                if (dataJson.has("author"))
                    advertisementData.setAuthor(dataJson.getString("author"));
                if (dataJson.has("browses"))
                    advertisementData.setBrowses(dataJson.getString("browses"));
                if (dataJson.has("content"))
                    advertisementData.setContent(dataJson.getString("content"));
                if (dataJson.has("icon"))
                    advertisementData.setIcon(dataJson.getString("icon"));
                if (dataJson.has("id"))
                    advertisementData.setId(dataJson.getString("id"));
                if (dataJson.has("likes"))
                    advertisementData.setLikes(dataJson.getString("likes"));
                if (dataJson.has("link"))
                    advertisementData.setLink(dataJson.getString("link"));
                if (dataJson.has("publishedTime"))
                    advertisementData.setPublishedTime(dataJson.getString("publishedTime"));
                if (dataJson.has("title"))
                    advertisementData.setTitle(dataJson.getString("title"));
                if (dataJson.has("type"))
                    advertisementData.setType(dataJson.getString("type"));

                //子广告暂时没使用
                if (dataJson.has("adsVOList")) {
                    ArrayList<AdvertisementData> adsVOList = new ArrayList<AdvertisementData>();
                    advertisementData.setAdsVOList(adsVOList);
                }
                advertisementDataList.add(advertisementData);
            }
            advertisementListData.setAdvertisementDatas(advertisementDataList);
            advertisementListData.setFlag(true);
            LogUtil.printPushLog("adsFindCarouselAll advertisementListData" + advertisementListData.toString());
            return advertisementListData;
        }
    }

    public static AdvertisementListData adsFindGeneralPager(String json, Context context) throws JSONException {
        AdvertisementListData advertisementListData = new AdvertisementListData();
        advertisementListData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("adsFindGeneralPager code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return advertisementListData;
        } else {
            JSONObject dataObjectJson = resultJson.getJSONObject("data");
            JSONArray recordsArrayJson = dataObjectJson.getJSONArray("records");
            ArrayList<AdvertisementData> advertisementDataList = new ArrayList<AdvertisementData>();
            for (int i = 0; i < recordsArrayJson.length(); i++) {
                AdvertisementData advertisementData = new AdvertisementData();
                JSONObject dataJson = (JSONObject) recordsArrayJson.get(i);
                if (dataJson.has("adClssifyId"))
                    advertisementData.setAdClssifyId(dataJson.getString("adClssifyId"));
                if (dataJson.has("adClssifyName"))
                    advertisementData.setAdClssifyName(dataJson.getString("adClssifyName"));
                if (dataJson.has("author"))
                    advertisementData.setAuthor(dataJson.getString("author"));
                if (dataJson.has("browses"))
                    advertisementData.setBrowses(dataJson.getString("browses"));
                if (dataJson.has("content"))
                    advertisementData.setContent(dataJson.getString("content"));
                if (dataJson.has("icon"))
                    advertisementData.setIcon(dataJson.getString("icon"));
                if (dataJson.has("id"))
                    advertisementData.setId(dataJson.getString("id"));
                if (dataJson.has("likes"))
                    advertisementData.setLikes(dataJson.getString("likes"));
                if (dataJson.has("link"))
                    advertisementData.setLink(dataJson.getString("link"));
                if (dataJson.has("publishedTime"))
                    advertisementData.setPublishedTime(dataJson.getString("publishedTime"));
                if (dataJson.has("title"))
                    advertisementData.setTitle(dataJson.getString("title"));
                if (dataJson.has("type"))
                    advertisementData.setType(dataJson.getString("type"));

                //子广告暂时没使用
                if (dataJson.has("adsVOList")) {
                    ArrayList<AdvertisementData> adsVOList = new ArrayList<AdvertisementData>();
                    JSONArray adsVOListArrayJson = dataJson.getJSONArray("adsVOList");
                    for (int a = 0; a < adsVOListArrayJson.length(); a++) {
                        AdvertisementData advertisementDataItem = new AdvertisementData();
                        JSONObject adsVOJson = (JSONObject) adsVOListArrayJson.get(a);
                        if (adsVOJson.has("adClssifyId"))
                            advertisementDataItem.setAdClssifyId(adsVOJson.getString("adClssifyId"));
                        if (adsVOJson.has("adClssifyName"))
                            advertisementDataItem.setAdClssifyName(adsVOJson.getString("adClssifyName"));
                        if (adsVOJson.has("author"))
                            advertisementDataItem.setAuthor(adsVOJson.getString("author"));
                        if (adsVOJson.has("browses"))
                            advertisementDataItem.setBrowses(adsVOJson.getString("browses"));
                        if (adsVOJson.has("content"))
                            advertisementDataItem.setContent(adsVOJson.getString("content"));
                        if (adsVOJson.has("icon"))
                            advertisementDataItem.setIcon(adsVOJson.getString("icon"));
                        if (adsVOJson.has("id"))
                            advertisementDataItem.setId(adsVOJson.getString("id"));
                        if (adsVOJson.has("likes"))
                            advertisementDataItem.setLikes(adsVOJson.getString("likes"));
                        if (adsVOJson.has("link"))
                            advertisementDataItem.setLink(adsVOJson.getString("link"));
                        if (adsVOJson.has("publishedTime"))
                            advertisementDataItem.setPublishedTime(adsVOJson.getString("publishedTime"));
                        if (adsVOJson.has("title"))
                            advertisementDataItem.setTitle(adsVOJson.getString("title"));
                        if (adsVOJson.has("type"))
                            advertisementDataItem.setType(adsVOJson.getString("type"));

                        //子广告暂时没使用
                        if (adsVOJson.has("adsVOList")) {
                            ArrayList<AdvertisementData> adsVOListItem = new ArrayList<AdvertisementData>();
                            advertisementDataItem.setAdsVOList(adsVOListItem);
                        }
                        adsVOList.add(advertisementDataItem);
                    }
                    advertisementData.setAdsVOList(adsVOList);
                }
                advertisementDataList.add(advertisementData);
            }
            advertisementListData.setAdvertisementDatas(advertisementDataList);
            advertisementListData.setFlag(true);
            LogUtil.printPushLog("adsFindGeneralPager advertisementListData" + advertisementListData.toString());
            return advertisementListData;
        }
    }


    public static ClassifyListData partsFindClassify(String json, Context context) throws JSONException {
        ClassifyListData classifyListData = new ClassifyListData();
        classifyListData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("partsFindClassify code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return classifyListData;
        } else {
            JSONArray dataArrayJson = resultJson.getJSONArray("data");
            ArrayList<ClassifyData> classifyDataList = new ArrayList<ClassifyData>();
            for (int i = 0; i < dataArrayJson.length(); i++) {
                ClassifyData classifyData = new ClassifyData();

                JSONObject dataJson = (JSONObject) dataArrayJson.get(i);
                if (dataJson.has("icon"))
                    classifyData.setIcon(dataJson.getString("icon"));
                if (dataJson.has("id"))
                    classifyData.setId(dataJson.getString("id"));
                if (dataJson.has("name"))
                    classifyData.setName(dataJson.getString("name"));
                classifyDataList.add(classifyData);
            }
            classifyListData.setClassifyData(classifyDataList);
            classifyListData.setFlag(true);
            LogUtil.printPushLog("partsFindClassify classifyListData" + classifyListData.toString());
            return classifyListData;
        }
    }

    public static PartsListData partsFindClassifyIdPager(String json, Context context) throws JSONException {
        PartsListData partsListData = new PartsListData();
        partsListData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("partsFindClassifyIdPager code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return partsListData;
        } else {
            JSONObject dataObjectJson = resultJson.getJSONObject("data");
            JSONArray recordsArrayJson = dataObjectJson.getJSONArray("records");
            ArrayList<PartsData> partsDataList = new ArrayList<PartsData>();
            for (int i = 0; i < recordsArrayJson.length(); i++) {
                PartsData partsData = new PartsData();
                JSONObject dataJson = (JSONObject) recordsArrayJson.get(i);
                if (dataJson.has("belistedTime"))
                    partsData.setBelistedTime(dataJson.getString("belistedTime"));
                if (dataJson.has("content"))
                    partsData.setContent(dataJson.getString("content"));
                if (dataJson.has("icon"))
                    partsData.setIcon(dataJson.getString("icon"));
                if (dataJson.has("id"))
                    partsData.setId(dataJson.getString("id"));
                if (dataJson.has("intro"))
                    partsData.setIntro(dataJson.getString("intro"));
                if (dataJson.has("name"))
                    partsData.setName(dataJson.getString("name"));
                if (dataJson.has("orgiPrice"))
                    partsData.setOrgiPrice(dataJson.getString("orgiPrice"));
                if (dataJson.has("partsClassifyId"))
                    partsData.setPartsClassifyId(dataJson.getString("partsClassifyId"));
                if (dataJson.has("partsClassifyName"))
                    partsData.setPartsClassifyName(dataJson.getString("partsClassifyName"));
                if (dataJson.has("ppPrice"))
                    partsData.setPpPrice(dataJson.getString("ppPrice"));
                if (dataJson.has("specs"))
                    partsData.setSpecs(dataJson.getString("specs"));
                if (dataJson.has("tips"))
                    partsData.setTips(dataJson.getString("tips"));

                ArrayList<PictureVOData> pictureVODataList = new ArrayList<PictureVOData>();
                try {
                    JSONArray pictureArrayJson = dataJson.getJSONArray("pictureVOList");
                    for (int a = 0; a < pictureArrayJson.length(); a++) {
                        PictureVOData pictureVOData = new PictureVOData();
                        JSONObject pictureJson = (JSONObject) pictureArrayJson.get(a);
                        if (pictureJson.has("id"))
                            pictureVOData.setId(pictureJson.getString("id"));
                        if (pictureJson.has("source"))
                            pictureVOData.setSource(pictureJson.getString("source"));
                        if (pictureJson.has("thumb"))
                            pictureVOData.setThumb(pictureJson.getString("thumb"));
                        pictureVODataList.add(pictureVOData);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                partsData.setPictureVOData(pictureVODataList);
                partsDataList.add(partsData);
            }
            partsListData.setPartsData(partsDataList);
            partsListData.setFlag(true);
            LogUtil.printPushLog("partsFindClassifyIdPager partsListData" + partsListData.toString());
            return partsListData;
        }
    }

    public static PartsData partsIdDetail(String json, Context context) throws JSONException {
        PartsData partsData = new PartsData();
        partsData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("partsIdDetail code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return partsData;
        } else {
            JSONObject dataJson = resultJson.getJSONObject("data");
            if (dataJson.has("belistedTime"))
                partsData.setBelistedTime(dataJson.getString("belistedTime"));
            if (dataJson.has("content"))
                partsData.setContent(dataJson.getString("content"));
            if (dataJson.has("icon"))
                partsData.setIcon(dataJson.getString("icon"));
            if (dataJson.has("id"))
                partsData.setId(dataJson.getString("id"));
            if (dataJson.has("intro"))
                partsData.setIntro(dataJson.getString("intro"));
            if (dataJson.has("name"))
                partsData.setName(dataJson.getString("name"));
            if (dataJson.has("orgiPrice"))
                partsData.setOrgiPrice(dataJson.getString("orgiPrice"));
            if (dataJson.has("partsClassifyId"))
                partsData.setPartsClassifyId(dataJson.getString("partsClassifyId"));
            if (dataJson.has("partsClassifyName"))
                partsData.setPartsClassifyName(dataJson.getString("partsClassifyName"));
            if (dataJson.has("ppPrice"))
                partsData.setPpPrice(dataJson.getString("ppPrice"));
            if (dataJson.has("specs"))
                partsData.setSpecs(dataJson.getString("specs"));
            if (dataJson.has("tips"))
                partsData.setTips(dataJson.getString("tips"));

            JSONArray pictureArrayJson = dataJson.getJSONArray("pictureVOList");
            ArrayList<PictureVOData> pictureVODataList = new ArrayList<PictureVOData>();
            for (int i = 0; i < pictureArrayJson.length(); i++) {
                PictureVOData pictureVOData = new PictureVOData();
                JSONObject pictureJson = (JSONObject) pictureArrayJson.get(i);
                if (pictureJson.has("id"))
                    pictureVOData.setId(pictureJson.getString("id"));
                if (pictureJson.has("source"))
                    pictureVOData.setSource(pictureJson.getString("source"));
                if (pictureJson.has("thumb"))
                    pictureVOData.setThumb(pictureJson.getString("thumb"));
                pictureVODataList.add(pictureVOData);
            }
            partsData.setPictureVOData(pictureVODataList);
            partsData.setFlag(true);
            LogUtil.printPushLog("partsIdDetail partsData" + partsData.toString());
            return partsData;
        }
    }


    public static ServeListData servePager(String json, Context context) throws JSONException {
        ServeListData serveListData = new ServeListData();
        serveListData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("servePager code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return serveListData;
        } else {
            JSONObject dataObjectJson = resultJson.getJSONObject("data");
            JSONArray recordsArrayJson = dataObjectJson.getJSONArray("records");
            ArrayList<ServeData> serveDataList = new ArrayList<ServeData>();
            for (int i = 0; i < recordsArrayJson.length(); i++) {
                ServeData serveData = new ServeData();
                JSONObject dataJson = (JSONObject) recordsArrayJson.get(i);
                if (dataJson.has("address"))
                    serveData.setAddress(dataJson.getString("address"));
                if (dataJson.has("contact"))
                    serveData.setContact(dataJson.getString("contact"));
                if (dataJson.has("distance")) {
                    String distance = "";
                    try {
                        distance = dataJson.getString("distance");
//                        LogUtil.printPushLog("distance distance1:"+distance);
//                        if (distance.contains("KM")) distance = distance.replace("KM", "");
//                        LogUtil.printPushLog("distance distance2:"+distance);
//                        if (distance.contains("M")) distance = distance.replace("M", "");
//                        LogUtil.printPushLog("distance distance3:"+distance);
//                        Double d = Double.valueOf(distance);
//                        LogUtil.printPushLog("distance d:"+d);
//                        int dis =new Double(d).intValue();
//                        LogUtil.printPushLog("distance dis:"+dis);
//                        if (dis > 1000) {
//                            distance = dis / 1000 + "KM";
//                        } else {
//                            distance = dis + "M";
//                        }
//                        LogUtil.printPushLog("distance distance:"+distance);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    serveData.setDistance(distance);
                }
                if (dataJson.has("id"))
                    serveData.setId(dataJson.getString("id"));
                if (dataJson.has("lat"))
                    serveData.setLat(dataJson.getString("lat"));
                if (dataJson.has("lng"))
                    serveData.setLng(dataJson.getString("lng"));
                if (dataJson.has("name"))
                    serveData.setName(dataJson.getString("name"));
                if (dataJson.has("scope"))
                    serveData.setScope(dataJson.getString("scope"));
                if (dataJson.has("tel"))
                    serveData.setTel(dataJson.getString("tel"));
                if (dataJson.has("time"))
                    serveData.setTime(dataJson.getString("time"));
                if (dataJson.has("tips"))
                    serveData.setTips(dataJson.getString("tips"));

                ArrayList<PictureVOData> pictureVODataList = new ArrayList<PictureVOData>();
                try {
                    JSONArray pictureArrayJson = dataJson.getJSONArray("pictureVOList");
                    for (int a = 0; a < pictureArrayJson.length(); a++) {
                        PictureVOData pictureVOData = new PictureVOData();
                        JSONObject pictureJson = (JSONObject) pictureArrayJson.get(i);
                        if (pictureJson.has("id"))
                            pictureVOData.setId(pictureJson.getString("id"));
                        if (pictureJson.has("source"))
                            pictureVOData.setSource(pictureJson.getString("source"));
                        if (pictureJson.has("thumb"))
                            pictureVOData.setThumb(pictureJson.getString("thumb"));
                        pictureVODataList.add(pictureVOData);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                serveData.setPictureVOData(pictureVODataList);
                serveDataList.add(serveData);
            }
            serveListData.setServeData(serveDataList);
            serveListData.setFlag(true);
            LogUtil.printPushLog("servePager serveListData" + serveListData.toString());
            return serveListData;
        }
    }

    public static ServeData serveIdDetail(String json, Context context) throws JSONException {
        ServeData serveData = new ServeData();
        serveData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("serveIdDetail code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return serveData;
        } else {
            JSONObject dataJson = resultJson.getJSONObject("data");
            if (dataJson.has("address"))
                serveData.setAddress(dataJson.getString("address"));
            if (dataJson.has("contact"))
                serveData.setContact(dataJson.getString("contact"));
            if (dataJson.has("distance"))
                serveData.setDistance(dataJson.getString("distance"));
            if (dataJson.has("id"))
                serveData.setId(dataJson.getString("id"));
            if (dataJson.has("lat"))
                serveData.setLat(dataJson.getString("lat"));
            if (dataJson.has("lng"))
                serveData.setLng(dataJson.getString("lng"));
            if (dataJson.has("name"))
                serveData.setName(dataJson.getString("name"));
            if (dataJson.has("scope"))
                serveData.setScope(dataJson.getString("scope"));
            if (dataJson.has("tel"))
                serveData.setTel(dataJson.getString("tel"));
            if (dataJson.has("time"))
                serveData.setTime(dataJson.getString("time"));
            if (dataJson.has("tips"))
                serveData.setTips(dataJson.getString("tips"));

            JSONArray pictureArrayJson = dataJson.getJSONArray("pictureVOList");
            ArrayList<PictureVOData> pictureVODataList = new ArrayList<PictureVOData>();
            for (int i = 0; i < pictureArrayJson.length(); i++) {
                PictureVOData pictureVOData = new PictureVOData();
                JSONObject pictureJson = (JSONObject) pictureArrayJson.get(i);
                if (pictureJson.has("id"))
                    pictureVOData.setId(pictureJson.getString("id"));
                if (pictureJson.has("source"))
                    pictureVOData.setSource(pictureJson.getString("source"));
                if (pictureJson.has("thumb"))
                    pictureVOData.setThumb(pictureJson.getString("thumb"));
                pictureVODataList.add(pictureVOData);
            }
            serveData.setPictureVOData(pictureVODataList);
            serveData.setFlag(true);
            LogUtil.printPushLog("serveIdDetail serveData" + serveData.toString());
            return serveData;
        }
    }


    /**
     * 政策法律
     *
     * @param json
     * @param context
     * @return
     * @throws JSONException
     */
    public static PolicyData policyFindName(String json, Context context) throws JSONException {
        PolicyData policyData = new PolicyData();
        policyData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("policyFindName code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return policyData;
        } else {
            JSONObject dataJson = resultJson.getJSONObject("data");
            if (dataJson.has("id"))
                policyData.setId(dataJson.getString("id"));
            if (dataJson.has("name"))
                policyData.setName(dataJson.getString("name"));
            if (dataJson.has("title"))
                policyData.setTitle(dataJson.getString("title"));
            if (dataJson.has("url"))
                policyData.setUrl(dataJson.getString("url"));
            policyData.setFlag(true);
            LogUtil.printPushLog("policyFindName policyData" + policyData.toString());
            return policyData;
        }
    }


    public static DebugMenuListData faultFindClassify(String json, Context context) throws JSONException {
        DebugMenuListData debugMenuListData = new DebugMenuListData();
        debugMenuListData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("faultFindClassify code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return debugMenuListData;
        } else {
            JSONArray dataArrayJson = resultJson.getJSONArray("data");
            ArrayList<DebugMenuData> ebugMenuDataList = new ArrayList<DebugMenuData>();
            for (int i = 0; i < dataArrayJson.length(); i++) {
                DebugMenuData debugMenuData = new DebugMenuData();
                JSONObject dataJson = (JSONObject) dataArrayJson.get(i);
                if (dataJson.has("icon"))
                    debugMenuData.setIcon(dataJson.getString("icon"));
                if (dataJson.has("id"))
                    debugMenuData.setId(dataJson.getString("id"));
                if (dataJson.has("mainTitle"))
                    debugMenuData.setMainTitle(dataJson.getString("mainTitle"));
                if (dataJson.has("subTitle"))
                    debugMenuData.setSubTitle(dataJson.getString("subTitle"));
                ebugMenuDataList.add(debugMenuData);
            }
            debugMenuListData.setDebugMenuData(ebugMenuDataList);
            debugMenuListData.setFlag(true);
            LogUtil.printPushLog("faultFindClassify debugMenuListData" + debugMenuListData.toString());
            return debugMenuListData;
        }
    }

    public static DebugListData faultFindIdList(String json, Context context) throws JSONException {
        DebugListData debugListData = new DebugListData();
        debugListData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("faultFindIdList code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return debugListData;
        } else {
            JSONArray dataArrayJson = resultJson.getJSONArray("data");
            ArrayList<DebugDetailData> ebugDetailDataList = new ArrayList<DebugDetailData>();
            for (int i = 0; i < dataArrayJson.length(); i++) {
                DebugDetailData debugDetailData = new DebugDetailData();
                JSONObject dataJson = (JSONObject) dataArrayJson.get(i);
                if (dataJson.has("content"))
                    debugDetailData.setContent(dataJson.getString("content"));
                if (dataJson.has("id"))
                    debugDetailData.setId(dataJson.getString("id"));
                if (dataJson.has("publishTime"))
                    debugDetailData.setPublishTime(dataJson.getString("publishTime"));
                if (dataJson.has("title"))
                    debugDetailData.setTitle(dataJson.getString("title"));
                ebugDetailDataList.add(debugDetailData);
            }
            debugListData.setDebugDetailData(ebugDetailDataList);
            debugListData.setFlag(true);
            LogUtil.printPushLog("faultFindIdList debugListData" + debugListData.toString());
            return debugListData;
        }
    }

    public static DebugDetailData faultFindIdDetail(String json, Context context) throws JSONException {
        DebugDetailData debugDetailData = new DebugDetailData();
        debugDetailData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("faultFindIdDetail code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if (TextUtils.isEmpty(lastError))
                lastError = context.getResources().getString(R.string.network_connection_failed);
            return debugDetailData;
        } else {
            JSONObject dataJson = resultJson.getJSONObject("data");
            if (dataJson.has("content"))
                debugDetailData.setContent(dataJson.getString("content"));
            if (dataJson.has("id"))
                debugDetailData.setId(dataJson.getString("id"));
            if (dataJson.has("publishTime"))
                debugDetailData.setPublishTime(dataJson.getString("publishTime"));
            if (dataJson.has("title"))
                debugDetailData.setTitle(dataJson.getString("title"));
            if (dataJson.has("url"))
                debugDetailData.setUrl(dataJson.getString("url"));
            debugDetailData.setFlag(true);
            LogUtil.printPushLog("faultFindIdDetail debugDetailData" + debugDetailData.toString());
            return debugDetailData;
        }
    }
    public static DebugListData faultSearch(String json, Context context) throws JSONException {
        DebugListData debugListData = new DebugListData();
        debugListData.setFlag(false);
        JSONObject resultJson = new JSONObject(json);
        int code = resultJson.getInt("code");
        LogUtil.printPushLog("faultSearch code" + code);
        if (code != 0) {
            lastError = resultJson.getString("msg");
            if(TextUtils.isEmpty(lastError))lastError = context.getResources().getString(R.string.network_connection_failed);
            return debugListData;
        } else {
            JSONObject dataObjectJson = resultJson.getJSONObject("data");
            JSONArray recordsArrayJson = dataObjectJson.getJSONArray("records");
            ArrayList<DebugDetailData> debugDetailDataList = new ArrayList<DebugDetailData>();
            for (int i = 0; i < recordsArrayJson.length(); i++) {
                DebugDetailData debugDetailData = new DebugDetailData();
                JSONObject dataJson = (JSONObject) recordsArrayJson.get(i);
                if (dataJson.has("content"))
                    debugDetailData.setContent(dataJson.getString("content"));
                if (dataJson.has("id"))
                    debugDetailData.setId(dataJson.getString("id"));
                if (dataJson.has("publishTime"))
                    debugDetailData.setPublishTime(dataJson.getString("publishTime"));
                if (dataJson.has("title"))
                    debugDetailData.setTitle(dataJson.getString("title"));
                debugDetailDataList.add(debugDetailData);
            }
            debugListData.setDebugDetailData(debugDetailDataList);
            debugListData.setFlag(true);
            LogUtil.printPushLog("faultSearch debugListData" + debugListData.toString());
            return debugListData;
        }
    }
}