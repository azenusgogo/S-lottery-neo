package com.sogou.lottery.web.ajax.stat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.game.GameMissing;
import com.sogou.lottery.base.vo.game.GameRuleFactory;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.constant.GameMissingConstant;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.cache.operator.service.GameMissingCacheService;
import com.sogou.lottery.cache.operator.service.GameNewsCacheService;
import com.sogou.lottery.common.constant.AjaxConstant;
import com.sogou.lottery.web.ajax.dto.ResultObjectDto;
import com.sogou.lottery.web.web.BaseController;

/**
 * 普通彩种ajax
 * 
 * @author lvzhenyu
 */
@Controller
@RequestMapping("/ajax/gameMissing")
public class GameMissingAjaxController extends BaseController {
	
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
	
	/**
	 * 根据gameId获得当前期次最大的
	 * 
	 * @return
	 */
	@RequestMapping("/get/{gameId}/{periodNo}/{missingType}")
	@ResponseBody
	public ResultObjectDto<String> getAll(@PathVariable("gameId")
	String gameId, @PathVariable("periodNo")
	String periodNo, @PathVariable("missingType")
	int missingType) {
	
		// 0.校验参数
		ResultObjectDto<String> resultDto = new ResultObjectDto<>(AjaxConstant.PARAM_ERROR_CODE, AjaxConstant.PARAM_ERROR_DESC);
		// 0.1校验gameId
		if (StringUtils.isBlank(gameId) || GameType.getGameType(gameId) == GameType.GAME_NULL) {
			// TODO log info
			return resultDto;
		}
		// 只有普通彩种和高频有遗漏号
		if (!(GameType.isCommonGame(GameType.getGameType(gameId)) || GameType.isHighFrequencyGame(GameType.getGameType(gameId)))) {
			// TODO log info
			return resultDto;
		}
		// 0.2校验periodNo
		if (StringUtils.isBlank(periodNo)) {
			// TODO log info
			return resultDto;
		}
		// 0.3校验missingType
		if (missingType < GameMissingConstant.MISSING_TYPE_ALL || missingType > GameMissingConstant.MISSING_TYPE_78_PHASE) {
			// TODO log info
			return resultDto;
		}
		// 1.得到彩种详情
		Game game = gameCacheService.getGameById(gameId);
		if (game == null) {
			// TODO log info
			return resultDto;
		}
		if (missingType == GameMissingConstant.MISSING_TYPE_ALL && !GameType.isHighFrequencyGame(game.getGameType())) {
			// TODO log info
			return resultDto;
		}
		// 2.得到期次信息
		// 2.1期次格式不正确，返回
		if (!GameRuleFactory.getGameRule(gameId).isValidPeriod(periodNo)) {
			// TODO log info
			return resultDto;
		}
		// 2.2得到当前期次
		List<Period> availablePeriodList = periodCacheService.getAvailablePeriod(gameId);
		if (availablePeriodList.isEmpty()) {
			// TODO log info
			return resultDto;
		}
		// 2.3如果传入的期次是在当前期之后，则校验不通过
		Period availablePeriod = availablePeriodList.get(0);
		if (availablePeriod.getPeriodNo().compareTo(periodNo) < 0) {
			// TODO log info
			return resultDto;
		}
		resultDto.setRetcode(AjaxConstant.SUCCESS_CODE);
		resultDto.setRetdesc(AjaxConstant.SUCCESS_DESC);
		// 3.得到遗漏值
		StringBuilder missingStr = new StringBuilder("");
		// 3.1普通彩种，只有当前遗漏
		if (GameType.isCommonGame(game.getGameType())) {
			GameMissing gameMissing = gameMissingCacheService.getGameMissing(gameId, periodNo, GameMissingConstant.MISSING_TYPE_PRESENT, null);
			if (gameMissing != null) {
				List<GameMissing> gameMissingList = new ArrayList<>();
				gameMissingList.add(gameMissing);
				String gameMissingStr = gameMissingCacheService.getGameMissingStr(gameMissingList);
				missingStr.append(GameMissingConstant.MISSING_TYPE_PRESENT).append("=").append(gameMissingStr);
			}
		} else {
			switch (missingType) {
				case GameMissingConstant.MISSING_TYPE_ALL:
				case GameMissingConstant.MISSING_TYPE_PRESENT:
					List<GameMissing> presentGameMissingList = gameMissingCacheService.getGameMissingList(gameId, periodNo, GameMissingConstant.MISSING_TYPE_PRESENT);
					if (presentGameMissingList != null && !presentGameMissingList.isEmpty()) {
						missingStr.append(GameMissingConstant.MISSING_TYPE_PRESENT).append("=").append(gameMissingCacheService.getGameMissingStr(presentGameMissingList));
					}
					if (missingType != GameMissingConstant.MISSING_TYPE_ALL) {
						break;
					} else {
						missingStr.append(";");
					}
				case GameMissingConstant.MISSING_TYPE_MAX:
					List<GameMissing> maxGameMissingList = gameMissingCacheService.getGameMissingList(gameId, periodNo, GameMissingConstant.MISSING_TYPE_MAX);
					if (maxGameMissingList != null && !maxGameMissingList.isEmpty()) {
						missingStr.append(GameMissingConstant.MISSING_TYPE_MAX).append("=").append(gameMissingCacheService.getGameMissingStr(maxGameMissingList));
					}
					if (missingType != GameMissingConstant.MISSING_TYPE_ALL) {
						break;
					} else {
						missingStr.append(";");
					}
			}
		}
		resultDto.setObject(missingStr.toString());
		return resultDto;
	}
}
