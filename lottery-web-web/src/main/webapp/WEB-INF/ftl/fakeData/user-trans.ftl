<#include "common.ftl" />

<#assign transDto = {
	"month":1,
	"pageNo":1,
	"pageSize":10,
	"type":1
} />

<#assign trans = {
	"total":0,
	"totalPage":10,
	"pageNo":10,
	"pageSize":10,
	"chargeAmount":0,
	"withDrawAmount":0,
	"payAmount":0,
	"refundAmount":0,
	"bonusAmount":0,
	"transList":[
		{
			"transactionId":"58531049424",
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"type":1,
			"typeDesc":"充值",
			"amount":9991,
			"channel":"支付宝",
			"status":1,
			"statusDesc":"成功",
			"confirmTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"Fee":200
		},
		{
			"transactionId":"58531049424",
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"type":2,
			"typeDesc":"提现",
			"amount":9999911,
			"channel":"建设银行",
			"status":1,
			"statusDesc":"失败",
			"Fee":100
		},
		{
			"transactionId":"58531049424",
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"type":1,
			"typeDesc":"消费",
			"amount":9999911,
			"channel":"支付宝",
			"status":1,
			"statusDesc":"成功",
			"confirmTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"Fee":50
		},
		{
			"transactionId":"58531049424",
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"type":1,
			"typeDesc":"消费",
			"amount":9999911,
			"channel":"支付宝",
			"status":1,
			"statusDesc":"成功",
			"confirmTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"transactionId":"58531049424",
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"type":1,
			"typeDesc":"消费",
			"amount":9999911,
			"channel":"支付宝",
			"status":1,
			"statusDesc":"成功",
			"confirmTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"transactionId":"58531049424",
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"type":1,
			"typeDesc":"消费",
			"amount":9999911,
			"channel":"支付宝",
			"status":1,
			"statusDesc":"成功",
			"confirmTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"transactionId":"58531049424",
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"type":1,
			"typeDesc":"消费",
			"amount":9999911,
			"channel":"支付宝",
			"status":1,
			"statusDesc":"成功",
			"confirmTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	]

} />