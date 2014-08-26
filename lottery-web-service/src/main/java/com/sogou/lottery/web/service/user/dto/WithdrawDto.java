package com.sogou.lottery.web.service.user.dto;

public class WithdrawDto extends BankDto {
	
	private String id;
	private String trueName;
	private Long amount;
	private Boolean audit;
	private Long fee;
    private String returl;
	
	public String getId() {
	
		return id;
	}
	
	public void setId(String id) {
	
		this.id = id;
	}
	
	public String getTrueName() {
	
		return trueName;
	}
	
	public void setTrueName(String trueName) {
	
		this.trueName = trueName;
	}
	
	public Long getAmount() {
	
		return amount;
	}
	
	public void setAmount(Long amount) {
	
		this.amount = amount;
	}
	
	public Boolean getAudit() {
	
		return audit;
	}
	
	public void setAudit(Boolean audit) {
	
		this.audit = audit;
	}
	
	public Long getFee() {
	
		return fee;
	}
	
	public void setFee(Long fee) {
	
		this.fee = fee;
	}
	
	public String getReturl() {
	
		return returl;
	}
	
	public void setReturl(String returl) {
	
		this.returl = returl;
	}

}
