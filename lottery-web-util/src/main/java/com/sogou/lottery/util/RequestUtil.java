package com.sogou.lottery.util;

import javax.servlet.http.HttpServletRequest;

import com.sogou.lottery.common.constant.WebConstant;

public class RequestUtil {
	
	public static Object getAttribute(String name) {

		return WebConstant.requestTL.get().getAttribute(name);
	}
	
	public static void setAttribute(String name, Object value) {

		WebConstant.requestTL.get().setAttribute(name, value);
	}
	
	public static void removeAttribute(String name) {

		WebConstant.requestTL.get().removeAttribute(name);
	}
	
	public static boolean containsKey(String name) {

		Object value = getAttribute(name);
		if (value != null) {
			return true;
		}
		return false;
	}
	
	public static boolean notContainsKey(String name) {

		Object value = getAttribute(name);
		if (value == null) {
			return true;
		}
		return false;
	}
	
	public static HttpServletRequest getRequest() {

		return WebConstant.requestTL.get();
	}
	
	public static String getParameter(String name) {

		return WebConstant.requestTL.get().getParameter(name);
	}
}
