package com.dfh.support.http;

import android.content.Context;
import android.text.TextUtils;


import com.dfh.support.entity.AdvertisementData;
import com.dfh.support.entity.AdvertisementListData;
import com.dfh.support.entity.ClassifyListData;
import com.dfh.support.entity.DebugDetailData;
import com.dfh.support.entity.DebugListData;
import com.dfh.support.entity.DebugMenuListData;
import com.dfh.support.entity.PartsData;
import com.dfh.support.entity.PolicyData;
import com.dfh.support.entity.ServeData;
import com.dfh.support.utils.LogUtil;

import org.json.JSONObject;


public class HttpJsonSend {


    /**
     * 轮播广告
     * @param context
     * @return
     */
    public static AdvertisementListData adsFindCarouselAll(Context context) {
        AdvertisementListData advertisementListData = new AdvertisementListData();
        advertisementListData.setFlag(false);
        String url = HttpConfig.url_ads_find_carousel_all;
        String path = HttpConfig.GetHttpClientAdress() + url;
        String result = HttpManager.httpGet(path, context);
        try {
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet adsFindCarouselAll result" + result);
                //解析json
                return HttpJsonAnaly.adsFindCarouselAll(result, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return advertisementListData;
    }

    /**
     * 广告列表
     * @param context
     * @param pageSize
     * @param pageNo
     * @return
     */
    public static boolean adsFindGeneralPager(Context context, String pageSize, String pageNo) {
        String url = HttpConfig.url_ads_find_general_pager;
        String path = HttpConfig.GetHttpClientAdress() + url;
        try {
            //方法四
            JSONObject formJSONObject = new JSONObject();
            formJSONObject.put("pageSize", pageSize);
            formJSONObject.put("pageNo", pageNo);
            String form = formJSONObject.toString();
            LogUtil.printPushLog("httpPost adsFindGeneralPager form：" + form);
            HttpManager.postJson(path, form, context);
            LogUtil.printPushLog("httpPost adsFindGeneralPager postJson");
        } catch (Exception e) {
            LogUtil.printPushLog("httpPost adsFindGeneralPager Exception");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 浏览广告
     * @param context
     * @param id
     * @return
     */
//    public static AdvertisementListData adsBrowseInfo(Context context,String id) {
//        AdvertisementListData advertisementListData = new AdvertisementListData();
//        advertisementListData.setFlag(false);
//        String url = HttpConfig.url_ads_browse;
//        String path = HttpConfig.GetHttpClientAdress() + url + id +"/info";
//        String result = HttpManager.httpGet(path, context);
//        try {
//            if (result != null && !TextUtils.isEmpty(result)) {
//                LogUtil.printPushLog("httpGet adsBrowseInfo result" + result);
//                //解析json
//                return HttpJsonAnaly.adsFindCarouselAll(result, context);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return advertisementListData;
//    }
    public static boolean adsBrowseInfo(Context context,String id) {
        String url = HttpConfig.url_ads_browse;
        String path = HttpConfig.GetHttpClientAdress() + url + id +"/info";
        String result = HttpManager.httpGet(path, context);
        try {
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet adsBrowseInfo result" + result);
                //解析json
                return HttpJsonAnaly.onResult(result, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 点赞广告
     * @param context
     * @param id
     * @return
     */
    public static boolean adsLike(Context context, String id) {
        String url = HttpConfig.url_ads_like;
        String path = HttpConfig.GetHttpClientAdress() + url + id;
        try {
            LogUtil.printPushLog("httpPut adsLike path:"+path);
            JSONObject formJSONObject = new JSONObject();
            formJSONObject.put("id", id);
            String form = formJSONObject.toString();
            LogUtil.printPushLog("httpPut adsLike form：" + form);
            String result = HttpManager.httpPut(path, form, context);
            LogUtil.printPushLog("httpPut adsLike httpPut");
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet adsLike result" + result);
                //解析json
                return HttpJsonAnaly.onResult(result, context);
            }
        } catch (Exception e) {
            LogUtil.printPushLog("httpPut adsLike Exception");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 配件分类
     * @param context
     * @return
     */
    public static ClassifyListData partsFindClassify(Context context) {
        ClassifyListData classifyListData = new ClassifyListData();
        classifyListData.setFlag(false);
        String url = HttpConfig.url_parts_find_classify;
        String path = HttpConfig.GetHttpClientAdress() + url;
        String result = HttpManager.httpGet(path, context);
        try {
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet partsFindClassify result" + result);
                //解析json
                return HttpJsonAnaly.partsFindClassify(result, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classifyListData;
    }

    /**
     * 备件分页
     * @param context
     * @param partsClassifyId
     * @param pageSize
     * @param pageNo
     * @return
     */
    public static boolean partsFindClassifyIdPager(Context context, String partsClassifyId, String pageSize, String pageNo) {
        String url = HttpConfig.url_parts_find_classify_id_pager;//parts/find/{partsClassifyId}/pager
        String path = HttpConfig.GetHttpClientAdress() + url + partsClassifyId +"/pager";
        try {
            //方法四
            JSONObject formJSONObject = new JSONObject();
            formJSONObject.put("pageSize", pageSize);
            formJSONObject.put("pageNo", pageNo);
            //formJSONObject.put("partsClassifyId", partsClassifyId);
            String form = formJSONObject.toString();
            LogUtil.printPushLog("httpPost partsFindClassifyIdPager form：" + form);
            HttpManager.postJson(path, form, context);
            LogUtil.printPushLog("httpPost partsFindClassifyIdPager postJson");
        } catch (Exception e) {
            LogUtil.printPushLog("httpPost partsFindClassifyIdPager Exception");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 备件详情
     * @param context
     * @param id
     * @return
     */
    public static PartsData partsIdDetail(Context context, String id) {
        PartsData partsData = new PartsData();
        partsData.setFlag(false);
        String url = HttpConfig.url_parts_id_detail;//parts/{id}/detail
        String path = HttpConfig.GetHttpClientAdress() + url + id +"/detail";
        String result = HttpManager.httpGet(path, context);
        try {
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet adsBrowseInfo result" + result);
                //解析json
                return HttpJsonAnaly.partsIdDetail(result, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return partsData;
    }

    public static final String SERVE_TYPE_SERVICE = "1";
    public static final String SERVE_TYPE_BUYING = "2";

    public static final int COME_FROM_MAIN = 1;
    public static final int COME_FROM_LIST = 2;

    public static final int TYPE_MAIN_SERVICE = 1;
    public static final int TYPE_MAIN_BUYING = 2;
    public static final int TYPE_LIST_SERVICE = 3;
    public static final int TYPE_LIST_BUYING = 4;

    /**
     * 服务分页
     * @param context
     * @param area
     * @param lat
     * @param lng
     * @param pageSize
     * @param pageNo
     * @param type
     * @return
     */
    public static boolean servePager(Context context,String area,String lat,String lng
            , String pageSize, String pageNo,String type,int comeFrom) {
        String url = HttpConfig.urL_serve_pager;
        String path = HttpConfig.GetHttpClientAdress() + url;
        try {
            JSONObject formJSONObject = new JSONObject();
            formJSONObject.put("area", area);
            formJSONObject.put("lat", lat);
            formJSONObject.put("lng", lng);
            formJSONObject.put("pageNo", pageNo);
            formJSONObject.put("pageSize", pageSize);
            formJSONObject.put("type", type);
            String form = formJSONObject.toString();
            LogUtil.printPushLog("httpPost servePager form：" + form);
            if(comeFrom==COME_FROM_MAIN){
                if(SERVE_TYPE_SERVICE.equals(type)){
                    HttpManager.postJsonType(path, form, context,TYPE_MAIN_SERVICE);
                }else{
                    HttpManager.postJsonType(path, form, context,TYPE_MAIN_BUYING);
                }
            }else{
                if(SERVE_TYPE_SERVICE.equals(type)){
                    HttpManager.postJsonType(path, form, context,TYPE_LIST_SERVICE);
                }else{
                    HttpManager.postJsonType(path, form, context,TYPE_LIST_BUYING);
                }
            }
            LogUtil.printPushLog("httpPost servePager postJson");
        } catch (Exception e) {
            LogUtil.printPushLog("httpPost servePager Exception");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 服务详情
     * @param context
     * @param id
     * @return
     */
    public static ServeData serveIdDetail(Context context, String id) {
        ServeData serveData = new ServeData();
        serveData.setFlag(false);
        String url = HttpConfig.url_serve_id_detail;//serve/{id}/detail
        String path = HttpConfig.GetHttpClientAdress() + url + id +"/detail";
        String result = HttpManager.httpGet(path, context);
        try {
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet serveIdDetail result" + result);
                //解析json
                return HttpJsonAnaly.serveIdDetail(result, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serveData;
    }

    /**
     * 获取政策
     * @param context
     * @param name
     * @return
     */
    public static PolicyData policyFindName(Context context, String name) {
        PolicyData policyData = new PolicyData();
        policyData.setFlag(false);
        String url = HttpConfig.url_policy_find_name;//policy/find/{name}
        String path = HttpConfig.GetHttpClientAdress() + url + name;
        LogUtil.printPushLog("httpGet policyFindName path" + path);
        String result = HttpManager.httpGet(path, context);
        try {
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet policyFindName result" + result);
                //解析json
                return HttpJsonAnaly.policyFindName(result, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return policyData;
    }

    /**
     * 故障分栏
     * @param context
     * @return
     */
    public static DebugMenuListData faultFindClassify(Context context) {
        DebugMenuListData debugMenuListData = new DebugMenuListData();
        debugMenuListData.setFlag(false);
        String url = HttpConfig.url_fault_find_classify;
        String path = HttpConfig.GetHttpClientAdress() + url;
        String result = HttpManager.httpGet(path, context);
        try {
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet faultFindClassify result" + result);
                //解析json
                return HttpJsonAnaly.faultFindClassify(result, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return debugMenuListData;
    }

    /**
     * 故障列表
     * @param context
     * @param id
     * @return
     */
    public static DebugListData faultFindIdList(Context context, String id) {
        DebugListData debugListData = new DebugListData();
        debugListData.setFlag(false);
        String url = HttpConfig.url_fault_find_id_list;
        String path = HttpConfig.GetHttpClientAdress() + url + id +"/list";
        String result = HttpManager.httpGet(path, context);
        try {
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet faultFindIdList result" + result);
                //解析json
                return HttpJsonAnaly.faultFindIdList(result, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return debugListData;
    }

    /**
     * 搜索详情
     * @param context
     * @param id
     * @return
     */
    public static DebugDetailData faultFindIdDetail(Context context, String id) {
        DebugDetailData debugDetailData = new DebugDetailData();
        debugDetailData.setFlag(false);
        String url = HttpConfig.url_fault_id_detail;//fault/{id}/detail
        String path = HttpConfig.GetHttpClientAdress() + url + id +"/detail";
        String result = HttpManager.httpGet(path, context);
        try {
            if (result != null && !TextUtils.isEmpty(result)) {
                LogUtil.printPushLog("httpGet faultFindIdDetail result" + result);
                //解析json
                return HttpJsonAnaly.faultFindIdDetail(result, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return debugDetailData;
    }

    /**
     * 故障搜索
     * @param context
     * @param keywords
     * @param pageSize
     * @param pageNo
     * @return
     */
    public static boolean faultSearch(Context context,String keywords, String pageSize, String pageNo) {
        String url = HttpConfig.url_fault_search;
        String path = HttpConfig.GetHttpClientAdress() + url;
        try {
            JSONObject formJSONObject = new JSONObject();
            formJSONObject.put("keywords", keywords);
            formJSONObject.put("pageNo", pageNo);
            formJSONObject.put("pageSize", pageSize);
            String form = formJSONObject.toString();
            LogUtil.printPushLog("httpPost faultSearch form：" + form);
            HttpManager.postJson(path, form, context);
            LogUtil.printPushLog("httpPost faultSearch postJson");
        } catch (Exception e) {
            LogUtil.printPushLog("httpPost faultSearch Exception");
            e.printStackTrace();
        }
        return true;
    }
}