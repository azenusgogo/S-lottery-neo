package com.sogou.lottery.web.web.util;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.constant.Words;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.common.constant.SeoConstant;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.web.web.BaseController;

@Controller
public class CalculatorController extends BaseController {
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@RequestMapping("/calculator/{gameId}")
	public String kaijiang(@PathVariable("gameId")
	String gameId) {
	
		if (StringUtils.isBlank(gameId) || (!gameId.equals(Words.SSQ) && !gameId.equals(Words.DLT))) {
			return WebConstant.FAIL_FTL;
		}
		List<AwardWithPeriod> awardWithPeriodList = awardCacheService.getLatestNPeriodAward(gameId, SystemConfigs.getIntValue("calculator_period_count_" + gameId, 50));
		setAttribute("awardWithPeriodList", awardWithPeriodList);
		
		Game game = gameCacheService.getGameById(gameId);
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_calculator_" + gameId, game.getGameCn() + "奖金计算器 – 搜狗彩票"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_calculator_" + gameId, ""));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_calculator_" + gameId, ""));
		
		return "util/calculator/" + gameId;
	}
}
