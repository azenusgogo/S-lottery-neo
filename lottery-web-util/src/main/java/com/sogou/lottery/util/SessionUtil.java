package com.sogou.lottery.util;

import javax.servlet.http.HttpSession;

import com.sogou.lottery.common.constant.WebConstant;

public class SessionUtil {
	
	public static Object getAttribute(String name) {

		return WebConstant.sessionTL.get().getAttribute(name);
	}
	
	public static void setAttribute(String name, Object value) {

		WebConstant.sessionTL.get().setAttribute(name, value);
	}
	
	public static void removeAttribute(String name) {

		WebConstant.sessionTL.get().removeAttribute(name);
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
	
	public static HttpSession getSession() {

		return WebConstant.sessionTL.get();
	}
	
}
