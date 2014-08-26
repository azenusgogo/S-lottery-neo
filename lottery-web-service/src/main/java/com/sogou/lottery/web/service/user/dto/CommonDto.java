package com.sogou.lottery.web.service.user.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CommonDto extends UserDto {
	
	private Integer errorCode;
	private String errorDesc;
	
	public CommonDto() {
	
	}
	
	public CommonDto(Integer errorCode, String errorDesc) {
	
		super();
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}
	
	public CommonDto(UserDto dto) {
	
		super(dto);
	}
	
	@JsonIgnore
	public Integer getErrorCode() {
	
		return errorCode;
	}
	
	public void setErrorCode(Integer errorCode) {
	
		this.errorCode = errorCode;
	}
	
	@JsonIgnore
	public String getErrorDesc() {
	
		return errorDesc;
	}
	
	public void setErrorDesc(String errorDesc) {
	
		this.errorDesc = errorDesc;
	}
	
}
