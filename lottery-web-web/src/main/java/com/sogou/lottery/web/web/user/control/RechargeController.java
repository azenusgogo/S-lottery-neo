package com.sogou.lottery.web.web.user.control;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.vo.user.User;
import com.sogou.lottery.web.service.qianbao.service.QianBaoService;
import com.sogou.lottery.web.service.user.dto.RechargeBackDto;
import com.sogou.lottery.web.service.user.dto.RechargeDto;
import com.sogou.lottery.web.service.user.dto.RechargeResultDto;
import com.sogou.lottery.web.service.user.dto.UserInfoDto;
import com.sogou.lottery.web.service.user.service.UserCacheService;
import com.sogou.lottery.web.service.user.service.UserService;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/login/charge")
public class RechargeController extends BaseController {
	
	/**
	 * 充值选择银行页面
	 */
	protected final static String CTL_CHAEGE = "/pre";
	protected final static String FTL_CHAEGE = "user/charge/pre";
	/**
	 * 充值确认页面
	 */
	protected final static String CTL_CHAEGE_CONFIRM = "/confirm";
	protected final static String FTL_CHAEGE_CONFIRM = "user/charge/confirm";
	/**
	 * 充值提交表单AJAX
	 */
	protected final static String CTL_CHAEGE_DO = "/charge";
	/**
	 * 充完成后点击成功表单
	 */
	protected final static String CTL_CHAEGE_DONE = "/done";
	/**
	 * 在银行完成操作后回调地址
	 */
	protected final static String CTL_CHAEGE_BACK = "/back";
	
	protected final static String MODEL_ORDER = "payOrderId";
	protected final static String MODEL_BANK = "chargeBank";
	protected static final String MODEL_RECHARGE = "recharge";
	protected static final String MODEL_MIN_CHARGE = "minAmount";
	protected static final String MODEL_MAX_CHARGE = "maxAmount";
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserCacheService userCacheService;
	@Autowired
	private QianBaoService qianBaoService;
	
	private RechargeDto bindRecharge(String payOrderId, String amount, String bankId) {
	
		RechargeDto dto = new RechargeDto();
		try {
			dto.setUserId(getUserId());
			dto.setUserIp(getClientIP());
			if (StringUtils.isNotBlank(amount)) {
				if (amount.endsWith("#")) {
					amount = amount.replace("#", "");
				}
				dto.setAmount(Long.parseLong(amount));
			}
			if (StringUtils.isNotBlank(bankId)) {
				if (bankId.endsWith("#")) {
					bankId = amount.replace("#", "");
				}
				dto.setBankId(bankId);
			}
			dto.setPayOrderId(payOrderId);
		} catch (Exception e) {
			log.error(e, e);
		}
		return dto;
	}
	
	/**
	 * 进入充值选择银行页面
	 * 
	 * @return
	 */
	@RequestMapping(CTL_CHAEGE)
	public String pre(@RequestParam(required = false)
	String payOrderId, @RequestParam(required = false)
	String amount, @RequestParam(required = false)
	String bankId, ModelMap model) {
	
		RechargeDto dto = bindRecharge(payOrderId, amount, bankId);
		RechargeResultDto result = new RechargeResultDto();
		dto.setUserId(getUserId());
		UserInfoDto userInfoDto = userCacheService.queryUserInfoCache(this.getUserId());
		User user = userInfoDto.getUser();
		String partyUserNickName = userInfoDto.getPartyUserNickName();
		if (user != null) {
			model.put(UserController.MODEL_NICK_NAME, user.getNickName());// 用户昵称
		}
		model.put(UserController.MODEL_PARTY_NICK_NAME, partyUserNickName);
		model.put(UserController.MODEL_USER_ID, this.getUserId());// 设置用户名
		result.setRechargeDto(dto);
		// payOrderId用来标示充值完成后是否跳回我的订单
		if (StringUtils.isNotBlank(dto.getPayOrderId())) {
			model.put(MODEL_ORDER, dto.getPayOrderId());
		}
		model.put(MODEL_RECHARGE, result);
		model.put(MODEL_MIN_CHARGE, userService.getRechargeMinAmount());
		model.put(MODEL_MAX_CHARGE, userService.getRechargeMaxAmount());
		model.put(MODEL_BANK, userService.getChargeBank());
		return FTL_CHAEGE;
	}
	
	/**
	 * 充值确认信息页面 先生成 PAGE_ERROR处理
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(CTL_CHAEGE_CONFIRM)
	public String confirm(@RequestParam(required = false)
	String payOrderId, @RequestParam(required = false)
	String amount, @RequestParam(required = false)
	String bankId, ModelMap model) {
	
		RechargeDto dto = bindRecharge(payOrderId, amount, bankId);
		BindingResult errors = new BindException(dto, "dto");
		new RechargeValidator().validate(dto, errors);
		// 充值参数有错，直接返回错误页面
		if (errors.hasErrors()) {
			printError(errors);
			return PAGE_ERROR;
		}
		UserInfoDto userInfoDto = userCacheService.queryUserInfoCache(this.getUserId());
		User user = userInfoDto.getUser();
		String partyUserNickName = userInfoDto.getPartyUserNickName();
		if (user != null) {
			model.put(UserController.MODEL_NICK_NAME, user.getNickName());// 用户昵称
		}
		model.put(UserController.MODEL_PARTY_NICK_NAME, partyUserNickName);
		model.put(UserController.MODEL_USER_ID, this.getUserId());// 设置用户名
		ResultUserDto<RechargeResultDto> result = userService.comfirmRecharge(dto);
		if (!result.isSucces()) {
			return PAGE_ERROR;
		}
		model.put(MODEL_RECHARGE, result.getResult());
		return FTL_CHAEGE_CONFIRM;
	}
	
	/**
	 * 旧窗口写入rechargeId 新窗口redirect到钱包再到银行 前台新窗口打开此链接，并且form提交表单,后台对新开的窗口做自动跳转
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(RechargeController.CTL_CHAEGE_DO)
	public String doRecharge(@RequestParam(required = false)
	String id, ModelMap model) {
	
		if (StringUtils.isBlank(id)) {
			if (log.isDebugEnabled()) {
				log.debug("chargeId must not be null");
			}
			return PAGE_ERROR;
		}
		RechargeDto dto = new RechargeDto();
		dto.setId(id);
		ResultUserDto<RechargeResultDto> result = userService.doRecharge(dto);
		if (result.getRetcode() == UserErrorCode.ChargeProccesing.getCode()) {
			return "forward:/login/charge" + CTL_CHAEGE + ".html";
		}
		if (!result.isSucces() || result.getResult() == null || StringUtils.isEmpty(result.getResult().getReturl())) {
			return PAGE_ERROR;
		}
		return "redirect:" + result.getResult().getReturl();
	}
	
	/**
	 * 充值完成后跳转.直接在充值中心充值，此步后跳转到“资金明细”下的“充值记录”页面 在购买时余额不足充值，此步后跳转到“我的订单—未支付订单”页面
	 * 点击时有校验，如果用户充值不成功，则跳转到“资金明细”下的“充值记录”页面
	 * 
	 * @return
	 */
	@RequestMapping(CTL_CHAEGE_DONE)
	public String done(@RequestParam(required = false)
	String payOrderId, @RequestParam(required = false)
	String id) {
	
		if (StringUtils.isBlank(id)) {
			if (log.isDebugEnabled()) {
				log.debug("chargeId must not be null");
			}
			return PAGE_ERROR;
		}
		// 充值已完成不需要校验其他参数
		// new RechargeValidator(userService).validate(rechargeDto, errors);
		// // 充值参数有错，直接返回错误页面
		// if (errors.hasErrors()) {
		// return PAGE_ERROR;
		// }
		RechargeDto dto = new RechargeDto();
		dto.setId(id);
		dto.setPayOrderId(payOrderId);
		ResultUserDto<RechargeResultDto> result = userService.doRecharge(dto);
		if (log.isDebugEnabled() && result.isSucces()) {
			log.debug(result);
		}
		if (result.isSucces() && StringUtils.isNotBlank(dto.getPayOrderId())) {
			return "forward:/login/user" + UserController.CTL_BETS_ORDER_LIST + ".html";
		}
		return "forward:/login/user" + UserController.CTL_CHARGE_LIST + ".html";
	}
	
	@RequestMapping(CTL_CHAEGE_BACK)
	public String done(RechargeBackDto dto) throws IOException {
	
		if (dto == null || StringUtils.isBlank(dto.getError_code()) || StringUtils.isBlank(dto.getError_desc()) || StringUtils.isBlank(dto.getPs_requestId()) || StringUtils.isBlank(dto.getPsid()) || StringUtils.isBlank(dto.getSignstr()) || StringUtils.isBlank(dto.getTransnum())) {
			if (log.isDebugEnabled()) {
				log.debug(dto);
			}
		}
		int res = userService.doneRecharge(dto);
		if (res >= 0 && StringUtils.isNotBlank(dto.getPayOrderId())) {
			return "forward:/login/user" + UserController.CTL_BETS_ORDER_LIST + ".html";
		} else {
			return "forward:/login/user" + UserController.CTL_CHARGE_LIST + ".html";
		}
	}
	
	public class RechargeValidator implements Validator {
		
		public boolean supports(Class<?> clazz) {
		
			return RechargeDto.class.equals(clazz);
		}
		
		public void validate(Object obj, Errors errors) {
		
			if (obj == null) {
				errors.rejectValue("userId", null, "参数不能为空");
				return;
			}
			if (obj instanceof RechargeDto) {
				RechargeDto dto = (RechargeDto) obj;
				dto.setUserId(getUserId());
				dto.setUserIp(getClientIP());
				if (StringUtils.isBlank(dto.getBankId())) {
					errors.rejectValue("bankId", null, "银行不能为空");
				}
				if (dto.getAmount() == null || dto.getAmount() <= 0) {
					errors.rejectValue("amount", null, "充值金额必须大于0");
				}
				// ValidationUtils.rejectIfEmptyOrWhitespace(errors, "token",
				// "require.token", "令牌不能为空");
				
			}
		}
	}
}
