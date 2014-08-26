package com.sogou.lottery.web.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sogou.lottery.base.util.LogUtil;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.util.LotteryUtil;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.service.UserService;
import com.sogou.lottery.web.web.util.Authentic;

/**
 * 所有Controller都需要的操作
 */
@Controller
public class BaseController {
	
	protected final static String PAGE_ERROR = "error";
	
	protected static Logger log = LOG.controller;
	
	@Autowired
	protected UserService userService;
	
	@ExceptionHandler(Exception.class)
	public String handleException(Exception e) {
	
		log.error(LogUtil.format("page", "base", "error"), e);
		return WebConstant.ERROR_FTL;
	}
	
	protected UserDto getUser() {
	
		UserDto userDto = new UserDto();
		userDto.setUserId(this.getUserId());
		userDto.setUserIp(this.getClientIP());
		return userDto;
	}
	
	protected void printError(BindingResult errors) {
	
		if (log.isDebugEnabled()) {
			for (ObjectError error : errors.getAllErrors()) {
				log.debug(error);
			}
		}
	}
	
	public boolean verifyPara(String... params) {
	
		for (String param : params) {
			if (StringUtils.isBlank(param)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 向request中设置属性
	 * 
	 * @return
	 */
	protected void setAttribute(String attrName, Object attrValue) {
	
		HttpServletRequest request = WebConstant.requestTL.get();
		request.setAttribute(attrName, attrValue);
	}
	
	/**
	 * 向request中设置属性
	 * 
	 * @return
	 */
	protected String getParameter(String parameterName) {
	
		HttpServletRequest request = WebConstant.requestTL.get();
		return LotteryUtil.trimString(request.getParameter(parameterName));
	}
	
	/**
	 * 向request中设置属性
	 * 
	 * @return
	 */
	protected String getClientIP() {
	
		HttpServletRequest request = WebConstant.requestTL.get();
		return LotteryUtil.getLastIp(request);
	}
	
	protected String getUserId() {
	
		HttpServletRequest request = WebConstant.requestTL.get();
		return Authentic.getUserId(request);
	}
	
	protected String getFirstError(Errors errors) {
	
		if (!errors.hasErrors()) {
			return null;
		}
		return errors.getFieldErrors().get(0).getDefaultMessage();
	}
}
