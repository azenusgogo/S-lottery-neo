package com.sogou.lottery.web.service.user.dto;

public class ChargeBankDto {
	
	private Integer bankId;
	private String bank;
	
	public ChargeBankDto() {
	
	}
	
	public ChargeBankDto(Integer bankId, String bank) {
	
		super();
		this.bankId = bankId;
		this.bank = bank;
	}
	
	public Integer getBankId() {
	
		return bankId;
	}
	
	public void setBankId(Integer bankId) {
	
		this.bankId = bankId;
	}
	
	public String getBank() {
	
		return bank;
	}
	
	public void setBank(String bank) {
	
		this.bank = bank;
	}
}
