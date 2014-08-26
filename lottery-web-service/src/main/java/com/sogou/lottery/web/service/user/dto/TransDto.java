package com.sogou.lottery.web.service.user.dto;

import java.sql.Timestamp;

public class TransDto extends UserDto {
	
	/**
	 * 0一周|1一个月|3三个月|6六个月|12十二个月
	 */
	private Integer month;
	private Integer pageNo;
	private Integer pageSize;
	private Timestamp startTime;
	private Timestamp endTime;
	private Integer type;
	
	public final static int TYPE_CHARGE = 1;
	public final static int TYPE_WITHDRAW = 2;
	
	// 1 充值
	// 2 提现
	// 3 消费
	// 4 退款收入
	// 5 提现手续费
	// 6 彩票返奖
	
	public Integer getPageNo() {
	
		return pageNo;
	}
	
	public Integer getMonth() {
	
		return month;
	}
	
	public void setMonth(Integer month) {
	
		this.month = month;
	}
	
	public void setPageNo(Integer pageNo) {
	
		this.pageNo = pageNo;
	}
	
	public Integer getPageSize() {
	
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
	
		this.pageSize = pageSize;
	}
	
	public Timestamp getStartTime() {
	
		return startTime;
	}
	
	public void setStartTime(Timestamp startTime) {
	
		this.startTime = startTime;
	}
	
	public Timestamp getEndTime() {
	
		return endTime;
	}
	
	public void setEndTime(Timestamp endTime) {
	
		this.endTime = endTime;
	}
	
	public Integer getType() {
	
		return type;
	}
	
	public void setType(Integer type) {
	
		this.type = type;
	}
	
}
