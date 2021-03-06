<#include "common.ftl" />

<#assign timeLevel = 3 />
<#assign pageNum = 15 />
<#assign pageSize = 10 />
<#assign winAwardFlag = 1 />
<#assign waitAwardFlag = 0 />
<#assign timeValidOrderFlag = 1 />
<#assign orderId = "12345" />

<#assign orderDetail = {
	"orderId":"14032819000000000005",
	"totalBetNums":10,
	"splitFlag":false,
	"pageNo":3,
	"pageSize":10,
	"totalNum":100,
	"totalPage":3,
	"prizeNumber":"3 4 5,12,1,1,0,1,0",
	"orderInfo":{
			"userOrderId":"12345",
			"gameId":"k3js",
			"gameDesc":"江苏快3",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":2,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":10,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"officialBonus":20000
	},
	"stakeBetInfoDtoList":[
		{
			"betNumber":{
				"betNumberId":"11",
				"userOrderId":"14030712000000000001",
				"stakeOrderId":"14030612000000000001S00000000",
				"localBetNumber":"HZ_5",
				"chooseType":1,
				"stakes":1,
				"overage":"peilv",
				"betTimes":"1",
				"betType":"",
				"playType":"胆拖",
				"betNumberStatus":1,
				"amount":1000,
				"gameId":"ssq",
				"periodNo":"12121221"
			},
			"stakeOrder":{
				"userOrderId":"14030712000000000001",
				"stakeOrderId":"14030612000000000001S00000000",
				"packageId":"14030712000000000001P00000001",
				"stakeAmount":90000,
				"stakeOrderStatus":1,
				"stakeOrderStatusDesc":"已出票",
				"stakePrizeStatus":2,
				"stakePrizeStatusDesc":"已中奖",
				"stakeBonus":50,
				"platformStakeOrderId":"00000000000100000001",
				"officialTicketId":"1395981106285",
				"betTimes":2,
				"betType":"type",
				"betTypeDesc":"单式",
				"platformCommission":0,
				"platformId":"lecai",
				"totalBetNumbers":5
			},
			"betNumberList":[
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"HZ_5,15",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"AAA_*",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"AAA_111,222",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"3BT_123",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"3BT_1234",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				}
			]
		},
		{
			"betNumber":{
				"betNumberId":"11",
				"userOrderId":"14030712000000000001",
				"stakeOrderId":"14030612000000000001S00000000",
				"localBetNumber":"2BT_1$23",
				"chooseType":1,
				"stakes":1,
				"overage":"peilv",
				"betTimes":"1",
				"betType":"",
				"playType":"胆拖",
				"betNumberStatus":1,
				"amount":1000,
				"gameId":"ssq",
				"periodNo":"12121221"
			},
			"stakeOrder":{
				"userOrderId":"14030712000000000001",
				"stakeOrderId":"14030612000000000001S00000000",
				"packageId":"14030712000000000001P00000001",
				"stakeAmount":90000,
				"stakeOrderStatus":1,
				"stakeOrderStatusDesc":"已出票",
				"stakePrizeStatus":2,
				"stakePrizeStatusDesc":"已中奖",
				"stakeBonus":50,
				"platformStakeOrderId":"00000000000100000001",
				"officialTicketId":"1395981106285",
				"betTimes":2,
				"betType":"type",
				"betTypeDesc":"单式",
				"platformCommission":0,
				"platformId":"lecai",
				"totalBetNumbers":5
			},
			"betNumberList":[
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"3BT_12$34",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"3LH_*",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"AA_11",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"AA_11,22",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"AA_11,22",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				}
			]
		},
		{
			"betNumber":{
				"betNumberId":"11",
				"userOrderId":"14030712000000000001",
				"stakeOrderId":"14030612000000000001S00000000",
				"localBetNumber":"AAX_11,22|3,4",
				"chooseType":1,
				"stakes":1,
				"overage":"peilv",
				"betTimes":"1",
				"betType":"",
				"playType":"胆拖",
				"betNumberStatus":1,
				"amount":1000,
				"gameId":"ssq",
				"periodNo":"12121221"
			},
			"stakeOrder":{
				"userOrderId":"14030712000000000001",
				"stakeOrderId":"14030612000000000001S00000000",
				"packageId":"14030712000000000001P00000001",
				"stakeAmount":90000,
				"stakeOrderStatus":1,
				"stakeOrderStatusDesc":"已出票",
				"stakePrizeStatus":2,
				"stakePrizeStatusDesc":"已中奖",
				"stakeBonus":50,
				"platformStakeOrderId":"00000000000100000001",
				"officialTicketId":"1395981106285",
				"betTimes":2,
				"betType":"type",
				"betTypeDesc":"单式",
				"platformCommission":0,
				"platformId":"lecai",
				"totalBetNumbers":5
			},
			"betNumberList":[
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"AAX_11|2",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"AAX_11,22|3,4",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"2BT_12",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"2BT_123",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				},
				{
					"betNumberId":"11",
					"userOrderId":"14030712000000000001",
					"stakeOrderId":"14030612000000000001S00000000",
					"localBetNumber":"2BT_1$23",
					"chooseType":1,
					"stakes":1,
					"overage":"peilv",
					"betTimes":"1",
					"betType":"",
					"playType":"胆拖",
					"betNumberStatus":1,
					"amount":1000,
					"gameId":"ssq",
					"periodNo":"12121221"
				}
			]
		}
	]
} />





<#assign orderList = {
	"totalNum":150,
	"totalPage":15,
	"orderQueryDto":{
		"pageNo":12,
		"timeLevel":3,
		"userId":"abc",
		"orderId":"12345",
		"prizeStatus":1,
		"timeValidOrderFlag":true,
		"pageSize":10
	},
	"orderInfoList":[
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":2,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2014-04-13 19:13"?datetime("yyyy-MM-dd HH:mm"),
			"officialBonus":20000
		},
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":1,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2014-04-14 19:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":1,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2014-04-15 19:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":1,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2014-04-10 19:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":1,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2014-04-12 19:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":1,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2014-04-12 10:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":1,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":1,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":1,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		},
		{
			"userOrderId":"12345",
			"gameId":"ssq",
			"gameDesc":"大乐透",
			"periodNo":"20140101",
			"orderType":2,
			"orderTypeDesc":"合买",
			"orderStatus":1,
			"orderStatusDesc":"支付完成",
			"splitFlag":true,
			"prizeStatus":3,
			"prizeStatusDesc":"大奖",
			"payerUserId":"abc",
			"bettorUserId":"touzhu",
			"bettorName":"姓名",
			"bettorIdno":"130604190000000000",
			"userOrderAmount":200,
			"refundAmount":0,
			"deliverable":true,
			"betTimes":3,
			"totalBetNumbers":0,
			"succBetNumbers":0,
			"failedBetNumbers":0,
			"createTime":"2013-03-2 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"deadline":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}

	]
} />