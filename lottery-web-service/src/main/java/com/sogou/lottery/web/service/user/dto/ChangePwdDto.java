package com.sogou.lottery.web.service.user.dto;

public class ChangePwdDto extends UserDto {
	
	private String payPwd;
	private String newPayPwd;
	private String newPayPwdConfirm;
	
	public String getPayPwd() {
	
		return payPwd;
	}
	
	public void setPayPwd(String payPwd) {
	
		this.payPwd = payPwd;
	}
	
	public String getNewPayPwd() {
	
		return newPayPwd;
	}
	
	public void setNewPayPwd(String newPayPwd) {
	
		this.newPayPwd = newPayPwd;
	}
	
	public String getNewPayPwdConfirm() {
	
		return newPayPwdConfirm;
	}
	
	public void setNewPayPwdConfirm(String newPayPwdConfirm) {
	
		this.newPayPwdConfirm = newPayPwdConfirm;
	}
	
}
