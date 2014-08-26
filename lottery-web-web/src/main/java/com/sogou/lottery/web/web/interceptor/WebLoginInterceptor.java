package com.sogou.lottery.web.web.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.web.web.LoginController;
import com.sogou.lottery.web.web.util.Authentic;

/**
 * 确认登录的拦截器
 */
public class WebLoginInterceptor implements HandlerInterceptor {
	
	private Logger log = LOG.controller;
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		String userId = Authentic.getUserId(request);
		try {
			if (StringUtils.isBlank(userId)) {
				LOG.controller.debug("Intercept request: need to login first, uri= " + request.getRequestURI());
				// 1.未登录，则先forward到login.html，将当前页面的url及参数set到request中，然后由login.ftl中取出，在提交给urs的form中，将url提交，urs登录之后会redirect回当前url,added
				// by lvzhenyu 20121023
				StringBuffer paramsBuffer = new StringBuffer();
				// 1.1把参数拼起来
				Enumeration<?> e = request.getParameterNames();
				while (e.hasMoreElements()) {
					String paramName = (String) e.nextElement();
					if (StringUtils.isNotBlank(paramName) && StringUtils.isNotBlank(request.getParameter(paramName))) {
						paramsBuffer.append(paramName.replaceAll("\n\r", "")).append("=").append(request.getParameter(paramName).replaceAll("\n\r", "")).append('&');
					}
				}
				StringBuffer retUrl = new StringBuffer(request.getRequestURL());
				if (paramsBuffer != null && paramsBuffer.length() > 0) {
					retUrl.append("?").append(paramsBuffer);
				}
				request.setAttribute(WebConstant.REDIRECT_URL, retUrl);
				request.getRequestDispatcher(LoginController.CTL_LOGIN + ".html").forward(request, response);
				return false;
			}
		} catch (Exception e) {
			log.error("[LoginInterceptor][userId=" + userId + "]", e);
		}
		return true;
	}
}
