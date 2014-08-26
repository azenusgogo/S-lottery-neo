package com.sogou.lottery.web.web.game;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.common.constant.SeoConstant;
import com.sogou.lottery.web.web.BaseController;

@Controller
public class TradFootballGameController extends BaseController {
	
	@RequestMapping("/f14")
	public String ssq() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_f14", "【足彩胜负彩】足球胜负彩_胜负彩14场网上投注– 搜狗彩票"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_f14", "足彩，胜负彩，足球胜负彩，胜负彩14场"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_f14", "搜狗彩票提供足球胜负彩网上投注，胜负彩14场网上投注等彩票购买服务。同时汇集足彩胜负彩玩法介绍和足彩胜负彩中奖规则，网上购买胜负彩，首选搜狗彩票！"));
		
		return "game/f14";
	}
	
	@RequestMapping("/f9")
	public String dlt() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_f9", "【任选九场】足彩任选九场_任选9场网上投注– 搜狗彩票"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_f9", "任选九场，任选9场"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_f9", "搜狗彩票提供足彩任选九场网上投注，任选9场网上投注等彩票购买服务。同时汇集任选九场玩法介绍和任选九场中奖规则，网上购买任选九场，首选搜狗彩票！"));
		
		return "game/f9";
	}
	
}
