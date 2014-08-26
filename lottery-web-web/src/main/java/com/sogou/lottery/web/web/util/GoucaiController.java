package com.sogou.lottery.web.web.util;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.constant.Words;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.util.JsonUtil;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.common.constant.SeoConstant;
import com.sogou.lottery.web.web.BaseController;

@Controller
public class GoucaiController extends BaseController {
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	@RequestMapping("/goucai")
	public String kaijiang() {
	
		List<AwardWithPeriod> ssqAwardWithPeriodList = awardCacheService.getLatestNPeriodAward(Words.SSQ, 1);
		if (!ssqAwardWithPeriodList.isEmpty()) {
			setAttribute("ssqAward", ssqAwardWithPeriodList.get(0));
		}
		List<AwardWithPeriod> dltAwardWithPeriodList = awardCacheService.getLatestNPeriodAward(Words.DLT, 1);
		if (!dltAwardWithPeriodList.isEmpty()) {
			setAttribute("dltAward", dltAwardWithPeriodList.get(0));
		}
		List<AwardWithPeriod> qxcAwardWithPeriodList = awardCacheService.getLatestNPeriodAward(Words.QXC, 1);
		if (!qxcAwardWithPeriodList.isEmpty()) {
			setAttribute("qxcAward", qxcAwardWithPeriodList.get(0));
		}
		setAttribute("gameTags", getGameTag());
		
		List<Game> gameList = awardCacheService.getTodayOpenAwardGameList(DateUtil.getCurrentDate());
		StringBuffer todayOpenAwardGameId = new StringBuffer();
		for (Game game : gameList) {
			todayOpenAwardGameId.append(game.getGameId()).append(",");
		}
		setAttribute("todayOpenAwardGameId", todayOpenAwardGameId.toString());
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_goucai", "网购彩票_彩票代购_彩票投注_网上购彩 – 搜狗彩票"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_goucai", "网购彩票，彩票代购，彩票投注，网上购彩	"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_goucai", "搜狗彩票购彩大厅为您提供网购彩票、彩票代购、网上购彩、福利彩票网上投注等服务。搜狗彩票网是一个安全、稳定、有信誉保证的网上彩票投注站。"));
		
		return "util/goucai";
	}
	
	private static Map<String,String> getGameTag() {
	
		Map<String,String> map = Maps.newHashMap();
		String str = SystemConfigs.get("KAIJIANG_GAME_TAG", "{\"dlt\":\"加奖\"}");
		try {
			map = JsonUtil.fromJson(str, new TypeToken<Map<String,String>>() {
			}.getType());
		} catch (Exception e) {
			
		}
		return map;
	}
	
}
