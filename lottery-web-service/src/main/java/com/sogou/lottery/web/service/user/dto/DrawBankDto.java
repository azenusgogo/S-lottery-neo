package com.sogou.lottery.web.service.user.dto;

public class DrawBankDto extends ChargeBankDto {
	
	private Boolean branch;// 是否需要补全省市支行信息
	
	public DrawBankDto(Integer bankId, String bank) {
	
		super(bankId, bank);
		this.branch = false;
	}
	
	public Boolean getBranch() {
	
		return branch;
	}
	
	public void setBranch(Boolean branch) {
	
		this.branch = branch;
	}
	
}
