package com.sogou.lottery.web.ajax.index;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.status.GameStatus;
import com.sogou.lottery.base.status.PeriodStatus;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.game.GameRule;
import com.sogou.lottery.base.vo.game.GameRuleFactory;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.service.AwardAnnouncementService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.common.constant.AjaxConstant;
import com.sogou.lottery.util.LotteryUtil;
import com.sogou.lottery.web.ajax.dto.ResultObjectDto;
import com.sogou.lottery.web.web.BaseController;
import com.sogou.lottery.web.web.dto.index.CommonPeriodDto;
import com.sogou.lottery.web.web.dto.index.HighFrequencyPeriodDto;

@Controller
@RequestMapping("/ajax/index")
@SuppressWarnings("rawtypes")
public class IndexAjaxController extends BaseController {
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	@Resource(name = "awardAnnouncementServiceImpl")
	private AwardAnnouncementService awardAnnouncementService;
	
	@RequestMapping("/{gameId}/refresh")
	@ResponseBody
	public ResultObjectDto getPeriod(@PathVariable("gameId")
	String gameId) {
	
		try {
			Game game = gameCacheService.getGameById(gameId);
			if (game == null) {
				return new ResultObjectDto<>(AjaxConstant.PARAM_ERROR_CODE, AjaxConstant.PARAM_ERROR_DESC);
			}
			if (GameType.isCommonGame(game.getGameType())) {
				// 2.得到当前期次
				List<Period> availablePeriodList = periodCacheService.getAvailablePeriod(game.getGameId());
				if (availablePeriodList == null || availablePeriodList.isEmpty()) {
					return new ResultObjectDto<>(AjaxConstant.PARAM_ERROR_CODE, AjaxConstant.PARAM_ERROR_DESC);
				}
				Period period = availablePeriodList.get(0);
				CommonPeriodDto periodDto = new CommonPeriodDto();
				periodDto.setGame(game);
				periodDto.setPeriod(period);
				// 4.得到当前期的奖池
				AwardWithPeriod awardWithPeriod = awardCacheService.getAwardCache(game.getGameId(), period.getPeriodNo());
				if (awardWithPeriod != null && awardWithPeriod.getAward() != null && awardWithPeriod.getAward().getBonusPool() > 0L) {
					periodDto.setBonusPool(LotteryUtil.getBonusPoolFormat(awardWithPeriod.getAward().getBonusPool()));
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
				return new ResultObjectDto<>(periodDto);
			} else if (GameType.isHighFrequencyGame(game.getGameType())) {
				
				HighFrequencyPeriodDto periodDto = new HighFrequencyPeriodDto();
				// 2.得到当前期次
				List<Period> availablePeriodList = periodCacheService.getAvailablePeriod(game.getGameId());
				if (availablePeriodList == null || availablePeriodList.isEmpty()) {
					return new ResultObjectDto<>(AjaxConstant.PARAM_ERROR_CODE, AjaxConstant.PARAM_ERROR_DESC);
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
				return new ResultObjectDto<>(periodDto);
			} else {
				return new ResultObjectDto<>(AjaxConstant.PARAM_ERROR_CODE, AjaxConstant.PARAM_ERROR_DESC);
			}
		} catch (Exception e) {
			log.error(e, e);
			return new ResultObjectDto<>(AjaxConstant.PARAM_ERROR_CODE, AjaxConstant.PARAM_ERROR_DESC);
		}
	}
}
