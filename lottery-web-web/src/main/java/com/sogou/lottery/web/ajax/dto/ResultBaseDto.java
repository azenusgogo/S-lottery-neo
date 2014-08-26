package com.sogou.lottery.web.ajax.dto;

import java.io.Serializable;

import com.sogou.lottery.common.constant.AjaxConstant;

public class ResultBaseDto<T> implements Serializable {
	
	private static final long serialVersionUID = -7066826024080281568L;
	private int retcode = AjaxConstant.SUCCESS_CODE;
	private String retdesc = AjaxConstant.SUCCESS_DESC;
	
	public ResultBaseDto() {
	
	}
	
	public ResultBaseDto(int retcode, String retdesc) {
	
		super();
		this.retcode = retcode;
		this.retdesc = retdesc;
	}
	
	public int getRetcode() {
	
		return retcode;
	}
	
	public void setRetcode(int retcode) {
	
		this.retcode = retcode;
	}
	
	public String getRetdesc() {
	
		return retdesc;
	}
	
	public void setRetdesc(String retdesc) {
	
		this.retdesc = retdesc;
	}
	
}
