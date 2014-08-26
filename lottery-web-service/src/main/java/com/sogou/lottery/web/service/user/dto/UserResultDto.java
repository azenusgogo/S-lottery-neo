package com.sogou.lottery.web.service.user.dto;

import com.sogou.lottery.base.vo.user.User;

public class UserResultDto {
	
	private User user;
	
	public void setUser(User user) {
	
		this.user = user;
	}
	
	public String getUserId() {
	
		return user.getUserId();
	}
	
	public String getNickName() {
	
		return user.getNickName();
	}
	
	public String getTrueName() {
	
		return user.getTrueName();
	}

    private String partyUserNickName;//第三方用户昵称

    public String getPartyUserNickName() {
        return partyUserNickName;
    }

    public void setPartyUserNickName(String partyUserNickName) {
        this.partyUserNickName = partyUserNickName;
    }
}
