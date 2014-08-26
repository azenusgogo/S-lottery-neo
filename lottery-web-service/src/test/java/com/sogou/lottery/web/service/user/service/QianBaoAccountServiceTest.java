package com.sogou.lottery.web.service.user.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sogou.lottery.dao.user.UserTempDao;
import com.sogou.lottery.web.service.qianbao.service.QianBaoAccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class QianBaoAccountServiceTest {
	
	@Autowired
	private QianBaoAccountService accountService;
	@Autowired
	private UserTempDao userTempDao;
	
	@Test
	public void test() {
	
		userTempDao.getByUserId("xxx");
	}
}
