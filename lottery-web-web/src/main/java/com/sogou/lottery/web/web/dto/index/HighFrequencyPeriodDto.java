package com.sogou.lottery.web.web.dto.index;

import java.util.Date;

import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.base.vo.period.Period;

public class HighFrequencyPeriodDto {
	
	private Game game;
	private Period period;
	
	private Integer interval;
	private String hotPlayMethod;
	private Boolean available;
	private String serverTime;
	
	public HighFrequencyPeriodDto() {
	
		serverTime = "" + System.currentTimeMillis();
	}
	
	public void setGame(Game game) {
	
		this.game = game;
	}
	
	public void setPeriod(Period period) {
	
		this.period = period;
	}
	
	public void setAvailable(Boolean available) {
	
		this.available = available;
	}
	
	public void setHotPlayMethod(String hotPlayMethod) {
	
		this.hotPlayMethod = hotPlayMethod;
	}
	
	public void setInterval(Integer interval) {
	
		this.interval = interval;
	}
	
	public Integer getInterval() {
	
		return interval;
	}
	
	public String getHotPlayMethod() {
	
		return hotPlayMethod;
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
	
	public Date getEndTime() {
	
		return period.getEndTime();
	}
	
	public Date getOffcialAwardTime() {
	
		return period.getOffcialAwardTime();
	}
	
	public Boolean getAvailable() {
	
		return available;
	}
	
	public String getServerTime() {
	
		return serverTime;
	}
	
}
