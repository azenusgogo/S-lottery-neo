/**
 * Created by caojungang on 2014/4/10.
 */
define([
    "lib/islider",
    "lib/tab",
    "lib/timer",
    "lib/events",
    "text!index/quick_common.tpl",
    "text!index/quick_highfrequency.tpl",
    "text!index/highfrequency_item.tpl",
    "login/index_login",
    "ad/index_reg_ad",
    "lib/jquery.cookie",
    "lib/unslider" // 无return
], function (Islider, Tab, Timer, events, quick_common_tpl, quick_highfrequency_tpl, highfrequency_item_tpl, index_login, index_reg_ad) {

    return function () {
        // 广告

        if (!$.cookie('adshow')){
            var adImg = new Image;
            adImg.onload = function(){
                adImg.onload = null;
                $.cookie('adshow', 1, { expires: 365, path: '/' });
                index_reg_ad.show();
                index_reg_ad.$el.addClass('index_reg_ad_show')
            };
            adImg.src = "http://img01.caipiao.sogoucdn.com/img/pic/index_ad1.png?v=20140325"; // 预加载
        }
        
        // 登录
        index_login();
        // focus
        $('.banner').unslider({
            dots: true
        });
        // 中奖播报
        new Islider({
            el: "#news_slider",
            main: "#news_slider ul",
            step: 12
        });

        // 开奖提醒
        new Islider({
            el: "#kj-tip",
            main: "#kj-tip ul",
            delay: 4000,
            speed: 300,
            step: 1,
            onload: function () {
                this.$btn = this.$el.find(".tou-btn");
            },
            complate: function (url) {
                url && this.$btn.attr("href", url)
            }
        });

        // 右侧登录下侧tab
        new Tab({
            $t: $("#tab_dis_t_1 span"),
            $c: $("#tab_dis_c_1,#tab_dis_c_2,#tab_dis_c_3"),
            tClassName: "cur",
            events: "mouseover"
        });

        new Tab({
            $t: $("#tab_dis_c_1 .tab2 span"),
            $c: $("#tab_dis_c_1 ul"),
            tClassName: "cur",
            events: "mouseover"
        });

        new Tab({
            $t: $("#tab_dis_c_2 .tab2 span"),
            $c: $("#tab_dis_c_2 p"),
            tClassName: "cur",
            events: "mouseover"
        });

        new Tab({
            $t: $("#tab_dis_c_3 .tab2 span"),
            $c: $("#tab_dis_c_3 ul"),
            tClassName: "cur",
            events: "mouseover"
        });


        // 体彩tab
        new Tab({
            $t: $("#ftab a"),
            tClassName: "cur"
        });
        // 同步服务器时间
        var serverTime = $("#serverTime").val();
        serverTime || (serverTime = +new Date);

        // 倒计时template
        var time_tpl = _.template(
                '<%var d=time[0]%>' +
                '<%var h=time[1]%>' +
                '<%var m=time[2]%>' +
                '<%var ss=time[3]%>' +
                '<%if(time[0]>0){%>' +
                '<%=d%>天' +
                '<%}%>' +
                '<%if(time[0]>0||time[1]>0){%>' +
                '<%=+h<10?"0"+h:h%>小时<%}%>' +
                '<%if(time[0]>0||time[1]>0||time[2]>0){%>' +
                '<%=+m<10?"0"+m:m%>分<%}%>' +
                '<%=+ss<10?"0"+ss:ss%>秒'
        );

        // 预编译模版
        var quick_common = _.template(quick_common_tpl);
        var quick_highfrequency = _.template(quick_highfrequency_tpl);
        var highfrequency_item = _.template(highfrequency_item_tpl);

        // 随机
        events.on("random", function (e) {
            var redArr, blueArr, arr, animate_range;
            if (e.gameId == "ssq") {
                redArr = _.sortBy(_.sample(_.range(1, 34), 6));
                blueArr = _.sample(_.range(1, 17), 1);
                arr = redArr.concat(blueArr);
                animate_range = [1, 33];
            } else if (e.gameId == "dlt") {
                redArr = _.sortBy(_.sample(_.range(1, 36), 5));
                blueArr = _.sortBy(_.sample(_.range(1, 13), 2));
                arr = redArr.concat(blueArr);
                animate_range = [1, 35];
            } else if (e.gameId == "qxc") {
                arr = [];
                for (var i = 7; i--;) {
                    arr.push(_.random(0, 9))
                }
                animate_range = [0, 9];
            } else if (e.gameId == "qlc") {
                arr = _.sortBy(_.sample(_.range(1, 31), 7));
                animate_range = [1, 30];
            }

            if (arr) {
                for (var i = 30; i--;) {
                    (function (i) {
                        setTimeout(function () {
                            _.each(e.$el, function (el, j) {
                                if (i == 29) {
                                    var v = +arr[j];
                                    el.value = e.gameId == "qxc" ? v : v < 10 ? ("0" + v) : v
                                } else {
                                    var v = _.random.apply(null, animate_range)
                                    el.value = e.gameId == "qxc" ? v : v < 10 ? "0" + v : v
                                }
                            })
                        }, 10 + i * 8)
                    })(i)
                }
            }
        });

        // 随机快3和值
        events.on("randomHz", function (e) {
            var arr1 = _.random(1, 6);
            var arr2 = _.random(1, 6);
            var arr3 = _.random(1, 6);
            var count = arr1 + arr2 + arr3;
            var hash = {"3": 240, "18": 240, "4": 80, "17": 80, "5": 40, "16": 40, "6": 25, "15": 25, "7": 16, "14": 16, "8": 12, "13": 12, "9": 10, "12": 10, "10": 9, "11": 9};
            var sum = hash[count];
            e.$el.html(highfrequency_item({
                arr: [arr1, arr2, arr3],
                count: count,
                sum: sum
            }));
            e.$el.find("input").val("HZ_" + count);
        })

        // 更新数字彩
        events.on("updateUI", function (e) {
            var $parent = e.$parent;
            $.post("/ajax/index/" + e.gameId + "/refresh.html", function (data) {
                if (data.retcode != 200) return;
                data = data["object"];
                var tpl = e.gameType == "common" ? quick_common : quick_highfrequency;
                $parent.html(tpl(data));

                // 随机
                var type = e.gameType == "common" ? "random" : "randomHz";
                var el_selector = e.gameType == "common" ? ".ball-num input" : ".k3ball";
                events.trigger({
                    type: type,
                    gameId: e.gameId,
                    //gameType:e.gameType,
                    $el: $parent.find(el_selector)
                });

                // 重新倒计时
                setTimeout(function () {
                    events.trigger({
                        type: "quickTimer",
                        Timer: Timer,
                        endTime: data.endTime,
                        gameId: e.gameId,
                        serverTime: data.serverTime,
                        $el: $parent.find(".time em"),
                        $parent: $parent,
                        gameType: e.gameType
                    })
                }, 500)

            }).error(function () {
                // var data = {
                //     "retcode": 200,
                //     "retdesc": "操作成功",
                //     "object": {
                //         "bonusPool": null,
                //         "available": true,
                //         "todayOpen": false,
                //         "endTime": +new Date + 800000,
                //         "periodNo": "2014045",
                //         "gameId": e.gameId,
                //         "gameCn": "双色球",
                //         "offcialAwardTime": 1398346200000
                //     }
                // }

                // var data = {
                //     "retcode": 200,
                //     "retdesc": "操作成功",
                //     "object": {
                //         "interval": 10,
                //         "hotPlayMethod": "和值",
                //         "available": true,
                //         "endTime": +new Date + 800000,
                //         "periodNo": "140423041",
                //         "gameId": e.gameId,
                //         "gameCn": "新快3",
                //         "offcialAwardTime": 1398241080000
                //     }
                // }


                // if (data.retcode != 200) return;
                // data = data["object"];
                // var tpl = e.gameType == "common" ? quick_common : quick_highfrequency;
                // $parent.html(tpl(data));

                // // 随机
                // var type = e.gameType == "common" ? "random" : "randomHz";
                // var el_selector = e.gameType == "common" ? ".ball-num input" : ".k3ball";
                // events.trigger({
                //     type: type,
                //     gameId: e.gameId,
                //     //gameType:e.gameType,
                //     $el: $parent.find(el_selector)
                // });

                // // 重新倒计时
                // events.trigger({
                //     type: "quickTimer",
                //     Timer: Timer,
                //     endTime: data.endTime,
                //     gameId: e.gameId,
                //     serverTime: data.serverTime,
                //     $el: $parent.find(".time em"),
                //     $parent: $parent,
                //     gameType: e.gameType
                // })
            })
        });

        // 倒计时
        events.on("quickTimer", function (e) {
            var timer = new e.Timer({
                range: [86400000, 3600000, 60000, 1000],
                endTime: e.endTime, //  +new Date + 5000
                el: e.$el,
                serverTime: e.serverTime,
                render: function (time) {
                    this.$el.html(time_tpl({time: time}))
                },
                onTimerEnd: function () {
                    window.setTimeout(function () {
                        events.trigger({
                            type: "updateUI",
                            gameId: e.gameId,
                            $parent: e.$parent,
                            gameType: e.gameType
                        })
                    }, 500)
                }
            });

            timer.sync(function () {
                timer.start();
            })
        });

        // 数字彩快捷投注tab
        new Tab({
            $t: $("#quick_bet_tab .cptab a"),
            $c: $("#quick_bet_tab .cpret"),
            delay: 200,
            tClassName: "cur",
            events: "mouseover",
            onload: function () {
                var $c = this.$c;
                this.$(".time em").each(function (index) {
                    var $this = $(this);
                    var gameId = $this.attr("data-gameId");
                    var $parent = $c.eq(index);
                    var $form = $parent.find("form");
                    var $ball = $form.find("input");
                    events.trigger({
                        type: "quickTimer",
                        Timer: Timer,
                        endTime: $this.attr("data-endTime"),
                        gameId: gameId,
                        serverTime: serverTime,
                        $el: $this,
                        $parent: $parent,
                        // "common", "highfrequency", "tradfootball", "competition", "singlerace"
                        gameType: "common"
                    });
                    events.trigger({
                        type: "random",
                        gameId: gameId,
                        $el: $parent.find(".ball-num input")
                    });
                    // 换一换
                    $parent.on("click", ".change", function () {
                        events.trigger({
                            type: "random",
                            gameId: gameId,
                            $el: $parent.find(".ball-num input")
                        })
                        return false
                    });

                    // 表单验证
                    $parent.on("keyup keydown", "input", function (e) {
                        var keyCode = e.keyCode;
                        if (!((keyCode <= 57 && keyCode >= 48) || (keyCode <= 105 && keyCode >= 96) || keyCode == 39 || keyCode == 37 || keyCode == 8 || keyCode == 46 || keyCode == 229)) {
                            e.preventDefault();
                            return
                        }
                        ;
                        var $this = $(this);
                        var max = $this.attr("data-max");
                        var min = gameId == "qxc" ? 0 : 1;
                        var v = +$this.val();
                        if (v > max || v < min) {
                            $this.val("")
                        }
                    });

                    $parent.on("blur", "input", function () {
                        var $this = $(this);
                        var v = $this.val();
                        if (v.length > 0) {
                            if (gameId == "qxc") {
                                v = +v;
                            } else {
                                v = v < 10 ? ("0" + +v) : +v
                            }
                        }
                        $this.val(v)
                    });

                    // 表单提交
                    $parent.on("click", ".home-tou-btn", function () {
                        var red = [];
                        var blue = [];
                        $parent.find(".ball-num input").each(function (i) {
                            var v = $(this).val();
                            if (v.length > 0) {
                                if (gameId == "ssq") {
                                    i < 6 && red.push(v) || blue.push(v);
                                } else if (gameId == "dlt") {
                                    i < 5 && red.push(v) || blue.push(v);
                                } else {
                                    red.push(v)
                                }
                            }
                        });
                        var atrText = "";
                        if (gameId == "ssq") {
                            if (red.length < 6) {
                                atrText += "红球号码必须是6位\n"
                            }
                            if (blue.length == 0) {
                                atrText += "篮球号码不能为空\n"
                            }
                        } else if (gameId == "dlt") {
                            if (red.length < 5) {
                                atrText += "红球号码必须是5位\n"
                            }
                            if (blue.length < 2) {
                                atrText += "篮球号码必须是2位\n"
                            }
                        } else if (gameId == "qxc") {
                            if (red.length < 7) {
                                atrText += "每位至少选择一个号码\n"
                            }
                        } else if (gameId == "qlc") {
                            if (red.length < 7) {
                                atrText += "必须选7个号码\n"
                            }
                        }
                        var arr = [];
                        if (atrText) {
                            alert(atrText)
                        } else {
                            if (gameId == "ssq" || gameId == "dlt") {
                                $ball.val(red.join(" ") + ":" + blue.join(" ") + "#1")
                            } else { // qxc qlc
                                $ball.val(red.join(" ") + "#1")
                            }
                            $form.trigger('submit');
                        }
                        return false
                    })
                });
            },
            fn: function (i) {
                if (i == this.i) return;
                var $el = this.$c.eq(i);
                events.trigger({
                    type: "random",
                    gameId: $el.find(".time em").attr("data-gameId"),
                    $el: $el.find(".ball-num input")
                })
            }
        });

        // 高频彩快捷投注tab
        new Tab({
            $t: $("#quick_bet_tab2 .cptab a"),
            $c: $("#quick_bet_tab2 .cpret"),
            tClassName: "cur",
            events: "mouseover",
            onload: function () {
                var $c = this.$c;
                this.$(".time em").each(function (index) {
                    var $this = $(this);
                    var gameId = $this.attr("data-gameId");
                    var $parent = $c.eq(index);
                    var $form = $parent.find("form");
                    var $ball = $form.find("input");
                    events.trigger({
                        type: "quickTimer",
                        Timer: Timer,
                        endTime: $this.attr("data-endTime"),
                        gameId: gameId,
                        serverTime: serverTime,
                        $el: $this,
                        $parent: $parent,
                        // "common", "highfrequency", "tradfootball", "competition", "singlerace"
                        gameType: "highfrequency"
                    });

                    events.trigger({
                        type: "randomHz",
                        gameId: gameId,
                        //gameType:e.gameType,
                        $el: $parent.find(".k3ball")
                    });

                    // 换一换
                    $parent.on("click", ".change", function () {
                        events.trigger({
                            type: "randomHz",
                            gameId: gameId,
                            //gameType:e.gameType,
                            $el: $parent.find(".k3ball")
                        });
                        return false
                    });
                    // 表单提交
                    $parent.on("click", ".home-tou-btn", function () {
                        var v = $parent.find(".k3ball input").val();
                        $ball.val(v + "#1");
                        $form.trigger('submit');
                        return false
                    })

                });
            },
            fn: function (i) {
                // console.log(i)
                if (i == this.i) return;
                // var $el = this.$c.eq(i);
            }
        });
    }


});