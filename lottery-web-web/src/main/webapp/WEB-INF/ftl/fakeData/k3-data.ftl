<#include "common.ftl" />

<#assign orderId = "20140317CP012315" />
<#assign todayOpenAward = 1 />
<#assign nextPeriod = "20140318" />
<#assign prevPeriod = "20140316" />
<#assign systemTime = "2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm") />

<#assign presentMissingStr = "126 58 30 85 8 9 21 1 28 18 3 17 21 24 14 13:10:120 172 20 39 52 13:0 4 1 2 2 1:2:0 66 10 3 5 13:9 86 10 3 7 14 23 18 7 25 3 9:0 4 1 21 2 1" />

<#assign maxMissingStr = "126 58 30 85 8 9 21 1 28 18 3 17 21 24 14 13:10:120 172 20 39 52 13:0 4 1 2 2 1:2:0 66 10 3 5 13:9 86 10 3 7 14 23 18 7 25 3 9:0 4 1 21 2 1" />

<#assign game = {
	"gameId":"k3gx",
	"gameCn":"快3",
	"gameType":1,
	"gameStatus":1
} />



<#assign formStatList = [
	"三同号:1,三不同号:2,三连号:3,二同号:4,二不同号:5",
	"三同号:1,三不同号:2,三连号:3,二同号:4,二不同号:5",
	"三同号:1,三不同号:2,三连号:3,二同号:4,二不同号:5"
] />

<#assign sumStatList = [
	"",
	"10:72,6:22,5:31,12:4,18:51,18:51,18:51,18:51,18:51,18:51,18:51,18:51,18:51,18:51,18:51,12:4",
	"10:72,6:22,5:31,12:4,18:51,18:51,18:51,18:51,18:51,18:51,18:51,18:51,18:51,18:51,18:51,12:4"
] />



<#assign availablePeriod = {
	"gameId":"k3",
	"periodNo":"140328001",
	"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
} />

<#assign awardList = [
	{
		"award":{
			"gameId":"k3jl",
			"periodNo":"140327075",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 1 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3jl",
			"periodNo":"2014032843",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014016",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 1 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014016",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014015",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 6,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014015",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014014",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014014",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	}
] />
<#assign todayAwardList = [
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014032878",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 1 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 1 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 6,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	},
	{
		"award":{
			"gameId":"k3",
			"periodNo":"2014017",
			"bonusLevelDetail":"10 6994695 10 6994695 10 6994695 10 6994695 10 6994695 10 6994695",
			"prizeNumber":"2 3 5,10,0,0,1,0,0,0,1",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"k3",
			"periodNo":"2014017",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	}
] />