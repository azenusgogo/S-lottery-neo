package com.sogou.lottery.web.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 */
public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// URSUtil ursUtil = new URSUtil();
		// // 获取NTES_SESS Cookie，如果cookie存在就不判断是否有持久cookie
		// String accountId = Authentic.getUserNameCookie();
		// if (StringUtils.isBlank(accountId)) {
		// // 获取持久cookie
		// String persistentCookieValue =
		// ursUtil.getNeteasePersistentCookie(request);
		// if (persistentCookieValue != null &&
		// persistentCookieValue.trim().length() > 0) {
		// // 判断持久cookie是否有效
		// ntescode n = new ntescode();
		// int isCookieValidate =
		// n.validate_persistent_cookie(persistentCookieValue.getBytes(), 8,
		// CommonConstant.PERSISTENT_VALID_TIME, true);
		// if (isCookieValidate >= 0) {
		// // cookie中的用户名，163帐号没有域信息；其它帐号有域信息
		// String ssn = new String(n.ssn);
		// // 取URL及所有参数，重定向到URS系统chgcookie.jsp页面进行置换cookie
		// StringBuffer paramsBuf = new StringBuffer("");
		// Enumeration e = request.getParameterNames();
		// while (e.hasMoreElements()) {
		// String paramName = (String) e.nextElement();
		// paramsBuf.append(paramName).append("=").append(request.getParameter(paramName)).append('&');
		// }
		// //跳转到http://baojian.163.com/index.html页面时，直接访问域名首页 modified by elton
		// liu
		// StringBuffer retUrl = new StringBuffer("");
		// String uri = request.getRequestURI();
		// if (CommonConstant.INDEX_URI.equals(uri)) {
		// retUrl = retUrl.append(InitBean.getDomain());
		// } else {
		// retUrl = retUrl.append(request.getRequestURL());
		// }
		// if (paramsBuf != null && paramsBuf.length() > 0) {
		// retUrl.append("?").append(paramsBuf);
		// }
		// // 跳转到urs通过持久cookie置换登录cookie
		// String redirectUrl = new
		// StringBuffer("http://reg.163.com/chgcookie.jsp?product=").append(CommonConstant.PRODUCT).append("&username=").append(ssn).append("&persistCookie=").append(persistentCookieValue).append("&retUrl=").append(java.net.URLEncoder.encode(retUrl.toString(),
		// "UTF-8")).append("&loginUrl=").append(java.net.URLEncoder.encode(InitBean.getLoginUrl(),
		// "UTF-8")).toString();
		// response.sendRedirect(redirectUrl);
		// return false;
		// }
		// }
		// }
		return true;
	}
}
