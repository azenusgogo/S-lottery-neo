package com.sogou.lottery.web.service.user.constant;

/**
 * @author huangtao 这是线下的配置，线上配在数据库Dictionary里
 */
@Deprecated
public enum BankId {
	
	// 前后台统一编码 //QianBao编码 //中文名称
	AlipayCB("ALICB", 406, /* 支付宝-中行 */"中国银行"),
	AlipayICBC("ALIICBC", 407, /* "支付宝-工行" */"工商银行"),
	AlipayZSYH("ALIZSYH", 408, /* "支付宝-招行" */"招商银行"),
	AlipayCCB("ALICCB", 409, /* "支付宝-建行" */"建设银行"),
	AlipayABC("ALIABC", 410, /* "支付宝-农行" */"中国农业银行"),
	AlipayPFYH("ALIPFYH", 411, /* "支付宝-浦发" */"浦发银行"),
	AlipayXYYH("ALIXYYH", 412, /* "支付宝-兴业银行" */"兴业银行"),
	AlipayGFYH("ALIGFYH", 413, /* "支付宝-广发" */"广发银行"),
	AlipaySFZ("ALISFZ", 414, /* "支付宝-深发" */"深圳发展银行"),
	AlipayMSYH("ALIMSYH", 415, /* "支付宝-民生银行" */"民生银行"),
	AlipayJTYH("ALIJTYH", 416, /* "支付宝-交通银行" */"交通银行"),
	AlipayZXYH("ALIZXYH", 417, /* "支付宝-中信银行" */"中信银行"),
	AlipayHZYH("ALIHZYH", 418, /* "支付宝-杭州银行" */"杭州银行"),
	AlipayGDYH("ALIGDYH", 419, /* "支付宝-光大银行" */"光大银行"),
	AlipaySHYH("ALISHYH", 420, /* "支付宝-上海银行" */"上海银行"),
	AlipayNBYH("ALINBYH", 421, /* "支付宝-宁波银行" */"宁波银行"),
	AlipayPAYH("ALIPAYH", 422, /* "支付宝-平安银行" */"平安银行"),
	AlipayBJNCXYS("ALIBJNCXYS", 423, /* "支付宝-北京农村信用社" */"北京农村信用社"),
	AlipayYZCX("ALIYZCX", 425, /* "支付宝-邮政储蓄" */"邮政储蓄银行"),
	Tecent("TECENT", 308, /* "财付通账户" */"财付通"),
	Alipay("ALIPAY", 307, /* "支付宝账户" */"支付宝");
	
	private String code;
	private int qianBaoCode;
	private String desc;
	
	private BankId(String code, int qianBaoCode, String desc) {
	
		this.code = code;
		this.qianBaoCode = qianBaoCode;
		this.desc = desc;
	}
	
	public String getCode() {
	
		return code;
	}
	
	public int getQianBaoCode() {
	
		return qianBaoCode;
	}
	
	public String getDesc() {
	
		return desc;
	}
	
	public static String getDesc(String code) {
	
		for (BankId payGate : BankId.values()) {
			if (payGate.code.equals(code)) {
				return payGate.getDesc();
			}
		}
		return null;
	}
	
	// public static boolean isValid(String tt) {
	//
	// return getQianBaoCode(tt) != null;
	// }
	//
	// public static String getCode(int qianBaoPayCode) {
	//
	// for (BankCode payGate : BankCode.values()) {
	// if (payGate.qianBaoCode == qianBaoPayCode) {
	// return payGate.getCode();
	// }
	// }
	// return null;
	// }
	//
	// public static Integer getQianBaoCode(String code) {
	//
	// for (BankCode payGate : BankCode.values()) {
	// if (payGate.code.equals(code)) {
	// return payGate.getQianBaoCode();
	// }
	// }
	// return null;
	// }
	//
	
}
