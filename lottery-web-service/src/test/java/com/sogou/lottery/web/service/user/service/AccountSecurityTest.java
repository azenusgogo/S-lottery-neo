package com.sogou.lottery.web.service.user.service;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.web.service.user.dto.ChangePwdDto;
import com.sogou.lottery.web.service.user.dto.ChangeSafeDto;
import com.sogou.lottery.web.service.user.dto.CommonDto;
import com.sogou.lottery.web.service.user.dto.FindPwdByMobileDto;
import com.sogou.lottery.web.service.user.dto.FindPwdBySafeDto;
import com.sogou.lottery.web.service.user.dto.SafeResultDto;
import com.sogou.lottery.web.service.user.dto.UserDto;

/**
 * 描述：账户安全相关测试
 * 
 * @author haojiaqi
 */
@ContextConfiguration(locations = { "classpath:/applicationContext.xml" })
@TransactionConfiguration(defaultRollback = false)
public class AccountSecurityTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	private static Log log = LogFactory.getLog(AccountSecurityTest.class);
	public static String bankId = "ALI";
	
	@Autowired
	UserService userService;
	private String captcha = "458621";
	
	@Before
	public void init() {
	
	}
	
	/**
	 * 描述：查询安全问题
	 */
	@Test
	// OK OK
	public void querySafeQuestionTest() {
	
		// 2.16
		UserDto dto = new UserDto();
		dto.setUserId(UserInfoServiceTest.userId);
		ResultUserDto<SafeResultDto> result = userService.querySafeQuestion(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
		log.info(result);
	}
	
	/**
	 * 描述：输入支付密码修改支付密码
	 */
	@Test
	// OK OK
	public void queryChangePwdTest() {
	
		// 2.16
		ChangePwdDto dto = new ChangePwdDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setPayPwd(UserInfoServiceTest.payPwd);
		dto.setNewPayPwd(UserInfoServiceTest.newPayPwd);
		dto.setNewPayPwdConfirm(UserInfoServiceTest.newPayPwd);
		ResultUserDto<CommonDto> result = userService.changePayPwd(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
		log.info(result);
		// this.restorePwdTest();
		
	}
	
	/**
	 * 描述：通过已修改支付密码恢复支付密码 验证码能用多次
	 */
	@Test
	public void restorePwdTest() {
	
		ChangePwdDto dto = new ChangePwdDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setPayPwd(UserInfoServiceTest.newPayPwd);
		dto.setNewPayPwd(UserInfoServiceTest.payPwd);
		dto.setNewPayPwdConfirm(UserInfoServiceTest.payPwd);
		ResultUserDto<CommonDto> result = userService.changePayPwd(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
	}
	
	/**
	 * 通过安全问题修改支付密码
	 */
	@Test
	// OK 有问题
	public void findPwdByQuestionTest() {
	
		FindPwdBySafeDto dto = new FindPwdBySafeDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setSafeAnswer(UserInfoServiceTest.safeAnswer);
		dto.setNewPassowrd(UserInfoServiceTest.newPayPwd);
		dto.setCaptcha(UserInfoServiceTest.captcha);
		ResultUserDto<CommonDto> result = userService.findPwdByQuestion(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
		log.debug(result);
		this.restorePwdByQuestionTest();
	}
	
	/**
	 * 通过安全问题恢复支付密码 验证码能用多次
	 */
	@Test
	// OK 有问题
	public void restorePwdByQuestionTest() {
	
		FindPwdBySafeDto dto = new FindPwdBySafeDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setSafeAnswer(UserInfoServiceTest.safeAnswer);
		dto.setNewPassowrd(UserInfoServiceTest.payPwd);
		dto.setCaptcha(UserInfoServiceTest.captcha);
		ResultUserDto<CommonDto> result = userService.findPwdByQuestion(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
		log.debug(result);
	}
	
	/**
	 * 描述：通过身份信息修改支付密码
	 */
	@Test
	// OK OK
	public void findPwdByInfoTest() {
	
		FindPwdByMobileDto dto = new FindPwdByMobileDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setTrueName(UserInfoServiceTest.trueName);
		dto.setIdCardNo(UserInfoServiceTest.idCardNo);
		dto.setIdCardType(UserInfoServiceTest.idCardType);
		dto.setCaptcha(UserInfoServiceTest.captcha);
		dto.setNewPassowrd(UserInfoServiceTest.newPayPwd);
		ResultUserDto<CommonDto> result = userService.findPwdByInfo(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
		log.debug(result);
		this.restorePwdByInfoTest();
	}
	
	/**
	 * 描述：通过身份信息修改支付密码 验证码只能用一次
	 */
	@Test
	// OK OK
	public void restorePwdByInfoTest() {
	
		FindPwdByMobileDto dto = new FindPwdByMobileDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setTrueName(UserInfoServiceTest.trueName);
		dto.setIdCardNo(UserInfoServiceTest.idCardNo);
		dto.setIdCardType(UserInfoServiceTest.idCardType);
		dto.setCaptcha(UserInfoServiceTest.captcha);
		dto.setNewPassowrd(UserInfoServiceTest.payPwd);
		ResultUserDto<CommonDto> result = userService.findPwdByInfo(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
		log.debug(result);
	}
	
	/**
	 * 描述：修改安全问题 TODO 安全问题ID
	 */
	@Test
	// OK OK
	public void changeSafeQuestionTest() {
	
		ChangeSafeDto dto = new ChangeSafeDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setSafeAnswer(UserInfoServiceTest.safeAnswer);
		dto.setNewQuestion(UserInfoServiceTest.newQuestionType);
		dto.setNewAnswer(UserInfoServiceTest.newSafeAnswer);
		dto.setPayPwd(UserInfoServiceTest.payPwd);
		ResultUserDto<CommonDto> result = userService.changeSafe(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
		log.debug(result);
		this.restoreSafeQuestionTest();
	}
	
	/**
	 * 描述：恢复安全问题
	 */
	@Test
	// OK OK
	public void restoreSafeQuestionTest() {
	
		ChangeSafeDto dto = new ChangeSafeDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setSafeAnswer(UserInfoServiceTest.newSafeAnswer);
		dto.setNewQuestion(UserInfoServiceTest.questionType);
		dto.setNewAnswer(UserInfoServiceTest.safeAnswer);
		dto.setPayPwd(UserInfoServiceTest.payPwd);
		ResultUserDto<CommonDto> result = userService.changeSafe(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
		log.debug(result);
	}
	
}
