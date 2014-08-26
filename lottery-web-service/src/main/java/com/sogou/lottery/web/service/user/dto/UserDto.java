package com.sogou.lottery.web.service.user.dto;

import com.sogou.lottery.base.vo.BaseObject;

public class UserDto extends BaseObject {
	
	private String userId;
	private String nickName;
	private String mobile;
	private String email;
	private String captcha;
	private String pwd;
	private String payPwd;
	private String userIp;
	private String uid;
	
	public UserDto() {
	
	}
	
	public UserDto(UserDto dto) {
	
		this.userId = dto.userId;
		this.nickName = dto.nickName;
		this.mobile = dto.mobile;
		this.email = dto.email;
		this.captcha = dto.captcha;
		this.pwd = dto.pwd;
		this.payPwd = dto.payPwd;
		this.userIp = dto.userIp;
		this.uid = dto.uid;
	}
	
	public String getUserId() {
	
		return userId;
	}
	
	public void setUserId(String userId) {
	
		this.userId = userId;
	}
	
	public String getMobile() {
	
		return mobile;
	}
	
	public void setMobile(String mobile) {
	
		this.mobile = mobile;
	}
	
	public String getUserIp() {
	
		return userIp;
	}
	
	public void setUserIp(String userIp) {
	
		this.userIp = userIp;
	}
	
	public String getEmail() {
	
		return email;
	}
	
	public void setEmail(String email) {
	
		this.email = email;
	}
	
	public String getCaptcha() {
	
		return captcha;
	}
	
	public void setCaptcha(String captcha) {
	
		this.captcha = captcha;
	}
	
	public String getPwd() {
	
		return pwd;
	}
	
	public void setPwd(String pwd) {
	
		this.pwd = pwd;
	}
	
	public String getPayPwd() {
	
		return payPwd;
	}
	
	public void setPayPwd(String payPwd) {
	
		this.payPwd = payPwd;
	}
	
	public String getNickName() {
	
		return nickName;
	}
	
	public void setNickName(String nickName) {
	
		this.nickName = nickName;
	}
	
	public String getUid() {
	
		return uid;
	}
	
	public void setUid(String uid) {
	
		this.uid = uid;
	}
}
