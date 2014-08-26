package com.sogou.lottery.web.web.interceptor.game;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.lottery.base.constant.UserOrderType;
import com.sogou.lottery.base.tool.OrderTool;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.order.BetNumber;
import com.sogou.lottery.base.vo.order.RawBetNumber;
import com.sogou.lottery.base.vo.order.UserOrder;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.common.constant.WebConstant;
import com.sogou.lottery.dao.RawBetNumberDao;
import com.sogou.lottery.dao.UserOrderDao;
import com.sogou.lottery.web.web.util.Authentic;

/**
 * 普通彩种拦截器
 */
public class BaseGameInterceptor implements HandlerInterceptor {
	
	@Resource(name = "periodCacheServiceImpl")
	private PeriodCacheService periodCacheService;
	
	@Resource(name = "awardCacheServiceImpl")
	private AwardCacheService awardCacheService;
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	@Autowired
	private UserOrderDao userOrderDao;
	
	@Autowired
	private RawBetNumberDao rawBetNumberDao;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
		request.setAttribute("systemTime", System.currentTimeMillis());
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		try {
			String uri = request.getRequestURI();
			String gameId = uri.split("\\.")[0].replaceAll("/", "");
			if (StringUtils.isBlank(gameId)) {
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
			// 2.得到是否有orderId参数，仅在普通购买情况下使用
			String orderId = request.getParameter("orderId");
			String userId = Authentic.getUserId(request);
			if (StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(userId) && OrderTool.isValidOrderId(orderId)) {
				UserOrder userOrder = userOrderDao.getById(orderId);
				// 如果是普通购买，且用户名和订单中的一致
				if (userOrder != null && userOrder.getBettorUserId().equals(userId) && userOrder.getOrderType() == UserOrderType.COMMON && userOrder.getGameId().equals(gameId)) {
					RawBetNumber rawBetNumber = rawBetNumberDao.getById(orderId);
					List<BetNumber> betNumberList = rawBetNumber.uncompress();
					request.setAttribute("betNumberList", betNumberList);
				}
			}
			// 3.得到Post参数，看看refer是否是从站内到的，如果不是，则不需要回写到页面中
			String betNumber = request.getParameter("quickBetNumber");
			if (StringUtils.isNotBlank(betNumber) && betNumber.length() < 500) {
				String refer = request.getHeader("Referer");
				if (StringUtils.isNotBlank(refer)) {
					request.setAttribute("quickBetNumber", betNumber);
				}
			}
			response.setHeader("Cache-Control","no-cache"); 
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
