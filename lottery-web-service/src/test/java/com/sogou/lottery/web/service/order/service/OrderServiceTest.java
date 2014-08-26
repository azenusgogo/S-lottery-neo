package com.sogou.lottery.web.service.order.service;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
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
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.dao.PeriodDao;
import com.sogou.lottery.web.service.AbstractSpringMockTest;
import com.sogou.lottery.web.service.passport.service.PassportServiceTest;
import com.sogou.lottery.web.service.user.dto.BalanceDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.service.UserService;

public class OrderServiceTest extends AbstractSpringMockTest {
	
	@InjectMocks
	@Autowired
	private OrderService orderService;
	@Mock
	private PeriodCacheService periodCacheService;
	@Mock
	private PeriodDao periodDao;
	@Mock
	private UserService userService;
	
	@Before
	public void myBefore() throws Exception {
	
		OrderService bas = (OrderService) unwrapProxy(orderService);
		ReflectionTestUtils.setField(bas, "periodCacheService", periodCacheService);
		ReflectionTestUtils.setField(bas, "periodDao", periodDao);
		ReflectionTestUtils.setField(bas, "userService", userService);
	}
	
	@Test
	@Rollback(false)
	public void addOrderTest() {
	
		Period period = new Period();
		period.setGameId("k3js");
		period.setPeriodNo("140327065");
		Date now = DateUtil.getCurrentDate();
		period.setOffcialStartTime(DateUtil.getStarting(now, Calendar.DATE));
		period.setOffcialEndTime(DateUtil.getEnding(now, Calendar.DATE));
		// Mockito.when(periodCacheService.getPeriod(Mockito.anyString(),
		// Mockito.anyString())).thenReturn(period);
		Mockito.when(periodCacheService.getPeriod(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		Mockito.when(periodDao.getByGameIdAndPeriodNo(Mockito.anyString(), Mockito.anyString())).thenReturn(period);
		
		// 模拟查询余额
		BalanceDto balance = new BalanceDto();
		balance.setUserId(PassportServiceTest.testPass1);
		balance.setAvailableAmount(0L);
		balance.setAvailableWithDrawAmount(0L);
		balance.setFrozenAmount(0L);
		balance.setStatus(0);
		ResultUserDto<BalanceDto> balanceDto = new ResultUserDto<>(balance);
		
		Mockito.when(userService.queryBalance((UserDto) Mockito.any())).thenReturn(balanceDto);
		
		UserBetDto bet = new UserBetDto();
		bet.setUserId(PassportServiceTest.testPass1);
		bet.setGameId("k3js");
		bet.setPeriodNo("140329066");
		bet.setPrice(200L);
		bet.setRawBetNumbers("HZ_3");
		bet.setSourceType(1);
		ResultDto<BetResultDto> result = orderService.addOrder(bet);
		
		System.out.println(JsonUtil.toJson(result));
		// System.out.println(result);
	}
	
}
