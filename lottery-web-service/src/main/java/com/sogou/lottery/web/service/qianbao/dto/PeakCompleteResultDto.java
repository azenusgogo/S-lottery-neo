package com.sogou.lottery.web.service.qianbao.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class PeakCompleteResultDto extends PeakCommonResultDto {
	
	String username;
	
	public String getUsername() {
	
		return username;
	}
	
	public void setUsername(String username) {
	
		this.username = username;
	}
}
