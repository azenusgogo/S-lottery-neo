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
public class AjaxConstant {
	
	public static final String RETURN_CODE_NAME = "retcode";// ajax请求返回的代码名称
	public static final String RETURN_DESC_NAME = "retdesc";// ajax请求返回的描述名称
	public static final int SUCCESS_CODE = 200;// 操作成功code
	public static final String SUCCESS_DESC = "操作成功";// 操作成功描述
	public static final int PARAM_ERROR_CODE = 400;//
	public static final String PARAM_ERROR_DESC = "参数错误";//
	public static final int NOT_LOGIN_CODE = 401;// 未登录code
	public static final String NOT_LOGIN_DESC = "未登录";//
	public static final int AUTHENTIC_ERROR_CODE = 403;//
	public static final String AUTHENTIC_ERROR_DESC = "IP无权限";//
	public static final int SYSTEM_ERROR_CODE = 500;//
	public static final String SYSTEM_ERROR_DESC = "系统错误";//
	
	public static final int YES_CODE = 1;// 是操作
	public static final int NO_CODE = 0;// 否操作
	public static final int LOGIN_NO = 0;
	public static final int LOGIN_YES = 1;
}
