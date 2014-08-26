package com.sogou.lottery.web.ajax.award;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.game.GameRuleFactory;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.common.constant.AjaxConstant;
import com.sogou.lottery.web.ajax.dto.AwardWithPeriodDto;
import com.sogou.lottery.web.ajax.dto.ResultObjectDto;
import com.sogou.lottery.web.web.BaseController;

/**
 * 系统Ajax
 * 
 * @author lvzhenyu
 */
@Controller
@RequestMapping("/ajax/award")
public class AwardAjaxController extends BaseController {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	private static final String AWARD_TYPE_TODAY = "today";
	private static final String AWARD_TYPE_PERIOD = "period";
	
	/**
	 * 获得开奖数据<br>
	 * awardType两种：today、period，如果是today，gameId必须是高频彩的一种
	 * 
	 * @return
	 */
	@RequestMapping("/get/{gameId}/{awardType}")
	@ResponseBody
	public ResultObjectDto<Object> getAward(@PathVariable("gameId")
	String gameId, @PathVariable("awardType")
	String awardType) {
	
		ResultObjectDto<Object> resultDto = new ResultObjectDto<>(AjaxConstant.PARAM_ERROR_CODE, AjaxConstant.PARAM_ERROR_DESC);
		// 0.校验参数
		if (StringUtils.isBlank(gameId) || GameType.getGameType(gameId) == GameType.GAME_NULL) {
			return resultDto;
		}
		if (StringUtils.isBlank(awardType) || (!awardType.equals(AWARD_TYPE_TODAY) && !awardType.equals(AWARD_TYPE_PERIOD))) {
			return resultDto;
		}
		// 1.得到彩种详情
		Game game = gameCacheService.getGameById(gameId);
		if (game == null) {
			return resultDto;
		}
		// 2.针对不同开奖类型
		List<AwardWithPeriod> awardWithPeriodList = new ArrayList<>();
		switch (awardType) {
			case AWARD_TYPE_PERIOD:
				// 2.1按期次获取
				String periodNo = getParameter("periodNo");
				if (StringUtils.isBlank(periodNo) || !GameRuleFactory.getGameRule(gameId).isValidPeriod(periodNo)) {
					return resultDto;
				}
				AwardWithPeriod awardWithPeriod = awardCacheService.getAwardCache(gameId, periodNo);
				awardWithPeriodList.add(awardWithPeriod);
				break;
			case AWARD_TYPE_TODAY:
				if (!GameType.isHighFrequencyGame(GameType.getGameType(gameId))) {
					return resultDto;
				}
				awardWithPeriodList = awardCacheService.getAwardListByDate(gameId, DateUtil.getCurrentDate());
				break;
		}
		// 2.得到期次信息
		resultDto.setRetcode(AjaxConstant.SUCCESS_CODE);
		resultDto.setRetdesc(AjaxConstant.SUCCESS_DESC);
		List<AwardWithPeriodDto> awardWithPeriodDtoList = new ArrayList<>();
		for (AwardWithPeriod awardWithPeriod : awardWithPeriodList) {
			awardWithPeriodDtoList.add(new AwardWithPeriodDto(awardWithPeriod));
		}
		if (GameType.isHighFrequencyGame(GameType.getGameType(gameId)) && awardType.equals(AWARD_TYPE_TODAY)) {
			Map<Integer,AwardWithPeriodDto> no2AwardWithPeriodDtoMap = new HashMap<Integer,AwardWithPeriodDto>();
			for (AwardWithPeriodDto awardWithPeriodDto : awardWithPeriodDtoList) {
				no2AwardWithPeriodDtoMap.put(Integer.parseInt(awardWithPeriodDto.getPeriodNo().substring(awardWithPeriodDto.getPeriodNo().length() - 2)), awardWithPeriodDto);
			}
			resultDto.setObject(no2AwardWithPeriodDtoMap);
		} else {
			resultDto.setObject(awardWithPeriodDtoList);
		}
		return resultDto;
	}
}
