package com.sogou.lottery.web.service.order.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.sogou.lottery.base.dto.ResultDto;
import com.sogou.lottery.base.order.dto.BetResultDto;
import com.sogou.lottery.base.order.dto.UserBetDto;
import com.sogou.lottery.base.pay.dto.PayResultDto;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.util.JsonUtil;
import com.sogou.lottery.base.vo.order.PayOrder;
import com.sogou.lottery.base.vo.order.RawBetNumber;
import com.sogou.lottery.base.vo.order.UserOrder;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.base.vo.user.User;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.dao.PayOrderDao;
import com.sogou.lottery.dao.PeriodDao;
import com.sogou.lottery.dao.RawBetNumberDao;
import com.sogou.lottery.dao.UserOrderDao;
import com.sogou.lottery.dao.user.UserDao;
import com.sogou.lottery.web.service.AbstractSpringMockTest;
import com.sogou.lottery.web.service.passport.service.PassportServiceTest;
import com.sogou.lottery.web.service.pay.dto.PayDto;
import com.sogou.lottery.web.service.pay.service.PayService;
import com.sogou.lottery.web.service.user.dto.BalanceDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.service.UserService;

public class PayIntegrationTest extends AbstractSpringMockTest {
	
	@InjectMocks
	@Autowired
	private PayService payService;
	@Mock
	private PeriodCacheService periodCacheService;
	@Mock
	private PeriodDao periodDao;
	@Mock
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserOrderDao userOrderDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private RawBetNumberDao rawBetNumberDao;
	@Autowired
	private UserDao userDao;
	
	private String payerUserId = "caipiaotest@sohu.com";
	
	@Before
	public void myBefore() throws Exception {
	
		PayService bas = (PayService) unwrapProxy(payService);
		ReflectionTestUtils.setField(bas, "periodCacheService", periodCacheService);
		ReflectionTestUtils.setField(bas, "periodDao", periodDao);
		ReflectionTestUtils.setField(bas, "userService", userService);
		
		periodCacheService = Mockito.mock(PeriodCacheService.class);
		
		OrderService bass = (OrderService) unwrapProxy(orderService);
		ReflectionTestUtils.setField(bass, "periodCacheService", periodCacheService);
		ReflectionTestUtils.setField(bass, "periodDao", periodDao);
		ReflectionTestUtils.setField(bass, "userService", userService);
		
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
		
		// ReflectionTestUtils.setField(bas, "userService", userService);
	}
	
	@Test
	// @Rollback(false)
	public void payServiceTest() {
	
		// 提交一个用户订单
		super.executeSqlScript("classpath:sql/setup/before_order.sql", false);
		
		// 下面才真正开始数据的测试
		String rawBetNumberString = "01 02 03 04 05 06:01";// 一注双色球的订单
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass2);
		bet.setGameId("ssq");
		bet.setPeriodNo("140329066");
		bet.setPrice(200L);
		bet.setRawBetNumbers(rawBetNumberString);
		bet.setSourceType(1);
		ResultDto<BetResultDto> result1 = orderService.addOrder(bet);
		
		// 先验证一把数据，保证数据的初始化正确，再测试支付
		List<UserOrder> userOrders = userOrderDao.getByPayerUserId(payerUserId);
		UserOrder userOrder = userOrders.get(0);
		String businessId = userOrder.getUserOrderId();
		PayOrder payOrder = payOrderDao.getByBusinessId(businessId);
		RawBetNumber rawBetNumber = rawBetNumberDao.getById(businessId);
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(1, (int)userOrder.getBetTimes());
		assertEquals("ssq", userOrder.getGameId());
		assertEquals(0, (int) userOrder.getOrderStatus());
		assertEquals(10, (int) userOrder.getOrderType());
		// assertEquals(200, (int)userOrder.getUserOrderAmount());
		// assertEquals(1, (int)userOrder.getTotalBetNumbers()); //这里有个bug
		assertEquals(0, (int) userOrder.getSuccBetNumbers());
		assertEquals(0, (int) userOrder.getFailedBetNumbers());
		
		// payorder
		assertEquals(0, (int) payOrder.getStatus());
		assertEquals(200, (int) payOrder.getPayAmount());
		assertEquals(200, (int) payOrder.getCashAmount());
		assertEquals(0, (int) payOrder.getRefundAmount());
		assertEquals(10, (int) payOrder.getBusinessType());
		
		// tb_raw_bet_numbers
		// assertEquals("01 02 03 04 05 06:01&0&1&1&single&&200",
		// rawBetNumber.getRawNumbersSmall());
		assertEquals(0, (int) rawBetNumber.getStatus());
		
		User user = userDao.getByUserId(bet.getUserId());
		
		// ///////////////////////////////////////////////////////////////////////////////////
		
		Period period = new Period();
		period.setGameId("k3js");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		// Mockito.when(periodCacheService.getPeriod(Mockito.anyString(),
		// Mockito.anyString())).thenReturn(period);
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		String payOrderId = result1.getResult().getPayOrderId();
		// 模拟支付成功的情况
		PayResultDto payRes = new PayResultDto();
		payRes.setCashAmount(1000L);
		// payRes.setLocalStatus();
		payRes.setPayAccountId(PassportServiceTest.testPass2);
		payRes.setPayOrderId(payOrderId);
		payRes.setPaySystemId("PEAK" + payOrderId);
		payRes.setPaySystemTime(new Timestamp(System.currentTimeMillis()));
		ResultUserDto<PayResultDto> payResult = new ResultUserDto<>(payRes);
		// 错误的模拟
		// ResultDto<PayResultDto> payResult = new
		// ResultDto<>(QianBaoErrorCode.PayPwd.getCode(),
		// QianBaoErrorCode.PayPwd.getDesc());
		Mockito.when(userService.doBalancePay((PayDto) Mockito.any())).thenReturn(payResult);
		
		PayDto dto = new PayDto();
		dto.setPayOrderId(payOrderId);
		dto.setPayPwd(PassportServiceTest.testPayPwd2);
		dto.setUserIp("127.0.0.1");
		
		ResultDto<PayResultDto> result = payService.payOrder(dto);
		String res = JsonUtil.toJson(result);
		System.out.println(res);
		
		// result
		assertEquals(0, result.getRetcode());
		// assertEquals("操作", result.getRetdesc());
		// 支付完成，验一般db
		userOrder = userOrderDao.getByPayerUserId(payerUserId).get(0);
		businessId = userOrder.getUserOrderId();
		payOrder = payOrderDao.getByBusinessId(businessId);
		rawBetNumber = rawBetNumberDao.getById(businessId);
		// user表
		assertEquals(1, (int) userOrder.getSourceType());
		// assertEquals(1, (int)userOrder.getBetTimes());
		assertEquals("ssq", userOrder.getGameId());
		assertEquals(1, (int) userOrder.getOrderStatus());
		assertEquals(user.getTrueName(), userOrder.getBettorName());
		assertEquals(user.getIdCardNo(), userOrder.getBettorIdno());
		
		// payorder
		assertEquals(2, (int) payOrder.getStatus());
		assertNotNull(payOrder.getPaySystemId());
		assertNotNull(payOrder.getPaySystemTime());
		
		// raw_bet_numbers
		assertEquals(0, (int) rawBetNumber.getStatus());
	}
}
