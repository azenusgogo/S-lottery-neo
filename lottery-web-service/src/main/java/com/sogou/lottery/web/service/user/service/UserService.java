package com.sogou.lottery.web.service.user.service;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.sogou.lottery.base.constant.Dictionary;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.passport.PassportErrorCode;
import com.sogou.lottery.base.passport.dto.ResultPassportDto;
import com.sogou.lottery.base.pay.dto.PayResultDto;
import com.sogou.lottery.base.user.QianBaoConstant;
import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.dict.DictionaryVo;
import com.sogou.lottery.base.vo.qianbao.QianBaoLogVo;
import com.sogou.lottery.base.vo.qianbao.RechargeVo;
import com.sogou.lottery.base.vo.qianbao.WithDrawVo;
import com.sogou.lottery.base.vo.user.User;
import com.sogou.lottery.base.vo.user.UserBankVo;
import com.sogou.lottery.base.vo.user.UserLog;
import com.sogou.lottery.base.vo.user.UserTempVo;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.dao.SequenceDao;
import com.sogou.lottery.dao.qianbao.QianBaoLogDao;
import com.sogou.lottery.dao.qianbao.RechargeDao;
import com.sogou.lottery.dao.qianbao.WithDrawDao;
import com.sogou.lottery.dao.user.UserBankDao;
import com.sogou.lottery.dao.user.UserDao;
import com.sogou.lottery.dao.user.UserLogDao;
import com.sogou.lottery.dao.user.UserTempDao;
import com.sogou.lottery.util.CaptchaUtil;
import com.sogou.lottery.web.service.init.EnvironmentBean;
import com.sogou.lottery.web.service.passport.dto.PassportDto;
import com.sogou.lottery.web.service.passport.dto.PassportResultDto.PassportData;
import com.sogou.lottery.web.service.passport.service.PassportService;
import com.sogou.lottery.web.service.pay.dto.PayDto;
import com.sogou.lottery.web.service.qianbao.service.QianBaoService;
import com.sogou.lottery.web.service.qianbao.service.QianBaoService.CaptchaType;
import com.sogou.lottery.web.service.user.aop.UserLogAspect;
import com.sogou.lottery.web.service.user.constant.TransType;
import com.sogou.lottery.web.service.user.constant.UserAccountDomainEnum;
import com.sogou.lottery.web.service.user.dto.BalanceDto;
import com.sogou.lottery.web.service.user.dto.BindCardDto;
import com.sogou.lottery.web.service.user.dto.ChangePwdDto;
import com.sogou.lottery.web.service.user.dto.ChangeSafeDto;
import com.sogou.lottery.web.service.user.dto.ChargeBankDto;
import com.sogou.lottery.web.service.user.dto.CityDto;
import com.sogou.lottery.web.service.user.dto.CommonDto;
import com.sogou.lottery.web.service.user.dto.DrawBankDto;
import com.sogou.lottery.web.service.user.dto.FindPwdByMobileDto;
import com.sogou.lottery.web.service.user.dto.FindPwdBySafeDto;
import com.sogou.lottery.web.service.user.dto.IdResultDto;
import com.sogou.lottery.web.service.user.dto.InfoDto;
import com.sogou.lottery.web.service.user.dto.InfoResultDto;
import com.sogou.lottery.web.service.user.dto.ProvinceDto;
import com.sogou.lottery.web.service.user.dto.QuestionDto;
import com.sogou.lottery.web.service.user.dto.RechargeBackDto;
import com.sogou.lottery.web.service.user.dto.RechargeDto;
import com.sogou.lottery.web.service.user.dto.RechargeResultDto;
import com.sogou.lottery.web.service.user.dto.SafeResultDto;
import com.sogou.lottery.web.service.user.dto.TransDto;
import com.sogou.lottery.web.service.user.dto.TransResultDto;
import com.sogou.lottery.web.service.user.dto.UserDto;
import com.sogou.lottery.web.service.user.dto.UserResultDto;
import com.sogou.lottery.web.service.user.dto.WithdrawDto;

/**
 * 有输入，验证支付密码，以及手机验证码的方法会被AOP
 * 
 * @author huangtao
 * @see UserLogAspect
 */
@Service
public class UserService {
	
	private static Logger log = LOG.user;
	
	@Autowired
	private SequenceDao sequenceDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserLogDao userLogDao;
	@Autowired
	private UserTempDao userTempDao;
	@Autowired
	private UserBankDao userBankDao;
	@Autowired
	private WithDrawDao withDrawDao;
	@Autowired
	private RechargeDao rechargeDao;
	@Autowired
	private PassportService passportService;
	@Autowired
	private QianBaoService qianBaoService;
	@Autowired
	private QianBaoLogDao qianBaoLogDao;
	@Autowired
	private UserCacheService userCacheService;
	
	private final static int MD5TYPE = 32;
	private static final String CAPTCHA_SPLIT_TOKEN = "_";
	
	/**
	 * 描述：通过用户id判断用户账户域名
	 * 
	 * @param userId
	 * @return
	 */
	public UserAccountDomainEnum getDomainEnum(String userId) {
	
		if (userId == null) return UserAccountDomainEnum.UNKOWN;
		String[] users = userId.split("@");
		if (users == null || users.length != 2) {
			return UserAccountDomainEnum.UNKOWN;
		}
		for (UserAccountDomainEnum userAccountDomainEnum : UserAccountDomainEnum.values()) {
			if (userAccountDomainEnum.getValue().equals(users[1])) {
				return userAccountDomainEnum;
			}
		}
		return UserAccountDomainEnum.UNKOWN;
	}
	
	public Boolean isNeedLoginPwd(String userId) {
	
		UserAccountDomainEnum userAccountDomainEnum = this.getDomainEnum(userId);
		if (userAccountDomainEnum == UserAccountDomainEnum.BAIDU || userAccountDomainEnum == UserAccountDomainEnum.QQ || userAccountDomainEnum == UserAccountDomainEnum.RENREN || userAccountDomainEnum == UserAccountDomainEnum.SINA) {
			return false;
		} else {
			return true;
		}
	}
	
	public UserResultDto queryUser(String userId) {
	
		User user = userDao.getByUserId(userId);
		if (user == null) {
			return null;
		}
		UserResultDto dto = new UserResultDto();
		dto.setUser(user);
		return dto;
	}
	
	/**
	 * 生成的验证码写入到os里 ;返回[0] = 原始验证码 [1] = 时间戳
	 * 
	 * @param os
	 * @return
	 */
	public String[] generateImageCaptcha(OutputStream os) {
	
		if (os == null) {
			return null;
		}
		String captchaStr = CaptchaUtil.getCaptcha(os);
		Validate.notEmpty(captchaStr);
		String[] res = new String[2];
		res[0] = captchaStr;
		String time = String.valueOf(System.currentTimeMillis());
		// 为验证码加入时间戳
		res[1] = Joiner.on(CAPTCHA_SPLIT_TOKEN).join(captchaStr, time);
		return res;
	}
	
	public boolean checkImageCaptcha(String captcha, String expect) {
	
		if (StringUtils.isBlank(captcha) || StringUtils.isBlank(expect)) {
			return false;
		}
		String[] goals = StringUtils.split(expect, CAPTCHA_SPLIT_TOKEN);
		if (goals.length != 2) {
			return false;
		}
		long oldTimes = Long.parseLong(goals[1]);
		long current = System.currentTimeMillis();
		int expired = SystemConfigs.getIntValue(QianBaoConstant.CONFIG_CAPTCHA_IMAGE_EXPIRE, QianBaoConstant.CONFIG_CAPTCHA_IMAGE_EXPIRE_DEFAULT);
		if ((current - oldTimes) > expired) {
			if (log.isDebugEnabled()) {
				log.debug("Image captcha expired");
			}
			return false;
		}
		if (!captcha.equals(goals[0])) {
			return false;
		}
		return true;
	}
	
	public ResultUserDto<CommonDto> checkUserExist(final UserDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notEmpty(dto.getUserId());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		PassportDto passportDto = new PassportDto();
		passportDto.setUserId(dto.getUserId());
		passportDto.setUid(dto.getUid());
		ResultPassportDto<PassportData> authResult = null;
		try {
			authResult = passportService.checkUser(passportDto);
		} catch (Exception e) {
			return new ResultUserDto<>(UserErrorCode.CommonNetwork);
		}
		if (!authResult.isSucces()) {
			return new ResultUserDto<>(authResult.getRetcode(), authResult.getRetdesc());
		}
		return new ResultUserDto<>(UserErrorCode.CommonOK);
	}
	
	public ResultUserDto<CommonDto> registerUser(UserDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notEmpty(dto.getUserId());
			Validate.notEmpty(dto.getPwd());
			Validate.notEmpty(dto.getUserId());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		PassportDto passportDto = new PassportDto();
		passportDto.setUserId(dto.getUserId());
		passportDto.setPassword(dto.getPwd());
		passportDto.setUserIp(dto.getUserIp());
		passportDto.setUid(dto.getUid());
		ResultPassportDto<PassportData> authResult = null;
		try {
			authResult = passportService.registerUser(passportDto);
		} catch (Exception e) {
			return new ResultUserDto<>(UserErrorCode.CommonNetwork);
		}
		if (!authResult.isSucces()) {
			return new ResultUserDto<>(authResult.getRetcode(), authResult.getRetdesc());
		}
		return new ResultUserDto<>(UserErrorCode.CommonOK);
	}
	
	public ResultUserDto<CommonDto> checkNickExist(final UserDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notEmpty(dto.getNickName());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		User user = userDao.getByNick(dto.getNickName());
		if (user == null) {
			return new ResultUserDto<>(new CommonDto());
		} else {
			return new ResultUserDto<>(UserErrorCode.InfoNickExist);
		}
	}
	
	public ResultUserDto<InfoResultDto> queryInfo(InfoDto dto, boolean bank) {
	
		return handleUserInfo(dto, false, bank);
	}
	
	/**
	 * 先检查有没有用户信息，如果存在则不会重复补全信息,显示用户现有基本信息；如果不存在则补全
	 * 
	 * @param info
	 * @return
	 */
	public ResultUserDto<InfoResultDto> completeInfo(InfoDto info) {
	
		return handleUserInfo(info, true, true);
	}
	
	public UserService() {
	
		super();
	}
	
	private ResultUserDto<InfoResultDto> handleUserInfo(InfoDto info, boolean add, boolean bank) {
	
		if (info == null || StringUtils.isBlank(info.getUserId())) {
			return new ResultUserDto<>(UserErrorCode.CommonArgument);
		}
		UserBankVo userBankVo = null;
		InfoResultDto res = new InfoResultDto();
		
		User user = userCacheService.queryUserInfoCache(info.getUserId()).getUser();
		UserTempVo userTemp = null;
		if (user == null) {
			userTemp = userTempDao.getByUserId(info.getUserId());
		}
		boolean complete = true;
		if (user == null && userTemp != null) {
			
			if (UserTempVo.INIT == userTemp.getStatus()) {
				// 如果状态是初始化，意味着上次操作可能失败,则先拿库中的临时数据先进行尝试补全信息
				InfoDto infoTemp = new InfoDto();
				infoTemp.setUserId(userTemp.getUserId());
				infoTemp.setPayPwd(userTemp.getPayPwd());
				infoTemp.setPayPwdConfirm(userTemp.getPayPwd());
				infoTemp.setQuestionType(userTemp.getSafeQuestion());
				infoTemp.setSafeAnswer(userTemp.getSafeAnswer());
				infoTemp.setTrueName(userTemp.getTrueName());
				infoTemp.setNickName(userTemp.getNickName());
				infoTemp.setIdCardType(userTemp.getIdCardType());
				infoTemp.setIdCardNo(userTemp.getIdCardNo());
				infoTemp.setMobile(userTemp.getMobile());
				infoTemp.setCaptcha(userTemp.getCaptcha());
				if (!checkNickExist(info).isSucces()) {
					userTempDao.updateStatus(info.getUserId(), UserTempVo.INVALID);
					return new ResultUserDto<>(UserErrorCode.InfoNickExist);
				}
				// infoTemp.setUserIp(userTemp.getCreateIp());
				// infoTemp.setEmail(userTemp.getEmail());
				// peak会先验证用户是否已补全信息，如果发现已补全，会返回10005，不会再验证验证码
				ResultUserDto<InfoResultDto> qbInfoRes = qianBaoService.doCompleteInfo(infoTemp);
				// 黄涛 15:46:17
				// 补全用户，如果是返回用户已存在，就说明上一次一定是补全成功的，所有字段校验都通过的？
				// 搜狗-李少松4446 15:47:09
				// 是
				if (qbInfoRes.isSucces() || qbInfoRes.getRetcode() == UserErrorCode.InfoExist.getCode()) {
					// 如果是账户信息已存在，则说明上次补全是成功的,直接拿临时表的数据拷进正式表
					user = new User();
					user.setUserId(infoTemp.getUserId());
					user.setNickName(infoTemp.getNickName());
					// user.setEmail(infoTemp.getEmail());
					user.setMobile(infoTemp.getMobile());
					user.setTrueName(infoTemp.getTrueName());
					user.setIdCardType(infoTemp.getIdCardType());
					user.setIdCardNo(infoTemp.getIdCardNo());
					// user.setCreateIp(infoTemp.getUserIp());
					try {
						((UserService) AopContext.currentProxy()).insertUser(user);
					} catch (RuntimeException e) {
						log.fatal("User Complete Info Trans Exception occurs - " + info, e);
						return new ResultUserDto<>(UserErrorCode.CommonTransaction);
					}
					complete = false;// 不用再次补全了
					userCacheService.queryUserInfoCache(user.getUserId(), true);// 强制刷新用户信息
				} else if (!(qbInfoRes.getRetcode() == UserErrorCode.InfoMobileFormat.getCode() || qbInfoRes.getRetcode() == UserErrorCode.InfoEmail.getCode() || qbInfoRes.getRetcode() == UserErrorCode.InfoAnswerSize.getCode() || qbInfoRes.getRetcode() == UserErrorCode.InfoQuesionSize.getCode() || qbInfoRes.getRetcode() == UserErrorCode.InfoError.getCode() || qbInfoRes.getRetcode() == UserErrorCode.InfoIdSize.getCode() || qbInfoRes.getRetcode() == UserErrorCode.InfoId.getCode() || qbInfoRes.getRetcode() == UserErrorCode.InfoIdSize.getCode() || qbInfoRes.getRetcode() == UserErrorCode.InfoCaptcha.getCode())) {
					userTempDao.updateStatus(info.getUserId(), UserTempVo.INVALID);
					// 确定重新补全不会再成功的状态
					// 不确定的状态暂时不由用户激发补全，仍用临时表的中的数据尝试补全,这里直接返回错误
					String m = "User Complete Info Error occurs - " + info;
					log.fatal(m);
					return new ResultUserDto<>(UserErrorCode.CommonError);
				}
				if (complete) {
					userTempDao.updateStatus(info.getUserId(), UserTempVo.INVALID);
				}
			} else if (UserTempVo.SYNC == userTemp.getStatus()) {
				String m = "User Complete Info Logic Exception occurs - " + info;
				log.fatal(m);
				return new ResultUserDto<>(UserErrorCode.CommonError);
			}
		}
		if (complete && user == null) {
			if (add) {
				try {
					// 如果还没补全用户信息
					Validate.notNull(info);
					if (this.isNeedLoginPwd(info.getUserId())) {
						Validate.notEmpty(info.getPwd());
						Validate.isTrue(info.getPwd().length() == MD5TYPE);
					}
					Validate.notEmpty(info.getPayPwd());
					Validate.isTrue(info.getPayPwd().length() == MD5TYPE);
					Validate.notEmpty(info.getPayPwdConfirm());
					Validate.isTrue(info.getPayPwdConfirm().length() == MD5TYPE);
					Validate.notNull(info.getIdCardType());
					Validate.notEmpty(info.getCaptcha());
					Validate.notEmpty(info.getIdCardNo());
					Validate.isTrue(info.getIdCardNo().length() < 19);
					// Validate.notEmpty(info.getEmail());
					Validate.notEmpty(info.getMobile());
					Validate.notEmpty(info.getNickName());
					Validate.notEmpty(info.getSafeAnswer());// 32MD5
					Validate.isTrue(info.getSafeAnswer().length() == MD5TYPE);
					Validate.notNull(info.getQuestionType());
					Validate.notEmpty(info.getTrueName());
					Validate.notEmpty(info.getNickName());
					Validate.isTrue(isValidIdCardType(info.getIdCardType()));
					Validate.isTrue(isValidSafeQuestion(info.getQuestionType()));
				} catch (IllegalArgumentException | NullPointerException e) {
					return illegarArgument(e);
				}
				if (!info.getPayPwd().equals(info.getPayPwdConfirm())) {
					return new ResultUserDto<>(UserErrorCode.InfoPwdNotSame);
				} else if (this.isNeedLoginPwd(info.getUserId()) && info.getPayPwd().equals(info.getPwd())) {
					return new ResultUserDto<>(UserErrorCode.InfoPwdSame);
				}
				if (!checkNickExist(info).isSucces()) {
					return new ResultUserDto<>(UserErrorCode.InfoNickExist);
				}
				if (this.isNeedLoginPwd(info.getUserId())) {
					// 验证通行证账户
					PassportDto passportDto = new PassportDto();
					passportDto.setUserId(info.getUserId());
					passportDto.setPassword(info.getPwd());
					passportDto.setUserIp(info.getUserIp());
					passportDto.setUid(info.getUid());
					ResultPassportDto<PassportData> authResult = null;
					try {
						authResult = passportService.authUser(passportDto);
					} catch (Exception e) {
						return new ResultUserDto<>(UserErrorCode.CommonNetwork);
					}
					if (!authResult.isSucces()) {
						return new ResultUserDto<>(authResult.getRetcode(), authResult.getRetdesc());
					}
				}
				/*
				 * // 先绑定手机为了防止后绑定失败(验证码)，但是补全信息已经成功了，绑定手机失败后直接返回给用户失败了 //
				 * 如果第一次补全失败了，第二次用户重新发起会重新获得验证码，再重新调用钱包的绑定手机接口重新绑定新手机
				 * ResultUserDto<CommonDto> bindResult =
				 * qianBaoService.doMobileBinding(info); if
				 * (!bindResult.isSucces()) { return new
				 * ResultUserDto<>(bindResult.getRetcode(),
				 * bindResult.getRetdesc()); }
				 */
				boolean isAdd = true;
				if (userTemp != null && userTemp.getUserId() != null) {
					isAdd = false;
				}
				if (userTemp == null) userTemp = new UserTempVo();
				userTemp.setUserId(info.getUserId());
				// userTemp.setEmail(info.getEmail());
				userTemp.setMobile(info.getMobile());
				userTemp.setCaptcha(info.getCaptcha());
				userTemp.setIdCardNo(info.getIdCardNo());
				userTemp.setTrueName(info.getTrueName());
				userTemp.setNickName(info.getNickName());
				// userTemp.setCreateIp(info.getUserIp());
				userTemp.setIdCardType(info.getIdCardType());
				userTemp.setIdCardNo(info.getIdCardNo());
				userTemp.setSafeQuestion(info.getQuestionType());
				userTemp.setSafeAnswer(info.getSafeAnswer());
				userTemp.setStatus(UserTempVo.INIT);
				userTemp.setPayPwd(info.getPayPwd());
				if (isAdd) {
					userTempDao.insert(userTemp);
				} else {
					userTempDao.update(userTemp);
				}
				// LOG.debug("userTemp:"+ReflectionToStringBuilder.toString(userTemp));
				// 补全信息
				ResultUserDto<InfoResultDto> qbInfoRes = qianBaoService.doCompleteInfo(info);
				if (!qbInfoRes.isSucces() && qbInfoRes.getRetcode() != UserErrorCode.InfoExist.getCode()) {
					userTempDao.updateStatus(info.getUserId(), UserTempVo.INVALID);
					// LOG.fatal("Complete User Info Error - " + qbInfoRes +
					// " - " + info);
					return new ResultUserDto<>(qbInfoRes.getRetcode(), qbInfoRes.getRetdesc());
				}
				user = new User();
				user.setUserId(info.getUserId());
				user.setNickName(info.getNickName());
				// user.setEmail(info.getEmail());
				user.setMobile(info.getMobile());
				user.setIdCardType(info.getIdCardType());
				user.setIdCardNo(info.getIdCardNo());
				user.setTrueName(info.getTrueName());
				// user.setCreateIp(info.getUserIp());
				try {
					((UserService) AopContext.currentProxy()).insertUser(user);
					// LOG.debug("user"+ReflectionToStringBuilder.toString(user));
				} catch (RuntimeException e) {
					log.fatal("User Complete Info Trans Exception occurs - " + info, e);
					return new ResultUserDto<>(UserErrorCode.CommonTransaction);
				}
				userCacheService.queryUserInfoCache(user.getUserId(), true);// 强制刷新用户信息
			} else {
				return new ResultUserDto<>(UserErrorCode.InfoNotExist);
			}
		} else {
			// 已经补充过用户资料
			if (bank) {
				userBankVo = userBankDao.getByUserId(info.getUserId());
				if (userBankVo != null) {
					res.setBankId(userBankVo.getBankId());
					res.setBankCardNo(userBankVo.getBankCardNo());
					res.setDisplayBankCardNo(trimBankCard(userBankVo.getBankCardNo()));
					res.setBranch(userBankVo.getBranch());
				}
			}
		}
		res.setUserId(user.getUserId());
		res.setTrueName(user.getTrueName());
		res.setDisplayTrueName(trimName(user.getTrueName()));
		res.setIdCardNo(user.getIdCardNo());
		res.setDisplayIdCardNo(this.getDisplayIdCardNo(user.getIdCardNo()));
		res.setIdCardType(user.getIdCardType());
		res.setMobile(user.getMobile());
		res.setDisplayMobile(trimMobile(user.getMobile()));
		// res.setEmail(user.getEmail());
		// res.setDisplayEmail(trimEmail(user.getEmail()));
		return new ResultUserDto<>(res);
	}
	
	public String getDisplayIdCardNo(String idcardNo) {
	
		int length = idcardNo.length();
		String start = idcardNo.substring(0, 6);
		char[] cc = new char[length - 8];
		for (int i = 0; i < cc.length; i++) {
			cc[i] = '*';
		}
		String midden = new String(cc);
		String end = idcardNo.substring(length - 2);
		return start + midden + end;
	}
	
	@Transactional(value = "main")
	public void insertUser(User user) {
	
		userTempDao.updateStatus(user.getUserId(), UserTempVo.SYNC);
		userDao.insert(user);
	}
	
	/**
	 * confirmRecharge和doRecharge的合并版本
	 * 
	 * @param dto
	 * @return
	 */
	public ResultUserDto<RechargeDto> recharge(RechargeDto dto) {
	
		String token;
		try {
			Validate.notNull(dto);
			if (StringUtils.isNotBlank(dto.getId())) {
				RechargeVo rechargeVo = rechargeDao.getById(dto.getId());
				if (rechargeVo == null) {
					return new ResultUserDto<>(UserErrorCode.ChargeNoExist);
				} else if (!rechargeVo.isSuccess()) {
					return new ResultUserDto<>(UserErrorCode.ChargeProccesing);
				} else {
					dto.setUserIp(rechargeVo.getUserId());
					dto.setBankId(rechargeVo.getBankId());
					dto.setAmount(rechargeVo.getAmount());
					dto.setUserIp(rechargeVo.getIp());
					return new ResultUserDto<>(dto);
				}
			}
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getUserIp());
			Validate.notNull(dto.getBankId());
			Validate.notNull(dto.getAmount());
			Long minAmount = getRechargeMinAmount();
			Long maxAmount = getRechargeMaxAmount();
			Validate.isTrue(dto.getAmount() >= minAmount);
			Validate.isTrue(dto.getAmount() <= maxAmount);
			String ret = EnvironmentBean.getDomainUrl() + SystemConfigs.get(QianBaoConstant.CONFIG_RECHARGE_RETURL, QianBaoConstant.CONFIG_RECHARGE_RETURL_DEFAULT);
			dto.setReturl(ret);
			// Validate.notNull(dto.getReturl());
			Validate.isTrue(isValidChargeBank(dto.getBankId()));
			
			token = qianBaoService.queryRechargeToken();
			Validate.notNull(token);
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		RechargeVo rechargeVo = new RechargeVo();
		rechargeVo.setUserId(dto.getUserId());
		rechargeVo.setAmount(dto.getAmount());
		rechargeVo.setIp(dto.getUserIp());
		rechargeVo.setBankId(dto.getBankId());
		rechargeVo.setStatus(RechargeVo.STATUS_INIT);
		rechargeDao.insert(rechargeVo);
		
		dto.setId(rechargeVo.getId());
		dto.setToken(token);
		String returl = qianBaoService.queryRechargeUrl(dto);
		dto.setReturl(returl);
		return new ResultUserDto<>(dto);
	}
	
	public ResultUserDto<RechargeResultDto> comfirmRecharge(RechargeDto dto) {
	
		try {
			Validate.notNull(dto);
			// 先检查库中是否充值已成功
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getUserIp());
			Validate.notNull(dto.getBankId());
			Validate.notNull(dto.getAmount());
			Long minAmount = getRechargeMinAmount();
			Long maxAmount = getRechargeMaxAmount();
			Validate.isTrue(dto.getAmount() >= minAmount);
			Validate.isTrue(dto.getAmount() <= maxAmount);
			String ret = EnvironmentBean.getDomainUrl() + SystemConfigs.get(QianBaoConstant.CONFIG_RECHARGE_RETURL, QianBaoConstant.CONFIG_RECHARGE_RETURL_DEFAULT);
			dto.setReturl(ret);
			// Validate.notNull(dto.getReturl());
			Validate.isTrue(isValidChargeBank(dto.getBankId()));
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		// 先入库，后入库会发生钱包成功但是彩票找不到记录的情况，方便business回调
		RechargeVo rechargeVo = new RechargeVo();
		rechargeVo.setUserId(dto.getUserId());
		rechargeVo.setBankId(dto.getBankId());
		rechargeVo.setIp(dto.getUserIp());
		rechargeVo.setAmount(dto.getAmount());
		rechargeVo.setBankId(dto.getBankId());
		rechargeVo.setStatus(RechargeVo.STATUS_INIT);
		rechargeDao.insert(rechargeVo);
		
		UserResultDto user = ((UserService) AopContext.currentProxy()).queryUser(dto.getUserId());
		// 充值确认页需要显示昵称
		dto.setUserId(dto.getUserId());
		dto.setNickName(user == null ? null : user.getNickName());
		dto.setId(rechargeVo.getId());
		RechargeResultDto result = new RechargeResultDto();
		result.setRechargeDto(dto);
		return new ResultUserDto<>(result);
	}
	
	public ResultUserDto<RechargeResultDto> doRecharge(RechargeDto dto) {
	
		String token;
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getId());
			// 先检查库中是否充值已成功
			RechargeVo rechargeVo = rechargeDao.getById(dto.getId());
			if (rechargeVo == null) {
				return new ResultUserDto<>(UserErrorCode.ChargeNoExist);
			} else if (RechargeVo.STATUS_PROCESSING == rechargeVo.getStatus()) {
				return new ResultUserDto<>(UserErrorCode.ChargeProccesing);
			} else if (rechargeVo.isSuccess()) {
				return new ResultUserDto<>(UserErrorCode.CommonOK);
			}
			// 只有充值状态是初始化在执行下面逻辑
			dto.setUserId(rechargeVo.getUserId());
			dto.setBankId(rechargeVo.getBankId());
			dto.setAmount(rechargeVo.getAmount());
			dto.setUserIp(rechargeVo.getIp());
			token = qianBaoService.queryRechargeToken();
			Validate.notNull(token);
			dto.setToken(token);
			String returl = qianBaoService.queryRechargeUrl(dto);
			Validate.notNull(returl);
			// 这里这个字段复用了，返回时表示用户redirect的页面
			dto.setReturl(returl);
			UserResultDto user = ((UserService) AopContext.currentProxy()).queryUser(dto.getUserId());
			// 充值确认页需要显示昵称
			dto.setNickName(user == null ? null : user.getNickName());
			RechargeResultDto result = new RechargeResultDto();
			result.setRechargeDto(dto);
			// 用来区分请求是否被发送到钱包(用户充值行为是否在充值确认页就已经终止了)
			rechargeDao.updateStatus(rechargeVo, RechargeVo.STATUS_PROCESSING);
			return new ResultUserDto<>(result);
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
	}
	
	/**
	 * TODO 以后有需要改成mq通知
	 * 
	 * @param dto
	 */
	@Deprecated
	@Transactional(value = "main")
	public int doneRecharge(RechargeBackDto dto) {
	
		String from = dto.getSignstr();
		String to = qianBaoService.getSign(dto.getPsid(), dto.getPsid(), dto.getPs_requestId(), dto.getTransnum(), dto.getError_code());
		if (!from.equals(to)) {
			log.error("Peak Notice Sign Error - " + to + " for " + dto);
			throw new IllegalArgumentException();
		}
		RechargeVo vo = rechargeDao.getById(dto.getPs_requestId());
		if (vo == null) {
			log.fatal("Recharge record not exist - " + dto);
			return -1;
		}
		int dtoStatus = "0".equals(dto.getError_code()) ? RechargeVo.STATUS_SUCCESS : RechargeVo.STATUS_PROCESSING;
		int dbStatus = vo.getStatus().intValue();
		if ((RechargeVo.STATUS_INIT == dbStatus && RechargeVo.STATUS_INIT != dtoStatus) || (RechargeVo.STATUS_PROCESSING == dbStatus && (RechargeVo.STATUS_FAILED == dtoStatus || RechargeVo.STATUS_SUCCESS == dtoStatus))) {
			if (rechargeDao.updateStatus(vo, dtoStatus) > 0) {
				QianBaoLogVo log = new QianBaoLogVo();
				log.setBizId(vo.getId());
				log.setType(QianBaoLogVo.TYPE_CHARGE);
				log.setStatus(dtoStatus);
				log.setCode(String.valueOf(dto.getError_code()));
				log.setNote(dto.getError_desc());
				qianBaoLogDao.insert(log);
				return 1;
			}
		}
		return 0;
	}
	
	public ResultUserDto<PayResultDto> doBalancePay(PayDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getPayPwd());
			Validate.notNull(dto.getPayOrderId());
			Validate.notNull(dto.getTitle());
			Validate.notNull(dto.getCashAmount());
			Validate.notNull(dto.getPayDeadline());
			Validate.isTrue(dto.getCashAmount() > 0);
		} catch (IllegalArgumentException | NullPointerException e) {
			log.error(e, e);
			return new ResultUserDto<>(UserErrorCode.CommonArgument);
		}
		return qianBaoService.doBalancePay(dto);
	}
	
	public ResultUserDto<SafeResultDto> querySafeQuestion(UserDto dto) {
	
		if (StringUtils.isBlank(dto.getUserId())) {
			return new ResultUserDto<>(UserErrorCode.CommonArgument);
		}
		return qianBaoService.querySafeQuestion(dto);
	}
	
	public ResultUserDto<CommonDto> queryCaptcha(UserDto dto, CaptchaType type) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(type);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getMobile());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		return qianBaoService.queryMobileCaptcha(dto, type);
	}
	
	public ResultUserDto<CommonDto> checkCaptcha(UserDto dto, CaptchaType type) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(type);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getMobile());
			Validate.notNull(dto.getCaptcha());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		return qianBaoService.checkMobileCaptcha(dto, type);
	}
	
	public ResultUserDto<CommonDto> changePayPwd(ChangePwdDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getNewPayPwd());
			Validate.notNull(dto.getPayPwd());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		if (dto.getPayPwd().equals(dto.getNewPayPwd())) {
			return new ResultUserDto<>(UserErrorCode.ChgPwdOldNewSame);
		}
		if (!dto.getNewPayPwdConfirm().equals(dto.getNewPayPwd())) {
			return new ResultUserDto<>(UserErrorCode.ChgPwdConfNotSame);
		}
		if (this.isNeedLoginPwd(dto.getUserId())) {
			PassportDto passDto = new PassportDto();
			passDto.setUserId(dto.getUserId());
			passDto.setPassword(dto.getNewPayPwd());
			passDto.setUid(dto.getUid());
			ResultPassportDto<PassportData> passResult = passportService.authUser(passDto);
			if (passResult.getRetcode() == PassportErrorCode.AuthPwd.getCode()) {
				// 校验是否和登录密码一样
				return qianBaoService.doChangePayPwd(dto);
			} else {
				if (passResult.isSucces()) {
					return new ResultUserDto<>(UserErrorCode.ChgPwdSame);
				} else {
					return new ResultUserDto<>(passResult.getRetcode(), passResult.getRetdesc());
				}
			}
		} else {
			return qianBaoService.doChangePayPwd(dto);
		}
	}
	
	public ResultUserDto<CommonDto> findPwdByQuestion(FindPwdBySafeDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getSafeAnswer());
			Validate.notNull(dto.getNewPassowrd());
			Validate.notNull(dto.getNewPassowrdConfirm());
			Validate.notNull(dto.getCaptcha());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		// 先绑定现有手机
		// 再通过钱包密保问题修改支付密码
		User user = userDao.getByUserId(dto.getUserId());
		dto.setMobile(user.getMobile());
		// dto.setPayPwd(dto.getNewPassowrd());
		ResultUserDto<CommonDto> result = qianBaoService.checkMobileCaptcha(dto, CaptchaType.FindPwd);
		if (!result.isSucces()) {
			return result;
		}
		return qianBaoService.doFindPwdByQuesiton(dto);
	}
	
	public ResultUserDto<CommonDto> findPwdByInfo(FindPwdByMobileDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getCaptcha());
			Validate.notNull(dto.getNewPassowrd());
			Validate.notNull(dto.getNewPassowrdConfirm());
			Validate.notNull(dto.getTrueName());
			Validate.notNull(dto.getIdCardNo());
			Validate.notNull(dto.getIdCardType());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		// 先验证身份信息
		// 再通过钱包密保问题修改支付密码
		User user = userDao.getByUserId(dto.getUserId());
		if (!(user.getTrueName().equals(dto.getTrueName()) && user.getIdCardNo().equals(dto.getIdCardNo()) && user.getIdCardType().equals(dto.getIdCardType()))) {
			return new ResultUserDto<>(UserErrorCode.ChgPwdIdInfo);
		}
		dto.setMobile(user.getMobile());
		return qianBaoService.doFindPwdByMobile(dto);
	}
	
	public ResultUserDto<CommonDto> changeSafe(ChangeSafeDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getSafeAnswer());
			Validate.notNull(dto.getNewQuestion());
			Validate.notNull(dto.getNewAnswer());
			Validate.notNull(dto.getPayPwd());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		return qianBaoService.doChangeSafeQuestion(dto);
	}
	
	public ResultUserDto<CommonDto> bindMobile(UserDto dto, boolean update) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getMobile());
			Validate.notNull(dto.getCaptcha());
			Validate.notNull(dto.getPayPwd());
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		ResultUserDto<CommonDto> result = qianBaoService.doMobileBinding(dto);
		if (result.isSucces()) {
			// 同样涉及到钱包成功，彩票失败的情况，暂时先这样处理 TODO
			User user = new User();
			user.setUserId(dto.getUserId());
			// user.setMobile(dto.getMobile());
			userDao.updateMobile(user, dto.getMobile());
			user.setMobile(dto.getMobile());
			userCacheService.queryUserInfoCache(dto.getUserId(), true);
		}
		return result;
	}
	
	@Transactional(value = "main")
	public ResultUserDto<CommonDto> bindBank(BindCardDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getBankId());
			Validate.notNull(dto.getBankCardNo());
			int valid = isValidDrawBank(dto.getBankId());
			Validate.isTrue(valid >= 0);
			boolean branch = valid > 0 ? false : true;
			if (branch) {
				Validate.notNull(dto.getBranch());
				Validate.notNull(dto.getCity());
				Validate.notNull(dto.getProvince());
			} else if (branch) {
				Validate.isTrue(isValidLocation(dto.getProvince(), dto.getCity()));
			}
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		boolean insert = false;
		UserBankVo userBank = userBankDao.getByUserId(dto.getUserId());
		if (userBank == null) {
			insert = true;
			userBank = new UserBankVo();
		}
		userBank.setUserId(dto.getUserId());
		userBank.setBankId(dto.getBankId());
		userBank.setBranch(dto.getBranch());
		userBank.setBankCardNo(dto.getBankCardNo());
		userBank.setProvince(dto.getProvince());
		userBank.setCity(dto.getCity());
		
		if (insert) {
			userBankDao.insert(userBank);
		} else {
			userBankDao.update(userBank);
		}
		return new ResultUserDto<>(new CommonDto());
	}
	
	public ResultUserDto<CommonDto> withdrawApply(WithdrawDto dto) {
	
		User user;
		UserBankVo userBank;
		try {
			Validate.notNull(dto);
			Validate.notNull(dto.getUserId());
			Validate.notNull(dto.getPayPwd());
			Validate.notNull(dto.getAmount());
			Long minAmount = getWithDrawMinAmount();
			Long maxAmount = getWithDrawMaxAmount();
			Validate.isTrue(dto.getAmount() >= minAmount);
			Validate.isTrue(dto.getAmount() <= maxAmount);
			user = userDao.getByUserId(dto.getUserId());
			if (user == null) {
				throw new IllegalArgumentException("User Not Exist " + dto.getUserId());
			}
			userBank = userBankDao.getByUserId(dto.getUserId());
			if (userBank == null) {
				return new ResultUserDto<>(UserErrorCode.DrawBank);
			}
		} catch (IllegalArgumentException | NullPointerException e) {
			return illegarArgument(e);
		}
		dto.setTrueName(user.getTrueName());
		dto.setBankCardNo(userBank.getBankCardNo());
		dto.setBankId(userBank.getBankId());
		dto.setProvince(userBank.getProvince());
		dto.setCity(userBank.getCity());
		dto.setBranch(userBank.getBranch());
		Long amountAudit = Long.parseLong(SystemConfigs.get(QianBaoConstant.CONFIG_AMOUNT_WITHDRAW_AUDIT, QianBaoConstant.CONFIG_AMOUNT_WITHDRAW_AUDIT_DEFAULT));
		if (dto.getAmount() >= amountAudit) {
			dto.setAudit(true);
		} else {
			dto.setAudit(false);
		}
		Long fee = getWithDrawFeeAmount();
		dto.setFee(fee);
		
		ResultUserDto<BalanceDto> balance = qianBaoService.queryUserBalance(dto);
		if (!balance.isSucces()) {
			return new ResultUserDto<>(balance.getRetcode(), balance.getRetdesc());
		}
		Long withDrawBalance = balance.getResult() == null || balance.getResult().getAvailableWithDrawAmount() == null ? 0 : balance.getResult().getAvailableWithDrawAmount();
		if (dto.getAmount() > withDrawBalance) {
			return new ResultUserDto<>(UserErrorCode.DrawBalance);
		}
		// String id = sequenceDao.getSequenc(SequenceDao.DEL_WITHDRAW);
		
		WithDrawVo withdrawVo = new WithDrawVo();
		// withdrawVo.setId(id);
		withdrawVo.setBankId(dto.getBankId());
		withdrawVo.setAmount(dto.getAmount());
		withdrawVo.setFee(dto.getFee());
		// 设置userid by haojiaqi 2014-03-27
		withdrawVo.setUserId(dto.getUserId());
		withdrawVo.setNote(dto.getUserIp());
		withdrawVo.setIp(dto.getUserIp());
		withdrawVo.setStatus(WithDrawVo.STATUS_INIT);
		withdrawVo.setAudit(dto.getAudit() ? WithDrawVo.AUDIT_INIT : WithDrawVo.AUDIT_PASS);
		withDrawDao.insert(withdrawVo);
		
		dto.setId(withdrawVo.getId());
		
		ResultUserDto<IdResultDto> result = qianBaoService.doWithDraw(dto);
		
		int status;
		if (result.isSucces()) {
			status = WithDrawVo.STATUS_PROCESSING;
		} else {
			status = WithDrawVo.STATUS_FAILED;
		}
		withDrawDao.updateStatus(withdrawVo, status);
		withdrawVo.setStatus(status);
		return new ResultUserDto<>(result.getRetcode(), result.getRetdesc());
	}
	
	public Long queryWithDrawApplying(UserDto dto) {
	
		try {
			Validate.notNull(dto);
			Validate.notEmpty(dto.getUserId());
		} catch (IllegalArgumentException | NullPointerException e) {
			return 0L;
		}
		Long sum = withDrawDao.getSumByUserIdStatus(dto.getUserId(), new Integer[] { WithDrawVo.STATUS_INIT, WithDrawVo.STATUS_PROCESSING });
		return sum == null ? 0L : sum;
	}
	
	public ResultUserDto<TransResultDto> queryTransWithDraw(TransDto dto) {
	
		dto.setType(TransType.WithDraw.getType());
		return queryTrans(dto);
	}
	
	public ResultUserDto<TransResultDto> queryTransRecharge(TransDto dto) {
	
		dto.setType(TransType.Recharge.getType());
		return queryTrans(dto);
	}
	
	public ResultUserDto<TransResultDto> queryTrans(TransDto dto) {
	
		if (dto.getMonth() == null) {
			dto.setMonth(3);
		}
		int m = dto.getMonth();
		// 钱包系统的接口最细粒度是小时
		Date end = DateUtil.add(DateUtil.getCurrentDate(), 1, Calendar.HOUR);
		Date start = null;
		if (m == 0) {
			// 接口是否包含
			start = DateUtil.add(end, -1, Calendar.WEEK_OF_YEAR);
		} else {
			start = DateUtil.add(end, -m, Calendar.MONTH);
		}
		dto.setEndTime(new Timestamp(end.getTime()));
		dto.setStartTime(new Timestamp(start.getTime()));
		// if (dto.getStartTime() == null || dto.getEndTime() == null) {
		// return new ResultUserDto<>(ErrorCode.IllegalArgument);
		// }
		// 2014-03-13 森强说从第一页开始计
		if (dto.getPageNo() == null) {
			dto.setPageNo(1);
		}
		if (dto.getPageSize() == null || dto.getPageSize() < 1 || dto.getPageSize() > 20) {
			dto.setPageSize(10);
		}
		return qianBaoService.queryDetails(dto);
	}
	
	public ResultUserDto<BalanceDto> queryBalance(UserDto dto) {
	
		if (StringUtils.isBlank(dto.getUserId())) {
			return new ResultUserDto<>(UserErrorCode.CommonArgument);
		}
		return qianBaoService.queryUserBalance(dto);
	}
	
	public int getPwdErrorTimes(String userId) {
	
		return userCacheService.getUserOperTimes(userId, UserLog.TYPE_PWD_ERROR);
		// Timestamp today = new
		// Timestamp(DateUtil.getStarting(DateUtil.getCurrentDate(),
		// Calendar.DATE).getTime());
		// int times = userLogDao.getOperTimes(userId, today, new Integer[] {
		// UserLog.TYPE_PWD_ERROR });
		// return times;
	}
	
	public int getCaptchaQueryTimes(String userId) {
	
		Timestamp fiveMinute = new Timestamp(DateUtil.add(DateUtil.getCurrentDate(), -getQianBaoCaptchaInterval(), Calendar.SECOND).getTime());
		int times = userLogDao.getOperTimes(userId, fiveMinute, new Integer[] { UserLog.TYPE_CAP_QUERY });
		return times;
	}
	
	public int getWithDrawTimes(String userId) {
	
		Timestamp today = new Timestamp(DateUtil.getStarting(DateUtil.getCurrentDate(), Calendar.DATE).getTime());
		int times = userLogDao.getOperTimes(userId, today, new Integer[] { UserLog.TYPE_WITHDRAW_APPLY });
		return times;
	}
	
	public int getChangeSafeTimes(String userId) {
	
		Timestamp today = new Timestamp(DateUtil.getStarting(DateUtil.getCurrentDate(), Calendar.DATE).getTime());
		int times = userLogDao.getOperTimes(userId, today, new Integer[] { UserLog.TYPE_CHANGE_SAFE });
		return times;
	}
	
	public int getPwdErrorMaxTimes() {
	
		return SystemConfigs.getIntValue(QianBaoConstant.PEAK_PAY_LIMIT, QianBaoConstant.PEAK_PAY_LIMIT_DEFAULT);
	}
	
	public int getQianBaoCaptchaTimes() {
	
		return SystemConfigs.getIntValue(QianBaoConstant.PEAK_CAPTCH_LIMIT, QianBaoConstant.PEAK_CAPTCH_LIMIT_DEFAULT);
	}
	
	public int getQianBaoCaptchaInterval() {
	
		return SystemConfigs.getIntValue(QianBaoConstant.PEAK_CAPTCH_INTERVAL, QianBaoConstant.PEAK_CAPTCH_INTERVAL_DEFAULT);
	}
	
	public long getRechargeMinAmount() {
	
		return Long.parseLong(SystemConfigs.get(QianBaoConstant.CONFIG_AMOUNT_MIN_RECHARGE, QianBaoConstant.CONFIG_AMOUNT_MIN_RECHARGE_DEFAULT));
	}
	
	public long getRechargeMaxAmount() {
	
		return Long.parseLong(SystemConfigs.get(QianBaoConstant.CONFIG_AMOUNT_MAX_RECHARGE, QianBaoConstant.CONFIG_AMOUNT_MAX_RECHARGE_DEFAULT));
	}
	
	public long getWithDrawFeeAmount() {
	
		return Long.parseLong(SystemConfigs.get(QianBaoConstant.CONFIG_AMOUNT_WITHDRAW_FEE, QianBaoConstant.CONFIG_AMOUNT_WITHDRAW_FEE_DEFAULT));
	}
	
	public long getWithDrawMinAmount() {
	
		return Long.parseLong(SystemConfigs.get(QianBaoConstant.CONFIG_AMOUNT_WITHDRAW_MIN, QianBaoConstant.CONFIG_AMOUNT_WITHDRAW_MIN_DEFAULT));
	}
	
	public long getWithDrawMaxAmount() {
	
		return Long.parseLong(SystemConfigs.get(QianBaoConstant.CONFIG_AMOUNT_WITHDRAW_MAX, QianBaoConstant.CONFIG_AMOUNT_WITHDRAW_MAX_DEFAULT));
	}
	
	public int getWithDrawMaxTimes() {
	
		return SystemConfigs.getIntValue(QianBaoConstant.PEAK_WITHDRAW_LIMIT, QianBaoConstant.PEAK_WITHDRAW_LIMIT_DEFAULT);
	}
	
	public int getChangeSafeMaxTimes() {
	
		return SystemConfigs.getIntValue(QianBaoConstant.PEAK_CHANGESAFE_LIMIT, QianBaoConstant.PEAK_CHANGESAFE_LIMIT_DEFAULT);
	}
	
	public List<QuestionDto> getQuestions() {
	
		List<QuestionDto> questions = new ArrayList<>();
		List<DictionaryVo> qs = Dictionary.getDictionary(Dictionary.Type.SafeQuesiton);
		for (DictionaryVo dict : qs) {
			QuestionDto q = new QuestionDto();
			q.setCode(Integer.parseInt(dict.getKey()));
			q.setQuestion(dict.getValue());
			questions.add(q);
		}
		return questions;
	}
	
	public List<CityDto> getCity() {
	
		List<CityDto> cities = new ArrayList<>();
		List<DictionaryVo> qs = Dictionary.getDictionary(Dictionary.Type.City);
		for (DictionaryVo dict : qs) {
			CityDto c = new CityDto();
			c.setCode(dict.getKey());
			c.setName(dict.getValue());
			cities.add(c);
		}
		return cities;
	}
	
	/**
	 * 描述：查询身份证证件类型
	 * 
	 * @return
	 */
	public List<DictionaryVo> getIdCardTypes() {
	
		List<DictionaryVo> qs = Dictionary.getDictionary(Dictionary.Type.IdCardType);
		return qs;
	}
	
	public List<ProvinceDto> getProvince() {
	
		List<ProvinceDto> ps = new ArrayList<>();
		List<DictionaryVo> qs = Dictionary.getDictionary(Dictionary.Type.Province);
		for (DictionaryVo dict : qs) {
			ProvinceDto p = new ProvinceDto();
			p.setCode(dict.getKey());
			p.setName(dict.getValue());
			ps.add(p);
		}
		return ps;
	}
	
	public List<DrawBankDto> getDrawBank() {
	
		List<DictionaryVo> banks = Dictionary.getDictionary(Dictionary.Type.DrawBank);
		List<DictionaryVo> banksExtra = Dictionary.getDictionary(Dictionary.Type.DrawBankExtra);
		
		List<DrawBankDto> bankList = new ArrayList<>();
		for (DictionaryVo dict : banks) {
			DrawBankDto b = new DrawBankDto(Integer.parseInt(dict.getKey()), dict.getValue());
			bankList.add(b);
		}
		for (DictionaryVo dict : banksExtra) {
			DrawBankDto b = new DrawBankDto(Integer.parseInt(dict.getKey()), dict.getValue());
			b.setBranch(true);
			bankList.add(b);
		}
		return bankList;
	}
	
	public List<ChargeBankDto> getChargeBank() {
	
		List<ChargeBankDto> bs = new ArrayList<>();
		List<DictionaryVo> ds = Dictionary.getDictionary(Dictionary.Type.ChargeBank);
		for (DictionaryVo dict : ds) {
			ChargeBankDto b = new DrawBankDto(Integer.parseInt(dict.getKey()), dict.getValue());
			bs.add(b);
		}
		return bs;
	}
	
	private boolean isValidIdCardType(Integer code) {
	
		String value = Dictionary.getValue(Dictionary.Type.IdCardType, String.valueOf(code));
		if (StringUtils.isBlank(value)) {
			return false;
		}
		// String targetKey =
		// Dictionary.getTargetKey(Dictionary.Type.IdCardType,
		// String.valueOf(code));
		// if (StringUtils.isBlank(targetKey)) {
		// return false;
		// }
		return true;
	}
	
	private boolean isValidSafeQuestion(Integer code) {
	
		String value = Dictionary.getValue(Dictionary.Type.SafeQuesiton, String.valueOf(code));
		if (StringUtils.isBlank(value)) {
			return false;
		}
		return true;
	}
	
	private boolean isValidLocation(String province, String city) {
	
		if (province == null || city == null) {
			return false;
		}
		String prov = Dictionary.getValue(Dictionary.Type.Province, province);
		String ci = Dictionary.getValue(Dictionary.Type.City, city);
		return !(prov == null || ci == null);
	}
	
	private int isValidDrawBank(String code) {
	
		String value = Dictionary.getValue(Dictionary.Type.DrawBank, code);
		if (StringUtils.isBlank(value)) {
			value = Dictionary.getValue(Dictionary.Type.DrawBankExtra, code);
			if (StringUtils.isBlank(value)) {
				return -1;
			}
			return 0;
		}
		return 1;
	}
	
	private boolean isValidChargeBank(String code) {
	
		String value = Dictionary.getValue(Dictionary.Type.ChargeBankQB, code);
		if (StringUtils.isBlank(value)) {
			return false;
		}
		return true;
	}
	
	public boolean isValidTransType(Integer code) {
	
		String value = Dictionary.getValue(Dictionary.Type.TransType, String.valueOf(code));
		if (StringUtils.isBlank(value)) {
			return false;
		}
		return true;
	}
	
	private static <T> ResultUserDto<T> illegarArgument(RuntimeException e) {
	
		log.error(e, e);
		return new ResultUserDto<>(UserErrorCode.CommonArgument);
	}
	
	protected static String trimName(String orgStr) {
	
		int lengthToGet = orgStr.length() > 3 ? 2 : 1;
		if (StringUtils.isBlank(orgStr)) {
			return "";
		}
		if (orgStr.length() <= lengthToGet) {
			return orgStr;
		}
		int length = 0;
		lengthToGet *= 2;
		for (int i = 0; i < orgStr.length(); i++) {
			length += Character.codePointAt(orgStr, i) < 256 ? 1 : 2;
			if (length > lengthToGet) {
				String str = orgStr.substring(0, i);
				if (orgStr.length() - str.length() > 2) {
					return orgStr.substring(0, i) + "**";
				} else {
					return orgStr.substring(0, i) + "*";
				}
			}
		}
		return orgStr;
	}
	
	protected static String trimEmail(String orgStr) {
	
		String[] tmp = orgStr.split("@");
		if (tmp.length != 2) {
			return orgStr;
		}
		int l = 2;
		if (tmp[0].length() < 3) {
			return "**";
		} else if (tmp[0].length() <= 4) {
			l = 1;
		}
		String left = tmp[0].substring(0, l);
		String right = tmp[0].substring(tmp[0].length() - l);
		int len = tmp[0].length() - l * 2;
		len = len > 5 ? 5 : len;
		for (int i = 0; i < len; i++) {
			left += "*";
		}
		return left + right + "@" + tmp[1];
	}
	
	protected static String trimMobile(String orgStr) {
	
		if (orgStr.length() != 11) {
			return orgStr;
		}
		return orgStr.substring(0, 3) + "****" + orgStr.substring(7);
	}
	
	protected static String trimBankCard(String orgStr) {
	
		if (orgStr.length() < 7) {
			return orgStr;
		}
		String m = "";
		for (int i = 0; i < orgStr.length() - 6; i++) {
			m += "*";
		}
		return orgStr.substring(0, 3) + m + orgStr.substring(orgStr.length() - 3);
	}
}
