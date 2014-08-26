package com.sogou.lottery.web.web.index;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.base.vo.sports.GameMatchWithMatch;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.cache.operator.service.TradFootBallMatchCacheService;
import com.sogou.lottery.common.constant.WebConstant;

@Controller
@RequestMapping("/iframe/index")
public class IndexTradFootballController {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	@Resource(name = "tradFootBallMatchCacheServiceImpl")
	private TradFootBallMatchCacheService tradFootBallMatchCacheService;
	
	private final static String FTL_INDEX_PREFIX = "iframe/index/football/";
	
	@RequestMapping("/football/{gameId}")
	public String index(@PathVariable("gameId")
	String gameId, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
	
		// 1.得到彩种详情
		Game game = gameCacheService.getGameById(gameId);
		if (game == null || !GameType.isTradFootballGame(GameType.getGameType(gameId))) {
			return WebConstant.FAIL_FTL;
		}
		request.setAttribute("game", game);
		// 2.得到当前期次
		List<Period> availablePeriodList = periodCacheService.getAvailablePeriod(game.getGameId());
		if (availablePeriodList == null || availablePeriodList.isEmpty()) {
			// request.getRequestDispatcher(WebConstant.ERROR_PAGE).forward(request,
			// response);
			return WebConstant.FAIL_FTL;
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
		// List<AwardWithPeriod> awardList =
		// awardCacheService.getLatestNPeriodAward(gameId,
		// SystemConfigs.getIntValue("tradfootball_game_award_list_count_" +
		// gameId, 5));
		// request.setAttribute("awardList", awardList);
		// 5.对阵信息
		List<GameMatchWithMatch> gameMatchList = tradFootBallMatchCacheService.getGameMatchList(game.getGameId(), periodNo);
		if (gameMatchList != null && !gameMatchList.isEmpty()) {
			request.setAttribute("gameMatchList", gameMatchList);
		} else {
			// TODO log fatal
		}
		return FTL_INDEX_PREFIX + game.getGameId();
	}
}
