package com.sogou.lottery.web.ajax.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.sogou.lottery.common.constant.AjaxConstant;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.util.LotteryUtil;
import com.sogou.lottery.web.ajax.BaseAjaxController;
import com.sogou.lottery.web.ajax.dto.AwardWithPeriodDto;
import com.sogou.lottery.web.ajax.dto.GameDto;
import com.sogou.lottery.web.ajax.dto.GameMatchWithMatchDto;
import com.sogou.lottery.web.ajax.dto.PeriodDto;
import com.sogou.lottery.web.ajax.dto.ResultObjectDto;
import com.sogou.lottery.web.web.util.Authentic;

/**
 * 普通彩种ajax
 * 
 * @author lvzhenyu
 */
@Controller
@RequestMapping("/ajax/game/tradfootball")
public class TradFootballGameAjaxController extends BaseAjaxController {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@Resource(name = "tradFootBallMatchCacheServiceImpl")
	private TradFootBallMatchCacheService tradFootBallMatchCacheService;
	
	/**
	 * 根据gameId获得当前期次最大的
	 * 
	 * @return
	 */
	@RequestMapping("/{gameId}/getAll")
	@ResponseBody
	public ResultObjectDto<Map<String,Object>> getAll(@PathVariable("gameId")
	String gameId) {
	
		ResultObjectDto<Map<String,Object>> resultDto = new ResultObjectDto<>(AjaxConstant.SUCCESS_CODE, AjaxConstant.SUCCESS_DESC);
		Map<String,Object> data = new HashMap<String,Object>();
		if (StringUtils.isBlank(gameId) || !GameType.isTradFootballGame(GameType.getGameType(gameId))) {
			resultDto.setRetcode(AjaxConstant.PARAM_ERROR_CODE);
			resultDto.setRetdesc(AjaxConstant.PARAM_ERROR_DESC);
			return resultDto;
		}
		// 1.得到彩种详情
		Game game = gameCacheService.getGameById(gameId);
		if (game == null) {
			return resultDto;
		}
		data.put("game", new GameDto(game));
		// 2.得到当前期次
		List<Period> availablePeriodList = periodCacheService.getAvailablePeriod(gameId);
		if (availablePeriodList == null || availablePeriodList.isEmpty()) {
			resultDto.setRetcode(AjaxConstant.SYSTEM_ERROR_CODE);
			resultDto.setRetdesc(AjaxConstant.SYSTEM_ERROR_DESC);
			// TODO log fatal
			return resultDto;
		}
		List<PeriodDto> availablePeriodDtoList = new ArrayList<>();
		for (Period period : availablePeriodList) {
			availablePeriodDtoList.add(new PeriodDto(period));
		}
		data.put("availablePeriodList", availablePeriodDtoList);
		// 3.得到开奖列表
		List<AwardWithPeriod> awardList = awardCacheService.getLatestNPeriodAward(gameId, SystemConfigs.getIntValue("tradfootball_game_award_list_count_" + gameId, 6));
		List<AwardWithPeriodDto> awardDtoList = new ArrayList<>();
		for (AwardWithPeriod awardWithPeriod : awardList) {
			awardDtoList.add(new AwardWithPeriodDto(awardWithPeriod));
		}
		data.put("awardList", awardDtoList);
		// 4.得到对阵信息
		List<GameMatchWithMatch> gameMatchList = tradFootBallMatchCacheService.getGameMatchList(gameId, availablePeriodList.get(0).getPeriodNo());
		List<GameMatchWithMatchDto> gameMatchWithMatchDtoList = new ArrayList<>();
		for (GameMatchWithMatch gameMatchWithMatch : gameMatchList) {
			gameMatchWithMatchDtoList.add(new GameMatchWithMatchDto(gameMatchWithMatch));
		}
		data.put("gameMatchList", gameMatchWithMatchDtoList);
		// 5.当前系统时间戳
		data.put("systemTime", String.valueOf(System.currentTimeMillis()));
		// 6.设置结果dto
		resultDto.setObject(data);
		return resultDto;
	}
	
	/**
	 * 向request中设置属性
	 * 
	 * @return
	 */
	protected void setAttribute(String attrName, Object attrValue) {
	
		HttpServletRequest request = WebConstant.requestTL.get();
		request.setAttribute(attrName, attrValue);
	}
	
	/**
	 * 向request中设置属性
	 * 
	 * @return
	 */
	protected String getParameter(String parameterName) {
	
		HttpServletRequest request = WebConstant.requestTL.get();
		return LotteryUtil.trimString(request.getParameter(parameterName));
	}
	
	/**
	 * 向request中设置属性
	 * 
	 * @return
	 */
	protected String getClientIP() {
	
		HttpServletRequest request = WebConstant.requestTL.get();
		return LotteryUtil.getLastIp(request);
	}
	
	protected String getUserId() {
	
		HttpServletRequest request = WebConstant.requestTL.get();
		return Authentic.getUserId(request);
	}
}
