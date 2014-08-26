package com.sogou.lottery.web.service.user.dto;

import com.sogou.lottery.base.vo.user.User;

public class InfoSummary {
	
	private User user;
	private BalanceDto balance;
	private Long withDrawApply;
	
	public String getUserId() {
	
		return user.getUserId();
	}
	
	public String getNickName() {
	
		return user.getNickName();
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
	
	public void setUser(User user) {
	
		this.user = user;
	}
	
	public void setBalance(BalanceDto balance) {
	
		this.balance = balance;
	}
	
	public void setWithDrawApply(Long withDrawApply) {
	
		this.withDrawApply = withDrawApply;
	}
}
