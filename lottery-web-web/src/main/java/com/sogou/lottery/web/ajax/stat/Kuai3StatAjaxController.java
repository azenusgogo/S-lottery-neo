package com.sogou.lottery.web.ajax.stat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.operator.service.Kuai3StatCacheService;
import com.sogou.lottery.common.constant.AjaxConstant;
import com.sogou.lottery.web.ajax.dto.ResultObjectDto;
import com.sogou.lottery.web.web.BaseController;

/**
 * 普通彩种ajax
 * 
 * @author lvzhenyu
 */
@Controller
@RequestMapping("/ajax/stat")
public class Kuai3StatAjaxController extends BaseController {
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@Resource(name = "kuai3StatCacheServiceImpl")
	private Kuai3StatCacheService kuai3StatCacheService;
	
	/**
	 * 得到快三形态统计和和值统计
	 * 
	 * @return
	 */
	@RequestMapping("/get/{gameId}/formSumStat")
	@ResponseBody
	public ResultObjectDto<List<List<String>>> getAll(@PathVariable("gameId")
	String gameId) {
	
		// 0.校验参数
		ResultObjectDto<List<List<String>>> resultDto = new ResultObjectDto<>(AjaxConstant.PARAM_ERROR_CODE, AjaxConstant.PARAM_ERROR_DESC);
		// 0.1校验gameId
		if (StringUtils.isBlank(gameId) || GameType.getGameType(gameId) == GameType.GAME_NULL) {
			return resultDto;
		}
		// 只为快三使用
		if (!GameType.isKuai3(gameId)) {
			return resultDto;
		}
		// 1.得到彩种详情
		Game game = gameCacheService.getGameById(gameId);
		if (game == null) {
			// TODO log info
			return resultDto;
		}
		// 2.得到
		resultDto.setRetcode(AjaxConstant.SUCCESS_CODE);
		resultDto.setRetdesc(AjaxConstant.SUCCESS_DESC);
		Date today = DateUtil.getCurrentDate();
		List<String> formStatList = kuai3StatCacheService.getKuai3FormStatDataList(gameId, today);
		List<String> sumStatList = kuai3StatCacheService.getKuai3SumStatDataList(gameId, today);
		List<List<String>> result = new ArrayList<>();
		result.add(formStatList);
		result.add(sumStatList);
		resultDto.setObject(result);
		return resultDto;
	}
}
