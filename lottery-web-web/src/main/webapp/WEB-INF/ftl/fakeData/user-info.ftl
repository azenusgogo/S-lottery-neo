<#include "common.ftl" />
<#assign captchaKey = "1234" />
<#assign orderId = "14031818000000000001" />

<#assign userid = "figo" />
<#assign minAmount  = 100 />
<#assign recharge = {
	"id":"",
	"orderId":"14031818000000000001",
	"amount":5199,
	"rechargeAmount":520,
	"bankId":"409",
	"token":"SASDASD1232132",
	"returl":"/"
} />

<#assign chargeBank = [
	{
		"bankId":406,
		"bank":"中国银行"
	},
	{
		"bankId":407,
		"bank":"工商银行"
	},
	{
		"bankId":408,
		"bank":"招商银行"
	},
	{
		"bankId":409,
		"bank":"建设银行"
	},
	{
		"bankId":410,
		"bank":"中国农业银行"
	},
	{
		"bankId":411,
		"bank":"浦发银行"
	},
	{
		"bankId":412,
		"bank":"兴业银行"
	},
	{
		"bankId":413,
		"bank":"广发银行"
	},
	{
		"bankId":414,
		"bank":"深圳发展银行"
	}
] />

<#assign idCardTypes = [
	{
		"type":1,
		"key":"1",
		"value":"身份证"
	},
	{
		"type":1,
		"key":"2",
		"value":"军官证"
	}
] />

<#assign questions = [
	{
		"code":1,
		"question":"您爸爸的名字是什么"
	},
	{
		"code":2,
		"question":"您妈妈的名字是什么"
	},
	{
		"code":3,
		"question":"您的出生地是哪里"
	},
	{
		"code":4,
		"question":"您爸爸的名字是什么"
	},
	{
		"code":5,
		"question":"您妈妈的名字是什么"
	},
	{
		"code":6,
		"question":"您的出生地是哪里"
	},
	{
		"code":7,
		"question":"你爸爸的名字是什么"
	}

] />

<#assign drawbanks = [
	{
		"bankId":409,
		"bank":"中国建设银行"
	},
	{
		"bankId":415,
		"bank":"中国民生银行"
	},
	{
		"bankId":408,
		"bank":"招商银行"
	},
	{
		"bankId":407,
		"bank":"中国工商银行"
	},
	{
		"bankId":410,
		"bank":"中国农业银行"
	},
	{
		"bankId":414,
		"bank":"深圳发展银行"
	},
	{
		"bankId":416,
		"bank":"交通银行"
	}
] />

<#assign userinfo = {
	"idCardType":1,
	"idCardNo":"130666198808081616",
	"email":"cp@sogou.com",
	"mobile":"13666666666",
	"trueName":"搜狗",
	"displayTrueName":"搜**",
	"displayEmail":"c*@sogou.com",
	"displayMobile":"136******88",
	"bankCardNo":"622888888888088",
	"bankId":1,
	"bank":"招商银行",
	"displayBankCardNo":"6228********088",
	"userid":"123",
	"userIp":"192.168.1.1",
	"payPwd":"E10ADC3949BA59ABBE56E057F20F883E",
	"captcha":"1234"
} />