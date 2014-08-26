package com.sogou.lottery.web.service.qianbao.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class PeakTokenResultDto extends PeakCommonResultDto {
	
	private String token;
	
	public String getToken() {
	
		return token;
	}
	
	public void setToken(String token) {
	
		this.token = token;
	}
	
}
