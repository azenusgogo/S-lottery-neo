package com.sogou.lottery.web.web.dto.index;

public class RecentAwardRecordDto {
	
	private String gameCn;
	private String nickName;
	private Long amount;
	
	public String getGameCn() {
	
		return gameCn;
	}
	
	public void setGameCn(String gameCn) {
	
		this.gameCn = gameCn;
	}
	
	public String getNickName() {
	
		return nickName;
	}
	
	public void setNickName(String nickName) {
	
		this.nickName = nickName;
	}
	
	public Long getAmount() {
	
		return amount;
	}
	
	public void setAmount(Long amount) {
	
		this.amount = amount;
	}
}
