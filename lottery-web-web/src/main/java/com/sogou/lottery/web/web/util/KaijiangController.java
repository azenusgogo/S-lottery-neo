package com.sogou.lottery.web.web.util;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.operator.dto.OpenAwardDto;
import com.sogou.lottery.cache.operator.service.AwardAnnouncementService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.common.constant.SeoConstant;
import com.sogou.lottery.web.web.BaseController;

@Controller
public class KaijiangController extends BaseController {
	
	@Resource(name = "awardAnnouncementServiceImpl")
	private AwardAnnouncementService awardAnnouncementService;
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	@RequestMapping("/kaijiang")
	public String kaijiang() {
	
		Date now = DateUtil.getCurrentDate();
		String today = DateUtil.formatDate(now, DateUtil.Format.HYPHEN_YYYYMMDD) + " (" + DateUtil.getWeekOfDate2(now) + ")";
		setAttribute("today", today);
		List<Game> todayOpenAwardGameList = awardCacheService.getTodayOpenAwardGameList(DateUtil.getCurrentDate());
		setAttribute("todayOpenAwardGameList", todayOpenAwardGameList);
		List<OpenAwardDto> commonAwardDtoList = awardAnnouncementService.getCommonOpenAwardDtoList();
		List<OpenAwardDto> hfAwardDtoList = awardAnnouncementService.getHighFrequencyOpenAwardDtoList();
		List<OpenAwardDto> sportAwardDtoList = awardAnnouncementService.getSportOpenAwardDtoList();
		setAttribute("commonOpenAwardDtoList", commonAwardDtoList);
		setAttribute("hfOpenAwardDtoList", hfAwardDtoList);
		setAttribute("sportOpenAwardDtoList", sportAwardDtoList);
		
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_kaijiang", "彩票开奖_彩票开奖结果_彩票开奖查询– 搜狗彩票"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_kaijiang", "彩票开奖，彩票开奖结果，彩票开奖查询"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_kaijiang", "搜狗彩票开奖中心提供福彩、体彩、足彩和高频彩票的开奖结果查询。包括双色球、超级大乐透、快乐8、3D、排列三、排列五、七星彩、七乐彩等彩票，查询最新彩票开奖结果上搜狗彩票"));
		return "util/kaijiang";
	}
}
