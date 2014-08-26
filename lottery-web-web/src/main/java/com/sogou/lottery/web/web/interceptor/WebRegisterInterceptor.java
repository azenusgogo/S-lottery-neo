package com.sogou.lottery.web.web.interceptor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.memcached.MemcachedConstantBase;
import com.sogou.lottery.base.util.DESUtil;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.BaseObject;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.web.service.user.service.UserCacheService;
import com.sogou.lottery.web.web.register.LoginConstant;
import com.sogou.lottery.web.web.vo.UserBehavior;

public class WebRegisterInterceptor implements HandlerInterceptor {
	
	private final static Logger log = LOG.controller;
	protected final static String COOKIE_UUID_CP = "cpuid";
	public final static String REGISTER = "register";
	
	@Autowired
	private UserCacheService userCacheService;
	
	/*
	 * a) 每个ip多个cookie值（类似局域网注册）100次/天 （后期会对数据做分析，加到白名单中，放大注册次数限制） b)
	 * 每个cookie多ip（同一台电脑上不断的换IP注册）5次/天 c) ip+cookie限制用户 10次/天
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		try {
			UserBehavior ub = new UserBehavior(request);
			if (StringUtils.isNotBlank(ub.getIp())) {
				String regex = SystemConfigs.get(LoginConstant.IP_WHITE_EXP, LoginConstant.IP_WHITE_EXP_DEFAULT);
				if (containsIp(ub.getIp(), regex)) {
					if (log.isDebugEnabled()) {
						log.debug(String.format("Found ip[%s] in white list", ub.getIp()));
					}
					return true;
				}
			}
			
			Cookie[] cookies = request.getCookies();
			String cpuid = null;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (COOKIE_UUID_CP.equals(cookie.getName())) {
						cpuid = cookie.getValue();
						break;
					}
				}
			}
			/*
			 * if (response != null && StringUtils.isEmpty(cpuid)) { cpuid =
			 * generateUID(); Cookie cookie = new Cookie(COOKIE_UUID_CP, cpuid);
			 * int age = SystemConfigs.getIntValue("REGISTER_UUID_AGE", 60 * 60
			 * * 24 * 1);// 保留1天 cookie.setMaxAge(age);
			 * response.addCookie(cookie); }
			 */
			if (!isValidUid(cpuid)) {
				log.error(String.format("Found illegal cpuid[%s] in white list", cpuid));
				request.getRequestDispatcher(WebConstant.ERROR_PAGE).forward(request, response);
				return false;
			}
			
			List<String> keyList = Lists.newArrayList();
			if (StringUtils.isNotBlank(cpuid) && StringUtils.isNotBlank(ub.getIp())) {
				keyList.add(getBehaviourKey(Restrict.RestrictIPCPUID, ub.getIp(), cpuid));
			}
			if (StringUtils.isNotBlank(cpuid)) {
				keyList.add(getBehaviourKey(Restrict.RestrictCPUID, cpuid));
			}
			if (StringUtils.isNotBlank(ub.getIp())) {
				keyList.add(getBehaviourKey(Restrict.RestrictIP, ub.getIp()));
			}
			
			Object[] objs = new Object[0];
			if (!keyList.isEmpty()) {
				objs = userCacheService.getMultiMemcachedData(MemcachedConstantBase.MEMCACHED_USER, keyList.toArray(new String[0]));
			}
			if (objs == null) {
				// 说明没有注册过
				return true;
			}
			boolean intercept = false;
			for (int i = 0; i < objs.length; i++) {
				Behaviour be = (Behaviour) objs[i];
				if (be != null && be.getTimes() >= getBehaviousLimit(be)) {
					intercept = true;
					log.error(String.format("Found illegal user behaviour[%s]", be));
					break;
				}
			}
			if (intercept) {
				request.getRequestDispatcher(WebConstant.ERROR_PAGE).forward(request, response);
				return false;
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
		try {
			Boolean ok = (Boolean) request.getAttribute(REGISTER);
			if (ok == null || !ok) {
				return;
			}
			UserBehavior ub = new UserBehavior(request);
			Cookie[] cookies = request.getCookies();
			String cpuid = null;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (COOKIE_UUID_CP.equals(cookie.getName())) {
						cpuid = cookie.getValue();
						break;
					}
				}
			}
			List<String> keyList = Lists.newArrayList();
			if (StringUtils.isNotBlank(cpuid) && StringUtils.isNotBlank(ub.getIp())) {
				keyList.add(getBehaviourKey(Restrict.RestrictIPCPUID, ub.getIp(), cpuid));
			}
			if (StringUtils.isNotBlank(cpuid)) {
				keyList.add(getBehaviourKey(Restrict.RestrictCPUID, cpuid));
			}
			if (StringUtils.isNotBlank(ub.getIp())) {
				keyList.add(getBehaviourKey(Restrict.RestrictIP, ub.getIp()));
			}
			Object[] objs = null;
			if (!keyList.isEmpty()) {
				objs = userCacheService.getMultiMemcachedData(MemcachedConstantBase.MEMCACHED_USER, keyList.toArray(new String[0]));
			}
			if (objs == null) {
				// 说明没有注册过
				objs = new Object[keyList.size()];
			}
			for (int i = 0; i < objs.length; i++) {
				Object obj = objs[i];
				String restrict = StringUtils.split(keyList.get(i), "|")[0];
				if (obj == null) {
					obj = new Behaviour(restrict, 0);
				}
				String key = keyList.get(i);
				Behaviour be = (Behaviour) obj;
				// memcached里设置时间最小为1000毫秒，如果小于1000，则永不过期
				long expired = DateUtil.getEnding(DateUtil.getCurrentDate(), Calendar.DATE).getTime() - DateUtil.getCurrentDate().getTime();
				if (expired < 1001) {
					expired = 1001;
				}
				Behaviour newbe = new Behaviour(be.getRestrict(), be.getTimes() + 1);
				userCacheService.setMemcachedData(MemcachedConstantBase.MEMCACHED_USER, key, newbe, new Date(expired));
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	
	}
	
	private static boolean containsIp(String ip, String list) {
	
		if (StringUtils.isBlank(list)) {
			return false;
		}
		return ip.matches(list);
	}
	
	public static String generateUID(HttpServletResponse response) {
	
		String str = null;
		try {
			str = DESUtil.encrypt(String.valueOf(System.currentTimeMillis()), getUidKey());
		} catch (Exception e) {
			log.error(e, e);
		}
		if (str != null && response != null) {
			Cookie cookie = new Cookie(COOKIE_UUID_CP, str);
			int age = SystemConfigs.getIntValue("REGISTER_UUID_AGE", 60 * 60 * 24 * 1);// 保留1天
			cookie.setMaxAge(age);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return str;
	}
	
	public static boolean isValidUid(String cpuid) {
	
		if (cpuid == null) {
			return false;
		}
		String str = null;
		try {
			str = DESUtil.decrypt(cpuid, getUidKey());
			new Date(Long.valueOf(str));
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(e, e);
			}
			return false;
		}
		return true;
	}
	
	private static String getUidKey() {
	
		return SystemConfigs.get(LoginConstant.REG_UID_KEY, LoginConstant.REG_UID_KEY_DEFAULT);
	}
	
	private int getBehaviousLimit(Behaviour be) {
	
		return Restrict.valueOf(be.getRestrict()).getTimes();
	}
	
	private String getBehaviourKey(Restrict restrict, String... key) {
	
		return restrict.name() + "|" + Joiner.on("|").join(key);
	}
	
	private static enum Restrict {
		RestrictCPUID(20), RestrictIP(150), RestrictIPCPUID(20);
		
		private int times;
		
		private Restrict(int times) {
		
			this.times = times;
		}
		
		public int getTimes() {
		
			return times;
		}
		
	}
	
	static class Behaviour extends BaseObject implements Serializable {
		
		private static final long serialVersionUID = 8663892959797160076L;
		private String restrict;
		private int times;
		
		public Behaviour(String restrict, int times) {
		
			super();
			this.restrict = restrict;
			this.times = times;
		}
		
		public String getRestrict() {
		
			return restrict;
		}
		
		public void setRestrict(String restrict) {
		
			this.restrict = restrict;
		}
		
		public int getTimes() {
		
			return times;
		}
		
		public void setTimes(int times) {
		
			this.times = times;
		}
	}
}
