package com.sogou.lottery.web.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.web.service.init.EnvironmentBean;

/**
 * 确认登录的拦截器
 */
public class ClickJackingInterceptor implements HandlerInterceptor {
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		// 如果不是搜狗彩票站内的referer，需做点击劫持防范
		String requestURI = request.getRequestURI();
		if (!SystemConfigs.getIframeUrlList().contains(requestURI)) {
			// && !requestURI.startsWith("/iframe/")) {
			String referer = request.getHeader("Referer");
			// 如果refer不是站内，则做点击劫持防范
			if (StringUtils.isBlank(referer)) {
				response.setHeader("X-Frame-Options", "DENY");
			} else {
				if (!referer.startsWith(EnvironmentBean.getDomainUrl()) && !referer.startsWith(EnvironmentBean.getHttpsDomainUrl())) {
					if (referer.indexOf("tongji.baidu.com") == -1 && referer.indexOf("sitecenter.baidu.com") == -1) {
						response.setHeader("X-Frame-Options", "DENY");
					}
				}
			}
		}
		
		return true;
	}
	
}
