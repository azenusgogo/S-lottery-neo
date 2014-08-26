<#include "common.ftl" />

<#assign commonGames = [
	{
	"gameId":"ssq",
	"gameCn":"双色球",
	"desc":"2元能中1000万"
	},
	{
	"gameId":"dlt",
	"gameCn":"大乐透",
	"desc":"3元可中1600万"
	},
	{
	"gameId":"qxc",
	"gameCn":"七星彩",
	"desc":"特别容易中奖"
	},
	{
	"gameId":"qlc",
	"gameCn":"七乐彩",
	"desc":"特别容易中奖"
	}
] />

<#assign highFrequencyGames = [
	{
	"gameId":"k3js",
	"gameCn":"江苏快3",
	"desc":"2000+800万"
	},
	{
	"gameId":"k3jl",
	"gameCn":"吉林快3",
	"desc":"骰子游戏好玩易中"
	},
	{
	"gameId":"k3gx",
	"gameCn":"广西快3",
	"desc":"猜中一个号有奖"
	}
] />

<#assign traditionalSportGames = [
	{
	"gameId":"f14",
	"gameCn":"胜负彩",
	"desc":"特别容易中奖"
	},
	{
	"gameId":"f9",
	"gameCn":"任选九",
	"desc":"特别容易中奖"
	}
] />

<#assign commonPeriods = [
	{
	"gameId":"ssq",
	"gameCn":"双色球",
	"periodNo":"20140101",
	"offcialAwardTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"endTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"available":true,
	"bonusPool":"1_500_1021:5",
	"todayOpen":true
	},
	{
	"gameId":"dlt",
	"gameCn":"大乐透",
	"periodNo":"20140101",
	"offcialAwardTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"endTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"available":true,
	"bonusPool":"1_500_1021:5",
	"todayOpen":true
	},
	{
	"gameId":"qlc",
	"gameCn":"七乐彩",
	"periodNo":"20140101",
	"offcialAwardTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"endTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"available":true,
	"bonusPool":"1_500_1021:5",
	"todayOpen":true
	},
	{
	"gameId":"qxc",
	"gameCn":"七星彩",
	"periodNo":"20140101",
	"offcialAwardTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"endTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"available":true,
	"bonusPool":"1_500_1021:5",
	"todayOpen":true
	}
] />

<#assign highFrequencyPeriods = [
	{
	"gameId":"k3js",
	"gameCn":"江苏快3",
	"periodNo":"20140101",
	"offcialAwardTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"endTime":"2014-03-18 18:00:00"?datetime("yyyy-MM-dd HH:mm:ss"),
	"available":true,
	"hotPlayMethod":"三连号通选"
	}
] />

<#assign awardAnnouncements = [
	{
		"gameId":"ssq",
		"gameCn":"双色球",
		"periodNo":"2014014",
		"prizeNumber":"02 06 05 08 17 15:06",
		"openAwardDate":"2014-03-18",
		"openAwardTime":"3-13（周四） 21:03",
		"todayOpen":false
	},
	{
		"gameId":"dlt",
		"gameCn":"大乐透",
		"periodNo":"2014014",
		"prizeNumber":"02 06 05 17 15:06 12",
		"openAwardDate":"2014-03-18",
		"openAwardTime":"3-13（周四） 21:03",
		"todayOpen":true
	},
	{
		"gameId":"qxc",
		"gameCn":"七星彩",
		"periodNo":"2014014",
		"prizeNumber":"02 06 05 08 17 15 06",
		"openAwardDate":"2014-03-18",
		"openAwardTime":"3-13（周四） 21:03",
		"todayOpen":true
	},
	{
		"gameId":"qlc",
		"gameCn":"七乐彩",
		"periodNo":"2014014",
		"prizeNumber":"02 06 05 08 17 15 06",
		"openAwardDate":"2014-03-18"
	}
] />

<#assign recentAwardRecords = [
	{
		"gameCn":"双色球",
		"nickName":"张三",
		"amount":99999900
	},
	{
		"gameCn":"大乐透",
		"nickName":"张三",
		"amount":99999900
	},
	{
		"gameCn":"双色球",
		"nickName":"张三",
		"amount":99999900
	},
	{
		"gameCn":"双色球",
		"nickName":"张三",
		"amount":99999900
	},
	{
		"gameCn":"江苏快3",
		"nickName":"张三",
		"amount":99999900
	},
	{
		"gameCn":"双色球",
		"nickName":"李四",
		"amount":99999900
	},
	{
		"gameCn":"七星彩",
		"nickName":"张三",
		"amount":99999900
	}
] />

<#assign announcementMap = [
	{
		"type":"最新",
		"indexAnnouncement":[
			{
				"title":"xxxxx",
				"link":"http://www.sogou.com/"
			},
			{
				"title":"xxxxx",
				"link":"http://www.sogou.com/"
			}
		]
	},
	{
		"活动":"最新",
		"indexAnnouncement":[
			{
				"title":"xxxxx",
				"link":"http://www.sogou.com/"
			},
			{
				"title":"xxxxx",
				"link":"http://www.sogou.com/"
			}
		]
	}
]>

<#assign announcementMap = {
	"最新":[
		{
			"type":"最新",
			"title":"要通过以下的方式来得到",
			"link":"http://www.sogou.com/"
		},
		{
			"type":"最新",
			"title":"双色球中500万",
			"link":"http://www.sogou.com/"
		}
	],
	"活动":[
		{
			"type":"活动",
			"title":"双色球中500万",
			"link":"http://www.sogou.com/"
		},
		{
			"type":"活动",
			"title":"要通过以下的方式来得到",
			"link":"http://www.sogou.com/"
		}
	]
} />
<#assign adsLinks = [
	{
		"title":"切换图1",
		"link":"http://www.sogou.ciom/",
		"image":"http://pimg1.126.net/caipiao_info/images/headFigure/indexPage/2014040918TT16768322.jpg",
		"position":0
	},
	{
		"title":"切换图2",
		"link":"http://www.sogou.ciom/",
		"image":"http://pimg1.126.net/caipiao_info/images/headFigure/indexPage/2014040918TT16768322.jpg",
		"position":0
	},
	{
		"title":"切换图3",
		"link":"http://www.sogou.ciom/",
		"image":"http://pimg1.126.net/caipiao_info/images/headFigure/indexPage/2014040918TT16768322.jpg",
		"position":0
	},
	{
		"title":"切换图4",
		"link":"http://www.sogou.ciom/",
		"image":"http://pimg1.126.net/caipiao_info/images/headFigure/indexPage/2014040918TT16768322.jpg",
		"position":0
	},
	{
		"title":"banner",
		"link":"http://www.sogou.ciom/",
		"image":"http://pimg1.126.net/caipiao_info/images/headFigure/pcToutuMid/2014040915TT15476012.jpg",
		"position":1
	}
] />