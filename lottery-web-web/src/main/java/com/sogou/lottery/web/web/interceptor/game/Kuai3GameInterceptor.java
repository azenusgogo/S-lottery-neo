package com.sogou.lottery.web.web.interceptor.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.base.constant.Kuai3Constant;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.cache.operator.service.GameMissingCacheService;
import com.sogou.lottery.cache.operator.service.Kuai3StatCacheService;
import com.sogou.lottery.common.constant.WebConstant;

/**
 * 高频彩种拦截器
 */
public class Kuai3GameInterceptor implements HandlerInterceptor {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@Resource(name = "gameMissingCacheServiceImpl")
	private GameMissingCacheService gameMissingCacheService;
	
	@Resource(name = "kuai3StatCacheServiceImpl")
	private Kuai3StatCacheService kuai3StatCacheService;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		try {
			String gameId = (String) request.getAttribute("gameId");
			Date today = DateUtil.getCurrentDate();
			// 1.快三形态/和值
			// 1.1形态
			List<String> formStatList = kuai3StatCacheService.getKuai3FormStatDataList(gameId, today);
			request.setAttribute("formStatList", formStatList);
			// 1.2和值
			List<String> sumStatList = kuai3StatCacheService.getKuai3SumStatDataList(gameId, today);
			request.setAttribute("sumStatList", sumStatList);
			// 2.得到今日开奖列表
			List<AwardWithPeriod> todayAwardList = awardCacheService.getAwardListByDate(gameId, today);
			int periodCount1Day = Kuai3Constant.GAMEID2PERIODCOUNT1DAY_MAP.get(gameId);
			if (periodCount1Day != todayAwardList.size()) {
				// 2.1拼装成序号到开奖列表
				Map<Integer,AwardWithPeriod> seq2AwardMap = new HashMap<Integer,AwardWithPeriod>();
				for (AwardWithPeriod awardWithPeriod : todayAwardList) {
					String periodNo = awardWithPeriod.getPeriod().getPeriodNo();
					seq2AwardMap.put(Integer.parseInt(periodNo.substring(periodNo.length() - 2)) - 1, awardWithPeriod);
				}
				// 2.2拼接成全部的开奖数据
				List<AwardWithPeriod> awardWithPeriodList = new ArrayList<>();
				for (int i = 0; i < periodCount1Day; ++i) {
					if (seq2AwardMap.containsKey(i)) {
						awardWithPeriodList.add(seq2AwardMap.get(i));
					} else {
						awardWithPeriodList.add(null);
					}
				}
				todayAwardList = awardWithPeriodList;
			}
			request.setAttribute("todayAwardList", todayAwardList);
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
