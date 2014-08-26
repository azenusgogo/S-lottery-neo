package com.sogou.lottery.web.service.user.dto;

public class InfoResultDto extends BankDto {
	
	private String trueName;
	private String displayTrueName;
	private Integer idCardType;
	private String idCardNo;
    private String displayIdCardNo;
	private String email;
	private String displayEmail;
	private String mobile;
	private String displayMobile;
	
	public String getTrueName() {
	
		return trueName;
	}
	
	public void setTrueName(String trueName) {
	
		this.trueName = trueName;
	}
	
	public String getIdCardNo() {
	
		return idCardNo;
	}
	
	public void setIdCardNo(String idCardNo) {
	
		this.idCardNo = idCardNo;
	}
	
	public String getEmail() {
	
		return email;
	}
	
	public void setEmail(String email) {
	
		this.email = email;
	}
	
	public String getMobile() {
	
		return mobile;
	}
	
	public void setMobile(String mobile) {
	
		this.mobile = mobile;
	}
	
	public String getDisplayTrueName() {
	
		return displayTrueName;
	}
	
	public void setDisplayTrueName(String displayTrueName) {
	
		this.displayTrueName = displayTrueName;
	}
	
	public Integer getIdCardType() {
	
		return idCardType;
	}
	
	public void setIdCardType(Integer idCardType) {
	
		this.idCardType = idCardType;
	}
	
	public String getDisplayEmail() {
	
		return displayEmail;
	}
	
	public void setDisplayEmail(String displayEmail) {
	
		this.displayEmail = displayEmail;
	}
	
	public String getDisplayMobile() {
	
		return displayMobile;
	}
	
	public void setDisplayMobile(String displayMobile) {
	
		this.displayMobile = displayMobile;
	}

    public String getDisplayIdCardNo() {
        return displayIdCardNo;
    }

    public void setDisplayIdCardNo(String displayIdCardNo) {
        this.displayIdCardNo = displayIdCardNo;
    }
}
