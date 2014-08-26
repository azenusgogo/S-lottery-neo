package com.sogou.lottery.web.service.user.service;

import junit.framework.Assert;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.sogou.lottery.base.passport.dto.ResultPassportDto;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.MD5Util;
import com.sogou.lottery.web.service.passport.dto.PassportDto;
import com.sogou.lottery.web.service.passport.dto.PassportResultDto;
import com.sogou.lottery.web.service.passport.service.PassportService;
import com.sogou.lottery.web.service.qianbao.service.QianBaoService;
import com.sogou.lottery.web.service.user.constant.IdCardType;
import com.sogou.lottery.web.service.user.dto.BindCardDto;
import com.sogou.lottery.web.service.user.dto.CommonDto;
import com.sogou.lottery.web.service.user.dto.InfoDto;
import com.sogou.lottery.web.service.user.dto.InfoResultDto;
import com.sogou.lottery.web.service.user.dto.UserDto;

/**
 * 描述：用户信息测试
 * 
 * @author haojiaqi
 */
// @ContextConfiguration(locations = {
// "classpath*:/spring-context.xml","classpath*:/spring-database.xml" })
// @RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext.xml" })
@TransactionConfiguration(defaultRollback = false)
public class UserInfoServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	private static Log log = LogFactory.getLog(UserInfoServiceTest.class);
	public static String userId = "wfones@sohu.com";
	public static String mobile = "18612693968";
	public static String payPwd = MD5Util.getMD5("hh654321");
	public static String newPayPwd = MD5Util.getMD5("hh654320");
	public static String pwd = MD5Util.getMD5("hh123456");
	public static String userIp = "10.129.192.189";
	public static String email = "406629974@qq.com";
	public static String idCardNo = "142327198903242277";
	public static String trueName = "李四";
	public static String withDrawBankId = "408";
	public static String rechargeBankId = "901";
	public static String bank = "招商银行";
	public static String bankCardNo = "6225880127281195";
	public static String safeAnswer = MD5Util.getMD5("张三");
	public static String newSafeAnswer = MD5Util.getMD5("程序员");
	public static String safeQuestion = MD5Util.getMD5("您爸爸的名字是什么？");
	public static String newSafeQuestion = MD5Util.getMD5("他的名字是什么？");
	public static int questionType = 1;
	public static int newQuestionType = 1;
	public static Integer idCardType = IdCardType.Id.getType();
	public static String captcha = "026734";
	
	@Autowired
	UserService userService;
	
	@Before
	public void init() {
	
	}


    /**
     * 测试用户domain信息
     */
    @Test
    public void testUserAccountDomain() {
        Assert.assertTrue(userService.isNeedLoginPwd("wfones@sohu.com"));
        Assert.assertTrue(userService.isNeedLoginPwd("nanajiaozixian@sogou.com"));
        Assert.assertTrue(!userService.isNeedLoginPwd("test@baidu.sohu.com"));
        Assert.assertTrue(!userService.isNeedLoginPwd("test@sina.sohu.com"));
        Assert.assertTrue(!userService.isNeedLoginPwd("test@renren.sohu.com"));
        Assert.assertTrue(!userService.isNeedLoginPwd("test@qq.sohu.com"));



    }
	
	/**
	 * 用户补全peak
	 */
	@Test
	public void testUserInfoComplete() {
	
		// 下发手机验证码
		// this.queryCaptchaTest();
		InfoDto infoDto = new InfoDto();
		
		infoDto.setUserId(userId);
		
		infoDto.setPwd(pwd);
		infoDto.setPayPwd(payPwd);
		infoDto.setPayPwdConfirm(payPwd);
		infoDto.setCaptcha(captcha);
		
		infoDto.setQuestionType(questionType);
		infoDto.setSafeQuestion(safeQuestion);
		infoDto.setSafeAnswer(safeAnswer);
		
		infoDto.setTrueName(trueName);
		infoDto.setIdCardNo(idCardNo);
		infoDto.setIdCardType(idCardType);
		infoDto.setEmail(email);
		infoDto.setMobile(mobile);
		infoDto.setUserIp(userIp);
		log.info("infoDto:" + ReflectionToStringBuilder.toString(infoDto));
		ResultUserDto<InfoResultDto> resultDto = userService.completeInfo(infoDto);
		log.info("infoResultDto:" + ReflectionToStringBuilder.toString(resultDto));
		Assert.assertTrue(resultDto.isSucces());
	}
	
	@Autowired
	private PassportService passportService;
	
	/**
	 * 测试通行证授权
	 */
	@Test
	public void testPassport() {
	
		InfoDto infoDto = new InfoDto();
		infoDto.setPwd(pwd);
		infoDto.setUserId(userId);
		infoDto.setUserIp(userIp);
		// 验证通行证账户
		PassportDto passportDto = new PassportDto();
		passportDto.setUserId(infoDto.getUserId());
		passportDto.setPassword(infoDto.getPwd());
		passportDto.setUserIp(infoDto.getUserIp());
		ResultPassportDto<PassportResultDto.PassportData> authResult = null;
		try {
			authResult = passportService.authUser(passportDto);
			log.debug("authResult:" + ReflectionToStringBuilder.toString(authResult));
			
		} catch (Exception e) {
			log.error(e);
		}
		Assert.assertTrue(authResult.isSucces());
	}
	
	/**
	 * 绑定银行卡
	 */
	@Test
	public void bindBank() {
	
		BindCardDto bindCardDto = new BindCardDto();
		bindCardDto.setCaptcha(captcha);
		bindCardDto.setUserIp(userIp);
		bindCardDto.setUserId(userId);
		bindCardDto.setMobile(mobile);
		bindCardDto.setEmail(email);
		bindCardDto.setPayPwd(payPwd);
		bindCardDto.setBank(UserInfoServiceTest.bank);
		bindCardDto.setBankId(UserInfoServiceTest.withDrawBankId);
		bindCardDto.setBankCardNo(UserInfoServiceTest.bankCardNo);
		bindCardDto.setBranch("清华园支行");
		bindCardDto.setCity("北京");
		bindCardDto.setProvince("北京");
		log.info("bindCardDto:" + ReflectionToStringBuilder.toString(bindCardDto));
		ResultUserDto<CommonDto> resultDto = userService.bindBank(bindCardDto);
		// Assert.assertTrue(resultDto.isSucces());
		
	}
	
	/**
	 * 测试绑定手机操作
	 */
	@Test
	// ok
	public void bindMobileTest() {
	
		UserDto dto = new UserDto();
		dto.setUserId(userId);
		dto.setMobile("18612693968");
		dto.setPayPwd(payPwd);
		dto.setCaptcha(captcha);
		ResultUserDto<CommonDto> result = userService.bindMobile(dto, false);
		log.debug(result);
	}
	
	/**
	 * 检查验证码
	 */
	@Test
	//
	public void checkCaptchaTest() {
	
		// 2.14
		UserDto dto = new UserDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setMobile(UserInfoServiceTest.mobile);
		dto.setCaptcha(UserInfoServiceTest.captcha);
		ResultUserDto<CommonDto> result = userService.checkCaptcha(dto, QianBaoService.CaptchaType.FindPwd);
		log.debug(result);
	}
	
	/**
	 * 下发验证码
	 */
	@Test
	// OK OK OK
	public void queryCaptchaTest() {
	
		this.queryCaptchaBindMobileTest();
		this.queryCaptchaFindPwdTest();
	}
	
	@Test
	// OK OK OK
	public void queryCaptchaBindMobileTest() {
	
		UserDto dto = new UserDto();
		dto.setUserId(userId);
		dto.setMobile(mobile);
		ResultUserDto<CommonDto> result = userService.queryCaptcha(dto, QianBaoService.CaptchaType.BingMobile);
		log.debug(result);
	}
	
	@Test
	// OK OK OK
	public void queryCaptchaFindPwdTest() {
	
		UserDto dto = new UserDto();
		dto.setUserId(userId);
		dto.setMobile(mobile);
		ResultUserDto<CommonDto> result = userService.queryCaptcha(dto, QianBaoService.CaptchaType.FindPwd);
		log.debug(result);
	}
	
}
