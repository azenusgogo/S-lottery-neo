package com.sogou.lottery.web.web.dto.index;

import java.util.Date;

import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.period.Period;

public class CommonPeriodDto {
	
	private Game game;
	private Period period;
	
	private String bonusPool;
	private Boolean available;
	private Boolean todayOpen;
	private String serverTime;
	
	public CommonPeriodDto() {
	
		serverTime = System.currentTimeMillis() + "";
	}
	
	public void setGame(Game game) {
	
		this.game = game;
	}
	
	public void setPeriod(Period period) {
	
		this.period = period;
	}
	
	public void setBonusPool(String bonusPool) {
	
		this.bonusPool = bonusPool;
	}
	
	public void setAvailable(Boolean available) {
	
		this.available = available;
	}
	
	public void setTodayOpen(Boolean todayOpen) {
	
		this.todayOpen = todayOpen;
	}
	
	public String getGameId() {
	
		return game.getGameId();
	}
	
	public String getGameCn() {
	
		return game.getGameCn();
	}
	
	public String getPeriodNo() {
	
		return period.getPeriodNo();
	}
	
	public Date getOffcialAwardTime() {
	
		return period.getOffcialAwardTime();
	}
	
	public Date getEndTime() {
	
		return period.getEndTime();
	}
	
	public Boolean getAvailable() {
	
		return available;
	}
	
	public String getBonusPool() {
	
		return bonusPool;
	}
	
	public Boolean getTodayOpen() {
	
		return todayOpen;
	}

	
	public String getServerTime() {
	
		return serverTime;
	}
}
