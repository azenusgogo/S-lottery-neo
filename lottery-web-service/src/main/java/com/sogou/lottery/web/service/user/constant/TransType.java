package com.sogou.lottery.web.service.user.constant;

public enum TransType {
	// 数字是彩票前后台的编码 // 钱包编码 //中文描述
	Recharge(1, "CHARGE", "充值"),
	WithDraw(2, "DEPOSIT", "提现"),
	Consume(3, "PAY", "消费"),
	Refund(4, "DRAWBACK", "退款"),
	WithDrawFee(5, "DEPOSIT_FEE", "提现手续费"),
	Award(6, "BONUS_LOTTERY", "彩票奖金");
	
	private int type;
	private String qianBaoType;
	private String desc;
	
	private TransType(int type, String qianBaoType, String desc) {
	
		this.type = type;
		this.qianBaoType = qianBaoType;
		this.desc = desc;
	}
	
	public int getType() {
	
		return type;
	}
	
	public String getQianBaoType() {
	
		return qianBaoType;
	}
	
	public String getDesc() {
	
		return desc;
	}
	
	public static String getDesc(Integer tt) {
		
		if (tt == null) {
			return null;
		}
		int type = tt;
		for (TransType t : TransType.values()) {
			if (t.getType() == type) {
				return t.getDesc();
			}
		}
		return null;
	}
	
	// public static boolean isValid(Integer tt) {
	//
	// try {
	// return getQianBaoType(tt) != null;
	// } catch (Exception e) {
	// return false;
	// }
	// }
	//
	// public static Integer getType(String qianBaoType) {
	//
	// for (TransType t : TransType.values()) {
	// if (t.getQianBaoType().equals(qianBaoType)) {
	// return t.getType();
	// }
	// }
	// return null;
	// }
	//
	// public static String getQianBaoType(Integer tt) {
	//
	// int type = tt;
	// for (TransType t : TransType.values()) {
	// if (t.getType() == type) {
	// return t.getQianBaoType();
	// }
	// }
	// throw new IllegalArgumentException("" + tt);
	// }
	

}
