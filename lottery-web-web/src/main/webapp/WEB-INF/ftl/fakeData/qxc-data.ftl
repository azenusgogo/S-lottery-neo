<#include "common.ftl" />

<#assign orderId = "20140317CP012315" />
<#assign todayOpenAward = 1 />
<#assign nextPeriod = "20140318" />
<#assign systemTime = 3333333333333 />
<#assign prevPeriod = "20140316" />
<#assign presentMissingStr = "40 6 1 20 9 4 3 2 13 0:3 22 9 1 4 0 19 2 16 5:0 5 4 21 7 2 3 34 1 44:3 4 5 1 13 7 0 6 12 9:5 10 3 14 6 1 18 0 12 22:25 2 6 13 24 8 1 0 14 5:2 13 4 1 9 3 0 21 16 24" />

<#assign game = {
	"gameId":"qxc",
	"gameCn":"七星彩",
	"gameType":1,
	"gameStatus":1
} />

<#assign availablePeriod = {
	"gameId":"qxc",
	"periodNo":"2014017",
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
			"gameId":"qxc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"0 1 2 3 4 5 6",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qxc",
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
			"gameId":"qxc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"0 1 2 3 4 5 6",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qxc",
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
			"gameId":"qxc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"0 1 2 3 4 5 6",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qxc",
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
			"gameId":"qxc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"0 1 2 3 4 5 6",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qxc",
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
			"gameId":"qxc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"0 1 2 3 4 5 6",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qxc",
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
			"gameId":"qxc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"0 1 2 3 4 5 6",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qxc",
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



