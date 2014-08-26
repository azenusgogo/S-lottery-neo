package com.sogou.lottery.web.service.user.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserOperDto implements Serializable {
	
	private static final long serialVersionUID = -4460998962792955226L;
	
	private String userId;
	private int operType;
	private int times;
	private Timestamp createTime;
	private Timestamp updateTime;
	private Timestamp expireTime;
	
	public UserOperDto() {
	
	}
	
	public UserOperDto(String userId, int operType, int times, Timestamp expireTime) {
	
		this.userId = userId;
		this.operType = operType;
		this.times = times;
		long now = System.currentTimeMillis();
		createTime = new Timestamp(now);
		updateTime = createTime;
		this.expireTime = expireTime;
	}
	
	public String getUserId() {
	
		return userId;
	}
	
	public void setUserId(String userId) {
	
		this.userId = userId;
	}
	
	public int getOperType() {
	
		return operType;
	}
	
	public void setOperType(int operType) {
	
		this.operType = operType;
	}
	
	public int getTimes() {
	
		return times;
	}
	
	public void setTimes(int times) {
	
		this.times = times;
	}
	
	public Timestamp getCreateTime() {
	
		return createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
	
		this.createTime = createTime;
	}
	
	public Timestamp getUpdateTime() {
	
		return updateTime;
	}
	
	public void setUpdateTime(Timestamp updateTime) {
	
		this.updateTime = updateTime;
	}
	
	public Timestamp getExpireTime() {
	
		return expireTime;
	}
	
	public void setExpireTime(Timestamp expireTime) {
	
		this.expireTime = expireTime;
	}
}
