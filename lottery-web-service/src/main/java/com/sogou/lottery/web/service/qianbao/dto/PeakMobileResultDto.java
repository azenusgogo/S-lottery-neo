package com.sogou.lottery.web.service.qianbao.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class PeakMobileResultDto extends PeakCommonResultDto {
	
	private String username;
	private String service;
	private String mobilenum;
	private String status;
	
	public String getStatus() {
	
		return status;
	}
	
	public void setStatus(String status) {
	
		this.status = status;
	}
	
	public String getUsername() {
	
		return username;
	}
	
	public void setUsername(String username) {
	
		this.username = username;
	}
	
	public String getService() {
	
		return service;
	}
	
	public void setService(String service) {
	
		this.service = service;
	}
	
	public String getMobilenum() {
	
		return mobilenum;
	}
	
	public void setMobilenum(String mobilenum) {
	
		this.mobilenum = mobilenum;
	}
}
