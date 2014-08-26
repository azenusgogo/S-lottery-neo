package com.sogou.lottery.web.service.pay.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TransferResultDto {
	
	private String transferId;
	private String accountIn;
	private Long amount;
	private Integer errorCode;
	
	public String getTransferId() {
	
		return transferId;
	}
	
	public void setTransferId(String transferId) {
	
		this.transferId = transferId;
	}
	
	public String getAccountIn() {
	
		return accountIn;
	}
	
	public void setAccountIn(String accountIn) {
	
		this.accountIn = accountIn;
	}
	
	public Long getAmount() {
	
		return amount;
	}
	
	public void setAmount(Long amount) {
	
		this.amount = amount;
	}
	
	public Integer getErrorCode() {
	
		return errorCode;
	}
	
	public void setErrorCode(Integer errorCode) {
	
		this.errorCode = errorCode;
	}
	
	public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
