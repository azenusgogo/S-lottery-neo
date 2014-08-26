<#include "common.ftl" />
<#assign betNumberList = [
	{
	"localBetNumber":"1 2 3 4 5 8:1",
	"betTimes":1
	},
	{
	"localBetNumber":"1 2 3 4 5 8:1",
	"betTimes":1
	},
	{
	"localBetNumber":"1 2 3 4 5 8:1",
	"betTimes":1
	},
	{
	"localBetNumber":"1 2 3 4 5 8:1",
	"betTimes":1
	},
	{
	"localBetNumber":"1 2 3 4 5 8:1",
	"betTimes":1
	},
	{
	"localBetNumber":"1 2 3 4 5 8:1",
	"betTimes":1
	}
] />

<#assign orderId = "20140317CP012315" />
<#assign todayOpenAward = 1 />
<#assign awardPool = "3_234_3173:60" />
<#assign nextPeriod = "20140318" />
<#assign prevPeriod = "20140316" />
<#assign systemTime = 1111111111111 />
<#assign presentMissingStr = "1 20 7 7 0 10 3 1 0 2 1 6 0 2 0 5 0 6 1 3 0 9 8 1 4 17 7 6 5 5 3 2 3:12 2 29 8 13 31 43 1 16 14 3 5 0 6 9 4" />

<#assign game = {
	"gameId":"ssq",
	"gameCn":"双色球",
	"gameType":1,
	"gameStatus":1
} />

<#assign availablePeriod = {
	"gameId":"ssq",
	"periodNo":"2014014",
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
			"gameId":"ssq",
			"periodNo":"2014017",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 07 15 26 15:06",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"ssq",
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
			"gameId":"ssq",
			"periodNo":"2014016",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 07 15 26 28:06",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"ssq",
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
			"gameId":"ssq",
			"periodNo":"2014015",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 07 15 26 28:06",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"ssq",
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
			"gameId":"ssq",
			"periodNo":"2014014",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 07 15 26 28:06",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"ssq",
			"periodNo":"2014014",
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
			"gameId":"ssq",
			"periodNo":"2014013",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 07 15 26 28:06",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"ssq",
			"periodNo":"2014013",
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
			"gameId":"ssq",
			"periodNo":"2014012",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 07 15 26 28:06",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"ssq",
			"periodNo":"2014012",
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
			"gameId":"ssq",
			"periodNo":"2014011",
			"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 07 15 26 28:06",
			"totalSales":100000000,
			"bonusPool":50000000000
		},
		"period":{
			"gameId":"ssq",
			"periodNo":"2014011",
			"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
		}
	}
] />