package com.sogou.lottery.common.constant;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Web静态常量公共接口
 * 
 * @author
 */
public class WebConstant {
	
	public static final String INDEX_URI = "/index.html";
	public static final ThreadLocal<HttpServletRequest> requestTL = new ThreadLocal<HttpServletRequest>(); // 保存request的threadlocal
	public static final ThreadLocal<HttpServletResponse> responseTL = new ThreadLocal<HttpServletResponse>(); // 保存response的threadlocal
	public static final ThreadLocal<HttpSession> sessionTL = new ThreadLocal<HttpSession>(); // 保存session的threadlocal
	public static final String[] CDN_URL = { "http://img01.caipiao.sogoucdn.com", "http://img02.caipiao.sogoucdn.com", "http://img03.caipiao.sogoucdn.com", "http://img04.caipiao.sogoucdn.com" };
	public static final String LOGIN_URI = "/login.html"; // 登录uri
	// 持久cookie的有效时间(秒数，保持2周内有效)
	public static final String REDIRECT_URL = "redirectUrl";
	public static final String CATEGORY_SOURCE = "/products/";
	
	public static final String RETURN_CODE_NAME = "retcode";// ajax请求返回的代码名称
	public static final String RETURN_DESC_NAME = "retdesc";// ajax请求返回的描述名称
	public static final int THREAD_POOL_CORE_SIZE = 5;// 线程池最少线程数
	public static final int THREAD_POOL_MAX_SIZE = 100;// 最大线程数
	public static final int THREAD_MAX_THREAD_WAIT = 1000;// 最大线程等待数
	public static final int THREAD_POOL_WAIT_SECONDS = 5 * 60;// 最长等待时间
	public static final int SUCCESS_CODE = 200;// 操作成功code
	public static final String SUCCESS_DESC = "操作成功";// 操作成功描述
	
	public static final ThreadLocal<List<String>> ERROR_MESSAGE_TL = new ThreadLocal<List<String>>(); // 保存错误信息的threadlocal
	public static final String ERROR_MESSAGE_REQUEST_KEY = "errorMessages";// 错误提示信息在request-attribute中的key
	
	public static final String FAIL_PAGE = "/fail.html"; // 参数错误、404错误跳转页
	public static final String ERROR_PAGE = "/error.html";// 运行时错误跳转页
	
	public static final String FAIL_FTL = "fail"; // 参数错误、404错误跳转页
	public static final String ERROR_FTL = "error";// 运行时错误跳转页
	
	public static final int YES_CODE = 1;// 是操作
	public static final int NO_CODE = 0;// 否操作
	public static final int LOGIN_NO = 0;
	public static final int LOGIN_YES = 1;
}
