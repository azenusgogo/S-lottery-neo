package com.sogou.lottery.web.web.dto.index;

import com.sogou.lottery.base.vo.game.Game;

public class GameDescDto {
	
	private Game game;
	private String desc;
	private String tag;
	
	public void setGame(Game game) {
	
		this.game = game;
	}
	
	public void setDesc(String desc) {
	
		this.desc = desc;
	}
	
	public void setTag(String tag) {
	
		this.tag = tag;
	}
	
	public String getGameId() {
	
		return game.getGameId();
	}
	
	public String getGameCn() {
	
		return game.getGameCn();
	}
	
	public String getDesc() {
	
		return desc;
	}
	
	public String getTag() {
	
		return tag;
	}
	
}
