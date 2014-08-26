package com.sogou.lottery.web.service.order.service;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;

import com.sogou.lottery.base.dto.ResultDto;
import com.sogou.lottery.base.order.dto.BetResultDto;
import com.sogou.lottery.base.order.dto.UserBetDto;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.util.JsonUtil;
import com.sogou.lottery.base.vo.order.PayOrder;
import com.sogou.lottery.base.vo.order.RawBetNumber;
import com.sogou.lottery.base.vo.order.UserOrder;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.dao.PayOrderDao;
import com.sogou.lottery.dao.PeriodDao;
import com.sogou.lottery.dao.RawBetNumberDao;
import com.sogou.lottery.dao.UserOrderDao;
import com.sogou.lottery.web.service.AbstractSpringMockTest;
import com.sogou.lottery.web.service.passport.service.PassportServiceTest;
import com.sogou.lottery.web.service.user.dto.BalanceDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.service.UserService;

public class OrderIntegrationTest extends AbstractSpringMockTest {
	
	@InjectMocks
	@Autowired
	private OrderService orderService;
	@Mock
	private PeriodCacheService periodCacheService;
	@Mock
	private PeriodDao periodDao;
	@Mock
	private UserService userService;
	
	@Autowired
	private UserOrderDao userOrderDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private RawBetNumberDao rawBetNumberDao;
	
	private String payerUserId = "caipiaotest@sohu.com";
	
	@Before
	public void myBefore() throws Exception {
	
		super.executeSqlScript("classpath:sql/setup/before_order.sql", false);
		periodCacheService = Mockito.mock(PeriodCacheService.class);
		periodDao = Mockito.mock(PeriodDao.class);
		userService = Mockito.mock(UserService.class);
		OrderService bas = (OrderService) unwrapProxy(orderService);
		ReflectionTestUtils.setField(bas, "periodCacheService", periodCacheService);
		ReflectionTestUtils.setField(bas, "periodDao", periodDao);
		ReflectionTestUtils.setField(bas, "userService", userService);
	}
	
	/*
	 * 测试常规的用户提交的订单，检查是否正常入库，是否正常返回给前端 测试简单的只有一注双色球
	 */
	@Test
	@Rollback(false)
	public void addSsqOneSingle() {
	
		Period period = new Period();
		period.setGameId("ssq");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06:01";// 一注双色球的订单
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(1, (int)userOrder.getBetTimes());
		assertEquals("ssq", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(200, (int) payOrder.getPayAmount());
		assertEquals(200, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06:01&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，应该是200
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试常规的用户提交的订单，检查是否正常入库，是否正常返回给前端 测试简单的2注双色球
	 */
	@Test
	@Rollback(false)
	public void addSsqTwoSingle() {
	
		Period period = new Period();
		period.setGameId("ssq");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06:01;01 02 03 04 05 06:01";// 一注双色球的订单
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(1, (int)userOrder.getBetTimes());
		assertEquals("ssq", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(400, (int) payOrder.getPayAmount());
		assertEquals(400, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06:01&0&1&&1&single&&200;01 02 03 04 05 06:01&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall());
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试常规的用户提交的订单，检查是否正常入库，是否正常返回给前端 测试简单的只有一注复式双色球
	 */
	@Test
	@Rollback(false)
	public void addSsqMultiple() {
	
		Period period = new Period();
		period.setGameId("ssq");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06:01 02";// 一注双色球的订单
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(1, (int)userOrder.getBetTimes());
		assertEquals("ssq", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(400, (int) payOrder.getPayAmount());
		assertEquals(400, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06:01 02&0&2&&1&multiple&&400", rawBetNumber.getRawNumbersSmall());
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试常规的用户提交的订单，检查是否正常入库，是否正常返回给前端 测试简单的只有一注复式双色球100倍
	 */
	@Test
	@Rollback(false)
	public void addSsqOneMultiple100Times() {
	
		Period period = new Period();
		period.setGameId("ssq");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06:01 02";// 一注双色球的订单
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setPrice(200L);
		bet.setBetTimes(100L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(1, (int)userOrder.getBetTimes());
		assertEquals("ssq", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(40000, (int) payOrder.getPayAmount());
		assertEquals(40000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06:01 02&0&2&&100&multiple&&400", rawBetNumber.getRawNumbersSmall()); // 这里需要再确认下
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试常规的用户提交的订单，检查是否正常入库，是否正常返回给前端 测试订单金额超过钱包剩余金额 预期：订单提交失败
	 */
	@Test
	@Rollback(false)
	public void addSsqAmountMoreThan() {
	
		Period period = new Period();
		period.setGameId("ssq");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有100L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(100L);
		balance.setAvailableWithDrawAmount(100L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06:01";// 一注双色球的订单
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(4000006, result.getRetcode());
		assertEquals("余额不足", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		// assertEquals(1, (int)userOrder.getBetTimes());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(200, (int) payOrder.getPayAmount());
		assertEquals(200, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06:01&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall());
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试用户的订单金额超过订单金额的上限990000 预期：返回失败,db为空
	 */
	@Test
	@Rollback(false)
	public void addSsqOrderAmountExceedsTopAmount() {
	
		Period period = new Period();
		period.setGameId("ssq");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06 07 08:01";// 一注双色球的订单
																	// 够10000注了
																	// 钱够了
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(200L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		List<UserOrder> userOrder = userOrderDao.getByPayerUserId(payerUserId);
		assertEquals(0, userOrder.size());
		
		assertEquals(4000001, result.getRetcode());
		assertEquals("支付金额超过上限9900.00元", result.getRetdesc());
		System.out.println(result);
	}
	
	/*
	 * 双色球胆拖投注方式 一注简单的胆拖投注，01 02 03$04 05 06 07:01 预期：彩票注数为4注
	 */
	@Test
	@Rollback(false)
	public void addSsqDantuo() {
	
		Period period = new Period();
		period.setGameId("ssq");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03$04 05 06 07:01";// 一注双色球的订单
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		// assertEquals(1, (int)userOrder.getBetTimes());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(800, (int) payOrder.getPayAmount());
		assertEquals(800, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03$04 05 06 07:01&0&4&&1&dantuo&&800", rawBetNumber.getRawNumbersSmall());
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试投注号码的格式，错误情况下不予入库 一注简单的胆拖投注，胆码和托码中有一个号码相同，01 02 03$01 05 06 07:01
	 * 预期：不予入库
	 */
	@Test
	@Rollback(false)
	public void addSsqDantuoOneSame() {
	
		Period period = new Period();
		period.setGameId("ssq");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03$01 05 06 07:01";// 一注双色球的订单
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		List<UserOrder> userOrder = userOrderDao.getByPayerUserId(payerUserId);
		assertEquals(0, userOrder.size());
		
		assertEquals(4000002, result.getRetcode());
		assertEquals("投注号码格式错误", result.getRetdesc());
		System.out.println(result);
	}
	
	/*
	 * 测试目的：胆拖投注中奖金的计算 测试一注胆拖投，投注10倍
	 */
	@Test
	@Rollback(false)
	public void addSsqDantuo10Times() {
	
		Period period = new Period();
		period.setGameId("ssq");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03$04 05 06 07 08 09 10:01";// 一注双色球的订单
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("ssq", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(70000, (int) payOrder.getPayAmount());
		assertEquals(70000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03$04 05 06 07 08 09 10:01&0&35&&10&dantuo&&7000", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	// 开始测试大乐透 dlt
	
	/*
	 * 测试目的：测试大乐透一注普通投注的投注金额计算 测试一注普通大乐透 明天你是否依然爱我-谭咏麟
	 */
	@Test
	@Rollback(false)
	public void addDltSingle() {
	
		Period period = new Period();
		period.setGameId("dlt");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05:01 02";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("dlt");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("dlt", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(200, (int) payOrder.getPayAmount());
		assertEquals(200, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05:01 02&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试大乐透一注普通投注的投注金额计算 测试一注普通大乐透 谁能告诉我，有没有这样的笔，能画出一双双不流泪的眼睛；
	 */
	@Test
	@Rollback(false)
	public void addDltTwoSingles() {
	
		Period period = new Period();
		period.setGameId("dlt");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05:01 02;01 02 03 04 05:01 02"; // 分隔号貌似变成;了
																					// OK
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("dlt");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("dlt", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(400, (int) payOrder.getPayAmount());
		assertEquals(400, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05:01 02&0&1&&1&single&&200;01 02 03 04 05:01 02&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试大乐透一注普通投注的投注金额计算 测试一注复式倍投 6个红球，4个篮球 投注10倍 人生何处不相逢-陈慧娴
	 */
	@Test
	@Rollback(false)
	public void addDltMultiple10Times() {
	
		Period period = new Period();
		period.setGameId("dlt");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06:01 02 04 05";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("dlt");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("dlt", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(72000, (int) payOrder.getPayAmount());
		assertEquals(72000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06:01 02 04 05&0&36&&10&multiple&&7200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试大乐透胆拖投注倍投的奖金计算 测试一注胆拖投注 01 02 03$04 05 06 07 08:01$02 03 04
	 * 依稀往梦似曾见-甄妮罗文
	 */
	@Test
	@Rollback(false)
	public void addDltDantuo10Times() {
	
		Period period = new Period();
		period.setGameId("dlt");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03$04 05 06 07 08:01$02 03 04";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("dlt");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("dlt", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(60000, (int) payOrder.getPayAmount());
		assertEquals(60000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03$04 05 06 07 08:01$02 03 04&0&30&&10&dantuo&&6000", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	// 开始测试七星彩 qxc
	/*
	 * 测试目的：测试七星彩一注不普通的投注的金额计算 测试一注普通七星彩 0 1 2 3 4 5 6
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addQxcSingle() {
	
		Period period = new Period();
		period.setGameId("qxc");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "0 1 2 3 4 5 6";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("qxc");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("qxc", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(200, (int) payOrder.getPayAmount());
		assertEquals(200, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("0 1 2 3 4 5 6&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试七星彩一注不普通的投注的金额计算 测试一注普通七星彩 0 1 2 3 4 5 6 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addQxcTwoSingles() {
	
		Period period = new Period();
		period.setGameId("qxc");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "0 1 2 3 4 5 6;0 1 2 3 4 5 6";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("qxc");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("qxc", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(400, (int) payOrder.getPayAmount());
		assertEquals(400, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("0 1 2 3 4 5 6&0&1&&1&single&&200;0 1 2 3 4 5 6&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试七星彩一注复式的金额计算 测试一注普通七星彩 012 12 234 34 456 12 012
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addQxcMultiple() {
	
		Period period = new Period();
		period.setGameId("qxc");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "012 12 234 34 4 1 012";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("qxc");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("qxc", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(21600, (int) payOrder.getPayAmount());
		assertEquals(21600, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("012 12 234 34 4 1 012&0&108&&1&multiple&&21600", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试七星彩一注复式彩票的倍投 10倍 测试一注普通七星彩 012 12 234 34 456 12 012 10倍
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addQxcMultiple10Times() {
	
		Period period = new Period();
		period.setGameId("qxc");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "012 12 234 34 4 1 012";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("qxc");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("qxc", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(216000, (int) payOrder.getPayAmount());
		assertEquals(216000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("012 12 234 34 4 1 012&0&108&&10&multiple&&21600", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	// 开始测试七乐彩 qlc
	
	/*
	 * 测试目的：测试七乐彩一注不普通的投注的金额计算 测试一注普通七乐彩 0 1 2 3 4 5 6
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addQlcSingle() {
	
		Period period = new Period();
		period.setGameId("qlc");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06 07";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("qlc");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("qlc", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(200, (int) payOrder.getPayAmount());
		assertEquals(200, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06 07&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试七乐彩一注不普通的投注的金额计算 测试一注普通七乐彩 0 1 2 3 4 5 6 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addQlcTwoSingles() {
	
		Period period = new Period();
		period.setGameId("qlc");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06 07;01 02 03 04 05 06 07";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("qlc");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("qlc", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(400, (int) payOrder.getPayAmount());
		assertEquals(400, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06 07&0&1&&1&single&&200;01 02 03 04 05 06 07&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试七乐彩一注复式的金额计算 测试一注普通七乐彩 01 02 03 04 05 06 07 08 09 10
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addQlcMultiple() {
	
		Period period = new Period();
		period.setGameId("qlc");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06 07 08 09 10";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("qlc");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("qlc", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(24000, (int) payOrder.getPayAmount());
		assertEquals(24000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06 07 08 09 10&0&120&&1&multiple&&24000", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试七乐彩一注复式彩票的倍投 10倍 测试一注普通七乐彩 01 02 03 04 05 06 07 08 09 10 11 10倍
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addQlcMultiple10Times() {
	
		Period period = new Period();
		period.setGameId("qlc");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06 07 08 09 10 11";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("qlc");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("qlc", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(660000, (int) payOrder.getPayAmount());
		assertEquals(660000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04 05 06 07 08 09 10 11&0&330&&10&multiple&&66000", rawBetNumber.getRawNumbersSmall());
	}
	
	/*
	 * 测试目的：测试七乐彩胆拖投注倍投 10倍 测试一注胆拖投注的七乐彩 01 02 03 04$05 06 07 08 09 10 11
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addQlcDantuo10Times() {
	
		Period period = new Period();
		period.setGameId("qlc");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04$05 06 07 08 09 10 11";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("qlc");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("qlc", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(70000, (int) payOrder.getPayAmount());
		assertEquals(70000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("01 02 03 04$05 06 07 08 09 10 11&0&35&&10&dantuo&&7000", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	// 七乐彩结束了，开始走f14投注
	/*
	 * 测试目的：测试f14一注不普通的投注的金额计算 测试一注普通f14 3 3 3 3 1 1 1 1 0 0 0 0 0 0
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addF14Single() {
	
		Period period = new Period();
		period.setGameId("f14");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3 3 3 3 1 1 1 1 0 0 0 0 0 0";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("f14");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("f14", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(200, (int) payOrder.getPayAmount());
		assertEquals(200, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3 3 3 3 1 1 1 1 0 0 0 0 0 0&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试f14两注普通的投注的金额计算 测试两注普通f14 3 3 3 3 1 1 1 1 0 0 0 0 0 0
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addF14TwoSingles() {
	
		Period period = new Period();
		period.setGameId("f14");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3 3 3 3 1 1 1 1 0 0 0 0 0 0;3 3 3 3 1 1 1 1 0 0 0 0 0 0";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("f14");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("f14", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(400, (int) payOrder.getPayAmount());
		assertEquals(400, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3 3 3 3 1 1 1 1 0 0 0 0 0 0&0&1&&1&single&&200;3 3 3 3 1 1 1 1 0 0 0 0 0 0&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	// 写到这里了，明儿继续吧，加油！！！！
	/*
	 * 测试目的：测试七乐彩一注复式的金额计算 测试一注普通f14 3 30 30 30 130 103 1 1 0 0 0 0 0 0
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addF14Multiple() {
	
		Period period = new Period();
		period.setGameId("f14");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3 30 30 30 130 103 1 1 0 0 0 0 0 0";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("f14");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("f14", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(14400, (int) payOrder.getPayAmount());
		assertEquals(14400, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3 30 30 30 310 310 1 1 0 0 0 0 0 0&0&72&&1&multiple&&14400", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试f14一注复式彩票的倍投 10倍 测试一注普通f14 3 30 30 30 132 103 1 1 0 0 0 0 0 0 10倍
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addF14Multiple10Times() {
	
		Period period = new Period();
		period.setGameId("f14");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3 30 30 30 310 310 1 1 0 0 0 0 0 0";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("f14");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("f14", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(144000, (int) payOrder.getPayAmount());
		assertEquals(144000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3 30 30 30 310 310 1 1 0 0 0 0 0 0&0&72&&10&multiple&&14400", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	// f14结束了，开始走f9投注
	
	/*
	 * 测试目的：测试f9一注不普通的投注的金额计算 测试一注普通f9 3 3 3 1 1 1 0 0 0 - - - - -
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addF9Single() {
	
		Period period = new Period();
		period.setGameId("f9");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3 3 3 1 1 1 0 0 0 - - - - -";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("f9");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("f9", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(200, (int) payOrder.getPayAmount());
		assertEquals(200, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3 3 3 1 1 1 0 0 0 - - - - -&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall());
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试f9两注普通的投注的金额计算 测试两注普通f9 3 3 3 1 1 1 0 0 0 - - - - -;3 3 3 1 1 1 0
	 * 0 0 - - - - - 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addF9TwoSingles() {
	
		Period period = new Period();
		period.setGameId("f9");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3 3 3 1 1 1 0 0 0 - - - - -;3 3 3 1 1 1 0 0 0 - - - - -";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("f9");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("f9", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(400, (int) payOrder.getPayAmount());
		assertEquals(400, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3 3 3 1 1 1 0 0 0 - - - - -&0&1&&1&single&&200;3 3 3 1 1 1 0 0 0 - - - - -&0&1&&1&single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	// 写到这里了，你妹啊，明儿继续吧，加油！！！！
	/*
	 * 测试目的：测试七乐彩一注复式的金额计算 测试一注普通f9 301 310 30 1 1 1 0 0 0 - - - - -
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Ignore
	@Rollback(false)
	public void addF9Multiple() {
	
		Period period = new Period();
		period.setGameId("f9");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "301 310 30 1 1 1 0 0 0 301 - - - -";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("f9");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("f9", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(108000, (int) payOrder.getPayAmount()); // bug，奖金计算错误
		assertEquals(108000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("012 12 234 34 456 12 012&0&648&&1&multiple&&129600", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试f9一注复式彩票的倍投 10倍 测试一注普通f9 301 310 30 1 1 1 0 0 0 301 - - - - 10倍
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addF9Multiple10Times() {
	
		Period period = new Period();
		period.setGameId("f9");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "301 310 30 1 1 1 0 0 0 301 - - - -";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("f9");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("f9", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(810000, (int) payOrder.getPayAmount());
		assertEquals(810000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("310 310 30 1 1 1 0 0 0 310 - - - -&0&405&&10&multiple&&81000", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试f9胆拖投注倍投 10倍 测试一注胆拖投注的七乐彩 3$ 10 3 3$ 1 1 0 - - 0$ 1 1 1 1
	 * 这一种想见不敢见的伤痛，而我对你的思念越来越浓
	 */
	@Test
	@Rollback(false)
	public void addF9Dantuo10Times() {
	
		Period period = new Period();
		period.setGameId("f9");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3$ 10 3 3$ 1 1 0 - - 0$ 1 1 1 1";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("f9");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("f9", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(280000, (int) payOrder.getPayAmount());
		assertEquals(280000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3$ 10 3 3$ 1 1 0 - - 0$ 1 1 1 1&0&140&&10&multiple&&28000", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	// 开始测试k3投注订单
	
	/*
	 * 测试目的：测试k3 和值单式 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3HezhiSingle() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "HZ_18";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(200, (int) payOrder.getPayAmount());
		assertEquals(200, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("HZ_18&0&1&&1&hz-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 和值单式 两注 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3HezhiTwoSingles() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "HZ_18;HZ_17";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		// bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(400, (int) payOrder.getPayAmount());
		assertEquals(400, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("HZ_18&0&1&&1&hz-single&&200;HZ_17&0&1&&1&hz-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 和值复式,投注10倍 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3HezhiMultiple() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "HZ_3,6,9";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(6000, (int) payOrder.getPayAmount());
		assertEquals(6000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("HZ_3,6,9&0&3&&10&hz-multiple&&600", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 三同号通选 AAA_*，投注10倍 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3SanTongTongXuan() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "AAA_*";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(2000, (int) payOrder.getPayAmount());
		assertEquals(2000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("AAA_*&0&1&&10&3thtx-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 三同号单选 AAA_111，投注10倍 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3SanTongDanXuanSingle() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "AAA_111";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(2000, (int) payOrder.getPayAmount());
		assertEquals(2000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("AAA_111&0&1&&10&3thd-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 三同号单选 AAA_111;AAA_222 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3SanTongDanXuan2Singles() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "AAA_111;AAA_222";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(4000, (int) payOrder.getPayAmount());
		assertEquals(4000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("AAA_111&0&1&&10&3thd-single&&200;AAA_222&0&1&&10&3thd-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 三同号单选 AAA_111,222,333,444,投注10倍
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3SanTongDanXuanMultiple() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "AAA_111,222,333,444";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(8000, (int) payOrder.getPayAmount());
		assertEquals(8000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("AAA_111,222,333,444&0&4&&10&3thd-multiple&&800", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 三不同标准 两个单式， 3BT_123;3BT_123 投注10被
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3SanBuTongSingle() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3BT_123;3BT_123";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(4000, (int) payOrder.getPayAmount());
		assertEquals(4000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3BT_123&0&1&&10&3bt-single&&200;3BT_123&0&1&&10&3bt-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 三不同标准 两个复式，3BT_1234;3BT_12345 投注10被
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3SanBuTong2Multiples() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3BT_1234;3BT_12345";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(28000, (int) payOrder.getPayAmount());
		assertEquals(28000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3BT_1234&0&4&&10&3bt-multiple&&800;3BT_12345&0&10&&10&3bt-multiple&&2000", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 三不同胆拖 3BT_12$3456 投注10被 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3SanBuTongDanTuo() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3BT_12$3456";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(8000, (int) payOrder.getPayAmount());
		assertEquals(8000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3BT_12$3456&0&4&&10&3bt-dantuo&&800", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 三连号通选 3LH_* 投注10倍 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3SanLianHaoTongXuan() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "3LH_*";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(2000, (int) payOrder.getPayAmount());
		assertEquals(2000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("3LH_*&0&1&&10&3lh-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 二同号复选单式 AA_11;AA_22 投注10倍 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addKErTongHaoFuXuanSingle() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "AA_11;AA_22";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(4000, (int) payOrder.getPayAmount());
		assertEquals(4000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("AA_11&0&1&&10&2thf-single&&200;AA_22&0&1&&10&2thf-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 二同号复选复式 AA_11,22;AA_22,33 投注10倍
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addErTongHaoFuXuanMultiple() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "AA_11,22;AA_22,33";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(8000, (int) payOrder.getPayAmount());
		assertEquals(8000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("AA_11,22&0&2&&10&2thf-multiple&&400;AA_22,33&0&2&&10&2thf-multiple&&400", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 二同号单选单式 AAX_11|2;AAX_11|2 投注10倍
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3ErTongHaoDanXuanSingle() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "AAX_11|2;AAX_11|2";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(4000, (int) payOrder.getPayAmount());
		assertEquals(4000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("AAX_11|2&0&1&&10&2thd-single&&200;AAX_11|2&0&1&&10&2thd-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 二同号单选复式 AAX_11,22|3,4 投注10倍 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3ErTongHaoDanXuanMultiple() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "AAX_11,22|3,4";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(8000, (int) payOrder.getPayAmount());
		assertEquals(8000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("AAX_11,22|3,4&0&4&&10&2thd-multiple&&800", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 二不同标准单式2BT_12;2BT_12;2BT_12 投注10倍
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3ErBuTongSingle() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "2BT_12;2BT_12;2BT_12";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(6000, (int) payOrder.getPayAmount());
		assertEquals(6000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("2BT_12&0&1&&10&2bt-single&&200;2BT_12&0&1&&10&2bt-single&&200;2BT_12&0&1&&10&2bt-single&&200", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 二不同标准单式2BT_12;2BT_12;2BT_12 投注10倍
	 * 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3ErBuTongMultiple() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "2BT_123456";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(30000, (int) payOrder.getPayAmount());
		assertEquals(30000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("2BT_123456&0&15&&10&2bt-multiple&&3000", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
	/*
	 * 测试目的：测试k3 二不同胆拖2BT_1$23456 投注10倍 依然记得从你口中说出再见坚决如铁，昏暗中有种烈日灼身的错觉-黄昏
	 */
	@Test
	@Rollback(false)
	public void addK3ErBuTongDanTuo() {
	
		Period period = new Period();
		period.setGameId("k3gx");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		
		// mock period
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额 可用金额还有1000000000L
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass2);
		balance.setAvailableAmount(1000000000L);
		balance.setAvailableWithDrawAmount(1000000000L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		// ResultDto<BalanceResultDto> balanceDto = new
		// ResultDto<>(ErrorCode.AlreadyPay);
		
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		// 模拟用户的余额数据，有了这个，可以任意构造用户余额了
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "2BT_1$23456";
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("k3gx");
		bet.setPeriodNo("140329066");
		bet.setBetTimes(10L);
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		
		UserOrder userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		
		assertEquals(0, result.getRetcode());
		assertEquals("操作成功", result.getRetdesc());
		System.out.println(result);
		// 下面开始验证DB数据
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(10, (int)userOrder.getBetTimes());
		assertEquals("k3gx", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		assertEquals(0, (int) userOrder.getTotalBetNumbers());
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(10000, (int) payOrder.getPayAmount());
		assertEquals(10000, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		assertEquals("2BT_1$23456&0&5&&10&2bt-dantuo&&1000", rawBetNumber.getRawNumbersSmall()); // 这里有个bug，倍数和金额没有体现出来
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
	
}
