package com.sogou.lottery.web.service.qianbao.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class PeakPayResultDto extends PeakResultDto {
	
	private String orderid;
	private String amount;
	private String ymd;
	private String moneytype;
	private String transnum;
	private String succmark;
	private String cause;
	private String memo1;
	private String memo2;
	
	public String getOrderid() {
	
		return orderid;
	}
	
	public void setOrderid(String orderid) {
	
		this.orderid = orderid;
	}
	
	public String getAmount() {
	
		return amount;
	}
	
	public void setAmount(String amount) {
	
		this.amount = amount;
	}
	
	public String getYmd() {
	
		return ymd;
	}
	
	public void setYmd(String ymd) {
	
		this.ymd = ymd;
	}
	
	public String getMoneytype() {
	
		return moneytype;
	}
	
	public void setMoneytype(String moneytype) {
	
		this.moneytype = moneytype;
	}
	
	public String getTransnum() {
	
		return transnum;
	}
	
	public void setTransnum(String transnum) {
	
		this.transnum = transnum;
	}
	
	public String getSuccmark() {
	
		return succmark;
	}
	
	public void setSuccmark(String succmark) {
	
		this.succmark = succmark;
	}
	
	public String getCause() {
	
		return cause;
	}
	
	public void setCause(String cause) {
	
		this.cause = cause;
	}
	
	public String getMemo1() {
	
		return memo1;
	}
	
	public void setMemo1(String memo1) {
	
		this.memo1 = memo1;
	}
	
	public String getMemo2() {
	
		return memo2;
	}
	
	public void setMemo2(String memo2) {
	
		this.memo2 = memo2;
	}
	
	public String toString() {
	
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
