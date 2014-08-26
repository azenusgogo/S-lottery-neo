package com.sogou.lottery.web.service.user.dto;

public class RechargeDto extends UserDto {
	
	private String id;
	/**
	 * 用来记录充值来源页面是否为下单页面，决定了充值后的回调页面
	 */
	private String payOrderId;
	private Long amount;
	private String bankId;
	private String token;
	private String returl;
	
	public String getId() {
	
		return id;
	}
	
	public void setId(String id) {
	
		this.id = id;
	}
	
	public String getPayOrderId() {
	
		return payOrderId;
	}
	
	public void setPayOrderId(String payOrderId) {
	
		this.payOrderId = payOrderId;
	}
	
	public Long getAmount() {
	
		return amount;
	}
	
	public void setAmount(Long amount) {
	
		this.amount = amount;
	}
	
	public String getBankId() {
	
		return bankId;
	}
	
	public void setBankId(String bankId) {
	
		this.bankId = bankId;
	}
	
	public String getToken() {
	
		return token;
	}
	
	public void setToken(String token) {
	
		this.token = token;
	}
	
	public String getReturl() {
	
		return returl;
	}
	
	public void setReturl(String returl) {
	
		this.returl = returl;
	}
}
