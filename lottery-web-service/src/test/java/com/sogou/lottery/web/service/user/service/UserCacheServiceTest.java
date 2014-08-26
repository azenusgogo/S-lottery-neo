package com.sogou.lottery.web.service.user.service;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.user.UserLog;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserCacheServiceTest {
	
	@Autowired
	private UserCacheService userCacheService;
	
	@Test
	public void test() throws InterruptedException {
	
		String userId = "yyy";
		int type = 100;
		int times = userCacheService.getUserOperTimes(userId, type);
		Assert.assertEquals(0, times);
		
		UserLog userLog = new UserLog();
		userLog.setUserId(userId);
		userLog.setType(type);
		userLog.setNote("支付密码输入错误");
		userLog.setIp(userId);
		Date date = DateUtil.add(DateUtil.getCurrentDate(), 2, Calendar.SECOND);
		userCacheService.setUserOper(userLog, date);
		times = userCacheService.getUserOperTimes(userId, type);
		Assert.assertEquals(1, times);
		
		Thread.sleep(2002);
		
		times = userCacheService.getUserOperTimes(userId, type);
		Assert.assertEquals(0, times);
		
		date = DateUtil.add(DateUtil.getCurrentDate(), 2, Calendar.SECOND);
		// userCacheService.deleteUserOperTimes(userId, type);
		userCacheService.setUserOper(userLog, date);
		userCacheService.setUserOper(userLog, date);
		userCacheService.setUserOper(userLog, date);
		userCacheService.setUserOper(userLog, date);
		
		times = userCacheService.getUserOperTimes(userId, type);
		Assert.assertEquals(4, times);
		
		Thread.sleep(2002);
		
		times = userCacheService.getUserOperTimes(userId, type);
		Assert.assertEquals(0, times);
	}
}
