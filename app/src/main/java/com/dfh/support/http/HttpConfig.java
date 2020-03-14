package com.dfh.support.http;

public class HttpConfig {
	public static boolean DevelopMode=false;
	private static final String HttpIP="http://101.37.168.242:8080/";//测试
	//private static final String HttpIP="http://pyex.pymeitan.com/";//正式
	private static final String HttpPolicyIP="http://101.37.168.242:8080/8848/";


	public static String GetHttpPolicyAdress()
	{
		return HttpPolicyIP;
	}
	public static String GetHttpClientAdress()
	{
		return HttpIP;
	}
	public static String url_login = "vip/info/login";
	public static String url_ads_find_carousel_all = "ads/find/carousel/all";
	public static String url_ads_find_general_pager = "ads/find/general/pager";
	public static String url_ads_browse = "ads/browse/";//ads/browse/{id}/info
	public static String url_ads_like = "ads/like/";//ads/like/{id}
	public static String url_parts_find_classify = "parts/find/classify";
	public static String url_parts_find_classify_id_pager = "parts/find/";//parts/find/{partsClassifyId}/pager
	public static String url_parts_id_detail = "parts/";//parts/{id}/detail

	public static String urL_serve_pager = "serve/pager";
	public static String url_serve_id_detail = "serve/";//serve/{id}/detail
	public static String url_policy_find_name = "policy/find/";//policy/find/{name}

	public static String url_fault_find_classify = "fault/find/classify";//fault/find/classify
	public static String url_fault_find_id_list = "fault/find/";//fault/find/{faultClassifyId}/list
	public static String url_fault_search = "fault/search";//fault/search
	public static String url_fault_id_detail = "fault/";//fault/{id}/detail


}