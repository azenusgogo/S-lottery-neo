package com.sogou.lottery.web.web.vo;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.netease.common.util.MD5Util;
import com.sogou.lottery.util.LotteryUtil;
import com.sogou.lottery.web.service.init.EnvironmentBean;
import com.sogou.lottery.web.web.util.Authentic;

/**
 * 用户行为model
 * 
 * @author lvzhenyu
 */
public class UserBehavior {
	
	private String accountId;
	private String sessionId;
	private String uri;
	private StringBuffer param;
	private String refer;
	private String ip;
	private String userAgent;
	private String ssuv;
	
	public UserBehavior(HttpServletRequest request) {
	
		accountId = Authentic.getUserId(request);
		if (StringUtils.isBlank(accountId)) {
			accountId = "";
		}
		sessionId = request.getSession().getId();
		if (StringUtils.isNotBlank(sessionId)) {
			sessionId = MD5Util.get(sessionId, "utf-8");
		} else {
			sessionId = "";
		}
		uri = request.getRequestURI();
		Iterator<?> iterator = request.getParameterMap().entrySet().iterator();
		param = new StringBuffer();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			@SuppressWarnings("rawtypes")
			Entry entry = (Entry) iterator.next();
			if (i == 1)
				param.append(entry.getKey()).append("=");
			else
				param.append("&").append(entry.getKey()).append("=");
			if (entry.getValue() instanceof String[]) {
				param.append(((String[]) entry.getValue())[0]);
			} else {
				param.append(entry.getValue());
			}
		}
		ip = LotteryUtil.getLastIp(request);
		refer = request.getHeader("Referer");
		if (StringUtils.isNotBlank(refer)) {
			refer = refer.replaceAll(EnvironmentBean.getDomainUrl(), "/").replaceAll(EnvironmentBean.getHttpsDomainUrl(), "/");
		} else {
			refer = "";
		}
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
			if (c.getName().equalsIgnoreCase("ssuv") && StringUtils.isNotBlank(c.getValue())) {
				ssuv = c.getValue().trim();
			}
		}
		userAgent = request.getHeader("user-agent");
	}
	
	public UserBehavior() {
	
	}
	
	@Override
	public String toString() {
	
		StringBuilder builder = new StringBuilder();
		builder.append("UserBehavior=[accountId=(");
		builder.append(accountId);
		builder.append(")], [sessionId=(");
		builder.append(sessionId);
		builder.append(")], [uri=(");
		builder.append(uri);
		builder.append(")], [param=(");
		builder.append(param);
		builder.append(")], [refer=(");
		builder.append(refer);
		builder.append(")], [ip=(");
		builder.append(ip);
		builder.append(")], [ssuv=(");
		builder.append(ssuv);
		builder.append(")], [userAgent=(");
		builder.append(userAgent);
		builder.append(")]");
		return builder.toString();
	}
	
	public String getAccountId() {
	
		return accountId;
	}
	
	public void setAccountId(String accountId) {
	
		this.accountId = accountId;
	}
	
	public String getSessionId() {
	
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
	
		this.sessionId = sessionId;
	}
	
	public String getUri() {
	
		return uri;
	}
	
	public void setUri(String uri) {
	
		this.uri = uri;
	}
	
	public StringBuffer getParam() {
	
		return param;
	}
	
	public void setParam(StringBuffer param) {
	
		this.param = param;
	}
	
	public String getRefer() {
	
		return refer;
	}
	
	public void setRefer(String refer) {
	
		this.refer = refer;
	}
	
	public String getIp() {
	
		return ip;
	}
	
	public void setIp(String ip) {
	
		this.ip = ip;
	}
	
	public String getUserAgent() {
	
		return userAgent;
	}
	
	public void setUserAgent(String userAgent) {
	
		this.userAgent = userAgent;
	}
	
	public String getSsuv() {
	
		return ssuv;
	}
	
	public void setSsuv(String ssuv) {
	
		this.ssuv = ssuv;
	}
	
}
