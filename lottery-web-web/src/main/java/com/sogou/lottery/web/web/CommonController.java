package com.sogou.lottery.web.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.web.web.util.Authentic;

@Controller
public class CommonController extends BaseController {
	
	@RequestMapping(value = "/error")
	public String error() {
	
		return WebConstant.ERROR_FTL;
	}
	
	@RequestMapping(value = "/check_login")
	@ResponseBody
	public int checkLogin(HttpServletRequest request) {
	
		String userName = Authentic.getUserId(request);
		if (StringUtils.isBlank(userName)) {
			// 没登陆
			return WebConstant.LOGIN_NO;
		}
		return WebConstant.LOGIN_YES;
	}
}
