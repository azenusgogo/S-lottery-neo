define([
    "lib/timer",
    "lib/events",
    "update/art",
    "lib/alert"
], function (Timer, events, art, dialogAlert) {
    var Alert = dialogAlert("提示");
    // 切换开奖信息
    events.on("jjcContToggle", function (e) {
        e.$jjcInfoArea.hide();
        e.$jjcInfoArea.eq(e.index).show()
    })

    // 切换赔率
    events.on("oddsListToggle", function (e) {
        e.$odds_list.find(".peilv").hide();
        e.$odds_list.find(".peilv:nth-child(" + (e.index + 1) + ")").show()
    })

    var time_tpl = _.template('<%var d=time[0]%><%var h=time[1]%><%var m=time[2]%><%var ss=time[3]%><%if(time[0]>0){%><b><%=d%></b>天<%}%><%if(time[0]>0||time[1]>0){%><b><%=+h<10?"0"+h:h%></b>小时<%}%><%if(time[0]>0||time[1]>0||time[2]>0){%><b><%=+m<10?"0"+m:m%></b>分<%}%><b><%=+ss<10?"0"+ss:ss%></b>秒')
    var timer = new Timer({
        range: [86400000, 3600000, 60000, 1000],
        el: "#ball_timer",
        render: function (time) {
            this.$el.html(time_tpl({time: time}))
        }
    });

    return function () {
        var gameStart = $("#gameStart").val();
        if (gameStart == 0) {
            Alert.show(function () {
                this.$(".txt-center").html("本期尚未开售，暂不接受投注，请耐心等待。");
            })
        }


        var $gameType = $("#gameType");
        var $gameId = $("#gameId");
        var $periodNo = $("#periodNo");
        var $endTime = $("#endTime");
        var $serverTime = $("#serverTime");

        var $jjcCont = $("#jjc_cont");
        var $jjcInfoArea = $jjcCont.find(".jjc-info-area");

        var $odds_toggle = $("#odds_toggle");
        var $odds_memu = $("#odds_toggle .sel");
        var $odds_list = $(".cp-area");

        var $periodNextNo = $("#periodNextNo");


        $jjcCont.on("change", ".h3r", function () {
            events.trigger({
                type: "jjcContToggle",
                $jjcInfoArea: $jjcInfoArea,
                index: $(this).val().split("_")[0]
            })
        });

        /*$odds_toggle.hover(function () {
            $odds_memu.show()
        }, function () {
            $odds_memu.hide()
        }); by zuonan 暂时去掉平均欧赔的下拉menu*/  

        $odds_memu.on("click", "a", function () {
            events.trigger({
                type: "oddsListToggle",
                index: $(this).index(),
                $odds_list: $odds_list
            });
            $odds_memu.hide();
            return false
        });

        timer.update({
            endTime: $endTime.val(), // +new Date + 500000
            serverTime: $serverTime.val(), // +new Date
            onTimerEnd: function () {
                // 期次结束提示
                art.show(function () {
                    this.url = true; // 刷新
                    this.$prev_period.html($periodNo.val());
                    this.$period.html($periodNextNo.val());
                });
            }
        })

        timer.sync(function () {
            timer.start();
        })
    }
})