package com.sogou.lottery.web.service.user.dto;

public class FindPwdBySafeDto extends UserDto {
	
	private String safeAnswer;
	private String newPassowrd;
	private String newPassowrdConfirm;
	
	public String getSafeAnswer() {
	
		return safeAnswer;
	}
	
	public void setSafeAnswer(String safeAnswer) {
	
		this.safeAnswer = safeAnswer;
	}
	
	public String getNewPassowrd() {
	
		return newPassowrd;
	}
	
	public void setNewPassowrd(String newPassowrd) {
	
		this.newPassowrd = newPassowrd;
	}
	
	public String getNewPassowrdConfirm() {
	
		return newPassowrdConfirm;
	}
	
	public void setNewPassowrdConfirm(String newPassowrdConfirm) {
	
		this.newPassowrdConfirm = newPassowrdConfirm;
	}
	
}
