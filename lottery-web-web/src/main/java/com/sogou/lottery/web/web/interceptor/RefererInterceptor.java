package com.sogou.lottery.web.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 外部来源的拦截器
 */
public class RefererInterceptor implements HandlerInterceptor {
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		// if (SessionUtil.getAttribute(CommonConstant.REFERER_SESSION_KEY) ==
		// null) {
		// String referer = request.getHeader("Referer");
		// if (referer == null) {
		// referer = "";
		// }
		// LogConstant.debugLog.info("[Referer]referer=" + referer);
		// SessionUtil.setAttribute(CommonConstant.REFERER_SESSION_KEY,
		// referer);
		// }
		
		return true;
	}
	
	// /**
	// * 检查referer是否是本域，是本域返回false
	// *
	// * @param referer
	// * @return
	// */
	// private boolean checkReferer(String referer) {
	//
	// boolean flag = false;
	// String refererCase = referer.toLowerCase();
	// for (String url : InitBean.getDomainRefererSet()) {
	// if (refererCase.startsWith(url)) {
	// return flag;
	// }
	// }
	// flag = true;
	// return flag;
	// }
}
