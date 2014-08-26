package com.sogou.lottery.web.web.dto.index;

import java.util.Date;

import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.award.AwardWithPeriod;
import com.sogou.lottery.base.vo.game.Game;

public class OpenAnnounceDto {
	
	private Game game;
	private AwardWithPeriod award;
	
	private Boolean todayOpen;
	
	public void setGame(Game game) {
	
		this.game = game;
	}
	
	public void setAward(AwardWithPeriod award) {
	
		this.award = award;
	}
	
	public void setTodayOpen(Boolean todayOpen) {
	
		this.todayOpen = todayOpen;
	}
	
	public Boolean getTodayOpen() {
	
		return todayOpen;
	}
	
	public String getGameId() {
	
		return game.getGameId();
	}
	
	public String getGameCn() {
	
		return game.getGameCn();
	}
	
	public String getPeriodNo() {
	
		return award.getPeriod().getPeriodNo();
	}
	
	public String getPrizeNumber() {
	
		return award.getAward() == null ? null : award.getAward().getPrizeNumber();
	}
	
	public String getOpenAwardDate() {
	
		String openAwardDate = "";
		if (award.getPeriod().getOffcialAwardTime() != null) {
			openAwardDate = DateUtil.formatDate(award.getPeriod().getOffcialAwardTime());
		}
		return openAwardDate;
	}
	
	public String getOpenAwardTime() {
	
		String openAwardDate = "";
		if (award.getPeriod().getOffcialAwardTime() != null) {
			Date openAwardTime = award.getPeriod().getOffcialAwardTime();
			openAwardDate = DateUtil.formatDate(openAwardTime, DateUtil.Format.HYPHEN_YYYYMMDDHHMM);
			openAwardDate = openAwardDate.substring(5);
			String week = DateUtil.getWeekOfDate2(openAwardTime);
			openAwardDate.replace(" ", "(" + week + ") ");
		}
		return openAwardDate;
	}
	
	public Date getOffcialAwardTime() {
	
		return award.getPeriod().getOffcialAwardTime();
	}
	
}
