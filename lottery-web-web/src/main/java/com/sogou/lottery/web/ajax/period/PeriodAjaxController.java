package com.sogou.lottery.web.ajax.period;

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
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.common.constant.AjaxConstant;
import com.sogou.lottery.web.ajax.dto.PeriodDto;
import com.sogou.lottery.web.ajax.dto.ResultObjectDto;
import com.sogou.lottery.web.web.BaseController;

/**
 * 系统Ajax
 * 
 * @author lvzhenyu
 */
@Controller
@RequestMapping("/ajax/period")
public class PeriodAjaxController extends BaseController {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	/**
	 * 得到期次列表，type有next、prev、available三种
	 * 
	 * @param gameId
	 * @param type
	 * @return
	 */
	@RequestMapping("/get/{gameId}/{type}")
	@ResponseBody
	public ResultObjectDto<List<PeriodDto>> getPeriod(@PathVariable("gameId")
	String gameId, @PathVariable("type")
	String type) {
	
		ResultObjectDto<List<PeriodDto>> resultDto = new ResultObjectDto<>(AjaxConstant.SUCCESS_CODE, AjaxConstant.SUCCESS_DESC);
		if (StringUtils.isBlank(gameId) || GameType.getGameType(gameId) == GameType.GAME_NULL || StringUtils.isBlank(type)) {
			resultDto.setRetcode(AjaxConstant.PARAM_ERROR_CODE);
			resultDto.setRetdesc(AjaxConstant.PARAM_ERROR_DESC);
			return resultDto;
		}
		// 1.得到彩种详情
		Game game = gameCacheService.getGameById(gameId);
		if (game == null) {
			resultDto.setRetcode(AjaxConstant.PARAM_ERROR_CODE);
			resultDto.setRetdesc(AjaxConstant.PARAM_ERROR_DESC);
			return resultDto;
		}
		// 2.得到期次信息
		List<Period> periodList = null;
		if (type.equals("next")) {
			periodList = periodCacheService.getNextPeriodList(gameId, 1);
		} else if (type.equals("prev")) {
			periodList = periodCacheService.getPrevPeriodList(gameId, 1);
		} else if (type.equals("available")) {
			periodList = periodCacheService.getAvailablePeriod(gameId);
		} else {
			resultDto.setRetcode(AjaxConstant.PARAM_ERROR_CODE);
			resultDto.setRetdesc(AjaxConstant.PARAM_ERROR_DESC);
			return resultDto;
		}
		if (periodList != null && !periodList.isEmpty()) {
			List<PeriodDto> periodDtoList = new ArrayList<>();
			for (Period period : periodList) {
				periodDtoList.add(new PeriodDto(period));
			}
			resultDto.setObject(periodDtoList);
		} else {
			// TODO log error
			resultDto.setRetcode(AjaxConstant.SYSTEM_ERROR_CODE);
			resultDto.setRetdesc(AjaxConstant.SYSTEM_ERROR_DESC);
		}
		return resultDto;
	}
	
}
