package com.sogou.lottery.web.service.user.dto;

public class BankDto extends UserDto {
	
	private String bankId;
	private String bank;
	private String province;
	private String city;
	private String branch;// 支行名称
	private String bankCardNo;
	private String displayBankCardNo;
	
	public String getBankId() {
	
		return bankId;
	}
	
	public void setBankId(String bankId) {
	
		this.bankId = bankId;
	}
	
	public String getBank() {
	
		return bank;
	}
	
	public void setBank(String bank) {
	
		this.bank = bank;
	}
	
	public String getProvince() {
	
		return province;
	}
	
	public void setProvince(String province) {
	
		this.province = province;
	}
	
	public String getCity() {
	
		return city;
	}
	
	public void setCity(String city) {
	
		this.city = city;
	}
	
	public String getBranch() {
	
		return branch;
	}
	
	public void setBranch(String branch) {
	
		this.branch = branch;
	}
	
	public String getBankCardNo() {
	
		return bankCardNo;
	}
	
	public void setBankCardNo(String bankCardNo) {
	
		this.bankCardNo = bankCardNo;
	}
	
	public String getDisplayBankCardNo() {
	
		return displayBankCardNo;
	}
	
	public void setDisplayBankCardNo(String displayBankCardNo) {
	
		this.displayBankCardNo = displayBankCardNo;
	}
}
