package com.sogou.lottery.web.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.util.LotteryUtil;
import com.sogou.lottery.web.web.vo.UserBehavior;

public class LoggerInterceptor implements HandlerInterceptor {
	
	private Logger log = LOG.accessLog;
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
	
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
		if (log.isDebugEnabled()) {
			// log.debug(request.getRequestURI() + " -response- " + mav == null
			// ? null : JsonUtil.toJson(mav.getModel()));
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		// 对非iframe的请求
		if (!LotteryUtil.isIframe(request)) {
			try {
				UserBehavior userBehavior = new UserBehavior(request);
				log.info(userBehavior);
			} catch (Exception e) {
				log.error("error", e);
			}
		}
		return true;
	}
	
}
