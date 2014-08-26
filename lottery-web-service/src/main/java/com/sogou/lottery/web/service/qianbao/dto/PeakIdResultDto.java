package com.sogou.lottery.web.service.qianbao.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class PeakIdResultDto extends PeakCommonResultDto {
	
	private String ps_requestId;
	
	public String getPs_requestId() {
	
		return ps_requestId;
	}
	
	public void setPs_requestId(String ps_requestId) {
	
		this.ps_requestId = ps_requestId;
	}
}
