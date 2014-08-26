package com.sogou.lottery.web.service.user.dto;

public class FindPwdByMobileDto extends UserDto {
	
	private String trueName;
	private Integer idCardType;
	private String idCardNo;
	private String newPassowrd;
    private String newPassowrdConfirm;
	
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
	
	public String getNewPassowrd() {
	
		return newPassowrd;
	}
	
	public void setNewPassowrd(String newPassowrd) {
	
		this.newPassowrd = newPassowrd;
	}

    public String getNewPassowrdConfirm() {
        return newPassowrdConfirm;
    }

    public void setNewPassowrdConfirm(String newPassowrdConfirm) {
        this.newPassowrdConfirm = newPassowrdConfirm;
    }
}
