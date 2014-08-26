package com.sogou.lottery.web.web.game;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.common.constant.SeoConstant;
import com.sogou.lottery.web.web.BaseController;

@Controller
public class HighFrequencyGameController extends BaseController {
	
	@RequestMapping("/k3gx")
	public String ssq() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_k3gx", "【新快3】福彩新快3网上投注_新快三在线机选号码– 搜狗彩票"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_k3gx", "新快3，新快三，福彩新快3"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_k3gx", "搜狗彩票提供福彩新快3网上投注，新快三在线机选号码，新快3复式投注等彩票购买服务。同时汇集新快3玩法介绍和新快3中奖规则，网上购买新快3，首选搜狗彩票！"));
		
		return "game/k3gx";
	}
	
	@RequestMapping("/k3jl")
	public String dlt() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_k3jl", "【快3】福彩快3网上投注_快三在线机选号码– 搜狗彩票"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_k3jl", "快3，快三，福彩快3"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_k3jl", "搜狗彩票提供福彩快3网上投注，快三在线机选号码，快3复式投注等彩票购买服务。同时汇集快3玩法介绍和快3中奖规则，网上购买快3，首选搜狗彩票！"));
		
		return "game/k3jl";
	}
	
	@RequestMapping("/k3js")
	public String qxc() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_k3js", "【老快3】福彩老快3网上投注_老快三在线机选号码– 搜狗彩票"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_k3js", "老快3，老快三，福彩老快3"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_k3js", "搜狗彩票提供福老彩快3网上投注，老快三在线机选号码，老快3复式投注等彩票购买服务。同时汇集老快3玩法介绍和老快3中奖规则，网上购买老快3，首选搜狗彩票！"));
		
		return "game/k3js";
	}
	
}
