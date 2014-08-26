package com.sogou.lottery.web.service.order.service;

import java.sql.Timestamp;
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
import com.sogou.lottery.base.pay.dto.PayResultDto;
import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.util.JsonUtil;
import com.sogou.lottery.base.vo.period.Period;
import com.sogou.lottery.cache.business.service.PeriodCacheService;
import com.sogou.lottery.dao.PeriodDao;
import com.sogou.lottery.web.service.AbstractSpringMockTest;
import com.sogou.lottery.web.service.passport.service.PassportServiceTest;
import com.sogou.lottery.web.service.pay.dto.PayDto;
import com.sogou.lottery.web.service.pay.service.PayService;
import com.sogou.lottery.web.service.user.service.UserService;

public class PayServiceTest extends AbstractSpringMockTest {
	
	@InjectMocks
	@Autowired
	private PayService payService;
	@Mock
	private PeriodCacheService periodCacheService;
	@Mock
	private PeriodDao periodDao;
	@Mock
	private UserService userService;
	
	@Before
	public void myBefore() throws Exception {
	
		PayService bas = (PayService) unwrapProxy(payService);
		ReflectionTestUtils.setField(bas, "periodCacheService", periodCacheService);
		ReflectionTestUtils.setField(bas, "periodDao", periodDao);
		// ReflectionTestUtils.setField(bas, "userService", userService);
	}
	
	@Test
	@Rollback(false)
	public void payServiceTest() {
	
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
		
		String payOrderId = "14032606PO0000000086";
		
		// 模拟支付失败，密码输入错误
		PayResultDto payRes = new PayResultDto();
		payRes.setCashAmount(1000L);
		// payRes.setLocalStatus();
		payRes.setPayAccountId(PassportServiceTest.testPass1);
		payRes.setPayOrderId(payOrderId);
		payRes.setPaySystemId("PEAK" + payOrderId);
		payRes.setPaySystemTime(new Timestamp(System.currentTimeMillis()));
		// ResultDto<PayResultDto> payResult = new ResultDto<>(payRes);
		ResultUserDto<PayResultDto> payResult = new ResultUserDto<>(UserErrorCode.PayPwd.getCode(), UserErrorCode.PayPwd.getDesc());
		
		Mockito.when(userService.doBalancePay((PayDto) Mockito.any())).thenReturn(payResult);
		
		PayDto dto = new PayDto();
		dto.setPayOrderId(payOrderId);
		dto.setPayPwd(PassportServiceTest.testPayPwd2);
		dto.setUserIp("127.0.0.1");
		
		ResultDto<PayResultDto> result = payService.payOrder(dto);
		String res = JsonUtil.toJson(result);
		System.out.println(res);
	}
}
