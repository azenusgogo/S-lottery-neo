package com.sogou.lottery.web.service.passport.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sogou.lottery.base.passport.PassportErrorCode;
import com.sogou.lottery.base.passport.dto.ResultPassportDto;
import com.sogou.lottery.base.util.MD5Util;
import com.sogou.lottery.web.service.passport.dto.PassportDto;
import com.sogou.lottery.web.service.passport.dto.PassportResultDto.PassportData;

public class PassportServiceTest {
	
	private PassportService passportService;
	private PassportDto passport;
	public final static String testPass1 = "sgcaipiao@sogou.com";
	public final static String testPassPwd1 = MD5Util.getMD5("hello123");
	public final static String testPayPwd1 = MD5Util.getMD5("hello1234");
	public final static String testPayNewPwd1 = MD5Util.getMD5("hello12345");
	public final static String testPass2 = "caipiaotest@sohu.com";
	public final static String testPassPwd2 = MD5Util.getMD5("caipiaotest123");
	public final static String testPayPwd2 = MD5Util.getMD5("caipiaotest1234");
	public final static String testPayNewPwd2 = MD5Util.getMD5("caipiaotest1235");
	
	@Before
	public void prepare() {
	
		passportService = new PassportService();
		passport = new PassportDto();
		
	}
	
	@Test
	public void testtest() {
	
		System.out.println(testPass1);
		System.out.println(testPassPwd1);
		System.out.println(testPayPwd1);
		System.out.println(testPayNewPwd1);
		System.out.println(testPass2);
		System.out.println(testPassPwd2);
		System.out.println(testPayPwd2);
		System.out.println(testPayNewPwd2);
	}
	
	@Test
	public void authSogouTest() {
	
		passport.setUserId(testPass1);
		passport.setPassword(testPassPwd1);
		ResultPassportDto<PassportData> result = passportService.authUser(passport);
		Assert.assertEquals(result.getRetcode(), 0);
	}
	
	@Test
	public void authSohuTest() {
	
		passport.setUserId(testPass2);
		passport.setPassword(testPassPwd2);
		ResultPassportDto<PassportData> result = passportService.authUser(passport);
		Assert.assertEquals(result.getRetcode(), 0);
	}
	
	@Test
	public void authSohuTestError() {
	
		passport.setUserId(testPass2);
		passport.setPassword(testPassPwd2 + "x");
		ResultPassportDto<PassportData> result = passportService.authUser(passport);
		Assert.assertEquals(result.getRetcode(), (int) PassportErrorCode.AuthPwd.getCode());
	}
	
	@Test
	public void registerSogouTest() {
	
		passport.setUserId("huangt820@sogou.com");
		passport.setPassword("hello123");
		passport.setUserIp("127.0.0.1");
		ResultPassportDto<PassportData> result = passportService.registerUser(passport);
		System.out.println(result);
		// Assert.assertEquals(result.getRetcode(), 0);
	}
	
	@Test
	public void checkSogouTest() {
	
		passport.setUserId("huangt820@sogou.com");
		ResultPassportDto<PassportData> result = passportService.checkUser(passport);
		System.out.println(result);
		// Assert.assertEquals(result.getRetcode(), 0);
	}
}
