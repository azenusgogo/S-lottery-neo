package com.sogou.lottery.web.service.user.aop;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sogou.lottery.base.order.OrderErrorCode;
import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.user.UserLog;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.dao.user.UserLogDao;
import com.sogou.lottery.web.service.sensitive.service.SensitiveService;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.service.UserCacheService;
import com.sogou.lottery.web.service.user.service.UserService;

@Aspect
@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserLogAspect {
	
	private Logger log = LOG.user;
	
	private final static String POINT_CUT_PAYPWD_COMMON = "execution(* *..UserService.withdrawApply(..)) or execution(* *..UserService.bindMobile(..)) or execution(* *..UserService.changeSafe(..)) or execution(* *..UserService.changePayPwd(..))";
	private final static String POINT_CUT_AFTER_PAYPWD_INPUT = "execution(* *..UserService.doBalancePay(..)) or " + POINT_CUT_PAYPWD_COMMON;
	private final static String POINT_CUT_AROUND_PAYPWD_INPUT = POINT_CUT_PAYPWD_COMMON;
	
	private final static String POINT_CUT_AFTER_CAPTCHA_QUERY = "execution(* *..UserService.queryCaptcha(..))";
	private final static String POINT_CUT_AROUND_CAPTCHA_QUERY = POINT_CUT_AFTER_CAPTCHA_QUERY;
	
	private final static String POINT_CUT_AROUND_PASSPORT = "execution(* *..UserService.checkNickExist(..)) or execution(* *..UserService.completeInfo(..))";
	
	private final static String POINT_CUT_AROUND_WITHDRAW_APPLY = "execution(* *..UserService.withdrawApply(..))";
	
	private final static String POINT_CUT_AROUND_RESET_QUESITON = "execution(* *..UserService.changeSafe(..))";
	
	@Autowired
	private UserLogDao userLogDao;
	@Autowired
	private UserService userService;
	@Autowired
	private SensitiveService sensitiveService;
	@Autowired
	private UserCacheService userCacheService;
	
	@AfterReturning(pointcut = POINT_CUT_AFTER_PAYPWD_INPUT, returning = "ret")
	public <T> void payPwdError(JoinPoint jp, Object ret) {
	
		UserDto user = null;
		try {
			ResultUserDto<T> result = (ResultUserDto<T>) ret;
			user = (UserDto) jp.getArgs()[0];
			if (!result.isSucces()) {
				if (UserErrorCode.PayPwd.getCode() == result.getRetcode() || UserErrorCode.BindPay.getCode() == result.getRetcode() || UserErrorCode.ChgSafePayPwd.getCode() == result.getRetcode() || UserErrorCode.ChgPwdError.getCode() == result.getRetcode() || UserErrorCode.DrawPwd.getCode() == result.getRetcode() || UserErrorCode.BindPayPwd.getCode() == result.getRetcode() || UserErrorCode.ChgSafePayPwdFrozen.getCode() == result.getRetcode() || UserErrorCode.ChgPwdFrozen.getCode() == result.getRetcode() || UserErrorCode.PayPwdFrozen.getCode() == result.getRetcode() || UserErrorCode.DrawPwdFrozen.getCode() == result.getRetcode()) {
					UserLog userLog = new UserLog();
					userLog.setUserId(user.getUserId());
					userLog.setType(UserLog.TYPE_PWD_ERROR);
					userLog.setNote("支付密码输入错误");
					userLog.setIp(user.getUserIp());
					userLogDao.insert(userLog);
					userCacheService.setUserOper(userLog, DateUtil.getEnding(DateUtil.getCurrentDate(), Calendar.DATE));
					// int times =
					// userCacheService.getUserOperTimes(user.getUserId(),
					// UserLog.TYPE_PWD_ERROR);
					int times = userService.getPwdErrorTimes(user.getUserId());
					// userLogDao.getOperTimes(user.getUserId(), getToday(), new
					// Integer[] { UserLog.TYPE_PWD_ERROR });
					int maxTimes = userService.getPwdErrorMaxTimes();
					int left = maxTimes - times;
					String desc = "";
					if (left > 0) {
						desc = "支付密码已经连续输入错误" + times + "次, 还有" + left + "次机会";
					} else {
						desc = "连续输入支付密码超过" + maxTimes + "次，请明天再试";
					}
					result.setRetdesc(desc);
				}
			} else {
				userCacheService.deleteUserOperTimes(user.getUserId(), UserLog.TYPE_PWD_ERROR);
			}
		} catch (Exception e) {
			log.error(user, e);
		}
	}
	
	@Around(POINT_CUT_AROUND_PAYPWD_INPUT)
	public Object payPwdFrozen(ProceedingJoinPoint jp) {
	
		ResultUserDto result = new ResultUserDto();
		UserDto user = null;
		try {
			user = (UserDto) jp.getArgs()[0];
			// 一天5次
			int times = userService.getPwdErrorTimes(user.getUserId());
			// userLogDao.getOperTimes(user.getUserId(), getToday(), new
			// Integer[] { UserLog.TYPE_PWD_ERROR });
			int maxTimes = userService.getPwdErrorMaxTimes();
			if (times >= maxTimes) {
				result = new ResultUserDto(OrderErrorCode.CommonArgument.getCode(), "连续输入支付密码超过" + maxTimes + "次，请明天再试");
			} else {
				result = (ResultUserDto) jp.proceed();
			}
			return result;
		} catch (Throwable e) {
			log.error(user, e);
			return new ResultUserDto(OrderErrorCode.CommonBusy);
		}
	}
	
	@AfterReturning(pointcut = POINT_CUT_AFTER_CAPTCHA_QUERY, returning = "ret")
	public <T> void captchaQuery(JoinPoint jp, Object ret) {
	
		UserDto user = null;
		try {
			ResultUserDto<T> result = (ResultUserDto<T>) ret;
			if (result.isSucces()) {
				user = (UserDto) jp.getArgs()[0];
				UserLog userLog = new UserLog();
				userLog.setUserId(user.getUserId());
				userLog.setType(UserLog.TYPE_CAP_QUERY);
				userLog.setNote("下发短信验证码");
				userLog.setIp(user.getUserIp());
				userLogDao.insert(userLog);
			}
		} catch (Exception e) {
			log.error(user, e);
		}
	}
	
	@Around(POINT_CUT_AROUND_CAPTCHA_QUERY)
	public Object captchaFrozen(ProceedingJoinPoint jp) {
	
		ResultUserDto result = new ResultUserDto();
		UserDto user = null;
		try {
			user = (UserDto) jp.getArgs()[0];
			// 5分钟1次
			int times = userService.getCaptchaQueryTimes(user.getUserId());
			// userLogDao.getOperTimes(user.getUserId(), getFiveMinute(), new
			// Integer[] { UserLog.TYPE_CAP_QUERY });
			int maxTimes = userService.getQianBaoCaptchaTimes();
			if (times >= maxTimes) {
				result = new ResultUserDto(UserErrorCode.CaptchaFreq);
			} else {
				result = (ResultUserDto) jp.proceed();
			}
			return result;
		} catch (Throwable e) {
			log.error(user, e);
			return new ResultUserDto(UserErrorCode.CommonBusy);
		}
	}
	
	/**
	 * a) 每个ip多个cookie值（类似局域网注册）100次/天 （后期会对数据做分析，加到白名单中，放大注册次数限制） b)
	 * 每个cookie多ip（同一台电脑上不断的换IP注册）5次/天 c) ip+cookie限制用户 10次/天
	 * 
	 * @param jp
	 * @return
	 */
	@Around(POINT_CUT_AROUND_PASSPORT)
	public Object nickNameFilter(ProceedingJoinPoint jp) {
	
		ResultUserDto result = new ResultUserDto();
		UserDto dto = null;
		try {
			dto = (UserDto) jp.getArgs()[0];
			if (StringUtils.isNotBlank(dto.getNickName()) && sensitiveService.containsSensitive(dto.getNickName())) {
				return new ResultUserDto<>(UserErrorCode.InfoSensitive.getCode(), UserErrorCode.InfoSensitive.getDesc());
			}
			result = (ResultUserDto) jp.proceed();
			return result;
		} catch (Throwable e) {
			log.error(dto, e);
			return new ResultUserDto(UserErrorCode.CommonBusy.getCode(), UserErrorCode.CommonBusy.getDesc());
		}
	}
	
	/**
	 * 每日提现次数限制
	 * 
	 * @param jp
	 * @return
	 */
	@Around(POINT_CUT_AROUND_WITHDRAW_APPLY)
	public Object withdrawLimit(ProceedingJoinPoint jp) {
	
		ResultUserDto result = new ResultUserDto();
		UserDto user = null;
		try {
			int times = 0;
			try {
				user = (UserDto) jp.getArgs()[0];
				// 每日3次
				times = userService.getWithDrawTimes(user.getUserId());
			} catch (Throwable e) {
				log.error(user, e);
				return new ResultUserDto(UserErrorCode.CommonBusy);
			}
			int maxTimes = userService.getWithDrawMaxTimes();
			if (times >= maxTimes) {
				result = new ResultUserDto(UserErrorCode.CommonArgument.getCode(), "您已超过每日提款次数限制，请明天再试");
			} else {
				result = (ResultUserDto) jp.proceed();
				if (result.isSucces()) {
					UserLog userLog = new UserLog();
					userLog.setUserId(user.getUserId());
					userLog.setType(UserLog.TYPE_WITHDRAW_APPLY);
					userLog.setNote("用户提现申请");
					userLog.setIp(user.getUserIp());
					userLogDao.insert(userLog);
				}
			}
			return result;
		} catch (Throwable e) {
			log.error(jp, e);
			return new ResultUserDto(UserErrorCode.CommonBusy);
		}
	}
	
	/**
	 * 每日提现次数限制
	 * 
	 * @param jp
	 * @return
	 */
	@Around(POINT_CUT_AROUND_RESET_QUESITON)
	public Object changeSafeLimit(ProceedingJoinPoint jp) {
	
		ResultUserDto result = new ResultUserDto();
		UserDto user = null;
		try {
			int times = 0;
			try {
				user = (UserDto) jp.getArgs()[0];
				// 每日3次
				times = userService.getChangeSafeTimes(user.getUserId());
			} catch (Throwable e) {
				log.error(user, e);
				return new ResultUserDto(UserErrorCode.CommonBusy);
			}
			int maxTimes = userService.getChangeSafeMaxTimes();
			if (times >= maxTimes) {
				result = new ResultUserDto(UserErrorCode.CommonArgument.getCode(), "您每天只能重置" + maxTimes + "次安全问题和答案，请明天再试");
			} else {
				result = (ResultUserDto) jp.proceed();
				if (result.isSucces()) {
					UserLog userLog = new UserLog();
					userLog.setUserId(user.getUserId());
					userLog.setType(UserLog.TYPE_CHANGE_SAFE);
					userLog.setNote("用户变更安全问题");
					userLog.setIp(user.getUserIp());
					userLogDao.insert(userLog);
				}
			}
			return result;
		} catch (Throwable e) {
			log.error(jp, e);
			return new ResultUserDto(UserErrorCode.CommonBusy);
		}
	}
}
