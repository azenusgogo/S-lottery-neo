<#include "common.ftl" />

<#assign systemTime = 9999999999999 />
<#assign todayOpenAward = 1 />

<#assign game = {
	"gameId":"f9",
	"gameCn":"任选九",
	"gameType":3,
	"gameStatus":1
} />

<#assign periodNo = "20140318" />

<#assign availablePeriodList = [
	{
	"gameId":"f9",
	"periodNo":"20140317",
	"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"endTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
	},
	{
	"gameId":"f9",
	"periodNo":"20140318",
	"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"endTime":"2013-03-26 11:18"?datetime("yyyy-MM-dd HH:mm"),
	"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
	},
	{
	"gameId":"f9",
	"periodNo":"20150319",
	"startTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"endTime":"2013-03-26 11:35"?datetime("yyyy-MM-dd HH:mm"),
	"groupBuyEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialStartTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialEndTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
	"offcialAwardTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm")
	}
] />

<#assign awardList = [
	{
		"award":{
			"gameId":"f9",
			"periodNo":"20140319",
			"prizeNumber":"",
			"prizeNumberDetail":"南非,巴西,1;捷克,挪威,3;罗马尼,阿根廷,*;土耳其,瑞典,0;乌克兰,美国,0;瑞士,克罗地,0;奥地利,乌拉圭,0;比利时,科特迪,0;德国,智利,0;爱尔兰,塞尔维,0;英格兰,丹麦,0;法国,荷兰,0;葡萄牙,喀麦隆,0;西班牙,意大利,0",
			"bonusLevelDetail":"1_15_9654;2_32_286",
			"totalSales":999999
		},
		"period":{
			"gameId":"f9",
			"periodNo":"20140317",
			"startTime":"",
			"endTime":"",
			"groupBuyEndTime":""
		}
	},
	{
		"award":{
			"gameId":"f9",
			"periodNo":"20140318",
			"prizeNumber":"",
			"prizeNumberDetail":"南非,巴西,0;捷克,挪威,0;罗马尼,阿根廷,0;土耳其,瑞典,0;乌克兰,美国,0;瑞士,克罗地,0;奥地利,乌拉圭,0;比利时,科特迪,0;德国,智利,0;爱尔兰,塞尔维,0;英格兰,丹麦,0;法国,荷兰,0;葡萄牙,喀麦隆,0;西班牙,意大利,0"
		},
		"period":{
			"gameId":"f9",
			"periodNo":"20140317",
			"startTime":"",
			"endTime":"",
			"groupBuyEndTime":""
		}
	},
	{
		"award":{
			"gameId":"f9",
			"periodNo":"20140317",
			"prizeNumber":"",
			"prizeNumberDetail":"南非,巴西,0;捷克,挪威,0;罗马尼,阿根廷,0;土耳其,瑞典,0;乌克兰,美国,0;瑞士,克罗地,0;奥地利,乌拉圭,0;比利时,科特迪,0;德国,智利,0;爱尔兰,塞尔维,0;英格兰,丹麦,0;法国,荷兰,0;葡萄牙,喀麦隆,0;西班牙,意大利,0"
		},
		"period":{
			"gameId":"f9",
			"periodNo":"20140317",
			"startTime":"",
			"endTime":"",
			"groupBuyEndTime":""
		}
	},
	{
		"award":{
			"gameId":"f9",
			"periodNo":"20140316",
			"prizeNumber":"",
			"prizeNumberDetail":"南非,巴西,0;捷克,挪威,0;罗马尼,阿根廷,0;土耳其,瑞典,0;乌克兰,美国,0;瑞士,克罗地,0;奥地利,乌拉圭,0;比利时,科特迪,0;德国,智利,0;爱尔兰,塞尔维,0;英格兰,丹麦,0;法国,荷兰,0;葡萄牙,喀麦隆,0;西班牙,意大利,0"
		},
		"period":{
			"gameId":"f9",
			"periodNo":"20140317",
			"startTime":"",
			"endTime":"",
			"groupBuyEndTime":""
		}
	},
	{
		"award":{
			"gameId":"f9",
			"periodNo":"20140315",
			"prizeNumber":"",
			"prizeNumberDetail":"南非,巴西,0;捷克,挪威,0;罗马尼,阿根廷,0;土耳其,瑞典,0;乌克兰,美国,0;瑞士,克罗地,0;奥地利,乌拉圭,0;比利时,科特迪,0;德国,智利,0;爱尔兰,塞尔维,0;英格兰,丹麦,0;法国,荷兰,0;葡萄牙,喀麦隆,0;西班牙,意大利,0"
		},
		"period":{
			"gameId":"f9",
			"periodNo":"20140317",
			"startTime":"",
			"endTime":"",
			"groupBuyEndTime":""
		}
	}

] />

<#assign gameMatchList = [
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":1,
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"1,,;1.11,2.35,3.18;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"意甲",
			"labelColor":"#154CA8",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}

	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":2,
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":",,3.18;1.11,2.35,3.18;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"法甲",
			"labelColor":"#EE5307",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}

	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":3,
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"1.11,2.35,3.18;;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"西甲",
			"labelColor":"#00AF63",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}

	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":"",
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"2,1,3.18;1.11,2.35,3.18;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"德甲",
			"labelColor":"#7C087C",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}

	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":"",
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"1.11,2.35,3.18;1.11,2.35,3.18;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"英超",
			"labelColor":"#C10000",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}
	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":"",
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"1.11,2.35,3.18;1.11,2.35,3.18;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"英超",
			"labelColor":"#C10000",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}
	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":"",
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"1.11,2.35,3.18;1.11,2.35,3.18;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"英超",
			"labelColor":"#C10000",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}
	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":"",
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"1.11,2.35,3.18;1.11,2.35,3.18;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"英超",
			"labelColor":"#C10000",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}
	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":"",
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"1.11,2.35,3.18;1.11,2.35,3.18;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"英超",
			"labelColor":"#C10000",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}
	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":"",
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"1.11,2.35,3.18;1.11,2.35,3.18;1.14,2.56,3.99"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"英超",
			"labelColor":"#C10000",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}
	},
	{
		"gameMatch":{
			"gameId":"f9",
			"periodNo":"20140318",
			"gameMatchNo":"",
			"gameMatchNoShort":"",
			"matchId":"",
			"concedeScores":"",
			"sp":"",
			"status":"",
			"odds":"1.11,2.35,3.18;1.11,2.35,3.18;1,,"
		},
		"match":{
			"gameId":"f9",
			"leagueId":"11",
			"leagueName":"英超",
			"labelColor":"#C10000",
			"homeTeamId":"1",
			"awayTeamId":"2",
			"homeTeamName":"上海申花",
			"awayTeamName":"北京绿城",
			"kickOffTime":"2013-03-26 11:13"?datetime("yyyy-MM-dd HH:mm"),
			"matchStatus":1
		}
	}
] />