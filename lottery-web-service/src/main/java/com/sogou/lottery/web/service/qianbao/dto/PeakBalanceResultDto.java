package com.sogou.lottery.web.service.qianbao.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class PeakBalanceResultDto extends PeakCommonResultDto {
	
	private String username;
	private String availableamount;
	private String frozenamt;
	private String availablewithdraw;
	private String securitylevel;
	private String code;
	private String key;
	
	public String getUsername() {
	
		return username;
	}
	
	public void setUsername(String username) {
	
		this.username = username;
	}
	
	public String getAvailableamount() {
	
		return availableamount;
	}
	
	public void setAvailableamount(String availableamount) {
	
		this.availableamount = availableamount;
	}
	
	public String getFrozenamt() {
	
		return frozenamt;
	}
	
	public void setFrozenamt(String frozenamt) {
	
		this.frozenamt = frozenamt;
	}
	
	public String getAvailablewithdraw() {
	
		return availablewithdraw;
	}
	
	public void setAvailablewithdraw(String availablewithdraw) {
	
		this.availablewithdraw = availablewithdraw;
	}
	
	public String getSecuritylevel() {
	
		return securitylevel;
	}
	
	public void setSecuritylevel(String securitylevel) {
	
		this.securitylevel = securitylevel;
	}
	
	public String getCode() {
	
		return code;
	}
	
	public void setCode(String code) {
	
		this.code = code;
	}
	
	public String getKey() {
	
		return key;
	}
	
	public void setKey(String key) {
	
		this.key = key;
	}
	
}
