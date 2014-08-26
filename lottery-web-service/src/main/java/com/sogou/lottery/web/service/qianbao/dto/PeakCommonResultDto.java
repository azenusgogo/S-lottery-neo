package com.sogou.lottery.web.service.qianbao.dto;

import org.apache.commons.lang3.StringUtils;

public class PeakCommonResultDto extends PeakResultDto {
	
	private String error_code;
	private String error_desc;
	
	public boolean isSuccess() {
	
		return "0".equals(StringUtils.trim(error_code));
	}
	
	public String getError_code() {
	
		return error_code;
	}
	
	public void setError_code(String error_code) {
	
		this.error_code = error_code;
	}
	
	public String getError_desc() {
	
		return error_desc;
	}
	
	public void setError_desc(String error_desc) {
	
		this.error_desc = error_desc;
	}
	
}
