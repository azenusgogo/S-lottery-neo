package com.sogou.lottery.web.service.user.dto;

public class WithdrawAuditDto extends UserDto {
	
	private String drawId;
	private Boolean audit;
	
	public String getDrawId() {
	
		return drawId;
	}
	
	public void setDrawId(String drawId) {
	
		this.drawId = drawId;
	}
	
	public Boolean getAudit() {
	
		return audit;
	}
	
	public void setAudit(Boolean audit) {
	
		this.audit = audit;
	}
}
