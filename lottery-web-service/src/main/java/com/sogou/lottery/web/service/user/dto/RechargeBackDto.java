package com.sogou.lottery.web.service.user.dto;

import com.sogou.lottery.base.vo.BaseObject;

public class RechargeBackDto extends BaseObject {
	
	// peak参数
	private String psid;
	private String ps_requestId;
	private String transnum;
	private String error_code;
	private String error_desc;
	private String signstr;
	// 彩票参数
	private String payOrderId;
	
	public String getPsid() {
	
		return psid;
	}
	
	public void setPsid(String psid) {
	
		this.psid = psid;
	}
	
	public String getPs_requestId() {
	
		return ps_requestId;
	}
	
	public void setPs_requestId(String ps_requestId) {
	
		this.ps_requestId = ps_requestId;
	}
	
	public String getTransnum() {
	
		return transnum;
	}
	
	public void setTransnum(String transnum) {
	
		this.transnum = transnum;
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
	
	public String getSignstr() {
	
		return signstr;
	}
	
	public void setSignstr(String signstr) {
	
		this.signstr = signstr;
	}
	
	public String getPayOrderId() {
	
		return payOrderId;
	}
	
	public void setPayOrderId(String payOrderId) {
	
		this.payOrderId = payOrderId;
	}
}
