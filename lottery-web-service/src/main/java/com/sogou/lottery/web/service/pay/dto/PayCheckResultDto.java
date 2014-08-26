package com.sogou.lottery.web.service.pay.dto;

public class PayCheckResultDto {
	
	private String userId;
	private String nickName;
	private String gameId;
	private String gameCn;
	private String periodNo;
	private String payOrderId;
	private Long payAmount;
	private Long chargeAmount;
	private Long balanceAmount;
	
	public String getUserId() {
	
		return userId;
	}
	
	public void setUserId(String userId) {
	
		this.userId = userId;
	}
	
	public String getNickName() {
	
		return nickName;
	}
	
	public void setNickName(String nickName) {
	
		this.nickName = nickName;
	}
	
	public String getGameId() {
	
		return gameId;
	}
	
	public void setGameId(String gameId) {
	
		this.gameId = gameId;
	}
	
	public String getGameCn() {
	
		return gameCn;
	}
	
	public void setGameCn(String gameCn) {
	
		this.gameCn = gameCn;
	}
	
	public String getPeriodNo() {
	
		return periodNo;
	}
	
	public void setPeriodNo(String periodNo) {
	
		this.periodNo = periodNo;
	}
	
	public String getPayOrderId() {
	
		return payOrderId;
	}
	
	public void setPayOrderId(String payOrderId) {
	
		this.payOrderId = payOrderId;
	}
	
	public Long getPayAmount() {
	
		return payAmount;
	}
	
	public void setPayAmount(Long payAmount) {
	
		this.payAmount = payAmount;
	}
	
	public Long getChargeAmount() {
	
		return chargeAmount;
	}
	
	public void setChargeAmount(Long chargeAmount) {
	
		this.chargeAmount = chargeAmount;
	}
	
	public Long getBalanceAmount() {
	
		return balanceAmount;
	}
	
	public void setBalanceAmount(Long balanceAmount) {
	
		this.balanceAmount = balanceAmount;
	}
	
}
