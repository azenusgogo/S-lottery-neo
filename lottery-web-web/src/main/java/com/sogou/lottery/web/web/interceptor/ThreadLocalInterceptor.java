package com.sogou.lottery.web.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.util.ErrorMessageUtil;
import com.sogou.lottery.util.RequestUtil;
import com.sogou.lottery.web.service.init.EnvironmentBean;

/**
 * 在本地threadLocal中保存request
 */
public class ThreadLocalInterceptor implements HandlerInterceptor {
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
	
		ErrorMessageUtil.clear();
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
		List<String> emList = ErrorMessageUtil.get();
		if (emList != null) {
			RequestUtil.setAttribute(WebConstant.ERROR_MESSAGE_REQUEST_KEY, emList);
		}
		WebConstant.requestTL.remove();
		WebConstant.responseTL.remove();
		WebConstant.sessionTL.remove();
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		// uri中的.不能超过两个
		if (SystemConfigs.getIntValue("uri_point_no_exceed_2_interceptor", 1) == 1) {
			String uri = request.getRequestURI();
			if (StringUtils.isNotBlank(uri)) {
				int times = 0;
				for (int i = 0; i < uri.length(); ++i) {
					if (uri.charAt(i) == '.') {
						++times;
					}
				}
				if (times >= 2 && !uri.startsWith("/bg/cron")) {
					request.getRequestDispatcher(WebConstant.INDEX_URI).forward(request, response);
					return false;
				}
			} else {
				request.getRequestDispatcher(WebConstant.INDEX_URI).forward(request, response);
				return false;
			}
		}
		WebConstant.requestTL.set(request);
		WebConstant.responseTL.set(response);
		WebConstant.sessionTL.set(request.getSession());
		request.setAttribute("cdnBaseUrl", EnvironmentBean.getCdnBaseUrl());
		request.setAttribute("cdnBaseUrl2", EnvironmentBean.getCdnBaseUrl2());
		request.setAttribute("cdnBaseUrl3", EnvironmentBean.getCdnBaseUrl3());
		request.setAttribute("cdnBaseUrl4", EnvironmentBean.getCdnBaseUrl4());
		request.setAttribute("versionId", SystemConfigs.getVersionId());
		request.setAttribute("domainUrl", EnvironmentBean.getDomainUrl());
		// 设置SEO底部导航
		request.setAttribute("seoFooterNav", SystemConfigs.get("seoFooterNav", "福利彩票双色球,/ssq/;彩票开奖,/kaijiang/;双色球投注,/ssq/;足球胜负彩,/f14/;足球任选九,/f9/;七星彩,/qxc/;七乐彩,/qlc/;双色球开奖结果,/calculator/ssq/;"));
		
		return true;
	}
}
