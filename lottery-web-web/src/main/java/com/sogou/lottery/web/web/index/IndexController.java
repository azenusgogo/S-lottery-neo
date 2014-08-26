package com.sogou.lottery.web.web.index;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.status.GameStatus;
import com.sogou.lottery.base.status.PeriodStatus;
import com.sogou.lottery.base.util.JsonUtil;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.award.RecentAwardRecordVo;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.game.GameRule;
import com.sogou.lottery.base.vo.game.GameRuleFactory;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.service.AwardAnnouncementService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.common.constant.SeoConstant;
import com.sogou.lottery.util.LotteryUtil;
import com.sogou.lottery.web.service.init.EnvironmentBean;
import com.sogou.lottery.web.web.BaseController;
import com.sogou.lottery.web.web.dto.index.AdImageDto;
import com.sogou.lottery.web.web.dto.index.CommonPeriodDto;
import com.sogou.lottery.web.web.dto.index.HighFrequencyPeriodDto;
import com.sogou.lottery.web.web.dto.index.IndexAnnouncementDto;
import com.sogou.lottery.web.web.dto.index.OpenAnnounceDto;
import com.sogou.lottery.web.web.dto.index.RecentAwardRecordDto;
import com.sogou.lottery.web.web.interceptor.CommonDataInterceptor;
import com.sogou.lottery.web.web.interceptor.CommonDataInterceptor.GameConfig;

@Controller
public class IndexController extends BaseController {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	@Resource(name = "awardAnnouncementServiceImpl")
	private AwardAnnouncementService awardAnnouncementService;
	
	private final static String FTL_INDEX = "index/index";
	
	private final static String MODEL_KEY_ADSLINK = "adsLinks";
	private final static String MODEL_KEY_ANNOUNCEMENT = "announcementMap";
	private final static String MODEL_KEY_PERIODS_COMMON = "commonPeriods";
	private final static String MODEL_KEY_QUICK_BET_TAG = "quickBetTags";
	private final static String MODEL_KEY_PERIODS_FREQUENCY = "highFrequencyPeriods";
	private final static String MODEL_KEY_AWARDS = "awardAnnouncements";
	private final static String MODEL_KEY_RECENT_AWARD = "recentAwardRecords";
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, ModelMap modelMap) {
	
		// 0.SEO
		setAttribute(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_index", "搜狗彩票网 - 网上购买彩票首选平台，中国福利彩票，中国体育彩票中奖福地"));
		setAttribute(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_index", "彩票，彩票网，网上购买彩票，彩票中奖"));
		setAttribute(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_index", "搜狗彩票是最值得信赖的专业彩票网站，彩民网上购买彩票首选平台。为广大彩民提供中国福利彩票，中国体育彩票，双色球，大乐透，快3，七星彩，七乐彩等热门彩种彩票购买服务，这里是您的彩票中奖福地！"));
		GameConfig gameConfig = (GameConfig) request.getAttribute(CommonDataInterceptor.MODEL_KEY_GAME_CONFIGS);
		// 1.得到彩种详情
		
		List<CommonPeriodDto> commonPeriods = Lists.newArrayList();
		List<HighFrequencyPeriodDto> highFrequencyPeriods = Lists.newArrayList();
		List<OpenAnnounceDto> announcements = Lists.newArrayList();
		List<RecentAwardRecordDto> recentList = Lists.newArrayList();
		
		try {
			List<Game> commonGames = gameCacheService.getCommonGameList();
			for (Game game : commonGames) {
				
				CommonPeriodDto periodDto = new CommonPeriodDto();
				// 2.得到当前期次
				List<Period> availablePeriodList = periodCacheService.getAvailablePeriod(game.getGameId());
				if (availablePeriodList == null || availablePeriodList.isEmpty()) {
					continue;
				}
				Period period = availablePeriodList.get(0);
				periodDto.setGame(game);
				periodDto.setPeriod(period);
				// 4.得到上期的奖池
				Period prevPeriod = periodCacheService.getPrevPeriod(game.getGameId(), period.getPeriodNo());
				AwardWithPeriod prevAwardWithPeriod = awardCacheService.getAwardCache(game.getGameId(), prevPeriod.getPeriodNo());
				if (prevAwardWithPeriod != null && prevAwardWithPeriod.getAward() != null && prevAwardWithPeriod.getAward().getBonusPool() > 0L) {
					periodDto.setBonusPool(LotteryUtil.getBonusPoolFormat(prevAwardWithPeriod.getAward().getBonusPool()));
				}
				// 5.当天是否开奖
				GameRule gameRule = GameRuleFactory.getGameRule(game.getGameId());
				boolean todayOpenAward = gameRule == null ? false : gameRule.isTodayOpenAward();
				periodDto.setTodayOpen(todayOpenAward);
				
				if (GameStatus.GMAE_STATUS_INVALID == game.getGameStatus() || period.getPeriodStatus() == PeriodStatus.PERIOD_STATUS_CANCELLED) {
					periodDto.setAvailable(false);
				} else {
					periodDto.setAvailable(true);
				}
				List<AwardWithPeriod> awardList = awardCacheService.getLatestNPeriodAward(game.getGameId(), 2);
				AwardWithPeriod awardWithPeriod = null;
				if (!(awardList == null || awardList.isEmpty())) {
					awardWithPeriod = awardList.get(0);
				}
				if (!(awardWithPeriod == null || awardWithPeriod.getAward() == null)) {
					OpenAnnounceDto awardDto = new OpenAnnounceDto();
					awardDto.setGame(game);
					awardDto.setAward(awardWithPeriod);
					awardDto.setTodayOpen(todayOpenAward);
					announcements.add(awardDto);
				}
				commonPeriods.add(periodDto);
			}
			
			// 1.高频彩得到彩种详情
			// List<GameDescDto> frequencyGameDesc = Lists.newArrayList();
			List<Game> highFrequencyGames = gameCacheService.getHighFrequencyGameList();
			for (Game game : highFrequencyGames) {
				
				// GameDescDto gameDto = new GameDescDto();
				// gameDto.setGame(game);
				// gameDto.setDesc(gameConfig.getGameDesc(game.getGameId()));
				// frequencyGameDesc.add(gameDto);
				
				HighFrequencyPeriodDto periodDto = new HighFrequencyPeriodDto();
				// 2.得到当前期次
				List<Period> availablePeriodList = periodCacheService.getAvailablePeriod(game.getGameId());
				if (availablePeriodList == null || availablePeriodList.isEmpty()) {
					continue;
				}
				Period period = availablePeriodList.get(0);
				periodDto.setGame(game);
				periodDto.setPeriod(period);
				periodDto.setHotPlayMethod(awardAnnouncementService.getHotPlayMethod(game.getGameId()));
				
				GameRule gr = GameRuleFactory.getGameRule(game.getGameId());
				int interval = (int) gr.getPeriodIntervals()[0] / (1000 * 60);
				periodDto.setInterval(interval);
				
				if (GameStatus.GMAE_STATUS_INVALID == game.getGameStatus() || period.getPeriodStatus() == PeriodStatus.PERIOD_STATUS_CANCELLED) {
					periodDto.setAvailable(false);
				} else {
					periodDto.setAvailable(true);
					
				}
				highFrequencyPeriods.add(periodDto);
			}
			
			// List<GameDescDto> traditionalGameDesc = Lists.newArrayList();
			// List<Game> traditionalGames =
			// gameCacheService.getTradFootballGameList();
			// for (Game game : traditionalGames) {
			// GameDescDto gameDto = new GameDescDto();
			// gameDto.setGame(game);
			// gameDto.setDesc(gameConfig.getGameDesc(game.getGameId()));
			// traditionalGameDesc.add(gameDto);
			// }
			
			List<RecentAwardRecordVo> recentAwards = buildRecentAward();
			for (RecentAwardRecordVo vo : recentAwards) {
				RecentAwardRecordDto dto = new RecentAwardRecordDto();
				GameRule gr = GameRuleFactory.getGameRule(vo.getGameId());
				dto.setGameCn(gr.getGameCn());
				dto.setAmount(vo.getBonus());
				dto.setNickName(StringUtils.isBlank(vo.getNickName()) ? trimName(vo.getUserId()) : trimName(vo.getNickName()));
				recentList.add(dto);
			}
			
			Collections.sort(commonPeriods, new GameComparator(gameConfig));
			Collections.sort(highFrequencyPeriods, new GameComparator(gameConfig));
			Collections.sort(announcements, new GameComparator(gameConfig));
			
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(e, e);
			}
		}
		
		modelMap.put(MODEL_KEY_ADSLINK, getAdsLinks());
		modelMap.put(MODEL_KEY_ANNOUNCEMENT, getIndexAnnouncement());
		modelMap.put(MODEL_KEY_QUICK_BET_TAG, getGameTag());
		modelMap.put(MODEL_KEY_PERIODS_COMMON, commonPeriods);
		modelMap.put(MODEL_KEY_PERIODS_FREQUENCY, highFrequencyPeriods);
		modelMap.put(MODEL_KEY_AWARDS, announcements);
		modelMap.put(MODEL_KEY_RECENT_AWARD, recentList);
		return FTL_INDEX;
	}
	
	private class GameComparator implements Comparator<Object> {
		
		private GameConfig gameConfig;
		
		GameComparator(GameConfig gameConfig) {
		
			this.gameConfig = gameConfig;
		}
		
		@Override
		public int compare(Object oo1, Object oo2) {
		
			try {
				if (oo1 instanceof CommonPeriodDto) {
					CommonPeriodDto o1 = (CommonPeriodDto) oo1;
					CommonPeriodDto o2 = (CommonPeriodDto) oo2;
					if (!o1.getAvailable() && o2.getAvailable()) {
						return -1;
					} else if (o1.getAvailable() && !o2.getAvailable()) {
						return 1;
					}
					return gameConfig.getGameIndex(o1.getGameId()) - gameConfig.getGameIndex(o2.getGameId());
				} else if (oo1 instanceof HighFrequencyPeriodDto) {
					HighFrequencyPeriodDto o1 = (HighFrequencyPeriodDto) oo1;
					HighFrequencyPeriodDto o2 = (HighFrequencyPeriodDto) oo2;
					if (!o1.getAvailable() && o2.getAvailable()) {
						return -1;
					} else if (o1.getAvailable() && !o2.getAvailable()) {
						return 1;
					}
					return gameConfig.getGameIndex(o1.getGameId()) - gameConfig.getGameIndex(o2.getGameId());
				} else if (oo1 instanceof OpenAnnounceDto) {
					OpenAnnounceDto o1 = (OpenAnnounceDto) oo1;
					OpenAnnounceDto o2 = (OpenAnnounceDto) oo2;
					return gameConfig.getGameIndex(o1.getGameId()) - gameConfig.getGameIndex(o2.getGameId());
				} else {
					throw new UnsupportedOperationException(oo1.toString());
				}
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug(e, e);
				}
			}
			return 0;
		}
	}
	
	/**
	 * 首页焦点图&首页下方广告条
	 * 
	 * @return
	 */
	private List<AdImageDto> getAdsLinks() {
	
		List<AdImageDto> list = Lists.newArrayList();
		try {
			String links = SystemConfigs.get("INDEX_ADS_LINK", "大乐透|/dlt/|" + EnvironmentBean.getCdnBaseUrl() + "img/pic/index_20140506.jpg|0 双色球|/ssq/|" + EnvironmentBean.getCdnBaseUrl() + "img/pic/index_20140425_1.jpg|0 胜负彩|/f14/|" + EnvironmentBean.getCdnBaseUrl() + "img/pic/index_20140425_2.jpg|0 大乐透|/dlt/|" + EnvironmentBean.getCdnBaseUrl() + "img/index/sy-dlt3.png|1");
			String[] temps = StringUtils.split(links);
			for (int i = 0; i < temps.length; i++) {
				String[] tmp = StringUtils.split(temps[i], "|");
				AdImageDto dto = new AdImageDto();
				dto.setTitle(tmp[0]);
				dto.setLink(tmp[1]);
				dto.setImage(tmp[2]);
				dto.setPosition(Integer.valueOf(tmp[3]));
				list.add(dto);
			}
		} catch (Exception e) {
			
		}
		return list;
	}
	
	private Map<String,List<IndexAnnouncementDto>> getIndexAnnouncement() {
	
		Map<String,List<IndexAnnouncementDto>> maps = new LinkedHashMap<>();
		try {
			// 超级大乐透玩法介绍及奖项规则 help/i-dlt-play.html
			String news = SystemConfigs.get("INDEX_GAME_ANNOUNCEMENT_NEW", "账号无法登录等问题说明|" + EnvironmentBean.getDomainUrl() + "notice/list-3.html 5月5日大乐透派奖1.5亿！3元可中2400万|" + EnvironmentBean.getDomainUrl() + "notice/list-2.html");
			String[] tmpNews = StringUtils.split(news);
			List<IndexAnnouncementDto> newList = Lists.newArrayList();
			for (String tmp : tmpNews) {
				IndexAnnouncementDto announ = new IndexAnnouncementDto();
				announ.setType("最新");
				announ.setTitle(StringUtils.split(tmp, "|")[0]);
				announ.setLink(StringUtils.split(tmp, "|")[1]);
				newList.add(announ);
			}
			String acts = SystemConfigs.get("INDEX_GAME_ANNOUNCEMENT_ACT", "搜狗彩票上线公告|" + EnvironmentBean.getDomainUrl() + "notice/list-1.html 搜狗彩票投注流程及投注方式介绍|" + EnvironmentBean.getDomainUrl() + "help/f-bet.html");
			String[] tmActs = StringUtils.split(acts);
			List<IndexAnnouncementDto> actList = Lists.newArrayList();
			for (String tmp : tmActs) {
				IndexAnnouncementDto announ = new IndexAnnouncementDto();
				announ.setType("公告");
				announ.setTitle(StringUtils.split(tmp, "|")[0]);
				announ.setLink(StringUtils.split(tmp, "|")[1]);
				actList.add(announ);
			}
			maps.put("最新", newList);
			maps.put("公告", actList);
		} catch (Exception e) {
			
		}
		return maps;
	}
	
	protected List<RecentAwardRecordVo> buildRecentAward() {
	
		List<RecentAwardRecordVo> recentList = Lists.newArrayList();
		try {
			recentList = awardCacheService.getRecentAwardRecords();
			String str = SystemConfigs.get("INDEX_RECENT_AWARD", "k3jl 精致*** 310,ssq 真情*** 305,dlt da*** 300,ssq bu*** 275,k3jl 意图*** 220,dlt 战术*** 190,ssq la*** 160,k3gx to*** 150,k3jl 影子*** 150,k3jl 木头*** 120,k3jl hb*** 108,k3gx 乖乖*** 96,k3js zh*** 80,k3jl 稳定*** 80,qxc 天下*** 64,qxc 交杯*** 55,k3gx 游戏*** 48,k3jl ea*** 44,qxc 往事*** 32,k3jl xu*** 24,k3js lo*** 12,k3jl s7*** 8");
			String[] tab = StringUtils.split(str, ",");
			for (String t : tab) {
				String[] tmp = StringUtils.split(t);
				RecentAwardRecordVo vo = new RecentAwardRecordVo();
				vo.setGameId(tmp[0]);
				vo.setNickName(tmp[1]);
				vo.setBonus(Long.valueOf(tmp[2]) * 100L);
				recentList.add(vo);
			}
			Collections.sort(recentList, new Comparator<RecentAwardRecordVo>() {
				
				@Override
				public int compare(RecentAwardRecordVo o1, RecentAwardRecordVo o2) {
				
					return (int) (o2.getBonus() - o1.getBonus());
				}
			});
			int size = recentList.size();
			if (size > 0 && size >= 24) {
				recentList = recentList.subList(0, 24);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return recentList;
	}
	
	private static Map<String,String> getGameTag() {
	
		Map<String,String> map = Maps.newHashMap();
		try {
			String str = SystemConfigs.get("INDEX_GAME_TAG", "{\"dlt\":\"加奖\"}");
			map = JsonUtil.fromJson(str, new TypeToken<Map<String,String>>() {
			}.getType());
		} catch (Exception e) {
			
		}
		return map;
	}
	
	protected static String trimName(String orgStr) {
	
		if (StringUtils.isBlank(orgStr) || orgStr.length() < 2) {
			return "搜狗彩民";
		}
		int lengthToGet = orgStr.length() > 2 ? 2 : 1;
		return orgStr.substring(0, lengthToGet) + "***";
	}
}
