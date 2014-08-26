package com.sogou.lottery.web.service.user.constant;

/**
 * 描述：用户账户域名
 * Created by haojiaqi on 14-4-16.
 */
public enum UserAccountDomainEnum {

    UNKOWN("unkown"), SOGOU("sogou.com"),SOHU("sohu.com"),BAIDU("baidu.sohu.com"),SINA("sina.sohu.com"),RENREN("renren.sohu.com"),QQ("qq.sohu.com");

    UserAccountDomainEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


