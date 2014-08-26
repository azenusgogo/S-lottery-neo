<#include "common.ftl" />

<#assign todayOpenAwardGameId = "ssq,dlt">

<#assign awardWithPeriodList = [
	{
		"award":{
			"gameId":"ssq",
			"periodNo":"2014017",
			"prizeNumber":"01 02 03 04 05 06:01",
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
			"bonusLevelDetail":"1_10_815230000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
			"prizeNumber":"01 02 07 15 26 06:12",
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
	}
] />

<#assign ssqAward = {
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
} />

<#assign dltAward = {
	"award":{
		"gameId":"ssq",
		"periodNo":"2014017",
		"bonusLevelDetail":"1_10_610050995;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
		"prizeNumber":"01 02 07 15 26:06 12",
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
} />

<#assign qxcAward = {
	"award":{
		"gameId":"ssq",
		"periodNo":"2014017",
		"bonusLevelDetail":"1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
		"prizeNumber":"01 02 07 15 26 15 06",
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
} />

<#assign today = "2014-04-02 (周三)" />
<#assign todayOpenAwardGameList = [
	{
		"gameId":"ssq",
		"gameCn":"双色球"
	},
	{
		"gameId":"dlt",
		"gameCn":"大乐透"
	}
] />

<#assign commonOpenAwardDtoList = [
	{
		"gameCn":"双色球",
		"gameId":"ssq",
		"periodNo":"2014001",
		"openAwardDate":"昨天（周四）",
		"prizeNumber":"05 06 01 21 33 12:06",
		"topBonus":"0_1000_0",
		"bonusPool":"2_5000_0",
		"recentNews":"2.4亿大加奖",
		"hotPlayMethod":"任五玩法"
	},
	{
		"gameCn":"大乐透",
		"gameId":"dlt",
		"periodNo":"2014001",
		"openAwardDate":"昨天（周四）",
		"prizeNumber":"05 06 01 21 33 12:06 08",
		"topBonus":"0_1000_0",
		"bonusPool":"2_5000_0",
		"isTodayOpen":true,
		"recentNews":"2.4亿大加奖",
		"hotPlayMethod":"任五玩法"
	},
	{
		"gameCn":"七乐彩",
		"gameId":"qlc",
		"periodNo":"2014001",
		"openAwardDate":"昨天（周四）",
		"prizeNumber":"05 06 01 21 33 12 15",
		"bonusPool":"2_5000_0",
		"isTodayOpen":false,
		"recentNews":"2.4亿大加奖",
		"hotPlayMethod":"任五玩法"
	},
	{
		"gameCn":"七星彩",
		"gameId":"qxc",
		"periodNo":"2014001",
		"openAwardDate":"昨天（周四）",
		"prizeNumber":"05 06 01 21 33 12 06",
		"topBonus":"0_1000_0",
		"recentNews":"2.4亿大加奖",
		"hotPlayMethod":"任五玩法"
	}

] />

<#assign sportOpenAwardDtoList = [
	{
		"gameCn":"胜负彩",
		"gameId":"f14",
		"periodNo":"2014001",
		"openAwardDate":"昨天（周四）",
		"prizeNumber":"3 1 0 0 0 1 3 0 3 1 *",
		"topBonus":"0_1000_0",
		"bonusPool":"2_5000_0",
		"recentNews":"2.4亿大加奖",
		"hotPlayMethod":"任五玩法"
	},
	{
		"gameCn":"任选九",
		"gameId":"f9",
		"periodNo":"2014001",
		"openAwardDate":"昨天（周四）",
		"prizeNumber":"3 1 0 0 0 1 3 0 3 1 *",
		"topBonus":"0_1000_0",
		"bonusPool":"2_5000_0",
		"isTodayOpen":true,
		"recentNews":"2.4亿大加奖",
		"hotPlayMethod":"任五玩法"
	}
] />

<#assign hfOpenAwardDtoList = [
	{
		"gameCn":"新快3",
		"gameId":"k3gx",
		"periodNo":"2014001",
		"openAwardDate":"今天 18:15",
		"prizeNumber":"2 1 5,11,0,0,1,0,0,0,1",
		"topBonus":"0_1000_0",
		"bonusPool":"2_5000_0",
		"recentNews":"2.4亿大加奖",
		"hotPlayMethod":"二同号复选"
	},
	{
		"gameCn":"老快3",
		"gameId":"k3js",
		"periodNo":"2014001",
		"openAwardDate":"今天 18:15",
		"prizeNumber":"2 1 5,6,0,0,1,0,0,0,1",
		"topBonus":"0_1000_0",
		"bonusPool":"2_5000_0",
		"isTodayOpen":true,
		"recentNews":"2.4亿大加奖",
		"hotPlayMethod":"和值"
	},
	{
		"gameCn":"快3",
		"gameId":"k3jl",
		"periodNo":"2014001",
		"openAwardDate":"今天 18:15",
		"prizeNumber":"2 1 5,10,0,0,1,0,0,0,1",
		"topBonus":"0_1000_0",
		"bonusPool":"2_5000_0",
		"isTodayOpen":true,
		"recentNews":"2.4亿大加奖",
		"hotPlayMethod":"和值"
	}
] />