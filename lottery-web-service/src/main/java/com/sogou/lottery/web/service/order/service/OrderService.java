package com.sogou.lottery.web.service.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sogou.lottery.base.constant.GameType;
import com.sogou.lottery.base.constant.PrizeConstant;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.constant.UserOrderType;
import com.sogou.lottery.base.order.OrderConstant;
import com.sogou.lottery.base.order.OrderErrorCode;
import com.sogou.lottery.base.order.dto.BetResultDto;
import com.sogou.lottery.base.order.dto.ResultOrderDto;
import com.sogou.lottery.base.order.dto.UserBetDto;
import com.sogou.lottery.base.status.GameStatus;
import com.sogou.lottery.base.status.PayOrderStatus;
import com.sogou.lottery.base.status.RawBetNumberStatus;
import com.sogou.lottery.base.status.UserOrderLogType;
import com.sogou.lottery.base.status.UserOrderPrizeStatus;
import com.sogou.lottery.base.status.UserOrderStatus;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.util.MathUtil;
import com.sogou.lottery.base.util.MoneyUtil;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.game.GameRule;
import com.sogou.lottery.base.vo.game.GameRuleFactory;
import com.sogou.lottery.base.vo.order.BetNumber;
import com.sogou.lottery.base.vo.order.OrderInfo;
import com.sogou.lottery.base.vo.order.PayOrder;
import com.sogou.lottery.base.vo.order.RawBetNumber;
import com.sogou.lottery.base.vo.order.StakeOrder;
import com.sogou.lottery.base.vo.order.UserOrder;
import com.sogou.lottery.base.vo.order.UserOrderLog;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.base.vo.user.User;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.cache.operator.service.AwardCacheService;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.dao.BetNumberDao;
import com.sogou.lottery.dao.PayOrderDao;
import com.sogou.lottery.dao.PeriodDao;
import com.sogou.lottery.dao.RawBetNumberDao;
import com.sogou.lottery.dao.SequenceDao;
import com.sogou.lottery.dao.StakeOrderDao;
import com.sogou.lottery.dao.UserOrderDao;
import com.sogou.lottery.dao.UserOrderLogDao;
import com.sogou.lottery.dao.pagination.dto.Page;
import com.sogou.lottery.dao.user.UserDao;
import com.sogou.lottery.util.PageUtil;
import com.sogou.lottery.web.service.user.dto.BalanceDto;
import com.sogou.lottery.web.service.user.dto.OrderDetailDto;
import com.sogou.lottery.web.service.user.dto.OrderQueryDto;
import com.sogou.lottery.web.service.user.dto.OrderQueryResultDto;
import com.sogou.lottery.web.service.user.dto.StakeBetInfoDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.service.UserService;

@Service
public class OrderService {
	
	private final Logger log = LOG.order;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SequenceDao sequenceDao;
	@Autowired
	private UserOrderDao userOrderDao;
	@Autowired
	private UserOrderLogDao userOrderLogDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PeriodCacheService periodCacheService;
	@Autowired
	private RawBetNumberDao rawBetNumberDao;
	@Autowired
	private PeriodDao periodDao;
	@Autowired
	private UserService userService;
	@Autowired
	private AwardCacheService awardCacheService;
	@Autowired
	private GameCacheService gameCacheService;
	
	private static final int PAGE_SIZE = 10;
	
	public ResultOrderDto<BetResultDto> addOrder(UserBetDto bet) {
	
		try {
			Validate.notBlank(bet.getUserId());
			Validate.notBlank(bet.getGameId());
			Validate.notBlank(bet.getPeriodNo());
			Validate.notNull(bet.getPrice());
			Validate.isTrue(bet.getPrice() > 0);
			Validate.notBlank(bet.getRawBetNumbers());
			Validate.notNull(bet.getSourceType());
		} catch (NullPointerException | IllegalArgumentException e) {
			if (log.isDebugEnabled()) {
				log.debug(bet, e);
			}
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.CommonArgument);
		}
		if (bet.getBetTimes() == null) {
			bet.setBetTimes(1L);
		}
		// 校验开始
		GameRule gr = GameRuleFactory.getGameRule(bet.getGameId());
		Game game = gameCacheService.getGameById(bet.getGameId());
		if (gr == null || game == null) {
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.CommonArgument);
		} else if (game.getGameStatus() == GameStatus.GMAE_STATUS_INVALID) {
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.OrderGame);
		}
		// 验证期次，场次时间是否到期到时
		Date now = DateUtil.getCurrentDate();
		Period period = periodCacheService.getPeriod(bet.getGameId(), bet.getPeriodNo());
		if (period != null && now.after(period.getStopPayEndTime())) {
			return new ResultOrderDto<>(OrderErrorCode.OrderPeriodExpired);
		} else if (period == null) {
			period = periodDao.getByGameIdAndPeriodNo(bet.getGameId(), bet.getPeriodNo());
		}
		if (period == null) {
			return new ResultOrderDto<>(OrderErrorCode.OrderPeriodExist);
		} else if (now.after(period.getOffcialEndTime()) || now.before(period.getOffcialStartTime())) {
			boolean preSalePeriod = false;
			List<Period> availables = periodCacheService.getAvailablePeriod(bet.getGameId());
			for (Period available : availables) {
				if (available.getPeriodNo().equals(period.getPeriodNo())) {
					preSalePeriod = true;
				}
			}
			// 如果当前是预售期次并且彩种允许预售，则不返回错误
			if (!preSalePeriod || GameType.isTradFootballGame(gr.getGameType())) {
				return new ResultOrderDto<>(OrderErrorCode.OrderPeriodExpired);
			}
		}
		Long maxAmount = Long.parseLong(SystemConfigs.get(OrderConstant.CONFIG_AMOUNT_MAX_ORDER, OrderConstant.CONFIG_AMOUNT_MAX_ORDER_DEFAULT));
		if (bet.getPrice() > maxAmount) {
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.OrderAmount.getCode(), OrderErrorCode.OrderAmount.getDesc().replaceAll("(\\d)+", MoneyUtil.fen2yuan(maxAmount)));
		}
		// 校验结束
		// 去PEAK查询余额
		UserDto userDto = new UserDto();
		userDto.setUserId(bet.getUserId());
		userDto.setUserIp(bet.getUserIp());
		ResultUserDto<BalanceDto> balance = null;
		try {
			balance = userService.queryBalance(userDto);
		} catch (Exception e) {
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.CommonNetwork);
		}
		if (!balance.isSucces()) {
			return new ResultOrderDto<BetResultDto>(balance.getRetcode(), balance.getRetdesc());
		}
		Long userBalance = balance.getResult().getAvailableAmount();
		/*
		 * // 因为计算注数可能会比较耗时,先和前台传的金额比，如果不足，直接返回 if (userBalance <
		 * bet.getPrice()) { return new
		 * ResultOrderDto<BetResultDto>(ErrorCode.NoBalance); }
		 */
		RawBetNumber rawBetNumber = null;
		try {
			rawBetNumber = gr.parseBetNumber(bet);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(e, e);
			}
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.OrderBetFormat);
		}
		// 后台解析投注号码后如果金额超过上限直接返回
		if (rawBetNumber.getPrice() > maxAmount) {
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.OrderAmount.getCode(), OrderErrorCode.OrderAmount.getDesc() + MoneyUtil.fen2yuan(maxAmount) + "元");
		}
		int maxTimes = userService.getPwdErrorMaxTimes();
		int times = userService.getPwdErrorTimes(bet.getUserId());
		User user = userDao.getByUserId(bet.getUserId());
		UserOrder userOrder = null;
		PayOrder payOrder = null;
		if (times <= maxTimes) {
			
			userOrder = new UserOrder();
			userOrder.setPayerUserId(bet.getUserId());
			// userOrder.setBettorUserId(bet.getUserId());
			userOrder.setGameId(period.getGameId());
			userOrder.setPeriodNo(period.getPeriodNo());
			userOrder.setSourceType(bet.getSourceType());
			userOrder.setOrderType(UserOrderType.COMMON);
			userOrder.setBettorName(user == null ? null : user.getTrueName());
			userOrder.setBettorIdno(user == null ? null : user.getIdCardNo());
			// userOrder.setBettorUserId(bet.getUserId());
			// 根据所投期次场次的时间设置订单支付截止时间
			// userOrder.setDeadline(new Timestamp(System.currentTimeMillis() +
			// timeout));
			userOrder.setBetTimes(rawBetNumber.getBetTimes());
			userOrder.setUserOrderAmount(rawBetNumber.getPrice());
			
			payOrder = new PayOrder();
			payOrder.setTitle(getPayDescription(bet));
			try {
				((OrderService) AopContext.currentProxy()).addOrder(userOrder, payOrder, rawBetNumber);
			} catch (RuntimeException e) {
				log.fatal("Adding Order Exception occurs - " + userOrder, e);
				return new ResultOrderDto<BetResultDto>(OrderErrorCode.CommonError);
			}
		}
		BetResultDto result = new BetResultDto();
		result.setUserId(user == null ? bet.getUserId() : user.getUserId());
		result.setNickName(user == null ? null : user.getNickName());
		result.setGameId(period.getGameId());
		result.setGameCn(gr.getGameCn());
		result.setPeriodNo(period.getPeriodNo());
		// 剩余的尝试次数
		result.setPwdErrorTimes(maxTimes - times);
		result.setUserOrderId(userOrder == null ? null : userOrder.getUserOrderId());
		result.setPayOrderId(payOrder == null ? null : payOrder.getPayOrderId());
		result.setPayAmount(userOrder == null ? null : userOrder.getUserOrderAmount());
		result.setBalanceAmount(userBalance);
		result.setChargeAmount(MathUtil.minus(result.getPayAmount(), result.getBalanceAmount()));
		result.setSystemTime(now.getTime());
		// 再和后台算的金额比，如果不足前台提示充值，投注号码先入库，后返回
		if (userBalance < bet.getPrice() || userBalance < rawBetNumber.getPrice()) {
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.OrderBalance, result);
		} else if (user == null) {
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.OrderUserExist, result);
		} else {
			return new ResultOrderDto<BetResultDto>(result);
		}
	}
	
	@Transactional(value = "main")
	public void addOrder(UserOrder userOrder, PayOrder payOrder, RawBetNumber rawBetNumber) {
	
		Validate.notNull(userOrder);
		Validate.notNull(payOrder);
		Validate.notNull(rawBetNumber);
		Validate.notBlank(userOrder.getGameId());
		Validate.notBlank(userOrder.getPeriodNo());
		Validate.notBlank(userOrder.getPayerUserId());
		Validate.notBlank(userOrder.getPeriodNo());
		Validate.notNull(userOrder.getOrderType());
		Validate.notNull(userOrder.getUserOrderAmount());
		// Validate.isTrue(UserOrderType.isFollow(userOrder) &&
		// userOrder.getFollowNo() == null);
		
		// 拆单的时候再赋值 by 廖旭20140512
		userOrder.setDeliverable(Boolean.FALSE);
		if (UserOrderType.isGroup(userOrder)) {
			// 如果是合买，则投注人使用内部公共账号投注
			// TODO
			// userOrder.setBettorUserId(bettorUserId);
		} else {
			userOrder.setBettorUserId(userOrder.getPayerUserId());
		}
		userOrder.setOrderStatus(UserOrderStatus.INITIAL);
		userOrder.setPrizeStatus(UserOrderPrizeStatus.INITIAL);
		userOrder.setOfficialBonus(0L);
		userOrder.setCalculatedBonus(-1L);
		userOrder.setRefundAmount(0L);
		userOrder.setBetTimes(rawBetNumber.getBetTimes());
		userOrder.setSuccBetNumbers(0);
		userOrder.setFailedBetNumbers(0);
		userOrder.setTotalBetNumbers(0);
		String payOrderId = sequenceDao.getWithSequence(SequenceDao.Sequence.PayOrder);
		payOrder.setPayOrderId(payOrderId);
		String userOrderId = sequenceDao.getWithSequence(SequenceDao.Sequence.UserOrder);
		userOrder.setUserOrderId(userOrderId);
		userOrderDao.addNewOrder(userOrder);
		
		payOrder.setUserId(userOrder.getPayerUserId());
		payOrder.setStatus(PayOrderStatus.INITIAL);
		payOrder.setPayAmount(userOrder.getUserOrderAmount());
		payOrder.setCashAmount(userOrder.getUserOrderAmount());
		payOrder.setRefundAmount(0L);
		payOrder.setBusinessType(userOrder.getOrderType());
		if (UserOrderType.isCommon(userOrder)) {
			payOrder.setBusinessId(userOrderId);
		}
		payOrder.setGameId(userOrder.getGameId());
		payOrderDao.add(payOrder);
		
		rawBetNumber.setGameId(userOrder.getGameId());
		rawBetNumber.setUserOrderId(userOrderId);
		rawBetNumber.setPeriodNo(userOrder.getPeriodNo());
		rawBetNumber.setStatus(RawBetNumberStatus.INITIAL);
		rawBetNumberDao.add(rawBetNumber);
		
		UserOrderLog userOrderLog = new UserOrderLog(userOrderId, UserOrderLogType.INITIAL, "INITIAL");
		userOrderLogDao.add(userOrderLog);
	}
	
	private String getPayDescription(UserBetDto bet) {
	
		GameRule gr = GameRuleFactory.getGameRule(bet.getGameId());
		if (GameType.isContest(gr.getGameType())) {
			return gr.getGameCn() + "投注金";
		} else {
			return gr.getGameCn() + "第" + bet.getPeriodNo() + "期投注金";
		}
	}
	
	/**
	 * 描述：未支付订单总数擦好像
	 * 
	 * @param orderQueryDto
	 * @return
	 */
	public ResultUserDto<Integer> findNopayUserOrderNums(OrderQueryDto orderQueryDto) {
	
		Validate.notNull(orderQueryDto.getUserId());
		Integer totalNum = 0;
		totalNum = userOrderDao.getNoPayCountsByBetUserId(orderQueryDto.getUserId());
		return new ResultUserDto<>(totalNum);
	}
	
	/**
	 * 描述：查询用户订单列表
	 * 
	 * @param orderQueryDto
	 * @return
	 */
	public OrderQueryResultDto findUserOrderList(OrderQueryDto orderQueryDto) {
	
		if (orderQueryDto.getPageNo() == null || orderQueryDto.getPageNo() < 1) {
			orderQueryDto.setPageNo(1);
		}
		if (orderQueryDto.getPageSize() == null || orderQueryDto.getPageSize() < 1 || orderQueryDto.getPageSize() > 20) {
			orderQueryDto.setPageSize(PAGE_SIZE);
		}
		OrderQueryResultDto orderQueryResultDto = new OrderQueryResultDto(orderQueryDto);
		String userId = orderQueryDto.getUserId();
		Assert.notNull(userId);
		Integer prizeStatus = orderQueryDto.getPrizeStatus();
		Boolean noPayOrderFlag = orderQueryDto.getNoPayOrderFlag();
		Integer timeLevel = orderQueryDto.getTimeLevel();
		Boolean timeValidOrderFlag = orderQueryDto.getTimeValidOrderFlag();
		Integer pageNo = orderQueryDto.getPageNo();
		Integer pageSize = orderQueryDto.getPageSize();
		orderQueryDto.doQuery();
		Page<OrderInfo> page = new Page<>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page = userOrderDao.findOrdersByUserIdByPage(page, userId, prizeStatus, timeLevel, noPayOrderFlag, timeValidOrderFlag);
		List<OrderInfo> orderInfos = page.getResult();
		Integer totalNum = page.getTotalCount();
		for (OrderInfo orderInfo : orderInfos) {
			orderInfo.setGameDesc(this.getGameDesc(orderInfo.getGameId()));
			orderInfo.setOrderTypeDesc(this.getOrderTypeDesc(orderInfo.getOrderType()));
			Period period = periodCacheService.getPeriod(orderInfo.getGameId(), orderInfo.getPeriodNo());
			if (period != null) {
				orderInfo.setDeadline(period.getStopPayEndTime());
			}
			orderInfo.setOrderStatusDesc(this.getOrderStatusDesc(orderInfo.getOrderStatus(), orderInfo, period));
			orderInfo.setPrizeStatusDesc(this.getPrizeStatusDesc(orderInfo.getOrderStatus(), orderInfo.getPrizeStatus()));
			orderInfo.setOfficialBonus(this.handleOfficeBonus(orderInfo, orderInfo.getOfficialBonus()));
			orderInfo.setSpiltFlag(UserOrderStatus.isHaveSplit(orderInfo.getOrderStatus()));
		}
		orderQueryResultDto.getOrderQueryDto().setPageNo(this.getCurrentPage(pageNo, page.getTotalPage()));
		orderQueryResultDto.setTotalPage(page.getTotalPage());
		orderQueryDto.doneQuery();
		orderQueryResultDto.setOrderInfoList(orderInfos);
		orderQueryResultDto.setTotalNum(totalNum);
		return orderQueryResultDto;
	}
	
	int getCurrentPage(int currentPage, int totalPage) {
	
		int validPage = currentPage <= 0 ? 1 : currentPage;
		validPage = validPage > totalPage ? totalPage : validPage;
		return validPage;
	}
	
	/**
	 * @param gameId
	 * @return
	 */
	private String getGameDesc(String gameId) {
	
		return gameCacheService.getGameById(gameId).getGameCn();
	}
	
	/**
	 * @param orderType
	 * @return
	 */
	private String getOrderTypeDesc(Integer orderType) {
	
		String orderTypeDesc = "";
		switch (orderType) {
			case 10:
				orderTypeDesc = "普通";
				break;
			case 1:
				orderTypeDesc = "追号";
				break;
			case 2:
				orderTypeDesc = "合买";
				break;
		}
		return orderTypeDesc;
	}
	
	/**
	 * @param orderStatus
	 * @return
	 */
	private String getOrderStatusDesc(Integer orderStatus, OrderInfo orderInfo, Period period) {
	
		String orderStatusDesc = null;
		switch (orderStatus) {
			case UserOrderStatus.INITIAL:// 初始化
				orderStatusDesc = "未付款";
				break;
			case UserOrderStatus.PAYSUCC:// 支付成功
				orderStatusDesc = "已付款";
				break;
			case UserOrderStatus.SPLITED:// 拆分完成
				orderStatusDesc = "等待出票";
				break;
			case UserOrderStatus.SENT:// 投注发送完成
				if (period.getOffcialEndTime().before(new Date())) {
					log.fatal("orderstatus error=orderId:" + orderInfo.getUserOrderId() + " need refund ,but not bet success  ");
					orderStatusDesc = "订单退款";
					break;
				}
				orderStatusDesc = "等待出票";
				break;
			case UserOrderStatus.CLOSED:// 过期关闭
				orderStatusDesc = "订单关闭";
				break;
			case UserOrderStatus.REFUNDED:// 已退款
				orderStatusDesc = "订单退款";
				break;
			case UserOrderStatus.BETSUCC:// 投注成功
				orderStatusDesc = "投注成功";
				break;
			case UserOrderStatus.FUNDRAISING:// 资金募集中
				orderStatusDesc = "资金募集中";
				break;
		}
		return orderStatusDesc;
	}
	
	private Long handleOfficeBonus(OrderInfo orderInfo, Long officeBonus) {
	
		Integer orderStatus = orderInfo.getOrderStatus();
		Integer prizeStatus = orderInfo.getPrizeStatus();
		switch (orderStatus) {
			case UserOrderStatus.INITIAL:// 初始化
			case UserOrderStatus.PAYSUCC:// 支付成功
			case UserOrderStatus.SPLITED:// 拆分完成
			case UserOrderStatus.SENT:// 投注发送完成
			case UserOrderStatus.CLOSED:// 过期关闭
			case UserOrderStatus.REFUNDED:// 已退款
			default:
				if (officeBonus != null && officeBonus > 0) {
					log.fatal("order officebonus error=orderId:" + orderInfo.getUserOrderId() + " officebonus need be zero ,but be:" + officeBonus);
				}
				officeBonus = null;
			case UserOrderStatus.BETSUCC:// 投注成功
				switch (prizeStatus) {
					case PrizeConstant.PRIZE_STATUS_WAITING:
					case PrizeConstant.PRIZE_STATUS_NO:
					default:
						if (officeBonus != null && officeBonus > 0) {
							log.fatal("order officebonus error=orderId:" + orderInfo.getUserOrderId() + " officebonus need be zero ,but be:" + officeBonus);
						}
						officeBonus = null;
					case PrizeConstant.PRIZE_STATUS_SMALL_PRIZE:
					case PrizeConstant.PRIZE_STATUS_BIG_PRIZE:
						break;
				}
		}
		return officeBonus;
	}
	
	/**
	 * @param prizeStatus
	 * @return
	 */
	private String getPrizeStatusDesc(Integer orderStatus, Integer prizeStatus) {
	
		String prizeStatusDesc = null;
		switch (orderStatus) {
			case UserOrderStatus.INITIAL:// 初始化
			case UserOrderStatus.PAYSUCC:// 支付成功
			case UserOrderStatus.SPLITED:// 拆分完成
			case UserOrderStatus.SENT:// 投注发送完成
			case UserOrderStatus.CLOSED:// 过期关闭
			case UserOrderStatus.REFUNDED:// 已退款
				prizeStatusDesc = "";
				break;
			case UserOrderStatus.BETSUCC:// 投注成功
				switch (prizeStatus) {
					case PrizeConstant.PRIZE_STATUS_WAITING:
						prizeStatusDesc = "等待开奖";
						break;
					case PrizeConstant.PRIZE_STATUS_NO:
						prizeStatusDesc = "未中奖";
						break;
					case PrizeConstant.PRIZE_STATUS_SMALL_PRIZE:
						prizeStatusDesc = "已中奖";
						break;
					case PrizeConstant.PRIZE_STATUS_BIG_PRIZE:
						prizeStatusDesc = "中大奖";
						break;
				}
		}
		return prizeStatusDesc;
	}
	
	@Autowired
	BetNumberDao betNumberDao;
	
	@Autowired
	StakeOrderDao stakeOrderDao;
	
	/**
	 * 描述：查询用户订单明细
	 */
	public OrderDetailDto getUserOrderDetail(OrderDetailDto orderDetailDto) {
	
		if (orderDetailDto.getPageNo() == null || orderDetailDto.getPageNo() < 1) {
			orderDetailDto.setPageNo(1);
		}
		if (orderDetailDto.getPageSize() == null || orderDetailDto.getPageSize() < 1 || orderDetailDto.getPageSize() > 20) {
			orderDetailDto.setPageSize(PAGE_SIZE);
		}
		String orderId = orderDetailDto.getOrderId();
		Validate.notNull(orderId);
		OrderInfo orderInfo = userOrderDao.getUserOrderByUserOrderId(orderDetailDto.getUserId(), orderId);
		Validate.notNull(orderInfo);
		AwardWithPeriod awardWithPeriod = awardCacheService.getAwardCache(orderInfo.getGameId(), orderInfo.getPeriodNo());
		if (awardWithPeriod != null && awardWithPeriod.getAward() != null) {
			orderDetailDto.setPrizeNumber(awardWithPeriod.getAward().getPrizeNumber());
		}
		Period period = periodCacheService.getPeriod(orderInfo.getGameId(), orderInfo.getPeriodNo());
		orderInfo.setDeadline(period.getStopPayEndTime());
		orderInfo.setGameDesc(this.getGameDesc(orderInfo.getGameId()));
		orderInfo.setOrderTypeDesc(this.getOrderTypeDesc(orderInfo.getOrderType()));
		orderInfo.setOrderStatusDesc(this.getOrderStatusDesc(orderInfo.getOrderStatus(), orderInfo, period));
		orderInfo.setPrizeStatusDesc(this.getPrizeStatusDesc(orderInfo.getOrderStatus(), orderInfo.getPrizeStatus()));
		orderInfo.setOfficialBonus(this.handleOfficeBonus(orderInfo, orderInfo.getOfficialBonus()));
		orderInfo.setSpiltFlag(UserOrderStatus.isHaveSplit(orderInfo.getOrderStatus()));
		orderDetailDto.setOrderInfo(orderInfo);
		if (log.isDebugEnabled()) {
			log.debug("orderInfo:" + orderInfo);
		}
		return orderDetailDto;
	}
	
	public boolean getOrderAddFlag(BetNumber betNumber) {
	
		if (betNumber.getLocalBetNumber().charAt(0) == '+') {
			return true;
		}
		return false;
	}
	
	public long calcBetNums(long userOrderAmount, boolean isAddBet) {
	
		return isAddBet ? userOrderAmount / 300 : userOrderAmount / 200;
	}
	
	/**
	 * 描述：根据订单查询所有的票号信息列表
	 * 
	 * @param orderDetailDto
	 * @return
	 */
	public OrderDetailDto findStakeBetListByOrderByPage(OrderDetailDto orderDetailDto) {
	
		if (orderDetailDto.getPageNo() == null || orderDetailDto.getPageNo() < 1) {
			orderDetailDto.setPageNo(1);
		}
		if (orderDetailDto.getPageSize() == null || orderDetailDto.getPageSize() < 1 || orderDetailDto.getPageSize() > 20) {
			orderDetailDto.setPageSize(PAGE_SIZE);
		}
		
		String orderId = orderDetailDto.getOrderId();
		Validate.notNull(orderId);
		OrderInfo orderInfo = userOrderDao.getUserOrderById(orderId);
		orderInfo.setSpiltFlag(UserOrderStatus.isHaveSplit(orderInfo.getOrderStatus()));
		orderDetailDto.setOrderInfo(orderInfo);
		Boolean spiltFlag = orderInfo.getSpiltFlag();
		orderDetailDto.setSplitFlag(spiltFlag);
		boolean isAddBet = false;
		long userOrderAmount = orderInfo.getUserOrderAmount();
		List<StakeBetInfoDto> stakeBetInfoDtos = new ArrayList<>();
		if (!spiltFlag) {
			// 未拆单，从原始下单数据中获取投注号
			RawBetNumber rawBetNumber = rawBetNumberDao.getById(orderId);
			List<BetNumber> betNumbers = rawBetNumber.uncompress();
			for (BetNumber betNumber : betNumbers) {
				StakeBetInfoDto stakeBetInfoDto = new StakeBetInfoDto(betNumber);
				betNumber.setUserOrderId(orderInfo.getUserOrderId());
				betNumber.setGameId(orderInfo.getGameId());
				betNumber.setPeriodNo(orderInfo.getPeriodNo());
				stakeBetInfoDtos.add(stakeBetInfoDto);
			}
			isAddBet = (betNumbers != null && betNumbers.size() > 0 && getOrderAddFlag(betNumbers.get(0)));
			// 数据量不大且集中 ，进行内存分页
			Integer pageNo = orderDetailDto.getPageNo();
			Integer pageSize = orderDetailDto.getPageSize();
			Integer totalNum = stakeBetInfoDtos.size();
			PageUtil<StakeBetInfoDto> pageUtil = new PageUtil<StakeBetInfoDto>(pageSize, stakeBetInfoDtos, pageNo);
			stakeBetInfoDtos = pageUtil.getCurrentPageData();
			orderDetailDto.setTotalNum(totalNum);
			orderDetailDto.setPageNo(pageUtil.getCurrentPage());
			orderDetailDto.setTotalPage(pageUtil.getPageCount());
			orderDetailDto.setStakeBetInfoDtoList(stakeBetInfoDtos);
			orderDetailDto.setTotalBetNums(calcBetNums(userOrderAmount, isAddBet));
		} else {
			Page<StakeOrder> page = new Page<>();
			page.setPageNo(orderDetailDto.getPageNo());
			page.setPageSize(orderDetailDto.getPageSize());
			page = stakeOrderDao.getByUserIdByPage(page, orderId);
			// 已出单，从已出票数据中聚合投注号
			List<StakeOrder> stakeOrders = page.getResult();
			Map<String,StakeBetInfoDto> stakeBetInfoMap = new HashMap<>();
			StringBuilder sb = new StringBuilder(" (''");
			for (StakeOrder stakeOrder : stakeOrders) {
				StakeBetInfoDto stakeBetInfoDto = new StakeBetInfoDto(stakeOrder);
				stakeBetInfoMap.put(stakeOrder.getStakeOrderId(), stakeBetInfoDto);
				stakeBetInfoDtos.add(stakeBetInfoDto);
				sb.append(",'" + stakeOrder.getStakeOrderId() + "'");
			}
			sb.append(") ");
			List<BetNumber> betNumbers = betNumberDao.getByStakeOrderIds(sb.toString(), orderId);
			for (BetNumber betNumber : betNumbers) {
				StakeBetInfoDto stakeBetInfoDto = stakeBetInfoMap.get(betNumber.getStakeOrderId());
				stakeBetInfoDto.getBetNumberList().add(betNumber);
			}
			isAddBet = (betNumbers != null && betNumbers.size() > 0 && getOrderAddFlag(betNumbers.get(0)));
			orderDetailDto.setPageNo(this.getCurrentPage(orderDetailDto.getPageNo(), page.getTotalPage()));
			orderDetailDto.setTotalNum(page.getTotalCount());
			orderDetailDto.setTotalPage(page.getTotalPage());
			orderDetailDto.setStakeBetInfoDtoList(stakeBetInfoDtos);
			orderDetailDto.setTotalBetNums(calcBetNums(userOrderAmount, isAddBet));
		}
		
		if (log.isDebugEnabled()) {
			log.debug("stakeBetInfoDtos.count:" + stakeBetInfoDtos.size() + ",orderDetailDto:" + orderDetailDto);
		}
		
		return orderDetailDto;
	}
	
	public void closeOrder(String orderId) {
	
		UserOrder userOrder = userOrderDao.getById(orderId);
		if (userOrder == null || UserOrderStatus.isClosed(userOrder)) {
			return;
		}
		userOrderDao.updateOrderStatus(orderId, UserOrderStatus.CLOSED);
	}
	
}
