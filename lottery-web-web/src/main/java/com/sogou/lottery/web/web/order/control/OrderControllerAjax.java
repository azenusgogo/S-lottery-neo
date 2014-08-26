package com.sogou.lottery.web.web.order.control;

import com.sogou.lottery.base.dto.ResultDto;
import com.sogou.lottery.base.user.UserErrorCode;
import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.web.service.user.dto.CommonDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.lottery.base.order.OrderErrorCode;
import com.sogou.lottery.base.order.dto.BetResultDto;
import com.sogou.lottery.base.order.dto.ResultOrderDto;
import com.sogou.lottery.base.order.dto.UserBetDto;
import com.sogou.lottery.base.vo.game.GameRule;
import com.sogou.lottery.base.vo.game.GameRuleFactory;
import com.sogou.lottery.web.service.order.service.OrderService;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/ajax/login/order")
public class OrderControllerAjax extends BaseController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/close/{orderId}")
	@ResponseBody
	public ResultDto<CommonDto> closeOrder(@PathVariable("orderId")
	String orderId) {
	
		if (StringUtils.isBlank(orderId)) {
			return new ResultUserDto<>(UserErrorCode.CommonArgument);
		}
		try {
			// TODO 这里有问题 订单关闭不是置成关闭状态，是加一个字段，表示订单是否隐藏/删除
			orderService.closeOrder(orderId);
		} catch (Exception e) {
			log.error(e, e);
			return new ResultUserDto<>(UserErrorCode.CommonError);
		}
		return new ResultUserDto<>(UserErrorCode.CommonOK);
	}
	
	/*
	 * @ModelAttribute public UserBetDto getUserBet(UserBetDto betDto) {
	 * betDto.setUserId(this.getUserId()); betDto.setUserIp(this.getClientIP());
	 * if (log.isDebugEnabled()) { log.debug(betDto); } return betDto; }
	 */
	
	@RequestMapping("/bet")
	@ResponseBody
	public ResultOrderDto<BetResultDto> bet(UserBetDto dto, BindingResult errors) {
	
		new BetValidator().validate(dto, errors);
		if (errors.hasErrors()) {
			printError(errors);
			return new ResultOrderDto<BetResultDto>(OrderErrorCode.CommonArgument.getCode(), getFirstError(errors));
		}
		ResultOrderDto<BetResultDto> result = null;
		try {
			result = orderService.addOrder(dto);
			if (log.isDebugEnabled()) {
				log.debug(result.getRetcode());
			}
		} catch (Exception e) {
			log.error(e, e);
			result = new ResultOrderDto<BetResultDto>(OrderErrorCode.CommonBusy);
		}
		return result;
	}
	
	public class BetValidator implements Validator {
		
		public boolean supports(Class<?> clazz) {
		
			return UserBetDto.class.equals(clazz);
		}
		
		public void validate(Object obj, Errors errors) {
		
			try {
				if (obj == null) {
					errors.rejectValue("userId", null, "参数不能为空");
					return;
				}
				if (obj instanceof UserBetDto) {
					UserBetDto dto = (UserBetDto) obj;
					dto.setUserId(getUserId());
					dto.setUserIp(getClientIP());
					if (StringUtils.isBlank(dto.getGameId())) {
						errors.rejectValue("gameId", null, "投注彩种不能为空");
					}
					GameRule gr = GameRuleFactory.getGameRule(dto.getGameId());
					if (gr == null) {
						errors.rejectValue("gameId", null, "投注彩种非法值");
					}
					// TODO 期次格式校验
					if (StringUtils.isBlank(dto.getPeriodNo())) {
						errors.rejectValue("periodNo", null, "投注期次不能为空");
					}
					if (dto.getPrice() == null) {
						errors.rejectValue("price", null, "投注金额不能为空");
					}
					if (dto.getPrice() != null && dto.getPrice() <= 0) {
						errors.rejectValue("price", null, "投注金额非法值");
					}
					if (StringUtils.isBlank(dto.getRawBetNumbers())) {
						errors.rejectValue("rawBetNumbers", null, "投注号码不能为空");
					}
					if (dto.getSourceType() == null) {
						errors.rejectValue("sourceType", null, "订单来源不能为空");
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
