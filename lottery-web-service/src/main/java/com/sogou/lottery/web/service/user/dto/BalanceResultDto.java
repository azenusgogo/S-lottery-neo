package com.sogou.lottery.web.service.user.dto;

public class BalanceResultDto {
	
	private BalanceDto balance;
	private Long withDrawApply;
	
	public void setBalance(BalanceDto balance) {
	
		this.balance = balance;
	}
	
	public void setWithDrawApply(Long withDrawApply) {
	
		this.withDrawApply = withDrawApply;
	}
	
	public Long getAvailableAmount() {
	
		return balance.getAvailableAmount();
	}
	
	public Long getFrozenAmount() {
	
		return balance.getFrozenAmount();
	}
	
	public Long getAvailableWithDrawAmount() {
	
		return balance.getAvailableWithDrawAmount();
	}
	
	public Integer getStatus() {
	
		return balance.getStatus();
	}
	
	public Long getWithDrawApply() {
	
		return withDrawApply;
	}
	
}
