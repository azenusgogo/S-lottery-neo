package com.sogou.lottery.web.service.user.dto;

public class RechargeResultDto {
	
	private RechargeDto dto;
	
	public void setRechargeDto(RechargeDto dto) {
	
		this.dto = dto;
	}
	
	public String getId() {
	
		return dto.getId();
	}
	
	public String getUserId() {
	
		return dto.getUserId();
	}
	
	public String getPayOrderId() {
	
		return dto.getPayOrderId();
	}
	
	public Long getAmount() {
	
		return dto.getAmount();
	}
	
	public String getBankId() {
	
		return dto.getBankId();
	}
	
	public String getReturl() {
	
		return dto.getReturl();
	}
	
}
