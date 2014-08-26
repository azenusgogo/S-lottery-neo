package com.sogou.lottery.web.service.user.dto;

public class BalanceDto extends UserDto {
	
	private Long availableAmount;
	private Long frozenAmount;
	private Long availableWithDrawAmount;
	private Integer status; // 0-正常,1-账户未激活，2-账户冻结中，3-异常状态,4-禁用状态
	
	public BalanceDto() {
	
	}
	
	public BalanceDto(UserDto dto) {
	
		super();
	}
	
	public Long getAvailableAmount() {
	
		return availableAmount;
	}
	
	public void setAvailableAmount(Long availableAmount) {
	
		this.availableAmount = availableAmount;
	}
	
	public Long getFrozenAmount() {
	
		return frozenAmount;
	}
	
	public void setFrozenAmount(Long frozenAmount) {
	
		this.frozenAmount = frozenAmount;
	}
	
	public Long getAvailableWithDrawAmount() {
	
		return availableWithDrawAmount;
	}
	
	public void setAvailableWithDrawAmount(Long availableWithDrawAmount) {
	
		this.availableWithDrawAmount = availableWithDrawAmount;
	}
	
	public Integer getStatus() {
	
		return status;
	}
	
	public void setStatus(Integer status) {
	
		this.status = status;
	}
	
}
