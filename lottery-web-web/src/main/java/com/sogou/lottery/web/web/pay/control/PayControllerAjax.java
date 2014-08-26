package com.sogou.lottery.web.web.pay.control;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.pay.PayErrorCode;
import com.sogou.lottery.base.pay.dto.PayResultDto;
import com.sogou.lottery.base.pay.dto.ResultPayDto;
import com.sogou.lottery.web.service.pay.dto.PayCheckResultDto;
import com.sogou.lottery.web.service.pay.dto.PayDto;
import com.sogou.lottery.web.service.pay.service.PayService;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/ajax/login/pay")
public class PayControllerAjax extends BaseController {
	
	@Autowired
	private PayService payService;
	
	@RequestMapping("/check")
	@ResponseBody
	public ResultPayDto<PayCheckResultDto> check(PayDto dto, BindingResult errors) {
	
		new PayValidator(false).validate(dto, errors);
		if (errors.hasErrors()) {
			printError(errors);
			return new ResultPayDto<>(PayErrorCode.CommonArgument.getCode(), getFirstError(errors));
		}
		ResultPayDto<PayCheckResultDto> result = null;
		try {
			result = payService.check(dto);
		} catch (Exception e) {
			log.error(e, e);
			result = new ResultPayDto<>(PayErrorCode.CommonBusy);
		}
		return result;
	}
	
	@RequestMapping("/order")
	@ResponseBody
	public ResultPayDto<PayResultDto> pay(PayDto dto, BindingResult errors) {
	
		new PayValidator().validate(dto, errors);
		if (errors.hasErrors()) {
			printError(errors);
			return new ResultPayDto<>(PayErrorCode.CommonArgument.getCode(), getFirstError(errors));
		}
		ResultPayDto<PayResultDto> result = null;
		try {
			result = payService.payOrder(dto);
			if (log.isDebugEnabled()) {
				log.debug(result.getRetcode());
			}
		} catch (Exception e) {
			log.error(e, e);
			result = new ResultPayDto<PayResultDto>(PayErrorCode.CommonBusy);
		}
		return result;
	}
	
	public class PayValidator implements Validator {
		
		PayValidator() {
		
		}
		
		PayValidator(boolean payFlag) {
		
			this.payFlag = payFlag;
		}
		
		private boolean payFlag = true;
		
		public boolean supports(Class<?> clazz) {
		
			return PayDto.class.equals(clazz);
		}
		
		public void validate(Object obj, Errors errors) {
		
			try {
				if (obj == null) {
					errors.rejectValue("userId", null, "参数不能为空");
					return;
				}
				if (obj instanceof PayDto) {
					PayDto dto = (PayDto) obj;
					dto.setUserId(getUserId());
					dto.setUserIp(getClientIP());
					if (StringUtils.isBlank(dto.getUserIp())) {
						errors.rejectValue("userIp", null, "用户IP不能为空");
					}
					if (StringUtils.isBlank(dto.getPayOrderId())) {
						errors.rejectValue("payOrderId", null, "支付订单号不能为空");
					}
					if (payFlag && StringUtils.isBlank(dto.getPayPwd())) {
						errors.rejectValue("payPwd", null, "支付密码不能为空");
					}
				}
			} catch (Exception e) {
				log.error(e, e);
				errors.rejectValue("userId", null, "参数有误");
				return;
			}
		}
	}
}
