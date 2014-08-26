package com.sogou.lottery.web.service.pay.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RefundResultDto {
	
	private String refundId;
	private Integer errorCode;
	private String errorDesc;
	
	public boolean isSuccess() {
	
		return errorCode == 0;
	}
	
	public String getRefundId() {
	
		return refundId;
	}
	
	public void setRefundId(String refundId) {
	
		this.refundId = refundId;
	}
	
	public Integer getErrorCode() {
	
		return errorCode;
	}
	
	public void setErrorCode(Integer errorCode) {
	
		this.errorCode = errorCode;
	}
	
	public String getErrorDesc() {
	
		return errorDesc;
	}
	
	public void setErrorDesc(String errorDesc) {
	
		this.errorDesc = errorDesc;
	}
	
	public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
