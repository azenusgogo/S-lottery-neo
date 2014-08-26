package com.sogou.lottery.web.service.user.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

import com.google.common.collect.Lists;
import com.sogou.lottery.base.pay.dto.PayResultDto;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.qianbao.AccountVo;
import com.sogou.lottery.base.vo.qianbao.TransferVo;
import com.sogou.lottery.web.service.pay.dto.PayDto;
import com.sogou.lottery.web.service.pay.dto.RefundDto;
import com.sogou.lottery.web.service.qianbao.service.QianBaoAccountService;
import com.sogou.lottery.web.service.qianbao.service.QianBaoService;
import com.sogou.lottery.web.service.user.dto.BalanceDto;
import com.sogou.lottery.web.service.user.dto.CommonDto;
import com.sogou.lottery.web.service.user.dto.RechargeDto;
import com.sogou.lottery.web.service.user.dto.RechargeResultDto;
import com.sogou.lottery.web.service.user.dto.TransDto;
import com.sogou.lottery.web.service.user.dto.TransResultDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.dto.WithdrawDto;

/**
 * 描述：资金操作测试
 * 
 * @author haojiaqi
 */
@ContextConfiguration(locations = { "classpath:/applicationContext.xml" })
@TransactionConfiguration(defaultRollback = false)
public class FundsOptTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static Log log = LogFactory.getLog(FundsOptTest.class);
	
	@Autowired
	QianBaoService qianBaoService;
	@Autowired
	private QianBaoAccountService accountService;
	
	/**
	 * 彩票专用paygate，充值时请传这些
	 * <p/>
	 * 901 搜狗彩票支付宝-账户 902 搜狗彩票快钱-账户 903 搜狗彩票财付通-账户 911 搜狗彩票支付宝-中行 912 搜狗彩票支付宝-工行
	 * 913 搜狗彩票支付宝-招行 914 搜狗彩票支付宝-建行 915 搜狗彩票支付宝-农行 916 搜狗彩票支付宝-浦发 917
	 * 搜狗彩票支付宝-兴业银行 918 搜狗彩票支付宝-广发 919 搜狗彩票支付宝-深发 920 搜狗彩票支付宝-民生银行 921
	 * 搜狗彩票支付宝-交通银行 922 搜狗彩票支付宝-中信银行 923 搜狗彩票支付宝-杭州银行 924 搜狗彩票支付宝-光大银行 925
	 * 搜狗彩票支付宝-上海银行 926 搜狗彩票支付宝-宁波银行 927 搜狗彩票支付宝-平安银行 928 搜狗彩票支付宝-北京农村 929
	 * 搜狗彩票支付宝-富滇银行 930 搜狗彩票支付宝-邮政储蓄
	 */
	
	@Autowired
	UserService userService;
	private String captcha = "458621";
	
	@Before
	public void init() {
	
	}
	
	/**
	 * 测试充值流程,生成跳转peak url为止，之后通过url充值 测试要素：检查跳转 peak url的正确性，查询充值记录，查询数据库充值记录
	 */
	@Test
	public void testRechargeFlow() {
	
		RechargeDto dto = new RechargeDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setUserIp(UserInfoServiceTest.userIp);
		dto.setBankId(UserInfoServiceTest.rechargeBankId);
		dto.setAmount(100L);// 金额单位为分
		ResultUserDto<RechargeResultDto> result = userService.comfirmRecharge(dto);
		Assert.assertTrue(result.isSucces());
		log.info("result::" + ReflectionToStringBuilder.toString(result));
		log.info("seqid:" + result.getResult().getId() + ",returl:" + result.getResult().getReturl());
		
	}
	
	/**
	 * 已完成支付确认 TODO 未测试
	 */
	@Test
	public void testDoneRecharge() {
	
		RechargeDto dto = new RechargeDto();
		String id = "14032704RC0000000219";
		dto.setId(id);
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setUserIp(UserInfoServiceTest.userIp);
		ResultUserDto<RechargeResultDto> result = userService.comfirmRecharge(dto);
		Assert.assertTrue(result.isSucces());
		log.info("result::" + ReflectionToStringBuilder.toString(result));
	}
	
	/**
	 * 测试退款流程，联动退款， 测试要素：查看退款peak返回是否成功，查询退款记录，查询数据库退款记录 TODO peak不能体现
	 */
	@Test
	public void testWithDrawFlow() {
	
		WithdrawDto dto = new WithdrawDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setPayPwd(UserInfoServiceTest.payPwd);
		dto.setAmount(100L);// 金额单位为分
		dto.setFee(0L);
		ResultUserDto<CommonDto> resultDto = userService.withdrawApply(dto);
		log.info("WithdrawDto::" + ReflectionToStringBuilder.toString(dto));
		log.info("resultDto::" + ReflectionToStringBuilder.toString(resultDto));
		Assert.assertTrue(resultDto.isSucces());
	}
	
	/**
	 * 描述余额支付 TODO 未测试
	 */
	@Test
	// OK OK
	public void balancePayTest() {
	
		PayDto dto = new PayDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setUserIp(UserInfoServiceTest.userIp);
		dto.setPayOrderId(String.valueOf(getId()));
		dto.setTitle("彩票投注");
		dto.setCashAmount(1L);
		dto.setPayDeadline(new Timestamp(DateUtil.add(DateUtil.getCurrentDate(), 99, Calendar.YEAR).getTime()));
		dto.setPayPwd(UserInfoServiceTest.payPwd);
		ResultUserDto<PayResultDto> result = userService.doBalancePay(dto);
		log.info("result:" + ReflectionToStringBuilder.toString(result));
	}
	
	/**
	 * 描述：查询余额
	 */
	@Test
	// OK OK OK
	public void balanceQueryTest() {
	
		UserDto dto = new UserDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setUserIp(UserInfoServiceTest.userIp);
		ResultUserDto<BalanceDto> result = userService.queryBalance(dto);
		log.debug("result:" + ReflectionToStringBuilder.toString(result));
		Assert.assertTrue(result.isSucces());
	}
	
	/**
	 * 描述：查询资金交易记录
	 */
	@Test
	// ok
	public void queryTransTest() {
	
		TransDto dto = new TransDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setStartTime(new Timestamp(DateUtil.getStarting(DateUtil.getCurrentDate(), Calendar.MONTH).getTime()));
		dto.setEndTime(new Timestamp(DateUtil.getEnding(DateUtil.getCurrentDate(), Calendar.MONTH).getTime()));
		ResultUserDto<TransResultDto> result = userService.queryTrans(dto);
		log.debug("result:" + ReflectionToStringBuilder.toString(result));
		Assert.assertTrue(result.isSucces());
	}
	
	/**
	 * 描述：查询提现记录
	 */
	@Test
	// ok
	public void queryTransWithDrawTest() {
	
		TransDto dto = new TransDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setStartTime(new Timestamp(DateUtil.getStarting(DateUtil.getCurrentDate(), Calendar.MONTH).getTime()));
		dto.setEndTime(new Timestamp(DateUtil.getEnding(DateUtil.getCurrentDate(), Calendar.MONTH).getTime()));
		ResultUserDto<TransResultDto> result = userService.queryTransWithDraw(dto);
		log.debug("result:" + ReflectionToStringBuilder.toString(result));
		Assert.assertTrue(result.isSucces());
	}
	
	/**
	 * 描述：查询充值记录记录
	 */
	@Test
	// ok
	public void queryTransRechargeTest() {
	
		TransDto dto = new TransDto();
		dto.setUserId(UserInfoServiceTest.userId);
		dto.setStartTime(new Timestamp(DateUtil.getStarting(DateUtil.getCurrentDate(), Calendar.MONTH).getTime()));
		dto.setEndTime(new Timestamp(DateUtil.getEnding(DateUtil.getCurrentDate(), Calendar.MONTH).getTime()));
		ResultUserDto<TransResultDto> result = userService.queryTransRecharge(dto);
		log.debug("result:" + ReflectionToStringBuilder.toString(result));
		Assert.assertTrue(result.isSucces());
	}
	
	/**
	 * 退款测试 TODO 未完成
	 */
	@Test
	// ok
	public void refundTest() {
	
		List<RefundDto> list = new ArrayList<>();
		
		RefundDto d1 = new RefundDto();
		d1.setPayOrderId("1394797019894");
		d1.setRefundId("" + (getId() + 1));
		d1.setRefundAmount(2L);
		d1.setRefundType(0);
		list.add(d1);
		
		RefundDto d2 = new RefundDto();
		d2.setPayOrderId("" + getId() + 2);
		d2.setRefundId("" + (getId() + 3));
		d2.setRefundAmount(5000L);
		d2.setRefundType(0);
		list.add(d2);
		
		// ResultUserDto<List<RefundResultDto>> result =
		// qianBaoService.doBatchRefund(list);
		// for (RefundResultDto dto : result.getResult()) {
		// log.info("RefundResultDto:" +
		// ReflectionToStringBuilder.toString(dto));
		// }
		
	}
	
	/**
	 * 转账测试 TODO 未完成
	 */
	@Test
	// ok
	public void transferTest() {
	
		AccountVo middle = accountService.getSogouMiddle();
		AccountVo profit = accountService.getSogouProfit();
		
		TransferVo vo1 = new TransferVo();
		vo1.setId("1");
		vo1.setInAccount(middle.getAccountId());
		vo1.setOutAccount(profit.getAccountId());
		vo1.setType(TransferVo.TYPE_QB_MIDDLE_SOGOU);
		vo1.setInFact(TransferVo.FACT_CASH);
		vo1.setOutFact(TransferVo.FACT_CASH);
		vo1.setAmount(1L);
		
		TransferVo vo2 = new TransferVo();
		vo2.setId("2");
		vo2.setInAccount(middle.getAccountId());
		vo2.setOutAccount(profit.getAccountId());
		vo2.setType(TransferVo.TYPE_QB_MIDDLE_SOGOU);
		vo2.setInFact(TransferVo.FACT_CASH);
		vo2.setOutFact(TransferVo.FACT_CASH);
		vo2.setAmount(1L);
		List<TransferVo> list = Lists.newArrayList(vo1, vo2);
		
		// List<TransferResultDto> result =
		// qianBaoService.transfer(TransferVo.TYPE_QB_MIDDLE_MERCHANT, list);
		// for (TransferResultDto dto : result) {
		// log.info("TransferResultDto:" +
		// ReflectionToStringBuilder.toString(dto));
		//
		// }
	}
	
	private Long getId() {
	
		return System.currentTimeMillis();
	}
	
}
