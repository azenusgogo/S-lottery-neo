package com.sogou.lottery.web.web.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.sogou.lottery.common.constant.EnvironmentConstant;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.web.service.init.EnvironmentBean;

public class Authentic {
	
	public static final String HTTP_HEADER_USER_ID = "x-sohupassport-userid"; // 获取当前登录用户名的Http
	
	// Header
	public static String getUserId(HttpServletRequest request) {
	
		String userName = "";
		if (EnvironmentBean.getEnvironment().equals(EnvironmentConstant.ENV_LOCAL)) {
			userName = "sgcaipiao@sogou.com";
		} else {
			userName = (String) request.getHeader(HTTP_HEADER_USER_ID);
		}
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String paramName = (String) headerNames.nextElement();
			LOG.controller.debug(paramName + "======");
		}
		LOG.controller.debug(userName + "--------------");
		return userName;
	}
}
