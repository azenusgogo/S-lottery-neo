define([
    "update/art",
    "lib/timer",
    "lib/tools",
    "text!update/top.tpl",
    "text!update/pool.tpl",
    "text!update/award_list.tpl"
], function (art, Timer, tools, top_tpl, pool_tpl, award_tpl) {
    return function () {
        var $gameType = $("#gameType");
        var $gameId = $("#gameId");
        var $periodNo = $("#periodNo");
        var $endTime = $("#endTime");
        var $serverTime = $("#serverTime");

        var up_top_tpl = _.template(top_tpl);
        var up_pool_tpl = _.template(pool_tpl);
        var up_award_tpl = _.template(award_tpl);

        var $up_top = $("#up_top");
        var $up_pool = $("#up_pool");
        var $up_award = $("#up_award");


        var $pt_red_list, $pt_blue_list, $dt_red_list, $dt_blue_list, $sh_red_list, $sh_blue_list;


        switch ($gameId.val()) {
            case "ssq":
                $pt_red_list = $("#py_play .red-ball span");
                $pt_blue_list = $("#py_play .blue-ball span");
                $dt_red_list = $("#dt_play .red-ball span");
                $dt_blue_list = $("#dt_play .blue-ball span");
                $sh_red_list = $("#sh_play .red-ball span");
                $sh_blue_list = $("#sh_play .blue-ball span");
                break;
            case "dlt":
                $pt_red_list = $("#dlt_pt_play .red-ball span");
                $pt_blue_list = $("#dlt_pt_play .blue-ball span");
                $dt_red_list = $("#dlt_dt_play .red-ball span");
                $dt_blue_list = $("#dlt_dt_play .blue-ball span");
                $sh_red_list = $("#dlt_sh_play .red-ball span");
                $sh_blue_list = $("#dlt_sh_play .blue-ball span");
                break;
            case "qxc":
                $pt_red_list = $("#qxc_py_play .l li span");
                break;
            case "qlc":
                $pt_red_list = $("#qlc_py_play .red-ball span");
                $dt_red_list = $("#qlc_dt_play .red-ball span");
                $sh_red_list = $("#qlc_sh_play .red-ball span");
                break;
        }

        var clearNumber = function () {
            $pt_red_list && $pt_red_list.html("-").removeClass('red');
            $dt_red_list && $dt_red_list.html("-").removeClass('red');
            $sh_red_list && $sh_red_list.html("-").removeClass('red');
            $pt_blue_list && $pt_blue_list.html("-").removeClass('red');
            $dt_blue_list && $dt_blue_list.html("-").removeClass('red');
            $sh_blue_list && $sh_blue_list.html("-").removeClass('red')
        };

        var updateNumber = function (gameId, periodNo, missingType, timeout) {
            var s = function () {
                $.get("/ajax/gameMissing/get/" + gameId + "/" + periodNo + "/" + missingType + ".html", function (data) {
                    if (data.retcode == 200) {
                        clearTimeout(updateNumber.timeout);
                        if (data["object"] == "") {
                            updateNumber.timeout = setTimeout(function () {
                                s()
                            }, timeout)
                            return
                        }

                        if (gameId == "ssq" || gameId == "dlt") {
                            var arr = data["object"].split(":");
                            var redArr = arr[0].split("=")[1].split(" ");
                            var blueArr = arr[1].split(" ");
                            var red_len = redArr.length;
                            var blue_len = blueArr.length;
                            var max_red = Math.max.apply(null, redArr);
                            var max_blue = Math.max.apply(null, blueArr)
                            _.each(redArr, function (v, i) {
                                var redColorClassName = v == max_red ? "red" : "";
                                $pt_red_list && $pt_red_list.eq(i).html(v).addClass(redColorClassName);
                                $dt_red_list && $dt_red_list.eq(i).html(v).addClass(redColorClassName);
                                $dt_red_list && $dt_red_list.eq(red_len + i).html(v).addClass(redColorClassName);
                                $sh_red_list && $sh_red_list.eq(i).html(v).addClass(redColorClassName)
                            });
                            _.each(blueArr, function (v, i) {
                                var blueColorClassName = v == max_blue ? "red" : "";
                                $pt_blue_list && $pt_blue_list.eq(i).html(v).addClass(blueColorClassName);
                                $dt_blue_list && $dt_blue_list.eq(i).html(v).addClass(blueColorClassName);
                                $dt_blue_list && $dt_blue_list.eq(blue_len + i).html(v).addClass(blueColorClassName);
                                $sh_blue_list && $sh_blue_list.eq(i).html(v).addClass(blueColorClassName)
                            });
                        } else if (gameId == "qxc") {
                            var redArr = data["object"].split("=")[1].split(":");
                            _.each(redArr, function (v, i) {
                                var arr = v.split(" ");
                                var max_red = Math.max.apply(null, arr);
                                _.each(arr, function (val, index) {
                                    var redColorClassName = val == max_red ? "red" : "";
                                    var j = +index + 10 * i;
                                    $pt_red_list.eq(j).html(val).addClass(redColorClassName);
                                });
                            });
                        } else if (gameId = "qlc") {
                            var arr = data["object"].split("=")[1].split(" ");
                            var red_len = arr.length;
                            var max_red = Math.max.apply(null, arr);
                            _.each(arr, function (v, i) {
                                var redColorClassName = v == max_red ? "red" : "";
                                $pt_red_list.eq(i).html(v).addClass(redColorClassName);
                                $dt_red_list.eq(i).html(v).addClass(redColorClassName);
                                $dt_red_list.eq(red_len + i).html(v).addClass(redColorClassName);
                                $sh_red_list.eq(i).html(v).addClass(redColorClassName);
                            })
                        }

                    }
                }).error(function () {
//                var data = {
//                    "object": "",
//                    //1=1 20 7 7 0 10 3 1 0 2 1 6 0 2 0 5 0 6 1 3 0 9 8 1 4 17 7 6 5 5 3 2 3:12 2 29 8 13 31 43 1 16 14 3 5 0 6 9 4
//                    "retdesc": "操作成功",
//                    "retcode": 200
//                };
//                if (gameId == "ssq"){
//                    data["object"] = "1=1 20 7 7 0 10 3 1 0 2 1 6 0 2 0 5 0 6 1 3 0 9 8 1 4 17 7 6 5 5 3 2 3:12 2 29 8 13 31 43 1 16 14 3 5 0 6 9 4"
//                }else if(gameId == "dlt"){
//                    data["object"] = "1=1 20 7 7 0 10 3 1 0 2 1 6 0 2 0 5 0 6 1 3 0 9 8 1 4 17 7 6 5 5 3 2 3 10 10:12 2 29 8 13 31 43 1 16 14 3 5"
//                }else if(gameId == "qxc"){
//                    data["object"] = "1=40 6 1 20 9 4 3 2 13 0:3 22 9 1 4 0 19 2 16 5:0 5 4 21 7 2 3 34 1 44:3 4 5 1 13 7 0 6 12 9:5 10 3 14 6 1 18 0 12 22:25 2 6 13 24 8 1 0 14 5:2 13 4 1 9 3 0 21 16 24"
//                }else if(gameId == "qlc"){
//                    data["object"] = "1=4 0 4 8 0 2 5 1 2 0 8 10 6 7 0 2 4 2 0 0 1 4 0 3 4 0 1 1 3 1"
//                }
//
//                if (data.retcode == 200){
//                    clearTimeout(updateNumber.timeout);
//                    if (data["object"] == ""){
//                        updateNumber.timeout =setTimeout(function(){
//                            s()
//                        },timeout)
//                        return
//                    }
//
//                    if (gameId == "ssq" || gameId == "dlt"){
//                        var arr = data["object"].split(":");
//                        var redArr = arr[0].split("=")[1].split(" ");
//                        var blueArr = arr[1].split(" ");
//                        var red_len = redArr.length;
//                        var blue_len = blueArr.length;
//                        var max_red = Math.max.apply(null, redArr);
//                        var max_blue = Math.max.apply(null, blueArr)
//                        _.each(redArr,function(v,i){
//                            var redColorClassName = v == max_red ? "red" : "";
//                            $pt_red_list && $pt_red_list.eq(i).html(v).addClass(redColorClassName);
//                            $dt_red_list && $dt_red_list.eq(i).html(v).addClass(redColorClassName);
//                            $dt_red_list && $dt_red_list.eq(red_len+i).html(v).addClass(redColorClassName);
//                            $sh_red_list && $sh_red_list.eq(i).html(v).addClass(redColorClassName)
//                        });
//                        _.each(blueArr,function(v,i){
//                            var blueColorClassName = v == max_blue ? "red" : "";
//                            $pt_blue_list && $pt_blue_list.eq(i).html(v).addClass(blueColorClassName);
//                            $dt_blue_list && $dt_blue_list.eq(i).html(v).addClass(blueColorClassName);
//                            $dt_blue_list && $dt_blue_list.eq(blue_len+i).html(v).addClass(blueColorClassName);
//                            $sh_blue_list && $sh_blue_list.eq(i).html(v).addClass(blueColorClassName)
//                        });
//                    }else if(gameId == "qxc"){
//                        var redArr = data["object"].split("=")[1].split(":");
//                        _.each(redArr,function(v,i) {
//                            var arr = v.split(" ");
//                            var max_red = Math.max.apply(null, arr);
//                            _.each(arr,function(val, index) {
//                                var redColorClassName = val == max_red ? "red" : "";
//                                var j = +index + 10 * i;
//                                $pt_red_list.eq(j).html(val).addClass(redColorClassName);
//                            });
//                        });
//                    }else if (gameId = "qlc"){
//                        var arr = data["object"].split("=")[1].split(" ");
//                        var red_len = arr.length;
//                        var max_red = Math.max.apply(null, arr);
//                        _.each(arr,function(v,i){
//                            var redColorClassName = v == max_red ? "red" : "";
//                            $pt_red_list.eq(i).html(v).addClass(redColorClassName);
//                            $dt_red_list.eq(i).html(v).addClass(redColorClassName);
//                            $dt_red_list.eq(red_len+i).html(v).addClass(redColorClassName);
//                            $sh_red_list.eq(i).html(v).addClass(redColorClassName);
//                        })
//                    }
//
//                }

                });
            };
            setTimeout(function () {
                s()
            }, 5523)
        };

        var update = function (timer) {
            var gameTypeArr = ["", "common", "highfrequency", "tradfootball", "competition", "singlerace"];
            var gameType = gameTypeArr[+$gameType.val()];
            var gameId = $gameId.val();
            var periodNo = $periodNo.val();
            $.get("/ajax/game/" + gameType + "/" + gameId + "/getAll.html?periodNo=" + periodNo, function (data) {
                var endTime = data["object"]["availablePeriod"]["endTime"];
                var serverTime = data["object"]["systemTime"];
                var periodNo = data["object"]["availablePeriod"]["periodNo"];
                if (data.retcode == 200) {
                    data.formatDate = tools.formatDate;
                    data.formatCurrency = tools.formatCurrency;
                    // 更新顶部
                    $up_top.html(up_top_tpl(data));
                    // 更新奖池
                    $up_pool.html(up_pool_tpl(data));
                    // 更新开奖公告
                    $up_award.html(up_award_tpl(data));

                    // 更新遗漏
                    clearNumber();
                    updateNumber(gameId, periodNo, 1, 5523);

                    // 重新倒计时
                    timer.$el = $("#ball_timer");
                    timer.endTime = endTime;
                    timer.serverTime = serverTime;
                    timer.sync(function () {
                        timer.start();
                    });
                    art.show(function () {
                        this.$prev_period.html($periodNo.val());
                        this.$period.html(periodNo);
                    });

                    $periodNo.val(periodNo);
                }

            }).error(function () {
//            var gamed = function(){
//                var str = "";
//                var prizeNumber = "";
//                switch (gameId){
//                    case "ssq":
//                        str = "双色球";
//                        prizeNumber = "06 14 19 25 28 01:01"
//                        break;
//                    case "dlt":
//                        str = "大乐透";
//                        prizeNumber = "06 14 19 25 28:01 02"
//                        break;
//                    case "qxc":
//                        str = "七星彩";
//                        prizeNumber = "06 14 19 25 28 01 02"
//                        break;
//                    case "qlc":
//                        str = "七乐彩";
//                        prizeNumber = "06 14 19 25 28 01 09"
//                        break;
//                }
//                return {
//                    gameCn:str,
//                    prizeNumber:prizeNumber
//                }
//            }();
//            var data = {
//                "object": {
//                    "systemTime": +new Date, // 服务器时间
//                    "awardList": [
//                        {
//                            "totalSales": 0,
//                            "periodNo": "2014027",
//                            "bonusPool": 0,
//                            "gameId": "ssq",
//                            "rehearsalNumber": "0",
//                            "bonusLevelDetail": "1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
//                            "prizeNumberDetail": null,
//                            "offcialEndTime": 1394967600000,
//                            "offcialStartTime": 1394798400000,
//                            "prizeNumber": gamed.prizeNumber,
//                            "offcialAwardTime": +new Date + parseInt(Math.random() * 1000)
//                        },
//                        {
//                            "totalSales": 0,
//                            "periodNo": "2014025",
//                            "bonusPool": 0,
//                            "gameId": "ssq",
//                            "rehearsalNumber": "0",
//                            "bonusLevelDetail": null,
//                            "prizeNumberDetail": null,
//                            "offcialEndTime": 1394794800000,
//                            "offcialStartTime": 1394625600000,
//                            "prizeNumber": gamed.prizeNumber,
//                            "offcialAwardTime": 1394795100000
//                        },
//                        {
//                            "totalSales": 0,
//                            "periodNo": "2014023",
//                            "bonusPool": 0,
//                            "gameId": "ssq",
//                            "rehearsalNumber": "0",
//                            // 1等奖 10注 500万
//                            "bonusLevelDetail": "1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
//                            "prizeNumberDetail": null,
//                            "offcialEndTime": 1394622000000,
//                            "offcialStartTime": 1394539200000,
//                            "prizeNumber": gamed.prizeNumber,
//                            "offcialAwardTime": 1394622300000
//                        }
//                    ],
//                    "todayOpenAward": 1, //  1 今日开奖 0 正在销售
//                    "game": {
//                        "gameType": gameType, // 1普通，2高频，3传统足彩，4竞技彩，5单场
//                        "gameId": gameId,
//                        "gameCn": gamed.gameCn,
//                        "gameStatus": 1 // 1在售，0停售
//                    },
//                    "awardPool": "3_234_3173:"+parseInt(Math.random()*10), //  3_234_3173:6   3亿234万3173元，至少可开出6注500万元 | 等待公布
//                    "availablePeriod": {
//                        "startTime": 1395921600000, // 开奖时间
//                        "endTime": +new Date + 12000, // 结束时间
//                        "periodNo": "20140"+(parseInt(Math.random()*(99-10))+10), //期次号
//                        "gameId": "ssq", // 彩种id
//                        "groupBuyEndTime": 1396180800000,
//                        "offcialEndTime": 1396180800000,
//                        "offcialStartTime": 1395921600000,
//                        "awardStatus": 0,
//                        "periodStatus": 0,
//                        "offcialAwardTime": 1396177500000
//                    }
//                },
//                "retcode": 200,
//                "retdesc": "操作成功"
//            };
//            var endTime = data["object"]["availablePeriod"]["endTime"];
//            var serverTime = data["object"]["systemTime"];
//            var periodNo = data["object"]["availablePeriod"]["periodNo"];
//            if (data.retcode == 200){
//                data.formatDate = tools.formatDate;
//                data.formatCurrency = tools.formatCurrency;
//                // 更新顶部
//                $up_top.html(up_top_tpl(data));
//                // 更新奖池
//                $up_pool.html(up_pool_tpl(data));
//                // 更新开奖公告
//                $up_award.html(up_award_tpl(data));
//
//                // 更新遗漏
//                clearNumber();
//                updateNumber(gameId, periodNo, 1, 5523);
//
//                // 重新倒计时
//                timer.$el = $("#ball_timer");
//                timer.endTime = endTime;
//                timer.serverTime = serverTime;
//                timer.sync(function(){
//                    timer.start();
//                });
//                art.show(function(){
//                    this.$prev_period.html($periodNo.val());
//                    this.$period.html(periodNo);
//                });
//
//                $periodNo.val(periodNo);
//            }

            })
        };

        var time_tpl = _.template('<%var d=time[0]%><%var h=time[1]%><%var m=time[2]%><%var ss=time[3]%><%if(time[0]>0){%><b><%=d%></b>天<%}%><%if(time[0]>0||time[1]>0){%><b><%=+h<10?"0"+h:h%></b>小时<%}%><%if(time[0]>0||time[1]>0||time[2]>0){%><b><%=+m<10?"0"+m:m%></b>分<%}%><b><%=+ss<10?"0"+ss:ss%></b>秒')
        var timer = new Timer({
            range: [86400000, 3600000, 60000, 1000],
            endTime: $endTime.val(), //  +new Date + 5000
            el: "#ball_timer",
            serverTime: $serverTime.val(), //  +new Date
            render: function (time) {
                this.$el.html(time_tpl({time: time}))
            },
            onTimerEnd: function () {
                update(this)
            }
        });

        timer.sync(function () {
            timer.start();
        })
    }
})