package com.sogou.lottery.web.service.user.dto;

public class InfoDto extends UserDto {
	
	private Integer idCardType;
	private String idCardNo;
	private String pwd;
	private String payPwdConfirm;
	private Integer questionType;
	private String safeQuestion;
	private String safeAnswer;
	private String trueName;
	
	public String getPwd() {
	
		return pwd;
	}
	
	public void setPwd(String pwd) {
	
		this.pwd = pwd;
	}
	
	public String getPayPwdConfirm() {
	
		return payPwdConfirm;
	}
	
	public void setPayPwdConfirm(String payPwdConfirm) {
	
		this.payPwdConfirm = payPwdConfirm;
	}
	
	public Integer getQuestionType() {
	
		return questionType;
	}
	
	public void setQuestionType(Integer questionType) {
	
		this.questionType = questionType;
	}
	
	public String getSafeQuestion() {
	
		return safeQuestion;
	}
	
	public void setSafeQuestion(String safeQuestion) {
	
		this.safeQuestion = safeQuestion;
	}
	
	public String getSafeAnswer() {
	
		return safeAnswer;
	}
	
	public void setSafeAnswer(String safeAnswer) {
	
		this.safeAnswer = safeAnswer;
	}
	
	public String getTrueName() {
	
		return trueName;
	}
	
	public void setTrueName(String trueName) {
	
		this.trueName = trueName;
	}
	
	public Integer getIdCardType() {
	
		return idCardType;
	}
	
	public void setIdCardType(Integer idCardType) {
	
		this.idCardType = idCardType;
	}
	
	public String getIdCardNo() {
	
		return idCardNo;
	}
	
	public void setIdCardNo(String idCardNo) {
	
		this.idCardNo = idCardNo;
	}
}
