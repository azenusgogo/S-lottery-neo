package com.sogou.lottery.web.web.session;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;


public class HttpServletRequestWrapper extends
		javax.servlet.http.HttpServletRequestWrapper {
	
	String sid = "";
	
	public HttpServletRequestWrapper(String sid, HttpServletRequest request) {

		super(request);
		this.sid = sid;
	}
	
	public HttpSession getSession(boolean create) {

		return (HttpSession) new HttpSessionSidWrapper(this.sid, super.getSession(create));
	}
	
	public HttpSession getSession() {

		return new HttpSessionSidWrapper(this.sid, super.getSession());
	}
	
	public String getHeader(String name) {

		if (name.equalsIgnoreCase("accept")) {
			String value = super.getHeader(name);
			if (StringUtils.isNotBlank(value)) {
				if (value.indexOf("/") != -1 || value.charAt(value.length() - 1) == '/') {
					return "*/*";
				}
			}
		}
		return super.getHeader(name);
	}
	
}
