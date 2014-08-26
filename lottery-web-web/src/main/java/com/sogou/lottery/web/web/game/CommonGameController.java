package com.sogou.lottery.web.web.game;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.common.constant.SeoConstant;
import com.sogou.lottery.web.web.BaseController;

@Controller
public class CommonGameController extends BaseController {
	
	@RequestMapping("/ssq")
	public String ssq() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_ssq", "【双色球】福利彩票双色球网上投注_双色球在线机选号码 – 搜狗彩票"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_ssq", "双色球，福利彩票双色球，双色球机选号码，双色球网上投注"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_ssq", "搜狗彩票提供福利彩票双色球网上投注，双色球在线机选号码，双色球复式投注等彩票购买服务。同时汇集双色球玩法介绍和双色球中奖规则，网上购买双色球，首选搜狗彩票！"));
		
		return "game/ssq";
	}
	
	@RequestMapping("/dlt")
	public String dlt() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_dlt", "【超级大乐透】体彩大乐透网上投注_大乐透在线机选号码 - 搜狗彩票"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_dlt", "大乐透，超级大乐透，体彩大乐透"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_dlt", "搜狗彩票提供体彩大乐透网上投注，超级大乐透在线机选号码，大乐透复式投注等彩票购买服务。同时汇集大乐透玩法介绍和大乐透中奖规则，网上购买大乐透，首选搜狗彩票！"));
		
		return "game/dlt";
	}
	
	@RequestMapping("/qxc")
	public String qxc() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_qxc", "【七星彩】体彩七星彩网上投注_七星彩机选号码 搜狗彩票"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_qxc", "七星彩，体彩七星彩，七星彩投注"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_qxc", "搜狗彩票提供体彩七星彩网上投注，七星彩在线机选号码，七星彩复式投注等彩票购买服务。同时汇集七星彩玩法介绍和七星彩中奖规则，网上购买七星彩，首选搜狗彩票！"));
		
		return "game/qxc";
	}
	
	@RequestMapping("/qlc")
	public String qlc() {
	
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_qlc", "【七乐彩】福彩七乐彩网上投注_七乐彩机选号码 - 搜狗彩票"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_qlc", "七乐彩，福彩七乐彩，七乐彩投注"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_qlc", "搜狗彩票提供福彩七乐彩网上投注，七乐彩在线机选号码，七乐彩复式投注等彩票购买服务。同时汇集七乐彩玩法介绍和七乐彩中奖规则，网上购买七乐彩，首选搜狗彩票！"));
		
		return "game/qlc";
	}
}
