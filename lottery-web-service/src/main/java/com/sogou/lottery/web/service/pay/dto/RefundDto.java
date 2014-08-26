package com.sogou.lottery.web.service.pay.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RefundDto {
	
	private String refundId;
	private String payOrderId;
	private Long refundAmount;
	private Integer refundType;// 退款种类 0 投注失败 1 退保底 2 追号条件满足退款 3 合买参与不成功 4
								// 合买成单失败
	private String userIp;
	
	public String getRefundId() {
	
		return refundId;
	}
	
	public void setRefundId(String refundId) {
	
		this.refundId = refundId;
	}
	
	public String getPayOrderId() {
	
		return payOrderId;
	}
	
	public void setPayOrderId(String payOrderId) {
	
		this.payOrderId = payOrderId;
	}
	
	public Long getRefundAmount() {
	
		return refundAmount;
	}
	
	public void setRefundAmount(Long refundAmount) {
	
		this.refundAmount = refundAmount;
	}
	
	public Integer getRefundType() {
	
		return refundType;
	}
	
	public void setRefundType(Integer refundType) {
	
		this.refundType = refundType;
	}
	
	public String getUserIp() {
	
		return userIp;
	}
	
	public void setUserIp(String userIp) {
	
		this.userIp = userIp;
	}
	
	public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
