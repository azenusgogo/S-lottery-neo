<#include "common.ftl" />

<#assign orderId = "20140317CP012315" />
<#assign todayOpenAward = 1 />
<#assign awardPool = "1_7469_4203:34" />
<#assign nextPeriod = "20140318" />
<#assign systemTime = 3333333333333 />
<#assign prevPeriod = "20140316" />
<#assign presentMissingStr = "4 0 4 8 0 2 5 1 2 0 8 10 6 7 0 2 4 2 0 0 1 4 0 3 4 0 1 1 3 1" />

<#assign game = {
	"gameId":"qlc",
	"gameCn":"七乐彩",
	"gameType":1,
	"gameStatus":1
} />

<#assign availablePeriod = {
	"gameId":"qlc",
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
			"gameId":"qlc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 03 04 05 06 07:08",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qlc",
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
			"gameId":"qlc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 03 04 05 06 07:08",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qlc",
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
			"gameId":"qlc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 03 04 05 06 07:08",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qlc",
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
			"gameId":"qlc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 03 04 05 06 07:08",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qlc",
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
			"gameId":"qlc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 03 04 05 06 07:08",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qlc",
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
			"gameId":"qlc",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 03 04 05 06 07:08",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"qlc",
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



