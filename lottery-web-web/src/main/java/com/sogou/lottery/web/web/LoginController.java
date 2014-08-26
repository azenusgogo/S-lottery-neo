package com.sogou.lottery.web.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.web.service.init.EnvironmentBean;

@Controller
public class LoginController extends BaseController {
	
	public final static String CTL_LOGIN = "/login";
	private final static String FTL_LOGIN = "login/login";
	
	@RequestMapping(CTL_LOGIN)
	public String login() {
	
		Object retUrl = WebConstant.requestTL.get().getAttribute(WebConstant.REDIRECT_URL);
		// 如果为空，则重定向到首页
		if (retUrl == null) {
			setAttribute(WebConstant.REDIRECT_URL, EnvironmentBean.getDomainUrl());
		}
		return FTL_LOGIN;
	}
	
}
