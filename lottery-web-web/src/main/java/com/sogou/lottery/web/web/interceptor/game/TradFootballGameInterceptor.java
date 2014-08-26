package com.sogou.lottery.web.web.interceptor.game;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sogou.lottery.base.vo.sports.MatchPointRankItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.base.vo.sports.GameMatchWithMatch;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.cache.operator.service.TradFootBallMatchCacheService;
import com.sogou.lottery.common.constant.WebConstant;

/**
 * 普通彩种拦截器
 */
public class TradFootballGameInterceptor implements HandlerInterceptor {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@Resource(name = "tradFootBallMatchCacheServiceImpl")
	private TradFootBallMatchCacheService tradFootBallMatchCacheService;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		try {
			String uri = request.getRequestURI();
			String gameId = uri.split("\\.")[0].replaceAll("/", "");
			if (StringUtils.isBlank(gameId) || !GameType.isTradFootballGame(GameType.getGameType(gameId))) {
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
			// 3.得到传入的期次号
			String periodNo = request.getParameter("periodNo");
			Period period = null;
			boolean isPeriodNoValid = false;
			for (Period tempPeriod : availablePeriodList) {
				if (tempPeriod.getPeriodNo().equals(periodNo)) {
					isPeriodNoValid = true;
					period = tempPeriod;
					break;
				}
			}
			if (!isPeriodNoValid) {
				period = availablePeriodList.get(0);
			}
			periodNo = period.getPeriodNo();
			request.setAttribute("periodNo", periodNo);
			request.setAttribute("availablePeriodList", availablePeriodList);
			// 4.得到开奖列表
			List<AwardWithPeriod> awardList = awardCacheService.getLatestNPeriodAward(gameId, SystemConfigs.getIntValue("tradfootball_game_award_list_count_" + gameId, 5));
			request.setAttribute("awardList", awardList);
			// 5.对阵信息
			List<GameMatchWithMatch> gameMatchList = tradFootBallMatchCacheService.getGameMatchList(gameId, periodNo);
			if (gameMatchList != null && !gameMatchList.isEmpty()) {
				request.setAttribute("gameMatchList", gameMatchList);
			} else {
				// TODO log fatal
				
			}
            List<List<MatchPointRankItem>> matchPointRankList = tradFootBallMatchCacheService.getMatchPointRankList();
            request.setAttribute("matchPointRank",matchPointRankList);
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
