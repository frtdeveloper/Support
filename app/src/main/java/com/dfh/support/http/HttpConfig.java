package com.dfh.support.http;

public class HttpConfig {
	public static boolean DevelopMode=false;
	private static final String HttpIP="http://39.108.182.174:8080/";//测试
	//private static final String HttpIP="http://pyex.pymeitan.com/";//正式


	public static String GetHttpClientAdress()
	{
		return HttpIP;
	}
	public static String url_login = "vip/info/login";
	public static String url_register = "vip/info/register";
	public static String url_send_sms = "oth/send";
	public static String url_check_sms = "oth/valid";
	public static String url_find_password = "vip/info/forget/pwd";
	public static String url_update_single_info = "vip/info/auth/single/update";
	public static String url_update_multi_info ="vip/info/auth/multi/update";
	public static String url_info_msg = "vip/info/auth/msg";

	public static String url_vip_branch_list = "auth/vip/branch/list";//GET学习学科范围列表
	public static String url_vip_branch_modify = "auth/vip/branch/modify";//PUT添加学习学科
	public static String url_sub_branch_list = "auth/sub/branch/list";//GET学科列表
	public static String url_vip_grade_list = "auth/vip/grade/list";//GET学习年级列表
	public static String url_vip_grade_modify = "auth/vip/grade/modify";//PUT修改学习年级
	public static String url_sub_grade_list = "auth/sub/grade/list";//GET年级列表
	public static String url_sub_grade_all_list = "auth/sub/grade/all/list";//GET年级列表

	public static String url_sub_resource_list = "auth/sub/resource/list";//资源搜索 ;
	public static String url_sub_resource_browse = "auth/sub/resource/browse/";//资源详情 {id}
	public static String url_oth_upload = "oth/upload";//
	public static String url_sub_question_search = "auth/sub/question/search";//题目搜索 ;
	public static String url_sub_question_finished = "auth/sub/question/finished";//完成答题
	public static String url_sub_question_analysis = "auth/sub/question/analysis/";//题目解析{questionId}
	public static String url_vip_mistakes_collection_search = "auth/vip/mistakes/collection/search";//错题搜索
	public static String url_auth_vip_mistakes_collection_search = "auth/sub/question/bealike";//举一反三
}