package com.sogou.lottery.web.service.user.dto;

import com.sogou.lottery.base.vo.BaseObject;
import com.sogou.lottery.base.vo.user.User;

import java.io.Serializable;

public class UserInfoDto extends BaseObject implements Serializable {
    private static final long serialVersionUID = -8273343814702263351L;

    //第三方用户昵称
    private String partyUserNickName;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPartyUserNickName() {
        return partyUserNickName;
    }

    public void setPartyUserNickName(String partyUserNickName) {
        this.partyUserNickName = partyUserNickName;
    }
}
