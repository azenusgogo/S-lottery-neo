package com.sogou.lottery.web.web.interceptor;

import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.common.constant.LOG;

/**
 * 参数过滤拦截器
 */
public class ParamCheckInterceptor implements HandlerInterceptor {
	
	private static final String XSS_PATTERN = "((<.+>)|(\\{.+\\})|(\\(.+\\).*>)|(/\\*.*/))+";
	private static final String XSS_PATTERN2 = "<|>|&gt|&lt|&#|/\\*.*\\*/|vbscript:|javascript:|=\\s*[\\[{\"']";
	private static Pattern pattern = Pattern.compile(XSS_PATTERN);
	private static Pattern pattern2 = Pattern.compile(XSS_PATTERN2);
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		@SuppressWarnings("rawtypes")
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String paramName = (String) e.nextElement();
			String paramValue = request.getParameter(paramName) == null ? null : StringUtils.trimToEmpty(request.getParameter(paramName));
			if (paramValue != null) {
				if (pattern.matcher(paramValue).find() || pattern2.matcher(paramValue).find()) {
					LOG.controller.debug("risky param value: " + paramValue);
					request.getRequestDispatcher("/WEB-INF/ftl/error.ftl").forward(request, response);
					return false;
				}
			}
		}
		return true;
	}
	
}
