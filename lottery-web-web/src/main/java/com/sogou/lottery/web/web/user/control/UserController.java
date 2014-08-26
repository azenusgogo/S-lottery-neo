package com.sogou.lottery.web.web.user.control;

import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.sogou.lottery.web.service.user.dto.*;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.LogUtil;
import com.sogou.lottery.base.vo.user.User;
import com.sogou.lottery.common.constant.SeoConstant;
import com.sogou.lottery.util.IDCardVeryUtils;
import com.sogou.lottery.web.service.order.service.OrderService;
import com.sogou.lottery.web.service.user.constant.IdCardType;
import com.sogou.lottery.web.service.user.constant.TransType;
import com.sogou.lottery.web.service.user.service.UserCacheService;
import com.sogou.lottery.web.service.user.service.UserService;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/login/user")
public class UserController extends BaseController {
	
	/**
	 * 查询用户余额
	 */
	protected final static String CTL_BALANCE = "/balance";
	/**
	 * 查询用户余额+提现申请金额
	 */
	protected final static String CTL_BALANCE_WITHDRAW = "/balance/withdraw";
	
	/**
	 * AJAX查询用户昵称等常用信息
	 */
	protected final static String CTL_INFO_BASE = "/info/base";
	/**
	 * 进入用户信息查看页
	 */
	protected final static String CTL_INFO = "/info";
	protected final static String CTL_INFO_COMPLETE = "/info/complete";
	protected final static String FTL_INFO = "user/info/info";
	/**
	 * 提交用户信息表单
	 */
	protected final static String CTL_INFO_ADD_INIT = "/info/add/init";
	protected final static String CTL_INFO_ADD = "/info/add";
	protected final static String FTL_FILL_ADD = "user/info/add";
	/**
	 * 进入修改支付密码页
	 */
	protected final static String CTL_PWD_CHANGE = "/pwd/change";
	protected final static String FTL_PWD = "user/pwd/change";
	/**
	 * 进入找回支付密码页
	 */
	protected final static String CTL_PWD_FIND = "/pwd/find";
	protected final static String FTL_PWD_FIND = "user/pwd/find";
	/**
	 * 通过身份信息和手机进入找回支付密码页
	 */
	protected final static String CTL_PWD_FIND_INFO = "/pwd/find/info";
	protected final static String FTL_PWD_FIND_INFO = "user/pwd/find/info";
	/**
	 * 通过密保问题和手机进入找回支付密码页
	 */
	protected final static String CTL_PWD_FIND_SAFE = "/pwd/find/safe";
	protected final static String FTL_PWD_FIND_SAFE = "user/pwd/find/safe";
	
	/**
	 * 获取用户当前安全问题
	 */
	protected final static String CTL_PWD_FIND_CURRENT_SAFE = "/pwd/find/currentsafe";
	
	/**
	 * 修改安全问题页面
	 */
	protected final static String CTL_SAFE_CHANGE = "/safe/change";
	protected final static String FTL_SAFE_CHANGE = "user/safe/change";
	/**
	 * 修改手机号码验证旧手机页面
	 */
	protected final static String CTL_MOBILE_CHANGE_AUTH = "/mobile/change/auth";
	protected final static String FTL_MOBILE_CHANGE_AUTH = "user/mobile/change/auth";
	/**
	 * 修改手机号码绑定新手机页面
	 */
	protected final static String CTL_MOBILE_CHANGE_BIND = "/mobile/change/bind";
	protected final static String FTL_MOBILE_CHANGE_BIND = "user/mobile/change/bind";
	
	/**
	 * 提现申请页面
	 */
	protected final static String CTL_DRAW = "/withdraw";
	protected final static String FTL_DRAW = "user/withdraw/apply";
	protected final static String CTL_DRAW_OPT_LIST = "/withdraw/transopt";
	protected final static String FTL_DRAW_OPT_LIST = "user/withdraw/transopt";
	
	/**
	 * 历史提现记录页面
	 */
	protected final static String CTL_DRAW_LIST = "/trans/withdraw";
	protected final static String FTL_DRAW_LIST = "user/trans/withdraw";
	/**
	 * 历史充值记录页面
	 */
	protected final static String CTL_CHARGE_LIST = "/trans/charge";
	public final static String FTL_CHARGE_LIST = "user/trans/charge";
	/**
	 * 交易记录明细页面
	 */
	protected final static String CTL_DETAILS = "/trans/trans";
	protected final static String FTL_DETAILS = "user/trans/trans";
	/**
	 * 提现表单提交
	 */
	protected final static String CTL_DRAW_APPLY = "/withdraw/apply";
	/**
	 * 新增绑定银行卡
	 */
	protected final static String CTL_DRAW_BANK_ADD = "/withdraw/bank/bind";
	
	/**
	 * AJAX绑定手机下发验证码
	 */
	protected final static String CTL_AJAX_MOBILE_CAPTCHA_BIND = "/mobile/captcha/bind";
	/**
	 * AJAX绑定新手机下发验证码
	 */
	protected final static String CTL_AJAX_MOBILE_CAPTCHA_BINDNEW = "/mobile/captcha/bindnew";
	/**
	 * AJAX绑定手机下发验证码,找回密码用
	 */
	protected final static String CTL_AJAX_MOBILE_CAPTCHA_FIND = "/mobile/captcha/find";
	
	protected final static String AUTH_MARK = "AUTH";
	/**
	 * 提现页面生成图形验证码
	 */
	public final static String CAPTCHA_IMAGE_GEN = "/captcha/{captchaKey}/gen";
	
	public final static String CAPTCHA_IMAGE_CHECK = "/captcha/{captchaKey}/check";
	
	protected final static String CAPTCHA_WITHDRAW = "withdraw";
	
	public final static String CAPTCHA_BASE_KEY = "captcha_";
	
	/**
	 * 订单列表页面
	 */
	protected final static String CTL_BETS_ORDER_LIST = "/order/bets";
	protected final static String FTL_BETS_ORDER_LIST = "/user/order/bets";
	protected final static String CTL_NOPAY_ORDER_LIST = "/order/nopay";
	protected final static String CTL_NOPAY_ORDER_COUNT = "/order/nopay/count";
	protected final static String FTL_NOPAY_ORDER_LIST = "/user/order/nopay";
	protected final static String CTL_ORDER_DETAIL_LIST = "/order/detail";
	protected final static String FTL_ORDER_DETAIL_LIST = "/user/order/detail";
	protected final static String CTL_ORDER_STAKE_BETS_LIST = "/order/stakebets";
	
	protected final static String MODEL_USER = "userinfo";
	protected final static String MODEL_QUESTION = "question";
	protected final static String MODEL_QUESTIONS = "questions";
	protected final static String MODEL_BANK_DRAW = "drawbanks";
	protected final static String MODEL_BANK_DRAW_CITY = "city";
	protected final static String MODEL_BANK_DRAW_PROV = "province";
	protected final static String ID_CARD_TYPES = "idCardTypes";
	protected final static String MODEL_USER_ID = "userId";
	protected final static String MODEL_USER_DATA = "userdata";
	protected final static String MODEL_USER_NAME = "username";
	protected final static String MODEL_NICK_NAME = "nickName";
	protected final static String MODEL_PARTY_NICK_NAME = "partyUserNickName";
	protected final static String USER_PWD_NEED = "pwdneed";
	protected final static String FROM_URL = "fromUrl";
	
	protected final static String MODEL_TRANS = "trans";
	protected final static String MODEL_ORDERS = "orderList";
	protected final static String MODEL_ORDER_DETAIL = "orderDetail";
	
	protected static final String MODEL_WITHDRAW_FEE = "fee";
	protected static final String MODEL_WITHDRAW_MIN = "minAmount";
	protected static final String MODEL_WITHDRAW_MAX = "maxAmount";
	protected static final String MODEL_WITHDRAW_TIMES = "maxTimes";
	
	protected final static String AUTH_OK = "1";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserCacheService userCacheService;
	
	/**
	 * 描述：用户投注记录初始化 TODO 异常和错误控制
	 */
	@RequestMapping(UserController.CTL_BETS_ORDER_LIST)
	public String userBetsOrderList(OrderQueryDto orderQueryDto, ModelMap model) {
	
		this.setUserInfo(model);
		this.initOrderQueryDto(orderQueryDto);
		orderQueryDto.setNoPayOrderFlag(false);
		orderQueryDto.setTimeValidOrderFlag(null);
		if (orderQueryDto.getTimeLevel() == null) {// 默认是3个月
			orderQueryDto.setTimeLevel(3);
		}
		OrderQueryResultDto orderQueryResultDto = orderService.findUserOrderList(orderQueryDto);
		if (!orderQueryResultDto.getOrderQueryDto().isSuccess()) {
			// 如果查询失败，返回查询表单重新提交
			return PAGE_ERROR;
		}
		model.put(MODEL_ORDERS, orderQueryResultDto);
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_bet_list", "投注记录– 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_bet_list", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_user_bet_list", ""));
		
		return FTL_BETS_ORDER_LIST;
	}
	
	/**
	 * 描述：用户未付款订单初始化
	 */
	@RequestMapping(UserController.CTL_NOPAY_ORDER_LIST)
	public String userNopayOrderList(OrderQueryDto orderQueryDto, ModelMap model) {
	
		this.setUserInfo(model);
		this.initOrderQueryDto(orderQueryDto);
		
		orderQueryDto.setNoPayOrderFlag(true);
		orderQueryDto.setPrizeStatus(null);
		orderQueryDto.setTimeLevel(null);
		OrderQueryResultDto orderQueryResultDto = orderService.findUserOrderList(orderQueryDto);
		if (!orderQueryResultDto.getOrderQueryDto().isSuccess()) {
			// 如果查询失败，返回查询表单重新提交
			return PAGE_ERROR;
		}
		model.put(MODEL_ORDERS, orderQueryResultDto);
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_unpaied_bet_list", "未付款订单– 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_unpaied_bet_list", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_user_unpaied_bet_list", ""));
		return FTL_NOPAY_ORDER_LIST;
	}
	
	/**
	 * 描述：查询用户订单明细
	 */
	@RequestMapping(UserController.CTL_ORDER_DETAIL_LIST)
	public String userOrderDetail(OrderDetailDto orderDetailDto, ModelMap model) {
	
		this.setUserInfo(model);
		
		orderDetailDto.setUserId(this.getUserId());
		if (StringUtils.isEmpty(orderDetailDto.getOrderId())) {
			log.error(LogUtil.format(CTL_ORDER_DETAIL_LIST, "illegalArgument", "order id should not empty"));
			return PAGE_ERROR;
		}
		orderDetailDto = orderService.getUserOrderDetail(orderDetailDto);
		model.put(MODEL_ORDER_DETAIL, orderDetailDto);
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_order_detail", "订单明细– 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_order_detail", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_user_order_detail", ""));
		return FTL_ORDER_DETAIL_LIST;
	}
	
	public OrderQueryDto initOrderQueryDto(OrderQueryDto orderQueryDto) {
	
		if (orderQueryDto == null) {
			orderQueryDto = new OrderQueryDto();
		}
		
		orderQueryDto.setUserId(this.getUserId());
		if (log.isDebugEnabled()) {
			log.debug(orderQueryDto);
		}
		Validate.notNull(orderQueryDto.getUserId());
		return orderQueryDto;
	}
	
	/**
	 * 描述：生成图形验证码，返回图片流
	 * 
	 * @param session
	 * @param os
	 */
	@RequestMapping(CAPTCHA_IMAGE_GEN)
	public void generateImageCaptcha(@PathVariable("captchaKey")
	String captchaKey, HttpSession session, OutputStream os) {
	
		try {
			String[] captchaStr = userService.generateImageCaptcha(os);
			if (captchaStr == null) {
				return;
			}
			if (log.isDebugEnabled()) {
				log.debug(LogUtil.format(CAPTCHA_IMAGE_GEN, null, captchaKey, session.getId(), captchaKey, captchaStr[1]));
			}
			session.setAttribute(getImageCaptchaKey(captchaKey), captchaStr[1]);
		} catch (Exception e) {
			log.error(e, e);
		}
	}
	
	public void setUserInfo(ModelMap model) {
	
		model.put(MODEL_USER_ID, this.getUserId());// 设置用户名
        UserInfoDto userInfoDto =userCacheService.queryUserInfoCache(this.getUserId());
        User user = userInfoDto.getUser();
        String partyUserNickName = userInfoDto.getPartyUserNickName();
        if (user != null) {
            model.put(MODEL_USER_DATA, user);
            model.put(MODEL_USER_NAME, user.getTrueName());// 用户真实姓名
            model.put(MODEL_NICK_NAME, user.getNickName());// 用户昵称
        }
        model.put(MODEL_PARTY_NICK_NAME,partyUserNickName);
	}
	
	public String isCompleted(ModelMap model, boolean bank) {
	
		InfoDto userDto = new InfoDto();
		userDto.setUserId(this.getUserId());
		userDto.setUserIp(this.getClientIP());
		model.put(MODEL_USER_ID, this.getUserId());// 设置用户名
		ResultUserDto<InfoResultDto> userInfo = userService.queryInfo(userDto, bank);
        UserInfoDto userInfoDto =userCacheService.queryUserInfoCache(this.getUserId());
        User user = userInfoDto.getUser();
        String partyUserNickName = userInfoDto.getPartyUserNickName();
		if (user != null) {
			model.put(MODEL_USER_DATA, user);
			model.put(MODEL_USER_NAME, user.getTrueName());// 用户真实姓名
			model.put(MODEL_NICK_NAME, user.getNickName());// 用户昵称
		}
        model.put(MODEL_PARTY_NICK_NAME,partyUserNickName);
		if (!userInfo.isSucces() && UserErrorCode.InfoNotExist.getCode() == userInfo.getRetcode()) {
			if (model != null) {
				model.put(MODEL_QUESTIONS, userService.getQuestions());
				model.put(ID_CARD_TYPES, userService.getIdCardTypes());
				model.put(USER_PWD_NEED, userService.isNeedLoginPwd(userDto.getUserId()));
			}
			return FTL_FILL_ADD;
		} else if (!userInfo.isSucces()) {
			return PAGE_ERROR;
		} else {
			if (model != null) {
				model.put(MODEL_USER, userInfo.getResult());
				if (log.isDebugEnabled()) {
					log.debug("users:" + ReflectionToStringBuilder.toString(userInfo.getResult()));
				}
			}
			return null;
		}
	}
	
	/**
	 * 如果补全返回身份信息页面，否则返回补全表单
	 * 
	 * @return
	 */
	@RequestMapping(CTL_INFO)
	public String user(ModelMap model) {
	
		String path = isCompleted(model, true);
		if (path != null) {
			return path;
		}
		List<DrawBankDto> banks = userService.getDrawBank();
		List<CityDto> cities = userService.getCity();
		List<ProvinceDto> provinces = userService.getProvince();
		model.put(MODEL_WITHDRAW_MIN, userService.getWithDrawMinAmount());
		model.put(MODEL_BANK_DRAW, banks);
		model.put(MODEL_BANK_DRAW_CITY, cities);
		model.put(MODEL_BANK_DRAW_PROV, provinces);
		
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_info_detail", "身份信息– 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_info_detail", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_user_info_detail", ""));
		
		return FTL_INFO;
	}
	
	/**
	 * 返回修改安全问题页面
	 */
	@RequestMapping(CTL_SAFE_CHANGE)
	public String changeQuestion(ModelMap model) {
	
		String path = isCompleted(model, false);
		if (path != null) {
			return path;
		}
		List<QuestionDto> questions = userService.getQuestions();
		model.put(MODEL_QUESTIONS, questions);
		InfoResultDto dto = new InfoResultDto();
		dto.setUserId(this.getUserId());
		dto.setUserIp(this.getClientIP());
		ResultUserDto<SafeResultDto> result = userService.querySafeQuestion(dto);
		model.put(MODEL_QUESTION, result.getResult().getSafeQuestion());
		
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_safe_question", "安全问题修改– 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_safe_question", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_safe_question", ""));
		
		return FTL_SAFE_CHANGE;
	}
	
	/**
	 * 如果补全返回身份信息页面，否则返回补全表单
	 * 
	 * @return
	 */
	@RequestMapping(CTL_INFO_COMPLETE)
	public String payToComplete(ModelMap model) {
	
		String path = isCompleted(model, true);
		if (path != null) {
			model.put(FROM_URL, "/login/user/order/nopay.html");
			return path;
		}
		List<DrawBankDto> banks = userService.getDrawBank();
		List<CityDto> cities = userService.getCity();
		List<ProvinceDto> provinces = userService.getProvince();
		model.put(MODEL_BANK_DRAW, banks);
		model.put(MODEL_BANK_DRAW_CITY, cities);
		model.put(MODEL_BANK_DRAW_PROV, provinces);

        model.put(MODEL_WITHDRAW_MIN, userService.getWithDrawMinAmount());

        model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_info_detail", "身份信息– 搜狗彩票"));
        model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_info_detail", ""));
        model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_user_info_detail", ""));
		return FTL_INFO;
	}
	
	/**
	 * 返回修改支付密码页
	 * 
	 * @return
	 */
	@RequestMapping(CTL_PWD_CHANGE)
	public String pwd(ModelMap model) {
	
		String path = isCompleted(model, false);
		if (path != null) {
			return path;
		}
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_password_modified", "修改支付密码 – 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_password_modified", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_password_modified", ""));
		
		return FTL_PWD;
	}
	
	/**
	 * 返回找回支付密码页面
	 * 
	 * @return
	 */
	@RequestMapping(CTL_PWD_FIND)
	public String findpwd(ModelMap model) {
	
		String path = isCompleted(model, false);
		if (path != null) {
			return path;
		}
		model.put(ID_CARD_TYPES, userService.getIdCardTypes());
		List<QuestionDto> questions = userService.getQuestions();
		model.put(MODEL_QUESTIONS, questions);
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_find_password", "找回支付密码 – 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_find_password", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_find_password", ""));
		return FTL_PWD_FIND;
	}
	
	/**
	 * 返回修改手机号码页面
	 */
	@RequestMapping(CTL_MOBILE_CHANGE_AUTH)
	public String mobileChange(ModelMap model) {
	
		String path = isCompleted(model, false);
		if (path != null) {
			return path;
		}
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_modify_phone", "修改手机号码 – 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_modify_phone", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_modify_phone", ""));
		return FTL_MOBILE_CHANGE_AUTH;
	}
	
	/**
	 * 验证当前绑定手机成功后跳转至验证新手机页面
	 */
	@RequestMapping(CTL_MOBILE_CHANGE_BIND)
	public String mobileAuth(UserDto dto, ModelMap model, HttpSession session) {
	
		String path = isCompleted(model, false);
		if (path != null) {
			return path;
		}
		String mark = (String) session.getAttribute(UserController.AUTH_MARK);
		if (UserController.AUTH_OK.equals(mark)) {
			return FTL_MOBILE_CHANGE_BIND;
		} else {
			return FTL_MOBILE_CHANGE_AUTH;
		}
	}
	
	@RequestMapping(CTL_DRAW)
	public String withDraw(ModelMap model) {
	
		String path = isCompleted(model, true);
		if (path != null) {
			return path;
		}
		List<DrawBankDto> banks = userService.getDrawBank();
		List<CityDto> cities = userService.getCity();
		List<ProvinceDto> provinces = userService.getProvince();
		model.put(MODEL_BANK_DRAW, banks);
		model.put(MODEL_BANK_DRAW_CITY, cities);
		model.put(MODEL_BANK_DRAW_PROV, provinces);
		model.put(MODEL_WITHDRAW_TIMES, userService.getWithDrawMaxTimes());
		model.put(MODEL_WITHDRAW_FEE, userService.getWithDrawFeeAmount());
		model.put(MODEL_WITHDRAW_MIN, userService.getWithDrawMinAmount());
		model.put(MODEL_WITHDRAW_MAX, userService.getWithDrawMaxAmount());
		
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_withdraw", "提款 – 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_withdraw", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_withdraw", ""));
		return FTL_DRAW;
	}
	
	public static String getImageCaptchaKey(String key) {
	
		return CAPTCHA_BASE_KEY + key;
	}
	
	@RequestMapping(CTL_CHARGE_LIST)
	public String rechargeList(TransDto dto, ModelMap model) {
	
		String path = isCompleted(model, true);
		if (path != null) {
			return path;
		}
		dto.setType(TransType.Recharge.getType());
		queryTrans(dto, model);
		return FTL_CHARGE_LIST;
	}
	
	@RequestMapping(CTL_DRAW_LIST)
	public String withDrawList(TransDto dto, ModelMap model) {
	
		String path = isCompleted(model, true);
		if (path != null) {
			return path;
		}
		dto.setType(TransType.WithDraw.getType());
		queryTrans(dto, model);
		model.put(SeoConstant.SEO_TITLE, SystemConfigs.get("seo_title_user_withdraw_list", "提款详情 – 搜狗彩票"));
		model.put(SeoConstant.SEO_KEYWORDS, SystemConfigs.get("seo_keywords_user_withdraw_list", ""));
		model.put(SeoConstant.SEO_DESCRIPTION, SystemConfigs.get("seo_description_withdraw_list", ""));
		return FTL_DRAW_LIST;
	}
	
	@RequestMapping(CTL_DRAW_OPT_LIST)
	public String withDrawOptList(TransDto dto, ModelMap model) {
	
		String path = isCompleted(model, true);
		if (path != null) {
			return path;
		}
		this.withDrawList(dto, model);
		return FTL_DRAW_OPT_LIST;
	}
	
	@RequestMapping(CTL_DETAILS)
	public String trans(TransDto dto, ModelMap model) {
	
		String path = isCompleted(model, true);
		if (path != null) {
			return path;
		}
		dto.setType(null);
		queryTrans(dto, model);
		return FTL_DETAILS;
	}
	
	@ModelAttribute
	public TransDto getTransDto(TransDto dto) {
	
		if (dto == null) {
			dto = new TransDto();
		}
		dto.setUserId(this.getUserId());
		dto.setUserIp(this.getClientIP());
		return dto;
	}
	
	private void queryTrans(TransDto dto, ModelMap model) {
	
		boolean empty = false;
		ResultUserDto<TransResultDto> res = null;
		String path = isCompleted(model, false);
		if (path != null) {
			empty = true;
		} else {
			res = userService.queryTrans(dto);
			if (!res.isSucces()) {
				empty = true;
			}
		}
		if (empty) {
			model.put(MODEL_TRANS, new TransResultDto());
		} else {
			model.put(MODEL_TRANS, res.getResult());
		}
	}
	
	public class WithdrawInfoValidator implements Validator {
		
		@Override
		public boolean supports(Class<?> clazz) {
		
			return WithdrawDto.class.equals(clazz);
		}
		
		@Override
		public void validate(Object target, Errors errors) {
		
			try {
				if (target == null) {
					errors.rejectValue("userId", null, "参数输入不能为空");
					return;
				}
				if (target instanceof WithdrawDto) {
					WithdrawDto dto = (WithdrawDto) target;
					dto.setUserIp(getClientIP());
					// if (StringUtils.isBlank(withdrawDto.getBankCardNo())) {
					// errors.rejectValue("bankCardNo", null, "请添加银行卡号");
					// }
					// if (StringUtils.isNotBlank(withdrawDto.getBankCardNo())
					// && withdrawDto.getBankCardNo().length() > 100) {
					// errors.rejectValue("bankCardNo", null, "请添加正确的银行卡号");
					// }
					if (dto.getAmount() == null || dto.getAmount() <= 0) {
						errors.rejectValue("amount", null, "请输入正确的提款金额");
					}
					if (StringUtils.isBlank(dto.getCaptcha())) {
						errors.rejectValue("captcha", null, "图形验证码不能为空");
					}
					if (StringUtils.isNotBlank(dto.getCaptcha()) && dto.getCaptcha().length() > 20) {
						errors.rejectValue("captcha", null, "请输入正确的图形验证码");
					}
				}
			} catch (Exception e) {
				errors.rejectValue("userId", null, "参数输入错误");
				log.error(e, e);
			}
		}
	}
	
	public class InfoValidator implements Validator {
		
		public boolean supports(Class<?> clazz) {
		
			return InfoDto.class.equals(clazz);
		}
		
		private boolean needLoginPwd = true;
		
		public InfoValidator() {
		
			needLoginPwd = true;
		}
		
		public InfoValidator(boolean needLoginPwd) {
		
			this.needLoginPwd = needLoginPwd;
		}
		
		public void validate(Object obj, Errors errors) {
		
			try {
				if (obj == null) {
					errors.rejectValue("userId", null, "参数不能为空");
					return;
				}
				if (obj instanceof InfoDto) {
					InfoDto info = (InfoDto) obj;
					info.setUserId(UserController.this.getUserId());
					info.setUserIp(UserController.this.getClientIP());
					if (StringUtils.isBlank(info.getUserId())) {
						errors.rejectValue("userId", null, "用户名不能为空");
					}
					if (StringUtils.isBlank(info.getUserIp())) {
						errors.rejectValue("userIp", null, "用户名不能为空");
					}
					if (needLoginPwd && StringUtils.isBlank(info.getPwd())) {
						errors.rejectValue("pwd", null, "登陆密码不能为空");
					}
					if (StringUtils.isBlank(info.getPayPwd())) {
						errors.rejectValue("payPwd", null, "支付密码不能为空");
					}
					if (StringUtils.isBlank(info.getPayPwdConfirm())) {
						errors.rejectValue("payPwdConfirm", null, "支付密码确认不能为空");
					}
					if (info.getQuestionType() == null) {
						errors.rejectValue("questionType", null, "安全问题不能为空");
					}
					if (StringUtils.isBlank(info.getSafeAnswer())) {
						errors.rejectValue("safeAnswer", null, "安全问题答案不能为空");
					}
					if (StringUtils.isBlank(info.getTrueName())) {
						errors.rejectValue("trueName", null, "真实姓名不能为空");
					}
					if (StringUtils.isBlank(info.getNickName())) {
						errors.rejectValue("nickName", null, "昵称不能为空");
					}
					if (info.getIdCardType() == null) {
						errors.rejectValue("idCardType", null, "身份证件类型不能为空");
					}
					if (StringUtils.isBlank(info.getIdCardNo())) {
						errors.rejectValue("idCardNo", null, "身份证件号码不能为空");
					}
					if (StringUtils.isBlank(info.getMobile())) {
						errors.rejectValue("mobile", null, "手机号码不能为空");
					}
					if (StringUtils.isBlank(info.getCaptcha())) {
						errors.rejectValue("captcha", null, "验证码不能为空");
					}
					if (info.getPayPwdConfirm() != null && info.getPayPwd() != null && !info.getPayPwdConfirm().equals(info.getPayPwd())) {
						errors.rejectValue("payPwdConfirm", null, "两次输入的支付密码不一致");
					}
					if (errors.hasErrors()) {
						return;
					}
					if (info.getIdCardType() != null && IdCardType.Id.getType() == info.getIdCardType() && info.getIdCardNo() != null && !new IDCardVeryUtils().verify(info.getIdCardNo())) {
						errors.rejectValue("idCardNo", null, "身份证件号码格式错误");
					}
					if (info.getMobile() != null && !isMobileNO(info.getMobile())) {
						errors.rejectValue("mobile", null, "手机号码格式错误");
					}
					if (!isLengthValid(info.getNickName())) {
						errors.rejectValue("nickName", null, "昵称长度超过限制");
					}
					if (!isLengthValid(info.getTrueName())) {
						errors.rejectValue("trueName", null, "真实姓名长度超过限制");
					}
				}
			} catch (Exception e) {
				errors.rejectValue("userId", null, "参数输入错误");
				log.error(e, e);
			}
		}
		
		private boolean isLengthValid(String name) {
		
			int len = 0;
			char[] chars = name.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (isChinese(chars[i])) {
					len += 2;
				} else {
					len += 1;
				}
			}
			if (len < 4 || len > 32) {
				return false;
			}
			return true;
		}
		
		/**
		 * 描述：手机号码校验
		 * 
		 * @param mobiles
		 * @return
		 */
		public boolean isMobileNO(String mobiles) {
		
			boolean flag = false;
			try {
				Pattern p = Pattern.compile("^0?(13[0-9]|15[0-9]|18[0-9]|14[57])[0-9]{8}$");
				Matcher m = p.matcher(mobiles);
				flag = m.matches();
			} catch (Exception e) {
				flag = false;
			}
			return flag;
		}
		
		public boolean isChinese(char c) {
		
			boolean result = false;
			if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result = true;
			}
			return result;
		}
	}
}
