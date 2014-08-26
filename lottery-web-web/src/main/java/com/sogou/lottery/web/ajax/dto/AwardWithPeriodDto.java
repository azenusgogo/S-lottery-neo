package com.sogou.lottery.web.ajax.dto;

import java.io.Serializable;
import java.util.Date;

import com.sogou.lottery.base.vo.award.AwardWithPeriod;

public class AwardWithPeriodDto implements Serializable {
	
	private static final long serialVersionUID = -8729954436087824261L;
	private AwardWithPeriod awardWithPeriod;
	
	public AwardWithPeriodDto(AwardWithPeriod awardWithPeriod) {
	
		super();
		this.awardWithPeriod = awardWithPeriod;
	}
	
	public String getGameId() {
	
		return awardWithPeriod.getAward().getGameId();
	}
	
	public String getPeriodNo() {
	
		return awardWithPeriod.getAward().getPeriodNo();
	}
	
	public String getPrizeNumber() {
	
		return awardWithPeriod.getAward().getPrizeNumber();
	}
	
	public String getRehearsalNumber() {
	
		return awardWithPeriod.getAward().getRehearsalNumber();
	}
	
	public String getBonusLevelDetail() {
	
		return awardWithPeriod.getAward().getBonusLevelDetail();
	}
	
	public String getPrizeNumberDetail() {
	
		return awardWithPeriod.getAward().getPrizeNumberDetail();
	}
	
	public long getTotalSales() {
	
		return awardWithPeriod.getAward().getTotalSales();
	}
	
	public long getBonusPool() {
	
		return awardWithPeriod.getAward().getBonusPool();
	}
	
	public Date getOffcialStartTime() {
	
		return awardWithPeriod.getPeriod().getOffcialStartTime();
	}
	
	public Date getOffcialEndTime() {
	
		return awardWithPeriod.getPeriod().getOffcialEndTime();
	}
	
	public Date getOffcialAwardTime() {
	
		return awardWithPeriod.getPeriod().getOffcialAwardTime();
	}
}
