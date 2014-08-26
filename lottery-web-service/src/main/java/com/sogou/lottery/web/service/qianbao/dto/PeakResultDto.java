package com.sogou.lottery.web.service.qianbao.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PeakResultDto {
	
	private String psid;
	private String signstr;
	
	public String getPsid() {
	
		return psid;
	}
	
	public void setPsid(String psid) {
	
		this.psid = psid;
	}
	
	public String getSignstr() {
	
		return signstr;
	}
	
	public void setSignstr(String signstr) {
	
		this.signstr = signstr;
	}
	
	public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
