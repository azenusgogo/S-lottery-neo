package com.sogou.lottery.web.service.user.dto;

public class QuestionDto {
	
	private Integer code;
	private String question;
	
	public QuestionDto() {
	
	}
	
	public QuestionDto(Integer code, String question) {
	
		super();
		this.code = code;
		this.question = question;
	}
	
	public Integer getCode() {
	
		return code;
	}
	
	public void setCode(Integer code) {
	
		this.code = code;
	}
	
	public String getQuestion() {
	
		return question;
	}
	
	public void setQuestion(String question) {
	
		this.question = question;
	}
	
}
