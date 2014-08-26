package com.sogou.lottery.web.web.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.mortbay.util.ajax.JSON;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.common.constant.AjaxConstant;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.web.service.init.EnvironmentBean;
import com.sogou.lottery.web.web.util.Authentic;

/**
 * 确认登录的拦截器
 */
public class AjaxLoginInterceptor implements HandlerInterceptor {
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		// 防止CSRF攻击：判断ajax的refer是否是站内，默认开启，调试时可设置开关为0
		if (SystemConfigs.getIntValue("defend_csrf_switch_key", 1) == 1) {
			String referer = request.getHeader("Referer");
			// 如果不是保健品站内的referer，则保存referer
			if (StringUtils.isNotBlank(referer)) {
				// refer为站外
				String domain = EnvironmentBean.getDomainUrl();
				String httpsDomain = domain.replace("http://", "https://");
				if (!referer.startsWith(domain) && !referer.startsWith(httpsDomain)) {
					LOG.controller.debug("Intercept request: fount inconssist refer, uri= " + request.getRequestURI());
					return false;
				}
			} else {
				LOG.controller.debug("Intercept request: fount differ refer, uri= " + request.getRequestURI());
				// refer为空
				return false;
			}
		}
		
		String userName = Authentic.getUserId(request);
		if (StringUtils.isBlank(userName)) {
			LOG.controller.debug("Intercept request: need to login first, uri= " + request.getRequestURI());
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put(WebConstant.RETURN_CODE_NAME, AjaxConstant.NOT_LOGIN_CODE);
			map.put(WebConstant.RETURN_DESC_NAME, AjaxConstant.NOT_LOGIN_DESC);
			response.setCharacterEncoding("utf-8");
			try {
				response.getWriter().print(JSON.toString(map));
			} catch (IOException e) {
			}
			
			return false;
		}
		return true;
	}
	
}
