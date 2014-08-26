package com.sogou.lottery.web.ajax.dto;

import java.io.Serializable;

import com.sogou.lottery.base.vo.game.Game;

public class GameDto implements Serializable {
	
	private static final long serialVersionUID = 369889580290611401L;
	private Game game;
	
	public GameDto(Game game) {
	
		super();
		this.game = game;
	}
	
	public String getGameId() {
	
		return game.getGameId();
	}
	
	public String getGameCn() {
	
		return game.getGameCn();
	}
	
	public int getGameType() {
	
		return game.getGameType();
	}
	
	public int getGameStatus() {
	
		return game.getGameStatus();
	}
	
}
