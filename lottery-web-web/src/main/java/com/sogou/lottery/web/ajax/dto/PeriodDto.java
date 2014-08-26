package com.sogou.lottery.web.ajax.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.sogou.lottery.base.vo.period.Period;

public class PeriodDto implements Serializable {
	
	private static final long serialVersionUID = 4873921627476627615L;
	private Period period;
	
	public PeriodDto(Period period) {
	
		super();
		this.period = period;
	}
	
	/**
	 * 得到本期实际开始时间
	 * 
	 * @return
	 */
	public Timestamp getStartTime() {
	
		return period.getStartTime();
	}
	
	/**
	 * 得到本期实际结束时间
	 * 
	 * @return
	 */
	public Timestamp getEndTime() {
	
		return period.getEndTime();
	}
	
	/**
	 * 得到本期合买实际结束时间
	 * 
	 * @return
	 */
	public Timestamp getGroupBuyEndTime() {
	
		return period.getGroupBuyEndTime();
	}
	
	public String getGameId() {
	
		return period.getGameId();
	}
	
	public String getPeriodNo() {
	
		return period.getPeriodNo();
	}
	
	public Date getOffcialStartTime() {
	
		return period.getOffcialStartTime();
	}
	
	public Date getOffcialEndTime() {
	
		return period.getOffcialEndTime();
	}
	
	public Date getOffcialAwardTime() {
	
		return period.getOffcialAwardTime();
	}
	
	public short getAwardStatus() {
	
		return period.getAwardStatus();
	}
	
	public short getPeriodStatus() {
	
		return period.getPeriodStatus();
	}
	
}
