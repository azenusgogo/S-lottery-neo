package com.sogou.lottery.web.ajax.dto;

import java.io.Serializable;
import java.util.Date;

import com.sogou.lottery.base.vo.sports.GameMatchWithMatch;

public class GameMatchWithMatchDto implements Serializable {
	
	private static final long serialVersionUID = -6850335985406672488L;
	private GameMatchWithMatch gameMatchWithMatch;
	
	public GameMatchWithMatchDto(GameMatchWithMatch gameMatchWithMatch) {
	
		super();
		this.gameMatchWithMatch = gameMatchWithMatch;
	}
	
	public long getGameMatchNo() {
	
		return gameMatchWithMatch.getGameMatch().getGameMatchNo();
	}
	
	public String getGameMatchNoShort() {
	
		return gameMatchWithMatch.getGameMatch().getGameMatchNoShort();
	}
	
	public short getConcedeScores() {
	
		return gameMatchWithMatch.getGameMatch().getConcedeScores();
	}
	
	public String getSp() {
	
		return gameMatchWithMatch.getGameMatch().getSp();
	}
	
	public String getBidScore() {
	
		return gameMatchWithMatch.getGameMatch().getBidScore();
	}
	
	public String getOdds() {
	
		return gameMatchWithMatch.getGameMatch().getOdds();
	}
	
	public String getLocalBetPercentage() {
	
		return gameMatchWithMatch.getGameMatch().getLocalBetPercentage();
	}
	
	public String getLeagueName() {
	
		return gameMatchWithMatch.getMatch().getLeagueName();
	}
	
	public String getLabelColor() {
	
		return gameMatchWithMatch.getMatch().getLabelColor();
	}
	
	public String getHomeTeamName() {
	
		return gameMatchWithMatch.getMatch().getHomeTeamName();
	}
	
	public String getAwayTeamName() {
	
		return gameMatchWithMatch.getMatch().getAwayTeamName();
	}
	
	public Date getKickOffTime() {
	
		return gameMatchWithMatch.getMatch().getKickOffTime();
	}
	
	public int getHalftimeHomeScores() {
	
		return gameMatchWithMatch.getMatch().getHalftimeHomeScores();
	}
	
	public int getHalftimeAwayScores() {
	
		return gameMatchWithMatch.getMatch().getHalftimeAwayScores();
	}
	
	public int getFulltimeHomeScores() {
	
		return gameMatchWithMatch.getMatch().getFulltimeHomeScores();
	}
	
	public int getFulltimeAwayScores() {
	
		return gameMatchWithMatch.getMatch().getFulltimeAwayScores();
	}
	
	public short getHomeTeamRankings() {
	
		return gameMatchWithMatch.getMatch().getHomeTeamRankings();
	}
	
	public short getAwayTeamRankings() {
	
		return gameMatchWithMatch.getMatch().getAwayTeamRankings();
	}
	
	public String getPartner7mMatchId() {
	
		return gameMatchWithMatch.getGameMatch().getPartner7mMatchId();
	}
	
}
