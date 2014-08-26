package com.sogou.lottery.common.constant;

public class MemcachedConstant {

    public static final String USERINFO = "userinfo_";
    public static final String USERNICKNAME = "userNickName_";

    public static String getUserinfoKey(String userId){
        return USERINFO + userId;
    }

    public static String getUserNickNameKey(String userId){
        return USERNICKNAME + userId;
    }
}
