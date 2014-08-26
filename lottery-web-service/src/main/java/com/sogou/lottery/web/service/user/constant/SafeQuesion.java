package com.sogou.lottery.web.service.user.constant;

public enum SafeQuesion {
	
	MotherName(1, "您妈妈的名字是什么"),
	BirthPlace(2, "您的出生地在哪"),
	SchoolName(3, "我的小学叫什么名字"),
	PetName(4, "我的宠物名字叫什么"),
	PrimaryTeacher(5, "我小学第一位班主任姓名是"),
	MiddleTeacher(6, "我中学第一位班主任姓名是"),
	MiddleMath(7, "我中学第一位数学老师姓名是");
	
	private Integer type;
	private String desc;
	
	private SafeQuesion(Integer type, String desc) {
	
		this.type = type;
		this.desc = desc;
	}
	
	public Integer getType() {
	
		return type;
	}
	
	public String getDesc() {
	
		return desc;
	}
	
	public String getDesc(Integer t) {
	
		if (t == null) {
			return null;
		}
		int tt = t;
		for (SafeQuesion q : values()) {
			if (tt == q.getType()) {
				return q.getDesc();
			}
		}
		return null;
	}
	
}
