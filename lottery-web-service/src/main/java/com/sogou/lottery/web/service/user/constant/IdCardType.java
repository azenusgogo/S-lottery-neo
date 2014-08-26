package com.sogou.lottery.web.service.user.constant;

public enum IdCardType {
	
	Id(1, "IDENTITY", "身份证"), Military(2, "MILITARY", "军官证");
	
	private Integer type;
	private String qianBaoType;
	private String desc;
	
	private IdCardType(Integer type, String qianBaoType, String desc) {
	
		this.type = type;
		this.qianBaoType = qianBaoType;
		this.desc = desc;
	}
	
	public Integer getType() {
	
		return type;
	}
	
	public String getQianBaoType() {
	
		return qianBaoType;
	}
	
	public String getDesc() {
	
		return desc;
	}
	
	public static boolean isValid(Integer tt) {
	
		try {
			return getQianBaoType(tt) != null;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	public static String getQianBaoType(Integer tt) {
	
		int qianBaoPayType = tt;
		for (IdCardType type : IdCardType.values()) {
			if (qianBaoPayType == type.type) {
				return type.getQianBaoType();
			}
		}
		throw new IllegalArgumentException("" + tt);
	}
	
	public static Integer getType(String code) {
	
		if (code == null) {
			return null;
		}
		for (IdCardType type : IdCardType.values()) {
			if (type.qianBaoType.equals(code)) {
				return type.getType();
			}
		}
		throw new IllegalArgumentException(code);
	}
	
	public static String getDesc(Integer tt) {
	
		if (tt == null) {
			return null;
		}
		int code = tt;
		for (IdCardType type : IdCardType.values()) {
			if (type.type == code) {
				return type.getDesc();
			}
		}
		return null;
	}
}
