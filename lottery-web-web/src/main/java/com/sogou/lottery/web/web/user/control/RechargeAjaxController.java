package com.sogou.lottery.web.web.user.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.web.service.user.dto.RechargeDto;
import com.sogou.lottery.web.service.user.dto.RechargeResultDto;
import com.sogou.lottery.web.service.user.service.UserService;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/ajax/login/charge")
public class RechargeAjaxController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 旧窗口写入rechargeId 新窗口redirect到钱包再到银行
	 * 
	 * @param recharge
	 * @return
	 */
	@Deprecated
	@RequestMapping(RechargeController.CTL_CHAEGE_DO)
	public ResultUserDto<RechargeResultDto> recharge(RechargeDto recharge, BindingResult errors) {
	
		try {
			new RechargeController().new RechargeValidator().validate(recharge, errors);
			if (errors.hasErrors()) {
				ResultUserDto<RechargeResultDto> result = new ResultUserDto<>(UserErrorCode.CommonArgument);
				return result;
			}
			return userService.doRecharge(recharge);
		} catch (Exception e) {
			log.error(e, e);
			return new ResultUserDto<>(UserErrorCode.CommonError);
		}
	}
}
