package com.sogou.lottery.web.service.user.service;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.aop.framework.AopContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.sogou.lottery.base.pay.dto.PayResultDto;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.util.MD5Util;
import com.sogou.lottery.base.vo.qianbao.AccountVo;
import com.sogou.lottery.base.vo.user.User;
import com.sogou.lottery.base.vo.user.UserBankVo;
import com.sogou.lottery.base.vo.user.UserTempVo;
import com.sogou.lottery.dao.SequenceDao;
import com.sogou.lottery.dao.qianbao.RechargeDao;
import com.sogou.lottery.dao.qianbao.WithDrawDao;
import com.sogou.lottery.dao.user.UserBankDao;
import com.sogou.lottery.dao.user.UserDao;
import com.sogou.lottery.dao.user.UserTempDao;
import com.sogou.lottery.web.service.passport.service.PassportService;
import com.sogou.lottery.web.service.passport.service.PassportServiceTest;
import com.sogou.lottery.web.service.pay.dto.PayDto;
import com.sogou.lottery.web.service.pay.dto.RefundDto;
import com.sogou.lottery.web.service.pay.dto.TransferDto;
import com.sogou.lottery.web.service.qianbao.service.QianBaoAccountService;
import com.sogou.lottery.web.service.qianbao.service.QianBaoService;
import com.sogou.lottery.web.service.user.dto.BalanceDto;
import com.sogou.lottery.web.service.user.dto.ChangePwdDto;
import com.sogou.lottery.web.service.user.dto.ChangeSafeDto;
import com.sogou.lottery.web.service.user.dto.CommonDto;
import com.sogou.lottery.web.service.user.dto.FindPwdByMobileDto;
import com.sogou.lottery.web.service.user.dto.FindPwdBySafeDto;
import com.sogou.lottery.web.service.user.dto.InfoDto;
import com.sogou.lottery.web.service.user.dto.InfoResultDto;
import com.sogou.lottery.web.service.user.dto.RechargeDto;
import com.sogou.lottery.web.service.user.dto.RechargeResultDto;
import com.sogou.lottery.web.service.user.dto.SafeResultDto;
import com.sogou.lottery.web.service.user.dto.TransDetailResultDto;
import com.sogou.lottery.web.service.user.dto.TransDto;
import com.sogou.lottery.web.service.user.dto.TransResultDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.dto.WithdrawDto;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.*" })
@PrepareForTest({ UserService.class, QianBaoService.class, AopContext.class })
public class UserServiceTest {
	
	private Log LOG = LogFactory.getLog(getClass());
	
	private UserService userService;
	private QianBaoService qianBaoService;
	// private QianBaoAccountService qianBaoAccountService;
	private UserDao userDao;
	private UserTempDao userTempDao;
	private UserBankDao userBankDao;
	private WithDrawDao withDrawDao;
	
	private User user;
	private UserTempVo userTemp;
	private UserBankVo userBank;
	
	private String ip = "127.0.0.1";
	private Integer questionType = 1;
	private String question = "安全问题";
	private String answer = MD5Util.getMD5("问题答案");
	
	private Long getId() {
	
		return System.currentTimeMillis();
	}
	
	@Before
	public void before() throws Exception {
	
		// String host = "http://10.1.104.137:8080/payment/internal/";
		String host = "http://10.1.221.26/payment/internal/";
		String bankId = "407";
		
		// qianBaoAccountService =
		// PowerMockito.mock(QianBaoAccountService.class);
		
		PowerMockito.spy(UserService.class);
		userService = PowerMockito.spy(new UserService());
		PowerMockito.when(userService, "isValidIdCardType", Mockito.anyInt()).thenReturn(true);
		PowerMockito.when(userService, "isValidSafeQuestion", Mockito.anyInt()).thenReturn(true);
		PowerMockito.when(userService, "isValidLocation", Mockito.anyString(), Mockito.anyString()).thenReturn(true);
		PowerMockito.when(userService, "isValidDrawBank", Mockito.anyString()).thenReturn(1);
		PowerMockito.when(userService, "isValidChargeBank", Mockito.anyString()).thenReturn(true);
		PowerMockito.when(userService, "isValidTransType", Mockito.anyInt()).thenReturn(true);
		
		PowerMockito.mockStatic(AopContext.class);
		PowerMockito.when(AopContext.class, "currentProxy").thenReturn((Object) userService);
		
		PowerMockito.spy(QianBaoService.class);
		qianBaoService = PowerMockito.spy(new QianBaoService());
		QianBaoAccountService qianBaoAccountService = Mockito.mock(QianBaoAccountService.class);
		AccountVo account = new AccountVo();
		account.setAccountId("2252");
		account.setKey("!qaz@wsx#Edc$rfv%tgb");
		Mockito.when(qianBaoAccountService.getSogouMiddle()).thenReturn(account);
		Mockito.when(qianBaoAccountService.getByAccountId(Mockito.anyString())).thenReturn(Lists.newArrayList(account));
		ReflectionTestUtils.setField(qianBaoService, "qianBaoAccountService", qianBaoAccountService);
		// Method getRechargeHost =
		// QianBaoService.class.getDeclaredMethod("getRechargeHost");
		
		PowerMockito.when(qianBaoService, "getRechargeHost").thenReturn("http://paytest.sohu.com/payment/internal/charge.action");
		PowerMockito.when(qianBaoService, "getSafeQuestion", Mockito.anyInt()).thenReturn(question);
		PowerMockito.when(qianBaoService, "getQianBaoHost").thenReturn(host);
		PowerMockito.when(qianBaoService, "getChargePayGateCode", Mockito.anyString()).thenReturn(bankId);
		PowerMockito.when(qianBaoService, "getIdCardType", Mockito.anyInt()).thenReturn("IDENTITY");
		PowerMockito.when(qianBaoService, "getTransType", Mockito.anyInt()).thenReturn("");// CHARGE
		PowerMockito.when(qianBaoService, "getTransType", Mockito.anyString()).thenReturn("1");
		PowerMockito.when(qianBaoService, "getDrawBankName", Mockito.anyString()).thenReturn("工商银行");
		PowerMockito.when(qianBaoService, "getProvinceCity", Mockito.anyString(), Mockito.anyString()).thenReturn(new String[] { "北京", "北京" });
		// PowerMockito.when(qianBaoService, "getBindingFact",
		// Mockito.anyString()).thenReturn("3001");
		// PowerMockito.when(qianBaoService,
		// "getCommonPsid").thenReturn("2252");
		// PowerMockito.when(qianBaoService, "getSeed",
		// Mockito.anyString()).thenReturn("!qaz@wsx#Edc$rfv%tgb");
		
		SequenceDao sequenceDao = PowerMockito.mock(SequenceDao.class);
		Mockito.when(sequenceDao.getSequenc(Mockito.anyString())).thenReturn(String.valueOf(getId()));
		
		RechargeDao rechargeDao = PowerMockito.mock(RechargeDao.class);
		Mockito.when(rechargeDao.getById(Mockito.anyString())).thenReturn(null);
		
		userBankDao = PowerMockito.mock(UserBankDao.class);
		userDao = PowerMockito.mock(UserDao.class);
		userTempDao = PowerMockito.mock(UserTempDao.class);
		
		user = new User();
		user.setTrueName("黄涛");
		user.setEmail("huangt820@gmail.com");
		// user.setCreateIp(ip);
		user.setUserId(PassportServiceTest.testPass1);
		user.setCreateTime(new Timestamp(System.currentTimeMillis()));
		user.setMobile("18610560162");
		user.setIdCardType(1);
		user.setIdCardNo("410102198306222011");
		
		userTemp = new UserTempVo();
		PropertyUtils.copyProperties(userTemp, user);
		userTemp.setPayPwd(PassportServiceTest.testPayPwd1);
		userTemp.setSafeQuestion(1);
		userTemp.setSafeAnswer(answer);
		userTemp.setStatus(UserTempVo.INIT);
		
		userBank = new UserBankVo();
		userBank.setUserId(user.getUserId());
		userBank.setBankId(bankId);
		userBank.setBankCardNo("12233r34343443434");
		userBank.setBranch("清华园支行");
		userBank.setCity("北京");
		userBank.setProvince("北京");
		
		Mockito.when(userDao.getByUserId(Mockito.anyString())).thenReturn(user);
		Mockito.when(userTempDao.getByUserId(Mockito.anyString())).thenReturn(userTemp);
		Mockito.when(userBankDao.getByUserId(Mockito.anyString())).thenReturn(userBank);
		
		withDrawDao = PowerMockito.mock(WithDrawDao.class);
		
		PassportService passportService = new PassportService();
		
		// ReflectionTestUtils.setField(qianBaoService, "qianBaoAccountService",
		// qianBaoAccountService);
		
		ReflectionTestUtils.setField(userService, "passportService", passportService);
		ReflectionTestUtils.setField(userService, "qianBaoService", qianBaoService);
		ReflectionTestUtils.setField(userService, "sequenceDao", sequenceDao);
		ReflectionTestUtils.setField(userService, "rechargeDao", rechargeDao);
		ReflectionTestUtils.setField(userService, "userDao", userDao);
		ReflectionTestUtils.setField(userService, "userTempDao", userTempDao);
		ReflectionTestUtils.setField(userService, "userBankDao", userBankDao);
		ReflectionTestUtils.setField(userService, "withDrawDao", withDrawDao);
	}
	
	// @Test
	// OK OK OK
	public void balanceQueryTest() {
	
		UserDto dto = new UserDto();
		dto.setUserId(PassportServiceTest.testPass1);
		dto.setUserIp(ip);
		ResultUserDto<BalanceDto> result = userService.queryBalance(dto);
		LOG.debug(result);
		Assert.isTrue(result.isSucces());
	}
	
	// @Test
	// OK OK OK
	public void rechargeTest() {
	
		RechargeDto dto = new RechargeDto();
		dto.setUserId(PassportServiceTest.testPass1);
		dto.setUserIp(ip);
		dto.setBankId("ALI");
		dto.setAmount(100L);
		ResultUserDto<RechargeResultDto> result = userService.comfirmRecharge(dto);
		LOG.debug(result);
		Assert.isTrue(result.isSucces());
	}
	
	// @Test
	// OK OK OK
	public void completeInfo() {
	
		try {
			Mockito.when(userDao.getByUserId(Mockito.anyString())).thenReturn(null);
			Mockito.when(userTempDao.getByUserId(Mockito.anyString())).thenReturn(null);
			Mockito.when(userBankDao.getByUserId(Mockito.anyString())).thenReturn(null);
			// Mockito.when(qianBaoService.doMobileBinding(Mockito.any(InfoDto.class))).thenReturn(new
			// ResultUserDto<CommonDto>());
			
			InfoDto dto = new InfoDto();
			dto.setUserId(PassportServiceTest.testPass1);
			dto.setPwd(PassportServiceTest.testPassPwd1);
			dto.setPayPwd(PassportServiceTest.testPayPwd1);
			dto.setPayPwdConfirm(PassportServiceTest.testPayPwd1);
			dto.setIdCardNo(user.getIdCardNo());
			dto.setIdCardType(1);
			dto.setTrueName(user.getTrueName());
			dto.setMobile(user.getMobile());
			dto.setEmail(user.getEmail());
			dto.setCaptcha("268797");
			dto.setQuestionType(questionType);
			dto.setSafeAnswer(answer);
			ResultUserDto<InfoResultDto> result = userService.completeInfo(dto);
			LOG.debug(result);
			Mockito.verify(qianBaoService, Mockito.times(1)).doCompleteInfo(Mockito.any(InfoDto.class));
			// Assert.isTrue(result.isSucces() || result.getRetcode() ==
			// QianBaoErrorCode.InfoExist.getCode());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Test
	// OK OK OK
	public void completeInfo2() {
	
		try {
			Mockito.when(userDao.getByUserId(Mockito.anyString())).thenReturn(null);
			Mockito.when(userTempDao.getByUserId(Mockito.anyString())).thenReturn(userTemp);
			Mockito.when(userBankDao.getByUserId(Mockito.anyString())).thenReturn(null);
			// Mockito.when(qianBaoService.doMobileBinding(Mockito.any(InfoDto.class))).thenReturn(new
			// ResultUserDto<CommonDto>());
			InfoDto dto = new InfoDto();
			dto.setUserId(user.getUserId());
			dto.setPwd(PassportServiceTest.testPassPwd1);
			dto.setPayPwd(PassportServiceTest.testPayPwd1);
			dto.setPayPwdConfirm(PassportServiceTest.testPayPwd1);
			dto.setIdCardNo(user.getIdCardNo());
			dto.setIdCardType(1);
			dto.setTrueName(user.getTrueName());
			dto.setMobile(user.getMobile());
			dto.setEmail(user.getEmail());
			dto.setCaptcha("232831");
			dto.setQuestionType(questionType);
			dto.setSafeAnswer(answer);
			ResultUserDto<InfoResultDto> result = userService.completeInfo(dto);
			Assert.isTrue(result.isSucces());
			result = userService.completeInfo(dto);
			LOG.debug(result);
			Mockito.verify(qianBaoService, Mockito.times(2)).doCompleteInfo(Mockito.any(InfoDto.class));
			Assert.isTrue(result.isSucces());
		} catch (Exception e) {
			throw e;
		}
	}
	
	// @Test
	// OK OK OK
	public void queryCaptchaTest() {
	
		// 2.14
		UserDto dto = new UserDto();
		dto.setUserId(PassportServiceTest.testPass1);
		dto.setMobile(user.getMobile());
		ResultUserDto<CommonDto> result = userService.queryCaptcha(dto, QianBaoService.CaptchaType.FindPwd);
		LOG.debug(result);
	}
	
	// @Test
	//
	public void checkCaptchaTest() {
	
		// 2.14
		UserDto dto = new UserDto();
		dto.setUserId(PassportServiceTest.testPass1);
		dto.setMobile(user.getMobile());
		dto.setCaptcha("049734");
		ResultUserDto<CommonDto> result = userService.checkCaptcha(dto, QianBaoService.CaptchaType.FindPwd);
		LOG.debug(result);
	}
	
	// @Test
	// OK OK OK
	public void queryCaptchaFindTest() {
	
		// 2.14
		UserDto dto = new UserDto();
		dto.setUserId(PassportServiceTest.testPass1);
		dto.setMobile(user.getMobile());
		ResultUserDto<CommonDto> result = userService.queryCaptcha(dto, QianBaoService.CaptchaType.FindPwd);
		LOG.debug(result);
	}
	
	// @Test
	// OK OK
	public void balancePayTest() {
	
		PayDto dto = new PayDto();
		dto.setUserId(PassportServiceTest.testPass1);
		dto.setUserIp(ip);
		dto.setPayOrderId(String.valueOf(getId()));
		dto.setTitle("彩票投注");
		dto.setCashAmount(5L);
		dto.setPayDeadline(new Timestamp(DateUtil.add(DateUtil.getCurrentDate(), 99, Calendar.YEAR).getTime()));
		dto.setPayPwd(PassportServiceTest.testPayPwd1);
		ResultUserDto<PayResultDto> result = userService.doBalancePay(dto);
		LOG.debug(result);
	}
	
	// @Test
	// ok
	public void bindMobileTest() {
	
		// 2.15
		UserDto dto = new UserDto();
		dto.setUserId(PassportServiceTest.testPass2);
		dto.setMobile(user.getMobile());
		dto.setPayPwd(PassportServiceTest.testPayNewPwd1);
		dto.setCaptcha("509645");
		ResultUserDto<CommonDto> result = userService.bindMobile(dto, false);
		LOG.debug(result);
	}
	
	// @Test
	// OK OK
	public void querySafeQuestionTest() {
	
		// 2.16
		UserDto dto = new UserDto();
		dto.setUserId(user.getUserId());
		ResultUserDto<SafeResultDto> result = userService.querySafeQuestion(dto);
		LOG.debug(result);
	}
	
	// @Test
	// OK OK
	public void queryChangePwdTest() {
	
		// 2.16
		ChangePwdDto dto = new ChangePwdDto();
		dto.setUserId(user.getUserId());
		dto.setPayPwd(PassportServiceTest.testPayNewPwd2);
		dto.setNewPayPwd(PassportServiceTest.testPayPwd2);
		dto.setNewPayPwdConfirm(PassportServiceTest.testPayPwd2);
		ResultUserDto<CommonDto> result = userService.changePayPwd(dto);
		LOG.debug(result);
	}
	
	// @Test
	// OK 有问题
	public void findPwdByQuestionTest() {
	
		// 2.18
		FindPwdBySafeDto dto = new FindPwdBySafeDto();
		dto.setUserId(user.getUserId());
		dto.setSafeAnswer(answer);
		dto.setNewPassowrd(PassportServiceTest.testPayNewPwd1);
		dto.setCaptcha("150888");
		ResultUserDto<CommonDto> result = userService.findPwdByQuestion(dto);
		LOG.debug(result);
	}
	
	// @Test
	// OK OK
	public void findPwdByInfoTest() {
	
		// 2.19
		FindPwdByMobileDto dto = new FindPwdByMobileDto();
		dto.setUserId(user.getUserId());
		dto.setTrueName(user.getTrueName());
		dto.setIdCardNo(user.getIdCardNo());
		dto.setIdCardType(user.getIdCardType());
		dto.setCaptcha("797299");
		dto.setNewPassowrd(PassportServiceTest.testPayPwd1);
		ResultUserDto<CommonDto> result = userService.findPwdByInfo(dto);
		LOG.debug(result);
	}
	
	// @Test
	// OK OK
	public void changeSafeQuestionTest() {
	
		// 2.17
		ChangeSafeDto dto = new ChangeSafeDto();
		dto.setUserId(user.getUserId());
		dto.setSafeAnswer(answer);
		dto.setNewQuestion(1);
		dto.setNewAnswer(answer);
		dto.setPayPwd(PassportServiceTest.testPayPwd2);
		ResultUserDto<CommonDto> result = userService.changeSafe(dto);
		LOG.debug(result);
	}
	
	@Test
	// ok
	public void queryTransTest() {
	
		// 2.17
		TransDto dto = new TransDto();
		dto.setUserId(user.getUserId());
		dto.setStartTime(new Timestamp(DateUtil.getStarting(DateUtil.getCurrentDate(), Calendar.MONTH).getTime()));
		dto.setEndTime(new Timestamp(DateUtil.getEnding(DateUtil.getCurrentDate(), Calendar.MONTH).getTime()));
		ResultUserDto<TransResultDto> result = userService.queryTrans(dto);
		for (TransDetailResultDto d : result.getResult().getTransList()) {
			LOG.debug(d);
		}
		LOG.debug(result);
	}
	
	// @Test
	// ok
	public void refundTest() {
	
		List<RefundDto> list = new ArrayList<>();
		
		RefundDto d1 = new RefundDto();
		d1.setPayOrderId("1394797019894");
		d1.setRefundId("" + (getId() + 1));
		d1.setRefundAmount(2L);
		d1.setRefundType(0);
		list.add(d1);
		
		// RefundDto d2 = new RefundDto();
		// d2.setPayOrderId("" + getId() + 2);
		// d2.setRefundId("" + (getId() + 3));
		// d2.setRefundAmount(5000L);
		// d2.setRefundType(0);
		// list.add(d2);
		
		// ResultUserDto<List<RefundResultDto>> result =
		// qianBaoService.doBatchRefund(list);
		// for (RefundResultDto dto : result.getResult()) {
		// LOG.debug(dto);
		// }
		
	}
	
	// @Test
	// ok
	public void transferTest() {
	
		List<TransferDto> dtos = new ArrayList<>();
		TransferDto d1 = new TransferDto();
		d1.setTransferId("" + getId());
		d1.setOutAccountId("2252");
		d1.setInAccountId("2253");
		d1.setAmount(1L);
		d1.setDrawable(true);
		dtos.add(d1);
		TransferDto d2 = new TransferDto();
		d2.setTransferId("" + (getId() + 1));
		d2.setOutAccountId("2252");
		d2.setInAccountId("2254");
		d2.setAmount(2L);
		d2.setDrawable(true);
		dtos.add(d2);
		// ResultUserDto<List<TransferResultDto>> result =
		// qianBaoService.getTransferB2B(dtos);
		// for (TransferResultDto dto : result.getResult()) {
		// LOG.debug(dto);
		// }
	}
	
	// @Test
	public void withdrawTest() {
	
		WithdrawDto dto = new WithdrawDto();
		dto.setUserId(user.getUserId());
		dto.setPayPwd(PassportServiceTest.testPayPwd1);
		dto.setAmount(1000L);
		userService.withdrawApply(dto);
		LOG.debug(dto);
		
	}
	
	@Test
	public void urlEncodeTest() throws Exception {
	
		String str = URLDecoder.decode("%CF%B5%CD%B3%B4%ED%CE%F3", "GBK");
		System.out.println(str);
	}
}
