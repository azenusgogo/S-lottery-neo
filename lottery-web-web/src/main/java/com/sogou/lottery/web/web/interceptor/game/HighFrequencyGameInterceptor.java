package com.sogou.lottery.web.web.interceptor.game;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.game.GameMissing;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.constant.GameMissingConstant;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.cache.operator.service.GameMissingCacheService;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.common.constant.WebConstant;

/**
 * 高频彩种拦截器
 */
public class HighFrequencyGameInterceptor implements HandlerInterceptor {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@Resource(name = "gameMissingCacheServiceImpl")
	private GameMissingCacheService gameMissingCacheService;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		try {
			String uri = request.getRequestURI();
			String gameId = uri.split("\\.")[0].replaceAll("/", "");
			if (!GameType.isHighFrequencyGame(GameType.getGameType(gameId))) {
				// TODO log info
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
			request.setAttribute("gameId", gameId);
			// 2.得到当前期次
			List<Period> availablePeriodList = periodCacheService.getAvailablePeriod(gameId);
			if (availablePeriodList == null || availablePeriodList.isEmpty()) {
				// TODO log fatal
				request.getRequestDispatcher(WebConstant.ERROR_PAGE).forward(request, response);
				return false;
			}
			Period period = availablePeriodList.get(0);
			request.setAttribute("availablePeriod", period);
			LOG.controller.info("[k3time]" + gameId + " " + period.getPeriodNo() + " " + DateUtil.formatDate(period.getEndTime(), DateUtil.Format.HYPHEN_YYYYMMDDHHMMSS) + " " + period.getEndTimeOffset() + " " + DateUtil.formatDate(period.getOffcialEndTime(), DateUtil.Format.HYPHEN_YYYYMMDDHHMMSS));
			// 3.得到开奖列表
			List<AwardWithPeriod> awardList = awardCacheService.getLatestNPeriodAward(gameId, SystemConfigs.getIntValue("hf_game_award_list_count_" + gameId, 6));
			request.setAttribute("awardList", awardList);
			// 3.1得到正在开奖的期次号
			Period prevPeriod = periodCacheService.getPrevPeriod(gameId, period.getPeriodNo());
			if (prevPeriod != null) {
				if (!awardList.isEmpty()) {
					if (!prevPeriod.getPeriodNo().equals(awardList.get(0).getPeriod().getPeriodNo())) {
						request.setAttribute("openingAwardPeriodNo", prevPeriod.getPeriodNo());
					}
				} else {
					request.setAttribute("openingAwardPeriodNo", prevPeriod.getPeriodNo());
				}
			}
			// 4.得到当前遗漏值
			List<GameMissing> presentGameMissingList = gameMissingCacheService.getGameMissingList(gameId, period.getPeriodNo(), GameMissingConstant.MISSING_TYPE_PRESENT);
			if (presentGameMissingList != null && !presentGameMissingList.isEmpty()) {
				request.setAttribute("presentMissingStr", gameMissingCacheService.getGameMissingStr(presentGameMissingList));
			}
			// 5.得到最大遗漏值
			List<GameMissing> maxGameMissingList = gameMissingCacheService.getGameMissingList(gameId, period.getPeriodNo(), GameMissingConstant.MISSING_TYPE_MAX);
			if (maxGameMissingList != null && !maxGameMissingList.isEmpty()) {
				request.setAttribute("maxMissingStr", gameMissingCacheService.getGameMissingStr(maxGameMissingList));
			}
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
