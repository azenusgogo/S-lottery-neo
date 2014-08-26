package com.sogou.lottery.web.service.user.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SafeResultDto {
	
	private String userId;
	private String safeQuestion;
	
	public String getUserId() {
	
		return userId;
	}
	
	public void setUserId(String userId) {
	
		this.userId = userId;
	}
	
	public String getSafeQuestion() {
	
		return safeQuestion;
	}
	
	public void setSafeQuestion(String safeQuestion) {
	
		this.safeQuestion = safeQuestion;
	}
	
	public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
