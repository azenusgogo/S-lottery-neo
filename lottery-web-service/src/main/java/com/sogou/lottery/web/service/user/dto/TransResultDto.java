package com.sogou.lottery.web.service.user.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransResultDto extends UserDto {
	
	private Timestamp startTime;
	private Timestamp endTime;
	
	private Long total = 0L;
	private Long totalPage = 0L;
	private Long pageNo = 1L;
	private Integer pageSize = 10;
	private Long chargeAmount = 0L;
	private Long withDrawAmount = 0L;
	private Long payAmount = 0L;
	private Long refundAmount = 0L;
	private Long bonusAmount = 0L;
	
	private List<TransDetailResultDto> transList = new ArrayList<>();
	
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
	
	public List<TransDetailResultDto> getTransList() {
	
		return transList;
	}
	
	public void setTransList(List<TransDetailResultDto> transList) {
	
		this.transList = transList;
	}
	
	public Long getTotal() {
	
		return total;
	}
	
	public void setTotal(Long total) {
	
		this.total = total;
	}
	
	public Long getTotalPage() {
	
		return totalPage;
	}
	
	public void setTotalPage(Long totalPage) {
	
		this.totalPage = totalPage;
	}
	
	public Long getPageNo() {
	
		return pageNo;
	}
	
	public void setPageNo(Long pageNo) {
	
		this.pageNo = pageNo;
	}
	
	public Integer getPageSize() {
	
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
	
		this.pageSize = pageSize;
	}
	
	public Long getChargeAmount() {
	
		return chargeAmount;
	}
	
	public void setChargeAmount(Long chargeAmount) {
	
		this.chargeAmount = chargeAmount;
	}
	
	public Long getWithDrawAmount() {
	
		return withDrawAmount;
	}
	
	public void setWithDrawAmount(Long withDrawAmount) {
	
		this.withDrawAmount = withDrawAmount;
	}
	
	public Long getPayAmount() {
	
		return payAmount;
	}
	
	public void setPayAmount(Long payAmount) {
	
		this.payAmount = payAmount;
	}
	
	public Long getRefundAmount() {
	
		return refundAmount;
	}
	
	public void setRefundAmount(Long refundAmount) {
	
		this.refundAmount = refundAmount;
	}
	
	public Long getBonusAmount() {
	
		return bonusAmount;
	}
	
	public void setBonusAmount(Long bonusAmount) {
	
		this.bonusAmount = bonusAmount;
	}
}
