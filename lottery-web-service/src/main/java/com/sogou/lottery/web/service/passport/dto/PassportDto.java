package com.sogou.lottery.web.service.passport.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * http://updwiki.sogou-inc.com/pages/viewpage.action?pageId=6460900
 * 
 * @author huangtao
 */
public class PassportDto {
	
	/**
	 * 产品在passport申请的clientId
	 */
	private String clientId;// client_id
	/**
	 * 服务端long型系统时间,单位毫秒，java为System.currentTimeMillis();
	 */
	private Long currentTime;// ct
	/**
	 * userid+ client_id + server_secret+ ct的md5
	 */
	private String code;// code
	/**
	 * 要查询的邮箱账号、搜狐的昵称、搜狗个性域名@sogou.com、手机号码
	 */
	private String userId;// userid
	/**
	 * MD5加密后的用户密码
	 */
	private String password;// password
	/**
	 * createip//用户的真实IP，目前开放平台、站长平台传了
	 */
	private String userIp;// createip
	
	/**
	 * 彩票限制频次使用
	 */
	private String uid;
	
	public String getClientId() {
	
		return clientId;
	}
	
	public void setClientId(String clientId) {
	
		this.clientId = clientId;
	}
	
	public Long getCurrentTime() {
	
		return currentTime;
	}
	
	public void setCurrentTime(Long currentTime) {
	
		this.currentTime = currentTime;
	}
	
	public String getCode() {
	
		return code;
	}
	
	public void setCode(String code) {
	
		this.code = code;
	}
	
	public String getUserId() {
	
		return userId;
	}
	
	public void setUserId(String userId) {
	
		this.userId = userId;
	}
	
	public String getPassword() {
	
		return password;
	}
	
	public void setPassword(String password) {
	
		this.password = password;
	}
	
	public String getUserIp() {
	
		return userIp;
	}
	
	public void setUserIp(String userIp) {
	
		this.userIp = userIp;
	}
	
	public String getUid() {
	
		return uid;
	}
	
	public void setUid(String uid) {
	
		this.uid = uid;
	}
	
	public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
