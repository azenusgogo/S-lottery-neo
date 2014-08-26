package com.sogou.lottery.web.service.qianbao;

public enum QianBaoError {
	
	BlankArgument(10001, "参数不能为空"),
	SignError(10002, "签名错误"),
	EncodeError(10003, "编码转换错误"),
	PsidError(10004, "PSID 未登记"),
	UserNotExist(10005, "用户不存在"),
	AccountIncomplet(10006, "请补全账户"),
	SendCodeError(10007, "发送验证码失败"),
	CodeError(10008, "验证码错误"),
	PwdFrozen(10009, "支付密码连续输入错误 5 次被冻结"),
	PwdError(10010, "支付密码错误"),
	BindMobileError(10011, "绑定手机失败"),
	SafeQuesitonError(10012, "获取安全问题失败"),
	AnswnerError(10013, "安全问题答案错误"),
	NotBindMobile(10014, "用户未绑定手机"),
	RechargeError(10015, "获取充值记录失败"),
	InBankError(10016, "收款银行账户错误"),
	DrawLimitError(10017, "提现金额超出范围"),
	DrawAmountError(10018, "可提现金额不足"),
	DrawError(10019, "提现失败"),
	DrawQueryError(10020, "查询提现记录失败"),
	SafeQuestionChangeError(10021, "修改安全问题失败"),
	FindPwdByQuestionError(10022, "通过安全问题找回支付密码失败"),
	FindPwdByMobileError(10023, "通过绑定手机找回支付密码失败"),
	AccountExistError(10101, "账户已经存在，无需再补全"),
	BindMobileFormatError(10102, "绑定手机格式错误"),
	BindEmailFormatError(10103, "绑定邮箱格式错误"),
	SafeQuesiontFormatError(10104, "安全问题答案长度不能大于 40 个字节或 20 个汉字"),
	SafeAnswnerFormatError(10105, "安全问题长度不能大于 40 个字节或 20 个汉字"),
	CompleteAccountError(10199, "补全账户失败"),
	AmountFormatError(10201, "订单金额格式错误"),
	AmountLimitError(10202, "订单金额超出范围"),
	BalanceError(10203, "账户余额不足"),
	ArgumentNotSameError(10204, "参数不一致"),
	PayError(10299, "支付错误"),
	OtherError(10990, "其他错误（见接口具体返回的描述）"),
	SystemError(10999, "系统错误");
	
	int code;
	String desc;
	
	QianBaoError(int code, String desc) {
	
		this.code = code;
		this.desc = desc;
	}
	
	public int getCode() {
	
		return code;
	}
	
	public String getDesc() {
	
		return desc;
	}
	
	public static QianBaoError getErrorCode(int errorCode) {
	
		for (QianBaoError error : QianBaoError.values()) {
			if (error.getCode() == errorCode) {
				return error;
			}
		}
		return null;
	}
}
