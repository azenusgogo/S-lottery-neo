define([
    "update/art",
    "lib/timer",
    "lib/tools",
    "lib/events",
    "text!update/top.tpl",
    "text!update/pool.tpl",
    "text!update/k3_award.tpl",
    "text!update/stat.tpl",
    "text!update/k3_award_list.tpl"
], function (art, Timer, tools, events, top_tpl, pool_tpl, award_tpl, stat_tpl, award_list_tpl) {

    var $gameType = $("#gameType");
    var $gameId = $("#gameId");
    var $periodNo = $("#periodNo");
    var $endTime = $("#endTime");
    var $serverTime = $("#serverTime");
    var $openingAwardPeriodNo = $("#openingAwardPeriodNo");
    var openingAwardPeriodNo_v = $.trim($openingAwardPeriodNo.val());

    // 模版
    var up_top_tpl = _.template(top_tpl);
    var up_pool_tpl = _.template(pool_tpl);
    var up_award_tpl = _.template(award_tpl);
    var up_stat_tpl = _.template(stat_tpl);
    var up_award_list_tpl = _.template(award_list_tpl);

    // 更新数据
    var $up_top = $("#up_top");
    var $up_pool = $("#up_pool");
    var $up_award = $("#up_award");
    var $award_list = $("#award_list"); // 今日开奖
    var $k3_side_tab_c1 = $("#k3_side_tab_c1 ul") // 形态统计
    var $k3_side_tab_c2 = $("#k3_side_tab_c2 ul") // 和值统计

    // 今日开奖列表
    var award_list_hash = {};
    $("#award_list td").each(function () {
        var $this = $(this);
        award_list_hash[+$(this).find("span").html()] = $this;
    });


    //  更新和值
    var $hz_list = $("#k3_hz_play .red-ball span") // 和值 2 

    var $3thtx_list = $("#k3_3thtx_play .red-ball span") // 三同号通选 2

    var $3thdx_list = $("#k3_3thdx_play .red-ball span") // 三同号单选 2

    var $3bt_rx_list = $("#k3_3bt_rx .l li span") // 三不同号 任选 1
    var $3bt_dt_list = $("#k3_3bt_dt .l li span") // 三不同号 胆拖 1

    var $3lhtx_list = $("#k3_3lhtx_play .red-ball span") // 三连号通选 2

    var $2thfx_list = $("#k3_2thfx_play .red-ball span") // 二同号复选 2

    var $2thdx_list = $("#k3_2thdx_play .red-ball span") // 二同号单选 1


    var $2bt_rx_list = $("#k3_2bt_rx .l li span") // 二不同号 任选 1
    var $2bt_dt_list = $("#k3_2bt_dt .l li span") // 二不同号 胆拖 1


    // 倒计时
    events.on("timer", function (e) {
        var timer = e.timer;
        timer.$el = e.$el;
        timer.endTime = e.endTime;
        timer.serverTime = e.serverTime;
        timer.sync(function () {
            timer.start();
        });
    })
    // 更新顶部
    events.on("updateTop", function (e) {
        e.$up_top.html(e.up_top_tpl(e.data));
    });

    // 更新奖池
    events.on("updatePool", function (e) {
        e.$up_pool.html(e.up_pool_tpl(e.data));
    });

    // 更新开奖公告
    events.on("updateAward", function (e) {
        e.$up_award.html(e.up_award_tpl(e.data));
    });

    // 清除 当前遗漏和最大遗漏
    events.on("clearMissing", function (e) {
        e.$hz_list.html("-").removeClass('red');
        e.$3thtx_list.html("-").removeClass('red');
        e.$3thdx_list.html("-").removeClass('red');
        e.$3bt_rx_list.html("-").removeClass('red');
        e.$3bt_dt_list.html("-").removeClass('red');
        e.$3lhtx_list.html("-").removeClass('red');
        e.$2thfx_list.html("-").removeClass('red');
        e.$2thdx_list.html("-").removeClass('red');
        e.$2bt_rx_list.html("-").removeClass('red');
        e.$2bt_dt_list.html("-").removeClass('red')
    });

    // 更新 当前遗漏和最大遗漏
    events.on("updateMissing", function (e) {
        // 高频彩
        $.get("/ajax/gameMissing/get/" + e.gameId + "/" + e.periodNo + "/0.html", function (data) {
            if (data.retcode == 200) {
                var st = 1;
                var arr = data["object"].split(";");
                var arr1, arr2;
                if (arr.length < 2) {
                    st = 0
                } else {
                    arr1 = arr[0].split("1=");
                    arr2 = arr[1].split("2=");
                    if (arr1.length < 2 || arr2.length < 2) {
                        st = 0
                    } else {
                        arr1 = arr1[1].split(":");
                        arr2 = arr2[1].split(":");
                    }
                }
                if (st) {
                    var $list = [
                        [e.$el.$hz_list], // 和值
                        [e.$el.$3thtx_list], // 三同号通选
                        [e.$el.$3thdx_list], // 三同号单选
                        [e.$el.$3bt_rx_list, e.$el.$3bt_dt_list], // 三不同号
                        [e.$el.$3lhtx_list], // 三连号通选
                        [e.$el.$2thfx_list], // 二同号复选
                        [e.$el.$2thdx_list], // 二同号单选
                        [e.$el.$2bt_rx_list, e.$el.$2bt_dt_list] // 二不同号
                    ]

                    //和值:三同号通选:三同号单选:三不同号:三连号:二同号复选:二同号单选同号 二同号单选不同号: 二不同号
                    // 0 58 30 35 8 9 21 1 28 11 3 17 2 24 14 13:0:0 137 191 39 5 13:0 4 1 2 2 1:2:0 26 10 3 5 13:9 26 10 3 7 14 23 10 7 25 3 9:0 4 1 2 2 1


                    _.each(arr1, function (v, i) {
                        var arr = v.split(" ");
                        var maxn = Math.max.apply(null, arr);
                        if (i == 0 || i == 1 || i == 2 || i == 4 || i == 5) {
                            _.each(arr, function (val, index) {
                                var className = (maxn == val && i != 4 && i != 1) ? "red" : "";
                                $list[i][0].eq(index * 2).html(val).addClass(className);
                            })
                        } else if (i == 3 || i == 7) {
                            _.each(arr, function (val, index) {
                                var className = maxn == val ? "red" : "";
                                $list[i][0].eq(index).html(val).addClass(className);
                                $list[i][1].eq(index).html(val).addClass(className);
                                $list[i][1].eq(index + 6).html(val).addClass(className);
                            })
                        } else if (i == 6) {
                            var maxn1 = Math.max.apply(null, arr.slice(0, 6));
                            var maxn2 = Math.max.apply(null, arr.slice(6, 12));
                            _.each(arr, function (val, index) {
                                var className = (maxn1 == val || maxn2 == val ) ? "red" : "";
                                $list[i][0].eq(index).html(val).addClass(className);
                            })
                        }

                    })
                    _.each(arr2, function (v, i) {
                        var arr = v.split(" "), maxn;
                        var maxn = Math.max.apply(null, arr);
                        if (i == 0 || i == 1 || i == 2 || i == 4 || i == 5) {
                            _.each(arr, function (val, index) {
                                var className = (maxn == val && i != 4 && i != 1) ? "red" : "";
                                $list[i][0].eq(index * 2 + 1).html(val).addClass(className);
                            })
                        }
                    })
                    setTimeout(function () {
                        events.trigger({
                            type: "updateStat",
                            gameId: e.gameId,
                            $k3_side_tab_c1: $k3_side_tab_c1,
                            $k3_side_tab_c2: $k3_side_tab_c2,
                            up_stat_tpl: up_stat_tpl
                        })
                    }, 2000)

                } else {
                    // 更新开奖公告列表
                    setTimeout(function () {
                        events.trigger({
                            type: "updateMissing",
                            gameId: e.gameId,
                            periodNo: e.periodNo,
                            $el: e.$el
                        })
                    }, 5000)
                }
            }
        }).error(function () {
           // var data = {
           //     "retcode": 200,
           //     "retdesc": "操作成功",
           //     "object": "1=0 58 30 35 8 9 21 1 28 11 3 17 2 24 14 18:10:0 137 191 39 5 13:0 4 1 2 2 1:2:0 26 10 3 5 13:9 26 10 3 7 14 23 10 7 25 3 9:0 4 1 2 2 1;2=0 58 30 35 8 9 21 1 28 11 3 17 2 24 14 13:0:0 137 191 39 5 13:0 4 1 2 2 1:2:0 26 10 3 5 13:9 26 10 3 7 14 23 10 7 25 3 9:0 4 1 2 2 1"
           // }
           // if (data.retcode == 200){
           //     var st = 1;
           //     var arr = data["object"].split(";");
           //     var arr1,arr2;
           //     if (arr.length < 2) {
           //         st = 0
           //     } else {
           //         arr1 = arr[0].split("1=");
           //         arr2 = arr[1].split("2=");
           //         if (arr1.length < 2 || arr2.length < 2){
           //             st = 0
           //         } else {
           //             arr1 = arr1[1].split(":");
           //             arr2 = arr2[1].split(":");
           //         }
           //     }
           //     if (st) {
           //         var $list = [
           //             [e.$el.$hz_list], // 和值
           //             [e.$el.$3thtx_list], // 三同号通选
           //             [e.$el.$3thdx_list], // 三同号单选
           //             [e.$el.$3bt_rx_list,e.$el.$3bt_dt_list], // 三不同号
           //             [e.$el.$3lhtx_list], // 三连号通选
           //             [e.$el.$2thfx_list], // 二同号复选
           //             [e.$el.$2thdx_list], // 二同号单选
           //             [e.$el.$2bt_rx_list,e.$el.$2bt_dt_list] // 二不同号
           //         ]

           //         //和值:三同号通选:三同号单选:三不同号:三连号:二同号复选:二同号单选同号 二同号单选不同号: 二不同号
           //         // 0 58 30 35 8 9 21 1 28 11 3 17 2 24 14 13:0:0 137 191 39 5 13:0 4 1 2 2 1:2:0 26 10 3 5 13:9 26 10 3 7 14 23 10 7 25 3 9:0 4 1 2 2 1


           //         _.each(arr1,function(v, i){
           //             var arr = v.split(" ");
           //             var maxn = Math.max.apply(null,arr);
           //             if (i == 0 || i == 1 || i == 2 || i == 4 || i == 5) {
           //                 _.each(arr,function(val,index){
           //                     var className = (maxn == val && i != 4 && i != 1) ? "red" : "";
           //                     $list[i][0].eq(index * 2).html(val).addClass(className);
           //                 })
           //             } else if (i == 3 || i == 7) {
           //                 _.each(arr,function(val,index){
           //                     var className = maxn == val ? "red" : "";
           //                     $list[i][0].eq(index).html(val).addClass(className);
           //                     $list[i][1].eq(index).html(val).addClass(className);
           //                     $list[i][1].eq(index+6).html(val).addClass(className);
           //                 })
           //             } else if (i == 6) {
           //                 var maxn1 =  Math.max.apply(null,arr.slice(0,6));
           //                 var maxn2 =  Math.max.apply(null,arr.slice(6,12));
           //                 _.each(arr,function(val,index){
           //                     var className = (maxn1 == val || maxn2 == val ) ? "red" : "";
           //                     $list[i][0].eq(index).html(val).addClass(className);
           //                 })
           //             }

           //         })
           //         _.each(arr2,function(v, i){
           //             var arr = v.split(" "),maxn;
           //             var maxn = Math.max.apply(null,arr);
           //             if (i == 0 || i == 1 || i == 2 || i == 4 || i == 5) {
           //                 _.each(arr,function(val,index){
           //                     var className = (maxn == val && i != 4 && i != 1) ? "red" : "";
           //                     $list[i][0].eq(index * 2 + 1).html(val).addClass(className);
           //                 })
           //             }
           //         })
           //         setTimeout(function(){
           //             events.trigger({
           //                 type:"updateStat",
           //                 gameId:e.gameId,
           //                 $k3_side_tab_c1:$k3_side_tab_c1,
           //                 $k3_side_tab_c2:$k3_side_tab_c2,
           //                 up_stat_tpl:up_stat_tpl
           //             })
           //         },2000)

           //     } else {
           //         // 更新开奖公告列表
           //         setTimeout(function(){
           //             events.trigger({
           //                 type:"updateMissing",
           //                 gameId : e.gameId,
           //                 periodNo : e.periodNo,
           //                 $el : e.$el
           //             })
           //         },5000)
           //     }
           // }
        });
    });

    // 更新 奖池 开间列表 最新数据
    events.on("updateAwardPool", function (e) {
        if (!e) return;
        var data = e.data;
        if (data) {
            var endTime = data["object"]["availablePeriod"]["endTime"];
            var serverTime = data["object"]["systemTime"];
            var periodNo = data["object"]["availablePeriod"]["periodNo"];
            var openingAwardPeriodNo = data["object"]["openingAwardPeriodNo"];
            // 更新奖池
            events.trigger({
                type: "updatePool",
                $up_pool: e.$up_pool,
                up_pool_tpl: e.up_pool_tpl,
                data: data
            });

            events.trigger({
                type: "updateAward",
                $up_award: e.$up_award,
                up_award_tpl: e.up_award_tpl,
                data: data
            });

            // 更新开奖数据
            if (openingAwardPeriodNo) {
                e.type = "getAward";
                e.data = null;
                setTimeout(function () {
                    events.trigger(e)
                }, 10000)
            } else {
                events.trigger({
                    type: "updateAwardList",
                    award_list_hash: award_list_hash,
                    up_award_list_tpl: up_award_list_tpl,
                    data: data
                });
                // 更新当前遗漏 最大遗漏
                setTimeout(function () {
                    events.trigger({
                        type: "updateMissing",
                        gameId: e.gameId,
                        periodNo: e.periodNo,
                        $el: {
                            $hz_list: $hz_list,
                            $3thtx_list: $3thtx_list,
                            $3thdx_list: $3thdx_list,
                            $3bt_rx_list: $3bt_rx_list,
                            $3bt_dt_list: $3bt_dt_list,
                            $3lhtx_list: $3lhtx_list,
                            $2thfx_list: $2thfx_list,
                            $2thdx_list: $2thdx_list,
                            $2bt_rx_list: $2bt_rx_list,
                            $2bt_dt_list: $2bt_dt_list
                        }
                    })
                }, 2000);
            }
        }
    })

    // 获取开奖最新数据
    events.on("getAward", function (e) {
        // var options = {
        //     type:"updateAwardPool",
        //     gameType : e.gameType,
        //     gameId : e.gameId,
        //     periodNo : e.periodNo,
        //     $up_pool : e.$up_pool,
        //     $up_award : e.$up_award,
        //     up_pool_tpl : e.up_pool_tpl,
        //     up_award_tpl : e.up_award_tpl
        // }
        var sendi = e.sendi || 0;
        e.type = "updateAwardPool";
        if (e.data) {
            events.trigger(e);
        } else {
            if (sendi > 30) return; // 5分钟   10000 * 30 / (1000*60)
            e.sendi = sendi + 1;
            $.get("/ajax/game/" + e.gameType + "/" + e.gameId + "/getAll.html?periodNo=" + e.periodNo, function (data) {
                e.data = data;
                events.trigger(e);
            }).error(function () {
               // var data = {
               //     "object": {
               //         "openingAwardPeriodNo":"",
               //         "systemTime": +new Date, // 服务器时间
               //         "awardList": [
               //             {
               //                 "totalSales": 0,
               //                 "periodNo": "201401",
               //                 "bonusPool": 0,
               //                 "gameId": "ssq",
               //                 "rehearsalNumber": "0",
               //                 "bonusLevelDetail": "1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
               //                 "prizeNumberDetail": null,
               //                 "offcialEndTime": 1394967600000,
               //                 "offcialStartTime": 1394798400000,
               //                 "prizeNumber": "2 3 5,10,0,0,1,0,0,0,1",
               //                 "offcialAwardTime": +new Date + parseInt(Math.random() * 1000)
               //             },
               //             {
               //                 "totalSales": 0,
               //                 "periodNo": "2014025",
               //                 "bonusPool": 0,
               //                 "gameId": "ssq",
               //                 "rehearsalNumber": "0",
               //                 "bonusLevelDetail": null,
               //                 "prizeNumberDetail": null,
               //                 "offcialEndTime": 1394794800000,
               //                 "offcialStartTime": 1394625600000,
               //                 "prizeNumber": "2 3 5,10,0,0,1,0,0,0,1",
               //                 "offcialAwardTime": 1394795100000
               //             },
               //             {
               //                 "totalSales": 0,
               //                 "periodNo": "2014023",
               //                 "bonusPool": 0,
               //                 "gameId": "ssq",
               //                 "rehearsalNumber": "0",
               //                 // 1等奖 10注 500万
               //                 "bonusLevelDetail": "1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
               //                 "prizeNumberDetail": null,
               //                 "offcialEndTime": 1394622000000,
               //                 "offcialStartTime": 1394539200000,
               //                 "prizeNumber": "2 3 5,10,0,0,1,0,0,0,1",
               //                 "offcialAwardTime": 1394622300000
               //             }
               //         ],
               //         "todayOpenAward": 1, //  1 今日开奖 0 正在销售
               //         "game": {
               //             "gameType": 2, // 1普通，2高频，3传统足彩，4竞技彩，5单场
               //             "gameId": "k3js",
               //             "gameCn": "江苏快3",
               //             "gameStatus": 1 // 1在售，0停售
               //         },
               //         "awardPool": "3_234_3173:"+parseInt(Math.random()*10), //  3_234_3173:6   3亿234万3173元，至少可开出6注500万元 | 等待公布
               //         "availablePeriod": {
               //             "startTime": 1395921600000, // 开奖时间
               //             "endTime": +new Date + 1200000, // 结束时间
               //             "periodNo": "201401",
               //             "gameId": "ssq", // 彩种id
               //             "groupBuyEndTime": 1396180800000,
               //             "offcialEndTime": 1396180800000,
               //             "offcialStartTime": 1395921600000,
               //             "awardStatus": 0,
               //             "periodStatus": 0,
               //             "offcialAwardTime": 1396177500000
               //         }
               //     },
               //     "retcode": 200,
               //     "retdesc": "操作成功"
               // };
               // e.data = data;
               // events.trigger(e);
            });
        }
    });

    // 更新统计
    events.on("updateStat", function (e) {
        $.get("/ajax/stat/get/" + e.gameId + "/formSumStat.html", function (data) {
            if (data.retcode == 200) {
                _.each(data["object"][0], function (v, i) {
                    var maxn = 0;
                    v = v ? v.split(",") : v;
                    if (v) {
                        for (var j = v.length; j--;) {
                            var n = +v[j].split(":")[1];
                            n > maxn && (maxn = n)
                        }
                    }
                    e.$k3_side_tab_c1.eq(i).html(e.up_stat_tpl({
                        list: v,
                        maxn: maxn,
                        type: 0,
                        index: i
                    }))
                });

                _.each(data["object"][1], function (v, i) {
                    var maxn = 0;
                    v = v ? v.split(",") : v;
                    if (v) {
                        for (var j = v.length; j--;) {
                            var n = +v[j].split(":")[1];
                            n > maxn && (maxn = n)
                        }
                    }
                    e.$k3_side_tab_c2.eq(i).html(e.up_stat_tpl({
                        list: v,
                        maxn: maxn,
                        type: 1,
                        index: i
                    }))
                });
            }
        }).error(function () {
           // var data = {
           //     "retcode": 200,
           //     "retdesc": "操作成功",
           //     "object": [
           //         [
           //             "三同号:1,三不同号:2,三连号:3,二同号:4,二不同号:5",
           //             null,
           //             null
           //         ],
           //         [
           //             "6:5,10:1,3:0,4:0,5:0,7:0,8:0,9:0,11:0,12:0,13:0,14:0,15:0,17:0,16:0,18:0",
           //             null,
           //             null
           //         ]
           //     ]
           // };

           // if (data.retcode == 200){
           //     _.each(data["object"][0],function(v,i){
           //         var maxn = 0;
           //         v = v ? v.split(",") : v;
           //         if (v) {
           //             for (var j = v.length; j--;){
           //                 var n = v[j].split(":")[1];
           //                 n > maxn && (maxn = n)
           //             }
           //         }
           //         e.$k3_side_tab_c1.eq(i).html(e.up_stat_tpl({
           //             list:v,
           //             maxn:maxn,
           //             type:0
           //         }))
           //     });

           //     _.each(data["object"][1],function(v,i){
           //         var maxn = 0;
           //         v = v ? v.split(",") : v;
           //         if (v) {
           //             for (var j = v.length; j--;){
           //                 var n = v[j].split(":")[1];
           //                 n > maxn && (maxn = n)
           //             }
           //         }
           //         e.$k3_side_tab_c2.eq(i).html(e.up_stat_tpl({
           //             list:v,
           //             maxn:maxn,
           //             type:1
           //         }))
           //     });
           // }

        });
    });

    // 更新期次
    // var _periodNo = "2014001"
    events.on("update", function (e) {
       // _periodNo = (+_periodNo + 1).toString();
        $.get("/ajax/game/" + e.gameType + "/" + e.gameId + "/getAll.html?periodNo=" + e.periodNo, function (data) {
            var endTime = data["object"]["availablePeriod"]["endTime"];
            var serverTime = data["object"]["systemTime"];
            var periodNo = data["object"]["availablePeriod"]["periodNo"];
            var openingAwardPeriodNo = data["object"]["openingAwardPeriodNo"];
            if (data.retcode == 200) {
                // 更新顶部
                events.trigger({
                    type: "updateTop",
                    $up_top: e.$up_top,
                    up_top_tpl: e.up_top_tpl,
                    data: data
                });

                // 顶部重新倒计时
                events.trigger({
                    type: "timer",
                    timer: e.timer,
                    endTime: endTime,
                    serverTime: serverTime,
                    $el: $("#ball_timer")
                });

                // 更新期次
                e.$periodNo.val(periodNo);

                if (openingAwardPeriodNo_v) {
                    $openingAwardPeriodNo.val("");
                } else {
                    // 期次结束提示
                    art.show(function () {
                        this.$prev_period.html(e.periodNo);
                        this.$period.html(periodNo);
                    });

                }

                // 更新奖池 + 更新开奖公告
                events.trigger({
                    type: "getAward",
                    gameType: e.gameType,
                    gameId: e.gameId,
                    periodNo: e.periodNo,
                    $up_pool: e.$up_pool,
                    $up_award: e.$up_award,
                    up_pool_tpl: e.up_pool_tpl,
                    up_award_tpl: e.up_award_tpl,
                    data: data // 不用请求接口立刻更新
                });
            }
        }).error(function () {
           // var data = {
           //     "object": {
           //         "openingAwardPeriodNo":"",
           //         "systemTime": +new Date, // 服务器时间
           //         "awardList": [
           //             {
           //                 "totalSales": 0,
           //                 "periodNo": _periodNo,
           //                 "bonusPool": 0,
           //                 "gameId": "ssq",
           //                 "rehearsalNumber": "0",
           //                 "bonusLevelDetail": "1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
           //                 "prizeNumberDetail": null,
           //                 "offcialEndTime": 1394967600000,
           //                 "offcialStartTime": 1394798400000,
           //                 "prizeNumber": "2 3 5,10,0,0,1,0,0,0,1",
           //                 "offcialAwardTime": +new Date + parseInt(Math.random() * 1000)
           //             },
           //             {
           //                 "totalSales": 0,
           //                 "periodNo": "2014025",
           //                 "bonusPool": 0,
           //                 "gameId": "ssq",
           //                 "rehearsalNumber": "0",
           //                 "bonusLevelDetail": null,
           //                 "prizeNumberDetail": null,
           //                 "offcialEndTime": 1394794800000,
           //                 "offcialStartTime": 1394625600000,
           //                 "prizeNumber": "2 3 5,10,0,0,1,0,0,0,1",
           //                 "offcialAwardTime": 1394795100000
           //             },
           //             {
           //                 "totalSales": 0,
           //                 "periodNo": "2014023",
           //                 "bonusPool": 0,
           //                 "gameId": "ssq",
           //                 "rehearsalNumber": "0",
           //                 // 1等奖 10注 500万
           //                 "bonusLevelDetail": "1_10_500000000;2_20_30000000;3_123_300000;4_456_20000;5_1000_1000;6_5000_500",
           //                 "prizeNumberDetail": null,
           //                 "offcialEndTime": 1394622000000,
           //                 "offcialStartTime": 1394539200000,
           //                 "prizeNumber": "2 3 5,10,0,0,1,0,0,0,1",
           //                 "offcialAwardTime": 1394622300000
           //             }
           //         ],
           //         "todayOpenAward": 1, //  1 今日开奖 0 正在销售
           //         "game": {
           //             "gameType": 2, // 1普通，2高频，3传统足彩，4竞技彩，5单场
           //             "gameId": "k3js",
           //             "gameCn": "江苏快3",
           //             "gameStatus": 1 // 1在售，0停售
           //         },
           //         "awardPool": "3_234_3173:"+parseInt(Math.random()*10), //  3_234_3173:6   3亿234万3173元，至少可开出6注500万元 | 等待公布
           //         "availablePeriod": {
           //             "startTime": 1395921600000, // 开奖时间
           //             "endTime": +new Date + 50000, // 结束时间
           //             "periodNo": _periodNo,
           //             "gameId": "ssq", // 彩种id
           //             "groupBuyEndTime": 1396180800000,
           //             "offcialEndTime": 1396180800000,
           //             "offcialStartTime": 1395921600000,
           //             "awardStatus": 0,
           //             "periodStatus": 0,
           //             "offcialAwardTime": 1396177500000
           //         }
           //     },
           //     "retcode": 200,
           //     "retdesc": "操作成功"
           // };
           // var endTime = data["object"]["availablePeriod"]["endTime"];
           // var serverTime = data["object"]["systemTime"];
           // var periodNo = data["object"]["availablePeriod"]["periodNo"];
           // var openingAwardPeriodNo = data["object"]["openingAwardPeriodNo"];
           // if (data.retcode == 200){
           //     // 更新顶部
           //     events.trigger({
           //         type:"updateTop",
           //         $up_top:e.$up_top,
           //         up_top_tpl:e.up_top_tpl,
           //         data:data
           //     });

           //     // 顶部重新倒计时
           //     events.trigger({
           //         type:"timer",
           //         timer:e.timer,
           //         endTime:endTime,
           //         serverTime:serverTime,
           //         $el:$("#ball_timer")
           //     });

           //     // 更新期次
           //     e.$periodNo.val(periodNo);

           //     // 期次结束提示
           //     art.show(function(){
           //         this.$prev_period.html(e.periodNo);
           //         this.$period.html(periodNo);
           //     });

           //     // 更新奖池 + 更新开奖公告
           //     events.trigger({
           //         type:"getAward",
           //         gameType : e.gameType,
           //         gameId : e.gameId,
           //         periodNo : e.periodNo,
           //         $up_pool : e.$up_pool,
           //         $up_award : e.$up_award,
           //         up_pool_tpl : e.up_pool_tpl,
           //         up_award_tpl : e.up_award_tpl,
           //         data:data // 不用请求接口立刻更新
           //     });
           // }
        });
    });

    // 更新今日开奖
    events.on("updateAwardList", function (e) {
        // e.award_list_hash[(e.data["object"].awardList[0].periodNo.slice(-2) - 1)].html(e.up_award_list_tpl(e.data))
        e.award_list_hash[(e.data["object"].awardList[0].periodNo.slice(-2))].html(e.up_award_list_tpl(e.data))
    })
    var gameTypeArr = ["", "common", "highfrequency", "tradfootball", "competition", "singlerace"];
    var time_tpl = _.template('<%var d=time[0]%><%var h=time[1]%><%var m=time[2]%><%var ss=time[3]%><%if(time[0]>0){%><b><%=d%></b>天<%}%><%if(time[0]>0||time[1]>0){%><b><%=+h<10?"0"+h:h%></b>小时<%}%><%if(time[0]>0||time[1]>0||time[2]>0){%><b><%=+m<10?"0"+m:m%></b>分<%}%><b><%=+ss<10?"0"+ss:ss%></b>秒')
    var timer = new Timer({
        range: [86400000, 3600000, 60000, 1000],
        endTime: $endTime.val(), // +new Date + 5000
        el: "#ball_timer",
        serverTime: $serverTime.val(), //  +new Date
        render: function (time) {
            this.$el.html(time_tpl({time: time}))
        },
        onTimerEnd: function () {
            events.trigger({
                type: "update",
                gameType: gameTypeArr[+$gameType.val()],
                gameId: $gameId.val(),
                periodNo: $periodNo.val(),
                $periodNo: $periodNo,
                $up_top: $up_top,
                $up_pool: $up_pool,
                $up_award: $up_award,
                up_top_tpl: up_top_tpl,
                up_pool_tpl: up_pool_tpl,
                up_award_tpl: up_award_tpl,
                timer: timer
            });
            events.trigger({
                type: "clearMissing",
                $hz_list: $hz_list,
                $3thtx_list: $3thtx_list,
                $3thdx_list: $3thdx_list,
                $3bt_rx_list: $3bt_rx_list,
                $3bt_dt_list: $3bt_dt_list,
                $3lhtx_list: $3lhtx_list,
                $2thfx_list: $2thfx_list,
                $2thdx_list: $2thdx_list,
                $2bt_rx_list: $2bt_rx_list,
                $2bt_dt_list: $2bt_dt_list
            })
        }
    });
    return function () {
        // 页面载入 开奖中更新页面
        if (openingAwardPeriodNo_v) {
            events.trigger({
                type: "update",
                gameType: gameTypeArr[+$gameType.val()],
                gameId: $gameId.val(),
                periodNo: openingAwardPeriodNo_v,
                $periodNo: $periodNo,
                $up_top: $up_top,
                $up_pool: $up_pool,
                $up_award: $up_award,
                up_top_tpl: up_top_tpl,
                up_pool_tpl: up_pool_tpl,
                up_award_tpl: up_award_tpl,
                timer: timer
            });
        }
        timer.sync(function () {
            timer.start();
        })
    }
})