package com.sogou.lottery.web.web.interceptor.game;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.game.GameMissing;
import com.sogou.lottery.base.vo.game.GameRule;
import com.sogou.lottery.base.vo.game.GameRuleFactory;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.constant.GameMissingConstant;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.cache.operator.service.GameMissingCacheService;
import com.sogou.lottery.cache.operator.service.GameNewsCacheService;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.util.LotteryUtil;

/**
 * 普通彩种拦截器
 */
public class CommonGameInterceptor implements HandlerInterceptor {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@Resource(name = "gameMissingCacheServiceImpl")
	private GameMissingCacheService gameMissingCacheService;
	
	@Resource(name = "gameNewsCacheServiceImpl")
	private GameNewsCacheService gameNewsCacheService;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		try {
			String uri = request.getRequestURI();
			String gameId = uri.split("\\.")[0].replaceAll("/", "");
			if (StringUtils.isBlank(gameId) || !GameType.isCommonGame(GameType.getGameType(gameId))) {
				request.getRequestDispatcher(WebConstant.FAIL_PAGE).forward(request, response);
				return false;
			}
			// 1.得到彩种详情
			Game game = gameCacheService.getGameById(gameId);
			if (game == null) {
				// TODO log fatal
				request.getRequestDispatcher(WebConstant.ERROR_PAGE).forward(request, response);
				return false;
			}
			request.setAttribute("game", game);
			// 2.得到当前期次
			List<Period> availablePeriodList = periodCacheService.getAvailablePeriod(gameId);
			if (availablePeriodList == null || availablePeriodList.isEmpty()) {
				// TODO log fatal
				request.getRequestDispatcher(WebConstant.ERROR_PAGE).forward(request, response);
				return false;
			}
			Period period = availablePeriodList.get(0);
			request.setAttribute("availablePeriod", period);
			// 3.得到开奖列表
			List<AwardWithPeriod> awardList = awardCacheService.getLatestNPeriodAward(gameId, SystemConfigs.getIntValue("common_game_award_list_count_" + gameId, 5));
			request.setAttribute("awardList", awardList);
			// 4.得到上一期的奖池
			Period prevPeriod = periodCacheService.getPrevPeriod(gameId, period.getPeriodNo());
			AwardWithPeriod prevAwardWithPeriod = awardCacheService.getAwardCache(gameId, prevPeriod.getPeriodNo());
			if (prevAwardWithPeriod != null && prevAwardWithPeriod.getAward() != null && prevAwardWithPeriod.getAward().getBonusPool() > 0L) {
				request.setAttribute("awardPool", LotteryUtil.getBonusPoolFormat(prevAwardWithPeriod.getAward().getBonusPool()));
			}
			// 5.当天是否开奖
			GameRule gameRule = GameRuleFactory.getGameRule(gameId);
			boolean todayOpenAward = gameRule == null ? false : gameRule.isTodayOpenAward();
			request.setAttribute("todayOpenAward", todayOpenAward ? 1 : 0);
			// 6.得到当前遗漏值
			GameMissing gameMissing = gameMissingCacheService.getGameMissing(gameId, period.getPeriodNo(), GameMissingConstant.MISSING_TYPE_PRESENT, null);
			if (gameMissing != null) {
				List<GameMissing> gameMissingList = new ArrayList<>();
				gameMissingList.add(gameMissing);
				String gameMissingStr = gameMissingCacheService.getGameMissingStr(gameMissingList);
				request.setAttribute("presentMissingStr", gameMissingStr);
			}
			// 7.得到当前时间戳
			request.setAttribute("systemTime", String.valueOf(System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher(WebConstant.ERROR_PAGE).forward(request, response);
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
	
	}
}
