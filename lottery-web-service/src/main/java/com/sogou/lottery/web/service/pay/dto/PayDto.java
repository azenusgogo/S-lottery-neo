package com.sogou.lottery.web.service.pay.dto;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sogou.lottery.web.service.user.dto.UserDto;

public class PayDto extends UserDto {
	
	private String payOrderId;
	private Long cashAmount;
	private String title;
	private Timestamp createTime;
	private Timestamp payDeadline;
	
	public String getPayOrderId() {
	
		return payOrderId;
	}
	
	public void setPayOrderId(String payOrderId) {
	
		this.payOrderId = payOrderId;
	}
	
	public Long getCashAmount() {
	
		return cashAmount;
	}
	
	public void setCashAmount(Long cashAmount) {
	
		this.cashAmount = cashAmount;
	}
	
	public String getTitle() {
	
		return title;
	}
	
	public void setTitle(String title) {
	
		this.title = title;
	}
	
	public Timestamp getCreateTime() {
	
		return createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
	
		this.createTime = createTime;
	}
	
	public Timestamp getPayDeadline() {
	
		return payDeadline;
	}
	
	public void setPayDeadline(Timestamp payDeadline) {
	
		this.payDeadline = payDeadline;
	}
	
	public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
