package com.sogou.lottery.web.web.user.control;

import javax.servlet.http.HttpSession;

import com.sogou.lottery.web.service.user.dto.*;
import com.sogou.lottery.web.service.user.service.UserCacheService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.vo.user.User;
import com.sogou.lottery.web.service.order.service.OrderService;
import com.sogou.lottery.web.service.qianbao.service.QianBaoService.CaptchaType;
import com.sogou.lottery.web.service.user.service.UserService;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/ajax/login/user")
public class UserControllerAjax extends BaseController {
	
	private Log LOG = LogFactory.getLog(getClass());
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserCacheService userCacheService;
	
	/**
	 * 描述：查询未付款订单总数
	 */
	@RequestMapping(UserController.CTL_NOPAY_ORDER_COUNT)
	@ResponseBody
	public ResultUserDto<Integer> userNopayOrderList(OrderQueryDto orderQueryDto, ModelMap model) {
	
		orderQueryDto.setUserId(this.getUserId());
		return orderService.findNopayUserOrderNums(orderQueryDto);
	}
	
	@RequestMapping(UserController.CTL_DRAW_APPLY)
	@ResponseBody
	public ResultUserDto<CommonDto> withDrawApply(WithdrawDto dto, BindingResult errors, HttpSession session) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<>(UserErrorCode.InfoNotExist);
		}
		new UserController().new WithdrawInfoValidator().validate(dto, errors);
		// 提款传参校验失败，重新提交
		if (errors.hasErrors()) {
			return new ResultUserDto<>(UserErrorCode.CommonArgument.getCode(), getFirstError(errors));
		}
		
		String correctCaptchaStr = (String) session.getAttribute(UserController.getImageCaptchaKey(UserController.CAPTCHA_WITHDRAW));
		if (LOG.isDebugEnabled()) {
			LOG.debug("sessionid:" + session.getId() + ",captchaStr:" + dto.getCaptcha() + ",correctCaptchaStr:" + correctCaptchaStr);
		}
		
		if (!userService.checkImageCaptcha(dto.getCaptcha(), correctCaptchaStr)) {
			return new ResultUserDto<>(UserErrorCode.ImageCaptcha);
		}
		return userService.withdrawApply(dto);
	}
	
	/**
	 * 返回通过密保问题和手机找回支付密码页面
	 * 
	 * @return
	 */
	@RequestMapping(UserController.CTL_PWD_FIND_CURRENT_SAFE)
	@ResponseBody
	public ResultUserDto<SafeResultDto> getUserCurrentPwdQuestion(ModelMap model) {
	
		InfoResultDto dto = new InfoResultDto();
		dto.setUserId(this.getUserId());
		dto.setUserIp(this.getClientIP());
		ResultUserDto<SafeResultDto> result = userService.querySafeQuestion(dto);
		String question = !result.isSucces() || result.getResult() == null ? null : result.getResult().getSafeQuestion();
		if (question == null) {
			return new ResultUserDto<>(UserErrorCode.CommonNetwork);
		}
		return result;
	}
	
	@RequestMapping(UserController.CTL_ORDER_STAKE_BETS_LIST)
	@ResponseBody
	public ResultUserDto<OrderDetailDto> findStakeBetListByOrder(OrderDetailDto orderDetailDto) {
	
		if (orderDetailDto == null) orderDetailDto = new OrderDetailDto();
		orderDetailDto.setUserId(this.getUserId());
		
		if (orderDetailDto.getPageNo() == null || orderDetailDto.getPageNo() < 1) {
			orderDetailDto.setPageNo(1);
		}
		if (orderDetailDto.getPageSize() == null || orderDetailDto.getPageSize() < 1 || orderDetailDto.getPageSize() > 20) {
			orderDetailDto.setPageNo(10);
		}
		if (StringUtils.isEmpty(orderDetailDto.getOrderId())) {
			return new ResultUserDto<OrderDetailDto>(UserErrorCode.CommonArgument);
		}
		orderDetailDto = orderService.findStakeBetListByOrderByPage(orderDetailDto);
        orderDetailDto.setOrderInfo(null);//避免不必要的数据传送
		return new ResultUserDto<OrderDetailDto>(orderDetailDto);
	}
	
	/**
	 * 描述:图形码验证，传参与session存储图形字符串比对
	 * 
	 * @param captchaStr
	 * @param session
	 * @return
	 */
	@RequestMapping(UserController.CAPTCHA_IMAGE_CHECK)
	@ResponseBody
	public ResultUserDto<Boolean> checkImageCaptcha(@PathVariable("captchaKey")
	String captchaKey, @RequestParam("captcha")
	String captchaStr, HttpSession session) {
	
		String correctCaptchaStr = (String) session.getAttribute(UserController.getImageCaptchaKey(captchaKey));
		if (LOG.isDebugEnabled()) {
			LOG.debug("sessionid:" + session.getId() + ",captchaKey:" + captchaKey + ",captchaStr:" + captchaStr + ",correctCaptchaStr:" + correctCaptchaStr);
		}
		if (userService.checkImageCaptcha(captchaStr, correctCaptchaStr)) {
			ResultUserDto<Boolean> resultDto = new ResultUserDto<>(UserErrorCode.ImageCaptcha);
			return resultDto;
		}
		return new ResultUserDto<>(UserErrorCode.CommonOK);
	}
	
	/**
	 * 补全信息表单提交,验证成功后返回用户信息页，失败则返回当前页面
	 * 
	 * @param infoDto
	 * @param result
	 * @return
	 */
	@RequestMapping(UserController.CTL_INFO_ADD)
	@ResponseBody
	public ResultUserDto<InfoResultDto> info(InfoDto infoDto, BindingResult result) {
	
		boolean needPwd = userService.isNeedLoginPwd(infoDto.getUserId());
		new UserController().new InfoValidator(needPwd).validate(infoDto, result);
		if (result.hasErrors()) {
			ResultUserDto<InfoResultDto> resultDto = new ResultUserDto<>(UserErrorCode.CommonArgument.getCode(), getFirstError(result));
			return resultDto;
		}
		
		ResultUserDto<InfoResultDto> userInfo = userService.completeInfo(infoDto);
		return userInfo;
	}
	
	public boolean isCompleted(UserDto dto) {
	
		UserDto user = getUser();
		if (dto != null) {
			dto.setUserId(user.getUserId());
			dto.setUserIp(dto.getUserIp());
		} else {
			dto = user;
		}
		InfoDto info = new InfoDto();
		info.setUserId(dto.getUserId());
		info.setUserIp(dto.getUserIp());
		ResultUserDto<InfoResultDto> userInfo = userService.queryInfo(info, false);
		if (!userInfo.isSucces()) {
			return false;
		} else {
			if (StringUtils.isEmpty(dto.getMobile())) {
				dto.setMobile(userInfo.getResult().getMobile());
			}
			// user.setEmail(userInfo.getResult().getEmail());
			return true;
		}
	}

    @RequestMapping(UserController.CTL_INFO_BASE)
    @ResponseBody
    public ResultUserDto<UserResultDto> queryUser() {

        UserInfoDto userInfoDto = userCacheService.queryUserInfoCache(this.getUserId());
        User user = userInfoDto.getUser();
        String partyUserNickName = userInfoDto.getPartyUserNickName();
        ResultUserDto<UserResultDto> res = null;
        if (user == null && partyUserNickName == null) {
            res = new ResultUserDto<>(UserErrorCode.UserExist);
        } else {
            if (user == null) {
                user = new User();
                user.setUserId(this.getUserId());
            }
            UserResultDto userResultDto = new UserResultDto();
            userResultDto.setUser(user);
            userResultDto.setPartyUserNickName(partyUserNickName);
            res = new ResultUserDto<>(userResultDto);
        }
        return res;
    }

    /**
	 * 查询用户钱包余额
	 * 
	 * @return
	 */
	@RequestMapping(UserController.CTL_BALANCE)
	@ResponseBody
	public ResultUserDto<BalanceResultDto> queryBalance() {
	
		UserDto dto = getUser();
		if (!this.isCompleted(dto)) {
			new ResultUserDto<>(UserErrorCode.InfoNotExist);
		}
		ResultUserDto<BalanceDto> balance = userService.queryBalance(dto);
		if (!balance.isSucces()) {
			return new ResultUserDto<>(balance.getRetcode(), balance.getRetdesc());
		}
		BalanceResultDto br = new BalanceResultDto();
		br.setBalance(balance.getResult());
		return new ResultUserDto<>(br);
	}
	
	@RequestMapping(UserController.CTL_BALANCE_WITHDRAW)
	@ResponseBody
	public ResultUserDto<BalanceResultDto> queryBalanceWithDraw() {
	
		UserDto dto = getUser();
		if (!this.isCompleted(dto)) {
			new ResultUserDto<>(UserErrorCode.InfoNotExist);
		}
		ResultUserDto<BalanceDto> balance = userService.queryBalance(dto);
		if (!balance.isSucces()) {
			return new ResultUserDto<>(balance.getRetcode(), balance.getRetdesc());
		}
		Long sum = userService.queryWithDrawApplying(dto);
		BalanceResultDto br = new BalanceResultDto();
		br.setBalance(balance.getResult());
		br.setWithDrawApply(sum);
		return new ResultUserDto<>(br);
	}
	
	/**
	 * 修改支付密码,ajax调用，失败后弹窗
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(UserController.CTL_PWD_CHANGE)
	@ResponseBody
	public ResultUserDto<CommonDto> chgpwd(ChangePwdDto dto, BindingResult result, ModelMap model) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<>(UserErrorCode.InfoNotExist);
		}
		new ChangePwdValidator().validate(dto, result);
		if (result.hasErrors()) {
			return new ResultUserDto<>(UserErrorCode.CommonArgument);
		}
		return userService.changePayPwd(dto);
	}
	
	/**
	 * 下发手机验证码其他用
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(UserController.CTL_AJAX_MOBILE_CAPTCHA_BIND)
	@ResponseBody
	public ResultUserDto<CommonDto> queryCaptchaForBinding(UserDto dto) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<CommonDto>(UserErrorCode.InfoNotExist);
		}
		return userService.queryCaptcha(dto, CaptchaType.BingMobile);
	}
	
	/**
	 * 新手机号 下发手机验证码其他用
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(UserController.CTL_AJAX_MOBILE_CAPTCHA_BINDNEW)
	@ResponseBody
	public ResultUserDto<CommonDto> queryCaptchaForNewMobileBinding(UserDto dto) {
	
		if (StringUtils.isEmpty(dto.getMobile())) {
			return new ResultUserDto<>(UserErrorCode.CommonArgument);
		}
		dto.setUserId(this.getUserId());
		dto.setUserIp(this.getClientIP());
		return userService.queryCaptcha(dto, CaptchaType.BingMobile);
	}
	
	/**
	 * 下发手机验证码找回密码用
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(UserController.CTL_AJAX_MOBILE_CAPTCHA_FIND)
	@ResponseBody
	public ResultUserDto<CommonDto> queryCaptchaForFinding(UserDto dto) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<CommonDto>(UserErrorCode.InfoNotExist);
		}
		return userService.queryCaptcha(dto, CaptchaType.FindPwd);
	}
	
	/**
	 * 通过身份信息和绑定手机找回支付密码, ajax请求，弹窗显示结果
	 */
	@RequestMapping(UserController.CTL_PWD_FIND_INFO)
	@ResponseBody
	public ResultUserDto<CommonDto> findpwdByInfo(FindPwdByMobileDto dto) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<CommonDto>(UserErrorCode.InfoNotExist);
		}
		return userService.findPwdByInfo(dto);
	}
	
	/**
	 * 通过密保问题和绑定手机找回支付密码, ajax请求，弹窗显示结果
	 */
	@RequestMapping(UserController.CTL_PWD_FIND_SAFE)
	@ResponseBody
	public ResultUserDto<CommonDto> findpwdByQuestion(FindPwdBySafeDto dto) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<CommonDto>(UserErrorCode.InfoNotExist);
		}
		return userService.findPwdByQuestion(dto);
	}
	
	/**
	 * 修改安全问题, ajax请求，弹窗显示结果
	 */
	@RequestMapping(UserController.CTL_SAFE_CHANGE)
	@ResponseBody
	public ResultUserDto<CommonDto> changeQuesion(ChangeSafeDto dto) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<CommonDto>(UserErrorCode.InfoNotExist);
		}
		return userService.changeSafe(dto);
	}
	
	/**
	 * 验证当前绑定手机, ajax请求，弹窗显示结果
	 */
	@RequestMapping(UserController.CTL_MOBILE_CHANGE_AUTH)
	@ResponseBody
	public ResultUserDto<CommonDto> mobileAuth(UserDto dto, ModelMap model, HttpSession session) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<CommonDto>(UserErrorCode.InfoNotExist);
		}
		ResultUserDto<CommonDto> result = userService.bindMobile(dto, false);
		if (result.isSucces()) {
			session.setAttribute(UserController.AUTH_MARK, UserController.AUTH_OK);
		} else {
			session.removeAttribute(UserController.AUTH_MARK);
		}
		return result;
	}
	
	/**
	 * 绑定新手机, ajax请求，弹窗显示结果
	 */
	@RequestMapping(UserController.CTL_MOBILE_CHANGE_BIND)
	@ResponseBody
	public ResultUserDto<CommonDto> mobileBind(UserDto dto, ModelMap model, HttpSession session) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<CommonDto>(UserErrorCode.InfoNotExist);
		}
		String mark = (String) session.getAttribute(UserController.AUTH_MARK);
		if (UserController.AUTH_OK.equals(mark)) {
			ResultUserDto<CommonDto> result = userService.bindMobile(dto, true);
			if (result.isSucces()) {
				session.removeAttribute(UserController.AUTH_MARK);
			}
			return result;
		} else {
			session.removeAttribute(UserController.AUTH_MARK);
			return new ResultUserDto<CommonDto>(UserErrorCode.MobileAuth);
		}
	}
	
	/**
	 * 绑定提现银行
	 * 
	 * @param dto
	 * @param model
	 * @return
	 */
	@RequestMapping(UserController.CTL_DRAW_BANK_ADD)
	@ResponseBody
	public ResultUserDto<CommonDto> bindBank(BindCardDto dto, BindingResult result, ModelMap model) {
	
		if (!isCompleted(dto)) {
			return new ResultUserDto<CommonDto>(UserErrorCode.InfoNotExist);
		}
		new BindCardValidator().validate(dto, result);
		if (result.hasErrors()) {
			return new ResultUserDto<CommonDto>(UserErrorCode.CommonArgument);
		}
		return userService.bindBank(dto);
	}
	
	public class ChangePwdValidator implements Validator {
		
		public boolean supports(Class<?> clazz) {
		
			return ChangePwdDto.class.equals(clazz);
		}
		
		public void validate(Object obj, Errors errors) {
		
			if (obj instanceof ChangePwdDto) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "require.userId", "用户名不能为空");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payPwd", "require.payPwd", "支付密码不能为空");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPayPwd", "require.newPayPwd", "新支付密码不能为空");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPayPwdConfirm", "require.newPayPwdConfirm", "新支付密码确认不能为空");
				ChangePwdDto dto = (ChangePwdDto) obj;
				if (dto.getPayPwd().equals(dto.getNewPayPwd())) {
					errors.rejectValue("newPayPwd", "error.newPayPwd", "新旧支付密码不能一样");
				}
				if (!dto.getNewPayPwdConfirm().equals(dto.getNewPayPwd())) {
					errors.rejectValue("newPayPwdConfirm", "error.newPayPwdConfirm", "两次输入的支付密码不一致");
				}
			}
		}
	}
	
	public class BindCardValidator implements Validator {
		
		public boolean supports(Class<?> clazz) {
		
			return BindCardDto.class.equals(clazz);
		}
		
		public void validate(Object obj, Errors errors) {
		
			if (obj instanceof BindCardDto) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "require.userId", "用户名不能为空");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bankId", "require.bankId", "提现银行不能为空");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bankCardNo", "require.bankCardNo", "提现银行卡号不能为空");
			}
		}
	}
}
