package com.sogou.lottery.web.service.pay.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.constant.PayType;
import com.sogou.lottery.base.pay.PayErrorCode;
import com.sogou.lottery.base.pay.dto.PayResultDto;
import com.sogou.lottery.base.pay.dto.ResultPayDto;
import com.sogou.lottery.base.status.GameStatus;
import com.sogou.lottery.base.status.PayOrderStatus;
import com.sogou.lottery.base.status.UserOrderStatus;
import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.util.MathUtil;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.game.GameRule;
import com.sogou.lottery.base.vo.game.GameRuleFactory;
import com.sogou.lottery.base.vo.order.PayDetail;
import com.sogou.lottery.base.vo.order.PayOrder;
import com.sogou.lottery.base.vo.order.UserOrder;
import com.sogou.lottery.base.vo.order.UserOrderLog;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.base.vo.user.User;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.dao.PayDetailDao;
import com.sogou.lottery.dao.PayOrderDao;
import com.sogou.lottery.dao.PeriodDao;
import com.sogou.lottery.dao.RawBetNumberDao;
import com.sogou.lottery.dao.SequenceDao;
import com.sogou.lottery.dao.UserOrderDao;
import com.sogou.lottery.dao.UserOrderLogDao;
import com.sogou.lottery.dao.user.UserDao;
import com.sogou.lottery.web.service.pay.dto.PayCheckResultDto;
import com.sogou.lottery.web.service.pay.dto.PayDto;
import com.sogou.lottery.web.service.user.dto.BalanceDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.service.UserService;

@Service
public class PayService {
	
	private final Logger Log = LOG.order;
	@Autowired
	private UserDao userDao;
	@Autowired
	private PeriodDao periodDao;
	@Autowired
	private SequenceDao sequenceDao;
	@Autowired
	private UserOrderDao userOrderDao;
	@Autowired
	private UserOrderLogDao userOrderLogDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PayDetailDao payDetailDao;
	@Autowired
	private RawBetNumberDao rawBetNumberDao;
	@Autowired
	private UserService userService;
	@Autowired
	private GameCacheService gameCacheService;
	@Autowired
	private PeriodCacheService periodCacheService;
	
	public ResultPayDto<PayCheckResultDto> check(PayDto pay) {
	
		Checked checked = checkPayOrder(pay, false);
		if (!checked.isSuccess()) {
			return new ResultPayDto<>(checked.error);
		}
		// 校验结束
		// 去PEAK查询余额
		UserDto userDto = new UserDto();
		userDto.setUserId(pay.getUserId());
		userDto.setUserIp(pay.getUserIp());
		ResultUserDto<BalanceDto> balance = null;
		try {
			balance = userService.queryBalance(userDto);
		} catch (Exception e) {
			return new ResultPayDto<>(PayErrorCode.CommonNetwork);
		}
		if (!balance.isSucces()) {
			return new ResultPayDto<>(balance.getRetcode(), balance.getRetdesc());
		}
		Long userBalance = balance.getResult().getAvailableAmount();
		PayCheckResultDto result = new PayCheckResultDto();
		result.setUserId(checked.user.getUserId());
		result.setNickName(checked.user.getNickName());
		result.setGameId(checked.userOrder.getGameId());
		result.setGameCn(checked.game.getGameCn());
		result.setPeriodNo(checked.userOrder.getPeriodNo());
		result.setPayOrderId(checked.payOrder.getPayOrderId());
		result.setPayAmount(checked.userOrder.getUserOrderAmount());
		result.setBalanceAmount(userBalance);
		result.setChargeAmount(MathUtil.minus(result.getPayAmount(), result.getBalanceAmount()));
		if (userBalance < checked.payOrder.getCashAmount()) {
			return new ResultPayDto<>(PayErrorCode.PayBalance, result);
		} else {
			return new ResultPayDto<>(result);
		}
	}
	
	/**
	 * 余额支付，需要支付密码
	 * 
	 * @param pay
	 * @return
	 */
	public ResultPayDto<PayResultDto> payOrder(PayDto pay) {
	
		Checked checked = checkPayOrder(pay, true);
		if (!checked.isSuccess()) {
			return new ResultPayDto<>(checked.error);
		}
		// 校验结束
		PayResultDto payRes = null;
		ResultPayDto<PayResultDto> payResult = null;
		int maxTimes = userService.getPwdErrorMaxTimes();
		int timesLeft = maxTimes - userService.getPwdErrorTimes(checked.userOrder.getPayerUserId());
		if (timesLeft > 0) {
			
			pay.setUserId(checked.payOrder.getUserId());
			pay.setTitle(checked.payOrder.getTitle());
			pay.setCashAmount(checked.payOrder.getCashAmount());
			// 支付订单30天在peak都有效
			pay.setPayDeadline(new Timestamp(DateUtil.add(DateUtil.getCurrentDate(), 30, Calendar.DATE).getTime()));
			pay.setCreateTime(checked.payOrder.getCreateTime());
			if (StringUtils.isBlank(checked.userOrder.getBettorName()) || StringUtils.isBlank(checked.userOrder.getBettorIdno())) {
				// 补全投注订单用户身份信息
				((PayService) AopContext.currentProxy()).updateUserInfo(checked.user, checked.userOrder, checked.payOrder);
			}
			// 支付调用
			ResultUserDto<PayResultDto> pr = userService.doBalancePay(pay);
			int res;
			if (pr.isSucces()) {
				payRes = pr.getResult();
				res = ((PayService) AopContext.currentProxy()).successPay(checked.userOrder, checked.payOrder, payRes);
				if (res < 0) {
					payResult = new ResultPayDto<>(PayErrorCode.PayRefund);
				} else if (res == 0) {
					Log.fatal(String.format("Found pay order status already payed while paying from web [%s], pls check", checked.payOrder.getPayOrderId()));
					payResult = new ResultPayDto<>(PayErrorCode.PayDone);
				} else {
					payResult = new ResultPayDto<>(new PayResultDto(payRes));
				}
			} else {
				payResult = new ResultPayDto<>(pr.getRetcode(), pr.getRetdesc(), pr.getResult());
				if (pr.getRetcode() == UserErrorCode.PayPwd.getCode()) {
					timesLeft--;
				}
			}
		} else {
			payResult = new ResultPayDto<>(UserErrorCode.PayPwdFrozen.getCode(), UserErrorCode.PayPwdFrozen.getDesc());
		}
		if (payRes == null) {
			payRes = new PayResultDto();
		}
		payRes.setNickName(checked.user.getNickName());
		payRes.setPayOrderId(pay.getPayOrderId());
		payRes.setPwdErrorTimes(timesLeft);
		// payRes.setGameId(userOrder.getGameId());
		// payRes.setPeriodNo(userOrder.getPeriodNo());
		payRes.setSystemTime(System.currentTimeMillis());
		payResult.setResult(payRes);
		return payResult;
	}
	
	@Transactional(value = "main")
	public void updateUserInfo(User user, UserOrder userOrder, PayOrder payOrder) {
	
		userOrder = userOrderDao.getByIdWithLock(userOrder.getUserOrderId());
		payOrder = payOrderDao.getByIdWithLock(payOrder.getPayOrderId());
		userOrder.setBettorName(user.getTrueName());
		userOrder.setBettorIdno(user.getIdCardNo());
		payOrder.setStatus(PayOrderStatus.SENT);
		userOrderDao.updateUserInfo(userOrder);
		payOrderDao.updateStatus(payOrder);
	}
	
	/**
	 * 支付成功后的回调方法
	 * 
	 * @param userOrder
	 * @param payOrder
	 * @param payResult
	 * @return 0-无需重复支付; 1-支付成功; -1支付失败，等待退款
	 */
	@Transactional(value = "main")
	public int successPay(UserOrder userOrder, PayOrder payOrder, PayResultDto payResult) {
	
		// TODO 改成rpc调用
		Validate.notNull(payResult, "PayResult is null while paying");
		Validate.notNull(userOrder, "UserOrder is null while paying");
		Validate.notNull(userOrder, "UserOrder is null while paying");
		Validate.notNull(payOrder, "PayOrder is null while paying");
		
		String userOrderId = userOrder.getUserOrderId();
		userOrder = userOrderDao.getByIdWithLock(userOrderId);
		Validate.notNull(userOrder, "Found null userOrder [%s] while paying", userOrderId);
		String payOrderId = payOrder.getPayOrderId();
		payOrder = payOrderDao.getByIdWithLock(payOrderId);
		Validate.notNull(payOrder, "Found null payOrder [%s] while paying", payOrderId);
		
		// 支付完成后，发现订单已经关闭了，这时需要对订单做退款
		if (UserOrderStatus.isClosed(userOrder)) {
			// TODO refund MQ
			Log.fatal("Pay Order expired, waiting for refund - " + payOrder);
			return -1;
		} else if (UserOrderStatus.isInitial(userOrder)) {
			Validate.isTrue(PayOrderStatus.isPayable(payOrder), "Found illegal payOrder status [%s][%s] while pay success", payOrderId, payOrder.getStatus());
			payOrder.setPaySystemId(payResult.getPaySystemId());
			payOrder.setPaySystemTime(payResult.getPaySystemTime());
			payOrder.setStatus(PayOrderStatus.PAYSUCC);
			userOrder.setOrderStatus(UserOrderStatus.PAYSUCC);
			userOrderDao.updateOrderStatus(userOrder.getUserOrderId(), userOrder.getOrderStatus());
			payOrderDao.updatePayStatus(payOrder);
			PayDetail payDetail = new PayDetail();
			payDetail.setPayOrderId(payOrder.getPayOrderId());
			payDetail.setPayAmount(payOrder.getCashAmount());
			payDetail.setPayType(PayType.CASH);
			payDetail.setRefundAmount(0L);
			payDetailDao.add(payDetail);
			userOrderLogDao.add(new UserOrderLog(userOrderId, UserOrderStatus.PAYSUCC, "支付成功"));
			return 1;
		}
		return 0;
	}
	
	private class Checked {
		
		Checked(PayErrorCode error) {
		
			this.error = error;
		}
		
		PayOrder payOrder;
		UserOrder userOrder;
		User user;
		// Period period;
		Game game;
		PayErrorCode error;
		
		boolean isSuccess() {
		
			return PayErrorCode.SUCCESS.getCode() == error.getCode();
		}
	}
	
	private Checked checkPayOrder(PayDto pay, boolean payFlag) {
	
		try {
			Validate.notNull(pay);
			Validate.notBlank(pay.getUserId());
			Validate.notBlank(pay.getUserIp());
			if (payFlag) {
				// 如果只真正支付，则校验支付密码空值
				Validate.notBlank(pay.getPayPwd());
			}
			Validate.notBlank(pay.getPayOrderId());
		} catch (Exception e) {
			if (Log.isDebugEnabled()) {
				Log.debug(e, e);
			}
			return new Checked(PayErrorCode.CommonArgument);
		}
		PayOrder payOrder = payOrderDao.getById(pay.getPayOrderId());
		if (payOrder == null) {
			return new Checked(PayErrorCode.PayOrderExist);
		}
		String userId = payOrder.getUserId();
		if (!pay.getUserId().equals(userId)) {
			return new Checked(PayErrorCode.PayUserDiff);
		}
		UserOrder userOrder = userOrderDao.getById(payOrder.getBusinessId());
		if (userOrder == null) {
			return new Checked(PayErrorCode.PayUserExist);
		}
		User user = userDao.getByUserId(userOrder.getPayerUserId());
		// 支付的时候不允许用户信息未补全
		if (user == null) {
			return new Checked(PayErrorCode.PayUserExist);
		}
		if (!PayOrderStatus.isPayable(payOrder)) {
			return new Checked(PayErrorCode.PayOrderStatus);
		} else if (UserOrderStatus.isClosed(userOrder)) {
			return new Checked(PayErrorCode.PayClosed);
		} else if (!UserOrderStatus.isInitial(userOrder)) {
			return new Checked(PayErrorCode.PayDone);
		}
		GameRule gr = GameRuleFactory.getGameRule(userOrder.getGameId());
		Game game = gameCacheService.getGameById(userOrder.getGameId());
		if (gr == null || game == null) {
			return new Checked(PayErrorCode.CommonArgument);
		} else if (game.getGameStatus() == GameStatus.GMAE_STATUS_INVALID) {
			return new Checked(PayErrorCode.OrderGame);
		}
		Date now = DateUtil.getCurrentDate();
		Period period = periodCacheService.getPeriod(userOrder.getGameId(), userOrder.getPeriodNo());
		if (period != null && now.after(period.getStopPayEndTime())) {
			return new Checked(PayErrorCode.PayExpired);
		}
		period = periodDao.getByGameIdAndPeriodNo(userOrder.getGameId(), userOrder.getPeriodNo());
		if (period == null) {
			return new Checked(PayErrorCode.PayPeriodExist);
		} else if (now.after(period.getOffcialEndTime()) || now.before(period.getOffcialStartTime())) {
			boolean preSalePeriod = false;
			List<Period> availables = periodCacheService.getAvailablePeriod(game.getGameId());
			for (Period available : availables) {
				if (available.getPeriodNo().equals(period.getPeriodNo())) {
					preSalePeriod = true;
				}
			}
			// 如果当前是预售期次并且彩种允许预售，则不返回错误
			if (!preSalePeriod || GameType.isTradFootballGame(game.getGameType())) {
				return new Checked(PayErrorCode.PayExpired);
			}
		}
		Checked checked = new Checked(PayErrorCode.SUCCESS);
		checked.payOrder = payOrder;
		checked.userOrder = userOrder;
		checked.user = user;
		// checked.period = period;
		checked.game = game;
		return checked;
	}
}
