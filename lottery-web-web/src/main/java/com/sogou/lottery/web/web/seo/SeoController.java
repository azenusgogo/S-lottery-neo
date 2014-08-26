package com.sogou.lottery.web.web.seo;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.web.web.BaseController;

@Controller
public class SeoController extends BaseController {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	@RequestMapping("/seo-{redirect}")
	public String index(HttpServletRequest request, HttpServletResponse response, @PathVariable("redirect")
	String redirect) throws ServletException, IOException {
	
		if (StringUtils.isBlank(redirect)) {
			request.getRequestDispatcher(WebConstant.INDEX_URI).forward(request, response);
			return null;
		} else {
			return "redirect:/" + redirect + "/";
		}
	}
}
