package com.sogou.lottery.web.service.pay.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sogou.lottery.web.service.user.dto.UserDto;

public class TransferDto extends UserDto {
	
	private String transferId;
	private Long amount;
	private String inAccountId;
	private String outAccountId;
	private Boolean drawable;
	
	public String getTransferId() {
	
		return transferId;
	}
	
	public void setTransferId(String transferId) {
	
		this.transferId = transferId;
	}
	
	public Long getAmount() {
	
		return amount;
	}
	
	public void setAmount(Long amount) {
	
		this.amount = amount;
	}
	
	public String getInAccountId() {
	
		return inAccountId;
	}
	
	public void setInAccountId(String inAccountId) {
	
		this.inAccountId = inAccountId;
	}
	
	public String getOutAccountId() {
	
		return outAccountId;
	}
	
	public void setOutAccountId(String outAccountId) {
	
		this.outAccountId = outAccountId;
	}
	
	public Boolean getDrawable() {
	
		return drawable;
	}
	
	public void setDrawable(Boolean drawable) {
	
		this.drawable = drawable;
	}
	
	public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
