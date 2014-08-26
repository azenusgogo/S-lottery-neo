package com.sogou.lottery.web.service.user.dto;

public class ChangeSafeDto extends UserDto {
	
	private String safeAnswer;
	private Integer newQuestion;
	private String newAnswer;
	
	public String getSafeAnswer() {
	
		return safeAnswer;
	}
	
	public void setSafeAnswer(String safeAnswer) {
	
		this.safeAnswer = safeAnswer;
	}
	
	public Integer getNewQuestion() {
	
		return newQuestion;
	}
	
	public void setNewQuestion(Integer newQuestion) {
	
		this.newQuestion = newQuestion;
	}
	
	public String getNewAnswer() {
	
		return newAnswer;
	}
	
	public void setNewAnswer(String newAnswer) {
	
		this.newAnswer = newAnswer;
	}
	
}
