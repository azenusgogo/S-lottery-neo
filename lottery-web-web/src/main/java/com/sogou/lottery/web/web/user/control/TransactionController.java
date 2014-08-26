package com.sogou.lottery.web.web.user.control;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sogou.lottery.base.user.dto.ResultUserDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.web.service.qianbao.service.QianBaoService;
import com.sogou.lottery.web.service.user.dto.RechargeDto;
import com.sogou.lottery.web.service.user.dto.TransDetailResultDto;
import com.sogou.lottery.web.service.user.dto.TransDto;
import com.sogou.lottery.web.service.user.dto.TransResultDto;
import com.sogou.lottery.web.web.BaseController;

@Controller
@RequestMapping("/detail")
public class TransactionController extends BaseController {
	
	private final static int PAGE_SIZE = 10;
	@Autowired
	private QianBaoService qianBaoService;
	
	@RequestMapping("/details")
	public String details(TransDto detail, ModelMap model) {
	
		getDetails(detail, model);
		return "detail/details";
	}
	
	@RequestMapping("/recharge")
	public String recharge(TransDto detail, ModelMap model) {
	
		detail.setType(TransDto.TYPE_CHARGE);
		getDetails(detail, model);
		return "detail/recharge";
	}
	
	@RequestMapping("/withdraw")
	public String withdraw(TransDto detail, ModelMap model) {
	
		detail.setType(TransDto.TYPE_WITHDRAW);
		getDetails(detail, model);
		return "detail/withdraw";
	}
	
	private void getDetails(TransDto detail, ModelMap model) {
	
		// TODO 把code转换成中文
		// TODO errcode要能区分 已成功，失败，处理中
		// TODO 增加 手续费，到账时间字段
		List<TransDetailResultDto> list = null;
		ResultUserDto<TransResultDto> result = qianBaoService.queryDetails(detail);
		if (result.isSucces()) {
			list = result.getResult().getTransList();
		} else {
			list = new ArrayList<>();
		}
		model.addAttribute("page", list);
	}
	
	@ModelAttribute
	public TransDto getRechargeDto(TransDto detailDto) {
	
		if (detailDto == null) {
			detailDto = new TransDto();
		}
		detailDto.setUserId(this.getUserId());
		detailDto.setUserIp(this.getClientIP());
		if (detailDto.getPageNo() == null) {
			detailDto.setPageNo(1);
		}
		detailDto.setPageSize(PAGE_SIZE);
		if (!(detailDto.getMonth() == null || 1 == detailDto.getMonth() || 3 == detailDto.getMonth() || 6 == detailDto.getMonth() || 12 == detailDto.getMonth())) {
			detailDto.setMonth(0);
		}
		detailDto.setType(null);
        if(detailDto.getMonth() == null){
            detailDto.setMonth(3);
        }
		int m = detailDto.getMonth();
		// 钱包系统的接口最细粒度是小时
		Date end = DateUtil.add(DateUtil.getCurrentDate(), 1, Calendar.HOUR);
		Date start = null;
		if (m == 0) {
			// 接口是否包含
			start = DateUtil.add(end, -1, Calendar.WEEK_OF_YEAR);
		} else {
			start = DateUtil.add(end, -m, Calendar.MONTH);
		}
		detailDto.setEndTime(new Timestamp(end.getTime()));
		detailDto.setStartTime(new Timestamp(start.getTime()));
		if (log.isDebugEnabled()) {
			log.debug(detailDto);
		}
		return detailDto;
	}
	
	@InitBinder
	public void initBinder(DataBinder binder) {
	
//		binder.setValidator(new RechargeValidator());
	}
	
	public class RechargeValidator implements Validator {
		
		public boolean supports(Class<?> clazz) {
		
			return RechargeDto.class.equals(clazz);
		}
		
		public void validate(Object obj, Errors errors) {
		
			if (obj instanceof RechargeDto) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "require.userIp", "用户名不能为空");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userIp", "require.userIp", "用户IP地址不能为空");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pageSize", "require.pageSize", "每页条数不能为空");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endTime", "require.endTime", "结束时间不能为空");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startTime", "require.startTime", "开始时间不能为空");
			}
		}
	}
}
