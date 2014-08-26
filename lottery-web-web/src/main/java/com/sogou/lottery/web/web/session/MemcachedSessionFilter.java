package com.sogou.lottery.web.web.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sogou.lottery.base.constant.SystemConfigs;

public class MemcachedSessionFilter extends HttpServlet implements Filter {
	
	private static final long serialVersionUID = -365105405910803550L;
	public Logger sessionLog = Logger.getLogger("session.log");
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
	
		if (SystemConfigs.get("memSessionSwitch", "true").equalsIgnoreCase("true")) {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			String sid = request.getSession().getId();
			if (StringUtils.isNotBlank(request.getParameter("jsessionid"))) {
				String jsessionid = request.getParameter("jsessionid");
				if (!StringUtils.equals(sid, jsessionid)) {
					HttpSession session = request.getSession(false);
					if (session != null) session.invalidate();
					Cookie cookie = new Cookie("JSESSIONID", jsessionid);
					cookie.setDomain(request.getServerName());
					cookie.setPath("/");
					response.addCookie(cookie);
					sid = jsessionid;
				}
			}
			sessionLog.info("get session in memcached...");
			filterChain.doFilter(new HttpServletRequestWrapper(sid, request), servletResponse);
		} else {
			sessionLog.info("get session in httpsession...");
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
}
